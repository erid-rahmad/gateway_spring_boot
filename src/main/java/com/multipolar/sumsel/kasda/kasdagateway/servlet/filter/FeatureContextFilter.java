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
        String feature = getFeatureFromRequest(httpServletRequest.getRequestURI());

        if (feature != null) {
            FeatureContextHolder.getContext().setFeatureName(feature);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    private String getFeatureFromRequest(String requestUri) {
        String result = StringUtils.substringBetween(requestUri, "/", "?");
        if (result == null)
            result = StringUtils.substringBetween(requestUri, "/", "/");

        if (result == null)
            result = StringUtils.substringAfter(requestUri, "/");

        return "validationAccountSource";
//        return result;
    }
}

//public class FeatureContextFilter { }
