package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsertCoverDeductableDetailsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("Type")
	private String type;

//	@JsonProperty("TotalCoverage")
//	private String totalCoverage;
	
	@JsonProperty("CoverSNoReq")
	private List<CoverSNoReq> coverSNoReq;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("Loginid")
	private String loginid;
	
	@JsonProperty("BranchCode")
	private String branchCode;
}
