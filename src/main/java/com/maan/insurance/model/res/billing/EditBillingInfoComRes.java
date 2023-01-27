package com.maan.insurance.model.res.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EditBillingInfoComRes {
	@JsonProperty("BillingInfo")
	private EditBillingInfoRes1 billingInfo;
	
	@JsonProperty("BillingTransaction")
	private List<EditBillingTransactionRes> billingTransaction;
}
