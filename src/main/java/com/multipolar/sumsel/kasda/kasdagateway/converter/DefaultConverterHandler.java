package com.multipolar.sumsel.kasda.kasdagateway.converter;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multipolar.sumsel.kasda.kasdagateway.model.ConverterRule;
import com.multipolar.sumsel.kasda.kasdagateway.model.NestedRule;
import com.multipolar.sumsel.kasda.kasdagateway.model.RequestRule;
import com.multipolar.sumsel.kasda.kasdagateway.model.Rule;
import com.multipolar.sumsel.kasda.kasdagateway.service.MessageService;
import com.multipolar.sumsel.kasda.kasdagateway.servlet.filter.FeatureContextHolder;
import com.multipolar.sumsel.kasda.kasdagateway.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DefaultConverterHandler extends AbstractMessageConverter {

    @Autowired
    MessageService messageService;

    public DefaultConverterHandler() {
    }

    @Override
    public boolean supports(String s) {
        // always return true
        return true;
    }

    @Override
    protected void setPrivateMessage(ISOMsg msg, Map<String, Object> map) throws ISOException {
        try {
            log.info("its setprivate message");
            Rule rule = getRule();

            msg.set(3, rule.getPcode());

            RequestRule[] requestRules = rule.getRequest();
            for (RequestRule requestRule : requestRules) {
                int bit = requestRule.getBit();
                ConverterRule[] values = requestRule.getValue();
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
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            log.info("ist traslete private message 1");
            Rule rule = getRule();

            String errorKey = rule.getErrorKey();
            if (msg.isResponse() && exception(msg, map, errorKey))
                return map;

            RequestRule[] responseRules = rule.getResponse();
            for (RequestRule responseRule : responseRules) {
                int bit = responseRule.getBit();
                String valueFromBit = msg.getString(bit);
                if (valueFromBit == null) {
                    log.error("Value get from bit is null, bit = {}", bit);
                    throw new ISOException("The rule said get value from bit but empty bit is found. bit " + bit);
                }

                ConverterRule[] values = responseRule.getValue();
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

            String destName = FeatureContextHolder.getContext().getDestinationName();
            if (!StringUtils.isEmpty(destName))
                map.put("destinationName", destName);
        } else {
            map.put("cardIdAtm", msg.getString(2));
            map.put("transactionDateAtm", msg.getString(13));
            try {
                map.put("transactionTimeAtm", Constants.TIME.format(Constants.TIME_SPACE.parse(msg.getString(12))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            map.put("traceNumberAtm", msg.getString(37));
            map.put("atmCodeAtm", msg.getString(41));
            map.put("atmNameAtm", msg.getString(43));
        }
        Map<String, String> statusMap = new HashMap<String, String>();
        statusMap.put("status", "00");
        statusMap.put("code", "00");
        statusMap.put("message", "Request Success");
        map.put("status", statusMap);
        return map;
    }

    protected String getValueForBit(Map<String, Object> map, int bit, ConverterRule[] rules) throws IllegalArgumentException {
        Map<String, String> values = new HashMap<>();
//        log.info("this rule: "+rules.toString());
//        log.info("this map "+map.get("account_number"));


        StringBuilder message = new StringBuilder();
        StringBuilder message1 = null,message2,message3 = new StringBuilder();

        for (ConverterRule rule : rules) {
//            log.info("this rule: "+rule);

            String other = rule.getOther();
            String key = rule.getKey();
            int length = rule.getLength();
            String leftpad = rule.getLeftpad();
            String rightpad = rule.getRightpad();
            NestedRule[] nestedRule = rule.getLain();


            Object value1 =  map.get(key);
            log.info("this value1 {}",value1);
            int x = 0;




            if(nestedRule != null) {
                    x=1;
                    for (NestedRule nestedRule1 : nestedRule) {
//                        log.info("this map 1 "+map.get("sender_info"));

                        String other1 = nestedRule1.getOther();
                        String key1 = nestedRule1.getKey();
                        int length1 = nestedRule1.getLength();
                        String leftpad1 = nestedRule1.getLeftpad();
                        String rightpad1 = nestedRule1.getRightpad();
//                        log.info("this key1 {}",key1);

                        Map<String,String> bebas = (Map<String, String>) map.get(key);
                        String asd = bebas.get(key1);
                        value1 = asd;
//                        log.info("this sub "+asd);

                        log.info("this value11 {}",value1);
//                        log.debug("for in converterrule key: {} lenght: {} leftpad: {} rightpad: {} other:{} nestedrule{}",key1,length1,leftpad1,rightpad1,other1,nestedRule1);
                        message2 = messageService.convert(other1,key1,leftpad1,rightpad1,length1,value1,bit,x);
                        message3=message3.append(message2);
                        log.info("this message 3 {}",message3);

                    }
            }
            if(nestedRule == null){
                message = messageService.convert(other,key,leftpad,rightpad,length,value1,bit,x);
                if(message1==null){
                    message1=message;
                }
                else {
                    message1=message.append(message1);
                }

                log.info("this message1 {}",message1);
            }else {
                log.info("not with asd");
                message1=message3.append(message1);
                log.info("this message1.1 {}",message1);
            }

//            log.info("this first message {}",message);
//            log.debug("for in converterrule key: {} lenght: {} leftpad: {} rightpad: {} other:{} nestedrule{}",key,length,leftpad,rightpad,other,nestedRule);




        }
        log.info("final message {}",message1);

        return message1.toString();
    }

    protected void setValueForMap(Map<String, Object> map, String bitValue, ConverterRule[] rules) {
        int index = 0;
        for (ConverterRule rule : rules) {
            log.info("its set valur for map converterrule");
            String key = rule.getKey();
            int length = rule.getLength();
            String leftpad = rule.getLeftpad();
            String rightpad = rule.getRightpad();

            if (length == 0)
                length = bitValue.length();

            String value = StringUtils.substring(bitValue, index, index + length);
            if (leftpad != null)
                value = StringUtils.stripStart(value, leftpad);
            else if (rightpad != null)
                value = StringUtils.stripEnd(value, rightpad);

            log.debug("just trykey ---  {} ",key);
            log.debug("just trykey value---  {} ",value);
            log.debug("just trykey bitvalue---  {} ",bitValue);

            map.put(key, value);
            index = index + length;
        }
    }

    public Rule getRule() throws IOException {
        // default implementasion is read the rule file base on feature -->
        // feature.rule.json
        String feature = FeatureContextHolder.getContext().getFeatureName();
        String filename = feature + Constants.RULE_EXTENSION;

        log.info("rule name is: {}", filename);

        ObjectMapper objectMapper = new ObjectMapper();

        Resource resource = new ClassPathResource("rules/" + filename);

        // convert json string to object
        return objectMapper.readValue(resource.getInputStream(), Rule.class);
    }

    protected boolean exception(ISOMsg msg, Map<String, Object> map, String errorKey) {
        String resultCode = msg.getString(39);
        if (StringUtils.equals(resultCode, "00"))
            return false;

        Map<String, String> statusMap = new HashMap<String, String>();
        statusMap.put("status", "01");
        statusMap.put("code", resultCode);
        statusMap.put("message", "Unmapping error code " + resultCode);
        map.put("status", statusMap);

        return true;
    }

    protected Map<String, Object> generalFail() {
        String generalMsg = "Terjadi kesalahan pada system. Silahkan ulangi atau hubungi administrator";
        Map<String, Object> statusMap = new HashMap<String, Object>();
        statusMap.put("status", "02");
        statusMap.put("code", "99");
        statusMap.put("message", generalMsg);
        return statusMap;
    }

    protected Map<String, Object> fail(String exc) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> statusMap = new HashMap<String, Object>();
        statusMap.put("status", "01");
        statusMap.put("code", "50");
        statusMap.put("message", exc);

        map.put("status", statusMap);
        return map;
    }

}
