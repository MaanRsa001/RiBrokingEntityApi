package com.maan.insurance.service.impl.retroClaim;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TtrnRetroClaimPayment;
import com.maan.insurance.model.entity.TtrnRetroClaimUpdation;
import com.maan.insurance.model.repository.TtrnRetroClaimPaymentRepository;
import com.maan.insurance.model.req.retroClaim.ContractDetailsMode1Req;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.model.res.retroClaim.ContractDetailsMode1Res;
import com.maan.insurance.model.res.retroClaim.ContractDetailsMode1Res1;
import com.maan.insurance.model.res.retroClaim.ContractDetailsMode1Res2;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.retroClaim.RetroClaimService;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;
@Service
public class RetroClaimServiceImple implements RetroClaimService{
	private Logger log = LogManager.getLogger(RetroClaimServiceImple.class);

	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;

	@Autowired
	private ValidationImple vi;
	@Autowired
	private TtrnRetroClaimPaymentRepository rcpRepo;
	
	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();
	private Query query1 = null;

	public RetroClaimServiceImple() {
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
	public CommonSaveRes getProposalNo(String departmentId, String contractNo, String layerNo,  String productId) {
		CommonSaveRes response = new CommonSaveRes();
		String proposalNo="";
		 CriteriaBuilder cb = em.getCriteriaBuilder(); 
		 CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		try {	
			// like table name
			Root<PositionMaster> pm = query.from(PositionMaster.class);

			// Select
			query.multiselect(pm.get("proposalNo").alias("PROPOSAL_NO"),pm.get("deptId").alias("DEPT_ID")); 
			
		 if("4".equals(productId)){
			 	//GET_PRO_PROPOSAL_NO

				// MAXAmend ID
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
				Predicate a2 = cb.equal( pm.get("deptId"), pms.get("deptId"));
				amend.where(a1,a2);

				// Where
				Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
				Predicate n2 = cb.equal(pm.get("deptId"), departmentId);
				Predicate n3 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2,n3);
			
		}
		else if("5".equals(productId)){
			//GET_XOL_PROPOSAL_NO
			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a2 = cb.equal( pm.get("layerNo"), pms.get("layerNo"));
			amend.where(a1,a2);

			// Where
			Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
			Predicate n2 = cb.equal(pm.get("layerNo"), layerNo==null?0:layerNo);
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3);
		}
		// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();
			
		if(list!=null &&list.size()>0){
			Tuple map = list.get(0);
			proposalNo = map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString();
			if(!"4".equalsIgnoreCase(productId)){
				departmentId = map.get("DEPT_ID")==null?"":map.get("DEPT_ID").toString();
			}
		}
		response.setResponse(proposalNo);
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
	public CommonSaveRes getShortname(String branchCode) {
		CommonSaveRes response = new CommonSaveRes();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			
			Root<TmasBranchMaster> pm = query.from(TmasBranchMaster.class);

			// GET_SHORT_NAME
			Subquery<String> name = query.subquery(String.class); 
			Root<CurrencyMaster> cm = name.from(CurrencyMaster.class);
			name.select(cm.get("shortName"));
			// MAXAmend ID
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<CurrencyMaster> cms = maxAmend.from(CurrencyMaster.class);
			maxAmend.select(cb.max(cms.get("amendId")));
			Predicate b1 = cb.equal( cms.get("currencyId"), cm.get("currencyId"));
			Predicate b2 = cb.equal( cms.get("branchCode"), cm.get("branchCode"));
			maxAmend.where(b1,b2);
			
			Predicate a1 = cb.equal( cm.get("currencyId"), pm.get("baseCurrencyId"));
			Predicate a2 = cb.equal( cm.get("branchCode"), pm.get("branchCode"));
			Predicate a3 = cb.equal( cm.get("amendId"), maxAmend);
			name.where(a1,a2,a3);
			
			// Select
			query.select(name); 

			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("status"), "Y");
			query.where(n1,n2);
			
			// Get Result
			TypedQuery<String> res = em.createQuery(query);
			List<String> list = res.getResultList();
			String shortName = "";
			if(list.size()>0) {
				shortName = list.get(0);
			}
		response.setResponse(shortName);	
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
	public ContractDetailsMode1Res contractDetailsMode1(ContractDetailsMode1Req req) {
		ContractDetailsMode1Res response = new ContractDetailsMode1Res();
		ContractDetailsMode1Res1 res = new ContractDetailsMode1Res1();
		List<ContractDetailsMode1Res2> res2List = new ArrayList<ContractDetailsMode1Res2>();
		String[] args =null;
		String query1 = "";
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			if(StringUtils.isNotBlank(req.getClaimNo()))
			{
					//CLAIM_SELECT_SUMPAIDAMT_RETRO
					List<TtrnRetroClaimPayment> list =	rcpRepo.findByClaimNoAndContractNo(req.getClaimNo(),req.getPolicyContractNo());
					Double paidAmt = list.stream().filter(o -> o.getPaidAmountOc() != null).mapToDouble(o -> o.getPaidAmountOc().doubleValue()).sum();
					res.setSumOfPaidAmountOC(fm.formatter(String.valueOf(paidAmt)));
					
					//CLAIM_SELECT_REVSUMPAIDAMT_RETRO
					
					CriteriaQuery<Long> query = cb.createQuery(Long.class); 
					
					Root<TtrnRetroClaimPayment> rd = query.from(TtrnRetroClaimPayment.class);

					// Select
					query.select(cb.sum(rd.get("paidAmountOc"))); 

					// maxEnd
					Subquery<Long> reverse = query.subquery(Long.class); 
					Root<TtrnRetroClaimUpdation> rds = reverse.from(TtrnRetroClaimUpdation.class);
					reverse.select(cb.max(rds.get("slNo")));
					Predicate a1 = cb.equal( rds.get("claimNo"), rd.get("claimNo"));
					Predicate a2 = cb.equal( rds.get("contractNo"), rd.get("contractNo"));
					reverse.where(a1,a2);

					// Where
					Predicate n1 = cb.equal(rd.get("claimNo"), req.getClaimNo());
					Predicate n2 = cb.equal(rd.get("contractNo"), req.getPolicyContractNo());
					Predicate n3 = cb.equal(rd.get("reserveId"), reverse==null?1:reverse);
					query.where(n1,n2,n3);

					// Get Result
					TypedQuery<Long> result = em.createQuery(query);
					List<Long> list1 = result.getResultList();
					
					res.setRevSumOfPaidAmt(fm.formatter(String.valueOf(list1.get(0))));
			}
				if("5".equals(req.getProductId())){
					query1= "claim.select.xolOrTeatyGetClimeQuery";
				}
				else{
					query1= "CLAIM_SELECT_RETRO_GETCLIMEQUERY";
				}
				args=new String[5];
				args[0]=req.getProposalNo();
				args[1]=req.getProductId();
				args[2]=req.getBranchCode();
				args[3]=req.getBranchCode();
				args[4]=req.getProposalNo();
			
				List<Map<String, Object>> list = queryImpl.selectList(query1,args);
				if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> contractDetails=(Map<String,Object>) list.get(i);
					ContractDetailsMode1Res2 res2 = new ContractDetailsMode1Res2();
					res2.setPolicyContractNo(contractDetails.get("RSK_CONTRACT_NO")==null?"":contractDetails.get("RSK_CONTRACT_NO").toString());
					res2.setAmendId(contractDetails.get("RSK_ENDORSEMENT_NO")==null?"":contractDetails.get("RSK_ENDORSEMENT_NO").toString());
					res2.setCedingcompanyName(contractDetails.get("CEDING_COMPANY")==null?"":contractDetails.get("CEDING_COMPANY").toString());
					res2.setCedingCompanyCode(contractDetails.get("RSK_CEDINGID")==null?"":contractDetails.get("RSK_CEDINGID").toString());
					res2.setProposalNo(contractDetails.get("RSK_PROPOSAL_NUMBER")==null?"":contractDetails.get("RSK_PROPOSAL_NUMBER").toString());
					if(StringUtils.isBlank(req.getDepartmentId())){
						res2.setDepartmentId(contractDetails.get("RSK_DEPTID")==null?"":contractDetails.get("RSK_DEPTID").toString());
						res2.setDepartmentClass(contractDetails.get("RSK_DEPTID")==null?"":contractDetails.get("RSK_DEPTID").toString());
					}
					res2.setUwYear(contractDetails.get("RSK_UWYEAR")==null?"":contractDetails.get("RSK_UWYEAR").toString());
					
					if(StringUtils.isBlank(req.getCurrecny())){
						res2.setCurrecny(contractDetails.get("RSK_ORIGINAL_CURR")==null?"":contractDetails.get("RSK_ORIGINAL_CURR").toString());
					}
					
					res2.setSignedShare(contractDetails.get("RSK_SHARE_SIGNED")==null?"0":contractDetails.get("RSK_SHARE_SIGNED").toString());
					res2.setLimitOrigCurr(contractDetails.get("RSK_LIMIT_OC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_OC").toString()));
					res2.setLimitOurshareUSD(contractDetails.get("RSK_LIMIT_DC")==null?"":fm.formatter(contractDetails.get("RSK_LIMIT_DC").toString()));
						res2.setSumInsOSOC(getShareVal(contractDetails.get("RSK_LIMIT_OC")==null?"0":contractDetails.get("RSK_LIMIT_OC").toString(),res2.getSignedShare(),"share"));
						res2.setSumInsOSDC(getShareVal(contractDetails.get("RSK_LIMIT_DC")==null?"0":contractDetails.get("RSK_LIMIT_DC").toString(),res2.getSignedShare(),"share"));
						res2.setDepartmentName(contractDetails.get("TMAS_DEPARTMENT_NAME")==null?"":contractDetails.get("TMAS_DEPARTMENT_NAME").toString());
					res2.setSubProfitCenter(contractDetails.get("TMAS_SPFC_NAME")==null?"":contractDetails.get("TMAS_SPFC_NAME").toString());
					res2.setRetention(contractDetails.get("RSK_CEDANT_RETENTION")==null?"":fm.formatter(contractDetails.get("RSK_CEDANT_RETENTION").toString()));
					res2.setFrom(contractDetails.get("INCP_DATE")==null?"":contractDetails.get("INCP_DATE").toString());
					res2.setTo(contractDetails.get("EXP_DATE")==null?"":contractDetails.get("EXP_DATE").toString());
					res2.setTreatyName(contractDetails.get("RSK_TREATYID")==null?"":contractDetails.get("RSK_TREATYID").toString());
					res2.setBrokercode(contractDetails.get("RSK_BROKERID")==null?"":contractDetails.get("RSK_BROKERID").toString());
					res2.setBrokerName(contractDetails.get("BROKER_NAME")==null?"":contractDetails.get("BROKER_NAME").toString());
					res2.setAcceptenceDate(contractDetails.get("RSK_ACCOUNT_DATE")==null?"":contractDetails.get("RSK_ACCOUNT_DATE").toString());
					String count="";
					if("4".equals(req.getProductId())){
						count= dropDowmImpl.getCombinedClass(req.getBranchCode(),req.getProductId(),req.getDepartmentId());
					}
					if(StringUtils.isBlank(count)){
					res2.setClaimdepartId(contractDetails.get("RSK_DEPTID")==null?"":contractDetails.get("RSK_DEPTID").toString());
					res2.setConsubProfitId(contractDetails.get("RSK_SPFCID")==null?"":contractDetails.get("RSK_SPFCID").toString());
					}
					if(StringUtils.isBlank(req.getInsuredName())){
					res2.setInsuredName(contractDetails.get("RSK_INSURED_NAME")==null?"":contractDetails.get("RSK_INSURED_NAME").toString());
					}
					res2.setProposalType(contractDetails.get("RSK_PROPOSAL_TYPE")==null?"":contractDetails.get("RSK_PROPOSAL_TYPE").toString());
					res2.setBasis(contractDetails.get("RSK_BASIS")==null?"":contractDetails.get("RSK_BASIS").toString());
					
					if("5".equalsIgnoreCase(req.getProductId()))
					{
						res2.setNatureofCoverage(contractDetails.get("RSK_PF_COVERED")==null?"":contractDetails.get("RSK_PF_COVERED").toString());
						res2.setReinstatementPremium(contractDetails.get("RSK_REINSTATEMENT_PREMIUM")==null?"":contractDetails.get("RSK_REINSTATEMENT_PREMIUM").toString());
						if(!"Y".equalsIgnoreCase(res2.getReinstatementPremium())){
							res2.setReinstType("NA");
							res2.setReinstPremiumOCOS("0");
						}
					}
					if("4".equalsIgnoreCase(req.getProductId()))
					{
						res2.setNatureofCoverage(contractDetails.get("RSK_RISK_COVERED")==null?"":contractDetails.get("RSK_RISK_COVERED").toString());
						res2.setCashLossOSOC(fm.formatter(((Double.parseDouble(contractDetails.get("RSK_CASHLOSS_LMT_OC").toString()==null?"0":contractDetails.get("RSK_CASHLOSS_LMT_OC").toString())*Double.parseDouble(contractDetails.get("RSK_SHARE_SIGNED")==null?"0":contractDetails.get("RSK_SHARE_SIGNED").toString()))/100.0)+""));
						res2.setCashLossOSDC(fm.formatter(((Double.parseDouble(contractDetails.get("RSK_CASHLOSS_LMT_DC").toString()==null?"0":contractDetails.get("RSK_CASHLOSS_LMT_DC").toString())*Double.parseDouble(contractDetails.get("RSK_SHARE_SIGNED").toString()==null?"0":contractDetails.get("RSK_SHARE_SIGNED").toString()))/100.0)+""));
					}
					res2List.add(res2);
					}
				}
				res.setContractDetails(res2List);
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
	private String getShareVal(String lossAdvise, String signedShare, String type) {
		String res="";
		try{
			if(StringUtils.isBlank(lossAdvise)){
				lossAdvise="0";
			}
			if(StringUtils.isBlank(signedShare)){
				signedShare="0";
			}
			if("share".equalsIgnoreCase(type)){
			res=Double.toString((Double.parseDouble(lossAdvise)*Double.parseDouble(signedShare))/100);
			}
			res=fm.formatter(res);
		}
		catch (Exception e) {
		}
		return res;
	}
}
