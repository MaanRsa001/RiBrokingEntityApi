package com.maan.insurance.model.res.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;

import lombok.Data;

@Data
public class GetClassLimitDetailsRes2 { 


	@JsonProperty("DeductableLimitAmount")
	private String deductableLimitAmount;
}
