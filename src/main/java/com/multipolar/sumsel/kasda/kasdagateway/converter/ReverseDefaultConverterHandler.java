package com.multipolar.sumsel.kasda.kasdagateway.converter;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.multipolar.sumsel.kasda.kasdagateway.model.ConverterRule;
import com.multipolar.sumsel.kasda.kasdagateway.model.RequestRule;
import com.multipolar.sumsel.kasda.kasdagateway.model.Rule;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ReverseDefaultConverterHandler extends DefaultConverterHandler {


    @Override
    protected void setPrivateMessage(ISOMsg msg, Map<String, Object> map) throws ISOException {
        try {
            Rule rule = getRule();

            msg.set(3, rule.getPcode());

            RequestRule[] responseRules = rule.getResponse();
            for (RequestRule responseRule : responseRules) {
                int bit = responseRule.getBit();
                ConverterRule[] values = responseRule.getValue();
                String valueForBit = getValueForBit(map, bit, values);
                msg.set(bit, valueForBit);
            }
        } catch (FileNotFoundException fnfe) {
            log.error("file not found exception", fnfe);
        } catch (JsonMappingException jme) {
            log.error("Can't parsing rule file, please validate the json format", jme);
        } catch (IOException iox) {
            log.error("io exception", iox);
        }
    }

    @Override
    protected Map<String, Object> translatePrivateMessage(ISOMsg msg, boolean fl) throws ISOException {
        Map<String, Object> map = new HashMap<>();
        try {
            Rule rule = getRule();

            String errorKey = rule.getErrorKey();
            if (msg.isResponse() && exception(msg, map, errorKey))
                return map;

            RequestRule[] requestRules = rule.getRequest();
            for (RequestRule requestRule : requestRules) {
                int bit = requestRule.getBit();
                String valueFromBit = msg.getString(bit);
                if (valueFromBit == null) {
                    log.error("Value get from bit is null, bit = {}", bit);
                    throw new ISOException("The rule said get value from bit but empty bit is found. bit " + bit);
                }

                ConverterRule[] values = requestRule.getValue();
                setValueForMap(map, valueFromBit, values);
            }
        } catch (FileNotFoundException fnfe) {
            log.error("file not found exception ==> ", fnfe);
            return generalFail();
        } catch (JsonMappingException jme) {
            log.error("Can't parsing rule file, please validate the json format", jme);
            return generalFail();
        } catch (IOException iox) {
            log.error("io exception", iox);
            return generalFail();
        }


        if (fl) {
            // we need put source account number in map also
//            String sourceName = FeatureContextHolder.getContext().getSourceName();
//            UserDetailImpl userDetail = (UserDetailImpl) SecurityContextHolder.getContext().getAuthentication().getDetails();
//            map.put("sourceName", sourceName);
//            map.put("transactionDate", DATE.format(FeatureContextHolder.getContext().getFeatureTransactionDate()));

        } else {
//            map.put("cardIdAtm", msg.getString(2));
//            map.put("transactionDateAtm", msg.getString(13));
//            try {
//                map.put("transactionTimeAtm", Constants.TIME.format(Constants.TIME_SPACE.parse(msg.getString(12))));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            map.put("traceNumberAtm", msg.getString(37));
//            map.put("atmCodeAtm", msg.getString(41));
//            map.put("atmNameAtm", msg.getString(43));
        }
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("status", "00");
        statusMap.put("code", "00");
        statusMap.put("message", "Request Success");
        map.put("status", statusMap);
        return map;
    }
}
