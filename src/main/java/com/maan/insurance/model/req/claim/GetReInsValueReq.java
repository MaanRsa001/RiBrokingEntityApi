package com.maan.insurance.model.req.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetReInsValueReq {
	@JsonProperty ("GetReInsValueListReq")
	private List<GetReInsValueListReq> getReInsValueListReq;
	
	
	
	
	@JsonProperty("PolicyContractNo")
	private String policyContractNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("TotalBookedPremium")
	private String totalBookedPremium;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String ProposalNo;
	
	
}
