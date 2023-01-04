package com.maan.insurance.model.req.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetDocListReq {
	@JsonProperty("Type")
	private String type;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ModuleType")
	private String moduleType;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("TranNo")
	private String tranNo; 
	@JsonProperty("ProductId")
	private String productId;
}
