package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReceiptTreasuryRes {
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("Brokername")
	private String brokername;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("Paymentamount")
	private String paymentamount;
	@JsonProperty("Trdate")
	private String trdate;
	@JsonProperty("Serialno")
	private String serialno;
	@JsonProperty("Exchangerate")
	private String exchangerate;
	@JsonProperty("Exrate")
	private String exrate;
	@JsonProperty("Totalexchaamount")
	private String totalexchaamount;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("DiffAmount")
	private String diffAmount;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	@JsonProperty("Payrecno")
	private String payrecno;
	@JsonProperty("BankCharges")
	private String bankCharges;
	@JsonProperty("NetAmt")
	private String netAmt;
	@JsonProperty("ReceiptBankName")
	private String receiptBankName;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	@JsonProperty("PremiumLavy")
	private String premiumLavy;
	

}
