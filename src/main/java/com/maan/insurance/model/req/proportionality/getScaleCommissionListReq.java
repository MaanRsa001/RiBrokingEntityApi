package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class getScaleCommissionListReq {
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("PageFor")
	private String pageFor;
	
	@JsonProperty("ReferenceNo")
	private String referenceNo;

}
