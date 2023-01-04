package com.maan.insurance.model.req.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes1;

import lombok.Data;

@Data
public class GetPendingListReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("DeptId")
	private String deptId;
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
	@JsonProperty("AttachedUW")
	private String attachedUW;
	
	@JsonProperty("CompanyNameSearch1")
	private String companyNameSearch1;
	@JsonProperty("BrokerNameSearch1")
	private String brokerNameSearch1;
	@JsonProperty("DepartmentNameSearch1")
	private String departmentNameSearch1;
	@JsonProperty("UwYearSearch2")
	private String uwYearSearch2;
	@JsonProperty("UwYearSearch3")
	private String uwYearSearch3;

	
	
	
	


}
