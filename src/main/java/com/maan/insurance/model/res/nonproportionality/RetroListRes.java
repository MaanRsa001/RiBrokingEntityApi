package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;


import lombok.Data;

@Data
public class RetroListRes {
	@JsonProperty("RetroList")
	private String retroList;
	@JsonProperty("RetroPercentage")
	private String retroPercentage;
	@JsonProperty("UWYear")
	private String uWYear;
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	@JsonProperty("RetroUWYear")
	private List<CommonResDropDown> retroUWYear;
	
	@JsonProperty("RetroFinalList")
	private List<GetRetroContractDetailsListRes1> retroFinalList;
}
