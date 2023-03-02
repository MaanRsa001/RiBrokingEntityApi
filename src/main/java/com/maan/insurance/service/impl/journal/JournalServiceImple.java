package com.maan.insurance.service.impl.journal;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.FinalJvFormat;
import com.maan.insurance.model.entity.JournelFormat;
import com.maan.insurance.model.entity.SessionMaster;
import com.maan.insurance.model.entity.TmasBranchMaster;
import com.maan.insurance.model.entity.TmasLedgerMaster;
import com.maan.insurance.model.entity.TmasOpenPeriod;
import com.maan.insurance.model.entity.TtrnManualJv;
import com.maan.insurance.model.repository.ConstantDetailRepository;
import com.maan.insurance.model.repository.SessionMasterRepository;
import com.maan.insurance.model.repository.TmasLedgerMasterRepository;
import com.maan.insurance.model.repository.TmasOpenPeriodRepository;
import com.maan.insurance.model.repository.TtrnManualJvRepository;
import com.maan.insurance.model.req.journal.GetEndDateStatusReq;
import com.maan.insurance.model.req.journal.GetLedgerEntryListReq;
import com.maan.insurance.model.req.journal.GetQuaterEndValidationReq;
import com.maan.insurance.model.req.journal.GetSpcCurrencyListReq;
import com.maan.insurance.model.req.journal.GetStartDateStatusReq;
import com.maan.insurance.model.req.journal.InsertInActiveOpenPeriodReq;
import com.maan.insurance.model.req.journal.InsertManualJVReq;
import com.maan.insurance.model.req.journal.InsertRetroProcessReq;
import com.maan.insurance.model.req.journal.LedgerIdReq;
import com.maan.insurance.model.res.journal.GetEndDateListRes;
import com.maan.insurance.model.res.journal.GetEndDateListRes1;
import com.maan.insurance.model.res.journal.GetJournalViewsRes;
import com.maan.insurance.model.res.journal.GetJournalViewsRes1;
import com.maan.insurance.model.res.journal.GetLedgerEntryListRes;
import com.maan.insurance.model.res.journal.GetLedgerEntryListRes1;
import com.maan.insurance.model.res.journal.GetOpenPeriodListRes;
import com.maan.insurance.model.res.journal.GetOpenPeriodListRes1;
import com.maan.insurance.model.res.journal.GetSpcCurrencyListRes;
import com.maan.insurance.model.res.journal.GetSpcCurrencyListRes1;
import com.maan.insurance.model.res.journal.GetStartDateListRes;
import com.maan.insurance.model.res.journal.GetStartDateListRes1;
import com.maan.insurance.model.res.journal.GetUserDetailsRes;
import com.maan.insurance.model.res.journal.GetUserDetailsRes1;
import com.maan.insurance.model.res.journal.GetViewLedgerDetailsRes;
import com.maan.insurance.model.res.journal.GetViewLedgerDetailsRes1;
import com.maan.insurance.model.res.journal.GetjounalListRes;
import com.maan.insurance.model.res.journal.GetjounalListRes1;
import com.maan.insurance.model.res.journal.LedgerdetailListRes;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.journal.JournalService;
import com.maan.insurance.validation.Formatters;

@Service
public class JournalServiceImple implements JournalService{
	private Logger log = LogManager.getLogger(JournalServiceImple.class);

	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;

	@Autowired
	private 	ConstantDetailRepository cdRepo;
	@Autowired
	private TmasOpenPeriodRepository opRepo;
	@Autowired
	private TmasLedgerMasterRepository lmRepo;
	@Autowired
	private SessionMasterRepository smRepo;
	@Autowired
	private TtrnManualJvRepository manuRepo;
	
	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();

