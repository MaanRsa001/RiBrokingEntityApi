package com.maan.insurance.model.res.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShowSecondPageData1Res1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("Month")
	private String month;
	
	@JsonProperty("Underwriter")
	private String underwriter;

	
	@JsonProperty("PolicyBranch")
	private String policyBranch;
}
