package com.maan.insurance.model.res.proportionality;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.GetRetroContractDetailsListReq;

import lombok.Data;

@Data
public class GetRetroContractDetailsListRes1 {
	@JsonProperty("UwYear1")
	private String uwYear1;
	
	@JsonProperty("UwYear2")
	private String uwYear2;
}
