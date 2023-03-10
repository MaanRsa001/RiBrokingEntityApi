package com.maan.insurance.service.claim;


import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.claim.AllocListReq;
import com.maan.insurance.model.req.claim.AllocationListReq;
import com.maan.insurance.model.req.claim.CedentNoListReq;
import com.maan.insurance.model.req.claim.ClaimListMode4Req;
import com.maan.insurance.model.req.claim.ClaimListReq;
import com.maan.insurance.model.req.claim.ClaimPaymentEditReq;
import com.maan.insurance.model.req.claim.ClaimPaymentListReq;
import com.maan.insurance.model.req.claim.ClaimTableListMode2Req;
import com.maan.insurance.model.req.claim.ClaimTableListReq;
import com.maan.insurance.model.req.claim.ContractDetailsModeReq;
import com.maan.insurance.model.req.claim.ContractDetailsReq;
import com.maan.insurance.model.req.claim.ContractidetifierlistReq;
import com.maan.insurance.model.req.claim.GetClaimAuthViewReq;
import com.maan.insurance.model.req.claim.GetContractNoReq;
import com.maan.insurance.model.req.claim.GetPaymentNoListReq;
import com.maan.insurance.model.req.claim.GetReInsValueReq;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode12Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode2Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode8Req;
import com.maan.insurance.model.req.claim.ProposalNoReq;
import com.maan.insurance.model.req.claim.claimNoListReq;
import com.maan.insurance.model.res.GetShortnameRes;
import com.maan.insurance.model.res.claim.AllocListRes1;
import com.maan.insurance.model.res.claim.AllocationListRes;
import com.maan.insurance.model.res.claim.CedentNoListRes;
import com.maan.insurance.model.res.claim.ClaimListMode3Response;
import com.maan.insurance.model.res.claim.ClaimListMode4Response;
import com.maan.insurance.model.res.claim.ClaimListMode5Response;
import com.maan.insurance.model.res.claim.ClaimListMode6Response;
import com.maan.insurance.model.res.claim.ClaimListRes;
import com.maan.insurance.model.res.claim.ClaimPaymentEditRes1;
import com.maan.insurance.model.res.claim.ClaimPaymentListRes1;
import com.maan.insurance.model.res.claim.ClaimTableListMode1Res;
import com.maan.insurance.model.res.claim.ClaimTableListMode2Res;
import com.maan.insurance.model.res.claim.ClaimTableListMode7Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode10Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode1Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode4Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode5Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode6Res;
import com.maan.insurance.model.res.claim.ContractDetailsMode7Res;
import com.maan.insurance.model.res.claim.ContractidetifierlistRes1;
import com.maan.insurance.model.res.claim.GetClaimAuthViewRes;
import com.maan.insurance.model.res.claim.GetContractNoRes1;
import com.maan.insurance.model.res.claim.GetPaymentNoListRes;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode12Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode2Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode3Res;
import com.maan.insurance.model.res.claim.InsertCliamDetailsMode8Res;
import com.maan.insurance.model.res.claim.ProductIdListRes1;
import com.maan.insurance.model.res.claim.ProposalNoRes;
import com.maan.insurance.model.res.claim.claimNoListRes;
@Service
public interface ClaimService {

	ProposalNoRes savepaymentReciept(ProposalNoReq req);

	ContractDetailsMode1Res saveContractDetailsMode1(ContractDetailsReq req);

	ContractDetailsMode4Res saveContractDetailsMode4(ContractDetailsModeReq req);

	ContractDetailsMode5Res saveContractDetailsMode5(ContractDetailsModeReq req);

	ContractDetailsMode6Res saveContractDetailsMode6(ContractDetailsModeReq req);

	ContractDetailsMode7Res saveContractDetailsMode7(ContractDetailsModeReq req);

	ContractDetailsMode10Res saveContractDetailsMode10(ContractDetailsModeReq req);

	AllocationListRes saveAllocationList(AllocationListReq req);

	ClaimListRes saveclaimlist(ClaimListReq req);

	ClaimTableListMode1Res claimTableListMode1(ClaimTableListReq req);

	

	ClaimTableListMode2Res claimTableListMode2(ClaimTableListMode2Req req);



	ClaimTableListMode7Res claimTableListMode7(ClaimTableListMode2Req req);



	ClaimListMode3Response claimListMode3(ClaimTableListMode2Req req);



	ClaimListMode4Response claimListMode4(ClaimListMode4Req req);



	ClaimListMode5Response claimListMode5(String claimNo, String contractNo);



	ClaimListMode6Response claimListMode6(String contractNo, String claimNo);



	GetContractNoRes1 getContractNo(GetContractNoReq req);



	ClaimPaymentEditRes1 claimPaymentEdit(ClaimPaymentEditReq req);







	AllocListRes1 allocList(AllocListReq req);



	ProductIdListRes1 productIdList(String branchCode);



	ContractidetifierlistRes1 contractidetifierlist(ContractidetifierlistReq req);



	ClaimPaymentListRes1 claimPaymentList(ClaimPaymentListReq req);



	String getReInsValue(GetReInsValueReq req);

	GetShortnameRes getShortname(String branchcode);

	InsertCliamDetailsMode2Res insertCliamDetailsMode2(InsertCliamDetailsMode2Req req);

	InsertCliamDetailsMode8Res insertCliamDetailsMode8(InsertCliamDetailsMode8Req req);

	InsertCliamDetailsMode3Res insertCliamDetailsMode3(InsertCliamDetailsMode3Req req);

	InsertCliamDetailsMode12Res insertCliamDetailsMode12(InsertCliamDetailsMode12Req req);

//	claimNoListRes claimNoList(claimNoListReq req);

	ClaimPaymentEditRes1 claimPaymentEditRi(ClaimPaymentEditReq req);

	InsertCliamDetailsMode3Res claimUpdatePaymentRi(InsertCliamDetailsMode3Req req);

	ClaimPaymentListRes1 claimPaymentRiList(ClaimPaymentListReq req);

	CedentNoListRes cedentNoList(CedentNoListReq req);

	GetPaymentNoListRes getPaymentNoList(GetPaymentNoListReq req);

	GetClaimAuthViewRes getClaimAuthView(GetClaimAuthViewReq req);


}
