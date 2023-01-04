package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertRetroCessReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("NoRetroCess")
	private String noRetroCess;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("InsertRetroCessReq1")
	private List<InsertRetroCessReq1> insertRetroCessReq1;
	
	
}
