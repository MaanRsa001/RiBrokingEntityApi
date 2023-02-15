package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReversalInfoReq {

	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("TransType")
	private String transType;
	
	@JsonProperty("PaymentAmount")
	private String paymentAmount;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	
	@JsonProperty("Flag")
	private String flag; 
	
	@JsonProperty("BranchCode")
	private String BranchCode;
	
	@JsonProperty("TransactionDate")
	private String transactionDate;
}
