package com.maan.insurance.model.res.proportionality;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.proportionality.InstalmentListRes;
import com.maan.insurance.model.res.proportionality.ViewRiskDetailsRes1;

import lombok.Data;
@Data
public class MappingProposalRes {
	@JsonProperty("MappingProposal")
	private String mappingProposal;
	
	@JsonProperty("MapingAmendId")
	private String mapingAmendId;
}
