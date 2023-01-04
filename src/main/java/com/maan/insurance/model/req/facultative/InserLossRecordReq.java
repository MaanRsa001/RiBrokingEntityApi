package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InserLossRecordReq {
	@JsonProperty("ProposalNo")
	private String ProposalNo;

	
	@JsonProperty("LossDetails")
	private List<LossDetailsReq> lossDetails;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	
}
