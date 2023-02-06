package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RiskDetailsEditModeReq {
	@JsonProperty("ContractMode")
	private Boolean contractMode;
	@JsonProperty("ContractNo")
	private String contNo;
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("SectionMode")
	private String sectionMode;  
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo; 
	@JsonProperty("LayerProposalNo")
	private String layerProposalNo;
	@JsonProperty("Flag")
	private String flag;

}
