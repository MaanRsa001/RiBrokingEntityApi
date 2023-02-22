package com.maan.insurance.service.placement;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.placement.AttachFileReq;
import com.maan.insurance.model.req.placement.DeleteFileReq;
import com.maan.insurance.model.req.placement.EditPlacingDetailsReq;
import com.maan.insurance.model.req.placement.GetApprovalPendingListReq;
import com.maan.insurance.model.req.placement.GetExistingAttachListReq;
import com.maan.insurance.model.req.placement.GetExistingReinsurerListReq;
import com.maan.insurance.model.req.placement.GetMailTemplateReq;
import com.maan.insurance.model.req.placement.GetMailToListReq;
import com.maan.insurance.model.req.placement.GetPlacementInfoListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewReq;
import com.maan.insurance.model.req.placement.GetPlacingInfoReq;
import com.maan.insurance.model.req.placement.GetReinsurerInfoReq;
import com.maan.insurance.model.req.placement.PlacementSummaryReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.placement.UpdateRiplacementReq;
import com.maan.insurance.model.req.placement.UploadDocumentReq;
import com.maan.insurance.model.req.placement.proposalInfoReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.placement.AttachFileRes;
import com.maan.insurance.model.res.placement.CommonSaveResList;
import com.maan.insurance.model.res.placement.EditPlacingDetailsRes;
import com.maan.insurance.model.res.placement.GetApprovalPendingListRes;
import com.maan.insurance.model.res.placement.GetExistingAttachListRes;
import com.maan.insurance.model.res.placement.GetMailTemplateRes;
import com.maan.insurance.model.res.placement.GetPlacementInfoListRes;
import com.maan.insurance.model.res.placement.GetPlacementNoRes;
import com.maan.insurance.model.res.placement.GetPlacementViewListRes;
import com.maan.insurance.model.res.placement.GetPlacementViewRes;
import com.maan.insurance.model.res.placement.GetPlacingInfoRes;
import com.maan.insurance.model.res.placement.GetReinsurerInfoRes;
import com.maan.insurance.model.res.placement.PlacementSummaryRes;
import com.maan.insurance.model.res.placement.ProposalInfoRes;
import com.maan.insurance.model.res.placement.SendMailRes;
import com.maan.insurance.model.res.placement.UpdatePlacementRes1;
import com.maan.insurance.model.res.placement.UpdateRiplacementRes;
import com.maan.insurance.model.res.placement.UploadDocumentRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;

@Service
public interface PlacementService {

	GetCommonDropDownRes getMailToList(GetMailToListReq req);

	GetCommonDropDownRes getExistingReinsurerList(GetExistingReinsurerListReq req);

	GetCommonDropDownRes getExistingBrokerList(GetExistingReinsurerListReq req);

	GetExistingAttachListRes getExistingAttachList(GetExistingAttachListReq req);

	ProposalInfoRes proposalInfo(proposalInfoReq req);

	GetReinsurerInfoRes getReinsurerInfo(GetReinsurerInfoReq req);

	GetPlacementInfoListRes getPlacementInfoList(GetPlacementInfoListReq req);

	CommonSaveResList savePlacing(SavePlacingReq req);

	GetPlacementNoRes getPlacementNo(SavePlacingReq req);

	/* InsertPlacingRes insertPlacing(SavePlacingReq req); */

	GetPlacingInfoRes getPlacingInfo(GetPlacingInfoReq req);

	EditPlacingDetailsRes editPlacingDetails(EditPlacingDetailsReq req);

	UpdatePlacementRes1 updatePlacement(UpdatePlacementReq req);

	UploadDocumentRes uploadDocument(UploadDocumentReq req);

	AttachFileRes attachFile(AttachFileReq req);

	SendMailRes sendMail(SendMailReq req);

	//CommonResponse updateStatus(UpdatePlacementReq req);

	//InsertMailDetailsRes insertMailDetails(InsertMailDetailsReq req);

	//CommonResponse updateMailDetails(UpdateMailDetailsReq req);

	CommonSaveRes deleteFile(DeleteFileReq req);

	CommonSaveRes downloadFile(DeleteFileReq req);

	GetPlacementViewListRes getPlacementViewList(GetPlacementViewListReq req);

	GetPlacementViewRes getPlacementView(GetPlacementViewReq req);

	PlacementSummaryRes placementSummary(PlacementSummaryReq req);

	GetMailTemplateRes getMailTemplate(GetMailTemplateReq req);

	GetApprovalPendingListRes getApprovalPendingList(GetApprovalPendingListReq req, String status);

	CommonResponse updateRiplacement(UpdateRiplacementReq req, String status);

}
