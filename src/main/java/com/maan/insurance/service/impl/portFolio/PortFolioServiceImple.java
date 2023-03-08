package com.maan.insurance.service.impl.portFolio;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
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

import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TtrnRiPlacement;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRskClassLimits;
import com.maan.insurance.model.entity.UnderwritterMaster;
import com.maan.insurance.model.repository.RskPremiumDetailsRepository;
import com.maan.insurance.model.repository.TmasDepartmentMasterRepository;
import com.maan.insurance.model.repository.TtrnClaimDetailsRepository;
import com.maan.insurance.model.repository.TtrnRiskDetailsRepository;
import com.maan.insurance.model.repository.UnderwritterMasterRepository;
import com.maan.insurance.model.req.portFolio.GetAutoPendingListReq;
import com.maan.insurance.model.req.portFolio.GetConfirmedListReq;
import com.maan.insurance.model.req.portFolio.GetContractsListReq;
import com.maan.insurance.model.req.portFolio.GetHistoryListReq;
import com.maan.insurance.model.req.portFolio.GetPendingListReq;
import com.maan.insurance.model.req.portFolio.PortfolioListReq;
import com.maan.insurance.model.req.portFolio.ProcAutoReq;
import com.maan.insurance.model.res.DropDown.GetCommonValueRes;
import com.maan.insurance.model.res.portFolio.ButtonSelectionListRes;
import com.maan.insurance.model.res.portFolio.GetAutoPendingListRes;
import com.maan.insurance.model.res.portFolio.GetAutoPendingListRes1;
import com.maan.insurance.model.res.portFolio.GetAutoPendingListResponse;
import com.maan.insurance.model.res.portFolio.GetConfirmedListRes;
import com.maan.insurance.model.res.portFolio.GetConfirmedListRes1;
import com.maan.insurance.model.res.portFolio.GetConfirmedListResponse;
import com.maan.insurance.model.res.portFolio.GetContractsListRes;
import com.maan.insurance.model.res.portFolio.GetContractsListRes1;
import com.maan.insurance.model.res.portFolio.GetContractsListResponse;
import com.maan.insurance.model.res.portFolio.GetHistoryListRes;
import com.maan.insurance.model.res.portFolio.GetHistoryListRes1;
import com.maan.insurance.model.res.portFolio.GetPendingListRes;
import com.maan.insurance.model.res.portFolio.GetPendingListRes1;
import com.maan.insurance.model.res.portFolio.GetPendingListResponse;
import com.maan.insurance.model.res.portFolio.GetRejectedListRes;
import com.maan.insurance.model.res.portFolio.GetRejectedListRes1;
import com.maan.insurance.model.res.portFolio.GetRejectedListResponse;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.portFolio.PortFolioService;
import com.maan.insurance.validation.Formatters;
@Service
public class PortFolioServiceImple implements PortFolioService{
	private Logger log=LogManager.getLogger(PortFolioServiceImple.class);
	@Autowired
	private QueryImplemention queryImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;

	@Autowired
	private UnderwritterMasterRepository uwRepo;

	@Autowired
	private TtrnClaimDetailsRepository cdRepo;
	
	@Autowired
	private RskPremiumDetailsRepository pdRepo;
	
	@Autowired
	private TmasDepartmentMasterRepository dmRepo;
	
	@Autowired
	private TtrnRiskDetailsRepository rdRepo;
	
	private Properties prop = new Properties();
	private Query query1=null;
	@PersistenceContext
	private EntityManager em;
	
	  public PortFolioServiceImple() {
	        try {
	        	InputStream  inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
	        	if (inputStream != null) {
					prop.load(inputStream);
				}
	        	
	        } catch (Exception e) {
	            log.info(e);
	        }
	    }
	  
	  private String Dateformat(Object output) {
		  return new SimpleDateFormat("dd/MM/yyyy").format(output).toString();
	  }

