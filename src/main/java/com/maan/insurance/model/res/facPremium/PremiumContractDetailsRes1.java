package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PremiumContractDetailsRes1 {
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("AmendId")
	private String amendId;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("DepartmentId")
	private String departmentId;
	@JsonProperty("SubProfitcenter")
	private String subProfitcenter;
	@JsonProperty("CedingCo")
	private String cedingCo;
	@JsonProperty("TreatyNametype")
	private String treatyNametype;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("InsDate")
	private String insDate;
	@JsonProperty("ExpDate")
	private String expDate;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("Insuredname")
	private String insuredname;
	@JsonProperty("BonusId")
	private String bonusId;
	@JsonProperty("AcqBonusName")
	private String acqBonusName;
}
