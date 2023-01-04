package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetCedingCompanyRes1 {
	@JsonProperty("CustomerId")
	private String customerId;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
}
