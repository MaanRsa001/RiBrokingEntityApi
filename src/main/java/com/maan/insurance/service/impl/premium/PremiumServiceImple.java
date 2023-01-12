package com.maan.insurance.service.impl.premium;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.maan.insurance.jpa.entity.xolpremium.TtrnRetroPrcl;
import com.maan.insurance.jpa.repository.propPremium.PropPremiumCustomRepository;
import com.maan.insurance.jpa.repository.treasury.TtrnAllocatedTransactionRepository;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasOpenPeriod;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.repository.TmasOpenPeriodRepository;
import com.maan.insurance.model.repository.TmasProductMasterRepository;
import com.maan.insurance.model.repository.TtrnRetroPrclRepository;
import com.maan.insurance.model.req.premium.ContractidetifierlistReq;
import com.maan.insurance.model.req.premium.PremiumListReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes1;
import com.maan.insurance.model.res.premium.ContractidetifierlistRes;
import com.maan.insurance.model.res.premium.ContractidetifierlistRes1;
import com.maan.insurance.model.res.premium.PremiumListRes;
import com.maan.insurance.model.res.premium.PremiumListRes1;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.jasper.JasperConfiguration;
import com.maan.insurance.service.premium.PremiumService;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;
@Service
public class PremiumServiceImple implements PremiumService{
	private Logger logger = LogManager.getLogger(PremiumServiceImple.class);
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private ValidationImple vi;
	
	
	@Autowired
	private TmasOpenPeriodRepository openRepo;
	
	@Autowired
	private TtrnAllocatedTransactionRepository atRepo;
	
	@Autowired
	private TtrnRetroPrclRepository prclRepo;
	
	@Autowired
	private TmasProductMasterRepository pmRepo;
	@Autowired
	PropPremiumCustomRepository propPremiumCustomRepository;
	
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private JasperConfiguration config;
	
	private Properties prop = new Properties();
	private Query query1=null;

