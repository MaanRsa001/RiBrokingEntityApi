package com.maan.insurance.model.req.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;

import lombok.Data;

@Data
public class GetBillingInfoListReq {
	@JsonProperty("TransType")
	private String transType;
	@JsonProperty("BranchCode")
	private String branchCode; 
}
