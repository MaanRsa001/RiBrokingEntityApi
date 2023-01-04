package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShowSecondPageDataRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("SubProfitcenter")
	private String subProfitcenter;
	
	@JsonProperty("CedingCo")
	private String cedingCo;

	@JsonProperty("Month")
	private String month;
	
	@JsonProperty("Underwriter")
	private String underwriter;
	
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	
	@JsonProperty("DepartClass")
	private String departClass;

	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("CeaseStatus")
	private String ceaseStatus;
	
	@JsonProperty("Endttypename")
	private String endttypename;
	
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	
	@JsonProperty("RetroFinalListRes")
	private List<RetroFinalListres> retroFinalListRes;

}
