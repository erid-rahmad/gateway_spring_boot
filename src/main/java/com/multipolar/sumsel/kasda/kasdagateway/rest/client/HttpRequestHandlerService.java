package com.multipolar.sumsel.kasda.kasdagateway.rest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.multipolar.sumsel.kasda.kasdagateway.model.HostnameRoutes;
import com.multipolar.sumsel.kasda.kasdagateway.model.HttpRequestBuilder;
import com.multipolar.sumsel.kasda.kasdagateway.repository.HostnameRoutesRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.ClientHttpResponseStatusCodeException;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class HttpRequestHandlerService {

    @Autowired
    private RestTemplate template;
    @Autowired
    private HostnameRoutesRepository repository;
    @Autowired
    private ConfigurableEnvironment env;

    public Optional<HostnameRoutes> findByServiceId(String serviceId) {
        String activeProfile = Arrays.stream(env.getActiveProfiles()).findFirst().orElse("default");
        log.debug("serviceId: {}, env: {}", serviceId, activeProfile);
        return repository.findByServiceIdAndEnvironment(serviceId, activeProfile);
    }

    public Optional<HostnameRoutes> findByKodeCabangAndKodeWilayah(String serviceId, String kodeCabang, String kodeWilayah) {
        String activeProfile = Arrays.stream(env.getActiveProfiles()).findFirst().orElse("default");
        log.debug("serviceId: {}, kodeCabang: {}, kodeWilayah: {}, activeProfile: {}", serviceId, kodeCabang, kodeWilayah);
        return repository.findByServiceIdAndKodeCabangAndKodeWilayahAndEnvironment(serviceId, kodeCabang, kodeWilayah, activeProfile);
    }

    @HystrixCommand(
            fallbackMethod = "callFallbackResponse",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
            },
            ignoreExceptions = {
                    HttpStatusCodeException.class,
                    JsonMappingException.class,
                    ClientHttpResponseStatusCodeException.class,
                    NullPointerException.class,
                    JsonProcessingException.class
            }
    )
    public ResponseEntity<?> call(Optional<HostnameRoutes> hostnameOptional, HttpRequestBuilder request) {
        HttpHeaders headers = new HttpHeaders();
        if (!request.getHeaders().isEmpty())
            request.getHeaders().forEach(headers::set);

        if (!hostnameOptional.isPresent()) {
            log.warn("hostname notFound in table!");
            return ResponseEntity.notFound().build();
        }

        HostnameRoutes hostname = hostnameOptional.get();

        HttpEntity<Map<String, Object>> entity = null;
        if (request.getBody() != null)
            entity = new HttpEntity<>(request.getBody(), headers);
        else
            entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriComponent = UriComponentsBuilder.fromHttpUrl(
                String.format("%s://%s:%s%s%s",
                        hostname.getProtocol(), hostname.getHost(), hostname.getPort(), hostname.getContextPath(), request.getPath())
        );

        if (request.getParams() != null && !request.getParams().isEmpty()) {
            for (Map.Entry<String, String> entry : request.getParams().entrySet()) {
                uriComponent.queryParam(entry.getKey(), entry.getValue());
            }
        }

        log.info("{ requestUrl: {}, requestBody: {}, requestHeaders: {}}",
                uriComponent.toUriString(), entity.getBody(), entity.getHeaders());

        ResponseEntity<String> exchange = null;
        try {
            exchange = this.template.exchange(uriComponent.toUriString(), request.getMethod(), entity, String.class);
        } catch (HttpClientErrorException hcee) {
            log.error("error message: {} => {}", uriComponent.toUriString(), hcee.getLocalizedMessage());
            HttpStatus statusCode = hcee.getStatusCode();
            switch (statusCode) {
                case NOT_FOUND:
                    exchange = ResponseEntity.notFound().build();
                    break;
                case FORBIDDEN:
                    exchange = new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    break;
                case BAD_REQUEST:
                    exchange = new ResponseEntity<>(hcee.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
                    break;
                case UNAUTHORIZED:
                    exchange = new ResponseEntity<>(hcee.getLocalizedMessage(), HttpStatus.UNAUTHORIZED);
                    break;
                case INTERNAL_SERVER_ERROR:
                    exchange = new ResponseEntity<>(hcee.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                    break;
                default:
                    new HystrixBadRequestException("Exception thrown hystrix call", hcee);
            }
        } catch (Exception ex) {
            exchange = new ResponseEntity(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            log.error("error exception: ", ex);
        }
        return exchange;
    }

    public ResponseEntity<?> callFallbackResponse(Optional<HostnameRoutes> hostnameOptional, HttpRequestBuilder request) {
        return new ResponseEntity<>(HttpStatus.GATEWAY_TIMEOUT);
    }


}
