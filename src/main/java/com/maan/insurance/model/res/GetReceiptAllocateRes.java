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
public class GetReceiptAllocateRes {

	@JsonProperty("SerialNo")
	private String serialNo;
	
	@JsonProperty("Paymentamount")
	private String paymentamount;
	
	@JsonProperty("Currency")
	private String currency;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("CedingId")
	private String cedingId;
	
	@JsonProperty("BrokerId")
	private String brokerId;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("AllocateCurrencyList")
	private List<Map<String,Object>> allocateCurrencyList;
	
	@JsonProperty("Broker")
	private String broker;

}
