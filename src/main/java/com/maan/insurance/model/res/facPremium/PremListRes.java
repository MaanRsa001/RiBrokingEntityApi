package com.maan.insurance.model.res.facPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremListRes {
	@JsonProperty("QuotaShare")
	private String quotaShare;
	@JsonProperty("Mdpremium")
	private String mdpremium;
	@JsonProperty("AdjustmentPremium")
	private String adjustmentPremium;
	@JsonProperty("Comqs")
	private String comqs;
	@JsonProperty("CurId")
	private String curId;
	
	@JsonProperty("PrecurId")
	private String precurId;
	
	
	@JsonProperty("Total")
	private String total;
}
