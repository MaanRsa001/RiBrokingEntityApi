package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShowLayerBrokerageRes1 {
	@JsonProperty("Brokerage")
	private String brokerage;
	
	@JsonProperty("Tax")
	private String tax;
}
