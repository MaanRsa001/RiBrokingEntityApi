package com.maan.insurance.service.impl.authentication;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.insurance.jpa.entity.propPremium.TtrnCashLossCredit;
import com.maan.insurance.jpa.entity.propPremium.TtrnDepositRelease;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.jpa.repository.propPremium.TtrnDepositReleaseRepository;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsTemp;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.repository.RskPremiumDetailsRepository;
import com.maan.insurance.model.repository.RskPremiumDetailsTempRepository;
import com.maan.insurance.model.repository.TtrnCashLossCreditRepository;
import com.maan.insurance.model.req.authentication.AuthenticationChangesReq;
import com.maan.insurance.model.req.authentication.AuthenticationListReq;
import com.maan.insurance.model.req.authentication.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.authentication.AuthenticationListRes;
import com.maan.insurance.model.res.authentication.AuthenticationListRes1;
import com.maan.insurance.model.res.premium.GetCashLossCreditRes1;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes;
import com.maan.insurance.model.res.authentication.GetPremiumDetailsRes1;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.authentication.AuthenticationService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.premium.PropPremiumServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;
@Service
public class AuthenticationServiceImple implements AuthenticationService{
	private Logger log = LogManager.getLogger(AuthenticationServiceImple.class);

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private RskPremiumDetailsTempRepository pdRepo;
	
	@Autowired
	private RskPremiumDetailsRepository  rskPremiumDetailsRepo;
	
	@Autowired
	private TtrnDepositReleaseRepository drRepo;
	
	@Autowired
	private TtrnCashLossCreditRepository clRepo;
	
	@Autowired
	private PropPremiumServiceImple propPreImple;
	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();
	private Query query1 = null;

