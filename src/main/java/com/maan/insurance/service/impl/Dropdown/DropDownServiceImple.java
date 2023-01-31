package com.maan.insurance.service.impl.Dropdown;


import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.jpa.entity.facultative.TtrnFacRiskProposal;
import com.maan.insurance.jpa.entity.facultative.TtrnFacSi;
import com.maan.insurance.jpa.entity.treasury.BankMaster;
import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.jpa.entity.xolpremium.TtrnMndInstallments;
import com.maan.insurance.jpa.mapper.TtrnPaymentReceiptDetailsMapper;
import com.maan.insurance.jpa.repository.propPremium.TtrnDepositReleaseRepository;
import com.maan.insurance.jpa.repository.treasury.TreasuryCustomRepository;
import com.maan.insurance.model.entity.CompanyMaster;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CountryMaster;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.ExchangeMaster;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.StatusMaster;
import com.maan.insurance.model.entity.SubStatusMaster;
import com.maan.insurance.model.entity.TerritoryMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasCrestaMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasDocTypeMaster;
import com.maan.insurance.model.entity.TmasOpenPeriod;
import com.maan.insurance.model.entity.TmasPfcMaster;
import com.maan.insurance.model.entity.TmasPolicyBranch;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TmasSpfcMaster;
import com.maan.insurance.model.entity.TmasTerritory;
import com.maan.insurance.model.entity.TtrnBonus;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.entity.TtrnIeModule;
import com.maan.insurance.model.entity.TtrnPttySection;
import com.maan.insurance.model.entity.TtrnRetroClaimDetails;
import com.maan.insurance.model.entity.TtrnRiPlacement;
import com.maan.insurance.model.entity.TtrnRip;
import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.entity.TtrnRskClassLimits;
import com.maan.insurance.model.entity.UnderwritterCapacityMaster;
import com.maan.insurance.model.entity.UnderwritterMaster;
import com.maan.insurance.model.repository.BankMasterRepository;
import com.maan.insurance.model.repository.ConstantDetailRepository;
import com.maan.insurance.model.repository.PositionMasterRepository;
import com.maan.insurance.model.repository.StatusMasterRepository;
import com.maan.insurance.model.repository.SubStatusMasterRepository;
import com.maan.insurance.model.repository.TerritoryMasterRepository;
import com.maan.insurance.model.repository.TmasCrestaMasterRepository;
import com.maan.insurance.model.repository.TmasDepartmentMasterRepository;
import com.maan.insurance.model.repository.TmasOpenPeriodRepository;
import com.maan.insurance.model.repository.TmasPfcMasterRepository;
import com.maan.insurance.model.repository.TmasPolicyBranchRepository;
import com.maan.insurance.model.repository.TmasSpfcMasterRepository;
import com.maan.insurance.model.repository.TtrnClaimDetailsRepository;
import com.maan.insurance.model.repository.TtrnInsurerDetailsRepository;
import com.maan.insurance.model.repository.TtrnMndInstallmentsRepository;
import com.maan.insurance.model.repository.TtrnPttySectionRepository;
import com.maan.insurance.model.repository.TtrnRetroCessionaryRepository;
import com.maan.insurance.model.repository.TtrnRetroClaimDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiPlacementRepository;
import com.maan.insurance.model.repository.TtrnRiskCommissionRepository;
import com.maan.insurance.model.repository.TtrnSoaDueRepository;
import com.maan.insurance.model.repository.UnderwritterCapacityMasterRepository;
import com.maan.insurance.model.repository.UnderwritterMasterRepository;
import com.maan.insurance.model.req.DropDown.DuplicateCountCheckReq;
import com.maan.insurance.model.req.DropDown.GetClaimDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetContractLayerNoReq;
import com.maan.insurance.model.req.DropDown.GetCopyQuoteReq;
import com.maan.insurance.model.req.DropDown.GetCurrencyIdReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentieModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetInwardBusinessTypeDropDownReq;
import com.maan.insurance.model.req.DropDown.GetPlacedProposalListReq;
import com.maan.insurance.model.req.DropDown.GetPreDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProductModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProfitCentreieModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProposalNoReq;
import com.maan.insurance.model.req.DropDown.GetSectionListReq;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiDropDownReq;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiReq;
import com.maan.insurance.model.req.DropDown.GetTreatyTypeDropDownReq;
import com.maan.insurance.model.req.DropDown.GetYearToListValueReq;
import com.maan.insurance.model.req.DropDown.updateBqEditModeReq;
import com.maan.insurance.model.req.DropDown.updateSubEditModeReq;
import com.maan.insurance.model.req.placement.GetPlacementInfoListReq;
import com.maan.insurance.model.req.proportionality.ContractReq;
import com.maan.insurance.model.req.proportionality.GetRetroContractDetailsListReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.CommonResponse;
import com.maan.insurance.model.res.DropDown.GetBouquetCedentBrokerInfoRes;
import com.maan.insurance.model.res.DropDown.GetBouquetCedentBrokerInfoRes1;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes;
import com.maan.insurance.model.res.DropDown.GetBouquetExistingListRes1;
import com.maan.insurance.model.res.DropDown.GetBouquetListRes;
import com.maan.insurance.model.res.DropDown.GetBouquetListRes1;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.DropDown.GetCompanyInfoRes;
import com.maan.insurance.model.res.DropDown.GetCompanyInfoRes1;
import com.maan.insurance.model.res.DropDown.GetContractValRes;
import com.maan.insurance.model.res.DropDown.GetContractValidationRes;
import com.maan.insurance.model.res.DropDown.GetNewContractInfoRes;
import com.maan.insurance.model.res.DropDown.GetNewContractInfoRes1;
import com.maan.insurance.model.res.DropDown.GetNotPlacedProposalListRes;
import com.maan.insurance.model.res.DropDown.GetNotPlacedProposalListRes1;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes1;
import com.maan.insurance.model.res.DropDown.GetPlacementInfoListRes;
import com.maan.insurance.model.res.DropDown.GetPlacementInfoListRes1;
import com.maan.insurance.model.res.DropDown.GetYearToListValueRes;
import com.maan.insurance.model.res.DropDown.GetYearToListValueRes1;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.service.Dropdown.DropDownService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.validation.Formatters;

@Service
public class DropDownServiceImple implements DropDownService{
	private Logger log = LogManager.getLogger(DropDownServiceImple.class);
	private List<Map<String, Object>> department;
	@Autowired
	private QueryImplemention queryImpl;
	@Autowired
	private TreasuryCustomRepository treasuryCustomRepository;
	@Autowired
	private TtrnPaymentReceiptDetailsMapper ttrnPaymentReceiptDetailsMapper;
	@Autowired
	private TmasSpfcMasterRepository spfcRepo;
	@Autowired
	private TtrnClaimDetailsRepository claimRepo;
	@Autowired
	private TtrnRetroClaimDetailsRepository retroClaimRepo;
	@Autowired
	private  TtrnPttySectionRepository pttyRepo;
	@Autowired
	private  TtrnDepositReleaseRepository repo;
	@Autowired
	private  BankMasterRepository bankRepo;
	@Autowired
	private  ConstantDetailRepository constRepo;
	@Autowired
	private  TmasPolicyBranchRepository polRepo;
	@Autowired
	private  TmasPfcMasterRepository pfcRepo;
	@Autowired
	private  TerritoryMasterRepository tmRepo;
	@Autowired
	private  UnderwritterCapacityMasterRepository uwcRepo;
	@Autowired
	private  TmasCrestaMasterRepository cresRepo;
	@Autowired
	private  TtrnSoaDueRepository ttrnSoaDueRepository;
	
	
	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
//	@Autowired
//	private DropDownValidation dropDownValidation;

	
	@Autowired
	private Formatters fm;
	@Autowired
	private TtrnRiskCommissionRepository rcRepo;
	@Autowired
	private TtrnMndInstallmentsRepository mndRepo;
	@Autowired
	private TtrnInsurerDetailsRepository idRepo;
	@Autowired
	private TtrnRetroCessionaryRepository cessRepo;
	@Autowired
	private TmasOpenPeriodRepository tmasOpenRepo;
	@Autowired
	private TmasDepartmentMasterRepository deptRepo;
	@Autowired
	private StatusMasterRepository smRepo;
	@Autowired
	private SubStatusMasterRepository ssmRepo;
	@Autowired
	private TtrnRiPlacementRepository riPlaceRepo;
	@Autowired
	private UnderwritterMasterRepository uwRepo;
	
	@Autowired
	private PositionMasterRepository pmRepo;
	@PersistenceContext
	private EntityManager em;


