package com.maan.insurance.model.req.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimPaymentEditReq {
	
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
//	@JsonProperty("DropDown")
//	private String dropDown;
}
