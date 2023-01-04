package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetPlacementInfoListReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("EproposalNo")
	private String eproposalNo;
	
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
	
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("SearchReinsurerId")
	private String searchReinsurerId;
	
	@JsonProperty("SearchType")
	private String searchType; 
	@JsonProperty("SearchBrokerId") 
	private String searchBrokerId; 
	@JsonProperty("SearchStatus") 
	private String searchStatus; 
}
