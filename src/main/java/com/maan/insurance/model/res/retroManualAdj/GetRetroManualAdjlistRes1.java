package com.maan.insurance.model.res.retroManualAdj;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.insurance.error.ErrorCheck;

import lombok.Data;

@Data
public class GetRetroManualAdjlistRes1 {
	@JsonProperty("TransactionNo")
	private String transactionNo;
	@JsonProperty("TransactionMonthYear")
	private String transactionMonthYear;
	@JsonProperty("RetroContractNumber")
	private String retroContractNumber;
	@JsonProperty("Uwy")
	private String uwy;
	@JsonProperty("RetroBroker")
	private String retroBroker;
	@JsonProperty("RetroCessionaire")
	private String retroCessionaire;
	@JsonProperty("ProposalNo")
	private String proposalNo;
}
