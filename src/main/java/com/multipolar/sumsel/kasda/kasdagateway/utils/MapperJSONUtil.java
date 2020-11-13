package com.multipolar.sumsel.kasda.kasdagateway.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
public class MapperJSONUtil {
    public static String prettyLog (Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        }catch (IOException e){
            log.info("Logging error");
            return null;
        }
    }
}
