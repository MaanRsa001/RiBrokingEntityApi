package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class claimNoListReq {

	@JsonProperty("CedentClaimNo")
	private String cedentClaimNo;
	
	@JsonProperty("DateofLoss")
	private String dateofLoss;
	
	@JsonProperty("CedingCompanyCode")
	private String cedingCompanyCode;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ClaimNo")
	private String claimNo;
}
