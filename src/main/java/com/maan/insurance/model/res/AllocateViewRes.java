package com.maan.insurance.model.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllocateViewRes {
	
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("Flag")
	private String flag1;
	

	
	@JsonProperty("AllocatedDate")
	private String allocatedDate;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ProductName")
	private String productName;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("CurrencyValue")
	private String currencyValue;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("PayAmount")
	private String payAmount;
	
	@JsonProperty("CheckPc")
	private String checkPc;
	
	@JsonProperty("AdjustmentType")
	private String adjustmentType;
	
	@JsonProperty("CurrencyName")
	private String currencyName;
	

	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	
	@JsonProperty("AlloccurrencyId")
	private String alloccurrencyId;

}
