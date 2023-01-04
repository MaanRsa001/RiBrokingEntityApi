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
public class GetReceiptAllocateReq {
	
	@JsonProperty("TransType")
	private String transType;

	@JsonProperty("AllocateType")
	private String allocateType;
	
	@JsonProperty("PaymentNoSearch")
	private String paymentNoSearch;
	
	@JsonProperty("BrokerNameSearch")
	private String brokerNameSearch;
	
	@JsonProperty("CompanyNameSearch")
	private String companyNameSearch;
	
	@JsonProperty("RemarksSearch")
	private String remarksSearch;
	
	@JsonProperty("CurrencySearch")
	private String currencySearch;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("SearchType")
	private String searchType;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	

}
