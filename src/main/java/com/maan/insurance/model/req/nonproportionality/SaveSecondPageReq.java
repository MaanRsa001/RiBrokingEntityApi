package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.proportionality.RetroListReq;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res1;

import lombok.Data;

@Data
public class SaveSecondPageReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("BranchCode")
	private String branchCode;
	@JsonProperty("LimitOurShare")
	private String limitOurShare;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	@JsonProperty("EpiAsPerOffer")
	private String epiAsPerOffer;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("Mdpremiumourservice")
	private String mdpremiumourservice;
	@JsonProperty("ReInstatementPremium")
	private String reInstatementPremium;
	@JsonProperty("CrestaStatus")
	private String crestaStatus;
	@JsonProperty("AcqBonus")
	private String acqBonus;
	@JsonProperty("AcqBonusPercentage")
	private String acqBonusPercentage;
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber;
	
	
	@JsonProperty("AnualAggregateLiability")
	private String anualAggregateLiability;
	@JsonProperty("ReinstNo")
	private String reinstNo;
	@JsonProperty("ReinstAdditionalPremium")
	private String reinstAdditionalPremium;
	@JsonProperty("MinPremiumOurShare")
	private String minPremiumOurShare;
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
	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("NoRetroCess")
	private String noRetroCess;
//	@JsonProperty("OverRidder")
//	private String overRidder;
//	@JsonProperty("PremiumReserve")
//	private String premiumReserve;
//	@JsonProperty("Lossreserve")
//	private String lossreserve;
//	@JsonProperty("Interest")
//	private String interest;
//	@JsonProperty("PortfolioinoutPremium")
//	private String portfolioinoutPremium;
//	@JsonProperty("PortfolioinoutLoss")
//	private String portfolioinoutLoss;
//	@JsonProperty("LossAdvise")
//	private String lossAdvise;
//	@JsonProperty("CashLossLimit")
//	private String cashLossLimit;
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
//	@JsonProperty("SlideScaleCommission")
//	private String slideScaleCommission;
//	@JsonProperty("LossParticipants")
//	private String lossParticipants;
//	@JsonProperty("CommissionSubClass")
//	private String commissionSubClass;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("LoginId")
	private String loginId;
	@JsonProperty("LeaderUnderwritercountry")
	private String leaderUnderwritercountry;
//	@JsonProperty("Orginalacqcost")
//	private String orginalacqcost;
//	@JsonProperty("Ourassessmentorginalacqcost")
//	private String ourassessmentorginalacqcost;
//	@JsonProperty("OuracqCost")
//	private String ouracqCost;
//	@JsonProperty("LosscommissionSubClass")
//	private String losscommissionSubClass;
//	@JsonProperty("SlidecommissionSubClass")
//	private String slidecommissionSubClass;
//	@JsonProperty("CrestacommissionSubClass")
//	private String crestacommissionSubClass;
//	@JsonProperty("ManagementExpenses")
//	private String managementExpenses;
//	@JsonProperty("CommissionType")
//	private String commissionType;
//	@JsonProperty("ProfitCommissionPer")
//	private String profitCommissionPer;
//	@JsonProperty("Setup")
//	private String setup;
//	@JsonProperty("SuperProfitCommission")
//	private String superProfitCommission;
//	@JsonProperty("LossCarried")
//	private String lossCarried;
//	@JsonProperty("Lossyear")
//	private String lossyear;
//	@JsonProperty("ProfitCarried")
//	private String profitCarried;
//	@JsonProperty("ProfitCarriedForYear")
//	private String profitCarriedForYear;
//	@JsonProperty("Fistpc")
//	private String fistpc;
//	@JsonProperty("ProfitMont")
//	private String profitMont;
//	@JsonProperty("SubProfitMonth")
//	private String subProfitMonth;
//	@JsonProperty("Subpc")
//	private String subpc;
//	@JsonProperty("SubSeqCalculation")
//	private String subSeqCalculation;
//	@JsonProperty("ProfitCommission")
//	private String profitCommission;
	@JsonProperty("DocStatus")
	private String docStatus;
//	@JsonProperty("LocRate")
//	private String locRate;
	@JsonProperty("ProStatus")
	private String proStatus;
//	
//	
//	@JsonProperty("TreatyType")
//	private String treatyType;
//	@JsonProperty("InwardType")
//	private String inwardType;
//	@JsonProperty("Broker")
//	private String broker;
	@JsonProperty("EndorsementStatus")
	private String endorsementStatus;
	@JsonProperty("AccDate")
	private String accDate;
	
	@JsonProperty("PreviousendoDate")
	private String previousendoDate;
	@JsonProperty("EndorsementDate")
	private String endorsementDate;
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
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
	
//	@JsonProperty("SlidePopUp")
//	private String slidePopUp;
//	@JsonProperty("BaseLayer")
//	private String baseLayer;
//	@JsonProperty("LossPopUp")
//	private String lossPopUp;
//	@JsonProperty("ProfitPopUp")
//	private String profitPopUp;
	@JsonProperty("RenewalFlag")
	private String renewalFlag;
	
	@JsonProperty("Renewalcontractno")
	private String renewalcontractno;
	@JsonProperty("RetroDupContract")
	private String retroDupContract;

	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("RetroType")
	private String retroType;
	@JsonProperty("AmendId")
	private String amendId;

	@JsonProperty("IncepDate")
	private String incepDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("BusinessType")
	private String businessType;
	
	@JsonProperty("RetroListReq")
	private List<RetroListReq> retroListReq;
	@JsonProperty("Instalmentperiod")
	private List<InstalmentperiodReq> instalmentperiod;
	@JsonProperty("NoRetroCessReq")
	private List<NoRetroCessReq> noRetroCessReq;
}