	@Override
	public GetCommonValueRes EditModeStatus(String proposalNo) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result="";
		try{
			//POS_MAS_ED_MODE_SELECT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			Root<PositionMaster> rd = query.from(PositionMaster.class);

			query.multiselect(cb.selectCase().when(cb.isNull(rd.get("editMode")), "N")
					.otherwise(rd.get("editMode"))); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> rds = amend.from(PositionMaster.class);
			amend.select(cb.max(rds.get("amendId")));
			Predicate a1 = cb.equal( rds.get("proposalNo"), rd.get("proposalNo"));
			amend.where(a1);

			Predicate n1 = cb.equal(rd.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(rd.get("amendId"), amend);
			query.where(n1,n2);

			TypedQuery<String> res = em.createQuery(query);
			List<String> list = res.getResultList();
			
			if (!CollectionUtils.isEmpty(list)) {
				result = list.get(0);
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonValueRes getCoverLimitAmount(String proposalNo, String departmentId, String productId) {
		GetCommonValueRes response = new GetCommonValueRes();
		String coverLimitAmount="0";
		List<String> list = null;
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 

			if("1".equalsIgnoreCase(productId)){
				//GET_CLAIM_COVERLIMIT_FAC
				CriteriaQuery<String> query = cb.createQuery(String.class); 
				Root<TtrnFacSi> rd = query.from(TtrnFacSi.class);

				Expression<Integer> exp = cb.sum(cb.<Integer>selectCase().when(cb.isNull(rd.<Integer>get("rskCoverlimitOc")),0)
						.otherwise(rd.<Integer>get("rskCoverlimitOc")));
				
				query.multiselect(exp.as(String.class).alias("RSK_COVERLIMIT_OC"));
				
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnFacSi> rds = amend.from(TtrnFacSi.class);
				amend.select(cb.max(rds.get("amendId")));
				Predicate a1 = cb.equal( rds.get("proposalNo"), rd.get("proposalNo"));
				amend.where(a1);

				Predicate n1 = cb.equal(rd.get("proposalNo"), proposalNo);
				Predicate n2 = cb.equal(rd.get("amendId"), amend);
				query.where(n1,n2);

				TypedQuery<String> res = em.createQuery(query);
				 list = res.getResultList();
				
			}else{
				//GET_CLAIM_COVERLIMIT
				CriteriaQuery<String> query = cb.createQuery(String.class); 
				Root<TtrnRskClassLimits> rd = query.from(TtrnRskClassLimits.class);
				
				Expression<Integer> exp = cb.<Integer>selectCase().when(cb.isNull(rd.<Integer>get("rskCoverLimt")),0)
						.otherwise(rd.<Integer>get("rskCoverLimt"));

				query.multiselect(exp.as(String.class).alias("RSK_COVERLIMIT_OC"));
				
				//end
				Subquery<Long> end = query.subquery(Long.class); 
				Root<TtrnRskClassLimits> rds = end.from(TtrnRskClassLimits.class);
				end.select(cb.max(rds.get("rskEndorsementNo")));
				Predicate a1 = cb.equal( rds.get("rskProposalNumber"), rd.get("rskProposalNumber"));
				Predicate a2 = cb.equal( rds.get("rskLayerNo"), rd.get("rskLayerNo"));
				Predicate a3 = cb.equal( rds.get("rskCoverClass"), rd.get("rskCoverClass"));
				end.where(a1,a2,a3);

				Predicate n1 = cb.equal(rd.get("rskProposalNumber"), proposalNo);
				Predicate n2 = cb.equal(rd.get("rskEndorsementNo"), end);
				Predicate n3 = cb.equal(rd.get("rskCoverClass"), departmentId);
				query.where(n1,n2,n3);

				TypedQuery<String> res = em.createQuery(query);
				list = res.getResultList();
			}
			if (!CollectionUtils.isEmpty(list)) {
				coverLimitAmount = list.get(0);
			}
			response.setCommonResponse(coverLimitAmount);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetOpenPeriodRes getOpenPeriod(String branchCode) {
		GetOpenPeriodRes response = new GetOpenPeriodRes();
		List<GetOpenPeriodRes1> finalList = new ArrayList<GetOpenPeriodRes1>();
		GetOpenPeriodRes1 res = new GetOpenPeriodRes1();
		String openPeriodDate="";
		try {
		//GET_OPEN_PERIOD_DATE
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
		Root<TmasOpenPeriod> root = cq.from(TmasOpenPeriod.class);

		cq.multiselect(root.get("startDate").alias("START_DATE"), root.get("endDate").alias("END_DATE")).where(cb.equal(root.get("status"), "Y"),
				cb.equal(root.get("branchCode"), branchCode));

		TypedQuery<Tuple> q = em.createQuery(cq);
		List<Tuple> list = q.getResultList();
		
		for(int i=0 ; i<list.size() ; i++) {
			Tuple tempMap =  list.get(i);
			res.setOpstartDate(tempMap.get("START_DATE")==null?"":tempMap.get("START_DATE").toString());
			res.setOpendDate(tempMap.get("END_DATE")==null?"":tempMap.get("END_DATE").toString());
			openPeriodDate=openPeriodDate+res.getOpstartDate()+" to "+res.getOpendDate()+" ,";
			finalList.add(res);
		}
		if(StringUtils.isNotBlank(openPeriodDate))
		openPeriodDate = openPeriodDate.substring(0, openPeriodDate.length() - 1);
		response.setOpenPeriodDate(openPeriodDate);
		response.setCommonResponse(finalList);
		response.setMessage("Success");
		response.setIsError(false);
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getCurrencyMasterDropDown(String branchCode, String countryId) {
		List<CommonResDropDown> personalInfo = new ArrayList<CommonResDropDown>();
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		try{
			//common.select.getCurrencyList
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<CurrencyMaster> rd = query.from(CurrencyMaster.class);

			query.multiselect(rd.get("currencyId").alias("CURRENCY_ID"),rd.get("shortName").alias("CURRENCY_NAME")).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<CurrencyMaster> rds = amend.from(CurrencyMaster.class);
			amend.select(cb.max(rds.get("amendId")));
			Predicate a1 = cb.equal( rds.get("branchCode"), rd.get("branchCode"));
			Predicate a2 = cb.equal( rds.get("countryId"), rd.get("countryId"));
			Predicate a3 = cb.equal( rds.get("status"), rd.get("status"));
			Predicate a4 = cb.equal( rds.get("currencyId"), rd.get("currencyId"));
			amend.where(a1,a2,a3,a4);

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("currencyId")));
			
			Predicate n1 = cb.equal(rd.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(rd.get("countryId"), countryId);
			Predicate n3 = cb.equal(rd.get("status"), "Y");
			Predicate n4 = cb.equal(rd.get("amendId"), amend);
			query.where(n1,n2,n3,n4).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			for(int i=0 ; i<list.size() ; i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap =  list.get(i);
				res.setCode(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
				res.setCodeDescription(tempMap.get("CURRENCY_NAME")==null?"":tempMap.get("CURRENCY_NAME").toString());
				personalInfo.add(res);
			}
			response.setCommonResponse(personalInfo);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonValueRes getDepartmentName(String branchCode, String productCode, String deptCode) {
		GetCommonValueRes response = new GetCommonValueRes();
		String deptName="";
		try{
			//common.select.getDeptName
			TmasDepartmentMaster list = deptRepo.findByBranchCodeAndTmasProductIdAndTmasDepartmentIdAndTmasStatus(branchCode,
					new BigDecimal(productCode),new BigDecimal(deptCode),"Y");
			if (list!= null) {
				deptName = list.getTmasDepartmentName() == null ? ""	
						: list.getTmasDepartmentName().toString();
			}
			response.setCommonResponse(deptName);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}

		return response;
	}

	@Override
	public GetCommonDropDownRes getClaimDepartmentDropDown(GetClaimDepartmentDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> department = new ArrayList<CommonResDropDown>();
		List<Tuple> list= null;
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			String count="";
			if("2".equals(req.getProductId())){
				count=getCombinedClass(req.getBranchCode(),req.getProductId(),req.getDepartmentId());
			}
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TmasDepartmentMaster> rd = query.from(TmasDepartmentMaster.class);

			query.multiselect(rd.get("tmasDepartmentId").alias("TMAS_DEPARTMENT_ID"),rd.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME")).distinct(true); 

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("tmasDepartmentName")));
			
			Predicate n1 = cb.equal(rd.get("branchCode"), req.getBranchCode());
			Predicate n2 = cb.equal(rd.get("tmasProductId"), req.getProductId());
			
			if(StringUtils.isNotBlank(count) && "2".equals(req.getProductId())){
				//common.department.combined.premiumclaim
				
				
				Predicate n3 = cb.equal(rd.get("tmasStatus"), "Y");
				List<String> dept =new ArrayList<String>(Arrays.asList(count.split(","))) ;
				Expression<String> e0=rd.get("tmasDepartmentId");
				Predicate n4 = e0.in(dept);
				query.where(n1,n2,n3,n4).orderBy(orderList);

				TypedQuery<Tuple> result = em.createQuery(query);
				list = result.getResultList();
				
			}else{
				  //common.select.getDepartmentList.preClaim
				//dept
				Subquery<String> dept = query.subquery(String.class); 
				Root<TtrnRskClassLimits> pi = dept.from(TtrnRskClassLimits.class);
				dept.select(pi.get("rskCoverClass"));
				//maxAmend
				Subquery<Long> end = query.subquery(Long.class); 
				Root<TtrnRskClassLimits> pis = end.from(TtrnRskClassLimits.class);
				end.select(cb.max(pis.get("rskEndorsementNo")));
				Predicate c1 = cb.equal( pis.get("rskProposalNumber"), pi.get("rskProposalNumber"));
				end.where(c1);
				Predicate d1 = cb.equal( pi.get("rskContractNo"), req.getContractNo());
				Predicate d2 = cb.equal( pi.get("rskLayerNo"), req.getLayerNo());
				Predicate d3 = cb.equal( pi.get("rskEndorsementNo"), end);
				dept.where(d1,d2,d3);
				
				Predicate n3 = cb.equal(rd.get("tmasStatus"), req.getStatus());
				Expression<String> e0=rd.get("tmasDepartmentId");
				Predicate n4 = e0.in(dept==null?null:dept);
				query.where(n1,n2,n3,n4).orderBy(orderList);

				TypedQuery<Tuple> result = em.createQuery(query);
				list = result.getResultList();
			
			if(list.size()==0){
				//DEPARTMENT_VALUE
				//dept
				Subquery<String> dept1 = query.subquery(String.class); 
				Root<PositionMaster> pi1 = dept1.from(PositionMaster.class);
				dept1.select(pi1.get("deptId"));
				//maxAmend
				Subquery<Long> amendId = query.subquery(Long.class); 
				Root<PositionMaster> pis1 = amendId.from(PositionMaster.class);
				amendId.select(cb.max(pis1.get("amendId")));
				Predicate q1 = cb.equal( pis1.get("proposalNo"), pi1.get("proposalNo"));
				amendId.where(q1);
				Predicate p1 = cb.equal( pi1.get("contractNo"), req.getContractNo());
				Predicate p2 = cb.equal( pi1.get("layerNo"), req.getLayerNo());
				Predicate p3 = cb.equal( pi1.get("amendId"), amendId);
				dept1.where(p1,p2,p3);
				
				Predicate m3 = cb.equal(rd.get("tmasStatus"), req.getStatus());
				Expression<String> e1=rd.get("tmasDepartmentId");
				Predicate m4 = e1.in(dept1==null?null:dept1);
				query.where(n1,n2,m3,m4).orderBy(orderList);

				TypedQuery<Tuple> result1 = em.createQuery(query);
				list = result1.getResultList();
			}
			}
			if(list!=null && list.size()>0) {
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 Tuple tempMap = list.get(i);
					res.setCode(tempMap.get("TMAS_DEPARTMENT_ID")==null?"":tempMap.get("TMAS_DEPARTMENT_ID").toString());
					res.setCodeDescription(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
					department.add(res);
				}
			}
			response.setCommonResponse(department);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}

		return response;
	}
	public String getCombinedClass(String branchCode,String productId,String departId){
		String count="";
		try {
			//common.select.getCoreCompanyName
			//in
			List<String> dept =new ArrayList<String>(Arrays.asList(departId.split(","))) ;
			List<BigDecimal> dept1 = dept.stream().map(BigDecimal::new).collect(Collectors.toList());
		
			TmasDepartmentMaster list = deptRepo.findByBranchCodeAndTmasProductIdAndTmasStatusAndCoreCompanyCodeNotNullAndTmasDepartmentIdIn(
					branchCode,new BigDecimal(productId),"Y",dept1);
			if(list!=null){
				count=list.getCoreCompanyCode()==null?"":list.getCoreCompanyCode().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}


	@Override
	public GetCommonDropDownRes getSubProfitCentreMulti(GetSubProfitCentreMultiReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> subProfitCenterList=new ArrayList<CommonResDropDown>();
		List<TmasSpfcMaster> list = null; 
		try{
			if(req.getSubProfitId()!=null && !req.getSubProfitId().equalsIgnoreCase("ALL")){
				//common.select.getspfcName1
				//in
				List<String> spfcId =new ArrayList<String>(Arrays.asList(req.getSubProfitId().split(","))) ;
				list = spfcRepo.findDistinctByBranchCodeAndTmasProductIdAndTmasStatusAndTmasDepartmentIdAndTmasSpfcIdInOrderByTmasSpfcNameAsc(
						req.getBranchCode(),new BigDecimal(req.getProductCode()),"Y",new BigDecimal(req.getDepartmentId()),spfcId);
			}else{
				  //common.select.getspfcName
				list = spfcRepo.findDistinctByBranchCodeAndTmasProductIdAndTmasStatusAndTmasDepartmentIdOrderByTmasSpfcNameAsc(
						req.getBranchCode(),new BigDecimal(req.getProductCode()),"Y",new BigDecimal(req.getDepartmentId()));
			}
			if(list.size()>0) {
			for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 TmasSpfcMaster tempMap =  list.get(i);
					res.setCode(tempMap.getTmasSpfcId()==null?"":tempMap.getTmasSpfcId().toString());
					res.setCodeDescription(tempMap.getTmasSpfcName()==null?"":tempMap.getTmasSpfcName().toString());
					subProfitCenterList.add(res);
				}
			}
			response.setCommonResponse(subProfitCenterList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	
	public String getpreReopendDate(String contractNo, String claimNo, String type) {
		String result="";
		try{
			if("Reopen".equalsIgnoreCase(type)){
				//GET_PRE_REOPEN_DATE
				TtrnClaimDetails list = claimRepo.findByContractNoAndClaimNo(contractNo, new BigDecimal(claimNo));
				if (list != null) {
					result = list.getReopenedDate() == null ? ""
							: list.getReopenedDate().toString();
				}
			}else if("RetroReopen".equalsIgnoreCase(type)){
				//GET_PRE_REOPEN_DATE_RETRO
				TtrnRetroClaimDetails list = retroClaimRepo.findByContractNoAndClaimNo(contractNo, new BigDecimal(claimNo));
				if (list != null) {
					result = list.getReopenedDate() == null ? ""
							: list.getReopenedDate().toString();
				}
			}else if("RetroReputed".equalsIgnoreCase(type)){
				//GET_PRE_REPUTED_DATE_RETRO
//				TtrnRetroClaimDetails list = retroClaimRepo.findByContractNoAndClaimNo(contractNo, new BigDecimal(claimNo));
//				if (list != null) {
//					result = list.getRepudateDate() == null ? ""
//							: list.getRepudateDate().toString();
//				}
				//GET_PRE_REOPEN_DATE_RETRO
				TtrnRetroClaimDetails list = retroClaimRepo.findByContractNoAndClaimNo(contractNo, new BigDecimal(claimNo));
				if (list != null) {
					result = list.getReopenedDate() == null ? ""
							: list.getReopenedDate().toString();
				}
			}
			else {
				//GET_PRE_REPUTED_DATE
				TtrnClaimDetails list = claimRepo.findByContractNoAndClaimNo(contractNo, new BigDecimal(claimNo));
				if (list != null) {
					result = list.getRepudateDate() == null ? ""
							: list.getRepudateDate().toString();
				}
			}
			System.out.println(result);
		}catch(Exception e){
			e.printStackTrace();
			log.debug("Exception @ {" + e + "}");	
		}
		return result;
	}

	@Override
	public GetCommonValueRes getCurrencyId(GetCurrencyIdReq req) {
		GetCommonValueRes response = new GetCommonValueRes();
		String currency="";
		try{
			if("4".equalsIgnoreCase(req.getProductId())||"5".equalsIgnoreCase(req.getProductId())){
				//GET_CLAIM_CURRENCY_RETRO
				TtrnRetroClaimDetails list = retroClaimRepo.findByContractNoAndClaimNoAndLayerNo(
						req.getContractNo(), new BigDecimal(req.getClaimNo()),new BigDecimal(req.getLayerNo()));
				if (list != null) {
					currency = list.getCurrency() == null ? ""
							: list.getCurrency().toString();
				}
			}else{
				//GET_CLAIM_CURRENCY
				TtrnClaimDetails list = claimRepo.findByContractNoAndClaimNoAndLayerNo(
						req.getContractNo(), new BigDecimal(req.getClaimNo()),new BigDecimal(req.getLayerNo()));
				if (list != null) {
					currency = list.getCurrency() == null ? ""
							: list.getCurrency().toString();
				}
			}
		
			response.setCommonResponse(currency);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonValueRes getProposalNo(GetProposalNoReq req) {
		GetCommonValueRes response = new GetCommonValueRes();
		String proposalNo="";
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			query.multiselect(pm.get("proposalNo").alias("PROPOSAL_NO"),pm.get("deptId").alias("DEPT_ID")); 
			
		if(StringUtils.isNotBlank(req.getProductId()) &&("1".equals(req.getProductId()) || "4".equals(req.getProductId())) ){
			//GET_FAC_PROPOSAL_NO
			
			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			amend.where(a1);

			Predicate n1 = cb.equal(pm.get("contractNo"),req.getContractNo());
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n3);
			
		
		}else if("2".equals(req.getProductId())){
			//GET_PRO_PROPOSAL_NO
			
			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a2 = cb.equal( pm.get("deptId"), pms.get("deptId"));
			amend.where(a1,a2);

			Predicate n1 = cb.equal(pm.get("contractNo"),req.getContractNo());
			Predicate n2 = cb.equal(pm.get("deptId"),req.getDepartId());
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3);
			
		}
		else if("3".equals(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())){
			//GET_XOL_PROPOSAL_NO
			
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a2 = cb.equal( pm.get("getLayerNo"), pms.get("getLayerNo"));
			amend.where(a1,a2);

			Predicate n1 = cb.equal(pm.get("contractNo"),req.getContractNo());
			Predicate n2 = cb.equal(pm.get("getLayerNo"),req.getLayerNo()==null?0:req.getLayerNo());
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3);
			
		}
		TypedQuery<Tuple> res = em.createQuery(query);
		List<Tuple> list = res.getResultList();
		
		if(list!=null &&list.size()>0){
			Tuple map=list.get(0);
			proposalNo=map.get("PROPOSAL_NO")==null?"":map.get("PROPOSAL_NO").toString();
		}

		response.setCommonResponse(proposalNo);
		response.setMessage("Success");
		response.setIsError(false);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	
	}
	public int Validatethree(String branchCode, String accountDate) {
		int result = 0;
		String[] args = null;
		try {
			String OpstartDate = "";
			String OpendDate = "";
			args = new String[1];
			args[0] = branchCode;
			List<TmasOpenPeriod> list = tmasOpenRepo.findByBranchCodeOrderBySnoDesc(branchCode);

			if (!CollectionUtils.isEmpty(list)) {
				for (TmasOpenPeriod tmasOpenPeriod : list) {
					OpstartDate = tmasOpenPeriod.getStartDate() == null ? ""
							: sdf.format(tmasOpenPeriod.getStartDate());
					OpendDate = tmasOpenPeriod.getEndDate() == null ? ""
							:sdf.format(tmasOpenPeriod.getEndDate());

					boolean status = ttrnPaymentReceiptDetailsMapper.parseDateLocal(accountDate)
							.after(ttrnPaymentReceiptDetailsMapper.parseDateLocal(OpstartDate))
							&& ttrnPaymentReceiptDetailsMapper.parseDateLocal(accountDate)
									.before(ttrnPaymentReceiptDetailsMapper.parseDateLocal(OpendDate));
					result = status ? 1 : 0;
					if (result > 0)
						break;
				}
			}

		} catch (Exception e) {
			log.debug("Exception @ {" + e + "}");
		}
		return 1;
	}
	@Override
	public GetCommonDropDownRes getSectionList(GetSectionListReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList=new ArrayList<CommonResDropDown>();
		try{
			//GET_SECTION_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnPttySection> pm = query.from(TtrnPttySection.class);

			query.multiselect(pm.get("sectionNo").alias("SECTION_NO"),pm.get("sectionName").alias("SECTION_NAME")); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnPttySection> pms = amend.from(TtrnPttySection.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a3 = cb.equal( pm.get("deptId"), pms.get("deptId"));
			amend.where(a1,a2,a3);

			Predicate n1 = cb.equal(pm.get("contractNo"), req.getContractNo());
			Predicate n2 = cb.equal(pm.get("deptId"), req.getDepartId());
			Predicate n3 = cb.equal(pm.get("branchCode"), req.getBranchCode());
			Predicate n4 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3,n4);
			
			TypedQuery<Tuple> reslt = em.createQuery(query);
			List<Tuple> list = reslt.getResultList();

			if(list!=null) {
			for(int i=0 ; i<list.size() ; i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap = list.get(i);
				res.setCode(tempMap.get("SECTION_NO")==null?"":tempMap.get("SECTION_NO").toString());
				res.setCodeDescription(tempMap.get("SECTION_NAME")==null?"":tempMap.get("SECTION_NAME").toString());
				resList.add(res);
			}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonValueRes getContractLayerNo(GetContractLayerNoReq req) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result =" ";
		
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			if("3".equalsIgnoreCase(req.getProductId()) || "5".equalsIgnoreCase(req.getProductId())){
			//GET_CON_DEPT_ID_LAYER
			
			query.multiselect(cb.selectCase().when(cb.isNull(pm.get("layerNo")), 0)
					.otherwise(pm.get("layerNo"))); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
			Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a3 = cb.equal( pm.get("deptId"), pms.get("deptId"));
			amend.where(a1,a2,a3);

			Predicate n1 = cb.equal(pm.get("contractNo"), req.getContractNo());
			Predicate n2 = cb.equal(pm.get("layerNo"), req.getLayerNo());
			Predicate n3 = cb.equal(pm.get("branchCode"), req.getBranchCode());
			Predicate n5 = cb.equal(pm.get("contractStatus"), "A");
			Predicate n4 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3,n4,n5);
			
			TypedQuery<Tuple> reslt = em.createQuery(query);
			List<Tuple> list = reslt.getResultList();
			
			if (!CollectionUtils.isEmpty(list)) {
				result =(list.get(0) == null ? ""
						: list.get(0).toString());
						}
			}
			else if("2".equalsIgnoreCase(req.getProductId())){
				//GET_CON_DEPT_ID
				query.multiselect(cb.selectCase().when(cb.isNull(pm.get("deptId")), "0")
						.otherwise(pm.get("deptId"))); 

				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
				Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
				Predicate a3 = cb.equal( pm.get("deptId"), pms.get("deptId"));
				Predicate a4 = cb.equal( pm.get("contractStatus"), pms.get("contractStatus"));
				amend.where(a1,a2,a3,a4);

				Predicate n1 = cb.equal(pm.get("contractNo"), req.getContractNo());
				Predicate n2 = cb.equal(pm.get("deptId"), req.getLayerNo());
				Predicate n3 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				Predicate n5 = cb.equal(pm.get("contractStatus"), "A");
				Predicate n4 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2,n3,n4,n5);
				
				TypedQuery<Tuple> reslt = em.createQuery(query);
				List<Tuple> list = reslt.getResultList();
				
				if (!CollectionUtils.isEmpty(list)) {
					result =(list.get(0) == null ? ""
							: list.get(0).toString());
							}
			}
			else if("1".equalsIgnoreCase(req.getProductId())){
				//GET_CON_DETAILS
				query.multiselect(cb.selectCase().when(cb.isNull(pm.get("contractNo")), 0)
						.otherwise(pm.get("contractNo"))); 

				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("contractNo"), pms.get("contractNo"));
				Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
				Predicate a4 = cb.equal( pm.get("contractStatus"), pms.get("contractStatus"));
				amend.where(a1,a2,a4);

				Predicate n1 = cb.equal(pm.get("contractNo"), req.getContractNo());
				Predicate n3 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				Predicate n5 = cb.equal(pm.get("contractStatus"), "A");
				Predicate n4 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n3,n4,n5);
				
				TypedQuery<Tuple> reslt = em.createQuery(query);
				List<Tuple> list = reslt.getResultList();
				
				if (!CollectionUtils.isEmpty(list)) {
					result =(list.get(0) == null ? ""
							: list.get(0).toString());
							}
				
				if (!CollectionUtils.isEmpty(list)) {
					result =(list.get(0) == null ? ""
							: list.get(0).toString());
				}
			}
			
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	
	}

	@Override
	public GetCommonDropDownRes getPreDepartmentDropDown(GetPreDepartmentDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> department = new ArrayList<CommonResDropDown>();
		List<Tuple> list= null;
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			String count="";
			if("2".equals(req.getProductId())){
				count=getCombinedClass(req.getBranchCode(),req.getProductId(),req.getDepartmentId());
			}
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TmasDepartmentMaster> rd = query.from(TmasDepartmentMaster.class);

			query.multiselect(rd.get("tmasDepartmentId").alias("TMAS_DEPARTMENT_ID"),rd.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME")).distinct(true); 

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(rd.get("tmasDepartmentName")));
			
			Predicate n1 = cb.equal(rd.get("branchCode"), req.getBranchCode());
			Predicate n2 = cb.equal(rd.get("tmasProductId"), req.getProductId());
			
			if( "2".equals(req.getProductId())){ //ri
				//common.department.combined.premiumclaim
				//common.select.getDepartmentList	
				
				Predicate n3 = cb.equal(rd.get("tmasStatus"), req.getStatus());
				query.where(n1,n2,n3).orderBy(orderList);

				TypedQuery<Tuple> result = em.createQuery(query);
				list = result.getResultList();
				
			}else{
				  //common.select.getDepartmentList.preClaim
				//dept
				Subquery<String> dept = query.subquery(String.class); 
				Root<TtrnRskClassLimits> pi = dept.from(TtrnRskClassLimits.class);
				dept.select(pi.get("rskCoverClass"));
				//maxAmend
				Subquery<Long> end = query.subquery(Long.class); 
				Root<TtrnRskClassLimits> pis = end.from(TtrnRskClassLimits.class);
				end.select(cb.max(pis.get("rskEndorsementNo")));
				Predicate c1 = cb.equal( pis.get("rskProposalNumber"), pi.get("rskProposalNumber"));
				end.where(c1);
				Predicate d1 = cb.equal( pi.get("rskContractNo"), req.getContractNo());
				Predicate d2 = cb.equal( pi.get("rskLayerNo"), req.getLayerNo());
				Predicate d3 = cb.equal( pi.get("rskEndorsementNo"), end);
				dept.where(d1,d2,d3);
				
				Predicate n3 = cb.equal(rd.get("tmasStatus"), req.getStatus());
				Expression<String> e0=rd.get("tmasDepartmentId");
				Predicate n4 = e0.in(dept==null?null:dept);
				query.where(n1,n2,n3,n4).orderBy(orderList);

				TypedQuery<Tuple> result = em.createQuery(query);
				list = result.getResultList();
			
			if(list.size()==0){
				//DEPARTMENT_VALUE
				//dept
				Subquery<String> dept1 = query.subquery(String.class); 
				Root<PositionMaster> pi1 = dept1.from(PositionMaster.class);
				dept1.select(pi1.get("deptId"));
				//maxAmend
				Subquery<Long> amendId = query.subquery(Long.class); 
				Root<PositionMaster> pis1 = amendId.from(PositionMaster.class);
				amendId.select(cb.max(pis1.get("amendId")));
				Predicate q1 = cb.equal( pis1.get("proposalNo"), pi1.get("proposalNo"));
				amendId.where(q1);
				Predicate p1 = cb.equal( pi1.get("contractNo"), req.getContractNo());
				Predicate p2 = cb.equal( pi1.get("layerNo"), req.getLayerNo());
				Predicate p3 = cb.equal( pi1.get("amendId"), amendId);
				dept1.where(p1,p2,p3);
				
				Predicate m3 = cb.equal(rd.get("tmasStatus"), req.getStatus());
				Expression<String> e1=rd.get("tmasDepartmentId");
				Predicate m4 = e1.in(dept1==null?null:dept1);
				query.where(n1,n2,m3,m4).orderBy(orderList);

				TypedQuery<Tuple> result1 = em.createQuery(query);
				list = result1.getResultList();
			}
			}
			if(list!=null && list.size()>0) {
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 Tuple tempMap = list.get(i);
					res.setCode(tempMap.get("TMAS_DEPARTMENT_ID")==null?"":tempMap.get("TMAS_DEPARTMENT_ID").toString());
					res.setCodeDescription(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
					department.add(res);
				}
			}
			response.setCommonResponse(department);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}

		return response;
	}

	@Override
	public GetCommonDropDownRes getSubProfitCentreMultiDropDown(GetSubProfitCentreMultiDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> subProfitCenterList=new ArrayList<CommonResDropDown>();
		List<TmasSpfcMaster> list = null; 
		try{
			if(req.getSubProfitId()!=null && !req.getSubProfitId().equalsIgnoreCase("ALL")){
				//common.select.getspfcName1
				//in
				List<String> spfcId =new ArrayList<String>(Arrays.asList(req.getSubProfitId().split(","))) ;
				list = spfcRepo.findDistinctByBranchCodeAndTmasProductIdAndTmasStatusAndTmasDepartmentIdAndTmasSpfcIdInOrderByTmasSpfcNameAsc(
						req.getBranchCode(),new BigDecimal(req.getProductCode()),"Y",new BigDecimal(req.getDepartmentId()),spfcId);
			}else{
				  //common.select.getspfcName
				list = spfcRepo.findDistinctByBranchCodeAndTmasProductIdAndTmasStatusAndTmasDepartmentIdOrderByTmasSpfcNameAsc(
						req.getBranchCode(),new BigDecimal(req.getProductCode()),"Y",new BigDecimal(req.getDepartmentId()));
			}
			if(list.size()>0) {
			for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 TmasSpfcMaster tempMap =  list.get(i);
					res.setCode(tempMap.getTmasSpfcId()==null?"":tempMap.getTmasSpfcId().toString());
					res.setCodeDescription(tempMap.getTmasSpfcName()==null?"":tempMap.getTmasSpfcName().toString());
					subProfitCenterList.add(res);
				}
			}
			response.setCommonResponse(subProfitCenterList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonValueRes getCashLossCount(String contractNo, String departmentId) {
		GetCommonValueRes response = new GetCommonValueRes();
		try{
			//GET_CASHLOSS_COUNT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			Root<TtrnClaimPayment> tcp = query.from(TtrnClaimPayment.class);
			Root<TtrnClaimDetails> tcd = query.from(TtrnClaimDetails.class);
			
			query.multiselect(cb.count(tcd)); 
			
			Expression<Long> e = cb.diff(cb.<Long>selectCase().when(cb.isNull(tcp.<Long>get("paidAmountOc")),0l)
					.otherwise(tcp.<Long>get("paidAmountOc")), cb.<Long>selectCase().when(cb.isNull(tcp.<Long>get("cashLossSettledTilldate")),0l)
							.otherwise(tcp.<Long>get("cashLossSettledTilldate")));
			
			Predicate n1 = cb.equal(tcp.get("contractNo"), contractNo);
			Predicate n2 = cb.equal(tcd.get("subClass"), departmentId);
			Predicate n3 = cb.equal(tcp.get("contractNo"), tcd.get("contractNo"));
			Predicate n5 = cb.equal(tcp.get("claimNo"), tcd.get("claimNo"));
			Predicate n4 = cb.greaterThan(e, 0l);
			query.where(n1,n2,n3,n4,n5);
			
			TypedQuery<Long> reslt = em.createQuery(query);
			Long count = reslt.getResultList().get(0);
			
			response.setCommonResponse(String.valueOf(count==null?"":count));
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	
	}
//	public int validatethree(String branchCode, String accountDate) {
//
//		int count=0;
//		
//		List<Map<String, Object>> result=new ArrayList<Map<String, Object>>();
//		
//		try{
//			String query="GET_OPEN_PERIOD_DATE";
//			String OpstartDate="";
//			String OpendDate="";
//			String[] args = new String[1];
//			args[0]=branchCode;
//			
//			log.info("Select Query ==> " + query);
//			List<Map<String,Object>> list=queryImpl.selectList(query,args);
//			for(int i=0 ; i<list.size() ; i++) {
//				Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
//				OpstartDate=tempMap.get("START_DATE")==null?"":tempMap.get("START_DATE").toString();
//				OpendDate=tempMap.get("END_DATE")==null?"":tempMap.get("END_DATE").toString();
//				String query1="GET_OPEN_PERIOD_VALIDATE";
//				args = new String[3];
//				args[0]=accountDate;
//				args[1]=OpstartDate;
//				args[2]=OpendDate;
//		
//				result=queryImpl.selectList(query1,args);
//				if (!CollectionUtils.isEmpty(result)) {
//					count =Integer.valueOf((result.get(0).get("TOTAL") == null ? ""
//							: result.get(0).get("TOTAL").toString()));
//				
//				}
//				
//				 if(count>0)
//					 break;
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//			log.debug("Exception @ {" + e + "}");	
//		}
//		return count;
//	
//	}
	public  String GetDesginationCountry(final String limitOrigCur,final String ExchangeRate) {
		String valu="0";
		if (StringUtils.isNotBlank(limitOrigCur)&& Double.parseDouble(limitOrigCur) != 0) {
			double originalCountry = Double.parseDouble(limitOrigCur)/ Double.parseDouble(ExchangeRate);
			DecimalFormat myFormatter = new DecimalFormat("###0.00");
			final double dround = Math.round(originalCountry * 100.0) / 100.0;
			valu = myFormatter.format(dround);
		}
		return valu;
	}
	public static String formatter(final String value)
	{
		String output="0.00";
		if(StringUtils.isNotBlank(value))
		{
			double doublevalue=Double.parseDouble(value);
			DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,##0.00");
			output =myFormatter.format(doublevalue);
		}
		return output;
	}
	public static String formattereight(final String value)
	{
		String output="0.00";
		if(StringUtils.isNotBlank(value))
		{
			double doublevalue=Double.parseDouble(value);
			DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,##0.00000000");
			output =myFormatter.format(doublevalue);
		}
		return output;
	}
	public GetCommonValueRes GetExchangeRate(final String location,final String date,final String countryid,final String branchCode) {
		GetCommonValueRes response=new GetCommonValueRes();
		String exRate="";
		String startDtOfMonth="";
		try{
			//common.select.getStartDTOfMonth
			Date date1 = sdf.parse(date);
			LocalDate localDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String s = String.valueOf(localDate);
			String regex = "-";
			String[] r =s.split(regex);
			String month =r[1];
			Long year = (long) localDate.getYear();
		//	int day = localDate.getDayOfMonth()	;
			startDtOfMonth = "01/" + month +"/" + year ;
			Date startDtOfMonth1 = sdf.parse(startDtOfMonth);
			
			if(!"01/".equalsIgnoreCase(startDtOfMonth))		{
				//common.select.getExRate
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<BigDecimal> query = cb.createQuery(BigDecimal.class); 
				Root<ExchangeMaster> m = query.from(ExchangeMaster.class);
				
				query.select(m.get("exchangeRate")); 
				
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<ExchangeMaster> rds = amend.from(ExchangeMaster.class);
				amend.select(cb.max(rds.get("amendId")));
				Predicate a1 = cb.equal( rds.get("currencyId"), m.get("currencyId"));
				Predicate a2 = cb.equal( rds.get("countryId"), m.get("countryId"));
				Predicate a3 = cb.lessThanOrEqualTo(rds.get("inceptionDate"), startDtOfMonth1);
				Predicate a4 = cb.equal( rds.get("branchCode"), m.get("branchCode"));
				amend.where(a1,a2,a3,a4);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(m.get("inceptionDate")));
			
				Predicate n1 = cb.equal(m.get("currencyId"), location);
				Predicate n2 = cb.equal(m.get("countryId"), countryid);
				Predicate n3 = cb.lessThanOrEqualTo(m.get("inceptionDate"), startDtOfMonth1);
				Predicate n5 = cb.equal(m.get("branchCode"), branchCode);
				Predicate n4 = cb.equal(m.get("amendId"), amend);
				query.where(n1,n2,n3,n4,n5).orderBy(orderList);
				
				TypedQuery<BigDecimal> reslt = em.createQuery(query);
				List<BigDecimal> val = reslt.getResultList();
			  if(val!=null) {
					exRate=val.get(0)==null?"":val.get(0).toString();
						}
			  }
			response.setCommonResponse(exRate);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	public static String exchRateFormat(final String value){
		String output="0.00";
		if(StringUtils.isNotBlank(value))
		{
			System.out.println(value);
			double doublevalue=Double.parseDouble(value);
			DecimalFormat myFormatter = new DecimalFormat("#####.##########");
			output = myFormatter.format(doublevalue);
		}
		return output;
	}

	@Override
	public GetCommonDropDownRes getPersonalInfoDropDown(String branchCode, String customerType, String pid) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			//common.select.getPersonalInfoList
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PersonalInfo> pm = query.from(PersonalInfo.class);

			Expression<String> firstname = cb.concat(pm.get("firstName"), " ");
			query.multiselect(pm.get("customerId").alias("CUSTOMER_ID"),
					cb.selectCase().when(cb.equal(pm.get("customerType"),"C"), pm.get("companyName"))
					.otherwise(cb.concat(firstname, pm.get("lastName")) ).alias("NAME")).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PersonalInfo> pms = amend.from(PersonalInfo.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("customerType"), pms.get("customerType"));
			Predicate a3 = cb.equal( pm.get("status"), pms.get("status"));
			Predicate a4 = cb.equal( pm.get("customerId"), pms.get("customerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("customerType"), customerType);
			Predicate n3 = cb.equal(pm.get("status"), "Y");
			//Not in
			Expression<String> e0 = pm.get("customerId");
      		Predicate n4 = e0.in("64").not();

			Predicate n5 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3,n4,n5);
			
			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();
			if(list.size()>0) {
				 for(int i=0 ; i<list.size() ; i++) {
					 CommonResDropDown resp = new CommonResDropDown();
					 Tuple tempMap = list.get(i);
						resp.setCode(tempMap.get("CUSTOMER_ID")==null?"":tempMap.get("CUSTOMER_ID").toString());
						resp.setCodeDescription(tempMap.get("NAME")==null?"":tempMap.get("NAME").toString());
						resList.add(resp);
					}	
			}
			
			if((customerType.equals("L") && !pid.equalsIgnoreCase("4") &&  !pid.equalsIgnoreCase("5")) ||(customerType.equals("C") && pid.equalsIgnoreCase("4") )){
				//common.select.getPersonalInfoList1
				CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
				
				Root<PersonalInfo> pm1 = query1.from(PersonalInfo.class);

				// Select
				query1.multiselect(pm1.get("customerId").alias("CUSTOMER_ID"),
						pm1.get("companyName").alias("NAME")).distinct(true).distinct(true); 

				// MAXAmend ID
				Subquery<Long> amendid = query1.subquery(Long.class); 
				Root<PersonalInfo> pms1 = amendid.from(PersonalInfo.class);
				amendid.select(cb.max(pms1.get("amendId")));
				Predicate b1 = cb.equal( pm1.get("branchCode"), pms1.get("branchCode"));
				Predicate b2 = cb.equal( pm1.get("customerType"), pms1.get("customerType"));
				Predicate b3 = cb.equal( pm1.get("status"), pms1.get("status"));
				Predicate b4 = cb.equal( pm1.get("customerId"), pms1.get("customerId"));
				amendid.where(b1,b2,b3,b4);


				// Where
				Predicate m1 = cb.equal(pm1.get("branchCode"), branchCode);
				Predicate m2 = cb.equal(pm1.get("customerType"), "C");
				Predicate m3 = cb.equal(pm1.get("status"), "Y");
	      		Predicate m4 = cb.equal(pm1.get("customerId"), "64");
				Predicate m5 = cb.equal(pm1.get("amendId"), amendid);
				query.where(m1,m2,m3,m4,m5);
				
				// Get Result
				TypedQuery<Tuple> res1 = em.createQuery(query);
				List<Tuple> list1 = res1.getResultList();
				if(list1.size()>0) {
					 for(int i=0 ; i<list1.size() ; i++) {
						 CommonResDropDown resp = new CommonResDropDown();
						 Tuple tempMap =  list1.get(i);
							resp.setCode(tempMap.get("CUSTOMER_ID")==null?"":tempMap.get("CUSTOMER_ID").toString());
							resp.setCodeDescription(tempMap.get("NAME")==null?"":tempMap.get("NAME").toString());
							resList.add(resp);
						}	
				}
			}
			resList.sort(Comparator.comparing(CommonResDropDown :: getCodeDescription));
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	
	}

	@Override
	public GetCommonDropDownRes getBankMasterDropDown(String branchCode) {
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		try{
			//common.select.getBankMasterList
			List<BankMaster> list = bankRepo.findByBranchCodeAndStatusOrderByBankName(branchCode,"Y");
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 BankMaster tempMap =  list.get(i);
					res.setCode(tempMap.getBankId()==null?"":tempMap.getBankId().toString());
					res.setCodeDescription(tempMap.getBankName()==null?"":tempMap.getBankName().toString());
					resList.add(res);
				}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	
	}

	@Override
	public GetCommonDropDownRes getConstantDropDown(String categoryId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		List<ConstantDetail> list = null;
		try{
			if("30".equalsIgnoreCase(categoryId) || "48".equalsIgnoreCase(categoryId)){
				//COM_SELECT_PERILS
				list = constRepo.findDistinctByCategoryIdAndStatusOrderByType(new BigDecimal(categoryId),"Y");
			}
			else{
			 //common.select.getConstDet
				list = constRepo.findDistinctByCategoryIdAndStatusOrderByType(new BigDecimal(categoryId),"Y");
			 }
			
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 ConstantDetail tempMap = list.get(i);
					res.setCode(tempMap.getType()==null?"":tempMap.getType().toString());
					res.setCodeDescription(tempMap.getDetailName()==null?"":tempMap.getDetailName().toString());
					resList.add(res);
				}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}



	public GetCommonDropDownRes getCurrencyShortList(String branchCode, String countryId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			//GET_CURRENCY_SHORT_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<CurrencyMaster> pm = query.from(CurrencyMaster.class);

			query.multiselect(pm.get("currencyId").alias("CURRENCY_ID"),pm.get("shortName").alias("SHORT_NAME")).distinct(true); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<CurrencyMaster> pms = amend.from(CurrencyMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("countryId"), pms.get("countryId"));
			Predicate a3 = cb.equal( pm.get("status"), pms.get("status"));
			Predicate a4 = cb.equal( pm.get("currencyId"), pms.get("currencyId"));
			amend.where(a1,a2,a3,a4);

			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("currencyId")));

			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("countryId"), countryId);
			Predicate n3 = cb.equal(pm.get("status"), "Y");
			Predicate n4 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 Tuple tempMap = list.get(i);
					res.setCode(tempMap.get("CURRENCY_ID")==null?"":tempMap.get("CURRENCY_ID").toString());
					res.setCodeDescription(tempMap.get("SHORT_NAME")==null?"":tempMap.get("SHORT_NAME").toString());
					resList.add(res);
				}
			response.setCommonResponse(resList);

			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;

	}

	@Override
	public GetCommonValueRes getDisableStatus1(String contractNo, String layerNo) {
		GetCommonValueRes response = new GetCommonValueRes();
		String status="Y";
		if(StringUtils.isBlank(contractNo)){
			contractNo="";
		}if(StringUtils.isBlank(layerNo)){
			layerNo="";
		}
		int cnt = 0;
		try {
			//GET_DISABLE_STATUS1
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			Root<TtrnClaimDetails> pm = query.from(TtrnClaimDetails.class);
			query.multiselect(cb.count(pm)); 
			
			Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
			Predicate n2 = cb.equal(cb.coalesce(pm.get("layerNo"), 0),StringUtils.isBlank(layerNo)?0:layerNo);
			query.where(n1,n2);
			TypedQuery<Long> res1 = em.createQuery(query);
			List<Long> list = res1.getResultList();
			if(list!=null) {
				cnt = list.get(0).intValue();	
				}
			
			if(cnt>0) {
				status="Y";
			}	else {
			CriteriaQuery<Long> query1 = cb.createQuery(Long.class); 
			Root<RskPremiumDetails> pd = query1.from(RskPremiumDetails.class);
			query1.multiselect(cb.count(pd)); 
			
			Predicate m1 = cb.equal(pd.get("contractNo"), contractNo);
			Predicate m2 = cb.equal(cb.coalesce(pd.get("layerNo"), 0),StringUtils.isBlank(layerNo)?0:layerNo);
			query1.where(m1,m2);
			TypedQuery<Long> res = em.createQuery(query1);
			List<Long> list1 = res.getResultList();
			if(list1!=null) {
				cnt = list1.get(0).intValue();	
				}
			if(cnt>0) {
				status="Y";
			}else {
				status="N";
			}
			}
			
			response.setCommonResponse(status);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonResponse riskDetailsEndorsement(String proposalNo, String endtStatus, String branchCode) {
		CommonResponse response = new CommonResponse();
		try {
			
			String query = "call COPYQUOTE (?,?,?,?,?)";
			
			queryImpl.updateQuery(query, new String[]{"Endt",endtStatus==null?"":endtStatus,"","branchCode",proposalNo});
			
			response.setMessage("Success");
			response.setIsError(false);
	 }	catch(Exception e){
				log.error(e);

	}
		return response;
	}

	@Override
	public GetCommonDropDownRes getCountryDropDown(String branchCode) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			//common.select.getCountryList
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<CountryMaster> pm = query.from(CountryMaster.class);
			query.multiselect(pm.get("countryId").alias("COUNTRY_ID"),pm.get("countryName").alias("COUNTRY_NAME")).distinct(true); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<CountryMaster> pms = amend.from(CountryMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("countryId"), pms.get("countryId"));
			Predicate a3 = cb.equal( pm.get("status"), pms.get("status"));
			amend.where(a1,a2,a3);

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("countryName")));

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(pm.get("status"), "Y");
			Predicate n4 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n3,n4).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
			for(int i=0 ; i<list.size() ; i++) {
					CommonResDropDown res = new CommonResDropDown();
					Tuple tempMap = list.get(i);
					res.setCode(tempMap.get("COUNTRY_ID")==null?"":tempMap.get("COUNTRY_ID").toString());
					res.setCodeDescription(tempMap.get("COUNTRY_NAME")==null?"":tempMap.get("COUNTRY_NAME").toString());
					resList.add(res);
			 	}
			 	response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			}
		     catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		        }
			return response;
	}
	
	@Override
	public GetCommonDropDownRes getTypeList(String type) {
	GetCommonDropDownRes response = new GetCommonDropDownRes();
	List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try {
			//GET_COMMISSION_LIST
			List<ConstantDetail> list = constRepo.findByCategoryIdAndStatus(new BigDecimal(type),"Y");			
			for(int i=0 ; i<list.size() ; i++){
				CommonResDropDown res = new CommonResDropDown();
				ConstantDetail tempMap =  list.get(i);
				res.setCode(tempMap.getDetailName()==null?"":tempMap.getDetailName().toString());
				res.setCodeDescription(tempMap.getRemarks()==null?"":tempMap.getRemarks().toString());
				resList.add(res);
				}
			    response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			}
		     catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		       }
			return response;	
	}

	@Override
	public GetCommonDropDownRes getReinstatementOptionList() {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try {
			//GET_REINSTATEMENT_LIST
			List<ConstantDetail> list = constRepo.findByCategoryIdAndStatus(new BigDecimal("33"),"Y");			
			for(int i=0 ; i<list.size() ; i++){
				CommonResDropDown res = new CommonResDropDown();
				ConstantDetail tempMap =  list.get(i);
				res.setCode(tempMap.getDetailName()==null?"":tempMap.getDetailName().toString());
				res.setCodeDescription(tempMap.getRemarks()==null?"":tempMap.getRemarks().toString());
				resList.add(res);
				}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}
		catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	
   @Override
	public GetCommonDropDownRes getTerritoryRegionList(String branchCode) {
		GetCommonDropDownRes red = new GetCommonDropDownRes();
		List<CommonResDropDown> res = new ArrayList<CommonResDropDown>();
		try{
			//GET_Rate_FLOW
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TmasTerritory> pm = query.from(TmasTerritory.class);
			query.multiselect(pm.get("territoryId").alias("TERRITORY_ID"),pm.get("territoryName").alias("TERRITORY_NAME")); 

			Expression<Object> caseExpression = cb.selectCase()
			            .when(cb.like(pm.get("territoryName"), "India%"), 1)
			            .otherwise(2);
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(caseExpression));
			orderList.add(cb.asc(pm.get("territoryName")));

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			query.where(n1).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			for(int i=0;i<list.size();i++){
				CommonResDropDown range=new CommonResDropDown();
				Tuple tempMap =  list.get(i);
				range.setCode(tempMap.get("TERRITORY_ID")==null?"":tempMap.get("TERRITORY_ID").toString());
				range.setCodeDescription(tempMap.get("TERRITORY_NAME")==null?"":tempMap.get("TERRITORY_NAME").toString());
				res.add(range);				
				}
				red.setCommonResponse(res);
				red.setMessage("Success");
				red.setIsError(false);
			}
		     catch(Exception e){
				e.printStackTrace();
				red.setMessage("Failed");
				red.setIsError(true);
		     }
		return red;	
	}
	
	@Override
	public GetCommonDropDownRes getDocType(String branchCode, String productId, String moduleType) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
        try{
        	//upload.getDocTypeList
        	CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TmasDocTypeMaster> pm = query.from(TmasDocTypeMaster.class);
			query.multiselect(pm.get("docType").alias("DOC_TYPE"),pm.get("docName").alias("DOC_NAME")); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TmasDocTypeMaster> pms = amend.from(TmasDocTypeMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("productId"), pms.get("productId"));
			Predicate a3 = cb.equal( pm.get("docType"), pms.get("docType"));
			Predicate a4 = cb.equal( pm.get("moduleType"), pms.get("moduleType"));
			amend.where(a1,a2,a3,a4);

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("docName")));

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("productId"), productId);
			Predicate n5 = cb.equal(pm.get("moduleType"), moduleType);
			Predicate n3 = cb.equal(pm.get("status"), "Y");
			Predicate n4 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n3,n4,n2,n5).orderBy(orderList);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
			for(int i=0;i<list.size();i++){
				CommonResDropDown range=new CommonResDropDown();
				Tuple tempMap = list.get(i);
				range.setCode(tempMap.get("DOC_TYPE")==null?"":tempMap.get("DOC_TYPE").toString());
				range.setCodeDescription(tempMap.get("DOC_NAME")==null?"":tempMap.get("DOC_NAME").toString());
				reslist.add(range);		
			}
				response.setCommonResponse(reslist);
				response.setMessage("Success");
				response.setIsError(false);
		}
			catch(Exception e) {
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}

	@Override
	public GetCommonDropDownRes getPolicyBranchDropDown(String branchCode) {
	GetCommonDropDownRes response = new GetCommonDropDownRes();
	List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		try{
			//common.select.getPolicyBranchList
			List<TmasPolicyBranch> list = polRepo.findDistinctByBranchCodeAndTmasStatusOrderByTmasPolBranchNameAsc(branchCode,"Y");
			for(int i=0;i<list.size();i++){
				CommonResDropDown range=new CommonResDropDown();
				TmasPolicyBranch tempMap =  list.get(i);
				range.setCode(tempMap.getTmasPolBranchId()==null?"":tempMap.getTmasPolBranchId().toString());
				range.setCodeDescription(tempMap.getTmasPolBranchName()==null?"":tempMap.getTmasPolBranchName().toString());
				reslist.add(range);		
				}
				response.setCommonResponse(reslist);
				response.setMessage("Success");
				response.setIsError(false);
			}
		catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getDepartmentDropDown(GetDepartmentDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TmasDepartmentMaster> pm = query.from(TmasDepartmentMaster.class);
			query.multiselect(pm.get("tmasDepartmentId").alias("TMAS_DEPARTMENT_ID"),pm.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME")).distinct(true); 
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("tmasDepartmentName")));

			Predicate n1 = cb.equal(pm.get("branchCode"), req.getBranchCode());
			Predicate n2 = cb.equal(pm.get("tmasStatus"), req.getStatus());
		
			if(StringUtils.isBlank(req.getProductCode())){
				//common.select.departlist.premium
				query.where(n1,n2).orderBy(orderList);
				
			}else if(!StringUtils.isBlank(req.getBaseLayer()) ) {
			/*	//common.select.getDepartmentList3
				//deptId
				Subquery<String> deptId = query.subquery(String.class); 
				Root<PositionMaster> pms = deptId.from(PositionMaster.class);
				deptId.select(pms.get("deptId"));
				//propNo
				Subquery<String> propNo = query.subquery(String.class); 
				Root<PositionMaster> pms1 = propNo.from(PositionMaster.class);
				propNo.select(pms1.get("proposalNo"));
				Predicate b1 = cb.notEqual( pms1.get("contractStatus"),"C");
				Predicate b2 = cb.equal( pms1.get("baseLayer"),req.getBaseLayer());
				Predicate b3 = cb.notEqual( pms1.get("proposalNo"),req.getProposalNo());
				propNo.where(b1,b2,b3);
				
				Predicate a1 = cb.equal( pms.get("proposalNo"),req.getBaseLayer());
				Expression<String> e1 = pms.get("proposalNo");
	      		Predicate a2 = e1.in(propNo);
	      		Predicate a3 = cb.or(a1,a2);
				deptId.where(a3);

				Predicate n3 = cb.equal(pm.get("tmasProductId"), req.getProductCode());
				Expression<String> e0 = pm.get("tmasDepartmentId");
	      		Predicate n4 = e0.in(deptId).not();
	      		query.where(n1,n2,n3,n4).orderBy(orderList); */
				
				//common.select.getDepartmentList
				Predicate n3 = cb.equal(pm.get("tmasProductId"), req.getProductCode());
				query.where(n1,n2,n3).orderBy(orderList);
					
			}else if(StringUtils.isBlank(req.getProposalNo()) && StringUtils.isBlank(req.getContractNo())){
				//common.select.getDepartmentList
				Predicate n3 = cb.equal(pm.get("tmasProductId"), req.getProductCode());
				query.where(n1,n2,n3).orderBy(orderList);
						
			}else if(StringUtils.isBlank(req.getContractNo()) && !StringUtils.isBlank(req.getProposalNo())){
			/*	//common.select.getDepartmentList2
				//deptId
				Subquery<String> deptId = query.subquery(String.class); 
				Root<PositionMaster> pms = deptId.from(PositionMaster.class);
				deptId.select(pms.get("deptId"));
				//propNo
				Subquery<String> propNo = query.subquery(String.class); 
				Root<PositionMaster> pms1 = propNo.from(PositionMaster.class);
				propNo.select(pms1.get("proposalNo"));
				Predicate b2 = cb.equal( pms1.get("baseLayer"),req.getBaseLayer());
				propNo.where(b2);
				
				Predicate a1 = cb.equal( pms.get("proposalNo"),req.getProposalNo());
				Predicate a2 = cb.equal( pms.get("proposalNo"),req.getBaseLayer()==""?null:req.getBaseLayer());
				Expression<String> e1 = pms.get("proposalNo");
	      		Predicate a3 = e1.in(propNo);
	      		Predicate a4 = cb.or(a1,a2,a3);
				deptId.where(a4);

				Predicate n3 = cb.equal(pm.get("tmasProductId"), req.getProductCode());
				Expression<String> e0 = pm.get("tmasDepartmentId");
	      		Predicate n4 = e0.in(deptId).not();
	      		query.where(n1,n2,n3,n4).orderBy(orderList); */
	      		
				//common.select.getDepartmentList
				Predicate n3 = cb.equal(pm.get("tmasProductId"), req.getProductCode());
				query.where(n1,n2,n3).orderBy(orderList);
					
			}else if((!StringUtils.isBlank(req.getContractNo()) && StringUtils.isBlank(req.getProposalNo())) || (!StringUtils.isBlank(req.getContractNo()))){
				//common.select.getDepartmentList1
				//deptId
				Subquery<String> deptId = query.subquery(String.class); 
				Root<PositionMaster> pms = deptId.from(PositionMaster.class);
				deptId.select(pms.get("deptId"));
				Predicate a1 = cb.equal( pms.get("contractNo"),req.getContractNo());
				Predicate a2 = cb.equal( pms.get("baseLayer"),req.getContractNo());
				Predicate a3 = cb.or(a1,a2);
				deptId.where(a3);
				
				Predicate n3 = cb.equal(pm.get("tmasProductId"), req.getProductCode());
				Expression<String> e0 = pm.get("tmasDepartmentId");
	      		Predicate n4 = e0.in(deptId).not();
	      		query.where(n1,n2,n3,n4).orderBy(orderList);
			}
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
				for(int m=0;m<list.size();m++){
					CommonResDropDown range=new CommonResDropDown();
					Tuple tempMap = list.get(m);
					range.setCode(tempMap.get("TMAS_DEPARTMENT_ID")==null?"":tempMap.get("TMAS_DEPARTMENT_ID").toString());
					range.setCodeDescription(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
					reslist.add(range);	
				}
				response.setCommonResponse(reslist);
				response.setMessage("Success");
				response.setIsError(false);
			}
			catch(Exception e){
				e.printStackTrace();
				response.setMessage("Success");
				response.setIsError(false);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getUnderwriterCountryList(String leaderUnderriter, String branchCode) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		try{
			//GET_UNDERWRITER_COUNTRY_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<CountryMaster> pm = query.from(CountryMaster.class);
			query.multiselect(pm.get("countryId").alias("COUNTRY_ID"),pm.get("countryName").alias("COUNTRY_NAME")); 

			//country
			Subquery<String> country = query.subquery(String.class); 
			Root<PersonalInfo> pms = country.from(PersonalInfo.class);
			country.select(pms.get("country"));
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PersonalInfo> pi = amend.from(PersonalInfo.class);
			amend.select(cb.max(pi.get("amendId")));
			Predicate b3 = cb.equal( pi.get("customerId"), pms.get("customerId"));
			Predicate b4 = cb.equal( pi.get("branchCode"), pms.get("branchCode"));
			amend.where(b3,b4);
			
			Predicate a1 = cb.equal( pms.get("customerId"), leaderUnderriter);
			Predicate a2 = cb.equal( pm.get("countryId"), pms.get("country"));
			Predicate a3 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a4 = cb.equal( pms.get("amendId"), amend);
			country.where(a1,a2,a3,a4);
			
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(pm.get("status"), "Y");
			Predicate n4 = cb.equal(pm.get("countryId"), country);
			query.where(n1,n3,n4);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			for(int i=0;i<list.size();i++){
				CommonResDropDown range=new CommonResDropDown();    
				Tuple tempMap =  list.get(i);
				range.setCode(tempMap.get("COUNTRY_ID")==null?"":tempMap.get("COUNTRY_ID").toString());
				range.setCodeDescription(tempMap.get("COUNTRY_NAME")==null?"":tempMap.get("COUNTRY_NAME").toString());
				reslist.add(range);		   
			}
				response.setCommonResponse(reslist);
				response.setMessage("Success");
				response.setIsError(false);
		}
		catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getProfitCentreDropDown(String branchCode) {
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		try{
			//common.select.getProfitCenterList
			//List<TmasPfcMaster> list = pfcRepo.findDistinctByBranchCodeAndTmasStatusOrderByTmasPfcNameAsc(branchCode,"Y");
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
			Root<TmasPfcMaster> Root = cq.from(TmasPfcMaster.class);
			
			cq.multiselect(Root.get("tmasPfcId").alias("TMAS_PFC_ID"),
					Root.get("tmasPfcName").alias("TMAS_PFC_NAME"),
					Root.get("produtid").alias("PRODUTID"))
			.where(cb.equal(Root.get("branchCode"), branchCode),
					cb.equal(Root.get("tmasStatus"), "Y"));
			
			cq.orderBy(cb.asc(Root.get("tmasPfcName")));
			
			List<Tuple> list = em.createQuery(cq).getResultList();		
			
			for(int i=0;i<list.size();i++){
				CommonResDropDown range=new CommonResDropDown();  
				Tuple tempMap = list.get(i);
				range.setCode(tempMap.get("TMAS_PFC_ID")==null?"":tempMap.get("TMAS_PFC_ID").toString());
				range.setCodeDescription(tempMap.get("TMAS_PFC_NAME")==null?"":tempMap.get("TMAS_PFC_NAME").toString());
				reslist.add(range);		
        	}
			response.setCommonResponse(reslist);
			response.setMessage("Success");
			response.setIsError(false);
		}
		catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	@Override
	public GetCommonDropDownRes RenewalDropDown(String branchCode, String productCode, String status) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		try{
			//common.select.getDepartmentList
			List<TmasDepartmentMaster> list = deptRepo.findDistinctByBranchCodeAndTmasProductIdAndTmasStatusOrderByTmasDepartmentNameAsc(branchCode,new BigDecimal(productCode),status);
			for(int i=0;i<list.size();i++){
				CommonResDropDown range=new CommonResDropDown();  
				TmasDepartmentMaster tempMap = list.get(i);
				range.setCode(tempMap.getTmasDepartmentId()==null?"":tempMap.getTmasDepartmentId().toString());
				range.setCodeDescription(tempMap.getTmasDepartmentName()==null?"":tempMap.getTmasDepartmentName().toString());
				reslist.add(range);		
			}
			response.setCommonResponse(reslist);
			response.setMessage("Success");
			response.setIsError(false);
		}
		catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getTerritoryDropDown(String branchCode) {
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		try {
			//common.select.getTerritoryList
			//List<TerritoryMaster> list = tmRepo.findDistinctByBranchCodeAndTerritoryStatusOrderByTerritoryDescrAsc(branchCode,"Y");
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Tuple> cq = cb.createQuery(Tuple.class);
			Root<TerritoryMaster> Root = cq.from(TerritoryMaster.class);
			
			cq.multiselect(Root.get("territoryCode").alias("TERRITORY_CODE"),
					Root.get("territoryDesc").alias("TERRITORY_DESC")).distinct(true)
			
			.where(cb.equal(Root.get("branchCode"), branchCode),
					cb.equal(Root.get("territoryStatus"), "Y"));
			
			cq.orderBy(cb.asc(Root.get("territoryDesc")));
			
			List<Tuple> list = em.createQuery(cq).getResultList();
					
					
			for(int i=0;i<list.size();i++){
				CommonResDropDown range=new CommonResDropDown();  
				Tuple tempMap =  list.get(i);
				range.setCode(tempMap.get("TERRITORY_CODE")==null?"":tempMap.get("TERRITORY_CODE").toString());
				range.setCodeDescription(tempMap.get("TERRITORY_DESC")==null?"":tempMap.get("TERRITORY_DESC").toString());
				reslist.add(range);		
			}
			response.setCommonResponse(reslist);
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
	public CommonResponse updateSubClass(String proposalNo, String type) {
		CommonResponse response = new CommonResponse();
		try {
		/*	//UPDATE_SUBCLASS_QUERY
			  StoredProcedureQuery integration = null;
			  integration = em.createStoredProcedureQuery("UPDATE_SUB")
			  .registerStoredProcedureParameter("PVPROPOSAL_NO", String.class, ParameterMode.IN)
			  .registerStoredProcedureParameter("pvstatus", String.class, ParameterMode.IN)
			  .registerStoredProcedureParameter("lcnt", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("Lvnewproposal", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("LvnewProductid", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("LVSLIDESCLASS", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("LVCMBINESCLASS", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("LVCRESTACLASS", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("LVLOSCOMBINESCLASS", String.class, ParameterMode.OUT)
			  .setParameter("PVPROPOSAL_NO",proposalNo)
			  .setParameter("pvstatus", type);
			  integration.execute(); */
			// output=(String) integration.getOutputParameterValue("pvQuoteNo");
			
			String query = "UPDATE_SUB_LAYER_INFO";
		
			int result = queryImpl.updateQuery(query,new String[]{proposalNo,proposalNo});
			query = "UPDATE_SUB_LAYER_RISK";
			result = queryImpl.updateQuery(query,new String[]{proposalNo,proposalNo});
			
			System.out.println(result);

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
	public CommonResponse updateRenewalEditMode(String proposalNo, String status, String updateProposalNo) {
		CommonResponse response = new CommonResponse();
		String proposal = "";
		try{
			//GET_BASE_PROPOSAL_NO
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			query.multiselect(cb.selectCase().when(cb.isNull(pm.get("proposalNo")), 0l)
					.otherwise(pm.get("proposalNo"))).distinct(true); 
			
			//contractNo
			Subquery<Long> cont = query.subquery(Long.class); 
			Root<PositionMaster> cs = cont.from(PositionMaster.class);
			cont.select(cs.get("oldContractno")).distinct(true);
			//amendId
			Subquery<Long> amendId = query.subquery(Long.class); 
			Root<PositionMaster> cs1 = amendId.from(PositionMaster.class);
			amendId.select(cb.max(cs1.get("amendId")));
			Predicate c1 = cb.equal( pm.get("proposalNo"), cs1.get("proposalNo"));
			amendId.where(c1);
			
			Predicate b1 = cb.equal( cs.get("proposalNo"), proposalNo);
			Predicate b2 = cb.equal( cs.get("amendId"), amendId);
			cont.where(b1,b2);

			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);

			// Where
			Predicate n1 = cb.isNull(pm.get("baseLayer"));
			Predicate n2 = cb.equal(pm.get("contractNo"), cont);
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n3,n2);
			
			TypedQuery<Long> result = em.createQuery(query);
			List<Long> list = result.getResultList();
			
				proposal = list.size()==0?"0":list.get(0).toString();

				if(!"0".equalsIgnoreCase(proposal)){
			updateSubEditMode(proposal,status,updateProposalNo);
			updateEditMode(proposal,status,updateProposalNo);
			}
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Transactional
	@Override
	public CommonResponse updateSubEditMode(String proposalNo, String status, String updateProposalNo) {
		CommonResponse response = new CommonResponse();
		try{
			//user requested cancel of current operation
			//UPDATE_SUB_ENDT_STATUS
			String args[] = new String[2];
			if(!"N".equalsIgnoreCase(status)){
			args[0] = status +"-"+ updateProposalNo;
			}
			else{
				args[0] = status;	
			}
			args[1] = proposalNo;
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
			update.set("editMode", args[0] );
			
			//propNo
			Subquery<Long> propNo = update.subquery(Long.class); 
			Root<PositionMaster> cs = propNo.from(PositionMaster.class);
			propNo.select(cs.get("proposalNo"));
			//amendId
			Subquery<Long> amendId = update.subquery(Long.class); 
			Root<PositionMaster> cs1 = amendId.from(PositionMaster.class);
			amendId.select(cb.max(cs1.get("amendId")));
			Predicate c1 = cb.equal( cs.get("proposalNo"), cs1.get("proposalNo"));
			Predicate c2 = cb.notEqual( cs1.get("contractStatus"), "C");
			amendId.where(c1,c2);
			
			Predicate b1 = cb.equal( cs.get("baseLayer"), args[1]);
			Predicate b2 = cb.equal( cs.get("amendId"), amendId);
			propNo.where(b1,b2);
			
			//amend
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( m.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);

			Expression<String> e0 = m.get("proposalNo");
			Predicate n1 = e0.in(propNo==null?null:propNo);
			Expression<String> e1 = m.get("amendId");
			Predicate n2 = e1.in(amend==null?null:amend);
			update.where(n1,n2);
			em.createQuery(update).executeUpdate();

			response.setMessage("Success");
			response.setIsError(false);
	 }catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Transactional
	@Override
		public CommonResponse updateEditMode(String proposalNo, String status,String updateProposalNo) {
			CommonResponse response = new CommonResponse();
			try{
				//POS_MAS_ED_MODE_UPDT
				String args[] = new String[2];
				if(!"N".equalsIgnoreCase(status)){
					args[0] = status +"-"+ updateProposalNo;
					}
					else{
						args[0] = status;	
					}
				args[1] = proposalNo;
				CriteriaBuilder cb = this.em.getCriteriaBuilder();
				CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
				Root<PositionMaster> m = update.from(PositionMaster.class);
				update.set("editMode", args[0] );
				
				//amend
				Subquery<Long> amend = update.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(m.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);

				Expression<String> e1 = m.get("amendId");
				Predicate n2 = e1.in(amend==null?null:amend);
				update.where(n2);
				em.createQuery(update).executeUpdate();
				
				response.setMessage("Success");
				response.setIsError(false);
		 }catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
		}

	public GetCommonValueRes getBaseProposal(String proposalNo) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result="";
		try{
			//GET_BASE_PROPOSAL
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			query.select(pm.get("baseLayer")); 
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal(pm.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2);
			TypedQuery<String> res1 = em.createQuery(query);
			List<String> list = res1.getResultList();
			
			if (!CollectionUtils.isEmpty(list)) {
				result = list.get(0) == null ? "": list.get(0).toString();
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
	 }catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	
	}
		public GetCommonDropDownRes getSubProfitCentreDropDown(String deptid,String branchCode,String productCode){
			GetCommonDropDownRes response = new GetCommonDropDownRes();
			List<CommonResDropDown>  resList = new ArrayList<CommonResDropDown>();
			try{
				//common.select.getSubProfitCenterList
			List<TmasSpfcMaster> list = spfcRepo.findDistinctByBranchCodeAndTmasProductIdAndTmasStatusAndTmasDepartmentIdOrderByTmasSpfcNameAsc(
					branchCode,new BigDecimal(productCode),"Y",new BigDecimal(deptid));
			
				 for(int i=0 ; i<list.size() ; i++) {
					 CommonResDropDown res = new CommonResDropDown();
					 TmasSpfcMaster tempMap =  list.get(i);
					 res.setCode(tempMap.getTmasSpfcId()==null?"":tempMap.getTmasSpfcId().toString());
					 res.setCodeDescription(tempMap.getTmasSpfcName()==null?"":tempMap.getTmasSpfcName().toString());
					 resList.add(res);
					}
				 response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
		 }	catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
		}	
		public GetCommonDropDownRes getRetroContractDetailsList(GetRetroContractDetailsListReq req,int flag, String UWYear){
			GetCommonDropDownRes response = new GetCommonDropDownRes();
			List<CommonResDropDown>  resList = new ArrayList<CommonResDropDown>();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy") ;
			try{
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<PositionMaster> pm = query.from(PositionMaster.class);
				
				if(flag==1)	{
					if("4".equals(req.getProductid())){
						//fac.select.uwYear
						query.multiselect(pm.get("uwYear").alias("CONTDET1"),pm.get("uwYear").alias("CONTDET2")).distinct(true); 
						//amend
						Subquery<Long> amend = query.subquery(Long.class); 
						Root<PositionMaster> p = amend.from(PositionMaster.class);
						amend.select(cb.max(p.get("amendId")));
						Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
						Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
						Predicate a3 = cb.equal(pm.get("retroType"), p.get("retroType"));
						Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
						Predicate a5 = cb.equal(pm.get("contractStatus"), p.get("contractStatus"));
//						ParameterExpression<Date> param1 = cb.parameter(Date.class, req.getIncepDate());
//						Predicate a6 = cb.lessThan(param1, p.<Date>get("expiryDate"));
						Date incep = sdf.parse(req.getIncepDate());
						Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
						amend.where(a1,a2,a3,a4,a5,a6);
						
						List<Order> orderList = new ArrayList<Order>();
						orderList.add(cb.asc(pm.get("uwYear")));
						
						Predicate n1 = cb.equal(pm.get("productId"), req.getProductid());
						Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
						Predicate n3 = cb.isNotNull(pm.get("contractNo"));
						Predicate n4 = cb.notEqual(pm.get("contractNo"), 0);
						Predicate n5 = cb.equal(pm.get("retroType"),"TR");
					//	ParameterExpression<Date> param = cb.parameter(Date.class, req.getIncepDate());
						Predicate n6 = cb.greaterThan( pm.get("expiryDate"),incep);
						Predicate n7 = cb.equal(pm.get("branchCode"),req.getBranchCode());
						Predicate n8 = cb.equal(pm.get("amendId"),amend);	
						query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
					}else{
						//risk.select.uwYear
						query.multiselect(pm.get("uwYear").alias("CONTDET1"),pm.get("uwYear").alias("CONTDET2")); 
						//amend
						Subquery<Long> amend = query.subquery(Long.class); 
						Root<PositionMaster> p = amend.from(PositionMaster.class);
						amend.select(cb.max(p.get("amendId")));
						Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
						Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
						Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
						Predicate a5 = cb.equal(pm.get("contractStatus"), p.get("contractStatus"));
				//		ParameterExpression<Date> param1 = cb.parameter(Date.class, req.getIncepDate());
						Date incep = sdf.parse(req.getIncepDate());
						Predicate a6 = cb.lessThanOrEqualTo(p.get("expiryDate") ,incep);
						amend.where(a1,a2,a4,a5,a6);
						
						List<Order> orderList = new ArrayList<Order>();
						orderList.add(cb.asc(pm.get("uwYear")));
						
						Predicate n1 = cb.equal(pm.get("productId"), req.getProductid());
						Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
						Predicate n3 = cb.isNotNull(pm.get("contractNo"));
						Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
				//		ParameterExpression<Date> param = cb.parameter(Date.class, req.getIncepDate());
						Predicate n6 = cb.lessThanOrEqualTo(p.get("expiryDate") ,incep);
						Predicate n7 = cb.equal(pm.get("branchCode"),req.getBranchCode());
						Predicate n8 = cb.equal(pm.get("amendId"),amend);
						query.where(n1,n2,n3,n4,n6,n7,n8).orderBy(orderList);
					}
					
					}
				 else if(StringUtils.isNotEmpty(UWYear)&& flag==2){
					 //fac.select.retroContDet
					 Expression<String> e0 = cb.concat(pm.get("contractNo"), "-");	
					  query.multiselect(pm.get("contractNo").alias("CONTDET1"),
							 cb.selectCase().when(cb.equal(pm.get("productId"),"4"), pm.get("contractNo"))
							.otherwise(cb.selectCase().when(cb.equal(pm.get("productId"),"5"), cb.concat(e0,pm.get("layerNo"))).otherwise(null)).alias("CONTDET2")); 
						//amend
						Subquery<Long> amend = query.subquery(Long.class); 
						Root<PositionMaster> p = amend.from(PositionMaster.class);
						amend.select(cb.max(p.get("amendId")));
						Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
						Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
						Predicate a3 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(p.get("productId"),"4"), StringUtils.isBlank(req.getRetroType())?"":req.getRetroType()).otherwise("N"));
						Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
						Predicate a5 = cb.equal(pm.get("uwYear"),UWYear);
//						ParameterExpression<Date> param3 = cb.parameter(Date.class, req.getIncepDate());
//						Predicate a6 = cb.lessThan(param3, p.<Date>get("expiryDate"));
						Date incep = sdf.parse(req.getIncepDate());
						Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
						Predicate a7 = cb.equal(pm.get("rskDummyContract"),"N");
						amend.where(a1,a2,a3,a4,a5,a6,a7);
						
						List<Order> orderList = new ArrayList<Order>();
						orderList.add(cb.asc(pm.get("contractNo")));
						
						Predicate n1 = cb.equal(pm.get("productId"), req.getProductid());
						Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
						Predicate n3 = cb.isNotNull(pm.get("contractNo"));
						Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
						Predicate n5 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(pm.get("productId"),"4"), StringUtils.isBlank(req.getRetroType())?"":req.getRetroType()).otherwise("N"));
						Predicate n9 = cb.equal(pm.get("uwYear"),UWYear);
//						ParameterExpression<Date> param2 = cb.parameter(Date.class, req.getIncepDate());
//						Predicate n6 = cb.lessThan(param2, pm.<Date>get("expiryDate"));
						Predicate n6 = cb.greaterThan(p.get("expiryDate") ,incep);
						Predicate n7 = cb.equal(pm.get("branchCode"),req.getBranchCode());
						Predicate n10 = cb.equal(pm.get("rskDummyContract"),"N");
						Predicate n8 = cb.equal(pm.get("amendId"),amend);
						query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10).orderBy(orderList);
				}
				 else if(StringUtils.isNotEmpty(UWYear)&&flag==3){
					//FAC_SELECT_RETRO_DUP_CONTRACT
					 Expression<String> e0 = cb.concat(pm.get("contractNo"), "-");		
					  query.multiselect(pm.get("contractNo").alias("CONTDET1"),
							 cb.selectCase().when(cb.equal(pm.get("productId"),"4"), pm.get("contractNo"))
							.otherwise(cb.selectCase().when(cb.equal(pm.get("productId"),"5"), cb.concat(e0,pm.get("layerNo"))).otherwise(null)).alias("CONTDET2")); 
						//amend
						Subquery<Long> amend = query.subquery(Long.class); 
						Root<PositionMaster> p = amend.from(PositionMaster.class);
						amend.select(cb.max(p.get("amendId")));
						Predicate a1 = cb.equal(pm.get("productId"), p.get("productId"));
						Predicate a2 = cb.equal(pm.get("contractNo"), p.get("contractNo"));
						Predicate a3 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(p.get("productId"),"4"), "TR").otherwise("N"));
						Predicate a4 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
						Predicate a5 = cb.equal(pm.get("uwYear"),UWYear);
//						ParameterExpression<Date> param3 = cb.parameter(Date.class, req.getIncepDate());
//						Predicate a6 = cb.lessThan(param3, p.<Date>get("expiryDate"));
						Date incep = sdf.parse(req.getIncepDate());
						Predicate a6 = cb.greaterThan(p.get("expiryDate") ,incep);
						Predicate a7 = cb.equal(pm.get("rskDummyContract"),"D");
						amend.where(a1,a2,a3,a4,a5,a6,a7);
						
						List<Order> orderList = new ArrayList<Order>();
						orderList.add(cb.asc(pm.get("contractNo")));
						
						Predicate n1 = cb.equal(pm.get("productId"), "4");
						Predicate n2 = cb.equal(pm.get("contractStatus"), "A");
						Predicate n3 = cb.isNotNull(pm.get("contractNo"));
						Predicate n4 = cb.notEqual(pm.get("contractNo"),"0");
						Predicate n5 = cb.equal(cb.coalesce(pm.get("retroType"),"N"),cb.selectCase().when(cb.equal(pm.get("productId"),"4"), "TR").otherwise("N"));
						Predicate n9 = cb.equal(pm.get("uwYear"),UWYear);
//						ParameterExpression<Date> param2 = cb.parameter(Date.class, req.getIncepDate());
//						Predicate n6 = cb.lessThan(param2, pm.<Date>get("expiryDate"));
						Predicate n6 = cb.greaterThan(p.get("expiryDate") ,incep);
						Predicate n7 = cb.equal(pm.get("branchCode"),req.getBranchCode());
						Predicate n10 = cb.equal(pm.get("rskDummyContract"),"D");
						Predicate n8 = cb.equal(pm.get("amendId"),amend);
						query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10).orderBy(orderList);
				}
				TypedQuery<Tuple> res1 = em.createQuery(query);
				List<Tuple> list = res1.getResultList();
				
				if(list!=null && list.size()>0){

					for (int i = 0; i < list.size(); i++) {
						CommonResDropDown res = new CommonResDropDown();
						Tuple insMap = list.get(i);
						res.setCode(insMap.get("CONTDET1")==null?"":insMap.get("CONTDET1").toString());
						res.setCodeDescription(insMap.get("CONTDET1")==null?"":insMap.get("CONTDET1").toString());
						resList.add(res);					
						}
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
		 }	}catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
		}
		public  String formatterpercentage(final String value)
		{
			String output="0.00";
			if(StringUtils.isNotBlank(value))
			{
				double doublevalue=Double.parseDouble(value);
				DecimalFormat myFormatter = new DecimalFormat("###,###,###,###,##0.0000");
				output =myFormatter.format(doublevalue);
			}
			return output;
		}
			
		public GetCommonValueRes getUnderWriterLimmit(String uwName,String processId,String ProductId,String deptId){
			GetCommonValueRes response=new GetCommonValueRes();
			String uwLimit=null;
			try{
				//common.select.getUWLimit1
				UnderwritterCapacityMaster uwList = uwcRepo.findByUnderwriteridAndProductIdAndDepartmentidAndStatus(uwName,ProductId,deptId,"Y");
				
				if(uwList!=null) {
					uwLimit=uwList.getUnderwritterLimit()==null?"0":fm.decimalFormat(uwList.getUnderwritterLimit().toString(),0);
				}else
					uwLimit ="0";
				response.setCommonResponse(uwLimit);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed"); 
				response.setIsError(true);
			}
			return response;
		}
		
		public List<GetContractValidationRes> getContractValidation(String productId, String cedingCompany,String inceptionDate, String expiryDate, String year,String originalCurrency, String departmentId, String type,String sumInsured, String ContNo, String profitCenter,String surplus, String coverPer, String dedPer, String layerNo, String branchCode) {
			String query="";
		//	GetContractValidationRes1 response = new GetContractValidationRes1();
			List<GetContractValidationRes> resList = new ArrayList<GetContractValidationRes>();
			List<Map<String,Object>> ContractList=new ArrayList<Map<String,Object>>();
			String args[] = null;
			try{
				if(StringUtils.isBlank(sumInsured)){
					sumInsured="0";
				}
				if(StringUtils.isBlank(surplus)){
					surplus="0";
				}
				if(StringUtils.isBlank(coverPer)){
					coverPer="0";
				}
				if(StringUtils.isBlank(dedPer)){
					dedPer="0";
				}
				if(!cedingCompany.matches("^[0-9]+$")){
					cedingCompany="";
				}
				if("1".equalsIgnoreCase(productId)){
					query="FAC_CONTRACT_LIST";
					 args = new String[10];
						args[0] = cedingCompany;
						args[1] = inceptionDate;
						args[2] = expiryDate;
						args[3] = year;
						args[4] = originalCurrency;
						
						args[5] = departmentId;
						args[6] = type;
						args[7] = sumInsured;
						args[8] = profitCenter;
						args[9] = branchCode;
						if(ContNo!="0"){
						query="FAC_CONTRACT_LIST1";
					}
				}
				else if("2".equalsIgnoreCase(productId)){
					query="PTTY_CONTRACT_LIST";
					args = new String[9];
					args[0] = cedingCompany;
					args[1] = inceptionDate;
					args[2] = expiryDate;
					args[3] = year;
					args[4] = originalCurrency;
					args[5] = departmentId;
					args[6] = type;
					args[7] = profitCenter;
					args[8] = branchCode;
					if("1".equalsIgnoreCase(type) ||"4".equalsIgnoreCase(type)||"5".equalsIgnoreCase(type) ){
						//query+="  and RP.RSK_LIMIT_OC="+req.getSumInsured();
						query="PTTY_CONTRACT_LIST1";
					}
					else if("2".equalsIgnoreCase(type)){
						//query+="  and RP.RSK_TREATY_SURP_LIMIT_OC="+req.getSumInsured();
						query="PTTY_CONTRACT_LIST2";
					}
					else if("3".equalsIgnoreCase(type)){
						//query+="  and RP.RSK_LIMIT_OC="+req.getSumInsured()+" and RP.RSK_TREATY_SURP_LIMIT_OC = "+req.getSurplus();
						query="PTTY_CONTRACT_LIST3";
					}
					if(ContNo!="0"){
						//query+="  and RD.RSK_CONTRACT_NO!="+req.getContno();
						query="PTTY_CONTRACT_LIST4";
					}
				}  if("3".equalsIgnoreCase(productId)){
					if(ContNo!="0"){
						query="NPTTY_CONTRACT_LIST_CON";
						args = new String[11];
						args[0] = cedingCompany;
						args[1] = inceptionDate;
						args[2] = expiryDate;
						args[3] = year;
						args[4] = originalCurrency;
						args[5] = departmentId;
						args[6] = type;
						args[7] = profitCenter;
						args[8] = layerNo;
						args[9] = ContNo;
						args[10] = branchCode;
					}
					else{
						query="NPTTY_CONTRACT_LIST";
						args = new String[10];
						args[0] = cedingCompany;
						args[1] = inceptionDate;
						args[2] = expiryDate;
						args[3] = year;
						args[4] = originalCurrency;
						args[5] = departmentId;
						args[6] = type;
						args[7] = profitCenter;
						args[8] = layerNo;
						args[9] = branchCode;
					}
					
					if("1".equalsIgnoreCase(type) ||"2".equalsIgnoreCase(type)||"3".equalsIgnoreCase(type)||"4".equalsIgnoreCase(type)||"5".equalsIgnoreCase(type) ){
						//query+="  and  E1.cvr_limit="+req.getSumInsured()+" AND E1.DEDU="+req.getSurplus();
						query="NPTTY_CONTRACT_LIST_CON1";
					}
					else if("5".equalsIgnoreCase(type)){
						//query+="  and  E1.cvr_limit="+req.getSumInsured()+" AND E1.DEDU="+req.getSurplus()+" AND E1.COVER_PER = "+req.getCoverPer()+" AND E1.DED_PER = "+req.getDedPer() ;
						query="NPTTY_CONTRACT_LIST1";
					}
				}
		
				
				ContractList =queryImpl.selectList(query,args);
			
		
					 for(int i=0 ; i<ContractList.size() ; i++) {
						 GetContractValidationRes res = new GetContractValidationRes();
							Map<String,Object> tempMap = (Map<String,Object>) ContractList.get(i);
							res.setContractNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
							res.setEndoresmentNo(tempMap.get("RSK_ENDORSEMENT_NO")==null?"":tempMap.get("RSK_ENDORSEMENT_NO").toString());
							res.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
							res.setBrokerId(tempMap.get("RSK_BROKERID")==null?"":tempMap.get("RSK_BROKERID").toString());
							res.setUnderWritter(tempMap.get("RSK_UNDERWRITTER")==null?"":tempMap.get("RSK_UNDERWRITTER").toString());
							res.setInsuredName(tempMap.get("RSK_INSURED_NAME")==null?"":tempMap.get("RSK_INSURED_NAME").toString());
							
							resList.add(res);
						}	
				
				
		 	}catch(Exception e){
					log.error(e);
					e.printStackTrace();
					
				}
			return resList;
		}
		//Productid 3
