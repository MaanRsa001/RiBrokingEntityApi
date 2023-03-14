package com.maan.insurance.service.XolPremium;


import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.premium.GetRIPremiumListReq;
import com.maan.insurance.model.req.xolPremium.ContractDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetAdjPremiumReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.xolPremium.GetPremiumedListReq;
import com.maan.insurance.model.req.xolPremium.MdInstallmentDatesReq;
import com.maan.insurance.model.req.xolPremium.PremiumEditReq;
import com.maan.insurance.model.req.xolPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.GetRIPremiumListRes;
import com.maan.insurance.model.res.xolPremium.CommonResponse;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.model.res.xolPremium.ContractDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetBrokerAndCedingNameRes;
import com.maan.insurance.model.res.xolPremium.GetPreListRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.xolPremium.GetPremiumedListRes;
import com.maan.insurance.model.res.xolPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.xolPremium.PremiumEditRes;
import com.maan.insurance.model.res.xolPremium.premiumInsertMethodRes;



@Service
public interface XolPremiumService {

	//CommonSaveRes getRPPremiumOC(String contractNo, String layerNo, String productId);
	
//	GetSPRetroListRes getSPRetroList(String contNo);

//	GetRetroContractsRes getRetroContracts(GetRetroContractsReq req);
	
//	GetSumOfShareSignRes getSumOfShareSign(String contNo);


	GetPremiumedListRes getPremiumedList(GetPremiumedListReq req);

	CommonSaveRes getContractPremium(String contractNo, String layerNo);

	CommonSaveRes getPreviousPremium(String contractNo);

	MdInstallmentDatesRes mdInstallmentDates(MdInstallmentDatesReq req);

	ContractDetailsRes contractDetails(ContractDetailsReq req);

	GetPreListRes getPreList(String contNo, String layerNo);

	GetDepartmentIdRes getDepartmentId(String contNo, String productId);

	PremiumEditRes premiumEdit(PremiumEditReq req);

	premiumInsertMethodRes premiumInsertMethod(PremiumInsertMethodReq req);

	premiumInsertMethodRes premiumUpdateMethod(PremiumInsertMethodReq req);

	GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req);

	CommonSaveRes getInstalmentAmount(String contNo, String layerNo,String instalmentno);

	GetBrokerAndCedingNameRes getBrokerAndCedingName(String contNo, String branchCode);

	CurrencyListRes currencyList(String branchCode);

	GetAllocatedListRes getAllocatedList(String contNo, String transactionNo);

	CommonSaveRes getAdjPremium(GetAdjPremiumReq req);

	GetRIPremiumListRes getRIPremiumList(GetRIPremiumListReq req);

	GetPremiumDetailsRes getPremiumDetailsRi(GetPremiumDetailsReq req);

	PremiumEditRes premiumEditRi(PremiumEditReq req);

	CommonResponse premiumUpdateMethodRi(PremiumInsertMethodReq req);

	CommonSaveRes getOfferNoCount(String offerNo);



}


