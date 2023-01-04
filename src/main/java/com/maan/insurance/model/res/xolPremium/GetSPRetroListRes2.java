package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.InstalListRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res1;

import lombok.Data;

@Data
public class GetSPRetroListRes2 {

	@JsonProperty("Spretro")
	private String spretro;
	
	@JsonProperty("NoofInsurers")
	private String noofinsurers;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
}
