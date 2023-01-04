package com.maan.insurance.model.res.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SecondPageInsertRes1 {
	@JsonProperty("ProStatus")
	private String proStatus;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("Backmode")
	private String backmode;
	
//	@JsonProperty("PmlOurShare")
//	private String pmlOurShare;
//	
//	@JsonProperty("TplOurShare")
//	private String tplOurShare;
//	
//	@JsonProperty("RiskGrade")
//	private String riskGrade;
	
}
