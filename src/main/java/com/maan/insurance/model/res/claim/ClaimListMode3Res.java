package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public final class ClaimListMode3Res {

	@JsonProperty("DateofLoss")
	private String dateofLoss;
	@JsonProperty("ClaimNo")
	private String claimNo;
	@JsonProperty("LossEstimateOurshareOrigCurr")
	private String lossEstimateOurshareOrigCurr;
	@JsonProperty("Currecny")
	private String currecny;
	@JsonProperty("Date")
	private String date;
	
}
