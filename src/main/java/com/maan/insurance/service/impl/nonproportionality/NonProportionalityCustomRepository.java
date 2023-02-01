package com.maan.insurance.service.impl.nonproportionality;

import java.util.List;

import javax.persistence.Tuple;

public interface NonProportionalityCustomRepository {

	String getShortName(String branchCode);

	List<Tuple> riskSelectGetSecPageData(String proposalNo, String branchCode, String productId);

	List<Tuple> getRemarksDetails(String proposalNo, String layerNo);

	List<Tuple> facSelectViewInsDetails(String[] args);

	List<Tuple> facSelectInsDetails(String[] args);

	List<Tuple> facSelectRetrocontdet23(String productId, String uwYear, String incepDate, String branchCode);

	List<Tuple> riskSelectGetRskProIdByProNo(String proposalNo);

}
