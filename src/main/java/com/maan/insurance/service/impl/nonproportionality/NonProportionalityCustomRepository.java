package com.maan.insurance.service.impl.nonproportionality;

import java.util.List;

import javax.persistence.Tuple;

public interface NonProportionalityCustomRepository {

	String getShortName(String branchCode);

	List<Tuple> riskSelectGetSecPageData(String proposalNo, String branchCode, String productId);

}
