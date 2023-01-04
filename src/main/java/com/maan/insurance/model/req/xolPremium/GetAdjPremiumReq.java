package com.maan.insurance.model.req.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes1;

import lombok.Data;

@Data
public class GetAdjPremiumReq {

	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("GnpiDate")
	private String gnpiDate;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("Predepartment")
	private String predepartment;

}
