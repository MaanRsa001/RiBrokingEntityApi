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
}

