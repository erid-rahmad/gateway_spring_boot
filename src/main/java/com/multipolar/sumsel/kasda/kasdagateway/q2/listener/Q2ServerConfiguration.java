package com.multipolar.sumsel.kasda.kasdagateway.q2.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterFactory;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterHandler;
import com.multipolar.sumsel.kasda.kasdagateway.entity.HostnameRoutes;
import com.multipolar.sumsel.kasda.kasdagateway.entity.Iso858eTemplateRequest;
import com.multipolar.sumsel.kasda.kasdagateway.model.HttpRequestBuilder;
import com.multipolar.sumsel.kasda.kasdagateway.rest.client.HttpRequestHandlerService;
import com.multipolar.sumsel.kasda.kasdagateway.service.Iso8583TemplateService;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Iso8583Utils;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.*;
import org.jpos.iso.channel.ASCIIChannel;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

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
    @Autowired
    private Iso8583TemplateService isoService;

    @EventListener(ContextRefreshedEvent.class)
    public void contextRefreshedEvent() throws Exception {
        ServerChannel channel = new ASCIIChannel(new ISO87APackager());
        ISOServer isoServer = new ISOServer(q2Port, channel, null);
        isoServer.addISORequestListener(this::process);
        new Thread(isoServer).start();
    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        MessageConverterHandler converterHandler = null;
        HttpRequestBuilder dataRequest = null;
        try {
            String mti = isoMsg.getMTI();
            String pCode = isoMsg.getString(3);
            log.info("request mti: {}, field3: {}", mti, pCode);
            ISOMsg requestMsg = (ISOMsg) isoMsg.clone();

            log.debug("iso incoming message: {}", Iso8583Utils.decode(requestMsg).orElse(null));
            FeatureContextHolder.getContext().setFeatureName(pCode);
            converterHandler = converterFactory.get(pCode, true);
            Map<String, Object> jsonRequest = converterHandler.doConvertToJSon(requestMsg, true);

            log.info("q2 server request -> {}", jsonRequest);
            String kodeCabang = (String) jsonRequest.getOrDefault("Kd_Cabang", null);
            String kodeWilayah = (String) jsonRequest.getOrDefault("Kd_Wilayah", null);

            Optional<HostnameRoutes> hostname =
                    this.httpService.findByKodeCabangAndKodeWilayah(this.serviceName, kodeCabang, kodeWilayah);
            log.info("hostname is present {}", hostname.isPresent());

            Optional<Iso858eTemplateRequest> isoTemplateOptional = this.isoService.findByServiceNameAndHttpMethodAndMtiAndPCode(this.serviceName, mti, pCode);

            log.info("iso template for mti: {}, pCode: {}, present: {}", mti, pCode, isoTemplateOptional.isPresent());
            if (!isoTemplateOptional.isPresent()) {
                return false;
            }

            Iso858eTemplateRequest isoTemplate = isoTemplateOptional.get();
            dataRequest = HttpRequestBuilder.builder()
                    .method(isoTemplate.getHttpMethod())
                    .path(isoTemplate.getPath())
                    .headers(new HashMap<>())
                    .params(new HashMap<>())
                    .body(jsonRequest)
                    .build();
            ResponseEntity<?> responseEntity = this.httpService.call(hostname, dataRequest);
            if (!isoTemplate.getIsReturn() && responseEntity.getStatusCode() == HttpStatus.OK) {
                return false;
            }

            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                return false;
            }

            List<Map<String, Object>> sendResponseList = new ArrayList<>();
            switch (isoTemplate.getResponseType()) {
                case LIST:
                    sendResponseList = new ObjectMapper().readValue(
                            responseEntity.getBody().toString(), List.class);
                    break;
                default:
                    Map<String, Object> jsonMap = new ObjectMapper().readValue(
                            responseEntity.getBody().toString(),
                            Map.class);
                    sendResponseList.add(jsonMap);
            }

            for (Map<String, Object> data : sendResponseList) {
                data.put("Kd_Cabang", kodeCabang);
                data.put("Kd_Wilayah", kodeWilayah);
                ISOMsg responseIso = converterHandler.doConvertToISO(data, (ISOMsg) isoMsg.clone());
//                ISOMsg responseIso = requestMsg;
                responseIso.setResponseMTI();
                responseIso.set(new ISOField(39, "00"));
//                responseIso.set(new ISOField(7, requestMsg.getString(7)));
                Optional<String> decode = Iso8583Utils.decode(responseIso);
                log.debug("iso outgoing message: {}", decode.orElse(null));
                if (isoSource.isConnected()) {
                    isoSource.send(responseIso);
                }
            }
            return true;
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
