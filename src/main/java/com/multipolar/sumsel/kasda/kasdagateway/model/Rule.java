package com.multipolar.sumsel.kasda.kasdagateway.model;

import lombok.Data;

@Data
public class Rule {
	// pcode yang digunakan
	private String pcode;
	
	// routing feature
	private String routing;
	
	// error mapping
	private String errorKey;
	
	// jenis transaction <-- digunakan untuk report
	private String transaction;
	
	
	private RequestRule[] request;
	private RequestRule[] response;
	
	private RequestRule[] report;
}
