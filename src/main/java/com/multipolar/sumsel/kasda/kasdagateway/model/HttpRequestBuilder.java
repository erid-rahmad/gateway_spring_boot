package com.multipolar.sumsel.kasda.kasdagateway.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
public class HttpRequestBuilder {

    @NotNull
    private String path;
    @NotNull
    private HttpMethod method;
    private Map<String, String> headers;
    private Map<String, Object> body;
    private Map<String, String> params;

}
