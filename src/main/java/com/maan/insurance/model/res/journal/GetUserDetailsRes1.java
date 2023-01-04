package com.maan.insurance.model.res.journal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.PremiumEditRes;
import com.maan.insurance.model.res.premium.PremiumEditRes1;

import lombok.Data;

@Data
public class GetUserDetailsRes1 {
	@JsonProperty("Result")
	private String result;
	@JsonProperty("LoginIdList")
	private String loginIdList;
}
