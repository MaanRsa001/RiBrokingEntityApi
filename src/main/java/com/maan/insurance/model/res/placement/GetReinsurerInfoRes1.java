package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.DropDown.GetBouquetCedentBrokerInfoRes;
import com.maan.insurance.model.res.DropDown.GetBouquetCedentBrokerInfoRes1;

import lombok.Data;

@Data
public class GetReinsurerInfoRes1 {
	@JsonProperty("ReinsSNo")
	private String reinsSNo;
	
	@JsonProperty("ReinsureName")
	private String reinsureName;
	
	@JsonProperty("PlacingBroker")
	private String placingBroker;
	
	@JsonProperty("ShareOffer")
	private String shareOffer;
	
	@JsonProperty("MailStatus")
	private String mailStatus;
	
	@JsonProperty("ProposalNos")
	private String proposalNos;
	
	@JsonProperty("DeleteStatus")
	private String deleteStatus;
	
	@JsonProperty("ChangeStatus")
	private String changeStatus;
}
