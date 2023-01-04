package com.maan.insurance.model.res.facPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facPremium.GetPremiumDetailsReq;

import lombok.Data;

@Data
public class SettlementstatusRes {
	@JsonProperty("Settlementstatus")
	private String settlementstatus;
}
