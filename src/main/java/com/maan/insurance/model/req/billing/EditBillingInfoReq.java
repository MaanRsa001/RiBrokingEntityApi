package com.maan.insurance.model.req.billing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.GetTransContractListReq;

import lombok.Data;

@Data
public class EditBillingInfoReq {
	@JsonProperty("BillingNo")
	private String billingNo;
}
