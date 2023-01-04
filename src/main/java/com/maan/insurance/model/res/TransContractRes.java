package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TransContractRes {
	@JsonProperty("Contractno")
	private String Contractno;
	@JsonProperty("Mode")
	private String mode;
	@JsonProperty("Productname")
	private String productname;
	@JsonProperty("Transactionno")
	private String transactionno;
	@JsonProperty("Inceptiobdate")
	private String inceptiobdate;
	@JsonProperty("Netdue")
	private String netdue;
	@JsonProperty("Payamount")
	private String payamount;
	@JsonProperty("AccPremium")
	private String accPremium;
	@JsonProperty("AccClaim")
	private String accClaim;
	@JsonProperty("CheckYN")
	private String checkYN;
	@JsonProperty("CheckPC")
	private String checkPC;
	@JsonProperty("SubClass")
	private String subClass;
	@JsonProperty("ProposalNo")
	private String proposalNo;

}
