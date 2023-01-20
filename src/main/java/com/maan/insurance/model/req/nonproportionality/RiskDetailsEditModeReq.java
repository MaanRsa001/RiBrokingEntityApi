package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.RiskDetailsEditModeReq;

import lombok.Data;
@Data
public class RiskDetailsEditModeReq {
	@JsonProperty("ContractMode")
	private Boolean contractMode;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;

	@JsonProperty("ProposalReference")
	private String proposalReference;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("ReMode")
	private String reMode;
	
	@JsonProperty("LayerProposalNo")
	private String layerProposalNo; 
	
	
}
