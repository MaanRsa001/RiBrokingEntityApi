package com.maan.insurance.service.impl.billing;

public interface BillingCustomRepository {

	int updateAlloTranDtls(String[] updateArgs);
	public Integer getNextRetDtlsNo();
}

