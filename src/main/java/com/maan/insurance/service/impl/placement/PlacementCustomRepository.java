package com.maan.insurance.service.impl.placement;

import java.util.List;

import javax.persistence.Tuple;

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
import com.maan.insurance.model.req.placement.PlacementSummaryReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.model.req.placement.UpdatePlacementListReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.placement.UpdateRiplacementReq;
import com.maan.insurance.model.res.placement.GetPlacementNoRes;

public interface PlacementCustomRepository {

	List<Tuple> getMailToList(GetMailToListReq bean);

	List<Tuple> getExistingReinsurerList(GetExistingReinsurerListReq bean);

	List<Tuple> getExistingBrokerList(GetExistingReinsurerListReq bean);

	List<Tuple> getExistingAttachList(GetExistingAttachListReq bean);

	List<Tuple> getExistingProposal(String proposal, String branchCode, String string);

	GetPlacementNoRes getPlacementNo(SavePlacingReq bean);

	String getMaxAmendId(String branchCode, String eproposalNo, String reinsurerId, String brokerId);

	Tuple getPlacementDetails(String eproposalNo, String reinsurerId, String brokerId, String branchCode);

	List<Tuple> GetPlacementEdit(String branchCode, String brokerId, String eproposalNo, String reinsurerId);

	List<Tuple> GetPlacementSearchEdit(EditPlacingDetailsReq bean);

	List<Tuple> getStatusInfo(EditPlacingDetailsReq bean);

	List<Tuple> GetPlacementBouquet(String branchCode, String brokerId, String bouquetNo, String reinsurerId,
			String baseProposalNo);

	void updateNotificationStatus(UpdatePlacementListReq resp, UpdatePlacementReq bean, String status);

	void updatePlacement(UpdatePlacementListReq resp, UpdatePlacementReq bean, String mailType);

	void updateAttachementStatus(UpdatePlacementListReq resp, UpdatePlacementReq bean);

	void updatePlacementStatus(UpdatePlacementListReq resp, UpdatePlacementReq bean);

	List<Tuple> getPlacementViewList(GetPlacementViewListReq bean);

	List<Tuple> getPlacementView(GetPlacementViewReq bean);

	List<Tuple> MailproposalInfo(GetMailTemplateReq bean);

	List<Tuple> getMailAttachList(SendMailReq bean);

	List<Tuple> getReinsurerInfoNotin(String branchCode, String eproposalNo);

	List<Tuple> getReinsurerInfoBouquetNotin(String branchCode, String bouquetNo);

	List<Tuple> getPlacementBouquetList(GetPlacementInfoListReq req);

	List<Tuple> getPlacementBaseList(GetPlacementInfoListReq bean);

	List<Tuple> getPlacementList(GetPlacementInfoListReq bean, String prop);

	List<Tuple> getPlacingBouquetList(GetPlacingInfoReq bean);

	List<Tuple> getPlacingBaselayerList(GetPlacingInfoReq bean);

	List<Tuple> getPlacingList(GetPlacingInfoReq bean, String prop);

	List<Tuple> newContractPlSummary(PlacementSummaryReq bean);

	double sumOfShareSigned(String proposalNo);

	List<Tuple> getApprovalPendingList(GetApprovalPendingListReq bean, String status);

	void updateRiplacement(UpdateRiplacementReq req);

	void updateRiplacementStatus(UpdateRiplacementReq req);

	String getStatusNo(String proposal, String branchCode, String reinsurerId, String brokerId);

	int getCountContractBouquet(String bouquetNo);

	int getCountContractBaseLayer(String baseProposalNo);

	int getCountContractProposal(String prop);

	
}

