package com.multipolar.sumsel.kasda.kasdagateway.controller;

import com.multipolar.sumsel.kasda.kasdagateway.entity.HostnameRoutes;
import com.multipolar.sumsel.kasda.kasdagateway.model.HttpRequestBuilder;
import com.multipolar.sumsel.kasda.kasdagateway.rest.client.HttpRequestHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/proxy/sipkd")
public class ProxySIPKDBillingController {

    @Value("${proxy.sipkd.name}")
    private String serviceName;
    private static final String url = "/api/billing";
    @Autowired
    private HttpRequestHandlerService service;

    @GetMapping(url)
    public ResponseEntity<?> getRequest(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.GET)
                .params(params)
                .path(url).build();
        Optional<HostnameRoutes> hostname = service.findByServiceId(this.serviceName);
        log.info("serviceId {} is present: {}", serviceName, hostname.isPresent());
        return this.service.call(hostname, request);
    }

    @PostMapping(url)
    public ResponseEntity<?> postController(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.POST)
                .params(params)
                .path(url).build();
        Optional<HostnameRoutes> hostname = service.findByServiceId(this.serviceName);
        log.info("serviceId {} is present: {}", serviceName, hostname.isPresent());
        return this.service.call(hostname, request);
    }

    @PutMapping(url)
    public ResponseEntity<?> putController(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.PUT)
                .params(params)
                .path(url).build();
        Optional<HostnameRoutes> hostname = service.findByServiceId(this.serviceName);
        log.info("serviceId {} is present: {}", serviceName, hostname.isPresent());
        return this.service.call(hostname, request);
    }

    @DeleteMapping(url)
    public ResponseEntity<?> deleteMapping(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.DELETE)
                .params(params)
                .path("/api/npwp").build();
        Optional<HostnameRoutes> hostname = service.findByServiceId(this.serviceName);
        log.info("serviceId {} is present: {}", serviceName, hostname.isPresent());
        return this.service.call(hostname, request);
    }
}
