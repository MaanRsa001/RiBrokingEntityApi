package com.maan.insurance.jpa.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceipt;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.jpa.mapper.TtrnAllocatedTransactionMapper;
import com.maan.insurance.jpa.mapper.TtrnPaymentReceiptDetailsMapper;
import com.maan.insurance.jpa.mapper.TtrnPaymentReceiptMapper;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.jpa.repository.treasury.TtrnAllocatedTransactionRepository;
import com.maan.insurance.jpa.repository.treasury.TtrnPaymentReceiptDetailsRepository;
import com.maan.insurance.jpa.repository.treasury.TtrnPaymentReceiptRepository;
import com.maan.insurance.model.req.AllocateDetailsReq;
import com.maan.insurance.model.req.AllocateViewReq;
import com.maan.insurance.model.req.AllocatedStatusReq;
import com.maan.insurance.model.req.CurrecyAmountReq;
import com.maan.insurance.model.req.GenerationReq;
import com.maan.insurance.model.req.GetAllTransContractReq;
import com.maan.insurance.model.req.GetReceiptAllocateReq;
import com.maan.insurance.model.req.GetReceiptGenerationReq;
import com.maan.insurance.model.req.GetReceiptReversalListReq;
import com.maan.insurance.model.req.GetRetroallocateTransactionReq;
import com.maan.insurance.model.req.GetReversalInfoReq;
import com.maan.insurance.model.req.GetTransContractListReq;
import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.GetTreasuryJournalViewReq;
import com.maan.insurance.model.req.PaymentRecieptReq;
import com.maan.insurance.model.req.ReceiptTreasuryReq;
import com.maan.insurance.model.req.ReceiptViewListReq;
import com.maan.insurance.model.req.ReciptListReq;
import com.maan.insurance.model.req.RetroTransReq;
import com.maan.insurance.model.req.ReverseInsertReq;
import com.maan.insurance.model.req.ReverseViewReq;
import com.maan.insurance.model.req.SecondPageInfoReq;
import com.maan.insurance.model.res.AllocateDetailsRes;
import com.maan.insurance.model.res.AllocateDetailsRes1;
import com.maan.insurance.model.res.AllocateViewCommonRes;
import com.maan.insurance.model.res.AllocateViewCommonRes1;
import com.maan.insurance.model.res.AllocateViewRes;
import com.maan.insurance.model.res.AllocatedStatusRes;
import com.maan.insurance.model.res.AllocatedStatusRes1;
import com.maan.insurance.model.res.AllocatedStatusResponse;
import com.maan.insurance.model.res.CurrecyAmountListsRes;
import com.maan.insurance.model.res.CurrencyAmountRes;
import com.maan.insurance.model.res.GetAllRetroTransContractRes;
import com.maan.insurance.model.res.GetAllTransContractRes;
import com.maan.insurance.model.res.GetAllTransContractRes1;
import com.maan.insurance.model.res.GetDirectCedingRes;
import com.maan.insurance.model.res.GetDirectCedingRes1;
import com.maan.insurance.model.res.GetReceiptAllocateRes;
import com.maan.insurance.model.res.GetReceiptAllocateRes1;
import com.maan.insurance.model.res.GetReceiptEditRes;
import com.maan.insurance.model.res.GetReceiptEditRes1;
import com.maan.insurance.model.res.GetReceiptGenerationRes;
import com.maan.insurance.model.res.GetReceiptGenerationRes1;
import com.maan.insurance.model.res.GetReceiptReversalListRes;
import com.maan.insurance.model.res.GetReceiptReversalListRes1;
import com.maan.insurance.model.res.GetRetroallocateTransactionRes;
import com.maan.insurance.model.res.GetReversalInfoRes;
import com.maan.insurance.model.res.GetReversalInfoRes1;
import com.maan.insurance.model.res.GetShortnameRes;
import com.maan.insurance.model.res.GetTransContractRes;
import com.maan.insurance.model.res.GetTransContractRes1;
import com.maan.insurance.model.res.GetTreasuryJournalViewRes;
import com.maan.insurance.model.res.GetTreasuryJournalViewRes1;
import com.maan.insurance.model.res.ListSecondPageInfo;
import com.maan.insurance.model.res.PaymentRecieptRes;
import com.maan.insurance.model.res.PaymentRecieptRes1;
import com.maan.insurance.model.res.ReceiptTreasuryListRes;
import com.maan.insurance.model.res.ReceiptTreasuryRes;
import com.maan.insurance.model.res.ReceiptViewListsRes;
import com.maan.insurance.model.res.ReciptGetLIstRes;
import com.maan.insurance.model.res.ReciptListRes;
import com.maan.insurance.model.res.ReciptViewRes;
import com.maan.insurance.model.res.RetroTransListRes;
import com.maan.insurance.model.res.RetroTransRes;
import com.maan.insurance.model.res.ReverseInsertRes;
import com.maan.insurance.model.res.ReverseRes;
import com.maan.insurance.model.res.ReverseViewRes;
import com.maan.insurance.model.res.ReverseViewRes1;
import com.maan.insurance.model.res.SecondPageInfoListsRes;
import com.maan.insurance.model.res.SecondPageInfoRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.service.TreasuryService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;

@Component
public class TreasuryJpaServiceImpl  implements TreasuryService  {
	private Logger log = LogManager.getLogger(TreasuryJpaServiceImpl.class);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private DropDownServiceImple dropimpl;

	@Autowired
	private TreasuryCustomRepository treasuryCustomRepository;

	@Autowired
	private TtrnPaymentReceiptRepository ttrnPaymentReceiptRepository;

	@Autowired
	private TtrnPaymentReceiptDetailsRepository ttrnPaymentReceiptDetailsRepository;

	@Autowired
	private TtrnPaymentReceiptMapper ttrnPaymentReceiptMapper;

	@Autowired
	private TtrnPaymentReceiptDetailsMapper ttrnPaymentReceiptDetailsMapper;
	
	@Autowired
	private TtrnAllocatedTransactionRepository ttrnAllocatedTransactionRepository;
	
	@Autowired
	private TtrnAllocatedTransactionMapper ttrnAllocatedTransactionMapper;
	
	@Autowired
	private TtrnAllocatedTransactionRepository allocRepo;
	@PersistenceContext
	private EntityManager em;

	// savepaymentReciept
	public CommonResponse savepaymentReciept(PaymentRecieptReq req) {
		CommonResponse res = new CommonResponse();
		try {
			if (StringUtils.isBlank(req.getSerialno())) {
				String refno = queryImpl.getSequenceNo(
						"PT".equalsIgnoreCase(req.getTransType()) ? "TreasuryrPR" : "TreasuryrRP", "", "",
						req.getBranchCode(), "", req.getTransactionDate());
				req.setSerialno(refno);
			}
			TtrnPaymentReceipt entity = ttrnPaymentReceiptMapper.toEntity(req);
			ttrnPaymentReceiptRepository.save(entity);
			generationInsert(req);
			res.setMessage(req.getSerialno());
			res.setMessage("Success");

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
		}
		return res;
	}

	// generationInsert
	@Transactional
	public void generationInsert(PaymentRecieptReq req) {
		try {
			if (!"Treasury".equals(req.getTransactionType())) {
				if (req.getGenerationReq() != null) {
					for (int i = 0; i < req.getGenerationReq().size(); i++) {
						GenerationReq request = req.getGenerationReq().get(i);
						String currencyId = StringUtils.isEmpty(request.getCurrencyValList()) ? "0"
								: request.getCurrencyValList();
						String recNo = StringUtils.isEmpty(request.getRecNoValList()) ? "" : request.getRecNoValList();
						if (!"0".equalsIgnoreCase(currencyId)) {
							TtrnPaymentReceiptDetails entity = ttrnPaymentReceiptDetailsMapper.toEntity(request);
							if (recNo.length() > 0) {
								entity.setTransDate(ttrnPaymentReceiptDetailsMapper.getTimestamp(req.getTransactionDate()));
								entity.setReceiptNo(new BigDecimal(recNo));
								Integer maxAmendId = treasuryCustomRepository.getMaxAmendIdSq(
										ttrnPaymentReceiptDetailsMapper.format(recNo),
										ttrnPaymentReceiptDetailsMapper.format(req.getSerialno()));
								entity.setAmendId(new BigDecimal(maxAmendId != null ? Long.valueOf(maxAmendId.longValue()) : null));
								entity.setReceiptSlNo(new BigDecimal(req.getSerialno()));
								entity.setLoginId(req.getLoginId());
								entity.setBranchCode(req.getBranchCode());
							} else {
								entity.setTransDate(
										ttrnPaymentReceiptDetailsMapper.getTimestamp(req.getTransactionDate()));
								Integer receiptNo = treasuryCustomRepository.getNextRetDtlsNo();
								entity.setReceiptNo(new BigDecimal(receiptNo != null ? Long.valueOf(receiptNo.longValue()) : null));
								entity.setReceiptSlNo(new BigDecimal(req.getSerialno()));
								entity.setAmendId(new BigDecimal(0));
								entity.setLoginId(req.getLoginId());
								entity.setBranchCode(req.getBranchCode());
							}
							ttrnPaymentReceiptDetailsRepository.save(entity);
						}
					}
				}
				treasuryCustomRepository.updateDiffAmt(req);
			} else {
				treasuryCustomRepository.updateReversalPayment(req, "RT");
				treasuryCustomRepository.updateReversalPayment(req, "PT");
				treasuryCustomRepository.updateReversalPaymentDetails(req, true);
				treasuryCustomRepository.updateReversalPaymentDetails(req, false);
			}
		} catch (Exception exception) {
			log.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
		}
	}

