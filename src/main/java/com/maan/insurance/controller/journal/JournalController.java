package com.maan.insurance.controller.journal;

import java.text.ParseException;
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
import com.maan.insurance.model.req.journal.GetEndDateStatusReq;
import com.maan.insurance.model.req.journal.GetJournalViewsReq;
import com.maan.insurance.model.req.journal.GetLedgerEntryListReq;
import com.maan.insurance.model.req.journal.GetQuaterEndValidationReq;
import com.maan.insurance.model.req.journal.GetSpcCurrencyListReq;
import com.maan.insurance.model.req.journal.GetStartDateStatusReq;
import com.maan.insurance.model.req.journal.InsertInActiveOpenPeriodReq;
import com.maan.insurance.model.req.journal.InsertManualJVReq;
import com.maan.insurance.model.req.journal.InsertRetroProcessReq;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;
import com.maan.insurance.model.res.journal.GetEndDateListRes;
import com.maan.insurance.model.res.journal.GetJournalViewsRes;
import com.maan.insurance.model.res.journal.GetLedgerEntryListRes;
import com.maan.insurance.model.res.journal.GetOpenPeriodListRes;
import com.maan.insurance.model.res.journal.GetSpcCurrencyListRes;
import com.maan.insurance.model.res.journal.GetStartDateListRes;
import com.maan.insurance.model.res.journal.GetUserDetailsRes;
import com.maan.insurance.model.res.journal.GetViewLedgerDetailsRes;
import com.maan.insurance.model.res.journal.GetjounalListRes;
import com.maan.insurance.model.res.journal.InsertManualJVRes;
import com.maan.insurance.model.res.portFolio.CommonSaveRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.statistics.GetCurrencyNameRes;
import com.maan.insurance.model.res.statistics.SlideScenarioRes;
import com.maan.insurance.service.journal.JournalService;
import com.maan.insurance.service.statistics.StatisticsService;
import com.maan.insurance.validation.journal.JournalValidation;
import com.maan.insurance.validation.statistics.StatisticsValidation;

@RestController
@RequestMapping("/Insurance/journal")
public class JournalController {
Gson gson = new Gson(); 
	
	@Autowired
	private JournalService serv;
	@Autowired
	private JournalValidation val;
	
	@PostMapping("/insertInActiveOpenPeriod")
	public CommonSaveRes insertInActiveOpenPeriod(@RequestBody InsertInActiveOpenPeriodReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.insertInActiveOpenPeriodVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.insertInActiveOpenPeriod(req);
	}  
	@GetMapping("/getjounalList")
	public GetjounalListRes getjounalList() throws CommonValidationException {
		return serv.getjounalList();
		} 
	@GetMapping("/getOpenPeriodList/{branchCode}")
	public GetOpenPeriodListRes getOpenPeriodList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException{
		return serv.getOpenPeriodList(branchCode);
		}  
	@GetMapping("/getStartDateList/{branchCode}")
		public GetStartDateListRes getStartDateList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException{
			return serv.getStartDateList(branchCode);
			}
	@GetMapping("/getEndDateList/{branchCode}")
		public GetEndDateListRes getEndDateList(@PathVariable ("branchCode") String branchCode) throws CommonValidationException{
			return serv.getEndDateList(branchCode);
		}
		@GetMapping("/getForExDiffName/{branchCode}")
		public CommonSaveRes getForExDiffName(@PathVariable ("branchCode") String branchCode) throws CommonValidationException{
			return serv.getForExDiffName(branchCode);
			} 
		@PostMapping("/insertActiveOpenPeriod")
			public CommonSaveRes insertActiveOpenPeriod(@RequestBody InsertInActiveOpenPeriodReq req) throws CommonValidationException {
				List<ErrorCheck> error= val.insertInActiveOpenPeriodVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return serv.insertActiveOpenPeriod(req);
			}  
		@GetMapping("/getShortname/{branchCode}")
		public CommonSaveRes getShortname(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
			return serv.getShortname(branchCode);
			}   
		@PostMapping("/getSpcCurrencyList")
			public GetSpcCurrencyListRes getSpcCurrencyList(@RequestBody GetSpcCurrencyListReq req) throws CommonValidationException {
				List<ErrorCheck> error= val.getSpcCurrencyListVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return serv.getSpcCurrencyList(req);
			}  
  
