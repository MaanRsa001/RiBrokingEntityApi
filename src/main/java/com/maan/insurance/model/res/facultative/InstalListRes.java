package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InstalListRes {
	@JsonProperty("InstalList")
	private String instalList;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("DepartClass")
	private String departClass;
	@JsonProperty("Endttypename")
	private String endttypename;
}
