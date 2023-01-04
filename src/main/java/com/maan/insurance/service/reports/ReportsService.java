package com.maan.insurance.service.reports;

import org.springframework.stereotype.Service;

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

@Service
public interface ReportsService {

	GetMoveMentInitRes getMoveMentInit(String branchCode);

	GetClaimMoveMentInitRes getClaimMoveMentInit(String branchCode);

	GetClaimJournelInitRes getClaimJournelInit(String branchCode);

	GetBookedUprInitRes getBookedUprInit(String branchCode);

	GetBookedPremiumInitRes getBookedPremiumInit(String branchCode);

	GetPipelineWrittenInitRes getPipelineWrittenInit(String branchCode);

	GetCommonDropDownRes getProductDropDown(String branchCode, String typeId);

	GetCedingCompanyRes getCedingCompany(String branchCode, String productId);

	CommonSaveRes getReportName(String typeId, String productId);

	GetPendingOffersListRes getPendingOffersList(ReportsCommonReq req);

	ReportsCommonRes getPolicyRegisterList(ReportsCommonReq req);

	ReportsCommonRes getPremiumRegisterList(ReportsCommonReq req);

	ReportsCommonRes getClaimRegisterList(GetClaimRegisterListReq req);

	ReportsCommonRes getRenewalDueList(ReportsCommonReq req);

	GetRetroQuarterlyReport getRetroQuarterlyReport(ReportsCommonReq req);

	GetInwardRetroMappingReportRes getInwardRetroMappingReport(GetInwardRetroMappingReportReq req);

	ReportsCommonRes getRetroInwardMappingReport(GetRetroInwardMappingReportReq req);

	ReportsCommonRes getTransactionMasterReport(GetTransactionMasterReportReq req);

	ReportsCommonRes getDebtorsAgeingReport(GetDebtorsAgeingReportReq req);

	GetMoveMntSummaryRes getMoveMntSummary(GetMoveMntSummaryReq req);

	ReportsCommonRes getallocationReportList(GetallocationReportListReq req);

	ReportsCommonRes getPayRecRegisterList(GetPayRecRegisterListReq req);

	ReportsCommonRes getClaimPaidRegisterList(GetClaimPaidRegisterListReq req);

	ReportsCommonRes getRetroRegisterList(GetRetroRegisterListReq req);

}
