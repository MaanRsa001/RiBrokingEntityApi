package com.maan.insurance.validation.jasper;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.maan.insurance.service.impl.jasper.JasperServiceImple;
import com.maan.insurance.validation.Formatters;

@Service
public class JasperValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(JasperValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private JasperServiceImple imple;
	
	@Autowired
	private Formatters fm;
	
	
	public JasperValidation() {
		try {
			InputStream inputStream = getClass().getClassLoader()
					.getResourceAsStream("application_field_names.properties");
			if (inputStream != null) {
				prop.load(inputStream);
			}

		} catch (Exception e) {
			log.info(e);
		}
	}


	public List<ErrorCheck> getJournalReportVali(GetJournalReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getJournalId())) {
			list.add(new ErrorCheck("Please Enter JournalId", "JournalId", "3"));
		}
		if (StringUtils.isBlank(req.getProcessStatus())) {
			list.add(new ErrorCheck("Please Enter ProcessStatus", "ProcessStatus", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "6"));
		}
	
		return list;
	}
	public List<ErrorCheck> getPayRecReportVali(GetPayRecReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getPayrecType())) {
			list.add(new ErrorCheck("Please Enter PayrecType", "PayrecType", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getShowAllFields())) {
			list.add(new ErrorCheck("Please Enter ShowAllFields", "ShowAllFields", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getTransactionType())) {
			list.add(new ErrorCheck("Please Enter TransactionType", "TransactionType", "13"));
		}
		return list;
	}


	public List<ErrorCheck> getTransactionReportVali(GetTransactionReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getShowAllFields())) {
			list.add(new ErrorCheck("Please Enter ShowAllFields", "ShowAllFields", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getDocType())) {
			list.add(new ErrorCheck("Please Enter DocType", "DocType", "13"));
		}
		if (StringUtils.isBlank(req.getLoginName())) {
			list.add(new ErrorCheck("Please Enter LoginName", "LoginName", "14"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "15"));
		}
		return list;
	}


	public List<ErrorCheck> getClaimRegisterReportVali(GetClaimRegisterReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter CountryId", "CountryId", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "14"));
		}
		return list;
	}


	public List<ErrorCheck> getPremiumRegisterReportVali(GetPremiumRegisterReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter PayrecType", "PayrecType", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "14"));
		}
		return list;
	}


	public List<ErrorCheck> getClaimPaidRegisterReportVali(GetClaimPaidRegisterReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter PayrecType", "PayrecType", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "14"));
		}
		return list;
	}


	public List<ErrorCheck> getPolicyRegisterReportVali(GetPolicyRegisterReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getDeptId())) {
			list.add(new ErrorCheck("Please Enter DeptId", "DeptId", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter CountryId", "CountryId", "14"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "15"));
		}
		return list;
	}


	public List<ErrorCheck> getJournalReport1Vali(GetJournalReportReq1 req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getJournalId())) {
			list.add(new ErrorCheck("Please Enter JournalId", "JournalId", "2"));
		}
		if (StringUtils.isBlank(req.getOpenperiod())) {
			list.add(new ErrorCheck("Please Enter Openperiod", "Openperiod", "3"));
		}
		if (StringUtils.isBlank(req.getShortname())) {
			list.add(new ErrorCheck("Please Enter Shortname", "Shortname", "4"));
		}
		return list;
	}


	public List<ErrorCheck> getRetroReportVali(GetRetroReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "3"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "4"));
		}
		return list;
	}


	public List<ErrorCheck> getRetroRegisterReportVali(GetRetroRegisterReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getDeptId())) {
			list.add(new ErrorCheck("Please Enter DeptId", "DeptId", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter CountryId", "CountryId", "14"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "15"));
		}
		return list;
	}


	public List<ErrorCheck> getTreatyWithdrawReportVali(GetTreatyWithdrawReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "4"));
		}
		if (StringUtils.isBlank(req.getTreatyMainClass())) {
			list.add(new ErrorCheck("Please Enter TreatyMainClass", "TreatyMainClass", "5"));
		}
		if (StringUtils.isBlank(req.getTreatyType())) {
			list.add(new ErrorCheck("Please Enter TreatyType", "TreatyType", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "8"));
		}
		return list;
	}


	public List<ErrorCheck> getDebtorsAgingReportVali(GetDebtorsAgingReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getDebFrom())) {
			list.add(new ErrorCheck("Please Enter DebFrom", "DebFrom", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getDebFrom1())) {
			list.add(new ErrorCheck("Please Enter DebFrom1", "DebFrom1", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getDebFrom2())) {
			list.add(new ErrorCheck("Please Enter DebFrom2", "DebFrom2", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getDebFrom3())) {
			list.add(new ErrorCheck("Please Enter DebFrom3", "DebFrom3", "8"));
		}
		if (StringUtils.isBlank(req.getDebFrom4())) {
			list.add(new ErrorCheck("Please Enter DebFrom4", "DebFrom4", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getDebFrom5())) {
			list.add(new ErrorCheck("Please Enter DebFrom5", "DebFrom5", "11"));
		}
		if (StringUtils.isBlank(req.getDebTo())) {
			list.add(new ErrorCheck("Please Enter DebTo", "DebTo", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getDebTo1())) {
			list.add(new ErrorCheck("Please Enter DebTo1", "DebTo1", "14"));
		}
		if (StringUtils.isBlank(req.getDebTo3())) {
			list.add(new ErrorCheck("Please Enter DebTo3", "DebTo3", "15"));
		}
		if (StringUtils.isBlank(req.getDebTo2())) {
			list.add(new ErrorCheck("Please Enter DebTo2", "DebTo2", "16"));
		}
		if (StringUtils.isBlank(req.getDebTo4())) {
			list.add(new ErrorCheck("Please Enter DebTo4", "DebTo4", "17"));
		}
		if (StringUtils.isBlank(req.getDebTo5())) {
			list.add(new ErrorCheck("Please Enter DebTo5", "DebTo5", "18"));
		}
		if (StringUtils.isBlank(req.getDocType())) {
			list.add(new ErrorCheck("Please Enter DocType", "DocType", "19"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "20"));
		}
		return list;
	}
	public List<ErrorCheck> getsoaReportVali(GetSoaReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getImagePath())) {
			list.add(new ErrorCheck("Please Enter ImagePath", "ImagePath", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getSaperatePRCLYN())) {
			list.add(new ErrorCheck("Please Enter SaperatePRCLYN", "SaperatePRCLYN", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getSettledItems())) {
			list.add(new ErrorCheck("Please Enter SettledItems", "SettledItems", "14"));
		}
		if (StringUtils.isBlank(req.getUnAllocatedCash())) {
			list.add(new ErrorCheck("Please Enter UnAllocatedCash", "UnAllocatedCash", "15"));
		}
		return list;
	}


	public List<ErrorCheck> getAllocationReportVali(GetAllocationReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getAllocateStatus())) {
			list.add(new ErrorCheck("Please Enter AllocateStatus", "AllocateStatus", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "6"));
		}
		if (StringUtils.isBlank(req.getSettlementType())) {
			list.add(new ErrorCheck("Please Enter SettlementType", "SettlementType", "7"));
		}
		return list;
	}


	public List<ErrorCheck> getTransactionPDFReportVali(GetTransactionPDFReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getImagePath())) {
			list.add(new ErrorCheck("Please Enter ImagePath", "ImagePath", "2"));
		}
		if (StringUtils.isBlank(req.getReceiptNo())) {
			list.add(new ErrorCheck("Please Enter ReceiptNo", "ReceiptNo", "3"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "4"));
		}
		return list;
	}


	public List<ErrorCheck> getClaimOslrReportVali(GetClaimOslrReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getImagePath())) {
			list.add(new ErrorCheck("Please Enter ImagePath", "ImagePath", "2"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "3"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		return list;
	}

	public List<ErrorCheck> getSOAReport1Vali(GetSOAReport1Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerName())) {
			list.add(new ErrorCheck("Please Enter BrokerName", "BrokerName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getCedingCoName())) {
			list.add(new ErrorCheck("Please Enter CedingCoName", "CedingCoName", "6"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "7"));
		}
		if (StringUtils.isBlank(req.getImagePath())) {
			list.add(new ErrorCheck("Please Enter ImagePath", "ImagePath", "8"));
		}
		if (StringUtils.isBlank(req.getProductName())) {
			list.add(new ErrorCheck("Please Enter ProductName", "ProductName", "9"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "10"));
		}
		if (StringUtils.isBlank(req.getSaperatePRCLYN())) {
			list.add(new ErrorCheck("Please Enter SaperatePRCLYN", "SaperatePRCLYN", "11"));
		}
		if (StringUtils.isBlank(req.getSysDate())) {
			list.add(new ErrorCheck("Please Enter SysDate", "SysDate", "12"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "13"));
		}
		if (StringUtils.isBlank(req.getSettledItems())) {
			list.add(new ErrorCheck("Please Enter SettledItems", "SettledItems", "14"));
		}
		if (StringUtils.isBlank(req.getUnAllocatedCash())) {
			list.add(new ErrorCheck("Please Enter UnAllocatedCash", "UnAllocatedCash", "15"));
		}
		return list;
	}


	public List<ErrorCheck> getJournalViewDownloadVali(GetJournalViewDownloadReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getJournalType())) {
			list.add(new ErrorCheck("Please Enter JournalType", "JournalType", "3"));
		}
		if (StringUtils.isBlank(req.getReportName())) {
			list.add(new ErrorCheck("Please Enter ReportName", "ReportName", "4"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "5"));
		}
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "6"));
		}
		if (StringUtils.isBlank(req.getTypeId())) {
			list.add(new ErrorCheck("Please Enter TypeId", "TypeId", "7"));
		}
		return list;
	}


	public List<ErrorCheck> getInsallmentDueReportVali(GetInsallmentDueReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "2"));
		}
		if (StringUtils.isBlank(req.getAllocationYN())) {
			list.add(new ErrorCheck("Please Enter AllocationYN", "AllocationYN", "3"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "4"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "5"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		return list;
	}
}
