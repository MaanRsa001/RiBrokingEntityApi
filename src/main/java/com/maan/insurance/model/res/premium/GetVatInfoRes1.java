package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetVatInfoRes1 {
	@JsonProperty("BrokerageAmt")
	private String brokerageAmt;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("BrokerageVat")
	private String brokerageVat;

	@JsonProperty("PremiumVat")
	private String premiumVat;
}
	