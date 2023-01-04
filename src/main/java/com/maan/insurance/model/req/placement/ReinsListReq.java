package com.maan.insurance.model.req.placement;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ReinsListReq {
	@JsonProperty("ReinsureName")
	private String reinsureName;
	@JsonProperty("PlacingBroker")
	private String placingBroker;
	@JsonProperty("ReinsSNo")
	private String reinsSNo;
	@JsonProperty("ShareOffer")
	private String shareOffer;
	
}
