package com.maan.insurance.controller.home;

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
import com.maan.insurance.model.req.home.GetOldProductIdReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.home.GetDepartmentListRes;
import com.maan.insurance.model.res.home.GetFinalMenuListRes;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.service.home.HomeService;
import com.maan.insurance.validation.home.HomeValidation;

@RestController
@RequestMapping("/Insurance/home")
public class HomeController {
Gson gson = new Gson(); 
	@Autowired
	private HomeService serv;
	@Autowired
	private HomeValidation val;
	
	@PostMapping("/getOldProductId")
	public CommonSaveRes getOldProductId(@RequestBody GetOldProductIdReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getOldProductIdVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getOldProductId(req);
	}  

	@GetMapping("/getMenuDropDownList/{branchCode}/{userId}")
	public GetMenuDropDownListRes getMenuDropDownList(@PathVariable ("branchCode") String branchCode,@PathVariable ("userId") String userId) throws CommonValidationException {
		return serv.getMenuDropDownList(branchCode,userId);
		}     
	
	@GetMapping("/getDepartmentList/{branchCode}/{productId}")
		public GetCommonDropDownRes getDepartmentList(@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId) throws CommonValidationException {
			return serv.getDepartmentList(branchCode,productId);
			}   
	@GetMapping("/getProcessList/{branchCode}/{productId}/{deptId}")
		public GetCommonDropDownRes getProcessList(@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId,@PathVariable ("deptId") String deptId) throws CommonValidationException {
			return serv.getProcessList(branchCode,productId,deptId);
			}  
		
	@GetMapping("/getFinalMenuList/{userId}/{processId}")
		public GetFinalMenuListRes getFinalMenuList(@PathVariable ("userId") String userId,@PathVariable ("processId") String processId) throws CommonValidationException {
			return serv.getFinalMenuList(userId,processId);
			}    

}
