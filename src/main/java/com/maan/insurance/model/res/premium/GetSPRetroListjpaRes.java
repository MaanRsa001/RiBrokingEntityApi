package com.maan.insurance.model.res.premium;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetSPRetroListjpaRes {
	
	@JsonProperty("SpRetro")
	private String spRetro;
	
	@JsonProperty("NoOfInsurers")
	private BigDecimal noOfInsurers;
	
	
	@JsonProperty("ProposalNo")
	private String proposalNo;
}
