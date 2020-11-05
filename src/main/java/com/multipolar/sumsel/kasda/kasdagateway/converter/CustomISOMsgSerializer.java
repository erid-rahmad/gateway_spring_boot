package com.multipolar.sumsel.kasda.kasdagateway.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@JsonComponent
@Component
public class CustomISOMsgSerializer extends JsonSerializer<ISOMsg> {

    @Autowired
    private MessageConverterFactory converterFactory;

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(ISOMsg msg, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        String feature = FeatureContextHolder.getContext().getFeatureName();
        MessageConverterHandler converter = converterFactory.get(feature);
        Map<String, Object> map = converter.doConvertToJSon(msg, true);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            log.info("key: {}, value: {}", entry.getKey(), entry.getValue());
        }

        //Map<String, Object> stringObjectMap = FeatureContextHolder.getContext().getParameters();

        // iteratively put map on as json object
        jsonGenerator.writeStartObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String)
                jsonGenerator.writeStringField(entry.getKey(), entry.getValue().toString());
            else if (entry.getValue() instanceof Map) {
                writeIterativeMap(jsonGenerator, entry.getKey(), (Map<String, Object>) entry.getValue());
            } else if (entry.getValue() instanceof List<?>) {
                jsonGenerator.writeFieldName(entry.getKey());
                jsonGenerator.writeStartArray();
                List<?> list = (List<?>) entry.getValue();
                for (Object obj : list) {
                    jsonGenerator.writeObject(obj);
                }
                jsonGenerator.writeEndArray();
            } else {
                if (entry.getValue() != null)
                    jsonGenerator.writeStringField(entry.getKey(), entry.getValue().toString());
                else
                    jsonGenerator.writeStringField(entry.getKey(), null);
            }
        }

        jsonGenerator.writeEndObject();
    }

    private void writeIterativeMap(JsonGenerator generator, String key, Map<String, Object> innerMap) throws IOException {
        generator.writeFieldName(key);
        generator.writeStartObject();
        for (Map.Entry<String, Object> entry : innerMap.entrySet()) {
            if (entry.getValue() instanceof String)
                generator.writeStringField(entry.getKey(), entry.getValue().toString());
            else {
                generator.writeStartObject();
            }
        }
        generator.writeEndObject();
    }
}
