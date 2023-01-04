package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MainCreditReq {
	@JsonProperty("MaincreditAmountCLClist")
	private String maincreditAmountCLClist;;
	@JsonProperty("MaincreditAmountCLDlist")
	private String maincreditAmountCLDlist;
	@JsonProperty("MainCLCsettlementRatelist")
	private String mainCLCsettlementRatelist;
	
	@JsonProperty("MainclaimPaymentNos")
	private String mainclaimPaymentNos;
}
