package com.maan.insurance.model.req.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditBillingInfoReq {
	@JsonProperty("BillingNo")
	private String billingNo;
}
