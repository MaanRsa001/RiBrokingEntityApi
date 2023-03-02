package com.maan.insurance.service.proportionality;


import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.proportionality.BaseLayerStatusReq;
import com.maan.insurance.model.req.proportionality.BonusSaveReq;
import com.maan.insurance.model.req.proportionality.CedentSaveReq;
import com.maan.insurance.model.req.proportionality.ConvertPolicyReq;
import com.maan.insurance.model.req.proportionality.CrestaSaveReq;
import com.maan.insurance.model.req.proportionality.FirstpageSaveReq;
import com.maan.insurance.model.req.proportionality.GetCrestaDetailListReq;
import com.maan.insurance.model.req.proportionality.GetRetentionDetailsReq;
import com.maan.insurance.model.req.proportionality.GetSectionDuplicationCheckReq;
import com.maan.insurance.model.req.proportionality.GetcalculateSCReq;
import com.maan.insurance.model.req.proportionality.GetprofitCommissionEnableReq;
import com.maan.insurance.model.req.proportionality.InsertCrestaDetailsReq;
import com.maan.insurance.model.req.proportionality.ProfitCommissionListReq;
import com.maan.insurance.model.req.proportionality.ProfitCommissionSaveReq;
import com.maan.insurance.model.req.proportionality.RemarksSaveReq;
import com.maan.insurance.model.req.proportionality.RetroSaveReq;
import com.maan.insurance.model.req.proportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.proportionality.ScaleCommissionInsertReq;
import com.maan.insurance.model.req.proportionality.SecondpageSaveReq;
import com.maan.insurance.model.req.proportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.proportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.proportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.proportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.req.proportionality.getScaleCommissionListReq;
import com.maan.insurance.model.req.proportionality.saveRiskDeatilsSecondFormReq;
import com.maan.insurance.model.req.proportionality.showSecondPageData1Req;
import com.maan.insurance.model.res.proportionality.BaseLayerStatusRes;
import com.maan.insurance.model.res.proportionality.CancelProposalRes;
import com.maan.insurance.model.res.proportionality.CheckProductMatchRes;
import com.maan.insurance.model.res.proportionality.CommonSaveRes;
import com.maan.insurance.model.res.proportionality.ConvertPolicyRes;
import com.maan.insurance.model.res.proportionality.FirstpagesaveRes;
import com.maan.insurance.model.res.proportionality.FirstpageupdateRes;
import com.maan.insurance.model.res.proportionality.GetCommonValueRes;
import com.maan.insurance.model.res.proportionality.GetCrestaDetailListRes;
import com.maan.insurance.model.res.proportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.proportionality.GetRetDetailsRes;
import com.maan.insurance.model.res.proportionality.GetRetentionDetailsRes;
import com.maan.insurance.model.res.proportionality.GetScaleCommissionListRes;
import com.maan.insurance.model.res.proportionality.GetSectionEditModeRes;
import com.maan.insurance.model.res.proportionality.GetSlidingScaleMethodInfoRes;
import com.maan.insurance.model.res.proportionality.GetcalculateSCRes;
import com.maan.insurance.model.res.proportionality.GetprofitCommissionEnableRes;
import com.maan.insurance.model.res.proportionality.InsertCrestaDetailsRes;
import com.maan.insurance.model.res.proportionality.ProfitCommissionListRes;
import com.maan.insurance.model.res.proportionality.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.proportionality.ScaleCommissionInsertRes;
import com.maan.insurance.model.res.proportionality.SecondpagesaveRes;
import com.maan.insurance.model.res.proportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.proportionality.ShowRetroContractsRes;
import com.maan.insurance.model.res.proportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.proportionality.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.proportionality.UpdateOfferNoReq;
import com.maan.insurance.model.res.proportionality.ViewRiskDetailsRes;
import com.maan.insurance.model.res.proportionality.checkAvialabilityRes;
import com.maan.insurance.model.res.proportionality.getSlidingScaleMethodInfo;
import com.maan.insurance.model.res.proportionality.getprofitCommissionDeleteRes;
import com.maan.insurance.model.res.proportionality.getprofitCommissionEditRes;
import com.maan.insurance.model.res.proportionality.saveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.proportionality.showSecondPageData1Res;
import com.maan.insurance.model.res.retro.CommonResponse;

