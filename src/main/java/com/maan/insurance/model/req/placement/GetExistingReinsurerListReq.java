package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetExistingReinsurerListReq {
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo;
	
	@JsonProperty("EproposalNo")
	private String eproposalNo;
	@JsonProperty("ProposalNo")
	private String proposalNo;

}
