package com.maan.insurance.model.req.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes1;

import lombok.Data;

@Data
public class MdInstallmentDatesReq {
	@JsonProperty("ProductId")
	private String productId;
	@JsonProperty("Layerno")
	private String layerno;
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("SourceId")
	private String sourceId;
	@JsonProperty("Mode")
	private String mode;
	

}
