package com.maan.insurance.model.res.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveSecondPageRes1 {
	@JsonProperty("ContractGendration")
	private String contractGendration;
	@JsonProperty("ProStatus")
	private String proStatus;
	@JsonProperty("SharSign")
	private String sharSign;
	@JsonProperty("ContractNo")
	private String ContractNo;
}
