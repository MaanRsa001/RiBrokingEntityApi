package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LowClaimBonusListReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("AcqBonus")
	private String acqBonus;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ReferenceNo")
	private String referenceNo; 
}
