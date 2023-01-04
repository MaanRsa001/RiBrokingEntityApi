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
public class ReverseViewRes {
	
	@JsonProperty("Allocateddate")
	private String allocateddate;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("ReversalDate")
	private String reversalDate;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("ReversedAmount")
	private String reversedAmount;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("AllTillDate")
	private String allTillDate;

}
