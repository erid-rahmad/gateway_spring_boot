package com.multipolar.sumsel.kasda.kasdagateway.controller;

import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterFactory;
import com.multipolar.sumsel.kasda.kasdagateway.converter.MessageConverterHandler;
import com.multipolar.sumsel.kasda.kasdagateway.service.NetworkMessageGateway;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.ConnectException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/verificationCard")
public class VerificationAccountSourceController {

    @Autowired
    private NetworkMessageGateway gateway;

    @Autowired
    private MessageConverterFactory converterFactory;

    @PostMapping("/transaction")
    public ISOMsg defaultController(@RequestBody ISOMsg msg) throws ISOException, ConnectException {
        Map<String, Object> map = FeatureContextHolder.getContext().getParameters();
        String context = FeatureContextHolder.getContext().getFeatureName();

        ISOMsg response = gateway.sendToHost(msg, 60);

        MessageConverterHandler converter = converterFactory.get(context);

        if (response != null) {
            Map<String, Object> responseMap = converter.doConvertToJSon(response, true);
            map.putAll(responseMap);
        }

        return response;

    }
}
