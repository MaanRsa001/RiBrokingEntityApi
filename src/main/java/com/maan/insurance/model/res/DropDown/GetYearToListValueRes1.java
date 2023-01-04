package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetYearToListValueRes1 { 
	@JsonProperty("Year")
	private String year;
}
