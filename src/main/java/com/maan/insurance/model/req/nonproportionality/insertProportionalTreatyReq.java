package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.RetroListReq;

import lombok.Data;

@Data
public class insertProportionalTreatyReq {
	
	
	@JsonProperty("Pid")
	private String pid;
	
	@JsonProperty("SaveFlag")
	private boolean saveFlag;
	
	@JsonProperty("RenewalFlag")
	private String renewalFlag;
	
	@JsonProperty("AmendId")
	private String amendId;

	
	@JsonProperty("RenewalEditMode")
	private String renewalEditMode;
	
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	
	@JsonProperty("SubProfitcenter")
	private String subProfitcenter;
	
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	
	@JsonProperty("CedingCo")
	private String cedingCo;
	
	@JsonProperty("Broker")
	private String broker;
	
	@JsonProperty("TreatyNametype")
	private String treatyNametype;
	
	@JsonProperty("Month")
	private String month;
	
	@JsonProperty("UwYear")
	private String uwYear;
	
	@JsonProperty("Underwriter")
	private String underwriter;
	
	@JsonProperty("IncepDate")
	private String incepDate;
	
	@JsonProperty("ExpDate")
	private String expDate;
	
	@JsonProperty("AccDate")
	private String accDate;
	
	@JsonProperty("OrginalCurrency")
	private String orginalCurrency;
	
	
	@JsonProperty("Basis")
	private String basis;
	
	@JsonProperty("Pnoc")
	private String pnoc;
	
	@JsonProperty("RiskCovered")
	private String riskCovered;
	
	@JsonProperty("Territoryscope")
	private String territoryscope;
	
	@JsonProperty("Territory")
	private String territory;
	
	@JsonProperty("ProStatus")
	private String proStatus;
	
	@JsonProperty("ProposalType")
	private String proposalType;
	
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber;
	
	@JsonProperty("NoRetroCess")
	private String noRetroCess;
	
	@JsonProperty("RetroType")
	private String retroType;
	
	@JsonProperty("InsuredName")
	private String insuredName;
	
	@JsonProperty("InwardType")
	private String inwardType;
	
	@JsonProperty("TreatyType")
	private String treatyType;
	
	@JsonProperty("BusinessType")
	private String businessType;
	
	@JsonProperty("ExchangeType")
	private String exchangeType;
	
	@JsonProperty("PerilCovered")
	private String perilCovered;
	
	@JsonProperty("LOCIssued")
	private String lOCIssued;
	
	@JsonProperty("UmbrellaXL")
	private String umbrellaXL;
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("CountryIncludedList")
	private String countryIncludedList;
	
	@JsonProperty("CountryExcludedList")
	private String countryExcludedList;
	
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
	
	@JsonProperty("RetentionYN")
	private String retentionYN;
	
	@JsonProperty("EndNo")
	private String endNo;
	
	@JsonProperty("LayerNo")
	private String layerNo;

	@JsonProperty("ChekmodeFlag")
	private boolean chekmodeFlag;
	
	@JsonProperty("LimitOrigCur")
	private String limitOrigCur;

	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	
	@JsonProperty("Epi")
	private String epi;
	
	@JsonProperty("ShareWritt")
	private String shareWritt;
	
	@JsonProperty("SharSign")
	private String sharSign;
	
	@JsonProperty("MaxLimitProduct")
	private String maxLimitProduct;
	
	@JsonProperty("SubPremium")
	private String subPremium;
	
	@JsonProperty("XlPremium")
	private String xlPremium;
	
	@JsonProperty("PortfoloCovered")
	private String portfoloCovered;
	
	@JsonProperty("DeduchunPercent")
	private String deduchunPercent;
	
	@JsonProperty("MdPremium")
	private String mdPremium;
	
	@JsonProperty("AdjRate")
	private String adjRate;
	
	@JsonProperty("SpRetro")
	private String spRetro;
	
	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	
	@JsonProperty("Mdpremiumourservice")
	private String mdpremiumourservice;
	
	@JsonProperty("LimitPerVesselOC")
	private String limitPerVesselOC;
	
	@JsonProperty("LimitPerLocationOC")
	private String limitPerLocationOC;
	
	@JsonProperty("EgnpiOffer")
	private String egnpiOffer;
	
	@JsonProperty("OurAssessment")
	private String ourAssessment;
	
