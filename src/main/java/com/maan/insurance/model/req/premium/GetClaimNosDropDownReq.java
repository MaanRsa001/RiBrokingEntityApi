package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.GetPreListRes1;

import lombok.Data;

@Data
public class GetClaimNosDropDownReq {
	@JsonProperty("ContractLayerNo")
	private String contractLayerNo;
}