	public AuthenticationServiceImple() {
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
	public AuthenticationListRes authenticationList(AuthenticationListReq bean) {
		AuthenticationListRes response = new AuthenticationListRes();
		List<AuthenticationListRes1> finalizeBean = new ArrayList<AuthenticationListRes1>();
		try{
			List<String> myList = new ArrayList<String>(Arrays.asList(bean.getCheckItem().split(",")));
			//		query = "SELECT TRANSACTION_NO,PREMIUM_CLASS,REQUEST_NO,LOGIN_ID,ACCOUNT_PERIOD_QTR,INSTALMENT_NUMBER,RECEIPT_NO,PREMIUM_SUBCLASS, REVERSEL_STATUS,REVERSE_TRANSACTION_NO,PRODUCT_ID,CONTRACT_NO,case TRA.PRODUCT_ID when 2 then (SELECT DISTINCT DETAIL_NAME FROM CONSTANT_DETAIL WHERE CATEGORY_ID=49 AND TYPE=TRA.ACCOUNT_PERIOD_QTR and STATUS='Y') || ' ' || TRA.ACCOUNT_PERIOD_YEAR else TRA.ACCOUNT_PERIOD_QTR || ' ' || ACCOUNT_PERIOD_YEAR  end ACC_PER FROM RSK_PREMIUM_DETAILS_TEMP TRA WHERE BRANCH_CODE=?  AND REQUEST_NO IN('"+val+"')";
					CriteriaBuilder cb = em.getCriteriaBuilder(); 
					CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
					
					Root<RskPremiumDetailsTemp> pm = query.from(RskPremiumDetailsTemp.class);
					
					// detailName
					Subquery<String> detailName = query.subquery(String.class); 
					Root<ConstantDetail> pms = detailName.from(ConstantDetail.class);
					detailName.select(pms.get("detailName"));
					Predicate a1 = cb.equal( pms.get("categoryId"), "49");
					Predicate a2 = cb.equal( pms.get("type"), pm.get("accountPeriodQtr"));
					Predicate a3 = cb.equal( pms.get("status"), "Y");
					detailName.where(a1,a2,a3).distinct(true);

					Expression<String> name = cb.concat(detailName, " ");
					Expression<String> accountPeriodQtr = cb.concat(pm.get("accountPeriodQtr"), " ");
					
					// Select
					query.multiselect(pm.get("transactionNo").alias("TRANSACTION_NO"),pm.get("premiumClass").alias("PREMIUM_CLASS"),
							pm.get("requestNo").alias("REQUEST_NO"),pm.get("loginId").alias("LOGIN_ID"),pm.get("layerNo").alias("LAYER_NO"),
							pm.get("accountPeriodQtr").alias("ACCOUNT_PERIOD_QTR"),pm.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
							pm.get("receiptNo").alias("RECEIPT_NO"),pm.get("premiumSubclass").alias("PREMIUM_SUBCLASS"),
							pm.get("reverselStatus").alias("REVERSEL_STATUS"),pm.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
							pm.get("productId").alias("PRODUCT_ID"),pm.get("contractNo").alias("CONTRACT_NO"), 
							pm.get("transactionMonthYear").alias("TRANS_DATE"),
							cb.selectCase().when(cb.equal( pm.get("productId"), 2), cb.concat(name, pm.get("accountPeriodYear")))
							.otherwise(cb.concat(accountPeriodQtr, pm.get("accountPeriodYear"))).alias("ACC_PER")); 
				
					Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			if("Multiple".equalsIgnoreCase(bean.getUploadStatus()) &&  bean.getCheckItem()!=null){
				// In
				Expression<Long> e = pm.get("requestNo");
				Predicate n2 = e.in(myList);
				query.where(n1,n2);
			}else{
				//GET_AUTHENTICATION_LIST   
				Predicate n2 = cb.equal( pm.get("transStatus"), "P");
				query.where(n1,n2);
			}
			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();
			
			if(list.size()>0)
			 {
				 for(int i=0;i<list.size();i++){
					 Tuple	 editPremium=list.get(i);
					 AuthenticationListRes1 beanObj = new AuthenticationListRes1();								
					 beanObj.setTransaction(editPremium.get("TRANS_DATE")==null?"":editPremium.get("TRANS_DATE").toString()); 
					if("EP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
						beanObj.setAccountPeriod("Endorsement Premium");
			    	}
					else if("RTP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
						beanObj.setAccountPeriod("Return Premium");
			    	}
					else if("RVP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
						beanObj.setAccountPeriod("Reversal Premium");
			    	}
					else if("RP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
						beanObj.setAccountPeriod("Reinstatement Premium");
			    	}
					else if("AP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
						beanObj.setAccountPeriod("Adjustment Premium");
			    	}
					else
			    	{
						beanObj.setAccountPeriod((editPremium.get("ACC_PER")==null?"":editPremium.get("ACC_PER").toString()));
			    	}						
					beanObj.setProductId(editPremium.get("PRODUCT_ID")==null?"":editPremium.get("PRODUCT_ID").toString());
					beanObj.setContNo(editPremium.get("CONTRACT_NO")==null?"":editPremium.get("CONTRACT_NO").toString());
					beanObj.setRequestNo(editPremium.get("REQUEST_NO")==null?"":editPremium.get("REQUEST_NO").toString());
					beanObj.setTransactionNo(editPremium.get("TRANSACTION_NO")==null?"":editPremium.get("TRANSACTION_NO").toString());
					beanObj.setLoginId(editPremium.get("LOGIN_ID")==null?"":editPremium.get("LOGIN_ID").toString());
					beanObj.setDepartmentId(editPremium.get("PREMIUM_CLASS")==null?"":editPremium.get("PREMIUM_CLASS").toString());
					beanObj.setLayerNo(editPremium.get("LAYER_NO")==null?"":editPremium.get("LAYER_NO").toString());
			        finalizeBean.add(beanObj);   
				}
			 }
			response.setCommonResponse(finalizeBean);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
	   				e.printStackTrace();
	   				response.setMessage("Failed");
	   				response.setIsError(true);
	   			}
	   		return response;
	}
	@Transactional
	@Override
	public CommonResponse authenticationChanges(AuthenticationChangesReq beanObj) {
		CommonResponse response = new CommonResponse();
		try {
		//FAC_TEMP_STATUS_UPDATE
		RskPremiumDetailsTemp entity = pdRepo.findByRequestNoAndBranchCode(new BigDecimal(beanObj.getRequestNo()),beanObj.getBranchCode());
		if("multiple".equalsIgnoreCase(beanObj.getUploadStatus())){
			String val[] = beanObj.getCheckItem().split(",");
			for(int i=0;i<val.length;i++){
				beanObj.setRequestNo(val[i]);
			//	getPremiumDetails(beanObj);
				if("A".equalsIgnoreCase(beanObj.getApproveStatus())){
					beanObj.setTransactionNo(fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction()));
				}
				if(entity != null){
				entity.setTransStatus(beanObj.getApproveStatus());
				entity.setLoginId(beanObj.getLoginId());
				entity.setTransactionNo(beanObj.getTransactionNo()==null?BigDecimal.ZERO:new BigDecimal(beanObj.getTransactionNo()));
				entity.setRequestNo(new BigDecimal(beanObj.getRequestNo()));
				entity.setBranchCode(beanObj.getBranchCode());
				pdRepo.saveAndFlush(entity);
				}
		 		updateTransactionnDetails(beanObj);
		 		if("A".equalsIgnoreCase(beanObj.getApproveStatus())){
		 			updateTempToMain(beanObj);
					}
			}
		}else{
		//	getPremiumDetails(beanObj);
			if("A".equalsIgnoreCase(beanObj.getApproveStatus())){
				beanObj.setTransactionNo(fm.getSequence("Premium",beanObj.getProductId(),beanObj.getDepartmentId(), beanObj.getBranchCode(),"",beanObj.getTransaction()));
			}
			if(entity != null){
			entity.setTransStatus(beanObj.getApproveStatus());
			entity.setLoginId(beanObj.getLoginId());
			entity.setTransactionNo(beanObj.getTransactionNo()==null?BigDecimal.ZERO:new BigDecimal(beanObj.getTransactionNo()));
			entity.setRequestNo(new BigDecimal(beanObj.getRequestNo()));
			entity.setBranchCode(beanObj.getBranchCode());
			pdRepo.saveAndFlush(entity);
			}
	 		updateTransactionnDetails(beanObj);
		 		if("A".equalsIgnoreCase(beanObj.getApproveStatus())){
		 			updateTempToMain(beanObj);	
					}
		}
		response.setMessage("Success");
		response.setIsError(false);
		}catch(Exception e){
   				e.printStackTrace();
   				response.setMessage("Failed");
   				response.setIsError(true);
   			}
   		return response;
	}
	@Transactional
	private void updateTransactionnDetails(AuthenticationChangesReq beanObj) {
		try{
			TtrnDepositRelease entity = drRepo.findByContractNoAndTempRequestNo(beanObj.getContractNo(),new BigDecimal(beanObj.getRequestNo()));
			//UPDATE_TRANSACTION_NO_STATUS
			if(entity != null){
			entity.setRlTransactionNo(beanObj.getTransactionNo()==null?"":beanObj.getTransactionNo());
			entity.setTableMoveStatus(beanObj.getApproveStatus());
			drRepo.saveAndFlush(entity);
			}
	 	
			//UPDATE_CASHLOSS_STATUS
			TtrnCashLossCredit  entity1 = 	clRepo.findByContractNoAndTempRequestNo(beanObj.getContractNo(),new BigDecimal(beanObj.getRequestNo()));
			if(entity1 != null){
			entity1.setCredittrxnno(beanObj.getTransactionNo()==null?"":beanObj.getTransactionNo());
			entity1.setTableMoveStatus(beanObj.getApproveStatus());
			clRepo.saveAndFlush(entity1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	@Transactional
	private void updateTempToMain(AuthenticationChangesReq beanObj) {
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder();
	 		//FAC_PREMIUM_TEMP_TO_MAIN
	 		RskPremiumDetailsTemp entity = pdRepo.findByRequestNoAndBranchCode(new BigDecimal(beanObj.getRequestNo()),beanObj.getBranchCode());
	 		if(entity != null){
	 		RskPremiumDetails  entity1 = new RskPremiumDetails();
	 		DozerBeanMapper dozerMapper = new DozerBeanMapper(); 
	 		dozerMapper.map(entity,entity1) ;         //mapper match with only name(not by datatype and order)
	 		rskPremiumDetailsRepo.saveAndFlush(entity1);
	 		}
	 		if(!"2".equalsIgnoreCase(beanObj.getProductId())){
	 			
	 		//UPDATE_MND_INSTALLMENT
	 			
	 		CriteriaUpdate<TtrnMndInstallments> update = cb.createCriteriaUpdate(TtrnMndInstallments.class);
	 		// set the root class
	 		Root<TtrnMndInstallments> m = update.from(TtrnMndInstallments.class);
	 		// set update and where clause
	 		update.set("transactionNo", beanObj.getTransactionNo());
	 		
	 		// maxEndRp
			Subquery<Long> maxEnd = update.subquery(Long.class); 
			Root<TtrnMndInstallments> rps = maxEnd.from(TtrnMndInstallments.class);
			maxEnd.select(cb.max(rps.get("endorsementNo")));
			Predicate y1 = cb.equal( rps.get("contractNo"), m.get("contractNo"));
			Predicate y2 = cb.equal( rps.get("layerNo"), m.get("layerNo"));
			Predicate y3 = cb.equal( rps.get("installmentNo"), m.get("installmentNo"));
			maxEnd.where(y1,y2,y3);
	 		
	 		Predicate n1 = cb.equal(m.get("contractNo"), beanObj.getContractNo());
	 		Predicate n2 = cb.equal(m.get("layerNo"), StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo());
	 		Predicate n3 = cb.equal(m.get("installmentNo"), beanObj.getInstlmentNo());
	 		Predicate n4 = cb.equal(m.get("endorsementNo"), maxEnd);
	 		update.where(n1,n2,n3,n4);
	 		// perform update
	 		em.createQuery(update).executeUpdate();
		
	 		}
	 		if("2".equalsIgnoreCase(beanObj.getProductId())){
	 			
	 			//UPDATE_PREMIUM_RESERVE
	 			CriteriaUpdate<RskPremiumDetails> update = cb.createCriteriaUpdate(RskPremiumDetails.class);
		 		// set the root class
		 		Root<RskPremiumDetails> m = update.from(RskPremiumDetails.class);
		 		
		 		Expression<Double> date = cb.<Double>selectCase().when(cb.isNull(m.get("prdAllocatedTillDate")), 0.0).otherwise(m.get("prdAllocatedTillDate"));
		 	
		 		// maxEndRp
				Subquery<Double> maxEnd = update.subquery(Double.class); 
				Root<TtrnDepositRelease> rps = maxEnd.from(TtrnDepositRelease.class);
				Expression<Double> amnt = cb.sum(cb.<Double>selectCase().when(cb.isNull(rps.get("rlAmountInRtCurr")), 0.0).otherwise(rps.get("rlAmountInRtCurr")));
				maxEnd.select(amnt);
				Predicate y1 = cb.equal( rps.get("contractNo"), beanObj.getContractNo());
				Predicate y2 = cb.equal( rps.get("tempRequestNo"), beanObj.getRequestNo());
				Predicate y3 = cb.equal( rps.get("tableMoveStatus"), "A");
				Predicate y4 = cb.equal( rps.get("releaseType"), "PRR");
				maxEnd.where(y1,y2,y3,y4);
		 		
		 		// set update and where clause
		 		update.set("prdAllocatedTillDate", cb.sum(date, maxEnd));
		 		
		 		Predicate n1 = cb.equal(m.get("contractNo"), beanObj.getContractNo());
		 		Predicate n2 = cb.equal(m.get("transactionNo"), beanObj.getTransactionNo());
		 		update.where(n1,n2);
		 		// perform update
		 		em.createQuery(update).executeUpdate();
		 		
		 		//UPDATE_LOSS_RESERVE
		 		CriteriaUpdate<RskPremiumDetails> update1 = cb.createCriteriaUpdate(RskPremiumDetails.class);
		 		// set the root class
		 		Root<RskPremiumDetails> ms = update1.from(RskPremiumDetails.class);
		 		
		 		Expression<Double> date1 = cb.<Double>selectCase().when(cb.isNull(ms.get("lrdAllocatedTillDate")), 0.0).otherwise(ms.get("lrdAllocatedTillDate"));
		 	
		 		// maxEndRp
				Subquery<Double> maxEnd1 = update1.subquery(Double.class); 
				Root<TtrnDepositRelease> rps1 = maxEnd1.from(TtrnDepositRelease.class);
				Expression<Double> amnt1 = cb.sum(cb.<Double>selectCase().when(cb.isNull(rps.get("rlAmountInRtCurr")), 0.0).otherwise(rps.get("rlAmountInRtCurr")));
				maxEnd1.select(amnt1);
				Predicate x1 = cb.equal( rps1.get("contractNo"), beanObj.getContractNo());
				Predicate x2 = cb.equal( rps1.get("tempRequestNo"), beanObj.getRequestNo());
				Predicate x3 = cb.equal( rps1.get("tableMoveStatus"), "A");
				Predicate x4 = cb.equal( rps1.get("releaseType"), "LRR");
				maxEnd1.where(x1,x2,x3,x4);
		 		
		 		// set update and where clause
				update1.set("prdAllocatedTillDate", cb.sum(date1, maxEnd1));
		 		
		 		Predicate m1 = cb.equal(ms.get("contractNo"), beanObj.getContractNo());
		 		Predicate m2 = cb.equal(ms.get("transactionNo"), beanObj.getTransactionNo());
		 		update1.where(m1,m2);
		 		// perform update
		 		em.createQuery(update1).executeUpdate();
		 		
		 		InsertPremiumReq req = new InsertPremiumReq();
		 		req.setGetCashLossCreditReq1(beanObj.getGetCashLossCreditReq1());	
		 		req.setContNo(beanObj.getContractNo());
		 		req.setDepartmentId(beanObj.getDepartmentId());
		 		req.setClaimPayNo(beanObj.getClaimPayNo());
		 		req.setCurrencyId(beanObj.getCurrencyId());
		 		req.setBranchCode(beanObj.getBranchCode());
		 		req.setMode(beanObj.getMode());
		 		
			 	List<GetCashLossCreditRes1> cashLossList = propPreImple.getCassLossCredit(req).getCommonResponse();
			 	 for(int i=0;i<cashLossList.size();i++){
			 		GetCashLossCreditRes1 form= cashLossList.get(0);
			 		//UPDATE_CLAIM_PAYMENT
			 		CriteriaUpdate<TtrnClaimPayment> update2 = cb.createCriteriaUpdate(TtrnClaimPayment.class);
			 		// set the root class
			 		Root<TtrnClaimPayment> m3 = update2.from(TtrnClaimPayment.class);
			 		
			 		Expression<Double> tillDate = cb.<Double>selectCase().when(cb.isNull(m3.get("cashLossSettledTilldate")), 0.0).otherwise(m3.get("cashLossSettledTilldate"));
			 	
			 		// maxEndRp
					Subquery<Double> credit = update2.subquery(Double.class); 
					Root<TtrnCashLossCredit> cl = credit.from(TtrnCashLossCredit.class);
					Expression<Double> amntcld = cb.sum(cb.<Double>selectCase().when(cb.isNull(cl.get("creditamountcld")), 0.0).otherwise(cl.get("creditamountcld")));
					credit.select(amntcld);
					Predicate z1 = cb.equal( cl.get("contractNo"), form.getContNo());
					Predicate z2 = cb.equal( cl.get("branchCode"), beanObj.getBranchCode());
					Predicate z3 = cb.equal( cl.get("tempRequestNo"), beanObj.getRequestNo());
					Predicate z4 = cb.equal( cl.get("tableMoveStatus"), "A");
					Predicate z5 = cb.equal( cl.get("claimNo"), form.getClaimNumber());
					Predicate z6 = cb.equal( cl.get("claimpaymentNo"), form.getClaimPaymentNo());
					credit.where(z1,z2,z3,z4,z5,z6);
			 		
			 		// set update and where clause
					update2.set("cashLossSettledTilldate", cb.sum(tillDate, credit));
			 		
			 		Predicate r1 = cb.equal(m3.get("contractNo"), form.getContNo());
			 		Predicate r2 = cb.equal(m3.get("claimNo"), form.getClaimNumber());
			 		Predicate r3 = cb.equal(m3.get("claimpaymentNo"), form.getClaimPaymentNo());
			 		update2.where(r1,r2,r3);
			 		// perform update
			 		em.createQuery(update2).executeUpdate();
			 	 }
	 		}
		
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
			  .setParameter("pvContractNo", beanObj.getContractNo())
			  .setParameter("pnLayerNo", StringUtils.isEmpty(beanObj.getLayerNo())?"0":beanObj.getLayerNo())
			  .setParameter("pnProductId", beanObj.getProductId())
			  .setParameter("pnPremiumTranNo", beanObj.getTransactionNo())
			  .setParameter("pdPremTranDate",beanObj.getTransaction() )
			  .setParameter("pnCurrencyId", beanObj.getCurrencyId())
			  .setParameter("pnExchange", beanObj.getExchRate())
			  .setParameter("pnBranchCode",beanObj.getBranchCode() )
			  .setParameter("pvtransactionType", "P")
			  .setParameter("pdAmendDate", beanObj.getAmendmentDate()==null?"":beanObj.getAmendmentDate())
			  .setParameter("pnReference", "")
			  .setParameter("pnTreatyName", "")
			  .setParameter("pnRemarks", "")
			  .setParameter("pnUwYear", "")
			  .setParameter("pnSubClass", "")
			  .setParameter("retroCession", beanObj.getRiCession());
			  integration.execute();
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public GetPremiumDetailsRes getPremiumDetails(GetPremiumDetailsReq req) {
		GetPremiumDetailsRes response = new GetPremiumDetailsRes();
		List<GetPremiumDetailsRes1> resList = new ArrayList<GetPremiumDetailsRes1>();
		try{
			//PREMIUM_SELECT_FACPREMIUMEDIT_TEMP_AUTH
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			// Find All, like table name
			Root<RskPremiumDetailsTemp> pm = query.from(RskPremiumDetailsTemp.class);

			// Select
			query.multiselect(pm.get("transactionMonthYear").alias("TRANS_DATE"),pm.get("statementDate").alias("STATEMENT_DATE"),
					pm.get("accountPeriodQtr").alias("ACCOUNT_PERIOD_QTR"),pm.get("instalmentNumber").alias("INSTALMENT_NUMBER"),
					pm.get("currencyId").alias("CURRENCY_ID"),pm.get("exchangeRate").alias("EXCHANGE_RATE"),
					pm.get("premiumQuotashareOc").alias("PREMIUM_QUOTASHARE_OC"),pm.get("commission").alias("COMMISSION"),
					pm.get("commissionQuotashareOc").alias("COMMISSION_QUOTASHARE_OC"),pm.get("brokerage").alias("BROKERAGE"),
					pm.get("brokerageAmtOc").alias("BROKERAGE_AMT_OC"),pm.get("tax").alias("TAX"),
					pm.get("taxAmtOc").alias("TAX_AMT_OC"),
					pm.get("status").alias("STATUS"),pm.get("receiptNo").alias("RECEIPT_NO"),	
					pm.get("entryDateTime").alias("ENTRY_DATE"),pm.get("amendmentDate").alias("AMENDMENT_DATE"),
					pm.get("enteringMode").alias("ENTERING_MODE"),pm.get("otherCostOc").alias("OTHER_COST_OC"),
					pm.get("cedantReference").alias("CEDANT_REFERENCE"),pm.get("remarks").alias("REMARKS"),
					pm.get("netdueOc").alias("NETDUE_OC"),pm.get("totalCrOc").alias("TOTAL_CR_OC"),
					pm.get("totalDrOc").alias("TOTAL_DR_OC"),pm.get("settlementStatus").alias("SETTLEMENT_STATUS"),
					pm.get("withHoldingTaxOc").alias("WITH_HOLDING_TAX_OC"),pm.get("withHoldingTaxDc").alias("WITH_HOLDING_TAX_DC"),
					pm.get("riCession").alias("RI_CESSION"),pm.get("tdsOc").alias("TDS_OC"),
					pm.get("stDc").alias("ST_DC"),pm.get("bonusOc").alias("BONUS_OC"),pm.get("stOc").alias("ST_OC"),
					pm.get("bonusDc").alias("BONUS_DC"),pm.get("reverseTransactionNo").alias("REVERSE_TRANSACTION_NO"),
					pm.get("reverselStatus").alias("REVERSEL_STATUS"),pm.get("productId").alias("PRODUCT_ID"),
					pm.get("premiumClass").alias("PREMIUM_CLASS"),pm.get("contractNo").alias("CONTRACT_NO")); 

			// Where
			Predicate n1 = cb.equal(pm.get("requestNo"), req.getRequestNo());
			query.where(n1);

			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();
		
			 if(list.size()>0)
			 {
				 for(int i=0;i<list.size();i++){
					 Tuple	 editPremium=list.get(i);
					 GetPremiumDetailsRes1 bean = new GetPremiumDetailsRes1();						
					bean.setTransaction(editPremium.get("TRANS_DATE")==null?"":editPremium.get("TRANS_DATE").toString()); 
					if("EP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
			    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
			    	}
					else if("RTP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
			    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
			    	}
					else if("RVP".equalsIgnoreCase(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString()))
			    	{
			    		bean.setAccountPeriod(editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString());
			    	}
					else
			    	{
			    		bean.setAccountPeriod((editPremium.get("INSTALMENT_NUMBER")==null?"":editPremium.get("INSTALMENT_NUMBER").toString())+"_"+(editPremium.get("ACCOUNT_PERIOD_QTR")==null?"":editPremium.get("ACCOUNT_PERIOD_QTR").toString()));
			    	}
						bean.setCurrencyId(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
						bean.setCurrency(editPremium.get("CURRENCY_ID")==null?"":editPremium.get("CURRENCY_ID").toString());
						if((editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString())==""){
							GetCommonValueRes com = dropDowmImpl.GetExchangeRate(bean.getCurrencyId(),bean.getTransaction(),req.getCountryId(),req.getBranchCode());
							bean.setExchRate(com.getCommonResponse());
						}
						else{
						bean.setExchRate(dropDowmImpl.exchRateFormat(editPremium.get("EXCHANGE_RATE")==null?"":editPremium.get("EXCHANGE_RATE").toString()));
						}
						bean.setPremiumQuotaShare(fm.formatter(editPremium.get("PREMIUM_QUOTASHARE_OC")==null?"":editPremium.get("PREMIUM_QUOTASHARE_OC").toString()));
						bean.setCommissionview(dropDowmImpl.formatterpercentage(editPremium.get("COMMISSION") ==null?"":editPremium.get("COMMISSION").toString()));
						bean.setCommissionQuotaShare(editPremium.get("COMMISSION_QUOTASHARE_OC")==null?"":editPremium.get("COMMISSION_QUOTASHARE_OC").toString());
						bean.setBrokerageview(editPremium.get("BROKERAGE")==null?"":editPremium.get("BROKERAGE").toString());
						bean.setBrokerage(editPremium.get("BROKERAGE_AMT_OC")==null?"":editPremium.get("BROKERAGE_AMT_OC").toString());
						bean.setTaxview(editPremium.get("TAX")==null?"":editPremium.get("TAX").toString());
						bean.setTax(editPremium.get("TAX_AMT_OC")==null?"":editPremium.get("TAX_AMT_OC").toString());
		     			bean.setStatus(editPremium.get("STATUS")==null?"":editPremium.get("STATUS").toString());
						bean.setReceiptno(editPremium.get("RECEIPT_NO")==null?"":editPremium.get("RECEIPT_NO").toString());		
						bean.setInceptionDate(editPremium.get("ENTRY_DATE")==null?"":editPremium.get("ENTRY_DATE").toString());
						bean.setEnteringMode(editPremium.get("ENTERING_MODE")==null?"":editPremium.get("ENTERING_MODE").toString().trim());
						bean.setOtherCost(editPremium.get("OTHER_COST_OC")==null?"":editPremium.get("OTHER_COST_OC").toString());
						bean.setCedentRef(editPremium.get("CEDANT_REFERENCE")==null?"":editPremium.get("CEDANT_REFERENCE").toString());
						bean.setRemarks(editPremium.get("REMARKS")==null?"":editPremium.get("REMARKS").toString());
						bean.setNetDue(editPremium.get("NETDUE_OC")==null?"":editPremium.get("NETDUE_OC").toString());
						bean.setWithHoldingTaxOC(fm.formatter(editPremium.get("WITH_HOLDING_TAX_OC")==null?"0":editPremium.get("WITH_HOLDING_TAX_OC").toString()));
						bean.setTaxDedectSource(fm.formatter(editPremium.get("TDS_OC")==null?"0":editPremium.get("TDS_OC").toString()));
			            bean.setServiceTax(fm.formatter(editPremium.get("ST_OC")==null?"0":editPremium.get("ST_OC").toString()));
			            bean.setBonus(fm.formatter(editPremium.get("BONUS_OC")==null?"0":editPremium.get("BONUS_OC").toString()));
						bean.setTotalCredit(editPremium.get("TOTAL_CR_OC")==null?"":editPremium.get("TOTAL_CR_OC").toString());						
						bean.setTotalDebit(editPremium.get("TOTAL_DR_OC")==null?"":editPremium.get("TOTAL_DR_OC").toString());
						bean.setAmendmentDate(editPremium.get("AMENDMENT_DATE")==null?"":editPremium.get("AMENDMENT_DATE").toString());
						bean.setRiCession(editPremium.get("RI_CESSION")==null?"":editPremium.get("RI_CESSION").toString());
						bean.setStatementDate(editPremium.get("STATEMENT_DATE")==null?"":editPremium.get("STATEMENT_DATE").toString());
						bean.setChooseTransaction(editPremium.get("REVERSEL_STATUS")==null?"":editPremium.get("REVERSEL_STATUS").toString() );
			            bean.setTransDropDownVal(editPremium.get("REVERSE_TRANSACTION_NO")==null?"":editPremium.get("REVERSE_TRANSACTION_NO").toString() );
			            bean.setProductId(editPremium.get("PRODUCT_ID")==null?"":editPremium.get("PRODUCT_ID").toString());
			            bean.setDepartmentId(editPremium.get("PREMIUM_CLASS")==null?"":editPremium.get("PREMIUM_CLASS").toString());
			            bean.setContNo(editPremium.get("CONTRACT_NO")==null?"":editPremium.get("CONTRACT_NO").toString());
			            resList.add(bean);
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
}
