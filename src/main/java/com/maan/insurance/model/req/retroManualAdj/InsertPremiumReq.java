package com.maan.insurance.model.req.retroManualAdj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.premium.GetCashLossCreditReq1;
import com.maan.insurance.model.req.premium.InsertLossReserved;
import com.maan.insurance.model.req.premium.InsertPremiumReserved;

import lombok.Data;

@Data
public class InsertPremiumReq {

	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("Transaction")
	private String Transaction;
	
	@JsonProperty("ContNo")
	private String contNo;

	@JsonProperty("TransactionNo")
	private String transactionNo;
	
	@JsonProperty("LoginId")
	private String  loginId;
	
	@JsonProperty("ExchRate")
	private String exchRate;

	@JsonProperty("Brokerage")
	private String brokerage;

	
	@JsonProperty("Tax")
	private String tax;
	
	@JsonProperty("Overrider")
	private String overrider;

	
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	
	@JsonProperty("InceptionDate")
	private String inceptionDate;

	@JsonProperty("MaxDate")
	private String maxDate;
	
	@JsonProperty("PremiumQuotaShare")
	private String  premiumQuotaShare;
	
	@JsonProperty("CommissionQuotaShare")
	private String commissionQuotaShare;
	
	@JsonProperty("PremiumSurplus")
	private String premiumSurplus;
	
	@JsonProperty("CommissionSurplus")
	private String commissionSurplus;
	
	@JsonProperty("PremiumportifolioIn")
	private String premiumportifolioIn;

	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	
	@JsonProperty("ServiceTax")
	private String serviceTax;
	
	@JsonProperty("SlideScaleCom")
	private String  slideScaleCom;

	@JsonProperty("Predepartment")
	private String predepartment;
	
	@JsonProperty("SubProfitId")
	private String subProfitId;

	
	@JsonProperty("LossParticipation")
	private String lossParticipation;

	@JsonProperty("Mode")
	private String mode;
	

	
	@JsonProperty("CliamPortfolioin")
	private String cliamPortfolioin;
	
	@JsonProperty("Premiumportifolioout")
	private String premiumportifolioout;
	
	@JsonProperty("LossReserveReleased")
	private String lossReserveReleased;
	
	@JsonProperty("PremiumReserveQuotaShare")
	private String premiumReserveQuotaShare;
	
	@JsonProperty("CashLossCredit")
	private String  cashLossCredit;
	
	@JsonProperty("LossReserveRetained")
	private String lossReserveRetained;
	
	
	@JsonProperty("ProfitCommission")
	private String profitCommission;
	
	@JsonProperty("CashLossPaid")
	private String cashLossPaid;

	
	@JsonProperty("Claimspaid")
	private String claimspaid;
	
	@JsonProperty("Settlementstatus")
	private String settlementstatus;
	
	@JsonProperty("XlCost")
	private String xlCost;
	
	@JsonProperty("Cliamportfolioout")
	private String  cliamportfolioout;
	
	@JsonProperty("PremiumReserveReleased")
	private String premiumReserveReleased;
	
	@JsonProperty("OtherCost")
	private String otherCost;

	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("TotalCredit")
	private String totalCredit;
	
	@JsonProperty("TotalDebit")
	private String totalDebit;
	
	@JsonProperty("Interest")
	private String interest;
	
	@JsonProperty("OsClaimsLossUpdateOC")
	private String  osClaimsLossUpdateOC;

	@JsonProperty("Layerno")
	private String layerno;
	
	@JsonProperty("AmendmentDate")
	private String AmendmentDate;


	@JsonProperty("Currency")
	private String currency;

	@JsonProperty("SourceId")
	private String sourceId;
	
	@JsonProperty("BusinessType")
	private String businessType;
	@JsonProperty("Reference")
	private String reference;
	@JsonProperty("TreatyName")
	private String treatyName;
	@JsonProperty("UwYear")
	private String uwYear;


	

}
