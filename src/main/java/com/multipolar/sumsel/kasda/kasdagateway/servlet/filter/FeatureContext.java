package com.multipolar.sumsel.kasda.kasdagateway.servlet.filter;

import org.jpos.iso.ISOMsg;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface FeatureContext extends Serializable {
    String getFeatureName();

    void setFeatureName(String featureName);

    String getSourceName();

    void setSourceName(String sourceName);

    String getDestinationName();

    void setDestinationName(String destinationName);

    Date getFeatureTransactionDate();

    void setFeatureTransactionDate(Date featureTransactionDate);

    Date getSettlementDate();

    void setSettlementDate(Date date);

    Map<String, Object> getParameters();

    void setParameters(Map<String, Object> add);

    String getTraceNumber();

    void setTraceNumber(String traceNumber);

    ISOMsg getRequest();

    void setRequest(ISOMsg isoMsg);

    String getTraceNumberTmp();

    void setTraceNumberTmp(String traceNumberTmp);

    String getNominal();

    void setNominal(String nominal);
}
