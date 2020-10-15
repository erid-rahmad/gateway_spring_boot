package com.multipolar.sumsel.kasda.kasdagateway.servlet.filter;

import org.jpos.iso.ISOMsg;

import java.util.Date;
import java.util.Map;

public class FeatureContextImpl implements FeatureContext {

    private static final long serialVersionUID = 1290690518767141798L;

    private String featureName;
    private String sourceName;
    private String destinationName;
    private Date settlementDate;
    private Date featureTransactionDate;
    private String traceNumber;
    private ISOMsg request;
    private String traceNumberTmp;
    private String nominal;

    private Map<String, Object> parameters;

    @Override
    public String getFeatureName() {
        return featureName;
    }

    @Override
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    @Override
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Override
    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    @Override
    public Date getFeatureTransactionDate() {
        return featureTransactionDate;
    }

    @Override
    public void setFeatureTransactionDate(Date featureTransactionDate) {
        this.featureTransactionDate = featureTransactionDate;
    }

    @Override
    public Date getSettlementDate() {
        return settlementDate;
    }

    @Override
    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Override
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public void setParameters(Map<String, Object> params) {
        this.parameters = params;
    }

    @Override
    public String getTraceNumber() {
        return traceNumber;
    }

    @Override
    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    @Override
    public ISOMsg getRequest() {
        return request;
    }

    @Override
    public void setRequest(ISOMsg isoMsg) {
        this.request = isoMsg;
    }

    @Override
    public String getTraceNumberTmp() {
        return traceNumberTmp;
    }

    @Override
    public void setTraceNumberTmp(String traceNumberTmp) {
        this.traceNumberTmp = traceNumberTmp;
    }

    @Override
    public String getNominal() {
        return nominal;
    }

    @Override
    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

}
