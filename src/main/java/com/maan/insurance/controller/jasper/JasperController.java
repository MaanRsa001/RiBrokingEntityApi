package com.maan.insurance.controller.jasper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maan.insurance.error.CommonValidationException;
import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.jasper.GetAllocationReportReq;
import com.maan.insurance.model.req.jasper.GetClaimOslrReportReq;
import com.maan.insurance.model.req.jasper.GetClaimPaidRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetClaimRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetDebtorsAgingReportReq;
import com.maan.insurance.model.req.jasper.GetInsallmentDueReportReq;
import com.maan.insurance.model.req.jasper.GetJournalReportReq;
import com.maan.insurance.model.req.jasper.GetJournalReportReq1;
import com.maan.insurance.model.req.jasper.GetJournalViewDownloadReq;
import com.maan.insurance.model.req.jasper.GetPayRecReportReq;
import com.maan.insurance.model.req.jasper.GetPolicyRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetPremiumRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetRetroRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetRetroReportReq;
import com.maan.insurance.model.req.jasper.GetSOAReport1Req;
import com.maan.insurance.model.req.jasper.GetSoaReportReq;
import com.maan.insurance.model.req.jasper.GetTransactionPDFReportReq;
import com.maan.insurance.model.req.jasper.GetTransactionReportReq;
import com.maan.insurance.model.req.jasper.GetTreatyWithdrawReportReq;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.res.jasper.JasperDocumentRes;
import com.maan.insurance.service.jasper.JasperService;
import com.maan.insurance.validation.jasper.JasperValidation;

@RestController
@RequestMapping("/Insurance/jasper")
public class JasperController {
	Gson gson = new Gson(); 
	@Autowired
	private JasperService serv;
	@Autowired
	private JasperValidation val;
	
	@PostMapping("/getJournalReport")
	public JasperDocumentRes getJournalReport(@RequestBody GetJournalReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getJournalReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getJournalReport(req);
	}  
 
	@PostMapping("/getPayRecReport")
	public JasperDocumentRes getPayRecReport(@RequestBody GetPayRecReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPayRecReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPayRecReport(req);
	}
	@PostMapping("/getTransactionReport")
	public JasperDocumentRes getTransactionReport(@RequestBody GetTransactionReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getTransactionReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getTransactionReport(req);
	}
	@PostMapping("/getClaimRegisterReport")
	public JasperDocumentRes getClaimRegisterReport(@RequestBody GetClaimRegisterReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getClaimRegisterReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getClaimRegisterReport(req);
	}
	@PostMapping("/getPremiumRegisterReport")
	public JasperDocumentRes getPremiumRegisterReport(@RequestBody GetPremiumRegisterReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPremiumRegisterReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPremiumRegisterReport(req);
	} 
	@PostMapping("/getClaimPaidRegisterReport")
	public JasperDocumentRes getClaimPaidRegisterReport(@RequestBody GetClaimPaidRegisterReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getClaimPaidRegisterReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getClaimPaidRegisterReport(req);
	}
	@PostMapping("/getPolicyRegisterReport")
	public JasperDocumentRes getPolicyRegisterReport(@RequestBody GetPolicyRegisterReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPolicyRegisterReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getPolicyRegisterReport(req); 
	}
	@PostMapping("/getJournalReport1")
	public JasperDocumentRes getJournalReport1(@RequestBody GetJournalReportReq1 req) throws CommonValidationException {
		List<ErrorCheck> error= val.getJournalReport1Vali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getJournalReport1(req); 
	} 
	@PostMapping("/getRetroReport")
	public JasperDocumentRes getRetroReport(@RequestBody GetRetroReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getRetroReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getRetroReport(req); 
	} 
	@PostMapping("/getRetroRegisterReport")
	public JasperDocumentRes getRetroRegisterReport(@RequestBody GetPolicyRegisterReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getPolicyRegisterReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getRetroRegisterReport(req); 
	} 
	@PostMapping("/getTreatyWithdrawReport")
	public JasperDocumentRes getTreatyWithdrawReport(@RequestBody GetTreatyWithdrawReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getTreatyWithdrawReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getTreatyWithdrawReport(req); 
	} 
	@PostMapping("/getDebtorsAgingReport")
	public JasperDocumentRes getDebtorsAgingReport(@RequestBody GetDebtorsAgingReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getDebtorsAgingReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getDebtorsAgingReport(req); 
	} 
	@PostMapping("/getsoaReport")
	public JasperDocumentRes getsoaReport(@RequestBody GetSoaReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getsoaReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getsoaReport(req); 
	}  
	@PostMapping("/getAllocationReport")
	public JasperDocumentRes getAllocationReport(@RequestBody  GetAllocationReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getAllocationReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getAllocationReport(req); 
	} 
	@PostMapping("/getTransactionPDFReport")
	public JasperDocumentRes getTransactionPDFReport(@RequestBody  GetTransactionPDFReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getTransactionPDFReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getTransactionPDFReport(req); 
	}
	@PostMapping("/getClaimOslrReport")
	public JasperDocumentRes getClaimOslrReport(@RequestBody  GetClaimOslrReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getClaimOslrReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		}
		return serv.getClaimOslrReport(req); 
	} 
	@PostMapping("/getSOAReport1")
	public JasperDocumentRes getSOAReport1(@RequestBody  GetSoaReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getsoaReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		} 
		return serv.getSOAReport1(req);  
	} 
	@PostMapping("/getJournalViewDownload")
	public JasperDocumentRes getJournalViewDownload(@RequestBody  GetJournalViewDownloadReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getJournalViewDownloadVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		} 
		return serv.getJournalViewDownload(req);  
	}  
	@PostMapping("/getInsallmentDueReport")
	public JasperDocumentRes getInsallmentDueReport(@RequestBody  GetInsallmentDueReportReq req) throws CommonValidationException {
		List<ErrorCheck> error= val.getInsallmentDueReportVali(req);
		if(error!=null && error.size()>0) {
			throw new CommonValidationException("error",error);
		} 
		return serv.getInsallmentDueReport(req);  
	} 
}
