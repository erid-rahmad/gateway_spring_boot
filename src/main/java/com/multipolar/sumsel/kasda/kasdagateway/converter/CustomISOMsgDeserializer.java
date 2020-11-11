package com.multipolar.sumsel.kasda.kasdagateway.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@JsonComponent
@Component
public class CustomISOMsgDeserializer extends StdDeserializer<ISOMsg> {
    private static final long serialVersionUID = -5748411949844882516L;

    @Autowired
    private MessageConverterFactory converterFactory;

    public CustomISOMsgDeserializer() {
        this(ISOMsg.class);
    }

    public CustomISOMsgDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ISOMsg deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        String feature = FeatureContextHolder.getContext().getFeatureName();
        MessageConverterHandler converter = converterFactory.get(feature);

        ObjectCodec codec = parser.getCodec();
        Map<String, Object> map = codec.readValue(parser, Map.class);
        FeatureContextHolder.getContext().setParameters(map);

        ISOMsg msg = converter.doConvertToISO(map);

        return msg;

    }
}
