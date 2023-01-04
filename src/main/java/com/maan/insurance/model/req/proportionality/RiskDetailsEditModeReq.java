package com.maan.insurance.model.req.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.FirstpagesaveRes;

import lombok.Data;

@Data
public class RiskDetailsEditModeReq {
	@JsonProperty("ContractMode")
	private Boolean contractMode;
	
	@JsonProperty("ContractNo")
	private String contNo;
	@JsonProperty("ProposalNo")
	private String proposalNo;

}
