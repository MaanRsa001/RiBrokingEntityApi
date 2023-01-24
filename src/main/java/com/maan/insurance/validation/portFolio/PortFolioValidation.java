package com.maan.insurance.validation.portFolio;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.portFolio.GetAutoPendingListReq;
import com.maan.insurance.model.req.portFolio.GetConfirmedListReq;
import com.maan.insurance.model.req.portFolio.GetContractsListReq;
import com.maan.insurance.model.req.portFolio.GetHistoryListReq;
import com.maan.insurance.model.req.portFolio.GetPendingListReq;
import com.maan.insurance.model.req.portFolio.ProcAutoReq;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.portFolio.PortFolioServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;

@Service
public class PortFolioValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(PortFolioValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private PortFolioServiceImple portImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public PortFolioValidation() {
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

	public List<ErrorCheck> getPendingListVali(GetPendingListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getAttachedUW())) {
			list.add(new ErrorCheck("Please Enter AttachedUW", "AttachedUW", "3"));
		}
		if (StringUtils.isBlank(req.getDeptId())) {
			list.add(new ErrorCheck("Please Enter DeptId", "DeptId", "4"));
		}
		if (StringUtils.isBlank(req.getFlag())) {
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "5"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "6"));
		}
		if (StringUtils.isBlank(req.getSearchType())) {
			//list.add(new ErrorCheck("Please Enter SearchType", "SearchType", "7"));
		}
		return list;
	}

	public List<ErrorCheck> getAutoPendingListVali(GetAutoPendingListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getDueDate())) {
			list.add(new ErrorCheck("Please Enter DueDate", "DueDate", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getFlag())) {
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "5"));
		}
		
		return list;
	}

	public List<ErrorCheck> getContractsListVali(GetContractsListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getAttachedUW())) {
			list.add(new ErrorCheck("Please Enter DueDate", "DueDate", "3"));
		}
		
		if (StringUtils.isBlank(req.getFlag())) {
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "5"));
		}
		if (StringUtils.isBlank(req.getDeptId())) {
			list.add(new ErrorCheck("Please Enter DeptId", "DeptId", "6"));
		}

		if (StringUtils.isBlank(req.getSearchType())) {
			//list.add(new ErrorCheck("Please Enter SearchType", "SearchType", "8"));
		}
		if (CollectionUtils.isEmpty(req.getMenuRights())) {
			list.add(new ErrorCheck("Please Enter MenuRights", "MenuRights", "9"));
		}
		return list;
	}

	public List<ErrorCheck> getHistoryListVali(GetHistoryListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getDeptId())) {
			list.add(new ErrorCheck("Please Enter DeptId", "DeptId", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getFlag())) {
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "5"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "6"));
		}
		return null;
	}

	public List<ErrorCheck> procAutoVali(ProcAutoReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter CountryId", "CountryId", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "5"));
		}
		if (StringUtils.isBlank(req.getSettlementStatus())) {
			list.add(new ErrorCheck("Please Enter SettlementStatus", "SettlementStatus", "6"));
		}
		if (StringUtils.isBlank(req.getTransactionError())) {
			list.add(new ErrorCheck("Please Enter TransactionError", "TransactionError", "7"));
		}
		if (CollectionUtils.isEmpty(req.getPortfolioListReq())) {
			list.add(new ErrorCheck("Please Enter PortfolioList", "PortfolioList", "8"));
		}
		return null;
	}

	public List<ErrorCheck> getConfirmedListVali(GetConfirmedListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (CollectionUtils.isEmpty(req.getMenuRights())) {
			list.add(new ErrorCheck("Please Enter MenuRights", "MenuRights", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		return list;
	}
}
