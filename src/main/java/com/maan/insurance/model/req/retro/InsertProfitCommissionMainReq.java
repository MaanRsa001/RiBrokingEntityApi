package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class InsertProfitCommissionMainReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;
; 
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	
	@JsonProperty("CommissionType")
	private String commissionType; 

	@JsonProperty("ShareProfitCommission")
	private String ShareProfitCommission; 
}
