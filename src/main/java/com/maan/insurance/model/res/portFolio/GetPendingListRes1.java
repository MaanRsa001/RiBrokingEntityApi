package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes1;

import lombok.Data;

@Data
public class GetPendingListRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("InsuredName")
	private String insuredName; 
	@JsonProperty("QuoteGendrateddate")
	private String quoteGendrateddate;
	@JsonProperty("Ceddingcompanyid")
	private String ceddingcompanyid;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("Title")
	private String title;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	
	@JsonProperty("Contractno1")
	private String contractno1;
	@JsonProperty("Lay1")
	private String lay1;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("UwMonth")
	private String uwMonth;
	@JsonProperty("Underwritter")
	private String underwritter;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("OldContract")
	private String oldContract;
	
	@JsonProperty("OfferNo")
	private String offerNo;
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("SubClass")
	private String subClass;
	@JsonProperty("ButtonSelectionList")
	private List<ButtonSelectionListRes> ButtonSelectionList;
}