//		public List<GetContractValidationRes3> getContractValidation3(String productId, String cedingCompany,String inceptionDate, String expiryDate, String year,String originalCurrency, String departmentId, String type,String sumInsured, String ContNo, String profitCenter,String surplus, String coverPer, String dedPer, String layerNo, String branchCode) {
//			String query="";
//		
//			List<GetContractValidationRes3> resList = new ArrayList<GetContractValidationRes3>();
//			List<Map<String,Object>> ContractList=new ArrayList<Map<String,Object>>();
//			String args[] = null;
//			try{
//				if(StringUtils.isBlank(sumInsured)){
//					sumInsured="0";
//				}
//				if(StringUtils.isBlank(surplus)){
//					surplus="0";
//				}
//				if(StringUtils.isBlank(coverPer)){
//					coverPer="0";
//				}
//				if(StringUtils.isBlank(dedPer)){
//					dedPer="0";
//				}
//				if(!cedingCompany.matches("^[0-9]+$")){
//					cedingCompany="";
//				}
//	
//				 if("3".equalsIgnoreCase(productId)){
//					if(ContNo!="0"){
//						query="NPTTY_CONTRACT_LIST_CON";
//						args = new String[11];
//						args[0] = cedingCompany;
//						args[1] = inceptionDate;
//						args[2] = expiryDate;
//						args[3] = year;
//						args[4] = originalCurrency;
//						args[5] = departmentId;
//						args[6] = type;
//						args[7] = profitCenter;
//						args[8] = layerNo;
//						args[9] = ContNo;
//						args[10] = branchCode;
//					}
//					else{
//						query="NPTTY_CONTRACT_LIST";
//						args = new String[10];
//						args[0] = cedingCompany;
//						args[1] = inceptionDate;
//						args[2] = expiryDate;
//						args[3] = year;
//						args[4] = originalCurrency;
//						args[5] = departmentId;
//						args[6] = type;
//						args[7] = profitCenter;
//						args[8] = layerNo;
//						args[9] = branchCode;
//					}
//					
//					if("1".equalsIgnoreCase(type) ||"2".equalsIgnoreCase(type)||"3".equalsIgnoreCase(type)||"4".equalsIgnoreCase(type)||"5".equalsIgnoreCase(type) ){
//						//query+="  and  E1.cvr_limit="+req.getSumInsured()+" AND E1.DEDU="+req.getSurplus();
//						query="NPTTY_CONTRACT_LIST_CON1";
//					}
//					else if("5".equalsIgnoreCase(type)){
//						//query+="  and  E1.cvr_limit="+req.getSumInsured()+" AND E1.DEDU="+req.getSurplus()+" AND E1.COVER_PER = "+req.getCoverPer()+" AND E1.DED_PER = "+req.getDedPer() ;
//						query="NPTTY_CONTRACT_LIST1";
//					}
//				}
//				
//				ContractList =queryImpl.selectList(query,args);
//			
//				
//					 for(int i=0 ; i<ContractList.size() ; i++) {
//						 GetContractValidationRes3 res = new GetContractValidationRes3();
//							Map<String,Object> tempMap = (Map<String,Object>) ContractList.get(i);
////							res.setDedPer(tempMap.get("DED_PER")==null?"":tempMap.get("DED_PER").toString());
////							res.setLayerNo(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
////							res.setCoverPer(tempMap.get("COVER_PER")==null?"":tempMap.get("COVER_PER").toString());
////							res.setDedu(tempMap.get("DEDU")==null?"":tempMap.get("DEDU").toString());
////							res.setCoverLimit(tempMap.get("CVR_LIMIT")==null?"":tempMap.get("CVR_LIMIT").toString());
//							
//							
//							res.setInsuredName(tempMap.get("RSK_INSURED_NAME")==null?"":tempMap.get("RSK_INSURED_NAME").toString());
//							res.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
//							res.setContractNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
//							res.setEndoresmentNo(tempMap.get("RSK_ENDORSEMENT_NO")==null?"":tempMap.get("RSK_ENDORSEMENT_NO").toString());
//							res.setBrokerId(tempMap.get("RSK_BROKERID")==null?"":tempMap.get("RSK_BROKERID").toString());
//							res.setUnderWritter(tempMap.get("RSK_UNDERWRITTER")==null?"":tempMap.get("RSK_UNDERWRITTER").toString());
//							
//							resList.add(res);
//						
//					 }
//				
//		 	}catch(Exception e){
//					log.error(e);
//					e.printStackTrace();
//					
//				}
//			return resList;
//		}
		public String getAcceptanceDate(String proposalNo) {
			String result="";
			try{
				//GET_ACCEPTANCE_DATE
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<PositionMaster> pm = query.from(PositionMaster.class);
				query.multiselect(pm.get("accountDate").alias("ACCOUNT_DATE")); 

				Subquery<Long> amend = query.subquery(Long.class); 
				Root<PositionMaster> pms = amend.from(PositionMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				amend.where(a1);

				Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
				Predicate n2 = cb.equal(pm.get("amendId"), amend);
				query.where(n1,n2);
				
				TypedQuery<Tuple> res1 = em.createQuery(query);
				List<Tuple> list = res1.getResultList();
				if (!CollectionUtils.isEmpty(list)) {
					result = list.get(0).get("ACCOUNT_DATE")==null ? "" 
							: sdf.format(list.get(0).get("ACCOUNT_DATE"));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return result;
		}	
		public  boolean GetShareEqualValidation(String productId,String leaderUnderwriterShare, String proposalNo ) {
			boolean result=false;
			int count=0;
			try {
				List<Integer> list = null;
				if("1".equals(productId)){
					//GET_SIGN_SHARE_EQUAL_PRODUCT1
					CriteriaBuilder cb = em.getCriteriaBuilder(); 
					CriteriaQuery<Integer> query = cb.createQuery(Integer.class); 
					Root<TtrnFacRiskProposal> pm = query.from(TtrnFacRiskProposal.class);
					query.multiselect(cb.count(pm)); 

					Subquery<Long> end = query.subquery(Long.class); 
					Root<TtrnFacRiskProposal> pms = end.from(TtrnFacRiskProposal.class);
					end.select(cb.max(pms.get("rskEndorsementNo")));
					Predicate a1 = cb.equal(pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
					end.where(a1);

					Predicate n1 = cb.equal(pm.get("shareSigned"), leaderUnderwriterShare);
					Predicate n2 = cb.equal(pm.get("rskProposalNumber"), proposalNo);
					Predicate n3 = cb.equal(pm.get("rskEndorsementNo"), end);
					query.where(n1,n2,n3);
					
					TypedQuery<Integer> res1 = em.createQuery(query);
					 list = res1.getResultList();
				}else{
					//GET_SIGN_SHARE_EQUAL_PRODUCT23
					CriteriaBuilder cb = em.getCriteriaBuilder(); 
					CriteriaQuery<Integer> query = cb.createQuery(Integer.class); 
					Root<TtrnRiskProposal> pm = query.from(TtrnRiskProposal.class);
					query.multiselect(cb.count(pm)); 

					Subquery<Long> end = query.subquery(Long.class); 
					Root<TtrnRiskProposal> pms = end.from(TtrnRiskProposal.class);
					end.select(cb.max(pms.get("rskEndorsementNo")));
					Predicate a1 = cb.equal(pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
					end.where(a1);

					Predicate n1 = cb.equal(pm.get("rskShareSigned"), leaderUnderwriterShare);
					Predicate n2 = cb.equal(pm.get("rskProposalNumber"), proposalNo);
					Predicate n3 = cb.equal(pm.get("rskEndorsementNo"), end);
					query.where(n1,n2,n3);
					
					TypedQuery<Integer> res1 = em.createQuery(query);
					list = res1.getResultList();
				}
				if (list != null) {
					count = list.get(0) == null ? 0: list.get(0);
				}
				if(count==0){
					result=true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

	



//@Override
//	public GetCommonDropDownRes getCeaseaccountStatus(String ContractNo) {
//		// TODO Auto-generated method stub
//		return null;
//	}

@Override
public GetCommonDropDownRes getCrestaIDList(String branchCode, String crestaValue) {
	GetCommonDropDownRes response = new GetCommonDropDownRes();
	try{
		
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		//GET_CRESTAID_LIST
		List<TmasCrestaMaster> list = cresRepo.findByBranchCodeAndStatusAndTerritoryCode(branchCode,"Y",StringUtils.isBlank(crestaValue)?"":crestaValue);

	for(int i=0;i<list.size();i++)	{
		CommonResDropDown range=new CommonResDropDown();  
		TmasCrestaMaster tempMap = list.get(i);
		range.setCode(tempMap.getCrestaId()==null?"":tempMap.getCrestaId().toString());
		range.setCodeDescription(tempMap.getCrestaName()==null?"":tempMap.getCrestaName().toString());
		reslist.add(range);		
	}
    response.setCommonResponse(reslist);
	response.setMessage("Success");
	response.setIsError(false);
}
	catch(Exception e){
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
}

@Override
public GetCommonDropDownRes getCrestaNameList(String branchCode, String crestaValue)
{
	GetCommonDropDownRes response = new GetCommonDropDownRes();
	List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
	try{
		//GET_CRESTA_NAME_LIST
		List<TmasCrestaMaster> list = cresRepo.findByBranchCodeAndStatusAndCrestaId(branchCode,"Y",StringUtils.isBlank(crestaValue)?"":crestaValue);
		for(int i=0;i<list.size();i++){
			CommonResDropDown range=new CommonResDropDown();  
			TmasCrestaMaster tempMap =  list.get(i);
			range.setCode(tempMap.getCrestaId()==null?"":tempMap.getCrestaId().toString());
			range.setCodeDescription(tempMap.getCrestaName()==null?"":tempMap.getCrestaName().toString());
			reslist.add(range);		
		}
		   response.setCommonResponse(reslist);
			response.setMessage("Success");
			response.setIsError(false);
		}
	
	catch(Exception e){
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
}

@Override
public GetCommonValueRes getCeaseaccountStatus(String contractNo)
{
	GetCommonValueRes response = new GetCommonValueRes();
	String ceaseStatus="";
	int count=0;
	try{
		//GET_SELECT_ORDER (union all)
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		Root<RskPremiumDetails> pm = query.from(RskPremiumDetails.class);
		query.multiselect(pm.get("netdueOc").alias("NETDUE_OC"),pm.get("allocatedTillDate").alias("ALLOCATED_TILL_DATE"));
		Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
		query.where(n1);
		TypedQuery<Tuple> res1 = em.createQuery(query);
		List<Tuple> list = res1.getResultList();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Tuple map=list.get(i);
				if(!((map.get("NETDUE_OC")==null?"":map.get("NETDUE_OC").toString()).equalsIgnoreCase((map.get("ALLOCATED_TILL_DATE")==null?"":map.get("ALLOCATED_TILL_DATE").toString())))){
					count=count+1;
				}
					}
		}
		CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
		Root<TtrnClaimPayment> cp = query1.from(TtrnClaimPayment.class);
		query1.multiselect(cp.get("paidAmountOc").alias("NETDUE_OC"),cp.get("allocatedTillDate").alias("ALLOCATED_TILL_DATE"));
		Predicate n2 = cb.equal(cp.get("contractNo"), contractNo);
		query1.where(n2);
		TypedQuery<Tuple> res = em.createQuery(query1);
		List<Tuple> list1 = res.getResultList();
		if(list1!=null && list1.size()>0){
			for(int i=0;i<list1.size();i++){
				Tuple map=list1.get(i);
				if(!((map.get("NETDUE_OC")==null?"":map.get("NETDUE_OC").toString()).equalsIgnoreCase((map.get("ALLOCATED_TILL_DATE")==null?"":map.get("ALLOCATED_TILL_DATE").toString())))){
					count=count+1;
				}
					}
		}
		if(count>0){
			ceaseStatus="N";
		}
			response.setCommonResponse(ceaseStatus);
			response.setMessage("Success");
			response.setIsError(false);
		}
		catch(Exception e){
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
	}
	return response;
}



@Override
public GetCommonDropDownRes getUnderWritterDropDown(String branchCode, String attachedUW) {
	List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
	GetCommonDropDownRes response = new GetCommonDropDownRes();
	try {
		List<String> attachedUWList =new ArrayList<String>(Arrays.asList(attachedUW.split(","))) ;
		List<UnderwritterMaster> list = null;
		if(StringUtils.isNotBlank(attachedUW) && !"ALL".equalsIgnoreCase(attachedUW)) {
			//GET_UNDERWRITER_ATTACHED
    	 	list = uwRepo.findDistinctByBranchCodeAndUwrStatusAndUwrCodeInOrderByUnderwritterAsc(branchCode,"Y",attachedUWList);
		}
		else {
			 //common.select.getUWList
			 list = uwRepo.findDistinctByBranchCodeAndUwrStatusOrderByUnderwritterAsc(branchCode,"Y");
		}
 	for(int i=0;i<list.size();i++){
		CommonResDropDown range=new CommonResDropDown();  
		UnderwritterMaster tempMap = list.get(i);
		range.setCode(tempMap.getUwrCode()==null?"":tempMap.getUwrCode().toString());
		range.setCodeDescription(tempMap.getUnderwritter()==null?"":tempMap.getUnderwritter().toString());
		reslist.add(range);		
	}
	response.setCommonResponse(reslist);
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
public GetCommonValueRes getCrestaName(String branchCode, String crestaValue) {
	String crestaName="";
	GetCommonValueRes response = new GetCommonValueRes();
	try {
		//boolean s = getPLCLCountStatus("10210007817","0");
		//GET_CRESTA_NAME_LIST
		List<TmasCrestaMaster> list = cresRepo.findByBranchCodeAndStatusAndCrestaId(branchCode,"Y",crestaValue);
		if(list!=null){
			crestaName=(list.get(0).getCrestaName()==null?"":list.get(0).getCrestaName().toString());
		}
		response.setCommonResponse(crestaName);
		response.setMessage("Success");
		response.setIsError(false);
	}
	catch(Exception e){
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
		return response;
}

@Override
public GetCommonValueRes getDisableStatus(String contractNo, String layerNo) {
	GetCommonValueRes response =new GetCommonValueRes();
	String status="Y";
	int count = 0;
	try {
		if(StringUtils.isBlank(contractNo)){
			contractNo="";
		}
		//GET_DISABLE_STATUS
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Long> query = cb.createQuery(Long.class); 
		Root<TtrnAllocatedTransaction> pm = query.from(TtrnAllocatedTransaction.class);
		query.multiselect(cb.count(pm));
		Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
		Predicate n2 = cb.equal(pm.get("status"), "Y");
		Predicate n3 = cb.equal(cb.coalesce(pm.get("layerNo"), "0"),layerNo==null?"0":layerNo);
		query.where(n1,n2,n3);
		TypedQuery<Long> res1 = em.createQuery(query);
		List<Long> list = res1.getResultList();
		if(list!=null) {
			count = list.get(0)==null?0:list.get(0).intValue();
			}
		if(count>0) {
			status = "Y";
		}else {
			status = "N";
		}
		response.setCommonResponse(status);
		response.setMessage("Success");
		response.setIsError(false);
}
catch(Exception e){
	e.printStackTrace();
	response.setMessage("Failed");
	response.setIsError(true);
}
return response;
}

@Override
public GetCommonValueRes getCopyQuote(GetCopyQuoteReq req) {
	GetCommonValueRes response = new GetCommonValueRes();
	String newProposalNo="";
	Connection con = null;
	//CallableStatement cstmt = null;
	 StoredProcedureQuery cstmt = null;
	try{
//	con = queryImpl.getDataSource().getConnection();
//	
//	cstmt = con.prepareCall("{CALL copyquote(?,?,?,?,?)}");
//	cstmt.setString(1, type);
//	cstmt.setString(2, "");
//	cstmt.setString(3, productId);
//	cstmt.setString(4, branchCode);
//	cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
//	cstmt.setString(5, proposalNo);
//	boolean count = cstmt.execute();
//	newProposalNo = cstmt.getString(5);
	response.setCommonResponse(newProposalNo);
	response.setMessage("Success");
	response.setIsError(false);
}
catch(Exception e){
e.printStackTrace();
response.setMessage("Failed");
response.setIsError(true);
}
return response;
}

@Override
public GetCommonValueRes getAllocationDisableStatus(String contractNo, String layerNo) {
	GetCommonValueRes response = new GetCommonValueRes();
	String status = "";
	int count = 0;
	try {
		if (StringUtils.isBlank(contractNo)) {
			contractNo = "";
		}
		//GET_ALLOCATION_DISABLE_STATUS
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Long> query = cb.createQuery(Long.class); 
		Root<TtrnAllocatedTransaction> pm = query.from(TtrnAllocatedTransaction.class);
		query.multiselect(cb.count(pm));
		Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
		Predicate n3 = cb.equal(cb.coalesce(pm.get("layerNo"), "0"),layerNo==null?"0":layerNo);
		query.where(n1,n3);
		TypedQuery<Long> res1 = em.createQuery(query);
		List<Long> list = res1.getResultList();
		if(list!=null) {
			count = list.get(0)==null?0:list.get(0).intValue();
			}
		if(count>0) {
			status = "Y";
		}else {
			status = "N";
		}
	
	response.setCommonResponse(status);
	response.setMessage("Success");
	response.setIsError(false);
	} catch(Exception e){
		log.error(e);
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
		return response;
	}

	@Override
	public CommonResponse updatepositionMasterEndtStatus(String proposalNo, String endtDate,String ceaseStatus) {
		CommonResponse response = new CommonResponse();
		try {
			//UPDATE_POSITION_MASTER_END_STATUS
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
			//amend
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal(m.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);
			
			update.set("endtStatus", "Y");
			update.set("contractStatus", cb.selectCase().when(cb.equal(m.get("proposalStatus"),"A"), "A")
					.otherwise(m.get("contractStatus")));
			update.set("endorsementDate", sdf.parse(endtDate));
			update.set("ceaseStatus", ceaseStatus);
			Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(m.get("amendId"), amend);
			update.where(n1,n2);
			em.createQuery(update).executeUpdate();

			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getYearListValue(String inceptionDate) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> yearsList = new ArrayList<CommonResDropDown>();
		
		String format = "dd/MM/yyyy";
		try {
			if (StringUtils.isNotBlank(inceptionDate)) {
			  
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				Date dateAsObj = null;
				dateAsObj = sdf.parse(inceptionDate);
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateAsObj);
				
				// cal.add(Calendar.MONTH, nbMonths);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH);
				int day = cal.get(Calendar.DATE);
				
					CommonResDropDown res = new CommonResDropDown();

					res.setCode(String.valueOf(year));
					res.setCodeDescription(String.valueOf(year));
					yearsList.add(res);

				if (month == 11) {
					if (day >= 25) {
						cal.add(Calendar.YEAR, 1);
						year = cal.get(Calendar.YEAR);

						res.setCode(String.valueOf(year));
						res.setCodeDescription(String.valueOf(year));
						yearsList.add(res);

					}
				} else if (month == 0) {
					if (7 >= day) {
						cal.add(Calendar.YEAR, -1);
						year = cal.get(Calendar.YEAR);
						res.setCode(String.valueOf(year));
						res.setCodeDescription(String.valueOf(year));
						yearsList.add(res);
					}
				}

			}

			response.setCommonResponse(yearsList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public CommonResponse updateInstallmentTransaction(String proposalNo) {
		CommonResponse response = new CommonResponse();
		try {
				//UPDATE_INSTALLMENT_TRANSACTION
				CriteriaBuilder cb = this.em.getCriteriaBuilder();
				CriteriaUpdate<TtrnMndInstallments> update = cb.createCriteriaUpdate(TtrnMndInstallments.class);
				Root<TtrnMndInstallments> m = update.from(TtrnMndInstallments.class);
				//tran
				Subquery<Long> tran = update.subquery(Long.class); 
				Root<TtrnMndInstallments> ti = tran.from(TtrnMndInstallments.class);
				tran.select(ti.get("transactionNo"));
				////end
				Subquery<Long> end = update.subquery(Long.class); 
				Root<TtrnMndInstallments> rds = end.from(TtrnMndInstallments.class);
				end.select(cb.diff(cb.max(rds.get("endorsementNo")),1l));
				Predicate b1 = cb.equal( rds.get("proposalNo"), ti.get("proposalNo"));
				end.where(b1);

				Predicate a1 = cb.equal(m.get("installmentNo"), ti.get("installmentNo"));
				Predicate a2 = cb.equal(m.get("proposalNo"), ti.get("proposalNo"));
				Predicate a3 = cb.equal(m.get("contractNo"), ti.get("contractNo"));
				Predicate a4 = cb.isNotNull(m.get("transactionNo"));
				Predicate a5 = cb.equal(ti.get("endorsementNo"), end);
				tran.where(a1,a2,a3,a4,a5);
				
				update.set("transactionNo", tran);
				
				//endNo
				Subquery<Long> endNo = update.subquery(Long.class); 
				Root<TtrnMndInstallments> e = endNo.from(TtrnMndInstallments.class);
				endNo.select(cb.max(e.get("endorsementNo")));
				Predicate c1 = cb.equal( e.get("proposalNo"), m.get("proposalNo"));
				endNo.where(c1);
			
				Predicate n1 = cb.equal(m.get("proposalNo"), proposalNo);
				Predicate n2 = cb.equal(m.get("endorsementNo"), endNo);
				update.where(n1,n2);
				em.createQuery(update).executeUpdate();
			
				response.setMessage("Success");
				response.setIsError(false);
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
			return response;
	}

	@Override
	public GetCommonDropDownRes getBonusList() {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try {
			//GET_BONUS_LIST
			List<ConstantDetail> list = constRepo.findByCategoryIdAndStatus(new BigDecimal("23"),"Y");
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				ConstantDetail tempMap =  list.get(i);
				res.setCode(tempMap.getDetailName() == null ? "" : tempMap.getDetailName().toString());
				res.setCodeDescription(tempMap.getRemarks() == null ? "" : tempMap.getRemarks() .toString());
				resList.add(res);
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}
		catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getConstantDropDownET(String categoryId, String contractNo) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		String transNo = "";
		String contNo = "";
		try {
			//common.select.getConstDet
			List<ConstantDetail> list = constRepo.findDistinctByCategoryIdAndStatus(new BigDecimal(categoryId),"Y");
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				ConstantDetail tempMap =  list.get(i);
				res.setCode(tempMap.getType() == null ? "" : tempMap.getType().toString());
				res.setCodeDescription(tempMap.getDetailName() == null ? "" : tempMap.getDetailName() .toString());
				resList.add(res);
			}
			//GET_CONSTANT_DROPDOWN_ET
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnMndInstallments> pm = query.from(TtrnMndInstallments.class);
			query.multiselect(cb.count(pm.get("transactionNo")).alias("TRANSACTION_NO"), cb.count(pm.get("contractNo")).alias("CONTRACT_NO"));
			//end
			Subquery<Long> endNo = query.subquery(Long.class); 
			Root<TtrnMndInstallments> e = endNo.from(TtrnMndInstallments.class);
			endNo.select(cb.max(e.get("endorsementNo")));
			Predicate c1 = cb.equal( e.get("proposalNo"), pm.get("proposalNo"));
			Predicate c2 = cb.equal( e.get("contractNo"), pm.get("contractNo"));
			endNo.where(c1,c2);
			
			Predicate n1 = cb.equal(pm.get("contractNo"), contractNo);
			Predicate n3 = cb.equal(pm.get("endorsementNo"),endNo);
			query.where(n1,n3);
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list1 = res1.getResultList();
			
			if (list1.size() > 0) {
				for (int i = 0; i < list1.size(); i++) {
					Tuple tempMap = list1.get(i);
					transNo = tempMap.get("TRANSACTION_NO") == null ? "" : tempMap.get("TRANSACTION_NO").toString();
					contNo = tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString();
				}
			}
			if (transNo.equalsIgnoreCase(contNo) && contractNo != null && contractNo != "") {
				CommonResDropDown res = new CommonResDropDown();
				res.setCode("GNPI");
				res.setCodeDescription("GNPI");
				resList.add(res);
				}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	@Override
	public GetCommonDropDownRes getCoverDEpartmentDropDown(String branchCode, String pid, String departId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<CommonResDropDown> department = new ArrayList<CommonResDropDown>();
		String query="";
		try{
			if("17".equalsIgnoreCase(departId) || "18".equalsIgnoreCase(departId) ||"19".equalsIgnoreCase(departId) ){
				query="GET_COVER_DEPT_LIST";
				list = queryImpl.selectList(query,new String[]{departId,branchCode,pid});
			}else{
				 list=getDepartmentDropDown(branchCode,pid,"Y","","","","","");
			}
	
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
					Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
					res.setCode(tempMap.get("TMAS_DEPARTMENT_ID")==null?"":tempMap.get("TMAS_DEPARTMENT_ID").toString());
					res.setCodeDescription(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
					department.add(res);
				}
			response.setCommonResponse(department);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}

		return response;
	}
	public 	List<Map<String,Object>> getDepartmentDropDown(String branchCode,String productCode,String status, String contNo, String endt, String proposalNo,String mode, String baseLayer){
		
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<CommonResDropDown> department = new ArrayList<CommonResDropDown>();
		String query="";
		try{

			if (productCode.equalsIgnoreCase("")) {
				query = "common.select.departlist.premium";
				list = queryImpl.selectList(query, new String[] { branchCode, status });
			} else if(!StringUtils.isBlank(baseLayer) ) { 
				query = "common.select.getDepartmentList3";
				list = queryImpl.selectList(query, new String[] { branchCode,productCode,status,baseLayer,baseLayer,proposalNo });
			}else if(StringUtils.isBlank(proposalNo) && StringUtils.isBlank(contNo)){
				 query = "common.select.getDepartmentList";
				 list=queryImpl.selectList(query,new String[]{branchCode,productCode,status});	
			}else if(StringUtils.isBlank(contNo) && !StringUtils.isBlank(proposalNo)){
				query = "common.select.getDepartmentList2";
				list = queryImpl.selectList(query,new String[] { branchCode, productCode, status, proposalNo, baseLayer, baseLayer });
			}else if(!StringUtils.isBlank(contNo) && StringUtils.isBlank(proposalNo)){
				query = "common.select.getDepartmentList1";
				list = queryImpl.selectList(query,new String[] { branchCode,productCode,status,contNo,contNo });
			}else if(!StringUtils.isBlank(contNo)){
				query = "common.select.getDepartmentList1";
				list = queryImpl.selectList(query,new String[] { branchCode,productCode,status,contNo,contNo });
			}
			
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
					Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
					res.setCode(tempMap.get("TMAS_DEPARTMENT_ID")==null?"":tempMap.get("TMAS_DEPARTMENT_ID").toString());
					res.setCodeDescription(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
					department.add(res);
				}
			
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				
		}

		return list;
	}


	@Override
	public GetCommonDropDownRes getProductieModuleDropDown(GetProductModuleDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> productList = new ArrayList<CommonResDropDown>();
		try {
			List<Tuple> list =null;
			if(req.getMode().equals("edit")){
				//GET_PRODUCTION_MODULE_DROPDOWN
					CriteriaBuilder cb = em.getCriteriaBuilder(); 
					CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
					Root<TmasProductMaster> pm = query.from(TmasProductMaster.class);
					query.multiselect(cb.concat("1~", pm.get("tmasProductId")).alias("TMAS_PRODUCT_ID"),pm.get("tmasProductName").alias("TMAS_PRODUCT_NAME")).distinct(true);						
							
					//itemType
					Subquery<String> itemType = query.subquery(String.class); 
					Root<TtrnIeModule> e = itemType.from(TtrnIeModule.class);
					itemType.select(e.get("itemType"));
					//amend
					Subquery<Long> amend = query.subquery(Long.class); 
					Root<TtrnIeModule> pms = amend.from(TtrnIeModule.class);
					amend.select(cb.max(pms.get("amendId")));
					Predicate a1 = cb.equal(e.get("proposalNo"), pms.get("proposalNo"));
					Predicate a2 = cb.equal(e.get("transactionNo"), req.getTransactionNo());
					Predicate a3 = cb.equal(e.get("itemNo"), pms.get("itemNo"));
					Predicate a4 = cb.equal(e.get("itemInclusionExclusion"), pms.get("itemInclusionExclusion"));
					amend.where(a1,a2,a3,a4);
					Predicate c1 = cb.equal( e.get("proposalNo"), req.getProposalNo());
					Predicate c2 = cb.equal( e.get("transactionNo"), req.getTransactionNo());
					Predicate c3 = cb.equal( e.get("itemNo"), "1");
					Predicate c4 = cb.equal( e.get("itemInclusionExclusion"), req.getType());
					Predicate c5 = cb.equal( e.get("amendId"), amend);
					itemType.where(c1,c2,c3,c4,c5);
					
					List<Order> orderList = new ArrayList<Order>();
					orderList.add(cb.asc(pm.get("tmasProductId")));
					
					Predicate n1 = cb.equal(pm.get("tmasStatus"), "1");
					Predicate n2 = cb.equal(pm.get("branchCode"), req.getBranchCode());
					Expression<String> e0 = pm.get("tmasProductId");
					List<String> pidList =new ArrayList<String>(Arrays.asList("1,2,3".split(","))) ;
					Predicate n3 = e0.in(pidList);
					Predicate n4 = e0.in(itemType);
					
					query.where(n1,n2,n3,n4).orderBy(orderList);
					TypedQuery<Tuple> res1 = em.createQuery(query);
					list = res1.getResultList();
				
			}else{
				//common.select.getPRoductListIE
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TmasProductMaster> pm = query.from(TmasProductMaster.class);
				query.multiselect(cb.concat("1~", pm.get("tmasProductId")).alias("TMAS_PRODUCT_ID"),pm.get("tmasProductName").alias("TMAS_PRODUCT_NAME")).distinct(true);						
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("tmasProductId")));
				Predicate n1 = cb.equal(pm.get("tmasStatus"), "1");
				Predicate n2 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				Expression<String> e0 = pm.get("tmasProductId");
				List<String> pidList =new ArrayList<String>(Arrays.asList("1,2,3".split(","))) ;
				Predicate n3 = e0.in(pidList);
				query.where(n1,n2,n3).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			}
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap = list.get(i);
				res.setCode(tempMap.get("TMAS_PRODUCT_ID") == null ? "" : tempMap.get("TMAS_PRODUCT_ID").toString());
				res.setCodeDescription(tempMap.get("TMAS_PRODUCT_NAME") == null ? "": tempMap.get("TMAS_PRODUCT_NAME").toString());
				productList.add(res);
			}
				response.setCommonResponse(productList);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getInwardBusinessTypeDropDown(GetInwardBusinessTypeDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> constantList = new ArrayList<CommonResDropDown>();
		try {
			List<Tuple> list =null;
			if(req.getMode().equals("edit")){
				 //GET_INWARD_BUSINESS_TYPE_DROPDOWN
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<ConstantDetail> pm = query.from(ConstantDetail.class);
				query.multiselect(cb.concat("2~", pm.get("type")).alias("TYPE"),pm.get("detailName").alias("DETAIL_NAME")).distinct(true);						
						
				//itemType
				Subquery<String> itemType = query.subquery(String.class); 
				Root<TtrnIeModule> e = itemType.from(TtrnIeModule.class);
				itemType.select(e.get("itemType"));
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnIeModule> pms = amend.from(TtrnIeModule.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(e.get("proposalNo"), pms.get("proposalNo"));
				Predicate a2 = cb.equal(e.get("transactionNo"), req.getTransactionNo());
				Predicate a3 = cb.equal(e.get("itemNo"), pms.get("itemNo"));
				Predicate a4 = cb.equal(e.get("itemInclusionExclusion"), pms.get("itemInclusionExclusion"));
				amend.where(a1,a2,a3,a4);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("detailName")));
				Predicate c1 = cb.equal( e.get("proposalNo"), req.getProposalNo());
				Predicate c2 = cb.equal( e.get("transactionNo"), req.getTransactionNo());
				Predicate c3 = cb.equal( e.get("itemNo"), "2");
				Predicate c4 = cb.equal( e.get("itemInclusionExclusion"), req.getType());
				Predicate c5 = cb.equal( e.get("amendId"), amend);
				itemType.where(c1,c2,c3,c4,c5);
				
				Predicate n1 = cb.equal(pm.get("status"), "Y");
				Predicate n2 = cb.equal(pm.get("categoryId"), req.getCategoryId());
				Expression<String> e0 = pm.get("type");
				Predicate n3 = e0.in(itemType);
				
				query.where(n1,n2,n3).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
				
			}else{
				//common.select.getConstDetie
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<ConstantDetail> pm = query.from(ConstantDetail.class);
				query.multiselect(cb.concat("2~", pm.get("type")).alias("TYPE"),pm.get("detailName").alias("DETAIL_NAME")).distinct(true);						
			
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("detailName")));
				
				Predicate n1 = cb.equal(pm.get("status"), "Y");
				Predicate n2 = cb.equal(pm.get("categoryId"), req.getCategoryId());
				query.where(n1,n2).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
				
			}
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap =  list.get(i);
				res.setCode(tempMap.get("TYPE") == null ? "" : tempMap.get("TYPE").toString());
				res.setCodeDescription(tempMap.get("DETAIL_NAME") == null ? "": tempMap.get("DETAIL_NAME").toString());
				constantList.add(res);
			}
			response.setCommonResponse(constantList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}

		return response;
	}

	@Override
	public GetCommonDropDownRes getTreatyTypeDropDown(GetTreatyTypeDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> constantList = new ArrayList<CommonResDropDown>();
		try {
			List<Tuple> list =null;
			if(req.getMode().equals("edit")){
				//GET_TREATY_TYPE_DROPDOWN
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<ConstantDetail> pm = query.from(ConstantDetail.class);
				query.multiselect(cb.concat("6~", pm.get("type")).alias("TYPE"),pm.get("detailName").alias("DETAIL_NAME")).distinct(true);						
						
				//itemType
				Subquery<String> itemType = query.subquery(String.class); 
				Root<TtrnIeModule> e = itemType.from(TtrnIeModule.class);
				itemType.select(e.get("itemType"));
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnIeModule> pms = amend.from(TtrnIeModule.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(e.get("proposalNo"), pms.get("proposalNo"));
				Predicate a2 = cb.equal(e.get("transactionNo"), req.getTransactionNo());
				Predicate a3 = cb.equal(e.get("itemNo"), pms.get("itemNo"));
				Predicate a4 = cb.equal(e.get("itemInclusionExclusion"), pms.get("itemInclusionExclusion"));
				amend.where(a1,a2,a3,a4);
				
				Predicate c1 = cb.equal( e.get("proposalNo"), req.getProposalNo());
				Predicate c2 = cb.equal( e.get("transactionNo"), req.getTransactionNo());
				Predicate c3 = cb.equal( e.get("itemNo"), "6");
				Predicate c4 = cb.equal( e.get("itemInclusionExclusion"), req.getType());
				Predicate c5 = cb.equal( e.get("amendId"), amend);
				itemType.where(c1,c2,c3,c4,c5);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("detailName")));
				
				Predicate n1 = cb.equal(pm.get("status"), "Y");
				Predicate n2 = cb.equal(pm.get("categoryId"), req.getCategoryId());
				Expression<String> e0 = pm.get("type");
				Predicate n3 = e0.in(itemType);
				
				query.where(n1,n2,n3).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
				
			}else{
				//common.select.getConstDetie1
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<ConstantDetail> pm = query.from(ConstantDetail.class);
				query.multiselect(cb.concat("6~", pm.get("type")).alias("TYPE"),pm.get("detailName").alias("DETAIL_NAME")).distinct(true);						
			
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("detailName")));
				
				Predicate n1 = cb.equal(pm.get("status"), "Y");
				Predicate n2 = cb.equal(pm.get("categoryId"), req.getCategoryId());
				query.where(n1,n2).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			}
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap =  list.get(i);
				res.setCode(tempMap.get("TYPE") == null ? "" : tempMap.get("TYPE").toString());
				res.setCodeDescription(tempMap.get("DETAIL_NAME") == null ? "": tempMap.get("DETAIL_NAME").toString());
				constantList.add(res);
			}
			response.setCommonResponse(constantList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}

		return response;
	}

	@Override
	public GetCommonDropDownRes getProfitCentreieModuleDropDown(GetProfitCentreieModuleDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> profitCenterList = new ArrayList<CommonResDropDown>();
		try {
			List<Tuple> list =null;
			if(req.getMode().equals("edit")){
				//GET_PROFIT_CENTREIE_MODULE_DROPDOWN
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TmasPfcMaster> pm = query.from(TmasPfcMaster.class);
				query.multiselect(cb.concat("3~", pm.get("tmasPfcId")).alias("TMAS_PFC_ID"),pm.get("tmasPfcName").alias("TMAS_PFC_NAME")).distinct(true);						
						
				//itemType
				Subquery<String> itemType = query.subquery(String.class); 
				Root<TtrnIeModule> e = itemType.from(TtrnIeModule.class);
				itemType.select(e.get("itemType"));
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnIeModule> pms = amend.from(TtrnIeModule.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(e.get("proposalNo"), pms.get("proposalNo"));
				Predicate a2 = cb.equal(e.get("transactionNo"), req.getTransactionNo());
				Predicate a3 = cb.equal(e.get("itemNo"), pms.get("itemNo"));
				Predicate a4 = cb.equal(e.get("itemInclusionExclusion"), pms.get("itemInclusionExclusion"));
				amend.where(a1,a2,a3,a4);
				
				Predicate c1 = cb.equal( e.get("proposalNo"), req.getProposalNo());
				Predicate c2 = cb.equal( e.get("transactionNo"), req.getTransactionNo());
				Predicate c3 = cb.equal( e.get("itemNo"), "3");
				Predicate c4 = cb.equal( e.get("itemInclusionExclusion"), req.getType());
				Predicate c5 = cb.equal( e.get("amendId"), amend);
				itemType.where(c1,c2,c3,c4,c5);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("tmasPfcName")));
				
				Predicate n1 = cb.equal(pm.get("tmasStatus"), "Y");
				Predicate n2 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				Expression<String> e0 = pm.get("tmasPfcId");
				Predicate n3 = e0.in(itemType);
				
				query.where(n1,n2,n3).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
				
			}else{
				//common.select.getProfitCenterListIE
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TmasPfcMaster> pm = query.from(TmasPfcMaster.class);
				query.multiselect(cb.concat("3~", pm.get("tmasPfcId")).alias("TMAS_PFC_ID"),pm.get("tmasPfcName").alias("TMAS_PFC_NAME")).distinct(true);						
			
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("tmasPfcName")));
				
				Predicate n1 = cb.equal(pm.get("tmasStatus"), "Y");
				Predicate n2 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				query.where(n1,n2).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			}
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap =  list.get(i);
				res.setCode(tempMap.get("TMAS_PFC_ID") == null ? "" : tempMap.get("TMAS_PFC_ID").toString());
				res.setCodeDescription(tempMap.get("TMAS_PFC_NAME") == null ? "": tempMap.get("TMAS_PFC_NAME").toString());
				profitCenterList.add(res);
			}
			response.setCommonResponse(profitCenterList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonDropDownRes getDepartmentieModuleDropDown(GetDepartmentieModuleDropDownReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> departmentList = new ArrayList<CommonResDropDown>();
		try {
			List<Tuple> list =null;
			if(req.getMode().equals("edit")){
				//GET_DEPARTMENTIE_MODULE_DROPDOWN
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TmasDepartmentMaster> pm = query.from(TmasDepartmentMaster.class);
				query.multiselect(cb.concat("4~", pm.get("tmasDepartmentId")).alias("TMAS_DEPARTMENT_ID"),pm.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME")).distinct(true);												
						
				//itemType
				Subquery<String> itemType = query.subquery(String.class); 
				Root<TtrnIeModule> e = itemType.from(TtrnIeModule.class);
				itemType.select(e.get("itemType"));
				//amend
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TtrnIeModule> pms = amend.from(TtrnIeModule.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal(e.get("proposalNo"), pms.get("proposalNo"));
				Predicate a2 = cb.equal(e.get("transactionNo"), req.getTransactionNo());
				Predicate a3 = cb.equal(e.get("itemNo"), pms.get("itemNo"));
				Predicate a4 = cb.equal(e.get("itemInclusionExclusion"), pms.get("itemInclusionExclusion"));
				amend.where(a1,a2,a3,a4);
				Predicate c1 = cb.equal( e.get("proposalNo"), req.getProposalNo());
				Predicate c2 = cb.equal( e.get("transactionNo"), req.getTransactionNo());
				Predicate c3 = cb.equal( e.get("itemNo"), "4");
				Predicate c4 = cb.equal( e.get("itemInclusionExclusion"), req.getType());
				Predicate c5 = cb.equal( e.get("amendId"), amend);
				itemType.where(c1,c2,c3,c4,c5);
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("tmasDepartmentName")));
				
				Predicate n1 = cb.equal(pm.get("tmasStatus"), "Y");
				Predicate n2 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				Expression<String> e0 = pm.get("tmasProductId");
				List<String> pidList =new ArrayList<String>(Arrays.asList("1,2,3".split(","))) ;
				Predicate n3 = e0.in(pidList);
				Expression<String> e1 = pm.get("tmasDepartmentId");
				Predicate n4 = e1.in(itemType);
				
				query.where(n1,n2,n3,n4).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			}else{
				//common.select.getDepartListIE
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<TmasDepartmentMaster> pm = query.from(TmasDepartmentMaster.class);
				query.multiselect(cb.concat("4~", pm.get("tmasDepartmentId")).alias("TMAS_DEPARTMENT_ID"),pm.get("tmasDepartmentName").alias("TMAS_DEPARTMENT_NAME")).distinct(true);						
				
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("tmasDepartmentName")));
				
				Predicate n1 = cb.equal(pm.get("tmasStatus"), "Y");
				Predicate n2 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				Expression<String> e0 = pm.get("tmasProductId");
				List<String> pidList =new ArrayList<String>(Arrays.asList("1,2,3".split(","))) ;
				Predicate n3 = e0.in(pidList);
				query.where(n1,n2,n3).orderBy(orderList);
				TypedQuery<Tuple> res1 = em.createQuery(query);
				list = res1.getResultList();
			}
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap =  list.get(i);
				res.setCode(tempMap.get("TMAS_DEPARTMENT_ID") == null ? "" : tempMap.get("TMAS_DEPARTMENT_ID").toString());
				res.setCodeDescription(tempMap.get("TMAS_DEPARTMENT_NAME") == null ? "": tempMap.get("TMAS_DEPARTMENT_NAME").toString());
				departmentList.add(res);
			}
			response.setCommonResponse(departmentList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}
		return response;

	}

	@Override
	public GetCommonDropDownRes getProposalStatus(String excludeProposalNo) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> proposalList = new ArrayList<CommonResDropDown>();
		try{
			//GET_PROPO_STATUS
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			query.multiselect(pm.get("proposalNo").alias("PROPOSAL_NO"),pm.get("contractStatus").alias("CONTRACT_STATUS"));
			
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pms = amend.from(PositionMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal(pm.get("proposalNo"), pms.get("proposalNo"));
			amend.where(a1);

			Expression<String> e0 = pm.get("proposalNo");
			List<String> propList =new ArrayList<String>(Arrays.asList(excludeProposalNo.split(","))) ;
			Predicate n1 = e0.in(propList);
			Expression<String> e1 = pm.get("productId");
			List<String> pidList =new ArrayList<String>(Arrays.asList("1,2,3".split(","))) ;
			Predicate n2 = e1.in(pidList);
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3);
			
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			 
			if(!(list==null)&&list.size()>0) {
			 for(int i=0 ; i<list.size() ; i++) {
				 CommonResDropDown res = new CommonResDropDown();
				 Tuple tempMap =  list.get(i);
					res.setCode(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
					res.setCodeDescription(tempMap.get("CONTRACT_STATUS")==null?"":tempMap.get("CONTRACT_STATUS").toString());
					proposalList.add(res);
				}
			}
			response.setCommonResponse(proposalList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}
		return response;
	}

	@Override
	public GetCommonValueRes getExcludeProposalNo(String branchCode, String proposalNo, String transactionNo) {
		GetCommonValueRes response = new GetCommonValueRes();
		String result =" ";
		try {
			//GET_EXCLUDE_PROPOSAL
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			Root<TtrnIeModule> pm = query.from(TtrnIeModule.class);
			query.select(pm.get("itemType")); 
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnIeModule> pms = amend.from(TtrnIeModule.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal(pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a2 = cb.equal(pm.get("transactionNo"), pms.get("transactionNo"));
			Predicate a3 = cb.equal(pm.get("branchCode"), pms.get("branchCode"));
			Predicate a4 = cb.equal(pm.get("itemInclusionExclusion"), pms.get("itemInclusionExclusion"));
			Predicate a5 = cb.equal(pms.get("itemNo"), "7");
			amend.where(a1,a2,a3,a4,a5);
			
			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n3 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n4 = cb.equal(pm.get("transactionNo"), transactionNo);
			Predicate n5 = cb.equal(pm.get("itemInclusionExclusion"), "E");
			Predicate n6 = cb.equal(pm.get("itemNo"), "7");
			Predicate n2 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3,n4,n5,n6); 
			TypedQuery<String> res1 = em.createQuery(query);
			List<String> list = res1.getResultList();
		
		int j= list.size()-1;
		for(int i=0;i<list.size();i++){
			String tempMap = list.get(i);
				if(i==j){
					result += tempMap==null?"":tempMap;
				}else{
					result += tempMap==null?"":tempMap+",";
				}
			}
		response.setCommonResponse(result);
		response.setMessage("Success");
		response.setIsError(false);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	@Override
	public GetCommonDropDownRes getConstantDropDownBusinessType(String categoryId,  String deptId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> departmentList = new ArrayList<CommonResDropDown>();
		try {
			List<ConstantDetail> list = null;
			if(!"16".equalsIgnoreCase(deptId)){
				//common.select.getConstDet
				list = constRepo.findDistinctByCategoryIdAndStatusAndCategoryDetailIdNotOrderByType(new BigDecimal(categoryId),"Y",new BigDecimal("7"));
			}else{
				//GET_CONSTANT_DROPDOWN_BUSINESS_TYPE1
				list = constRepo.findDistinctByCategoryIdAndStatusAndCategoryDetailIdNotOrderByType(new BigDecimal(categoryId),"Y",new BigDecimal("2"));
			}
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				ConstantDetail tempMap =  list.get(i);
				res.setCode(tempMap.getType()== null ? "" : tempMap.getType().toString());
				res.setCodeDescription(tempMap.getDetailName() == null ? "": tempMap.getDetailName().toString());
				departmentList.add(res);
			}
			response.setCommonResponse(departmentList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}

		return response;
	}

	@Override
	public GetCommonDropDownRes getConstantDropDownBasis(String categoryId, String deptId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> constantList = new ArrayList<CommonResDropDown>();
		List<ConstantDetail> list = null;
		try {
			//COMMON_SELECT_GETCONSTDET_BASIC
			if(!"16".equalsIgnoreCase(deptId)){
				//GET_CONSTANT_DROPDOWN_BASIS
				list = constRepo.findDistinctByCategoryIdAndStatusAndCategoryDetailIdNotOrderByType(new BigDecimal(categoryId),"Y",new BigDecimal("3"));
			}else {
			list = constRepo.findDistinctByCategoryIdAndStatusOrderByType(new BigDecimal(categoryId),"Y");
			}
			for (int i = 0; i < list.size(); i++) {
				CommonResDropDown res = new CommonResDropDown();
				ConstantDetail tempMap = list.get(i);
				res.setCode(tempMap.getType()== null ? "" : tempMap.getType().toString());
				res.setCodeDescription(tempMap.getDetailName() == null ? "": tempMap.getDetailName().toString());
				constantList.add(res);
			}
			response.setCommonResponse(constantList);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
		}

		return response;
	}

	@Override
	public int DuplicateCountCheck(DuplicateCountCheckReq req) {
		int count=0;
		try {
			//DUPLICATE_COUNT_CHECK
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Long> query = cb.createQuery(Long.class); 
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			query.multiselect(cb.count(pm.get("proposalNo")));						
		
			Predicate n1 = cb.equal(pm.get("uwYear"),req.getUwYear());
			Predicate n2 = cb.equal(pm.get("branchCode"), req.getBranchCode());
			Predicate n3 = cb.equal(pm.get("productId"),req.getPid());
			Predicate n4 = cb.equal(pm.get("rskDummyContract"), req.getType());
			Predicate n5 = cb.equal(pm.get("proposalStatus"),"A");
			Predicate n6 = cb.equal(pm.get("contractStatus"), "A");
		
			if(StringUtils.isNotBlank(req.getProposalNo())) {
				//DUPLICATE_COUNT_CHECK1
				Predicate n7 = cb.notEqual(pm.get("proposalNo"), req.getProposalNo());
				query.where(n1,n2,n3,n4,n5,n6,n7);
				
			}else {
				query.where(n1,n2,n3,n4,n5,n6);
			}
			TypedQuery<Long> res = em.createQuery(query);
			List<Long> list = res.getResultList();
		
			for (int i = 0; i < list.size(); i++) {
				if (!CollectionUtils.isEmpty(list)) {
					count =Integer.valueOf((list.get(0) == null ? "": list.get(0).toString()));
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception @ {" + e + "}");
		}
		return count;
}

	@Override
	public GetCommonDropDownRes getCountryDate(String branchCode, String DEC_FILE) {
		List<CommonResDropDown> result = new ArrayList<CommonResDropDown>();
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		try{
			//GET_CRESTA_DATE
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TmasCrestaMaster> pm = query.from(TmasCrestaMaster.class);
			Root<CountryMaster> cm = query.from(CountryMaster.class);
			query.multiselect(pm.get("crestaId").alias("CRESTA_ID"),pm.get("crestaName").alias("CRESTA_NAME"),cm.get("countryName").alias("COUNTRY_NAME"));
			Predicate n1 = cb.equal(pm.get("status"),"Y");
			Predicate n2 = cb.equal(pm.get("branchCode"),branchCode);
			Predicate n3 = cb.equal(pm.get("territoryCode"),cm.get("countryId"));
			Predicate n4 = cb.equal(cm.get("branchCode"),branchCode);
			Predicate n5 = cb.equal(cm.get("status"),"Y");
			query.where(n1,n2,n3,n4,n5);
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
			XSSFWorkbook workbook = new XSSFWorkbook();
		    XSSFSheet sheet = workbook.createSheet("Master");
		    
		    Map<String, Object[]> data = new TreeMap<String, Object[]>();
		    data.put("1", new Object[] {"Territory Code", "Cresta ID", "Cresta Name"});
		    int j=2;
		    for(int i=0;i<list.size();i++){
		    	Tuple map =list.get(i);
		    	data.put(Integer.toString(j), new Object[]{map.get("COUNTRY_NAME")==null?"":map.get("COUNTRY_NAME"),map.get("CRESTA_ID")==null?"":map.get("CRESTA_ID"),map.get("CRESTA_NAME")==null?"":map.get("CRESTA_NAME")});
		    	j++;
		    }
		    Set<String> keyset = data.keySet();
		    int rownum = 0;
		    for (String key : keyset)
		    {
		        XSSFRow row = sheet.createRow(rownum++);
		        Object [] objArr = data.get(key);
		        int cellnum = 0;
		        for (Object obj : objArr)
		        {
		           XSSFCell cell = row.createCell(cellnum++);
		           if(obj instanceof String)
		                cell.setCellValue((String)obj);
		            else if(obj instanceof Integer)
		                cell.setCellValue((Integer)obj);
		        }
		    }
		    XSSFSheet sheet1 = workbook.createSheet("Import");
		    Map<String, Object[]> data1 = new TreeMap<String, Object[]>();
		    data1.put("1", new Object[] {"Territory Code", "Cresta ID", "Currency ","Accumulation Date","Accumulation of Risk"});
		    Set<String> keyset1 = data1.keySet();
		    int rownum1 = 0;
		    for (String key : keyset1)
		    {
		        XSSFRow row = sheet1.createRow(rownum1++);
		        Object [] objArr = data1.get(key);
		        int cellnum = 0;
		        for (Object obj : objArr)
		        {
		           XSSFCell cell = row.createCell(cellnum++);
		           if(obj instanceof String)
		                cell.setCellValue((String)obj);
		            else if(obj instanceof Integer)
		                cell.setCellValue((Integer)obj);
		        }
		    }
		    try
		    {
		        FileOutputStream out = new FileOutputStream(new File(DEC_FILE));
		        for (int k = 0; k<  5; k++) {
		        	sheet1.autoSizeColumn(k);
                }
		        for (int l = 0; l < 3; l++) {
		        	sheet.autoSizeColumn(l);
                }
		        workbook.write(out);
		        out.close();
		    }
		    catch (Exception e)
		    {
		        e.printStackTrace();
		    }
		
			for(int i=0 ; i<list.size() ; i++) {
				CommonResDropDown res = new CommonResDropDown();
				Tuple tempMap =  list.get(i);
				res.setCode(tempMap.get("CRESTA_ID")==null?"":tempMap.get("CRESTA_ID").toString());
				res.setCodeDescription(tempMap.get("CRESTA_NAME")==null?"":tempMap.get("CRESTA_NAME").toString());
				
				result.add(res);
			}
			response.setCommonResponse(result);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	public double getSumOfInstallmentBooked(String contractNo,String layerNo) {
		double amount=0.0;
		String	amount1 ="";
		try {
			String obj[]=new String[2];
			obj[0]=contractNo;
			obj[1]=layerNo;
			//GET_SUM_INSTALLMENT_BOOKED
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnMndInstallments> pm = query.from(TtrnMndInstallments.class);
			query.multiselect(cb.sum(pm.get("mndPremiumOc")).alias("MND_PREMIUM_OC"));		
			//end
			Subquery<Long> endNo = query.subquery(Long.class); 
			Root<TtrnMndInstallments> e = endNo.from(TtrnMndInstallments.class);
			endNo.select(cb.max(e.get("endorsementNo")));
			Predicate c1 = cb.equal( e.get("layerNo"), pm.get("layerNo"));
			Predicate c2 = cb.equal( e.get("contractNo"), pm.get("contractNo"));
			Predicate c3 = cb.isNotNull(e.get("transactionNo"));
			endNo.where(c1,c2,c3);
			
			Predicate n1 = cb.equal(pm.get("contractNo"),contractNo);
			Predicate n2 = cb.equal(pm.get("layerNo"),layerNo);
			Predicate n3 = cb.isNotNull(pm.get("transactionNo"));
			Predicate n4 = cb.equal(pm.get("endorsementNo"),endNo==null?null:endNo);
			query.where(n1,n2,n3,n4);
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
				if (!CollectionUtils.isEmpty(list)) {
					amount1 =list.get(0).get("MND_PREMIUM_OC")==null?"0.0":list.get(0).get("MND_PREMIUM_OC").toString();
					}
				amount = Double.parseDouble(amount1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amount;
	}
	public int getCountOfInstallmentBooked(String contractNo,String layerNo) {
		int count=0;
		try {
			//GET_COUNT_INSTALLMENT_BOOKED
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnMndInstallments> pm = query.from(TtrnMndInstallments.class);
			query.multiselect(cb.count(pm).alias("COUNT"));		
			//end
			Subquery<Long> endNo = query.subquery(Long.class); 
			Root<TtrnMndInstallments> e = endNo.from(TtrnMndInstallments.class);
			endNo.select(cb.max(e.get("endorsementNo")));
			Predicate c1 = cb.equal( e.get("layerNo"), pm.get("layerNo"));
			Predicate c2 = cb.equal( e.get("contractNo"), pm.get("contractNo"));
			Predicate c3 = cb.isNotNull(e.get("transactionNo"));
			endNo.where(c1,c2,c3);
			
			Predicate n1 = cb.equal(pm.get("contractNo"),contractNo);
			Predicate n2 = cb.equal(pm.get("layerNo"),layerNo);
			Predicate n3 = cb.isNotNull(pm.get("transactionNo"));
			Predicate n4 = cb.equal(pm.get("endorsementNo"),endNo==null?null:endNo);
			query.where(n1,n2,n3,n4);
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
			if (!CollectionUtils.isEmpty(list)) {
				count =Integer.valueOf((list.get(0).get("COUNT") == null ? "": list.get(0).get("COUNT").toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	public boolean getPLCLCountStatus(String contractNo,String layerNo) {
		boolean  status=false;
		int plclCount=0;
		int count1 = 0;
		int count2 = 0;
		try{
			//common.select.getPRCLCount
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<RskPremiumDetails> pm = query.from(RskPremiumDetails.class);
			query.multiselect(cb.count(pm).alias("COUNT1"));		
			
			Predicate n1 = cb.equal(pm.get("contractNo"),contractNo);
			Predicate n2 = cb.equal(pm.get("layerNo"),layerNo);
			query.where(n1,n2);
			TypedQuery<Tuple> res1 = em.createQuery(query);
			List<Tuple> list = res1.getResultList();
			
			if(list!=null) {
				count1 =	list.get(0).get("COUNT1")==null?0:	Integer.valueOf(list.get(0).get("COUNT1").toString());
				}
			
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			Root<TtrnClaimDetails> cd = query1.from(TtrnClaimDetails.class);
			query1.multiselect(cb.count(cd).alias("COUNT2"));		
			
			Predicate m1 = cb.equal(cd.get("contractNo"),contractNo);
			Predicate m2 = cb.equal(cd.get("layerNo"),layerNo);
			query1.where(m1,m2);
			TypedQuery<Tuple> res2 = em.createQuery(query1);
			List<Tuple> list1 = res2.getResultList();
			
			if(list1!=null) {
				count2 =	list1.get(0).get("COUNT2")==null?0:	Integer.valueOf(list1.get(0).get("COUNT2").toString());
				}
			plclCount = count1 + count2;
			
			if(plclCount>0)
				status=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return status;
	}
	public String getRiskComMaxAmendId(final String proposalNo) {
		String result="";
		try{
			List<TtrnRiskCommission> list = rcRepo.findTop1ByRskProposalNumberOrderByRskEndorsementNoDesc(proposalNo);
			if(list.size()>0) {
				result= list.get(0).getRskEndorsementNo()==null?"":list.get(0).getRskEndorsementNo().toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}
	public GetContractValRes getContractValidation(ContractReq req) {
		GetContractValRes resp=new GetContractValRes();
		String query="";
		String sumInsured="";
		String surplus="",coverPer="",dedPer="";
		String ContNo=StringUtils.isEmpty(req.getContNo())? "0":req.getContNo();
			List<GetContractValidationRes> resList = new ArrayList<GetContractValidationRes>();
			List<Map<String,Object>> ContractList=new ArrayList<Map<String,Object>>();
			String args[] = null;
			try{
				if(StringUtils.isNotBlank(req.getCedingCo())&&StringUtils.isNotBlank(req.getIncepDate())&&StringUtils.isNotBlank(req.getExpDate())&&StringUtils.isNotBlank(req.getUwYear())&&StringUtils.isNotBlank(req.getOrginalCurrency())&&StringUtils.isNotBlank(req.getDepartId())&&StringUtils.isNotBlank(req.getTreatyType())&&StringUtils.isNotBlank(req.getProfitCenter())){
					if(StringUtils.isNotBlank(req.getTreatyType())&&("4".equalsIgnoreCase(req.getTreatyType()) || "5".equalsIgnoreCase(req.getTreatyType()))){
							sumInsured =StringUtils.isEmpty(req.getFaclimitOrigCur())? "0":req.getFaclimitOrigCur().replaceAll(",", "");
						}
					else if(StringUtils.isNotBlank(req.getTreatyType())&&"3".equalsIgnoreCase(req.getTreatyType())){
							sumInsured =Double.toString(Double.parseDouble(StringUtils.isEmpty(req.getLimitOrigCur())? "0":req.getLimitOrigCur().replaceAll(",", "")));
							surplus = 	Double.toString(Double.parseDouble(StringUtils.isEmpty(req.getTreatyLimitsurplusOC())? "0":req.getTreatyLimitsurplusOC().replaceAll(",", ""))) ;
						}	
					else if(StringUtils.isNotBlank(req.getTreatyType())&&"1".equalsIgnoreCase(req.getTreatyType())){
						sumInsured = StringUtils.isEmpty(req.getLimitOrigCur())? "0":req.getLimitOrigCur().replaceAll(",", "");
					}
					else if(StringUtils.isNotBlank(req.getTreatyType())&&"2".equalsIgnoreCase(req.getTreatyType())){
						surplus = StringUtils.isEmpty(req.getTreatyLimitsurplusOC())? "0":req.getTreatyLimitsurplusOC().replaceAll(",", "");
					}
					
					
				if(StringUtils.isBlank(sumInsured)){
					sumInsured="0";
				}
				if(StringUtils.isBlank(surplus)){
					surplus="0";
				}
				if(StringUtils.isBlank(coverPer)){
					coverPer="0";
				}
				if(StringUtils.isBlank(dedPer)){
					dedPer="0";
				}
				if(!req.getCedingCo().matches("^[0-9]+$")){
					req.setCedingCo("");
				}
				if("1".equalsIgnoreCase(req.getProductId())){
					query="FAC_CONTRACT_LIST";
					 args = new String[10];
						args[0] = req.getCedingCo();
						args[1] = req.getIncepDate();
						args[2] = req.getExpDate();
						args[3] = req.getUwYear();
						args[4] = req.getOrginalCurrency();
						
						args[5] = req.getDepartId();
						args[6] = req.getTreatyType();
						args[7] = sumInsured;
						args[8] = req.getProfitCenter();
						args[9] = req.getBranchCode();
						if(ContNo!="0"){
						query="FAC_CONTRACT_LIST1";
					}
				}
				else if("2".equalsIgnoreCase(req.getProductId())){
					query="PTTY_CONTRACT_LIST";
					args = new String[9];
					args[0] = req.getCedingCo();
					args[1] = req.getIncepDate();
					args[2] = req.getExpDate();
					args[3] = req.getUwYear();
					args[4] = req.getOrginalCurrency();
					args[5] = req.getDepartId();
					args[6] = req.getTreatyType();
					args[7] = req.getProfitCenter();
					args[8] = req.getBranchCode();
					if("1".equalsIgnoreCase(req.getTreatyType()) ||"4".equalsIgnoreCase(req.getTreatyType())||"5".equalsIgnoreCase(req.getTreatyType()) ){
						//query+="  and RP.RSK_LIMIT_OC="+req.getSumInsured();
						query="PTTY_CONTRACT_LIST1";
					}
					else if("2".equalsIgnoreCase(req.getTreatyType())){
						//query+="  and RP.RSK_TREATY_SURP_LIMIT_OC="+req.getSumInsured();
						query="PTTY_CONTRACT_LIST2";
					}
					else if("3".equalsIgnoreCase(req.getTreatyType())){
						//query+="  and RP.RSK_LIMIT_OC="+req.getSumInsured()+" and RP.RSK_TREATY_SURP_LIMIT_OC = "+req.getSurplus();
						query="PTTY_CONTRACT_LIST3";
					}
					if(ContNo!="0"){
						//query+="  and RD.RSK_CONTRACT_NO!="+req.getContno();
						query="PTTY_CONTRACT_LIST4";
					}
				}  if("3".equalsIgnoreCase(req.getProductId())){
					if(!ContNo.equalsIgnoreCase("0")){
						query="NPTTY_CONTRACT_LIST_CON";
						args = new String[11];
						args[0] = req.getCedingCo();
						args[1] = req.getIncepDate();
						args[2] = req.getExpDate();
						args[3] = req.getUwYear();
						args[4] = req.getOrginalCurrency();
						args[5] = req.getDepartId();
						args[6] = req.getTreatyType();
						args[7] = req.getProfitCenter();
						args[8] = req.getLayerNo();
						args[9] = ContNo;
						args[10] = req.getBranchCode();
					}
					else{
						query="NPTTY_CONTRACT_LIST";
						args = new String[10];
						args[0] = req.getCedingCo();
						args[1] = req.getIncepDate();
						args[2] = req.getExpDate();
						args[3] = req.getUwYear();
						args[4] = req.getOrginalCurrency();
						args[5] = req.getDepartId();
						args[6] = req.getTreatyType();
						args[7] = req.getProfitCenter();
						args[8] = req.getLayerNo();
						args[9] = req.getBranchCode();
					}
					
					if("1".equalsIgnoreCase(req.getTreatyType()) ||"2".equalsIgnoreCase(req.getTreatyType())||"3".equalsIgnoreCase(req.getTreatyType())||"4".equalsIgnoreCase(req.getTreatyType())||"5".equalsIgnoreCase(req.getTreatyType()) ){
						args = new String[13];
						args[0] = req.getCedingCo();
						args[1] = req.getIncepDate();
						args[2] = req.getExpDate();
						args[3] = req.getUwYear();
						args[4] = req.getOrginalCurrency();
						args[5] = req.getDepartId();
						args[6] = req.getTreatyType();
						args[7] = req.getProfitCenter();
						args[8] = req.getLayerNo();
						args[9] = ContNo;
						args[10] = req.getBranchCode();
						args[11] = sumInsured;
						args[12] = surplus;
						query="NPTTY_CONTRACT_LIST_CON1";
					}
					else if("5".equalsIgnoreCase(req.getTreatyType())){
						args = new String[14];
						args[0] = req.getCedingCo();
						args[1] = req.getIncepDate();
						args[2] = req.getExpDate();
						args[3] = req.getUwYear();
						args[4] = req.getOrginalCurrency();
						args[5] = req.getDepartId();
						args[6] = req.getTreatyType();
						args[7] = req.getProfitCenter();
						args[8] = req.getLayerNo();
						args[9] = req.getBranchCode();
						args[10] = sumInsured;
						args[11] = surplus;
						args[12] = coverPer;
						args[13] = dedPer;
						
						query="NPTTY_CONTRACT_LIST1";
					}
				}
		
				
				ContractList =queryImpl.selectList(query,args);
			
		
					 for(int i=0 ; i<ContractList.size() ; i++) {
						 GetContractValidationRes res = new GetContractValidationRes();
							Map<String,Object> tempMap = (Map<String,Object>) ContractList.get(i);
							res.setContractNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
							res.setEndoresmentNo(tempMap.get("RSK_ENDORSEMENT_NO")==null?"":tempMap.get("RSK_ENDORSEMENT_NO").toString());
							res.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
							res.setBrokerId(tempMap.get("RSK_BROKERID")==null?"":tempMap.get("RSK_BROKERID").toString());
							res.setUnderWritter(tempMap.get("RSK_UNDERWRITTER")==null?"":tempMap.get("RSK_UNDERWRITTER").toString());
							res.setInsuredName(tempMap.get("RSK_INSURED_NAME")==null?"":tempMap.get("RSK_INSURED_NAME").toString());
							
							resList.add(res);
						}	
					 resp.setCommonResponse(resList);
					 resp.setMessage("Success");
					 resp.setIsError(false);
				}
		 	}catch(Exception e){
					log.error(e);
					e.printStackTrace();
					resp.setMessage("Failed");
					resp.setIsError(true);
				}
			return resp;
	}
	public boolean getMode(final String proposalNo, int instNo, String endtNo,final int mode) {
		boolean flag=false;
		int count=0;
		try {
			if(mode==1) {
				count = mndRepo.countByProposalNoAndInstallmentNoAndEndorsementNo(proposalNo,new BigDecimal(instNo),new BigDecimal(endtNo));
			}else if(mode==2) {
				count =  idRepo.countByProposalNoAndInsurerNoAndEndorsementNo(proposalNo,new BigDecimal(instNo),new BigDecimal(endtNo));
				}else if(mode==3) {
					count = cessRepo.countByProposalNoAndSnoAndAmendId(proposalNo,new BigDecimal(instNo),new BigDecimal(endtNo));
				}
			if (count==0) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public static String GetACC(final double dround) {
		String valu="0";
			DecimalFormat myFormatter = new DecimalFormat("####.####");
			valu = myFormatter.format(dround);
		return valu;
	}
	public String[] getIncObjectArray(String[] srcObj,String[] descObj){
		final String[] tempObj = new String[srcObj.length];
		System.arraycopy(srcObj, 0, tempObj, 0, srcObj.length);
		srcObj=new String[tempObj.length+descObj.length];
		System.arraycopy(tempObj, 0, srcObj, 0, tempObj.length);
		System.arraycopy(descObj, 0, srcObj, tempObj.length, descObj.length);
		return srcObj;
	}
	public synchronized String getSequencePTRT(String type,String productID,String departmentId,String branchCode, String proposalNo, String trDate){ 
		String seqName="";
		try{
			//select Fn_get_SeqNo(?,?,?,?,?,?) from dual
			String query="select Fn_get_SeqNo(?,?,?,?,?,?) SEQNAME from dual";
			List<Map<String, Object>> list  = queryImpl.selectList(query,new String[]{type,productID,departmentId,branchCode,proposalNo,trDate});
			if (!CollectionUtils.isEmpty(list)) {
				seqName = list.get(0).get("SEQNAME") == null ? ""
						: list.get(0).get("SEQNAME").toString();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return seqName;
	}
	public GetCommonDropDownRes getDeptName(String branchCode) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> relist = new ArrayList<CommonResDropDown>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try{
			String query="common.select.deptName";
			String args [] = new String [1];
			args[0] = branchCode;
			list =queryImpl.selectList(query, args);
			
			for(int i=0 ; i<list.size() ; i++) {
			
				 CommonResDropDown res = new CommonResDropDown();
					Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
					res.setCode(tempMap.get("TMAS_DEPARTMENT_ID")==null?"":tempMap.get("TMAS_DEPARTMENT_ID").toString());
					res.setCodeDescription(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
					relist.add(res);
				}
			response.setCommonResponse(relist);
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	
	}

	public GetCommonDropDownRes getYearToListValue(GetYearToListValueReq req) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try {
			if(StringUtils.isNotBlank(req.getInceptionDate())){
				 String format = "dd/MM/yyyy" ;
			     SimpleDateFormat sdf = new SimpleDateFormat(format) ;
			      Date dateAsObj = null;
			      Date dateAsObj1 = null;
				try {
						dateAsObj = sdf.parse(req.getInceptionDate());
						dateAsObj1 = sdf.parse(req.getExpiryDate());
				} catch (ParseException e) {
						e.printStackTrace();
				}
			    Calendar cal = Calendar.getInstance();
			    cal.setTime(dateAsObj);
			    int year =cal.get(Calendar.YEAR);
			    Calendar cal1 = Calendar.getInstance();
			    cal1.setTime(dateAsObj1);
			    int year1 =cal1.get(Calendar.YEAR);
	        
			   for(int j=year;j<=year1;j++){
				   CommonResDropDown res = new CommonResDropDown();
					res.setCode(String.valueOf(j));
					res.setCodeDescription(String.valueOf(j));
					resList.add(res);
				}
		 }
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	public CommonResponse updateBaseLayer(String baseLayer, String proposalNo) {
		CommonResponse response = new CommonResponse();
		try{
			//UPDATE_BASE_LAYER
			PositionMaster list = pmRepo.findByProposalNo(new BigDecimal(proposalNo));
			if(list != null) {
				list.setBaseLayer(baseLayer);
			}
			pmRepo.saveAndFlush(list);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	public GetCommonDropDownRes getPaymentPartnerlist(String branchCode, String cedingId, String brokerId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			if(StringUtils.isNotBlank(cedingId) && StringUtils.isNotBlank(brokerId)) {
			//GET_PAYMENT_PARTNER_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
      		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
      		
      		Root<PersonalInfo> tc = query.from(PersonalInfo.class); 

      		// Select
      		query.multiselect(tc.get("customerId").alias("customerId"),
      				cb.selectCase().when(cb.equal(tc.get("customerType"), "C"), tc.get("companyName")).otherwise(cb.concat(tc.get("firstName"), tc.get("lastName"))).alias("name"))
      				.distinct(true) ;  

      		// MAXAmend ID
      		Subquery<Long> maxAmend = query.subquery(Long.class); 
      		Root<PersonalInfo> pms = maxAmend.from(PersonalInfo.class);
      		maxAmend.select(cb.max(pms.get("amendId")));
      		Predicate a1 = cb.equal(tc.get("branchCode"), pms.get("branchCode"));
      		Predicate a2 = cb.equal(tc.get("customerType"), pms.get("customerType"));
      		Predicate a3 = cb.equal(tc.get("status"), pms.get("status"));
      		Predicate a4 = cb.equal(tc.get("customerId"), pms.get("customerId"));
      		maxAmend.where(a1,a2,a3,a4);
      		
      		//Order By
//			List<Order> orderList = new ArrayList<Order>();
//			orderList.add(cb.asc(cb.selectCase().when(cb.equal(tc.get("customerType"), "C"), tc.get("companyName")).otherwise(cb.concat(tc.get("firstName"), tc.get("lastName")))));
      		
      		// Where
      		Predicate n1 = cb.equal(tc.get("branchCode"), branchCode);
      		Predicate n2 = cb.equal(tc.get("customerType"), "C");
      		Predicate n3 = cb.equal(tc.get("status"), "Y");
      		Predicate n4 = cb.equal(tc.get("customerId"), cedingId);
      		Predicate n5 = cb.equal(tc.get("amendId"), maxAmend);
      		//Order By name (alias name need to check and update)
      		if(!"63".equals(brokerId)){
				//GET_PAYMENT_PARTNER_BR_LIST
      			Predicate n7 = cb.equal(tc.get("customerId"), brokerId);
				Predicate n6 = cb.equal(tc.get("customerType"), "B");
				query.where(n1,n2,n3,n4,n5,n6,n7);
			}else {
      		query.where(n1,n2,n3,n4,n5);
			}
      		
      		// Get Result
      		TypedQuery<Tuple> list = em.createQuery(query);
      		List<Tuple> result = list.getResultList();
			
			//query+="ORDER BY NAME";
      		if(result.size()>0) {
      			for(Tuple data: result) {
      				CommonResDropDown res = new CommonResDropDown();
      				res.setCode(data.get("customerId")==null?"":data.get("customerId").toString());
      				res.setCodeDescription(data.get("name")==null?"": data.get("name").toString());;
      				resList.add(res);
      			}
      			resList.sort(Comparator.comparing(CommonResDropDown :: getCodeDescription));
      		}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Transactional	
	public CommonResponse updateProposalno(String referenceNo, String proposalNo) {
		CommonResponse response = new CommonResponse();
		try{
		//UPDATE_PROPOSAL_SCALE
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			// create update
			CriteriaUpdate<TtrnBonus> update = cb.createCriteriaUpdate(TtrnBonus.class);
			// set the root class
			Root<TtrnBonus> m = update.from(TtrnBonus.class);
			// set update and where clause
			update.set("proposalNo", new BigDecimal(proposalNo));
			Predicate n1 = cb.equal(m.get("referenceNo"), referenceNo);
			update.where(n1 );
			// perform update
			em.createQuery(update).executeUpdate();
			
			//UPDATE_PROPOSAL_REINS
			CriteriaUpdate<TtrnRip> update1 = cb.createCriteriaUpdate(TtrnRip.class);
			// set the root class
			Root<TtrnRip> tr = update1.from(TtrnRip.class);
			// set update and where clause
			update1.set("proposalNo",  new BigDecimal(proposalNo));
			Predicate m1 = cb.equal(tr.get("referenceNo"), referenceNo);
			update1.where(m1);
			// perform update
			em.createQuery(update1).executeUpdate();
			
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	public GetBouquetListRes getBouquetList(String branchCode) {
		GetBouquetListRes response = new GetBouquetListRes();
		List<GetBouquetListRes1> resList = new ArrayList<GetBouquetListRes1>();
		try{
			//GET_BOUQUET_LIST
			List<PositionMaster> list = pmRepo.findDistinctByBranchCodeAndBouquetNoNotNull(branchCode);
			if(list.size()>0) {
				for(PositionMaster data: list) {
					GetBouquetListRes1 res = new GetBouquetListRes1();
					res.setBouquetNo(data.getBouquetNo()==null?"":data.getBouquetNo().toString());
					resList.add(res);
					}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	public String getSysDate()
	{
	String sysDate="";
	try{
	List<Map<String, Object>> list  = queryImpl.selectSingle("SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') SYSDT FROM DUAL",new String[]{});
	if (!CollectionUtils.isEmpty(list)) {
	sysDate = list.get(0).get("SYSDT") == null ? ""
	: list.get(0).get("SYSDT").toString();
	}
	}catch(Exception e){
	e.printStackTrace();
	}

	return sysDate;
	}

	@Override
	public GetBouquetExistingListRes getBouquetExistingList(String branchCode, String bouquetNo, String bouquetYN) {
		GetBouquetExistingListRes response = new GetBouquetExistingListRes();
		List<GetBouquetExistingListRes1> resList = new ArrayList<GetBouquetExistingListRes1>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try{
			if(StringUtils.isNotBlank(bouquetNo) && "Y".equals(bouquetYN)) {
			list= queryImpl.selectList("GET_EXISTING_BOUQUET",new String[]{branchCode,bouquetNo});
			if(list.size()>0) {
				for(Map<String,Object> data: list) {
					GetBouquetExistingListRes1 res = new GetBouquetExistingListRes1();
					res.setInsDate(data.get("INS_DATE")==null?"":data.get("INS_DATE").toString()); 
					res.setExpDate(data.get("EXP_DATE")==null?"":data.get("EXP_DATE").toString()); 
					res.setCompanyName(data.get("COMPANY_NAME")==null?"":data.get("COMPANY_NAME").toString()); 
					res.setUwYear(data.get("UW_YEAR")==null?"":data.get("UW_YEAR").toString()); 
					res.setUwYearTo(data.get("UW_YEAR_TO")==null?"":data.get("UW_YEAR_TO").toString()); 
					res.setTreatytype(data.get("TREATYTYPE")==null?"":data.get("TREATYTYPE").toString()); 
					res.setProductId(data.get("PRODUCT_ID")==null?"":data.get("PRODUCT_ID").toString()); 
					res.setBusinessType(data.get("BUSINESS_TYPE")==null?"":data.get("BUSINESS_TYPE").toString()); 
					res.setProposalNo(data.get("PROPOSAL_NO")==null?"":data.get("PROPOSAL_NO").toString()); 
					res.setTreatytype(data.get("TREATY_TYPE")==null?"":data.get("TREATY_TYPE").toString()); 
					res.setRskTreatyid(data.get("RSK_TREATYID")==null?"":data.get("RSK_TREATYID").toString()); 
					res.setPolicyStatus(data.get("POLICY_STATUS")==null?"":data.get("POLICY_STATUS").toString()); 
					res.setExistingShare(data.get("EXISTING_SHARE")==null?"":data.get("EXISTING_SHARE").toString()); 
					res.setBaseLayer(data.get("BASE_LAYER")==null?"":data.get("BASE_LAYER").toString()); 
					res.setSectionNo(data.get("SECTION_NO")==null?"":data.get("SECTION_NO").toString()); 
					res.setLayerNo(data.get("LAYER_NO")==null?"":data.get("LAYER_NO").toString());
					res.setTmasDepartmentName(data.get("TMAS_DEPARTMENT_NAME")==null?"":data.get("TMAS_DEPARTMENT_NAME").toString()); 
					res.setSubClass(data.get("SUB_CLASS")==null?"":data.get("SUB_CLASS").toString());
					res.setOfferNo(data.get("OFFER_NO")==null?"":data.get("OFFER_NO").toString());
					resList.add(res);
					}
			}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonDropDownRes getStatusDropDown(String branchCode) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			//GET_STATUS_DROP_DOWN
			List<StatusMaster> list = smRepo.findByBranchCodeAndStatus(branchCode,"Y");
			
			if(list.size()>0) {
      			for(StatusMaster data: list) {
      				CommonResDropDown res = new CommonResDropDown();
      				res.setCode(data.getStatusCode()==null?"":data.getStatusCode().toString());
      				res.setCodeDescription(data.getStatusName()==null?"": data.getStatusName().toString());;
      				resList.add(res);
      			}
      		}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonDropDownRes getSubStatusDropDown(String branchCode, String statusCode) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			//GET_SUBSTATUS_DROP_DOWN
			List<SubStatusMaster> list = ssmRepo.findByBranchCodeAndStatusAndStatusCode(branchCode,"Y",statusCode);
			
			if(list.size()>0) {
      			for(SubStatusMaster data: list) {
      				CommonResDropDown res = new CommonResDropDown();
      				res.setCode(data.getSubStatusCode()==null?"":data.getSubStatusCode().toString());
      				res.setCodeDescription(data.getSubStatusName()==null?"": data.getSubStatusName().toString());;
      				resList.add(res);
      			}
      		}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
    @Transactional
	@Override
	public CommonResponse updateBqEditMode(updateBqEditModeReq req) {
		CommonResponse response = new CommonResponse();
		try{
			//POS_MAS_BQ_MODE_UPDT
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			// create update
			CriteriaUpdate<PositionMaster> update = cb.createCriteriaUpdate(PositionMaster.class);
			Root<PositionMaster> m = update.from(PositionMaster.class);
			
			if(!"N".equalsIgnoreCase(req.getVal())){
				update.set("editMode", req.getVal() +"-"+ req.getUpdateProposalNo());
				}
				else{
					update.set("editMode", req.getVal());
				}
			// MAXAmend ID
      		Subquery<Long> maxAmend = update.subquery(Long.class); 
      		Root<PositionMaster> pms = maxAmend.from(PositionMaster.class);
      		maxAmend.select(cb.max(pms.get("amendId")));
      		Predicate a1 = cb.equal(m.get("proposalNo"), pms.get("proposalNo"));
      		maxAmend.where(a1);
			
			Predicate n1 = cb.equal(m.get("bouquetNo"), req.getProposalNo());
			Predicate n2 = cb.equal(m.get("amendId"), maxAmend);
			update.where(n1,n2);
			// perform update
			em.createQuery(update).executeUpdate();
			
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetCommonDropDownRes getPlacedProposalList(GetPlacedProposalListReq bean) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 	
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
      		
      		Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class); 
      		Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class); 
      		
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				if("C".equalsIgnoreCase(bean.getPlacementMode())) {
					//GET_PLACED_PROPOSAL_BOUQUET
				
		      		// Select
		      		query.multiselect(pm.get("bouquetNo").alias("CODE")).distinct(true) ;  
		      	//Order By
					List<Order> orderList = new ArrayList<Order>();
					orderList.add(cb.asc(pm.get("bouquetNo")));

		      		// Where
					Predicate n1 = cb.isNotNull(pm.get("bouquetNo"));
		      		Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
		      		Predicate n4 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
					query.where(n1,n2,n3,n4).orderBy(orderList);
					// Get Result
		      		TypedQuery<Tuple> list = em.createQuery(query);
		      		List<Tuple> result = list.getResultList();
		      		if(result.size()>0) {
		      			for(Tuple data: result) {
		      				CommonResDropDown res = new CommonResDropDown();
		      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
		      				res.setCodeDescription(data.get("CODE")==null?"": data.get("CODE").toString());;
		      				resList.add(res);
		      			}
		      			resList.sort(Comparator.comparing(CommonResDropDown :: getCode));
		      		}
					
				}else {
					//GET_PLACED_PROPOSAL_BOUQUET_SINGLE
					Expression<String> e1 = cb.concat(pm.get("proposalNo"), "(");
					Expression<String> e2 = cb.concat(e1, rd.get("rskTreatyid"));
					Expression<String> e3 = cb.concat(e2, ")");
		      		// Select
		      		query.multiselect(pm.get("proposalNo").alias("CODE"),e3.alias("CODEDESC")).distinct(true) ; 
		      	//Order By
					List<Order> orderList = new ArrayList<Order>();
					orderList.add(cb.asc(pm.get("bouquetNo")));

		      		// Where
					Predicate n1 = cb.isNotNull(pm.get("bouquetNo"));
		      		Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
		      		Predicate n4 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
					query.where(n1,n2,n3,n4).orderBy(orderList);
					// Get Result
		      		TypedQuery<Tuple> list = em.createQuery(query);
		      		List<Tuple> result = list.getResultList();
		      		if(result.size()>0) {
		      			for(Tuple data: result) {
		      				CommonResDropDown res = new CommonResDropDown();
		      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
		      				res.setCodeDescription(data.get("CODEDESC")==null?"": data.get("CODEDESC").toString());;
		      				resList.add(res);
		      			}
		      			resList.sort(Comparator.comparing(CommonResDropDown :: getCode));
		      		}
				}
				
			
			}else {
				if("C".equalsIgnoreCase(bean.getPlacementMode())) {
					//GET_PLACED_PROPOSAL_BASELAYER
		      		query.multiselect(pm.get("baseProposalNo").alias("CODE")).distinct(true) ; 
		      		//Order By
					List<Order> orderList = new ArrayList<Order>();
					orderList.add(cb.asc(pm.get("baseProposalNo")));
					Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
		      		Predicate n4 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
		      		Predicate n5 = cb.equal(pm.get("proposalNo"), bean.getBaseProposalNo());
		      		Predicate n6 = cb.or(n4,n5);
		      		query.where(n2,n3,n6).orderBy(orderList);
		      	// Get Result
		      		TypedQuery<Tuple> list = em.createQuery(query);
		      		List<Tuple> result = list.getResultList();
		      		if(result.size()>0) {
		      			for(Tuple data: result) {
		      				CommonResDropDown res = new CommonResDropDown();
		      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
		      				res.setCodeDescription(data.get("CODE")==null?"": data.get("CODE").toString());;
		      				resList.add(res);
		      			}
		      			resList.sort(Comparator.comparing(CommonResDropDown :: getCode));
		      		}
					
				}else {
					//GET_PLACED_PROPOSAL_BASELAYER_SINGLE
					Expression<String> e1 = cb.concat(pm.get("proposalNo"), "(");
					Expression<String> e2 = cb.concat(e1, rd.get("rskTreatyid"));
					Expression<String> e3 = cb.concat(e2, ")");
		      		query.multiselect(pm.get("proposalNo").alias("CODE"),e3.alias("CODEDESC"),
		      				cb.selectCase().when(cb.isNull(pm.get("baseProposalNo")), pm.get("proposalNo")).otherwise(pm.get("baseProposalNo")).alias("baseLayer"))
		      				.distinct(true) ; 
					//Order By
//					List<Order> orderList = new ArrayList<Order>();
//					orderList.add(cb.asc(pm.get("bouquetNo")));
					Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
		      		Predicate n4 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
		      		Predicate n5 = cb.equal(pm.get("proposalNo"), bean.getBaseProposalNo());
		      		Predicate n6 = cb.or(n4,n5);
		      		query.where(n2,n3,n6);
		      	// Get Result
		      		TypedQuery<Tuple> list = em.createQuery(query);
		      		List<Tuple> result = list.getResultList();
		      		if(result.size()>0) {
		      			for(Tuple data: result) {
		      				CommonResDropDown res = new CommonResDropDown();
		      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
		      				res.setCodeDescription(data.get("CODEDESC")==null?"": data.get("CODEDESC").toString());;
		      				resList.add(res);
		      			}
		      			resList.sort(Comparator.comparing(CommonResDropDown :: getCode));
		      		}
				}
				
			}
			
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetNotPlacedProposalListRes getNotPlacedProposalList(GetPlacedProposalListReq bean) {
		GetNotPlacedProposalListRes response = new GetNotPlacedProposalListRes();
		List<GetNotPlacedProposalListRes1> resList = new ArrayList<GetNotPlacedProposalListRes1>();
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 	
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
      		
      		Root<PositionMaster> pm = query.from(PositionMaster.class); 
      		Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class); 
      		
				if(StringUtils.isNotBlank(bean.getBouquetNo())) {
					if("C".equalsIgnoreCase(bean.getPlacementMode())) {
						//GET_NOTPLACED_PROPOSAL_BOUQUET
						
			      		query.multiselect(pm.get("bouquetNo").alias("CODE")).distinct(true) ;  
						
						Subquery<Long> prop = query.subquery(Long.class); 
			      		Root<TtrnRiPlacement> pms = prop.from(TtrnRiPlacement.class);
			      		prop.select((pms.get("proposalNo")));
			      		Predicate a1 = cb.equal(pms.get("bouquetNo"), pm.get("bouquetNo"));
			      		prop.where(a1);
			      	
						Predicate n1 = cb.isNotNull(pm.get("bouquetNo"));
			      		Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			      		Predicate n4 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
			      		Predicate n5 = cb.equal(pm.get("contractStatus"), "P");
			    		Expression<String> e0 = pm.get("proposalNo");
			      		Predicate n6 = e0.in(prop).not();
			      		
						query.where(n1,n2,n3,n4,n5,n6);
						
			      		TypedQuery<Tuple> list = em.createQuery(query);
			      		List<Tuple> result = list.getResultList();
			      		if(result.size()>0) {
			      			for(Tuple data: result) {
			      				GetNotPlacedProposalListRes1 res = new GetNotPlacedProposalListRes1();
			      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
			      				res.setCodeDescription(data.get("CODE")==null?"": data.get("CODE").toString());;
			      				resList.add(res);
			      			}
			      			resList.sort(Comparator.comparing(GetNotPlacedProposalListRes1 :: getCode));
			      		}
					}else {
						//GET_NOTPLACED_PROPOSAL_BOUQUET_SINGLE
						Expression<String> e1 = cb.concat(pm.get("proposalNo"), "(");
						Expression<String> e2 = cb.concat(e1, rd.get("rskTreatyid"));
						Expression<String> e3 = cb.concat(e2, ")");
			      		
			      		query.multiselect(pm.get("proposalNo").alias("CODE"),e3.alias("CODEDESC")) ; 
			      	
						List<Order> orderList = new ArrayList<Order>();
						orderList.add(cb.asc(pm.get("productId")));
						orderList.add(cb.asc(pm.get("bouquetNo")));
						orderList.add(cb.asc(pm.get("proposalNo")));
	
						Subquery<Long> prop = query.subquery(Long.class); 
			      		Root<TtrnRiPlacement> pms = prop.from(TtrnRiPlacement.class);
			      		prop.select((pms.get("proposalNo")));
			      		Predicate a1 = cb.equal(pms.get("bouquetNo"), pm.get("bouquetNo"));
			      		prop.where(a1);
			      		
						Predicate n1 = cb.isNotNull(pm.get("bouquetNo"));
			      		Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			      		Predicate n4 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
			      		Predicate n5 = cb.equal(pm.get("contractStatus"), "P");
			    		Expression<String> e0 = pm.get("proposalNo");
			      		Predicate n6 = e0.in(prop).not();
						query.where(n1,n2,n3,n4,n5,n6).orderBy(orderList);
					
			      		TypedQuery<Tuple> list = em.createQuery(query);
			      		List<Tuple> result = list.getResultList();
			      		if(result.size()>0) {
			      			for(Tuple data: result) {
			      				GetNotPlacedProposalListRes1 res = new GetNotPlacedProposalListRes1();
			      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
			      				res.setCodeDescription(data.get("CODEDESC")==null?"": data.get("CODEDESC").toString());;
			      				resList.add(res);
			      			}
			      		}
					}
				}else {
					if("C".equalsIgnoreCase(bean.getPlacementMode())) {
						//GET_NOTPLACED_PROPOSAL_BASELAYER
						query.multiselect(pm.get("baseLayer").alias("CODE")).distinct(true) ; 
						
						Subquery<Long> prop = query.subquery(Long.class); 
			      		Root<TtrnRiPlacement> pms = prop.from(TtrnRiPlacement.class);
			      		prop.select((pms.get("proposalNo")));
			      		Predicate a1 = cb.equal(pms.get("baseProposalNo"), pm.get("baseLayer"));
			      		prop.where(a1);
			      		
						Predicate n1 = cb.isNotNull(pm.get("baseLayer"));
						Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			      		Predicate n4 = cb.equal(pm.get("baseLayer"), bean.getBaseProposalNo());
			      		Predicate n5 = cb.equal(pm.get("proposalNo"), bean.getBaseProposalNo());
			      		Predicate n6 = cb.or(n4,n5);
			      		Predicate n7 = cb.equal(pm.get("contractStatus"), "P");
			    		Expression<String> e0 = pm.get("proposalNo");
			      		Predicate n8 = e0.in(prop).not();
			      		
			      		query.where(n1,n2,n3,n6,n7,n8);
			    
			      		TypedQuery<Tuple> list = em.createQuery(query);
			      		List<Tuple> result = list.getResultList();
			      		if(result.size()>0) {
			      			for(Tuple data: result) {
			      				GetNotPlacedProposalListRes1 res = new GetNotPlacedProposalListRes1();
			      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
			      				res.setCodeDescription(data.get("CODE")==null?"": data.get("CODE").toString());;
			      				resList.add(res);
			      			}
			      			resList.sort(Comparator.comparing(GetNotPlacedProposalListRes1 :: getCode));
			      		}
					}else {
						//GET_NOTPLACED_PROPOSAL_BASELAYER_SINGLE
						Expression<String> e1 = cb.concat(pm.get("proposalNo"), "(");
						Expression<String> e2 = cb.concat(e1, rd.get("rskTreatyid"));
						Expression<String> e3 = cb.concat(e2, ")");
			      		
			      		query.multiselect(pm.get("proposalNo").alias("CODE"),e3.alias("CODEDESC"),
			      			cb.selectCase().when(cb.isNull(pm.get("baseLayer")), pm.get("proposalNo")).otherwise(pm.get("baseLayer").as(BigDecimal.class)).alias("baseLayer")) ; 
			      	
						List<Order> orderList = new ArrayList<Order>();
						orderList.add(cb.asc(pm.get("productId")));
						orderList.add(cb.asc(pm.get("baseLayer")));
						orderList.add(cb.asc(pm.get("proposalNo")));
	
						Subquery<Long> prop = query.subquery(Long.class); 
			      		Root<TtrnRiPlacement> pms = prop.from(TtrnRiPlacement.class);
			      		prop.select((pms.get("proposalNo")));
			      		Predicate a1 = cb.equal(pms.get("proposalNo"), pm.get("proposalNo"));
			      		prop.where(a1);
			      		
						
			      		Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			      		Predicate n3 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			      		Predicate n4 = cb.equal(pm.get("baseLayer"), bean.getBaseProposalNo());
			      		Predicate n5 = cb.equal(pm.get("proposalNo"), bean.getBaseProposalNo());
			      		Predicate n6 = cb.or(n4,n5);
			      		Predicate n7 = cb.equal(pm.get("contractStatus"), "P");
			    		Expression<String> e0 = pm.get("proposalNo");
			      		Predicate n8 = e0.in(prop).not();
			      		
			      		query.where(n2,n3,n6,n7,n8);
					
			      		TypedQuery<Tuple> list = em.createQuery(query);
			      		List<Tuple> result = list.getResultList();
			      		if(result.size()>0) {
			      			for(Tuple data: result) {
			      				GetNotPlacedProposalListRes1 res = new GetNotPlacedProposalListRes1();
			      				res.setCode(data.get("CODE")==null?"":data.get("CODE").toString());
			      				res.setCodeDescription(data.get("CODEDESC")==null?"": data.get("CODEDESC").toString());
			      				res.setBaseLayer(data.get("baseLayer")==null?"": data.get("baseLayer").toString());
			      				resList.add(res);
			      			}
			      		}
					}
					}
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}

	public GetBouquetExistingListRes getBaseLayerExistingList(String branchCode, String baseProposalNo) {
		GetBouquetExistingListRes response = new GetBouquetExistingListRes();
		List<GetBouquetExistingListRes1> resList = new ArrayList<GetBouquetExistingListRes1>();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		try{
			list= queryImpl.selectList("GET_EXISTING_BASELAYER",new String[]{branchCode,baseProposalNo});
			if(list.size()>0) {
				for(Map<String,Object> data: list) {
					GetBouquetExistingListRes1 res = new GetBouquetExistingListRes1();
					res.setInsDate(data.get("INS_DATE")==null?"":data.get("INS_DATE").toString());  
					res.setExpDate(data.get("EXP_DATE")==null?"":data.get("EXP_DATE").toString());  
					res.setCompanyName(data.get("COMPANY_NAME")==null?"":data.get("COMPANY_NAME").toString());  
					res.setUwYear(data.get("UW_YEAR")==null?"":data.get("UW_YEAR").toString());  
					res.setUwYearTo(data.get("UW_YEAR_TO")==null?"":data.get("UW_YEAR_TO").toString());  
					res.setTreatytype(data.get("TREATYTYPE")==null?"":data.get("TREATYTYPE").toString());  
					res.setProductId(data.get("PRODUCT_ID")==null?"":data.get("PRODUCT_ID").toString());  
					res.setBusinessType(data.get("BUSINESS_TYPE")==null?"":data.get("BUSINESS_TYPE").toString());  
					res.setProposalNo(data.get("PROPOSAL_NO")==null?"":data.get("PROPOSAL_NO").toString());  
					res.setTreatyType1(data.get("TREATY_TYPE")==null?"":data.get("TREATY_TYPE").toString());  
					res.setRskTreatyid(data.get("RSK_TREATYID")==null?"":data.get("RSK_TREATYID").toString());  
					res.setPolicyStatus(data.get("POLICY_STATUS")==null?"":data.get("POLICY_STATUS").toString());  
					res.setExistingShare(data.get("EXISTING_SHARE")==null?"":data.get("EXISTING_SHARE").toString());  
					res.setBaseLayer(data.get("BASE_LAYER")==null?"":data.get("BASE_LAYER").toString());  
					res.setSectionNo(data.get("SECTION_NO")==null?"":data.get("SECTION_NO").toString());  
					res.setLayerNo(data.get("LAYER_NO")==null?"":data.get("LAYER_NO").toString());  
					res.setTmasDepartmentName(data.get("TMAS_DEPARTMENT_NAME")==null?"":data.get("TMAS_DEPARTMENT_NAME").toString());  
					res.setSubClass(data.get("SUB_CLASS")==null?"":data.get("SUB_CLASS").toString());  
					res.setOfferNo(data.get("OFFER_NO")==null?"":data.get("OFFER_NO").toString());  
					resList.add(res);
				}
		}
		response.setCommonResponse(resList);
		response.setMessage("Success");
		response.setIsError(false);
	}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	public GetBouquetCedentBrokerInfoRes getBouquetCedentBrokerInfo(String bouquetNo) {
		GetBouquetCedentBrokerInfoRes response = new GetBouquetCedentBrokerInfoRes();
		//doubt, output empty here  , sql dev value 
		try {
			//GET_BOUQUET_CEDENT_BROKER
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(pms.get("companyName"));
			Predicate a1 = cb.equal( pm.get("cedingCompanyId"), pms.get("customerId"));
			Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a3 = cb.equal( pms.get("customerType"), "C");
			cedingCompanyName.where(a1,a2,a3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
			
			//select CASE WHEN PI.CUSTOMER_TYPE = 'C' THEN PI.COMPANY_NAME 
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pis1.get("amendId")));
			Predicate c1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			maxAmend1.where(c1);
			
			Predicate d1 = cb.equal( pi1.get("customerType"), "B");
			Predicate d2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate d3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi1.get("amendId"), maxAmend1);
			brokerName.where(d1,d2,d3,d4);

			query.multiselect(pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					pm.get("brokerId").alias("BROKER_ID"),
					cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),
					pm.get("inceptionDate").alias("INCEPTION_DATE"),
					pm.get("expiryDate").alias("EXPIRY_DATE")).distinct(true); 

			//maxamend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> s = amend.from(PositionMaster.class);
			amend.select(cb.max(s.get("amendId")));
			Predicate r1 = cb.equal( pm.get("proposalNo"), s.get("proposalNo"));
			Predicate r2 = cb.equal( s.get("contractStatus"), "P");
			amend.where(r1,r2);

			Predicate n1 = cb.equal(pm.get("bouquetNo"), bouquetNo);
			Predicate n2 = cb.equal(pm.get("contractStatus"), "P");
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3);
			
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();
			if(list!=null && list.size()>0){
				Tuple data = list.get(0);
				GetBouquetCedentBrokerInfoRes1 bean =new GetBouquetCedentBrokerInfoRes1();
				bean.setCedingCo(data.get("CEDING_COMPANY_ID")==null?"":data.get("CEDING_COMPANY_ID").toString());
				bean.setBroker(data.get("BROKER_ID")==null?"":data.get("BROKER_ID").toString());
				bean.setCedingCompanyName(data.get("CEDING_COMPANY_NAME")==null?"":data.get("CEDING_COMPANY_NAME").toString());
				bean.setBrokerName(data.get("BROKER_NAME")==null?"":data.get("BROKER_NAME").toString());
				bean.setUwYear(data.get("UW_YEAR")==null?"":data.get("UW_YEAR").toString());
				bean.setUwYearTo(data.get("UW_YEAR_TO")==null?"":data.get("UW_YEAR_TO").toString());
				bean.setIncepDate(data.get("INCEPTION_DATE")==null?"":data.get("INCEPTION_DATE").toString());
				bean.setExpDate(data.get("EXPIRY_DATE")==null?"":data.get("EXPIRY_DATE").toString());
				response.setCommonResponse(bean);
			}
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	public CommonSaveRes getBouquetCedentBrokercheck(String bouquetNo, String cedingCo, String broker) {
		CommonSaveRes response =new CommonSaveRes();
		boolean result=false;
		//doubt, output empty here  , sql dev value 
		try{
			//GET_BOUQUET_CEDENT_BROKER
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(pms.get("companyName"));
			Predicate a1 = cb.equal( pm.get("cedingCompanyId"), pms.get("customerId"));
			Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a3 = cb.equal( pms.get("customerType"), "C");
			cedingCompanyName.where(a1,a2,a3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
			
			//select CASE WHEN PI.CUSTOMER_TYPE = 'C' THEN PI.COMPANY_NAME 
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pis1.get("amendId")));
			Predicate c1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			maxAmend1.where(c1);
			
			Predicate d1 = cb.equal( pi1.get("customerType"), "B");
			Predicate d2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate d3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi1.get("amendId"), maxAmend1);
			brokerName.where(d1,d2,d3,d4);

			query.multiselect(pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					pm.get("brokerId").alias("BROKER_ID"),
					cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),
					pm.get("inceptionDate").alias("INCEPTION_DATE"),
					pm.get("expiryDate").alias("EXPIRY_DATE")).distinct(true); 

			//maxamend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> s = amend.from(PositionMaster.class);
			amend.select(cb.max(s.get("amendId")));
			Predicate r1 = cb.equal( pm.get("proposalNo"), s.get("proposalNo"));
			Predicate r2 = cb.equal( s.get("contractStatus"), "P");
			amend.where(r1,r2);

			Predicate n1 = cb.equal(pm.get("bouquetNo"), bouquetNo);
			Predicate n2 = cb.equal(pm.get("contractStatus"), "P");
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3);
			
			TypedQuery<Tuple> result1 = em.createQuery(query);
			List<Tuple> list = result1.getResultList();
			
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Tuple map= list.get(i);
						String res=map.get("CEDING_COMPANY_ID")==null?"":map.get("CEDING_COMPANY_ID").toString();
						String res1=map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString();
						if(!res.equalsIgnoreCase(cedingCo)){
							result=true;
						}else if(!res1.equalsIgnoreCase(broker)){
							result=true;
						}
					}
				}
				response.setResponse(String.valueOf(result));
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}

	public CommonSaveRes gePltDisableStatus(String proposalNo) {
		CommonSaveRes response = new CommonSaveRes();
		String status="N";
		try {
			//GET_PLDISABLE_STATUS
			int count = riPlaceRepo.countByProposalNo(new BigDecimal(proposalNo));
			if(count>0) {
				 status="Y";
			}
			response.setResponse(status);
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	public CommonSaveRes getUWFromTocheck(String bouquetNo, String uwYear, String uwYearTo) {
		CommonSaveRes response = new CommonSaveRes();
		boolean result=false;
		//doubt, output empty here  , sql dev value 
		try{
			//GET_BOUQUET_CEDENT_BROKER
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(pms.get("companyName"));
			Predicate a1 = cb.equal( pm.get("cedingCompanyId"), pms.get("customerId"));
			Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a3 = cb.equal( pms.get("customerType"), "C");
			cedingCompanyName.where(a1,a2,a3);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
			
			//select CASE WHEN PI.CUSTOMER_TYPE = 'C' THEN PI.COMPANY_NAME 
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pis1.get("amendId")));
			Predicate c1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			maxAmend1.where(c1);
			
			Predicate d1 = cb.equal( pi1.get("customerType"), "B");
			Predicate d2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate d3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi1.get("amendId"), maxAmend1);
			brokerName.where(d1,d2,d3,d4);

			query.multiselect(pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
					pm.get("brokerId").alias("BROKER_ID"),
					cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					brokerName.alias("BROKER_NAME"),
					pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),
					pm.get("inceptionDate").alias("INCEPTION_DATE"),
					pm.get("expiryDate").alias("EXPIRY_DATE")).distinct(true); 

			//maxamend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> s = amend.from(PositionMaster.class);
			amend.select(cb.max(s.get("amendId")));
			Predicate r1 = cb.equal( pm.get("proposalNo"), s.get("proposalNo"));
			Predicate r2 = cb.equal( s.get("contractStatus"), "P");
			amend.where(r1,r2);

			Predicate n1 = cb.equal(pm.get("bouquetNo"), bouquetNo);
			Predicate n2 = cb.equal(pm.get("contractStatus"), "P");
			Predicate n3 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2,n3);
			
			TypedQuery<Tuple> result1 = em.createQuery(query);
			List<Tuple> list = result1.getResultList();
			
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						Tuple map=list.get(i);
						String res=map.get("UW_YEAR")==null?"":map.get("UW_YEAR").toString();
						String res1=map.get("UW_YEAR_TO")==null?"":map.get("UW_YEAR_TO").toString();
						if(!res.equalsIgnoreCase(uwYear)){
							result=true;
						}else if(!res1.equalsIgnoreCase(uwYearTo)){
							result=true;
						}
					}
				}
				response.setResponse(String.valueOf(result));
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
	}
	@Transactional
	public GetNewContractInfoRes getNewContractInfo(String branchCode, String proposalNo) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		GetNewContractInfoRes response = new GetNewContractInfoRes();
		List<GetNewContractInfoRes1> resList = new ArrayList<GetNewContractInfoRes1>();
		try{
			list= queryImpl.selectList("NEW_CONTRACT_SUMMARY", new String[]{branchCode,proposalNo});
			if(list != null && list.size()>0) {
				for(Map<String,Object> data: list) {
					GetNewContractInfoRes1 res = new GetNewContractInfoRes1();
					 res.setOfferNo(data.get("OFFER_NO")==null?"":data.get("OFFER_NO").toString()); 
					 res.setBaseProposal(data.get("BASE_PROPOSAL")==null?"":data.get("BASE_PROPOSAL").toString()); 
					 res.setProposalNo(data.get("PROPOSAL_NO")==null?"":data.get("PROPOSAL_NO").toString()); 
					 res.setTreatyName(data.get("TREATY_NAME")==null?"":data.get("TREATY_NAME").toString()); 
					 res.setLayerSection(data.get("LAYER_SECTION")==null?"":data.get("LAYER_SECTION").toString()); 
					 res.setSno(data.get("SNO")==null?"":data.get("SNO").toString()); 
					 res.setReinsurerName(data.get("REINSURER_NAME")==null?"":data.get("REINSURER_NAME").toString()); 
					  res.setBrokerName(data.get("BROKER_NAME")==null?"":data.get("BROKER_NAME").toString()); 
					  res.setCurrency(data.get("CURRENCY")==null?"":data.get("CURRENCY").toString()); 
					  res.setEpi100Oc(data.get("EPI_100_OC")==null?"":data.get("EPI_100_OC").toString()); 
					  res.setEpi100Dc(data.get("EPI_100_DC")==null?"":data.get("EPI_100_DC").toString()); 
					  res.setPlacingStatus(data.get("PLACING_STATUS")==null?"":data.get("PLACING_STATUS").toString()); 
					  res.setShareSigned(data.get("SHARE_SIGNED")==null?"":data.get("SHARE_SIGNED").toString()); 
					  res.setBrokerage(data.get("BROKERAGE")==null?"":data.get("BROKERAGE").toString()); 
					  res.setBrokerageAmt(data.get("BROKERAGE_AMT")==null?"":data.get("BROKERAGE_AMT").toString()); 
					 resList.add(res);
					 }
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
		response.setIsError(false);
	}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
	return response;
	}

	public GetPlacementInfoListRes getPlacementInfoList(String branchCode, String layerProposalNo) {
		GetPlacementInfoListRes  response = new GetPlacementInfoListRes();
		List<Map<String,Object>>list=null;
		List<GetPlacementInfoListRes1> resList = new ArrayList<GetPlacementInfoListRes1>();
		try {
			list= queryImpl.selectList("GET_REINSURER_INFO", new String[]{branchCode,layerProposalNo});
			if(!CollectionUtils.isEmpty(list)) {
				for(int i=0;i<list.size();i++) {
					Map<String,Object>map=list.get(i);
					GetPlacementInfoListRes1 res = new GetPlacementInfoListRes1();
					res.setBaseproposalNos(map.get("BASE_PROPOSAL_NO")==null?"":map.get("BASE_PROPOSAL_NO").toString());
					res.setBouquetNos(map.get("BOUQUET_NO")==null?"":map.get("BOUQUET_NO").toString());
					res.setBrokerages(map.get("BROKERAGE_PER")==null?"":map.get("BROKERAGE_PER").toString());
					res.setBrokerIds(map.get("BROKER_ID")==null?"":map.get("BROKER_ID").toString());
					res.setCurrentStatus(map.get("CURRENT_STATUS")==null?"":map.get("CURRENT_STATUS").toString());
					res.setNewStatus(map.get("NEW_STATUS")==null?"":map.get("NEW_STATUS").toString());
					res.setProposedSL(map.get("SHARE_PROPOSED_SIGNED")==null?"":formattereight(map.get("SHARE_PROPOSED_SIGNED").toString()));
					res.setProposedWL(map.get("SHARE_PROPOSAL_WRITTEN")==null?"":formattereight(map.get("SHARE_PROPOSAL_WRITTEN").toString()));
					res.setReinsurerIds(map.get("REINSURER_ID")==null?"":map.get("REINSURER_ID").toString());
					res.setShareOffered(map.get("SHARE_OFFERED")==null?"":formattereight(map.get("SHARE_OFFERED").toString()));
					res.setSignedLine(map.get("SHARE_SIGNED")==null?"":formattereight(map.get("SHARE_SIGNED").toString()));
					res.setSnos(map.get("SNO")==null?"":map.get("SNO").toString());
					res.setWrittenLine(map.get("SHARE_WRITTEN")==null?"":formattereight(map.get("SHARE_WRITTEN").toString()));
					res.setStatusNo(map.get("STATUS_NO")==null?"":map.get("STATUS_NO").toString());				
					resList.add(res);
					} }
				response.setCommonResponse(resList);
				response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public CommonResponse updateRenewalEditMode(updateSubEditModeReq req) {
		CommonResponse response = new CommonResponse();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String proposal  = "";
		try{
			String query="GET_BASE_PROPOSAL_NO";
			String args[] = new String[1];
			args[0] = req.getProposalNo();
			list=queryImpl.selectList(query, args);
			if (!CollectionUtils.isEmpty(list)) {
				proposal = list.get(0).get("PROPOSAL_NO") == null ? ""
						: list.get(0).get("PROPOSAL_NO").toString();
			}
			
			if(!"0".equalsIgnoreCase(proposal)){
			updateSubEditMode(req);
			updateEditMode(req);
			}
			response.setMessage("Success");
			response.setIsError(false);
	 }	catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	public CommonResponse updateSubEditMode(updateSubEditModeReq req) {
		CommonResponse response = new CommonResponse();
		try{
			String query="UPDATE_SUB_ENDT_STATUS";
			String args[] = new String[2];
			if(!"N".equalsIgnoreCase(req.getStatus())){
			args[0] = req.getStatus() +"-"+ req.getUpdateProposalNo();
			}
			else{
				args[0] = req.getStatus();	
			}
			args[1] = req.getProposalNo();
			queryImpl.updateQuery(query, args);
			response.setMessage("Success");
			response.setIsError(false);
	 }	catch(Exception e){
				log.error(e);
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
		public CommonResponse updateEditMode(updateSubEditModeReq req) {
			CommonResponse response = new CommonResponse();
			try{
				String query="POS_MAS_ED_MODE_UPDT";
				String args[] = new String[2];
				if(!"N".equalsIgnoreCase(req.getStatus())){
					args[0] = req.getStatus() +"-"+ req.getUpdateProposalNo();
					}
					else{
						args[0] = req.getStatus();	
					}
				args[1] = req.getProposalNo();
				queryImpl.updateQuery(query, args);
				response.setMessage("Success");
				response.setIsError(false);
		 }	catch(Exception e){
					log.error(e);
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
		}
		public void getSOATableInsert(String proposalNo, String contractno,String branchCode) {
			Connection con = null;
			CallableStatement cstmt = null;
			try {
				//DELETE FROM TTRN_SOA_DUE WHERE PROPOSAL_NO=? AND CONTRACT_NO=? AND BRANCH_CODE=?
				ttrnSoaDueRepository.deleteByProposalNoAndContractNoAndBranchCode(new BigDecimal(proposalNo),new BigDecimal(contractno),branchCode);	
				
//				con = this.mytemplate.getDataSource().getConnection(); 	
//				cstmt = con.prepareCall("{CALL PRC_SOA_PENDING_DUE(?,?,?,?)}");
//				cstmt.setString(1, branchCode.trim() );	
//				cstmt.setString(2, proposalNo.trim());
//				cstmt.setString(3, contractno.trim());
//				cstmt.registerOutParameter(4,java.sql.Types.VARCHAR );
//				cstmt.setString(4, error);
//				boolean count = cstmt.execute();
				
				//Procedure Call
				StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("PRC_SOA_PENDING_DUE");
				storedProcedure.registerStoredProcedureParameter("pBranchCode", String.class, ParameterMode.IN);
				storedProcedure.registerStoredProcedureParameter("pProposalNo", String.class, ParameterMode.IN);
				storedProcedure.registerStoredProcedureParameter("pContractNo", String.class, ParameterMode.IN);
				
				// Set parameters
				storedProcedure.setParameter("pBranchCode", branchCode);
				storedProcedure.setParameter("pProposalNo", proposalNo);
				storedProcedure.setParameter("pContractNo", contractno);
				// execute SP
				storedProcedure.execute();
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if(cstmt!=null) {
					try {
						cstmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(con!=null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public GetCompanyInfoRes getCompanyInfo(String branchCode) {
			GetCompanyInfoRes response = new GetCompanyInfoRes();
			List<GetCompanyInfoRes1> resList = new ArrayList<GetCompanyInfoRes1>();
			try{
				//GET_COMPANY_INFO
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				Root<CompanyMaster> rd = query.from(CompanyMaster.class);
				
				Subquery<String> code = query.subquery(String.class); 
				Root<TmasBranchMaster> rds = code.from(TmasBranchMaster.class);
				code.select(rds.get("companyCode"));
				Predicate a1 = cb.equal( rds.get("branchCode"), branchCode);
				Predicate a2 = cb.equal( rds.get("status"), "Y");
				code.where(a1,a2);

				query.multiselect(rd.get("companyCode").alias("COMPANY_CODE"),rd.get("companyName").alias("COMPANY_NAME"),
						rd.get("email").alias("EMAIL"),rd.get("address1").alias("ADDRESS1"),
						rd.get("country").alias("COUNTRY"),rd.get("webSite").alias("WEB_SITE"),
						rd.get("mobile").alias("MOBILE"),rd.get("headerLogo").alias("HEADER_LOGO"),
						rd.get("mainLogo").alias("MAIN_LOGO")); 

				Expression<String> e1=rd.get("companyCode");
				Predicate n1 = e1.in(code==null?null:code);
				query.where(n1);

				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> list = result.getResultList();
				if(list.size()>0){
					for(int i=0 ; i<list.size() ; i++) {
						Tuple tempMap =  list.get(i);
						GetCompanyInfoRes1 res = new GetCompanyInfoRes1();
						res.setCompanyCode(tempMap.get("COMPANY_CODE")==null?"":tempMap.get("COMPANY_CODE").toString());
						res.setCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
						res.setCountry(tempMap.get("COUNTRY")==null?"":tempMap.get("COUNTRY").toString());
						res.setEmail(tempMap.get("EMAIL")==null?"":tempMap.get("EMAIL").toString());
						res.setHeaderLogo(tempMap.get("HEADER_LOGO")==null?"":tempMap.get("HEADER_LOGO").toString());
						res.setMainLogo(tempMap.get("MAIN_LOGO")==null?"":tempMap.get("MAIN_LOGO").toString());
						res.setMobile(tempMap.get("MOBILE")==null?"":tempMap.get("MOBILE").toString());
						res.setWebSite(tempMap.get("WEB_SITE")==null?"":tempMap.get("WEB_SITE").toString());						
						resList.add(res);
					}
				}
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
		 }	catch(Exception e){
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;
		}
}
