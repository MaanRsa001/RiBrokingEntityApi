package com.maan.insurance.model.res.claim;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPaymentNoListRes1 {
	@JsonProperty("ClaimPaymentNo")
	private String claimPaymentNo;
}
