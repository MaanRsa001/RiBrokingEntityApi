package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ShowSecondPageData1Res1 {
	
	@JsonProperty("SubProfitcenter")
	private String subProfitcenter; 
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("Month")
	private String month;
	
	@JsonProperty("Underwriter")
	private String underwriter;
	
	@JsonProperty("PolBr")
	private String polBr;
	
	@JsonProperty("Proposalno")
	private String proposalno;
	
	@JsonProperty("DepartClass")
	private String departClass;
	
	@JsonProperty("RetroFinalListRes")
	private List<RetroFinalListres> retroFinalListres;

}
