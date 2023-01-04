package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertBonusDetailsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("LayerNo")
	private String layerNo; 
	
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("Type")
	private String type; 
	@JsonProperty("PageFor")
	private String pageFor;
	@JsonProperty("SlideScaleCommission")
	private String slideScaleCommission; 
	@JsonProperty("LossParticipants")
	private String lossParticipants; 
}
