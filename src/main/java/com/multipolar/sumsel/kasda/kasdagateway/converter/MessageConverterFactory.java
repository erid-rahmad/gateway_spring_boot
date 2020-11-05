package com.multipolar.sumsel.kasda.kasdagateway.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MessageConverterFactory {
    private final List<MessageConverterHandler> converterHandlerServices;

    @Autowired
    public MessageConverterFactory(List<MessageConverterHandler> converterHandlerServices) {
        this.converterHandlerServices = converterHandlerServices;
    }

    public MessageConverterHandler get(String s) {
        MessageConverterHandler defaultHandler = null;
        for (MessageConverterHandler handler : converterHandlerServices) {
            if (handler.supports(s))
                return handler;

            if (handler.getClass().equals(DefaultConverterHandler.class))
                defaultHandler = handler;
        }

        if (defaultHandler == null)
            log.error("No handler found for this type feature, application will not run properly. Feature: {}", s);

        // here, no hander support for the requested feature, return default implementation
        return defaultHandler;
    }

    public MessageConverterHandler get(String featureName, boolean isReverse) {
        MessageConverterHandler defaultHandler = null;
        for (MessageConverterHandler handler : converterHandlerServices) {
//            if (handler.supports(featureName))
//                return handler;

            if (isReverse && handler.getClass().equals(ReverseDefaultConverterHandler.class))
                defaultHandler = handler;

            if (handler.getClass().equals(DefaultConverterHandler.class))
                defaultHandler = handler;
        }

        log.info("default converter handler: {}", defaultHandler.getClass().toString());

        if (defaultHandler == null)
            log.error("No handler found for this type feature, application will not run properly. Feature: {}", featureName);

        // here, no hander support for the requested feature, return default implementation
        return defaultHandler;
    }
}
