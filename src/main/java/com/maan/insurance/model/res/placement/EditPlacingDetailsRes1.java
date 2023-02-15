package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBaseLayerExistingListRes1;

import lombok.Data;

@Data
public class EditPlacingDetailsRes1 {
	@JsonProperty("Sno")
	private String sno; 
	@JsonProperty("BaseproposalNo")
	private String baseproposalNo; 
	@JsonProperty("BouquetNo")
	private String bouquetNo; 
	@JsonProperty("ReinsurerId")
	private String reinsurerId; 
	@JsonProperty("ReinsurerName")
	private String reinsurerName; 
	@JsonProperty("BrokerName")
	private String brokerName; 
	@JsonProperty("CedingCompany")
	private String cedingCompany; 
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName; 
	@JsonProperty("BrokerId")
	private String brokerId; 
	@JsonProperty("ProposalNo")
	private String proposalNo; 
	@JsonProperty("ShareOffered")
	private String shareOffered; 
	@JsonProperty("WrittenLine")
	private String writtenLine; 
	@JsonProperty("Brokerage")
	private String brokerage; 
	@JsonProperty("Writtenvaliditydate")
	private String writtenvaliditydate; 
	@JsonProperty("WrittenvalidityRemarks")
	private String writtenvalidityRemarks; 
	@JsonProperty("ProposedWL")
	private String proposedWL; 
	@JsonProperty("SignedLine")
	private String signedLine; 
	@JsonProperty("ProposedSL")
	private String proposedSL; 
	@JsonProperty("Reoffer")
	private String reoffer ;
	
	@JsonProperty("TqrBrokerageAmt")
	private String tqrBrokerageAmt; 
	@JsonProperty("SignedLineValidity")
	private String signedLineValidity; 
	@JsonProperty("SignedLineRemarks")
	private String signedLineRemarks; 
	@JsonProperty("EmailStatus")
	private String emailStatus; 
	@JsonProperty("PsignedLine")
	private String psignedLine ;
	@JsonProperty("Epi")
	private String epi ;
}
