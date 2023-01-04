package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetDepartmentDropDownReq 
{

@JsonProperty("BranchCode")
private String branchCode;
	
@JsonProperty("Status")
private String status;


@JsonProperty("ContractNo")
private String contractNo;


@JsonProperty("productCode")
private String productCode;

@JsonProperty("BaseLayer")
private String baseLayer;


@JsonProperty("ProposalNo")
private String ProposalNo;


}
