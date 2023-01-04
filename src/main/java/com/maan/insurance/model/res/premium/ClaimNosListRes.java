package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimNosListRes {
	@JsonProperty("ClaimNo")
	private String claimNo;
}
