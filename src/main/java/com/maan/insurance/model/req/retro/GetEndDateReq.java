package com.maan.insurance.model.req.retro;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetEndDateReq {
	@JsonProperty("EndDate")
	private String endDate;
	
}
