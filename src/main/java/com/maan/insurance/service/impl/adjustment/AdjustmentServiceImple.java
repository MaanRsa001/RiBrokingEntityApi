package com.maan.insurance.service.impl.adjustment;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.treasury.TtrnPaymentReceiptDetails;
import com.maan.insurance.jpa.repository.treasury.TtrnAllocatedTransactionRepository;
import com.maan.insurance.jpa.repository.treasury.TtrnPaymentReceiptDetailsRepository;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.repository.RskPremiumDetailsRepository;
import com.maan.insurance.model.repository.TtrnClaimPaymentRepository;
import com.maan.insurance.model.req.adjustment.AdjustmentViewReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentDoneListReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentListReq;
import com.maan.insurance.model.req.adjustment.InsertReverseReq;
import com.maan.insurance.model.req.adjustment.TransactionNoListReq;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes1;
import com.maan.insurance.model.res.adjustment.AdjustmentViewResponse;
import com.maan.insurance.model.res.adjustment.GetAdjustmentDoneListRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentDoneListRes1;
import com.maan.insurance.model.res.adjustment.GetAdjustmentListRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentListRes1;
import com.maan.insurance.model.res.adjustment.GetAdjustmentListResponse;
import com.maan.insurance.model.res.adjustment.GetAdjustmentPayRecListRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentPayRecListRes1;
import com.maan.insurance.model.res.adjustment.GetAdjustmentPayRecListResponse;
import com.maan.insurance.model.res.adjustment.InsertReverseRes;
import com.maan.insurance.model.res.adjustment.InsertReverseResponse;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.adjustment.AdjustmentService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;

@Service
public class AdjustmentServiceImple implements AdjustmentService {
	private Logger log = LogManager.getLogger(AdjustmentServiceImple.class);
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;


	@PersistenceContext
	private EntityManager em;
	@Autowired
	private TtrnAllocatedTransactionRepository allocRepo;
	@Autowired
	private RskPremiumDetailsRepository pdRepo;
	@Autowired
	private TtrnClaimPaymentRepository cpRepo;
	@Autowired
	private TtrnPaymentReceiptDetailsRepository prdRepo;
	@Autowired
	private AdjustmentCustomRepository adjustmentCustomRepository;

	private Properties prop = new Properties();
	private Query query1 = null;

