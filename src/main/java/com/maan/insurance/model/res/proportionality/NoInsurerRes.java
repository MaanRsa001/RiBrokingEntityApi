package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;

import lombok.Data;

@Data
public class NoInsurerRes {
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	@JsonProperty("Productid")
	private String productid;

	@JsonProperty("RetroUwyear")
	private List<CommonResDropDown> retroUwyear;
}
