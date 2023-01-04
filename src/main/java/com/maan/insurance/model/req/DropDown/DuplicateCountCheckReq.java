package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DuplicateCountCheckReq 
{
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;

	@JsonProperty("UwYear")
	private String uwYear;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("Pid")
	private String pid;

}
