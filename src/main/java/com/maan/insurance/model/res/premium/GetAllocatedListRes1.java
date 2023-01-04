package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAllocatedListRes1 {
	@JsonProperty("SerialNo")
	private String serialNo;
	@JsonProperty("AllocatedDate")
	private String allocatedDate;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("PayAmount")
	private String payAmount;
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	@JsonProperty("AlloccurrencyId")
	private String alloccurrencyId;
	@JsonProperty("AllocateType")
	private String allocateType;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("SettlementType")
	private String settlementType;
	@JsonProperty("PayRecNo")
	private String payRecNo;

}
