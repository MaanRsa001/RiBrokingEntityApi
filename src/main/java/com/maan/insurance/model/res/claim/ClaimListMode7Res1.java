package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimListMode7Res1 {
	@JsonProperty("CurrencyName")
	private String currencyName;
		
	@JsonProperty("TotalORpaidAmount")
	private String totalORpaidAmount;
	
	@JsonProperty("TotalSApaidAmount")
	private String totalSApaidAmount;
	
	@JsonProperty("TotalOPpaidAmount")
	private String totalOPpaidAmount;
	
	@JsonProperty("TotalTORpaidAmount")
	private String totalTORpaidAmount;
	
	@JsonProperty("Result")
	private List<ClaimListMode7ResList> commonResponse;
}
