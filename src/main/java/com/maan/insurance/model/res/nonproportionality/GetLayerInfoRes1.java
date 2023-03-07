package com.maan.insurance.model.res.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetLayerInfoRes1 {
	@JsonProperty("OfferNo")
	private String offerNo;
	@JsonProperty("NewLayerNo")
	private String newLayerNo;
	@JsonProperty("LayerNo")
	private String layerNo;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("CedingCompanyId")
	private String cedingCompanyId;
	@JsonProperty("BaseLayer")
	private String baseLayer;
	@JsonProperty("ContractNo")
	private String contractNo;
	@JsonProperty("DeptId")
	private String deptId;
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("SectionNo")
	private String sectionNo;
	@JsonProperty("TmasDepartmentName")
	private String tmasDepartmentName;
	@JsonProperty("TreatyType")
	private String treatyType;
	@JsonProperty("RskTreatyid")
	private String rskTreatyid;

}
