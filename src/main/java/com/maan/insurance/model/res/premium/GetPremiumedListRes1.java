package com.maan.insurance.model.res.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetPremiumedListRes1 {
	@JsonProperty("RequestNo")
	private String requestNo;
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
//	
	@JsonProperty("ContNo")
	private String contNo;
//	
//	@JsonProperty("CedingCompanyName")
//	private String cedingCompanyName;
//	
//	@JsonProperty("Broker")
//	private String broker;
//	
	@JsonProperty("Layerno")
	private String layerno;
	
	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	
	@JsonProperty("AccountPeriodDate")
	private String accountPeriodDate;
	
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
//	
//	@JsonProperty("EndtYN")
//	private String endtYN;
//	
//	@JsonProperty("ProductId")
//	private String productId;
//	
//	@JsonProperty("InceptionDate")
//	private String inceptionDate;
	
	@JsonProperty("StatementDate")
	private String statementDate;
	
//	@JsonProperty("MovementYN")
//	private String movementYN;
	
	@JsonProperty("TransDate")
	private String transDate;
	@JsonProperty("TransOpenperiodStatus")
	private String transOpenperiodStatus;
	
	@JsonProperty("AllocatedYN")
	private String allocatedYN;
//	
//	@JsonProperty("DeleteStatus")
//	private String deleteStatus;

	@JsonProperty("SectionNo")
	private String sectionNo;
}
