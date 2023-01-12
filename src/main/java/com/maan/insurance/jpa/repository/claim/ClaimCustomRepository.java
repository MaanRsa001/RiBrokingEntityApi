package com.maan.insurance.jpa.repository.claim;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.req.claim.ClaimListReq;
import com.maan.insurance.model.req.claim.ClaimPaymentEditReq;
import com.maan.insurance.model.req.claim.ClaimPaymentListReq;
import com.maan.insurance.model.req.claim.ContractidetifierlistReq;
import com.maan.insurance.model.req.claim.GetReInsValueReq;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;

public interface ClaimCustomRepository {

	public List<Tuple> getClaimAllocationList(String contractNo, String transactionNo);

	public String selectSumPaidAmt(String claimNo, String contractNo);

	public String selectRevSumPaidAmt(String claimNo, String contractNo);

	public String selectLossEstimateRevisedOc(String claimNo, String contractNo);

	public List<Tuple> selectGetClaimUpdateList(String claimNo, String policyContractNo);

	public List<Tuple> selectGetClaimReviewQuery(String claimNo, String policyContractNo);

	public List<Tuple> selectClaimClaimmaster(ClaimListReq req, String searchCriteria);

	public List<Tuple> selectDate(String branchCode);

	public Integer getCliampaymnetCount(String claimNo, String contractNo);

	public List<Tuple> selectGetClaimPaymentList(String claimNo, String contractNo);

	public List<Tuple> selectGetClaimReviewList(String claimNo, String contractNo);

	public List<Tuple> selectGetClaimList(String claimNo, String contractNo, String branchCode);

	public List<Tuple> selectClaimTableList(String policyContractNo, String layerNo, String departmentId);

	public List<Tuple> getClaimPaymentData(ClaimPaymentEditReq req);

	public String getShortName(String branchCode);

	public List<Tuple> getContractNumber(String claimNo, String contractNo);

	public List<Tuple> getFacProposalNo(String contractNo);

	public List<Tuple> getProProposalNo(String contractNo, String deptId);

	public List<Tuple> getXolProposalNo(String contractNo, String layerNo);

	public List<Tuple> getProductIdList(String branchCode);

	public List<Map<String, Object>> getRipValues(GetReInsValueReq req, String paymentCoverPlus);

	public String selectExcRate(String claimNo, String contractNo);

	public String selectMaxSnoDTB3(String claimNo, String contractNo);

	public Integer updateCloseClaim(String status, Date date, String claimNo, String contractNo);

	public Long selectPaymentReqNo(String claimNo, String paymentRequestNo);

	public void getPremiumSpRetroSplit(InsertCliamDetailsMode3Req req);

	public String selectRiRecovery(String claimNo, String contractNo);

	public Integer updateTotalAmtPaidTillDate(String amt, String claimNo, String contractNo);

	public Integer claimUpdatePayment(InsertCliamDetailsMode3Req req);

	public String selectMaxResvId(String claimNo, String contractNo);

	public TtrnClaimPayment clainArchInsert(InsertCliamDetailsMode3Req req);

	public String selectMaxIdArchive(InsertCliamDetailsMode3Req req);

	public List<Tuple> selectClaimEdit(String claimNo, String contractNo);

	public List<Tuple> getEditAccData(String claimNo, String policyContractNo);

	public List<Map<String, Object>> selectGetClaimReserveList(String claimNo, String contractNo);

	public String selectCurrecyName(String branchCode);

	public List<Map<String, Object>> selectGetClaimReserveListModeFour(String claimNo, String contractNo);

	public String selectMaxNo(String claimNo, String contractNo);

	public List<Map<String, Object>> coverSumInsuredVal(GetReInsValueReq req);

	public String getDateOfLoss(GetReInsValueReq req);

	public List<Tuple> getRdsDate(GetReInsValueReq req);

	public List<Map<String, Object>> selectFacGetCliamQuery(String proposalNo, String productId, String branchCode);

	public List<Map<String, Object>> selectXolOrTeatyGetClimeQuery(String proposalNo, String productId,
			String branchCode);
	
	public List<Map<String, Object>> selectGetpaymentlist(ClaimPaymentListReq req);
	
	public List<Map<String, Object>> partialSelectGetpaymentlist(ClaimPaymentListReq req);

	public String selectCountAllocatedYN(String policyContractNo, String claimNo, String layerNo);
	
	public String getStatusOfClaim(String policyContractNo, String claimNo, String layerNo);
	
	public String selectMaxno(String claimNo, String policyContractNo);
	
	public List<Tuple> contractIdentifierList(ContractidetifierlistReq req);
	
}
