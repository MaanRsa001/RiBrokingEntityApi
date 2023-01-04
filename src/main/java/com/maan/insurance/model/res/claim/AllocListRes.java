package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllocListRes {


	@JsonProperty("Date")
	private String date;
	@JsonProperty("SNo")
	private String sNo;
	@JsonProperty("TransactionType")
	private String transactionType;
	@JsonProperty("PaidAmountOrigcurr")
	private String paidAmountOrigcurr;

	@JsonProperty("Statusofclaim")
	private String statusofclaim;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Sign")
	private String sign;
	@JsonProperty("ReceiptNo")
	private String receiptNo;

}
