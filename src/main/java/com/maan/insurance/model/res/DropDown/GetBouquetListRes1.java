package com.maan.insurance.model.res.DropDown;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetBouquetListRes1 {
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("CodeDescription")
	private String codeDescription;
}
