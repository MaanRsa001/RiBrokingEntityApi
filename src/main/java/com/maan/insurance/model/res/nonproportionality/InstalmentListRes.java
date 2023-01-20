package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.Data;

@Data
public class InstalmentListRes {
	@JsonProperty("InstalmentDateList")
	private String instalmentDateList;
	@JsonProperty("PaymentDueDays")
	private String paymentDueDays; 
	@JsonProperty("InstalList")
	private String instalList;
}
