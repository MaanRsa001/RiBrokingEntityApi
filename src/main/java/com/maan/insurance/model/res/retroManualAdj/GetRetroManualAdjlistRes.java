package com.maan.insurance.model.res.retroManualAdj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.res.journal.GetJournalViewsRes;
import com.maan.insurance.model.res.journal.GetJournalViewsRes1;

import lombok.Data;

@Data
public class GetRetroManualAdjlistRes {
	@JsonProperty("Result")
	private List<GetRetroManualAdjlistRes1> commonResponse;
	
	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")
	private Boolean isError;

	@JsonProperty("ErrorMessage")
	private List<ErrorCheck> errors;

	@JsonProperty("ErroCode")
	private int erroCode;
}
