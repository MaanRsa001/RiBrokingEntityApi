package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsListMode5res {
	@JsonProperty("SumOfPaidAmountOC")
	private String sumOfPaidAmountOC;
	@JsonProperty("RevSumOfPaidAmt")
	private String revSumOfPaidAmt;
	@JsonProperty("PaidAmountOrigcurr")
	private String paidAmountOrigcurr;
	@JsonProperty("PaymentRequestNo")
	private String paymentRequestNo;
	@JsonProperty("LossEstimateRevisedOrigCurr")
	private String lossEstimateRevisedOrigCurr;
	@JsonProperty("ClaimNoterecommendations")
	private String claimNoterecommendations;
	@JsonProperty("PaymentReference")
	private String paymentReference;
	@JsonProperty("AdviceTreasury")
	private String adviceTreasury;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("PaidAmountUSD")
	private String paidAmountUSD;
	@JsonProperty("LossEstimateRevisedUSD")
	private String lossEstimateRevisedUSD;
	
	
	

}
