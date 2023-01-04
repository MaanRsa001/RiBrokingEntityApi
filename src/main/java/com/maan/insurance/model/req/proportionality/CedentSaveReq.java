package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CedentSaveReq {

	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("Productid")
	private String productid;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("RetentionYN")
	private String retentionYN;
	@JsonProperty("CedentList")
	private List<CedentRetentReq> cedentRetentReq;
	
}
