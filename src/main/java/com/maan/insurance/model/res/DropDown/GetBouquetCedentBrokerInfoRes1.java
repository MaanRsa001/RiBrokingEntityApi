package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetBouquetCedentBrokerInfoRes1 {
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	
	@JsonProperty("BrokerName")
	private String brokerName;
	
	@JsonProperty("UwYear")
	private String uwYear;
	
	@JsonProperty("UwYearTo")
	private String uwYearTo;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("ExpDate")
	private String expDate;
}
