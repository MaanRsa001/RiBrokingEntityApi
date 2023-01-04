package com.maan.insurance.service.retroManualAdj;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.retroManualAdj.InsertPremiumReq;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.retroManualAdj.GetPremiumDetailsRes;
import com.maan.insurance.model.res.retroManualAdj.GetRetroDetailsRes;
import com.maan.insurance.model.res.retroManualAdj.GetRetroManualAdjlistRes;
import com.maan.insurance.model.res.retroManualAdj.PremiumEditRes;

@Service
public interface RetroManualAdjService {

	GetRetroManualAdjlistRes getRetroManualAdjlist(String branchCode, String productId);

	PremiumEditRes premiumEdit(String contractNo, String transactionNo, String countyId, String branchCode);

	GetPremiumDetailsRes getPremiumDetails(String contractNo, String transactionNo, String branchCode);

	GetRetroDetailsRes getRetroDetails(String branchCode, String contractNo);

	CommonSaveRes InsertPremium(InsertPremiumReq req);

}
