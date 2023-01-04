package com.maan.insurance.model.req.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.facultative.GetLossDEtailsRes1;

import lombok.Data;

@Data
public class GetRetroContractDetailsReq {
	@JsonProperty("DropDown")
	private String dropDown;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Year")
	private String year;
	
	@JsonProperty("RetroDupMode")
	private String retroDupMode;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	
}
