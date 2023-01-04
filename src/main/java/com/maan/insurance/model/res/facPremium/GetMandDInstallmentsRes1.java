package com.maan.insurance.model.res.facPremium;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetMandDInstallmentsRes1 {
	@JsonProperty("InstallmentNo")
	private String installmentNo;
}
