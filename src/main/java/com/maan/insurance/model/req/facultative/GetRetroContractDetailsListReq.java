package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroContractDetailsListReq {
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("Flag")
	private String  flag;
	
	@JsonProperty("Year")
	private String  year;
}
