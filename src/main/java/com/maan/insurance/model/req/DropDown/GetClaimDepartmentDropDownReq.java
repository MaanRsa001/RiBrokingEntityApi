package com.maan.insurance.model.req.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetClaimDepartmentDropDownReq 
{
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("DepartmentId")
	private String departmentId;

	@JsonProperty("ContractNo")
	private String contractNo;

	@JsonProperty("LayerNo")
	private String layerNo;

	@JsonProperty("Status")
	private String status;
	
	
	@JsonProperty("ProposalNo")
	private String proposalNo;


}
