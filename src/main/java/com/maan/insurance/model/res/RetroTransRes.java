package com.maan.insurance.model.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroTransRes {
	@JsonProperty("Contractno")
	private String contractno;
	@JsonProperty("Transactionno")
	private String transactionno;
	@JsonProperty("Inceptiobdate")
	private String inceptiobdate;
	@JsonProperty("Netdue")
	private String netdue;
	@JsonProperty("CheckPC")
	private String checkPC;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("AllocType")
	private String allocType;
	@JsonProperty("Count")
	private String count;
	@JsonProperty("ReceivePayAmounts")
	private String receivePayAmounts;
	
	@JsonProperty("Chkbox")
	private String chkbox;
	
	@JsonProperty("PreviousValue")
	private String previousValue;
}
