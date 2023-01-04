package com.maan.insurance.model.res.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;

import lombok.Data;

@Data
public class SlideScenarioRes1 {
	@JsonProperty("SlideScenario")
	private String slideScenario;
	@JsonProperty("SlidedepartId")
	private String slidedepartId;
	@JsonProperty("PremiumScenario")
	private String premiumScenario;
	
	@JsonProperty("PremiumdepartId")
	private String premiumdepartId;
	@JsonProperty("LossScenario")
	private String lossScenario;
	@JsonProperty("LossdepartId")
	private String lossdepartId;
	@JsonProperty("BonusStatus")
	private String bonusStatus;
}
