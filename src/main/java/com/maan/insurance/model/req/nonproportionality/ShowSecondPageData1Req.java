package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes1;

import lombok.Data;

@Data
public class ShowSecondPageData1Req {
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("MdInstalmentNumber")
	private String  mdInstalmentNumber;

}
