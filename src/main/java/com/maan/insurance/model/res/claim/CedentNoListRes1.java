package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CedentNoListRes1 {
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("CreatedDate")
	private String createdDate;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("DateOfLoss")
	private String dateOfLoss;
	@JsonProperty("GrosslossFguOc")
	private String grosslossFguOc;
	@JsonProperty("LossEstimateOsOc")
	private String lossEstimateOsOc;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("CedentClaimNo")
	private String cedentClaimNo;

	
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("BrokerName")
	private String brokerName;
	
}
