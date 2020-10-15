package com.multipolar.sumsel.kasda.kasdagateway.servlet.filter;

import org.springframework.util.Assert;

public class ThreadLocalFeatureContextHolderStrategy {

    private static final ThreadLocal<FeatureContext> contextHolder = new ThreadLocal<FeatureContext>();

    public void clearContext() {
        contextHolder.remove();
    }

    public FeatureContext getContext() {
        FeatureContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public void setContext(FeatureContext context) {
        Assert.notNull(context, "Only non-null FeatureContext instances are permitted");
        contextHolder.set(context);
    }

    public FeatureContext createEmptyContext() {
        return new FeatureContextImpl();
    }
}
