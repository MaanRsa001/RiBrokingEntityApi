package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstalmentperiodReq {
	@JsonProperty("InstalmentDateList")
	private String instalmentDateList;
	
	@JsonProperty("InstallmentPremium")
	private String installmentPremium;
	
	@JsonProperty("PaymentDueDays")
	private String paymentDueDays;
	
}
