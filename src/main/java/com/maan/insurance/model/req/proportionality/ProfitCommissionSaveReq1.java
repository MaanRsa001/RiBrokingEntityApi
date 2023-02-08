package com.maan.insurance.model.req.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProfitCommissionSaveReq1 {
	@JsonProperty("CommissionFrom")
	private String commissionFrom;
	@JsonProperty("CommissionTo")
	private String commissionTo;
	@JsonProperty("CommissionPer")
	private String commissionPer;
	@JsonProperty("CommissionSNo")
	private String commissionSNo;
}
