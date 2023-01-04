package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShowSecondPageDataReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("UwYear")
	private String uwYear;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("RetroFinalListReq")
	private List<RetroFinalListReq> retroFinalListReq;
}
