package com.multipolar.sumsel.kasda.kasdagateway.converter;

import com.multipolar.sumsel.kasda.kasdagateway.model.Rule;
import org.jpos.iso.ISOMsg;

import java.io.IOException;
import java.util.Map;

public interface MessageConverterHandler {
		
	Map<String, Object> doConvertToJSon(ISOMsg isoMsg, boolean fl) throws InvalidMessageException;

	ISOMsg doConvertToISO(Map<String, Object> map) throws InvalidMessageException;

    boolean supports(String s);
    
    Rule getRule() throws IOException;
}
