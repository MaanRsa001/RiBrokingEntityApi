package com.maan.insurance.model.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class AllocateTransactionalRes {
	@JsonProperty("Transactionno")
    private String transactionno;
	@JsonProperty("Contractno")
    private String contractno;
	@JsonProperty("Mode")
    private String mode;
	@JsonProperty("Productname")
    private String productname;
	@JsonProperty("CheckPC")
    private String checkPC;
	@JsonProperty("SubClass")
    private String subClass;
	@JsonProperty("ProposalNo")
    private String proposalNo;
	
	
}
