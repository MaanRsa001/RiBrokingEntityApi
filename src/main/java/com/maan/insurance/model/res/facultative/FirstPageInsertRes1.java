package com.maan.insurance.model.res.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.BonusRes;

import lombok.Data;

@Data
public class FirstPageInsertRes1 {
	@JsonProperty("Backmode")
	private String backmode;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("RenewalFlag")
	private String renewalFlag;
	@JsonProperty("SaveFlag")
	private String saveFlag;
}
