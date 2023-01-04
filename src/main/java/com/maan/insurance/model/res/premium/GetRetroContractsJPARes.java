package com.maan.insurance.model.res.premium;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetRetroContractsJPARes {
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("RetroPercentage")
	private BigDecimal retroPercentage;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("Uwyear")
	private String uwyear;
	
	@JsonProperty("RetroType")
	private String retroType;
	

	
}
