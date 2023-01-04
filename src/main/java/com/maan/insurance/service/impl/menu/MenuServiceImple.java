package com.maan.insurance.service.impl.menu;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasHomepageMaster;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.repository.TmasDepartmentMasterRepository;
import com.maan.insurance.model.repository.TmasHomepageMasterRepository;
import com.maan.insurance.model.repository.TmasProductMasterRepository;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.menu.GetAdminMenuRes;
import com.maan.insurance.model.res.menu.GetAdminMenuRes1;
import com.maan.insurance.model.res.menu.GetRigthsOfProcessRes;
import com.maan.insurance.model.res.menu.GetRigthsOfProcessRes1;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.menu.MenuService;
import com.maan.insurance.validation.Formatters;

@Service
public class MenuServiceImple implements MenuService{
	private Logger log = LogManager.getLogger(MenuServiceImple.class);

	@Autowired
	private QueryImplemention queryImpl;

	@Autowired
	private DropDownServiceImple dropDowmImpl;

	@Autowired
	private Formatters fm;
	
	@Autowired
	private TmasProductMasterRepository pmRepo;
	
	@Autowired
	private TmasDepartmentMasterRepository dmRepo;
	
	@Autowired
	private TmasHomepageMasterRepository hmRepo;
	
	@PersistenceContext
	private EntityManager em;
	
	private Properties prop = new Properties();
	private Query query1 = null;

	public MenuServiceImple() {
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
	public GetAdminMenuRes getAdminMenu(String loginId) {
		GetAdminMenuRes response = new GetAdminMenuRes();
		List<GetAdminMenuRes1> resList = new ArrayList<GetAdminMenuRes1>();
		try {
		  String query;
			//MENUDAO_SELECT_ADMINUSERMENUS
			query = "menudao.select.adminuserMenus";
			List<Map<String, Object>> list = queryImpl.selectList(query,new String[] {loginId});
			if(list.size()>0) {
			for(int i=0; i<list.size();i++){
			 Map<String, Object> tempMap = list.get(i);
				GetAdminMenuRes1  menuvb = new GetAdminMenuRes1();
				menuvb.setContent(tempMap.get("MENU_NAME")==null?"":tempMap.get("MENU_NAME").toString());
				menuvb.setUrl(tempMap.get("MENU_URL")==null?"":tempMap.get("MENU_URL").toString());
				resList.add(menuvb);
			}
			}
			GetAdminMenuRes1 menu = new GetAdminMenuRes1();
			menu.setContent("Logout");
			menu.setUrl("login.do?method=logout");
			resList.add(menu);
			
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
	public CommonSaveRes getManuName(String mfrId, String branchCode) {
		CommonSaveRes response = new CommonSaveRes();
		String manuName ="";
		try {
		//MENUDAO_SELECT_MENUQUERY
		TmasProductMaster entity = pmRepo.findByTmasProductIdAndBranchCodeAndTmasStatus(new BigDecimal(mfrId),branchCode,"1");
		if(entity != null) {
			manuName =	entity.getTmasProductName()==null?"":entity.getTmasProductName().toString();
		}
		response.setResponse(manuName);
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
	public GetRigthsOfProcessRes getRigthsOfProcess(String loginId, String menuId, String processId) {
		GetRigthsOfProcessRes response = new GetRigthsOfProcessRes();
		List<GetRigthsOfProcessRes1> resList = new ArrayList<GetRigthsOfProcessRes1>();
		try{
			if(menuId!=null && !"null".equals(menuId.trim()) && !"".equals(menuId.trim())){
				//MENUDAO_SELECT_GETRIGTHSOFPROCESSID
				String sql= "menudao.select.getRigthsOfProcessId";
				List<Map<String, Object>> list = queryImpl.selectList(sql,new String[] {menuId,processId,menuId,processId,loginId});
				if(list.size()>0) {
					for(int i=0; i<list.size();i++){
					 Map<String, Object> tempMap = list.get(i);
					 GetRigthsOfProcessRes1  menuvb = new GetRigthsOfProcessRes1();
						menuvb.setSubMenuName(tempMap.get("SUB_MENU_NAME")==null?"":tempMap.get("SUB_MENU_NAME").toString());
						menuvb.setSubMenuCode(tempMap.get("SUB_MENU_CODE")==null?"":tempMap.get("SUB_MENU_CODE").toString());
						resList.add(menuvb);
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
	public GetCommonDropDownRes getDepartmentListNew(String productId, String branchCode) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			//USER_SELECT_DEPARTMENTMAP
			List<TmasDepartmentMaster> entityList = dmRepo.findByTmasProductIdAndBranchCodeAndTmasStatusOrderByTmasDepartmentName(new BigDecimal(productId),branchCode,"H");
			for(TmasDepartmentMaster data: entityList){
				CommonResDropDown res = new CommonResDropDown();
				res.setCode(data.getTmasDepartmentId()==null?"":data.getTmasDepartmentId().toString());
				res.setCodeDescription(data.getTmasDepartmentName()==null?"":data.getTmasDepartmentName().toString());
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

}
