package com.maan.insurance.controller.reports;

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
import com.maan.insurance.model.req.reports.GetClaimPaidRegisterListReq;
import com.maan.insurance.model.req.reports.GetClaimRegisterListReq;
import com.maan.insurance.model.req.reports.GetDebtorsAgeingReportReq;
import com.maan.insurance.model.req.reports.GetInwardRetroMappingReportReq;
import com.maan.insurance.model.req.reports.GetMoveMntSummaryReq;
import com.maan.insurance.model.req.reports.GetPayRecRegisterListReq;
import com.maan.insurance.model.req.reports.GetRetroInwardMappingReportReq;
import com.maan.insurance.model.req.reports.GetRetroRegisterListReq;
import com.maan.insurance.model.req.reports.GetTransactionMasterReportReq;
import com.maan.insurance.model.req.reports.GetallocationReportListReq;
import com.maan.insurance.model.req.reports.ReportsCommonReq;
import com.maan.insurance.model.res.DropDown.GetCedingCompanyRes;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.reports.GetBookedPremiumInitRes;
import com.maan.insurance.model.res.reports.GetBookedUprInitRes;
import com.maan.insurance.model.res.reports.GetClaimJournelInitRes;
import com.maan.insurance.model.res.reports.GetClaimMoveMentInitRes;
import com.maan.insurance.model.res.reports.GetInwardRetroMappingReportRes;
import com.maan.insurance.model.res.reports.GetInwardRetroMappingReportRes1;
import com.maan.insurance.model.res.reports.GetMoveMentInitRes;
import com.maan.insurance.model.res.reports.GetMoveMntSummaryRes;
import com.maan.insurance.model.res.reports.GetPendingOffersListRes;
import com.maan.insurance.model.res.reports.GetPipelineWrittenInitRes;
import com.maan.insurance.model.res.reports.GetRetroQuarterlyReport;
import com.maan.insurance.model.res.reports.ReportsCommonRes;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.service.reports.ReportsService;
import com.maan.insurance.validation.reports.ReportsValidation;

@RestController
@RequestMapping("/Insurance/reports")
public class ReportsController {
	Gson gson = new Gson();
	
	@Autowired
	private ReportsService serv;
	@Autowired
	private ReportsValidation val;
	
	@PostMapping("/getPendingOffersList")
	public GetPendingOffersListRes getPendingOffersList(@RequestBody ReportsCommonReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getReportsCommonVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPendingOffersList(req);
	}
	
	@GetMapping("/getMoveMentInit/{branchCode}")
	public GetMoveMentInitRes getMoveMentInit(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getMoveMentInit(branchCode);
		} 
	@GetMapping("/getClaimMoveMentInit/{branchCode}")
	public GetClaimMoveMentInitRes getClaimMoveMentInit(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getClaimMoveMentInit(branchCode);
		} 
	@GetMapping("/getClaimJournelInit/{branchCode}")
	public GetClaimJournelInitRes getClaimJournelInit(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getClaimJournelInit(branchCode);
		} 
	@GetMapping("/getBookedUprInit/{branchCode}")
	public GetBookedUprInitRes getBookedUprInit(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getBookedUprInit(branchCode);
		} 
	@GetMapping("/getBookedPremiumInit/{branchCode}")
	public GetBookedPremiumInitRes getBookedPremiumInit(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getBookedPremiumInit(branchCode);
		} 	
	@GetMapping("/getPipelineWrittenInit/{branchCode}")
	public GetPipelineWrittenInitRes getPipelineWrittenInit(@PathVariable ("branchCode") String branchCode) throws CommonValidationException {
		return serv.getPipelineWrittenInit(branchCode);
		} 
	@GetMapping("/getProductDropDown/{branchCode}/{typeId}")
	public GetCommonDropDownRes getProductDropDown(@PathVariable ("branchCode") String branchCode,@PathVariable ("typeId") String typeId) throws CommonValidationException {
		return serv.getProductDropDown(branchCode,typeId);
		} 			
	@GetMapping("/getCedingCompany/{branchCode}/{productId}")
	public GetCedingCompanyRes getCedingCompany(@PathVariable ("branchCode") String branchCode,@PathVariable ("productId") String productId) throws CommonValidationException {
		return serv.getCedingCompany(branchCode,productId);
		}
	@GetMapping("/getReportName/{typeId}/{productId}")
	public CommonSaveRes getReportName(@PathVariable ("typeId") String typeId,@PathVariable ("productId") String productId) throws CommonValidationException {
		return serv.getReportName(typeId,productId);
		}
	@PostMapping("/getPolicyRegisterList")
	public ReportsCommonRes getPolicyRegisterList(@RequestBody ReportsCommonReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getReportsCommonVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPolicyRegisterList(req);
	} 
	@PostMapping("/getPremiumRegisterList")
	public ReportsCommonRes getPremiumRegisterList(@RequestBody ReportsCommonReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getReportsCommonVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPremiumRegisterList(req);
	} 
	@PostMapping("/getClaimRegisterList")
	public ReportsCommonRes getClaimRegisterList(@RequestBody GetClaimRegisterListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getClaimRegisterListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getClaimRegisterList(req);
	}
	@PostMapping("/getRenewalDueList")
	public ReportsCommonRes getRenewalDueList(@RequestBody ReportsCommonReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getReportsCommonVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getRenewalDueList(req);
	} 
	@PostMapping("/getRetroQuarterlyReport")
	public GetRetroQuarterlyReport getRetroQuarterlyReport(@RequestBody ReportsCommonReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getReportsCommonVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getRetroQuarterlyReport(req);
	}
	@PostMapping("/getInwardRetroMappingReport")
	public GetInwardRetroMappingReportRes getInwardRetroMappingReport(@RequestBody GetInwardRetroMappingReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getInwardRetroMappingReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getInwardRetroMappingReport(req);
	} 
	@PostMapping("/getRetroInwardMappingReport")
	public ReportsCommonRes getRetroInwardMappingReport(@RequestBody GetRetroInwardMappingReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getRetroInwardMappingReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getRetroInwardMappingReport(req);
	}  
	@PostMapping("/getTransactionMasterReport")
	public ReportsCommonRes getTransactionMasterReport(@RequestBody GetTransactionMasterReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getTransactionMasterReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getTransactionMasterReport(req);
	}  
	@PostMapping("/getDebtorsAgeingReport")
	public ReportsCommonRes getDebtorsAgeingReport(@RequestBody GetDebtorsAgeingReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getDebtorsAgeingReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getDebtorsAgeingReport(req);
	}
	@PostMapping("/getMoveMntSummary")
	public GetMoveMntSummaryRes getMoveMntSummary(@RequestBody GetMoveMntSummaryReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getMoveMntSummaryVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getMoveMntSummary(req);
	} 
	@PostMapping("/getallocationReportList")
	public ReportsCommonRes getallocationReportList(@RequestBody GetallocationReportListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getallocationReportListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getallocationReportList(req);
	}  
	@PostMapping("/getPayRecRegisterList")
	public ReportsCommonRes getPayRecRegisterList(@RequestBody GetPayRecRegisterListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPayRecRegisterListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPayRecRegisterList(req);
	} 
	@PostMapping("/getClaimPaidRegisterList")
	public ReportsCommonRes getClaimPaidRegisterList(@RequestBody GetClaimPaidRegisterListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getClaimPaidRegisterListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getClaimPaidRegisterList(req);
	} 
	@PostMapping("/getRetroRegisterList")
	public ReportsCommonRes getRetroRegisterList(@RequestBody GetRetroRegisterListReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getRetroRegisterListVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getRetroRegisterList(req);
	} 
} 
