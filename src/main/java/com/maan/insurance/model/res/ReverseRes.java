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
public class ReverseRes {

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
