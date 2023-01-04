package com.maan.insurance.model.req.nonproportionality;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShowSecondpageEditItemsReq {
	@JsonProperty("ProposalNo")
	private String proposalNo;
	
	@JsonProperty("LayerLayerNo")
	private String layerLayerNo;
	
	@JsonProperty("BranchCode")
	private String branchCode;

	@JsonProperty("LayerNo")
	private String layerNo;
	
	@JsonProperty("ProductId")
	private String productId;
	
	@JsonProperty("AnualAggregateLiability")
	private String anualAggregateLiability;
	
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber;
	
	@JsonProperty("PaymentDuedays")
	private String paymentDuedays;
	
	@JsonProperty("NoInsurer")
	private String noInsurer;
	
	@JsonProperty("IncepDate")
	private String incepDate;
}
