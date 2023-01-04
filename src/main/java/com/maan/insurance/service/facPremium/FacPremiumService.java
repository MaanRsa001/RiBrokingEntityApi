package com.maan.insurance.service.facPremium;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.facPremium.AddFieldValueReq;
import com.maan.insurance.model.req.facPremium.BonusdetailsReq;
import com.maan.insurance.model.req.facPremium.GetBonusValueReq;
import com.maan.insurance.model.req.facPremium.GetFieldValuesReq;
import com.maan.insurance.model.req.facPremium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.facPremium.PremiumContractDetailsReq;
import com.maan.insurance.model.req.facPremium.PremiumEditReq;
import com.maan.insurance.model.req.facPremium.PremiumInsertMethodReq;
import com.maan.insurance.model.req.facPremium.PremiumedListreq;
import com.maan.insurance.model.res.facPremium.BonusdetailsRes;
import com.maan.insurance.model.res.facPremium.CommonResponse;
import com.maan.insurance.model.res.facPremium.ContractDetailsRes;
import com.maan.insurance.model.res.facPremium.GetAllocatedListRes;
import com.maan.insurance.model.res.facPremium.GetCommonValueRes;
import com.maan.insurance.model.res.facPremium.GetDepartmentIdRes;
import com.maan.insurance.model.res.facPremium.GetFieldValuesRes;
import com.maan.insurance.model.res.facPremium.GetMandDInstallmentsRes;
import com.maan.insurance.model.res.facPremium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.facPremium.MdInstallmentDatesRes;
import com.maan.insurance.model.res.facPremium.PreListRes1;
import com.maan.insurance.model.res.facPremium.PremiumContractDetailsRes;
import com.maan.insurance.model.res.facPremium.PremiumEditRes;
import com.maan.insurance.model.res.facPremium.PremiumTempRes1;
import com.maan.insurance.model.res.facPremium.PremiumedListRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;

@Service
public interface FacPremiumService {

	PremiumedListRes getPremiumedList(PremiumedListreq req);

	PreListRes1 getPreList(String contNo,String deptId);

	PremiumTempRes1 getpremiumTempList(String contNo, String branchCode);

	GetCommonValueRes getPreviousPremium(String contNo);

	GetCommonValueRes getContractPremium(String contNo, String branchCode);

	ContractDetailsRes contractDetails(String contNo, String branchCode, String productId);

	MdInstallmentDatesRes mdInstallmentDates(String contNo, String layerNo);

	GetDepartmentIdRes getDepartmentId(String contNo, String productId);

	PremiumEditRes premiumEdit(PremiumEditReq req);

	CommonResponse premiumInsertMethod(PremiumInsertMethodReq req);

	CommonResponse premiumUpdateMethod(PremiumInsertMethodReq req);

	GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req);

	GetCommonValueRes getInstalmentAmount(String contNo, String instalmentno);

	GetAllocatedListRes getAllocatedList(String contNo, String transactionNo);

	CurrencyListRes currencyList(String branchCode);

	PremiumContractDetailsRes premiumContractDetails(String contNo, String branchCode, String layerNo);

	GetFieldValuesRes getFieldValues(GetFieldValuesReq req);

	BonusdetailsRes bonusdetails(BonusdetailsReq req);

	CommonResponse addFieldValue(AddFieldValueReq req);

	GetMandDInstallmentsRes getMandDInstallments(String contNo, String layerNo);


//	GetCommonValueRes getBonusValue(GetFieldValuesReq bean, double ans);


}
