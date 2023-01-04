package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetVatInfoReq {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;

	@JsonProperty("PremiumAmount")
	private String premiumAmount;
}
