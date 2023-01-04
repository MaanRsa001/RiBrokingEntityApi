package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsListMode10res {
	@JsonProperty("SumOfPaidAmountOC")
	private String sumOfPaidAmountOC;
	@JsonProperty("RevSumOfPaidAmt")
	private String revSumOfPaidAmt;
	@JsonProperty("LossEstimateRevisedOrigCurr")
	private String lossEstimateRevisedOrigCurr;
	

}
