package com.maan.insurance.model.req.nonproportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsInstallReq {
	@JsonProperty("Installsno")
	private List<String> installsno; 
	@JsonProperty("MdInstalmentNumber")
	private String mdInstalmentNumber; 
	@JsonProperty("MdPremium")
	private String mdPremium ;
	@JsonProperty("InstalmentDetails")
	private List<InstalmentperiodReq> instalmentDetails;
}
