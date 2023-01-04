package com.maan.insurance.model.res.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumInsertRes {

	@JsonProperty("RequestNo")
	private String requestNo;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
}
