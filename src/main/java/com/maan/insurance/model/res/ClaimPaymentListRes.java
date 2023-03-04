package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimPaymentListRes {
	
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("CedingcompanyName")
	private String cedingcompanyName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("PaymentRequestNo")
	private String paymentRequestNo;
	@JsonProperty("PaidAmountOrigcurr")
	private String paidAmountOrigcurr;
	@JsonProperty("LossEstimateRevisedOrigCurr")
	private String lossEstimateRevisedOrigCurr;
	@JsonProperty("InceptionDt")
	private String inceptionDt;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
	@JsonProperty("SNo")
	private String sNo;
	@JsonProperty("SettlementStatus")
	private String settlementStatus;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("TransactionNumber")
	private String transactionNumber;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("AllocatedYN")
	private String allocatedYN;
	@JsonProperty("Statusofclaim")
	private String statusofclaim;
	@JsonProperty("TransOpenperiodStatus")
	private String transOpenperiodStatus;
	@JsonProperty("SectionNo")
	private String sectionNo;
	@JsonProperty("ClaimPaymentRiNo")
	private String claimPaymentRiNo;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("NewLayerNo")
	private String newLayerNo;
}
