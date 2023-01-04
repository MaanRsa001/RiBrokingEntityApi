package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsListMode6res {
	@JsonProperty("SumOfPaidAmountOC")
	private String sumOfPaidAmountOC;
	@JsonProperty("RevSumOfPaidAmt")
	private String revSumOfPaidAmt;
	@JsonProperty("LossEstimateRevisedOrigCurr")
	private String lossEstimateRevisedOrigCurr;
	@JsonProperty("LossEstimateRevisedUSD")
	private String lossEstimateRevisedUSD;
	@JsonProperty("UpdateReference")
	private String updateReference;
	@JsonProperty("CliamupdateDate")
	private String cliamupdateDate;

}
