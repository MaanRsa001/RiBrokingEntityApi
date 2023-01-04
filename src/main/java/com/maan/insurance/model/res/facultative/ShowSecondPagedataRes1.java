package com.maan.insurance.model.res.facultative;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes1;

import lombok.Data;

@Data
public class ShowSecondPagedataRes1 {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("SubProfitCenter")
	private String subProfitCenter;
	@JsonProperty("CedingCompany")
	private String cedingCompany;
	@JsonProperty("Broker")
	private String broker;
	@JsonProperty("Underwriter")
	private String underwriter;
	@JsonProperty("InsuredName")
	private String insuredName;
	@JsonProperty("DepartClass")
	private String departClass;
	@JsonProperty("Endttypename")
	private String endttypename;
//	@JsonProperty("InstalList")
//	private List<InstalListRes> instalList;
	@JsonProperty("PaymentDueDays")
	private List<String> 	paymentDueDays;
	@JsonProperty("InstalList")
	private List<String> instalList;
	
	@JsonProperty("RetroList")
	private List<RetroListRes> retroListRes;
	
	@JsonProperty("RetroDupList")
	private List<RetroDupListRes> RetroDupList;
}
