package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsModeReq {
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
//	@JsonProperty("SumOfPaidAmountOC")
//	private String sumOfPaidAmountOC;
//	@JsonProperty("RevSumOfPaidAmt")
//	private String revSumOfPaidAmt;
	

}
