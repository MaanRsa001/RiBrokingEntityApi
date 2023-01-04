package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.premium.GetAllocatedCassLossCreditRes;
import com.maan.insurance.model.res.premium.GetAllocatedCassLossCreditRes1;

import lombok.Data;

@Data
public class SubmitPremiumReservedReq {
	@JsonProperty("ReqList")
	private List<SubmitPremiumReservedReq1> reqList;
	
	@JsonProperty("PremiumReservedReq")
	private GetPremiumReservedReq premiumReservedReq;
}
