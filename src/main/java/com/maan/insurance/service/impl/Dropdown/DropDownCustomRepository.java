package com.maan.insurance.service.impl.Dropdown;

import java.util.List;

import javax.persistence.Tuple;


public interface DropDownCustomRepository {

	List<Tuple> getExistingBouquet(String branchCode, String bouquetNo);

	List<Tuple> getExistingBaselayer(String branchCode, String baseProposalNo);

	List<Tuple> newContractSummary(String branchCode, String proposalNo);

	List<Tuple> getReinsurerInfo(String branchCode, String layerProposalNo);

	String getBaseProposalNo(String proposalNo);

	List<Tuple> getPaymentPartnerlist(String branchCode, String cedingId, String brokerId);

}
