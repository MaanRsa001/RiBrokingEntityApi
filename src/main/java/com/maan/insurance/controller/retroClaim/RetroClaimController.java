package com.maan.insurance.controller.retroClaim;

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
import com.maan.insurance.model.req.retroClaim.ContractDetailsMode1Req;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.model.res.retroClaim.ContractDetailsMode1Res;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes;
import com.maan.insurance.model.res.statistics.SlideScenarioRes;
import com.maan.insurance.service.retroClaim.RetroClaimService;
import com.maan.insurance.service.statistics.StatisticsService;
import com.maan.insurance.validation.retroClaim.RetroClaimValidation;
import com.maan.insurance.validation.statistics.StatisticsValidation;

@RestController
@RequestMapping("/Insurance/retroClaim")
public class RetroClaimController {
Gson gson = new Gson(); 
	
	@Autowired
	private RetroClaimService serv;
	@Autowired
	private RetroClaimValidation val;
	
	@PostMapping("/slideScenario")
	public ContractDetailsMode1Res contractDetailsMode1(@RequestBody ContractDetailsMode1Req req) throws CommonValidationException {
		List<ErrorCheck> error= val.contractDetailsMode1Vali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.contractDetailsMode1(req);
	}  
	@GetMapping("/getProposalNo/{departmentId}/{contractNo}/{layerNo}/{productId}")
	public CommonSaveRes getProposalNo(@PathVariable ("departmentId") String departmentId,@PathVariable ("contractNo") String contractNo,@PathVariable ("layerNo") String layerNo,@PathVariable ("productId") String productId) throws CommonValidationException {
		return serv.getProposalNo(departmentId,contractNo,layerNo,productId);
		} 
	@GetMapping("/getShortname/{branchCode}")
	public CommonSaveRes getShortname(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getShortname(branchCode);
		}  
}
