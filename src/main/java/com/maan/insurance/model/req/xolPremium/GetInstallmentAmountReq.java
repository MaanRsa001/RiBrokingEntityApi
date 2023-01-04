package com.maan.insurance.model.req.xolPremium;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes1;

import lombok.Data;

@Data
public class GetInstallmentAmountReq {
	@JsonProperty("ContNo")
	private String contNo;
	@JsonProperty("LayerNo")
	private String layerno;
	@JsonProperty("Instalmentno")
	private String instalmentno;

	

}
