package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ViewRiskDetailsReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("SharSign")
	private String sharSign;
	@JsonProperty("ProductId")
	private String productId;
}
