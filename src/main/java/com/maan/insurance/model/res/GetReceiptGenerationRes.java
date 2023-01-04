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
public class GetReceiptGenerationRes {

	
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("Currecncy")
	private String currecncy;
	
	@JsonProperty("PaymentAmount")
	private String paymentAmount;
	
	@JsonProperty("TrDate")
	private String trDate;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	@JsonProperty("Entrate")
	private String entrate;
	
	@JsonProperty("TotalExchaAmount")
	private String totalExchaAmount;
	
	@JsonProperty("DiffAmount")
	private String diffAmount;
	
	@JsonProperty("ConvDiffAmount")
	private String convDiffAmount;
	
	@JsonProperty("CurrecncyValue")
	private String currecncyValue;
	
	@JsonProperty("TransactionType")
	private String transactionType;
	
	@JsonProperty("BankCharges")
	private String bankCharges;
	
	@JsonProperty("NetAmt")
	private String netAmt;
	
	@JsonProperty("ReceiptBankName")
	private String receiptBankName;
	
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("PremiumLavy")
	private String premiumLavy;
	
	
}
