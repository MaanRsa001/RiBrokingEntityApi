package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPlacingInfoReq {
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
	@JsonProperty("EproposalNo")
	private String eproposalNo;
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("SearchType")
	private String searchType;
	@JsonProperty("SearchReinsurerId")
	private String searchReinsurerId;
	@JsonProperty("SearchBrokerId")
	private String searchBrokerId; 
	@JsonProperty("SearchStatus")
	private String searchStatus; 
}
