package com.multipolar.sumsel.kasda.kasdagateway.converter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipolar.sumsel.kasda.kasdagateway.model.Rule;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
public abstract class AbstractMessageConverter implements MessageConverterHandler {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMddHHmmss");
    private static final SimpleDateFormat LOCAL_DATE = new SimpleDateFormat("MMdd");
    private static final SimpleDateFormat LOCAL_TIME = new SimpleDateFormat("HHmmss");
    private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
    private String defaultMTI = "0200";
    private String defaultBIN = "627486";
    private String defaultAcquirer = Constants.AQUIERER_ID;
    private String defaultForwarder = "100800";
    private String defaultErrorDesc = "0000000";
    private String defaultCurrency = Constants.CURRENCY_CODE;

    private static final String RULE_EXTENSION = ".rule.json";

//    @Autowired
//    private TraceNumberGenerator stan;

    public Map<String, Object> doConvertToJSon(ISOMsg msg, boolean fl) throws InvalidMessageException {
        // log iso msg here

        // call spesific conversion
        try {
            return translatePrivateMessage(msg, fl);
        } catch (ISOException e) {
            throw new InvalidMessageException(e.getMessage());
        }
    }

    @Override
    public ISOMsg doConvertToISO(Map<String, Object> map) throws InvalidMessageException {
        Date date = new Date();
        FeatureContextHolder.getContext().setFeatureTransactionDate(date);
        try {
            ISOMsg msg = new ISOMsg();
            setMti(msg, defaultMTI);
            // setPan(msg, defaultBIN);
            setTransmissionDateTime(msg, date);
            setTraceAuditNumber(msg);
            setTransmissionTime(msg, date);
            setTransmissionDate(msg, date);
//            setSettlementDate(msg, date);
            setChannelID(msg, Constants.MERCHANT_TYPE);
            setAcquirer(msg, defaultAcquirer);
            setForwarder(msg, defaultForwarder);
            setCurrency(msg, defaultCurrency);
            // setBit62(msg);
//            FIXME: relate with stan
//            setRefferenceNumber(msg, getReferenceNumber());
            // setTerminal(msg);
            // setTerminalLocation(msg);
            setPrivateMessage(msg, map);
            //setCheckNumber(msg);
            setTerminalID(msg);

            return msg;
        } catch (ISOException ex) {
            throw new InvalidMessageException(ex.getMessage());
        }
    }

    protected void setMti(ISOMsg msg, String mti) throws ISOException {
        try {
            msg.setMTI(mti);
        } catch (ISOException e) {
            log.error("Failed to set MTI for ISO Message: {}", mti);
            throw new ISOException(e);
        }
    }

    protected void setPan(ISOMsg msg, String bin) throws ISOException {
        msg.set(2, StringUtils.rightPad(bin, 19, '0'));
    }

    protected void setTransmissionDateTime(ISOMsg msg, Date date) throws ISOException {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE_GMT));
        msg.set(7, DATE_FORMAT.format(FeatureContextHolder.getContext().getFeatureTransactionDate()));
    }

    // FIXME: for stan generator
    protected void setTraceAuditNumber(ISOMsg msg) throws ISOException {
//        String s = stan.getTraceNumber();
//        FeatureContextHolder.getContext().setTraceNumber(s);
//        msg.set(11, s);
    }

    protected void setTransmissionTime(ISOMsg msg, Date date) throws ISOException {
        msg.set(12, LOCAL_TIME.format(date));
    }

    protected void setTransmissionDate(ISOMsg msg, Date date) throws ISOException {
        msg.set(13, LOCAL_DATE.format(date));
    }

    protected void setSettlementDate(ISOMsg msg, Date date) throws ISOException {
        msg.set(15, LOCAL_DATE.format(date));
    }

    protected void setChannelID(ISOMsg msg, String merchant) throws ISOException {
        msg.set(18, merchant);
    }

    protected void setAcquirer(ISOMsg msg, String s) throws ISOException {
        msg.set(32, s);
    }

    protected void setForwarder(ISOMsg msg, String s) throws ISOException {
        msg.set(33, s);
    }

    protected void setCurrency(ISOMsg msg, String s) throws ISOException {
        msg.set(49, s);
    }

    protected void setRefferenceNumber(ISOMsg msg, String s) throws ISOException {
        msg.set(37, StringUtils.leftPad(s, 12, "0"));
    }

    protected void setBit62(ISOMsg msg) throws ISOException {
        String year = YEAR_FORMAT.format(new Date());
        String s = year + defaultErrorDesc + defaultForwarder + "     ";
        msg.set(62, s);
    }

    protected void setCheckNumber(ISOMsg isoMsg) throws ISOException {
        String checkNumber = isoMsg.getString(58);
        if (checkNumber == null) {
            return;
        } else if (StringUtils.isBlank(checkNumber)) {
            isoMsg.unset(58);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder(isoMsg.getString(3));
        stringBuilder.setCharAt(2, '2');
        isoMsg.set(3, stringBuilder.toString());
    }

    protected void setDefaultMTI(String mti) {
        defaultMTI = mti;
    }

    protected void setDefaultBIN(String bin) {
        defaultBIN = bin;
    }

    protected abstract void setPrivateMessage(ISOMsg msg, Map<String, Object> map) throws ISOException;

    protected abstract Map<String, Object> translatePrivateMessage(ISOMsg msg, boolean fl) throws ISOException;

//    protected String getStan() {
//        return stan.getTraceNumber();
//    }
//
//    protected String getReferenceNumber() {
//        return stan.getReferenceNumber();
//    }

    protected void setTerminalID(ISOMsg msg) throws ISOException {
        msg.set(41, Constants.TERMINAL_ID);
    }

    public Rule getRule() throws IOException {
        // default implementasion is read the rule file base on feature -->
        // feature.rule.json
        String feature = FeatureContextHolder.getContext().getFeatureName();
        String filename = feature + RULE_EXTENSION;

        log.info("rule name is: {}", filename);

        ObjectMapper objectMapper = new ObjectMapper();

        Resource resource = new ClassPathResource("rules/" + filename);

        // convert json string to object
        return objectMapper.readValue(resource.getInputStream(), Rule.class);
    }

}
