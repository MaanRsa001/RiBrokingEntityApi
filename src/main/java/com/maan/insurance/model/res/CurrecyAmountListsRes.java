package com.maan.insurance.model.res;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrecyAmountListsRes {
	@JsonProperty("CurrecyAmount")
	private String currecyAmount;

}
