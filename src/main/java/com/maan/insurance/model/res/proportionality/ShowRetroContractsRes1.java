package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class ShowRetroContractsRes1 {
	
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	
	@JsonProperty("RetroDupContract")
	private String retroDupContract;
	
	@JsonProperty("RetentionPercentage")
	private String retentionPercentage;

	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("RetroList")
	private List<RetroListRes> retroList;

	

	
}
