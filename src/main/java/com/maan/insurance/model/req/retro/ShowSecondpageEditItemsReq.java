package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ShowSecondpageEditItemsReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("LayerNo")
	private String layerNo;
//	@JsonProperty("SaveFlag")
//	private String saveFlag;
}
