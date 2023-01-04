package com.maan.insurance.model.req.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertBonusDetailsReq {
	@JsonProperty("ProposalNo")
	private String ProposalNo;

	@JsonProperty("AcqBonus")
	private String acqBonus;
	
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("Loginid")
	private String loginid;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
}
