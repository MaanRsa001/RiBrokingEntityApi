package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetProposalNoReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("DepartId")
	private String departId;
	
}
