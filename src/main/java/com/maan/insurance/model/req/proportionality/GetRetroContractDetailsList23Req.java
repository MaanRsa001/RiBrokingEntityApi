package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroContractDetailsList23Req {
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
}
