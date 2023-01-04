package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.InstalListRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res1;

import lombok.Data;

@Data
public class GetRetroContracts1 {

	@JsonProperty("ContractNo")
	private String contractno;
	
	@JsonProperty("RetroPercentage")
	private String retropercentage;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("UWYear")
	private String uwyear;
	
	@JsonProperty("RetroType")
	private String retrotype;
	


}
