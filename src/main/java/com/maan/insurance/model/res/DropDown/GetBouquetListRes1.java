package com.maan.insurance.model.res.DropDown;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.retro.CommonSaveRes;

import lombok.Data;

@Data
public class GetBouquetListRes1 {
	@JsonProperty("BouquetNo")
	private String bouquetNo;
}