	public JournalServiceImple() {
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
	public GetjounalListRes getjounalList() {
		GetjounalListRes response = new GetjounalListRes();
		List<GetjounalListRes1> resList = new ArrayList<GetjounalListRes1>();
		try{
		//GET_JOURNAL_LIST
		List<ConstantDetail> list =	cdRepo.findByCategoryId(new BigDecimal("16"));
		if(list.size()>0) {
			for(int i=0; i<list.size();i++) {
				ConstantDetail journal = list.get(i);
				GetjounalListRes1 res = new GetjounalListRes1();
				res.setCategoryDetailId(String.valueOf(journal.getCategoryDetailId()));	
				res.setDetailName(journal.getDetailName());
				res.setParam1(journal.getStatus());
				res.setPercent(String.valueOf(journal.getPercent()));
				res.setRemarks(journal.getRemarks());
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
	public GetOpenPeriodListRes getOpenPeriodList(String branchCode){
		GetOpenPeriodListRes response = new GetOpenPeriodListRes();
		List<GetOpenPeriodListRes1> resList = new ArrayList<GetOpenPeriodListRes1>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try{
		//OPEN_PERIOD_LIST
		List<TmasOpenPeriod> list = opRepo.findByBranchCodeOrderBySnoDesc(branchCode);
		if(list.size()>0) {
			for(int i=0; i<list.size();i++) {
				TmasOpenPeriod journal = list.get(i);
				GetOpenPeriodListRes1 res = new GetOpenPeriodListRes1();
				res.setSNo(String.valueOf(journal.getSno()));
				res.setOpenPeriodValue(sdf.format(journal.getStartDate()) + "-" + sdf.format(journal.getEndDate()));
				res.setOpenPeriodKey(sdf.format(journal.getStartDate()) + "-" + sdf.format(journal.getEndDate()) + "-" + journal.getStatus() + "-" + String.valueOf(journal.getSno()));
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
	public GetStartDateListRes getStartDateList(String branchCode) {
		GetStartDateListRes response = new GetStartDateListRes();
		List<GetStartDateListRes1> resList = new ArrayList<GetStartDateListRes1>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dbf = new  SimpleDateFormat("dd-MM-yy");
		try{
			//GET_OPEN_PERIOD_STARTDATE
		//	List<TmasOpenPeriod> list = opRepo.findDistinctStartDateByBranchCodeOrderByStartDateDesc(branchCode);
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			
			Root<TmasOpenPeriod> rd = query.from(TmasOpenPeriod.class);

			// Select
			query.select(rd.get("startDate")).distinct(true); 

			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(rd.get("startDate")));

			// Where
			Predicate n1 = cb.equal(rd.get("branchCode"), branchCode);
		
			query.where(n1).orderBy(orderList);

			// Get Result
			TypedQuery<String> result = em.createQuery(query);
			List<String> list = result.getResultList();
			
			if(list.size()>0) {
				for(int i=0; i<list.size();i++) {
					GetStartDateListRes1 res = new GetStartDateListRes1();
					res.setFromDate(sdf.format(list.get(i)));
					res.setStartDate(dbf.format(list.get(i)));
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
	public GetEndDateListRes getEndDateList(String branchCode) {
		GetEndDateListRes response = new GetEndDateListRes();
		List<GetEndDateListRes1> resList = new ArrayList<GetEndDateListRes1>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dbf = new  SimpleDateFormat("dd-MM-yy");
		try{
			//GET_OPEN_PERIOD_ENDDATE
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<String> query = cb.createQuery(String.class); 
			
			Root<TmasOpenPeriod> rd = query.from(TmasOpenPeriod.class);

			// Select
			query.select(rd.get("endDate")).distinct(true); 

			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(rd.get("endDate")));

			// Where
			Predicate n1 = cb.equal(rd.get("branchCode"), branchCode);
		
			query.where(n1).orderBy(orderList);

			// Get Result
			TypedQuery<String> result = em.createQuery(query);
			List<String> list = result.getResultList();
			
			if(list.size()>0) {
				for(int i=0; i<list.size();i++) {
					GetEndDateListRes1 res = new GetEndDateListRes1();
					res.setToDate(sdf.format(list.get(i)));
					res.setEndDate(dbf.format(list.get(i)));
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
	public CommonSaveRes getForExDiffName(String branchCode) {
		CommonSaveRes response = new CommonSaveRes();
		String diffName = "";
		try {
			//GET_FOR_EX_DIFF_NAME  TmasLedgerMaster
			TmasLedgerMaster list =	lmRepo.findByBranchCodeAndStatusAndCoaIdOrderByCoaId(branchCode, "Y", new BigDecimal("169"));
			if(list != null) {
			 diffName =list.getIntegrationName();
			}
			response.setResponse(diffName);
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
	public CommonSaveRes insertInActiveOpenPeriod(InsertInActiveOpenPeriodReq req) {
		CommonSaveRes response = new CommonSaveRes();
		int result=1;
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try {
			//INACTIVE_OPEN_PERIOD_PROC
			try {
				  StoredProcedureQuery integration = null;
				  integration = em.createStoredProcedureQuery("JOURNAL_REPORTS") 
				  .registerStoredProcedureParameter("PvstartDate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("Pvenddate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("Pvbranch", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pvtype", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("Pviftype", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("PVLOGINID", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("lvSeq", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvfirstid", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvsecondid", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvCategoryDetail_Id", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("Lvprocedure", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("vstrQtr", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvReversal", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvFirstNarr", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvremarksnarr", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("LV_LOGINID_CNT", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("V_ERROR", String.class, ParameterMode.OUT)
				  .setParameter("PvstartDate", req.getStartDate())
				  .setParameter("Pvenddate", req.getEndDate())
				  .setParameter("Pvbranch", req.getBranchCode())
				  .setParameter("pvtype", "~"+req.getJournalname()+"~")
				  .setParameter("Pviftype", "F")
				  .setParameter("PVLOGINID", req.getLoginId());
				 
				  integration.execute();
			//	  output=(String) integration.getOutputParameterValue("pvQuoteNo");
				}catch(Exception e) {
					log.info(e);
				} 
			
			//UPDATE_OPEN_PERIOD
			List<TmasOpenPeriod> entity = opRepo.findByStartDateAndEndDateAndBranchCode(sdf.parse(req.getStartDate()),sdf.parse(req.getEndDate()),req.getBranchCode());
		 if(entity.size()>0) {
			 for(TmasOpenPeriod data: entity) {
				 data.setStatus("N");
				 opRepo.save(data);
			 }
		 }
				response.setResponse(String.valueOf(result));
				response.setMessage("Success");
				response.setIsError(false);
			}catch(Exception e){
				result=0;
					e.printStackTrace();
					response.setResponse(String.valueOf(result));
					response.setMessage("Failed");
					response.setIsError(true);
				}
			return response;	
	}

	@Override
	public CommonSaveRes insertActiveOpenPeriod(InsertInActiveOpenPeriodReq req) {
		CommonSaveRes response = new CommonSaveRes();
		int result=1;
		try {
			//ACTIVE_OPEN_PERIOD_PROC
			try {
				  StoredProcedureQuery integration = null;
				  integration = em.createStoredProcedureQuery("JOURNAL_REPORTS") 
				  .registerStoredProcedureParameter("PvstartDate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("Pvenddate", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("Pvbranch", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("pvtype", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("Pviftype", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("PVLOGINID", String.class, ParameterMode.IN)
				  .registerStoredProcedureParameter("lvSeq", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvfirstid", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvsecondid", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvCategoryDetail_Id", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("Lvprocedure", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("vstrQtr", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvReversal", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvFirstNarr", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("lvremarksnarr", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("LV_LOGINID_CNT", String.class, ParameterMode.OUT)
				  .registerStoredProcedureParameter("V_ERROR", String.class, ParameterMode.OUT)
				  .setParameter("PvstartDate", req.getStartDate())
				  .setParameter("Pvenddate", req.getEndDate())
				  .setParameter("Pvbranch", req.getBranchCode())
				  .setParameter("pvtype", "~"+req.getJournalname()+"~")
				  .setParameter("Pviftype", "I")
				  .setParameter("PVLOGINID", req.getLoginId());
				 
				  integration.execute();
			//	  output=(String) integration.getOutputParameterValue("pvQuoteNo");
				}catch(Exception e) {
					log.info(e);
				} 
			response.setResponse(String.valueOf(result));
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
			result=0;
				e.printStackTrace();
				response.setResponse(String.valueOf(result));
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
	public GetSpcCurrencyListRes getSpcCurrencyList(GetSpcCurrencyListReq req) {
		GetSpcCurrencyListRes response = new GetSpcCurrencyListRes();
		List<GetSpcCurrencyListRes1> resList = new ArrayList<GetSpcCurrencyListRes1>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try{
			if(req.getStatus().equalsIgnoreCase("Y")){ 
				//SPE_CURRENCT_LIST 
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				
				// like table name
				Root<JournelFormat> pm = query.from(JournelFormat.class);

				// Select
				query.multiselect(pm.get("uwy").alias("uwy") ,pm.get("spc").alias("spc"),pm.get("currency").alias("currency"),
						pm.get("jvDate").alias("jvDate"),pm.get("narration").alias("narration"),
						pm.get("productId").alias("productId"),pm.get("journalId").alias("journalId"),
						pm.get("reference").alias("reference")).distinct(true);

				//Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("journalId")));
				orderList.add(cb.asc(pm.get("productId")));
				orderList.add(cb.asc(pm.get("uwy")));
				orderList.add(cb.asc(pm.get("spc")));
				orderList.add(cb.asc(pm.get("currency")));
				
				// Where
				Predicate n1 = cb.equal(pm.get("jvId"), req.getJournalID());
				Predicate n2 = cb.equal(pm.get("startDate"), sdf.parse(req.getStartDate()));
				Predicate n3 = cb.equal(pm.get("endDate"),sdf.parse(req.getEndDate()));
				Predicate n4 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				query.where(n1,n2,n3,n4)
				.groupBy(pm.get("ledger"),
						pm.get("uwy") ,pm.get("spc"),
						pm.get("currency"),	pm.get("jvId"),
						pm.get("jvDate"),pm.get("narration"),
						pm.get("productId"),pm.get("journalId"),
						pm.get("reference")).orderBy(orderList);
				
				// Get Result
				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> list = result.getResultList();
				if(list.size()>0)
				{
					for(Tuple data: list) {
					GetSpcCurrencyListRes1 res = new GetSpcCurrencyListRes1();
					res.setCurrency(data.get("currency").toString());
					res.setJournalId(data.get("journalId").toString());
					res.setNarration(data.get("narration").toString());
					res.setProductId(data.get("productId").toString());
					res.setReference(data.get("reference").toString());
					res.setSpc(data.get("spc").toString());
					res.setStartDate(data.get("startDate").toString());
					res.setUwy(data.get("uwy").toString());
					resList.add(res);					
				 }}
			}
			else{ 
				//SPE_CURRENCT_LIST1
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				
				// like table name
				Root<FinalJvFormat> pm = query.from(FinalJvFormat.class);

				// Select
				query.multiselect(pm.get("uwy").alias("uwy") ,pm.get("spc").alias("spc"),pm.get("currency").alias("currency"),
						pm.get("jvDate").alias("jvDate"),pm.get("narration").alias("narration"),
						pm.get("productId").alias("productId"),pm.get("journalId").alias("journalId"),
						pm.get("reference").alias("reference")).distinct(true);

				//Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(pm.get("journalId")));
				orderList.add(cb.asc(pm.get("productId")));
				orderList.add(cb.asc(pm.get("uwy")));
				orderList.add(cb.asc(pm.get("spc")));
				orderList.add(cb.asc(pm.get("currency")));
				
				// Where
				Predicate n1 = cb.equal(pm.get("jvId"), req.getJournalID());
				Predicate n2 = cb.equal(pm.get("startDate"), sdf.parse(req.getStartDate()));
				Predicate n3 = cb.equal(pm.get("endDate"),sdf.parse(req.getEndDate()));
				Predicate n4 = cb.equal(pm.get("branchCode"), req.getBranchCode());
				query.where(n1,n2,n3,n4)
				.groupBy(pm.get("ledger"),
						pm.get("uwy") ,pm.get("spc"),
						pm.get("currency"),	pm.get("jvId"),
						pm.get("jvDate"),pm.get("narration"),
						pm.get("productId"),pm.get("journalId"),
						pm.get("reference")).orderBy(orderList);
				
				// Get Result
				TypedQuery<Tuple> result = em.createQuery(query);
				List<Tuple> list = result.getResultList();
				if(list.size()>0)
				{
					for(Tuple data: list) {
					GetSpcCurrencyListRes1 res = new GetSpcCurrencyListRes1();
					res.setCurrency(data.get("currency").toString());
					res.setJournalId(data.get("journalId").toString());
					res.setNarration(data.get("narration").toString());
					res.setProductId(data.get("productId").toString());
					res.setReference(data.get("reference").toString());
					res.setSpc(data.get("spc").toString());
					res.setStartDate(data.get("startDate").toString());
					res.setUwy(data.get("uwy").toString());
					resList.add(res);					
					
				 }}
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
	public GetJournalViewsRes getJournalViews(GetSpcCurrencyListReq req) {
		GetJournalViewsRes response = new GetJournalViewsRes();
		List<GetJournalViewsRes1> resList = new ArrayList<GetJournalViewsRes1>();
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			for(int i=0; i<getSpcCurrencyList(req).getCommonResponse().size() ; i++) {
				GetSpcCurrencyListRes1	map = getSpcCurrencyList(req).getCommonResponse().get(i);
				String spc=map.getSpc()==null?"":map.getSpc().toString();
				String cur=map.getCurrency()==null?"":map.getCurrency().toString();
				String year=map.getUwy()==null?"":map.getUwy().toString();
				String productId=map.getProductId()==null?"":map.getProductId().toString();
				if(req.getStatus().equalsIgnoreCase("Y")){
					//PIPE_LINE_JOURNALS
					CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
					
					// like table name
					Root<JournelFormat> pm = query.from(JournelFormat.class);

					// Select
					query.multiselect(pm.get("startDate").alias("startDate"),pm.get("reference").alias("reference"),
							pm.get("ledger").alias("ledger"),pm.get("uwy").alias("uwy"),
							pm.get("spc").alias("spc"),pm.get("currency").alias("currency"),
							pm.get("debitoc").alias("debitoc"),pm.get("creditoc").alias("creditoc"),
							pm.get("debitugx").alias("debitugx"),pm.get("creditugx").alias("creditugx"),
							pm.get("narration").alias("narration"),pm.get("productId").alias("productId"),
							pm.get("endDate").alias("endDate")); 

					// Where
					Predicate n1 = cb.equal(pm.get("spc"), spc);
					Predicate n2 = cb.equal(pm.get("currency"), cur);
					Predicate n3 = cb.equal(pm.get("uwy"), year);
					Predicate n4 = cb.equal(pm.get("jvId"), req.getJournalID());
					Predicate n5 = cb.equal(pm.get("startDate"), req.getStartDate());
					Predicate n6 = cb.equal(pm.get("endDate"), req.getEndDate());
					Predicate n7 = cb.equal(pm.get("productId"), productId);
					Predicate n8 = cb.equal(pm.get("branchCode"), req.getBranchCode());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8);
					// Get Result
					TypedQuery<Tuple> result = em.createQuery(query);
					List<Tuple> list = result.getResultList();
					if(list.size()>0) {
						for(Tuple data: list) {
							GetJournalViewsRes1 res = new GetJournalViewsRes1();
							res.setCreditoc(data.get("creditoc").toString());
							res .setCreditugx(data.get("creditugx").toString());
							res.setCurrency(data.get("currency").toString());
							res.setDebitoc(data.get("debitoc").toString());
							res.setDebitugx(data.get("debitugx").toString());
							res.setEndDate(data.get("endDate").toString());
							res.setLedger(data.get("ledger").toString());
							res.setNarration(data.get("narration").toString());
							res.setProductId(data.get("productId").toString());
							res.setReference(data.get("reference").toString());
							res.setSpc(data.get("spc").toString());
							res.setStartDate(data.get("startDate").toString());
							res.setUwy(data.get("uwy").toString());
							resList.add(res);
						}
					}
					// Select
					query.multiselect(
							cb.sum(pm.get("debitoc")).alias("debitoc"), cb.sum(pm.get("creditoc")).alias("creditoc"),
									cb.sum(pm.get("debitugx")).alias("debitugx"), cb.sum(pm.get("creditugx")).alias("creditugx"));
					// Get Result
					TypedQuery<Tuple> result1 = em.createQuery(query);
					List<Tuple> list1 = result1.getResultList();
					if(list1.size()>0) {
						for(Tuple data: list1) {
							GetJournalViewsRes1 res = new GetJournalViewsRes1();
							res.setCreditoc(data.get("creditoc").toString());
							res .setCreditugx(data.get("creditugx").toString());
							res.setDebitoc(data.get("debitoc").toString());
							res.setDebitugx(data.get("debitugx").toString());
							res.setLedger("Total");
							resList.add(res);
						}
					}
											
						
				}
				else{
					//PIPE_LINE_JOURNALS1
					CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
					
					// like table name
					Root<FinalJvFormat> pm = query.from(FinalJvFormat.class);

					// Select
					query.multiselect(pm.get("startDate").alias("startDate"),pm.get("reference").alias("reference"),
							pm.get("ledger").alias("ledger"),pm.get("uwy").alias("uwy"),
							pm.get("spc").alias("spc"),pm.get("currency").alias("currency"),
							pm.get("debitoc").alias("debitoc"),pm.get("creditoc").alias("creditoc"),
							pm.get("debitugx").alias("debitugx"),pm.get("creditugx").alias("creditugx"),
							pm.get("narration").alias("narration"),pm.get("productId").alias("productId"),
							pm.get("endDate").alias("endDate")); 

					// Where
					Predicate n1 = cb.equal(pm.get("spc"), spc);
					Predicate n2 = cb.equal(pm.get("currency"), cur);
					Predicate n3 = cb.equal(pm.get("uwy"), year);
					Predicate n4 = cb.equal(pm.get("jvId"), req.getJournalID());
					Predicate n5 = cb.equal(pm.get("startDate"), req.getStartDate());
					Predicate n6 = cb.equal(pm.get("endDate"), req.getEndDate());
					Predicate n7 = cb.equal(pm.get("productId"), productId);
					Predicate n8 = cb.equal(pm.get("branchCode"), req.getBranchCode());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8);
					// Get Result
					TypedQuery<Tuple> result = em.createQuery(query);
					List<Tuple> list = result.getResultList();
					if(list.size()>0) {
						for(Tuple data: list) {
							GetJournalViewsRes1 res = new GetJournalViewsRes1();
							res.setCreditoc(data.get("creditoc").toString());
							res .setCreditugx(data.get("creditugx").toString());
							res.setCurrency(data.get("currency").toString());
							res.setDebitoc(data.get("debitoc").toString());
							res.setDebitugx(data.get("debitugx").toString());
							res.setEndDate(data.get("endDate").toString());
							res.setLedger(data.get("ledger").toString());
							res.setNarration(data.get("narration").toString());
							res.setProductId(data.get("productId").toString());
							res.setReference(data.get("reference").toString());
							res.setSpc(data.get("spc").toString());
							res.setStartDate(data.get("startDate").toString());
							res.setUwy(data.get("uwy").toString());
							resList.add(res);
						}
					}
					// Select
					query.multiselect(
							cb.sum(pm.get("debitoc")).alias("debitoc"), cb.sum(pm.get("creditoc")).alias("creditoc"),
									cb.sum(pm.get("debitugx")).alias("debitugx"), cb.sum(pm.get("creditugx")).alias("creditugx"));
					// Get Result
					TypedQuery<Tuple> result1 = em.createQuery(query);
					List<Tuple> list1 = result1.getResultList();
					if(list1.size()>0) {
						for(Tuple data: list1) {
							GetJournalViewsRes1 res = new GetJournalViewsRes1();
							res.setCreditoc(data.get("creditoc").toString());
							res .setCreditugx(data.get("creditugx").toString());
							res.setDebitoc(data.get("debitoc").toString());
							res.setDebitugx(data.get("debitugx").toString());
							res.setLedger("Total");
							resList.add(res);
						}
					}
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
	public CommonSaveRes getCountOpenPeriod(String branchCode, String sNo) {
		CommonSaveRes response = new CommonSaveRes();
		boolean result=false;
		try {
	//GET_COUNT_OPEN_PERIOD
		int count=opRepo.countBySnoLessThanAndBranchCodeAndStatus(new BigDecimal(sNo), branchCode, "Y");
		if(count>0)
			result=true;
		response.setResponse(String.valueOf(result));	
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
	public GetUserDetailsRes getUserDetails(String branchCode, List<String>  loginId) {
		GetUserDetailsRes response = new GetUserDetailsRes();
		GetUserDetailsRes1 res = new GetUserDetailsRes1();
				boolean result=false;
				String loginIdList="";
				try {
					//GET_SESSION_USER_DETAILS
				List<SessionMaster>	list =	smRepo.findByBranchCodeAndLogoutDtIsNullAndLoginIdNotIn(branchCode,loginId);
				if(list!=null && list.size()>0){
					for(int i=0;i<list.size();i++){
						SessionMaster map = list.get(i);
						if(i==0){
							loginIdList+=map.getLoginId()==null?"":map.getLoginId().toString();
						}else{
						loginIdList+=map.getLoginId()==null?"":","+map.getLoginId().toString();
						}
					}
					res.setLoginIdList(loginIdList);
					result=true;
					res.setResult(String.valueOf(result));
				}
				response.setCommonResponse(res);;
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
	public CommonSaveRes getQuaterEndValidation(GetQuaterEndValidationReq req) {
		CommonSaveRes response = new CommonSaveRes();
		int count=0;
		String qType,startDate,endDate;
		try {
			//GET_REPORTING_PERIOD
			String query = "GET_REPORTING_PERIOD";
			List<Map<String, Object>> list = queryImpl.selectList(query,new String[]{req.getSampledate(),req.getSampledate(),req.getBranchCode(),req.getSampledate()});
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object>map=(Map<String, Object>) list.get(i);
					qType=map.get("QTYPE")==null?"":map.get("QTYPE").toString();
					startDate=map.get("STARTDATE")==null?"":map.get("STARTDATE").toString();
					endDate=map.get("ENDDATE")==null?"":map.get("ENDDATE").toString();
					query= "get.count.prcl.processid";
					List<Map<String, Object>> list1 = queryImpl.selectList(query,new String[]{startDate,endDate,req.getBranchCode(),qType});
					if (!CollectionUtils.isEmpty(list1)) {
						count= list1.get(0).get("COUNT") == null ? 0
								: Integer.valueOf(list1.get(0).get("COUNT").toString()) ;
					}
					if(count>0)
						break;
				}
			}
			response.setResponse(String.valueOf(count));
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
	public CommonResponse activateInActivateLoginUsers(String branchCode, String loginId, String status) {
		CommonResponse response = new CommonResponse();
		try {
			//UPDATE_USER_STATUS
			String query = "UPDATE_USER_STATUS";
			queryImpl.updateQuery(query, new String[] {query, branchCode,loginId});
		
//			List<MarinLoginModel> list = loginRepo.findByBranchcodeAndLoginidNotIn(branchCode,loginId);
//			if(list.size()>0) {
//				for(MarinLoginModel data: list) {
//					data.setStatus(status);
//					loginRepo.save(data);
//					}
//			}
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
	public CommonSaveRes insertRetroProcess(InsertRetroProcessReq req) {
		CommonSaveRes response = new CommonSaveRes();
		int result=1;
		try {
			//GET_END_DATE_YEAR
			List<Map<String, Object>> list = queryImpl.selectList("GET_END_DATE_YEAR", new String[] {req.getEndDate(),req.getEndDate(),req.getEndDate(),req.getEndDate()});
		
			//GET_RETRO_PROCESSING
			try {
			 StoredProcedureQuery integration = null;
			  integration = em.createStoredProcedureQuery("RETRO_PROCESSING") 
			  .registerStoredProcedureParameter("pvstartdate", String.class, ParameterMode.IN)
			  .registerStoredProcedureParameter("pvenddate", String.class, ParameterMode.IN)
			  .registerStoredProcedureParameter("pvbranch", String.class, ParameterMode.IN)
			  .registerStoredProcedureParameter("pvType", String.class, ParameterMode.IN)
			 
			  .registerStoredProcedureParameter("lvSeq", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvtransno", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvexchange", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvSeq1", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvtransno1", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvCount", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvCount1", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvsoaseq", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvsoaconstant", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvsoaarchseq", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvsoaarchconstant", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvArchSeq", String.class, ParameterMode.OUT)
			  .registerStoredProcedureParameter("lvmainSeq", String.class, ParameterMode.OUT)
			  .setParameter("PvstartDate", list.get(0))
			  .setParameter("Pvenddate", req.getEndDate())
			  .setParameter("Pvbranch", req.getBranchCode())
			  .setParameter("pvtype", req.getType());
			
			  integration.execute();
		//	  output=(String) integration.getOutputParameterValue("pvQuoteNo");
			}catch(Exception e) {
				log.info(e);
			} 
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				result = 0;
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public GetLedgerEntryListRes getLedgerEntryList(GetLedgerEntryListReq req) {
		GetLedgerEntryListRes response = new GetLedgerEntryListRes();
		List<GetLedgerEntryListRes1> finalList = new ArrayList<GetLedgerEntryListRes1>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try{
			//GET_LEDGER_ENTRY_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			// like table name
			Root<TtrnManualJv> pm = query.from(TtrnManualJv.class);
			
			// shortname
			Subquery<String> name = query.subquery(String.class); 
			Root<CurrencyMaster> cm = name.from(CurrencyMaster.class);
			name.select(cm.get("shortName"));
			// MAXAmend CM
			Subquery<Long> amendCm = query.subquery(Long.class); 
			Root<CurrencyMaster> cms = amendCm.from(CurrencyMaster.class);
			amendCm.select(cb.max(cms.get("amendId")));
			Predicate d1 = cb.equal( cm.get("currencyId"), cms.get("currencyId"));
			Predicate d2 = cb.equal( cm.get("branchCode"), cms.get("branchCode"));
			amendCm.where(d1,d2);
			
			Predicate c1 = cb.equal(cm.get("currencyId"), pm.get("currency"));
			Predicate c2 = cb.equal(cm.get("branchCode"), pm.get("branchCode"));
			Predicate c3 = cb.equal(cm.get("amendId"), amendCm);
			name.where(c1,c2,c3);

			// Select
			query.multiselect(pm.get("transactionNo").alias("TRANSACTION_NO"),pm.get("entryDate").alias("ENRTY_DATE"), 
					pm.get("reveralNo").alias("REVERAL_NO"),name.alias("CURRENCY_NAME"),
					pm.get("currency").alias("currency")).distinct(true); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnManualJv> pms = amend.from(TtrnManualJv.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("transactionNo"), pms.get("transactionNo"));
			Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			amend.where(a1,a2);

			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.desc(pm.get("transactionNo")));

			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), req.getBranchCode());
			Predicate n2 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> ledgerInfo = res.getResultList();
		
			if(ledgerInfo!=null && ledgerInfo.size()>0){
				for(int i=0;i<ledgerInfo.size();i++){
					Tuple insMap=ledgerInfo.get(i);  
					GetLedgerEntryListRes1 tempBean=new GetLedgerEntryListRes1();
					tempBean.setTransactionDate((insMap.get("ENRTY_DATE")==null?"":sdf.format(insMap.get("ENRTY_DATE")).toString()));
					tempBean.setCurrency((insMap.get("CURRENCY_NAME")==null?"":insMap.get("CURRENCY_NAME").toString()));
					tempBean.setTranId((insMap.get("TRANSACTION_NO")==null?"":insMap.get("TRANSACTION_NO").toString()));
					tempBean.setReversalNo(insMap.get("REVERAL_NO")==null?"":insMap.get("REVERAL_NO").toString());
					if((StringUtils.isNotBlank(req.getOpstartDate()))&& (StringUtils.isNotBlank(req.getOpendDate()))){
						if(dropDowmImpl.Validatethree(req.getBranchCode(), tempBean.getTransactionDate())==0){
							tempBean.setTransOpenperiodStatus("N");
						}else
						{
							tempBean.setTransOpenperiodStatus("Y");
						}
					}if(StringUtils.isBlank(tempBean.getReversalNo())){
						tempBean.setReversalStatus("Y");
					}else{
						tempBean.setReversalStatus("N");
					}
					finalList.add(tempBean);
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
	@Transactional
	@Override
	public CommonResponse insertManualJV(InsertManualJVReq bean) {
		CommonResponse response = new CommonResponse();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try {
		String transactionNo="";
		String revesalNo="";
		String amendId="";
		String value="";
		if(StringUtils.isBlank(bean.getTranId()) && "new".equals(bean.getMode())){
		transactionNo = dropDowmImpl.getSequencePTRT("ManualJournal" ,"","", bean.getBranchCode(),"",bean.getTransactionDate());
		bean.setTranId(transactionNo);
		}else if("reverse".equals(bean.getMode())){
			revesalNo= dropDowmImpl.getSequencePTRT("ManualJournal" ,"","", bean.getBranchCode(),"",bean.getReversalDate());
		}
	//	sql= "SELECT NVL(MAX(AMEND_ID)+1,0) FROM  TTRN_MANUAL_JV WHERE BRANCH_CODE=? AND TRANSACTION_NO=?";
		amendId =	manuRepo.findTop1ByBranchCodeAndTransactionNoOrderByAmendIdDesc(bean.getBranchCode(),bean.getTranId());
		amendId = amendId==null?"0":amendId;
		if(bean.getLedgerIdReq()!=null && bean.getLedgerIdReq().size()>0){
		for(int i=0;i<bean.getLedgerIdReq().size();i++){
		if(bean.getLedgerIdReq().get(i) != null){
			LedgerIdReq req = bean.getLedgerIdReq().get(i);
		TtrnManualJv entity = new TtrnManualJv();
		entity.setClas(bean.getLedClass());
		entity.setCurrency(bean.getCurrency());
		entity.setExchangeRate(bean.getExchRate());
		entity.setLedger(req.getLedgerId());
		
		if("edit".equals(bean.getMode()) || "new".equals(bean.getMode())){
			entity.setTransactionNo(bean.getTranId());
			entity.setDebitOc(req.getDebitOC().replaceAll(",", ""));
			entity.setCreditOc(req.getCreditOC().replaceAll(",", ""));
			entity.setDebitDc(req.getDebitDC().replaceAll(",", ""));
			entity.setCreditDc(req.getCreditDC().replaceAll(",", ""));
			entity.setEntryDate(sdf.parse(bean.getTransactionDate()));
			entity.setStatus("C");
			entity.setReveralNo(revesalNo);
		}else{
			entity.setTransactionNo(revesalNo);
			entity.setDebitOc(req.getCreditOC().replaceAll(",", ""));
			entity.setCreditOc(req.getDebitOC().replaceAll(",", ""));
			entity.setDebitDc(req.getCreditDC().replaceAll(",", ""));
			entity.setCreditDc(req.getDebitDC().replaceAll(",", ""));
			entity.setEntryDate(sdf.parse(bean.getReversalDate()));
			entity.setStatus("R");
			entity.setReveralNo(bean.getTranId());
		}
		entity.setNarration(bean.getNarration());
		entity.setAmendId(new BigDecimal(amendId));
		entity.setAmendmentDate(sdf.parse(bean.getAmendmentDate()));
		entity.setBranchCode(bean.getBranchCode());
		entity.setLoginId(bean.getLoginId());
		entity.setSysDate(new Date());	
		entity.setSno(new BigDecimal(i+200));
		value=String.valueOf(i+300);
		manuRepo.save(entity);
		}
		}
		}
		TtrnManualJv entity = new TtrnManualJv();
		entity.setClas(bean.getLedClass());
		entity.setCurrency(bean.getCurrency());
		entity.setExchangeRate(bean.getExchRate());
		entity.setLedger("169");
		if("edit".equals(bean.getMode()) || "new".equals(bean.getMode())){
			entity.setTransactionNo(bean.getTranId());
			entity.setDebitOc("");
			entity.setCreditOc("");
			entity.setDebitDc(bean.getExdebitDC().replaceAll(",", ""));
			entity.setCreditDc(bean.getExcreditDC().replaceAll(",", ""));
			entity.setEntryDate(sdf.parse(bean.getTransactionDate()));
			entity.setStatus("C");
			entity.setReveralNo(revesalNo);
		
		}else{
			entity.setTransactionNo(revesalNo);
			entity.setDebitOc("");
			entity.setCreditOc("");
			entity.setDebitDc(bean.getExcreditDC().replaceAll(",", ""));
			entity.setCreditDc(bean.getExdebitDC().replaceAll(",", ""));
			entity.setEntryDate(sdf.parse(bean.getReversalDate()));
			entity.setStatus("R");
			entity.setReveralNo(bean.getTranId());
		}
		entity.setNarration(bean.getNarration());
		entity.setAmendId(new BigDecimal(amendId));
		entity.setAmendmentDate(sdf.parse(bean.getAmendmentDate()));
		entity.setBranchCode(bean.getBranchCode());
		entity.setLoginId(bean.getLoginId());
		entity.setSysDate(new Date());	
		entity.setSno(new BigDecimal(value));
		manuRepo.save(entity);
		//UPADATE_REVERSAL_JV
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaUpdate<TtrnManualJv> query1 = cb.createCriteriaUpdate(TtrnManualJv.class); 
		
		Root<TtrnManualJv> pm = query1.from(TtrnManualJv.class);

		//Update
		query1.set("reveralNo", revesalNo);

		// MAXAmend ID
		Subquery<Long> amend = query1.subquery(Long.class); 
		Root<TtrnManualJv> pms = amend.from(TtrnManualJv.class);
		amend.select(cb.max(pms.get("amendId")));
		Predicate y1 = cb.equal( pm.get("transactionNo"), pms.get("transactionNo"));
		Predicate y2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
		amend.where(y1,y2);

		// Where
		Predicate x1 = cb.equal(pm.get("transactionNo"),"50000717");
		Predicate x2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		Predicate x3 = cb.equal(pm.get("status"), "C");
		Predicate x4 = cb.equal(pm.get("amendId"), amend);
		query1.where(x1,x2,x3,x4);
		
		em.createQuery(query1).executeUpdate();
	
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
	public GetViewLedgerDetailsRes getViewLedgerDetails(String branchCode, String transId, String reversalStatus) {
		GetViewLedgerDetailsRes response = new GetViewLedgerDetailsRes();
		GetViewLedgerDetailsRes1 res = new GetViewLedgerDetailsRes1();
		double totalcreditOC=0.00;
		double totaldebitOC=0.00;
		double totalcreditDC=0.00;
		double totaldebitDC=0.00;
		double exchangediff=0.00;
		try{
			//GET_LEDGER_EDIT_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			
			Root<TtrnManualJv> pm = query1.from(TtrnManualJv.class);

			query1.multiselect(pm.get("transactionNo").alias("TRANSACTION_NO"),pm.get("clas").alias("CLASS"),
					pm.get("currency").alias("CURRENCY"),pm.get("exchangeRate").alias("EXCHANGE_RATE"),
					pm.get("ledger").alias("LEDGER"),pm.get("debitOc").alias("DEBIT_OC"),
					pm.get("creditOc").alias("CREDIT_OC"),pm.get("debitDc").alias("DEBIT_DC"),
					pm.get("creditDc").alias("CREDIT_DC"),pm.get("entryDate").alias("ENRTY_DATE"),
					pm.get("amendmentDate").alias("AMENDMENT_DATE"),pm.get("narration").alias("NARRATION"),
					pm.get("status").alias("STATUS"));

			// MAXAmend ID
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<TtrnManualJv> pms = amend.from(TtrnManualJv.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate y1 = cb.equal( pm.get("transactionNo"), pms.get("transactionNo"));
			Predicate y2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			amend.where(y1,y2);
			
			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("sno")));

			// Where
			Predicate x1 = cb.equal(pm.get("transactionNo"),transId);
			Predicate x2 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate x3 = cb.notEqual(pm.get("ledger"), "169");
			Predicate x4 = cb.equal(pm.get("amendId"), amend);
			query1.where(x1,x2,x3,x4).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res2 = em.createQuery(query1);
			List<Tuple> list = res2.getResultList();
			List<LedgerdetailListRes> beanList = new ArrayList<LedgerdetailListRes>();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Tuple insMap = list.get(i);
					LedgerdetailListRes bean = new LedgerdetailListRes();
					bean.setLedgerId((insMap.get("LEDGER")==null?"":insMap.get("LEDGER").toString()));
					bean.setDebitOC((insMap.get("DEBIT_OC")==null?"":fm.formatter(insMap.get("DEBIT_OC").toString())));
					bean.setCreditOC((insMap.get("CREDIT_OC")==null?"":fm.formatter(insMap.get("CREDIT_OC").toString())));
					bean.setDebitDC((insMap.get("DEBIT_DC")==null?"":fm.formatter(insMap.get("DEBIT_DC").toString())));
					bean.setCreditDC((insMap.get("CREDIT_DC")==null?"":fm.formatter(insMap.get("CREDIT_DC").toString())));
			
					bean.setTransactionDate((insMap.get("ENRTY_DATE")==null?"":insMap.get("ENRTY_DATE").toString()));
					bean.setCurrency((insMap.get("CURRENCY")==null?"":insMap.get("CURRENCY").toString()));
					bean.setExchRate((insMap.get("EXCHANGE_RATE")==null?"":insMap.get("EXCHANGE_RATE").toString()));
					bean.setLedClass((insMap.get("CLASS")==null?"":insMap.get("CLASS").toString()));
					bean.setNarration((insMap.get("NARRATION")==null?"":insMap.get("NARRATION").toString()));
					bean.setAmendmentDate((insMap.get("AMENDMENT_DATE")==null?"":insMap.get("AMENDMENT_DATE").toString()));
					bean.setReversalStatus((insMap.get("STATUS")==null?"":insMap.get("STATUS").toString()));
					totaldebitOC+=Double.parseDouble((insMap.get("DEBIT_OC")==null?"0":insMap.get("DEBIT_OC").toString()));
					totalcreditOC+=Double.parseDouble((insMap.get("CREDIT_OC")==null?"0":insMap.get("CREDIT_OC").toString()));
					totaldebitDC+=Double.parseDouble((insMap.get("DEBIT_DC")==null?"0":insMap.get("DEBIT_DC").toString()));
					totalcreditDC+=Double.parseDouble((insMap.get("CREDIT_DC")==null?"0":insMap.get("CREDIT_DC").toString()));
					exchangediff=totaldebitDC-totalcreditDC;
					beanList.add(bean);
					}
			}
		//	bean.setLedgerdetailList(list);
			if("C".equals(reversalStatus)){
				res.setTotaldebitOC(fm.formatter(Double.toString(totaldebitOC)));
				res.setTotalcreditOC(fm.formatter(Double.toString(totalcreditOC)));
				res.setTotaldebitDC(fm.formatter(Double.toString(totaldebitDC)));
				res.setTotalcreditDC(fm.formatter(Double.toString(totalcreditDC)));
				if(exchangediff<0){
					res.setExdebitDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotaldebitDC(fm.formatter(Double.toString(totaldebitDC+Math.abs(exchangediff))));
				}else if (exchangediff>0){
					res.setExcreditDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotalcreditDC(fm.formatter(Double.toString(totalcreditDC+Math.abs(exchangediff))));
				}
				
			}else{
				res.setTotaldebitOC(fm.formatter(Double.toString(totalcreditOC)));
				res.setTotalcreditOC(fm.formatter(Double.toString(totaldebitOC)));
				res.setTotaldebitDC(fm.formatter(Double.toString(totalcreditDC)));
				res.setTotalcreditDC(fm.formatter(Double.toString(totaldebitDC)));
				if(exchangediff<0){
					res.setExcreditDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotalcreditDC(fm.formatter(Double.toString(totaldebitDC+Math.abs(exchangediff))));
				}else if (exchangediff>0){
					res.setExdebitDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotaldebitDC(fm.formatter(Double.toString(totalcreditDC+Math.abs(exchangediff))));
				}
			}
			res.setLoopcount(Integer.toString(list.size()));
			res.setLedgerdetailList(beanList);
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
	public GetViewLedgerDetailsRes getEditLedgerDetails(String branchCode, String transId, String mode) {
		GetViewLedgerDetailsRes response = new GetViewLedgerDetailsRes();
		GetViewLedgerDetailsRes1 res = new GetViewLedgerDetailsRes1();
		double totalcreditOC=0.00;
		double totaldebitOC=0.00;
		double totalcreditDC=0.00;
		double totaldebitDC=0.00;
		double exchangediff=0.00;
		try{
			//GET_LEDGER_EDIT_LIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			
			Root<TtrnManualJv> pm = query1.from(TtrnManualJv.class);

			query1.multiselect(pm.get("transactionNo").alias("TRANSACTION_NO"),pm.get("clas").alias("CLASS"),
					pm.get("currency").alias("CURRENCY"),pm.get("exchangeRate").alias("EXCHANGE_RATE"),
					pm.get("ledger").alias("LEDGER"),pm.get("debitOc").alias("DEBIT_OC"),
					pm.get("creditOc").alias("CREDIT_OC"),pm.get("debitDc").alias("DEBIT_DC"),
					pm.get("creditDc").alias("CREDIT_DC"),pm.get("entryDate").alias("ENRTY_DATE"),
					pm.get("amendmentDate").alias("AMENDMENT_DATE"),pm.get("narration").alias("NARRATION"),
					pm.get("status").alias("STATUS"));

			// MAXAmend ID
			Subquery<Long> amend = query1.subquery(Long.class); 
			Root<TtrnManualJv> pms = amend.from(TtrnManualJv.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate y1 = cb.equal( pm.get("transactionNo"), pms.get("transactionNo"));
			Predicate y2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			amend.where(y1,y2);
			
			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("sno")));

			// Where
			Predicate x1 = cb.equal(pm.get("transactionNo"),transId);
			Predicate x2 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate x3 = cb.notEqual(pm.get("ledger"), "169");
			Predicate x4 = cb.equal(pm.get("amendId"), amend);
			query1.where(x1,x2,x3,x4).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res2 = em.createQuery(query1);
			List<Tuple> list = res2.getResultList();
			List<LedgerdetailListRes> beanList = new ArrayList<LedgerdetailListRes>();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Tuple insMap = list.get(i);
					LedgerdetailListRes bean = new LedgerdetailListRes();
					bean.setLedgerId((insMap.get("LEDGER")==null?"":insMap.get("LEDGER").toString()));
					bean.setDebitOC((insMap.get("DEBIT_OC")==null?"":fm.formatter(insMap.get("DEBIT_OC").toString())));
					bean.setCreditOC((insMap.get("CREDIT_OC")==null?"":fm.formatter(insMap.get("CREDIT_OC").toString())));
					bean.setDebitDC((insMap.get("DEBIT_DC")==null?"":fm.formatter(insMap.get("DEBIT_DC").toString())));
					bean.setCreditDC((insMap.get("CREDIT_DC")==null?"":fm.formatter(insMap.get("CREDIT_DC").toString())));
			
					bean.setTransactionDate((insMap.get("ENRTY_DATE")==null?"":insMap.get("ENRTY_DATE").toString()));
					bean.setCurrency((insMap.get("CURRENCY")==null?"":insMap.get("CURRENCY").toString()));
					bean.setExchRate((insMap.get("EXCHANGE_RATE")==null?"":insMap.get("EXCHANGE_RATE").toString()));
					bean.setLedClass((insMap.get("CLASS")==null?"":insMap.get("CLASS").toString()));
					bean.setNarration((insMap.get("NARRATION")==null?"":insMap.get("NARRATION").toString()));
					bean.setAmendmentDate((insMap.get("AMENDMENT_DATE")==null?"":insMap.get("AMENDMENT_DATE").toString()));
					bean.setReversalStatus((insMap.get("STATUS")==null?"":insMap.get("STATUS").toString()));
					totaldebitOC+=Double.parseDouble((insMap.get("DEBIT_OC")==null?"0":insMap.get("DEBIT_OC").toString()));
					totalcreditOC+=Double.parseDouble((insMap.get("CREDIT_OC")==null?"0":insMap.get("CREDIT_OC").toString()));
					totaldebitDC+=Double.parseDouble((insMap.get("DEBIT_DC")==null?"0":insMap.get("DEBIT_DC").toString()));
					totalcreditDC+=Double.parseDouble((insMap.get("CREDIT_DC")==null?"0":insMap.get("CREDIT_DC").toString()));
					exchangediff=totaldebitDC-totalcreditDC;
					beanList.add(bean);
				}
			}
		//	bean.setLedgerdetailList(list);
		
			if("edit".equals(mode)){
				res.setTotaldebitOC(fm.formatter(Double.toString(totaldebitOC)));
				res.setTotalcreditOC(fm.formatter(Double.toString(totalcreditOC)));
				res.setTotaldebitDC(fm.formatter(Double.toString(totaldebitDC)));
				res.setTotalcreditDC(fm.formatter(Double.toString(totalcreditDC)));
				if(exchangediff<0){
					res.setExdebitDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotaldebitDC(fm.formatter(Double.toString(totaldebitDC+Math.abs(exchangediff))));
				}else if (exchangediff>0){
					res.setExcreditDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotalcreditDC(fm.formatter(Double.toString(totalcreditDC+Math.abs(exchangediff))));
				}
			
			}else{
				res.setTotaldebitOC(fm.formatter(Double.toString(totalcreditOC)));
				res.setTotalcreditOC(fm.formatter(Double.toString(totaldebitOC)));
				res.setTotaldebitDC(fm.formatter(Double.toString(totalcreditDC)));
				res.setTotalcreditDC(fm.formatter(Double.toString(totaldebitDC))); 
				res.setNarration("Being reversal of journal no "+transId+" dated "+ beanList.get(0).getTransactionDate());
				if(exchangediff<0){
					res.setExcreditDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotalcreditDC(fm.formatter(Double.toString(totaldebitDC+Math.abs(exchangediff))));
				}else if (exchangediff>0){
					res.setExdebitDC(fm.formatter(Double.toString(Math.abs(exchangediff))));
					res.setTotaldebitDC(fm.formatter(Double.toString(totalcreditDC+Math.abs(exchangediff))));
				}
			}
			res.setLoopcount(Integer.toString(list.size()));
		res.setLedgerdetailList(beanList);
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
	public CommonSaveRes getStartDateStatus(GetStartDateStatusReq bean) {
		CommonSaveRes response = new CommonSaveRes();
		String status="";
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try {
			//GET_OP_START_DATE_STATUS
			List<TmasOpenPeriod> list =	opRepo.findByStartDateAndBranchCode(sdf.parse(bean.getStartDate()),bean.getBranchCode());
			if(list!=null && list.size()>0){
				TmasOpenPeriod map=list.get(0);
				status=map.getStatus()==null?"":map.getStatus().toString();
			}
			response.setResponse(status);
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
	public CommonSaveRes getEndDateStatus(GetEndDateStatusReq bean) {
		CommonSaveRes response = new CommonSaveRes();
		String status="";
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try {
			//GET_OP_END_DATE_STATUS
			List<TmasOpenPeriod> list =	opRepo.findByEndDateAndBranchCode(sdf.parse(bean.getEndDate()),bean.getBranchCode());
			if(list!=null && list.size()>0){
				TmasOpenPeriod map=list.get(0);
				status=map.getStatus()==null?"":map.getStatus().toString();
			}
			response.setResponse(status);
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
