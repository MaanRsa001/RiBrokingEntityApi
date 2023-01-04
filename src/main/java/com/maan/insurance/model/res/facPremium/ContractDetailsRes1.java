package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ContractDetailsRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("ProfitCenter")
	private String profitCenter;
	@JsonProperty("SubProfitcenter")
	private String subProfitcenter;
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("Broker")
	private String broker; 
	@JsonProperty("TreatyNametype")
	private String treatyNametype;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("InsDate")
	private String insDate;
	@JsonProperty("ExpDate")
	private String expDate;
	@JsonProperty("Month")
	private String month;
	@JsonProperty("BaseCurrencyId")
	private String baseCurrencyId;
	@JsonProperty("BaseCurrencyName")
	private String baseCurrencyName;
	@JsonProperty("Insuredname")
	private String insuredname;
	@JsonProperty("Address")
	private String address;
	@JsonProperty("TdsRate")
	private String tdsRate;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("Predepartment")
	private String predepartment;
	@JsonProperty("ConsubProfitId")
	private String consubProfitId;
	@JsonProperty("SubProfitId")
	private String subProfitId;
	@JsonProperty("AcceptenceDate")
	private String acceptenceDate;
	@JsonProperty("Commissionview")
	private String commissionview; 
	@JsonProperty("CommissionSurbview")
	private String commissionSurbview;
	@JsonProperty("OverRiderview")
	private String overRiderview;
	@JsonProperty("Brokerageview")
	private String brokerageview;
	@JsonProperty("Taxview")
	private String taxview;
	@JsonProperty("OtherCostView")
	private String otherCostView;
	@JsonProperty("ShareSigned")
	private String shareSigned;
	@JsonProperty("GwpiOS")
	private String gwpiOS;
	@JsonProperty("RdsExchageRate")
	private String rdsExchageRate;
	@JsonProperty("Sumofpaidpremium")
	private String sumofpaidpremium;
	@JsonProperty("Epibalance")
	private String epibalance;
	@JsonProperty("CurrencyName")
	private String currencyName; 
	@JsonProperty("SaveFlag")
	private String saveFlag;
}
