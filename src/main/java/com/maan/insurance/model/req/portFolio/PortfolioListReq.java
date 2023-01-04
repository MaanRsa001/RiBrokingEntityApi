package com.maan.insurance.model.req.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PortfolioListReq {
	@JsonProperty("PremiumClass")
	private String premiumClass;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("CurrencyId")
	private String currencyId;
	@JsonProperty("TransactionDate")
	private String transactionDate;
	@JsonProperty("ExchangeRate")
	private String exchangeRate;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("InstallmentDate")
	private String installmentDate;
	@JsonProperty("InstallmentNo")
	private String installmentNo;
	@JsonProperty("PremiumOC")
	private String premiumOC;
	@JsonProperty("Commission")
	private String commission;
	@JsonProperty("CommissionOC")
	private String commissionOC;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("BrokerageOC")
	private String brokerageOC;
	@JsonProperty("Tax")
	private String tax;
	@JsonProperty("TaxOC")
	private String taxOC;
	@JsonProperty("ReceiveDate")
	private String receiveDate;
	@JsonProperty("OtherCostOC")
	private String otherCostOC;
	@JsonProperty("Reference")
	private String reference;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("WithHoldingTaxOC")
	private String withHoldingTaxOC;
	@JsonProperty("PremiumSubClass")
	private String premiumSubClass;
	@JsonProperty("NetDueOC")
	private String netDueOC; 
	
	@JsonProperty("TaxDedectSource")
	private String taxDedectSource;
	@JsonProperty("ServiceTax")
	private String serviceTax;
	@JsonProperty("Bonus")
	private String bonus;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
}
