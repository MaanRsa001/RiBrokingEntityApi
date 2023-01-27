package com.maan.insurance.model.res.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EditBillingTransactionRes {
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("CurrencyId")
	private String currencyId; 
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("AdjustmentType")
	private String adjustmentType;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("LayerNo")
	private String layerNo; 
	@JsonProperty("PaidAmount")
	private String paidAmount;
	@JsonProperty("ProcessType")
	private String processType;
	@JsonProperty("ProductName")
	private String productName; 
	
	@JsonProperty("AmedId")
	private String amedId; 
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ReversalAmount")
	private String reversalAmount;
	@JsonProperty("ReversalDate")
	private String reversalDate; 
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("status")
	private String status;
	@JsonProperty("billSno")
	private String billSno;
	@JsonProperty("ContractNo")
	private String contractNo;
	
}
