package com.maan.insurance.model.req.upload;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class DoDeleteDocDetailsReq {
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ModuleType")
	private String moduleType;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("DocId")
	private String docId; 
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("OurFileName")
	private String ourFileName; 
	@JsonProperty("TranNo")
	private String tranNo;
}
