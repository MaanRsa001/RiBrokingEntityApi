package com.maan.insurance.model.res.adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertReverseResponse {
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("Allocateddate")
	private String allocateddate;
	@JsonProperty("Currency")
	private String Currency;
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("Type")
	private String 	type;
	
	@JsonProperty("ReceiptCheck")
	private String receiptCheck;
	@JsonProperty("Netdue")
	private String netdue;
	@JsonProperty("Payamount")
	private String payamount;

}
