package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.RetroListRes;

import lombok.Data;

@Data
public class ShowRetroContractsRes1 {
	@JsonProperty("RetentionPercentage")
	private String retentionPercentage;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("RetroType")
	private String retroType;
	@JsonProperty("RetroDupYerar")
	private String retroDupYerar;
	
	@JsonProperty("RetroDupContract")
	private String retroDupContract;
	
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("UwYear")
	private String uwYear;
	

	



	
	@JsonProperty("IncepDate")
	private String incepDate;


	@JsonProperty("RetroListRes")
	private List<RetroListRes> retroListRes;
}
