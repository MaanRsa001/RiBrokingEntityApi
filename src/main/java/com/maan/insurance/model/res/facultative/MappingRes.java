package com.maan.insurance.model.res.facultative;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.InstalListRes;

import lombok.Data;

@Data
public class MappingRes {
	@JsonProperty("MappingProposal")
	private String mappingProposal;
	@JsonProperty("MapingAmendId")
	private String mapingAmendId;
}
