package com.maan.insurance.model.req.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.facultative.CoverLimitOCReq;
import com.maan.insurance.model.res.nonproportionality.BonusRes;

import lombok.Data;

@Data
public class FirstPageInsertReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("UsCurrencyRate")
	private String usCurrencyRate;
	@JsonProperty("ProfitCenterCode")
	private String profitCenterCode;
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	@JsonProperty("Month")
	private String month;
	@JsonProperty("Underwriter")
	private String underwriter;
	@JsonProperty("FacultativeDepartment")
	private String facultativeDepartment;
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("Flag")
	private String flag;
	@JsonProperty("Contract")
	private String contract;
	@JsonProperty("InwardType")
	private String inwardType;
	@JsonProperty("ReceiptofPayment")
	private String receiptofPayment;
	@JsonProperty("LocIssued")
	private String locIssued;
	@JsonProperty("Latitude")
	private String latitude;
	@JsonProperty("Longitude")
	private String longitude;
	@JsonProperty("Vessaletonnage")
	private String vessaletonnage;
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
	@JsonProperty("LocBankName")
	private String locBankName;
	@JsonProperty("LocCreditPrd")
	private String locCreditPrd;
	@JsonProperty("LocCreditAmt")
	private String locCreditAmt;
	@JsonProperty("LocBeneficerName")
	private String locBeneficerName;
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	@JsonProperty("DeductibleFacXolPml")
	private String deductibleFacXolPml;
	@JsonProperty("DeductibleFacXolPmlOurShare")
	private String deductibleFacXolPmlOurShare;
	@JsonProperty("GwpiPml")
	private String gwpiPml;
	@JsonProperty("GwpiPmlOurShare")
	private String gwpiPmlOurShare;
	@JsonProperty("PslOC")
	private String pslOC;
	@JsonProperty("PslOurShare")
	private String pslOurShare;
	@JsonProperty("PllOC")
	private String pllOC;
	@JsonProperty("PllOurShare")
	private String pllOurShare;
	@JsonProperty("PblOurShare")
	private String pblOurShare;
	@JsonProperty("PblOC")
	private String pblOC;
	@JsonProperty("PmlOCOurShare")
	private String pmlOCOurShare;
	@JsonProperty("SumInsuredPml")
	private String sumInsuredPml;
	@JsonProperty("SumInsuredPmlOurShare")
	private String sumInsuredPmlOurShare;
	
	
	@JsonProperty("Loginid")
	private String loginid;
	@JsonProperty("DeductibleFacXol")
	private String deductibleFacXol;
	@JsonProperty("XolOC")
	private String xolOC;
	@JsonProperty("XolOSOC")
	private String xolOSOC;
	@JsonProperty("NoOfInst")
	private String noOfInst;
	@JsonProperty("ModeOfTransport")
	private String modeOfTransport;
	@JsonProperty("VesselName")
	private String vesselName;
	@JsonProperty("VesselAge")
	private String vesselAge;
	@JsonProperty("LimitPerVesselOC")
	private String limitPerVesselOC;
	@JsonProperty("LimitPerLocationOC")
	private String limitPerLocationOC;
	@JsonProperty("CountryIncludedList")
	private String countryIncludedList;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("CountryExcludedList")
	private String countryExcludedList;
	@JsonProperty("ContractListVal")
	private String contractListVal;
	@JsonProperty("XollayerNo")
	private String xollayerNo;
	@JsonProperty("AccountDate")
	private String accountDate;
	@JsonProperty("OriginalCurrency")
	private String originalCurrency;
	@JsonProperty("Territory")
	private String territory;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("Location")
	private String location;
	@JsonProperty("City")
	private String city;
	@JsonProperty("CedantsRet")
	private String cedantsRet;
	@JsonProperty("Nr")
	private String nr;
	@JsonProperty("Maxiumlimit")
	private String maxiumlimit;
	@JsonProperty("Deductible")
	private String deductible;
	@JsonProperty("Interest")
	private String interest;
	@JsonProperty("SpRetro")
	private String spRetro;
	@JsonProperty("Pml")
	private String pml;
	@JsonProperty("Sipml")
	private String sipml;
	@JsonProperty("SumInsured")
	private String sumInsured;
	@JsonProperty("Gwpi")
	private String gwpi;
	@JsonProperty("Pmll")
	private String pmll;
	@JsonProperty("Tpl")
	private String tpl;
	@JsonProperty("ShWt")
	private String shWt;
	@JsonProperty("ProStatus")
	private String proStatus;
	@JsonProperty("ShSd")
	private String shSd;
	@JsonProperty("Year")
	private String year;
	@JsonProperty("BaseLoginID")
	private String baseLoginID;
	@JsonProperty("NoInsurer")
	private String noInsurer;
	@JsonProperty("RenewalContractno")
	private String renewalContractno;
	@JsonProperty("RenewalStatus")
	private String renewalStatus;
	@JsonProperty("Premiumrate")
	private String premiumrate;
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	@JsonProperty("CedRetenType")
	private String cedRetenType;
	@JsonProperty("SumInsuredOurShare")
	private String sumInsuredOurShare;
	@JsonProperty("GwpiOurShare")
	private String gwpiOurShare;
	@JsonProperty("PmlOurShare")
	private String pmlOurShare;
	@JsonProperty("TplOurShare")
	private String tplOurShare;
	@JsonProperty("TotalCoverage")
	private String totalCoverage;
	@JsonProperty("CoverLimitOC")
	private List<CoverLimitOCReq> coverLimitOCReq;
	
	@JsonProperty("TotalGWPI")
	private String totalGWPI;
	@JsonProperty("XoltotalGWPI")
	private String xoltotalGWPI;
	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("ProcessId")
	private String processId;
	@JsonProperty("Edit")
	private String edit;
	@JsonProperty("CountryID")
	private String countryID;
	@JsonProperty("TotalDeductible")
	private String totalDeductible;
	
}
