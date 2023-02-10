package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SavePlacingReq {
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
	@JsonProperty("EproposalNo")
	private String eproposalNo;
	@JsonProperty("UserId")
	private String userId;
	@JsonProperty("PlacementMode")
	private String placementMode;
	@JsonProperty("NotplacedProposal")
	private String NotplacedProposal;
	@JsonProperty("PlacedProposal")
	private String placedProposal;
	
	@JsonProperty("BouquetModeYN")
	private String bouquetModeYN;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ReinsListReq")
	private List<ReinsListReq> reinsListReq;
 
}
