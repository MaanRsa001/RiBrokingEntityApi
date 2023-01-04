package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroContractsRes1 {
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("RetroPercentage")
	private String retroPercentage;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("RetroType")
	private String retroType;
	@JsonProperty("Uwyear")
	private String uwyear;
}
