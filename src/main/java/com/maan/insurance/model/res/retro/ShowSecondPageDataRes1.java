package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.RetroCessListRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;

import lombok.Data;

@Data
public class ShowSecondPageDataRes1 {
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
	
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("Endttypename")
	private String endttypename;
	
	@JsonProperty("ReinstNo")
	private String reinstNo;
	
	@JsonProperty("ReinstAditionalPremiumpercent")
	private String reinstAditionalPremiumpercent;
	
	@JsonProperty("BurningCost")
	private String burningCost;
	
	@JsonProperty("Remarks")
	private String remarks;
}
