package com.maan.insurance.model.req.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertCrestaMaintableReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("BranchCode")
	private String branchCode;
	

	
	
}
