package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class showSecondPageData1Req {
	
	@JsonProperty("Proposal")
	private String proposal;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("PercentRetro")
	private String percentRetro;
	
	@JsonProperty("RetroYear")
	private String retroYear;
	
	@JsonProperty("RetroDupContract")
	private String retroDupContract;
	
	@JsonProperty("RetroFinalList")
	private String retroFinalList;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("UwYear")
	private String uwYear;
		
}
