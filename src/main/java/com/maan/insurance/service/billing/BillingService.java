package com.maan.insurance.service.billing;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.model.res.billing.GetTransContractResRi;
import com.maan.insurance.model.res.billing.InsertBillingInfoRes;

@Service
public interface BillingService {

	InsertBillingInfoRes insertBillingInfo(InsertBillingInfoReq req);

	GetTransContractResRi getTransContract(GetTransContractReqRi req);

}
