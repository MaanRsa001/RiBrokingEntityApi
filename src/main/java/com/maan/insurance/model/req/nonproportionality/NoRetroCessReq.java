package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.RetroListReq;

import lombok.Data;

@Data
public class NoRetroCessReq {
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
