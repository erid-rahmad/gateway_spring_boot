package com.multipolar.sumsel.kasda.kasdagateway.model;

import lombok.Data;

@Data
public class RequestRule {

	private int bit;
	private String source;
	private String rcInclude;
	private String rcExclude;
	private ConverterRule[] value;
}
