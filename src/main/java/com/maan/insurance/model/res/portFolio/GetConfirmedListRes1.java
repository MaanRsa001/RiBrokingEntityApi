package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetConfirmedListRes1 {
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("OfferNo")
	private String offerNo;
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("SubClass")
	private String subClass;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("QuoteGendratedDate")
	private String quoteGendratedDate;
	@JsonProperty("CeddingCompanyId")
	private String ceddingCompanyId;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("SectionNo")
	private String sectionNo;
	@JsonProperty("Flag")
	private String flag;
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
	@JsonProperty("ButtonSelectionList")
	private List<ButtonSelectionListRes> buttonSelectionList;
}
