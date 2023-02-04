package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.placement.EditPlacingDetailsRes1;

import lombok.Data;

@Data
public class AttachFileReq {
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("UserId")
	private String userId; 
	@JsonProperty("PlacementMode")
	private String placementMode;  
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
	@JsonProperty("InsertDocdetailsReq")
	private List<InsertDocdetailsReq> insertDocdetailsReq; 
}
