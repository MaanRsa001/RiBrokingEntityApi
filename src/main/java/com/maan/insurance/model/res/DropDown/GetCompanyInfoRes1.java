package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.nonproportionality.GetLayerInfoRes;
import com.maan.insurance.model.res.nonproportionality.GetLayerInfoRes1;

import lombok.Data;

@Data
public class GetCompanyInfoRes1 {
	@JsonProperty("CompanyCode")
	private String companyCode;
	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("Email")
	private String email;
	@JsonProperty("Country")
	private String country;
	@JsonProperty("WebSite")
	private String webSite;
	@JsonProperty("Mobile")
	private String mobile;
	@JsonProperty("HeaderLogo")
	private String headerLogo;
	@JsonProperty("MainLogo")
	private String mainLogo;
}
