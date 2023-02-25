package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsInstallRes1 {
	@JsonProperty("InstalmentDateList")
	private String instalmentDateList; 
//	@JsonProperty("InstallmentPremium")
//	private String installmentPremium; 
	@JsonProperty("PaymentDueDays")
	private String paymentDueDays ;
	@JsonProperty("InstalList")
	private String instalList ;
}
