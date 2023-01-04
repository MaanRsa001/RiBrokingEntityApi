package com.maan.insurance.model.res.adjustment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetAdjustmentPayRecListRes1 {

	@JsonProperty("TransactionNo")
	private String 	transactionNo;
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("CedingName")
	private String cedingName;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("PendingAmount")
	private String 	pendingAmount;
	
	@JsonProperty("AdjustAmount")
	private String adjustAmount;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("ProductName")
	private String productName;
	@JsonProperty("SubClass")
	private String subClass;
	@JsonProperty("ProposlaNo")
	private String 	proposlaNo;
	@JsonProperty("AdjustType")
	private String 	adjustType;
	@JsonProperty("Check")
	private String 	check;

}
