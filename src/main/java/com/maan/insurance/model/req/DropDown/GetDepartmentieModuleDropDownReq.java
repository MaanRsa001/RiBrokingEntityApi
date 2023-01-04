package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetDepartmentieModuleDropDownReq 
{
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;

	@JsonProperty("TransactionNo")
	private String transactionNo;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("Mode")
	private String mode;

}
