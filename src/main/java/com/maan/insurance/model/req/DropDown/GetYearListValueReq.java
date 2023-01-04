package com.maan.insurance.model.req.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetYearListValueReq {
	@JsonProperty("InceptionDate")
	private String inceptionDate;

}
