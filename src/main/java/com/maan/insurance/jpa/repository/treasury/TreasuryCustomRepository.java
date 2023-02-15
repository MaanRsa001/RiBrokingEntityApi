package com.maan.insurance.jpa.repository.treasury;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.model.entity.TmasOpenPeriod;
import com.maan.insurance.model.req.CurrecyAmountReq;
import com.maan.insurance.model.req.GetAllTransContractReq;
import com.maan.insurance.model.req.GetReceiptAllocateReq;
import com.maan.insurance.model.req.GetReceiptGenerationReq;
import com.maan.insurance.model.req.GetReceiptReversalListReq;
import com.maan.insurance.model.req.GetRetroallocateTransactionReq;
import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.GetTreasuryJournalViewReq;
import com.maan.insurance.model.req.PaymentRecieptReq;
import com.maan.insurance.model.req.ReceiptTreasuryReq;
import com.maan.insurance.model.req.ReceiptViewListReq;
import com.maan.insurance.model.req.ReciptListReq;
import com.maan.insurance.model.req.RetroTransReq;
import com.maan.insurance.model.req.ReverseViewReq;
import com.maan.insurance.model.req.SecondPageInfoReq;

public interface TreasuryCustomRepository {

	public List<TtrnPaymentReceiptDetails> getSecondPageDtls(SecondPageInfoReq req);

	public List<Tuple> currencyAmt(CurrecyAmountReq req);

	public List<TmasOpenPeriod> getOpenPeriodDate(String branchCode);

	public Integer getNextRetDtlsNo();

	public Integer updateDiffAmt(PaymentRecieptReq req);

	public Integer getMaxAmendIdSq(Integer receiptNo, Integer receiptSlNo);

	public Integer updateReversalPayment(PaymentRecieptReq req, String type) throws ParseException;

	public Integer updateReversalPaymentDetails(PaymentRecieptReq req, boolean isSerial);

	public List<TtrnPaymentReceipt> getRetDtls(String paymentReceiptNo, String branchCode);

	public List<TtrnAllocatedTransaction> getAllTranDtls(ReverseViewReq req);

	public String getSelCurrency(String branchCode, String currecnyId);

	public Integer getAlloAmt(Integer receiptSlNo, String currencyId);

	public  List<Tuple>  getPymtRetStatus(String branchCode, String payRecNo, String currencyId);

	public String getCompName(String branchCode, String brokerId, String customerType);

	public List<Tuple> getReversaltLists(GetReceiptReversalListReq req);

	public Integer getDiffAmt(Long paymentReceiptNo, String branchCode);

	public List<Long> getTotCount(String receiptNo, String status);

	public List<Tuple> cedingSelectDirect(String branchId);

	public String getShortName(String branchcode);

	public List<Tuple> getRetViewDtls(ReceiptViewListReq req);

	public Integer updateRetroDetails(String[] args);
	
	public Integer updateRetroStatus(String[] args);

	public List<Tuple> getDirBroDtls(ReceiptTreasuryReq req);

	public List<Tuple> getBroDtls(ReceiptTreasuryReq req);

	public List<Tuple> getAlloTransDtls(String receiptNo, String serialNo);

	public Integer updateAllocatedDtls(String[] args);

	public Integer updateRskPremDtls(String[] args);

	public List<Tuple> getRskPremDtls(String contractNo, String transactionNo);

	public Integer updateClaimPymtDtls(String[] args);

	public List<Tuple> getClaimPymtDtls(String contractNo, String claimPaymentNo);

	public Integer updateRetroPayment(String[] args);

	public List<Tuple> getRetroPayment(String contractNo, String transactionNo);

	public Integer updatePymtRetDtls(String[] args);

	public List<Tuple> getPymtRetDtls(String receiptSlNo, String currencyId);
	
	public List<Tuple> getAllocatedTls(String payRecNo);
	
	public String getAllcoationType(String serialNo);
	
	public Double getRetAmtDtls1(String serialNo);
	
	public Double getRetAmtDtls(String serialNo);
	
	public List<Tuple> getBroCedingIds(String payRecNo, String branchCode);
	
	public BigDecimal getExchRate(String serialNo, String currecncyValue);
	
	public List<Tuple> getDirBroDtls(GetReceiptGenerationReq req);

	public List<Tuple> getBroDtls(GetReceiptGenerationReq req);
	
	public List<Tuple> getDirBroDtlsRetro(GetRetroallocateTransactionReq req);
	
	public Long getseqno(String [] args);
	
	public Integer updateAlloTranDtls(String [] args);
	
	public List<Tuple> getTranContDtls(GetTransContractReq req);
	
	public List<Tuple> getDirBroDtlsRetro(RetroTransReq req);
	
	public List<Tuple> treasuryJournalView(GetTreasuryJournalViewReq req);
	
	public List<Tuple> getRetLists(ReciptListReq req);
	
	public List<Tuple> paymentAmountDetails(String branchCode, String payrecno);
	
	public Integer updateRskPremChkyn();
	
	public Integer updateClaimPymtChkyn();
	
	public List<Tuple> getRetAlloDtls(GetReceiptAllocateReq req);
	
	public List<Tuple> getPymtRetCurrDtls(String branchCode, String receiptSlNo);

	public List<Tuple> getTranContDtls(GetAllTransContractReq req);
	
	public List<Map<String, Object>> getReversalInfo(String[] args);
	
	public List<Map<String, Object>> getReversalInfoTreasury(String[] args);
	
	public Integer getAllocationStatus(String brachCode, String paymentReceiptNo);

	public Integer updatePremiumDetails(String[] args);

	public Integer updatepreSetStatus(String[] updateArgs);

	public Integer updateclaimPymtAlloDtls(String[] updateArgs);

	public Integer updateclaimSetStatus(String[] updateArgs);

	public String getAmend(String serialno, String string);

	public String getTrans(String policyno, String string);
	
}
