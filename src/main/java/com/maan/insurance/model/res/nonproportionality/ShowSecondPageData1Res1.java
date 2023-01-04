package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes1;

import lombok.Data;

@Data
public class ShowSecondPageData1Res1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("Month")
	private String month;
	
	@JsonProperty("Underwriter")
	private String underwriter;
	
	@JsonProperty("DepartClass")
	private String departClass;
	
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	@JsonProperty("InstalList")
	private List<InstalListRes> instalList;
}
