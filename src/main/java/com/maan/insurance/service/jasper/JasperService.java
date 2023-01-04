package com.maan.insurance.service.jasper;

import org.springframework.stereotype.Service;

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

@Service
public interface JasperService {

	JasperDocumentRes getJournalReport(GetJournalReportReq req);

	JasperDocumentRes getPayRecReport(GetPayRecReportReq req);

	JasperDocumentRes getTransactionReport(GetTransactionReportReq req);

	JasperDocumentRes getClaimRegisterReport(GetClaimRegisterReportReq req);

	JasperDocumentRes getPremiumRegisterReport(GetPremiumRegisterReportReq req);

	JasperDocumentRes getClaimPaidRegisterReport(GetClaimPaidRegisterReportReq req);

	JasperDocumentRes getPolicyRegisterReport(GetPolicyRegisterReportReq req);

	JasperDocumentRes getJournalReport1(GetJournalReportReq1 req);

	JasperDocumentRes getRetroReport(GetRetroReportReq req);

	JasperDocumentRes getRetroRegisterReport(GetPolicyRegisterReportReq req);

	JasperDocumentRes getTreatyWithdrawReport(GetTreatyWithdrawReportReq req);

	JasperDocumentRes getDebtorsAgingReport(GetDebtorsAgingReportReq req);

	JasperDocumentRes getsoaReport(GetSoaReportReq req);

	JasperDocumentRes getAllocationReport(GetAllocationReportReq req);

	JasperDocumentRes getTransactionPDFReport(GetTransactionPDFReportReq req);

	JasperDocumentRes getClaimOslrReport(GetClaimOslrReportReq req);

	JasperDocumentRes getSOAReport1(GetSoaReportReq req);

	JasperDocumentRes getJournalViewDownload(GetJournalViewDownloadReq req);

	JasperDocumentRes getInsallmentDueReport(GetInsallmentDueReportReq req);

}
