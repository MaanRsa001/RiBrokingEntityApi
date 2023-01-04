package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facultative.CoverLimitOCReq;
import com.maan.insurance.model.req.facultative.FirstPageInsertReq;

import lombok.Data;

@Data
public class GetRetroContractDetailsListRes1 {
	@JsonProperty("CONTDET1")
	private String cONTDET1;
	
	@JsonProperty("CONTDET2")
	private String cONTDET2;
}
