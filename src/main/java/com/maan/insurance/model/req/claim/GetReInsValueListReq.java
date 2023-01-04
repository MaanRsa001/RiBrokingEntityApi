package com.maan.insurance.model.req.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetReInsValueListReq {
	@JsonProperty("ClaimPaymentNoList")
	private String claimPaymentNoList;
	
	@JsonProperty("PaymentCoverPlus")
	private String paymentCoverPlus;
	
	@JsonProperty("ClaimPaidOC")
	private String claimPaidOC;;
}
