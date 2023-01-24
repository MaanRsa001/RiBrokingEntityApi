package com.maan.insurance.model.req.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode12Res;

import lombok.Data;

@Data
public class CedentNoListReq {
	@JsonProperty("CedentClaimNo")
	private String cedentClaimNo;
	@JsonProperty("DateOfLoss")
	private String dateOfLoss;
	@JsonProperty("CedingCompanyCode")
	private String cedingCompanyCode;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ClaimNo")
	private String claimNo;
	
}
