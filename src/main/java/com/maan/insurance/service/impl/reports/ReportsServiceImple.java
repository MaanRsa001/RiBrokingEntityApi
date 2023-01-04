package com.maan.insurance.service.impl.reports;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.req.reports.GetClaimPaidRegisterListReq;
import com.maan.insurance.model.req.reports.GetClaimRegisterListReq;
import com.maan.insurance.model.req.reports.GetDebtorsAgeingReportReq;
import com.maan.insurance.model.req.reports.GetInwardRetroMappingReportReq;
import com.maan.insurance.model.req.reports.GetMoveMntSummaryReq;
import com.maan.insurance.model.req.reports.GetPayRecRegisterListReq;
import com.maan.insurance.model.req.reports.GetRetroInwardMappingReportReq;
import com.maan.insurance.model.req.reports.GetRetroRegisterListReq;
import com.maan.insurance.model.req.reports.GetTransactionMasterReportReq;
import com.maan.insurance.model.req.reports.GetallocationReportListReq;
import com.maan.insurance.model.req.reports.ReportsCommonReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCedingCompanyRes;
import com.maan.insurance.model.res.DropDown.GetCedingCompanyRes1;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.reports.GetBookedPremiumInitRes;
import com.maan.insurance.model.res.reports.GetBookedPremiumInitRes1;
import com.maan.insurance.model.res.reports.GetBookedUprInitRes;
import com.maan.insurance.model.res.reports.GetBookedUprInitRes1;
import com.maan.insurance.model.res.reports.GetClaimJournelInitRes;
import com.maan.insurance.model.res.reports.GetClaimJournelInitRes1;
import com.maan.insurance.model.res.reports.GetClaimMoveMentInitRes;
import com.maan.insurance.model.res.reports.GetClaimMoveMentInitRes1;
import com.maan.insurance.model.res.reports.GetInwardRetroMappingReportRes;
import com.maan.insurance.model.res.reports.GetInwardRetroMappingReportRes1;
import com.maan.insurance.model.res.reports.GetMoveMentInitRes;
import com.maan.insurance.model.res.reports.GetMoveMentInitRes1;
import com.maan.insurance.model.res.reports.GetMoveMntSummaryRes;
import com.maan.insurance.model.res.reports.GetMoveMntSummaryRes1;
import com.maan.insurance.model.res.reports.GetPendingOffersListRes;
import com.maan.insurance.model.res.reports.GetPendingOffersListRes1;
import com.maan.insurance.model.res.reports.GetPipelineWrittenInitRes;
import com.maan.insurance.model.res.reports.GetPipelineWrittenInitRes1;
import com.maan.insurance.model.res.reports.GetRetroQuarterlyReport;
import com.maan.insurance.model.res.reports.GetRetroQuarterlyReport1;
import com.maan.insurance.model.res.reports.ReportsCommonRes;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.jasper.JasperConfiguration;
import com.maan.insurance.service.reports.ReportsService;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;

import oracle.jdbc.OracleTypes;


@Service
public class ReportsServiceImple implements ReportsService {
	private Logger logger = LogManager.getLogger(ReportsServiceImple.class);
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private ValidationImple vi;
	
	@Autowired
	private TtrnRiskDetailsRepository rdRepo;
	
	@PersistenceContext
	private EntityManager em;
	@Autowired
	private JasperConfiguration config;
	
	private Properties prop = new Properties();
	private Query query1=null;
	  public ReportsServiceImple() {
	        try {
	        	InputStream  inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
	        	if (inputStream != null) {
					prop.load(inputStream);
				}
	        	
	        } catch (Exception e) {
	            logger.info(e);
	        }
	    }


