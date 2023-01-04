package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllocatedListRes1 {
	@JsonProperty("Serialno")
	private String serialno;
	@JsonProperty("Allocateddate")
	private String allocateddate;
	@JsonProperty("Productname")
	private String productname;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Payamount")
	private String payamount;
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	@JsonProperty("Alloccurrencyid")
	private String alloccurrencyid;
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("Payrecno")
	private String payrecno;
	@JsonProperty("SettlementType")
	private String settlementType;
	@JsonProperty("AllocateType")
	private String allocateType;

}
