package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTransactionListReq {
	@JsonProperty("BranchCode")
	private String branchCode; 
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("Date")
	private String date;
	@JsonProperty("ProductId")
	private String productId;
	
}
