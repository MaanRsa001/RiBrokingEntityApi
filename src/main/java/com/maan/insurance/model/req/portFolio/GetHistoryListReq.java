package com.maan.insurance.model.req.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetHistoryListReq {

	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Flag")
	private String flag;
	
	@JsonProperty("DeptId")
	private String deptId;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;

}
