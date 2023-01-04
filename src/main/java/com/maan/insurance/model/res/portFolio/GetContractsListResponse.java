package com.maan.insurance.model.res.portFolio;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetContractsListResponse {
	@JsonProperty("GetContractsList")
	private List<GetContractsListRes1> getContractsList;
//	@JsonProperty("Mode")
//	private String mode;
	@JsonProperty("Type")
	private String type;
}
