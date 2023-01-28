package com.maan.insurance.validation.billing;

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
import com.maan.insurance.jpa.service.impl.ClaimJpaServiceImpl;
import com.maan.insurance.model.req.GetTransContractReq;
import com.maan.insurance.model.req.ReverseInsertReq;
import com.maan.insurance.model.req.billing.EditBillingInfoReq;
import com.maan.insurance.model.req.billing.GetBillingInfoListReq;
import com.maan.insurance.model.req.billing.GetTransContractReqRi;
import com.maan.insurance.model.req.billing.InsertBillingInfoReq;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.billing.BillingServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;

@Service
public class BillingValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(QueryImplemention.class);
	private Properties prop = new Properties();
	@Autowired
	private BillingServiceImple claimImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;

	
public BillingValidation() {
		
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


public List<ErrorCheck> insertBillingInfoVali(InsertBillingInfoReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
	}
	if (StringUtils.isBlank(req.getAmendmentDate())) {
		//list.add(new ErrorCheck("Please Enter AmendmentDate", "AmendmentDate", "2"));
	}
	if (StringUtils.isBlank(req.getBillDate())) {
		list.add(new ErrorCheck("Please Enter BillDate", "BillDate", "3"));
	}
	if (StringUtils.isBlank(req.getBillingNo())) {
		//list.add(new ErrorCheck("Please Enter BillingNo", "BillingNo", "4"));
	}
	if (StringUtils.isBlank(req.getBrokerId())) {
		list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "5"));
	}
	else if("63".equals(req.getBrokerId())) {
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "3"));
		}
	}
	if (StringUtils.isBlank(req.getCurrencyId())) {
		list.add(new ErrorCheck("Please Enter CurrencyId", "CurrencyId", "7"));
	}
	if (StringUtils.isBlank(req.getLoginId())) {
		list.add(new ErrorCheck("Please Enter LoginId", "LoginId", "8"));
	}
	if (StringUtils.isBlank(req.getProductId())) {
		list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "9"));
	}
	if (StringUtils.isBlank(req.getRemarks())) {
		//list.add(new ErrorCheck("Please Enter Remarks", "Remarks", "10"));
	}
	if (StringUtils.isBlank(req.getTransType())) {
		list.add(new ErrorCheck("Please Enter BillingType", "TransType", "11"));
	}
	if (CollectionUtils.isEmpty(req.getTransContractListReq())) {
		list.add(new ErrorCheck("Please Enter TransContractListReq", "TransContractListReq", "12"));
	}
	return list;
}


public List<ErrorCheck> getTransContractVali(GetTransContractReqRi req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
	}
	if (StringUtils.isBlank(req.getBrokerId())) {
		list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "2"));
	}
	else if("63".equals(req.getBrokerId())) {
	if (StringUtils.isBlank(req.getCedingId())) {
		list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "3"));
	}
	}
	if (StringUtils.isBlank(req.getCurrencyId())) {
		list.add(new ErrorCheck("Please Enter CurrencyId", "CurrencyId", "4"));
	}
	return list;
}


public List<ErrorCheck> getBillingInfoListVali(GetBillingInfoListReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
	}
	if (StringUtils.isBlank(req.getTransType())) {
		list.add(new ErrorCheck("Please Enter BillingType", "TransType", "2"));
	}
	return null;
}

public List<ErrorCheck> getTransContractRiVali(GetTransContractReqRi req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBranchCode())) {
		list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
	}
	if (StringUtils.isBlank(req.getBrokerId())) {
		list.add(new ErrorCheck("Please Enter BrokerId", "BrokerId", "2"));
	}
	else if("63".equals(req.getBrokerId())) {
		if (StringUtils.isBlank(req.getCedingId())) {
			list.add(new ErrorCheck("Please Enter CedingId", "CedingId", "3"));
		}
	}
	if (StringUtils.isBlank(req.getCurrencyId())) {
		list.add(new ErrorCheck("Please Enter CurrencyId", "CurrencyId", "4"));
	}
	return list;
}

public List<ErrorCheck> editBillingInfo(EditBillingInfoReq req) {
	List<ErrorCheck> list = new ArrayList<ErrorCheck>();
	if (StringUtils.isBlank(req.getBillingNo())) {
		list.add(new ErrorCheck("Please Enter BillingNo", "BillingNo", "1"));
	}
	return list;
}




public List<ErrorCheck> reverseInsertvalidate(com.maan.insurance.model.req.billing.ReverseInsertReq req) {
	// TODO Auto-generated method stub
	return null;
}
}
