package com.multipolar.sumsel.kasda.kasdagateway.rest.client;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RequestLoggingInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);

        log.info("request method: {}, request URI: {}, request headers: {}, request body: {}, response status code: {}, response headers: {}, response body: {}",
                request.getMethod(),
                request.getURI(),
                request.getHeaders(),
                new String(body, StandardCharsets.UTF_8),
                response.getStatusCode(),
                response.getHeaders(),
                new String(ByteStreams.toByteArray(response.getBody()), StandardCharsets.UTF_8)
        );

        return response;
    }
}
