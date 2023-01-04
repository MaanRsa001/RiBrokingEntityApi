package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetCopyQuoteReq {
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
}
