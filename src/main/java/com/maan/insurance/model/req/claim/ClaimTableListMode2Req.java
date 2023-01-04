package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ClaimTableListMode2Req {

	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("ContractNo")
	private String contractNo;
}