	// getSecondPageInfo(SecondPageInfoReq req)
	public SecondPageInfoRes getSecondPageInfo(SecondPageInfoReq req) {
		SecondPageInfoRes response = new SecondPageInfoRes();
		SecondPageInfoListsRes res = new SecondPageInfoListsRes();
		List<SecondPageInfoListsRes> finalList = new ArrayList<SecondPageInfoListsRes>();
		log.info("PaymentDAOImpl getSecondPageInfo() || Enter");
		String diffAmt = "";
		List<String>paylist=new ArrayList<String>();
		try {
			List<TtrnPaymentReceiptDetails> list = treasuryCustomRepository.getSecondPageDtls(req);
			log.info("Query=>" + list);
			log.info("List Size=>" + list.size());
			double totAmt = 0.0;
			double totConRecCur = 0.0;
			
			List<ListSecondPageInfo> infos = new ArrayList<ListSecondPageInfo>();
			ListSecondPageInfo info = new ListSecondPageInfo();
			for (TtrnPaymentReceiptDetails ttrnPaymentReceiptDetails : list) {

				info.setCedingCompanyValList(ttrnPaymentReceiptDetails.getCurrencyId().toString());
				info.setExachangeValList(ttrnPaymentReceiptDetails.getExchangeRate() == null ? "0"
						: fm.formattereight(ttrnPaymentReceiptDetails.getExchangeRate().toString()));
				info.setPayamountValList(ttrnPaymentReceiptDetails.getAmount() == null ? "0"
						: fm.formatter(ttrnPaymentReceiptDetails.getAmount().toString()));
				info.setRowamountValList(ttrnPaymentReceiptDetails.getTotAmt() == null ? "0"
						: fm.formatter(ttrnPaymentReceiptDetails.getTotAmt().toString()));
				info.setRecNoValList(ttrnPaymentReceiptDetails.getReceiptNo().toString());
				info.setSetExcRateValList(ttrnPaymentReceiptDetails.getSettledExcrate() == null ? "0"
						: fm.formattereight(ttrnPaymentReceiptDetails.getSettledExcrate().toString()));
				info.setConRecCurValList(ttrnPaymentReceiptDetails.getConvertedReccur() == null ? "0"
						: fm.formatter(ttrnPaymentReceiptDetails.getConvertedReccur().toString()));

				infos.add(info);

				totAmt = totAmt + Double.parseDouble(ttrnPaymentReceiptDetails.getTotAmt() == null ? "0"
						: ttrnPaymentReceiptDetails.getTotAmt().toString());
				totConRecCur = totConRecCur
						+ Double.parseDouble(ttrnPaymentReceiptDetails.getConvertedReccur() == null ? "0"
								: ttrnPaymentReceiptDetails.getConvertedReccur().toString());
				finalList.add(res);
				paylist.add("");
			}
			res.setPaymentList(paylist);
			res.setTotalConRecCur(String.valueOf(totConRecCur));
			Integer iDiffAmt = treasuryCustomRepository
					.getDiffAmt(ttrnPaymentReceiptDetailsMapper.formatLong(req.getSerialno()), req.getBranchCode());
			diffAmt = (iDiffAmt == null ? "" : iDiffAmt.toString());
			log.info("Result=>" + diffAmt);
			if (diffAmt != null && diffAmt.length() > 0) {
				double Amt = Double.parseDouble(diffAmt);
				if (Amt < 0) {
					Amt = Amt * (-1);
					totAmt = totAmt - Amt;
					res.setDifftype("M");
				} else {
					res.setDifftype("B");
					totAmt = totAmt + Amt;
				}
				res.setTxtDiffAmt(String.valueOf(Amt));
			} else {
				res.setDifftype("N");
			}
			res.setTxtTotalAmt(String.valueOf(totAmt));
			res.setHideRowCnt(String.valueOf(list.size()));
			res.setListSecondPageInfo(infos);
			response.setCommonResponse(finalList);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);

		}
		return response;
	}
	// getReceiptEdit(String paymentReceiptNo, String branchCode)
	public GetReceiptEditRes1 getReceiptEdit(String paymentReceiptNo, String branchCode) {
		GetReceiptEditRes1 response = new GetReceiptEditRes1();
		GetReceiptEditRes res = new GetReceiptEditRes();
		try {
			// query - payment.select.getRetDtls
			List<TtrnPaymentReceipt> list = treasuryCustomRepository.getRetDtls(paymentReceiptNo, branchCode);
			if (!CollectionUtils.isEmpty(list)) {
				if (list.size() > 0) {
					TtrnPaymentReceipt resMap = list.get(0);
					res.setPayRecNo(
							resMap.getPaymentReceiptNo() == null ? "" : resMap.getPaymentReceiptNo().toString());
					res.setCedingCo(resMap.getCedingId() == null ? "" : resMap.getCedingId().toString());
					res.setBroker(resMap.getBrokerId() == null ? "" : resMap.getBrokerId().toString());
					res.setReceiptBankId(resMap.getReceiptBank() == null ? "" : resMap.getReceiptBank().toString());
					res.setTrDate(resMap.getTransDate() == null ? ""
							: sdf.format(resMap.getTransDate()));
					res.setPaymentAmount(resMap.getPaidAmt() == null ? "" : resMap.getPaidAmt().toString());
					res.setCurrency(resMap.getCurrencyId() == null ? "" : resMap.getCurrencyId().toString());
					res.setExrate(resMap.getExchangeRate() == null ? "" : resMap.getExchangeRate().toString());
					res.setTransactionType(
							resMap.getTransactionType() == null ? "" : resMap.getTransactionType().toString());
					res.setTransactionTypeTest(
							resMap.getTransactionType() == null ? "" : resMap.getTransactionType().toString());
					res.setBankCharges(resMap.getBankCharges() == null ? "" : resMap.getBankCharges().toString());
					res.setNetAmt(resMap.getNetAmt() == null ? "" : resMap.getNetAmt().toString());
					res.setAmendDate(resMap.getAmendmentDate() == null ? ""
							: sdf.format(resMap.getAmendmentDate()));
					res.setRemarks(resMap.getRemarks() == null ? "" : resMap.getRemarks());
					res.setBankchargeDC(resMap.getBankCharges() == null && resMap.getExchangeRate() == null ? ""
							: (resMap.getBankCharges() / resMap.getExchangeRate()) + "");
					res.setNetAmtDC(resMap.getNetAmt() == null && resMap.getExchangeRate() == null ? ""
							: (resMap.getNetAmt() / resMap.getExchangeRate()) + "");
					res.setWithHoldingTaxOC(resMap.getWhTax() == null ? "" : resMap.getWhTax().toString());
					res.setPremiumLavy(resMap.getPremiumLavy() == null ? "" : resMap.getPremiumLavy().toString());
				}
			}
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;

	}

	// reverseView(ReverseViewReq req)
	public ReverseViewRes1 reverseView(ReverseViewReq req) {
		log.info("PaymentDAOImpl reverseView() || Enter");
		List<ReverseViewRes> resList = new ArrayList<ReverseViewRes>();
		ReverseViewRes1 response = new ReverseViewRes1();
		try {

			// query - payment.select.getAllTranDtls
			List<TtrnAllocatedTransaction> list = treasuryCustomRepository.getAllTranDtls(req);

			for (TtrnAllocatedTransaction ttrnAllocatedTransaction : list) {
				final ReverseViewRes tempBean = new ReverseViewRes();
				tempBean.setSerialNo(
						ttrnAllocatedTransaction.getSno() == null ? "" : ttrnAllocatedTransaction.getSno().toString());
				tempBean.setAllocateddate(ttrnAllocatedTransaction.getInceptionDate() == null ? ""
						: sdf.format(ttrnAllocatedTransaction.getInceptionDate()));
				tempBean.setReversalDate(ttrnAllocatedTransaction.getReversalDate() == null ? ""
						: sdf.format(ttrnAllocatedTransaction.getReversalDate()));
				tempBean.setContractNo(ttrnAllocatedTransaction.getContractNo() == null ? ""
						: ttrnAllocatedTransaction.getContractNo().toString());
				tempBean.setTransactionNo(ttrnAllocatedTransaction.getTransactionNo() == null ? ""
						: ttrnAllocatedTransaction.getTransactionNo().toString());
				tempBean.setProductName(ttrnAllocatedTransaction.getProductName() == null ? ""
						: ttrnAllocatedTransaction.getProductName());
				tempBean.setReversedAmount(ttrnAllocatedTransaction.getReversalAmount() == null ? ""
						: fm.formatter(ttrnAllocatedTransaction.getReversalAmount().toString()));
				tempBean.setType(ttrnAllocatedTransaction.getType() == null ? "" : ttrnAllocatedTransaction.getType());
				String status = ttrnAllocatedTransaction.getStatus() == null ? ""
						: ttrnAllocatedTransaction.getStatus();
				if (status.equals("Y"))
					tempBean.setStatus("Allocated");
				else if (status.equals("R"))
					tempBean.setStatus("Reverted");

				// query - payment.select.getSelCurrency
				String currency = treasuryCustomRepository.getSelCurrency(req.getBranchCode(),
						ttrnAllocatedTransaction.getCurrencyId() != null
								? ttrnAllocatedTransaction.getCurrencyId().toString()
								: "");
				tempBean.setCurrency(currency == null ? "" : currency);

				// query - payment.select.getAlloAmt
				Integer amount = treasuryCustomRepository.getAlloAmt(
						ttrnPaymentReceiptDetailsMapper.format(req.getSerialNo()),
						ttrnAllocatedTransaction.getCurrencyId() != null
								? ttrnAllocatedTransaction.getCurrencyId().toString()
								: "");
				tempBean.setAllTillDate(amount != null ? amount.toString() : "");

				tempBean.setPayRecNo("");
				tempBean.setBroker("");
				tempBean.setCedingCo("");
				resList.add(tempBean);
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	// getAllocatedStatus(AllocatedStatusReq req)
	public AllocatedStatusRes1 getAllocatedStatus(AllocatedStatusReq req) {
		AllocatedStatusRes1 response = new AllocatedStatusRes1();
		List<AllocatedStatusRes> finalList = new ArrayList<AllocatedStatusRes>();
		AllocatedStatusResponse res1 = new AllocatedStatusResponse();
		try {
			// payment.select.getPymtRetStatus
			List<Tuple>	list = treasuryCustomRepository.getPymtRetStatus(req.getBranchCode(), req.getPayRecNo(),
						req.getAlloccurrencyId());

			for (int i = 0; i < list.size(); i++) {
				Tuple resMap = list.get(i);
				AllocatedStatusRes tempBean = new AllocatedStatusRes();
				tempBean.setCurrency(resMap.get("CURRENCY_NAME")==null?"":resMap.get("CURRENCY_NAME").toString());
				tempBean.setAllocated(resMap.get("ALLOCATED")==null?"":fm.formatter(resMap.get("ALLOCATED").toString()));
				tempBean.setUtilized(resMap.get("UTILIZED")==null?"":fm.formatter(resMap.get("UTILIZED").toString()));	
				tempBean.setNotUtilized(resMap.get("NOTUTILIZED")==null?"":fm.formatter(resMap.get("NOTUTILIZED").toString()));	
				tempBean.setStatus(resMap.get("STATUS")==null?"":resMap.get("STATUS").toString());
				tempBean.setPaymentDate(resMap.get("PAYMANT_DATE")==null?"":resMap.get("PAYMANT_DATE").toString());
				tempBean.setBank(resMap.get("Bank_name")==null?"":resMap.get("Bank_name").toString());
				tempBean.setCedingCompanyName(resMap.get("Ceding_company")==null?"":resMap.get("Ceding_company").toString());
				finalList.add(tempBean);
			}
			if (StringUtils.isBlank(req.getBrokerName())) {
				String company = treasuryCustomRepository.getCompName(req.getBranchCode(), req.getBrokerId(), "B");
				res1.setBrokerName((company == null ? "" : company));
			}else {
				res1.setBrokerName(req.getBrokerName());
			}
			res1.setAllocatedStatusRes(finalList);
			response.setCommonResponse(res1);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	// getReceiptReversalList(GetReceiptReversalListReq req)
	public GetReceiptReversalListRes1 getReceiptReversalList(GetReceiptReversalListReq req) {
		List<GetReceiptReversalListRes> finalList=new ArrayList<GetReceiptReversalListRes>();
		
		GetReceiptReversalListRes1 response = new GetReceiptReversalListRes1();
		List<Tuple> tempList = new ArrayList<>();
		try {
			//query -- "getReversaltLists"
			List<Tuple> result = treasuryCustomRepository.getReversaltLists(req);
			tempList = new ArrayList<Tuple>(result);
			
			if(StringUtils.isNotBlank(req.getSearchType())){
				tempList = filter("getReversaltLists1", result, new String[] {req.getBranchCode()});
				
            	if(StringUtils.isNotBlank(req.getPaymentNoSearch()))
            		tempList = filter("getReversaltLists2", result, new String[] {req.getBranchCode(), req.getPaymentNoSearch()});
            	
            	if(StringUtils.isNotBlank(req.getBrokerNameSearch()))
            		tempList = filter("getReversaltLists3", result, new String[] {req.getBranchCode(), req.getBrokerNameSearch().toUpperCase()});
            	
            	if(StringUtils.isNotBlank(req.getCompanyNameSearch()))
            		tempList = filter("getReversaltLists4", result, new String[] {req.getBranchCode(), req.getCompanyNameSearch().toUpperCase()});
            	
			}

			int count = 0;
			for(int i = 0; i < tempList.size(); i++ ) {
				GetReceiptReversalListRes res = new GetReceiptReversalListRes();
				Tuple resMap = tempList.get(i);
				res.setPayRecNo(resMap.get(0)==null?"":resMap.get(0).toString());
				res.setCedingCo(resMap.get(1)==null?"":resMap.get(1).toString());
				res.setBroker(resMap.get(2)==null?"":resMap.get(2).toString());
				res.setName(resMap.get(3)==null?"":resMap.get(3).toString());
				res.setPayamount(resMap.get(4)==null?"":fm.formatter(resMap.get(4).toString()));
				res.setBrokerId(resMap.get(5)==null?"":resMap.get(5).toString());
				res.setSerialNo(resMap.get(6)==null?"":resMap.get(6).toString());
				
				//query - payment.select.getTotCount
				List<Long> countList = treasuryCustomRepository.getTotCount(resMap.get(0).toString(), "Y");
				count = countList.size();
				if(count>0) {
					res.setEditShowStatus("No");
				}
				else {
					res.setEditShowStatus("Yes");                	  
				}
				
				//query - payment.select.getTotCount
				countList = treasuryCustomRepository.getTotCount(resMap.get(0).toString(), "R");
				count = countList.size();
				//count = this.mytemplate.queryForInt(getQuery(DBConstants.PAYMENT_SELECT_GETTOTCOUNT),new Object[]{resMap.get("PAYMENT_RECEIPT_NO").toString(),"R"});
				if(count>0)
				{
					res.setReversedShowStatus("Yes");
				}else 
				{
					res.setReversedShowStatus("No");                	  
				}
				
				finalList.add(res);
			}
			response.setCommonResponse(finalList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	private List<Tuple> filter(String condition, List<Tuple> input, String...value){
		switch(condition) {
		case "getReversaltLists1":
			return input.stream()  
            .filter(tuple -> tuple.get(7).toString().equals(value[0]))   // filtering branch code  
            .collect(Collectors.toList());
			
		case "getReversaltLists2":
			return input.stream()  
            .filter(tuple -> tuple.get(7).toString().equals(value[0]) &&
            		tuple.get(0).toString().contains(value[1]))   // filtering Payment Receipt No  
            .collect(Collectors.toList());
			
		case "getReversaltLists3":
			return input.stream()  
            .filter(tuple -> tuple.get(7).toString().equals(value[0]) &&
            		tuple.get(2).toString().toUpperCase().contains(value[1]))   // filtering Broker  
            .collect(Collectors.toList());
			
		case "getReversaltLists4":
			return input.stream()  
            .filter(tuple -> tuple.get(7).toString().equals(value[0]) &&
            		tuple.get(1).toString().toUpperCase().contains(value[1]))   // filtering Company Name  
            .collect(Collectors.toList());
		}
		return input;
	}
	
	// getDirectCeding(String branchId)  -- STARTS
	//Required Model
	public GetDirectCedingRes1 getDirectCeding(String branchId) {
		 log.info("TreasuryDAOImpl getDirectCeding || Enter");
		 GetDirectCedingRes1 response1 = new GetDirectCedingRes1();
	        List<Tuple> result=new ArrayList<>();
	        List<GetDirectCedingRes> response = new ArrayList<GetDirectCedingRes>();
	        try{
	        	 //Query -- "ceding.select.direct"
	            result=treasuryCustomRepository.cedingSelectDirect(branchId);
	            for(int i=0 ; i<result.size(); i++) {
					GetDirectCedingRes tempBean=new GetDirectCedingRes();
					Tuple tempMap = result.get(i);
					tempBean.setCode(tempMap.get(0)==null?"":tempMap.get(0).toString());
					tempBean.setCodeDescription(tempMap.get(1)==null?"":tempMap.get(1).toString());
					response.add(tempBean);
				}
	            
	            log.info("Result Size====>"+result.size());
	            response1.setCommonResponse(response);
				response1.setMessage("Success");
				response1.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response1.setMessage("Failed");
				response1.setIsError(true);
			}
			return response1;
	}
	
	// getDirectCeding(String branchId)  -- ENDS
	
	
	//getShortname(String branchcode) -- STARTS
	public GetShortnameRes getShortname(String branchcode) {
		GetShortnameRes res = new GetShortnameRes();
		String Short="";
		String result = treasuryCustomRepository.getShortName(branchcode);
		Short = (result == null ? "" : result);
		res.setCommonResponse(Short);
		return res;
	}
	//getShortname(String branchcode) -- ENDS
	
	//getCurrecyAmount(CurrecyAmountReq req) -- STARTS
	public CommonSaveRes getCurrecyAmount(CurrecyAmountReq req) {
		CommonSaveRes response = new CommonSaveRes();
		CurrecyAmountListsRes finalList = new CurrecyAmountListsRes();
		String  currecyAmount="";
		try{
			//query -- payment.select.currencyAmt
			List<Tuple> list = treasuryCustomRepository.currencyAmt(req);
			log.info("Select Query=>"+list);
			log.info("List Size=>"+list.size());

			if(list!=null &&list.size()>0) {
				Tuple resMap = list.get(0);
				currecyAmount=(new BigDecimal(resMap.get(0).toString()).doubleValue()-new BigDecimal(resMap.get(1).toString()).doubleValue())+"$"+resMap.get(2).toString();
				
			}
			response.setResponse(currecyAmount);
			response.setMessage("Success");
			response.setIsError(false);
		} 
		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	//getCurrecyAmount(CurrecyAmountReq req) -- ENDS
	
	//getReceiptViewList(ReceiptViewListReq req)  -- STARTS
	public ReceiptViewListsRes getReceiptViewList(ReceiptViewListReq req) {
		ReceiptViewListsRes response = new ReceiptViewListsRes();
		log.info("PaymentDAOImpl getReceiptViewList() || Enter"); 
		List<ReciptViewRes> finalList=new ArrayList<ReciptViewRes>();
		try
		{
			//query -- payment.select.getRetViewDtls
			List<Tuple> resultList = treasuryCustomRepository.getRetViewDtls(req);
			for(int i=0;i<resultList.size();i++) {
				ReciptViewRes tempBean=new ReciptViewRes();
				Tuple resMap = resultList.get(i);
				tempBean.setPayres(resMap.get(0)==null?"":resMap.get(0).toString());
				tempBean.setSerialno(resMap.get(1)==null?"":resMap.get(1).toString());
				tempBean.setPayamount(resMap.get(2)==null?"":fm.formatter(resMap.get(2).toString()));
				tempBean.setExrate(resMap.get(3)==null?"":fm.formattereight(resMap.get(3).toString()));
				tempBean.setInceptiobdate(resMap.get(4)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(4)).toString());
				tempBean.setCurrency(resMap.get(5)==null?"":resMap.get(5).toString());
				tempBean.setTotAmount(resMap.get(6)==null?"":fm.formatter(resMap.get(6).toString()));
				tempBean.setSetExcRate(resMap.get(7)==null?"":fm.formattereight(resMap.get(7).toString()));
				tempBean.setConRecCur(resMap.get(8)==null?"":fm.formatter(resMap.get(8).toString()));
				tempBean.setHideRowCnt(Integer.toString(resultList.size()));
				finalList.add(tempBean);
			}
			response.setCommonResponse(finalList);
		}
		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	//getReceiptViewList(ReceiptViewListReq req)  -- ENDS
	
	//getReceiptTreasuryGeneration(ReceiptTreasuryReq req) --STARTS
	public ReceiptTreasuryListRes getReceiptTreasuryGeneration(ReceiptTreasuryReq req) {
		List<ReceiptTreasuryRes> finalList = new ArrayList<ReceiptTreasuryRes>();
		ReceiptTreasuryListRes response =new ReceiptTreasuryListRes();
		List<Tuple> recipt;
		try {
			if("63".equals(req.getBroker())) 
				//query -- payment.select.getDirBroDtls
				recipt = treasuryCustomRepository.getDirBroDtls(req);
			
			else 
				//query -- payment.select.getBroDtls
				recipt = treasuryCustomRepository.getBroDtls(req);
			
			log.info("Query==> " + recipt);

			//for(int i=0;i<list.size();i++) {
			if(recipt.size()>0) {
				ReceiptTreasuryRes tempBean=new ReceiptTreasuryRes();
				Tuple resMap = recipt.get(0);
				tempBean.setCedingCo(resMap.get(0)==null?"":resMap.get(0).toString());
				tempBean.setBrokername(resMap.get(1)==null?"":resMap.get(1).toString());
				tempBean.setCurrency(resMap.get(2)==null?"":resMap.get(2).toString());
				tempBean.setPaymentamount(resMap.get(3)==null?"":fm.formatter(resMap.get(3).toString()));
				tempBean.setTrdate(resMap.get(4)==null?"":resMap.get(4).toString());
				tempBean.setSerialno(resMap.get(5)==null?"":resMap.get(5).toString());
				tempBean.setExchangerate(resMap.get(6)==null?"":resMap.get(6).toString());
				tempBean.setExrate(resMap.get(7)==null?"":fm.formatter(resMap.get(7).toString()));
				tempBean.setTotalexchaamount(resMap.get(15)==null?"":fm.formatter(resMap.get(15).toString()));
				tempBean.setBroker(resMap.get(8)==null?"":resMap.get(8).toString());
				tempBean.setDiffAmount(resMap.get(9)==null?"":fm.formatter(resMap.get(9).toString()));
				tempBean.setTransactionType(resMap.get(10)==null?"":resMap.get(10).toString());
				tempBean.setCurrencyValue(resMap.get(11)==null?"":resMap.get(11).toString());
				tempBean.setPayrecno(resMap.get(12)==null?"":resMap.get(12).toString());
				tempBean.setBankCharges(resMap.get(13)==null?"":fm.formatter(resMap.get(13).toString()));
				tempBean.setNetAmt(resMap.get(14)==null?"":fm.formatter(resMap.get(14).toString()));
				tempBean.setReceiptBankName(resMap.get(16)==null?"":resMap.get(16).toString());
                tempBean.setWithHoldingTaxOC(resMap.get(17) == null ? "" : fm.formatter(resMap.get(17).toString()));
                tempBean.setPremiumLavy(resMap.get(19) == null ? "" : fm.formatter(resMap.get(19).toString()));
                finalList.add(tempBean);
            }
			//}
			response.setCommonResponse(finalList);
		}	
		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	// allocateView(AllocateViewReq req) -- STARTS
	public AllocateViewCommonRes1 allocateView(AllocateViewReq req) {
		log.info("PaymentDAOImpl allocateView() || Enter");
		AllocateViewCommonRes1 res1 = new AllocateViewCommonRes1();
		AllocateViewCommonRes response = new AllocateViewCommonRes();
		
		String[] args = null;
		final List<AllocateViewRes> alllist = new ArrayList<AllocateViewRes>();
		final List<AllocateViewRes> allocatedList = new ArrayList<AllocateViewRes>();
		final List<AllocateViewRes> revertedList = new ArrayList<AllocateViewRes>();
		try {
			List<Tuple> allocateView = new ArrayList<>();
			// query -- payment.select.getAlloTransDtls1
			args = new String[2];
			if (StringUtils.isNotBlank(req.getPayRecNo())) {
				args[0] = req.getPayRecNo();
			} else {
				args[0] = req.getSerialNo();
			}
				allocateView = treasuryCustomRepository.getAlloTransDtls(args[0], null);
			if (!"".equals(req.getSerialNo()) && !"VIEW".equalsIgnoreCase(req.getFlag())) {
				// query -- payment.select.getAlloTransDtls
				allocateView = treasuryCustomRepository.getAlloTransDtls(args[0], req.getSerialNo());
			}
			

			if (!"".equals(req.getSerialNo()) && !"VIEW".equalsIgnoreCase(req.getFlag())) {
				
				//query -- payment.select.getBroCedingIds
				List<Tuple> cedingList =  treasuryCustomRepository.getBroCedingIds(req.getPayRecNo(), req.getBranchCode());

				if (cedingList.size() > 0) {

					String input = cedingList.get(0).get(0) == null ? ""
							: cedingList.get(0).get(0).toString();
					
					//query -- common.select.getCompName
					String brokerName = treasuryCustomRepository.getCompName(req.getBranchCode(), input, "B");
					response.setBrokerName(brokerName);
					if ("63".equals(cedingList.get(0).get(0) == null ? ""
							: cedingList.get(0).get(0).toString())) {
						
						input = cedingList.get(0).get(1) == null ? ""
								: cedingList.get(0).get(1).toString();
						//query -- common.select.getCompName
						String compName = treasuryCustomRepository.getCompName(req.getBranchCode(), input, "C");
						response.setCedingCo((compName == null ? ""	: compName.toString()));
						
					}
				}
				args[1] = req.getAlloccurrencyId();

				//query -- payment.select.getPymtRetDtls
				List<Tuple >amount = treasuryCustomRepository.getPymtRetDtls(args[0], args[1]);
				if (!CollectionUtils.isEmpty(amount)) {
					Double diff = (Double) amount.get(0).get(0)- (Double) amount.get(0).get(1);
					response.setAllTillDate(diff == null ? ""
							: diff.toString());
				}

			} 

			if (allocateView.size() > 0) {

				for (int i = 0; i < allocateView.size(); i++) {
					AllocateViewRes res = new AllocateViewRes();
					Tuple resMap = allocateView.get(i);
					res.setSerialNo(resMap.get(0) == null ? "" : resMap.get(0).toString());
					res.setAllocatedDate(
							resMap.get(1) == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(1)).toString());
					res.setTransactionNo(
							resMap.get(5) == null ? "" : resMap.get(5).toString());
					res.setContractNo(resMap.get(6) == null ? "" : resMap.get(6).toString());
					res.setProductName(resMap.get(7) == null ? "" : resMap.get(7).toString());
					res.setType(resMap.get(8) == null ? "" : resMap.get(8).toString());
					res.setCurrencyValue(resMap.get(12) == null ? "" : resMap.get(12).toString());
					res.setAlloccurrencyId(resMap.get(12)==null?"":resMap.get(12).toString());
					res.setStatus(("R".equals(resMap.get(13) == null ? "" : resMap.get(13).toString())
							? "Reverted"
							: "Allocated"));
					if ("Reverted".equalsIgnoreCase(res.getStatus())) {
						res.setPayAmount(
								resMap.get(4) == null ? "" : resMap.get(4).toString());
						res.setAllocatedDate(
								resMap.get(2) == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(2)).toString());
						res.setAllocatedDate(
								resMap.get(2) == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(2)).toString());
						res.setCheckPc((resMap.get(3) == null ? ""
								: resMap.get(3).toString()));
					} else {
						res.setPayAmount(resMap.get(10) == null ? "" : resMap.get(10).toString());
						res.setAllocatedDate(
								resMap.get(1) == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(1)).toString());
						res.setAllocatedDate(resMap.get(1)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(1)).toString());
						res.setCheckPc((resMap.get(9) == null ? ""
								: resMap.get(9).toString()));
					}
					if(resMap.getElements().size() > 15)
						res.setAdjustmentType(
								(resMap.get(15) == null ? "" : resMap.get(15).toString()));

					//query -- payment.select.getSelCurrency
					String currency = treasuryCustomRepository.getSelCurrency(req.getBranchCode(), res.getCurrencyValue());
					res.setCurrencyName(currency == null ? "" : currency);

					args = new String[2];
					if (StringUtils.isNotBlank(req.getPayRecNo())) {
						args[0] = req.getPayRecNo();
					} else {
						args[0] = req.getSerialNo();
					}
					args[1] = res.getCurrencyValue();

					//query -- payment.select.getExchRate
					BigDecimal exchangeRate = treasuryCustomRepository.getExchRate(args[0], args[1]);
					res.setExchangeRate(exchangeRate == null ? "" : exchangeRate.toString());

					alllist.add(res);
					if ("Reverted".equalsIgnoreCase(res.getStatus())) {
						revertedList.add(res);
					} else {
						allocatedList.add(res);
					}
				}
			}
			response.setAlllist(alllist);
			response.setAllocatedList(allocatedList);
			response.setRevertedList(revertedList);
			res1.setCommonResponse(response);
			res1.setMessage("Success");
			res1.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				res1.setMessage("Failed");
				res1.setIsError(true);
			}
		return res1;
	}
	// allocateView(AllocateViewReq req) -- ENDS
	
	// allocateDetails(AllocateDetailsReq req)  --STARTS
	public AllocateDetailsRes1 allocateDetails(AllocateDetailsReq req) {
		AllocateDetailsRes1 response = new AllocateDetailsRes1();
		List<AllocateDetailsRes> finalList = new ArrayList<AllocateDetailsRes>();
		try{
			List<Tuple> list = treasuryCustomRepository.getAllocatedTls(req.getPayRecNo());
			
			for(int i = 0; i < list.size(); i++) {
				Tuple resMap = list.get(i);
				AllocateDetailsRes res = new AllocateDetailsRes();
				res.setSerialNo(resMap.get(0)==null?"":resMap.get(0).toString());
				res.setAllocatedDate(resMap.get(2)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(2)).toString());
				res.setCurrencyValue(resMap.get(1)==null?"":resMap.get(1).toString());
				res.setAdjustmentType(resMap.get(3)==null?"":resMap.get(3).toString());
				
				//query -- payment.select.getSelCurrency
				String currency = treasuryCustomRepository.getSelCurrency(req.getBranchCode(), resMap.get(1).toString());
				res.setCurrency((currency == null ? "" : currency));
				
				//query -- GET_ALOCATION_TYPE
				//String type = treasuryCustomRepository.getAllcoationType(req.getSerialNo());
				res.setRetroType(resMap.get(3)==null?"":resMap.get(3).toString());
				Double amount = null;
				if("RE".equalsIgnoreCase(resMap.get(3)==null?"":resMap.get(3).toString())){
					amount = treasuryCustomRepository.getRetAmtDtls1(res.getSerialNo());
				}
				else{
					amount = treasuryCustomRepository.getRetAmtDtls(res.getSerialNo());
				}
				res.setAllTillDate(fm.formatter(amount == null ? ""	: amount.toString()));
				res.setPayRecNo(req.getPayRecNo());
				res.setBroker(req.getBroker());
				res.setCedingCo(req.getCedingCo());
				finalList.add(res);
			}
		response.setCommonResponse(finalList);
		response.setMessage("Success");
		response.setIsError(false);
	}catch(Exception e){
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}
	// allocateDetails(AllocateDetailsReq req)  --ENDS
	
	// savereverseInsert(ReverseInsertReq req) -- STARTS
	public ReverseInsertRes savereverseInsert(ReverseInsertReq req) {
		String[] args = null;
		ReverseInsertRes response = new ReverseInsertRes();
		List<ReverseRes> reverseRes = new ArrayList<ReverseRes>();
		
		String currency = "";
		Double a = 0.0, b = 0.0, c = 0.0, diff = 0.0;
		try {
			List<Tuple> resList = new ArrayList<>();
			
			if (StringUtils.isBlank(req.getSerialNo())) {
				//Query -- payment.select.getAlloTransDtls1
				resList = treasuryCustomRepository.getAlloTransDtls(req.getPayRecNo(), null);
			} else {
				//Query -- payment.select.getAlloTransDtls
				resList = treasuryCustomRepository.getAlloTransDtls(req.getPayRecNo(), req.getSerialNo());
			}

			String curencyId = "";
			if (resList.size() > 0) {
				for (int i = 0; i < resList.size(); i++) {
					ReverseRes res1 = new ReverseRes();
					Tuple resMap = resList.get(i);
					curencyId = resMap.get(12) == null ? "" : resMap.get(12).toString();
					if ("Y".equalsIgnoreCase(resMap.get(13) == null ? "" : resMap.get(13).toString())) {

						res1.setSerialNo(resMap.get(0) == null ? "" : resMap.get(0).toString());
						res1.setAllocateddate(
								resMap.get(1) == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(1)).toString());
						String currencyId = resMap.get(12) == null ? ""
								: resMap.get(12).toString();
						
						//Query -- payment.select.getSelCurrency
						currency = treasuryCustomRepository.getSelCurrency(req.getBranchCode(), currencyId);
						currency = currency == null ? "" : currency;
						res1.setCurrency(currency);

						res1.setTransactionNo(
								resMap.get(5) == null ? "" : resMap.get(5).toString());
						res1.setContractNo(
								resMap.get(6) == null ? "" : resMap.get(6).toString());
						res1.setProductName(
								resMap.get(7) == null ? "" : resMap.get(7).toString());
						res1.setType(resMap.get(8) == null ? "" : resMap.get(8).toString());
						
						args = new String[8];
						args[0] = "R";
						args[1] = req.getReversalDate();
						args[2] = "RE".equalsIgnoreCase(resMap.get(8)==null?"":resMap.get(8).toString())?resMap.get(11)==null?"":resMap.get(11).toString():resMap.get(10)==null?"":resMap.get(10).toString();
						args[3] = "0";
						args[4] = req.getLoginId();
						args[5] = req.getBranchCode();
						args[6] = resMap.get(5)==null?"":resMap.get(5).toString();
						args[7] = resMap.get(0)==null?"":resMap.get(0).toString();
						
						//Query -- payment.update.allocatedDtls
						treasuryCustomRepository.updateAllocatedDtls(args);

						if ("P".equalsIgnoreCase(resMap.get(8) == null ? "" : resMap.get(8).toString())) {
							args = new String[6];
							args[0] = "Pending";
							args[1] = resMap.get(10)==null?"":resMap.get(10).toString();
							args[2] = req.getLoginId();
							args[3] = req.getBranchCode();
							args[4] = resMap.get(6)==null?"":resMap.get(6).toString();
							args[5] = resMap.get(5)==null?"":resMap.get(5).toString();
							
							//Query -- payment.update.rskPremDtls1
							treasuryCustomRepository.updateRskPremDtls(args);

							args = new String[2];
							args[0] = resMap.get(6)==null?"":resMap.get(6).toString();
							args[1] = resMap.get(5)==null?"":resMap.get(5).toString();
							
							//Query -- payment.select.getRskPremDtls
							List<Tuple> rskList = treasuryCustomRepository.getRskPremDtls(args[0], args[1]);
							diff = (Double) rskList.get(0).get(0) - (Double) rskList.get(0).get(1); 
							if (!CollectionUtils.isEmpty(rskList)) {
								res1.setNetDue(diff.toString());
							}

							a = a + Double.parseDouble(
									resMap.get(10) == null ? "" : resMap.get(10).toString());

						} else if ("C"
								.equalsIgnoreCase(resMap.get(8) == null ? "" : resMap.get(8).toString())) {
							args = new String[6];
							args[0] = "Pending";
							args[1] = resMap.get(10)==null?"":resMap.get(10).toString();
							args[2] = req.getBranchCode();
							args[3] = req.getLoginId();
							args[4] = resMap.get(6)==null?"":resMap.get(6).toString();
							args[5] = resMap.get(5)==null?"":resMap.get(5).toString();
							
							//Query -- payment.update.claimPymtDtls
							treasuryCustomRepository.updateClaimPymtDtls(args);

							args = new String[2];
							args[0] = resMap.get(6)==null?"":resMap.get(6).toString();
							args[1] = resMap.get(5)==null?"":resMap.get(5).toString();
							
							//Query -- payment.select.getClaimPymtDtls
							List<Tuple> claim = treasuryCustomRepository.getClaimPymtDtls(args[0], args[1]);
							diff = (Double) claim.get(0).get(0) - (Double) claim.get(0).get(1); 
							if (!CollectionUtils.isEmpty(claim)) {
								res1.setPayAmount((diff == null ? ""
										: diff.toString()));
							}

							b = b + Double.parseDouble(
									resMap.get(10) == null ? "" : resMap.get(10).toString());

						} else if ("RE"
								.equalsIgnoreCase(resMap.get(8) == null ? "" : resMap.get(8).toString())) {

							args = new String[3];
							args[0] = resMap.get(11)==null?"":resMap.get(11).toString();
							args[1] = resMap.get(6)==null?"":resMap.get(6).toString();
							args[2] = resMap.get(5)==null?"":resMap.get(5).toString();
							
							//Query -- payment.update.retro.payment
							treasuryCustomRepository.updateRetroPayment(args);

							args = new String[2];
							args[0] = resMap.get(6)==null?"":resMap.get(6).toString();
							args[1] = resMap.get(5)==null?"":resMap.get(5).toString();
							
							//Query -- payment.get.retro.payment
							List<Tuple> retroSoa = treasuryCustomRepository.getRetroPayment(args[0], args[1]);
							diff = (Double) retroSoa.get(0).get(0) - (Double) retroSoa.get(0).get(1); 
							if (!CollectionUtils.isEmpty(retroSoa)) {
								res1.setNetDue(diff == null ? ""
										: diff.toString());
							}

							double netresultsign = Math.signum(Double.valueOf(Double.parseDouble(res1.getNetDue())));
							res1.setCheckPc("1.0".equals(String.valueOf(netresultsign)) ? "P" : "C");

							a = a + (req.getTransType().equals("PT")
									? (-1) * Double.parseDouble(resMap.get(11) == null ? ""
											: resMap.get(11).toString())
									: Double.parseDouble(resMap.get(11) == null ? ""
											: resMap.get(11).toString()));
						}
						reverseRes.add(res1);
					}

				}
				if ("RT".equalsIgnoreCase(req.getTransType())) {
					c = a - b;
					
				} else if ("PT".equalsIgnoreCase(req.getTransType())) {
					c = b - a;
					
				}
				if ("RE".equalsIgnoreCase(req.getRetroType())) {
					c = a;
				}

				args = new String[5];
				args[0] = c.toString();
				args[1] = req.getLoginId();
				args[2] = req.getBranchCode();
				args[3] = req.getPayRecNo();
				args[4] = curencyId;
				
				//Query -- payment.update.pymtRetDtls
				treasuryCustomRepository.updatePymtRetDtls(args);

				args = new String[2];
				args[0] = req.getPayRecNo();
				args[1] = curencyId;
				
				//Query -- payment.select.getPymtRetDtls
				List<Tuple> paymentRet = treasuryCustomRepository.getPymtRetDtls(req.getPayRecNo(), curencyId);
				diff = (Double) paymentRet.get(0).get(0) - (Double) paymentRet.get(0).get(1); 
				
				if (!CollectionUtils.isEmpty(paymentRet)) {
					//res1.setPayAmount(diff == null ? "": diff.toString());
				}

				
				response.setCommonResponse(reverseRes);

			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;

	}
	
	
	//getReceiptGeneration(GetReceiptGenerationReq req)
	public GetReceiptGenerationRes1 getReceiptGeneration(GetReceiptGenerationReq req) {
		GetReceiptGenerationRes1 response = new GetReceiptGenerationRes1();
		GetReceiptGenerationRes res = new GetReceiptGenerationRes();
		List<Tuple> list = new ArrayList<>();
		try {
			if ("63".equals(req.getBroker())) {
				//query -- payment.select.getDirBroDtls
				list = treasuryCustomRepository.getDirBroDtls(req);

			} else {
				//query -- payment.select.getDirBroDtls
				list = treasuryCustomRepository.getBroDtls(req);
			}

			if (!CollectionUtils.isEmpty(list)) {
				Tuple resMap = list.get(0);
				res.setCedingCo(resMap.get(0) == null ? "" : resMap.get(0).toString());
				res.setBrokerName(resMap.get(1) == null ? "" : resMap.get(1).toString());

				res.setCurrecncy(resMap.get(2) == null ? "" : resMap.get(2).toString());
				res.setPaymentAmount(
						resMap.get(3) == null ? "" : fm.formatter(resMap.get(3).toString()));
				res.setTrDate(resMap.get(4) == null ? "" : new SimpleDateFormat("dd/MM/yyyy").format(resMap.get(4)).toString());
				res.setSerialNo(
						resMap.get(5) == null ? "" : resMap.get(5).toString());
				res.setExchangeRate(resMap.get(6) == null ? "" : resMap.get(6).toString());

				res.setEntrate(resMap.get(6) == null ? ""
						: fm.formatter(resMap.get(6).toString()));
				res.setTotalExchaAmount(
						resMap.get(7) == null ? "" : fm.formatter(resMap.get(7).toString()));
				res.setBroker(resMap.get(8) == null ? "" : resMap.get(8).toString());
				res.setDiffAmount(
						resMap.get(9) == null ? "" : fm.formatter(resMap.get(9).toString()));
				res.setConvDiffAmount(resMap.get(15) == null ? ""
						: fm.formatter(resMap.get(15).toString()));
				res.setTransactionType(
						resMap.get(10) == null ? "" : resMap.get(10).toString());

				res.setCurrecncyValue(resMap.get(11) == null ? "" : resMap.get(11).toString());
				res.setPayRecNo(resMap.get(12) == null ? "" : resMap.get(12).toString());
				res.setBankCharges(
						resMap.get(13) == null ? "" : fm.formatter(resMap.get(13).toString()));
				res.setNetAmt(resMap.get(14) == null ? "" : fm.formatter(resMap.get(14).toString()));
				res.setReceiptBankName(resMap.get(16) == null ? "" : resMap.get(16).toString());
				res.setWithHoldingTaxOC(
						resMap.get(17) == null ? "" : fm.formatter(resMap.get(17).toString()));
				res.setRemarks(resMap.get(18) == null ? "" : resMap.get(18).toString());
				res.setPremiumLavy(
						resMap.get(19) == null ? "" : fm.formatter(resMap.get(19).toString()));
			}
			response.setCommonResponse(res);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	
	//getRetroallocateTransaction(GetRetroallocateTransactionReq req)
	public CommonResponse getRetroallocateTransaction(GetRetroallocateTransactionReq req) {
		CommonResponse response = new CommonResponse();
		log.info("PaymentDAOImpl getRetroallocateTransaction() || Enter");

		String[] args=null;
		try{
			
			List<GetAllRetroTransContractRes> payList = getAllRetroTransContract(req);
			
			Double a=0.0;
			String serialNo;
			
			serialNo=getSequence("TreasuryARP","","", req.getBranchCode(),"",req.getAccountDate());
			req.setSerialNo(serialNo);
			for(int i=0;i<payList.size();i++) {
				GetAllRetroTransContractRes form= payList.get(i);
				List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> form.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
				if(!CollectionUtils.isEmpty(filterTrack)) {
					
					 args=new String[17];
					 args[0]=serialNo;	
					 args[1]=form.getContractNo();
					
					 args[2]=StringUtils.isBlank(form.getMode())?"0":form.getMode();
					 args[3]=form.getProductName();
					 args[4]=form.getTransactionNo();
					 args[5]=req.getAccountDate();
					
					args[6]= req.getTransType().equals("PT")?Double.toString((-1)*Double.parseDouble(filterTrack.get(0).getTransactionNo())):Double.toString(Double.parseDouble(filterTrack.get(0).getTransactionNo()));
				 	args[7]="RE";
				 	
				 	String[] updateArgs = new String[3];
				 	updateArgs[0] = req.getTransType().equals("PT")?Double.toString((-1)*Double.parseDouble(filterTrack.get(0).getTransactionNo())):Double.toString(Double.parseDouble(filterTrack.get(0).getTransactionNo()));
					updateArgs[1] = form.getContractNo();
					updateArgs[2] = form.getTransactionNo();
					
					//Query -- payment.update.retro.details
				 	treasuryCustomRepository.updateRetroDetails(updateArgs);
					
					updateArgs[0] = "Allocated";
					updateArgs[1] = form.getContractNo();
					updateArgs[2] = form.getTransactionNo();
					
					//Query -- payment.update.retro.status
				 	treasuryCustomRepository.updateRetroStatus(updateArgs);
				 	
				 	a=a+Double.parseDouble(filterTrack.get(0).getTransactionNo());
					args[8]="Y";
					args[9]="0";
					args[10]=req.getPolicyNo();//Receipt No
					req.setPayRecNo(req.getPolicyNo());
					args[11]=req.getAllOccurencyId();//Currency ID
					args[12]=StringUtils.isBlank(req.getHideprocessType())?"I":"O";
					args[13]=req.getSubClass();
					args[14]=req.getLoginId();
					args[15]=req.getBranchCode();
					args[16]=req.getProposalNo();
					
					//query -- payment.insert.alloTran
					TtrnAllocatedTransaction ttrnAllocatedTransaction = ttrnAllocatedTransactionMapper.toEntity(args);
					ttrnAllocatedTransactionRepository.save(ttrnAllocatedTransaction);
				}
			}
			
			String[] updateArgs = new String[5];
			updateArgs[0] = String.valueOf(a);
			updateArgs[1] = req.getLoginId();
			updateArgs[2] = req.getBranchCode();
			updateArgs[3] = req.getPolicyNo();
			updateArgs[4] = req.getAllOccurencyId();
			
			//query -- "payment.update.AlloTranDtls";
			treasuryCustomRepository.updateAlloTranDtls(updateArgs);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	
	public List<GetAllRetroTransContractRes> getAllRetroTransContract(GetRetroallocateTransactionReq req) {
		List<GetAllRetroTransContractRes> payList = new ArrayList<GetAllRetroTransContractRes>();
		try {
			
			//Query -- payment.select.getDirBroDtls.retro
			List<Tuple> list = treasuryCustomRepository.getDirBroDtlsRetro(req);
			
			for(int i=0 ; i<list.size(); i++) {
				GetAllRetroTransContractRes tempBean=new GetAllRetroTransContractRes();
				Tuple tempMap = list.get(i);
				tempBean.setContractNo(tempMap.get(0)==null?"":tempMap.get(0).toString());
				tempBean.setTransactionNo(tempMap.get(1)==null?"":tempMap.get(1).toString());
				tempBean.setInceptiobdate(tempMap.get(3)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get(3)).toString());
				tempBean.setNetDue(tempMap.get(5)==null?"":tempMap.get(5).toString());
				tempBean.setCheckPC(tempMap.get(6)==null?"":tempMap.get(6).toString());
				tempBean.setCedingCompanyName(tempMap.get(7)==null?"":tempMap.get(7).toString());
				
				payList.add(tempBean);
			}
		}
		catch(Exception exception) {
			log.debug("Exception @ { " + exception + " } ");
		}
		return payList;
	}
	
	public synchronized String getSequence(String type,String productID,String departmentId,String branchCode, String proposalNo,String date){ 
		String seqName="";
		try{
			Long seqNo = treasuryCustomRepository.getseqno(new String[]{type,productID,departmentId,branchCode,proposalNo,date});
			seqName = (seqNo == null ? "": seqNo.toString());
			log.info("Result==> " + seqName);
			
		}catch(Exception e){
			log.debug("Exception @ {" + e + "}");
		}

		return seqName;
	}
	
	//getTransContract(GetTransContractReq req) -- STARTS
	public GetTransContractRes1 getTransContract(GetTransContractReq req) {
		List<GetTransContractRes> finalList=new ArrayList<GetTransContractRes>();
		GetTransContractRes1 response = new GetTransContractRes1();
        try {
           
        	List<Tuple> list = treasuryCustomRepository.getTranContDtls(req);
          
			for(int i=0 ,count=0; i<list.size(); i++,count++) {
				GetTransContractRes res = new GetTransContractRes();
				Tuple tempMap = list.get(i);
				res.setContractNo(tempMap.get(1)==null?"":tempMap.get(1).toString());
				res.setMode(tempMap.get(2)==null?"":tempMap.get(2).toString());
				res.setProductName(tempMap.get(3)==null?"":tempMap.get(3).toString());
				res.setTransactionNo(tempMap.get(0)==null?"":tempMap.get(0).toString());
				res.setInceptiobDate(tempMap.get(4)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get(4)).toString());
				res.setNetDue(tempMap.get(5)==null?"":tempMap.get(5).toString());
				res.setPayAmount(tempMap.get(6)==null?"":tempMap.get(6).toString());
				res.setAccPremium(tempMap.get(7)==null?"":tempMap.get(7).toString());
				res.setAccClaim(tempMap.get(8)==null?"":tempMap.get(8).toString());
				res.setCheckYN(tempMap.get(9)==null?"":tempMap.get(9).toString());
				res.setCheckPC(tempMap.get(10)==null?"":tempMap.get(10).toString());
				res.setCedingCompanyName(tempMap.get(11)==null?"":tempMap.get(11).toString());
				res.setAllocType(req.getAllocType());
				if(!CollectionUtils.isEmpty(req.getTransContractListReq())) {
					List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> res.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
					if(!CollectionUtils.isEmpty(filterTrack)) {
						res.setReceivePayAmounts(filterTrack.get(0).getReceivePayAmounts());
						res.setPreviousValue(filterTrack.get(0).getReceivePayAmounts());
						res.setPayAmounts(filterTrack.get(0).getReceivePayAmounts());
						res.setChkBox("true");
				
					}
					else {
						res.setReceivePayAmounts("");
						res.setPreviousValue("");
						res.setPayAmounts("");
						res.setChkBox("false");
					}
				}
				res.setTotalRecCount(String.valueOf(list.size()));
				res.setCount(String.valueOf(count));
				finalList.add(res);
			}
        response.setCommonResponse(finalList);
		response.setMessage("Success");
		response.setIsError(false);
	}catch(Exception e){
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}
	//getTransContract(GetTransContractReq req) -- ENDS
	
	//receiptdetail(PaymentRecieptReq req) -- STARTS
	public PaymentRecieptRes1 receiptdetail(PaymentRecieptReq req) {
		PaymentRecieptRes1 resp=new PaymentRecieptRes1();
		List<GenerationReq> lres=new ArrayList<GenerationReq>();
		try {
			for(int i=0;i<3;i++){
			GenerationReq res=new GenerationReq();
			if(i==0) {
			res.setCurrencyValList(req.getCurrency());
			res.setPayamountValList(fm.formatter(req.getPaymentAmount()));
			res.setSetExcRateValList(fm.formatter("1"));
			}
			lres.add(res);
			
			}
			resp.setGenerationReq(lres);
		} catch (Exception e) {
			e.printStackTrace();
			resp.setMessage("Failed");
			resp.setIsError(true);
		}
		return resp;
	}
	//receiptdetail(PaymentRecieptReq req) -- ENDS
	
	// getRetroTransContract(RetroTransReq req) -- STARTS
	public RetroTransListRes getRetroTransContract(RetroTransReq req) {
		RetroTransListRes response = new RetroTransListRes();
		List<RetroTransRes> finalList = new ArrayList<RetroTransRes>();

		try {
			// Query -- payment.select.getDirBroDtls.retro
			List<Tuple> list = treasuryCustomRepository.getDirBroDtlsRetro(req);

			for (int i = 0; i < list.size(); i++) {
				RetroTransRes tempBean = new RetroTransRes();
				Tuple tempMap = list.get(i);
				tempBean.setContractno(tempMap.get(0) == null ? "" : tempMap.get(0).toString());
				tempBean.setTransactionno(tempMap.get(1) == null ? "" : tempMap.get(1).toString());
				tempBean.setInceptiobdate(tempMap.get(3) == null ? ""
						: new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get(3)).toString());
				tempBean.setNetdue(tempMap.get(5) == null ? "" : tempMap.get(5).toString());
				tempBean.setCheckPC(tempMap.get(6) == null ? "" : tempMap.get(6).toString());
				tempBean.setCedingCompanyName(tempMap.get(7) == null ? "" : tempMap.get(7).toString());
				tempBean.setAllocType(req.getAllocType());
				List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream()
						.filter(o -> tempBean.getTransactionno().equalsIgnoreCase(o.getTransactionNo()))
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(filterTrack)) {
					tempBean.setReceivePayAmounts(
							Integer.toString(Math.abs(Integer.parseInt(filterTrack.get(0).getTransactionNo()))));
					tempBean.setPreviousValue(filterTrack.get(0).getTransactionNo());
					tempBean.setChkbox("true");
				} else {
					tempBean.setReceivePayAmounts("");
					tempBean.setPreviousValue("");
					tempBean.setChkbox("false");
				}
				finalList.add(tempBean);
			}
			response.setCommonResponse(finalList);
		} catch (Exception exception) {
			log.debug("Exception @ { " + exception + " } ");
		}
		return response;
	}
	//getRetroTransContract(RetroTransReq req) -- ENDS
	
	//getTreasuryJournalView(GetTreasuryJournalViewReq req) -- STARTS
	public GetTreasuryJournalViewRes1 getTreasuryJournalView(GetTreasuryJournalViewReq req) {
		GetTreasuryJournalViewRes1 response1 = new GetTreasuryJournalViewRes1();
		List<GetTreasuryJournalViewRes> response = new ArrayList<GetTreasuryJournalViewRes>();
		List<Tuple> result = new ArrayList<>();
		try {
			result = treasuryCustomRepository.treasuryJournalView(req);
			for (int i = 0; i < result.size(); i++) {
				GetTreasuryJournalViewRes tempBean = new GetTreasuryJournalViewRes();
				Tuple tempMap = result.get(i);
				tempBean.setStartDate(tempMap.get("STARTDATE") == null ? "" : tempMap.get("STARTDATE").toString());
				tempBean.setReference(tempMap.get("REFERENCE") == null ? "" : tempMap.get("REFERENCE").toString());
				tempBean.setLedger(tempMap.get("LEDGER") == null ? "" : tempMap.get("LEDGER").toString());
				tempBean.setUwy(tempMap.get("UWY") == null ? "" : tempMap.get("UWY").toString());
				tempBean.setSpc(tempMap.get("SPC") == null ? "" : tempMap.get("SPC").toString());
				tempBean.setCurrency(tempMap.get("CURRENCY") == null ? "" : tempMap.get("CURRENCY").toString());
				tempBean.setDebitoc(tempMap.get("DEBITOC") == null ? "" : tempMap.get("DEBITOC").toString());
				tempBean.setCreditoc(tempMap.get("CREDITOC") == null ? "" : tempMap.get("CREDITOC").toString());
				tempBean.setDebitugx(tempMap.get("DEBITUGX") == null ? "" : tempMap.get("DEBITUGX").toString());
				tempBean.setCreditugx(tempMap.get("CREDITUGX") == null ? "" : tempMap.get("CREDITUGX").toString());
				tempBean.setNarration(tempMap.get("NARRATION") == null ? "" : tempMap.get("NARRATION").toString());
				tempBean.setProductId(tempMap.get("PRODUCT_ID") == null ? "" : tempMap.get("PRODUCT_ID").toString());
				tempBean.setEndDate(tempMap.get("END_DATE") == null ? "" : tempMap.get("END_DATE").toString());
				response.add(tempBean);
			}
			response1.setCommonResponse(response);
			response1.setMessage("Success");
			response1.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response1.setMessage("Failed");
			response1.setIsError(true);
		}
		return response1;
	}
	//getTreasuryJournalView(GetTreasuryJournalViewReq req) -- ENDS
	
	//getReciptList(ReciptListReq req) -- STARTS
	public ReciptGetLIstRes getReciptList(ReciptListReq req) {
		ReciptGetLIstRes response = new ReciptGetLIstRes();
		List<ReciptListRes> finalList = new ArrayList<ReciptListRes>();
		List<Tuple> list = new ArrayList<>();
		List<Tuple> tempList = new ArrayList<>();
		int count = 0;
		try {
			GetOpenPeriodRes openres=dropimpl.getOpenPeriod(req.getBranchCode());
			
			// Query -- payment.select.getRetLists
			list = treasuryCustomRepository.getRetLists(req);
			Collections.sort(list, (e1, e2) -> {
				if (e2.get("PAYMENT_RECEIPT_NO") == null) {
					return (e1.get("PAYMENT_RECEIPT_NO") == null) ? 0 : -1;
				}
				if (e1.get("PAYMENT_RECEIPT_NO") == null) {
					return 1;
				}
				return ((Long) e2.get("PAYMENT_RECEIPT_NO")).compareTo((Long) e1.get("PAYMENT_RECEIPT_NO"));
			});
			tempList = new ArrayList<Tuple>(list);
			
			if(StringUtils.isNotBlank(req.getSearchType())){
				
				//query -- payment.select.getRetLists1
				tempList = filterGetReversaltLists("getRetLists1", list, new String[] {req.getBranchCode()});
				
            	if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
            		//query -- payment.select.getRetLists2;
            		tempList = filterGetReversaltLists("getRetLists2", list, new String[] {req.getBranchCode(), req.getPaymentNoSearch()});
            	}
            	
            	if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
            		//query -- payment.select.getRetLists3;
            		tempList = filterGetReversaltLists("getRetLists3", list, new String[] {req.getBranchCode(), req.getBrokerNameSearch().toUpperCase()});
            	}
            	
            	if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
            		//query -- payment.select.getRetLists4;
            		tempList = filterGetReversaltLists("getRetLists4", list, new String[] {req.getBranchCode(), req.getCompanyNameSearch().toUpperCase()});
            	}
            	
            	if(StringUtils.isNotBlank(req.getRemarksSearch())){
            		//query -- payment.select.getRetLists5;
            		tempList = filterGetReversaltLists("getRetLists5", list, new String[] {req.getBranchCode(), req.getRemarksSearch().toUpperCase()});
            	}
            	
			}
			
			for(int i = 0; i < tempList.size(); i++ ) {
				ReciptListRes tempBean=new ReciptListRes();
				Tuple resMap = tempList.get(i);
				tempBean.setPayrecno(resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString());
				tempBean.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				tempBean.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				tempBean.setName(resMap.get("PAYMENT_RESPONSE")==null?"":resMap.get("PAYMENT_RESPONSE").toString());
				tempBean.setPayamount(resMap.get("PAID_AMT")==null?"":fm.formatter(resMap.get("PAID_AMT").toString()));
				tempBean.setBrokerid(resMap.get("BROKER_ID")==null?"":resMap.get("BROKER_ID").toString());
				tempBean.setSerialno(resMap.get("REVERSALTRANSNO")==null?"":resMap.get("REVERSALTRANSNO").toString());
				tempBean.setRemarks(resMap.get("REMARKS")==null?"":resMap.get("REMARKS").toString());
				tempBean.setTrdate(resMap.get("TRANS_DATE")==null?"":new SimpleDateFormat("dd/MM/yyyy").format(resMap.get("TRANS_DATE")).toString()); 
				tempBean.setTransactionType(resMap.get("TRANSCATIONTYPE")==null?"":resMap.get("TRANSCATIONTYPE").toString());
				
				//query -- payment.select.getTotCount
				List<Long> allocount = treasuryCustomRepository.getTotCount(resMap.get("PAYMENT_RECEIPT_NO").toString(), "Y");
				if(!CollectionUtils.isEmpty(allocount)) {
					count=allocount.get(0) ==null?0:Integer.parseInt(allocount.get(0).toString()) ;
				}
				
				if(count!=0) {
					tempBean.setEditShowStatus("No");
				}
				else {
					tempBean.setEditShowStatus("Yes");                	  
				}
				
				List<Long> counting = treasuryCustomRepository.getTotCount(resMap.get("PAYMENT_RECEIPT_NO").toString(), "R");
				if(!CollectionUtils.isEmpty(counting)) {
					count=counting.get(0) ==null?0:Integer.parseInt(counting.get(0).toString()) ;
				}
				
				if(count!=0)
				{
					tempBean.setReversedShowStatus("Yes");
					
				}else 
				{
					tempBean.setReversedShowStatus("No");
					
				}
				 if(StringUtils.isNotBlank(openres.getCommonResponse().get(0).getOpstartDate())&& StringUtils.isNotBlank(openres.getCommonResponse().get(0).getOpendDate()) && StringUtils.isNotBlank(tempBean.getTrdate())){
						if(dropimpl.Validatethree(req.getBranchCode(), tempBean.getTrdate())==0){
							tempBean.setRecpayOpenYN("Yes");
						}
					}
				  
				 //query -- payment_amount_details
				 double Total=0.00;
				 List<Tuple> curList= treasuryCustomRepository.paymentAmountDetails(req.getBranchCode(),
							tempBean.getPayrecno()); 
					if(!CollectionUtils.isEmpty(curList)) {
						Total=curList.get(0).get("AMOUNT")==null?0.00: Double.parseDouble(curList.get(0).get("AMOUNT").toString());
					}
					if(Total!=0) {
						tempBean.setAllocationStatus("Partial");
						tempBean.setType("PA");						}
					else if(Total==0) {
						tempBean.setAllocationStatus("Full");
						tempBean.setType("FA");							
					}
				
				finalList.add(tempBean);
			}
			response.setCommonResponse(finalList);
		} 
		catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
	private List<Tuple> filterGetReversaltLists (String condition, List<Tuple> input, String...value){
		switch(condition) {
		case "getRetLists1":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]))   // filtering branch code  
            .collect(Collectors.toList());
			
		case "getRetLists2":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
            		tuple.get("PAYMENT_RECEIPT_NO").toString().contains(value[1]))   // filtering Payment Receipt No  
            .collect(Collectors.toList());
			
		case "getRetLists3":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
            		tuple.get("BROKER").toString().toUpperCase().contains(value[1]))   // filtering Broker  
            .collect(Collectors.toList());
			
		case "getRetLists4":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
            		tuple.get("COMPANY_NAME").toString().toUpperCase().contains(value[1]))   // filtering Company Name  
            .collect(Collectors.toList());
			
		case "getRetLists5":
			return input.stream()  
	        .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
	        		tuple.get("REMARKS").toString().toUpperCase().contains(value[1]))   // filtering Remarks  
	        .collect(Collectors.toList());
		}
		return input;
	}
	//getReciptList(ReciptListReq req) -- ENDS
	
	
	//getReceiptAllocate(GetReceiptAllocateReq req) -- STARTS
	public GetReceiptAllocateRes1 getReceiptAllocate(GetReceiptAllocateReq req) {
		final List<GetReceiptAllocateRes> finalList=new ArrayList<GetReceiptAllocateRes>();
		GetReceiptAllocateRes1 response = new GetReceiptAllocateRes1();
		String[] args=null;
		List<Tuple> dataList = new ArrayList<>();
		List<Tuple> tempList = null;
		try{
			//Query -- payment.update.rskPremChkyn
			treasuryCustomRepository.updateRskPremChkyn();
			
			//Query -- payment.update.claimPymtChkyn
			treasuryCustomRepository.updateClaimPymtChkyn();
			
 			//query -- "payment.select.getRetAlloDtls";
			dataList = treasuryCustomRepository.getRetAlloDtls(req);
			
			
			Collections.sort(dataList, (e1, e2) -> {
				if (e2.get("PAYMENT_RECEIPT_NO") == null) {
					return (e1.get("PAYMENT_RECEIPT_NO") == null) ? 0 : -1;
				}
				if (e1.get("PAYMENT_RECEIPT_NO") == null) {
					return 1;
				}
				return ((Long) e2.get("PAYMENT_RECEIPT_NO")).compareTo((Long) e1.get("PAYMENT_RECEIPT_NO"));
			});
			tempList = new ArrayList<Tuple>(dataList);
			
			if(StringUtils.isNotBlank(req.getSearchType())){
				//query = "payment.select.getRetAlloDtls1";
				tempList = filterReceiptAllocate("getRetAlloDtls1", dataList, new String[] {req.getBranchCode()});
				
            	if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
            		//query = "payment.select.getRetAlloDtls2";
            		tempList = filterReceiptAllocate("getRetAlloDtls2", dataList, new String[] {req.getBranchCode(), req.getPaymentNoSearch()});
            	}
            	if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
            		//query = "payment.select.getRetAlloDtls3";
            		tempList = filterReceiptAllocate("getRetAlloDtls3", dataList, new String[] {req.getBranchCode(), req.getBrokerNameSearch().toUpperCase()});
            	}
            	if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
            		//query = "payment.select.getRetAlloDtls4";
            		tempList = filterReceiptAllocate("getRetAlloDtls4", dataList, new String[] {req.getBranchCode(), req.getCompanyNameSearch().toUpperCase()});
            	}
            	if(StringUtils.isNotBlank(req.getRemarksSearch())){
            		//query = "payment.select.getRetAlloDtls5";
            		tempList = filterReceiptAllocate("getRetAlloDtls5", dataList, new String[] {req.getBranchCode(), req.getRemarksSearch().toUpperCase()});
            	}
            	if(StringUtils.isNotBlank(req.getCurrencySearch())){
            		//query = "payment.select.getRetAlloDtls6";
            		tempList = filterReceiptAllocate("getRetAlloDtls6", dataList, new String[] {req.getBranchCode(), req.getCurrencySearch().toUpperCase()});
            	}
            	
			}
			
			if(tempList.size()>0) {
				for(int i = 0; i < tempList.size(); i++) {
					GetReceiptAllocateRes res = new GetReceiptAllocateRes();
					Tuple resMap = tempList.get(i);
					res.setSerialNo(resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString());
					res.setPaymentamount(resMap.get("PAID_AMT")==null?"":fm.formatter(resMap.get("PAID_AMT").toString()));
					res.setCurrency(resMap.get("CURRENCY_NAME")==null?"":resMap.get("CURRENCY_NAME").toString());
					res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
					res.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
					res.setCedingId(resMap.get("CEDDINGID")==null?"":resMap.get("CEDDINGID").toString());
					res.setBrokerId(resMap.get("BROKERID")==null?"":resMap.get("BROKERID").toString());
					res.setRemarks(resMap.get("REMARKS")==null?"":resMap.get("REMARKS").toString());
					
					args = new String[2];
					args[0] = req.getBranchCode();
					args[1] = resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString();
					
					
					//query = "payment.select.getPymtRetCurrDtls";
					List<Tuple> curList = treasuryCustomRepository.getPymtRetCurrDtls(args[0] , args[1]);
					List<Map<String,Object>> allocateCurrencyList = new ArrayList<Map<String,Object>>();
				
					double Total=0.0;
					for(int c = 0; c < curList.size(); c++) {
						Tuple inResMap = curList.get(c);
						Total=Total+Double.parseDouble(inResMap.get("AMOUNT").toString());
					}for(int c = 0; c < curList.size(); c++) {
						Tuple temp = curList.get(c);
						Map<String,Object> inResMap = new HashMap<>();
						if("PA".equalsIgnoreCase(req.getType())&& Total!=0) {
							inResMap.put("CURRENCY_ID",temp.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							inResMap.put("CURRENCY_NAME",temp.get("CURRENCY_NAME").toString());
							allocateCurrencyList.add(inResMap);
						}
						else if("FA".equalsIgnoreCase(req.getType())&&Total==0) {
							inResMap.put("CURRENCY_ID",temp.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							inResMap.put("CURRENCY_NAME",temp.get("CURRENCY_NAME").toString());
							allocateCurrencyList.add(inResMap);
						}
						else if("PA".equalsIgnoreCase(req.getAllocateType())&& Total!=0) {
							inResMap.put("CURRENCY_ID",temp.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							inResMap.put("CURRENCY_NAME",temp.get("CURRENCY_NAME").toString());
							allocateCurrencyList.add(inResMap);
						}
						else if("FA".equalsIgnoreCase(req.getAllocateType())&& Total==0) {
							inResMap.put("CURRENCY_ID",temp.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							inResMap.put("CURRENCY_NAME",temp.get("CURRENCY_NAME").toString());
							allocateCurrencyList.add(inResMap);
						}
					}
					res.setAllocateCurrencyList(allocateCurrencyList);
					log.info("Result=>"+res.getAllocateCurrencyList());
					if(allocateCurrencyList.size()>0) {
						finalList.add(res);
					}
				}
			}
		response.setCommonResponse(finalList);
		response.setMessage("Success");
		response.setIsError(false);
	}catch(Exception e){
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}
	
	
	private List<Tuple> filterReceiptAllocate (String condition, List<Tuple> input, String...value){
		switch(condition) {
		case "getRetAlloDtls1":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]))   // filtering branch code  
            .collect(Collectors.toList());
			
		case "getRetAlloDtls2":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
            		tuple.get("PAYMENT_RECEIPT_NO").toString().contains(value[1]))   // filtering Payment Receipt No  
            .collect(Collectors.toList());
			
		case "getRetAlloDtls3":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
            		tuple.get("BROKER").toString().toUpperCase().contains(value[1]))   // filtering Broker  
            .collect(Collectors.toList());
			
		case "getRetAlloDtls4":
			return input.stream()  
            .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
            		tuple.get("COMPANY_NAME").toString().toUpperCase().contains(value[1]))   // filtering Company Name  
            .collect(Collectors.toList());
			
		case "getRetAlloDtls5":
			return input.stream()  
	        .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
	        		tuple.get("REMARKS").toString().toUpperCase().contains(value[1]))   // filtering Remarks  
	        .collect(Collectors.toList());
		
		case "getRetAlloDtls6":
			return input.stream()  
	        .filter(tuple -> tuple.get("BRANCH_CODE").toString().equals(value[0]) &&
	        		tuple.get("CURRENCY_NAME").toString().toUpperCase().contains(value[1]))   // filtering Remarks  
	        .collect(Collectors.toList());
		}
		return input;
	}
	
	//getReceiptAllocate(GetReceiptAllocateReq req) -- ENDS
	
	//getAllTransContract(GetAllTransContractReq req) -- STARTS
	public GetAllTransContractRes1 getAllTransContract(GetAllTransContractReq req) {
		GetAllTransContractRes1 response = new GetAllTransContractRes1();
		List<GetAllTransContractRes> payList = new ArrayList<GetAllTransContractRes>();
		try {
			List<Tuple> list = treasuryCustomRepository.getTranContDtls(req);
			
			for(int i=0 ; i<list.size(); i++) {
				GetAllTransContractRes tempBean=new GetAllTransContractRes();
				Tuple tempMap = list.get(i);
				tempBean.setContractNo(tempMap.get(1)==null?"":tempMap.get(1).toString());
				tempBean.setMode(tempMap.get(2)==null?"":tempMap.get(2).toString());
				tempBean.setProductName(tempMap.get(3)==null?"":tempMap.get(3).toString());
				tempBean.setTransactionno(tempMap.get(0)==null?"":tempMap.get(0).toString());
				tempBean.setInceptiobdate(tempMap.get(4)==null?"":new SimpleDateFormat("dd/MM/yyyy").format(tempMap.get(4)).toString());
				tempBean.setNetdue(tempMap.get(5)==null?"":tempMap.get(5).toString());
				tempBean.setPayamount(tempMap.get(6)==null?"":tempMap.get(6).toString());
				tempBean.setAccPremium(tempMap.get(7)==null?"":tempMap.get(7).toString());
				tempBean.setAccClaim(tempMap.get(8)==null?"":tempMap.get(8).toString());
				tempBean.setCheckYN(tempMap.get(9)==null?"":tempMap.get(9).toString());
				tempBean.setCheckPC(tempMap.get(10)==null?"":tempMap.get(10).toString());
				tempBean.setSubClass(tempMap.get(12)==null?"":tempMap.get(12).toString());
				tempBean.setProposalNo(tempMap.get(13)==null?"":tempMap.get(13).toString());
				payList.add(tempBean);
			}
			response.setCommonResponse(payList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	public GetReversalInfoRes1 getReversalInfo(GetReversalInfoReq req) {
		GetReversalInfoRes1 response = new GetReversalInfoRes1();
		List<GetReversalInfoRes> finalList = new ArrayList<GetReversalInfoRes>();
		String[] args= new String[12];
		List<Map<String,Object>> list;
			try {
				args[0]=("PT".equalsIgnoreCase(req.getTransType())?"RT":"PT");
				args[1]=req.getPaymentAmount().replaceAll(",", "");
				if(!"VIEW".equalsIgnoreCase(req.getFlag()) && StringUtils.isEmpty(req.getCurrencyValue())){
					args[2]=req.getCurrency();
				}
				else{
				args[2]=req.getCurrencyValue();
				}
				args[3]=req.getBranchCode();
				args[4]=req.getBranchCode();
				args[5]=req.getBranchCode();
				args[6]=req.getBranchCode();
				
			if(StringUtils.isNotBlank(req.getPayRecNo()))
			{
				args[7]=req.getPayRecNo();
				args[8]="R";
				list = treasuryCustomRepository.getReversalInfo(args);
				
			}else
			{
				args[7]=req.getBroker();
				args[8]=StringUtils.isBlank(req.getCedingCo() ) ?"0":req.getCedingCo();
				args[9]=StringUtils.isBlank(req.getCedingCo() )?"0":req.getCedingCo();
				args[10]=StringUtils.isBlank(req.getCedingCo() )?"0":req.getCedingCo();
				args[11]="Y";
				list = treasuryCustomRepository.getReversalInfoTreasury(args);
			}
		
			if(list.size()>0) {
			for(int i = 0; i < list.size(); i++) {
				Map<String,Object> resMap = list.get(i);
				final GetReversalInfoRes res = new GetReversalInfoRes();
				res.setPayRecNo(resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString());
				res.setBrokerName(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				res.setBrokerId(resMap.get("BROKER_ID")==null?"":resMap.get("BROKER_ID").toString());
				res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				res.setCurrencyName(resMap.get("CURRENCY_NAME")==null?"":resMap.get("CURRENCY_NAME").toString());
				res.setExrate(resMap.get("EXCHANGE_RATE")==null?"":resMap.get("EXCHANGE_RATE").toString());
				res.setPaymentAmount(resMap.get("PAID_AMT")==null?"":resMap.get("PAID_AMT").toString());
				res.setHideRowCnt(Integer.toString(list.size()));
				
				args = new String[2];
				args[0] = req.getBranchCode();
				args[1] = resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString();
				
				List<Tuple> curList = treasuryCustomRepository.getPymtRetCurrDtls(args[0], args[1]);
				List<Map<String,Object>> allocateCurrencyList = new ArrayList<Map<String,Object>>();
				for(int c = 0; c < curList.size(); c++) {
					Tuple temp = curList.get(c);
					Map<String,Object> inResMap = new HashMap<>();
							
					if(Double.parseDouble(temp.get("AMOUNT").toString())!=0) {
						int count = 0;
						if(StringUtils.isBlank(req.getPayRecNo())){
							count=allocRepo.countByBranchCodeAndReceiptNoAndStatus(args[0], new BigDecimal(args[1]),"Y");
							//count = treasuryCustomRepository.getAllocationStatus(args[0], args[1]);
						}
						if(count<=0){
							inResMap.put("CURRENCY_ID",temp.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							allocateCurrencyList.add(inResMap);
						}
					}
				}
				res.setAllocateCurrencyList(allocateCurrencyList);
				if(allocateCurrencyList.size()>0) {
					finalList.add(res);
				}
			}
		}
			response.setCommonResponse(finalList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	
	public CommonResponse getAllocateTransaction(GetTransContractReq req) {
		CommonResponse response = new CommonResponse();
		log.info("PaymentDAOImpl getallocateTransaction() || Enter");
		try{
			GetAllTransContractReq request=new GetAllTransContractReq();
			request.setAlloccurrencyid(req.getAlloccurrencyId());
			request.setBranchCode(req.getBranchCode());
			request.setBrokerid(req.getBrokerId());
			request.setCedingid(req.getCedingId());
			GetAllTransContractRes1 res= getAllTransContract(request);
			List<GetAllTransContractRes> payList = res.getCommonResponse();
			String serialNo;
			Double a=0.0,b=0.0,c=0.0;
		
			serialNo=queryImpl.getSequenceNo("TreasuryARP","","", req.getBranchCode(),"",req.getAccountDate());
		
			req.setSerialno(serialNo);
			String [] args = null;
		
			for(int i=0;i<payList.size();i++) {
		
			GetAllTransContractRes form= payList.get(i);
		
			List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> form.getTransactionno().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
			if(!CollectionUtils.isEmpty(filterTrack)) {
		
			//if(receivePayAmountMap.containsKey(form.getTransactionNo())) {	 
			 args=new String[17];
			 args[0]=serialNo;	
			 args[1]=form.getContractNo();
			 args[2]=StringUtils.isBlank(form.getMode())?"0":form.getMode();
		
			 args[3]=form.getProductName();
			 args[4]=form.getTransactionno();
			 args[5]=req.getAccountDate();
			if("P".equalsIgnoreCase(form.getCheckPC())){
				args[6]= filterTrack.get(0).getReceivePayAmounts();
			 	args[7]="P";
			 	String[] updateArgs = new String[5];
		
			 	updateArgs[0] = filterTrack.get(0).getReceivePayAmounts();
			 	updateArgs[1] = req.getLoginid();
			 	updateArgs[2] = req.getBranchCode();
			 	updateArgs[3] = form.getContractNo();
			 	updateArgs[4] = form.getTransactionno();
			 	treasuryCustomRepository.updatePremiumDetails(updateArgs);
			
			 	
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getLoginid();
				updateArgs[2] = req.getBranchCode();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionno();
				treasuryCustomRepository.updatepreSetStatus(updateArgs);
			
			 	a=a+Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
			}
			else if("C".equalsIgnoreCase(form.getCheckPC())) {
				args[6] = filterTrack.get(0).getReceivePayAmounts();
				args[7]="C";
				
				String[] updateArgs = new String[5];
				updateArgs[0] = filterTrack.get(0).getReceivePayAmounts();
				updateArgs[1] = req.getBranchCode();
				updateArgs[2] = req.getLoginid();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionno();
			
				treasuryCustomRepository.updateclaimPymtAlloDtls(updateArgs);
			
				updateArgs = new String[5];
				updateArgs[0] = "Allocated";
				updateArgs[1] = req.getBranchCode();
				updateArgs[2] = req.getLoginid();
				updateArgs[3] = form.getContractNo();
				updateArgs[4] = form.getTransactionno();
				treasuryCustomRepository.updateclaimSetStatus(updateArgs);
			
				b = b + Double.parseDouble(filterTrack.get(0).getReceivePayAmounts());
			}
			args[8]="Y";
			args[9]="0";
			args[10]=req.getPolicyno();//Receipt No
			req.setPayrecno(req.getPolicyno());
			args[11]=req.getAlloccurrencyId();//Currency ID
			args[12]=StringUtils.isBlank(req.getHideprocessType())?"I":"O";
			args[13]=form.getSubClass();
			args[14]=req.getLoginid();
			args[15]=req.getBranchCode();
			args[16]=form.getProposalNo();
			TtrnAllocatedTransaction ttrnAllocatedTransaction = ttrnAllocatedTransactionMapper.toEntity(args);
			ttrnAllocatedTransactionRepository.save(ttrnAllocatedTransaction);
			}
			}
		
			if("RT".equalsIgnoreCase(req.getTransType()))
			{
			c=a-b;
			//c=a+b;
			}
			else if("PT".equalsIgnoreCase(req.getTransType()))
			{
			c=b-a;
			//c=b+a;
			}
			String[] updateArgs = new String[5];
			updateArgs[0] = String.valueOf(c);
			updateArgs[1] = req.getLoginid();
			updateArgs[2] = req.getBranchCode();
			updateArgs[3] = req.getPolicyno();
			updateArgs[4] = req.getAlloccurrencyId();
			//queryImpl.updateQuery("payment.update.AlloTranDtls", updateArgs);
			treasuryCustomRepository.updateAlloTranDtls(updateArgs);
		
			//queryImpl.updateQuery("payment.update.rskPremChkyn");
			treasuryCustomRepository.updateRskPremChkyn();
			
			response.setMessage(serialNo);
			response.setIsError(false);
			}
			catch(Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		
			}
			return response;
		}
	
}
