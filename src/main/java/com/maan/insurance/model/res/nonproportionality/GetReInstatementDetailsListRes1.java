package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetReInstatementDetailsListRes1 {

	
	@JsonProperty("BusinessType")
	private String businessType;
	
	@JsonProperty("CoverList")
	private List<CoverListInstate> CoverList;
	
	@JsonProperty("CoverLimitAmount")
	private List<CoverLimitAmountInstate> coverLimitAmount;
	
	@JsonProperty("ReInStatementRes")
	private List<ReInStatementRes> reInStatementRes;
	
	@JsonProperty("ReinstatementOption")
	private String reinstatementOption;

	
	
}
