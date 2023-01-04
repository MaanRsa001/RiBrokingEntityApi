package com.maan.insurance.model.res.menu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes1;

import lombok.Data;

@Data
public class GetAdminMenuRes1 {
	@JsonProperty("Content")
	private String content;
	@JsonProperty("URL")
	private String url;
}
