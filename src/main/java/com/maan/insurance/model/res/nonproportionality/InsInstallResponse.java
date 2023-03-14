package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsInstallResponse {
	@JsonProperty("InsInstallRes1")
	private List<InsInstallRes1> insInstallResList;
	
	@JsonProperty("InsInstallRes1")
	private List<String> installmentPremium;
}
