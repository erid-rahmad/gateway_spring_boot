package com.multipolar.sumsel.kasda.kasdagateway.servlet.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class FeatureContextFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String feature = getFeatureFromRequest(
                httpServletRequest.getRequestURI(),
                httpServletRequest.getParameter("transactionType"));

        if (feature != null) {
            FeatureContextHolder.getContext().setFeatureName(feature);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Deprecated
    private String getFeatureFromRequest(String requestUri) {
        String result = StringUtils.substringBetween(requestUri, "/", "?");
        if (result == null)
            result = StringUtils.substringBetween(requestUri, "/", "/");

        if (result == null)
            result = StringUtils.substringAfter(requestUri, "/");

        return result;
    }

    private String getFeatureFromRequest(String requestUri, String transactionType) {
        if (StringUtils.containsIgnoreCase(requestUri, "/api/v1.0/request/transaction")) {
            return transactionType;
        }

        return null;
    }
}

//public class FeatureContextFilter { }
