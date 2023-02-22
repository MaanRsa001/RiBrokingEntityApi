package com.maan.insurance.model.req.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdatePlacementReq {
	@JsonProperty("StatusNo")
	private String statusNo; 
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("StatUpdatePlacementListRequsNo")
	private List<UpdatePlacementListReq> placementListReq; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("EproposalNo")
	private String eproposalNo; 
	@JsonProperty("NewStatus")
	private String newStatus;
	@JsonProperty("PlacementamendId")
	private String placementamendId; 
	@JsonProperty("UserId")
	private String userId; 
	@JsonProperty("CorresId")
	private String corresId; 
	@JsonProperty("EmailBy")
	private String emailBy; 
	@JsonProperty("CurrentStatus")
	private String currentStatus; 
	@JsonProperty("CedentCorrespondent")
	private String cedentCorrespondent; 
	@JsonProperty("ReinsurerCorrespondent")
	private String reinsurerCorrespondent; 
	
	@JsonProperty("TqrCorrespondent")
	private String tqrCorrespondent; 
	@JsonProperty("UpdateDate")
	private String updateDate; 
	@JsonProperty("Status")
	private String status; 
	@JsonProperty("ApproverStatus")
	private String approverStatus;
}
