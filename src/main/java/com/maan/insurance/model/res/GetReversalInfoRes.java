package com.maan.insurance.model.res;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReversalInfoRes {

	@JsonProperty("PayRecNo")
	private String payRecNo;
	
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("CurrencyName")
	private String currencyName;
	
	@JsonProperty("Exrate")
	private String exrate;
	
	@JsonProperty("PaymentAmount")
	private String paymentAmount;
	
	@JsonProperty("HideRowCnt")
	private String hideRowCnt;
	
	@JsonProperty("AllocateCurrencyList")
	private List<Map<String,Object>>  allocateCurrencyList;
	
	
}
