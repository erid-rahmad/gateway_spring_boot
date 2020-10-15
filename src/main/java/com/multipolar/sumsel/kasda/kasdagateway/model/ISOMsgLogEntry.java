package com.multipolar.sumsel.kasda.kasdagateway.model;

import lombok.Data;
import org.jpos.iso.ISOMsg;

import java.util.Date;

@Data
public class ISOMsgLogEntry {

	private ISOMsg message;
	private Date date;
	
	public ISOMsgLogEntry() {}
	
	public ISOMsgLogEntry(ISOMsg msg, Date date) {
		this.message = msg;
		this.date = date;
	}
}
