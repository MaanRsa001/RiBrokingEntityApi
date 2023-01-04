package com.maan.insurance.validation.authentication;

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
import com.maan.insurance.model.req.authentication.AuthenticationChangesReq;
import com.maan.insurance.model.req.authentication.AuthenticationListReq;
import com.maan.insurance.model.req.authentication.GetPremiumDetailsReq;
import com.maan.insurance.model.req.premium.InsertPremiumReq;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.journal.JournalServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.statistics.StatisticsValidation;

@Service
public class AuthenticationValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(AuthenticationValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private JournalServiceImple imple;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public AuthenticationValidation() {
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

	public List<ErrorCheck> authenticationListVali(AuthenticationListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getUploadStatus())) {
			list.add(new ErrorCheck("Please Enter UploadStatus", "UploadStatus", "1"));
		}
		if (StringUtils.isBlank(req.getCheckItem())) {
			list.add(new ErrorCheck("Please Enter CheckItem", "CheckItem", "2"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		return list;
	}

	public List<ErrorCheck> validationApproval(AuthenticationChangesReq bean) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if("multiple".equalsIgnoreCase(bean.getUploadStatus()) &&(bean.getCheckItem()==null || "".equalsIgnoreCase(bean.getCheckItem()))){
			list.add(new ErrorCheck(prop.getProperty("error.authe.checkItem"),"UploadStatus","01"));
		}
		return list;
	}

	public List<ErrorCheck> getPremiumDetailsVali(GetPremiumDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getCountryId())) {
			list.add(new ErrorCheck("Please Enter CountryId", "CountryId", "2"));
		}
		if (StringUtils.isBlank(req.getRequestNo())) {
			list.add(new ErrorCheck("Please Enter RequestNo", "RequestNo", "3"));
		}
		return list;
	}
}
