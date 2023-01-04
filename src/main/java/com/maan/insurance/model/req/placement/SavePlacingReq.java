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
	@JsonProperty("PlacementMode")
	private String placementMode; 
	
	@JsonProperty("BouquetModeYN")
	private String bouquetModeYN;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("EProposalNo")
	private String eProposalNo;
	@JsonProperty("ReinsListReq")
	private List<ReinsListReq> reinsListReq;
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("PlacementNo")
	private String placementNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("SectionNo")
	private String sectionNo; 
	
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	@JsonProperty("PlacementamendId")
	private String placementamendId; 
	@JsonProperty("StatusNo")
	private String statusNo; 
	@JsonProperty("UserId")
	private String userId;  
	@JsonProperty("NotplacedProposal")
	private String notplacedProposal; 
	@JsonProperty("PlacedProposal")
	private String placedProposal;  
}
