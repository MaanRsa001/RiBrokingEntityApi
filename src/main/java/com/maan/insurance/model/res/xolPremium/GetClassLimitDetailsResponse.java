package com.maan.insurance.model.res.xolPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes1;

import lombok.Data;

@Data
public class GetClassLimitDetailsResponse {
	@JsonProperty("ClassLimitDetails")
	private List<GetClassLimitDetailsRes1> classLimitDetails;
	@JsonProperty("Deducttotal")
	private List<GetClassLimitDetailsRes2> deducttotal;
}
