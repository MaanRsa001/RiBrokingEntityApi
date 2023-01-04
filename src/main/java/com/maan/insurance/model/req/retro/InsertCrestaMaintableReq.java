package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertCrestaMaintableReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("BranchCode")
	private String branchCode;
}
