package com.maan.insurance.model.res.placement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetPlacingInfoRes1 {
	@JsonProperty("Sno")
	private String sno;
	@JsonProperty("ProposalNo")
	private String proposalNo;
	@JsonProperty("BouquetNo")
	private String bouquetNo;
	@JsonProperty("ReinsurerId")
	private String reinsurerId;
	@JsonProperty("BrokerId")
	private String brokerId;
	@JsonProperty("ReinsurerName")
	private String reinsurerName;
	@JsonProperty("BrokerName")
	private String brokerName;
	@JsonProperty("ShareOffered")
	private String shareOffered;
	@JsonProperty("OfferStatus")
	private String offerStatus;
	@JsonProperty("MailStatus")
	private String mailStatus;
}
