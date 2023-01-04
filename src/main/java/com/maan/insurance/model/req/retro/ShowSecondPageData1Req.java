package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.retro.ShowSecondPageData1Res1;

import lombok.Data;

@Data
public class ShowSecondPageData1Req {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Proposal")
	private String proposal;
	
	@JsonProperty("ProductId")
	private String productId;
}
