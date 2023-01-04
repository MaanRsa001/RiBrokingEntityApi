package com.maan.insurance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
import com.maan.insurance.model.res.TransContractRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.service.TreasuryService;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;

@Service
public class TreasuryServiceImpl implements TreasuryService {
	private Logger log = LogManager.getLogger(TreasuryServiceImpl.class);
	
	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private Formatters fm;
	@Autowired
	private DropDownServiceImple dropimpl;
	
	@Override
	public CommonResponse savepaymentReciept(PaymentRecieptReq req) {
		CommonResponse res = new CommonResponse();
		try {
			String[] args = null;
			args = new String[20];
			if (StringUtils.isBlank(req.getSerialno())) {
				String refno = queryImpl.getSequenceNo(
						"PT".equalsIgnoreCase(req.getTransType()) ? "TreasuryrPR" : "TreasuryrRP", "", "",
						req.getBranchCode(), "", req.getTransactionDate());
				req.setSerialno(refno);
			}
			args[0] = req.getSerialno();
			args[1] = req.getBroker();
			args[2] = req.getCedingCompany() == null ? "0" : req.getCedingCompany().trim();
			args[3] = req.getCurrency();
			args[4] = req.getPaymentAmount();
			args[5] = req.getExchangeRate();// req.getName();
			args[6] = req.getReceiptBankId();
			args[7] = req.getProductId();
			args[8] = req.getDepartmentNo();
			args[9] = "Y";
			args[10] = req.getTransactionDate();
			args[11] = req.getTransType();
			args[12] = req.getTransactionType();
			args[13] = req.getBranchCode();
			args[14] = req.getBankCharges().replaceAll(",", "");
			args[15] = req.getNetAmount().replaceAll(",", "");
			args[16] = req.getRemarks();
			args[17] = req.getWithHoldingTaxOC().replaceAll(",", "");
			args[18] = req.getLoginId();
			args[19] = req.getPremiumLavy();

			queryImpl.updateQuery("PAYMENT_INSERT_RECEIPT", args);
			generationInsert(req);
			res.setMessage("Success");
			res.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			res.setMessage("Failed");
			res.setIsError(true);
		}
		return res;
	}
	public void generationInsert(PaymentRecieptReq req) {
		try{
			if(!"Treasury".equals(req.getTransactionType())){
				if(req.getGenerationReq()!=null) {
				for(int i=0;i<req.getGenerationReq().size();i++) {
					GenerationReq request=req.getGenerationReq().get(i);
					String currencyId=StringUtils.isEmpty(request.getCurrencyValList()) ? "0" :request.getCurrencyValList();
					String recNo=StringUtils.isEmpty(request.getRecNoValList()) ? "" :request.getRecNoValList();
					if(!"0".equalsIgnoreCase(currencyId)){
					if(recNo.length()>0) {
						String[] obj=new String[14];
						obj[0]=currencyId;
						obj[1]=StringUtils.isEmpty(request.getPayamountValList()) ? "0" :request.getPayamountValList().trim().replaceAll(",", "");
						obj[2]=StringUtils.isEmpty(request.getExachangeValList()) ? "0" :request.getExachangeValList().replaceAll(",", "");
						obj[3]=req.getTransactionDate(); 
						obj[4]="Y";
						obj[5]=StringUtils.isEmpty(request.getRowamountValList()) ? "0" :request.getRowamountValList().replaceAll(",", "");
						obj[6]=StringUtils.isEmpty(request.getSetExcRateValList()) ? "0" :request.getSetExcRateValList().replaceAll(",", "");
						obj[7]=StringUtils.isEmpty(request.getConRecCurValList()) ? "0" :request.getConRecCurValList().replaceAll(",", "");
						obj[8]=recNo;
						obj[9]=req.getSerialno();
						obj[10]=recNo;
						obj[11]=req.getSerialno();
						obj[12]=req.getLoginId();
						obj[13]=req.getBranchCode();
						queryImpl.updateQuery("payment.update.payRetDtls", obj);
						
					}
					else {
						String[] obj=new String[13];
						obj[0]=maxVal();
						obj[1]=req.getSerialno();  
						obj[2]=currencyId;
						obj[3]=StringUtils.isEmpty(request.getPayamountValList()) ? "0" :request.getPayamountValList().trim().replaceAll(",", "");
						obj[4]=StringUtils.isEmpty(request.getExachangeValList()) ? "0" :request.getExachangeValList().replaceAll(",", "");
						obj[5]=req.getTransactionDate();
						obj[6]="Y";
						obj[7]=StringUtils.isEmpty(request.getRowamountValList()) ? "0" :request.getRowamountValList().replaceAll(",", "");
						obj[8]=StringUtils.isEmpty(request.getSetExcRateValList()) ? "0":request.getSetExcRateValList().replaceAll(",", "");
						obj[9]=StringUtils.isEmpty(request.getConRecCurValList()) ? "0" :request.getConRecCurValList().replaceAll(",", "");
						obj[10]=req.getSerialno();  
						obj[11]=req.getLoginId();
						obj[12]=req.getBranchCode();
						queryImpl.updateQuery("payment.insert.payRetDtls", obj);
						
					}
				}
				}
				}
				double diffAmt=Double.parseDouble(StringUtils.isEmpty(req.getTxtDiffPer()) ? "0" :req.getTxtDiffPer().replaceAll(",", ""));
				double diffAmt1=Double.parseDouble(StringUtils.isEmpty(req.getConvertedDiffAmount()) ? "0" :req.getConvertedDiffAmount().replaceAll(",", ""));
				
				String[] obj = new String[6];
				obj[0] = String.valueOf(diffAmt);
				obj[1] = String.valueOf(diffAmt1);
				obj[2] = req.getLoginId();
				obj[3] = req.getBranchCode();
				obj[4] = req.getSerialno();
				obj[5] = req.getBranchCode();
				queryImpl.updateQuery("payment.update.diffAmt", obj);
					
			
			}
				else
			{
				String[] obj=new String[8];
				obj[0]="R";
				obj[1]="RT";
				obj[2]=req.getRevPayReceiptNo();
				obj[3]=req.getLoginId();
				obj[4]=req.getLoginId();
				obj[5]=req.getBranchCode();
				obj[6]=req.getSerialno();
				obj[7]=req.getBranchCode();
				queryImpl.updateQuery("payment.update.reversalPayment", obj);

				
				obj[0]="R";
				obj[1]="PT";
				obj[2]=req.getSerialno();
				obj[3]=req.getLoginId();
				obj[4]=req.getLoginId();
				obj[5]=req.getBranchCode();
				obj[6]=req.getRevPayReceiptNo();
				obj[7]=req.getBranchCode();
				
				queryImpl.updateQuery("payment.update.reversalPayment", obj);
				
				String[] obj1=new String[3];
				obj[0]=req.getLoginId();
				obj[1]=req.getBranchCode();
				obj[2]=req.getSerialno();
				queryImpl.updateQuery("payment.update.reversalPaymentDetails", obj1);
				
				obj[0]=req.getLoginId();
				obj[1]=req.getBranchCode();
				obj[2]=req.getRevPayReceiptNo();
				queryImpl.updateQuery("payment.update.reversalPaymentDetails", obj1);
	
			}
		}
		catch(Exception exception) {
			log.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
		}
	}
	private String maxVal() {
		String result="";
		List<Map<String, Object>> resList = new ArrayList<>();
		resList = queryImpl.selectList("payment.select.getNextRetDtlsNo",new String[] {});
		if(resList!=null) {
			result=resList.get(0).get("RECEIPT")==null?"":resList.get(0).get("RECEIPT").toString();
		}
		return result;
	}
	@Override
	public ReverseInsertRes savereverseInsert(ReverseInsertReq req) {
		String[] args = null;
		ReverseInsertRes response = new ReverseInsertRes();
		List<ReverseRes> reverseRes = new ArrayList<ReverseRes>();
		ReverseRes res1 = new ReverseRes();
		String currency = "";
		Double a = 0.0, b = 0.0, c = 0.0;
		try {
			List<Map<String, Object>> resList = new ArrayList<>();
			
			if (StringUtils.isBlank(req.getSerialNo())) {
				resList = queryImpl.selectList("payment.select.getAlloTransDtls1",
						new String[] { req.getPayRecNo() });
			} else {
				resList = queryImpl.selectList("payment.select.getAlloTransDtls",
						new String[] {req.getPayRecNo(), req.getSerialNo()});
			}

			String curencyId = "";
			if (resList.size() > 0) {
				for (int i = 0; i < resList.size(); i++) {
					Map<String, Object> resMap = resList.get(i);
					curencyId = resMap.get("CURRENCY_ID") == null ? "" : resMap.get("CURRENCY_ID").toString();
					if ("R".equalsIgnoreCase(resMap.get("STATUS") == null ? "" : resMap.get("STATUS").toString())) {

						res1.setSerialNo(resMap.get("SNO") == null ? "" : resMap.get("SNO").toString());
						res1.setAllocateddate(
								resMap.get("INCEPTION_DATE") == null ? "" : resMap.get("INCEPTION_DATE").toString());
						String currencyId = resMap.get("CURRENCY_ID") == null ? ""
								: resMap.get("CURRENCY_ID").toString();
						
						args = new String[2];
						args[0] = req.getBranchCode();
						args[1] = resMap.get("CURRENCY_ID")==null?"":resMap.get("CURRENCY_ID").toString();

						List<Map<String, Object>> curList = queryImpl.selectList("payment.select.getSelCurrency",
								args);
						if (!CollectionUtils.isEmpty(curList)) {
							currency = curList.get(0).get("CURRENCY_NAME") == null ? ""
									: curList.get(0).get("CURRENCY_NAME").toString();
						}
						res1.setCurrency(currency);

						res1.setTransactionNo(
								resMap.get("TRANSACTION_NO") == null ? "" : resMap.get("TRANSACTION_NO").toString());
						res1.setContractNo(
								resMap.get("CONTRACT_NO") == null ? "" : resMap.get("CONTRACT_NO").toString());
						res1.setProductName(
								resMap.get("PRODUCT_NAME") == null ? "" : resMap.get("PRODUCT_NAME").toString());
						res1.setType(resMap.get("TYPE") == null ? "" : resMap.get("TYPE").toString());
						args = new String[8];
						args[0] = "R";
						args[1] = req.getReversalDate();
						args[2] = "RE".equalsIgnoreCase(resMap.get("TYPE")==null?"":resMap.get("TYPE").toString())?resMap.get("RETRO_PAID_AMOUNT")==null?"":resMap.get("RETRO_PAID_AMOUNT").toString():resMap.get("PAID_AMOUNT")==null?"":resMap.get("PAID_AMOUNT").toString();
						args[3] = "0";
						args[4] = req.getLoginId();
						args[5] = req.getBranchCode();
						args[6] = resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString();
						args[7] = resMap.get("SNO")==null?"":resMap.get("SNO").toString();
						
						
						queryImpl.updateQuery("payment.update.allocatedDtls", args);

						if ("P".equalsIgnoreCase(resMap.get("TYPE") == null ? "" : resMap.get("TYPE").toString())) {
							args = new String[5];
							args[0] = resMap.get("PAID_AMOUNT")==null?"":resMap.get("PAID_AMOUNT").toString();
							args[1] = req.getLoginId();
							args[2] = req.getBranchCode();
							args[3] = resMap.get("CONTRACT_NO")==null?"":resMap.get("CONTRACT_NO").toString();
							args[4] = resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString();
							queryImpl.updateQuery("payment.update.rskPremDtls1", args);

							args = new String[2];
							args[0] = resMap.get("CONTRACT_NO")==null?"":resMap.get("CONTRACT_NO").toString();
							args[1] = resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString();
							List<Map<String, Object>> rskList = queryImpl.selectList("payment.select.getRskPremDtls",
									args);

							if (!CollectionUtils.isEmpty(rskList)) {
								res1.setNetDue((rskList.get(0).get("NETDUE") == null ? ""
										: rskList.get(0).get("NETDUE").toString()));
							}

							a = a + Double.parseDouble(
									resMap.get("PAID_AMOUNT") == null ? "" : resMap.get("PAID_AMOUNT").toString());

						} else if ("C"
								.equalsIgnoreCase(resMap.get("TYPE") == null ? "" : resMap.get("TYPE").toString())) {
							args = new String[6];
							args[0] = "Pending";
							args[1] = resMap.get("PAID_AMOUNT")==null?"":resMap.get("PAID_AMOUNT").toString();
							args[2] = req.getBranchCode();
							args[3] = req.getLoginId();
							args[4] = resMap.get("CONTRACT_NO")==null?"":resMap.get("CONTRACT_NO").toString();
							args[5] = resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString();
							queryImpl.updateQuery("payment.update.claimPymtDtls", args);

							args = new String[2];
							args[0] = resMap.get("CONTRACT_NO")==null?"":resMap.get("CONTRACT_NO").toString();
							args[1] = resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString();
							List<Map<String, Object>> claim = queryImpl.selectList("payment.select.getClaimPymtDtls",
									args);
							if (!CollectionUtils.isEmpty(claim)) {
								res1.setPayAmount((claim.get(0).get("PAID_AMOUNT") == null ? ""
										: claim.get(0).get("PAID_AMOUNT").toString()));
							}

							b = b + Double.parseDouble(
									resMap.get("PAID_AMOUNT") == null ? "" : resMap.get("PAID_AMOUNT").toString());

						} else if ("RE"
								.equalsIgnoreCase(resMap.get("TYPE") == null ? "" : resMap.get("TYPE").toString())) {

							args = new String[3];
							args[0] = resMap.get("RETRO_PAID_AMOUNT")==null?"":resMap.get("RETRO_PAID_AMOUNT").toString();
							args[1] = resMap.get("CONTRACT_NO")==null?"":resMap.get("CONTRACT_NO").toString();
							args[2] = resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString();
							//pending
							queryImpl.updateQuery("payment.update.retro.payment", args);

							args = new String[2];
							args[0] = resMap.get("CONTRACT_NO")==null?"":resMap.get("CONTRACT_NO").toString();
							args[1] = resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString();
							List<Map<String, Object>> retroSoa = queryImpl.selectList("payment.get.retro.payment",
									args);
							if (!CollectionUtils.isEmpty(retroSoa)) {
								res1.setNetDue((retroSoa.get(0).get("NETDUE") == null ? ""
										: retroSoa.get(0).get("NETDUE").toString()));
							}

							double netresultsign = Math.signum(Double.valueOf(Double.parseDouble(res1.getNetDue())));
							res1.setCheckPc("1.0".equals(String.valueOf(netresultsign)) ? "P" : "C");

							a = a + (req.getTransType().equals("PT")
									? (-1) * Double.parseDouble(resMap.get("RETRO_PAID_AMOUNT") == null ? ""
											: resMap.get("RETRO_PAID_AMOUNT").toString())
									: Double.parseDouble(resMap.get("RETRO_PAID_AMOUNT") == null ? ""
											: resMap.get("RETRO_PAID_AMOUNT").toString()));
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
				queryImpl.updateQuery("payment.update.pymtRetDtls", args);

				args = new String[2];
				args[0] = req.getPayRecNo();
				args[1] = curencyId;
				List<Map<String, Object>> paymentRet = queryImpl.selectList("payment.select.getPymtRetDtls", args);
				if (!CollectionUtils.isEmpty(paymentRet)) {
					res1.setPayAmount((paymentRet.get(0).get("AMOUNT") == null ? ""
							: paymentRet.get(0).get("AMOUNT").toString()));
				}

				reverseRes.add(res1);
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

	// For Validation
	public int Validatethree(String branchCode, String accountDate) {
		int result = 0;
		String[] args = null;
		try {
			String OpstartDate = "";
			String OpendDate = "";
			args = new String[1];
			args[0] = branchCode;
			List<Map<String, Object>> list = queryImpl.selectList("GET_OPEN_PERIOD_DATE", args);

			if (!CollectionUtils.isEmpty(list)) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> tempMap = (Map<String, Object>) list.get(i);
					OpstartDate = tempMap.get("START_DATE") == null ? "" : tempMap.get("START_DATE").toString();
					OpendDate = tempMap.get("END_DATE") == null ? "" : tempMap.get("END_DATE").toString();

					args = new String[3];
					args[0] = accountDate;
					args[1] = OpstartDate;
					args[2] = OpendDate;
					List<Map<String, Object>> count = queryImpl.selectList("GET_OPEN_PERIOD_VALIDATE", args);
					if (!CollectionUtils.isEmpty(count)) {
						result = count.get(0).get("COUNT") == null ? 0
								: Integer.parseInt(count.get(0).get("COUNT").toString());
					}

					if (result > 0)
						break;
				}
			}

		} catch (Exception e) {
			log.debug("Exception @ {" + e + "}");
		}
		return result;
	}

	@Override
	public AllocatedStatusRes1 getAllocatedStatus(AllocatedStatusReq req) {
		AllocatedStatusRes1 response = new AllocatedStatusRes1();
		List<AllocatedStatusRes> finalList = new ArrayList<AllocatedStatusRes>();
		AllocatedStatusRes res = new AllocatedStatusRes();
		AllocatedStatusResponse res1 = new AllocatedStatusResponse();
		String[] args = null;
		try {
			//List<Map<String, Object>> list = queryImpl.selectList("payment.select.getPymtRetStatus", args);
			List<Map<String, Object>> list;
			if (StringUtils.isBlank(req.getAlloccurrencyId())) {
				args = new String[2];
				args[0] = req.getBranchCode();
				args[1] = req.getPayRecNo();
				list = queryImpl.selectList("payment.select.getPymtRetStatus", args);
			} else {
				args = new String[3];
				args[0] = req.getBranchCode();
				args[1] = req.getPayRecNo();
				args[2] = req.getAlloccurrencyId();
				list = queryImpl.selectList("payment.select.getPymtRetStatus1", args);
			}

			for (int i = 0; i < list.size(); i++) {

				Map<String, Object> resMap = list.get(i);
				res.setCurrency(resMap.get("CURRENCY_NAME") == null ? "" : resMap.get("CURRENCY_NAME").toString());
				res.setAllocated(
						resMap.get("ALLOCATED") == null ? "" : fm.formatter(resMap.get("ALLOCATED").toString()));
				res.setUtilized(resMap.get("UTILIZED") == null ? "" : fm.formatter(resMap.get("UTILIZED").toString()));
				res.setNotUtilized(
						resMap.get("NOTUTILIZED") == null ? "" : fm.formatter(resMap.get("NOTUTILIZED").toString()));
				res.setStatus(resMap.get("STATUS") == null ? "" : resMap.get("STATUS").toString());
				res.setPaymentDate(resMap.get("PAYMANT_DATE") == null ? "" : resMap.get("PAYMANT_DATE").toString());
				res.setBank(resMap.get("BANK_NAME") == null ? "" : resMap.get("BANK_NAME").toString());
				res.setCedingCompanyName(
						resMap.get("CEDING_COMPANY") == null ? "" : resMap.get("CEDING_COMPANY").toString());
				finalList.add(res);
			}
			if ("".equals(req.getBrokerName())) {

				args = new String[3];
				args[0] = req.getBranchCode();
				args[1] = req.getBrokerId();
				args[2] = "B";
				List<Map<String, Object>> company = queryImpl.selectList("common.select.getCompName", args);

				if (!CollectionUtils.isEmpty(company)) {
					res1.setBrokerName(
							(company.get(0).get("COMPNAY") == null ? "" : company.get(0).get("COMPNAY").toString()));
				}
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

	@Override
	public AllocateViewCommonRes1 allocateView(AllocateViewReq req) {
		log.info("PaymentDAOImpl allocateView() || Enter");
		AllocateViewCommonRes1 res1 = new AllocateViewCommonRes1();
		AllocateViewCommonRes response = new AllocateViewCommonRes();
		AllocateViewRes res = new AllocateViewRes();
		String[] args = null;
		final List<AllocateViewRes> alllist = new ArrayList<AllocateViewRes>();
		final List<AllocateViewRes> allocatedList = new ArrayList<AllocateViewRes>();
		final List<AllocateViewRes> revertedList = new ArrayList<AllocateViewRes>();
		try {
			args = new String[1];
			if (!"".equals(req.getPayRecNo())) {
				args[0] = req.getPayRecNo();
			} else {
				args[0] = req.getSerialNo();
			}
			List<Map<String, Object>> allocateView = new ArrayList<>();

			if (!"".equals(req.getSerialNo()) && !"VIEW".equalsIgnoreCase(req.getFlag())) {
				allocateView = queryImpl.selectList("payment.select.getAlloTransDtls", new String[] {req.getPayRecNo(), req.getSerialNo()});
			}
			allocateView = queryImpl.selectList("payment.select.getAlloTransDtls1", new String[] {req.getPayRecNo()});

			if (!"".equals(req.getSerialNo()) && !"VIEW".equalsIgnoreCase(req.getFlag())) {
				args = new String[2];
				args[0] = req.getPayRecNo();
				args[1] = req.getBranchCode();
				allocateView = queryImpl.selectList("payment.select.getBroCedingIds", args);

				if (allocateView.size() > 0) {

					args = new String[3];
					args[0] = req.getBranchCode();
					args[1] = allocateView.get(0).get("BROKER_ID") == null ? ""
							: allocateView.get(0).get("BROKER_ID").toString();
					args[2] = "B";
					allocateView = queryImpl.selectList("common.select.getCompName", args);

					if ("63".equals(allocateView.get(0).get("BROKER_ID") == null ? ""
							: allocateView.get(0).get("BROKER_ID").toString())) {
						args = new String[3];
						args[0] = req.getBranchCode();
						args[1] = allocateView.get(0).get("CEDING_ID") == null ? ""
								: allocateView.get(0).get("CEDING_ID").toString();
						args[2] = "C";
						allocateView = queryImpl.selectList("common.select.getCompName", args);
						if (!CollectionUtils.isEmpty(allocateView)) {
							res.setCedingCo((allocateView.get(0).get("COMPNAY") == null ? ""
									: allocateView.get(0).get("COMPNAY").toString()));
						}
						
						
					}
				}
				args = new String[2];
				if (!"".equals(req.getPayRecNo())) {
					args[0] = req.getPayRecNo();
				} else {
					args[0] = req.getSerialNo();
				}
				args[1] = req.getAlloccurrencyId();

				allocateView = queryImpl.selectList("payment.select.getPymtRetDtls", args);

				if (!CollectionUtils.isEmpty(allocateView)) {
					res.setPayAmount((allocateView.get(0).get("AMOUNT") == null ? ""
							: allocateView.get(0).get("AMOUNT").toString()));
				}

			} 

			if (allocateView.size() > 0) {

				for (int i = 0; i < allocateView.size(); i++) {

					Map<String, Object> resMap = allocateView.get(i);
					res.setSerialNo(resMap.get("SNO") == null ? "" : resMap.get("SNO").toString());
					res.setAllocatedDate(
							resMap.get("INCEPTION_DATE") == null ? "" : resMap.get("INCEPTION_DATE").toString());
					res.setTransactionNo(
							resMap.get("TRANSACTION_NO") == null ? "" : resMap.get("TRANSACTION_NO").toString());
					res.setContractNo(resMap.get("CONTRACT_NO") == null ? "" : resMap.get("CONTRACT_NO").toString());
					res.setProductName(resMap.get("PRODUCT_NAME") == null ? "" : resMap.get("PRODUCT_NAME").toString());
					res.setType(resMap.get("TYPE") == null ? "" : resMap.get("TYPE").toString());
					res.setCurrencyValue(resMap.get("CURRENCY_ID") == null ? "" : resMap.get("CURRENCY_ID").toString());
					res.setAlloccurrencyId(resMap.get("CURRENCY_ID")==null?"":resMap.get("CURRENCY_ID").toString());
					res.setStatus(("R".equals(resMap.get("STATUS") == null ? "" : resMap.get("STATUS").toString())
							? "Reverted"
							: "Allocated"));
					if ("Reverted".equalsIgnoreCase(res.getStatus())) {
						res.setPayAmount(
								resMap.get("REVERSAL_AMOUNT") == null ? "" : resMap.get("REVERSAL_AMOUNT").toString());
						res.setAllocatedDate(
								resMap.get("REVERSAL_DATE") == null ? "" : resMap.get("REVERSAL_DATE").toString());
						res.setAllocatedDate(
								resMap.get("REVERSAL_DATE") == null ? "" : resMap.get("REVERSAL_DATE").toString());
						res.setCheckPc((resMap.get("REVERSAL_AMOUNT_SIGN") == null ? ""
								: resMap.get("REVERSAL_AMOUNT_SIGN").toString()));
					} else {
						res.setPayAmount(resMap.get("PAID_AMOUNT") == null ? "" : resMap.get("PAID_AMOUNT").toString());
						res.setAllocatedDate(
								resMap.get("INCEPTION_DATE") == null ? "" : resMap.get("INCEPTION_DATE").toString());
						res.setAllocatedDate(resMap.get("INCEPTION_DATE")==null?"":resMap.get("INCEPTION_DATE").toString());
						res.setCheckPc((resMap.get("PAID_AMOUNT_SIGN") == null ? ""
								: resMap.get("PAID_AMOUNT_SIGN").toString()));
					}
					res.setAdjustmentType(
							(resMap.get("ADJUSTMENT_TYPE") == null ? "" : resMap.get("ADJUSTMENT_TYPE").toString()));

					args = new String[2];
					args[0] = req.getBranchCode();
					args[1] = req.getCurrecncyValue();
					allocateView = queryImpl.selectList("payment.select.getSelCurrency", args);
					if (!CollectionUtils.isEmpty(allocateView)) {
						res.setCurrencyName((allocateView.get(0).get("CURRENCY_NAME") == null ? ""
								: allocateView.get(0).get("CURRENCY_NAME").toString()));
					}

					args = new String[2];
					if (!"".equals(req.getPayRecNo())) {
						args[0] = req.getPayRecNo();
					} else {
						args[0] = req.getSerialNo();
					}
					args[1] = req.getCurrecncyValue();

					allocateView = queryImpl.selectList("payment.select.getExchRate", args);
					if (!CollectionUtils.isEmpty(allocateView)) {
						res.setExchangeRate((allocateView.get(0).get("EXCHANGE_RATE") == null ? ""
								: allocateView.get(0).get("EXCHANGE_RATE").toString()));
					}

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

	@Override
	public GetReceiptEditRes1 getReceiptEdit(String paymentReceiptNo, String branchCode) {
		GetReceiptEditRes1 response = new GetReceiptEditRes1();
		GetReceiptEditRes res = new GetReceiptEditRes();
		String[] args = null;
		try {
			args = new String[2];
			args[0] = paymentReceiptNo;
			args[1] = branchCode;
			List<Map<String, Object>> list = queryImpl.selectList("payment.select.getRetDtls", args);
			if (!CollectionUtils.isEmpty(list)) {
				if (list.size() > 0) {
					Map<String, Object> resMap = list.get(0);
					res.setPayRecNo(resMap.get("PAYMENT_RECEIPT_NO") == null ? ""
							: resMap.get("PAYMENT_RECEIPT_NO").toString());
					res.setCedingCo(resMap.get("CEDING_ID") == null ? "" : resMap.get("CEDING_ID").toString());
					res.setBroker(resMap.get("BROKER_ID") == null ? "" : resMap.get("BROKER_ID").toString());
					res.setReceiptBankId(
							resMap.get("RECEIPT_BANK") == null ? "" : resMap.get("RECEIPT_BANK").toString());
					res.setTrDate(resMap.get("TRANS_DATE") == null ? "" : resMap.get("TRANS_DATE").toString());
					res.setPaymentAmount(resMap.get("PAID_AMT") == null ? "" : resMap.get("PAID_AMT").toString());
					res.setCurrency(resMap.get("CURRENCY_ID") == null ? "" : resMap.get("CURRENCY_ID").toString());
					res.setExrate(resMap.get("EXCHANGE_RATE") == null ? "" : resMap.get("EXCHANGE_RATE").toString());
					res.setTransactionType(
							resMap.get("TRANSCATIONTYPE") == null ? "" : resMap.get("TRANSCATIONTYPE").toString());
					res.setTransactionTypeTest(
							resMap.get("TRANSCATIONTYPE") == null ? "" : resMap.get("TRANSCATIONTYPE").toString());
					res.setBankCharges(resMap.get("BANK_CHARGES") == null ? "" : resMap.get("BANK_CHARGES").toString());
					res.setNetAmt(resMap.get("NET_AMT") == null ? "" : resMap.get("NET_AMT").toString());
					res.setAmendDate(
							resMap.get("AMENDMENT_DATE") == null ? "" : resMap.get("AMENDMENT_DATE").toString());
					res.setRemarks(resMap.get("REMARKS") == null ? "" : resMap.get("REMARKS").toString());
					res.setBankchargeDC(resMap.get("BANKDC") == null ? "" : resMap.get("BANKDC").toString());
					res.setNetAmtDC(resMap.get("NET_AMTDC") == null ? "" : resMap.get("NET_AMTDC").toString());
					res.setWithHoldingTaxOC(resMap.get("WH_TAX") == null ? "" : resMap.get("WH_TAX").toString());
					res.setPremiumLavy(resMap.get("PREMIUM_LAVY") == null ? "" : resMap.get("PREMIUM_LAVY").toString());
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

	@Override
	public GetReceiptGenerationRes1 getReceiptGeneration(GetReceiptGenerationReq req) {
		GetReceiptGenerationRes1 response = new GetReceiptGenerationRes1();
		GetReceiptGenerationRes res = new GetReceiptGenerationRes();
		String[] args = null;
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			if ("63".equals(req.getBroker())) {

				args = new String[5];
				args[0] = req.getBranchCode();
				args[1] = req.getBranchCode();
				args[2] = req.getBranchCode();
				args[3] = req.getSerialNo();
				args[4] = req.getBranchCode();
				list = queryImpl.selectList("payment.select.getDirBroDtls", args);

			} else {
				args = new String[4];
				args[0] = req.getBranchCode();
				args[1] = req.getBranchCode();
				args[2] = req.getSerialNo();
				args[3] = req.getBranchCode();
				list = queryImpl.selectList("payment.select.getBroDtls", args);

			}

			if (list.size() > 0) {
				Map<String, Object> resMap = list.get(0);
				res.setCedingCo(resMap.get("COMPANY_NAME") == null ? "" : resMap.get("COMPANY_NAME").toString());
				res.setBrokerName(resMap.get("BROKER") == null ? "" : resMap.get("BROKER").toString());

				res.setCurrecncy(resMap.get("CURRENCY_NAME") == null ? "" : resMap.get("CURRENCY_NAME").toString());
				res.setPaymentAmount(
						resMap.get("PAID_AMT") == null ? "" : fm.formatter(resMap.get("PAID_AMT").toString()));
				res.setTrDate(resMap.get("TRANSDATE") == null ? "" : resMap.get("TRANSDATE").toString());
				res.setSerialNo(
						resMap.get("PAYMENT_RECEIPT_NO") == null ? "" : resMap.get("PAYMENT_RECEIPT_NO").toString());
				res.setExchangeRate(resMap.get("EXCHANGE_RATE") == null ? "" : resMap.get("EXCHANGE_RATE").toString());

				res.setEntrate(resMap.get("EXCHANGE_RATE") == null ? ""
						: fm.formatter(resMap.get("EXCHANGE_RATE").toString()));
				res.setTotalExchaAmount(
						resMap.get("TOT_EXC_AMT") == null ? "" : fm.formatter(resMap.get("TOT_EXC_AMT").toString()));
				res.setBroker(resMap.get("BROKER_ID") == null ? "" : resMap.get("BROKER_ID").toString());
				res.setDiffAmount(
						resMap.get("DIFF_AMT") == null ? "" : fm.formatter(resMap.get("DIFF_AMT").toString()));
				res.setConvDiffAmount(resMap.get("CONVERTED_DIFF_AMT") == null ? ""
						: fm.formatter(resMap.get("CONVERTED_DIFF_AMT").toString()));
				res.setTransactionType(
						resMap.get("TRANSCATIONTYPE") == null ? "" : resMap.get("TRANSCATIONTYPE").toString());

				res.setCurrecncyValue(resMap.get("CURRENCY_ID") == null ? "" : resMap.get("CURRENCY_ID").toString());
				res.setPayRecNo(resMap.get("REVERSALTRANSNO") == null ? "" : resMap.get("REVERSALTRANSNO").toString());
				res.setBankCharges(
						resMap.get("BANK_CHARGES") == null ? "" : fm.formatter(resMap.get("BANK_CHARGES").toString()));
				res.setNetAmt(resMap.get("NET_AMT") == null ? "" : fm.formatter(resMap.get("NET_AMT").toString()));
				res.setReceiptBankName(resMap.get("BANK_NAME") == null ? "" : resMap.get("BANK_NAME").toString());
				res.setWithHoldingTaxOC(
						resMap.get("WH_TAX") == null ? "" : fm.formatter(resMap.get("WH_TAX").toString()));
				res.setRemarks(resMap.get("REMARKS") == null ? "" : resMap.get("REMARKS").toString());
				res.setPremiumLavy(
						resMap.get("PREMIUM_LAVY") == null ? "" : fm.formatter(resMap.get("PREMIUM_LAVY").toString()));
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

	@Override
	public GetReversalInfoRes1 getReversalInfo(GetReversalInfoReq req) {
		GetReversalInfoRes1 response = new GetReversalInfoRes1();
		List<GetReversalInfoRes> finalList = new ArrayList<GetReversalInfoRes>();
		String[] args=null;
		String query="";
		List<Map<String,Object>> list;
			try {
			//query = "PAYMENT_SELECT_GETREVERSALINFO_TREASURY ";
			if(StringUtils.isNotBlank(req.getPayRecNo()))
			{
				//query = "payment.select.getReversalInfo";
				args = new String[9];
				args[7]=req.getPayRecNo();
				args[8]="R";
				query = "payment.select.getReversalInfo1";
				
			}else
			{
				args = new String[12];
				args[7]=req.getBroker();
				args[8]=req.getCedingCo() == null || org.apache.commons.lang3.StringUtils.isBlank(req.getCedingCo() ) ?"0":req.getCedingCo();
				args[9]=req.getCedingCo() == null || org.apache.commons.lang3.StringUtils.isBlank(req.getCedingCo() )?"0":req.getCedingCo();
				args[10]=req.getCedingCo() == null || org.apache.commons.lang3.StringUtils.isBlank(req.getCedingCo() )?"0":req.getCedingCo();
				args[11]="Y";
				query = "PAYMENT_SELECT_GETREVERSALINFO_TREASURY";
			}
		
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
			
			
			 list = queryImpl.selectList(query, args);
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
				query =  "payment.select.getPymtRetCurrDtls";
				List<Map<String,Object>> curList = queryImpl.selectList(query, args);
				List<Map<String,Object>> allocateCurrencyList = new ArrayList<Map<String,Object>>();
				for(int c = 0; c < curList.size(); c++) {
					Map<String,Object> inResMap = curList.get(c);
					if(Double.parseDouble(inResMap.get("AMOUNT").toString())!=0) {
						int count = 0;
						if(StringUtils.isBlank(req.getPayRecNo()) ){
						
						query = "GET_ALLOCATION_STATUS";
						list = queryImpl.selectList(query, args);
						count= list.size();
						}
						if(count<=0){
						inResMap.put("CURRENCY_ID",inResMap.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
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

	@Override
	public ReverseViewRes1 reverseView(ReverseViewReq req) {
		log.info("PaymentDAOImpl reverseView() || Enter");
		ReverseViewRes1 response = new ReverseViewRes1();
		List<ReverseViewRes> resList = new ArrayList<ReverseViewRes>();
		
		try{
		
			List<Map<String, Object>> list = queryImpl.selectList("payment.select.getAllTranDtls", new String[] {req.getSerialNo()});
			
			for(int i = 0; i < list.size(); i++) {
				Map<String,Object> resMap = (Map<String,Object>) list.get(i);
				final ReverseViewRes tempBean=new ReverseViewRes();
				tempBean.setSerialNo(resMap.get("SNO")==null?"":resMap.get("SNO").toString());
				tempBean.setAllocateddate(resMap.get("ALLOCATION_DATE")==null?"":resMap.get("ALLOCATION_DATE").toString());
				tempBean.setReversalDate(resMap.get("REVERSAL_DATE1")==null?"":resMap.get("REVERSAL_DATE1").toString());
				tempBean.setContractNo(resMap.get("CONTRACT_NO")==null?"":resMap.get("CONTRACT_NO").toString());
				tempBean.setTransactionNo(resMap.get("TRANSACTION_NO")==null?"":resMap.get("TRANSACTION_NO").toString());
				tempBean.setProductName(resMap.get("PRODUCT_NAME")==null?"":resMap.get("PRODUCT_NAME").toString());
				tempBean.setReversedAmount(resMap.get("REVERSAL_AMOUNT")==null?"":fm.formatter(resMap.get("REVERSAL_AMOUNT").toString()));
				tempBean.setType(resMap.get("TYPE")==null?"":resMap.get("TYPE").toString());
				tempBean.setStatus(resMap.get("STATUS")==null?"":resMap.get("STATUS").toString());
				List<Map<String, Object>> currency = queryImpl.selectList("payment.select.getSelCurrency",new String[]{req.getBranchCode(),(resMap.get("CURRENCY_ID")==null?"":resMap.get("CURRENCY_ID").toString())});
				if (!CollectionUtils.isEmpty(currency)) {
					tempBean.setCurrency((currency.get(0).get("CURRENCY_NAME") == null ? ""
							: currency.get(0).get("CURRENCY_NAME").toString()));
				}
				
			
				List<Map<String, Object>>	amount = queryImpl.selectList("payment.select.getAlloAmt", new String[]{req.getSerialNo(),resMap.get("CURRENCY_ID")==null?"":resMap.get("CURRENCY_ID").toString()});
				if (!CollectionUtils.isEmpty(amount)) {
					tempBean.setAllTillDate((amount.get(0).get("AMOUNT") == null ? ""
							: amount.get(0).get("AMOUNT").toString()));
				}
				
				
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

	@Override
	public GetReceiptReversalListRes1 getReceiptReversalList(GetReceiptReversalListReq req) {
		GetReceiptReversalListRes1 response = new GetReceiptReversalListRes1();
		List<GetReceiptReversalListRes> finalList=new ArrayList<GetReceiptReversalListRes>();
		GetReceiptReversalListRes res = new GetReceiptReversalListRes();
		String[] args=null;
		String	query = "";
		try {
			query = "getReversaltLists";
			
			args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTranstype(),req.getBranchCode(),"R",req.getBranchCode(),req.getTranstype(),req.getBranchCode(), "R"};
			
			
			if(StringUtils.isNotBlank(req.getSearchType())){
				args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTranstype(),req.getBranchCode(),"R",req.getBranchCode(),req.getTranstype(),req.getBranchCode(), "R", req.getBranchCode()};
				query = "getReversaltLists1";
				
            	if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTranstype(),req.getBranchCode(),"R",req.getBranchCode(),req.getTranstype(),req.getBranchCode(), "R", req.getBranchCode(), "%"+req.getPaymentNoSearch()+"%"};
            		
            		query = "getReversaltLists2";
            	}
            	
            	if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
            		
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTranstype(),req.getBranchCode(),"R",req.getBranchCode(),req.getTranstype(),req.getBranchCode(), "R", req.getBranchCode(),"%"+ req.getBrokerNameSearch()+"%"};
            		query = "getReversaltLists3";
            	}
            	if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
            		
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTranstype(),req.getBranchCode(),"R",req.getBranchCode(),req.getTranstype(),req.getBranchCode(), "R", req.getBranchCode(), "%"+req.getCompanyNameSearch()+"%"};
            		query = "getReversaltLists4";
            	}
            	if(StringUtils.isNotBlank(req.getRemarksSearch())){
            		
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTranstype(),req.getBranchCode(),"R",req.getBranchCode(),req.getTranstype(),req.getBranchCode(), "R", req.getBranchCode(),"%"+req.getRemarksSearch()+"%"};
            		query = "getReversaltLists5";
            	}
            	
			}
			
			

			int count = 0;
			List<Map<String,Object>> list = queryImpl.selectList(query, args);
			for(int i = 0; i < list.size(); i++ ) {
				
				Map<String,Object> resMap = list.get(i);
				res.setPayRecNo(resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString());
				res.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				res.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				res.setName(resMap.get("PAYMENT_RESPONSE")==null?"":resMap.get("PAYMENT_RESPONSE").toString());
				res.setPayamount(resMap.get("PAID_AMT")==null?"":fm.formatter(resMap.get("PAID_AMT").toString()));
				res.setBrokerId(resMap.get("BROKER_ID")==null?"":resMap.get("BROKER_ID").toString());
				res.setSerialNo(resMap.get("REVERSALTRANSNO")==null?"":resMap.get("REVERSALTRANSNO").toString());
				
				list = queryImpl.selectList("payment.select.getTotCount", new String[]{resMap.get("PAYMENT_RECEIPT_NO").toString(),"Y"});
				//count = this.mytemplate.queryForInt(getQuery(DBConstants.PAYMENT_SELECT_GETTOTCOUNT),new Object[]{resMap.get("PAYMENT_RECEIPT_NO").toString(),"Y"});
				count = list.size();
				if(count>0) {
					res.setEditShowStatus("No");
				}
				else {
					res.setEditShowStatus("Yes");                	  
				}
				list = queryImpl.selectList("payment.select.getTotCount",new String[]{resMap.get("PAYMENT_RECEIPT_NO").toString(),"R"});
				count = list.size();
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

	@Override
	public GetReceiptAllocateRes1 getReceiptAllocate(GetReceiptAllocateReq req) {
		GetReceiptAllocateRes1 response = new GetReceiptAllocateRes1();
		final List<GetReceiptAllocateRes> finalList=new ArrayList<GetReceiptAllocateRes>();
		GetReceiptAllocateRes res = new GetReceiptAllocateRes();
		String query = "";
		String[] args=null;
		try{
			queryImpl.updateQueryWP("payment.update.rskPremChkyn");	
			
			queryImpl.updateQueryWP("payment.update.claimPymtChkyn");
			
			args = new String[] {req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getTransType()};
			
			query = "payment.select.getRetAlloDtls";
		/*	if(req.getPayRecNo() !=null && req.getAllocateType()!=null){
				res.setSearchBy("1");
				res.setSearchValue(req.getPayRecNo());
				
			}*/
			if(StringUtils.isNotBlank(req.getSearchType())){
				query = "payment.select.getRetAlloDtls1";
				args = new String[] {req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getTransType(),req.getBranchCode()};
				
				
            	if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
            		query = "payment.select.getRetAlloDtls2";
            		args = new String[] {req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),"%"+req.getPaymentNoSearch()+"%"};
            		//query +=" AND PAYMENT_RECEIPT_NO like '%"+req.getPaymentNoSearch()+"%'";
            	}
            	if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
            		args = new String[] {req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),"%"+req.getBrokerNameSearch()+"%"};
            		query = "payment.select.getRetAlloDtls3";
            		//query +=" AND UPPER(BROKER) like UPPER('%"+req.getBrokerNameSearch()+"%')";
            	}
            	
            	if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
            		args = new String[] {req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),"%"+req.getCompanyNameSearch()+"%"};
            		query = "payment.select.getRetAlloDtls4";
            		//query +=" AND UPPER(COMPANY_NAME) like UPPER('%"+req.getCompanyNameSearch()+"%')";
            	}
            	if(StringUtils.isNotBlank(req.getRemarksSearch())){
            		args = new String[] {req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),"%"+req.getRemarksSearch()+"%"};
            		query = "payment.select.getRetAlloDtls5";
            	//	query +=" AND UPPER(REMARKS) like UPPER('%"+req.getRemarksSearch()+"%')";
            	}
            	if(StringUtils.isNotBlank(req.getCurrencySearch())){
            		args = new String[] {req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),"%"+req.getCurrencySearch()+"%"};
            		query = "payment.select.getRetAlloDtls6";
            		//query +=" AND UPPER(CURRENCY_NAME) like UPPER('%"+req.getCurrencySearch()+"%')";
            	}
            	
			}
			
			/*if(StringUtils.isBlank(req.getFullSearch()) && StringUtils.isBlank(req.getSearchType())){
				query ="select * from ( "+query+" )where  rownum<=100";
        	}*/
			
			log.info("Query==> "+query);
			log.info("Args==> " + StringUtils.join(args,","));

			List<Map<String,Object>> dataList = queryImpl.selectList(query,args);
			if(dataList.size()>0) {
				for(int i = 0; i < dataList.size(); i++) {
					
					Map<String,Object> resMap = dataList.get(i);
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
					query = "payment.select.getPymtRetCurrDtls";

					log.info("query==> " + query);
					log.info("Args==> " + StringUtils.join(args,","));
					List<Map<String,Object>> curList = queryImpl.selectList(query,args);
					List<Map<String,Object>> allocateCurrencyList = new ArrayList<Map<String,Object>>();
				
					double Total=0.0;
					for(int c = 0; c < curList.size(); c++) {
						Map<String,Object> inResMap = curList.get(c);
						Total=Total+Double.parseDouble(inResMap.get("AMOUNT").toString());
					}for(int c = 0; c < curList.size(); c++) {
						Map<String,Object> inResMap = curList.get(c);
						if("PA".equalsIgnoreCase(req.getType())&& Total!=0) {
							inResMap.put("CURRENCY_ID",inResMap.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							allocateCurrencyList.add(inResMap);
						}
						else if("FA".equalsIgnoreCase(req.getType())&&Total==0) {
							inResMap.put("CURRENCY_ID",inResMap.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							allocateCurrencyList.add(inResMap);
						}
						else if("PA".equalsIgnoreCase(req.getAllocateType())&& Total!=0) {
							inResMap.put("CURRENCY_ID",inResMap.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
							allocateCurrencyList.add(inResMap);
						}
						else if("FA".equalsIgnoreCase(req.getAllocateType())&& Total==0) {
							inResMap.put("CURRENCY_ID",inResMap.get("CURRENCY_ID").toString()+"$"+resMap.get("PAYMENT_RECEIPT_NO").toString());
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

	@Override
	public AllocateDetailsRes1 allocateDetails(AllocateDetailsReq req) {
		AllocateDetailsRes1 response = new AllocateDetailsRes1();
		List<AllocateDetailsRes> finalList = new ArrayList<AllocateDetailsRes>();
		final AllocateDetailsRes res = new AllocateDetailsRes();
		String[] args=null;
		String selectQry = "";
		try{
			
			args = new String[1];
			args[0] = req.getPayRecNo();
			
			List<Map<String,Object>> list = queryImpl.selectList("TREASURY_PAYMENT_SELECT_GETALLOCATEDTLS", args);
			
			for(int i = 0; i < list.size(); i++) {
				Map<String,Object> resMap = list.get(i);
				
				res.setSerialNo(resMap.get("SNO")==null?"":resMap.get("SNO").toString());
				res.setAllocatedDate(resMap.get("INCEPTION_DATE")==null?"":resMap.get("INCEPTION_DATE").toString());
				res.setCurrencyValue(resMap.get("CURRENCY_ID")==null?"":resMap.get("CURRENCY_ID").toString());
				res.setAdjustmentType(resMap.get("ADJUSTMENT_TYPE")==null?"":resMap.get("ADJUSTMENT_TYPE").toString());
				
				list = queryImpl.selectList("payment.select.getSelCurrency", new String[]{req.getBranchCode(),resMap.get("CURRENCY_ID").toString()});
				if (!CollectionUtils.isEmpty(list)) {
					res.setCurrency((list.get(0).get("CURRENCY_NAME") == null ? ""
							: list.get(0).get("CURRENCY_NAME").toString()));
				}
				
				
				list = queryImpl.selectList("GET_ALOCATION_TYPE",new String[]{res.getSerialNo()});
				
				
			
				for(int k=0;k<list.size();k++){
					res.setRetroType(resMap.get("TYPE")==null?"":resMap.get("TYPE").toString());
				}
				
				if("RE".equalsIgnoreCase(resMap.get("TYPE")==null?"":resMap.get("TYPE").toString())){
					selectQry = "payment.select.getRetAmtDtls1";
				}
				else{
					selectQry = "payment.select.getRetAmtDtls";
				}
				args = new String[2];
				args[0] = res.getSerialNo();
				args[1] = res.getSerialNo();
				list = queryImpl.selectList(selectQry, args);
				if (!CollectionUtils.isEmpty(list)) {
					res.setAllTillDate(fm.formatter(list.get(0).get("PAID_AMOUNT") == null ? ""
							: list.get(0).get("PAID_AMOUNT").toString()));
				}
				
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

	@Override
	public GetTransContractRes1 getTransContract(GetTransContractReq req) {
		GetTransContractRes1 response = new GetTransContractRes1();
		List<GetTransContractRes> finalList=new ArrayList<GetTransContractRes>();
		GetTransContractRes res = new GetTransContractRes();
		String[] args=null;
        try {
        	args=new String[13];
    		log.info("Transaction Type=>"+req.getTransType());
    		args[0]=req.getBranchCode();
    		args[1]=req.getAlloccurrencyId();
    		args[2]=req.getBrokerId();
    		if("63".equalsIgnoreCase(req.getBrokerId()))
    			args[3]=req.getCedingId();
    		else        	
    			args[3]=req.getBrokerId();
    		
    		args[4] = "%%";
    		args[5] ="%%";
    		args[6]=req.getBranchCode();
    		args[7]=req.getAlloccurrencyId();
    		args[8]=req.getBrokerId();
    		if("63".equalsIgnoreCase(req.getBrokerId())) {
    			args[9]=req.getCedingId();
    		}
    		else {        	
    			args[9]=req.getBrokerId();
    		}
    		
    		args[10]="%%";
    		args[11]="%%";
    		args[12] = "%%";
            String selectQry;
           
            selectQry = "payment.select.getTranContDtls";
            log.info("Select Query=>" + selectQry);
            log.info("Args==> " + StringUtils.join(args, ","));
            List<Map<String, Object>> list =  queryImpl.selectList(selectQry, args);
            res.setTotalRecCount(String.valueOf(list.size()));

			
			List<String> receivePayAmounts = new ArrayList<String>();
			List<String> chkbox = new ArrayList<String>();
			List<String> previousValue = new ArrayList<String>();

		
			for(int i=0 ,count=0; i<list.size(); i++,count++) {
				
				Map<String,Object> tempMap = list.get(i);
				res.setContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
				res.setMode(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
				res.setProductName(tempMap.get("PRODUCT_NAME")==null?"":tempMap.get("PRODUCT_NAME").toString());
				res.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
				res.setInceptiobDate(tempMap.get("ADATE")==null?"":tempMap.get("ADATE").toString());
				res.setNetDue(tempMap.get("NETDUE")==null?"":tempMap.get("NETDUE").toString());
				res.setPayAmount(tempMap.get("PAID_AMOUNT_OC")==null?"":tempMap.get("PAID_AMOUNT_OC").toString());
				res.setAccPremium(tempMap.get("ACC_PREMIUM")==null?"":tempMap.get("ACC_PREMIUM").toString());
				res.setAccClaim(tempMap.get("ACC_CLAIM")==null?"":tempMap.get("ACC_CLAIM").toString());
				res.setCheckYN(tempMap.get("CHECKYN")==null?"":tempMap.get("CHECKYN").toString());
				res.setCheckPC(tempMap.get("BUSINESS_TYPE")==null?"":tempMap.get("BUSINESS_TYPE").toString());
				res.setCedingCompanyName(tempMap.get("CEDING_COMPANY_NAME")==null?"":tempMap.get("CEDING_COMPANY_NAME").toString());
				res.setAllocType(req.getAllocType());
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
				res.setCount(String.valueOf(count));
				finalList.add(res);
			}
		/*	res.setReceivePayAmounts(receivePayAmounts);
			res.setPayAmounts(receivePayAmounts);
			res.setChkBox(chkbox);
			res.setPreviousValue(previousValue);*/
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

	@Override
	public GetAllTransContractRes1 getAllTransContract(GetAllTransContractReq req) {
		GetAllTransContractRes1 response = new GetAllTransContractRes1();
		List<GetAllTransContractRes> payList = new ArrayList<GetAllTransContractRes>();
		String[] args=null;
		try {
			String selectQry = "payment.select.getTranContDtls";
			log.info("Select Query=>"+selectQry);
			
			args=new String[13];
			
			args[0]=req.getBranchCode();
			args[1]=req.getAlloccurrencyid();
			args[2]=req.getBrokerid();
			if("63".equalsIgnoreCase(req.getBrokerid()))
				args[3]=req.getCedingid();
			else        	
				args[3]=req.getBrokerid();
			args[4]="%%";
			args[5]="%%";
			args[6]=req.getBranchCode();
			args[7]=req.getAlloccurrencyid();
			args[8]=req.getBrokerid();
			if("63".equalsIgnoreCase(req.getBrokerid())) {
				args[9]=req.getCedingid();
			}
			else {        	
				args[9]=req.getBrokerid();
			}
			args[10]="%%";
			args[11]="%%";
			args[12]="%%";
			
			
			log.info("Args==> " + StringUtils.join(args,","));
			List<Map<String,Object>> list = queryImpl.selectList(selectQry,args);
			for(int i=0 ; i<list.size(); i++) {
				GetAllTransContractRes tempBean=new GetAllTransContractRes();
				Map<String,Object> tempMap = list.get(i);
				tempBean.setContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
				tempBean.setMode(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
				tempBean.setProductName(tempMap.get("PRODUCT_NAME")==null?"":tempMap.get("PRODUCT_NAME").toString());
				tempBean.setTransactionno(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
				tempBean.setInceptiobdate(tempMap.get("ADATE")==null?"":tempMap.get("ADATE").toString());
				tempBean.setNetdue(tempMap.get("NETDUE")==null?"":tempMap.get("NETDUE").toString());
				tempBean.setPayamount(tempMap.get("PAID_AMOUNT_OC")==null?"":tempMap.get("PAID_AMOUNT_OC").toString());
				tempBean.setAccPremium(tempMap.get("ACC_PREMIUM")==null?"":tempMap.get("ACC_PREMIUM").toString());
				tempBean.setAccClaim(tempMap.get("ACC_CLAIM")==null?"":tempMap.get("ACC_CLAIM").toString());
				tempBean.setCheckYN(tempMap.get("CHECKYN")==null?"":tempMap.get("CHECKYN").toString());
				tempBean.setCheckPC(tempMap.get("BUSINESS_TYPE")==null?"":tempMap.get("BUSINESS_TYPE").toString());
				tempBean.setSubClass(tempMap.get("DEPT_ID")==null?"":tempMap.get("DEPT_ID").toString());
				tempBean.setProposalNo(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
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

	@Override
	public GetDirectCedingRes1 getDirectCeding(String branchId) {
		GetDirectCedingRes1 response1 = new GetDirectCedingRes1();
		 log.info("TreasuryDAOImpl getDirectCeding || Enter");
	        List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
	        List<GetDirectCedingRes> response = new ArrayList<GetDirectCedingRes>();
	      
	        String[] args=null;
	        try{
	        	 String sql="ceding.select.direct";
	            
	            args=new String[]{branchId};
	           
	            log.info("Select Query====>"+sql);
	            log.info("Args==> " + StringUtils.join(args,","));
	            result=queryImpl.selectList(sql,args);
	            for(int i=0 ; i<result.size(); i++) {
					GetDirectCedingRes tempBean=new GetDirectCedingRes();
					Map<String,Object> tempMap = result.get(i);
					tempBean.setCode(tempMap.get("ID")==null?"":tempMap.get("ID").toString());
					tempBean.setCodeDescription(tempMap.get("NAME")==null?"":tempMap.get("NAME").toString());
					
					
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

	@Override
	public GetShortnameRes getShortname(String branchcode) {
		GetShortnameRes res = new GetShortnameRes();
		String Short="";
		  List<Map<String, Object>> list = queryImpl.selectList("GET_SHORT_NAME", new String[] {branchcode});
		  if (!CollectionUtils.isEmpty(list)) {
				Short = ((list.get(0).get("COUNTRY_SHORT_NAME") == null ? ""
						: list.get(0).get("COUNTRY_SHORT_NAME").toString()));
			}

		res.setCommonResponse(Short);;
		return res;
	}

	@Override
	public GetTreasuryJournalViewRes1 getTreasuryJournalView(GetTreasuryJournalViewReq req) {
		GetTreasuryJournalViewRes1 response1 = new GetTreasuryJournalViewRes1();
		List<GetTreasuryJournalViewRes> response = new ArrayList<GetTreasuryJournalViewRes>();
	
		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
		 String[] args=null;
		try{
			args=new String[2];
			if(req.getType().equalsIgnoreCase("")){
			args[0]=req.getSerialNo();
			args[1]=req.getSerialNo();
			}
			else{
				args[0]=req.getAllocationNo();
				args[1]=req.getAllocationNo();
			}
			String sql=	"treasury.journal.view";
			
			result=queryImpl.selectList(sql,args);
			
			for(int i=0 ; i<result.size(); i++) {
				GetTreasuryJournalViewRes tempBean=new GetTreasuryJournalViewRes();
				Map<String,Object> tempMap = result.get(i);
				tempBean.setStartDate(tempMap.get("STARTDATE ")==null?"":tempMap.get("STARTDATE").toString());
				tempBean.setReference(tempMap.get("REFERENCE")==null?"":tempMap.get("REFERENCE").toString());
				tempBean.setLedger(tempMap.get("LEDGER")==null?"":tempMap.get("LEDGER").toString());
				tempBean.setUwy(tempMap.get("UWY")==null?"":tempMap.get("UWY").toString());
				tempBean.setSpc(tempMap.get("SPC")==null?"":tempMap.get("SPC").toString());
				tempBean.setCurrency(tempMap.get("CURRENCY")==null?"":tempMap.get("CURRENCY").toString());
				tempBean.setDebitoc(tempMap.get("DEBITOC")==null?"":tempMap.get("DEBITOC").toString());
				tempBean.setCreditoc(tempMap.get("CREDITOC")==null?"":tempMap.get("CREDITOC").toString());
				tempBean.setDebitugx(tempMap.get("DEBITUGX")==null?"":tempMap.get("DEBITUGX").toString());
				tempBean.setCreditugx(tempMap.get("CREDITUGX")==null?"":tempMap.get("CREDITUGX").toString());
				tempBean.setNarration(tempMap.get("NARRATION")==null?"":tempMap.get("NARRATION").toString());
				tempBean.setProductId(tempMap.get("PRODUCT_ID")==null?"":tempMap.get("PRODUCT_ID").toString());
				tempBean.setEndDate(tempMap.get("END_DATE")==null?"":tempMap.get("END_DATE").toString());
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

	@Override
	public CommonResponse getRetroallocateTransaction(GetRetroallocateTransactionReq req) {
		CommonResponse response = new CommonResponse();
		log.info("PaymentDAOImpl getRetroallocateTransaction() || Enter");

		String[] args=null;
		try{
			
			List<GetAllRetroTransContractRes> payList = getAllRetroTransContract(req);
			
			Double a=0.0,b=0.0,c=0.0;
			String updateQry;
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
				 	updateQry = "payment.update.retro.details";
				 
				 	String[] updateArgs = new String[3];
				 	updateArgs[0] = req.getTransType().equals("PT")?Double.toString((-1)*Double.parseDouble(filterTrack.get(0).getTransactionNo())):Double.toString(Double.parseDouble(filterTrack.get(0).getTransactionNo()));
					updateArgs[1] = form.getContractNo();
					updateArgs[2] = form.getTransactionNo();
					log.info("Update Query=>"+updateQry);
					log.info("Args[0]=>"+updateArgs[0]+"\nArgs[1]=>"+updateArgs[1]+"\nArgs[2]=>"+updateArgs[2]);
					
					queryImpl.updateQuery(updateQry,updateArgs);
					
				 	
					updateQry =	"payment.update.retro.status";


				 	updateArgs = new String[3];
					updateArgs[0] = "Allocated";
					updateArgs[1] = form.getContractNo();
					updateArgs[2] = form.getTransactionNo();
					log.info("Update Query=>"+updateQry);
					log.info("Args[0]=>"+updateArgs[0]+"\nArgs[1]=>"+updateArgs[1]+"\nArgs[2]=>"+updateArgs[2]);
					
					queryImpl.updateQuery(updateQry,updateArgs);
				 
				
				 
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
					updateQry =	"payment.insert.alloTran";
					
					log.info("Query=>"+updateQry);
				
					log.info("Args==> " + StringUtils.join(args,","));
					
					queryImpl.updateQuery(updateQry,args);
				
					
				}
			}
			
			String[] updateArgs = new String[5];
			updateArgs[0] = String.valueOf(a);
			updateArgs[1] = req.getLoginId();
			updateArgs[2] = req.getBranchCode();
			updateArgs[3] = req.getPolicyNo();
			updateArgs[4] = req.getAllOccurencyId();
			updateQry= "payment.update.AlloTranDtls";
			log.info("Update Query=>"+updateQry);
			log.info("Args[0]=>"+updateArgs[0]+" Args[1]=>"+updateArgs[1]+"\nArgs[2]=>"+updateArgs[2]);
			
			queryImpl.updateQuery(updateQry,updateArgs);
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
		String[] args=null;
		try {
			
			String selectQry ="payment.select.getDirBroDtls.retro";
			log.info("Select Query=>"+selectQry);
			
			args=new String[4];
			log.info("Transaction Type=>"+req.getTransType());
			args[0]=req.getBranchCode();
			args[1]=req.getAllOccurencyId();
			args[2]=req.getBrokerId();
			if("63".equalsIgnoreCase(req.getBrokerId()))
				args[3]=req.getCedingId();
			else        	
				args[3]=req.getBrokerId();
			
			log.info("Args==> " + StringUtils.join(args,","));
			List<Map<String,Object>> list = queryImpl.selectList(selectQry,args);
			for(int i=0 ; i<list.size(); i++) {
				GetAllRetroTransContractRes tempBean=new GetAllRetroTransContractRes();
				Map<String,Object> tempMap = list.get(i);
				tempBean.setContractNo(tempMap.get("RETRO_CONTRACT_NUMBER")==null?"":tempMap.get("RETRO_CONTRACT_NUMBER").toString());
				tempBean.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
				tempBean.setInceptiobdate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
				tempBean.setNetDue(tempMap.get("NETDUE")==null?"":tempMap.get("NETDUE").toString());
				tempBean.setCheckPC(tempMap.get("SIGN")==null?"":tempMap.get("SIGN").toString());
				tempBean.setCedingCompanyName(tempMap.get("CEDING_COMPANY_NAME")==null?"":tempMap.get("CEDING_COMPANY_NAME").toString());
				
				payList.add(tempBean);
			}
		}
		catch(Exception exception) {
			log.debug("Exception @ { " + exception + " } ");
		}
		return payList;
	}


	public synchronized String getSequence(String type,String productID,String departmentId,String branchCode, String proposalNo,String date){ 
		//Logger logger = (Logger) LogUtil.getLogger(TreasuryServiceImpl.class);
		String seqName="";
		try{
			
			//String query=getQuery(DBConstants.COMMON_SELECT_SEQNAME);
			//String query="select fn_get_seqno('Proposal',1,2,'06') from dual";
			String query="dropdowm.getseqno";
			log.info("Query==> " + query);
			log.info("Type==> " + type + ", Product ID==> " + productID + ", dept ID==> " + departmentId + ", Branch Code==> " + branchCode);
			List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{type,productID,departmentId,branchCode,proposalNo,date});
			if (!CollectionUtils.isEmpty(list)) {
				seqName = ((list.get(0).get("SEQNO") == null ? ""
						: list.get(0).get("SEQNO").toString()));
			}
			
			
			log.info("Result==> " + seqName);
			
		}catch(Exception e){
			log.debug("Exception @ {" + e + "}");
		}

		return seqName;
	}
	public String getAmend(GetRetroallocateTransactionReq req) {

		String	AmendDate="";
		try{
			
			String query="GET_AMENDMENT_DATE";
			log.info("Select Query ==> " + query);
			log.info("Args[0]==> " + req.getSerialNo());
			List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{req.getSerialNo()});
			if (!CollectionUtils.isEmpty(list)) {
				AmendDate = list.get(0).get("AMENDMENT_DATE") == null ? ""
						: list.get(0).get("AMENDMENT_DATE").toString();
			}
			
			System.out.println(AmendDate);
		}catch(Exception e){
			log.debug("Exception @ {" + e + "}");	
		}

		return AmendDate;
	}

	public String getTrans(GetRetroallocateTransactionReq req) {
		String transDate="";
		try{
			String query="GET_TRANSACTION_DATE";
			log.info("Select Query ==> " + query);
			log.info("Args[0]==> " + req.getPolicyNo());
			List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{req.getPolicyNo()});
			if (!CollectionUtils.isEmpty(list)) {
				transDate = list.get(0).get("TRANS_DATE") == null ? ""
						: list.get(0).get("TRANS_DATE").toString();
			}
			System.out.println(transDate);
		}catch(Exception e){
			log.debug("Exception @ {" + e + "}");	
		}

		return transDate;
	}

	@Override
	public ReciptGetLIstRes getReciptList(ReciptListReq req) {
		ReciptGetLIstRes response = new ReciptGetLIstRes();
		List<ReciptListRes> finalList = new ArrayList<ReciptListRes>();
		String [] args=null;
		int count = 0;
		try {
			GetOpenPeriodRes openres=dropimpl.getOpenPeriod(req.getBranchCode());
			args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getBranchCode(), req.getTransType(), req.getBranchCode()};
			String	receiptList = "payment.select.getRetLists";
			
			
			
			if(StringUtils.isNotBlank(req.getSearchType())){
				//query+=" WHERE BRANCH_CODE="+branchCode;
				
				receiptList = "payment.select.getRetLists1";
				args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getBranchCode(), req.getTransType(), req.getBranchCode(), req.getBranchCode()};
            	if(StringUtils.isNotBlank(req.getPaymentNoSearch())){
            		//query +=" AND PAYMENT_RECEIPT_NO like '%"+bean.getPaymentNoSearch()+"%'";
            		receiptList = "payment.select.getRetLists2";
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getBranchCode(), req.getTransType(), req.getBranchCode(), req.getBranchCode(),req.getPaymentNoSearch()};
            		
            	}
            	if(StringUtils.isNotBlank(req.getBrokerNameSearch())){
            		//query +=" AND UPPER(BROKER) like UPPER('%"+bean.getBrokerNameSearch()+"%')";
            		receiptList = "payment.select.getRetLists3";
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getBranchCode(), req.getTransType(), req.getBranchCode(), req.getBranchCode(),req.getBrokerNameSearch()};
            	
            	}
            	if(StringUtils.isNotBlank(req.getCompanyNameSearch())){
            		//query +=" AND UPPER(COMPANY_NAME) like UPPER('%"+bean.getCompanyNameSearch()+"%')";
            		receiptList = "payment.select.getRetLists4";
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getBranchCode(), req.getTransType(), req.getBranchCode(), req.getBranchCode(),req.getCompanyNameSearch()};
            	}
            	if(StringUtils.isNotBlank(req.getRemarksSearch())){
            		//query +=" AND UPPER(REMARKS) like UPPER('%"+bean.getRemarksSearch()+"%')";
            		receiptList = "payment.select.getRetLists5";
            		args = new String[] {req.getBranchCode(),req.getBranchCode(),req.getTransType(),req.getBranchCode(),req.getBranchCode(), req.getTransType(), req.getBranchCode(), req.getBranchCode(),req.getRemarksSearch()};
            	}
            	
			}
			
		/*	if(StringUtils.isBlank(bean.getFullSearch()) && StringUtils.isBlank(bean.getSearchType())){
				query ="select * from ( "+query+" )where  rownum<=100";
        	}*/
			
			
			
			List<Map<String,Object>> list = queryImpl.selectList(receiptList, args);
			
			for(int i = 0; i < list.size(); i++ ) {
				ReciptListRes tempBean=new ReciptListRes();
				Map<String,Object> resMap = list.get(i);
				tempBean.setPayrecno(resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString());
				tempBean.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				tempBean.setBroker(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				tempBean.setName(resMap.get("PAYMENT_RESPONSE")==null?"":resMap.get("PAYMENT_RESPONSE").toString());
				tempBean.setPayamount(resMap.get("PAID_AMT")==null?"":fm.formatter(resMap.get("PAID_AMT").toString()));
				tempBean.setBrokerid(resMap.get("BROKER_ID")==null?"":resMap.get("BROKER_ID").toString());
				tempBean.setSerialno(resMap.get("REVERSALTRANSNO")==null?"":resMap.get("REVERSALTRANSNO").toString());
				tempBean.setRemarks(resMap.get("REMARKS")==null?"":resMap.get("REMARKS").toString());
				tempBean.setTrdate(resMap.get("TRANS_DATE")==null?"":resMap.get("TRANS_DATE").toString()); 
				tempBean.setTransactionType(resMap.get("TRANSCATIONTYPE")==null?"":resMap.get("TRANSCATIONTYPE").toString());
				
				args = new String[2];
				args[0] = resMap.get("PAYMENT_RECEIPT_NO").toString();
				args[1] = "Y";
				List<Map<String,Object>> allocount = queryImpl.selectList("payment.select.getTotCount", args);
				if(!CollectionUtils.isEmpty(allocount)) {
					count=allocount.get(0).get("COUNT")==null?0:Integer.parseInt(allocount.get(0).get("COUNT").toString()) ;
				}
				
				if(count!=0) {
					tempBean.setEditShowStatus("No");
				}
				else {
					tempBean.setEditShowStatus("Yes");                	  
				}
				args = new String[2];
				args[0] = resMap.get("PAYMENT_RECEIPT_NO").toString();
				args[1] = "R";
				List<Map<String,Object>> counting = queryImpl.selectList("payment.select.getTotCount",args);
				if(!CollectionUtils.isEmpty(counting)) {
					count=counting.get(0).get("COUNT")==null?0:Integer.parseInt(counting.get(0).get("COUNT").toString()) ;
				}
				//count = this.mytemplate.queryForInt(getQuery(DBConstants.PAYMENT_SELECT_GETTOTCOUNT),new Object[]{resMap.get("PAYMENT_RECEIPT_NO").toString(),"R"});
				/*if(count>0)
				{
					tempBean.setReversedShowStatus("Yes");
				}else 
				{
					tempBean.setReversedShowStatus("No");                	  
				}*/
				if(count!=0)
				{
					tempBean.setReversedShowStatus("Yes");
					//tempBean.setEditShowStatus("No");
				}else 
				{
					tempBean.setReversedShowStatus("No");
					//tempBean.setEditShowStatus("Yes");
				}
				 if(StringUtils.isNotBlank(openres.getCommonResponse().get(0).getOpstartDate())&& StringUtils.isNotBlank(openres.getCommonResponse().get(0).getOpendDate()) && StringUtils.isNotBlank(tempBean.getTrdate())){
						if(Validatethree(req.getBranchCode(), tempBean.getTrdate())==0){
							tempBean.setRecpayOpenYN("Yes");
						}
					}
				    args = new String[2];
					args[0] =req.getBranchCode();
					args[1] = tempBean.getPayrecno();
				 List<Map<String,Object>> curList = queryImpl.selectList("payment_amount_details",args);
				 	//String query1=getQuery(DBConstants.PAYMENT_AMOUNT_DETAILS);
//					args = new Object[2];
//					args[0] = branchCode;
//					args[1] = tempBean.getPay_rec_no();
//					List<Map<String,Object>> curList = this.mytemplate.queryForList(query1,args);
					double Total=0.0;
					for(int c = 0; c < curList.size(); c++) {
						Map<String,Object> inResMap = curList.get(c);
						Total=Total+Double.parseDouble(inResMap.get("AMOUNT").toString());
					}for(int c = 0; c < curList.size(); c++) {
						Map<String,Object> inResMap = curList.get(c);
						if(Total!=0) {
							tempBean.setAllocationStatus("Partial");
							tempBean.setType("PA");						}
						else if(Total==0) {
							tempBean.setAllocationStatus("Full");
							tempBean.setType("FA");							
						}
					}
					/*for(int c = 0; c < curList.size(); c++) {
						Map<String,Object> inResMap = curList.get(c);
						if(Double.parseDouble(inResMap.get("AMOUNT").toString())!=0) {
							tempBean.setAllocationStatus("Partial");
							tempBean.setType("PA");
							
						}
						else if( Double.parseDouble(inResMap.get("AMOUNT").toString())==0) {
							tempBean.setAllocationStatus("Full");
							tempBean.setType("FA");
						}
					}*/
				finalList.add(tempBean);
			}
			response.setCommonResponse(finalList);
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

	@Override
	public RetroTransListRes getRetroTransContract(RetroTransReq req) {
		RetroTransListRes response = new RetroTransListRes();
		List<RetroTransRes> finalList = new ArrayList<RetroTransRes>();
		String [] args=null;
		args=new String[4];
		args[0]=req.getBranchcode();
		args[1]=req.getAlloccurrencyid();
		args[2]=req.getBrokerid();
		if("63".equalsIgnoreCase(req.getBrokerid()))
			args[3]=req.getCedingid();
		else        	
			args[3]=req.getBrokerid();
		//args[4]="%"+(bean.getSearchContractNo()).trim()+"%";
		//args[5]="%"+bean.getSearchBusinessType()+"%";
		
        try {
            List<Map<String, Object>> select = queryImpl.selectList("payment.select.getDirBroDtls.retro", args);
           
            log.info("Args==> " + StringUtils.join(args, ","));
            List<Map<String, Object>> list = select;


					for(int i=0 ; i<list.size() ; i++) {
						RetroTransRes tempBean=new RetroTransRes();
						Map<String,Object> tempMap = list.get(i);
						tempBean.setContractno(tempMap.get("RETRO_CONTRACT_NUMBER")==null?"":tempMap.get("RETRO_CONTRACT_NUMBER").toString());
						tempBean.setTransactionno(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
						tempBean.setInceptiobdate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
						tempBean.setNetdue(tempMap.get("NETDUE")==null?"":tempMap.get("NETDUE").toString());
						tempBean.setCheckPC(tempMap.get("sign")==null?"":tempMap.get("sign").toString());
						tempBean.setCedingCompanyName(tempMap.get("CEDING_COMPANY_NAME")==null?"":tempMap.get("CEDING_COMPANY_NAME").toString());
						tempBean.setAllocType(req.getAllocType());
						List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> tempBean.getTransactionno().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
						if(!CollectionUtils.isEmpty(filterTrack)) {
							tempBean.setReceivePayAmounts(Integer.toString(Math.abs(Integer.parseInt(filterTrack.get(0).getTransactionNo()))));
							tempBean.setPreviousValue(filterTrack.get(0).getTransactionNo());
							tempBean.setChkbox("true");
						}else {
							tempBean.setReceivePayAmounts("");
							tempBean.setPreviousValue("");
							tempBean.setChkbox("false");
						}
						finalList.add(tempBean);
					}
					response.setCommonResponse(finalList);
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
	@Override
	public ReceiptTreasuryListRes getReceiptTreasuryGeneration(ReceiptTreasuryReq req) {
		List<ReceiptTreasuryRes> finalList = new ArrayList<ReceiptTreasuryRes>();
		ReceiptTreasuryListRes response =new ReceiptTreasuryListRes();
		String[] args = null;
		List<Map<String,Object>> recipt;
		try {
			if("63".equals(req.getBroker())) {
				//query=getQuery(DBConstants.PAYMENT_SELECT_GETDIRBRODTLS);
				args=new String[5];
				args[0]= req.getBranchCode();
				args[1]=req.getBranchCode();
				args[2]=req.getBranchCode();
				args[3]=req.getPayrecno();
				args[4]=req.getBranchCode();
				recipt = queryImpl.selectList("payment.select.getDirBroDtls", args);
			}
			else {
				//query=getQuery(DBConstants.PAYMENT_SELECT_GETBRODTLS);
				args=new String[4];
				args[0]=req.getBranchCode();
				args[1]=req.getBranchCode();
				args[2]=req.getPayrecno();
				args[3]=req.getBranchCode();
				recipt = queryImpl.selectList("payment.select.getBroDtls", args);
			}
			log.info("Query==> " + recipt);
			log.info("Args==> " + StringUtils.join(args,","));

			//for(int i=0;i<list.size();i++) {
			if(recipt.size()>0) {
				ReceiptTreasuryRes tempBean=new ReceiptTreasuryRes();
				Map<String,Object> resMap = recipt.get(0);
				tempBean.setCedingCo(resMap.get("COMPANY_NAME")==null?"":resMap.get("COMPANY_NAME").toString());
				tempBean.setBrokername(resMap.get("BROKER")==null?"":resMap.get("BROKER").toString());
				tempBean.setCurrency(resMap.get("CURRENCY_NAME")==null?"":resMap.get("CURRENCY_NAME").toString());
				tempBean.setPaymentamount(resMap.get("PAID_AMT")==null?"":fm.formatter(resMap.get("PAID_AMT").toString()));
				tempBean.setTrdate(resMap.get("TRANSDATE")==null?"":resMap.get("TRANSDATE").toString());
				tempBean.setSerialno(resMap.get("PAYMENT_RECEIPT_NO")==null?"":resMap.get("PAYMENT_RECEIPT_NO").toString());
				tempBean.setExchangerate(resMap.get("EXCHANGE_RATE")==null?"":resMap.get("EXCHANGE_RATE").toString());
				tempBean.setExrate(resMap.get("EXCHANGE_RATE")==null?"":fm.formatter(resMap.get("EXCHANGE_RATE").toString()));
				tempBean.setTotalexchaamount(resMap.get("TOT_EXC_AMT")==null?"":fm.formatter(resMap.get("TOT_EXC_AMT").toString()));
				tempBean.setBroker(resMap.get("BROKER_ID")==null?"":resMap.get("BROKER_ID").toString());
				tempBean.setDiffAmount(resMap.get("DIFF_AMT")==null?"":fm.formatter(resMap.get("DIFF_AMT").toString()));
				tempBean.setTransactionType(resMap.get("TRANSCATIONTYPE")==null?"":resMap.get("TRANSCATIONTYPE").toString());
				tempBean.setCurrencyValue(resMap.get("CURRENCY_ID")==null?"":resMap.get("CURRENCY_ID").toString());
				tempBean.setPayrecno(resMap.get("REVERSALTRANSNO")==null?"":resMap.get("REVERSALTRANSNO").toString());
				tempBean.setBankCharges(resMap.get("BANK_CHARGES")==null?"":fm.formatter(resMap.get("BANK_CHARGES").toString()));
				tempBean.setNetAmt(resMap.get("NET_AMT")==null?"":fm.formatter(resMap.get("NET_AMT").toString()));
				tempBean.setReceiptBankName(resMap.get("BANK_NAME")==null?"":resMap.get("BANK_NAME").toString());
                tempBean.setWithHoldingTaxOC(resMap.get("WH_TAX") == null ? "" : fm.formatter(resMap.get("WH_TAX").toString()));
                tempBean.setPremiumLavy(resMap.get("PREMIUM_LAVY") == null ? "" : fm.formatter(resMap.get("PREMIUM_LAVY").toString()));
                finalList.add(tempBean);
            }
			//}
			response.setCommonResponse(finalList);
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

	@Override
	public ReceiptViewListsRes getReceiptViewList(ReceiptViewListReq req) {
		ReceiptViewListsRes response = new ReceiptViewListsRes();
		log.info("PaymentDAOImpl getReceiptViewList() || Enter"); 
		List<ReciptViewRes> finalList=new ArrayList<ReciptViewRes>();
		Object[] args = null;
		try
		{
			List<Map<String,Object>> resultList = queryImpl.selectList("payment.select.getRetViewDtls", null);
			//String selectQry = getQuery(DBConstants.PAYMENT_SELECT_GETRETVIEWDTLS);
			args = new Object[2];
			args[0] = req.getBranchCode();
			args[1] = req.getSerialno();
			log.info("Select Query=>"+resultList);
			log.info("Args==> " + StringUtils.join(args,","));
			//List<Map<String,Object>> resultList = this.mytemplate.queryForList(selectQry,args);
			for(int i=0;i<resultList.size();i++) {
				ReciptViewRes tempBean=new ReciptViewRes();
				Map<String,Object> resMap = resultList.get(i);
				tempBean.setPayres(resMap.get("RECEIPT_NO")==null?"":resMap.get("RECEIPT_NO").toString());
				tempBean.setSerialno(resMap.get("RECEIPT_SL_NO")==null?"":resMap.get("RECEIPT_SL_NO").toString());
				tempBean.setPayamount(resMap.get("AMOUNT")==null?"":fm.formatter(resMap.get("AMOUNT").toString()));
				tempBean.setExrate(resMap.get("EXCHANGE_RATE")==null?"":fm.formattereight(resMap.get("EXCHANGE_RATE").toString()));
				tempBean.setInceptiobdate(resMap.get("IDATE")==null?"":resMap.get("IDATE").toString());
				tempBean.setCurrency(resMap.get("CURRENCY_NAME")==null?"":resMap.get("CURRENCY_NAME").toString());
				tempBean.setTotAmount(resMap.get("TOT_AMT")==null?"":fm.formatter(resMap.get("TOT_AMT").toString()));
				tempBean.setSetExcRate(resMap.get("SETTLED_EXCRATE")==null?"":fm.formattereight(resMap.get("SETTLED_EXCRATE").toString()));
				tempBean.setConRecCur(resMap.get("CONVERTED_RECCUR")==null?"":fm.formatter(resMap.get("CONVERTED_RECCUR").toString()));
				tempBean.setHideRowCnt(Integer.toString(resultList.size()));
				finalList.add(tempBean);
			}
			response.setCommonResponse(finalList);
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

	@Override
	public CommonSaveRes getCurrecyAmount(CurrecyAmountReq req) {
		CommonSaveRes response = new CommonSaveRes();
		String  currecyAmount="";
		String[] args = null;
		try{
			String selectQry="";
			args=new String[3];
			args[0]=req.getBranchCode();
			args[1]=req.getSerialNo();
			args[2]=req.getCurrValu();
			//selectQry = getQuery(DBConstants.PAYMENT_SELECT_CURRECYAMT);
			List<Map<String,Object>> list = queryImpl.selectList("payment.select.currencyAmt", args);
			log.info("Select Query=>"+list);
			log.info("Args[0]=>"+args[0]);
			log.info("Args[1]=>"+args[1]);
			log.info("Args[2]=>"+args[2]);
			//List<Map<String,Object>> list = this.mytemplate.queryForList(selectQry, args);
			log.info("List Size=>"+list.size());

			if(list!=null &&list.size()>0) {
				Map<String,Object> resMap = list.get(0);
				currecyAmount=resMap.get("AMOUNT").toString()+"$"+resMap.get("CURRENCY_NAME").toString();
				
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

	@Override
	public SecondPageInfoRes getSecondPageInfo(SecondPageInfoReq req) {
		SecondPageInfoRes response = new SecondPageInfoRes();
		SecondPageInfoListsRes res = new SecondPageInfoListsRes();
		List<SecondPageInfoListsRes> finalList = new ArrayList<SecondPageInfoListsRes>();
		log.info("PaymentDAOImpl getSecondPageInfo() || Enter");
		String[] args = null;
		String diffAmt = "";
		try{
			args = new String[1];
			args[0] = req.getSerialno();
			//String selectQry = getQuery(DBConstants.PAYMENT_SELECT_GETSECONDPAGEDTLS);
			List<Map<String,Object>> list = queryImpl.selectList("payment.select.getSecondPageDtls", args);
			log.info("Query=>"+list);
			log.info("Arg[0]=>"+args[0]);
			//List<Map<String,Object>> list = this.mytemplate.queryForList(selectQry,args);
			log.info("List Size=>"+list.size());
			double totAmt=0.0;
			double totConRecCur=0.0;

//			ArrayList<String> cedingCompanyValList = new ArrayList<String>();
//			List<String> exachangeValList = new ArrayList<String>();
//			List<String> payamountValList = new ArrayList<String>();
//			List<String> rowamountValList = new ArrayList<String>();
//			List<String> recNoValList = new ArrayList<String>();
//			List<String> setExcRateValList = new ArrayList<String>();
//			List<String> conRecCurValList = new ArrayList<String>();
			List<ListSecondPageInfo> infos = new ArrayList<ListSecondPageInfo>();
			ListSecondPageInfo info = new ListSecondPageInfo();
			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> resMap = list.get(i);
				
				
				info.setCedingCompanyValList(resMap.get("CURRENCY_ID").toString());
				info.setExachangeValList(resMap.get("EXCHANGE_RATE")==null?"0":fm.formattereight(resMap.get("EXCHANGE_RATE").toString()));
				info.setPayamountValList(resMap.get("AMOUNT")==null?"0":fm.formatter(resMap.get("AMOUNT").toString()));
				info.setRowamountValList(resMap.get("TOT_AMT")==null?"0":fm.formatter(resMap.get("TOT_AMT").toString()));
				info.setRecNoValList(resMap.get("RECEIPT_NO").toString());
				info.setSetExcRateValList(resMap.get("SETTLED_EXCRATE")==null?"0":fm.formattereight(resMap.get("SETTLED_EXCRATE").toString()));
				info.setConRecCurValList(resMap.get("CONVERTED_RECCUR")==null?"0":fm.formatter(resMap.get("CONVERTED_RECCUR").toString()));
				
				infos.add(info);
				
				totAmt=totAmt+Double.parseDouble(resMap.get("TOT_AMT")==null?"0":resMap.get("TOT_AMT").toString());
				totConRecCur=totConRecCur+Double.parseDouble(resMap.get("CONVERTED_RECCUR")==null?"0":resMap.get("CONVERTED_RECCUR").toString());
				finalList.add(res);
			}
			
			res.setTotalConRecCur(String.valueOf(totConRecCur));
			String selectQry = "payment.select.getDiffAmt";
		 list = queryImpl.selectList(selectQry, new String[] {req.getSerialno(),req.getBranchCode()});
		 if (!CollectionUtils.isEmpty(list)) {
			 diffAmt = ((list.get(0).get("DIFF_AMT") == null ? ""
						: list.get(0).get("DIFF_AMT").toString()));
			}
			log.info("Result=>"+diffAmt);
			if(diffAmt!=null && diffAmt.length()>0) {
				double Amt=Double.parseDouble(diffAmt);
				if(Amt<0) {
					Amt=Amt*(-1);
					totAmt=totAmt-Amt;
					res.setDifftype("M");
				} else {
					res.setDifftype("B");
					totAmt=totAmt+Amt;
				}
				res.setTxtDiffAmt(String.valueOf(Amt));
			} else {
				res.setDifftype("N");
			}
			res.setTxtTotalAmt(String.valueOf(totAmt));
			
			res.setListSecondPageInfo(infos);
			response.setCommonResponse(finalList);
			response.setMessage("Success");
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

@Override
	public CommonResponse getAllocateTransaction(GetTransContractReq req) {
	CommonResponse response = new CommonResponse();
	
		log.info("PaymentDAOImpl getallocateTransaction() || Enter");
		try{
			//List<TreasuryBean> payList=getTransContract(payform,branchCode,receivePayAmountMap);
			
			List<TransContractRes> payList = getAllTransContract(req);
			String serialNo;
			String updateQry;
			Double a=0.0,b=0.0,c=0.0;
			
			
			serialNo=queryImpl.getSequenceNo("TreasuryARP","","", req.getBranchCode(),"",req.getAccountDate());
				
			req.setSerialno(serialNo);
			String [] args = null;
			
			for(int i=0;i<payList.size();i++) {
				
				TransContractRes form= payList.get(i);
				
				List<GetTransContractListReq> filterTrack = req.getTransContractListReq().stream().filter( o -> form.getTransactionno().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
				if(!CollectionUtils.isEmpty(filterTrack)) {
				
				//if(receivePayAmountMap.containsKey(form.getTransactionNo())) {	 
					 args=new String[17];
					 args[0]=serialNo;	
					 args[1]=form.getContractno();
					 args[2]=StringUtils.isBlank(form.getMode())?"0":form.getMode();
					
					 args[3]=form.getProductname();
					 args[4]=form.getTransactionno();
					 args[5]=req.getAccountDate();
						if("P".equalsIgnoreCase(form.getCheckPC())){
						 	
							args[6]= filterTrack.get(0).getTransactionNo();
						 	args[7]="P";
						 	queryImpl.updateQuery("payment.update.rskPremAlloDtls", args);
						 	String[] updateArgs = new String[5];
							
						 	updateArgs[0] = filterTrack.get(0).getTransactionNo();
						 	updateArgs[1] = req.getLoginid();
						 	updateArgs[2] = req.getBranchCode();
							updateArgs[3] = form.getContractno();
							updateArgs[4] = form.getTransactionno();
						 	queryImpl.updateQuery("payment.update.preSetStatus", updateArgs);
						 	updateArgs = new String[5];
							updateArgs[0] = "Allocated";
							updateArgs[1] = req.getLoginid();
						 	updateArgs[2] = req.getBranchCode();
							updateArgs[3] = form.getContractno();
							updateArgs[4] = form.getTransactionno();

						 	a=a+Double.parseDouble(filterTrack.get(0).getTransactionNo());
						}
						else if("C".equalsIgnoreCase(form.getCheckPC())) {
							//args[6]=form.getAccClaim();
							args[6] = filterTrack.get(0).getTransactionNo();
						 	args[7]="C";
							queryImpl.updateQuery("payment.update.claimPymtAlloDtls", args);
							String[] updateArgs = new String[5];
							//updateArgs[0] = form.getAccClaim();
							updateArgs[0] = filterTrack.get(0).getTransactionNo();
							updateArgs[1] = req.getBranchCode();
						 	updateArgs[2] = req.getLoginid();
							updateArgs[3] = form.getContractno();
							updateArgs[4] = form.getTransactionno();

							queryImpl.updateQuery("payment.update.claimSetStatus", updateArgs);
							updateArgs = new String[5];
							updateArgs[0] = "Allocated";
							updateArgs[1] = req.getBranchCode();
						 	updateArgs[2] = req.getLoginid();
							updateArgs[3] = form.getContractno();
							updateArgs[4] = form.getTransactionno();
							b = b + Double.parseDouble(filterTrack.get(0).getTransactionNo());
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
					queryImpl.updateQuery("payment.insert.alloTran", args);
					
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
		    queryImpl.updateQuery("payment.update.AlloTranDtls", updateArgs);

			queryImpl.updateQuery("payment.update.rskPremChkyn", updateArgs);
			response.setMessage("Success");
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

	public List<TransContractRes> getAllTransContract(GetTransContractReq req) {
		List<TransContractRes> payList = new ArrayList<TransContractRes>();
		String [] args = null;
		try {
			//String selectQry = getQuery(DBConstants.PAYMENT_SELECT_GETTRANCONTDTLS);
			
			
			
			args=new String[13];
			log.info("Transaction Type=>"+req.getTransType());
			args[0]=req.getBranchCode();
			args[1]=req.getAlloccurrencyId();
			args[2]=req.getBrokerId();
			if("63".equalsIgnoreCase(req.getBrokerId()))
				args[3]=req.getCedingId();
			else        	
				args[3]=req.getBrokerId();
			args[4]="%%";
			args[5]="%%";
			args[6]=req.getBranchCode();
			args[7]=req.getAlloccurrencyId();
			args[8]=req.getBrokerId();
			if("63".equalsIgnoreCase(req.getBrokerId())) {
				args[9]=req.getCedingId();
			}
			else {        	
				args[9]=req.getBrokerId();
			}
			args[10]="%%";
			args[11]="%%";
			args[12]="%%";
			
			
			log.info("Args==> " + StringUtils.join(args,","));
			List<Map<String,Object>> list = queryImpl.selectList("payment.select.getTranContDtls", args);
			for(int i=0 ; i<list.size(); i++) {
				TransContractRes tempBean=new TransContractRes();
				Map<String,Object> tempMap = list.get(i);
				tempBean.setContractno(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
				tempBean.setMode(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
				tempBean.setProductname(tempMap.get("PRODUCT_NAME")==null?"":tempMap.get("PRODUCT_NAME").toString());
				tempBean.setTransactionno(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
				tempBean.setInceptiobdate(tempMap.get("ADATE")==null?"":tempMap.get("ADATE").toString());
				tempBean.setNetdue(tempMap.get("NETDUE")==null?"":tempMap.get("NETDUE").toString());
				tempBean.setPayamount(tempMap.get("PAID_AMOUNT_OC")==null?"":tempMap.get("PAID_AMOUNT_OC").toString());
				tempBean.setAccPremium(tempMap.get("ACC_PREMIUM")==null?"":tempMap.get("ACC_PREMIUM").toString());
				tempBean.setAccClaim(tempMap.get("ACC_CLAIM")==null?"":tempMap.get("ACC_CLAIM").toString());
				tempBean.setCheckYN(tempMap.get("CHECKYN")==null?"":tempMap.get("CHECKYN").toString());
				tempBean.setCheckPC(tempMap.get("BUSINESS_TYPE")==null?"":tempMap.get("BUSINESS_TYPE").toString());
				tempBean.setSubClass(tempMap.get("DEPT_ID")==null?"":tempMap.get("DEPT_ID").toString());
				tempBean.setProposalNo(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
				payList.add(tempBean);
			}
		}
		catch(Exception exception) {
			log.debug("Exception @ { " + exception + " } ");
		}
		return payList;
	}

	public String getAmend(GetTransContractReq req) {

		String	AmendDate="";
		try{

		String query="GET_AMENDMENT_DATE";
		log.info("Select Query ==> " + query);
		log.info("Args[0]==> " + req.getSerialno());
		List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{req.getSerialno()});
		
		
		if (!CollectionUtils.isEmpty(list)) {
			AmendDate = ((list.get(0).get("AMENDMENT_DATE") == null ? ""
					: list.get(0).get("AMENDMENT_DATE").toString()));
		}
		

		System.out.println(AmendDate);
		}catch(Exception e){
		log.debug("Exception @ {" + e + "}");	
		}

		return AmendDate;
		}
		public String getTrans(GetTransContractReq req) {
		String transDate="";
		try{
		String query="GET_TRANSACTION_DATE";
		log.info("Select Query ==> " + query);
		log.info("Args[0]==> " + req.getPolicyno());
		List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{req.getPolicyno()});
		
		if (!CollectionUtils.isEmpty(list)) {
			transDate = ((list.get(0).get("TRANS_DATE") == null ? ""
					: list.get(0).get("TRANS_DATE").toString()));
		}
		System.out.println(transDate);
		}catch(Exception e){
		log.debug("Exception @ {" + e + "}");	
		}

		return transDate;
		}
		@Override
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


	}