	@JsonProperty("ContNo")
	private String contNo;
	
	@JsonProperty("Eventlimit")
	private String eventlimit;
	
	@JsonProperty("EventLimitOurShare")
	private String eventLimitOurShare;
	
	@JsonProperty("CoverLimitXL")
	private String coverLimitXL;
	
	@JsonProperty("CoverLimitXLOurShare")
	private String coverLimitXLOurShare;
	
	@JsonProperty("DeductLimitXL")
	private String deductLimitXL;
	
	@JsonProperty("DeductLimitXLOurShare")
	private String deductLimitXLOurShare;
	
	@JsonProperty("Pml")
	private String pml;
	
	@JsonProperty("PmlPercent")
	private String pmlPercent;
	
	@JsonProperty("Egnpipml")
	private String egnpipml;
	
	@JsonProperty("EgnpipmlOurShare")
	private String egnpipmlOurShare;
	
	@JsonProperty("Premiumbasis")
	private String premiumbasis;
	
	@JsonProperty("MinimumRate")
	private String minimumRate;
	
	@JsonProperty("BurningCostLF")
	private String burningCostLF;
	
	@JsonProperty("MaximumRate")
	private String maximumRate;
	
	@JsonProperty("MinPremium")
	private String minPremium;
	
	@JsonProperty("MinPremiumOurShare")
	private String minPremiumOurShare;
	
	@JsonProperty("PaymentDuedays")
	private String paymentDuedays;
	
	@JsonProperty("LimitOrigCurPml")
	private String limitOrigCurPml;

	@JsonProperty("LimitOrigCurPmlOS")
	private String limitOrigCurPmlOS;
	
	@JsonProperty("TreatyLimitsurplusOCPml")
	private String treatyLimitsurplusOCPml;
	
	@JsonProperty("TreatyLimitsurplusOCPmlOS")
	private String treatyLimitsurplusOCPmlOS;
	
	@JsonProperty("Epipml")
	private String epipml;
	
	@JsonProperty("EpipmlOS")
	private String epipmlOS;
	
	@JsonProperty("ContractListVal")
	private String contractListVal;
	
	@JsonProperty("MaxAmdId")
	private String maxAmdId;
	
	@JsonProperty("EpiAsPerShare")
	private String epiAsPerShare;
	
	@JsonProperty("LayerProposalNo")
	private String layerProposalNo;
	
	@JsonProperty("BaseLayer")
	private String baseLayer;
	
	@JsonProperty("BaseLoginID")
	private String baseLoginID;
	
	@JsonProperty("Renewalcontractno")
	private String renewalcontractno;
	
	@JsonProperty("LayerMode")
	private String layerMode;
	
	@JsonProperty("SourceId")
	private String sourceId;
	

	

	@JsonProperty("Edit")
	private String edit;
	
	@JsonProperty("OpendDate")
	private String opendDate;
	
	@JsonProperty("OpstartDate")
	private String opstartDate;
	
	@JsonProperty("ProcessId")
	private String processId;
	
	@JsonProperty("OpenPeriodDate")
	private String openPeriodDate;
	

	
	@JsonProperty("CedReten")
	private String cedReten;
	
	@JsonProperty("DocStatus")
	private String docStatus;
	
	@JsonProperty("EndorsementStatus")
	private String endorsementStatus;
	
	@JsonProperty("AmendStatus")
	private Boolean amendStatus; 
	
	@JsonProperty("BouquetNo") //RI
	private String bouquetNo;
	
	@JsonProperty("BouquetModeYN")
	private String bouquetModeYN; 
	
	@JsonProperty("OfferNo")
	private String offerNo; 
	
	@JsonProperty("UwYearTo")
	private String uwYearTo; 
	@JsonProperty("SectionNo")
	private String sectionNo; 
	
	@JsonProperty("ProductId") 
	private String productId;  
	
	@JsonProperty("AccountingPeriodNotes")
	private String accountingPeriodNotes; 
	
	@JsonProperty("StatementConfirm")
	private String statementConfirm;
	
	@JsonProperty("RiskdetailYN")
	private String riskdetailYN;
	
	@JsonProperty("BrokerdetYN")
	private String brokerdetYN;
	
	@JsonProperty("PremiumdetailYN")
	private String premiumdetailYN;
	
	@JsonProperty("InstallYN")
	private String installYN;
	
