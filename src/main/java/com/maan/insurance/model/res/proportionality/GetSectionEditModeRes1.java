package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetSectionEditModeRes1 {
	@JsonProperty("CedingCo")
	private String cedingCo;

	@JsonProperty("IncepDate")
	private String incepDate;

	@JsonProperty("ExpDate")
	private String expDate;
	@JsonProperty("UwYear")
	private String uwYear;

	@JsonProperty("UwYearTo")
	private String uwYearTo;

	@JsonProperty("BouquetModeYN")
	private String bouquetModeYN;
	@JsonProperty("BouquetNo")
	private String bouquetNo;

	@JsonProperty("ProposalNo")
	private String proposalNo;

	@JsonProperty("BaseLayer")
	private String baseLayer;
	
	@JsonProperty("Endorsmentno")
	private String endorsmentno;
	@JsonProperty("ContNo")
	private String contNo;

	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("DepartId")
	private String departId;
}
