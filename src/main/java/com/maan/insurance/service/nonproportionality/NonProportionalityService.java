package com.maan.insurance.service.nonproportionality;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.nonproportionality.CrestaSaveReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.nonproportionality.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertBonusDetailsReq;
import com.maan.insurance.model.req.nonproportionality.InsertIEModuleReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroCessReq;
import com.maan.insurance.model.req.nonproportionality.InsertRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.InstalMentPremiumReq;
import com.maan.insurance.model.req.nonproportionality.LowClaimBonusInserReq;
import com.maan.insurance.model.req.nonproportionality.MoveReinstatementMainReq;
import com.maan.insurance.model.req.nonproportionality.ReInstatementMainInsertReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.req.nonproportionality.RiskDetailsEditModeReq;
import com.maan.insurance.model.req.nonproportionality.SaveSecondPageReq;
import com.maan.insurance.model.req.nonproportionality.ShowRetroCess1Req;
import com.maan.insurance.model.req.nonproportionality.ShowRetroContractsReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageData1Req;
import com.maan.insurance.model.req.nonproportionality.ShowSecondPageDataReq;
import com.maan.insurance.model.req.nonproportionality.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.nonproportionality.UpdateProportionalTreatyReq;
import com.maan.insurance.model.req.nonproportionality.ViewRiskDetailsReq;
import com.maan.insurance.model.req.nonproportionality.insertClassLimitReq;
import com.maan.insurance.model.req.nonproportionality.insertProportionalTreatyReq;
import com.maan.insurance.model.req.proportionality.GetClassLimitDetailsReq;
import com.maan.insurance.model.res.nonproportionality.CheckAvialabilityRes;
import com.maan.insurance.model.res.nonproportionality.CommonResponse;
import com.maan.insurance.model.res.nonproportionality.CommonSaveRes;
import com.maan.insurance.model.res.nonproportionality.GetClassLimitDetailsRes;
import com.maan.insurance.model.res.nonproportionality.GetCommonValueRes;
import com.maan.insurance.model.res.nonproportionality.GetInclusionExListRes;
import com.maan.insurance.model.res.nonproportionality.GetLowClaimBonusListRes;
import com.maan.insurance.model.res.nonproportionality.GetReInstatementDetailsListRes;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;
import com.maan.insurance.model.res.nonproportionality.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.nonproportionality.RiskDetailsEditModeRes;
import com.maan.insurance.model.res.nonproportionality.SaveRiskDeatilsSecondFormRes;
import com.maan.insurance.model.res.nonproportionality.SaveSecondPageRes;
import com.maan.insurance.model.res.nonproportionality.ShowLayerBrokerageRes;
import com.maan.insurance.model.res.nonproportionality.ShowRetroCess1Res;
import com.maan.insurance.model.res.nonproportionality.ShowRetroContractsRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageData1Res;
import com.maan.insurance.model.res.nonproportionality.ShowSecondPageDataRes;
import com.maan.insurance.model.res.nonproportionality.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.nonproportionality.UpdateProportionalTreatyRes;
import com.maan.insurance.model.res.nonproportionality.ViewRiskDetailsRes;
import com.maan.insurance.model.res.nonproportionality.insertProportionalTreatyRes;

@Service
public interface NonProportionalityService {

	GetCommonValueRes getShortname(String branchCode);

	ShowSecondPageData1Res showSecondPageData1(ShowSecondPageData1Req req);

	GetRemarksDetailsRes GetRemarksDetails(String proposalNo, String layerNo);

	GetCommonValueRes getSumOfCover(String proposalNo, String branchCode, String productId, String layerNo);

	ShowLayerBrokerageRes showLayerBrokerage(String layerProposalNo);

	ShowRetroContractsRes showRetroContracts(ShowRetroContractsReq req);

	GetCommonValueRes getRetroContractDetails(GetRetroContractDetailsReq req);

	CheckAvialabilityRes checkAvialability(String proposalNo, String productId);

	ShowRetroCess1Res showRetroCess1(ShowRetroCess1Req req);

	UpdateProportionalTreatyRes updateProportionalTreaty(UpdateProportionalTreatyReq req);

	CommonResponse insertClassLimit(insertClassLimitReq req);

	ViewRiskDetailsRes viewRiskDetails(ViewRiskDetailsReq req);

	RiskDetailsEditModeRes riskDetailsEditMode(RiskDetailsEditModeReq req);

	ShowSecondPageDataRes showSecondPageData(ShowSecondPageDataReq req);

	GetRetroContractDetailsListRes getRetroContractDetailsList(GetRetroContractDetailsListReq req, int flag, String UWYear);

	SaveSecondPageRes saveSecondPage(SaveSecondPageReq req);
	
	insertProportionalTreatyRes insertProportionalTreaty(insertProportionalTreatyReq req);


	CommonResponse moveReinstatementMain(MoveReinstatementMainReq req);

	CommonResponse deleteMainTable(String proposalNo, String amendId, String branchCode);

	GetReInstatementDetailsListRes getReInstatementDetailsList(String proposalNo, String branchCode);

	GetLowClaimBonusListRes getLowClaimBonusList(String proposalNo, String branchCode, String acqBonus);

	CommonResponse lowClaimBonusInser(LowClaimBonusInserReq req);

	GetInclusionExListRes getInclusionExList(String proposalNo, String branchCode);

	CommonResponse insertIEModule(InsertIEModuleReq req);

	ShowSecondpageEditItemsRes showSecondpageEditItems(ShowSecondpageEditItemsReq req);

	CommonSaveRes insertRemarkDetails(RemarksSaveReq req);

	GetClassLimitDetailsRes getClassLimitDetails(GetClassLimitDetailsReq req);

	SaveRiskDeatilsSecondFormRes saveRiskDeatilsSecondForm(SaveSecondPageReq req);

	CommonResponse insertRetroCess(InsertRetroCessReq req);

	CommonResponse instalMentPremium(InstalMentPremiumReq req);

	CommonResponse insertRetroContracts(InsertRetroContractsReq req);

	CommonResponse reInstatementMainInsert(ReInstatementMainInsertReq req);

	CommonResponse insertCrestaMaintable(CrestaSaveReq req);

	CommonResponse insertBonusDetails(InsertBonusDetailsReq req);


}
