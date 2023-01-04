package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;
@Data
public class GetRetroContractDetailsListReq {
	@JsonProperty("Productid")
	private String productid;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("UwYear")
	private String  uwYear;
	
	@JsonProperty("Flag")
	private int  flag;
}
