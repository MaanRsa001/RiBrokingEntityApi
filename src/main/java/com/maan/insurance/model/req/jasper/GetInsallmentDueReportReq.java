package com.maan.insurance.model.req.jasper;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class GetInsallmentDueReportReq {
	@JsonProperty("ProductId")
	public String productId;
	
	@JsonProperty("EndDate")
	public String endDate;
	
	@JsonProperty("BrokerId")
	public String brokerId;
	
	@JsonProperty("BranchCode")
	public String branchCode;
	
	@JsonProperty("CedingId")
	public String cedingId; 
	
	@JsonProperty("AllocationYN")
	public String allocationYN;

}
