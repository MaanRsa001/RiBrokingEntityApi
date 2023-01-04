package com.maan.insurance.model.res.reports;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetBookedPremiumInitRes1 {
	@JsonProperty("Sno")
	private String sno;
	@JsonProperty("ReportDate")
	private String reportDate;
	@JsonProperty("UWYear")
	private String uWYear;
	@JsonProperty("MovementType")
	private String movementType;
}
