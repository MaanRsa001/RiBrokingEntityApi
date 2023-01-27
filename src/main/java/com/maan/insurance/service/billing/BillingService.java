package com.maan.insurance.service.billing;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.billing.EditBillingInfoReq;
import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.model.res.billing.EditBillingInfoRes;
import com.maan.insurance.model.res.billing.GetBillingInfoListRes;
import com.maan.insurance.model.res.billing.GetTransContractResRi;
import com.maan.insurance.model.res.billing.InsertBillingInfoRes;
import com.maan.insurance.model.res.retro.CommonResponse;

@Service
public interface BillingService {

	CommonResponse insertBillingInfo(InsertBillingInfoReq req);

	GetTransContractResRi getTransContract(GetTransContractReqRi req);

	GetBillingInfoListRes getBillingInfoList(GetBillingInfoListReq req);

	GetTransContractResRi getTransContractRi(GetTransContractReqRi req);

	EditBillingInfoRes editBillingInfo(EditBillingInfoReq req);

}
