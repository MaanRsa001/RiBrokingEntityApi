package com.maan.insurance.service.impl.Dropdown;

import java.util.List;

import javax.persistence.Tuple;

import com.maan.insurance.model.req.DropDown.GetTransactionListReq;


public interface DropDownCustomRepository {

	List<Tuple> getExistingBouquet(String branchCode, String bouquetNo);

	List<Tuple> getExistingBaselayer(String branchCode, String baseProposalNo);

	List<Tuple> newContractSummary(String branchCode, String proposalNo);

	List<Tuple> getReinsurerInfo(String branchCode, String layerProposalNo);

	String getBaseProposalNo(String proposalNo);

	List<Tuple> getSubStatusInfo(String approvelStatus);
	
	List<Tuple> getPaymentPartnerlist(String branchCode, String cedingId, String brokerId);

	List<Tuple> transactionNoList(GetTransactionListReq req);

	List<Tuple> transactionNoListRetro(GetTransactionListReq req);


}
