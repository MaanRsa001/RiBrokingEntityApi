package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsResponse {
	@JsonProperty("RiskDetails")
	private ContractDetailsRes1 commonResponse1;
	
	@JsonProperty("CommissionDetails")
	private List<ContractDetailsRes2> commonResponse2;
}
