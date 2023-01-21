package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetLayerInfoReq {
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("ContractMode")
	private String contractMode;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("ProposalNo")
	private String proposalNo;
}
