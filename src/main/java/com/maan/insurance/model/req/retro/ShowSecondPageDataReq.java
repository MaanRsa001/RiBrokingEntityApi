package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ShowSecondPageDataReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Proposal")
	private String proposal;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("ReMode")
	private String reMode;
}