	@Override
	public GetMoveMentInitRes getMoveMentInit(String branchCode) {
		GetMoveMentInitRes response = new GetMoveMentInitRes();
		List<GetMoveMentInitRes1> resList = new ArrayList<GetMoveMentInitRes1>();
		try{
			List<Map<String, Object>> list = queryImpl.selectList("report.select.moveMentInit", new String[] {branchCode,branchCode,branchCode});
			if(list.size()>0) {
				for(Map<String, Object> data : list) {
					GetMoveMentInitRes1 res = new GetMoveMentInitRes1();
					res.setAccountPeriod(data.get("ACCOUNT_PERIOD_DATE")==null?"":data.get("ACCOUNT_PERIOD_DATE").toString());
					res.setAccountPeriodDate(data.get("ACCOUNT_PERIOD_DATE")==null?"":data.get("ACCOUNT_PERIOD_DATE").toString());	
					res.setAccountPeriodQtr(data.get("ACCOUNT_PERIOD_QTR")==null?"":data.get("ACCOUNT_PERIOD_QTR").toString());
					res.setDeptName(data.get("DEPT_NAME")==null?"":data.get("DEPT_NAME").toString());
					res.setDetailName(data.get("DETAIL_NAME")==null?"":data.get("DETAIL_NAME").toString());
					res.setMovmentTranid(data.get("MOVMENT_TRANID")==null?"":data.get("MOVMENT_TRANID").toString());
					res.setProductName(data.get("PRODUCT_NAME")==null?"":data.get("PRODUCT_NAME").toString());
					res.setReportType(data.get("REPORT_TYPE")==null?"":data.get("REPORT_TYPE").toString());
					resList.add(res);
					}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
		logger.debug("Exception @ {" + e + "}");
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}

	@Override
	public GetClaimMoveMentInitRes getClaimMoveMentInit(String branchCode) {
		GetClaimMoveMentInitRes response = new GetClaimMoveMentInitRes();
		List<GetClaimMoveMentInitRes1> resList = new ArrayList<GetClaimMoveMentInitRes1>();
		try{
			List<Map<String, Object>> list = queryImpl.selectList("report.select.clMoveMentInit", new String[] {branchCode});
			if(list!=null && list.size()>0){
				for(Map<String, Object> tempMap : list) {
				GetClaimMoveMentInitRes1 bean = new GetClaimMoveMentInitRes1();
				bean.setSno(tempMap.get("MOVEMENT_ID")==null?"":tempMap.get("MOVEMENT_ID").toString());
				bean.setAccountDate(tempMap.get("MOVEMENT_DT")==null?"":tempMap.get("MOVEMENT_DT").toString());
				bean.setMovementType(tempMap.get("REPORT_TYPE")==null?"Interim":tempMap.get("REPORT_TYPE").toString().equalsIgnoreCase("F")?"Final":"Intreim");
				resList.add(bean);
				}
			}	
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
		logger.debug("Exception @ {" + e + "}");
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}

	@Override
	public GetClaimJournelInitRes getClaimJournelInit(String branchCode) {
		GetClaimJournelInitRes response = new GetClaimJournelInitRes();
		List<GetClaimJournelInitRes1> resList = new ArrayList<GetClaimJournelInitRes1>();
		try{
			List<Map<String, Object>> list = queryImpl.selectList("report.select.clJournalInit", new String[] {branchCode});
			if(list!=null && list.size()>0){
				for(Map<String, Object> tempMap : list) {
				GetClaimJournelInitRes1 bean = new GetClaimJournelInitRes1();
				bean.setSno(tempMap.get("MOVMENT_TRANID")==null?"":tempMap.get("MOVMENT_TRANID").toString());
				bean.setAccountPeriod(tempMap.get("DETAIL_NAME")==null?"":tempMap.get("DETAIL_NAME").toString());
				bean.setAccper(tempMap.get("ACCOUNT_PERIOD_QTR")==null?"":tempMap.get("ACCOUNT_PERIOD_QTR").toString());
				bean.setAccountDate(tempMap.get("ACCOUNT_PERIOD")==null?"":tempMap.get("ACCOUNT_PERIOD").toString());
				bean.setMovementType(tempMap.get("REPORT_TYPE")==null?"Interim":tempMap.get("REPORT_TYPE").toString().equalsIgnoreCase("F")?"Final":"Intreim");
				resList.add(bean);
			}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
		logger.debug("Exception @ {" + e + "}");
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}

	@Override
	public GetBookedUprInitRes getBookedUprInit(String branchCode) {
		GetBookedUprInitRes response = new GetBookedUprInitRes();
		List<GetBookedUprInitRes1> resList = new ArrayList<GetBookedUprInitRes1>();
		try{
			List<Map<String, Object>> list = queryImpl.selectList("BOOKED_UPR_INIT", new String[] {branchCode});
			if(list!=null && list.size()>0){
				for(Map<String, Object> tempMap : list) {
					GetBookedUprInitRes1 bean = new GetBookedUprInitRes1();		
				bean.setSno(tempMap.get("MOVEMENT_ID")==null?"":tempMap.get("MOVEMENT_ID").toString());
				bean.setUWYear(tempMap.get("UW_YEAR")==null?"":tempMap.get("UW_YEAR").toString());
				bean.setReportDate(tempMap.get("REPORT_DATE")==null?"":tempMap.get("REPORT_DATE").toString());
				bean.setMovementType(tempMap.get("MOVEMENT_TYPE")==null?"Interim":tempMap.get("MOVEMENT_TYPE").toString().equalsIgnoreCase("F")?"Final":"Intreim");
				resList.add(bean);
			}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
		logger.debug("Exception @ {" + e + "}");
		e.printStackTrace();	
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}

	@Override
	public GetBookedPremiumInitRes getBookedPremiumInit(String branchCode) { 
		GetBookedPremiumInitRes response = new GetBookedPremiumInitRes();
		List<GetBookedPremiumInitRes1> resList = new ArrayList<GetBookedPremiumInitRes1>();
		try{
			List<Map<String, Object>> list = queryImpl.selectList("BOOKED_PREMIUM_INIT", new String[] {branchCode});
			if(list!=null && list.size()>0){
				for(Map<String, Object> tempMap : list) {
					GetBookedPremiumInitRes1 bean = new GetBookedPremiumInitRes1();		
				bean.setSno(tempMap.get("MOVEMENT_ID")==null?"":tempMap.get("MOVEMENT_ID").toString());
				bean.setUWYear(tempMap.get("UW_YEAR")==null?"":tempMap.get("UW_YEAR").toString());
				bean.setReportDate(tempMap.get("REPORT_DATE")==null?"":tempMap.get("REPORT_DATE").toString());
				bean.setMovementType(tempMap.get("MOVEMENT_TYPE")==null?"Interim":tempMap.get("MOVEMENT_TYPE").toString().equalsIgnoreCase("F")?"Final":"Intreim");
				resList.add(bean);
			}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
		logger.debug("Exception @ {" + e + "}");
		e.printStackTrace();	
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}

	@Override
	public GetPipelineWrittenInitRes getPipelineWrittenInit(String branchCode) { 
		GetPipelineWrittenInitRes response = new GetPipelineWrittenInitRes();
		List<GetPipelineWrittenInitRes1> resList = new ArrayList<GetPipelineWrittenInitRes1>();
		try{
			List<Map<String, Object>> list = queryImpl.selectList("PIPELIEN_WRITTEN_INIT", new String[] {branchCode});
			if(list!=null && list.size()>0){
				for(Map<String, Object> tempMap : list) {
					GetPipelineWrittenInitRes1 bean = new GetPipelineWrittenInitRes1();		
				bean.setSno(tempMap.get("MOVEMENT_ID")==null?"":tempMap.get("MOVEMENT_ID").toString());
				bean.setUWYear(tempMap.get("UW_YEAR")==null?"":tempMap.get("UW_YEAR").toString());
				bean.setReportDate(tempMap.get("REPORT_DATE")==null?"":tempMap.get("REPORT_DATE").toString());
				bean.setBranchCode(tempMap.get("BRANCH_CODE")==null?"":tempMap.get("BRANCH_CODE").toString());
				bean.setMovementType(tempMap.get("MOVEMENT_TYPE")==null?"Interim":tempMap.get("MOVEMENT_TYPE").toString().equalsIgnoreCase("F")?"Final":"Intreim");
				resList.add(bean);
			}
			}
			response.setCommonResponse(resList);
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
		logger.debug("Exception @ {" + e + "}");
		e.printStackTrace();	
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
	}

	@Override
	public GetCommonDropDownRes getProductDropDown(String branchCode,String typeId) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		try{
			list = queryImpl.selectList("reportdao.select.getReportProductList",new String[]{typeId,branchCode});
			for(int i=0 ; i<list.size() ; i++) {
				CommonResDropDown res = new CommonResDropDown();
				Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
				res.setCode(tempMap.get("TMAS_PRODUCT_ID")==null?"":tempMap.get("TMAS_PRODUCT_ID").toString());
				res.setCodeDescription(tempMap.get("TMAS_PRODUCT_NAME")==null?"":tempMap.get("TMAS_PRODUCT_NAME").toString());
				resList.add(res);
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
	public GetCedingCompanyRes getCedingCompany(String branchCode, String productId) {
		GetCedingCompanyRes response = new GetCedingCompanyRes();
		List<GetCedingCompanyRes1> resList = new ArrayList<GetCedingCompanyRes1>();
		String  cedingco="";
		try{
			List<Map<String,Object>> list = queryImpl.selectList("report.select.getCedingCompanyList",new String[]{branchCode,"C","Y",branchCode,productId});
			if(list!=null && list.size()>0){
				logger.info("List Size=>"+list.size());
				Map<String,Object> resMap;
				for(int i=0;i<list.size();i++){
					GetCedingCompanyRes1 res = new GetCedingCompanyRes1();
					resMap = (Map<String,Object>)list.get(i); 
					if(i==(list.size()-1)){
						cedingco+=resMap.get("CUSTOMER_ID").toString()+"~"+resMap.get("NAME").toString();
					}else{
						cedingco+=resMap.get("CUSTOMER_ID").toString()+"~"+resMap.get("NAME").toString()+"~";	
					}
					res.setCustomerId(resMap.get("CUSTOMER_ID")==null?"":resMap.get("CUSTOMER_ID").toString());
					res.setName(resMap.get("NAME")==null?"":resMap.get("NAME").toString());
					res.setCedingCo(cedingco);
					resList.add(res);
				} }
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
	public CommonSaveRes getReportName(String typeId, String productId) {
		CommonSaveRes response = new CommonSaveRes();
		String reportName=null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
			if("19".equals(typeId) || "21".equals(typeId)|| "11".equals(typeId) || "54".equals(typeId)){
				list=  queryImpl.selectList("report.select.reportname",new String[]{typeId,"Y"});
			}else{
				list=  queryImpl.selectList("report.select.getReportName",new String[]{productId,typeId,"Y"});
			}
			if (!CollectionUtils.isEmpty(list)) {
				reportName =list.get(0).get("TYPE_NAME") == null ? "": list.get(0).get("TYPE_NAME").toString();
			}
			response.setResponse(reportName);
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
	public GetPendingOffersListRes getPendingOffersList(ReportsCommonReq bean) {
		GetPendingOffersListRes response = new GetPendingOffersListRes();
		List<GetPendingOffersListRes1> resList = new ArrayList<GetPendingOffersListRes1>();
		String query="";
		String[] obj= new String[0];
		String qutext = "";
		try {
		if("1".equalsIgnoreCase(bean.getProductId())){
			query = "reportdao.select.facgetQuoteRegisterList";
			  qutext = prop.getProperty(query);
			obj=new String[4];
			obj[0]=bean.getBranchCode();
			obj[1]=bean.getProductId();
			obj[2]=bean.getStartDate();
			obj[3]=bean.getEndDate();
			if(!"-1".equals(bean.getLoginId())){
				obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
			}
			if(!"-1".equals(bean.getBrokerId())){
				obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
			}
			if(!"-1".equals(bean.getCedingId())){
				obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
			}
			if(!"-1".equals(bean.getUwYear())){
				obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
				qutext += " " +prop.getProperty("report.select.reportUWYear");
			}
			qutext += " " +prop.getProperty("reportdao.select.facOrder");
		}else if("2".equalsIgnoreCase(bean.getProductId()) || "3".equalsIgnoreCase(bean.getProductId())){
			query= "reportdao.select.xolOrTreatygetQuoteRegisterList";
			  qutext = prop.getProperty(query);
			obj=new String[4];
			obj[0]=bean.getBranchCode();
			obj[1]=bean.getProductId();
			obj[2]=bean.getStartDate();
			obj[3]=bean.getEndDate();
			if(!"-1".equals(bean.getLoginId())){
				obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
			}
			if(!"-1".equals(bean.getBrokerId())){
				obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
			}
			if(!"-1".equals(bean.getCedingId())){
				obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
			}
			if(!"-1".equals(bean.getUwYear())){
				obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
				qutext += " " +prop.getProperty("report.select.reportUWYear");
			}
			qutext += " " +prop.getProperty("reportdao.select.facOrder");
		}
		else if("4".equalsIgnoreCase(bean.getProductId()) || "5".equalsIgnoreCase(bean.getProductId())){
			query="reportdao.select.deptNameRetroRetroxol";
			qutext = prop.getProperty(query);
			obj=new String[6];
			obj[0]=bean.getBranchCode();
			obj[1]=bean.getBranchCode();
			obj[2]=bean.getProductId();
			obj[3]=bean.getDept();
			obj[4]=bean.getStartDate();
			obj[5]=bean.getEndDate();
			logger.info("DeptName==>"+bean.getDept());
			if(!"-1".equals(bean.getLoginId())){
				obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
			}
			if(!"-1".equals(bean.getBrokerId())){
				obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
			}
			if(!"-1".equals(bean.getCedingId())){
				obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
			}
			if(!"-1".equals(bean.getUwYear())){
				obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
				qutext += " " +prop.getProperty("report.select.reportUWYear");
			}
			qutext += " " +prop.getProperty("reportdao.select.facOrder");
		}else{
			query= "reportdao.select.xolOrTreatygetQuoteRegisterList";
			qutext = prop.getProperty(query);
			obj=new String[4];
			obj[0]=bean.getBranchCode();
			obj[1]=bean.getProductId();
			obj[2]=bean.getStartDate();
			obj[3]=bean.getEndDate();
			if(!"-1".equals(bean.getLoginId())){
				obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
			}
			if(!"-1".equals(bean.getBrokerId())){
				obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
			}
			if(!"-1".equals(bean.getCedingId())){
				obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
				qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
			}
			if(!"-1".equals(bean.getUwYear())){
				obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
				qutext += " " +prop.getProperty("report.select.reportUWYear");
			}
			qutext += " " +prop.getProperty("reportdao.select.facOrder");
		}
		List<Map<String, Object>> list = new ArrayList<>();
    	query1 =queryImpl.setQueryProp(qutext, obj);
		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		try {
			 list = query1.getResultList();
		} catch(Exception e) {
			e.printStackTrace();
		} 
		if(list!=null && list.size()>0){
			for(Map<String, Object> tempMap : list) {
				GetPendingOffersListRes1 res = new GetPendingOffersListRes1();
				res.setAccDate(tempMap.get("ACC_DATE")==null?"":tempMap.get("ACC_DATE").toString());
				res.setBranchCode(tempMap.get("BRANCH_CODE")==null?"":tempMap.get("BRANCH_CODE").toString());
				res.setBrokerName(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
				res.setCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());
				res.setExpiryDate(tempMap.get("EXP_DATE")==null?"":tempMap.get("EXP_DATE").toString());
				res.setInceptionDate(tempMap.get("INC_DATE")==null?"":tempMap.get("INC_DATE").toString());
				res.setInsuredName(tempMap.get("RSK_INSURED_NAME")==null?"":tempMap.get("RSK_INSURED_NAME").toString());
				res.setProposalNo(tempMap.get("RSK_PROPOSAL_NUMBER")==null?"":tempMap.get("RSK_PROPOSAL_NUMBER").toString());
				res.setProposalStatus(tempMap.get("PROPOSAL_STATUS")==null?"":tempMap.get("PROPOSAL_STATUS").toString());
				res.setShareSigned(tempMap.get("SHARE_SIGNED")==null?"":tempMap.get("SHARE_SIGNED").toString());
				res.setShareWritten(tempMap.get("SHARE_WRITTEN")==null?"":tempMap.get("SHARE_WRITTEN").toString());
				res.setTmasSpfcName(tempMap.get("TMAS_SPFC_NAME")==null?"":tempMap.get("TMAS_SPFC_NAME").toString());
				res.setUnderWritter(tempMap.get("RSK_UNDERWRITTER")==null?"":tempMap.get("RSK_UNDERWRITTER").toString());
				res.setUWYear(tempMap.get("RSK_UWYEAR")==null?"":tempMap.get("RSK_UWYEAR").toString());
				resList.add(res);
		} }
				response.setCommonResponse(resList);
				response.setMessage("Success");
				response.setIsError(false);
			} catch (Exception e) {
			logger.debug("Exception @ {" + e + "}");
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
	private String[] getIncObjectArray(String[] srcObj,String[] descObj){
		final String[] tempObj = new String[srcObj.length];
		System.arraycopy(srcObj, 0, tempObj, 0, srcObj.length);
		srcObj=new String[tempObj.length+descObj.length];
		System.arraycopy(tempObj, 0, srcObj, 0, tempObj.length);
		System.arraycopy(descObj, 0, srcObj, tempObj.length, descObj.length);
		return srcObj;
	}

	@Override
	public ReportsCommonRes getPolicyRegisterList(ReportsCommonReq bean) {
		ReportsCommonRes response = new ReportsCommonRes();
		ResultSet resultSet=null;
		String query="";
		 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		 String[] obj= new String[0];
		 String qutext = "";
		 try {
		if("1".equalsIgnoreCase(bean.getProductId())){
		Connection connection=null;
		try {
			connection = config.getDataSourceForJasper().getConnection();
		} 
		catch (Exception e1) {
			e1.printStackTrace();
		} 
		try {
			String runSP = "{call PRC_FAC_POL_REG(?,?,?,?,?,?,?,?,?,?)}";
			CallableStatement cs = connection.prepareCall(runSP);
			cs.setString(1, bean.getStartDate());
			cs.setString(2, bean.getEndDate());
			cs.setString(3, bean.getBranchCode());
			cs.setString(4, bean.getLoginId());
			cs.setString(5, bean.getBrokerId());
			cs.setString(6, bean.getCedingId());
			cs.setString(7, bean.getUwYear());
			cs.setString(8, "10");
			cs.setString(9, "1");

			cs.registerOutParameter(10, OracleTypes.CURSOR);

			cs.execute();

			resultSet = (ResultSet) cs.getObject(10);
			
			response.setCommonResponse(resultSetToArrayList(resultSet));
			response.setMessage("Success");
			response.setIsError(false);
		} catch (Exception e) {
		e.printStackTrace();
		response.setMessage("Failed");
		response.setIsError(true);
	}
	return response;
        
		}else if("2".equalsIgnoreCase(bean.getProductId()) || "3".equalsIgnoreCase(bean.getProductId())){
			query = "report.select.policyRegPropotionalTreaty";
			qutext = prop.getProperty(query);
			obj=new String[4];
			obj[0]=bean.getProductId();
			obj[1]=bean.getStartDate();
			obj[2]=bean.getEndDate();
			obj[3]=bean.getBranchCode();
			if(!"-1".equals(bean.getLoginId())){
				obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacLoginId");
			}
			if(!"-1".equals(bean.getBrokerId())){
				obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacBrokerId");
			}
			if(!"-1".equals(bean.getCedingId())){
				obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacCedingId");
			}
			if(!"-1".equals(bean.getUwYear())){
				obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
				qutext += " " +prop.getProperty("report.select.reportUWYear");
			}
			qutext += " " +prop.getProperty("report.select.policyRegFacOrderBy");
		}else if("4".equalsIgnoreCase(bean.getProductId())||"5".equalsIgnoreCase(bean.getProductId())){
			query = "report.select.retroPolicyReport";
			qutext = prop.getProperty(query);
			obj=new String[6];
			obj[0]=bean.getBranchCode();
			obj[1]=bean.getProductId();
			obj[2]=bean.getDept();
			obj[3]=bean.getStartDate();
			obj[4]=bean.getEndDate();
			obj[5]=bean.getBranchCode();
			if(!"-1".equals(bean.getLoginId())){
				obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacLoginId");
			}
			if(!"-1".equals(bean.getBrokerId())){
				obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacBrokerId");
			}
			if(!"-1".equals(bean.getCedingId())){
				obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacCedingId");
			}
			if(!"-1".equals(bean.getUwYear())){
				obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
				qutext += " " +prop.getProperty("report.select.reportUWYear");
			}
			qutext += " " +prop.getProperty("report.select.policyRegFacOrderBy");
		}
		query1 =queryImpl.setQueryProp(qutext, obj);
		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		try {
			 list = query1.getResultList();
		} catch(Exception e) {
			e.printStackTrace();
		} 
				response.setCommonResponse(list);
				response.setMessage("Success");
				response.setIsError(false);
			} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Failed");
			response.setIsError(true);
		}
		return response;
	}
		 public List<Map<String,Object>> resultSetToArrayList(ResultSet rs) {
			 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				try {
					ResultSetMetaData md = rs.getMetaData();
					int columns = md.getColumnCount();
					
					while (rs.next()){
						HashMap row = new HashMap(columns);
						for(int i=1; i<=columns; ++i){
							row.put(md.getColumnName(i),rs.getObject(i));
						}
						list.add(row);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return list;
			}


		@Override
		public ReportsCommonRes getPremiumRegisterList(ReportsCommonReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			String[] obj= new String[0];
			String query="";
			String qutext = "";
			try {
			if("1".equalsIgnoreCase(bean.getProductId())){
				obj= new String[7];
				obj[0]=bean.getStartDate();
				obj[1]=bean.getEndDate();
				obj[2]=bean.getBranchCode();
				obj[3]=bean.getLoginId();
				obj[4] =bean.getBrokerId();
				obj[5] =bean.getCedingId();
				obj[6] = bean.getUwYear();
				query = "report.select.premiumReport";
				qutext = prop.getProperty(query);
			}else if("2".equalsIgnoreCase(bean.getProductId())){
				obj= new String[7];
				obj[0]=bean.getStartDate();
				obj[1]=bean.getEndDate();
				obj[2]=bean.getBranchCode();
				obj[3]=bean.getLoginId();
				obj[4] =bean.getBrokerId();
				obj[5] =bean.getCedingId();
				obj[6] = bean.getUwYear();
				query="REPORT_SELECT_PTTYPREMIUMREPORT";
				qutext = prop.getProperty(query);
			}else if("3".equalsIgnoreCase(bean.getProductId())){
				obj= new String[7];
				obj[0]=bean.getStartDate();
				obj[1]=bean.getEndDate();
				obj[2]=bean.getBranchCode();
				obj[3]=bean.getLoginId();
				obj[4] =bean.getBrokerId();
				obj[5] =bean.getCedingId();
				obj[6] = bean.getUwYear();
				query="REPORT_SELECT_NPTTYPREMIUMREPORT";
				qutext = prop.getProperty(query);
			}
			else if("4".equalsIgnoreCase(bean.getProductId()) || "5".equalsIgnoreCase(bean.getProductId())){
				obj=new String[6];
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getProductId();
				obj[2]=bean.getDept();
				obj[3]=bean.getStartDate();
				obj[4]=bean.getEndDate();
				obj[5]=bean.getBranchCode();
				query="report.select.retroPermiumClaim";
				qutext = prop.getProperty(query);
			}
			if("4".equalsIgnoreCase(bean.getProductId()) || "5".equalsIgnoreCase(bean.getProductId())){
				if(!"-1".equals(bean.getLoginId())){
					obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacLoginId");
				}
				if(!"-1".equals(bean.getBrokerId())){
					obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacBrokerId");
				}
				if(!"-1".equals(bean.getCedingId())){
					obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacCedingId");
				}
				if(!"-1".equals(bean.getUwYear())){
					obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
					qutext += " " +prop.getProperty("report.select.policyRegFacUWYear");
				}
			}
			qutext += " " +prop.getProperty("report.select.premiumReportOrderBy");
			query1 =queryImpl.setQueryProp(qutext, obj);
			query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			try {
				 list = query1.getResultList();
			} catch(Exception e) {
				e.printStackTrace();
			} 
			response.setCommonResponse(list);
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
		public ReportsCommonRes getClaimRegisterList(GetClaimRegisterListReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			 List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			try{
			String[] obj= new String[0];
			String query="";
			query  = "GET_CLAIM_REG_REPORT";
			obj= new String[7];
			obj[0] = bean.getProductId();
			obj[1]=bean.getEndDate();
			obj[2]=bean.getBranchCode();
			obj[3]=bean.getLoginId();
			obj[4] =bean.getBrokerId();
			obj[5] =bean.getCedingId();
			obj[6] = bean.getUwYear();
			list = queryImpl.selectList(query,obj);
			response.setCommonResponse(list);
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
		public ReportsCommonRes getRenewalDueList(ReportsCommonReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			String query="";
			String[] obj= new String[0];
			String qutext ="";
			try {
			if("1".equalsIgnoreCase(bean.getProductId())){
				query= "reportdao.select.renewalDueFac";
				qutext = prop.getProperty(query);
				obj=new String[6];
				obj[0]=bean.getProductId();
				obj[1]=bean.getBranchCode();
				obj[2]=bean.getBranchCode();
				obj[3]=bean.getBranchCode();
				obj[4]=bean.getStartDate();
				obj[5]=bean.getEndDate();
				if(!"-1".equals(bean.getLoginId())){
					obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
				}
				if(!"-1".equals(bean.getBrokerId())){
					obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
				}
				if(!"-1".equals(bean.getCedingId())){
					obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
				}
				if(!"-1".equals(bean.getUwYear())){
					obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
					qutext += " " +prop.getProperty("report.select.reportUWYear");
				}
				qutext += " " +prop.getProperty("reportdao.select.renewalOrder");
			}else if("2".equalsIgnoreCase(bean.getProductId()) || "3".equalsIgnoreCase(bean.getProductId())){
				query="reportda.select.xolRenewalDueList";
				qutext = prop.getProperty(query);
				obj=new String[4];
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getProductId();
				obj[2]=bean.getStartDate();
				obj[3]=bean.getEndDate();
				if(!"-1".equals(bean.getLoginId())){
					obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
				}
				if(!"-1".equals(bean.getBrokerId())){
					obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
				}
				if(!"-1".equals(bean.getCedingId())){
					obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
				}
				if(!"-1".equals(bean.getUwYear())){
					obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
					qutext += " " +prop.getProperty("report.select.reportUWYear");
				}
			}else if("4".equalsIgnoreCase(bean.getProductId()) || "5".equalsIgnoreCase(bean.getProductId())){
				query = "reportda.select.retroRetroxol";
				qutext = prop.getProperty(query);
				obj=new String[6];
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getBranchCode();
				obj[2]=bean.getProductId();
				obj[3]=bean.getDept();
				obj[4]=bean.getStartDate();
				obj[5]=bean.getEndDate();
				if(!"-1".equals(bean.getLoginId())){
					obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
				}
				if(!"-1".equals(bean.getBrokerId())){
					obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
				}
				if(!"-1".equals(bean.getCedingId())){
					obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
				}
				if(!"-1".equals(bean.getUwYear())){
					obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
					qutext += " " +prop.getProperty("report.select.reportUWYear");
				}
			}else{
				query= "reportda.select.xolRenewalDueList";
				qutext = prop.getProperty(query);
				obj=new String[4];
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getProductId();
				obj[2]=bean.getStartDate();
				obj[3]=bean.getEndDate();
				if(!"-1".equals(bean.getLoginId())){
					obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterLoginId");
				}
				if(!"-1".equals(bean.getBrokerId())){
					obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterBrokerId");
				}
				if(!"-1".equals(bean.getCedingId())){
					obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
					qutext += " " +prop.getProperty("reportdao.select.facgetQuoteRegisterCedingId");
				}
				if(!"-1".equals(bean.getUwYear())){
					obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
					qutext += " " +prop.getProperty("report.select.reportUWYear");
				}
			}
			query1 =queryImpl.setQueryProp(qutext, obj);
			query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			try {
				 list = query1.getResultList();
			} catch(Exception e) {
				e.printStackTrace();
			} 
			response.setCommonResponse(list);
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
		public GetRetroQuarterlyReport getRetroQuarterlyReport(ReportsCommonReq bean) {
			GetRetroQuarterlyReport response = new GetRetroQuarterlyReport();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			List<GetRetroQuarterlyReport1> resList = new ArrayList<GetRetroQuarterlyReport1>();
			String query="";
			String qutext = "";
			String[] obj= new String[0];
			try {
			query="report.select.retroQuarRDS";
			qutext = prop.getProperty(query);
			obj=new String[6];
			obj[0]=bean.getBranchCode();
			obj[1]=bean.getProductId();
			obj[2]=bean.getDept();
			obj[3]=bean.getStartDate();
			obj[4]=bean.getEndDate();
			obj[5]=bean.getBranchCode();
			if(!"-1".equals(bean.getLoginId())){
				obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacLoginId");
			}
			if(!"-1".equals(bean.getBrokerId())){
				obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacBrokerId");
			}
			if(!"-1".equals(bean.getCedingId())){
				obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
				qutext += " " +prop.getProperty("report.select.policyRegFacCedingId");
			}
			query1 =queryImpl.setQueryProp(qutext, obj);
			query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			try {
				list = query1.getResultList();
			} catch(Exception e) {
				e.printStackTrace();
			} 
			if(list!=null && list.size()>0){
				for(Map<String, Object> tempMap : list) {
					GetRetroQuarterlyReport1 res = new GetRetroQuarterlyReport1();
					res.setAdjustmentPremiumDc(tempMap.get("ADJUSTMENT_PREMIUM_DC")==null?"":tempMap.get("ADJUSTMENT_PREMIUM_DC").toString());
					res.setAdjustmentPremiumOc(tempMap.get("ADJUSTMENT_PREMIUM_OC")==null?"":tempMap.get("ADJUSTMENT_PREMIUM_OC").toString());
					res.setBrokerageAmtDc(tempMap.get("BROKERAGE_AMT_DC")==null?"":tempMap.get("BROKERAGE_AMT_DC").toString());
					res.setBrokerageAmtOc(tempMap.get("BROKERAGE_AMT_OC")==null?"":tempMap.get("BROKERAGE_AMT_OC").toString());					
					res.setBrokerName(tempMap.get("BROKER_NAME")==null?"":tempMap.get("BROKER_NAME").toString());
					res.setCashLossCreditDc(tempMap.get("CASH_LOSS_CREDIT_DC")==null?"":tempMap.get("CASH_LOSS_CREDIT_DC").toString());
					res.setCashLossCreditOc(tempMap.get("CASH_LOSS_CREDIT_OC")==null?"":tempMap.get("CASH_LOSS_CREDIT_OC").toString());					
					res.setCashLosspaidDc(tempMap.get("CASH_LOSSPAID_DC")==null?"":tempMap.get("CASH_LOSSPAID_DC").toString());
					res.setCashLosspaidOc(tempMap.get("CASH_LOSSPAID_OC")==null?"":tempMap.get("CASH_LOSSPAID_OC").toString());
					res.setClaimPortfolioinDc(tempMap.get("CLAIM_PORTFOLIOIN_DC")==null?"":tempMap.get("CLAIM_PORTFOLIOIN_DC").toString());
					res.setClaimPortfolioinOc(tempMap.get("CLAIM_PORTFOLIOIN_OC")==null?"":tempMap.get("CLAIM_PORTFOLIOIN_OC").toString());			
					res.setClaimPortfolioOutDc(tempMap.get("CLAIM_PORTFOLIO_OUT_DC")==null?"":tempMap.get("CLAIM_PORTFOLIO_OUT_DC").toString());
					res.setClaimPortfolioOutOc(tempMap.get("CLAIM_PORTFOLIO_OUT_OC")==null?"":tempMap.get("CLAIM_PORTFOLIO_OUT_OC").toString());
					res.setClaimsPaidDc(tempMap.get("CLAIMS_PAID_DC")==null?"":tempMap.get("CLAIMS_PAID_DC").toString());
					res.setClaimsPaidOc(tempMap.get("CLAIMS_PAID_OC")==null?"":tempMap.get("CLAIMS_PAID_OC").toString());
					res.setCommissionOc(tempMap.get("COMMISSIONOC")==null?"":tempMap.get("COMMISSIONOC").toString());
					res.setCommissionQuotashareDc(tempMap.get("COMMISSION_QUOTASHARE_DC")==null?"":tempMap.get("COMMISSION_QUOTASHARE_DC").toString());
					res.setCommissionQuotashareOc(tempMap.get("COMMISSION_QUOTASHARE_OC")==null?"":tempMap.get("COMMISSION_QUOTASHARE_OC").toString());
					res.setCommissionSurplusDc(tempMap.get("COMMISSION_SURPLUS_DC")==null?"":tempMap.get("COMMISSION_SURPLUS_DC").toString());
					res.setCommissionSurplusOc(tempMap.get("COMMISSION_SURPLUS_OC")==null?"":tempMap.get("COMMISSION_SURPLUS_OC").toString());	
					res.setCompanyName(tempMap.get("COMPANY_NAME")==null?"":tempMap.get("COMPANY_NAME").toString());		
					res.setContractNo(tempMap.get("ACC_DATE")==null?"":tempMap.get("ACC_DATE").toString());
					res.setDocType(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
					res.setExchangeRate(tempMap.get("EXCHANGE_RATE")==null?"":tempMap.get("EXCHANGE_RATE").toString());		
					res.setInceptionDate(tempMap.get("INCEPTION_DATE")==null?"":tempMap.get("INCEPTION_DATE").toString());
					res.setInterestDc(tempMap.get("INTEREST_DC")==null?"":tempMap.get("INTEREST_DC").toString());
					res.setInterestOc(tempMap.get("INTEREST_OC")==null?"":tempMap.get("INTEREST_OC").toString());
					res.setInwardtype(tempMap.get("INWARDTYPE")==null?"":tempMap.get("INWARDTYPE").toString());
					res.setInwuwYear(tempMap.get("INWUW_YEAR")==null?"":tempMap.get("INWUW_YEAR").toString());
					res.setLayerNo(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
					res.setLossReserveReleasedDc(tempMap.get("LOSS_RESERVE_RELEASED_DC")==null?"":tempMap.get("LOSS_RESERVE_RELEASED_DC").toString());
					res.setLossReserveReleasedOc(tempMap.get("LOSS_RESERVE_RELEASED_OC")==null?"":tempMap.get("LOSS_RESERVE_RELEASED_OC").toString());
					res.setLossReserveRetainedDc(tempMap.get("LOSS_RESERVERETAINED_DC")==null?"":tempMap.get("LOSS_RESERVERETAINED_DC").toString());
					res.setLossReserveretainedOc(tempMap.get("LOSS_RESERVERETAINED_OC")==null?"":tempMap.get("LOSS_RESERVERETAINED_OC").toString());
					res.setMdPremiumDc(tempMap.get("M_DPREMIUM_DC")==null?"":tempMap.get("M_DPREMIUM_DC").toString());
					res.setMdPremiumOc(tempMap.get("M_DPREMIUM_OC")==null?"":tempMap.get("M_DPREMIUM_OC").toString());
					res.setNetdueDc(tempMap.get("NETDUE_DC")==null?"":tempMap.get("NETDUE_DC").toString());	
					res.setNetdueOc(tempMap.get("NETDUEOC")==null?"":tempMap.get("NETDUEOC").toString());	
					res.setNetdueUgx(tempMap.get("NETDUE_UGX")==null?"":tempMap.get("NETDUE_UGX").toString());
					res.setOtherCostDc(tempMap.get("OTHER_COST_DC")==null?"":tempMap.get("OTHER_COST_DC").toString());
					res.setOtherCostOc(tempMap.get("OTHER_COST_OC")==null?"":tempMap.get("OTHER_COST_OC").toString());
					res.setOutwardoverriderOc(tempMap.get("OUTWARDOVERRIDER_OC")==null?"":tempMap.get("OUTWARDOVERRIDER_OC").toString());
					res.setOutwaroverriderDc(tempMap.get("OUTWAROVERRIDER_DC")==null?"":tempMap.get("OUTWAROVERRIDER_DC").toString());
					res.setOverriderAmtDc(tempMap.get("OVERRIDER_AMT_DC")==null?"":tempMap.get("OVERRIDER_AMT_DC").toString());
					res.setOverriderAmtOc(tempMap.get("OVERRIDER_AMT_OC")==null?"":tempMap.get("OVERRIDER_AMT_OC").toString());
					res.setPremiumOc(tempMap.get("PREMIUM_OC")==null?"":tempMap.get("PREMIUM_OC").toString());	
					res.setPremiumPortfolioinDc(tempMap.get("PREMIUM_PORTFOLIOIN_DC")==null?"":tempMap.get("PREMIUM_PORTFOLIOIN_DC").toString());
					res.setPremiumPortfolioinOc(tempMap.get("PREMIUM_PORTFOLIOIN_OC")==null?"":tempMap.get("PREMIUM_PORTFOLIOIN_OC").toString());
					res.setPremiumPortfoliooutDc(tempMap.get("PREMIUM_PORTFOLIOOUT_DC")==null?"":tempMap.get("PREMIUM_PORTFOLIOOUT_DC").toString());
					res.setPremiumPortfoliooutOc(tempMap.get("PREMIUM_PORTFOLIOOUT_OC")==null?"":tempMap.get("PREMIUM_PORTFOLIOOUT_OC").toString());
					res.setPremiumQuotashareDc(tempMap.get("PREMIUM_QUOTASHARE_DC")==null?"":tempMap.get("PREMIUM_QUOTASHARE_DC").toString());
					res.setPremiumQuotashareOc(tempMap.get("PREMIUM_QUOTASHARE_OC")==null?"":tempMap.get("PREMIUM_QUOTASHARE_OC").toString());		
					res.setPremiumReserveRealsedDc(tempMap.get("PREMIUM_RESERVE_REALSED_DC")==null?"":tempMap.get("PREMIUM_RESERVE_REALSED_DC").toString());
					res.setPremiumReserveRealsedOc(tempMap.get("PREMIUM_RESERVE_REALSED_OC")==null?"":tempMap.get("PREMIUM_RESERVE_REALSED_OC").toString());
					res.setPremiumReserveRetainedDc(tempMap.get("PREMIUM_RESERVE_RETAINED_DC")==null?"":tempMap.get("PREMIUM_RESERVE_RETAINED_DC").toString());
					res.setPremiumReserveRetainedOc(tempMap.get("PREMIUM_RESERVE_RETAINED_OC")==null?"":tempMap.get("PREMIUM_RESERVE_RETAINED_OC").toString());
					res.setPremiumSurplusDc(tempMap.get("PREMIUM_SURPLUS_DC")==null?"":tempMap.get("PREMIUM_SURPLUS_DC").toString());
					res.setPremiumSurplusOc(tempMap.get("PREMIUM_SURPLUS_OC")==null?"":tempMap.get("PREMIUM_SURPLUS_OC").toString());
					res.setPremiumTranNo(tempMap.get("PREMIUM_TRAN_NO")==null?"":tempMap.get("PREMIUM_TRAN_NO").toString());
					res.setPremiumUgx(tempMap.get("PREMIUMUGX")==null?"":tempMap.get("PREMIUMUGX").toString());
					res.setProfitCommissionDc(tempMap.get("PROFIT_COMMISSION_DC")==null?"":tempMap.get("PROFIT_COMMISSION_DC").toString());
					res.setProfitcommissionoc(tempMap.get("PROFITCOMMISSIONOC")==null?"":tempMap.get("PROFITCOMMISSIONOC").toString());
					res.setRecPremiumDc(tempMap.get("REC_PREMIUM_DC")==null?"":tempMap.get("REC_PREMIUM_DC").toString());
					res.setRecPremiumOc(tempMap.get("REC_PREMIUM_OC")==null?"":tempMap.get("REC_PREMIUM_OC").toString());
					res.setRetrobroker(tempMap.get("RETROBROKER")==null?"":tempMap.get("RETROBROKER").toString());
					res.setRetroCeding(tempMap.get("RETRO_CEDING")==null?"":tempMap.get("RETRO_CEDING").toString());
					res.setRetroContractNo(tempMap.get("RETRO_CONTRACT_NO")==null?"":tempMap.get("RETRO_CONTRACT_NO").toString());
					res.setRetroExpiry(tempMap.get("RETROEXPIRY")==null?"":tempMap.get("RETROEXPIRY").toString());
					res.setRetroInception(tempMap.get("RETROINCEPTION")==null?"":tempMap.get("RETROINCEPTION").toString());
					res.setRetroLayerNo(tempMap.get("RETRO_LAYER_NO")==null?"":tempMap.get("RETRO_LAYER_NO").toString());
					res.setRetroPercentage(tempMap.get("RETRO_PERCENTAGE")==null?"":tempMap.get("RETRO_PERCENTAGE").toString());
					res.setRetroUwy(tempMap.get("RETROUWY")==null?"":tempMap.get("RETROUWY").toString());
					res.setShortName(tempMap.get("SHORT_NAME")==null?"":tempMap.get("SHORT_NAME").toString());
					res.setSubclass(tempMap.get("SUBCLASS")==null?"":tempMap.get("SUBCLASS").toString());
					res.setTaxAmtDc(tempMap.get("TAX_AMT_DC")==null?"":tempMap.get("TAX_AMT_DC").toString());
					res.setTaxAmtOc(tempMap.get("TAX_AMT_OC")==null?"":tempMap.get("TAX_AMT_OC").toString());
					res.setTotalCrDc(tempMap.get("TOTAL_CR_DC")==null?"":tempMap.get("TOTAL_CR_DC").toString());
					res.setTotalCrOc(tempMap.get("TOTAL_CR_OC")==null?"":tempMap.get("TOTAL_CR_OC").toString());
					res.setTransactionDate(tempMap.get("TRANSACTION_DATE")==null?"":tempMap.get("TRANSACTION_DATE").toString());
					res.setTransactiontype(tempMap.get("TRANSACTIONTYPE")==null?"":tempMap.get("TRANSACTIONTYPE").toString());
					res.setXlCostDc(tempMap.get("XL_COST_DC")==null?"":tempMap.get("XL_COST_DC").toString());					
					res.setXlCostOc(tempMap.get("XL_COST_OC")==null?"":tempMap.get("XL_COST_OC").toString());
					resList.add(res);
				}}
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
		public GetInwardRetroMappingReportRes getInwardRetroMappingReport(GetInwardRetroMappingReportReq bean) {
			GetInwardRetroMappingReportRes response = new GetInwardRetroMappingReportRes();
			List<GetInwardRetroMappingReportRes1> resList = new ArrayList<GetInwardRetroMappingReportRes1>();
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			String query="";
			String qutext = "";
			String[] obj= new String[0];
			try {
			if("1".equalsIgnoreCase(bean.getProductId())){
				query= "reportdao.select.getFacRetroList";
				qutext = prop.getProperty(query);
				obj=new String[3];
				obj[0]=bean.getStartDate();
				obj[1]=bean.getEndDate();
				obj[2]=bean.getBranchCode();
				if(!"-1".equals(bean.getLoginId())){
					obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacLoginId");
				}
				if(!"-1".equals(bean.getBrokerId())){
					obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacBrokerId");
				}
				if(!"-1".equals(bean.getCedingId())){
					obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacCedingId");
				}
				if(!"-1".equals(bean.getUwYear())){
					obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
					qutext += " " +prop.getProperty("report.select.policyRegFacUWYear");
				}
				qutext += " " +prop.getProperty("reportdao.select.getFacRetroOrderBy");
			}else {
				query= "reportdao.select.getTreatyXOLRetroList";
				qutext = prop.getProperty(query);
				obj=new String[4];
				obj[0]=bean.getProductId();
				obj[1]=bean.getStartDate();
				obj[2]=bean.getEndDate();
				obj[3]=bean.getBranchCode();
				if(!"-1".equals(bean.getLoginId())){
					obj=getIncObjectArray(obj,new String[]{bean.getLoginId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacLoginId");
				}
				if(!"-1".equals(bean.getBrokerId())){
					obj=getIncObjectArray(obj,new String[]{bean.getBrokerId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacBrokerId");
				}
				if(!"-1".equals(bean.getCedingId())){
					obj=getIncObjectArray(obj,new String[]{bean.getCedingId()});
					qutext += " " +prop.getProperty("report.select.policyRegFacCedingId");

				}
				if(!"-1".equals(bean.getUwYear())){
					obj=getIncObjectArray(obj,new String[]{bean.getUwYear()});
					qutext += " " +prop.getProperty("report.select.policyRegFacUWYear");
				}
				qutext += " " +prop.getProperty("reportdao.select.getFacRetroOrderBy");
			}
			query1 =queryImpl.setQueryProp(qutext, obj);
			query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
			try {
				list = query1.getResultList();
			} catch(Exception e) {
				e.printStackTrace();
			} 
			if(list!=null && list.size()>0){
				for(Map<String, Object> tempMap : list) {
					GetInwardRetroMappingReportRes1 res = new GetInwardRetroMappingReportRes1();
					res.setAccountDate(tempMap.get("ACCOUNT_DATE")==null?"":tempMap.get("ACCOUNT_DATE").toString());
					res.setBrokerId(tempMap.get("BROKER_ID")==null?"":tempMap.get("BROKER_ID").toString());
					res.setCedingCompanyId(tempMap.get("CEDING_COMPANY_ID")==null?"":tempMap.get("CEDING_COMPANY_ID").toString());
					res.setContractNo(tempMap.get("CONTRACT_NO")==null?"":tempMap.get("CONTRACT_NO").toString());
					res.setDeptId(tempMap.get("DEPT_ID")==null?"":tempMap.get("DEPT_ID").toString());
					res.setLayerNo(tempMap.get("LAYER_NO")==null?"":tempMap.get("LAYER_NO").toString());
					res.setLoginId(tempMap.get("LOGIN_ID")==null?"":tempMap.get("LOGIN_ID").toString());
					res.setNet(tempMap.get("NET")==null?"":tempMap.get("NET").toString());
					res.setOverRider(tempMap.get("OVER_RIDER")==null?"":tempMap.get("OVER_RIDER").toString());
					res.setOverRiderPer(tempMap.get("OVER_RIDER_PER")==null?"":tempMap.get("OVER_RIDER_PER").toString());
					res.setOwiCommission(tempMap.get("OWI_COMMISSION")==null?"":tempMap.get("OWI_COMMISSION").toString());
					res.setOwiCommissionQs(tempMap.get("OWI_COMMISSION_QS")==null?"":tempMap.get("OWI_COMMISSION_QS").toString());
					res.setOwiCommissionSurplus(tempMap.get("OWI_COMMISSION_SURPLUS")==null?"":tempMap.get("OWI_COMMISSION_SURPLUS").toString());
					res.setOwiOtherCharges(tempMap.get("OWI_OTHER_CHARGES")==null?"":tempMap.get("OWI_OTHER_CHARGES").toString());
					res.setOwiPremiumUsd(tempMap.get("OWI_PREMIUM_USD")==null?"":tempMap.get("OWI_PREMIUM_USD").toString());
					res.setProductId(tempMap.get("PRODUCT_ID")==null?"":tempMap.get("PRODUCT_ID").toString());
					res.setRetroContractNo(tempMap.get("RETRO_CONTRACT_NO")==null?"":tempMap.get("RETRO_CONTRACT_NO").toString());
					res.setRetroLayerNo(tempMap.get("RETRO_LAYER_NO")==null?"":tempMap.get("RETRO_LAYER_NO").toString());
					res.setRetroPercentage(tempMap.get("RETRO_PERCENTAGE")==null?"":tempMap.get("RETRO_PERCENTAGE").toString());
					res.setUwYear(tempMap.get("UW_YEAR")==null?"":tempMap.get("UW_YEAR").toString());
					resList.add(res);
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
		public ReportsCommonRes getRetroInwardMappingReport(GetRetroInwardMappingReportReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			try {
				String[] obj = new String[3];
				String query = "";
				obj[0] = bean.getStartDate();
				obj[1] = bean.getEndDate();
				obj[2] = bean.getBranchCode();
				query = "GET_RETRO_RDS_REPORT";
				list = queryImpl.selectList(query, obj);
				response.setCommonResponse(list);
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
		public ReportsCommonRes getTransactionMasterReport(GetTransactionMasterReportReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			String query="";
			String[] obj= new String[0];
			try {
			query= "reportdao.select.getTransMasterList";
			obj=new String[8];
			obj[0]=bean.getStartDate();
			obj[1]=bean.getEndDate();
			obj[2]=bean.getBranchCode();
			obj[3]=bean.getDocType().replaceAll("\\s+","");
			obj[4]=bean.getBrokerId();
			obj[5]=bean.getCedingId();
			obj[6]=bean.getUwYear();
			obj[7]=bean.getContractNo();
			List<Map<String,Object>> list= queryImpl.selectList(query, obj);
			response.setCommonResponse(list);
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
		public ReportsCommonRes getDebtorsAgeingReport(GetDebtorsAgeingReportReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			String query = "";
			String[] obj = new String[19];
			try {
				query = "reportdao.select.getAgeingList";
				if("1".equalsIgnoreCase(bean.getType())){
					query+=" ORDER BY CURRENCY, BROKER_CODE,COMPANY_CODE,TRANSACTION_DATE,TRANSACTION_NO";
				}else if("2".equalsIgnoreCase(bean.getType())){
					query+=" ORDER BY UPPER(CLIENT_ID), CURRENCY";
				}
				obj[0] = bean.getType();
				obj[1] = bean.getStartDate();
				obj[2] = bean.getDebFrom();
				obj[3] = bean.getDebTo();
				obj[4] = bean.getDebFrom1();
				obj[5] = bean.getDebTo1();
				obj[6] = bean.getDebFrom2();
				obj[7] = bean.getDebTo2();
				obj[8] = bean.getDebFrom3();
				obj[9] = bean.getDebTo3();
				obj[10] = bean.getDebFrom4();
				obj[11] = bean.getDebTo4();
				obj[12] = bean.getDebFrom5();
				obj[13] = bean.getDebTo5();
				obj[14] = bean.getBranchCode();
				obj[15] = bean.getProductId();
				obj[16] = bean.getBrokerId();
				obj[17] = bean.getCedingId();
				obj[18] = bean.getDocType();
				List<Map<String,Object>> list= queryImpl.selectList(query, obj);
				response.setCommonResponse(list);
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
		public GetMoveMntSummaryRes getMoveMntSummary(GetMoveMntSummaryReq bean) {
			GetMoveMntSummaryRes response = new GetMoveMntSummaryRes();
			List<GetMoveMntSummaryRes1> resList = new ArrayList<GetMoveMntSummaryRes1>();
			String query="";
			String[] obj= new String[3];
			try{
				query= "reportdao.select.getMovMntSummaryList";
				obj[0]=bean.getBranchCode();
				obj[1]=bean.getStartDate();
				obj[2]=bean.getEndDate();
				List<Map<String,Object>> list= queryImpl.selectList(query, obj);
				if(list!=null && list.size()>0){
					for(Map<String, Object> tempMap : list) {
						GetMoveMntSummaryRes1 res = new GetMoveMntSummaryRes1();
						res.setRownum(tempMap.get("ROWNUM")==null?"":tempMap.get("ROWNUM").toString());
						res.setRskUwyear(tempMap.get("RSK_UWYEAR")==null?"":tempMap.get("RSK_UWYEAR").toString());
						res.setSumoftotprDc(tempMap.get("SUMOFTOTPR_DC")==null?"":tempMap.get("SUMOFTOTPR_DC").toString());
						res.setTmasSpfcName(tempMap.get("TMAS_SPFC_NAME")==null?"":tempMap.get("TMAS_SPFC_NAME").toString());	
						resList.add(res);
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
		public ReportsCommonRes getallocationReportList(GetallocationReportListReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			try{
				String[] args=new String[7];
				args[0]=bean.getStartDate();
				args[1]=bean.getEndDate();
				args[2]=bean.getSettlementType();
				args[3]=bean.getBrokerId();
				args[4]=bean.getCedingId();
				args[5]=bean.getAllocateStatus();
				args[6]=bean.getBranchCode();
				if("-1".equals(bean.getBrokerId())){
					args[3]="ALL";
				}
				if("-1".equals(bean.getCedingId())){
					args[4]="ALL";
				}
				List<Map<String,Object>> list= queryImpl.selectList("allocation.report.list", args);
				response.setCommonResponse(list);
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
		public ReportsCommonRes getPayRecRegisterList(GetPayRecRegisterListReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			try{
				String[] args=new String[8];
				args[0]=bean.getBranchCode();
				args[1]=bean.getStartDate();
				args[2]=bean.getEndDate();
				if("-1".equals(bean.getCedingId())){
					args[3]="ALL";
				}
				if("-1".equals(bean.getBrokerId())){
					args[4]="ALL";
				}
				args[5]=bean.getPayrecType();
				args[6]=bean.getTransactionType();
				args[7]=bean.getShowAllFields();
			
				List<Map<String,Object>> list= queryImpl.selectList("payrec.register.list", args);
				response.setCommonResponse(list);
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
		public ReportsCommonRes getClaimPaidRegisterList(GetClaimPaidRegisterListReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			try{
				String[] obj=new String[8];
				obj[0]=bean.getProductId();
				obj[1]=bean.getBranchCode();
				if("-1".equals(bean.getLoginId())){
					obj[2]="ALL";
				}else{
					obj[2]=	bean.getLoginId();
				}
				if("-1".equals(bean.getBrokerId())){
					obj[3]="ALL";
				}else{
					obj[3]=	bean.getBrokerId();
				}
				if("-1".equals(bean.getCedingId())){
					obj[4]="ALL";
				}else{
					obj[4]=	bean.getCedingId();
				}
				if("-1".equals(bean.getUwYear())){
					obj[5]="ALL";
				}else{
					obj[5]=	bean.getUwYear();
				}
				obj[6]=bean.getStartDate();
				obj[7]=bean.getEndDate();
			
				List<Map<String,Object>> list = queryImpl.selectList("claim.paid.report", obj);
				response.setCommonResponse(list);
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
		public ReportsCommonRes getRetroRegisterList(GetRetroRegisterListReq bean) {
			ReportsCommonRes response = new ReportsCommonRes();
			try{
				String[] obj=new String[12];
				obj[0]=bean.getStartDate();
				obj[1]=bean.getEndDate();
				obj[2]=bean.getBranchCode();
				if("-1".equals(bean.getBrokerId())){
					obj[3]="ALL";
					obj[4]="ALL";
					obj[5]="ALL";
				}else{
					obj[3]=	bean.getBrokerId();
					obj[4]=	bean.getBrokerId();
					obj[5]=	bean.getBrokerId();
				}
				if("-1".equals(bean.getCedingId())){
					obj[6]="ALL";
					obj[7]="ALL";
					obj[8]="ALL";
				}else{
					obj[6]=	bean.getCedingId();
					obj[7]=	bean.getCedingId();
					obj[8]=	bean.getCedingId();
				}
				if("-1".equals(bean.getUwYear())){
					obj[9]="ALL";
					obj[10]="ALL";
					obj[11]="ALL";
				}else{
					obj[9]=	bean.getUwYear();
					obj[10]=bean.getUwYear();
					obj[11]=bean.getUwYear();
				}
				List<Map<String,Object>> list = queryImpl.selectList("retro.report.list", obj);
				
				response.setCommonResponse(list);
				response.setMessage("Success");
					response.setIsError(false);
					} catch (Exception e) {
					e.printStackTrace();
					response.setMessage("Failed");
					response.setIsError(true);
				}
				return response;
		}
}
