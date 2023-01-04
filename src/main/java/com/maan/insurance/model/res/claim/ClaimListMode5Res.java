package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimListMode5Res {
	@JsonProperty("PaymentRequestNo")
	private String paymentRequestNo;
	@JsonProperty("PaidAmountOrigCurr")
	private String paidAmountOrigCurr;
	@JsonProperty("LossEstimateRevisedOrigCurr")
	private String lossEstimateRevisedOrigCurr;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
	@JsonProperty("SNo")
	private String sNo;
}
