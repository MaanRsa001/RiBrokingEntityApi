package com.maan.insurance.model.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetReceiptReversalListReq {
	
	@JsonProperty("SearchType")
	private String searchType;
	
	@JsonProperty("PaymentNoSearch")
	private String paymentNoSearch;
	
	@JsonProperty("BrokerNameSearch")
	private String brokerNameSearch;
	
	@JsonProperty("CompanyNameSearch")
	private String companyNameSearch;
	
	@JsonProperty("RemarksSearch")
	private String remarksSearch;
	
	@JsonProperty("TransType")
	private String transtype;
	
	
	@JsonProperty("BranchCode")
	private String branchCode;


}
