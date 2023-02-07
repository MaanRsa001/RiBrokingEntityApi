package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class insertClassLimitReq {
	@JsonProperty("ContractMode")
	private String contractMode;
	
	@JsonProperty("Productid")
	private String productid;
	
	@JsonProperty("Proposalno")
	private String proposalno;
	
	@JsonProperty("BusinessType")
	private String businessType;
	
	@JsonProperty("CoverList")
	private List<CoverList> CoverList;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
	
	@JsonProperty("CoverLimitAmount")
	private List<CoverLimitAmount> CoverLimitAmount;
	

	
}
