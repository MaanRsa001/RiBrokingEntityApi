package com.maan.insurance.model.res.adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AdjustmentViewRes1 {
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("Allocateddate")
	private String allocateddate;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Payamount")
	private String payamount;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("ReceiptNo")
	private String receiptNo;
	@JsonProperty("AdjustType")
	private String adjustType;
	@JsonProperty("CurrencyName")
	private String 	currencyName;
}
