package com.maan.insurance.model.res.retroManualAdj;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroDetailsRes1 {
	@JsonProperty("TreatyName")
	private String treatyName;
	@JsonProperty("LeadBroker")
	private String leadBroker;
	@JsonProperty("LeadRetro")
	private String leadRetro;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("CurrId")
	private String currId;
	
}
