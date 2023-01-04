package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetNotPlacedProposalListRes1 {
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("CodeDescription")
	private String codeDescription;
	
	@JsonProperty("BaseLayer")
	private String baseLayer;
	
}
