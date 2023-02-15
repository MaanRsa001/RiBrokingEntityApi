package com.maan.insurance.model.res.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EditBillingTransactionRes {

	@JsonProperty("CurrencyId")
	private String currencyId; 

	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("LayerNo")
	private String layerNo; 
	@JsonProperty("PaidAmount")
	private String paidAmount;
	
	@JsonProperty("ProductName")
	private String productName; 
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("billSno")
	private String billSno;
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("GrossAmount")
	private String grossAmount;
	@JsonProperty("WhtPremium")
	private String whtPremium; 
	@JsonProperty("WhtBrokerage")
	private String whtBrokerage; 
	@JsonProperty("NetAmount")
	private String netAmount;
	
}
