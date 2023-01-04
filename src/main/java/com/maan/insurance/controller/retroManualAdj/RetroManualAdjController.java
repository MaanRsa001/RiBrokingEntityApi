package com.maan.insurance.controller.retroManualAdj;

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
import com.maan.insurance.model.req.journal.InsertInActiveOpenPeriodReq;
import com.maan.insurance.model.req.retroManualAdj.InsertPremiumReq;
import com.maan.insurance.model.res.journal.GetjounalListRes;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.retroManualAdj.GetPremiumDetailsRes;
import com.maan.insurance.model.res.retroManualAdj.GetRetroDetailsRes;
import com.maan.insurance.model.res.retroManualAdj.GetRetroManualAdjlistRes;
import com.maan.insurance.model.res.retroManualAdj.PremiumEditRes;
import com.maan.insurance.service.journal.JournalService;
import com.maan.insurance.service.retroManualAdj.RetroManualAdjService;
import com.maan.insurance.validation.journal.JournalValidation;
import com.maan.insurance.validation.retroManualAdj.RetroManualAdjValidation;

@RestController
@RequestMapping("/Insurance/retromanualadj")
public class RetroManualAdjController {
Gson gson = new Gson(); 
	
	@Autowired
	private RetroManualAdjService serv;
	@Autowired
	private RetroManualAdjValidation val;
	
	@PostMapping("/InsertPremium")
	public CommonSaveRes InsertPremium(@RequestBody InsertPremiumReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.validateProportionPremium(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.InsertPremium(req);
	}  

	@GetMapping("/getRetroManualAdjlist/{branchCode}/{productId}")
	public GetRetroManualAdjlistRes getRetroManualAdjlist(@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId) throws CommonValidationException {
		return serv.getRetroManualAdjlist(branchCode,productId);
		}    
	@GetMapping("/PremiumEdit/{contractNo}/{transactionNo}/{countyId}/{branchCode}")
		public PremiumEditRes premiumEdit(@PathVariable ("contractNo") String contractNo,@PathVariable ("transactionNo") String transactionNo,@PathVariable ("countyId") String countyId,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
			return serv.premiumEdit(contractNo,transactionNo,countyId,branchCode);
			} 
		@GetMapping("/getPremiumDetails/{contractNo}/{transactionNo}/{branchCode}")
			public GetPremiumDetailsRes getPremiumDetails(@PathVariable ("contractNo") String contractNo,@PathVariable ("transactionNo") String transactionNo,@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
				return serv.getPremiumDetails(contractNo,transactionNo,branchCode);
		} 
	@GetMapping("/getRetroDetails/{branchCode}/{contractNo}")
	public GetRetroDetailsRes getRetroDetails(@PathVariable ("branchCode") String branchCode,@PathVariable ("contractNo") String contractNo) throws CommonValidationException {
		return serv.getRetroDetails(branchCode,contractNo);
		}     
	
} 
