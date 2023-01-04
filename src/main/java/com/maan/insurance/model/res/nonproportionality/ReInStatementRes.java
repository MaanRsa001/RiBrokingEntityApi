package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReInStatementRes {
	@JsonProperty("Sno")
	private String sno;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Amount")
	private String amount;

	@JsonProperty("Minamount")
	private String minamount;
	
	@JsonProperty("MinTime")
	private String minTime;
}
