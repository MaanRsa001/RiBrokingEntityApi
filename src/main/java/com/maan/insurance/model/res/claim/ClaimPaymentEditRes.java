package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimPaymentEditRes {
	@JsonProperty("PaymentRequestNo")
	private String paymentRequestNo;
	@JsonProperty("PaymentReference")
	private String paymentReference;
	@JsonProperty("PaidClaimOs")
	private String paidClaimOs;
	@JsonProperty("SurveyOrFeeOs")
	private String surveyOrFeeOs;
	@JsonProperty("OtherprofFeeOs")
	private String otherprofFeeOs;
	@JsonProperty("PaidAmountOrigCurr")
	private String paidAmountOrigCurr;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
//	@JsonProperty("ReinstType")
//	private String reinstType;
	@JsonProperty("ReinstPremiumOCOS")
	private String reinstPremiumOCOS;
	@JsonProperty("PaymentType")
	private String paymentType;
}
