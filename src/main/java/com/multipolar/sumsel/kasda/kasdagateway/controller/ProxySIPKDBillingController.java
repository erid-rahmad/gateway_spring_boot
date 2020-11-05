package com.multipolar.sumsel.kasda.kasdagateway.controller;

import com.multipolar.sumsel.kasda.kasdagateway.model.HttpRequestBuilder;
import com.multipolar.sumsel.kasda.kasdagateway.rest.client.HttpRequestHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/proxy/sipkd")
public class ProxySIPKDBillingController {

    @Value("${proxy.sipkd.name}")
    private String serviceName;

    private static final String url = "/api/billing";

    @Autowired
    private HttpRequestHandlerService service;

    @GetMapping(url)
    public ResponseEntity<?> getRequest(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.GET)
                .params(params)
                .path(url).build();
        return this.service.call(this.serviceName, request);
    }

    @PostMapping(url)
    public ResponseEntity<?> postController(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.POST)
                .params(params)
                .path(url).build();
        return this.service.call(this.serviceName, request);
    }

    @PutMapping(url)
    public ResponseEntity<?> putController(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.PUT)
                .params(params)
                .path(url).build();
        return this.service.call(this.serviceName, request);
    }

    @DeleteMapping(url)
    public ResponseEntity<?> deleteMapping(
            @RequestHeader(required = false) Map<String, String> headers,
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, String> params) {
        HttpRequestBuilder request = HttpRequestBuilder.builder()
                .body(body)
                .headers(headers)
                .method(HttpMethod.DELETE)
                .params(params)
                .path("/api/npwp").build();
        return this.service.call(this.serviceName, request);
    }
}
