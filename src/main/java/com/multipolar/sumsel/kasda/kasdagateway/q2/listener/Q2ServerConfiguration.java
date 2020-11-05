package com.multipolar.sumsel.kasda.kasdagateway.q2.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterFactory;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterHandler;
import com.multipolar.sumsel.kasda.kasdagateway.model.HostnameRoutes;
import com.multipolar.sumsel.kasda.kasdagateway.model.HttpRequestBuilder;
import com.multipolar.sumsel.kasda.kasdagateway.rest.client.HttpRequestHandlerService;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.*;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class Q2ServerConfiguration implements ISORequestListener {

    @Autowired
    private MessageConverterFactory converterFactory;

    @Autowired
    Q2ServerConfiguration isoServer;
    @Value("${q2.server-port}")
    private Integer q2Port;
    @Value("${proxy.kasda-api.name}")
    private String serviceName;
    @Autowired
    private HttpRequestHandlerService httpService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() throws Exception {
        ServerChannel channel = new ASCIIChannel(new ISO87APackager());
        ISOServer isoServer = new ISOServer(q2Port, channel, null);
        isoServer.addISORequestListener(this::process);
        new Thread(isoServer).start();
    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        MessageConverterHandler converter = null;
        HttpRequestBuilder dataRequest = null;
        try {
            log.info("request mti: {}, field3: {}", isoMsg.getMTI(), isoMsg.getString(3));
            ISOMsg requestMsg = (ISOMsg) isoMsg.clone();
            String pCode = requestMsg.getString(3);

            // DEVELOPER Notes: after received set value bit 39 = '00' when success or if failed = '01'
            requestMsg.set(39, "00");
            switch (pCode) {
                case "330054":
                    // FIXME: please check again service not available for now
                    dataRequest = HttpRequestBuilder.builder()
                            .params(new HashMap<>())
                            .headers(new HashMap<>())
                            .path("/api/SP2D")
                            .method(HttpMethod.GET)
                            .build();
                    break;
                case "330055":
                    // FIXME: please check again service not available for now
                    dataRequest = HttpRequestBuilder.builder()
                            .params(new HashMap<>())
                            .headers(new HashMap<>())
                            .path("/api/SPJ")
                            .method(HttpMethod.GET)
                            .build();
                    break;
                case "330005":
                    // FIXME: please check again service not available for now
                    dataRequest = HttpRequestBuilder.builder()
                            .params(new HashMap<>())
                            .headers(new HashMap<>())
                            .path("/api/SPJ")
                            .method(HttpMethod.GET)
                            .build();
                    break;
                case "330006":
                    // FIXME: please check again service not available for now
                    dataRequest = HttpRequestBuilder.builder()
                            .params(new HashMap<>())
                            .headers(new HashMap<>())
                            .path("/api/SPJ")
                            .method(HttpMethod.GET)
                            .build();
                    break;
                case "010054":
                    // FIXME: please check again service not available for now
                    dataRequest = HttpRequestBuilder.builder()
                            .params(new HashMap<>())
                            .headers(new HashMap<>())
                            .path("/api/SPJ")
                            .method(HttpMethod.GET)
                            .build();
                    break;
                case "010055":
                    // FIXME: please check again service not available for now
                    dataRequest = HttpRequestBuilder.builder()
                            .params(new HashMap<>())
                            .headers(new HashMap<>())
                            .path("/api/SPJ")
                            .method(HttpMethod.GET)
                            .build();
                    break;
            }

            FeatureContextHolder.getContext().setFeatureName(pCode);
            converter = converterFactory.get(pCode);
            Map<String, Object> jsonRequest = converter.doConvertToJSon(requestMsg, true);
            assert dataRequest != null;
            log.info("q2 server request -> {}", jsonRequest);
            String kodeCabang = (String) jsonRequest.getOrDefault("Kd_Cabang", null);
            String kodeWilayah = (String) jsonRequest.getOrDefault("Kd_Wilayah", null);

            // set body after convert to json
            dataRequest.setBody(jsonRequest);
            Optional<HostnameRoutes> hostname =
                    this.httpService.findByKodeCabangAndKodeWilayah(this.serviceName, kodeCabang, kodeWilayah);
            log.info("hostname is present {}", hostname.isPresent());
            ResponseEntity<?> responseEntity = this.httpService.call(hostname, dataRequest);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> jsonMap = new ObjectMapper().readValue(
                        responseEntity.getBody().toString(),
                        Map.class);
                ISOMsg isoResponseData = converter.doConvertToISO(jsonMap);
                isoSource.send(isoResponseData);
                return true;
            } else {
                return false;
            }
        } catch (ISOException e) {
            log.error("iso exception ==>", e);
            return false;
        } catch (JsonMappingException e) {
            log.error("json mapping exception ==>", e);
            return false;
        } catch (JsonProcessingException e) {
            log.error("json proccessing exception ==>", e);
            return false;
        } catch (IOException e) {
            log.error("ioe exception ==>", e);
            return false;
        }
    }
}
