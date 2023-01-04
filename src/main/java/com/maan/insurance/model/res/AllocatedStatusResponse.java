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
public class AllocatedStatusResponse {
	@JsonProperty("AllocatedStatusRes")
	private List<AllocatedStatusRes> AllocatedStatusRes;
	
	@JsonProperty("BrokerName")
	private String brokerName;
}
