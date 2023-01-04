package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class getprofitCommissionEditRes1 {
	
	@JsonProperty("ProfitSno")
	private String profitSno;
	
	@JsonProperty("ManagementExpenses")
	private String managementExpenses;
	
	@JsonProperty("LossCF")
	private String lossCF;
	
	@JsonProperty("Fistpc")
	private String fistpc;
	
	@JsonProperty("ProfitMont")
	private String profitMont;
	
	@JsonProperty("ProfitQuarters")
	private String profitQuarters;
	
	@JsonProperty("ProfitYear")
	private String profitYear;
	
	@JsonProperty("ProfitCommission")
	private String profitCommission;

}
