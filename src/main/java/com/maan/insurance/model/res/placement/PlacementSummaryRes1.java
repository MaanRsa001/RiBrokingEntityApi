package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes1;

import lombok.Data;

@Data
public class PlacementSummaryRes1 {

	@JsonProperty("OfferNo")
	private String offerNo;
	@JsonProperty("BaseProposal")
	private String baseProposal;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("RskTreatyid")
	private String rskTreatyid;
	@JsonProperty("LayerSection")
	private String layerSection;
	@JsonProperty("Sno")
	private String sno;
	@JsonProperty("ReinsurerName")
	private String reinsurerName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("Currency")
	private String currency;
	@JsonProperty("Epi100Oc")
	private String epi100Oc;
	@JsonProperty("Epi100Dc")
	private String epi100Dc;
	@JsonProperty("PlacingStatus")
	private String placingStatus;
	@JsonProperty("ShareSigned")
	private String shareSigned;
	@JsonProperty("Brokerage")
	private String brokerage;
	@JsonProperty("BrokerageAmt")
	private String brokerageAmt;

}
