package com.maan.insurance.service.impl.billing;

import java.util.List;

import javax.persistence.Tuple;

import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;

public interface BillingCustomRepository {

	int updateAlloTranDtls(String[] updateArgs);
	
	public Integer getNextRetDtlsNo();
	
	List<Tuple> getTranContDtls(GetTransContractReqRi req);

	List<Tuple> getBillingInfoList(GetBillingInfoListReq req);

	List<Tuple> getTranContDtlsRi(GetTransContractReqRi req);

	public Integer updatePremiumRiDetails(String[] updateArgs);

	public Integer updatepreRiSetStatus(String[] updateArgs);

	public Integer updateclaimRiPymtAlloDtls(String[] updateArgs);

	public Integer updateclaimRiSetStatus(String[] updateArgs);

	public Integer updateRskPremRiChkyn();


	List<Tuple> getAlloTransDtls(String receiptNo, String serialNo);

	String getSelCurrency(String branchCode, String currencyId);

	Integer updateAllocatedDtls(String[] args);

	Integer updateRskPremDtls(String[] args);

	List<Tuple> getRskPremDtls(String string, String string2);

	Integer updateClaimPymtDtls(String[] args);

	List<Tuple> getClaimPymtDtls(String string, String string2);

	Integer updatePymtRetDtls(String[] args);

	List<Tuple> getPymtRetDtls(String payRecNo, String curencyId);

	Integer updateRskPremRiDtls(String[] args);

	List<Tuple> getRskPremRiDtls(String string, String string2);

	Integer updateClaimPymtRiDtls(String[] args);

	List<Tuple> getClaimPymtRiDtls(String string, String string2);
}

