package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsListMode7res {
	@JsonProperty("SumOfPaidAmountOC")
	private String sumOfPaidAmountOC;
	@JsonProperty("RevSumOfPaidAmt")
	private String revSumOfPaidAmt;
	@JsonProperty("ReviewDate")
	private String reviewDate;
	@JsonProperty("ReviewDoneBy")
	private String reviewDoneBy;
	
	
	

}
