package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RiskDetailsEditModeReq {

	@JsonProperty("ContractMode")
	private String contractMode;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
//	@JsonProperty("LayerNo")
//	private String layerNo;

//	@JsonProperty("ProposalReference")
//	private String proposalReference;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("ReMode")
	private String reMode;

}
