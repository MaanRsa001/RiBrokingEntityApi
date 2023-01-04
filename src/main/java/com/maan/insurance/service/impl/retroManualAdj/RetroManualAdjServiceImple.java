package com.maan.insurance.service.impl.retroManualAdj;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.jpa.entity.xolpremium.TtrnRetroPrcl;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.repository.RskPremiumDetailsRepository;
import com.maan.insurance.model.repository.TmasBranchMasterRepository;
import com.maan.insurance.model.req.retroManualAdj.InsertPremiumReq;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.retroManualAdj.GetPremiumDetailsRes;
import com.maan.insurance.model.res.retroManualAdj.GetPremiumDetailsRes1;
import com.maan.insurance.model.res.retroManualAdj.GetPremiumDetailsResponse;
import com.maan.insurance.model.res.retroManualAdj.GetRetroDetailsRes;
import com.maan.insurance.model.res.retroManualAdj.GetRetroDetailsRes1;
import com.maan.insurance.model.res.retroManualAdj.GetRetroManualAdjlistRes;
import com.maan.insurance.model.res.retroManualAdj.GetRetroManualAdjlistRes1;
import com.maan.insurance.model.res.retroManualAdj.PremiumEditRes;
import com.maan.insurance.model.res.retroManualAdj.PremiumEditRes1;
import com.maan.insurance.model.res.retroManualAdj.PremiumEditResponse;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.retroManualAdj.RetroManualAdjService;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;

@Service
public class RetroManualAdjServiceImple implements RetroManualAdjService{
	private Logger log = LogManager.getLogger(RetroManualAdjServiceImple.class);

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;

	@Autowired
	private RskPremiumDetailsRepository pdRepo;
	@Autowired
	private TmasBranchMasterRepository bmRepo;
	
	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();
	private Query query1 = null;

