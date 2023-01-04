package com.maan.insurance.model.req;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRecieptReq1 {
	
	@JsonProperty("SerialNo")
	private String serialno;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("PaymentAmount")
	private String paymentAmount;
	
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	
	@JsonProperty("ReceiptBankId")
	private String receiptBankId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DepartmentId")
	private String departmentNo;
	
	@JsonProperty("TransactionDate")
	private String transactionDate;
	
	@JsonProperty("TransType")
	private String transType;
	
	@JsonProperty("TransactionType")
	private String transactionType;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("BankCharges")
	private String bankCharges;
	
	@JsonProperty("NetAmount")
	private String netAmount;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	
	@JsonProperty("AmendDate")
	private String amendDate;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("TxtDiffPer")
	private String txtDiffPer;
	
	@JsonProperty("ConvertedDiffAmount")
	private String convertedDiffAmount;
	
	@JsonProperty("RevPayReceiptNo")
	private String revPayReceiptNo;
	
	@JsonProperty("PremiumLavy")
	private String premiumLavy;
	
	
	
	
}
