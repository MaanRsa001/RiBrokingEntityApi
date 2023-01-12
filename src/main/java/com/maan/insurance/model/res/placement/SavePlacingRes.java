package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;
@Data
public class SavePlacingRes {
	@JsonProperty("EproposalNo")
	private String eproposalNo;
}
