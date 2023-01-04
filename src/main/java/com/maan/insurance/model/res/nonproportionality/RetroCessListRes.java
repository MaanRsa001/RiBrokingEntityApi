package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroCessListRes {
	
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	
	@JsonProperty("RetroBroker")
	private String retroBroker;
	
	@JsonProperty("ProposalStatus")
	private String proposalStatus;
	
	@JsonProperty("ShareAccepted")
	private String shareAccepted;
	
	@JsonProperty("ShareSigned")
	private String shareSigned;
	
	@JsonProperty("Commission")
	private String commission;
}