	@JsonProperty("AcqdetailYN")
	private String acqdetailYN;
	
	@JsonProperty("ReinstdetailYN")
	private String reinstdetailYN;
	
	@JsonProperty("CoverdetYN")
	private String coverdetYN;
	
	@JsonProperty("CommissiondetailYN")
	private String commissiondetailYN;
	
	@JsonProperty("DepositdetailYN")
	private String depositdetailYN;
	
	@JsonProperty("LossdetailYN")
	private String lossdetailYN;
	
	@JsonProperty("DocdetailYN")
	private String docdetailYN;
	
	@JsonProperty("PaymentPartner")
	private String paymentPartner;
	
	@JsonProperty("RateOnLine")
	private String rateOnLine; 
	
	@JsonProperty("QuotesharePercent")
	private String quotesharePercent;
	
	@JsonProperty("ContractMode")
	private String contractMode;
	//-----------------------savesecondpage

	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	
	@JsonProperty("ReInstatementPremium")
	private String reInstatementPremium;
	@JsonProperty("CrestaStatus")
	private String crestaStatus;
	@JsonProperty("AcqBonus")
	private String acqBonus;
	@JsonProperty("AcqBonusPercentage")
	private String acqBonusPercentage;

	
	
	@JsonProperty("AnualAggregateLiability")
	private String anualAggregateLiability;
	@JsonProperty("ReinstNo")
	private String reinstNo;
	@JsonProperty("ReinstAdditionalPremium")
	private String reinstAdditionalPremium;

	@JsonProperty("CrestaPopUp")
	private String crestaPopUp;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("ShareProfitCommission")
	private String shareProfitCommission;
	
	@JsonProperty("acquisitionCost")
	private String AcquisitionCost;

	@JsonProperty("LeaderUnderwriter")
	private String leaderUnderwriter;
	@JsonProperty("LeaderUnderwritershare")
	private String leaderUnderwritershare;
	@JsonProperty("Accounts")
	private String accounts;
	@JsonProperty("Exclusion")
	private String exclusion;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("UnderwriterRecommendations")
	private String underwriterRecommendations;
	@JsonProperty("GmsApproval")
	private String gmsApproval;
	@JsonProperty("Othercost")
	private String othercost;
	@JsonProperty("ReinstAditionalPremiumpercent")
	private String reinstAditionalPremiumpercent;
	@JsonProperty("BurningCost")
	private String burningCost;
	@JsonProperty("AnualAggregateDeduct")
	private String anualAggregateDeduct;
	@JsonProperty("OccurrentLimit")
	private String occurrentLimit;

	@JsonProperty("DepartmentId")
	private String departmentId;

	@JsonProperty("LeaderUnderwritercountry")
	private String leaderUnderwritercountry;


	
	@JsonProperty("PreviousendoDate")
	private String previousendoDate;
	@JsonProperty("EndorsementDate")
	private String endorsementDate;

	@JsonProperty("MaxDate")
	private String maxDate;
	@JsonProperty("NoInsurer")
	private String NoInsurer;
	
	@JsonProperty("RetentionPercentage")
	private String retentionPercentage;

	@JsonProperty("BonusPopUp")
	private String bonusPopUp;

	@JsonProperty("ReinsPopUp")
	private String reinsPopUp;

	@JsonProperty("RetroDupContract")
	private String retroDupContract;


	@JsonProperty("ExpiryDate")
	private String expiryDate;

	
	@JsonProperty("RetroListReq")
	private List<RetroListReq> retroListReq;
//	@JsonProperty("Instalmentperiod")
//	private List<InstalmentperiodReq> instalmentperiod;
	@JsonProperty("NoRetroCessReq")
	private List<NoRetroCessReq> noRetroCessReq;
	
	@JsonProperty("ReferenceNo")
	private String referenceNo; //ri
//-------------------installmentPremium
	
	@JsonProperty("EndorsementNo")
	private String endorsementNo;
	
	@JsonProperty("InstalmentperiodReq")
	private List<InstalmentperiodReq> instalmentperiodReq;

	@JsonProperty("CoverList")
	private List<CoverList> CoverList;
	
	@JsonProperty("CoverLimitAmount")
	private List<CoverLimitAmount> CoverLimitAmount;
	@JsonProperty("RequestNumber")
	private String requestNumber;

}
