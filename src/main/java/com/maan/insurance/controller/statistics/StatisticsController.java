package com.maan.insurance.controller.statistics;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maan.insurance.controller.XolPremium.Xolpremiumcontroller;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.adjustment.GetAdjustmentListReq;
import com.maan.insurance.model.req.statistics.GetColumnHeaderlistReq;
import com.maan.insurance.model.req.statistics.GetRenewalStatisticsListReq;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.statistics.GetColumnHeaderlistRes;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes;
import com.maan.insurance.model.res.statistics.GetRenewalStatisticsListRes;
import com.maan.insurance.model.res.statistics.SlideScenarioRes;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;
import com.maan.insurance.service.retro.RetroService;
import com.maan.insurance.service.statistics.StatisticsService;
import com.maan.insurance.validation.retro.RetroValidation;
import com.maan.insurance.validation.statistics.StatisticsValidation;

@RestController
@RequestMapping("/Insurance/statistics")
public class StatisticsController {
	Gson gson = new Gson(); 
	
	@Autowired
	private StatisticsService statServ;
	@Autowired
	private StatisticsValidation statVali;
	
	@PostMapping("/slideScenario")
	public SlideScenarioRes addAdjustmentPayRec(@RequestBody SlideScenarioReq req) throws CommonValidationException {
		List<ErrorCheck> error= statVali.slideScenarioVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return statServ.slideScenario(req);
	}  
	@GetMapping("/getCurrencyName/{proposalNo}/{contractNo}/{productId}")
	public GetCurrencyNameRes getCurrencyName(@PathVariable ("proposalNo") String proposalNo,@PathVariable ("contractNo") String contractNo,@PathVariable ("productId") String productId) throws CommonValidationException {
		return statServ.getCurrencyName(proposalNo,contractNo,productId);
		} 
	@PostMapping("/getColumnHeaderlist")
		public GetColumnHeaderlistRes getColumnHeaderlist(@RequestBody GetColumnHeaderlistReq req) throws CommonValidationException {
			List<ErrorCheck> error= statVali.getColumnHeaderlistVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return statServ.getColumnHeaderlist(req);
		}  
		@PostMapping("/getRenewalStatisticsList")
		public GetRenewalStatisticsListRes getRenewalStatisticsList(@RequestBody GetRenewalStatisticsListReq req) throws CommonValidationException {
			List<ErrorCheck> error= statVali.getRenewalStatisticsListVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return statServ.getRenewalStatisticsList(req);
		} 
		@PostMapping("/getRenewalCalStatisticsList")
		public GetRenewalStatisticsListRes getRenewalCalStatisticsList(@RequestBody GetRenewalStatisticsListReq req) throws CommonValidationException {
			List<ErrorCheck> error= statVali.getRenewalStatisticsListVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
			return statServ.getRenewalCalStatisticsList(req);
		}
}