@Service
public interface ProportionalityService {

	FirstpagesaveRes insertProportionalTreaty(FirstpageSaveReq req, boolean b, boolean c);

	FirstpageupdateRes updateProportionalTreaty(FirstpageSaveReq req);

	GetRemarksDetailsRes GetRemarksDetails(String proposalNo, String layerNo);

	SecondpagesaveRes saveSecondPage(FirstpageSaveReq req);

	CommonSaveRes insertRetroContracts(RetroSaveReq req);

	CommonSaveRes insertCrestaMaintable(CrestaSaveReq req);

	CommonSaveRes insertBonusDetails(BonusSaveReq req, String type);

	CommonSaveRes insertProfitCommission(ProfitCommissionSaveReq req);

	CommonSaveRes insertRemarkDetails(RemarksSaveReq req);

	CommonSaveRes insertCedentRetention(CedentSaveReq req);
	
	GetCommonValueRes getShortname(String branchCode);

	GetCommonValueRes getEditMode(String proposalNo);

	RiskDetailsEditModeRes riskDetailsEditMode(RiskDetailsEditModeReq req);

	GetRetDetailsRes getGetRetDetails(String proposalNo, String branchCode, String productId);

	BaseLayerStatusRes BaseLayerStatus(BaseLayerStatusReq req);

	ShowSecondpageEditItemsRes showSecondpageEditItems(ShowSecondpageEditItemsReq req);

	GetprofitCommissionEnableRes getprofitCommissionEnable(GetprofitCommissionEnableReq req);

	ShowSecondPageDataRes showSecondPageData(ShowSecondPageDataReq req);

	ShowLayerBrokerageRes showLayerBrokerage(String layerProposalNo);

	CheckProductMatchRes checkProductMatch(String proposalNo, boolean contractMode, String productId);

	ShowRetroContractsRes showRetroContracts(ShowRetroContractsReq req);

	GetCrestaDetailListRes getCrestaDetailList(GetCrestaDetailListReq req);

	InsertCrestaDetailsRes insertCrestaDetails(InsertCrestaDetailsReq req);

	CancelProposalRes cancelProposal(String proposalNo, String newProposal, String proposalReference);

	GetRetentionDetailsRes getRetentionDetails(GetRetentionDetailsReq req);

	GetScaleCommissionListRes getScaleCommissionList(getScaleCommissionListReq req);

	ViewRiskDetailsRes viewRiskDetails(ViewRiskDetailsReq req);
	
	getprofitCommissionDeleteRes getprofitCommissionDelete(String proposalno, String branchCode, String profitSno);

	showSecondPageData1Res showSecondPageData1(showSecondPageData1Req req);

	getprofitCommissionEditRes getprofitCommissionEdit(String proposalno, String branchCode, String profitSno);

	ProfitCommissionListRes ProfitCommissionList(ProfitCommissionListReq req);

	ScaleCommissionInsertRes ScaleCommissionInsert(ScaleCommissionInsertReq req);

	saveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(saveRiskDeatilsSecondFormReq req);

	checkAvialabilityRes checkAvialability(String proposalno, String pid);

	CommonSaveRes updateOfferNo(UpdateOfferNoReq req);

	GetSlidingScaleMethodInfoRes getSlidingScaleMethodInfo(getSlidingScaleMethodInfo req);

	CommonResponse insertSlidingScaleMentodInfo(ScaleCommissionInsertReq req);

	GetcalculateSCRes getcalculateSC(GetcalculateSCReq req);

	GetSectionEditModeRes getSectionEditMode(String proposalNo);

	CommonSaveRes getSectionDuplicationCheck(GetSectionDuplicationCheckReq req);

	ConvertPolicyRes convertPolicy(ConvertPolicyReq req);

	CommonResponse ttrnRipDelete(String referenceNo);

	CommonResponse ttrnBonusDelete(String referenceNo);
}
