package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BonusRes {
	@JsonProperty("BonusSNo")
	private String bonusSNo;
	@JsonProperty("BonusFrom")
	private String bonusFrom;
	@JsonProperty("BonusTo")
	private String bonusTo;
	@JsonProperty("BonusLowClaimBonus")
	private String bonusLowClaim;
}
