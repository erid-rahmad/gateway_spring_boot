package com.multipolar.sumsel.kasda.kasdagateway.model;

import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
public class HttpRequestBuilder {

    @NotNull
    private String path;
    @NotNull
    private HttpMethod method;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, Object> body = new HashMap<>();
    private Map<String, String> params = new HashMap<>();

}
