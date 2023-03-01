package com.maan.insurance.service.impl.home;

import java.io.InputStream;
import java.math.BigDecimal;
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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.model.entity.MenuMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasHomepageMaster;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.repository.TmasDepartmentMasterRepository;
import com.maan.insurance.model.repository.TmasHomepageMasterRepository;
import com.maan.insurance.model.repository.TmasLoginDetailsRepository;
import com.maan.insurance.model.req.home.GetOldProductIdReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.home.GetFinalMenuListRes;
import com.maan.insurance.model.res.home.GetFinalMenuListRes1;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes1;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.service.home.HomeService;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.Claim.ValidationImple;

@Service
public class HomeServiceImple implements HomeService{
	private Logger log = LogManager.getLogger(HomeServiceImple.class);
	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private TmasDepartmentMasterRepository dmRepo;
	@Autowired
	private TmasHomepageMasterRepository hmRepo;
	
	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();
	private Query query1 = null;

	public HomeServiceImple() {
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
	public GetMenuDropDownListRes getMenuDropDownList(String branchCode, String userId) {
		GetMenuDropDownListRes response = new GetMenuDropDownListRes();
		List<GetMenuDropDownListRes1> resList = new ArrayList<GetMenuDropDownListRes1>();
		try{
		//	String sql="SELECT PRODUCT_ID||'~'||DEPT_ID||'~'||PROCESS_ID||'~'||MENU_NAME ||'~'||MENU_URL||'~'||(SELECT TMAS_PRODUCT_NAME FROM TMAS_PRODUCT_MASTER WHERE TMAS_PRODUCT_ID=PRODUCT_ID AND BRANCH_CODE=? AND  TMAS_STATUS='1' )||'~'||(SELECT TMAS_DEPARTMENT_NAME FROM TMAS_DEPARTMENT_MASTER WHERE TMAS_PRODUCT_ID =PRODUCT_ID AND TMAS_DEPARTMENT_ID=DEPT_ID  AND BRANCH_CODE=? AND  TMAS_STATUS = 'H' ) ||'~'||( SELECT    PROCESS_NAME FROM   TMAS_HOMEPAGE_MASTER WHERE   STATUS = 'Y' AND PRODUCT_ID = MM.PRODUCT_ID AND DEPT_ID = MM.DEPT_ID AND PROCESS_ID=MM.PROCESS_ID AND BRANCH_CODE = ?)||'~'||MENU_ID MENUKEY,(SELECT TMAS_PRODUCT_NAME FROM TMAS_PRODUCT_MASTER WHERE TMAS_PRODUCT_ID=PRODUCT_ID AND BRANCH_CODE=? AND  TMAS_STATUS='1' )||'>'||(SELECT TMAS_DEPARTMENT_NAME||'>' FROM TMAS_DEPARTMENT_MASTER WHERE TMAS_PRODUCT_ID =PRODUCT_ID AND TMAS_DEPARTMENT_ID=DEPT_ID  AND BRANCH_CODE=? AND  TMAS_STATUS = 'H' ) ||( SELECT    PROCESS_NAME FROM   TMAS_HOMEPAGE_MASTER WHERE   STATUS = 'Y' AND PRODUCT_ID = MM.PRODUCT_ID AND DEPT_ID = MM.DEPT_ID AND PROCESS_ID=MM.PROCESS_ID AND BRANCH_CODE = ?)||'>'||MENU_NAME MENU_LIST FROM MENU_MASTER MM WHERE TYPE = 'User' AND ACTIVE='1' and menu_id in (SELECT   REGEXP_SUBSTR ( MENU_IDS,'[^,]+', 1, LEVEL) AS MENU_ID FROM   (SELECT   to_char(listagg(MENU_IDS)) MENU_IDS FROM   TMAS_LOGIN_DETAILS WHERE   LOGIN_ID = ?  AND ACTIVE = '1') CONNECT BY   LEVEL <= LENGTH (MENU_IDS)  - LENGTH (REPLACE (MENU_IDS, ',', '')) + 1) ORDER BY PRODUCT_ID,DEPT_ID,PROCESS_ID  ,ORDER_NO";
			List<String> menuIds = new ArrayList<String>();
			String query1 = "getMenuDropDownList_SubQuery";
			List<Map<String, Object>> sub = queryImpl.selectList(query1, new String[] {userId});
			if(sub.size()>0) {
				for(int i=0; i<sub.size();i++) {
				Map<String, Object> menu =sub.get(i);
				if(!(menu.get("MENU_ID")==null) && !(menu.get("MENU_ID")=="") ){
				String menuid = menu.get("MENU_ID").toString();
				menuIds.add(menuid);
				}
				}
			}
			
//			List<String> menuIds = new ArrayList<String>();
//			List<TmasLoginDetails>	list =	ldRepo.findByLoginIdAndActive(userId,"1");
//			for(TmasLoginDetails data: list) {
//				
//			List<String>   menuid =	 new ArrayList<String>(Arrays.asList(data.getMenuIds().split(",") ))  ;
//			menuid = menuid.stream().filter( o -> ! o.equalsIgnoreCase("")).collect(Collectors.toList());
//			 menuIds.addAll(menuid);
//			}
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<MenuMaster> pm = query.from(MenuMaster.class);

			// productName
			Subquery<String> productName = query.subquery(String.class); 
			Root<TmasProductMaster> pms = productName.from(TmasProductMaster.class);
			productName.select(pms.get("tmasProductName"));
			Predicate a1 = cb.equal( pm.get("productId"), pms.get("tmasProductId"));
			Predicate a2 = cb.equal( pms.get("branchCode"), branchCode);
			Predicate a3 = cb.equal( pms.get("tmasStatus"), "1");
			productName.where(a1,a2,a3);
			
			//TMAS_DEPARTMENT_NAME
			Subquery<String> deptName = query.subquery(String.class); 
			Root<TmasDepartmentMaster> dms = deptName.from(TmasDepartmentMaster.class);
			deptName.select(dms.get("tmasDepartmentName"));
			Predicate b1 = cb.equal( pm.get("productId"), dms.get("tmasProductId"));
			Predicate b2 = cb.equal( dms.get("branchCode"), branchCode);
			Predicate b3 = cb.equal( dms.get("tmasStatus"), "H");
			Predicate b4 = cb.equal( dms.get("tmasDepartmentId"), pm.get("deptId"));
			deptName.where(b1,b2,b3,b4);
			
			//processName
			Subquery<String> processName = query.subquery(String.class); 
			Root<TmasHomepageMaster> hm = processName.from(TmasHomepageMaster.class);
			processName.select(hm.get("processName"));
			Predicate c1 = cb.equal( pm.get("productId"), hm.get("productId"));
			Predicate c2 = cb.equal( hm.get("branchCode"), branchCode);
			Predicate c3 = cb.equal( hm.get("status"), "Y");
			Predicate c4 = cb.equal( hm.get("deptId"), pm.get("deptId"));
			Predicate c5 = cb.equal( hm.get("processId"), pm.get("processId"));
			processName.where(c1,c2,c3,c4,c4,c5);
			
			// Select
			query.multiselect(pm.get("productId").alias("productId"), pm.get("deptId").alias("deptId"), pm.get("processId").alias("processId"),
					pm.get("menuName").alias("menuName"), pm.get("menuUrl").alias("menuUrl"),productName.alias("productName"),
					deptName.alias("deptName"), processName.alias("processName"),pm.get("menuId").alias("menuId")); 

			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("productId")));
			orderList.add(cb.asc(pm.get("deptId")));
			orderList.add(cb.asc(pm.get("processId")));
			orderList.add(cb.asc(pm.get("orderNo")));
		
			//In 
			Expression<BigDecimal>e0=pm.get("menuId");
			
			// Where
			Predicate n1 = cb.equal(pm.get("type"), "User");
			Predicate n2 = cb.equal(pm.get("active"), "1");
			Predicate n3 = e0.in(menuIds);
		
			query.where(n1,n2,n3).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list1 = result.getResultList();
		if(list1.size()>0) {
			for(Tuple data: list1) {
				GetMenuDropDownListRes1 res = new GetMenuDropDownListRes1();
				res.setMenuKey(data.get("productId") + "~" + data.get("deptId") + "~" + data.get("processId") + "~" + data.get("menuName") + "~" 
						+ data.get("menuUrl") + "~" + data.get("productName") + "~" + (data.get("deptName")==null?"":data.get("deptName")) + "~" + data.get("processName") + "~" 
						+ data.get("menuId"));
				res.setMenuList(data.get("productName") + ">" + (data.get("deptName")==null?"":data.get("deptName")+ ">" ) + data.get("processName") + ">" 
						+data.get("menuName"));
				resList.add(res);
				}
		}
		//	result=this.mytemplate.queryForList(sql,args);
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
	public GetCommonDropDownRes getDepartmentList(String branchCode, String productId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
		//USER_SELECT_DEPARTMENTMAP
			List<TmasDepartmentMaster> list = dmRepo.findByTmasStatusAndTmasProductIdAndBranchCodeOrderByTmasDepartmentNameAsc("H",new BigDecimal(productId),branchCode);
			if(list.size()>0) {
				for(TmasDepartmentMaster data: list) {
					CommonResDropDown res = new CommonResDropDown();
					res.setCodeDescription(data.getTmasDepartmentName());
					res.setCode(data.getTmasDepartmentId().toString());
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
	public GetCommonDropDownRes getProcessList(String branchCode, String productId, String deptId) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
		//USER_SELECT_PROCESSMAP
			List<TmasHomepageMaster> list = hmRepo.findByStatusAndProductIdAndDeptIdAndBranchCodeOrderByOrderNoAsc("Y",new BigDecimal(productId),new BigDecimal(deptId),branchCode);
			if(list.size()>0) {
				for(TmasHomepageMaster data: list) {
					CommonResDropDown res = new CommonResDropDown();
					res.setCodeDescription(data.getProcessName());
					res.setCode(data.getProcessId().toString());
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
	public GetFinalMenuListRes getFinalMenuList(String userId, String processId) {
		GetFinalMenuListRes response = new GetFinalMenuListRes();
		List<GetFinalMenuListRes1> resList = new ArrayList<GetFinalMenuListRes1>();
		try{
			String sql= "user.select.finalMenuList";
			List<Map<String, Object>> list = queryImpl.selectList(sql,new String[] {userId,processId,processId});
		if(list.size()>0) {
			for(int i=0; i<list.size();i++) {
				Map<String, Object> resMap = (Map<String, Object>)list.get(i);
				GetFinalMenuListRes1 res = new GetFinalMenuListRes1();
				res.setMenuId(resMap.get("MENU_ID")==null?"":resMap.get("MENU_ID").toString());
				res.setMenuName(resMap.get("MENU_NAME")==null?"":resMap.get("MENU_NAME").toString());
				res.setMsenuUrl(resMap.get("MENU_URL")==null?"":resMap.get("MENU_URL").toString());
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
	public CommonSaveRes getOldProductId(GetOldProductIdReq req) {
		CommonSaveRes response = new CommonSaveRes();
		String oldProductId ="";
		try{
			//USER_SELECT_GETOLDPRODUCTID
			List<TmasHomepageMaster> list = hmRepo.findByProductIdAndDeptIdAndProcessIdAndBranchCodeAndStatus(new BigDecimal(req.getProductId()),new BigDecimal(req.getDepartmentId()),new BigDecimal(req.getProcessId()),req.getBranchCode(),"Y");
	 if(list.size()>0) {
		 oldProductId = list.get(0).getOldProductId()==null?"":list.get(0).getOldProductId().toString();
	 }
			response.setResponse(oldProductId);	
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
