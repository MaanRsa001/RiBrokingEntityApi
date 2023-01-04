package com.maan.insurance.model.res.portFolio;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ButtonSelectionListRes {
	@JsonProperty("Type")
	private String type;
	@JsonProperty("DetailName")
	private String detailName;
}
