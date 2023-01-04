package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReciptListRes {

	
	@JsonProperty("PayRecNo")
	private String payrecno;
	
	@JsonProperty("CedingCo")
	private String cedingCo;

	@JsonProperty("Broker")
	private String broker;	

	@JsonProperty("Name")
	private String name;

	@JsonProperty("BrokerId")
	private String brokerid;

	@JsonProperty("SerialNo")
	private String serialno;

	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("Trdate")
	private String trdate;

	@JsonProperty("TransactionType")
	private String transactionType;

	@JsonProperty("EditShowStatus")
	private String editShowStatus;

	@JsonProperty("ReversedShowStatus")
	private String reversedShowStatus;

	@JsonProperty("AllocationStatus")
    private String allocationStatus;

	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("RecpayOpenYN")
	private String recpayOpenYN;

	@JsonProperty("Payamount")
	private String payamount;
	
			
	@JsonProperty("PaymentNoSearch")
	private String paymentNoSearch;
	
	@JsonProperty("BrokerNameSearch")
	private String brokerNameSearch;
	
	@JsonProperty("CompanyNameSearch")
	private String companyNameSearch;
	
	@JsonProperty("RemarksSearch")
	private String remarksSearch;	
	
	

}
