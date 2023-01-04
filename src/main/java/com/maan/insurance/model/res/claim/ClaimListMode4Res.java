package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimListMode4Res {
	@JsonProperty("PaymentRequestNo")
	private String paymentRequestNo;
	@JsonProperty("PaidAmountOrigCurr")
	private String paidAmountOrigCurr;
//	@JsonProperty("LossEstimateRevisedOrigCurr")
//	private String lossEstimateRevisedOrigCurr;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
	@JsonProperty("SNo")
	private String sNo;
//	@JsonProperty("SettlementStatus")
//	private String settlementStatus;
//	@JsonProperty("TransactionType")
//	private String transactionType;
//	@JsonProperty("TransactionNumber")
//	private String transactionNumber;
//	@JsonProperty("StatusofClaim")
//	private String statusofClaim;
//	@JsonProperty("AllocatedYN")
//	private String allocatedYN;
//	@JsonProperty("TransOpenperiodStatus")
//	private String transOpenperiodStatus;
	
}
