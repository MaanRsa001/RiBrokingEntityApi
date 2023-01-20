package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpdateOfferNoReq {
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("OfferNo")
	private String offerNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
}
