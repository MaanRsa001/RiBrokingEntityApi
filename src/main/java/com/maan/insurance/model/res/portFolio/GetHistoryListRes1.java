package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.portFolio.GetContractsListReq;

import lombok.Data;
@Data
public class GetHistoryListRes1 {

	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("CedingCompanyName")
	private String cedingCompanyName;
	@JsonProperty("Ceddingcompanyid")
	private String ceddingcompanyid;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("InsuredName")
	private String insuredName; 
	@JsonProperty("Display")
	private String display;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("EndorsmentTypeName")
	private String endorsmentTypeName; 
	@JsonProperty("UwYear")
	private String uwYear ;
	@JsonProperty("UwMonth")
	private String uwMonth;
	@JsonProperty("Underwritter")
	private String underwritter;
	@JsonProperty("OldContract")
	private String oldContract; 
	@JsonProperty("ProposalId")
	private String proposalId;
	@JsonProperty("QuoteGendrateddate")
	private String quoteGendrateddate;
	@JsonProperty("RenewalStatus")
	private String renewalStatus;
	@JsonProperty("RenewalPeriod")
	private String renewalPeriod;

}
