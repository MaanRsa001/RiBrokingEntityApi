package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getReInstatementDetailsListReq {
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ReferenceNo")
	private String referenceNo;
	
	

}
