package com.maan.insurance.model.res.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;

import lombok.Data;

@Data
public class GetBillingInfoListRes1 {
	@JsonProperty("BillingNo")
	private String billingNo;
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("Status")
	private String status; 
	@JsonProperty("BillDate")
	private String billDate;
	@JsonProperty("ReinsurerName")
	private String reinsurerName; 
}
