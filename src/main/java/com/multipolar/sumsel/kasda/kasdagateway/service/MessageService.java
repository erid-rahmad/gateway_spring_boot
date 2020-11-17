package com.multipolar.sumsel.kasda.kasdagateway.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.HashMap;

@Slf4j
@Service
public class MessageService {

    public StringBuilder convert(String other, String key, String leftpad, String rightpad, int length,Object value1,int bit,int x,StringBuilder trace){
        StringBuilder message = new StringBuilder();
        Map<String,String> values = new HashMap<>();
        String requestValue = null;

        if(x==0) {

            if (other != null)
                requestValue = other;
            else if (key != null) {
                Object value = value1;

                if (value == null) {
                    log.warn(
                            "key value is null, please check schema validation or rule for this request. Value is set to empty string. Key is {}", key);
                    requestValue = "";
                } else {
                    requestValue = value.toString();

                }
            }
        }
        else if (x==2){

            if (other != null)
                requestValue = other;
            else if (key != null) {
                Object value = value1;
                if (value == null) {
                    log.warn(
                            "key value is null, please check schema validation or rule for this request. Value is set to empty string. Key is {}", key);
                    requestValue = "";
                } else {
                    requestValue = value.toString();
                }
            }
        }
        else {
            if (other != null)
                requestValue = other;
            else if (key != null) {
                Object value = value1;
                if (value == null) {
                    log.warn(
                            "key value is null, please check schema validation or rule for this request. Value is set to empty string. Key is {}", key);
                    requestValue = "";
                } else {
                    requestValue = value.toString();
                }
            }
        }
        values.put(key, requestValue);

        if(key.equals("tx_partner_id")){

        }


        if (requestValue == null)
            throw new IllegalArgumentException("One of other or key need to be defined in the rule");

        if (length != 0) {
            if (leftpad != null && rightpad == null) {
                message.append(StringUtils.leftPad(requestValue, length, leftpad));
                message.delete(length,message.length());
//                log.info("msg {} length {}",message,message.length());
            }
            else if (rightpad != null && leftpad == null) {
                message.append(StringUtils.rightPad(requestValue, length, rightpad));
                message.delete(length,message.length());
            }
            else if (leftpad == null && rightpad == null && requestValue.length() == length) {
                message.append(requestValue);
                message.delete(length,message.length());
            }
            else {
                String exception = "Can't parse rule, please recheck the rule file for bit " + bit;
                log.error(exception);
                log.info("masuk bit");
                throw new IllegalArgumentException(exception);
            }
        } else {
            if (leftpad != null || rightpad != null) {
                String exception = "Cant have leftpad or rightpad when length is zero";
                log.error(exception);
                throw new IllegalArgumentException(exception);
            } else {
                message.append("");
                log.info("massage4 {}",message);
            }
        }
        if(key.equals("tx_partner_id") | key.equals("inquiry_partner_id")){
            message.replace(0,6, String.valueOf(trace));
        }
        return message;
    }
    public StringBuilder convert(String other, String key, String leftpad, String rightpad, int length,Object value1,int bit,int x){
        StringBuilder message = new StringBuilder();
        Map<String,String> values = new HashMap<>();
        String requestValue = null;

        if(x==0) {

            if (other != null)
                requestValue = other;
            else if (key != null) {
                Object value = value1;

                if (value == null) {
                    log.warn(
                            "key value is null, please check schema validation or rule for this request. Value is set to empty string. Key is {}", key);
                    requestValue = "";
                } else {
                    requestValue = value.toString();

                }
            }
        }
        else if (x==2){

            if (other != null)
                requestValue = other;
            else if (key != null) {
                Object value = value1;
                if (value == null) {
                    log.warn(
                            "key value is null, please check schema validation or rule for this request. Value is set to empty string. Key is {}", key);
                    requestValue = "";
                } else {
                    requestValue = value.toString();
                }
            }
        }
        else {
            if (other != null)
                requestValue = other;
            else if (key != null) {
                Object value = value1;
                if (value == null) {
                    log.warn(
                            "key value is null, please check schema validation or rule for this request. Value is set to empty string. Key is {}", key);
                    requestValue = "";
                } else {
                    requestValue = value.toString();
                }
            }
        }
        values.put(key, requestValue);

        if (requestValue == null)
            throw new IllegalArgumentException("One of other or key need to be defined in the rule");

        if (length != 0) {
            if (leftpad != null && rightpad == null) {
                message.append(StringUtils.leftPad(requestValue, length, leftpad));
                message.delete(length,message.length());
//                log.info("msg {} length {}",message,message.length());
            }
            else if (rightpad != null && leftpad == null) {
                message.append(StringUtils.rightPad(requestValue, length, rightpad));
                message.delete(length,message.length());
            }
            else if (leftpad == null && rightpad == null && requestValue.length() == length) {
                message.append(requestValue);
                message.delete(length,message.length());
            }
            else {
                String exception = "Can't parse rule, please recheck the rule file for bit " + bit;
                log.error(exception);
                log.info("masuk bit");
                throw new IllegalArgumentException(exception);
            }
        } else {
            if (leftpad != null || rightpad != null) {
                String exception = "Cant have leftpad or rightpad when length is zero";
                log.error(exception);
                throw new IllegalArgumentException(exception);
            } else {
                message.append("");
                log.info("massage4 {}",message);
            }
        }
        return message;
    }
}
