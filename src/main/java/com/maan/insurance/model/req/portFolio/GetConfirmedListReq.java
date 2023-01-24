package com.maan.insurance.model.req.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.portFolio.GetHistoryListRes;
import com.maan.insurance.model.res.portFolio.GetHistoryListRes1;

import lombok.Data;

@Data
public class GetConfirmedListReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("SearchType")
	private String searchType;
	@JsonProperty("ProposalNoSearch")
	private String proposalNoSearch;
	@JsonProperty("ContractNoSearch")
	private String contractNoSearch;
	@JsonProperty("CompanyNameSearch")
	private String companyNameSearch;
	@JsonProperty("BrokerNameSearch")
	private String brokerNameSearch;
	@JsonProperty("DepartmentNameSearch")
	private String departmentNameSearch;
	@JsonProperty("BouquetNoSearch")
	private String bouquetNoSearch;
	
	@JsonProperty("SubclassSearch")
	private String subclassSearch;
	@JsonProperty("InsuredNameSearch")
	private String insuredNameSearch;
	@JsonProperty("UwYearSearch")
	private String uwYearSearch;
	@JsonProperty("UnderwriterSearch")
	private String underwriterSearch;
	@JsonProperty("UwYearSearch1")
	private String uwYearSearch1;
	@JsonProperty("UnderwriterSearch1")
	private String underwriterSearch1;
	@JsonProperty("OfferNoSearch")
	private String offerNoSearch;
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("AttachedUW")
	private String attachedUW;
	@JsonProperty("MenuRights")
	private List<String> 	menuRights; 
}
