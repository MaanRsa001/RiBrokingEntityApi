package com.maan.insurance.validation.reports;

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
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.reports.ReportsServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;

@Service
public class ReportsValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(ReportsValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private ReportsServiceImple impl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public ReportsValidation() {
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

	

	public List<ErrorCheck> getReportsCommonVali(ReportsCommonReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		if (StringUtils.isBlank(req.getDept())) {
			list.add(new ErrorCheck("Please Enter Dept", "Dept", "5"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "8"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "9"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "10"));
		}
		return list;
	}



	public List<ErrorCheck> getClaimRegisterListVali(GetClaimRegisterListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "8"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "10"));
		}
		return list;
	}



	public List<ErrorCheck> getInwardRetroMappingReportVali(GetInwardRetroMappingReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "8"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "9"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "10"));
		}
		return list;
	}


	public List<ErrorCheck> getRetroInwardMappingReportVali(GetRetroInwardMappingReportReq req) {
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
		return list;
	}



	public List<ErrorCheck> getTransactionMasterReportVali(GetTransactionMasterReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		if (StringUtils.isBlank(req.getDocType())) {
			list.add(new ErrorCheck("Please Enter DocType", "DocType", "5"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "9"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "10"));
		}
		return list;
	}



	public List<ErrorCheck> getDebtorsAgeingReportVali(GetDebtorsAgeingReportReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		if (StringUtils.isBlank(req.getDebFrom())) {
			list.add(new ErrorCheck("Please Enter DebFrom", "DebFrom", "5"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getDebFrom1())) {
			list.add(new ErrorCheck("Please Enter DebFrom1", "DebFrom1", "8"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "9"));
		}
		if (StringUtils.isBlank(req.getDebFrom2())) {
			list.add(new ErrorCheck("Please Enter DebFrom2", "DebFrom2", "10"));
		}
		if (StringUtils.isBlank(req.getDebFrom4())) {
			list.add(new ErrorCheck("Please Enter DebFrom4", "DebFrom4", "11"));
		}
		if (StringUtils.isBlank(req.getDebTo())) {
			list.add(new ErrorCheck("Please Enter DebTo", "DebTo", "12"));
		}
		if (StringUtils.isBlank(req.getDebFrom5())) {
			list.add(new ErrorCheck("Please Enter DebFrom5", "DebFrom5", "13"));
		}
		if (StringUtils.isBlank(req.getDebTo1())) {
			list.add(new ErrorCheck("Please Enter DebTo1", "DebTo1", "14"));
		}
		if (StringUtils.isBlank(req.getDebTo2())) {
			list.add(new ErrorCheck("Please Enter DebTo2", "DebTo2", "15"));
		}
		if (StringUtils.isBlank(req.getDebTo3())) {
			list.add(new ErrorCheck("Please Enter DebTo3", "DebTo3", "16"));
		}
		if (StringUtils.isBlank(req.getDebTo4())) {
			list.add(new ErrorCheck("Please Enter DebTo4", "DebTo4", "17"));
		}
		if (StringUtils.isBlank(req.getDebTo5())) {
			list.add(new ErrorCheck("Please Enter DebTo5", "DebTo5", "18"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "19"));
		}
		if (StringUtils.isBlank(req.getDocType())) {
			list.add(new ErrorCheck("Please Enter DocType", "DocType", "20"));
		}
		return list;
	}



	public List<ErrorCheck> getMoveMntSummaryVali(GetMoveMntSummaryReq req) {
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
		return list;
	}



	public List<ErrorCheck> getallocationReportListVali(GetallocationReportListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		if (StringUtils.isBlank(req.getSettlementType())) {
			list.add(new ErrorCheck("Please Enter SettlementType", "SettlementType", "5"));
		}
		if (StringUtils.isBlank(req.getAllocateStatus())) {
			list.add(new ErrorCheck("Please Enter AllocateStatus", "AllocateStatus", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "9"));
		}
		
		return list;
	}



	public List<ErrorCheck> getPayRecRegisterListVali(GetPayRecRegisterListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		if (StringUtils.isBlank(req.getShowAllFields())) {
			list.add(new ErrorCheck("Please Enter ShowAllFields", "ShowAllFields", "5"));
		}
		if (StringUtils.isBlank(req.getPayrecType())) {
			list.add(new ErrorCheck("Please Enter PayrecType", "PayrecType", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getTransactionType())) {
			list.add(new ErrorCheck("Please Enter TransactionType", "TransactionType", "8"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "9"));
		}
		return list;
	}

	public List<ErrorCheck> getClaimPaidRegisterListVali(GetClaimPaidRegisterListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getBrokerId())) {
			list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "3"));
		}
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "4"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getEndDate())) {
			list.add(new ErrorCheck("Please Enter EndDate", "EndDate", "7"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "8"));
		}
		if (StringUtils.isBlank(req.getStartDate())) {
			list.add(new ErrorCheck("Please Enter StartDate", "StartDate", "9"));
		}
		if (StringUtils.isBlank(req.getUwYear())) {
			list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "10"));
		}
		return list;
	}



	public List<ErrorCheck> getRetroRegisterListVali(GetRetroRegisterListReq req) {
		// TODO Auto-generated method stub
		return null;
	}
}
