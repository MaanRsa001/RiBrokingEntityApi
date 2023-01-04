package com.maan.insurance.model.res.reports;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetRetroQuarterlyReport1 {
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("InceptionDate")
	private String inceptionDate;
	@JsonProperty("InwuwYear")
	private String inwuwYear;
	@JsonProperty("PremiumTranNo")
	private String premiumTranNo;
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("RetroContractNo")
	private String retroContractNo;
	@JsonProperty("RetroLayerNo")
	private String retroLayerNo;
	@JsonProperty("DocType")
	private String docType;
	@JsonProperty("ShortName")
	private String shortName;
	@JsonProperty("RetroPercentage")
	private String retroPercentage;
	@JsonProperty("PremiumQuotashareOc")
	private String premiumQuotashareOc;
	@JsonProperty("PremiumSurplusOc")
	private String premiumSurplusOc;
	@JsonProperty("PremiumPortfolioinOc")
	private String premiumPortfolioinOc;
	@JsonProperty("ClaimPortfolioinOc")
	private String claimPortfolioinOc;
	@JsonProperty("PremiumReserveRealsedOc")
	private String premiumReserveRealsedOc;
	@JsonProperty("LossReserveReleasedOc")
	private String lossReserveReleasedOc;
	@JsonProperty("InterestOc")
	private String interestOc;
	@JsonProperty("CashLossCreditOc")
	private String cashLossCreditOc;
	@JsonProperty("TotalCrOc")
	private String totalCrOc;
	@JsonProperty("CommissionQuotashareOc")
	private String commissionQuotashareOc;
	@JsonProperty("CommissionSurplusOc")
	private String commissionSurplusOc;
	@JsonProperty("BrokerageAmtOc")
	private String brokerageAmtOc;
	@JsonProperty("TaxAmtOc")
	private String taxAmtOc;
	@JsonProperty("OtherCostOc")
	private String otherCostOc;
	@JsonProperty("XlCostOc")
	private String xlCostOc;
	@JsonProperty("PremiumPortfoliooutOc")
	private String premiumPortfoliooutOc;
	@JsonProperty("ClaimPortfolioOutOc")
	private String claimPortfolioOutOc;
	@JsonProperty("PremiumReserveRetainedOc")
	private String premiumReserveRetainedOc;
	@JsonProperty("LossReserveretainedOc")
	private String lossReserveretainedOc;
	@JsonProperty("CashLosspaidOc")
	private String cashLosspaidOc;
	@JsonProperty("ClaimsPaidOc")
	private String claimsPaidOc;
	@JsonProperty("NetdueOc")
	private String netdueOc;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	@JsonProperty("PremiumQuotashareDc")
	private String premiumQuotashareDc;
	@JsonProperty("PremiumSurplusDc")
	private String premiumSurplusDc;
	@JsonProperty("PremiumPortfolioinDc")
	private String premiumPortfolioinDc;
	@JsonProperty("ClaimPortfolioinDc")
	private String claimPortfolioinDc;
	@JsonProperty("PremiumReserveRealsedDc")
	private String premiumReserveRealsedDc;
	@JsonProperty("LossReserveReleasedDc")
	private String lossReserveReleasedDc;
	@JsonProperty("InterestDc")
	private String interestDc;
	@JsonProperty("cashLossCreditDc")
	private String cashLossCreditDc;
	@JsonProperty("TotalCrDc")
	private String totalCrDc;
	@JsonProperty("CommissionQuotashareDc")
	private String commissionQuotashareDc;
	@JsonProperty("CommissionSurplusDc")
	private String commissionSurplusDc;
	@JsonProperty("BrokerageAmtDc")
	private String brokerageAmtDc;
	@JsonProperty("TaxAmtDc")
	private String taxAmtDc;
	@JsonProperty("OtherCostDc")
	private String otherCostDc;
	@JsonProperty("XlCostDc")
	private String xlCostDc;
	@JsonProperty("PremiumPortfoliooutDc")
	private String premiumPortfoliooutDc;
	@JsonProperty("ClaimPortfolioOutDc")
	private String claimPortfolioOutDc;
	@JsonProperty("PremiumReserveRetainedDc")
	private String premiumReserveRetainedDc;
	@JsonProperty("lossReserveRetainedDc")
	private String lossReserveRetainedDc;
	@JsonProperty("CashLosspaidDc")
	private String cashLosspaidDc;
	@JsonProperty("ClaimsPaidDc")
	private String claimsPaidDc;
	@JsonProperty("NetdueDc")
	private String netdueDc;
	@JsonProperty("MdPremiumOc")
	private String mdPremiumOc;
	@JsonProperty("AdjustmentPremiumOc")
	private String adjustmentPremiumOc;
	@JsonProperty("RecPremiumOc")
	private String recPremiumOc;
	@JsonProperty("MdPremiumDc")
	private String mdPremiumDc;
	@JsonProperty("AdjustmentPremiumDc")
	private String adjustmentPremiumDc;
	@JsonProperty("RecPremiumDc")
	private String recPremiumDc;
	@JsonProperty("Transactiontype")
	private String transactiontype;
	@JsonProperty("Subclass")
	private String subclass;
	@JsonProperty("Inwardtype")
	private String inwardtype;
	@JsonProperty("RetroCeding")
	private String retroCeding;
	@JsonProperty("Retrobroker")
	private String retrobroker;
	@JsonProperty("RetroInception")
	private String retroInception;
	@JsonProperty("RetroExpiry")
	private String retroExpiry;
	@JsonProperty("RetroUwy")
	private String retroUwy;
	@JsonProperty("PremiumOc")
	private String premiumOc;
	@JsonProperty("CommissionOc")
	private String commissionOc;
	@JsonProperty("Profitcommissionoc")
	private String profitcommissionoc;
	@JsonProperty("netdueUgx")
	private String netdueUgx;
	@JsonProperty("PremiumUgx")
	private String premiumUgx;
	@JsonProperty("OverriderAmtOc")
	private String overriderAmtOc;
	@JsonProperty("OutwardoverriderOc")
	private String outwardoverriderOc;
	@JsonProperty("OverriderAmtDc")
	private String overriderAmtDc;
	@JsonProperty("ProfitCommissionDc")
	private String profitCommissionDc;
	@JsonProperty("OutwaroverriderDc")
	private String outwaroverriderDc;

}
