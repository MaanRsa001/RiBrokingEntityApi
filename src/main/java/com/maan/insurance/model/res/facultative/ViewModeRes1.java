package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.nonproportionality.InstalmentperiodReq;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.RemarksRes;
import com.maan.insurance.model.res.nonproportionality.InstalListRes;

import lombok.Data;

@Data
public class ViewModeRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("DepartmentName")
	private String departmentName;
	@JsonProperty("ProfitCenterCode")
	private String profitCenterCode;
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("Month")
	private String month;
	@JsonProperty("Year")
	private String year;
	@JsonProperty("Underwriter")
	private String underwriter;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("ExpiryDate")
	private String expiryDate;
	@JsonProperty("AccountDate")
	private String accountDate;
	@JsonProperty("OriginalCurrency")
	private String originalCurrency;
	@JsonProperty("UsCurrencyRate")
	private String usCurrencyRate;
	@JsonProperty("TerritoryName")
	private String territoryName;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("Location")
	private String location;
	@JsonProperty("City")
	private String city;
	@JsonProperty("Nr")
	private String nr;
	@JsonProperty("CedantsRet")
	private String cedantsRet;
	@JsonProperty("Maxiumlimit")
	private String maxiumlimit;
	@JsonProperty("Deductible")
	private String deductible;
	@JsonProperty("DeductibleDC")
	private String deductibleDC;
	@JsonProperty("DeductibleFacXol")
	private String deductibleFacXol;
	@JsonProperty("DeductibleFacXolDC")
	private String deductibleFacXolDC;
	@JsonProperty("SpRetro")
	private String spRetro;
	@JsonProperty("Pml")
	private String pml;
	@JsonProperty("Sipml")
	private String sipml;
	@JsonProperty("Cu")
	private String cu;
	@JsonProperty("CuRsn")
	private String cuRsn;
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
	@JsonProperty("ShSd")
	private String shSd;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Interest")
	private String interest;
	@JsonProperty("Sumusd")
	private String sumusd;
	@JsonProperty("GwpiUsd")
	private String gwpiUsd;
	@JsonProperty("Pmlusd")
	private String pmlusd;
	@JsonProperty("Tplusd")
	private String tplusd;
	@JsonProperty("SumOrginalUsd")
	private String sumOrginalUsd;
	@JsonProperty("GwpiOurShareusd")
	private String gwpiOurShareusd;
	@JsonProperty("PmlOurShareusd")
	private String pmlOurShareusd;
	@JsonProperty("TplOurshareusd")
	private String tplOurshareusd;
	@JsonProperty("DeductibleOurShareusd")
	private String deductibleOurShareusd;
	@JsonProperty("CoverlimitOurShareusd")
	private String coverlimitOurShareusd;
	@JsonProperty("NoInsurer")
	private String noInsurer;
	@JsonProperty("PolicyBranch")
	private String policyBranch;
	@JsonProperty("Scope")
	private String scope;
	@JsonProperty("SumInsuredOurShare")
	private String sumInsuredOurShare;
	@JsonProperty("GwpiOurShare")
	private String gwpiOurShare;
	@JsonProperty("PmlOCOurShare")
	private String pmlOCOurShare;
	@JsonProperty("TplOurShare")
	private String tplOurShare;
	@JsonProperty("DeductibleOurShare")
	private String deductibleOurShare;
	@JsonProperty("CoverlimitOurShare")
	private String coverlimitOurShare;
	@JsonProperty("Premiumrate")
	private String premiumrate;
	@JsonProperty("CedRetenType")
	private String cedRetenType;
	@JsonProperty("ModeOfTransport")
	private String modeOfTransport;
	@JsonProperty("VesselName")
	private String vesselName;
	@JsonProperty("VesselAge")
	private String vesselAge;
	@JsonProperty("XolOC")
	private String xolOC;
	@JsonProperty("XolDC")
	private String xolDC;
	@JsonProperty("XolOSOC")
	private String xolOSOC;
	@JsonProperty("XolOSDC")
	private String xolOSDC;
	@JsonProperty("NoOfInst")
	private String noOfInst;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("LimitPerVesselOC")
	private String limitPerVesselOC;
	@JsonProperty("LimitPerVesselDC")
	private String limitPerVesselDC;
	@JsonProperty("LimitPerLocationOC")
	private String limitPerLocationOC;
	@JsonProperty("LimitPerLocationDC")
	private String limitPerLocationDC;
	@JsonProperty("InwardType")
	private String inwardType;
	@JsonProperty("LocIssued")
	private String locIssued;
	@JsonProperty("Latitude")
	private String latitude;
	@JsonProperty("Longitude")
	private String longitude;
	@JsonProperty("Vessaletonnage")
	private String vessaletonnage;
	@JsonProperty("PslOC")
	private String pslOC;
	@JsonProperty("Pslusd")
	private String pslusd;
	@JsonProperty("PslOurShare")
	private String pslOurShare;
	@JsonProperty("PslOurShareusd")
	private String pslOurShareusd;
	@JsonProperty("PllOC")
	private String pllOC;
	@JsonProperty("Pllusd")
	private String pllusd;
	@JsonProperty("PllOurShare")
	private String pllOurShare;
	@JsonProperty("PllOurShareusd")
	private String pllOurShareusd;
	@JsonProperty("PblOC")
	private String pblOC;
	@JsonProperty("Pblusd")
	private String pblusd;
	@JsonProperty("PblOurShare")
	private String pblOurShare;
	@JsonProperty("PblOurShareusd")
	private String pblOurShareusd;
	@JsonProperty("ReceiptofPayment")
	private String receiptofPayment;
	@JsonProperty("LocBankName")
	private String locBankName;
	@JsonProperty("LocCreditPrd")
	private String locCreditPrd;
	@JsonProperty("LocCreditAmt")
	private String locCreditAmt;
	
	@JsonProperty("LocBeneficerName")
	private String locBeneficerName;
	@JsonProperty("Territory")
	private String territory;
	@JsonProperty("Endorsmenttype")
	private String endorsmenttype;
	@JsonProperty("XollayerNo")
	private String xollayerNo;
	@JsonProperty("CountryIncludedList")
	private String countryIncludedList;
	@JsonProperty("CountryIncludedName")
	private String countryIncludedName;
	@JsonProperty("CountryExcludedList")
	private String countryExcludedList;
	@JsonProperty("CountryExcludedName")
	private String countryExcludedName;
	@JsonProperty("RiskGrade")
	private String riskGrade;
	@JsonProperty("OccCode")
	private String occCode;
	@JsonProperty("RiskDetail")
	private String riskDetail;
	@JsonProperty("FireProt")
	private String fireProt;
	@JsonProperty("Mbind")
	private String mbind;
	@JsonProperty("CategoryZone")
	private String categoryZone;
	@JsonProperty("EqwsInd")
	private String eqwsInd;
	@JsonProperty("WsThreat")
	private String wsThreat;
	@JsonProperty("EqThreat")
	private String eqThreat;
	@JsonProperty("AcqBonus")
	private String acqBonus;
	@JsonProperty("AcqBonusPercentage")
	private String acqBonusPercentage;
	@JsonProperty("Commn")
	private String commn;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("LossRecord")
	private String lossRecord;
	@JsonProperty("DgmsApproval")
	private String dgmsApproval;
	@JsonProperty("UnderwriterCode")
	private String underwriterCode;
	@JsonProperty("UwRecommendation")
	private String uwRecommendation;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("OthAccep")
	private String othAccep;
	@JsonProperty("ReftoHO")
	private String reftoHO;
	@JsonProperty("AcqCost")
	private String acqCost;
	@JsonProperty("Accusd")
	private String accusd;
	@JsonProperty("Othercost")
	private String othercost;
	@JsonProperty("AcqCostPer")
	private String acqCostPer;
	@JsonProperty("MlopYN")
	private String mlopYN;
	@JsonProperty("AlopYN")
	private String alopYN;
	@JsonProperty("EndorsementDate")
	private String endorsementDate;
	@JsonProperty("LeaderUnderwritercountry")
	private String leaderUnderwritercountry;
	@JsonProperty("LeaderUnderwriter")
	private String leaderUnderwriter;
	@JsonProperty("LeaderUnderwritershare")
	private String leaderUnderwritershare;
	@JsonProperty("CrestaStatus")
	private String crestaStatus;
	@JsonProperty("Exclusion")
	private String exclusion;
	@JsonProperty("AcqBonusName")
	private String acqBonusName;
	@JsonProperty("ContractListVal")
	private String contractListVal;

	@JsonProperty("MappingRes")
	private List<MappingRes> MappingRes;
	@JsonProperty("InstalListRes")
	private List<InstalListRes> instalList;
	@JsonProperty("Instalmentperiod")
	private List<InstalmentperiodReq> instalmentperiod;

}
