package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class ProposalInfoRes1 {
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName; 
	@JsonProperty("CedingCompany")
	private String cedingCompany; 
	@JsonProperty("BrokerCompany")
	private String brokerCompany; 
	@JsonProperty("TreatyName")
	private String treatyName; 
	@JsonProperty("InceptionDate")
	private String inceptionDate; 
	@JsonProperty("ExpiryDate")
	private String expiryDate; 
	@JsonProperty("UwYear")
	private String uwYear; 
	@JsonProperty("UwYearTo")
	private String uwYearTo; 
	@JsonProperty("BouquetModeYN")
	private String bouquetModeYN; 
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("BaseProposalNo")
	private String baseProposalNo; 
	@JsonProperty("ContractNo")
	private String contractNo; 
	@JsonProperty("LayerNo")
	private String layerNo; 
	@JsonProperty("SectionNo")
	private String sectionNo; 
	@JsonProperty("OfferNo")
	private String offerNo; 
	@JsonProperty("AmendId")
	private String amendId; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("EproposalNo")
	private String eproposalNo;
}
