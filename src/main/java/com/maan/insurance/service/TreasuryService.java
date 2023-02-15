package com.maan.insurance.service;

import com.maan.insurance.model.req.AllocateDetailsReq;
import com.maan.insurance.model.req.AllocateViewReq;
import com.maan.insurance.model.req.AllocatedStatusReq;
import com.maan.insurance.model.req.GetAllTransContractReq;
import com.maan.insurance.model.req.GetReceiptAllocateReq;
import com.maan.insurance.model.req.GetReceiptGenerationReq;
import com.maan.insurance.model.req.GetReceiptReversalListReq;
import com.maan.insurance.model.req.GetRetroallocateTransactionReq;
import com.maan.insurance.model.req.GetReversalInfoReq;
import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.GetTreasuryJournalViewReq;
import com.maan.insurance.model.req.CurrecyAmountReq;
import com.maan.insurance.model.req.PaymentRecieptReq;
import com.maan.insurance.model.req.ReceiptTreasuryReq;
import com.maan.insurance.model.req.ReceiptViewListReq;
import com.maan.insurance.model.req.RetroTransReq;
import com.maan.insurance.model.req.ReciptListReq;
import com.maan.insurance.model.req.ReverseInsertReq;
import com.maan.insurance.model.req.ReverseViewReq;
import com.maan.insurance.model.res.AllocateDetailsRes1;
import com.maan.insurance.model.res.AllocateViewCommonRes1;
import com.maan.insurance.model.res.AllocatedStatusRes1;
import com.maan.insurance.model.res.GetAllTransContractRes1;
import com.maan.insurance.model.res.GetDirectCedingRes1;
import com.maan.insurance.model.res.GetReceiptAllocateRes1;
import com.maan.insurance.model.res.GetReceiptEditRes1;
import com.maan.insurance.model.res.GetReceiptGenerationRes1;
import com.maan.insurance.model.res.GetReceiptReversalListRes1;
import com.maan.insurance.model.res.GetReversalInfoRes1;
import com.maan.insurance.model.res.GetShortnameRes;
import com.maan.insurance.model.res.GetTransContractRes1;
import com.maan.insurance.model.res.GetTreasuryJournalViewRes1;
import com.maan.insurance.model.req.SecondPageInfoReq;
import com.maan.insurance.model.res.PaymentRecieptRes1;
import com.maan.insurance.model.res.ReceiptTreasuryListRes;
import com.maan.insurance.model.res.ReceiptViewListsRes;
import com.maan.insurance.model.res.ReciptGetLIstRes;
import com.maan.insurance.model.res.RetroTransListRes;
import com.maan.insurance.model.res.ReverseInsertRes;
import com.maan.insurance.model.res.ReverseViewRes1;
import com.maan.insurance.model.res.SecondPageInfoRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.CommonSaveRes;

public interface TreasuryService {

	CommonSaveRes savepaymentReciept(PaymentRecieptReq req);

	ReverseInsertRes savereverseInsert(ReverseInsertReq req);

	AllocatedStatusRes1 getAllocatedStatus(AllocatedStatusReq req);

	AllocateViewCommonRes1 allocateView(AllocateViewReq req);

	GetReceiptEditRes1 getReceiptEdit(String paymentReceiptNo, String branchCode);

	GetReceiptGenerationRes1 getReceiptGeneration(GetReceiptGenerationReq req);

	GetReversalInfoRes1 getReversalInfo(GetReversalInfoReq req);

	ReverseViewRes1 reverseView(ReverseViewReq req);

	GetReceiptReversalListRes1 getReceiptReversalList(GetReceiptReversalListReq req);

	GetReceiptAllocateRes1 getReceiptAllocate(GetReceiptAllocateReq req);

	AllocateDetailsRes1 allocateDetails(AllocateDetailsReq req);

	GetTransContractRes1 getTransContract(GetTransContractReq req);

	GetAllTransContractRes1 getAllTransContract(GetAllTransContractReq req);

	//List<GetDirectCedingRes> getDirectCeding(GetDirectCedingReq req);

	GetShortnameRes getShortname(String branchcode);

	GetTreasuryJournalViewRes1 getTreasuryJournalView(GetTreasuryJournalViewReq req);

	CommonResponse getRetroallocateTransaction(GetRetroallocateTransactionReq req);

//	List<GetAllRetroTransContractRes> getAllRetroTransContract(GetRetroallocateTransactionReq req);

	GetDirectCedingRes1 getDirectCeding(String brokerId);

	ReciptGetLIstRes getReciptList(ReciptListReq req);

	RetroTransListRes getRetroTransContract(RetroTransReq req);

	ReceiptTreasuryListRes getReceiptTreasuryGeneration(ReceiptTreasuryReq req);

	ReceiptViewListsRes getReceiptViewList(ReceiptViewListReq req);

	CommonSaveRes getCurrecyAmount(CurrecyAmountReq req);

	SecondPageInfoRes getSecondPageInfo(SecondPageInfoReq req);

	PaymentRecieptRes1 receiptdetail(PaymentRecieptReq req);

	CommonResponse getAllocateTransaction(GetTransContractReq req);

}
