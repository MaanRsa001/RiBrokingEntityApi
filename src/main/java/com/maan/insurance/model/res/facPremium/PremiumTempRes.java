package com.maan.insurance.model.res.facPremium;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumTempRes {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("RequestNo")
	private String requestNo;
	
	@JsonProperty("AccountPeriod")
	private String accountPeriod;
	@JsonProperty("TransDropDownVal")
	private String transDropDownVal;
	
	@JsonProperty("EndtYN")
	private String endtYN;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("StatementDate")
	private String statementDate;
	@JsonProperty("MovementYN")
	private String movementYN;
	@JsonProperty("TransDate")
	private String transDate;
	@JsonProperty("TransOpenperiodStatus")
	private String transOpenperiodStatus;
	@JsonProperty("ProductId")
	private String productId;
	
	
	

}
