package com.maan.insurance.model.res.placement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.req.placement.ReinsListReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;

import lombok.Data;

@Data
public class GetPlacingInfoRes1 {
	@JsonProperty("Sno")
	private String sno;
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
}
