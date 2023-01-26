package com.maan.insurance.model.req.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetBillingInfoListReq {
	@JsonProperty("BillingType")
	private String transType;
	@JsonProperty("BranchCode")
	private String branchCode; 
}