	  public PremiumServiceImple() {
	        try {
	        	InputStream  inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
	        	if (inputStream != null) {
					prop.load(inputStream);
				}
	        	
	        } catch (Exception e) {
	            logger.info(e);
	        }
	    }
	  private String formatDate(Object input) {
			return new SimpleDateFormat("dd/MM/yyyy").format(input).toString();
		}
	@Override
	public GetOpenPeriodRes getOpenPeriod(String branchCode) {
		GetOpenPeriodRes response = new GetOpenPeriodRes();
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		 List<GetOpenPeriodRes1> resList = new ArrayList<GetOpenPeriodRes1>();
		try {
		//GET_OPEN_PERIOD_DATE
		List<TmasOpenPeriod> list = openRepo.findByBranchCodeAndStatus(branchCode, "Y");
		if(list.size()>0) {
		for(TmasOpenPeriod data: list) {
			GetOpenPeriodRes1 bean = new GetOpenPeriodRes1();
			bean.setOpstartDate(sdf.format(data.getStartDate()));
			bean.setOpendDate(sdf.format(data.getEndDate()));
			resList.add(bean);
		}
		}
		response.setCommonResponse(resList);
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
	public PremiumListRes PremiumList(PremiumListReq bean) {
		PremiumListRes response = new PremiumListRes();
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		List<Tuple>allocists=null;
		//List<Map<String, Object>> allocists=new ArrayList<Map<String, Object>>();
		List<PremiumListRes1> finalList = new ArrayList<PremiumListRes1>();
	
		try{
			if("Main".equalsIgnoreCase(bean.getTableType())){
			if(bean.getType().equalsIgnoreCase("premium")){
					allocists=propPremiumCustomRepository.getPremiumFullList(bean);
			}
			else{
				allocists=propPremiumCustomRepository.getPremiumFullList(bean);
			}
			}else{
				allocists=propPremiumCustomRepository.getPremiumTempList(bean);
			}
		if(!CollectionUtils.isEmpty(allocists)) {
		for(int i=0 ; i<allocists.size() ; i++) {
			Tuple tempMap = allocists.get(i);
			PremiumListRes1 tempBean=new PremiumListRes1();
			tempBean.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
			tempBean.setContractNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
			tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
			tempBean.setBroker(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setLayerno(tempMap.get("RSK_LAYER_NO")==null?"":tempMap.get("RSK_LAYER_NO").toString());
			tempBean.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
			tempBean.setTransactionType(tempMap.get("RSK_PRODUCTID")==null?"":tempMap.get("RSK_PRODUCTID").toString());
			tempBean.setTransDropDownVal(tempMap.get("REVERSE_TRANSACTION_NO")==null?"":tempMap.get("REVERSE_TRANSACTION_NO").toString());
			tempBean.setSectionNo(tempMap.get("SECTION_NO")==null?"":tempMap.get("SECTION_NO").toString());
			tempBean.setRequestNo(tempMap.get("REQUEST_NO")==null?"":tempMap.get("REQUEST_NO").toString());
			if(tempBean.getTransactionType().equalsIgnoreCase("1")){
				tempBean.setTransactionTypeName("Facultative");
			}
			else if(tempBean.getTransactionType().equalsIgnoreCase("2")){
				tempBean.setTransactionTypeName("Proportional Treaty");
			}
			else if(tempBean.getTransactionType().equalsIgnoreCase("3")){
				tempBean.setTransactionTypeName("Non-Proportional Treaty");
			}
			else if(tempBean.getTransactionType().equalsIgnoreCase("5")){
				tempBean.setTransactionTypeName("Retro - Xol");
			}
			tempBean.setInsDate(tempMap.get("INS_DATE")==null?"":formatDate(tempMap.get("INS_DATE")));
			tempBean.setExpDate(tempMap.get("EXP_DATE")==null?"":formatDate(tempMap.get("EXP_DATE")));
			
			tempBean.setInceptionDate(tempMap.get("INS_DATE")==null?"":formatDate(tempMap.get("INS_DATE")));
			
			tempBean.setTransactionDate(tempMap.get("TRANSACTION_DATE")==null?"":formatDate(tempMap.get("TRANSACTION_DATE")));
			int count= dropDowmImpl.Validatethree(bean.getBranchCode(), tempBean.getTransactionDate());
			if("Main".equalsIgnoreCase(bean.getTableType())){
					if((StringUtils.isNotBlank(bean.getOpstartDate()))&& (StringUtils.isNotBlank(bean.getOpendDate()))){
						if(count ==0){
							tempBean.setTransOpenperiodStatus("N");
						}else
						{
							tempBean.setTransOpenperiodStatus("Y");
						}
						}
					
					int cnt = atRepo.countByContractNoAndTransactionNoAndLayerNoAndTypeAndStatus(tempBean.getContractNo(),new BigDecimal(tempBean.getTransactionNo()),tempBean.getLayerno(),"P","Y");
					tempBean.setAllocatedYN(cnt==0?"Y":"N");
						
						//ALLOCATION_STATUS_COMPARITION
						int allocationstatus= atRepo.countByTransactionNoAndStatus(new BigDecimal(tempBean.getTransactionNo()),"Y");
						
						//RETRO_PRCL_STATUS_COMPARITION
						int retroPrclStatus= prclRepo.countByTransactionNo(new Long(tempBean.getTransactionNo()));
						
						int retroPrclStatus1=0;
						if(retroPrclStatus!=0){
						//RETRO_PRCL_STATUS_COMPARITION1
			         
			      		CriteriaQuery<Integer> query1 = cb.createQuery(Integer.class); 
			      		
			      		// like table name
			      		Root<TtrnRetroPrcl> pm = query1.from(TtrnRetroPrcl.class);

			      		// Select
			      		query1.multiselect(cb.count(pm)); 

			      		// MAXAmend ID
			      		Subquery<Long> maxAmend = query1.subquery(Long.class); 
			      		Root<TtrnRetroPrcl> pms = maxAmend.from(TtrnRetroPrcl.class);
			      		maxAmend.select(cb.max(pms.get("amendId")));
			      		Predicate a1 = cb.equal( pm.get("transactionNo"), pms.get("transactionNo"));
			      		maxAmend.where(a1);
			      		
			      		// Where
			      		Predicate n1 = cb.equal(pm.get("transactionNo"), tempBean.getTransactionNo());
			      		Predicate n2 = cb.isNotNull(pm.get("processId"));
			      		Predicate n3 = cb.equal(pm.get("amendId"), maxAmend);
			      		query1.where(n1,n2,n3);
			      		
			      		// Get Result
			      		TypedQuery<Integer> res = em.createQuery(query1);
			      		List<Integer> result = res.getResultList();
			      		retroPrclStatus1 =result.get(0);
						}
						
						if(count!=0 && allocationstatus ==0 &&  retroPrclStatus1 ==0 ){
							tempBean.setDeleteStatus("Y");
						}
						if(StringUtils.isNotBlank(tempBean.getProposalNo()) ){
							CriteriaQuery<String> query1 = cb.createQuery(String.class); 
				      		
				      		Root<PositionMaster> pm = query1.from(PositionMaster.class);

				      		// Select
				      		query1.select(pm.get("ceaseStatus")); 

				      		// MAXAmend ID
				      		Subquery<Long> maxAmend = query1.subquery(Long.class); 
				      		Root<PositionMaster> pms = maxAmend.from(PositionMaster.class);
				      		maxAmend.select(cb.max(pms.get("amendId")));
				      		Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				      		maxAmend.where(a1);
				      		
				      		// Where
				      		Predicate n1 = cb.equal(pm.get("proposalNo"), tempBean.getProposalNo());
				      		Predicate n3 = cb.equal(pm.get("amendId"), maxAmend);
				      		query1.where(n1,n3);
				      		
				      		// Get Result
				      		TypedQuery<String> res = em.createQuery(query1);
				      		List<String> result = res.getResultList();
				      		if(result!=null) {
				      		tempBean.setCeaseStatus(result.get(0));
				      		}
						}
			}
			finalList.add(tempBean);
		}
		}
		response.setCommonResponse(finalList);
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
	public GetCommonDropDownRes productIdList(String branchCode) {
		List<CommonResDropDown> reslist = new ArrayList<CommonResDropDown>();
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		try{
			//GET_PREM_PRODUC_ID_LIST
			String s ="1,2,3";
			List<String> myList = new ArrayList<String>(Arrays.asList(s.split(",")));
			List<BigDecimal> bigDecimalList = myList.stream()
			        .map(BigDecimal::new)
			        .collect(Collectors.toList());

			List<TmasProductMaster> list = pmRepo.findByBranchCodeAndTmasProductIdIn(branchCode,bigDecimalList);
			if(list.size()>0) {
				for(TmasProductMaster data: list) {
					CommonResDropDown range=new CommonResDropDown();  
					range.setCode(data.getTmasProductId()==null?"":data.getTmasProductId().toString());
					range.setCodeDescription(data.getTmasProductName()==null?"":data.getTmasProductName().toString());
					reslist.add(range);		
				}
			}
		response.setCommonResponse(reslist);
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
	public ContractidetifierlistRes contractidetifierlist(ContractidetifierlistReq bean) {
		ContractidetifierlistRes response = new ContractidetifierlistRes();
		List<ContractidetifierlistRes1> finalList = new ArrayList<ContractidetifierlistRes1>();
		try{
			List<Tuple> allocists = propPremiumCustomRepository.contractIdentifierList(bean);
		for(int i=0 ; i<allocists.size() ; i++) {
			Tuple tempMap =  allocists.get(i);
			ContractidetifierlistRes1 tempBean=new ContractidetifierlistRes1();
			tempBean.setProposalNo(tempMap.get("PROPOSAL_NO")==null?"":tempMap.get("PROPOSAL_NO").toString());
			tempBean.setContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
			tempBean.setCedingcompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
			tempBean.setBrokerName(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setLayerNo(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
			//tempBean.setTransactionNumber(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
			tempBean.setTransactionType(bean.getTransactionType());
			tempBean.setDeptId(tempMap.get("TMAS_DEPARTMENT_NAME")==null?"":tempMap.get("TMAS_DEPARTMENT_NAME").toString());
			tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE")==null?"":tempMap.get("EXPIRY_DATE").toString());
			tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE")==null?"":tempMap.get("INCEPTION_DATE").toString());
			//tempBean.setTransactionDate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
			tempBean.setUnderwritingYear(tempMap.get("UW_YEAR")==null?"":tempMap.get("UW_YEAR").toString());
			tempBean.setUnderwriter(tempMap.get("UNDERWRITTER")==null?"":tempMap.get("UNDERWRITTER").toString());
			tempBean.setOldContract(tempMap.get("OLD_CONTRACTNO")==null?"":tempMap.get("OLD_CONTRACTNO").toString());
			tempBean.setDepartmentId(tempMap.get("DEPT_ID")==null?"":tempMap.get("DEPT_ID").toString());
			finalList.add(tempBean);
		}
		
		response.setCommonResponse(finalList);
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
	public PremiumListRes PremiumRiList(PremiumListReq bean) {
		PremiumListRes response = new PremiumListRes();
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		List<Tuple>allocists=null;
		//List<Map<String, Object>> allocists=new ArrayList<Map<String, Object>>();
		List<PremiumListRes1> finalList = new ArrayList<PremiumListRes1>();
	
		try{
			if("Main".equalsIgnoreCase(bean.getTableType())){
			if(bean.getType().equalsIgnoreCase("premium")){
					allocists=propPremiumCustomRepository.getPremiumRiFullList(bean);
			}
			else{
				allocists=propPremiumCustomRepository.getPremiumRiFullList(bean);
			}
			}
		if(!CollectionUtils.isEmpty(allocists)) {
		for(int i=0 ; i<allocists.size() ; i++) {
			Tuple tempMap = allocists.get(i);
			PremiumListRes1 tempBean=new PremiumListRes1();
			tempBean.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
			tempBean.setContractNo(tempMap.get("RSK_CONTRACT_NO")==null?"":tempMap.get("RSK_CONTRACT_NO").toString());
			tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
			tempBean.setBroker(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
			tempBean.setLayerno(tempMap.get("RSK_LAYER_NO")==null?"":tempMap.get("RSK_LAYER_NO").toString());
			tempBean.setTransactionNo(tempMap.get("TRANSACTION_NO")==null?"":tempMap.get("TRANSACTION_NO").toString());
			tempBean.setTransactionType(tempMap.get("RSK_PRODUCTID")==null?"":tempMap.get("RSK_PRODUCTID").toString());
			tempBean.setTransDropDownVal(tempMap.get("REVERSE_TRANSACTION_NO")==null?"":tempMap.get("REVERSE_TRANSACTION_NO").toString());
			tempBean.setSectionNo(tempMap.get("SECTION_NO")==null?"":tempMap.get("SECTION_NO").toString());
			tempBean.setRequestNo(tempMap.get("REQUEST_NO")==null?"":tempMap.get("REQUEST_NO").toString());
			if(tempBean.getTransactionType().equalsIgnoreCase("1")){
				tempBean.setTransactionTypeName("Facultative");
			}
			else if(tempBean.getTransactionType().equalsIgnoreCase("2")){
				tempBean.setTransactionTypeName("Proportional Treaty");
			}
			else if(tempBean.getTransactionType().equalsIgnoreCase("3")){
				tempBean.setTransactionTypeName("Non-Proportional Treaty");
			}
			else if(tempBean.getTransactionType().equalsIgnoreCase("5")){
				tempBean.setTransactionTypeName("Retro - Xol");
			}
			tempBean.setInsDate(tempMap.get("INS_DATE")==null?"":formatDate(tempMap.get("INS_DATE")));
			tempBean.setExpDate(tempMap.get("EXP_DATE")==null?"":formatDate(tempMap.get("EXP_DATE")));
			
			tempBean.setInceptionDate(tempMap.get("INS_DATE")==null?"":formatDate(tempMap.get("INS_DATE")));
			
			tempBean.setTransactionDate(tempMap.get("TRANSACTION_DATE")==null?"":formatDate(tempMap.get("TRANSACTION_DATE")));
			tempBean.setRitransactionNo(tempMap.get("RI_TRANSCATION_NO")==null?"":tempMap.get("RI_TRANSCATION_NO").toString());
			
			int count= dropDowmImpl.Validatethree(bean.getBranchCode(), tempBean.getTransactionDate());
			if("Main".equalsIgnoreCase(bean.getTableType())){
					if((StringUtils.isNotBlank(bean.getOpstartDate()))&& (StringUtils.isNotBlank(bean.getOpendDate()))){
						if(count ==0){
							tempBean.setTransOpenperiodStatus("N");
						}else
						{
							tempBean.setTransOpenperiodStatus("Y");
						}
						}
					
					int cnt = atRepo.countByContractNoAndTransactionNoAndLayerNoAndTypeAndStatus(tempBean.getContractNo(),new BigDecimal(tempBean.getTransactionNo()),tempBean.getLayerno(),"P","Y");
					tempBean.setAllocatedYN(cnt==0?"Y":"N");
						
						//ALLOCATION_STATUS_COMPARITION
						int allocationstatus= atRepo.countByTransactionNoAndStatus(new BigDecimal(tempBean.getTransactionNo()),"Y");
						
						//RETRO_PRCL_STATUS_COMPARITION
						int retroPrclStatus= prclRepo.countByTransactionNo(new Long(tempBean.getTransactionNo()));
						
						int retroPrclStatus1=0;
						if(retroPrclStatus!=0){
						//RETRO_PRCL_STATUS_COMPARITION1
			         
			      		CriteriaQuery<Integer> query1 = cb.createQuery(Integer.class); 
			      		
			      		// like table name
			      		Root<TtrnRetroPrcl> pm = query1.from(TtrnRetroPrcl.class);

			      		// Select
			      		query1.multiselect(cb.count(pm)); 

			      		// MAXAmend ID
			      		Subquery<Long> maxAmend = query1.subquery(Long.class); 
			      		Root<TtrnRetroPrcl> pms = maxAmend.from(TtrnRetroPrcl.class);
			      		maxAmend.select(cb.max(pms.get("amendId")));
			      		Predicate a1 = cb.equal( pm.get("transactionNo"), pms.get("transactionNo"));
			      		maxAmend.where(a1);
			      		
			      		// Where
			      		Predicate n1 = cb.equal(pm.get("transactionNo"), tempBean.getTransactionNo());
			      		Predicate n2 = cb.isNotNull(pm.get("processId"));
			      		Predicate n3 = cb.equal(pm.get("amendId"), maxAmend);
			      		query1.where(n1,n2,n3);
			      		
			      		// Get Result
			      		TypedQuery<Integer> res = em.createQuery(query1);
			      		List<Integer> result = res.getResultList();
			      		retroPrclStatus1 =result.get(0);
						}
						
						if(count!=0 && allocationstatus ==0 &&  retroPrclStatus1 ==0 ){
							tempBean.setDeleteStatus("Y");
						}
						if(StringUtils.isNotBlank(tempBean.getProposalNo()) ){
							CriteriaQuery<String> query1 = cb.createQuery(String.class); 
				      		
				      		Root<PositionMaster> pm = query1.from(PositionMaster.class);

				      		// Select
				      		query1.select(pm.get("ceaseStatus")); 

				      		// MAXAmend ID
				      		Subquery<Long> maxAmend = query1.subquery(Long.class); 
				      		Root<PositionMaster> pms = maxAmend.from(PositionMaster.class);
				      		maxAmend.select(cb.max(pms.get("amendId")));
				      		Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
				      		maxAmend.where(a1);
				      		
				      		// Where
				      		Predicate n1 = cb.equal(pm.get("proposalNo"), tempBean.getProposalNo());
				      		Predicate n3 = cb.equal(pm.get("amendId"), maxAmend);
				      		query1.where(n1,n3);
				      		
				      		// Get Result
				      		TypedQuery<String> res = em.createQuery(query1);
				      		List<String> result = res.getResultList();
				      		if(result!=null) {
				      		tempBean.setCeaseStatus(result.get(0));
				      		}
						}
			}
			finalList.add(tempBean);
		}
		}
		response.setCommonResponse(finalList);
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
//	public CommonResponse copyDatatoDeleteTable(CopyDatatoDeleteTableReq bean) {
//		CommonResponse response = new CommonResponse();
//		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
//		String data="";
//		String header="";
//		String qry="";
//		String deletedId="";
//		int ColumnCount = 0;
//		String[] Tables=new String[3];
//		Tables[0]="RSK_PREMIUM_DETAILS";
//		Tables[1]="RSK_PREMIUM_DETAILS_ARCHIVE";
//		Tables[2]="TTRN_RETRO_PRCL";
//		String[][] dataset=null;
//		try {
//			qry="select nvl(max(deleted_id),0)+1 deleted_id from ttrn_deleted_transaction";
//			list = queryImpl.selectSingle(qry, new String[] {});
//			if (!CollectionUtils.isEmpty(list)) {
//				deletedId = list.get(0).get("DELETED_ID") == null ? ""
//						: list.get(0).get("DELETED_ID").toString();
//			
//			}
//			for(int p=0;p<Tables.length;p++){
//			qry="SELECT count(column_name) column_name FROM USER_TAB_COLUMNS WHERE table_name = '"+Tables[p]+"'";
//			list = queryImpl.selectSingle(qry, new String[] {});
//			if (!CollectionUtils.isEmpty(list)) {
//				ColumnCount = Integer.valueOf(list.get(0).get("COLUMN_NAME") == null ? ""
//						: list.get(0).get("COLUMN_NAME").toString());
//			
//			}
//			Connection conn = this.mytemplate.getDataSource().getConnection();
//			java.sql.Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery( "select * from  "+Tables[p]+" where transaction_no='"+bean.getTransactionNo()+"'");
//			int columnCount = rs.getMetaData().getColumnCount();
//			
//			ResultSetMetaData rsmd = rs.getMetaData();
//			while(rs.next())
//			{
//				ArrayList <String[]> result = new ArrayList<String[]>();
//				header="";
//				
//			    String[] row = new String[columnCount];
//			    for (int i=0; i <columnCount ; i++){
//			   
//			    	header+=rsmd.getColumnName(i+1)+"|";
//			    	
//			       row[i] = rs.getString(i + 1);
//			    }
//			    result.add(row);
//			    if (result.size()>0) {			
//					for (int j=0;j<result.size();j++){
//						dataset=result.toArray(new String[0][j]);
//						 for(int l=0;l<ColumnCount;l++){
//							 data+=dataset[0][l]+"|";
//					 }
//				    qry="insert into ttrn_deleted_transaction(Deleted_id,Deleted_data,DEL_TABLE,DEL_TAB_HEADER,DEL_BY,DEL_DATE)values('"+deletedId+"','"+data+"','"+Tables[p]+"','"+header+"','"+bean.getUserName()+"',to_date(sysdate,'dd/mm/yyyy'))";
//				    queryImpl.updateQueryWP(qry);
//					data="";
//					}
//				}
//			}
//			qry="delete  from "+Tables[p]+" where transaction_no='"+bean.getTransactionNo()+"'";
//			  queryImpl.updateQueryWP(qry);
//			}
//			response.setMessage("Success");
//			response.setIsError(false);
//			} catch (Exception e) {
//			e.printStackTrace();
//			response.setMessage("Failed");
//			response.setIsError(true);
//			}
//		return response;
//	}
}