			@PostMapping("/getJournalViews")
			public GetJournalViewsRes getJournalViews(@RequestBody GetSpcCurrencyListReq req) throws CommonValidationException {
				List<ErrorCheck> error= val.getSpcCurrencyListVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
				return serv.getJournalViews(req);
			}   
			
			@GetMapping("/getCountOpenPeriod/{branchCode}/{sNo}")
			public CommonSaveRes getCountOpenPeriod(@PathVariable ("branchCode") String branchCode,@PathVariable ("sNo") String sNo) throws CommonValidationException {
				return serv.getCountOpenPeriod(branchCode,sNo);
				}   
			@GetMapping("/getUserDetails/{branchCode}/{loginId}")
				public GetUserDetailsRes getUserDetails(@PathVariable ("branchCode") String branchCode,@PathVariable ("loginId") List<String> loginId) throws CommonValidationException {
					return serv.getUserDetails(branchCode,loginId);
			} 
	@PostMapping("/getQuaterEndValidation")
	public CommonSaveRes getQuaterEndValidation(@RequestBody GetQuaterEndValidationReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getQuaterEndValidationVali(req);
			if(error!=null && error.size()>0) {
				throw new CommonValidationException("error",error);
			}
				return serv.getQuaterEndValidation(req);
			}   
			@GetMapping("/activateInActivateLoginUsers/{branchCode}/{loginId}/{status}")
			public CommonResponse activateInActivateLoginUsers(@PathVariable ("branchCode") String branchCode,@PathVariable ("loginId") String loginId,@PathVariable ("status") String status) throws CommonValidationException {
				return serv.activateInActivateLoginUsers(branchCode,loginId, status);
		} 
		@PostMapping("/insertRetroProcess")
		public CommonSaveRes insertRetroProcess(@RequestBody InsertRetroProcessReq req) throws CommonValidationException {
			List<ErrorCheck> error= val.insertRetroProcessVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
					return serv.insertRetroProcess(req);
				}   	
		@PostMapping("/getLedgerEntryList")
		public GetLedgerEntryListRes getLedgerEntryList(@RequestBody GetLedgerEntryListReq req) throws CommonValidationException {
			List<ErrorCheck> error= val.getLedgerEntryListVali(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
					return serv.getLedgerEntryList(req);
		} 
		
		@PostMapping("/insertManualJV")
		public CommonResponse insertManualJV(@RequestBody InsertManualJVReq req) throws CommonValidationException {
			List<ErrorCheck> error= val.validationManual(req);
				if(error!=null && error.size()>0) {
					throw new CommonValidationException("error",error);
				}
					return serv.insertManualJV(req);
		} 
	
		@GetMapping("/getViewLedgerDetails/{branchCode}/{transId}/{reversalStatus}")
		public GetViewLedgerDetailsRes getViewLedgerDetails(@PathVariable ("branchCode") String branchCode,@PathVariable ("transId") String transId,@PathVariable ("reversalStatus") String reversalStatus) throws CommonValidationException {
			return serv.getViewLedgerDetails(branchCode,transId,reversalStatus);
	}  
		
		@GetMapping("/getEditLedgerDetails/{branchCode}/{transId}/{mode}")
	public GetViewLedgerDetailsRes getEditLedgerDetails(@PathVariable ("branchCode") String branchCode,@PathVariable ("transId") String transId,@PathVariable ("mode") String mode) throws CommonValidationException {
		return serv.getEditLedgerDetails(branchCode,transId,mode);
} 
	@PostMapping("/getStartDateStatus")
	public CommonSaveRes getStartDateStatus(@RequestBody GetStartDateStatusReq req) throws CommonValidationException {
	List<ErrorCheck> error= val.getStartDateStatusVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
			return serv.getStartDateStatus(req);
}  
	
	@PostMapping("/getEndDateStatus")
public CommonSaveRes getEndDateStatus(@RequestBody GetEndDateStatusReq req) throws CommonValidationException {
List<ErrorCheck> error= val.getEndDateStatusVali(req);
	if(error!=null && error.size()>0) {
		throw new CommonValidationException("error",error);
	}
		return serv.getEndDateStatus(req);
}
}
