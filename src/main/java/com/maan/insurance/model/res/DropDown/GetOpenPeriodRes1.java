package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetOpenPeriodRes1 {
	
	@JsonProperty("OpendDate")
	private String opendDate;
	@JsonProperty("OpstartDate")
	private String opstartDate;
	
}
