package com.maan.insurance.model.res.adjustment;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.retro.GetEndDateReq;

import lombok.Data;

@Data
public class GetAdjustmentDoneListRes1 {
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("CedingName")
	private String cedingName;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("CurrencyName")
	private String currencyName;
	@JsonProperty("AdjustType")
	private String adjustType;
	@JsonProperty("Payamount")
	private String payamount;
	@JsonProperty("Status")
	private String status;

}