	public AdjustmentServiceImple() {
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
			if (inputStream != null) {
				prop.load(inputStream);
			}

		} catch (Exception e) {
			log.info(e);
		}
	}

	@Override
	public GetAdjustmentDoneListRes getAdjustmentDoneList(GetAdjustmentDoneListReq bean) {
		GetAdjustmentDoneListRes response = new GetAdjustmentDoneListRes();
		List<GetAdjustmentDoneListRes1> adjustmentList = new ArrayList<GetAdjustmentDoneListRes1>();
		try {
			// ADJUSTMENT_GETALLOTRANSDTLS_LIST
			String args[] = new String[1];
			args[0] = bean.getBranchCode();

			String query = "adjustment.list.allocated"; //FROM TABLE (fn_adjustment_type (?)),XMLTABLE
			String qutext = prop.getProperty(query);
			if (bean.getSearchBy() != null) {
				if ("1".equalsIgnoreCase(bean.getSearchBy())) {
					qutext += " where SNO like '%" + bean.getSearchValue() + "%'";
				} else if ("2".equalsIgnoreCase(bean.getSearchBy())) {
					qutext += " where UPPER(CEDINGCOMPANY) like UPPER('%" + bean.getSearchValue() + "%')";
				} else if ("3".equalsIgnoreCase(bean.getSearchBy())) {
					qutext += " where UPPER(BROKERNAME) like UPPER('%" + bean.getSearchValue() + "%')";
				} else if ("4".equalsIgnoreCase(bean.getSearchBy())) {
					qutext += " where UPPER(TYPE) like UPPER('%" + bean.getSearchValue() + "%')";
				}
			}
			List<Map<String, Object>> list = new ArrayList<>();
			query1 = queryImpl.setQueryProp(qutext, args);
			query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			try {
				list = query1.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					GetAdjustmentDoneListRes1 tempBean = new GetAdjustmentDoneListRes1();
					Map<String, Object> resMap = list.get(i);
					tempBean.setSerialNo(resMap.get("SNO") == null ? "" : resMap.get("SNO").toString());
					tempBean.setBrokerName(resMap.get("BROKERNAME") == null ? "" : resMap.get("BROKERNAME").toString());
					tempBean.setCedingName(
							resMap.get("CEDINGCOMPANY") == null ? "" : resMap.get("CEDINGCOMPANY").toString());
					tempBean.setType(resMap.get("TYPE") == null ? "" : resMap.get("TYPE").toString());
					tempBean.setCurrencyName(
							resMap.get("CURRENCY_NAME") == null ? "" : resMap.get("CURRENCY_NAME").toString());
					tempBean.setAdjustType(
							resMap.get("ADJUSTMENT_TYPE") == null ? "" : resMap.get("ADJUSTMENT_TYPE").toString());
					tempBean.setPayamount(
							resMap.get("PAIDAMOUNT") == null ? "" : fm.formatter(resMap.get("PAIDAMOUNT").toString()));
					tempBean.setStatus(resMap.get("status") == null ? "" : resMap.get("status").toString());
					adjustmentList.add(tempBean);
				}
			}
			response.setCommonResponse(adjustmentList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public AdjustmentViewRes adjustmentView(AdjustmentViewReq bean) {
		AdjustmentViewRes response = new AdjustmentViewRes();
		AdjustmentViewResponse res1 = new AdjustmentViewResponse();
		try {
			Double a = 0.0, b = 0.0, c = 0.0;
			List<AdjustmentViewRes1> allocatedList = new ArrayList<AdjustmentViewRes1>();
			// ADJUSTMENT_GETREVERSE_TRANSDTLS, ADJUSTMENT_GETALLOTRANSDTLS
			List<TtrnAllocatedTransaction> list = allocRepo.findBySno(new BigDecimal(bean.getSerialNo()));
			if (list.size() > 0) {
				AdjustmentViewRes1 tempBean;
				for (int i = 0; i < list.size(); i++) {
					tempBean = new AdjustmentViewRes1();
					TtrnAllocatedTransaction resMap = list.get(i);
					tempBean.setSerialNo(resMap.getSno() == null ? "" : resMap.getSno().toString());
					tempBean.setAllocateddate(
							resMap.getInceptionDate() == null ? "" : resMap.getInceptionDate().toString());
					tempBean.setTransactionNo(
							resMap.getTransactionNo() == null ? "" : resMap.getTransactionNo().toString());
					tempBean.setContractNo(resMap.getContractNo() == null ? "" : resMap.getContractNo().toString());
					tempBean.setProductName(resMap.getProductName() == null ? "" : resMap.getProductName().toString());
					tempBean.setType(resMap.getType() == null ? "" : resMap.getType().toString());
					if ("ReverseInsert".equalsIgnoreCase(bean.getMode()) || "R".equalsIgnoreCase(bean.getStatus())) {
						tempBean.setPayamount(resMap.getReversalAmount() == null ? ""
								: fm.formatter(resMap.getReversalAmount().toString()));
					} else {
						tempBean.setPayamount(
								resMap.getPaidAmount() == null ? "" : fm.formatter(resMap.getPaidAmount().toString()));
					}
					tempBean.setCurrencyId(resMap.getCurrencyId() == null ? "" : resMap.getCurrencyId().toString());
					tempBean.setReceiptNo(resMap.getReceiptNo() == null ? "" : resMap.getReceiptNo().toString());
					if ("ReverseInsert".equalsIgnoreCase(bean.getMode()) || "R".equalsIgnoreCase(bean.getStatus())) {
						if (tempBean.getType().equals("P")) {
							a = a + Double.parseDouble(
									resMap.getReversalAmount() == null ? "0.0" : resMap.getReversalAmount().toString());

						} else if (tempBean.getType().equals("C")) {
							b = b + Double.parseDouble(
									resMap.getReversalAmount() == null ? "0.0" : resMap.getReversalAmount().toString());
						} else if (tempBean.getType().equals("PT")) {
							a = a + Double.parseDouble(
									resMap.getReversalAmount() == null ? "0.0" : resMap.getReversalAmount().toString());
						} else if (tempBean.getType().equals("RT")) {
							b = b + Double.parseDouble(
									resMap.getReversalAmount() == null ? "0.0" : resMap.getReversalAmount().toString());
						}
					} else {
						if (tempBean.getType().equals("P")) {
							a = a + Double.parseDouble(
									resMap.getPaidAmount() == null ? "0.0" : resMap.getPaidAmount().toString());

						} else if (tempBean.getType().equals("C")) {
							b = b + Double.parseDouble(
									resMap.getPaidAmount() == null ? "0.0" : resMap.getPaidAmount().toString());
						} else if (tempBean.getType().equals("PT")) {
							a = a + Double.parseDouble(
									resMap.getPaidAmount() == null ? "v" : resMap.getPaidAmount().toString());

						} else if (tempBean.getType().equals("RT")) {
							b = b + Double.parseDouble(
									resMap.getPaidAmount() == null ? "0.0" : resMap.getPaidAmount().toString());
						}
					}
					if (resMap.getAdjustmentType() == null) {
						tempBean.setAdjustType("");
					} else if ("R".equalsIgnoreCase(resMap.getAdjustmentType())) {
						tempBean.setAdjustType("Round Off");
					} else if ("W".equalsIgnoreCase(resMap.getAdjustmentType())) {
						tempBean.setAdjustType("Write Off");
					} else {
						tempBean.setAdjustType(resMap.getAdjustmentType());
					}

					// PAYMENT_SELECT_GETSELCURRENCY
					CriteriaBuilder cb = em.getCriteriaBuilder();
					CriteriaQuery<String> query = cb.createQuery(String.class);

					//  like table name
					Root<CurrencyMaster> pm = query.from(CurrencyMaster.class);

					//  Select
					query.select(pm.get("shortName"));

					//  MAXAmend ID
					Subquery<Long> amend = query.subquery(Long.class);
					Root<CurrencyMaster> pms = amend.from(CurrencyMaster.class);
					amend.select(cb.max(pms.get("amendId")));
					Predicate a1 = cb.equal(pm.get("currencyId"), pms.get("currencyId"));
					Predicate a2 = cb.equal(pm.get("branchCode"), pms.get("branchCode"));
					amend.where(a1, a2);

					//  Where
					Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
					Predicate n2 = cb.equal(pm.get("currencyId"), tempBean.getCurrencyId());
					Predicate n3 = cb.equal(pm.get("amendId"), amend);
					query.where(n1, n2, n3);

					//  Get Result
					TypedQuery<String> res = em.createQuery(query);
					List<String> list1 = res.getResultList();
					if (list1.size() > 0) {
						tempBean.setCurrencyName(list1.get(0));
					}
					allocatedList.add(tempBean);
				}
			}
			res1.setAllocatedList(allocatedList);
			res1.setUnUtilizedAmt(fm.formatter(Double.toString(a - b)));
			response.setCommonResponse(res1);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

//	@Override
//	public GetAdjustmentListRes getAdjustmentList(GetAdjustmentListReq bean) {
//		GetAdjustmentListRes response = new GetAdjustmentListRes();
//		Double a = 0.0, b = 0.0, c = 0.0;
//		GetAdjustmentListResponse res1 = new GetAdjustmentListResponse();
//		List<GetAdjustmentListRes1> adjustmentList = new ArrayList<GetAdjustmentListRes1>();
//		try {
//			String qutext = "";
//			String query = "";
//			String[] obj = null;
//			if (bean.getTest().equals("ALL")) {
//				obj = new String[8];
//				query = "adjutment.list"; //union all
//				qutext = prop.getProperty(query);
//				if (bean.getAmountType().equals("1")) {
//					obj = dropDowmImpl.getIncObjectArray(obj, new String[] { (bean.getAmount()) });
//					qutext += " where pending_amount=? ORDER BY   CONTRACT_NO, TRANSACTION_NO";
//				} else if (bean.getAmountType().equals("2")) {
//					obj = dropDowmImpl.getIncObjectArray(obj, new String[] { (bean.getAmount()) });
//					qutext += " where pending_amount<=? ORDER BY   CONTRACT_NO, TRANSACTION_NO";
//				} else if (bean.getAmountType().equals("3")) {
//					obj = dropDowmImpl.getIncObjectArray(obj, new String[] { (bean.getAmount()) });
//					qutext += " where pending_amount>=? ORDER BY   CONTRACT_NO, TRANSACTION_NO";
//				}
//				obj[0] = bean.getBranchCode();
//				obj[1] = bean.getCurrencyId();
//				obj[2] = bean.getBrokerId();
//				if ("63".equalsIgnoreCase(bean.getBrokerId()))
//					obj[3] = bean.getCedingId();
//				else
//					obj[3] = bean.getBrokerId();
//				obj[4] = bean.getBranchCode();
//				obj[5] = bean.getCurrencyId();
//				obj[6] = bean.getBrokerId();
//				if ("63".equalsIgnoreCase(bean.getBrokerId()))
//					obj[7] = bean.getCedingId();
//				else
//					obj[7] = bean.getBrokerId();
//
//			} else {
//				obj = new String[4];
//				query = "adjustment.list.ind.transactionCP"; //select * from table(SPLIT_TEXT_FN(?)
//				qutext = prop.getProperty(query);
//				obj[0] = bean.getBranchCode();
//				obj[1] = bean.getTransactionNo();
//				obj[2] = bean.getBranchCode();
//				obj[3] = bean.getTransactionNo();
//
//			}
//			List<Map<String, Object>> list = new ArrayList<>();
//			query1 = queryImpl.setQueryProp(qutext, obj);
//			query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//			try {
//				list = query1.getResultList();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			for (int i = 0; i < list.size(); i++) {
//				Map map = (Map) list.get(i);
//				GetAdjustmentListRes1 bean1 = new GetAdjustmentListRes1();
//				bean1.setTransactionNo(map.get("TRANSACTION_NO") == null ? "" : map.get("TRANSACTION_NO").toString());
//				bean1.setTransactionDate(map.get("ADATE") == null ? "" : map.get("ADATE").toString());
//				bean1.setContractNo(map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
//				bean1.setLayerNo(map.get("LAYER_NO") == null ? "" : map.get("LAYER_NO").toString());
//				bean1.setBrokerName(map.get("BROKER_NAME") == null ? "" : map.get("BROKER_NAME").toString());
//				bean1.setCedingName(
//						map.get("CEDING_COMPANY_NAME") == null ? "" : map.get("CEDING_COMPANY_NAME").toString());
//				bean1.setCurrency(map.get("CURRENCY_NAME") == null ? "" : map.get("CURRENCY_NAME").toString());
//				bean1.setPendingAmount(
//						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
//				bean1.setAdjustAmount(
//						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
//				bean1.setType(map.get("BUSINESS_TYPE") == null ? "" : map.get("BUSINESS_TYPE").toString());
//				bean1.setProductName(map.get("PRODUCT_NAME") == null ? "" : map.get("PRODUCT_NAME").toString());
//				bean1.setCurrencyId(map.get("CURRENCY_ID") == null ? "" : map.get("CURRENCY_ID").toString());
//				bean1.setSubClass(map.get("DEPT_ID") == null ? "" : map.get("DEPT_ID").toString());
//				bean1.setProposlaNo(map.get("PROPOSAL_NO") == null ? "" : map.get("PROPOSAL_NO").toString());
//				bean1.setAdjustType(bean.getAdjustType());
//				if (bean1.getType().equals("Premium")) {
//					a = a + Double.parseDouble(
//							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
//
//				} else {
//					b = b + Double.parseDouble(
//							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
//				}
////				List<TransactionNoListReq> filterTrack = bean.getTransactionNoListReq().stream().filter( o -> bean1.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
////				if(!CollectionUtils.isEmpty(filterTrack)) {
////					String string= filterTrack.get(0).getTransactionNo();
////					String[] st=string.split("~");
////					bean1.setAdjustType(st[1]);
////					bean1.setCheck(st[2]);
////					bean1.setAdjustAmount(fm.formatter(st[0].replace(",", "")));
////				}
////				
////				else if(!receiveAdjustAmountMap.containsKey(bean1.getTransactionNo()) && bean.getMode()!="list") {
////					bean1.setCheck("false");
////				}
//				adjustmentList.add(bean1);
//			}
//			res1.setAdjustmentList(adjustmentList);
//			res1.setUnUtilizedAmt(Double.toString(a - b));
//			response.setCommonResponse(res1);
//			response.setMessage("Success");
//			response.setIsError(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setMessage("Failed");
//			response.setIsError(true);
//		}
//		return response;
//	}
	@Override
	public GetAdjustmentListRes getAdjustmentList(GetAdjustmentListReq bean) {
		GetAdjustmentListRes response = new GetAdjustmentListRes();
		Double a = 0.0, b = 0.0, c = 0.0;
		GetAdjustmentListResponse res1 = new GetAdjustmentListResponse();
		List<GetAdjustmentListRes1> adjustmentList = new ArrayList<GetAdjustmentListRes1>();
		List<Tuple> comlist = new ArrayList<>();
		List<Tuple> list1 = new ArrayList<>();
		List<Tuple> list2 = new ArrayList<>();
		try {
			String[] obj = null;
			if (bean.getTest().equals("ALL")) {
				obj = new String[4];
				
				obj[0] = bean.getBranchCode();
				obj[1] = bean.getCurrencyId();
				obj[2] = bean.getBrokerId();
				if ("63".equalsIgnoreCase(bean.getBrokerId()))
					obj[3] = bean.getCedingId();
				else
					obj[3] = bean.getBrokerId();
				
				//adjutment.list union all
				list1 = adjustmentCustomRepository.adjutmentList(obj,bean);
				if(list1!=null) {
					for(Tuple data: list1) {
						comlist.add(data);
						}
				}
				//adjutment.list
				list2 = adjustmentCustomRepository.adjutmentListUnion(obj,bean);
				if(list2!=null) {
					for(Tuple data: list2) {
						comlist.add(data);
						}
				}
				
			
			} else {
				//adjustment.list.ind.transactionCP
				list1 = adjustmentCustomRepository.adjustmentListIndTransactionCP(bean.getBranchCode(),bean.getTransactionNo());
				if(list1!=null) {
					for(Tuple data: list1) {
						comlist.add(data);
						}
				}
				//adjustment.list.ind.transactionCP
				list2 = adjustmentCustomRepository.adjustmentListIndTransactionCPUnion(bean.getBranchCode(),bean.getTransactionNo());
				if(list2!=null) {
					for(Tuple data: list2) {
						comlist.add(data);
						}
				}
				
			}
			
			for (int i = 0; i < comlist.size(); i++) {
				Tuple  map = comlist.get(i);
				GetAdjustmentListRes1 bean1 = new GetAdjustmentListRes1();
				bean1.setTransactionNo(map.get("TRANSACTION_NO") == null ? "" : map.get("TRANSACTION_NO").toString());
				bean1.setTransactionDate(map.get("ADATE") == null ? "" : sdf.format(map.get("ADATE")));
				bean1.setContractNo(map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
				bean1.setLayerNo(map.get("LAYER_NO") == null ? "" : map.get("LAYER_NO").toString());
				bean1.setBrokerName(map.get("BROKER_NAME") == null ? "" : map.get("BROKER_NAME").toString());
				bean1.setCedingName(
						map.get("CEDING_COMPANY_NAME") == null ? "" : map.get("CEDING_COMPANY_NAME").toString());
				bean1.setCurrency(map.get("CURRENCY_NAME") == null ? "" : map.get("CURRENCY_NAME").toString());
				bean1.setPendingAmount(
						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
				bean1.setAdjustAmount(
						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
				bean1.setType(map.get("BUSINESS_TYPE") == null ? "" : map.get("BUSINESS_TYPE").toString());
				bean1.setProductName(map.get("PRODUCT_NAME") == null ? "" : map.get("PRODUCT_NAME").toString());
				bean1.setCurrencyId(map.get("CURRENCY_ID") == null ? "" : map.get("CURRENCY_ID").toString());
				bean1.setSubClass(map.get("DEPT_ID") == null ? "" : map.get("DEPT_ID").toString());
				bean1.setProposlaNo(map.get("PROPOSAL_NO") == null ? "" : map.get("PROPOSAL_NO").toString());
				bean1.setAdjustType(bean.getAdjustType());
				if (bean1.getType().equals("Premium")) {
					a = a + Double.parseDouble(
							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
				} else {
					b = b + Double.parseDouble(
							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
				}
//				List<TransactionNoListReq> filterTrack = bean.getTransactionNoListReq().stream().filter( o -> bean1.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()) ).collect(Collectors.toList());
//				if(!CollectionUtils.isEmpty(filterTrack)) {
//					String string= filterTrack.get(0).getTransactionNo();
//					String[] st=string.split("~");
//					bean1.setAdjustType(st[1]);
//					bean1.setCheck(st[2]);
//					bean1.setAdjustAmount(fm.formatter(st[0].replace(",", "")));
//				}
//				
//				else if(!receiveAdjustAmountMap.containsKey(bean1.getTransactionNo()) && bean.getMode()!="list") {
//					bean1.setCheck("false");
//				}
				adjustmentList.add(bean1);
			}
			res1.setAdjustmentList(adjustmentList);
			res1.setUnUtilizedAmt(Double.toString(a - b));
			response.setCommonResponse(res1);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

//	@Override
//	public GetAdjustmentPayRecListRes getAdjustmentPayRecList(GetAdjustmentListReq bean) {
//		GetAdjustmentPayRecListRes response = new GetAdjustmentPayRecListRes();
//		Double a = 0.0, b = 0.0, c = 0.0;
//		List<GetAdjustmentPayRecListRes1> adjustmentList = new ArrayList<GetAdjustmentPayRecListRes1>();
//		GetAdjustmentPayRecListResponse res1 = new GetAdjustmentPayRecListResponse();
//		try {
//			String qutext = "";
//			String query = "";
//			String[] obj = null;
//			if (bean.getTest().equals("ALL")) {
//				obj = new String[4];
//				query = "adjustment.payrec.list";
//				qutext = prop.getProperty(query);
//				if (bean.getAmountType().equals("1")) {
//					obj = dropDowmImpl.getIncObjectArray(obj, new String[] { (bean.getAmount()) });
//					qutext += "  and (Nvl (B.Amount, 0) - Nvl (B.Allocated_Till_Date,0))=? order  by B.Receipt_Sl_No";
//				} else if (bean.getAmountType().equals("2")) {
//					obj = dropDowmImpl.getIncObjectArray(obj, new String[] { (bean.getAmount()) });
//					qutext += "  and (Nvl (B.Amount, 0) - Nvl (B.Allocated_Till_Date,0))<=? order  by B.Receipt_Sl_No";
//				} else if (bean.getAmountType().equals("3")) {
//					obj = dropDowmImpl.getIncObjectArray(obj, new String[] { (bean.getAmount()) });
//					qutext += "  and (Nvl (B.Amount, 0) - Nvl (B.Allocated_Till_Date,0))>=? order  by B.Receipt_Sl_No";
//				}
//				obj[0] = bean.getBranchCode();
//				obj[1] = bean.getCurrencyId();
//				obj[2] = bean.getBrokerId();
//				if ("63".equalsIgnoreCase(bean.getBrokerId()))
//					obj[3] = bean.getCedingId();
//				else
//					obj[3] = bean.getBrokerId();
//
//			} else {
//				obj = new String[2];
//				query = "adjustment.list.ind.transactionRP";
//				qutext = prop.getProperty(query);
//				obj[0] = bean.getBranchCode();
//				obj[1] = bean.getTransactionNo();
//
//			}
//			List<Map<String, Object>> list = new ArrayList<>();
//			query1 = queryImpl.setQueryProp(qutext, obj);
//			query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
//			try {
//				list = query1.getResultList();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			for (int i = 0; i < list.size(); i++) {
//				Map map = (Map) list.get(i);
//				GetAdjustmentPayRecListRes1 bean1 = new GetAdjustmentPayRecListRes1();
//				bean1.setTransactionNo(map.get("TRANSACTION_NO") == null ? "" : map.get("TRANSACTION_NO").toString());
//				bean1.setTransactionDate(map.get("ADATE") == null ? "" : map.get("ADATE").toString());
//				bean1.setContractNo(map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
//				bean1.setLayerNo(map.get("LAYER_NO") == null ? "" : map.get("LAYER_NO").toString());
//				bean1.setBrokerName(map.get("BROKER_NAME") == null ? "" : map.get("BROKER_NAME").toString());
//				bean1.setCedingName(
//						map.get("CEDING_COMPANY_NAME") == null ? "" : map.get("CEDING_COMPANY_NAME").toString());
//				bean1.setCurrency(map.get("CURRENCY_NAME") == null ? "" : map.get("CURRENCY_NAME").toString());
//				bean1.setPendingAmount(
//						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
//				bean1.setAdjustAmount(
//						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
//				bean1.setType(map.get("BUSINESS_TYPE") == null ? "" : map.get("BUSINESS_TYPE").toString());
//				bean1.setProductName(map.get("PRODUCT_NAME") == null ? "" : map.get("PRODUCT_NAME").toString());
//				bean1.setCurrencyId(map.get("CURRENCY_ID") == null ? "" : map.get("CURRENCY_ID").toString());
//				bean1.setSubClass(map.get("SUB_CLASS") == null ? "" : map.get("SUB_CLASS").toString());
//				bean1.setProposlaNo(map.get("PROPOSAL_NO") == null ? "" : map.get("PROPOSAL_NO").toString());
//				if (bean1.getType().equals("Payment")) {
//					a = a + Double.parseDouble(
//							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
//				} else {
//					b = b + Double.parseDouble(
//							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
//				}
//				List<TransactionNoListReq> filterTrack = bean.getTransactionNoListReq().stream()
//						.filter(o -> bean1.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()))
//						.collect(Collectors.toList());
//				if (!CollectionUtils.isEmpty(filterTrack)) {
////					String string=filterTrack.get(0).getTransactionNo();
////					String[] st=string.split("~");
////					bean1.setAdjustType(st[1]);
////					bean1.setCheck(st[2]);
////					bean1.setAdjustAmount(fm.formatter(st[0].replace(",", "")));
//					bean1.setAdjustType(filterTrack.get(0).getAdjustType());
//					bean1.setCheck(filterTrack.get(0).getCheck());
//					bean1.setAdjustAmount(fm.formatter(filterTrack.get(0).getAdjustAmount().replace(",", "")));
//				} else if (CollectionUtils.isEmpty(filterTrack) && bean.getMode() != "list") {
//					bean1.setCheck("false");
//				}
//				bean1.setAdjustType(bean.getAdjustType());
//				adjustmentList.add(bean1);
//			}
//			res1.setAdjustmentList(adjustmentList);
//			res1.setUnUtilizedAmt(Double.toString(a - b));
//			response.setCommonResponse(res1);
//			response.setMessage("Success");
//			response.setIsError(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//			response.setMessage("Failed");
//			response.setIsError(true);
//		}
//		return response;
//	}
	@Override
	public GetAdjustmentPayRecListRes getAdjustmentPayRecList(GetAdjustmentListReq bean) {
		GetAdjustmentPayRecListRes response = new GetAdjustmentPayRecListRes();
		Double a = 0.0, b = 0.0, c = 0.0;
		List<GetAdjustmentPayRecListRes1> adjustmentList = new ArrayList<GetAdjustmentPayRecListRes1>();
		GetAdjustmentPayRecListResponse res1 = new GetAdjustmentPayRecListResponse();
		List<Tuple> list = new ArrayList<>();
		try {
			String[] obj = null;
			if (bean.getTest().equals("ALL")) {
				obj = new String[4];
				
				obj[0] = bean.getBranchCode();
				obj[1] = bean.getCurrencyId();
				obj[2] = bean.getBrokerId();
				if ("63".equalsIgnoreCase(bean.getBrokerId()))
					obj[3] = bean.getCedingId();
				else
					obj[3] = bean.getBrokerId();
				//adjustment.payrec.list
				list = adjustmentCustomRepository.adjustmentPayrecList(obj, bean);

			} else {
				//adjustment.list.ind.transactionRP
				list = adjustmentCustomRepository.adjustmentListIndTransactionRP(bean.getBranchCode(),bean.getTransactionNo());
			}
			

			for (int i = 0; i < list.size(); i++) {
				Tuple map =  list.get(i);
				GetAdjustmentPayRecListRes1 bean1 = new GetAdjustmentPayRecListRes1();
				bean1.setTransactionNo(map.get("TRANSACTION_NO") == null ? "" : map.get("TRANSACTION_NO").toString());
				bean1.setTransactionDate(map.get("ADATE") == null ? "" : sdf.format(map.get("ADATE")));
//				bean1.setContractNo(map.get("CONTRACT_NO") == null ? "" : map.get("CONTRACT_NO").toString());
//				bean1.setLayerNo(map.get("LAYER_NO") == null ? "" : map.get("LAYER_NO").toString());
				bean1.setBrokerName(map.get("BROKER_NAME") == null ? "" : map.get("BROKER_NAME").toString());
				bean1.setCedingName(
						map.get("CEDING_COMPANY_NAME") == null ? "" : map.get("CEDING_COMPANY_NAME").toString());
				bean1.setCurrency(map.get("CURRENCY_NAME") == null ? "" : map.get("CURRENCY_NAME").toString());
				bean1.setPendingAmount(
						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
				bean1.setAdjustAmount(
						map.get("PENDING_AMOUNT") == null ? "" : fm.formatter(map.get("PENDING_AMOUNT").toString()));
				bean1.setType(map.get("BUSINESS_TYPE") == null ? "" : map.get("BUSINESS_TYPE").toString());
		//		bean1.setProductName(map.get("PRODUCT_NAME") == null ? "" : map.get("PRODUCT_NAME").toString());
				bean1.setCurrencyId(map.get("CURRENCY_ID") == null ? "" : map.get("CURRENCY_ID").toString());
			//	bean1.setSubClass(map.get("SUB_CLASS") == null ? "" : map.get("SUB_CLASS").toString());
			//	bean1.setProposlaNo(map.get("PROPOSAL_NO") == null ? "" : map.get("PROPOSAL_NO").toString());
				if (bean1.getType().equals("Payment")) {
					a = a + Double.parseDouble(
							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
				} else {
					b = b + Double.parseDouble(
							map.get("PENDING_AMOUNT") == null ? "0.0" : map.get("PENDING_AMOUNT").toString());
				}
				List<TransactionNoListReq> filterTrack = bean.getTransactionNoListReq().stream()
						.filter(o -> bean1.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()))
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(filterTrack)) {
//					String string=filterTrack.get(0).getTransactionNo();
//					String[] st=string.split("~");
//					bean1.setAdjustType(st[1]);
//					bean1.setCheck(st[2]);
//					bean1.setAdjustAmount(fm.formatter(st[0].replace(",", "")));
					bean1.setAdjustType(filterTrack.get(0).getAdjustType());
					bean1.setCheck(filterTrack.get(0).getCheck());
					bean1.setAdjustAmount(fm.formatter(filterTrack.get(0).getAdjustAmount().replace(",", "")));
				} else if (CollectionUtils.isEmpty(filterTrack) && bean.getMode() != "list") {
					bean1.setCheck("false");
				}
				bean1.setAdjustType(bean.getAdjustType());
				adjustmentList.add(bean1);
			}
			res1.setAdjustmentList(adjustmentList);
			res1.setUnUtilizedAmt(Double.toString(a - b));
			response.setCommonResponse(res1);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}


	@Override
	public CommonResponse addAdjustment(GetAdjustmentListReq req) {
		CommonResponse response = new CommonResponse();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dbf = new SimpleDateFormat("yyyy-MM-dd"); // DB format
		try {
			GetAdjustmentListRes res = getAdjustmentList(req);
			List<GetAdjustmentListRes1> payList = res.getCommonResponse().getAdjustmentList();
			Double a = 0.0, b = 0.0, c = 0.0;
			// PAYMENT_SELECT_GETNEXTSNO
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> query = cb.createQuery(Long.class);

			Root<TtrnAllocatedTransaction> pm = query.from(TtrnAllocatedTransaction.class);

			query.multiselect(cb.max(pm.get("sno")));

			TypedQuery<Long> result = em.createQuery(query);
			List<Long> list = result.getResultList();
			String serialNo = "";
			if (list.size() > 0 && list != null) {
				serialNo = list.get(0) == null ? "0" : String.valueOf(list.get(0) + 1);
			}

			// bean.setSerialNo(serialNo);

			for (int i = 0; i < payList.size(); i++) {

				GetAdjustmentListRes1 form = payList.get(i);
				List<TransactionNoListReq> filterTrack = req.getTransactionNoListReq().stream()
						.filter(o -> form.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()))
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(filterTrack)) {
					TtrnAllocatedTransaction insert = new TtrnAllocatedTransaction();
					insert.setSno(new BigDecimal(serialNo));
					;
					insert.setContractNo(form.getContractNo());
					insert.setLayerNo(form.getLayerNo());
					insert.setProductName(form.getProductName());
					insert.setTransactionNo(new BigDecimal(form.getTransactionNo()));
					insert.setInceptionDate(sdf.parse(req.getAdjustmentDate()));

					if ("PREMIUM".equalsIgnoreCase(form.getType())) {
//							String string= filterTrack.get(0).getTransactionNo();
//							String[] st=string.split("~");
						insert.setPaidAmount(new BigDecimal(filterTrack.get(0).getAdjustAmount().replace(",", "")));
						insert.setType("P");
						insert.setAdjustmentType(filterTrack.get(0).getAdjustType());

						// PAYMENT_UPDATE_RSKPREMALLODTLS
						RskPremiumDetails entity = pdRepo.findByContractNoAndTransactionNo(
								new BigDecimal(form.getContractNo()), new BigDecimal(form.getTransactionNo()));
						String allocatedTillDate = "";
						if (entity != null) {
							allocatedTillDate = entity.getAllocatedTillDate() == null ? "0"
									: entity.getAllocatedTillDate()
											+ filterTrack.get(0).getAdjustAmount().replace(",", "");
							entity.setAllocatedTillDate(new BigDecimal(allocatedTillDate));
							entity.setLoginId(req.getLoginId());
							entity.setBranchCode(req.getBranchCode());
							entity.setContractNo(new BigDecimal(form.getContractNo()));
							entity.setTransactionNo(new BigDecimal(form.getTransactionNo()));
							pdRepo.save(entity);
						}
						// PAYMENT_UPDATE_PREMSETSTAUS
						List<RskPremiumDetails> entity1 = pdRepo
								.findByLoginIdAndBranchCodeAndContractNoAndTransactionNo(req.getLoginId(),
										req.getBranchCode(), new BigDecimal(form.getContractNo()),
										new BigDecimal(form.getTransactionNo()));
						if (entity1.size() > 0) {
							RskPremiumDetails update = null;
							for (int j = 0; j < entity1.size(); j++) {
								update = entity1.get(j);
								int m = (update.getNetdueOc() == null ? 0 : update.getNetdueOc().intValue())
										- (update.getAllocatedTillDate() == null ? 0
												: update.getAllocatedTillDate().intValue());

								if (m == 0) {
									update.setSettlementStatus("Allocated");
								}
							}
							pdRepo.save(update);
						}

						a = a + Double.parseDouble(filterTrack.get(0).getAdjustAmount().replace(",", ""));
					} else if ("CLAIM".equalsIgnoreCase(form.getType())) {
//							String string= filterTrack.get(i).getTransactionNo();
//							String[] st=string.split("~");
						insert.setPaidAmount(new BigDecimal(filterTrack.get(0).getAdjustAmount().replace(",", "")));
						insert.setType("C");
						insert.setAdjustmentType(filterTrack.get(0).getAdjustType());

						// PAYMENT_UPDATE_CLAIMPYMTALLODTLS
						TtrnClaimPayment entity = cpRepo.findByContractNoAndClaimPaymentNo(form.getContractNo(),
								new BigDecimal(form.getTransactionNo()));
						if (entity != null) {
							int val = entity.getAllocatedTillDate() == null ? 0
									: entity.getAllocatedTillDate().intValue()
											+ Integer.valueOf(filterTrack.get(0).getAdjustAmount() == null ? "0"
													: filterTrack.get(0).getAdjustAmount());

							entity.setAllocatedTillDate(new BigDecimal(val));
							entity.setSysDate(new Date());
							entity.setLoginId(req.getLoginId());
							entity.setBranchCode(req.getBranchCode());
							cpRepo.save(entity);
						}

						// PAYMENT_UPDATE_CLAIMSETSTAUS
						TtrnClaimPayment entity2 = cpRepo.findByContractNoAndClaimPaymentNo(form.getContractNo(),
								new BigDecimal(form.getTransactionNo()));
						if (entity2 != null) {
							int m = (entity2.getPaidAmountOc() == null ? 0 : entity2.getPaidAmountOc().intValue())
									- (entity2.getAllocatedTillDate() == null ? 0
											: entity2.getAllocatedTillDate().intValue());
							if (m == 0) {
								entity2.setSettlementStatus("Allocated");
								entity2.setSysDate(new Date());
								entity2.setLoginId(req.getLoginId());
								entity2.setBranchCode(req.getBranchCode());
								cpRepo.save(entity2);
								;
							}
						}

						b = b + Double.parseDouble(filterTrack.get(0).getAdjustAmount().replace(",", ""));
					}
					insert.setStatus("Y");
					insert.setAmendId(BigDecimal.ZERO);
					insert.setCurrencyId(new BigDecimal(form.getCurrencyId()));
					insert.setLoginId(req.getLoginId());
					insert.setBranchCode(req.getBranchCode());
					insert.setSubClass(form.getSubClass());
					insert.setProposalNo(form.getProposlaNo());
					allocRepo.save(insert);
					// ADJUSTMENT_INSERT
				}
			}
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public CommonResponse addAdjustmentPayRec(GetAdjustmentListReq bean) {
		CommonResponse response = new CommonResponse();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			GetAdjustmentPayRecListRes res = getAdjustmentPayRecList(bean);
			List<GetAdjustmentPayRecListRes1> payList = res.getCommonResponse().getAdjustmentList();

			Double a = 0.0, b = 0.0, c = 0.0;
			// PAYMENT_SELECT_GETNEXTSNO
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Long> query = cb.createQuery(Long.class);

			Root<TtrnAllocatedTransaction> pm = query.from(TtrnAllocatedTransaction.class);

			query.multiselect(cb.max(pm.get("sno")));

			TypedQuery<Long> result = em.createQuery(query);
			List<Long> list = result.getResultList();
			String serialNo = "";
			if (list.size() > 0 && list != null) {
				serialNo = list.get(0) == null ? "0" : String.valueOf(list.get(0) + 1);
			}
			// bean.setSerialNo(serialNo);

			for (int i = 0; i < payList.size(); i++) {
				GetAdjustmentPayRecListRes1 form = payList.get(i);
				List<TransactionNoListReq> filterTrack = bean.getTransactionNoListReq().stream()
						.filter(o -> form.getTransactionNo().equalsIgnoreCase(o.getTransactionNo()))
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(filterTrack)) {
//					String string=receiveAdjustAmountMap.get(form.getTransactionNo());
//					String[] st=string.split("~");
					// PAYMENT_UPDATE_ALLOTRANDTLS
					CriteriaQuery<TtrnPaymentReceiptDetails> query1 = cb.createQuery(TtrnPaymentReceiptDetails.class);

					Root<TtrnPaymentReceiptDetails> rd = query1.from(TtrnPaymentReceiptDetails.class);

					//  Select
					query1.select(rd);

					//  amend
					Subquery<Long> amend = query1.subquery(Long.class);
					Root<TtrnPaymentReceiptDetails> rds = amend.from(TtrnPaymentReceiptDetails.class);
					amend.select(cb.max(rds.get("amendId")));
					Predicate a1 = cb.equal(rds.get("receiptNo"), rd.get("receiptNo"));
					Predicate a2 = cb.equal(rds.get("receiptSlNo"), rd.get("receiptSlNo"));
					amend.where(a1, a2);

					//  Where
					Predicate n1 = cb.equal(rd.get("receiptSlNo"), form.getTransactionNo());
					Predicate n2 = cb.equal(rd.get("currencyId"), form.getCurrencyId());
					Predicate n3 = cb.equal(rd.get("amendId"), amend);
					query1.where(n1, n2, n3);

					//  Get Result
					TypedQuery<TtrnPaymentReceiptDetails> result1 = em.createQuery(query1);
					List<TtrnPaymentReceiptDetails> list1 = result1.getResultList();

					if (list1.size() > 0) {
						TtrnPaymentReceiptDetails entity = list1.get(0);
						int val = entity.getAllocatedTillDate() == null ? 0
								: entity.getAllocatedTillDate().intValue()
										+ Integer.valueOf(filterTrack.get(0).getAdjustAmount() == null ? "0"
												: filterTrack.get(0).getAdjustAmount());
						entity.setAllocatedTillDate(new BigDecimal(val));
						entity.setLoginId(bean.getLoginId());
						;
						entity.setBranchCode(bean.getBranchCode());
						prdRepo.save(entity);
					}
					// ADJUSTMENT_INSERT
					TtrnAllocatedTransaction insert = new TtrnAllocatedTransaction();
					insert.setSno(new BigDecimal(serialNo));
					insert.setContractNo(form.getContractNo());
					insert.setLayerNo(form.getLayerNo());
					insert.setProductName(form.getProductName());

					insert.setInceptionDate(sdf.parse(bean.getAdjustmentDate()));
					insert.setPaidAmount(new BigDecimal(filterTrack.get(0).getAdjustAmount().replace(",", "")));
					insert.setType(form.getType().equals("Payment") ? "PT" : "RT");

					insert.setAdjustmentType(filterTrack.get(0).getAdjustType());
					insert.setStatus("Y");
					insert.setAmendId(BigDecimal.ZERO);
					insert.setReceiptNo(new BigDecimal(form.getTransactionNo()));
					;
					insert.setCurrencyId(new BigDecimal(form.getCurrencyId()));
					insert.setLoginId(bean.getLoginId());
					insert.setBranchCode(bean.getBranchCode());
					insert.setSubClass(form.getSubClass());
					insert.setProposalNo(form.getProposlaNo());
					allocRepo.save(insert);

				}
			}
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public InsertReverseRes insertReverse(InsertReverseReq bean) {
		InsertReverseRes response = new InsertReverseRes();
		List<InsertReverseResponse> payList = new ArrayList<InsertReverseResponse>();
		String curencyId = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Double a = 0.0, b = 0.0, c = 0.0;
			// ADJUSTMENT_GETALLOTRANSDTLS
			List<TtrnAllocatedTransaction> list = allocRepo.findBySno(new BigDecimal(bean.getSerialNo()));
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					InsertReverseResponse pay = new InsertReverseResponse();
					TtrnAllocatedTransaction resMap = list.get(i);
					curencyId = resMap.getCurrencyId() == null ? "" : resMap.getCurrencyId().toString();
					if ("Y".equalsIgnoreCase(resMap.getStatus() == null ? "" : resMap.getStatus().toString())) {

						pay.setSerialNo(resMap.getSno() == null ? "" : resMap.getSno().toString());
						pay.setAllocateddate(
								resMap.getInceptionDate() == null ? "" : resMap.getInceptionDate().toString());
						// PAYMENT_SELECT_GETSELCURRENCY
						CriteriaBuilder cb = em.getCriteriaBuilder();
						CriteriaQuery<String> query = cb.createQuery(String.class);

						Root<CurrencyMaster> pm = query.from(CurrencyMaster.class);

						query.select(pm.get("shortName"));

						Subquery<Long> amend = query.subquery(Long.class);
						Root<CurrencyMaster> pms = amend.from(CurrencyMaster.class);
						amend.select(cb.max(pms.get("amendId")));
						Predicate a1 = cb.equal(pm.get("currencyId"), pms.get("currencyId"));
						Predicate a2 = cb.equal(pm.get("branchCode"), pms.get("branchCode"));
						amend.where(a1, a2);

						Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
						Predicate n2 = cb.equal(pm.get("currencyId"),
								resMap.getCurrencyId() == null ? "" : resMap.getCurrencyId().toString());
						Predicate n3 = cb.equal(pm.get("amendId"), amend);
						query.where(n1, n2, n3);

						TypedQuery<String> res = em.createQuery(query);
						List<String> list1 = res.getResultList();
						if (list1.size() > 0) {
							pay.setCurrency(list1.get(0));
						}
						pay.setTransactionNo(
								resMap.getTransactionNo() == null ? "" : resMap.getTransactionNo().toString());
						pay.setContractNo(resMap.getContractNo() == null ? "" : resMap.getContractNo().toString());
						pay.setProductName(resMap.getProductName() == null ? "" : resMap.getProductName().toString());
						pay.setType(resMap.getType() == null ? "" : resMap.getType().toString());
						pay.setReceiptCheck(resMap.getReceiptNo() == null ? "" : resMap.getReceiptNo().toString());

						// PAYMENT_UPDATE_ALLOCATEDDTLS1,PAYMENT_UPDATE_ALLOCATEDDTLS

						TtrnAllocatedTransaction entity = null;
						if (pay.getReceiptCheck().equalsIgnoreCase("")) {
							entity = allocRepo.findByTransactionNoAndSno(
									new BigDecimal(resMap.getTransactionNo() == null ? ""
											: resMap.getTransactionNo().toString()),
									new BigDecimal(resMap.getSno() == null ? "" : resMap.getSno().toString()));
						} else {
							entity = allocRepo.findByReceiptNoAndSno(
									new BigDecimal(
											resMap.getReceiptNo() == null ? "" : resMap.getReceiptNo().toString()),
									new BigDecimal(resMap.getSno() == null ? "" : resMap.getSno().toString()));
						}
						if (entity != null) {
							entity.setStatus("R");
							entity.setReversalDate(sdf.parse(bean.getReverseDate()));
							entity.setReversalAmount(resMap.getReversalAmount() == null ? BigDecimal.ZERO
									: new BigDecimal(resMap.getReversalAmount().toString()));
							entity.setPaidAmount(BigDecimal.ZERO);
							entity.setLoginId(bean.getLoginId());
							entity.setBranchCode(bean.getBranchCode());
							entity.setSysDate(new Date());
							allocRepo.save(entity);
						}
					
						if ("P".equalsIgnoreCase(resMap.getType() == null ? "" : resMap.getType().toString())) {
							// PAYMENT_UPDATE_RSKPREMDTLS1
							RskPremiumDetails entity1 = pdRepo.findByContractNoAndTransactionNo(
									new BigDecimal(
											resMap.getContractNo() == null ? "" : resMap.getContractNo().toString()),
									new BigDecimal(resMap.getTransactionNo() == null ? ""
											: resMap.getTransactionNo().toString()));
							if (entity1 != null) {
								entity1.setSettlementStatus("Pending");
								int val = entity1.getAllocatedTillDate() == null ? 0
										: entity1.getAllocatedTillDate().intValue()
												- Integer.valueOf(resMap.getPaidAmount() == null ? "0"
														: resMap.getPaidAmount().toString());
								entity1.setAllocatedTillDate(new BigDecimal(val));
								entity1.setLoginId(bean.getLoginId());
								entity1.setBranchCode(bean.getBranchCode());
								entity1.setEntryDate(new Date());
								pdRepo.save(entity1);
								// PAYMENT_SELECT_GETRSKPREMDTLS
								int val1 = entity1.getNetdueOc() == null ? 0
										: entity1.getNetdueOc().intValue()
												- Integer.valueOf(entity1.getAllocatedTillDate() == null ? 0
														: entity1.getAllocatedTillDate().intValue());
								pay.setNetdue(String.valueOf(val1));
							}
							a = a + Double.parseDouble(
									resMap.getPaidAmount() == null ? "" : resMap.getPaidAmount().toString());
							
						} else if ("C".equalsIgnoreCase(resMap.getType() == null ? "" : resMap.getType().toString())) {
							// PAYMENT_UPDATE_CLAIMPYMTDTLS1
							TtrnClaimPayment entity1 = cpRepo.findByContractNoAndClaimPaymentNo(
									resMap.getContractNo() == null ? "" : resMap.getContractNo().toString(),
									new BigDecimal(resMap.getTransactionNo() == null ? ""
											: resMap.getTransactionNo().toString()));
							if (entity1 != null) {
								entity1.setSettlementStatus("Pending");
								int val = entity1.getAllocatedTillDate() == null ? 0
										: entity1.getAllocatedTillDate().intValue()
												- Integer.valueOf(resMap.getPaidAmount() == null ? "0"
														: resMap.getPaidAmount().toString());
								entity1.setAllocatedTillDate(new BigDecimal(val));
								entity1.setLoginId(bean.getLoginId());
								entity1.setBranchCode(bean.getBranchCode());
								cpRepo.save(entity1);

								// PAYMENT_SELECT_GETCLAIMPYMTDTLS
								int val1 = entity1.getPaidAmountOc() == null ? 0
										: entity1.getPaidAmountOc().intValue()
												- Integer.valueOf(entity1.getAllocatedTillDate() == null ? 0
														: entity1.getAllocatedTillDate().intValue());
								pay.setPayamount(String.valueOf(val1));

								b = b + Double.parseDouble(
										resMap.getPaidAmount() == null ? "" : resMap.getPaidAmount().toString());
							} else if ("RT"
									.equalsIgnoreCase(resMap.getType() == null ? "" : resMap.getType().toString())
									|| "PT".equalsIgnoreCase(
											resMap.getType() == null ? "" : resMap.getType().toString())) {
								// PAYMENT_UPDATE_PYMTRETDTLS
								CriteriaQuery<TtrnPaymentReceiptDetails> query1 = cb
										.createQuery(TtrnPaymentReceiptDetails.class);

								Root<TtrnPaymentReceiptDetails> rd = query1.from(TtrnPaymentReceiptDetails.class);

								query1.select(rd);

								Subquery<Long> mamend = query1.subquery(Long.class);
								Root<TtrnPaymentReceiptDetails> rds = mamend.from(TtrnPaymentReceiptDetails.class);
								mamend.select(cb.max(rds.get("amendId")));
								Predicate b1 = cb.equal(rds.get("receiptNo"), rd.get("receiptNo"));
								Predicate b2 = cb.equal(rds.get("receiptSlNo"), rd.get("receiptSlNo"));
								mamend.where(b1, b2);

								Predicate m1 = cb.equal(rd.get("receiptSlNo"),
										resMap.getReceiptNo() == null ? "" : resMap.getReceiptNo().toString());
								Predicate m2 = cb.equal(rd.get("currencyId"), curencyId);
								Predicate m3 = cb.equal(rd.get("amendId"), mamend);
								query1.where(m1, m2, m3);

								TypedQuery<TtrnPaymentReceiptDetails> result1 = em.createQuery(query1);
								List<TtrnPaymentReceiptDetails> list2 = result1.getResultList();
								if (list2.size() > 0) {
									TtrnPaymentReceiptDetails update = list2.get(0);
									int val = update.getAllocatedTillDate() == null ? 0
											: update.getAllocatedTillDate().intValue()
													- Integer.valueOf(resMap.getPaidAmount() == null ? "0"
															: resMap.getPaidAmount().toString());
									update.setAllocatedTillDate(new BigDecimal(val));
									update.setLoginId(curencyId);
									update.setBranchCode(curencyId);
									update.setSysDate(new Date());
									prdRepo.save(update);
								}
								// PAYMENT_SELECT_GETPYMTRETDTLS

							}
						}
					}
					payList.add(pay);
				}
			}
			response.setCommonResponse(payList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	public void allocatedDate(InsertReverseReq bean) {
		try {
			// ADJUSMENT_ALLOCATION_PERION_DATE
			List<TtrnAllocatedTransaction> list = allocRepo.findBySno(new BigDecimal(bean.getSerialNo()));
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					TtrnAllocatedTransaction resMap = list.get(i);
					bean.setAllocateddate(resMap.getInceptionDate()==null?"":resMap.getInceptionDate().toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public String GetMaxDate(GetAdjustmentListReq bean) {
		String result="";
		try{
			//ADJUSTMENT_LIST_MAXDATE
			String query= "adjutment.list.maxdate";
			List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{StringUtils.join(bean.getTransDate())});
			if (!CollectionUtils.isEmpty(list)) {
				result = list.get(0).get("MAXDATE") == null ? ""
						: list.get(0).get("MAXDATE").toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}
}
