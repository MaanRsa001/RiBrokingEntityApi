package com.maan.insurance.model.req.premium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetCashLossCreditReq2 {
	@JsonProperty("CreditAmountCLDlist")
	private String creditAmountCLDlist;
	@JsonProperty("CreditAmountCLClist")
	private String creditAmountCLClist;
	@JsonProperty("CLCsettlementRatelist")
	private String cLCsettlementRatelist;
}