	@Override
	public GetPendingListRes getPendingList(GetPendingListReq beanObj) {
		GetPendingListRes response = new GetPendingListRes();
		GetPendingListResponse res1 = new GetPendingListResponse();
        try {
            String query = "";
            String[] obj = new String[4];
            obj[0] = beanObj.getProductId();
            obj[1] = beanObj.getBranchCode();
            obj[2] = beanObj.getBranchCode();
            obj[3] = beanObj.getProductId();
            query = "portfolio.select.ntu"; //left outer join
            String qutext = prop.getProperty(query);
            if (beanObj.getFlag() != null && "N".equalsIgnoreCase(beanObj.getFlag())) {
            	qutext += " " +prop.getProperty("portfolio.select.flagn");
            } else if("RP".equalsIgnoreCase(beanObj.getFlag())){
            	qutext += " a.CONTRACT_STATUS IS NULL OR a.CONTRACT_STATUS IN ('P','N') " ;
            }else if(!"RP".equalsIgnoreCase(beanObj.getFlag())){
            	qutext += " " +prop.getProperty("portfolio.select.flagnull");
            }
            if ("1".equals(beanObj.getProductId())) {
                obj = dropDowmImpl.getIncObjectArray(obj, new String[]{beanObj.getBranchCode(), beanObj.getBranchCode(), beanObj.getDeptId(), beanObj.getBranchCode()});
                qutext += " " +prop.getProperty("portfolio.select.ntudept");
            } else {
                obj = dropDowmImpl.getIncObjectArray(obj, new String[]{beanObj.getBranchCode(), beanObj.getBranchCode(), beanObj.getBranchCode()});
                qutext += " " +prop.getProperty("portfolio.select.ntuend");
            }
            if(StringUtils.isNotBlank(beanObj.getSearchType()) && null !=beanObj.getSearchType()){
            	if(StringUtils.isNotBlank(beanObj.getProposalNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getProposalNoSearch() + "%")});
            		 qutext += " " +prop.getProperty("portfolio.select.perno");
            	}
            	if(StringUtils.isNotBlank(beanObj.getContractNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getContractNoSearch() + "%")});
            		qutext += " " +prop.getProperty("portfolio.select.conno");
            	}
        		if(StringUtils.isNotBlank(beanObj.getCompanyNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getCompanyNameSearch() + "%")});
            		qutext += " " +prop.getProperty("portfolio.select.comname");
            	}
        		if(StringUtils.isNotBlank(beanObj.getBrokerNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getBrokerNameSearch() + "%")});
            		qutext += " " +prop.getProperty("portfolio.select.broname");
            	}
        		if(StringUtils.isNotBlank(beanObj.getDepartmentNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getDepartmentNameSearch() + "%")});
            		 qutext += " AND UPPER(B.TMAS_DEPARTMENT_NAME) LIKE UPPER(?) ";
            	}
        		if(StringUtils.isNotBlank(beanObj.getBouquetNoSearch())){ //ri
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getBouquetNoSearch() + "%")});
            		qutext += " AND UPPER(A.Bouquet_No) LIKE UPPER(?)";
            	}
        		if(StringUtils.isNotBlank(beanObj.getSubclassSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getSubclassSearch() + "%")});
            		qutext += " AND UPPER((select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',')  from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in(select * from table(SPLIT_TEXT_FN(replace(E.RSK_SPFCID,' ', '')))) AND  SPFC.TMAS_PRODUCT_ID = E.RSK_PRODUCTID AND SPFC.BRANCH_CODE = E.BRANCH_CODE)) LIKE UPPER(?)";
            	}
            	if("1".equalsIgnoreCase(beanObj.getProductId())){
        		if(StringUtils.isNotBlank(beanObj.getInsuredNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getInsuredNameSearch() + "%")});
            		 qutext += " AND UPPER(E.RSK_INSURED_NAME) LIKE UPPER(?)";
            	}
            	if(StringUtils.isNotBlank(beanObj.getUwYearSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getUwYearSearch() + "%")});
            		qutext += " " +prop.getProperty("portfolio.select.uyear");
            	}
            	String res ="";
            	if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch())){
            		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch());
           		 if (!CollectionUtils.isEmpty(list)) {
           			 res =  list.get(0).getUwrCode();
           			}
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + res + "%")});
            		 qutext += " AND UPPER(E.RSK_UNDERWRITTER) LIKE UPPER(?) ";
            	}
            	}else{
            		if(StringUtils.isNotBlank(beanObj.getUwYearSearch1())){
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getUwYearSearch1() + "%")});
                		qutext += " " +prop.getProperty("portfolio.select.uyear");
                	}if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch1())){
                		String res ="";
//                		String qry ="portfolio.select_uwr_code";
//                		String value = "%"+beanObj.getUnderwriterSearch1()+"%";
                		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch1());
                  		 if (!CollectionUtils.isEmpty(list)) {
                  			 res =  list.get(0).getUwrCode();
                  			}
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + res + "%")});
                		 qutext += " AND UPPER(E.RSK_UNDERWRITTER) LIKE UPPER(?) ";
                	}
            	} if(StringUtils.isNotBlank(beanObj.getOfferNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getOfferNoSearch() + "%")});
            		qutext += " AND A.OFFER_NO LIKE ? ";
            		
            	}
            	
            	res1.setType("Yes");
            }else{
            	beanObj.setProposalNoSearch("");
            	beanObj.setContractNoSearch("");
            	beanObj.setCompanyNameSearch("");
            	beanObj.setBrokerNameSearch("");
            	beanObj.setDepartmentNameSearch("");
            	beanObj.setInsuredNameSearch("");
            	beanObj.setUwYearSearch("");
            	beanObj.setUnderwriterSearch("");
            	beanObj.setUwYearSearch1("");
            	beanObj.setUnderwriterSearch1("");
            	beanObj.setOfferNoSearch("");
            }
            if ("RP".equalsIgnoreCase(beanObj.getFlag())) {
            //	qutext += " " +prop.getProperty("portfolio.select.renewalPending");
            } else {
            //	qutext += " " +prop.getProperty("portfolio.select.pending");
            }
            if(StringUtils.isNotBlank(beanObj.getAttachedUW()) && !"ALL".equalsIgnoreCase(beanObj.getAttachedUW())) {
        		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{(beanObj.getAttachedUW())});
        		 qutext += " AND UPPER(E.RSK_UNDERWRITTER) IN (select * from table(SPLIT_TEXT_FN(?)))";
        	}
            qutext += " " +prop.getProperty("portfolio.select.orderbyprono");
            List<Map<String, Object>> list = new ArrayList<>();
        	query1 =queryImpl.setQueryProp(qutext, obj);
    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
    		try {
    			 list = query1.getResultList();
    		} catch(Exception e) {
    			e.printStackTrace();
    		} 
            List<GetPendingListRes1> finalList =new ArrayList<GetPendingListRes1>();
            if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> tempMap = list.get(i);
                GetPendingListRes1 tempBean = new GetPendingListRes1();
                tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString());
                tempBean.setOfferNo(tempMap.get("OFFER_NO") == null ? "" : tempMap.get("OFFER_NO").toString());
                tempBean.setBouquetNo(tempMap.get("BOUQUET_NO") == null ? "" : tempMap.get("BOUQUET_NO").toString());
                tempBean.setAmendId(tempMap.get("AMEND_ID") == null ? "" : tempMap.get("AMEND_ID").toString());
                tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME") == null ? "" : tempMap.get("COMPANY_NAME").toString());
                tempBean.setDepartmentName(tempMap.get("TMAS_DEPARTMENT_NAME") == null ? "" : tempMap.get("TMAS_DEPARTMENT_NAME").toString());
                tempBean.setSubClass(tempMap.get("TMAS_SPFC_NAME") == null ? "" : tempMap.get("TMAS_SPFC_NAME").toString());
                tempBean.setDepartmentId(tempMap.get("TMAS_DEPARTMENT_ID") == null ? "" : tempMap.get("TMAS_DEPARTMENT_ID").toString());
                tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE") == null ? "" : tempMap.get("INCEPTION_DATE").toString());
                tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE") == null ? "" : tempMap.get("EXPIRY_DATE").toString());
                tempBean.setInsuredName(tempMap.get("RSK_INSURED_NAME") == null ? "" : tempMap.get("RSK_INSURED_NAME").toString());
                tempBean.setQuoteGendrateddate(tempMap.get("ACCOUNT_DATE") == null ? "" : tempMap.get("ACCOUNT_DATE").toString());
                tempBean.setCeddingcompanyid(tempMap.get("CEDING_COMPANY_ID") == null ? "" : tempMap.get("CEDING_COMPANY_ID").toString());
                tempBean.setLayerNo(tempMap.get("LAYER_NO") == null ? "" : tempMap.get("LAYER_NO").toString());
                if (beanObj.getFlag() != null && "N".equalsIgnoreCase(beanObj.getFlag())) {
                    tempBean.setFlag("N");
                } else if ("RP".equalsIgnoreCase(beanObj.getFlag())) {
                    tempBean.setFlag("RP");
                    tempBean.setTitle("RP");
                   
                } else {
                    tempBean.setFlag("P");
                }
                if (tempMap.get("BASE_LAYER") != null) {
                    tempBean.setBaseLayer(tempMap.get("BASE_LAYER").toString());
                    tempBean.setContractno1(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
                    tempBean.setLay1("layer");
                } else {
                    tempBean.setBaseLayer("");
                }
                tempBean.setUwYear(tempMap.get("UW_YEAR") == null ? "" : tempMap.get("UW_YEAR").toString());
                tempBean.setUwMonth(tempMap.get("UW_MONTH") == null ? "" : tempMap.get("UW_MONTH").toString());
                tempBean.setUnderwritter(tempMap.get("UNDERWRITTER") == null ? "" : tempMap.get("UNDERWRITTER").toString());
                tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString());
                tempBean.setOldContract(tempMap.get("OLD_CONTRACTNO") == null ? "" : "0".equals(tempMap.get("OLD_CONTRACTNO")) == true ? "" : tempMap.get("OLD_CONTRACTNO").toString());
                tempBean.setButtonSelectionList(getPendingButtonList(beanObj.getMenuRights()));
                finalList.add(tempBean);
            }
            res1.setPendingList(finalList);
            response.setCommonResponse(res1);
    		response.setMessage("Success");
    		response.setIsError(false);
        } }catch (Exception e) {
        		e.printStackTrace();
        		response.setMessage("Failed");
        		response.setIsError(true);
    }
    return response;
	}
	 private List<ButtonSelectionListRes> getPendingButtonList( List<String> menuRights) {
		 List<ButtonSelectionListRes> pendingList = new ArrayList<ButtonSelectionListRes>();
	    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			try{
				
				if( menuRights.toString().contains("EN") ){//&&"N".equalsIgnoreCase(tempBean.getEditMode())&&!"".equalsIgnoreCase(tempBean.getEditMode())){
					map.put("TYPE","E");
					map.put("DETAIL_NAME","Edit");
					list.add(map);
					map=new HashMap<String,Object>();
				}
				if( menuRights.toString().contains("V")){
					map.put("TYPE","V");
					map.put("DETAIL_NAME","View");
					list.add(map);
					map=new HashMap<String,Object>();
				}
				if( menuRights.toString().contains("V")){
					map.put("TYPE","RI");
					map.put("DETAIL_NAME","Reinsurer");
					list.add(map);
					map=new HashMap<String,Object>();
				}
				if( menuRights.toString().contains("V")){
					map.put("TYPE","PL");
					map.put("DETAIL_NAME","Placement");
					list.add(map);
					map=new HashMap<String,Object>();
				}
				
    			if(list.size()>0&&list!=null) {
    			for(int i=0; i<list.size(); i++) {
    				Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
    				ButtonSelectionListRes button = new ButtonSelectionListRes();
    				button.setType(tempMap.get("TYPE")==null?"":tempMap.get("TYPE").toString());	
    				button.setDetailName(tempMap.get("DETAIL_NAME")==null?"":tempMap.get("DETAIL_NAME").toString());
    				pendingList.add(button);
    			}
    			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return pendingList;
		}
	
	@Override
	public GetRejectedListRes getRejectedList(GetPendingListReq beanObj) {
		GetRejectedListRes response = new GetRejectedListRes();
        List<GetRejectedListRes1> finalList = new ArrayList<GetRejectedListRes1>();
        GetRejectedListResponse res1 = new GetRejectedListResponse();
        String res ="";
        try {
            String query = "";
            String[] obj = new String[8];
            obj[0] = beanObj.getProductId();
            obj[1] = beanObj.getBranchCode();
            obj[2] = beanObj.getProductId();
            obj[3] = beanObj.getBranchCode();
            obj[4] = beanObj.getBranchCode();
            obj[5] = beanObj.getProductId();
            obj[6] = beanObj.getBranchCode();
            obj[7] = beanObj.getBranchCode();
            query = "portfolio.select.rejected";
            String qutext = prop.getProperty(query);
            if ("1".equals(beanObj.getProductId())) {
                obj = dropDowmImpl.getIncObjectArray(obj, new String[]{beanObj.getDeptId()});
            	qutext += " " +prop.getProperty("portfolio.select.rejecteddept");
            }

            if(StringUtils.isNotBlank(beanObj.getSearchType()) && null !=beanObj.getSearchType()){
            	if(StringUtils.isNotBlank(beanObj.getProposalNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getProposalNoSearch() + "%")});
                    qutext += " " +prop.getProperty("portfolio.select.perno");
            	}
            	if(StringUtils.isNotBlank(beanObj.getContractNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getContractNoSearch() + "%")});
                    qutext += " " +prop.getProperty("portfolio.select.conno");
            	}
        		if(StringUtils.isNotBlank(beanObj.getCompanyNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getCompanyNameSearch() + "%")});
                    qutext += " " +prop.getProperty("portfolio.select.comname");
            	}
        		if(StringUtils.isNotBlank(beanObj.getBrokerNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getBrokerNameSearch() + "%")});
                    qutext += " " +prop.getProperty("portfolio.select.broname");
            	}
        		if(StringUtils.isNotBlank(beanObj.getDepartmentNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getDepartmentNameSearch() + "%")});
            		qutext += " AND UPPER(B.TMAS_DEPARTMENT_NAME) LIKE UPPER(?)";
            	}
            	if("1".equalsIgnoreCase(beanObj.getProductId())){
        		if(StringUtils.isNotBlank(beanObj.getInsuredNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getInsuredNameSearch() + "%")});
            		qutext += " AND UPPER(D.RSK_INSURED_NAME) LIKE UPPER(?)";
            	}
            	if(StringUtils.isNotBlank(beanObj.getUwYearSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getUwYearSearch() + "%")});
                    qutext += " " +prop.getProperty("portfolio.select.uyear");
                	
            	}if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch())){
            		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch());
             		 if (!CollectionUtils.isEmpty(list)) {
             			 res =  list.get(0).getUwrCode();
             			}
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + res + "%")});
            		qutext += " AND UPPER(D.RSK_UNDERWRITTER) LIKE UPPER(?) ";
            	}
            	}else{
            		if(StringUtils.isNotBlank(beanObj.getUwYearSearch1())){
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getUwYearSearch1() + "%")});
                        qutext += " " +prop.getProperty("portfolio.select.uyear");
                	}if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch1())){
                		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch1());
                 		 if (!CollectionUtils.isEmpty(list)) {
                 			 res =  list.get(0).getUwrCode();
                 			}
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + res + "%")});
                		qutext += " AND UPPER(RD.RSK_UNDERWRITTER) LIKE UPPER(?) ";
                	}
            	}
            	
            	res1.setType("Yes");
            }
            else{
            	beanObj.setProposalNoSearch("");
            	beanObj.setContractNoSearch("");
            	beanObj.setCompanyNameSearch1("");
            	beanObj.setBrokerNameSearch1("");
            	beanObj.setDepartmentNameSearch1("");
            	beanObj.setCompanyNameSearch("");
            	beanObj.setBrokerNameSearch("");
            	beanObj.setDepartmentNameSearch("");
            	beanObj.setInsuredNameSearch("");
            	beanObj.setUwYearSearch2("");
            	beanObj.setUnderwriterSearch("");
            	beanObj.setUwYearSearch3("");
            	beanObj.setUnderwriterSearch1("");
            }
            if(StringUtils.isNotBlank(beanObj.getAttachedUW()) && !"ALL".equalsIgnoreCase(beanObj.getAttachedUW())) {
        		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{(beanObj.getAttachedUW())});
        		qutext += " AND UPPER(RD.RSK_UNDERWRITTER) IN (select * from table(SPLIT_TEXT_FN(?)))";
        	}
            qutext += " " +prop.getProperty("portfolio.select.orderbyprono");
            res1.setMode("true");
            
            List<Map<String, Object>> list = new ArrayList<>();
            
        	query1 =queryImpl.setQueryProp(qutext, obj);
    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
    		try {
    			 list = query1.getResultList();
    		} catch(Exception e) {
    			e.printStackTrace();
    		} 
            if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> tempMap = list.get(i);
                GetRejectedListRes1 tempBean = new GetRejectedListRes1();
                tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString());
                tempBean.setAmendId(tempMap.get("AMEND_ID") == null ? "" : tempMap.get("AMEND_ID").toString());
                tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME") == null ? "" : tempMap.get("COMPANY_NAME").toString());
                tempBean.setDepartmentName(tempMap.get("TMAS_DEPARTMENT_NAME") == null ? "" : tempMap.get("TMAS_DEPARTMENT_NAME").toString());
                tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE") == null ? "" : tempMap.get("INCEPTION_DATE").toString());
                tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE") == null ? "" : tempMap.get("EXPIRY_DATE").toString());
                tempBean.setInsuredName(tempMap.get("RSK_INSURED_NAME") == null ? "" : tempMap.get("RSK_INSURED_NAME").toString());
                tempBean.setQuoteGendrateddate(tempMap.get("ACCOUNT_DATE") == null ? "" : tempMap.get("ACCOUNT_DATE").toString());
                tempBean.setCeddingcompanyid(tempMap.get("CEDING_COMPANY_ID") == null ? "" : tempMap.get("CEDING_COMPANY_ID").toString());
                tempBean.setFlag("R");
                if (tempMap.get("BASE_LAYER") != null) {
                    tempBean.setBaseLayer(tempMap.get("BASE_LAYER") == null ? "" : tempMap.get("BASE_LAYER").toString());
                } else {
                    tempBean.setBaseLayer("");
                    tempBean.setUwYear(tempMap.get("UW_YEAR") == null ? "" : tempMap.get("UW_YEAR").toString());
                    tempBean.setUwMonth(tempMap.get("UW_MONTH") == null ? "" : tempMap.get("UW_MONTH").toString());
                    tempBean.setUnderwritter(tempMap.get("UNDERWRITTER") == null ? "" : tempMap.get("UNDERWRITTER").toString());
                    tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString());
                }
                finalList.add(tempBean);
            }
            res1.setPendingList(finalList);
            response.setCommonResponse(res1);
    		response.setMessage("Success");
    		response.setIsError(false);
        } }catch (Exception e) {
        		e.printStackTrace();
        		response.setMessage("Failed");
        		response.setIsError(true);
    }
    return response;
    }

	@Override
	public GetAutoPendingListRes getAutoPendingList(GetAutoPendingListReq beanObj) {
		GetAutoPendingListRes response = new GetAutoPendingListRes();
		GetAutoPendingListResponse res1 = new GetAutoPendingListResponse();
        String query= "";
        String res = "";
        try {
        int mode = parseNumber(beanObj.getProductId()).intValue();
        if (mode == 1) {
            query = "portfolio.select.autoPending1";
        } else if (mode == 3) {
            query = "portfolio.select.autoPendingNPT";
        }
//        else {
//            return new ArrayList<GetAutoPendingListRes1>();
//        }
        String[] args = new String [14];
		args[0] = beanObj.getBranchCode();
		args[1] = beanObj.getBranchCode();
		args[2] = beanObj.getBranchCode();
		args[3] = beanObj.getBranchCode();
		args[4] = beanObj.getBranchCode();
		args[5] = beanObj.getBranchCode();
		args[6] = beanObj.getBranchCode();
		args[7] = beanObj.getBranchCode();
		args[8] = beanObj.getBranchCode();
		args[9] = beanObj.getBranchCode();
		args[10] = beanObj.getBranchCode();
		args[11] = beanObj.getDueDate();
		args[12] = beanObj.getProductId();
		args[13] = beanObj.getBranchCode();
        List<GetAutoPendingListRes1> portfolioBeans = new ArrayList<GetAutoPendingListRes1>();
        List<Map<String, Object>> list = queryImpl.selectList(query, args);
        for (Map<String, Object> tempMap : list) {
        	GetAutoPendingListRes1 tempBean = new GetAutoPendingListRes1();
            if(!"N".equalsIgnoreCase(tempMap.get("EDIT_MODE").toString())){
            	portfolioBeans = new ArrayList<GetAutoPendingListRes1>();
            	res1.setMode("error");
            	break;
            }else{
            tempBean.setProposalNo(emptyIfNull(tempMap.get("RSK_PROPOSAL_NUMBER")));
            tempBean.setContractno1(emptyIfNull(tempMap.get("RSK_CONTRACT_NO")));
            tempBean.setContractNo(emptyIfNull(tempMap.get("RSK_CONTRACT_NO")));
            tempBean.setAmendId(emptyIfNull(tempMap.get("RSK_ENDORSEMENT_NO")));
            tempBean.setCedingCompanyName(emptyIfNull(tempMap.get("COMPANY")));
            tempBean.setCeddingcompanyid(emptyIfNull(tempMap.get("CEDING_ID")));
            tempBean.setBrokerName(emptyIfNull(tempMap.get("BROKER")));
            tempBean.setBrokerId(emptyIfNull(tempMap.get("BROKER_ID")));
            tempBean.setDepartmentId(emptyIfNull(tempMap.get("TMAS_DEPARTMENT_ID")));
            tempBean.setDepartmentName(emptyIfNull(tempMap.get("TMAS_DEPARTMENT_NAME")));
            tempBean.setPremiumClass(emptyIfNull(tempMap.get("RSK_DEPTID")));
            tempBean.setPremiumSubClass(emptyIfNull(tempMap.get("RSK_SPFCID")));
            tempBean.setSubClass(emptyIfNull(tempMap.get("TMAS_SPFC_NAME")));
            tempBean.setCurrencyShort(emptyIfNull(tempMap.get("SHORT_NAME")));
            tempBean.setCurrencyId(emptyIfNull(tempMap.get("RSK_ORIGINAL_CURR")));
            tempBean.setInceptionDate(emptyIfNull(tempMap.get("INSDATE")));
            tempBean.setExpiryDate(emptyIfNull(tempMap.get("EXPDATE")));
            tempBean.setInsuredName(emptyIfNull(tempMap.get("RSK_INSURED_NAME")));
            tempBean.setExchangeRate(emptyIfNull(tempMap.get("RSK_EXCHANGE_RATE")));
            tempBean.setShareSign(makeDoubleString(emptyIfNull(tempMap.get("SHARE_SIGNED")), 4));
            tempBean.setInstallmentDate(emptyIfNull(tempMap.get("INSTALLMENT_DATE")));
            tempBean.setInstallmentNo(emptyIfNull(tempMap.get("INSTALLMENT_NO")));
            tempBean.setPremiumOC(emptyIfNull(tempMap.get("MND_PREMIUM_OC")));
            tempBean.setCommission(emptyIfNull(tempMap.get("RSK_COMM")));
            tempBean.setBrokerage(emptyIfNull(tempMap.get("RSK_BROKERAGE")));
            tempBean.setTax(emptyIfNull(tempMap.get("RSK_TAX")));
            tempBean.setOtherCost(emptyIfNull(tempMap.get("RSK_OTHER_COST")).trim());
            tempBean.setTreatyName(emptyIfNull(tempMap.get("TREATY")));
            tempBean.setLayerNo(emptyIfNull(tempMap.get("LAYER_NO")));
            tempBean.setAcceptenceDate(emptyIfNull(tempMap.get("ACDATE")));
            if (beanObj.getFlag() != null && "N".equalsIgnoreCase(beanObj.getFlag())) {
                tempBean.setFlag("N");
            } else if ("RP".equalsIgnoreCase(beanObj.getFlag())) {
                tempBean.setFlag("RP");
            } else {
                tempBean.setFlag("P");
            }
               
            if (tempMap.get("BASE_LAYER") != null) {
                tempBean.setBaseLayer(tempMap.get("BASE_LAYER").toString());
                tempBean.setLay1("layer");
            } else {
                tempBean.setBaseLayer("");
            }
            tempBean.setUwYear(emptyIfNull(tempMap.get("UW_YEAR")));
            tempBean.setUwMonth(emptyIfNull(tempMap.get("UW_MONTH")));
            tempBean.setUnderwritter(emptyIfNull(tempMap.get("UNDERWRITTER")));
            tempBean.setOldContract(tempMap.get("OLD_CONTRACTNO") == null ? "" : "0".equals(tempMap.get("OLD_CONTRACTNO")) ? "" : tempMap.get("OLD_CONTRACTNO").toString());
            portfolioBeans.add(tempBean);
            }
        }
        res1.setAutoPendingList(portfolioBeans);
        response.setCommonResponse(res1);
		response.setMessage("Success");
		response.setIsError(false);
     }catch (Exception e) {
    		e.printStackTrace();
    		response.setMessage("Failed");
    		response.setIsError(true);
     	}
        return response;
	}
	 private String emptyIfNull(Object str) {
	        return (str == null || str.toString().isEmpty()) ? "" : str.toString().trim();
	    }
	 private Double parseNumber(String dirtyNumber) {
	        if (dirtyNumber == null || dirtyNumber.isEmpty()) return 0d;
	        return Double.parseDouble(dirtyNumber.replaceAll(",", "").trim());
	    }
	 public static String makeDoubleString(String numeric, int i) {
	        try {
	            Double n = Double.parseDouble(numeric.trim().replaceAll(",",""));
	            StringBuilder sb = new StringBuilder();
	            int c = i;
	            while (c-->0) {
	                sb.append("0");
	            }
	            DecimalFormat df = new DecimalFormat("##0." + sb.toString());
	            return df.format(n);
	        } catch (NumberFormatException e) {
	            e.printStackTrace();
	        }
	        return "0";
	    }

	@Override
	public GetContractsListRes getContractsList(GetContractsListReq beanObj) {
		GetContractsListRes response = new GetContractsListRes();
        List<GetContractsListRes1> finalList = new ArrayList<GetContractsListRes1>();
        GetContractsListResponse res1 = new GetContractsListResponse();
        String res = "";
        try {
            String query = "";
            String[] obj = new String[0];
            obj = new String[8];
            obj[0] = beanObj.getProductId();
            obj[1] = beanObj.getBranchCode();
            obj[2] = beanObj.getBranchCode();
            obj[3] = beanObj.getBranchCode();
            obj[4] = beanObj.getProductId();
            obj[5] = beanObj.getBranchCode();
            obj[6] = beanObj.getProductId();
            obj[7] = beanObj.getBranchCode();
            query = "portfolio.select.contractList1";
            String qutext = prop.getProperty(query);
            if (!"1".equals(beanObj.getProductId()))
            	qutext += " " +prop.getProperty("portfolio.select.contractListLayerNo1");
            qutext += " " +prop.getProperty("portfolio.select.contractList2"); //  UNDERWRITTER_MASTER E //RSK_PRODUCTID missing
            if (!"1".equals(beanObj.getProductId()))
            	qutext += " " +prop.getProperty("portfolio.select.contractListLayerNo2");
                qutext += " " +prop.getProperty("portfolio.select.contractList3");
            if (!"1".equals(beanObj.getProductId()))
            	qutext += " " +prop.getProperty("portfolio.select.contractListLayerNo3");
                qutext += " " +prop.getProperty("portfolio.select.contractList4");
            if ("1".equals(beanObj.getProductId())) {
                obj = dropDowmImpl.getIncObjectArray(obj, new String[]{beanObj.getDeptId()});
                qutext += " " +prop.getProperty("portfolio.select.contractListDeptID");
            }
            if ("2".equals(beanObj.getProductId())) {
            }
            if ("RD".equalsIgnoreCase(beanObj.getFlag())) {
            	qutext += " " +prop.getProperty("portfolio.select.renewalDue");
            }
            
            if ("NC".equalsIgnoreCase(beanObj.getFlag())) {
                //obj = dropDowmImpl.getIncObjectArray(obj, new String[]{"NC"});
                //query += " " + getQuery(DBConstants.PORTFOLIO_SELECT_CLAIMYN);
            }
            if ("EC".equalsIgnoreCase(beanObj.getFlag())) {
                obj = dropDowmImpl.getIncObjectArray(obj, new String[]{"EC"});
                qutext += " " +prop.getProperty("portfolio.select.claimYN");
            }
            if(StringUtils.isNotBlank(beanObj.getSearchType()) && null !=beanObj.getSearchType()){
            	if(StringUtils.isNotBlank(beanObj.getProposalNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getProposalNoSearch() + "%")});
            		qutext += " " +prop.getProperty("portfolio.select.perno");
            	}
            	if(StringUtils.isNotBlank(beanObj.getContractNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getContractNoSearch() + "%")});
            		qutext += " " +prop.getProperty("portfolio.select.conno");
            	}
