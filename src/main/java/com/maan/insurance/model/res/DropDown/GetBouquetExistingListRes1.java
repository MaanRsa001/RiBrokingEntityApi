package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetBouquetExistingListRes1 {
	@JsonProperty("InsDate")
	private String insDate;
	@JsonProperty("ExpDate")
	private String expDate;
	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("UwYear")
	private String uwYear;
	@JsonProperty("UwYearTo")
	private String uwYearTo;
	@JsonProperty("Treatytype")
	private String treatytype;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("BusinessType")
	private String businessType;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	//@JsonProperty("TreatyType1")
	//private String treatyType1;
	@JsonProperty("RskTreatyid")
	private String rskTreatyid;
	@JsonProperty("PolicyStatus")
	private String policyStatus;
	@JsonProperty("ExistingShare")
	private String existingShare;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("SectionNo")
	private String sectionNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("TmasDepartmentName")
	private String tmasDepartmentName;
	@JsonProperty("SubClass")
	private String subClass;
	@JsonProperty("OfferNo")
	private String offerNo;
	
}
