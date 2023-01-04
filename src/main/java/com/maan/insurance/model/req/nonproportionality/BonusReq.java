package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class BonusReq {
	@JsonProperty("BonusFrom")
	private String bonusFrom;
	@JsonProperty("BonusTo")
	private String bonusTo;
	@JsonProperty("BonusLowClaimBonus")
	private String bonusLowClaimBonus;

	@JsonProperty("BonusSNo")
	private String bonusSNo;
}
