package com.maan.insurance.service.facultative;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.facultative.DeleteMaintableReq;
import com.maan.insurance.model.req.facultative.FirstPageInsertReq;
import com.maan.insurance.model.req.facultative.GetInsurarerDetailsReq;
import com.maan.insurance.model.req.facultative.GetLowClaimBonusListReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsListReq;
import com.maan.insurance.model.req.facultative.GetRetroContractDetailsReq;
import com.maan.insurance.model.req.facultative.InserLossRecordReq;
import com.maan.insurance.model.req.facultative.InsertBonusDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InsertCrestaMaintableReq;
import com.maan.insurance.model.req.facultative.InsertInsurarerTableInsertReq;
import com.maan.insurance.model.req.facultative.InsertXolCoverDeductableDetailsReq;
import com.maan.insurance.model.req.facultative.InstalMentPremiumReq;
import com.maan.insurance.model.req.facultative.MoveBonusReq;
import com.maan.insurance.model.req.facultative.SecondPageInsertReq;
import com.maan.insurance.model.req.facultative.ShowSecondPagedataReq;
import com.maan.insurance.model.req.facultative.ShowSecondpageEditItemsReq;
import com.maan.insurance.model.req.facultative.UpdateSecondPageReq;
import com.maan.insurance.model.req.facultative.ViewModeReq;
import com.maan.insurance.model.req.nonproportionality.RemarksSaveReq;
import com.maan.insurance.model.res.facultative.CommonResponse;
import com.maan.insurance.model.res.facultative.FirstPageInsertRes;
import com.maan.insurance.model.res.facultative.GetCommonValueRes;
import com.maan.insurance.model.res.facultative.GetCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.GetInsurarerDetailsRes;
import com.maan.insurance.model.res.facultative.GetLossDEtailsRes;
import com.maan.insurance.model.res.facultative.GetLowClaimBonusListRes;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsListRes;
import com.maan.insurance.model.res.facultative.GetRetroContractDetailsRes;
import com.maan.insurance.model.res.facultative.GetXolCoverDeductableDetailsRes;
import com.maan.insurance.model.res.facultative.SecondPageInsertRes;
import com.maan.insurance.model.res.facultative.ShowSecondPagedataRes;
import com.maan.insurance.model.res.facultative.ShowSecondpageEditItemsRes;
import com.maan.insurance.model.res.facultative.ViewModeRes;
import com.maan.insurance.model.res.nonproportionality.GetRemarksDetailsRes;

@Service
public interface FacultativeService {

	GetCommonValueRes getShortname(String branchCode);

	FirstPageInsertRes firstPageInsert(FirstPageInsertReq req);

	CommonResponse insertRemarkDetails(RemarksSaveReq req);

	CommonResponse UpadateUWShare(String shSd, String proposalNo);

	CommonResponse insertCoverDeductableDetails(InsertCoverDeductableDetailsReq req);

	CommonResponse insertXolCoverDeductableDetails(InsertXolCoverDeductableDetailsReq req);

//	GetLowClaimBonusListRes getLowClaimBonusList(GetLowClaimBonusListReq req);

	GetLowClaimBonusListRes getLowClaimBonusList(String proposalNo, String branchCode, String acqBonus);

	ShowSecondPagedataRes showSecondPagedata(ShowSecondPagedataReq req);

	GetRetroContractDetailsListRes getRetroContractDetailsList(GetRetroContractDetailsListReq req, String flag);

	ShowSecondpageEditItemsRes ShowSecondPageEditItems(ShowSecondpageEditItemsReq req);

	GetRemarksDetailsRes getRemarksDetails(String proposalNo);

	GetInsurarerDetailsRes getInsurarerDetails(GetInsurarerDetailsReq req);

	GetLossDEtailsRes getLossDEtails(String proposalNo);

	CommonResponse moveBonus(MoveBonusReq req);

	CommonResponse deleteMaintable(DeleteMaintableReq req);

	SecondPageInsertRes secondPageInsert(SecondPageInsertReq req);

	CommonResponse updateSecondPage(UpdateSecondPageReq req);

	CommonResponse inserLossRecord(InserLossRecordReq req);

	CommonResponse instalMentPremium(InstalMentPremiumReq req);

	CommonResponse insertInsurarerTableInsert(InsertInsurarerTableInsertReq req);

	CommonResponse insertBonusDetails(InsertBonusDetailsReq req);

	CommonResponse insertCrestaMaintable(InsertCrestaMaintableReq req);

	ViewModeRes viewMode(ViewModeReq req);

	GetCoverDeductableDetailsRes getCoverDeductableDetails(String proposalNo, String branchCode, String productId);

	GetXolCoverDeductableDetailsRes GetXolCoverDeductableDetails(String proposalNo, String branchCode);

	GetCommonValueRes getRetroContractDetails(GetRetroContractDetailsReq req);

	GetCommonValueRes getShareValidation(String proposalNo, String leaderUWShare);

	GetCommonValueRes getBonusListCount(String proposalNo, String branchCode, String acqBonus, String endorsmentno);

}