//            	if("3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())){
//	            	if(StringUtils.isNotBlank(beanObj.getCompanyNameSearch1())){
//	            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getCompanyNameSearch1() + "%")});
//	            		qutext += " " +prop.getProperty("portfolio.select.comname=");
//	            	}
//	            	if(StringUtils.isNotBlank(beanObj.getBrokerNameSearch1())){
//	            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getBrokerNameSearch1() + "%")});
//	            		qutext += " " +prop.getProperty("portfolio.select.broname");
//	            	}
//	            	if(StringUtils.isNotBlank(beanObj.getDepartmentNameSearch1())){
//	            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getDepartmentNameSearch1() + "%")});
//	            		qutext += " AND UPPER(B.TMAS_DEPARTMENT_NAME) LIKE UPPER(?)";
//	            	}
//            	}else{
            		if(StringUtils.isNotBlank(beanObj.getCompanyNameSearch())){
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getCompanyNameSearch() + "%")});
                		qutext += " " +prop.getProperty("portfolio.select.comname");
                	}
            		if(StringUtils.isNotBlank(beanObj.getBrokerNameSearch())){
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getBrokerNameSearch() + "%")});
                		qutext += " " +prop.getProperty("portfolio.select.broname");
                	}
            		if(StringUtils.isNotBlank(beanObj.getDepartmentNameSearch())){
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getDepartmentNameSearch() + "%")});
                		qutext += " AND UPPER(B.TMAS_DEPARTMENT_NAME) LIKE UPPER(?)";
                	}
            		if(StringUtils.isNotBlank(beanObj.getSubclassSearch())){
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getSubclassSearch() + "%")});
                		qutext += " AND UPPER((select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',')  from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in(select * from table(SPLIT_TEXT_FN(replace(E.RSK_SPFCID,' ', '')))) AND  SPFC.TMAS_PRODUCT_ID = E.RSK_PRODUCTID AND SPFC.BRANCH_CODE = E.BRANCH_CODE)) LIKE UPPER(?)";
                	}
           // 	}
            	if("1".equalsIgnoreCase(beanObj.getProductId()) ){
            	if(StringUtils.isNotBlank(beanObj.getInsuredNameSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getInsuredNameSearch() + "%")});
            		qutext += " AND UPPER(D.RSK_INSURED_NAME) LIKE UPPER(?)";
            	}
            	}
            	if("1".equalsIgnoreCase(beanObj.getProductId()) || "3".equalsIgnoreCase(beanObj.getProductId()) || "5".equalsIgnoreCase(beanObj.getProductId())){
            	if(StringUtils.isNotBlank(beanObj.getUwYearSearch2())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getUwYearSearch2() + "%")});
            		qutext += " " +prop.getProperty("portfolio.select.uyear");
            	}if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch())){
            		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch());
             		 if (!CollectionUtils.isEmpty(list)) {
             			 res =  list.get(0).getUwrCode();
             			}
           		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + res + "%")});
           		qutext += " AND UPPER(D.RSK_UNDERWRITTER) LIKE UPPER(?) ";
            	}
            	
            	}else{
            		if(StringUtils.isNotBlank(beanObj.getUwYearSearch3())){
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getUwYearSearch3() + "%")});
                		qutext += " " +prop.getProperty("portfolio.select.uyear");
                	}if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch1())){
                		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch1());
                 		 if (!CollectionUtils.isEmpty(list)) {
                 			 res =  list.get(0).getUwrCode();
                 			}
                		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + res + "%")});
                		qutext += " AND UPPER(D.RSK_UNDERWRITTER) LIKE UPPER(?) ";
                	}
            	}
            	if(StringUtils.isNotBlank(beanObj.getOfferNoSearch())){
            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getOfferNoSearch() + "%")});
            		qutext += " AND A.OFFER_NO LIKE ? ";
            		
            	}
            	res1.setType("Yes");
            }else{
            	beanObj.setProposalNoSearch("");
            	beanObj.setContractNoSearch("");
            	beanObj.setCompanyNameSearch1("");
            	beanObj.setBrokerNameSearch1("");
            	beanObj.setDepartmentNameSearch1("");
            	beanObj.setCompanyNameSearch("");
            	beanObj.setBrokerNameSearch("");
            	beanObj.setDepartmentNameSearch("");
            	beanObj.setInsuredNameSearch("");
            	beanObj.setUwYearSearch2("");
            	beanObj.setUnderwriterSearch("");
            	beanObj.setUwYearSearch3("");
            	beanObj.setUnderwriterSearch1("");
            }
            if(StringUtils.isNotBlank(beanObj.getAttachedUW()) && !"ALL".equalsIgnoreCase(beanObj.getAttachedUW())) {
        		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{(beanObj.getAttachedUW())});
        		qutext += " AND UPPER(D.RSK_UNDERWRITTER) IN (select * from table(SPLIT_TEXT_FN(?)))";
        	}
            qutext += " " +prop.getProperty("portfolio.select.orderbyconno");
            if(StringUtils.isBlank(res1.getType()) ){
            	qutext ="select * from ( "+qutext+" )where  rownum<=100";
            }
            List<Map<String, Object>> list = new ArrayList<>();
        	query1 =queryImpl.setQueryProp(qutext, obj);
    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
    		try {
    			 list = query1.getResultList();
    		} catch(Exception e) {
    			e.printStackTrace();
    		} 
    		List<BigDecimal>departId=new ArrayList<BigDecimal>();
    		departId.add(beanObj.getDeptId()==null?BigDecimal.ZERO:new BigDecimal(beanObj.getDeptId()));
            if(list!=null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> tempMap = list.get(i);
                GetContractsListRes1 tempBean = new GetContractsListRes1();
                tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString());
                tempBean.setOfferNo(tempMap.get("OFFER_NO") == null ? "" : tempMap.get("OFFER_NO").toString());
                tempBean.setBouquetNo(tempMap.get("BOUQUET_NO") == null ? "" : tempMap.get("BOUQUET_NO").toString());
                tempBean.setContractNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
                tempBean.setProposalId(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString());
                tempBean.setAmendId(tempMap.get("AMEND_ID") == null ? "" : tempMap.get("AMEND_ID").toString());
                tempBean.setProposalStatus(tempMap.get("PROPOSAL_STATUS")==null?"":tempMap.get("PROPOSAL_STATUS").toString());
                tempBean.setContractStatus(tempMap.get("CONTRACT_STATUS")==null?"":tempMap.get("CONTRACT_STATUS").toString());
                tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME") == null ? "" : tempMap.get("COMPANY_NAME").toString());
                tempBean.setDepartmentName(tempMap.get("TMAS_DEPARTMENT_NAME") == null ? "" : tempMap.get("TMAS_DEPARTMENT_NAME").toString());
                tempBean.setSubClass(tempMap.get("TMAS_SPFC_NAME") == null ? "" : tempMap.get("TMAS_SPFC_NAME").toString());

                tempBean.setDepartmentId(tempMap.get("TMAS_DEPARTMENT_ID") == null ? "" : tempMap.get("TMAS_DEPARTMENT_ID").toString());
                tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE") == null ? "" : tempMap.get("INCEPTION_DATE").toString());
                tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE") == null ? "" : tempMap.get("EXPIRY_DATE").toString());
                tempBean.setInsuredName(tempMap.get("RSK_INSURED_NAME") == null ? "" : tempMap.get("RSK_INSURED_NAME").toString());
                tempBean.setQuoteGendrateddate(tempMap.get("ACCOUNT_DATE") == null ? "" : tempMap.get("ACCOUNT_DATE").toString());
                tempBean.setCeddingcompanyid(tempMap.get("CEDING_COMPANY_ID") == null ? "" : tempMap.get("CEDING_COMPANY_ID").toString());
                tempBean.setLayerNo(tempMap.get("LAYER_NO") == null ? "" : tempMap.get("LAYER_NO").toString());
                tempBean.setOldStatus(tempMap.get("OLD_STATUS") == null ? "" : tempMap.get("OLD_STATUS").toString());
                tempBean.setAcceptenceDate(tempMap.get("ACCOUNT_DATE") == null ? "" : tempMap.get("ACCOUNT_DATE").toString());
                tempBean.setProductId(tempMap.get("PRODUCT_ID") == null ? "" : tempMap.get("PRODUCT_ID").toString());
                if (tempMap.get("BASE_LAYER") != null)
                    tempBean.setBaseLayer(tempMap.get("BASE_LAYER") == null ? "" : tempMap.get("BASE_LAYER").toString());
                tempBean.setOldContract(tempMap.get("OLD_CONTRACTNO") == null ? "" : "0".equals(tempMap.get("OLD_CONTRACTNO")) == true ? "" : tempMap.get("OLD_CONTRACTNO").toString());
                tempBean.setRenewalStatus(tempMap.get("RENEWAL_STATUS") == null ? "" : tempMap.get("RENEWAL_STATUS").toString());
                if (tempMap.get("RENWALDATE") != null) {
                    final float RenwalPreiod = Float.parseFloat(tempMap.get("RENWALDATE") == null ? "" : tempMap.get("RENWALDATE").toString());
                    if (RenwalPreiod <= 60) {
                        tempBean.setRenewalPeriod("1");
                    } else {
                        tempBean.setRenewalPeriod("0");
                    }
                }
                tempBean.setUwYear(tempMap.get("UW_YEAR") == null ? "" : tempMap.get("UW_YEAR").toString());
                tempBean.setUwMonth(tempMap.get("UW_MONTH") == null ? "" : tempMap.get("UW_MONTH").toString());
                tempBean.setUnderwritter(tempMap.get("UNDERWRITTER") == null ? "" : tempMap.get("UNDERWRITTER").toString());
                tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString());
                tempBean.setEditMode(tempMap.get("EDIT_MODE") == null ? "" : tempMap.get("EDIT_MODE").toString());
                tempBean.setLayerNo(tempMap.get("LAYER_NO") == null ? "" : tempMap.get("LAYER_NO").toString());
                tempBean.setSectionNo(tempMap.get("SECTION_NO") == null ? "" : tempMap.get("SECTION_NO").toString());
                if ("C".equalsIgnoreCase(beanObj.getFlag())) {
                    tempBean.setFlag("C");
                } else if ("RD".equalsIgnoreCase(beanObj.getFlag())) {
                    tempBean.setFlag("RD");
                }
                if("P".equalsIgnoreCase(tempBean.getContractStatus().trim()) && "A".equalsIgnoreCase(tempBean.getProposalStatus().trim()) ){
                	tempBean.setDisableStatus("N");
                }
                else if(!"N".equalsIgnoreCase(tempBean.getEditMode())&&!"".equalsIgnoreCase(tempBean.getEditMode())){
                	tempBean.setDisableStatus("N");
                }
                else{
                	tempBean.setDisableStatus("N");
                }
                if("0".equalsIgnoreCase(tempBean.getRenewalStatus().trim())){
                	tempBean.setRenewaldisable("Y");
                }
                else{
                	tempBean.setRenewaldisable("N");
                }
                tempBean.setCeaseStatus(tempMap.get("CEASE_STATUS") == null ? "" : tempMap.get("CEASE_STATUS").toString());
               
                BigDecimal layerno = StringUtils.isBlank(tempBean.getLayerNo())? null: new BigDecimal(tempBean.getLayerNo());
                long  count  = cdRepo.countByContractNoAndLayerNo(tempBean.getProposalNo(),layerno);
                tempBean.setClaimCount(String.valueOf(count));
                
                count  = pdRepo.countByContractNoAndLayerNo(new BigDecimal(tempBean.getProposalNo()),layerno);
                tempBean.setPremiumcount(String.valueOf(count));
                
                count  = dmRepo.countByBranchCodeAndTmasProductIdAndTmasStatusAndCoreCompanyCodeIsNotNullAndTmasDepartmentIdIn(beanObj.getBranchCode(),new BigDecimal(beanObj.getProductId()),"Y",departId);
                tempBean.setCombinedClassCount(String.valueOf(count==0?"N":"Y"));

                if("Y".equalsIgnoreCase(tempBean.getCeaseStatus())){
                	if(tempBean.getPremiumcount()=="0" ){
                		tempBean.setPremiumStatus("Y");
                	}
                	else{
                		tempBean.setPremiumStatus("N");
                	}
                	if(tempBean.getClaimCount()=="0" ){
                		tempBean.setClaimStatus("Y");
                	}
                	else{
                		tempBean.setClaimStatus("N");
                	}
                }
                String res2 ="";
                res2 = rdRepo.countRsEndorsementTypeFindByRskProposalNumberAndRskContractNoAndRsEndorsementType(tempBean.getProposalId(),tempBean.getProposalNo(),"GNPI");
                tempBean.setGnpiFlag(res2);
                tempBean.setButtonSelectionList(getButtonList(tempBean,beanObj.getMenuRights()));
                finalList.add(tempBean);
            }
            res1.setGetContractsList(finalList);
            }      
	            response.setCommonResponse(res1);
	    		response.setMessage("Success");
	    		response.setIsError(false);
            }  catch (Exception e) {
            		e.printStackTrace();
            		response.setMessage("Failed");
            		response.setIsError(true);
        }
        return response;
	}
        private List<ButtonSelectionListRes> getButtonList(GetContractsListRes1 tempBean, List<String> menuRights) {
        	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    		Map<String,Object> map=new HashMap<String,Object>();
    		List<ButtonSelectionListRes> buttonList = new ArrayList<ButtonSelectionListRes>();
    		try{
    			
    			
    			if( menuRights.toString().contains("EN") ){
    				map.put("TYPE","E");
    				map.put("DETAIL_NAME","Edit");
    				list.add(map);
    				map=new HashMap<String,Object>();
    			}
    			if( menuRights.toString().contains("V")){
    				map.put("TYPE","V");
    				map.put("DETAIL_NAME","View");
    				list.add(map);
    				map=new HashMap<String,Object>();
    			}
				/*
				 * if( menuRights.toString().contains("EN")
				 * ){//&&"N".equalsIgnoreCase(tempBean.getEditMode())&&!"".equalsIgnoreCase(
				 * tempBean.getEditMode())){ map.put("TYPE","E"); map.put("DETAIL_NAME","Edit");
				 * list.add(map); map=new HashMap<String,Object>(); }
				 */
    			if(!"0".equalsIgnoreCase(tempBean.getAmendId())){
    				map=new HashMap<String,Object>();
    				map.put("TYPE","H");
    				map.put("DETAIL_NAME","History");
    				list.add(map);
    				map=new HashMap<String,Object>();
    				}
    			if( menuRights.toString().contains("P") &&!"Y".equalsIgnoreCase(tempBean.getDisableStatus()) && !"Y".equalsIgnoreCase(tempBean.getPremiumStatus()) &&!"4".equalsIgnoreCase(tempBean.getProductId())){
    				map.put("TYPE","P");
    				map.put("DETAIL_NAME","Premium");
    				list.add(map);
    				map=new HashMap<String,Object>();
    			}
    			if( menuRights.toString().contains("C") &&!"Y".equalsIgnoreCase(tempBean.getDisableStatus())&& !"Y".equalsIgnoreCase(tempBean.getClaimStatus())){
    				map.put("TYPE","C");
    				map.put("DETAIL_NAME","Claim");
    				list.add(map);
    				map=new HashMap<String,Object>();
    			}
    			if( menuRights.toString().contains("R")&&"N".equalsIgnoreCase(tempBean.getDisableStatus()) &&"N".equalsIgnoreCase(tempBean.getRenewaldisable())&&"1".equalsIgnoreCase(tempBean.getRenewalStatus())&&"1".equalsIgnoreCase(tempBean.getRenewalPeriod())){
    				map.put("TYPE","R");
    				map.put("DETAIL_NAME","Renewal");
    				list.add(map);
    				map=new HashMap<String,Object>();
    			}
    			if( menuRights.toString().contains("V")){
					map.put("TYPE","PL");
					map.put("DETAIL_NAME","Pl. Summar");
					list.add(map);
					map=new HashMap<String,Object>();
				}
//    			if("5".equalsIgnoreCase(tempBean.getProductId())){
//    				map.put("TYPE","IE");
//    				map.put("DETAIL_NAME","IE Module");
//    				list.add(map);
//    				map=new HashMap<String,Object>();
//    			}
//    			if( menuRights.toString().contains("ST")){
//    				map.put("TYPE","S");
//    				map.put("DETAIL_NAME","Stats & Calc.");
//    				list.add(map);
//    				map=new HashMap<String,Object>();
//    			}
//    			if( menuRights.toString().contains("CS") &&!"Y".equalsIgnoreCase(tempBean.getDisableStatus()) &&!"Y".equalsIgnoreCase(tempBean.getCombinedClassCount()) && "2".equalsIgnoreCase(tempBean.getProductId()) &&StringUtils.isBlank(tempBean.getBaseLayer())){
//    				if( "1".equalsIgnoreCase(tempBean.getRenewalStatus())){
//    				map.put("TYPE","CS");
//    				map.put("DETAIL_NAME","Classes");
//    				list.add(map);
//    				map=new HashMap<String,Object>();
//    				}
//    			}
//    			if(("3".equalsIgnoreCase(tempBean.getProductId())||"5".equalsIgnoreCase(tempBean.getProductId()))&&"0".equalsIgnoreCase(tempBean.getGnpiFlag())&&StringUtils.isBlank(tempBean.getBaseLayer()) &&!"Y".equalsIgnoreCase(tempBean.getDisableStatus()) &&"1".equalsIgnoreCase(tempBean.getRenewalStatus())){
//    				map.put("TYPE","L");
//    				map.put("DETAIL_NAME","Duplicate Layer");
//    				list.add(map);
//    				map=new HashMap<String,Object>();
//    			}
//    			if( menuRights.toString().contains("R")&&"N".equalsIgnoreCase(tempBean.getDisableStatus()) &&"N".equalsIgnoreCase(tempBean.getRenewaldisable())&&"1".equalsIgnoreCase(tempBean.getRenewalStatus())&&"1".equalsIgnoreCase(tempBean.getRenewalPeriod())){
//    				if(StringUtils.isBlank(tempBean.getBaseLayer())){
//    					map.put("TYPE","R");
//    					map.put("DETAIL_NAME","Renewal");
//    					list.add(map);
//    					map=new HashMap<String,Object>();
//    				}
//    			}
//    			if( menuRights.toString().contains("CP")&&StringUtils.isBlank(tempBean.getBaseLayer())){
//    				map.put("TYPE","CP");
//    				map.put("DETAIL_NAME","Copy");
//    				list.add(map);
//    				map=new HashMap<String,Object>();
//    			}
//    			map.put("TYPE","D");
//    			map.put("DETAIL_NAME","Document");
//    			list.add(map);
//    			map=new HashMap<String,Object>();
    			if(list.size()>0&&list!=null) {
    			for(int i=0; i<list.size(); i++) {
    				Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
    				ButtonSelectionListRes button = new ButtonSelectionListRes();
    				button.setType(tempMap.get("TYPE")==null?"":tempMap.get("TYPE").toString());	
    				button.setDetailName(tempMap.get("DETAIL_NAME")==null?"":tempMap.get("DETAIL_NAME").toString());
    				buttonList.add(button);
    			}
    			}
    			}
    		catch(Exception e){
    			e.printStackTrace();
    		}
    		return buttonList;
    	}

		@Override
		public GetHistoryListRes getHistoryList(GetHistoryListReq beanObj) {
			GetHistoryListRes response = new GetHistoryListRes();
	        List<GetHistoryListRes1> finalList = new ArrayList<GetHistoryListRes1>();
	        try {
	            String query = "";
	            String[] obj = new String[0];
	            obj = new String[7];
	            obj[0] = beanObj.getProductId();
	            obj[1] = beanObj.getProductId();
	            obj[2] = beanObj.getBranchCode();
	            obj[3] = beanObj.getBranchCode();
	            obj[4] = beanObj.getBranchCode();
	            obj[5] = beanObj.getBranchCode();
	            obj[6] = beanObj.getBranchCode();
	            query = "portfolio.select.contractList1";
	            String qutext = prop.getProperty(query);
	            if (!"1".equals(beanObj.getProductId()))
	            qutext += " " +prop.getProperty("portfolio.select.contractListLayerNo1");
	            qutext += " " +prop.getProperty("portfolio.select.contractList2");
	            if (!"1".equals(beanObj.getProductId()))
	            qutext += " " +prop.getProperty("portfolio.select.contractListLayerNo2");
	            qutext += " " +prop.getProperty("portfolio.select.contractList3History");
	            if ("1".equals(beanObj.getProductId())) {
	                obj = dropDowmImpl.getIncObjectArray(obj, new String[]{beanObj.getDeptId()});
	                qutext += " " +prop.getProperty("portfolio.select.contractListDeptID");
	            }
	            if ("RD".equalsIgnoreCase(beanObj.getFlag())) {
	                qutext += " " +prop.getProperty("portfolio.select.renewalDue");
	            }
	            if ("NC".equalsIgnoreCase(beanObj.getFlag())) {
	                obj = dropDowmImpl.getIncObjectArray(obj, new String[]{"NC"});
	                qutext += " " +prop.getProperty("portfolio.select.claimYN");
	            }
	            if ("EC".equalsIgnoreCase(beanObj.getFlag())) {
	                obj =dropDowmImpl.getIncObjectArray(obj, new String[]{"EC"});
	                qutext += " " +prop.getProperty("portfolio.select.claimYN");
	            }
	            qutext += " " +prop.getProperty("portfolio.select.perno");
	            obj = dropDowmImpl.getIncObjectArray(obj, new String[]{beanObj.getProposalNo()});
	            qutext += " " +prop.getProperty("portfolio.select.orderbyconno");
	            
	            List<Map<String, Object>> list = new ArrayList<>();
	        	query1 =queryImpl.setQueryProp(qutext, obj);
	    		query1.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
	    		try {
	    			 list = query1.getResultList();
	    		} catch(Exception e) {
	    			e.printStackTrace();
	    		} 
	            //if(list!=null && list.size()>0){
	            for (int i = 0; i < list.size(); i++) {
	                Map<String, Object> tempMap = list.get(i);
	                GetHistoryListRes1 tempBean = new GetHistoryListRes1();
	                tempBean.setProposalNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
	                tempBean.setProposalId(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString());
	                tempBean.setAmendId(tempMap.get("AMEND_ID") == null ? "" : tempMap.get("AMEND_ID").toString());
	                tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME") == null ? "" : tempMap.get("COMPANY_NAME").toString());
	                tempBean.setDepartmentName(tempMap.get("TMAS_DEPARTMENT_NAME") == null ? "" : tempMap.get("TMAS_DEPARTMENT_NAME").toString());
	                tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE") == null ? "" : tempMap.get("INCEPTION_DATE").toString());
	                tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE") == null ? "" : tempMap.get("EXPIRY_DATE").toString());
	                tempBean.setInsuredName(tempMap.get("RSK_INSURED_NAME") == null ? "" : tempMap.get("RSK_INSURED_NAME").toString());
	                tempBean.setQuoteGendrateddate(tempMap.get("ACCOUNT_DATE") == null ? "" : tempMap.get("ACCOUNT_DATE").toString());
	                tempBean.setCeddingcompanyid(tempMap.get("CEDING_COMPANY_ID") == null ? "" : tempMap.get("CEDING_COMPANY_ID").toString());
	                tempBean.setLayerNo(tempMap.get("LAYER_NO") == null ? "" : tempMap.get("LAYER_NO").toString());
	                if("0".equalsIgnoreCase(tempBean.getAmendId())){
	                	tempBean.setEndorsmentTypeName("Original");
	                }
	                else{
	                tempBean.setEndorsmentTypeName(tempMap.get("RS_ENDORSEMENT_TYPE") == null ? "" : tempMap.get("RS_ENDORSEMENT_TYPE").toString());
	                }
	                if (tempMap.get("BASE_LAYER") != null)
	                    tempBean.setBaseLayer(tempMap.get("BASE_LAYER") == null ? "" : tempMap.get("BASE_LAYER").toString());
	                tempBean.setOldContract(tempMap.get("OLD_CONTRACTNO") == null ? "" : "0".equals(tempMap.get("OLD_CONTRACTNO")) == true ? "" : tempMap.get("OLD_CONTRACTNO").toString());
	                tempBean.setRenewalStatus(tempMap.get("RENEWAL_STATUS") == null ? "" : tempMap.get("RENEWAL_STATUS").toString());
	                if (tempMap.get("RENWALDATE") != null) {
	                    final float RenwalPreiod = Float.parseFloat(tempMap.get("RENWALDATE") == null ? "" : tempMap.get("RENWALDATE").toString());
	                    if (RenwalPreiod <= 365) {
	                        tempBean.setRenewalPeriod("1");
	                    } else {
	                        tempBean.setRenewalPeriod("0");
	                    }
	                }
	                tempBean.setUwYear(tempMap.get("UW_YEAR") == null ? "" : tempMap.get("UW_YEAR").toString());
	                tempBean.setUwMonth(tempMap.get("UW_MONTH") == null ? "" : tempMap.get("UW_MONTH").toString());
	                tempBean.setUnderwritter(tempMap.get("UNDERWRITTER") == null ? "" : tempMap.get("UNDERWRITTER").toString());
	                tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString());
	                tempBean.setLayerNo(tempMap.get("LAYER_NO") == null ? "" : tempMap.get("LAYER_NO").toString());
	                tempBean.setSectionNo(tempMap.get("SECTION_NO") == null ? "" : tempMap.get("SECTION_NO").toString());
	                if ("C".equalsIgnoreCase(beanObj.getFlag())) {
	                    tempBean.setFlag("C");
	                } else if ("RD".equalsIgnoreCase(beanObj.getFlag())) {
	                    tempBean.setFlag("RD");
	                }
	                tempBean.setDisplay("History");
	                finalList.add(tempBean);
	            }
	            response.setCommonResponse(finalList);
	    		response.setMessage("Success");
	    		response.setIsError(false);
	            }  catch (Exception e) {
	            		e.printStackTrace();
	            		response.setMessage("Failed");
	            		response.setIsError(true);
	        }
	        return response;
		}

		@Override
		public CommonResponse procAuto(ProcAutoReq bean) {
			CommonResponse response = new CommonResponse();
			 List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
		        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        DecimalFormat df = new DecimalFormat("#.00");
		        String transactionNo = "";
		        try {
					if (parseNumber(bean.getProductId()) > 0) {
					    if (bean.getPortfolioListReq() != null)
					        for (PortfolioListReq b : bean.getPortfolioListReq()){
					            if(StringUtils.isNotBlank(b.getPremiumClass()) && "3".equalsIgnoreCase(bean.getProductId())){
					            	try{
					            	//GET_DEPT_CODE_LIMIT
						         	CriteriaBuilder cb = em.getCriteriaBuilder(); 
						      		CriteriaQuery<String> query = cb.createQuery(String.class); 
						      		
						      		// like table name
						      		Root<TtrnRskClassLimits> pm = query.from(TtrnRskClassLimits.class);

						      		// Select
						      		query.select(pm.get("rskCoverClass")); 

						      		// MAXAmend ID
						      		Subquery<Long> end = query.subquery(Long.class); 
						      		Root<TtrnRskClassLimits> pms = end.from(TtrnRskClassLimits.class);
						      		end.select(cb.max(pms.get("rskEndorsementNo")));
						      		Predicate a1 = cb.equal( pm.get("rskContractNo"), pms.get("rskContractNo"));
						      		Predicate a2 = cb.equal( pm.get("rskLayerNo"), pms.get("rskLayerNo"));
						      		end.where(a1,a2);
						      		
						      		// Where
						      		Predicate n1 = cb.equal(pm.get("rskContractNo"), b.getContractNo());
						      		Predicate n2 = cb.equal(pm.get("rskLayerNo"), StringUtils.isEmpty(b.getLayerNo())? "0" : b.getLayerNo());
						      		Predicate n3 = cb.equal(pm.get("rskProductid"), bean.getProductId());
						      		Predicate n4 = cb.equal(pm.get("rskSno"), "1");
						      		Predicate n5 = cb.equal(pm.get("rskEndorsementNo"), end);
						      		query.where(n1,n2,n3,n4,n5);
						      		
						      		// Get Result
						      		TypedQuery<String> res = em.createQuery(query);
						      		List<String> result = res.getResultList();
						         if(result.size()>0) {
						      		b.setPremiumClass(result.get(0));
						         }  }catch(Exception e){
							         	   e.printStackTrace();
							          }
					            }
					            GetCommonValueRes com = dropDowmImpl.GetExchangeRate(b.getCurrencyId(), b.getTransactionDate(),bean.getCountryId(),bean.getBranchCode());
					            b.setExchangeRate(com.getCommonResponse());
					            Map<String, Object> map = new HashMap<String, Object>();
					            map.put("receipt", "");
					            map.put("contractNo", b.getContractNo());
					            map.put("transactionNo", fm.getSequence("Premium",bean.getProductId(),b.getDepartmentId(), bean.getBranchCode(),"",b.getTransactionDate()));
					            transactionNo =  map.get("transactionNo").toString();
					            map.put("transactionDate", b.getTransactionDate());
					            map.put("installmentDate", b.getInstallmentDate());
					            map.put("installmentNo", b.getInstallmentNo());
					            map.put("currencyId", b.getCurrencyId());
					            map.put("exRate", parseNumber(b.getExchangeRate()));
					            map.put("premQS", parseNumber(b.getPremiumOC()));
					            map.put("commission", parseNumber(b.getCommission()));
					            map.put("commissionOC", parseNumber(b.getCommissionOC()==null?"":b.getCommissionOC()));
					            map.put("brokerage", parseNumber(b.getBrokerage()));
					            map.put("brokerageOC", parseNumber(b.getBrokerageOC()));
					            map.put("tax", parseNumber(b.getTax()));
					            map.put("taxOC", parseNumber(b.getTaxOC()));
					            map.put("entryDate", b.getReceiveDate());
					            map.put("status", "Y");
					            map.put("enteringMode", "2");
					            map.put("settlementStatus", bean.getSettlementStatus());
					            map.put("otherCostOC", parseNumber(b.getOtherCostOC()));
					            map.put("premQSDC", getDC(map.get("premQS").toString(), b.getExchangeRate()));
					            map.put("commissionDC", b.getCommissionOC()==null?"":getDC(map.get("commissionOC").toString(), b.getExchangeRate()));
					            map.put("brokerageDC", getDC(map.get("brokerageOC").toString(), b.getExchangeRate()));
					            map.put("taxDC", getDC(map.get("taxOC").toString(), b.getExchangeRate()));
					            map.put("otherCostDC", getDC(map.get("otherCostOC").toString(), b.getExchangeRate()));
					            map.put("cedantRef", b.getReference());
					            map.put("remark", b.getRemarks());
					            map.put("whtOC", parseNumber(b.getWithHoldingTaxOC()));
					            map.put("whtDC", getDC(b.getWithHoldingTaxOC(), b.getExchangeRate()));
					            map.put("totalCrOC", parseNumber(b.getPremiumOC()));
					            map.put("totalCrDC", getDC(map.get("totalCrOC").toString(), b.getExchangeRate()));
					            map.put("totalDrOC", getTotalDebit(map));
					            map.put("totalDrDC", getDC(map.get("totalDrOC").toString(), b.getExchangeRate()));
					            map.put("netDueOC", df.format(parseNumber(map.get("totalCrOC").toString()) - parseNumber(map.get("totalDrOC").toString())));
					            map.put("netDueDC", getDC((map.get("netDueOC").toString()), b.getExchangeRate()));
					            b.setNetDueOC(map.get("netDueOC").toString());
					            map.put("layerNo", StringUtils.isEmpty(b.getLayerNo())? "0" : b.getLayerNo());
					            map.put("loginId", bean.getLoginId()==null?"":bean.getLoginId());
					            map.put("branchCode", bean.getBranchCode()==null?"":bean.getBranchCode());
					            map.put("subClass", b.getDepartmentId()==null?"":b.getDepartmentId());
					            if("1".equalsIgnoreCase(bean.getProductId())){
					            	map.put("premiumClass", b.getPremiumClass()==null?"":b.getPremiumClass());
					            	map.put("premiumsubClass", b.getPremiumSubClass()==null?"":b.getPremiumSubClass());
					            }
					            else{
					            	map.put("premiumClass", b.getPremiumClass()==null?"":b.getPremiumClass());
					            	map.put("premiumsubClass", "D");
					            }
					            map.put("taxDetectedOC", parseNumber(b.getTaxDedectSource()));
					            map.put("taxDetectedDC", getDC(b.getTaxDedectSource(), b.getExchangeRate()));
					            map.put("servicetaxOC", parseNumber(b.getServiceTax()));
					            map.put("servicetaxDC", getDC(b.getServiceTax(), b.getExchangeRate()));
					            map.put("bonusOC", parseNumber(b.getBonus()));
					            map.put("bonusDC", getDC(b.getBonus(), b.getExchangeRate()));
					            map.put("receivedDate", b.getTransactionDate());
					            map.put("proposalNo", b.getProposalNo());
					            map.put("productId", bean.getProductId());
					            maps.add(map);
					        }
					 //   doubt
//					    	 if("1".equalsIgnoreCase(bean.getProductId())){
//							    	getSimpleJdbcTemplate().batchUpdate(getQuery("premium.insert.facpremium.auto"), maps.toArray(new Map[maps.size()]));
//							    	getSimpleJdbcTemplate().batchUpdate(getQuery("premium.insert.facpremium.auto.retro"), maps.toArray(new Map[maps.size()]));
//							    }else if("3".equalsIgnoreCase(bean.getProductId())){
//							    	getSimpleJdbcTemplate().batchUpdate(getQuery("premium.insert.nonproppremium.auto"), maps.toArray(new Map[maps.size()]));
//							    	getSimpleJdbcTemplate().batchUpdate(getQuery("premium.insert.nonproppremium.auto.retro"), maps.toArray(new Map[maps.size()]));
//							    }
//						    getSimpleJdbcTemplate().batchUpdate(getQuery("UPDATE_MND_INSTALLMENT_AUTO"), maps.toArray(new Map[maps.size()]));
					
					} else {

					}
					response.setMessage("Success");
		    		response.setIsError(false);
				} catch (Exception e) {
					bean.setTransactionError("error");
					for(int i=maps.size()-1;i>=0;i--){
						Map<String,Object> limap=maps.get(i);
					//	String query="{CALL sequence_reset(?,?,?)}";
						  StoredProcedureQuery integration = null;
						  integration = em.createStoredProcedureQuery("SEQUENCE_RESET")
						  .registerStoredProcedureParameter("pvnewvalue", String.class, ParameterMode.IN)
						  .registerStoredProcedureParameter("Pvproduct", String.class, ParameterMode.IN) 
						  .registerStoredProcedureParameter("Pvbranch", String.class, ParameterMode.IN)
						//  .registerStoredProcedureParameter("Lvseqname", String.class, ParameterMode.OUT)
						//  .registerStoredProcedureParameter("seqowner", String.class, ParameterMode.OUT)
						//  .registerStoredProcedureParameter("lvnewvalue", String.class, ParameterMode.OUT)
						  .setParameter("pvnewvalue", limap.get("transactionNo"))
						  .setParameter("Pvproduct", limap.get("productId"))
						  .setParameter("Pvbranch", limap.get("branchCode"));
						  
						  integration.execute();
						 // output=(String) integration.getOutputParameterValue("pvQuoteNo");
					e.printStackTrace();
					}
					response.setMessage("Failed");
            		response.setIsError(true);
		         }
		        return response;
		}
		
	  private Double getDC(String oc, String exchange) {
	    	  DecimalFormat df = new DecimalFormat("#.00");
	        if (oc == null || exchange == null ||exchange == "")
	            return 0d;
	        Double ocd = parseNumber(oc.replaceAll(",", "")), exchanged = parseNumber(exchange.replaceAll(",", ""));
	        return Double.parseDouble(df.format(ocd / exchanged));
	    }
		  private Double getTotalDebit(Map<String, Object> map) {
		        List<String> debits = new ArrayList<String>();
		        debits.add("commissionOC");
		        debits.add("brokerageOC");
		        debits.add("taxOC");
		        debits.add("otherCostOC");
		        debits.add("whtOC");
		        Double total = 0d;
		        for (String key : debits) {
		            total += parseNumber(map.get(key).toString());
		        }
		        return total;
		    }

		@Override
		public GetConfirmedListRes getConfirmedList(GetConfirmedListReq beanObj) {
			GetConfirmedListRes response = new GetConfirmedListRes();
			GetConfirmedListResponse res1 = new GetConfirmedListResponse();
			List<GetConfirmedListRes1> finalList = new ArrayList<GetConfirmedListRes1>();
			String res ="";
	        try {
	           //GET_NEW_CONTRACT_LIST
	            CriteriaBuilder cb = em.getCriteriaBuilder(); 
	      		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
	      		
	      		Root<PersonalInfo> c = query.from(PersonalInfo.class);
	      		Root<PersonalInfo> c1 = query.from(PersonalInfo.class);
	      		Root<PositionMaster> a = query.from(PositionMaster.class);
	      		Root<TtrnRiskDetails> e = query.from(TtrnRiskDetails.class);
	      		//Root<TmasDepartmentMaster> b = query.from(TmasDepartmentMaster.class);
	      	//	Root<UnderwritterMaster> d = query.from(UnderwritterMaster.class);
	      		
	      		Subquery<String> deptName = query.subquery(String.class); 
				Root<TmasDepartmentMaster> coms = deptName.from(TmasDepartmentMaster.class);
				deptName.select(coms.get("tmasDepartmentName"));
				Predicate dp1 = cb.equal( coms.get("tmasDepartmentId"), e.get("rskDeptid"));
				Predicate dp2 = cb.equal( coms.get("tmasProductId"), e.get("rskProductid"));
				Predicate dp3 = cb.equal( coms.get("branchCode"), a.get("branchCode"));
				Predicate dp4 = cb.equal( coms.get("tmasStatus"), "Y");
				deptName.where(dp1,dp2,dp3,dp4);
				
	      		Expression<String> e0 = cb.concat(c1.get("firstName"), " ");
	      		
	      		query.multiselect(
	      				e.get("rskSpfcid").alias("RSK_SPFCID"),
	      				a.get("offerNo").alias("OFFER_NO"),
	      				a.get("proposalNo").alias("PROPOSAL_NO"),
	      				//b.get("tmasDepartmentId").alias("TMAS_DEPARTMENT_ID"),
	      				deptName.alias("TMAS_DEPARTMENT_NAME"),
	      				a.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),
	      				cb.concat(e0,c1.get("lastName")).alias("BROKER_NAME"),
	      				c.get("companyName").alias("COMPANY_NAME"),
	      				a.get("inceptionDate").alias("INCEPTION_DATE"),
	      				a.get("accountDate").alias("ACCOUNT_DATE"),
	      				a.get("expiryDate").alias("EXPIRY_DATE"),
	      				cb.coalesce(a.get("baseLayer"),a.get("proposalNo")).alias("BASE_LAYER"),
	      				a.get("contractNo").alias("CONTRACT_NO"),
	      				cb.selectCase().when(cb.notEqual(a.get("uwYear"), 0),a.get("uwYear")).otherwise("null").alias("UW_YEAR"),	      				
	      				a.get("uwMonth").alias("UW_MONTH"),
	      				a.get("sectionNo").alias("SECTION_NO"),
	      		//		d.get("underwritter").alias("UNDERWRITTER"),	
	      				a.get("layerNo").alias("LAYER_NO"),
	      				a.get("oldContractno").alias("OLD_CONTRACTNO"),
	      				a.get("amendId").alias("AMEND_ID"),
	      				e.get("rskInsuredName").alias("RSK_INSURED_NAME"),
	      				a.get("bouquetNo").alias("Bouquet_No"),
	      				cb.coalesce(a.get("baseLayer"),a.get("proposalNo")).alias("BASE_LAYER_DESC")
	      				); 

	      		// MAXAmend ID
	      		Subquery<Long> amendC = query.subquery(Long.class); 
	      		Root<PersonalInfo> pms = amendC.from(PersonalInfo.class);
	      		amendC.select(cb.max(pms.get("amendId")));
	      		Predicate a1 = cb.equal( c.get("customerId"), pms.get("customerId"));
	      		Predicate a2 = cb.equal( c.get("branchCode"), pms.get("branchCode"));
	      		Predicate a3 = cb.equal( c.get("customerType"), pms.get("customerType"));
	      		amendC.where(a3,a1,a2);
	      		
	      		Subquery<Long> amendA = query.subquery(Long.class); 
	      		Root<PositionMaster> pm = amendA.from(PositionMaster.class);
	      		amendA.select(cb.max(pm.get("amendId")));
	      		Predicate b1 = cb.equal( a.get("proposalNo"), pm.get("proposalNo"));
	      		Predicate b2 = cb.equal( a.get("branchCode"), pm.get("branchCode"));
	      		amendA.where(b1,b2);
	      		
	      		Subquery<Long> amendC1 = query.subquery(Long.class); 
	      		Root<PersonalInfo> pi = amendC1.from(PersonalInfo.class);
	      		amendC1.select(cb.max(pi.get("amendId")));
	      		Predicate d1 = cb.equal( c1.get("customerId"), pi.get("customerId"));
	      		Predicate d2 = cb.equal( c1.get("branchCode"), pi.get("branchCode"));
	      		Predicate d3 = cb.equal( c1.get("customerType"), pi.get("customerType"));
	      		amendC1.where(d3,d1,d2);
	      		
	      		Subquery<Long> prop = query.subquery(Long.class); 
	      		Root<TtrnRiPlacement> ri = prop.from(TtrnRiPlacement.class);
	      		prop.select(ri.get("proposalNo")).distinct(true);
	      		Expression<String> app=ri.get("approverStatus");
	      		List<String>appstatus=new ArrayList<>();
	      		appstatus.add("A");appstatus.add("Y");
	      		Subquery<Long> statusNo = query.subquery(Long.class); 
	      		Root<TtrnRiPlacement> ri1 = statusNo.from(TtrnRiPlacement.class);
	      		statusNo.select(cb.max(ri1.get("statusNo")));
	      		Predicate g1 = cb.equal( ri.get("proposalNo"), ri1.get("proposalNo"));
	      		Predicate g2 = cb.equal( ri.get("sno"), ri1.get("sno"));
	      		Predicate g3 = cb.equal( ri.get("branchCode"), ri1.get("branchCode"));
	      		statusNo.where(g3,g1,g2);
	      		Predicate f1 = cb.equal( a.get("proposalNo"), ri.get("proposalNo"));
	      		Predicate f2 = cb.equal( ri.get("status"), "CSL");
	      		Predicate f4 = app.in(appstatus);
	      		Expression<String> e1 = ri.get("statusNo");
 	      		Predicate f3 = e1.in(statusNo);
	      		prop.where(f3,f1,f2,f4);
	      		
	      		List<Predicate> predicateList = new ArrayList<Predicate>();
	      		//predicateList.add(cb.equal(e.get("rskDeptid"), b.get("tmasDepartmentId")));
	      		//predicateList.add(cb.equal(b.get("tmasProductId"), beanObj.getProductId()));
	      		//predicateList.add(cb.equal(b.get("branchCode"), beanObj.getBranchCode()));
	      		//predicateList.add(cb.equal(b.get("tmasStatus"), "Y"));
//	      		predicateList.add(cb.equal(d.get("uwrCode"), e.get("rskUnderwritter")));
//	      		predicateList.add(cb.equal(d.get("branchCode"),  beanObj.getBranchCode()));
	      		predicateList.add(cb.equal(a.get("productId"), beanObj.getProductId()));      		
	      		predicateList.add(cb.or(cb.equal(a.get("contractStatus"), "P"),cb.isNull(a.get("contractStatus"))));
		        predicateList.add(cb.equal(c.get("customerId"), a.get("cedingCompanyId")));
		      	predicateList.add(cb.equal(c.get("branchCode"),  beanObj.getBranchCode()));
		      	predicateList.add(cb.equal(c.get("customerType"),  "C"));
		      	predicateList.add(cb.equal(c.get("amendId"),  amendC));
		      	predicateList.add(cb.equal(a.get("proposalNo"), e.get("rskProposalNumber")));
		      	predicateList.add(cb.equal(a.get("amendId"), e.get("rskEndorsementNo")));
		      	predicateList.add(cb.equal(a.get("productId"), e.get("rskProductid")));
		      	predicateList.add(cb.equal(a.get("branchCode"), beanObj.getBranchCode()));
		      
		      	predicateList.add(cb.equal(a.get("amendId"), amendA));
		      	predicateList.add(cb.equal(c1.get("customerId"), a.get("brokerId")));
		      	predicateList.add(cb.equal(c1.get("branchCode"),  beanObj.getBranchCode()));
		      	predicateList.add(cb.equal(c1.get("amendId"), amendC1));
		        predicateList.add(cb.or(cb.isNull(e.get("oldContractno")),cb.equal(e.get("oldContractno"), "0")));
		      	predicateList.add(cb.equal(a.get("proposalNo"), prop==null?null:prop));	           		
	            
	            if(StringUtils.isNotBlank(beanObj.getSearchType()) && null !=beanObj.getSearchType()){
	            	if(StringUtils.isNotBlank(beanObj.getProposalNoSearch())){
	            	
	            		predicateList.add(cb.like(a.get("proposalNo").as(String.class), "%"+beanObj.getProposalNoSearch()+"%"));	            		
	            	}	
	            	if(StringUtils.isNotBlank(beanObj.getContractNoSearch())){
	                    predicateList.add(cb.like(a.get("contractNo").as(String.class), "%"+beanObj.getContractNoSearch()+"%"));	        
	            	}
	        		if(StringUtils.isNotBlank(beanObj.getCompanyNameSearch())){
	                    predicateList.add(cb.like(cb.upper(c.get("companyName")), "%"+beanObj.getCompanyNameSearch().toUpperCase()+"%"));	     
	            	}
	        		if(StringUtils.isNotBlank(beanObj.getBrokerNameSearch())){ 
	                    predicateList.add(cb.like(cb.upper(c1.get("firstName")), "%"+beanObj.getBrokerNameSearch().toUpperCase()+"%"));	 
	            	}
	        		if(StringUtils.isNotBlank(beanObj.getDepartmentNameSearch())){
	            		 predicateList.add(cb.like(deptName, "%"+beanObj.getDepartmentNameSearch().toUpperCase()+"%"));	 
	            	}
	        		if(StringUtils.isNotBlank(beanObj.getBouquetNoSearch())){
	            		 predicateList.add(cb.like(cb.upper(a.get("bouquetNo").as(String.class)), "%"+beanObj.getBouquetNoSearch().toUpperCase()+"%"));	 
	            	}
	        		if(StringUtils.isNotBlank(beanObj.getSubclassSearch())){ //pending
//	            		obj = dropDowmImpl.getIncObjectArray(obj, new String[]{("%" + beanObj.getSubclassSearch() + "%")});
//	            		 query += " AND UPPER((select RTRIM(XMLAGG(XMLELEMENT(E,TMAS_SPFC_NAME,',')).EXTRACT('//text()'),',')  from TMAS_SPFC_MASTER SPFC where SPFC.TMAS_SPFC_ID in(select * from table(SPLIT_TEXT_FN(replace(E.RSK_SPFCID,' ', '')))) AND  SPFC.TMAS_PRODUCT_ID = E.RSK_PRODUCTID AND SPFC.BRANCH_CODE = E.BRANCH_CODE)) LIKE UPPER(?)";
	            	}
	            	if("1".equalsIgnoreCase(beanObj.getProductId())){
	        		if(StringUtils.isNotBlank(beanObj.getInsuredNameSearch())){
	            		 predicateList.add(cb.like(cb.upper(e.get("rskInsuredName")), "%"+beanObj.getInsuredNameSearch().toUpperCase()+"%"));	
	            	}
	            	if(StringUtils.isNotBlank(beanObj.getUwYearSearch())){  
	                    predicateList.add(cb.like(a.get("uwYear"), "%"+beanObj.getUwYearSearch()+"%"));	        
	            	
	            	}if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch())){
	            		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch());
                		 if (!CollectionUtils.isEmpty(list)) {
                			 res =  list.get(0).getUwrCode();
                			}
	            		 predicateList.add(cb.like(cb.upper(e.get("rskUnderwritter")), "%"+res.toUpperCase()+"%"));	
	            	}
	            	}else{
	            		if(StringUtils.isNotBlank(beanObj.getUwYearSearch1())){
	                        predicateList.add(cb.like(a.get("uwYear"), "%"+beanObj.getUwYearSearch1()+"%"));	  
	                	}if(StringUtils.isNotBlank(beanObj.getUnderwriterSearch1())){
	                		List<UnderwritterMaster> list = uwRepo.findByBranchCodeAndUwrStatusAndUnderwritterIgnoreCaseContaining(beanObj.getBranchCode(),"Y",beanObj.getUnderwriterSearch1());
	                		 if (!CollectionUtils.isEmpty(list)) {
	                			 res =  list.get(0).getUwrCode();
	                			}
	                		 predicateList.add(cb.like(cb.upper(e.get("rskUnderwritter")), "%"+res.toUpperCase()+"%"));	
	                	}
	            	}if(StringUtils.isNotBlank(beanObj.getOfferNoSearch())){
	            		  predicateList.add(cb.like(a.get("offerNo"), "%"+beanObj.getOfferNoSearch()+"%"));	  
	            	}
	            	
	            	res1.setType("Yes");
	            }else{
//	            	beanObj.setProposalNoSearch("");
//	            	beanObj.setContractNoSearch("");
//	            	beanObj.setCompanyNameSearch("");
//	            	beanObj.setBrokerNameSearch("");
//	            	beanObj.setDepartmentNameSearch("");
//	            	beanObj.setInsuredNameSearch("");
//	            	beanObj.setUwYearSearch("");
//	            	beanObj.setUnderwriterSearch("");
//	            	beanObj.setUwYearSearch1("");
//	            	beanObj.setUnderwriterSearch1("");
//	            	beanObj.setOfferNoSearch("");
	            }
	            if ("RP".equalsIgnoreCase(beanObj.getFlag())) {
	                predicateList.add(cb.isNotNull(e.get("oldContractno")));	
	                predicateList.add(cb.notEqual(e.get("oldContractno"), "0"));	
	            } else { 
//		            predicateList.add(cb.isNull(e.get("oldContractno")));	
//	                predicateList.add(cb.equal(e.get("oldContractno"),"0"));	
	            }
	            if(StringUtils.isNotBlank(beanObj.getAttachedUW()) && !"ALL".equalsIgnoreCase(beanObj.getAttachedUW())) {
	        		Expression<String> e2 = cb.upper(e.get("rskUnderwritter"));
	        		List<String> attachedUW =new ArrayList<String>(Arrays.asList(beanObj.getAttachedUW().split(",") )) ;
	        		predicateList.add(e2.in(attachedUW));
	        	}
	            List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.desc(a.get("offerNo")));
				orderList.add(cb.asc(a.get("proposalNo")));
	         
	            query.where(predicateList.toArray(new Predicate[0])).orderBy(orderList);
	      		
	      		TypedQuery<Tuple> result = em.createQuery(query);
	      		List<Tuple> list = result.getResultList();
	      			
	            for (int i = 0; i < list.size(); i++) {
	            	Tuple tempMap = list.get(i);
	            	String id = tempMap.get("RSK_SPFCID") == null ? "" : tempMap.get("RSK_SPFCID").toString();
	            	
	                GetConfirmedListRes1 tempBean = new GetConfirmedListRes1();
	                tempBean.setContractNo(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
	                tempBean.setProposalNo(tempMap.get("PROPOSAL_NO") == null ? "" : tempMap.get("PROPOSAL_NO").toString());
	                tempBean.setOfferNo(tempMap.get("OFFER_NO") == null ? "" : tempMap.get("OFFER_NO").toString());
	                tempBean.setBouquetNo(tempMap.get("Bouquet_No") == null ? "" : tempMap.get("Bouquet_No").toString());
	                tempBean.setAmendId(tempMap.get("AMEND_ID") == null ? "" : tempMap.get("AMEND_ID").toString());
	                tempBean.setCedingCompanyName(tempMap.get("COMPANY_NAME") == null ? "" : tempMap.get("COMPANY_NAME").toString());
	                
	                tempBean.setDepartmentName(tempMap.get("TMAS_DEPARTMENT_NAME") == null ? "" : tempMap.get("TMAS_DEPARTMENT_NAME").toString());
	            
	                tempBean.setSubClass(dropDowmImpl.getSubClass(id, beanObj.getBranchCode(), beanObj.getProductId()));	                
	                // tempBean.setDepartmentId(tempMap.get("TMAS_DEPARTMENT_ID") == null ? "" : tempMap.get("TMAS_DEPARTMENT_ID").toString());
	                tempBean.setInceptionDate(tempMap.get("INCEPTION_DATE") == null ? "" : Dateformat(tempMap.get("INCEPTION_DATE")).toString());
	                tempBean.setExpiryDate(tempMap.get("EXPIRY_DATE") == null ? "" : Dateformat(tempMap.get("EXPIRY_DATE")).toString());
	                tempBean.setInsuredName(tempMap.get("RSK_INSURED_NAME") == null ? "" : tempMap.get("RSK_INSURED_NAME").toString());
	                tempBean.setQuoteGendratedDate(tempMap.get("ACCOUNT_DATE") == null ? "" : tempMap.get("ACCOUNT_DATE").toString());
	                tempBean.setCeddingCompanyId(tempMap.get("CEDING_COMPANY_ID") == null ? "" : tempMap.get("CEDING_COMPANY_ID").toString());
	                tempBean.setLayerNo(tempMap.get("LAYER_NO") == null ? "" : tempMap.get("LAYER_NO").toString());
	                tempBean.setSectionNo(tempMap.get("SECTION_NO") == null ? "" : tempMap.get("SECTION_NO").toString());
	                if (beanObj.getFlag() != null && "N".equalsIgnoreCase(beanObj.getFlag())) {
	                    tempBean.setFlag("N");
	                } else if ("RP".equalsIgnoreCase(beanObj.getFlag())) {
	                    tempBean.setFlag("RP");
	                    tempBean.setTitle("RP");
	                   
	                } else {
	                    tempBean.setFlag("P");
	                }
					
	                if (tempMap.get("BASE_LAYER") != null) {
	                    tempBean.setBaseLayer(tempMap.get("BASE_LAYER").toString());
	                    tempBean.setContractno1(tempMap.get("CONTRACT_NO") == null ? "" : tempMap.get("CONTRACT_NO").toString());
	                    tempBean.setLay1("layer");
	                } else {
	                    tempBean.setBaseLayer("");
	                }
	                tempBean.setUwYear(tempMap.get("UW_YEAR") == null ? "" : tempMap.get("UW_YEAR").toString());
	                tempBean.setUwMonth(tempMap.get("UW_MONTH") == null ? "" : tempMap.get("UW_MONTH").toString());
	        //        tempBean.setUnderwritter(tempMap.get("UNDERWRITTER") == null ? "" : tempMap.get("UNDERWRITTER").toString());
	                tempBean.setBrokerName(tempMap.get("BROKER_NAME") == null ? "" : tempMap.get("BROKER_NAME").toString());
	                tempBean.setOldContract(tempMap.get("OLD_CONTRACTNO") == null ? "" : "0".equals(tempMap.get("OLD_CONTRACTNO")) == true ? "" : tempMap.get("OLD_CONTRACTNO").toString());
	                tempBean.setButtonSelectionList(getConfimedButtonList(tempBean,beanObj.getMenuRights()));
	                finalList.add(tempBean);
	            }
	            res1.setConfirmedList(finalList);
	            response.setCommonResponse(res1);
	    		response.setMessage("Success");
	    		response.setIsError(false);
	            }  catch (Exception e) {
	            		e.printStackTrace();
	            		response.setMessage("Failed");
	            		response.setIsError(true);
	        }
	        return response;
		}

		private List<ButtonSelectionListRes> getConfimedButtonList(GetConfirmedListRes1 tempBean, List<String> menuRights) {
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			Map<String,Object> map=new HashMap<String,Object>();
			List<ButtonSelectionListRes> buttonList = new ArrayList<ButtonSelectionListRes>();
			try{
				
				
				if( menuRights.toString().contains("V")){
					map.put("TYPE","V");
					map.put("DETAIL_NAME","View");
					list.add(map);
					map=new HashMap<String,Object>();
				}
				if( menuRights.toString().contains("V")){
					map.put("TYPE","PL");
					map.put("DETAIL_NAME","Pl. Summar");
					list.add(map);
					map=new HashMap<String,Object>();
				}
				if( menuRights.toString().contains("V")){
					map.put("TYPE","C");
					map.put("DETAIL_NAME","Contract");
					list.add(map);
					map=new HashMap<String,Object>();
				}
				if(list.size()>0&&list!=null) {
	    			for(int i=0; i<list.size(); i++) {
	    				Map<String,Object> tempMap = (Map<String,Object>) list.get(i);
	    				ButtonSelectionListRes button = new ButtonSelectionListRes();
	    				button.setType(tempMap.get("TYPE")==null?"":tempMap.get("TYPE").toString());	
	    				button.setDetailName(tempMap.get("DETAIL_NAME")==null?"":tempMap.get("DETAIL_NAME").toString());
	    				buttonList.add(button);
	    			}
	    			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return buttonList;
		}
		
}
