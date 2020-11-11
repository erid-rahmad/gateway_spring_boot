package com.multipolar.sumsel.kasda.kasdagateway.controller;

import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterFactory;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterHandler;
import com.multipolar.sumsel.kasda.kasdagateway.service.NetworkMessageGateway;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.ConnectException;
import java.util.Map;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v1.0/request")
public class TransactionRequestController extends ResponseEntityExceptionHandler {

    @Value("${q2.default-timeout}")
    private Integer defaultTimeout;
    @Autowired
    private NetworkMessageGateway gateway;
    @Autowired
    private MessageConverterFactory converterFactory;

    @PostMapping("/transaction")
    public ResponseEntity<?> defaultController(
            @RequestBody ISOMsg msg,
            @RequestParam(value = "transactionType") String transactionType,
            @RequestParam(value = "terminalId") String terminalId) throws ISOException, ConnectException {

        String context = FeatureContextHolder.getContext().getFeatureName();
        log.warn("its context {}",context);
        msg.set(41, StringUtils.leftPad(terminalId, 8, "0"));
        ISOMsg response = gateway.sendToHost(msg, defaultTimeout);
        log.warn("its response {}",response);
        MessageConverterHandler converter = converterFactory.get(context, false);
        log.warn("its converter {}",converter);

        if (response != null) {
            Map<String, Object> responseMap = converter.doConvertToJSon(response, true);
            log.error("just try 9");
            return ok(responseMap);
        } else {
            return noContent().build();
        }

    }
}
