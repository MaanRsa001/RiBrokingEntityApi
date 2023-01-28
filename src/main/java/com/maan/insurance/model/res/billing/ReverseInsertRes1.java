package com.maan.insurance.model.res.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.billing.ReverseInsertReq;

import lombok.Data;

@Data
public class ReverseInsertRes1 {
	@JsonProperty("Allocateddate")
	private String allocateddate;

	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("NetDue")
	private String netDue;
	
	@JsonProperty("PayAmount")
	private String payAmount;
	
	@JsonProperty("CheckPc")
	private String checkPc;

	
	@JsonProperty("TtrnRetroSoa")
	private String ttrnRetroSoa;
	
	@JsonProperty("AllTillDate")
	private String allTillDate;
	
}
