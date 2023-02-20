package com.maan.insurance.service.impl.statistics;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.model.entity.TtrnRiskCommission;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.req.statistics.GetColumnHeaderlistReq;
import com.maan.insurance.model.req.statistics.GetRenewalStatisticsListReq;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;
import com.maan.insurance.model.res.statistics.GetColumnHeaderlistRes;
import com.maan.insurance.model.res.statistics.GetColumnHeaderlistRes1;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes1;
import com.maan.insurance.model.res.statistics.GetRenewalStatisticsListRes;
import com.maan.insurance.model.res.statistics.GetRenewalStatisticsListRes1;
import com.maan.insurance.model.res.statistics.SlideScenarioRes;
import com.maan.insurance.model.res.statistics.SlideScenarioRes1;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.statistics.StatisticsService;
@Service
public class StatisticsServiceImple implements StatisticsService{
	private Logger log = LogManager.getLogger(StatisticsServiceImple.class);

	@Autowired
	private QueryImplemention queryImpl;



	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();
	private Query query1 = null;

	public StatisticsServiceImple() {
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
	public SlideScenarioRes slideScenario(SlideScenarioReq bean) {
		SlideScenarioRes response = new SlideScenarioRes();
		SlideScenarioRes1 res = new SlideScenarioRes1();
		String slide  = "";
		String combine ="";
		String premium ="";
		String preCombine="";
		String loss ="";
		String lossCombine="";
		String proposalNo = "";
		String bonus="";
		try{
			 CriteriaBuilder cb = em.getCriteriaBuilder(); 
			if("2".equalsIgnoreCase(bean.getProductId())){
             
              //GET_SLIDE_COMM_VALUE
    			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
    			
    			// like table name
    			Root<TtrnRiskCommission> pm = query.from(TtrnRiskCommission.class);

    			// Select
    			query.multiselect(pm.get("rskSlideCombinSubClass").alias("RSK_SLIDE_COMBIN_SUB_CLASS"),pm.get("rskSladscaleComm").alias("RSK_SLADSCALE_COMM"),
    					pm.get("rskProfitComm").alias("RSK_PROFIT_COMM"),pm.get("rskCombinSubClass").alias("RSK_COMBIN_SUB_CLASS"),
    					pm.get("rskLossPartCarridor").alias("RSK_LOSS_PART_CARRIDOR"),pm.get("rskLossCombinSubClass").alias("RSK_LOSS_COMBIN_SUB_CLASS"));

    			// MAXAmend ID
    			Subquery<Long> maxEnd = query.subquery(Long.class); 
    			Root<TtrnRiskCommission> pms = maxEnd.from(TtrnRiskCommission.class);
    			maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
    			Predicate a1 = cb.equal( pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
    			Predicate a2 = cb.equal( pm.get("subClass"), pms.get("subClass"));
    			maxEnd.where(a1,a2);

    			// Where
    			Predicate n1 = cb.equal(pm.get("rskProposalNumber"), bean.getProposalNo());
    			Predicate n2 = cb.equal(pm.get("subClass"), bean.getDepartmentId());
    			Predicate n3 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
    			query.where(n1,n2,n3);
    			
    			// Get Result
    			TypedQuery<Tuple> result = em.createQuery(query);
    			List<Tuple> list = result.getResultList();
    			if(list.size()>0) {
					Tuple map = list.get(0);
					
					 slide  = map.get("RSK_SLADSCALE_COMM")==null?"": map.get("RSK_SLADSCALE_COMM").toString();
					 combine  =map.get("RSK_SLIDE_COMBIN_SUB_CLASS")==null?"": map.get("RSK_SLIDE_COMBIN_SUB_CLASS").toString();
					 premium = map.get("RSK_PROFIT_COMM")==null?"":map.get("RSK_PROFIT_COMM").toString();
					 preCombine =map.get("RSK_COMBIN_SUB_CLASS")==null?"":map.get("RSK_COMBIN_SUB_CLASS").toString();
					 loss =map.get("RSK_LOSS_PART_CARRIDOR")==null?"": map.get("RSK_LOSS_PART_CARRIDOR").toString();
					 lossCombine =map.get("RSK_LOSS_COMBIN_SUB_CLASS")==null?"":map.get("RSK_LOSS_COMBIN_SUB_CLASS").toString();
					 
					if(slide.equalsIgnoreCase("Y")){
                        if(combine.equalsIgnoreCase("1")){
                        	res.setSlideScenario("one");
                        	res.setSlidedepartId("ALL");
                        }
                        else if(combine.equalsIgnoreCase("2")){
                        	res.setSlideScenario("two");
                        }
					}
					if(premium.equalsIgnoreCase("1")){
						if(preCombine.equalsIgnoreCase("1")){
							res.setPremiumScenario("one");
							res.setPremiumdepartId("ALL");
						}
						else if(preCombine.equalsIgnoreCase("2")){
							res.setPremiumScenario("two");
                        }
					}
					if(loss.equalsIgnoreCase("Y")){
						if(lossCombine.equalsIgnoreCase("1")){
							res.setLossScenario("one");
							res.setLossdepartId("ALL");
						}
						else if(lossCombine.equalsIgnoreCase("2")){
							res.setLossScenario("two");
                        }
					}
				}
				if(combine.equalsIgnoreCase("2") || preCombine.equalsIgnoreCase("2")|| lossCombine.equalsIgnoreCase("2")) {
                //GET_SLIDE_COMM_VALUE2
					if(list.size()>0) {
							Tuple map = list.get(0);
						
						 slide  = map.get("RSK_SLADSCALE_COMM")==null?"":map.get("RSK_SLADSCALE_COMM").toString();
						 premium = map.get("RSK_PROFIT_COMM")==null?"":map.get("RSK_PROFIT_COMM").toString();
						 loss = map.get("RSK_LOSS_PART_CARRIDOR")==null?"":map.get("RSK_LOSS_PART_CARRIDOR").toString();
						if(slide.equalsIgnoreCase("Y") ){
                            res.setSlideScenario("three");
						}
						if(premium.equalsIgnoreCase("1")){
							res.setSlideScenario("three");
						}
						if(loss.equalsIgnoreCase("Y") ){
                            res.setSlideScenario("three");
						}
					}
			}
			}	else if("1".equalsIgnoreCase(bean.getProductId()) ||"3".equalsIgnoreCase(bean.getProductId())){
				//GET_BONUS_ID
				CriteriaQuery<String> query1 = cb.createQuery(String.class); 
    			
    			// like table name
    			Root<TtrnRiskCommission> rc = query1.from(TtrnRiskCommission.class);

    			// Select
    			query1.select(rc.get("rskBonusId"));
    		 

    			// MAXAmend ID
    			Subquery<Long> maxEnd1 = query1.subquery(Long.class); 
    			Root<TtrnRiskCommission> rcs = maxEnd1.from(TtrnRiskCommission.class);
    			maxEnd1.select(cb.max(rcs.get("rskEndorsementNo")));
    			Predicate b1 = cb.equal( rc.get("rskProposalNumber"), rcs.get("rskProposalNumber"));
    			maxEnd1.where(b1);
    			
    			if("3".equalsIgnoreCase(bean.getProductId())){
    			Predicate m1 = cb.equal(rc.get("rskProposalNumber"), bean.getProposalNo());
    			Predicate m2 = cb.equal(rc.get("rskLayerNo"), bean.getLayerNo());
    			Predicate m3 = cb.equal(rc.get("rskEndorsementNo"), maxEnd1);
    			query1.where(m1,m2,m3);
    			}else {
    				Predicate m1 = cb.equal(rc.get("rskProposalNumber"), bean.getProposalNo());
    				Predicate m3 = cb.equal(rc.get("rskEndorsementNo"), maxEnd1);
    				query1.where(m1,m3);
    				}
    			
    			
    			// Get Result
    			TypedQuery<String> result1 = em.createQuery(query1);
    			List<String> list1 = result1.getResultList();
			if(list1.size()>0) {
				res.setBonusStatus(list1.get(0));
			}
			}
		//	getCurrencyName(bean);
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
	public GetCurrencyNameRes getCurrencyName(String proposalNo, String contractNo, String productId) {
		GetCurrencyNameRes response = new GetCurrencyNameRes();
		GetCurrencyNameRes1 bean = new GetCurrencyNameRes1();
		try{
			//GET_CURRENCY_ID
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query1 = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> pm = query1.from(TtrnRiskDetails.class);

			query1.multiselect(pm.get("rskOriginalCurr").alias("RSK_ORIGINAL_CURR"), pm.get("rskEndorsementNo").alias("RSK_ENDORSEMENT_NO"));

			// MAXAmend ID
			Subquery<Long> maxEnd = query1.subquery(Long.class); 
			Root<TtrnRiskDetails> pms = maxEnd.from(TtrnRiskDetails.class);
			maxEnd.select(cb.max(pms.get("rskEndorsementNo")));
			Predicate y1 = cb.equal( pm.get("rskProposalNumber"), pms.get("rskProposalNumber"));
			Predicate y2 = cb.equal( pm.get("rskContractNo"), pms.get("rskContractNo"));
			maxEnd.where(y1,y2);

			// Where
			Predicate x1 = cb.equal(pm.get("rskProposalNumber"),proposalNo);
			Predicate x2 = cb.equal(pm.get("rskEndorsementNo"), maxEnd);
			Predicate x3 = cb.equal(pm.get("rskContractNo"),contractNo);
			query1.where(x1,x2,x3);
			
			// Get Result
			TypedQuery<Tuple> res2 = em.createQuery(query1);
			List<Tuple> result = res2.getResultList();
			
			if(result.size()>0) {
				Tuple map = result.get(0);
			bean.setAmendId(map.get("RSK_ENDORSEMENT_NO")==null?"":map.get("RSK_ENDORSEMENT_NO").toString());
			if("2".equalsIgnoreCase(productId)){
				bean.setSlideCurrency(map.get("RSK_ORIGINAL_CURR")==null?"":map.get("RSK_ORIGINAL_CURR").toString());
				bean.setProfitCurrency(map.get("RSK_ORIGINAL_CURR")==null?"":map.get("RSK_ORIGINAL_CURR").toString());
				bean.setLossCurrency(map.get("RSK_ORIGINAL_CURR")==null?"":map.get("RSK_ORIGINAL_CURR").toString());
			}
			else if("1".equalsIgnoreCase(productId) || "3".equalsIgnoreCase(productId)){
				bean.setBonusCurrency(map.get("RSK_ORIGINAL_CURR")==null?"":map.get("RSK_ORIGINAL_CURR").toString());
			}
			}
			response.setCommonResponse(bean);
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
	public GetColumnHeaderlistRes getColumnHeaderlist(GetColumnHeaderlistReq bean) {
		GetColumnHeaderlistRes response = new GetColumnHeaderlistRes();
		GetColumnHeaderlistRes1 res = new GetColumnHeaderlistRes1();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> ColumnHeaderlis=new ArrayList<Map<String,Object>>();
		String query ="";
		try{
			Map<String,Object>map= new HashMap<String, Object>();
			map.put("RANGE", "Item");
			ColumnHeaderlis.add(map);
			map=new HashMap<String,Object>();
			map.put("RANGE", "Contract No");
			ColumnHeaderlis.add(map);
			map=new HashMap<String,Object>();
			map.put("RANGE", "UWY");
			ColumnHeaderlis.add(map);
			query= "GET_COLUMN_HEADER";
			String args[]=new String[5];
			args[0] = bean.getPeriodFrom();
			args[1] = bean.getPeriodFrom();
			args[2] = bean.getPeriodFrom();
			args[3] = bean.getPeriodFrom();
			args[4] = bean.getPeriodTo();
			list= queryImpl.selectList(query,args);
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String,Object> map1=list.get(i);
					ColumnHeaderlis.add(map1);
				}
			}
			map=new HashMap<String,Object>();
			map.put("RANGE", "Total");
			ColumnHeaderlis.add(map);
			ColumnHeaderlis.toString();
			res.setRange(ColumnHeaderlis);
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
	public GetRenewalStatisticsListRes getRenewalStatisticsList(GetRenewalStatisticsListReq bean) {
		GetRenewalStatisticsListRes response = new GetRenewalStatisticsListRes();
		List<GetRenewalStatisticsListRes1> resList = new ArrayList<GetRenewalStatisticsListRes1>();
		try {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String query ="";
		String args[]=null;
		query= "GET_RENEWAL_STATISTICS_LIST";
		args=new String[9];
		args[0] = bean.getPeriodFrom();
		args[1] = bean.getPeriodTo();
		args[2] = bean.getUwFrom();
		args[3] = bean.getUwTo();
		args[4] = bean.getContractNo();
		args[5] = bean.getBranchCode();
		args[6] = bean.getType();
		args[7] = bean.getUwFrom();
		args[8] = bean.getUwTo();
		list= queryImpl.selectList(query,args);
		if(list!=null && list.size()>0){
			GetRenewalStatisticsListRes1 res=new GetRenewalStatisticsListRes1();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> insMap = (Map<String, Object>)list.get(i);
				res.setItem(insMap.get("ITEM")==null?" ":insMap.get("ITEM").toString());
				res.setCnt(insMap.get("ITEM")==null?" ":insMap.get("ITEM").toString());
				res.setContractNo(insMap.get("CONTRACT_NO")==null?" ":insMap.get("CONTRACT_NO").toString());
				res.setUwYear(insMap.get("UW_YEAR")==null?" ":insMap.get("UW_YEAR").toString());
				res.setM1(insMap.get("M1")==null?" ":insMap.get("M1").toString());
				res.setM2(insMap.get("M2")==null?" ":insMap.get("M2").toString());
				res.setM3(insMap.get("M3")==null?" ":insMap.get("M3").toString());
				res.setM4(insMap.get("M4")==null?" ":insMap.get("M4").toString());
				res.setM5(insMap.get("M5")==null?" ":insMap.get("M5").toString());
				res.setM6(insMap.get("M6")==null?" ":insMap.get("M6").toString());
				res.setM7(insMap.get("M7")==null?" ":insMap.get("M7").toString());
				res.setM8(insMap.get("M8")==null?" ":insMap.get("M8").toString());
				res.setM9(insMap.get("M9")==null?" ":insMap.get("M9").toString());
				res.setM10(insMap.get("M10")==null?" ":insMap.get("M10").toString());
				res.setM11(insMap.get("M11")==null?" ":insMap.get("M11").toString());
				res.setM12(insMap.get("M12")==null?" ":insMap.get("M12").toString());
				res.setM13(insMap.get("M13")==null?" ":insMap.get("M13").toString());
				res.setM14(insMap.get("M14")==null?" ":insMap.get("M14").toString());
				res.setM15(insMap.get("M15")==null?" ":insMap.get("M15").toString());
				res.setM16(insMap.get("M16")==null?" ":insMap.get("M16").toString());
				res.setM17(insMap.get("M17")==null?" ":insMap.get("M17").toString());
				res.setM18(insMap.get("M18")==null?" ":insMap.get("M18").toString());
				res.setM19(insMap.get("M19")==null?" ":insMap.get("M19").toString());
				res.setM20(insMap.get("M20")==null?" ":insMap.get("M20").toString());
				res.setTotal(insMap.get("TOTAL")==null?" ":insMap.get("TOTAL").toString());
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
	public GetRenewalStatisticsListRes getRenewalCalStatisticsList(GetRenewalStatisticsListReq bean) {
		GetRenewalStatisticsListRes response = new GetRenewalStatisticsListRes();
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String query ="";
		String args[]=null;
		List<GetRenewalStatisticsListRes1> resList = new ArrayList<GetRenewalStatisticsListRes1>();
		try {
		if("treaty".equals(bean.getType())){
		query= "GET_TREATY_RESULT_UW";
		}else if("calimratio".equals(bean.getType())){
			query= "GET_CLAIM_RATIO_UW";
		}else if("combined".equals(bean.getType())){
			query= "GET_COMINED_RATIO_UW";
		}
		args=new String[32];
		args[0] = bean.getPeriodFrom();
		args[1] = bean.getPeriodTo();
		args[2] = bean.getUwFrom();
		args[3] = bean.getUwTo();
		args[4] = bean.getContractNo();
		args[5] = bean.getBranchCode();
		args[6] = bean.getUwFrom();
		args[7] = bean.getUwTo();
		
		args[8] = bean.getPeriodFrom();
		args[9] = bean.getPeriodTo();
		args[10] = bean.getUwFrom();
		args[11] = bean.getUwTo();
		args[12] = bean.getContractNo();
		args[13] = bean.getBranchCode();
		args[14] = bean.getUwFrom();
		args[15] = bean.getUwTo();
		
		args[16] = bean.getPeriodFrom();
		args[17] = bean.getPeriodTo();
		args[18] = bean.getUwFrom();
		args[19] = bean.getUwTo();
		args[20] = bean.getContractNo();
		args[21] = bean.getBranchCode();
		args[22] = bean.getUwFrom();
		args[23] = bean.getUwTo();
		
		args[24] = bean.getPeriodFrom();
		args[25] = bean.getPeriodTo();
		args[26] = bean.getUwFrom();
		args[27] = bean.getUwTo();
		args[28] = bean.getContractNo();
		args[29] = bean.getBranchCode();
		args[30] = bean.getUwFrom();
		args[31] = bean.getUwTo();
		list= queryImpl.selectList(query,args);
		if(list!=null && list.size()>0){
			GetRenewalStatisticsListRes1 res=new GetRenewalStatisticsListRes1();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> insMap = (Map<String, Object>)list.get(i);
				res.setItem(insMap.get("ITEM")==null?" ":insMap.get("ITEM").toString());
				res.setCnt(insMap.get("ITEM")==null?" ":insMap.get("ITEM").toString());
				res.setContractNo(insMap.get("CONTRACT_NO")==null?" ":insMap.get("CONTRACT_NO").toString());
				res.setUwYear(insMap.get("UW_YEAR")==null?" ":insMap.get("UW_YEAR").toString());
				res.setM1(insMap.get("M1")==null?" ":insMap.get("M1").toString());
				res.setM2(insMap.get("M2")==null?" ":insMap.get("M2").toString());
				res.setM3(insMap.get("M3")==null?" ":insMap.get("M3").toString());
				res.setM4(insMap.get("M4")==null?" ":insMap.get("M4").toString());
				res.setM5(insMap.get("M5")==null?" ":insMap.get("M5").toString());
				res.setM6(insMap.get("M6")==null?" ":insMap.get("M6").toString());
				res.setM7(insMap.get("M7")==null?" ":insMap.get("M7").toString());
				res.setM8(insMap.get("M8")==null?" ":insMap.get("M8").toString());
				res.setM9(insMap.get("M9")==null?" ":insMap.get("M9").toString());
				res.setM10(insMap.get("M10")==null?" ":insMap.get("M10").toString());
				res.setM11(insMap.get("M11")==null?" ":insMap.get("M11").toString());
				res.setM12(insMap.get("M12")==null?" ":insMap.get("M12").toString());
				res.setM13(insMap.get("M13")==null?" ":insMap.get("M13").toString());
				res.setM14(insMap.get("M14")==null?" ":insMap.get("M14").toString());
				res.setM15(insMap.get("M15")==null?" ":insMap.get("M15").toString());
				res.setM16(insMap.get("M16")==null?" ":insMap.get("M16").toString());
				res.setM17(insMap.get("M17")==null?" ":insMap.get("M17").toString());
				res.setM18(insMap.get("M18")==null?" ":insMap.get("M18").toString());
				res.setM19(insMap.get("M19")==null?" ":insMap.get("M19").toString());
				res.setM20(insMap.get("M20")==null?" ":insMap.get("M20").toString());
				res.setTotal(insMap.get("TOTAL")==null?" ":insMap.get("TOTAL").toString());
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
	
}
