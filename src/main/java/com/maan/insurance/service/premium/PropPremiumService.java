package com.maan.insurance.service.premium;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.premium.ClaimTableListReq;
import com.maan.insurance.model.req.premium.ContractDetailsReq;
import com.maan.insurance.model.req.premium.GetCashLossCreditReq;
import com.maan.insurance.model.req.premium.GetCassLossCreditReq;
import com.maan.insurance.model.req.premium.GetConstantPeriodDropDownReq;
import com.maan.insurance.model.req.premium.GetPreListReq;
import com.maan.insurance.model.req.premium.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.GetPremiumReservedReq;
import com.maan.insurance.model.req.premium.GetPremiumedListReq;
import com.maan.insurance.model.req.premium.GetRIPremiumListReq;
import com.maan.insurance.model.req.premium.GetSPRetroListReq;
import com.maan.insurance.model.req.premium.GetVatInfoReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.req.premium.PremiumEditReq;
import com.maan.insurance.model.req.premium.PremiumUpdateMethodReq;
import com.maan.insurance.model.req.premium.PremiumUpdateMethodRiReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq;
import com.maan.insurance.model.req.premium.CashLossmailTriggerReq;
import com.maan.insurance.model.req.premium.InsertReverseCashLossCreditReq;
import com.maan.insurance.model.res.premium.ClaimTableListMode1Res;
import com.maan.insurance.model.res.premium.ContractDetailsRes;
import com.maan.insurance.model.res.premium.CurrencyListRes;
import com.maan.insurance.model.res.premium.GetAllocatedCassLossCreditRes;
import com.maan.insurance.model.res.premium.GetAllocatedListRes;
import com.maan.insurance.model.res.premium.GetAllocatedTransListRes;
import com.maan.insurance.model.res.premium.GetBrokerAndCedingNameRes;
import com.maan.insurance.model.res.premium.GetCashLossCreditRes;
import com.maan.insurance.model.res.premium.GetClaimNosDropDownRes;
import com.maan.insurance.model.res.premium.GetConstantPeriodDropDownRes;
import com.maan.insurance.model.res.premium.GetContractPremiumRes;
import com.maan.insurance.model.res.premium.GetCountCleanCUTRes;
import com.maan.insurance.model.res.premium.GetDepartmentNoRes;
import com.maan.insurance.model.res.premium.GetDepositReleaseCountRes;
import com.maan.insurance.model.res.premium.GetOSBListRes;
import com.maan.insurance.model.res.premium.GetPreListRes;
import com.maan.insurance.model.res.premium.GetPremiumDetailsRes;
import com.maan.insurance.model.res.premium.GetPremiumReservedRes1;
import com.maan.insurance.model.res.premium.GetPremiumedListRes;
import com.maan.insurance.model.res.premium.GetPreviousPremiumRes;
import com.maan.insurance.model.res.premium.GetRIPremiumListRes;
import com.maan.insurance.model.res.premium.GetRetroContractsRes;
import com.maan.insurance.model.res.premium.GetSPRetroListRes;
import com.maan.insurance.model.res.premium.GetSumOfShareSignRes;
import com.maan.insurance.model.res.premium.GetVatInfoRes;
import com.maan.insurance.model.res.premium.InsertPremiumRes;
import com.maan.insurance.model.res.premium.PremiumEditRes;
import com.maan.insurance.model.res.premium.SubmitPremiumReservedRes;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIReq;
import com.maan.insurance.model.res.premium.ViewPremiumDetailsRIRes;
import com.maan.insurance.model.res.premium.ViewRIPremiumListRes;
import com.maan.insurance.model.res.premium.getCurrencyShortNameRes;
import com.maan.insurance.model.res.premium.getReverseCassLossCreditRes;
import com.maan.insurance.model.res.premium.premiumUpdateMethodRes;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.model.res.retro.CommonResponse;

@Service
public interface PropPremiumService {

	GetPremiumedListRes getPremiumedList(GetPremiumedListReq req);

	GetPreListRes getPreList(GetPreListReq req);

	GetConstantPeriodDropDownRes getConstantPeriodDropDown(GetConstantPeriodDropDownReq req);

	GetPreviousPremiumRes getPreviousPremium(String contractNo);

	GetContractPremiumRes getContractPremium(String contractNo, String departmentId, String branchCode);

	GetClaimNosDropDownRes getClaimNosDropDown(String contractNo);

	ContractDetailsRes contractDetails(ContractDetailsReq req);

	ClaimTableListMode1Res claimTableListMode1(ClaimTableListReq req);

	GetCountCleanCUTRes getCountCleanCUT(String contractNo);

	GetSPRetroListRes getSPRetroList(GetSPRetroListReq req);

	GetRetroContractsRes getRetroContracts(String proposalNo, String noOfRetro);

	GetSumOfShareSignRes getSumOfShareSign(String retroContractNo);

	GetDepartmentNoRes getDepartmentNo(String contractNo);

	GetOSBListRes getOSBList(String transaction, String contractNo, String branchCode);

	InsertPremiumRes insertPremium(InsertPremiumReq req);

	GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req);

	PremiumEditRes premiumEdit(PremiumEditReq req);

	GetBrokerAndCedingNameRes getBrokerAndCedingName(String contNo, String branchCode);

	GetAllocatedListRes getAllocatedList(String contNo, String transactionNo);

	CurrencyListRes currencyList(String branchCode);

	GetPremiumReservedRes1 getPremiumReserved(GetPremiumReservedReq req);

	GetCashLossCreditRes getCassLossCredit(GetCassLossCreditReq req);

	GetAllocatedTransListRes getAllocatedTransList(String proposalNo);

	GetAllocatedCassLossCreditRes getAllocatedCassLossCredit(String proposalNo, String branchCode);

	SubmitPremiumReservedRes submitPremiumReserved(SubmitPremiumReservedReq req);

	GetDepositReleaseCountRes getDepositReleaseCount(String dropDown, String contractNo, String branchCode, String type);

	premiumUpdateMethodRes premiumUpdateMethod(InsertPremiumReq req);

	ViewPremiumDetailsRIRes viewPremiumDetailsRI(ViewPremiumDetailsRIReq req);

	GetVatInfoRes getVatInfo(GetVatInfoReq req);

	ViewRIPremiumListRes viewRIPremiumList(GetRIPremiumListReq req);

	CommonResponse updateRIStatus(GetRIPremiumListReq req);

	CommonResponse InsertCashLossCredit(InsertPremiumReq req);

	CommonResponse InsertReverseCashLossCredit(InsertReverseCashLossCreditReq req);

	CommonResponse CashLossmailTrigger(CashLossmailTriggerReq req);

	getReverseCassLossCreditRes getReverseCassLossCredit(String proposalNo, String cashlosstranId);

	getCurrencyShortNameRes getCurrencyShortName(String currencyId, String branchCode);

	PremiumEditRes premiumEditRi(PremiumEditReq req);

	GetPremiumDetailsRes getPremiumDetailsRi(GetPremiumDetailsReq req);

	premiumUpdateMethodRes premiumUpdateMethodRi(InsertPremiumReq req);

	CommonSaveRes getOfferNoCount(String offerNo);


}
