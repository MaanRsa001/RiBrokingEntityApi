package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReceiptEditRes {

	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("ReceiptBankId")
	private String receiptBankId;
	
	@JsonProperty("TrDate")
	private String trDate;
	
	@JsonProperty("PaymentAmount")
	private String paymentAmount;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("Exrate")
	private String exrate;
	
	@JsonProperty("TransactionType")
	private String transactionType;
	
	@JsonProperty("TransactionTypeTest")
	private String transactionTypeTest;
	
	@JsonProperty("BankCharges")
	private String bankCharges;
	
	@JsonProperty("NetAmt")
	private String netAmt;
	
	@JsonProperty("AmendDate")
	private String amendDate;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("BankchargeDC")
	private String bankchargeDC;
	
	@JsonProperty("NetAmtDC")
	private String netAmtDC;
	
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	
	@JsonProperty("PremiumLavy")
	private String premiumLavy;
}
