package com.maan.insurance.model.res.reports;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;
@Data
public class GetClaimJournelInitRes1 {
	@JsonProperty("Sno")
	private String sno;
	@JsonProperty("AccountDate")
	private String accountDate;
	@JsonProperty("MovementType")
	private String movementType;
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	@JsonProperty("Accper")
	private String accper;
}
