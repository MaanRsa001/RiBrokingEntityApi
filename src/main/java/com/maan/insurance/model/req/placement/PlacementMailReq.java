package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PlacementMailReq {
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("NewStatus")
	private String newStatus; 
	@JsonProperty("CurrentStatus")
	private String currentStatus; 
	@JsonProperty("MailType")
	private String mailType; 
	@JsonProperty("ApproverStatus")
	private String approverStatus; 
	@JsonProperty("ApproverLoginId")
	private String approverLoginId; 
	@JsonProperty("Remarks")
	private String remarks ;
}
