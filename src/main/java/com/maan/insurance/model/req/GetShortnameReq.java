package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.GetShortnameRes;

import lombok.Data;

@Data
public class GetShortnameReq {
	@JsonProperty("BranchCode")
	private String branchCode;
}
