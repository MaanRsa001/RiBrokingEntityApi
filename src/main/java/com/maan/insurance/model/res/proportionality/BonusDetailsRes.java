package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BonusDetailsRes {
	@JsonProperty("BonusSno")
	private String bonusSno;
	@JsonProperty("BonusFrom")
	private String bonusFrom;
	@JsonProperty("BonusTo")
	private String bonusTo;
	@JsonProperty("BonusLowClaimBonus")
	private String bonusLowClaimBonus;
}
