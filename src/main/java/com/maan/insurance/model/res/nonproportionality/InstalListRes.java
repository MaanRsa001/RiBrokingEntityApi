package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstalListRes {
	@JsonProperty("Install")
	private String install;
}
