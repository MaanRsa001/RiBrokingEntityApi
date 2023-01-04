package com.maan.insurance.model.res.claim;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimListMode6Res {
	@JsonProperty("ReviewDoneBy")
	private String reviewDoneBy;
	@JsonProperty("ReviewDate")
	private String reviewDate;
	@JsonProperty("Remarks")
	private String remarks;

	@JsonProperty("SNo")
	private String sNo;
}
