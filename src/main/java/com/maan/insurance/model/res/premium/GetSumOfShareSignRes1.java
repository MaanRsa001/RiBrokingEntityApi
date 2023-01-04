package com.maan.insurance.model.res.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetSumOfShareSignRes1 {
	@JsonProperty("SumShareSigned")
	private String sumShareSigned;
}
