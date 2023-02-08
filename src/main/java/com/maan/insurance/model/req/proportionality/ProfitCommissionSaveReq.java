package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProfitCommissionSaveReq {

	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ShareProfitCommission")
	private String shareProfitCommission;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Productid")
	private String productid;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("CommissionType")
	private String commissionType;
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("ReferenceNo")
	private String referenceNo;
	@JsonProperty("CommissionReq")
	private List<ProfitCommissionSaveReq1> commissionReq;
	
	
	
	
	
	
	

}
