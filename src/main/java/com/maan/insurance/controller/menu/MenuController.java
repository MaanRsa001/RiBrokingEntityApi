package com.maan.insurance.controller.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.authentication.AuthenticationListReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.authentication.AuthenticationListRes;
import com.maan.insurance.model.res.menu.GetAdminMenuRes;
import com.maan.insurance.model.res.menu.GetRigthsOfProcessRes;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.service.authentication.AuthenticationService;
import com.maan.insurance.service.menu.MenuService;
import com.maan.insurance.validation.authentication.AuthenticationValidation;
import com.maan.insurance.validation.menu.MenuValidation;

@RestController
@RequestMapping("/Insurance/menu")
public class MenuController {
Gson gson = new Gson(); 
	
	@Autowired
	private  MenuService serv;
	@Autowired
	private  MenuValidation val;
	
//	@PostMapping("/authenticationList")
//	public AuthenticationListRes insertInActiveOpenPeriod(@RequestBody AuthenticationListReq req) throws CommonValidationException {
//		List<ErrorCheck> error= val.authenticationListVali(req);
//		if(error!=null && error.size()>0) {
//			throw new CommonValidationException("error",error);
//		}
//		return serv.authenticationList(req);
//	}  
 
	@GetMapping("/getAdminMenu/{loginId}")
	public GetAdminMenuRes getAdminMenu(@PathVariable ("loginId") String loginId) throws CommonValidationException {
		return serv.getAdminMenu(loginId);
		}  
	@GetMapping("/getManuName/{mfrId}/{branchCode}")
		public CommonSaveRes getManuName(@PathVariable ("mfrId") String mfrId,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
			return serv.getManuName(mfrId,branchCode);
			}  
	@GetMapping("/getRigthsOfProcess/{loginId}/{menuId}/{processId}")
		public GetRigthsOfProcessRes getRigthsOfProcess(@PathVariable ("loginId") String loginId,@PathVariable ("menuId") String menuId,@PathVariable ("processId") String processId) throws CommonValidationException {
			return serv.getRigthsOfProcess(loginId,menuId,processId);
			}  
	@GetMapping("/getDepartmentListNew/{productId}/{branchCode}")
		public GetCommonDropDownRes getDepartmentListNew(@PathVariable ("productId") String productId,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getDepartmentListNew(productId,branchCode);
		}
	@GetMapping("/getProcessList/{branchCode}/{productId}/{deptId}")
	public GetCommonDropDownRes getProcessList(@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId,@PathVariable ("deptId") String deptId) throws CommonValidationException {
		return serv.getProcessList(branchCode,productId,deptId);
		} 
}
