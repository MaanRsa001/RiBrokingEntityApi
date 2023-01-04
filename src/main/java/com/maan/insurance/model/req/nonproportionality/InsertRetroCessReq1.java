package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertRetroCessReq1 {
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	
	@JsonProperty("RetroBroker")
	private String retroBroker;
	
	@JsonProperty("ShareAccepted")
	private String shareAccepted;
	
	@JsonProperty("ShareSigned")
	private String shareSigned;
	
	@JsonProperty("ProposalStatus")
	private String proposalStatus;
	
	@JsonProperty("Commission")
	private String commission;
}

