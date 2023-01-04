package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class premiumUpdateMethodRes1 {
	
	@JsonProperty("RequestNo")
	private String requestNo;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;

}
