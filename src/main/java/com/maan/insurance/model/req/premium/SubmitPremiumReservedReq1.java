package com.maan.insurance.model.req.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SubmitPremiumReservedReq1 {
	@JsonProperty("ClaimPaymentNos")
	private String claimPaymentNos;
	@JsonProperty("CreditAmountCLC")
	private String creditAmountCLC;
	@JsonProperty("CreditAmountCLD")
	private String creditAmountCLD;
	@JsonProperty("CLCsettlementRate")
	private String cLCsettlementRate;
	@JsonProperty("Chkbox")
	private String chkbox;
	@JsonProperty("PayAmountList")
	private String payAmountList;
	@JsonProperty("PrAllocatedList")
	private String prAllocatedList;
}
