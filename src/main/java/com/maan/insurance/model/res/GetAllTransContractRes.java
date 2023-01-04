package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllTransContractRes {

	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("Mode")
	private String mode;

	@JsonProperty("ProductName")
	private String productName;

	@JsonProperty("Transactionno")
	private String Transactionno;

	@JsonProperty("Inceptiobdate")
	private String Inceptiobdate;

	@JsonProperty("Netdue")
	private String Netdue;

	@JsonProperty("Payamount")
	private String Payamount;

	@JsonProperty("AccPremium")
	private String AccPremium;

	@JsonProperty("AccClaim")
	private String AccClaim;
	
	@JsonProperty("CheckYN")
	private String CheckYN;
	
	@JsonProperty("CheckPC")
	private String CheckPC;
	
	@JsonProperty("SubClass")
	private String subClass;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
}
