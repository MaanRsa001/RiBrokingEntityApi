package com.maan.insurance.model.res.retro;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class FirstInsertRes1 {
	@JsonProperty("SaveFlag")
	private String saveFlag;
	@JsonProperty("ContractGendration")
	private String contractGendration;
	@JsonProperty("RenewalFlag")
	private String renewalFlag;
}
