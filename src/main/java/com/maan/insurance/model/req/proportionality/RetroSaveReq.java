package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RetroSaveReq {

	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("NoInsurer")
	private String noInsurer;
	@JsonProperty("RetentionPercentage")
	private String retentionPercentage;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("RetroDupContract")
	private String retroDupContract;
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	@JsonProperty("RetroType")
	private String retroType;
	@JsonProperty("RetroDetail")
	private List<RetroDetailReq> retroDetailReq;
	
	
	
	
	
	

}
