package com.maan.insurance.service.retro;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.retro.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.retro.InsertProfitCommissionMainReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.retro.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.retro.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.retro.UpdateRiskProposalReq;
import com.maan.insurance.model.req.retro.ShowSecondPageData1Req;
import com.maan.insurance.model.req.retro.ShowSecondPageDataReq;
import com.maan.insurance.model.req.retro.FirstInsertReq;
import com.maan.insurance.model.req.retro.GetEndDateReq;
import com.maan.insurance.model.req.retro.InsertBonusDetailsReq;
import com.maan.insurance.model.req.retro.InsertRemarkDetailsReq;
import com.maan.insurance.model.req.retro.InsertRetroDetailsReq;
import com.maan.insurance.model.req.retro.SaveSecondPageReq;
import com.maan.insurance.model.req.retro.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.retro.ViewRiskDetailsReq;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.retro.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;
import com.maan.insurance.model.res.retro.ShowSecondPageData1Res;
import com.maan.insurance.model.res.retro.ShowSecondPageDataRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.FirstInsertRes;
import com.maan.insurance.model.res.retro.SaveSecondPageRes;
import com.maan.insurance.model.res.retro.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.retro.ViewRiskDetailsRes;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;

@Service
public interface RetroService {

	FirstInsertRes firstInsert(FirstInsertReq req);

	ShowSecondpageEditItemsRes showSecondpageEditItems(ShowSecondpageEditItemsReq req);

	CommonResponse insertRemarkDetails(InsertRemarkDetailsReq req);

	CommonSaveRes getShortname(String branchCode);

	CommonSaveRes checkAvialability(String proposalNo, String productId);

	ViewRiskDetailsRes viewRiskDetails(ViewRiskDetailsReq req);

	GetRemarksDetailsRes getRemarksDetails(String proposalNo);


	ShowRetroCess1Res showRetroCess(ShowRetroCess1Req req);

	ShowSecondPageData1Res showSecondPageData1(ShowSecondPageData1Req req);

	ShowSecondPageDataRes showSecondPageData(ShowSecondPageDataReq req);

	CommonSaveRes getEditMode(String proposalNo);

	SaveSecondPageRes saveSecondPage(SaveSecondPageReq req);

	CommonResponse insertRetroCess(InsertRetroCessReq req);

	CommonResponse insertCrestaMaintable(InsertCrestaMaintableReq req);

	CommonResponse insertBonusDetails(InsertBonusDetailsReq req);

	CommonResponse insertProfitCommissionMain(InsertProfitCommissionMainReq req);

	CommonResponse instalMentPremium(InstalMentPremiumReq req);

	SaveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(SaveSecondPageReq req);

	ShowLayerBrokerageRes showLayerBrokerage(String layerProposalNo);

	CommonSaveRes updateProportionalTreaty(UpdateProportionalTreatyReq req);

	CommonSaveRes updateRiskProposal(FirstInsertReq req);

	CommonSaveRes previouRetroTypeChect(String proposalNo, String branchCode);

	RiskDetailsEditModeRes riskDetailsEditMode(RiskDetailsEditModeReq req);

	CommonSaveRes getEndDate(GetEndDateReq req);

	CommonResponse insertRetroDetails(InsertRetroDetailsReq req);

}
