package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetLowClaimBonusListRes1 {
	@JsonProperty("BonusTypeId")
	private String bonusTypeId;
	
	@JsonProperty("BonusRes")
	private List<BonusRes> bonusRes;
}