	public RetroManualAdjServiceImple() {
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
	public GetRetroManualAdjlistRes getRetroManualAdjlist(String branchCode, String productId) {
		GetRetroManualAdjlistRes response = new GetRetroManualAdjlistRes();
		List<GetRetroManualAdjlistRes1> resList = new ArrayList<GetRetroManualAdjlistRes1>();
		try{
			//GET_RETRO_MANUAL_ADJ_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			// like table name
			Root<TtrnRetroPrcl> pm = query.from(TtrnRetroPrcl.class);
			
			// retroBroker
			Subquery<String> retroBroker = query.subquery(String.class); 
			Root<PersonalInfo> pi = retroBroker.from(PersonalInfo.class);
			retroBroker.select(pi.get("firstName"));
			//maxamend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pi.get("customerId"), pis.get("customerId"));
			Predicate c2 = cb.equal( pi.get("branchCode"), pis.get("branchCode"));
			maxAmend.where(c1,c2);
			Predicate b1 = cb.equal( pm.get("branchCode"), pi.get("branchCode"));
			Predicate b2 = cb.equal( pm.get("retroBroker"), pi.get("customerId"));
			Predicate b3 = cb.equal( pi.get("amendId"), maxAmend);
			retroBroker.where(b1,b2,b3);
			
			// retroCessionaire
			Subquery<String> retroCessionaire = query.subquery(String.class); 
			Root<PersonalInfo> b = retroCessionaire.from(PersonalInfo.class);
			retroCessionaire.select(b.get("companyName"));
			//maxamend
			Subquery<Long> amendPI = query.subquery(Long.class); 
			Root<PersonalInfo> bs = amendPI.from(PersonalInfo.class);
			amendPI.select(cb.max(bs.get("amendId")));
			Predicate y1 = cb.equal( b.get("customerId"), bs.get("customerId"));
			Predicate y2 = cb.equal( b.get("branchCode"), bs.get("branchCode"));
			amendPI.where(y1,y2);
			Predicate x1 = cb.equal( b.get("branchCode"), pm.get("branchCode"));
			Predicate x2 = cb.equal( b.get("customerId"), pm.get("retrocessionaire"));
			Predicate x3 = cb.equal( b.get("amendId"), amendPI);
			retroCessionaire.where(x1,x2,x3);

			// Select
			query.multiselect(pm.get("transactionNo").alias("TRANSACTION_NO"),pm.get("transactionMonthYear").alias("TRANSACTION_MONTH_YEAR"),
					pm.get("retroContractNumber").alias("RETRO_CONTRACT_NUMBER"),pm.get("uwy").alias("UWY"),
					retroBroker.alias("RETRO_BROKER"),retroCessionaire.alias("RETROCESSIONAIRE"),
					pm.get("proposalNo").alias("PROPOSAL_NO")); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRetroPrcl> pms = amend.from(TtrnRetroPrcl.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a3 = cb.equal( pm.get("transactionNo"), pms.get("transactionNo"));
			amend.where(a1,a2,a3);

			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.isNull(pm.get("processId"));
			Predicate n3 = cb.equal(pm.get("productId"), productId); 
			Predicate n4 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3,n4);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			if(list.size()>0) {
				for(Tuple data: list) {
					GetRetroManualAdjlistRes1 res = new GetRetroManualAdjlistRes1();
					res.setProposalNo(data.get("PROPOSAL_NO")==null?"":data.get("PROPOSAL_NO").toString());
					res.setRetroBroker(data.get("RETRO_BROKER")==null?"":data.get("RETRO_BROKER").toString());
					res.setRetroCessionaire(data.get("RETROCESSIONAIRE")==null?"":data.get("RETROCESSIONAIRE").toString());
					res.setRetroContractNumber(data.get("RETRO_CONTRACT_NUMBER")==null?"":data.get("RETRO_CONTRACT_NUMBER").toString());
					res.setTransactionMonthYear(data.get("TRANSACTION_MONTH_YEAR")==null?"":data.get("TRANSACTION_MONTH_YEAR").toString());
					res.setTransactionNo(data.get("TRANSACTION_NO")==null?"":data.get("TRANSACTION_NO").toString());
					res.setUwy(data.get("UWY")==null?"":data.get("UWY").toString());
					resList.add(res);
				}
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
	public PremiumEditRes premiumEdit(String contractNo, String transactionNo, String countryId, String branchCode) {
		PremiumEditRes response = new PremiumEditRes();
		List<PremiumEditRes1> beanList = new ArrayList<PremiumEditRes1>();
		PremiumEditResponse res = new PremiumEditResponse();
		 boolean saveFlag=false;
		 try {
				//GET_RETRO_PREMIUM
			 	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			 	CriteriaQuery<TtrnRetroPrcl> query1 = cb.createQuery(TtrnRetroPrcl.class); 
				Root<TtrnRetroPrcl> pd = query1.from(TtrnRetroPrcl.class);

				// Select
				query1.select(pd);
				
				// maxEndRP 
				Subquery<Long> amend = query1.subquery(Long.class); 
				Root<TtrnRetroPrcl> pds = amend.from(TtrnRetroPrcl.class);
				amend.select(cb.max(pds.get("amendId")));
				Predicate k1 = cb.equal(pds.get("retroContractNumber"),pd.get("retroContractNumber"));
				Predicate k2 = cb.equal(pds.get("transactionNo"),pd.get("transactionNo"));
				amend.where(k1,k2);
		
				// Where
				Predicate r1 = cb.equal(pd.get("retroContractNumber"), contractNo);
				Predicate r2 = cb.equal(pd.get("transactionNo"),transactionNo);
				Predicate r3 = cb.equal(pd.get("amendId"), amend);
				query1.where(r1,r2,r3);
				
				// Get Result
				TypedQuery<TtrnRetroPrcl> result1 = em.createQuery(query1);
				List<TtrnRetroPrcl> list = result1.getResultList();

				for(int i=0;i<list.size();i++){
					TtrnRetroPrcl editPremium = list.get(i);
					PremiumEditRes1 bean = new PremiumEditRes1();
						bean.setTransaction(editPremium.getTransactionMonthYear()==null?"":editPremium.getTransactionMonthYear().toString());
						bean.setCurrencyId(editPremium.getCurrencyId()==null?"":editPremium.getCurrencyId().toString());
						if(null==editPremium.getExchangeRate()){
							GetCommonValueRes dd = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),bean.getTransaction(),countryId, branchCode);
							bean.setExchRate(dd.getCommonResponse());
						}
						else{
						bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.getExchangeRate()==null?"":editPremium.getExchangeRate().toString()));
						}
						bean.setBrokerage(editPremium.getBrokerageAmtOc()==null?"":fm.formatter(editPremium.getBrokerageAmtOc().toString()));
						bean.setTax(editPremium.getTaxAmtOc()==null?"":fm.formatter(editPremium.getTaxAmtOc().toString()));
						bean.setPremiumQuotaShare(editPremium.getPremiumQuotashareOc()==null?"":fm.formatter(editPremium.getPremiumQuotashareOc().toString()));
						bean.setCommissionQuotaShare(editPremium.getCommissionQuotashareOc()==null?"":fm.formatter(editPremium.getCommissionQuotashareOc().toString()));
						bean.setPremiumSurplus(editPremium.getPremiumSurplusOc()==null?"":fm.formatter(editPremium.getPremiumSurplusOc().toString()));
						bean.setCommissionSurplus(editPremium.getCommissionSurplusOc()==null?"":fm.formatter(editPremium.getCommissionSurplusOc().toString()));
						bean.setPremiumportifolioIn(editPremium.getPremiumPortfolioinOc()==null?"":fm.formatter(editPremium.getPremiumPortfolioinOc().toString()));
						bean.setCliamPortfolioin(editPremium.getClaimPortfolioinOc()==null?"":fm.formatter(editPremium.getClaimPortfolioinOc().toString()));
						bean.setPremiumportifolioout(editPremium.getPremiumPortfoliooutOc()==null?"":fm.formatter(editPremium.getPremiumPortfoliooutOc().toString()));
						bean.setLossReserveReleased(editPremium.getLossReserveReleasedOc()==null?"":fm.formatter(editPremium.getLossReserveReleasedOc().toString()));
						bean.setPremiumReserveQuotaShare(editPremium.getPremiumreserveQuotashareOc()==null?"":fm.formatter(editPremium.getPremiumreserveQuotashareOc().toString()));
						bean.setCashLossCredit(editPremium.getCashLossCreditOc()==null?"":fm.formatter(editPremium.getCashLossCreditOc().toString()));
						bean.setLossReserveRetained(editPremium.getLossReserveretainedOc()==null?"":fm.formatter(editPremium.getLossReserveretainedOc().toString()));
						bean.setProfitCommission(editPremium.getProfitCommissionOc()==null?"":fm.formatter(editPremium.getProfitCommissionOc().toString()));
						bean.setCashLossPaid(editPremium.getCashLosspaidOc()==null?"":fm.formatter(editPremium.getCashLosspaidOc().toString()));
						bean.setStatus(editPremium.getStatus()==null?"":editPremium.getStatus().toString());
						bean.setNetDue(fm.formatter(editPremium.getNetDueOc()==null?"0":editPremium.getNetDueOc().toString()));
						bean.setNetdueusd(fm.formatter(editPremium.getNetDueDc()==null?"0":editPremium.getNetDueDc().toString()));
						bean.setClaimspaid(editPremium.getClaimsPaidOc()==null?"":fm.formatter(editPremium.getClaimsPaidOc().toString()));
					    bean.setMdpremium(editPremium.getMDpremiumOc()==null?"":fm.formatter(editPremium.getMDpremiumOc().toString()));
					    bean.setAdjustmentpremium(editPremium.getAdjustmentPremiumOc()==null?"":fm.formatter(editPremium.getAdjustmentPremiumOc().toString()));
					    bean.setRecuirementpremium(editPremium.getRecPremiumOc()==null?"":fm.formatter(editPremium.getRecPremiumOc().toString()));
				//	 	COMMISSION not in query doubt
				    // bean.setCommission(editPremium.get("COMMISSION")==null?"":fm.formatter(editPremium.get("COMMISSION").toString()));
					    bean.setXlCost(editPremium.getXlCostOc()==null?"":fm.formatter(editPremium.getXlCostOc().toString()));
					    bean.setCliamportfolioout(editPremium.getClaimPortfolioOutOc()==null?"":fm.formatter(editPremium.getClaimPortfolioOutOc().toString()));
					    bean.setPremiumReserveReleased(editPremium.getPremiumReserveRealsedOc()==null?"":fm.formatter(editPremium.getPremiumReserveRealsedOc().toString()));
					    bean.setOtherCost(editPremium.getOtherCostOc()==null?"":fm.formatter(editPremium.getOtherCostOc().toString()));
						bean.setRemarks(editPremium.getRemarks()==null?"":editPremium.getRemarks().toString());
						bean.setNetDue(editPremium.getNetDueOc()==null?"":fm.formatter(editPremium.getNetDueOc().toString()));
						bean.setInterest(fm.formatter(editPremium.getInterestOc()==null?"0":editPremium.getInterestOc().toString()));
						bean.setOsClaimsLossUpdateOC(fm.formatter(editPremium.getOsclaimLossupdateOc()==null?"0":editPremium.getOsclaimLossupdateOc().toString()));
						bean.setOverrider(editPremium.getOverriderAmtOc()==null?"":fm.formatter(editPremium.getOverriderAmtOc().toString()));
						bean.setOverriderUSD(editPremium.getOverriderAmtDc()==null?"":fm.formatter(editPremium.getOverriderAmtDc().toString()));
						bean.setAmendmentDate(editPremium.getAmendmentDate()==null?"":editPremium.getAmendmentDate().toString());
	                    bean.setWithHoldingTaxOC(fm.formatter(editPremium.getWithHoldingTaxOc()==null?"":(editPremium.getWithHoldingTaxOc().toString())));
	                    bean.setWithHoldingTaxDC(fm.formatter(editPremium.getWithHoldingTaxDc()==null?"":(editPremium.getWithHoldingTaxDc().toString())));
	                	//	 RI_CESSION not in query doubt
	                   //    bean.setRicession(editPremium.get("RI_CESSION")==null?"":fm.formatter(editPremium.get("RI_CESSION").toString()));
	                    bean.setTaxDedectSource(fm.formatter(editPremium.getTdsOc()==null?"0":editPremium.getTdsOc().toString()));
	    				bean.setTaxDedectSourceDc(fm.formatter(editPremium.getTdsDc()==null?"0":editPremium.getTdsDc().toString()));
	    				bean.setServiceTax(fm.formatter(editPremium.getStOc()==null?"0":editPremium.getStOc().toString()));
	    				bean.setServiceTaxDc(fm.formatter(editPremium.getStDc()==null?"0":editPremium.getStDc().toString()));
	    				bean.setLossParticipation(fm.formatter(editPremium.getLpcOc()==null?"0":editPremium.getLpcOc().toString()));
	    				bean.setLossParticipationDC(fm.formatter(editPremium.getLpcDc()==null?"0":editPremium.getLpcDc().toString()));
						bean.setSlideScaleCom(fm.formatter(editPremium.getScCommOc()==null?"0":editPremium.getScCommOc().toString()));
						bean.setSlideScaleComDC(fm.formatter(editPremium.getScCommDc()==null?"0":editPremium.getScCommDc().toString()));
						bean.setSubProfitId(editPremium.getSpc()==null?"":editPremium.getSpc().toString());
				// not in query doubt
				//		bean.setPrAllocatedAmount(editPremium.get("PRD_ALLOCATED_TILL_DATE")==null?"":fm.formatter(editPremium.get("PRD_ALLOCATED_TILL_DATE").toString()));
				//		bean.setLrAllocatedAmount(editPremium.get("LRD_ALLOCATED_TILL_DATE")==null?"":fm.formatter(editPremium.get("LRD_ALLOCATED_TILL_DATE").toString()));
						bean.setPredepartment(editPremium.getPremiumClass()==null?"":editPremium.getPremiumClass().toString()) ;
						bean.setUwYear(editPremium.getUwy()==null?"":editPremium.getUwy().toString()) ;
						 bean.setReference(editPremium.getReference()==null?"":editPremium.getReference().toString());
						 bean.setBusinessType(editPremium.getBusinessType()==null?"":editPremium.getBusinessType().toString());
						 beanList.add(bean);
					}
				res.setSaveFlag(String.valueOf(saveFlag));	
				res.setPremiumEditRes1(beanList);
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
	public GetPremiumDetailsRes getPremiumDetails(String contractNo, String transactionNo, String branchCode) {
		GetPremiumDetailsRes response = new GetPremiumDetailsRes();
		List<GetPremiumDetailsRes1> beanList = new ArrayList<GetPremiumDetailsRes1>();
		GetPremiumDetailsResponse res = new GetPremiumDetailsResponse();
		try{
			
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			
			//GET_RETRO_PREMIUM
		 	CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnRetroPrcl> pd = query1.from(TtrnRetroPrcl.class);
			
			//GET_RETRO_PREMIUM(TMAS_DEPARTMENT_NAME)
			Subquery<String> name = query1.subquery(String.class); 
			Root<TmasDepartmentMaster> dm = name.from(TmasDepartmentMaster.class);
			name.select(dm.get("tmasDepartmentName"));
			Predicate k1 = cb.equal(dm.get("tmasDepartmentId"),pd.get("premiumClass"));
			Predicate k2 = cb.equal(dm.get("branchCode"), pd.get("branchCode"));
			Predicate k3 = cb.equal(dm.get("tmasProductId"), "4");
			Predicate k4 = cb.equal(dm.get("tmasStatus"), "Y");
			name.where(k1,k2,k3,k4);
			
			// Select
			query1.multiselect(pd.alias("treatyView"),name.alias("tmasDepartmentName"));
			
			// maxEndRP 
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<TtrnRetroPrcl> pds = amend.from(TtrnRetroPrcl.class);
			amend.select(cb.max(pds.get("amendId")));
			Predicate m1 = cb.equal(pds.get("retroContractNumber"),pd.get("retroContractNumber"));
			Predicate m2 = cb.equal(pds.get("transactionNo"),pd.get("transactionNo"));
			amend.where(m1,m2);
	
			// Where
			Predicate r1 = cb.equal(pd.get("retroContractNumber"), contractNo);
			Predicate r2 = cb.equal(pd.get("transactionNo"),transactionNo);
			Predicate r3 = cb.equal(pd.get("amendId"), amend);
			query1.where(r1,r2,r3);
			
			// Get Result
			TypedQuery<Tuple> result1 = em.createQuery(query1);
			List<Tuple> list = result1.getResultList();
			
			for(int i=0;i<list.size();i++){
				TtrnRetroPrcl treatyView=(TtrnRetroPrcl) list.get(i).get("treatyView");
				String name1 = (String) list.get(i).get("tmasDepartmentName");
				GetPremiumDetailsRes1 bean = new GetPremiumDetailsRes1();
					bean.setContNo(treatyView.getContractNo()==null?"":treatyView.getContractNo().toString());
					bean.setTransactionNo(treatyView.getTransactionNo()==null?"":treatyView.getTransactionNo().toString());
					bean.setTransaction(treatyView.getTransactionMonthYear()==null?"":treatyView.getTransactionMonthYear().toString());
					bean.setBrokerage(treatyView.getBrokerageAmtOc()==null?"":fm.formatter(treatyView.getBrokerageAmtOc().toString()));
					bean.setTax(treatyView.getTaxAmtOc()==null?"":fm.formatter(treatyView.getTaxAmtOc().toString()));
					bean.setPremiumQuotaShare(treatyView.getPremiumQuotashareOc()==null?"":fm.formatter(treatyView.getPremiumQuotashareOc().toString()));
					bean.setCommissionQuotaShare(treatyView.getCommissionQuotashareOc()==null?"":fm.formatter(treatyView.getCommissionQuotashareOc().toString()));
					bean.setPremiumSurplus(treatyView.getPremiumSurplusOc()==null?"":fm.formatter(treatyView.getPremiumSurplusOc().toString()));
					bean.setCommissionSurplus(treatyView.getCommissionSurplusOc()==null?"":fm.formatter(treatyView.getCommissionSurplusOc().toString()));
					bean.setPremiumportifolioIn(treatyView.getPremiumPortfolioinOc()==null?"":fm.formatter(treatyView.getPremiumPortfolioinOc().toString()));
					bean.setCliamPortfolioin(treatyView.getClaimPortfolioinOc()==null?"":fm.formatter(treatyView.getClaimPortfolioinOc().toString()));
					bean.setPremiumportifolioout(treatyView.getPremiumPortfoliooutOc()==null?"":fm.formatter(treatyView.getPremiumPortfoliooutOc().toString()));
					bean.setLossReserveReleased(treatyView.getLossReserveReleasedOc()==null?"":fm.formatter(treatyView.getLossReserveReleasedOc().toString()));
					bean.setPremiumReserveQuotaShare(treatyView.getPremiumreserveQuotashareOc()==null?"":fm.formatter(treatyView.getPremiumreserveQuotashareOc().toString()));
					bean.setCashLossCredit(treatyView.getCashLossCreditOc()==null?"":fm.formatter(treatyView.getCashLossCreditOc().toString()));
					bean.setLossReserveRetained(treatyView.getLossReserveretainedOc()==null?"":fm.formatter(treatyView.getLossReserveretainedOc().toString()));
					bean.setProfitCommission(treatyView.getProfitCommissionOc()==null?"":fm.formatter(treatyView.getProfitCommissionOc().toString()));
					bean.setCashLossPaid(treatyView.getCashLosspaidOc()==null?"":fm.formatter(treatyView.getCashLosspaidOc().toString()));
					bean.setClaimsPaid(treatyView.getClaimsPaidOc()==null?"":fm.formatter(treatyView.getClaimsPaidOc().toString()));
					bean.setInceptionDate(treatyView.getEndDate()==null?"":treatyView.getEndDate().toString());
					bean.setXlCost(treatyView.getXlCostOc()==null?"":fm.formatter(treatyView.getXlCostOc().toString()));
					bean.setCliamPortfolioOut(treatyView.getClaimPortfolioOutOc()==null?"":fm.formatter(treatyView.getClaimPortfolioOutOc().toString()));
					bean.setPremiumReserveReleased(treatyView.getPremiumReserveRealsedOc()==null?"":fm.formatter(treatyView.getPremiumReserveRealsedOc().toString()));
					bean.setOtherCost(treatyView.getOtherCostOc()==null?"":fm.formatter(treatyView.getOtherCostOc().toString()));
					bean.setRemarks(treatyView.getRemarks()==null?"":treatyView.getRemarks().toString());
					bean.setInterest(fm.formatter(treatyView.getInterestOc()==null?"0":treatyView.getInterestOc().toString()));
					//not in query doubt
				//	bean.setAccountPeriod(treatyView.get("ACCOUNT_PERIOD_QTR")==null?"":treatyView.get("ACCOUNT_PERIOD_QTR").toString());
				//	bean.setAccount_Period_year(treatyView.get("ACCOUNT_PERIOD_YEAR")==null?"":treatyView.get("ACCOUNT_PERIOD_YEAR").toString());
					bean.setCurrencyId(treatyView.getCurrencyId()==null?"":treatyView.getCurrencyId().toString());
					bean.setBrokerageUsd(fm.formatter(treatyView.getBrokerageAmtDc()==null?"0":treatyView.getBrokerageAmtDc().toString()));
					bean.setTaxUsd(fm.formatter(treatyView.getTaxAmtDc()==null?"0":treatyView.getTaxAmtDc().toString()));
					bean.setPremiumQuotaShareUsd(fm.formatter(treatyView.getPremiumQuotashareDc()==null?"0":treatyView.getPremiumQuotashareDc().toString()));
					bean.setCommsissionQuotaShareUsd(fm.formatter(treatyView.getCommissionQuotashareDc()==null?"0":treatyView.getCommissionQuotashareDc().toString()));
					bean.setPremiumSurplusUsd(fm.formatter(treatyView.getPremiumSurplusDc()==null?"0":treatyView.getPremiumSurplusDc().toString()));
					bean.setComissionSurplusUsd(fm.formatter(treatyView.getCommissionSurplusDc()==null?"0":treatyView.getCommissionSurplusDc().toString()));
					bean.setPremiumPortfolioInUsd(fm.formatter(treatyView.getPremiumPortfolioinDc()==null?"0":treatyView.getPremiumPortfolioinDc().toString()));
					bean.setCliamPortfolioUsd(fm.formatter(treatyView.getClaimPortfolioinDc()==null?"0":treatyView.getClaimPortfolioinDc().toString()));
					bean.setPremiumPortfolioOutUsd(fm.formatter(treatyView.getPremiumPortfoliooutDc()==null?"0":treatyView.getPremiumPortfoliooutDc().toString()));
					bean.setLossReserveReleasedUsd(fm.formatter(treatyView.getLossReserveReleasedDc()==null?"0":treatyView.getLossReserveReleasedDc().toString()));
					bean.setPremiumReserveQuotaShareUsd(fm.formatter(treatyView.getPremiumreserveQuotashareDc()==null?"0":treatyView.getPremiumreserveQuotashareDc().toString()));
					bean.setCashLossCreditUsd(fm.formatter(treatyView.getCashLossCreditDc()==null?"0":treatyView.getCashLossCreditDc().toString()));
					bean.setLossReserveRetainedUsd(fm.formatter(treatyView.getLossReserveretainedDc()==null?"0":treatyView.getLossReserveretainedDc().toString()));
					bean.setProfitCommissionUsd(fm.formatter(treatyView.getProfitCommissionDc()==null?"0":treatyView.getProfitCommissionDc().toString()));
					bean.setCashLossPaidUsd(fm.formatter(treatyView.getCashLosspaidDc()==null?"0":treatyView.getCashLosspaidDc().toString()));
					bean.setClamsPaidUsd(fm.formatter(treatyView.getClaimsPaidDc()==null?"0":treatyView.getClaimsPaidDc().toString()));
					bean.setXlCostUsd(fm.formatter(treatyView.getXlCostDc()==null?"0":treatyView.getXlCostDc().toString()));
					bean.setCliamPortfolioOutUsd(fm.formatter(treatyView.getClaimPortfolioOutDc()==null?"0":treatyView.getClaimPortfolioOutDc().toString()));
					bean.setPremiumReserveReleasedUsd(fm.formatter(treatyView.getPremiumReserveRealsedDc()==null?"0":treatyView.getPremiumReserveRealsedDc().toString()));
					bean.setOtherCostUSD(fm.formatter(treatyView.getOtherCostDc()==null?"0":treatyView.getOtherCostDc().toString()));
					bean.setInterestDC(fm.formatter(treatyView.getInterestDc()==null?"0":treatyView.getInterestDc().toString()));
					bean.setOsClaimsLossUpdateOC(fm.formatter(treatyView.getOsclaimLossupdateOc()==null?"0":treatyView.getOsclaimLossupdateOc().toString()));
					bean.setOsClaimsLossUpdateDC(fm.formatter(treatyView.getOsclaimLossupdateDc()==null?"0":treatyView.getOsclaimLossupdateDc().toString()));
					bean.setOverrider(fm.formatter(treatyView.getOverriderAmtOc()==null?"0":treatyView.getOverriderAmtOc().toString()));
					bean.setOverriderUSD(fm.formatter(treatyView.getOverriderAmtDc()==null?"0":treatyView.getOverriderAmtDc().toString()));
					bean.setAmendmentDate(treatyView.getAmendmentDate()==null?"":treatyView.getAmendmentDate().toString());
	                bean.setWithHoldingTaxOC(fm.formatter(treatyView.getWithHoldingTaxOc()==null?"0":treatyView.getWithHoldingTaxOc().toString()));
	                bean.setWithHoldingTaxDC(fm.formatter(treatyView.getWithHoldingTaxDc()==null?"0":treatyView.getWithHoldingTaxDc().toString()));
	                //not in query doubt
	                // bean.setDueDate(treatyView.get("due_date")==null?"":treatyView.get("due_date").toString());
	                bean.setTaxDedectSource(fm.formatter(treatyView.getTdsOc()==null?"0":treatyView.getTdsOc().toString()));
					bean.setTaxDedectSourceDc(fm.formatter(treatyView.getTdsDc()==null?"0":treatyView.getTdsDc().toString()));
					bean.setServiceTax(fm.formatter(treatyView.getStOc()==null?"0":treatyView.getStOc().toString()));
					bean.setServiceTaxDc(fm.formatter(treatyView.getStDc()==null?"0":treatyView.getStDc().toString()));
					bean.setLossParticipation(fm.formatter(treatyView.getLpcOc()==null?"0":treatyView.getLpcOc().toString()));
					bean.setLossParticipationDC(fm.formatter(treatyView.getLpcDc()==null?"0":treatyView.getLpcDc().toString()));
					bean.setSlideScaleCom(fm.formatter(treatyView.getScCommOc()==null?"0":treatyView.getScCommOc().toString()));
					bean.setSlideScaleComDC(fm.formatter(treatyView.getScCommDc()==null?"0":treatyView.getScCommDc().toString()));
					bean.setExchRate(dropDowmImpl.exchRateFormat(treatyView.getExchangeRate()==null?"0":treatyView.getExchangeRate().toString()));
					Double totalOc = Double.parseDouble(bean.getPremiumQuotaShare())+Double.parseDouble(bean.getPremiumportifolioIn())+Double.parseDouble(bean.getCliamPortfolioin())
					+Double.parseDouble(bean.getPremiumReserveReleased())+Double.parseDouble(bean.getLossReserveReleased())+Double.parseDouble(bean.getInterest())
					+Double.parseDouble(bean.getCashLossCredit())+Double.parseDouble(bean.getTaxDedectSource())+Double.parseDouble(bean.getServiceTax())
					+Double.parseDouble(bean.getLossParticipation());
					bean.setTotalCredit(fm.formatter(String.valueOf(totalOc)));
					bean.setTotalCreditDC(fm.formatter(dropDowmImpl.GetDesginationCountry(bean.getTotalCredit().replaceAll(",", ""),bean.getExchRate())));
					Double totalDc = Double.parseDouble(bean.getCommissionQuotaShare())+Double.parseDouble(bean.getSlideScaleCom())+
							Double.parseDouble(bean.getCommissionSurplus())+Double.parseDouble(bean.getBrokerage())+Double.parseDouble(bean.getTax())+
							Double.parseDouble(bean.getWithHoldingTaxOC())+Double.parseDouble(bean.getOverrider())+Double.parseDouble(bean.getOtherCost())
							+Double.parseDouble(bean.getPremiumportifolioout())+Double.parseDouble(bean.getCliamPortfolioOut())+Double.parseDouble(bean.getPremiumReserveQuotaShare())
							+Double.parseDouble(bean.getLossReserveRetained())+Double.parseDouble(bean.getProfitCommission())+Double.parseDouble(bean.getCashLossPaid())
							+Double.parseDouble(bean.getClaimsPaid());
					
					bean.setTotalDebit(fm.formatter(String.valueOf(totalDc)));
					bean.setTotalDebitDC(fm.formatter(dropDowmImpl.GetDesginationCountry(bean.getTotalDebit().replaceAll(",", ""),bean.getExchRate())));
					String valu =Double.toString(Double.parseDouble(bean.getTotalCredit().replaceAll(",", ""))-Double.parseDouble(bean.getTotalDebit().replaceAll(",", "")));
					bean.setNetDue(fm.formatter(valu));
					bean.setNetDueUsd(fm.formatter(dropDowmImpl.GetDesginationCountry(bean.getNetDue().replaceAll(",", ""),bean.getExchRate())));
					//doubt not in query
					//	bean.setStatementDate(treatyView.get("STATEMENT_DATE")==null?"":treatyView.get("STATEMENT_DATE").toString());
					
					bean.setPremiumClass(name1==null?"":name1.toString());
		            bean.setPremiumSubClass(treatyView.getSpc()==null?"":treatyView.getSpc().toString());
	                if(!"ALL".equalsIgnoreCase(bean.getPremiumSubClass().toString())){
	                	//need to change
//	                	select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()' ),',') from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in( select * 
//	                			from table(SPLIT_TEXT_FN(replace(PD.SPC,' ', '')))) AND  SPFC.TMAS_PRODUCT_ID = '4' AND PD.BRANCH_CODE = SPFC.BRANCH_CODE) PREMIUM_SUBCLASS_NAME
	                //bean.setPremiumSubClass(treatyView.get("PREMIUM_SUBCLASS_NAME")==null?"":treatyView.get("PREMIUM_SUBCLASS_NAME").toString());
	                }
	                bean.setReference(treatyView.getReference()==null?"":treatyView.getReference().toString());
		        bean.setUwYear(treatyView.getUwy()==null?"":treatyView.getUwy().toString()) ;
		        bean.setBusinessType(treatyView.getBusinessType()==null?"":treatyView.getBusinessType().toString());
		        
		     	if(StringUtils.isNotBlank(bean.getCurrencyId())){
				//PREMIUM_SELECT_CURRENCY
		     		CriteriaQuery<String> currencyName = cb.createQuery(String.class); 
					Root<CurrencyMaster> pm = currencyName.from(CurrencyMaster.class);
					currencyName.select(pm.get("shortName")); 
					// MAXAmend ID
					Subquery<Long> maxAmend = currencyName.subquery(Long.class); 
					Root<CurrencyMaster> cms = maxAmend.from(CurrencyMaster.class);
					maxAmend.select(cb.max(cms.get("amendId")));
					Predicate b1 = cb.equal( cms.get("countryId"), pm.get("countryId"));
					Predicate b2 = cb.equal( cms.get("branchCode"), pm.get("branchCode"));
					Predicate b3 = cb.equal( cms.get("currencyId"), pm.get("currencyId"));
					Predicate b4 = cb.equal( cms.get("status"), pm.get("status"));
					maxAmend.where(b1,b2,b3,b4);
					Predicate n1 = cb.equal(pm.get("currencyId"), bean.getCurrencyId());
					Predicate n2 = cb.equal(pm.get("branchCode"), branchCode);
					Predicate n3 = cb.equal(pm.get("status"), "Y");
					Predicate n4 = cb.equal(pm.get("amendId"), maxAmend);
					currencyName.where(n1,n2,n3,n4);
					TypedQuery<String> result = em.createQuery(currencyName);
					String currency = result.getResultList().get(0)==null?"":result.getResultList().get(0);
					bean.setCurrency(currency);
			   	}
		        
		        beanList.add(bean);
		        }
			res.setTreatyView(beanList);
			//PREMIUM_SELECT_SUMOFPAIDPREMIUM
			CriteriaQuery<Double> query = cb.createQuery(Double.class); 
			Root<RskPremiumDetails> pm = query.from(RskPremiumDetails.class);
			query.select(cb.sumAsDouble(pm.get("premiumQuotashareOc"))); 
			Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
			query.where(n1);
			TypedQuery<Double> result = em.createQuery(query);
			Double paid = result.getResultList().get(0)==null?0.0:result.getResultList().get(0);
			res.setSumofpaidpremium(paid.toString());
		  
			//PREMIUM_SELECT_CURRENCY_NAME
			TmasBranchMaster bm =	bmRepo.findByBranchCode(branchCode);
		   	res.setCurrencyName(bm.getCountryShortName());
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
	public GetRetroDetailsRes getRetroDetails(String branchCode, String contractNo) {
		GetRetroDetailsRes response = new GetRetroDetailsRes();
		List<GetRetroDetailsRes1> beanList = new ArrayList<GetRetroDetailsRes1>();
		try{
			//GET_RETRO_MANUAL_ADJ
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> pm = query.from(TtrnRiskDetails.class);
			
			// Customer_name 
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pi = companyName.from(PersonalInfo.class);
			companyName.select(pi.get("companyName"));
			//maxamend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pi.get("customerId"), pis.get("customerId"));
			Predicate c2 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate c3 = cb.equal( pi.get("customerType"), pis.get("customerType"));
			maxAmend.where(c1,c2,c3);
			Predicate b1 = cb.equal( pm.get("branchCode"), pi.get("branchCode"));
			Predicate b2 = cb.equal( pm.get("rskCedingid"), pi.get("customerId"));
			Predicate b3 = cb.equal( pi.get("amendId"), maxAmend);
			Predicate b4 = cb.equal( pi.get("customerType"), "C");
			companyName.where(b1,b2,b3,b4);
			
			// Broker_name
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> b = brokerName.from(PersonalInfo.class);
			brokerName.select(b.get("firstName"));
			//maxamend
			Subquery<Long> amendPI = query.subquery(Long.class); 
			Root<PersonalInfo> bs = amendPI.from(PersonalInfo.class);
			amendPI.select(cb.max(bs.get("amendId")));
			Predicate y1 = cb.equal( b.get("customerId"), bs.get("customerId"));
			Predicate y2 = cb.equal( pm.get("branchCode"), bs.get("branchCode"));
			Predicate y3 = cb.equal( b.get("customerType"), bs.get("customerType"));
			amendPI.where(y1,y2,y3);
			Predicate x1 = cb.equal( b.get("branchCode"), pm.get("branchCode"));
			Predicate x2 = cb.equal( b.get("customerId"), pm.get("rskBrokerid"));
			Predicate x3 = cb.equal( b.get("amendId"), amendPI);
			Predicate x4 = cb.equal( b.get("customerType"), "B");
			brokerName.where(x1,x2,x3,x4);

			// Select
			query.multiselect(companyName.alias("Customer_name"),brokerName.alias("Broker_name"),
					pm.get("rskInsuredName").alias("RSK_INSURED_NAME"),pm.get("rskTreatyid").alias("RSK_TREATYID"),
					pm.get("rskProposalNumber").alias("RSK_PROPOSAL_NUMBER"),pm.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"),
					pm.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR")); 

			// MAXAmend ID
			Subquery<Long> end = query.subquery(Long.class); 
			Root<TtrnRiskDetails> pms = end.from(TtrnRiskDetails.class);
			end.select(cb.max(pms.get("rskEndorsementNo")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("rskContractNo"), pms.get("rskContractNo"));
			end.where(a1,a2);

			// Where
			Predicate n1 = cb.equal(pm.get("rskContractNo"), contractNo);
			Predicate n3 = cb.equal(pm.get("branchCode"), branchCode); 
			Predicate n4 = cb.equal(pm.get("rskEndorsementNo"), end);
			query.where(n1,n3,n4);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
		
			for(int i=0;i<list.size();i++){
				Tuple map = list.get(i);
				GetRetroDetailsRes1 bean = new GetRetroDetailsRes1();
				if(map.get("RSK_INSURED_NAME")!=null &&map.get("RSK_INSURED_NAME")!=""){
					bean.setTreatyName(map.get("RSK_INSURED_NAME")==null?"":map.get("RSK_INSURED_NAME").toString());	
				}else{
					bean.setTreatyName(map.get("RSK_TREATYID")==null?"":map.get("RSK_TREATYID").toString());
				}
				bean.setLeadBroker(map.get("Broker_name")==null?"":map.get("Broker_name").toString());
				bean.setLeadRetro(map.get("Customer_name")==null?"":map.get("Customer_name").toString());
				bean.setProposalNo(map.get("RSK_PROPOSAL_NUMBER")==null?"":map.get("RSK_PROPOSAL_NUMBER").toString());
				bean.setAmendId(map.get("RSK_ENDORSEMENT_NO")==null?"":map.get("RSK_ENDORSEMENT_NO").toString());
				bean.setCurrency(map.get("RSK_ORIGINAL_CURR")==null?"":map.get("RSK_ORIGINAL_CURR").toString());
				bean.setCurrId(map.get("RSK_ORIGINAL_CURR")==null?"":map.get("RSK_ORIGINAL_CURR").toString());
				beanList.add(bean);
				}
			response.setCommonResponse(beanList);
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
	public CommonSaveRes InsertPremium(InsertPremiumReq bean) {
		CommonSaveRes response = new CommonSaveRes();
		try {
			//PREMIUM_INSERT_TREATYPREMIUM
			 String transNo =  "";
		 	int result= insertArguments(bean);
		 	  if(StringUtils.isBlank(bean.getTransactionNo())){
		 		 transNo = dropDowmImpl.getSequencePTRT("RetroJournal" ,"","", bean.getBranchCode(),"",bean.getTransaction());
			    }else{
			    	transNo=bean.getTransactionNo();
			    }
			if (result==1) {
				//PREMIUM_SP_RETROSPLIT
				 StoredProcedureQuery integration = null;
				  integration = em.createStoredProcedureQuery("RetroPremium_Split_claim")
				  .registerStoredProcedureParameter("pvContractNo", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnLayerNo", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnProductId", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnPremiumTranNo", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pdPremTranDate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnCurrencyId", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnExchange", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnBranchCode", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pvtransactionType", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pdAmendDate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnReference", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnTreatyName", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnRemarks", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnUwYear", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pnSubClass", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("retroCession", String.class, ParameterMode.IN)
				  .setParameter("pvContractNo", bean.getContNo())
				  .setParameter("pnLayerNo", StringUtils.isEmpty(bean.getLayerno())?"0":bean.getLayerno())
				  .setParameter("pnProductId", bean.getProductId())
				  .setParameter("pnPremiumTranNo", transNo)
				  .setParameter("pdPremTranDate",bean.getTransaction() )
				  .setParameter("pnCurrencyId", bean.getCurrency())
				  .setParameter("pnExchange", bean.getExchRate())
				  .setParameter("pnBranchCode",bean.getBranchCode() )
				  .setParameter("pvtransactionType", "P")
				  .setParameter("pdAmendDate", bean.getAmendmentDate()==null?"":bean.getAmendmentDate())
				  .setParameter("pnReference", bean.getReference())
				  .setParameter("pnTreatyName", bean.getTreatyName())
				  .setParameter("pnRemarks", bean.getRemarks())
				  .setParameter("pnUwYear", bean.getUwYear())
				  .setParameter("pnSubClass", bean.getSubProfitId())
				  .setParameter("retroCession", "Yes");
				  integration.execute();
				// output=(String) integration.getOutputParameterValue("pvQuoteNo");
			//	int spresult=this.mytemplate.update(query,args);
				boolean saveFlag = true;
				response.setResponse(String.valueOf(saveFlag));
				response.setMessage("Success");
				response.setIsError(false);
			}	}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
		}
	private int insertArguments(InsertPremiumReq beanObj){
	int count = 0;
	SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
	try {
		RskPremiumDetails entity = new RskPremiumDetails();
		    entity.setContractNo(new BigDecimal(beanObj.getContNo()));
		    if(StringUtils.isBlank(beanObj.getTransactionNo())){
		    	entity.setTransactionNo(new BigDecimal(dropDowmImpl.getSequencePTRT("RetroJournal" ,"","", beanObj.getBranchCode(),"",beanObj.getTransaction())));
		    }else{
		    	entity.setTransactionNo(new BigDecimal(beanObj.getTransactionNo()));
		    }
		    entity.setTransactionMonthYear(sdf.parse(beanObj.getTransaction()));
		    entity.setCurrencyId(new BigDecimal(beanObj.getCurrency()));
		    entity.setExchangeRate(new BigDecimal(beanObj.getExchRate()));
		    entity.setBrokerageAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getBrokerage(),beanObj)));;
		    entity.setBrokerageAmtDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getBrokerageAmtOc().toString(), beanObj.getExchRate())));
		    entity.setTaxAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getTax(),beanObj)));;
		    entity.setTaxAmtDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getTaxAmtOc().toString(), beanObj.getExchRate())));;
		    entity.setOverriderAmtOc(new BigDecimal(getModeOfTransaction(beanObj.getOverrider(),beanObj)));
		    entity.setOverriderAmtDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getOverriderAmtOc().toString(),beanObj.getExchRate())));
		    entity.setWithHoldingTaxOc(new BigDecimal(getModeOfTransaction(beanObj.getWithHoldingTaxOC(),beanObj)));
		    entity.setWithHoldingTaxDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getWithHoldingTaxOc().toString(), beanObj.getExchRate())));;
		    entity.setEntryDateTime(StringUtils.isEmpty(beanObj.getInceptionDate()) ?null :sdf.parse(beanObj.getInceptionDate()));;
		    entity.setPremiumQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumQuotaShare(),beanObj)));;
		    entity.setPremiumQuotashareDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getPremiumQuotashareOc().toString(), beanObj.getExchRate())));
		    entity.setCommissionQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getCommissionQuotaShare(),beanObj)));
		    entity.setCommissionQuotashareDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getCommissionQuotashareOc().toString(), beanObj.getExchRate())));
		    entity.setPremiumPortfolioinOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumportifolioIn(),beanObj)));		
		    entity.setPremiumPortfolioinDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getPremiumPortfolioinOc().toString(), beanObj.getExchRate())));
		    entity.setRiCession("Yes");	
		    entity.setLoginId(beanObj.getLoginId());
		    entity.setBranchCode(beanObj.getBranchCode());
		    entity.setSubClass(new BigDecimal(beanObj.getPredepartment()));		
		    entity.setTdsOc(new BigDecimal(getModeOfTransaction(beanObj.getTaxDedectSource(),beanObj)));
		    entity.setTdsDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getTdsOc().toString(), beanObj.getExchRate())));;
		    //entity.setStOc(new BigDecimal(getModeOfTransaction(beanObj.getServiceTax(),beanObj)));;
		   // entity.setStDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getStOc().toString(), beanObj.getExchRate())));;
		    entity.setScCommOc(new BigDecimal(getModeOfTransaction(beanObj.getSlideScaleCom(),beanObj)));
		    entity.setScCommDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getScCommOc().toString(), beanObj.getExchRate())));
		    entity.setPremiumClass(beanObj.getPredepartment());	
		    entity.setPremiumSubclass(beanObj.getSubProfitId().replace(" ", ""));			
		    entity.setLpcOc(new BigDecimal(getModeOfTransaction(beanObj.getLossParticipation(),beanObj)));
		    entity.setLpcDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getLpcOc().toString(), beanObj.getExchRate())));
		    entity.setProductId(new BigDecimal(beanObj.getProductId()));
		    entity.setBusinessType(beanObj.getBusinessType());
		    entity.setClaimPortfolioinOc(new BigDecimal(getModeOfTransaction(beanObj.getCliamPortfolioin(),beanObj)));
		    entity.setClaimPortfolioinDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getClaimPortfolioinOc().toString(), beanObj.getExchRate())));
		    entity.setPremiumPortfoliooutOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumportifolioout(),beanObj)));;
		    entity.setPremiumPortfoliooutDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getPremiumPortfoliooutOc().toString(), beanObj.getExchRate())));		
		    entity.setLossReserveReleasedOc(new BigDecimal(getModeOfTransaction(beanObj.getLossReserveReleased(),beanObj)));;
		    entity.setLossReserveReleasedDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getLossReserveReleasedOc().toString(), beanObj.getExchRate())));
		    entity.setPremiumreserveQuotashareOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumReserveQuotaShare(),beanObj)));
		    entity.setPremiumreserveQuotashareDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getPremiumreserveQuotashareOc().toString(), beanObj.getExchRate())));
		    entity.setCashLossCreditOc(new BigDecimal(getModeOfTransaction(beanObj.getCashLossCredit(),beanObj)));
		    entity.setCashLossCreditDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getCashLossCreditOc().toString(), beanObj.getExchRate())));
		    entity.setLossReserveretainedOc(new BigDecimal(getModeOfTransaction(beanObj.getLossReserveRetained(),beanObj)));
		    entity.setLossReserveretainedDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getLossReserveretainedOc().toString(), beanObj.getExchRate())));
		    entity.setProfitCommissionOc(new BigDecimal(getModeOfTransaction(StringUtils.isBlank(beanObj.getProfitCommission()) ? "0" : beanObj.getProfitCommission(),beanObj)));
		    entity.setProfitCommissionDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getProfitCommissionOc().toString(), beanObj.getExchRate())));		
		    entity.setCashLosspaidOc(new BigDecimal(getModeOfTransaction(beanObj.getCashLossPaid(),beanObj)));		
		    entity.setCashLosspaidDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getCashLosspaidOc().toString(), beanObj.getExchRate())));
		    entity.setStatus("Y");
		    entity.setEnteringMode("2");
		    entity.setClaimsPaidOc(new BigDecimal(getModeOfTransaction(beanObj.getClaimspaid(),beanObj)));
		    entity.setClaimsPaidDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getClaimsPaidOc().toString(), beanObj.getExchRate())));		
		    entity.setSettlementStatus(beanObj.getSettlementstatus());
		    entity.setXlCostOc(new BigDecimal(getModeOfTransaction(beanObj.getXlCost(),beanObj)));
		    entity.setXlCostDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getXlCostOc().toString(), beanObj.getExchRate())));
		    entity.setClaimPortfolioOutOc(new BigDecimal(getModeOfTransaction(beanObj.getCliamportfolioout(),beanObj)));
		    entity.setClaimPortfolioOutDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getClaimPortfolioOutOc().toString(), beanObj.getExchRate())));;
		    entity.setPremiumReserveRealsedOc(new BigDecimal(getModeOfTransaction(beanObj.getPremiumReserveReleased(),beanObj)));
		    entity.setPremiumReserveReleaseDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getPremiumReserveRealsedOc().toString(), beanObj.getExchRate())));			
		    entity.setOtherCostOc(new BigDecimal(getModeOfTransaction(beanObj.getOtherCost(),beanObj)));
		    entity.setOtherCostDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getOtherCostOc().toString(), beanObj.getExchRate())));
		    entity.setRemarks(beanObj.getRemarks());
		    entity.setTotalCrOc(new BigDecimal(getModeOfTransaction(beanObj.getTotalCredit(),beanObj)));
		    entity.setTotalCrDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getTotalCrOc().toString(),beanObj.getExchRate())));
		    entity.setTotalDrOc(new BigDecimal(getModeOfTransaction(beanObj.getTotalDebit(),beanObj)));;
		    entity.setTotalDrDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getTotalDrOc().toString(),beanObj.getExchRate())));
		    entity.setInterestOc(new BigDecimal(getModeOfTransaction(beanObj.getInterest(),beanObj)));
		    entity.setInterestDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getInterestOc().toString(),beanObj.getExchRate())));		    
		    entity.setOsclaimLossupdateOc(new BigDecimal(StringUtils.isEmpty(beanObj.getOsClaimsLossUpdateOC())?"0":getModeOfTransaction(beanObj.getOsClaimsLossUpdateOC(),beanObj)));
		    entity.setOsclaimLossupdateDc(new BigDecimal(dropDowmImpl.GetDesginationCountry(entity.getOsclaimLossupdateOc().toString(), beanObj.getExchRate())));		
		    entity.setPremiumSurplusOc(BigDecimal.ZERO);
		    entity.setCommissionSurplusOc(BigDecimal.ZERO);
		    entity.setNetdueOc(new BigDecimal(getNetDueAmount(entity,getModeOfTransaction(beanObj.getClaimspaid(),beanObj))));
		    entity.setNetdueDc(new BigDecimal(dropDowmImpl.GetDesginationCountry( entity.getNetdueOc().toString(), beanObj.getExchRate())));
		    entity.setAmendId(BigDecimal.ZERO);
		    pdRepo.save(entity);
		    beanObj.setTransactionNo(entity.getTransactionNo().toString());
			
			count=1;
	}catch(Exception e) {
		e.printStackTrace();;
	}
	return count;
	}
	private static String getModeOfTransaction(final String Value,final InsertPremiumReq beanObj) {
		/*LOGGER.info("PremiumDAOImpl getModeOfTransaction || Enter");
		LOGGER.info("Value=>"+Value);
		LOGGER.info("Entering Mode=>"+beanObj.getEnteringMode());
		LOGGER.info("ShareSigned=>"+beanObj.getShareSigned());
		String result="0";
		double shareSigned=0.0;
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		if(beanObj.getEnteringMode()!=null)
		{
			if("1".equalsIgnoreCase(beanObj.getEnteringMode()))
			{
				shareSigned=Double.parseDouble(beanObj.getShareSigned());
			}
			else if("2".equalsIgnoreCase(beanObj.getEnteringMode()))
			{
				shareSigned=100;
			}
			//shareSigned=100;
			LOGGER.info("Value==>"+Value);
			if(!"".equalsIgnoreCase(Value))
			{
					double finalValue=Double.parseDouble(Value) *shareSigned/100;
					LOGGER.info("Final Value==>"+finalValue);
					result=String.valueOf(Double.valueOf(twoDForm.format(finalValue)));
			}*/
			
			String result=Value;
		return result;
		}
	private  String getNetDueAmount( RskPremiumDetails entity, String CliamPaid) {
		double Abt=0;
		double Bbt=0;
		  double cbt= 0.0;
		try {
		if(StringUtils.isNotEmpty(entity.getPremiumQuotashareOc().toString())) 
		{
		Abt+=Double.parseDouble(entity.getPremiumQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumSurplusOc().toString()))
		{
		Abt+=Double.parseDouble(entity.getPremiumSurplusOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumPortfolioinOc().toString()))
		{
		Abt+=Double.parseDouble(entity.getPremiumPortfolioinOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getClaimPortfolioinOc().toString()))
		{
		Abt+=Double.parseDouble(entity.getClaimPortfolioinOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLossReserveReleasedOc().toString()))
		{
		Abt+=Double.parseDouble(entity.getLossReserveReleasedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCashLossCreditOc().toString()))
		{
		Abt+=Double.parseDouble(entity.getCashLossCreditOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumReserveRealsedOc().toString()))
		{
			Abt+=Double.parseDouble(entity.getPremiumReserveRealsedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getInterestOc().toString()))
		{
			Abt+=Double.parseDouble(entity.getInterestOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getTdsOc().toString()))
		{
		Abt+=Double.parseDouble(entity.getTdsOc().toString());
		}
		//if(StringUtils.isNotEmpty(entity.getStOc().toString()))
		//{
		//Abt+=Double.parseDouble(entity.getStOc().toString());
		//}
		
		if(StringUtils.isNotEmpty(entity.getLpcOc().toString()))
		{
		Abt+=Double.parseDouble(entity.getLpcOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCommissionQuotashareOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getCommissionQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCommissionSurplusOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getCommissionSurplusOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getBrokerageAmtOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getBrokerageAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getTaxAmtOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getTaxAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumPortfoliooutOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getPremiumPortfoliooutOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getPremiumreserveQuotashareOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getPremiumreserveQuotashareOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getLossReserveretainedOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getLossReserveretainedOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getProfitCommissionOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getProfitCommissionOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getCashLosspaidOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getCashLosspaidOc().toString());
		}
		if(StringUtils.isNotEmpty(CliamPaid))
		{
			Bbt+=Double.parseDouble(CliamPaid);
		}
		if(StringUtils.isNotEmpty(entity.getClaimPortfolioOutOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getClaimPortfolioOutOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getXlCostOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getXlCostOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getOtherCostOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getOtherCostOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getOverriderAmtOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getOverriderAmtOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getWithHoldingTaxOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getWithHoldingTaxOc().toString());
		}
		if(StringUtils.isNotEmpty(entity.getScCommOc().toString()))
		{
			Bbt+=Double.parseDouble(entity.getScCommOc().toString());
		}
	      cbt=Abt-Bbt;
		}catch(Exception e) {
			e.printStackTrace();
			}
		return String.valueOf(cbt);
	}
	}


