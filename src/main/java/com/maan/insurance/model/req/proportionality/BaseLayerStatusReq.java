package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiReq;

import lombok.Data;

@Data
public class BaseLayerStatusReq {
	@JsonProperty("productId")
	private String ProductId;
	
	@JsonProperty("branchCode")
	private String BranchCode;
	
	@JsonProperty("proposalNo")
	private String ProposalNo;
}
