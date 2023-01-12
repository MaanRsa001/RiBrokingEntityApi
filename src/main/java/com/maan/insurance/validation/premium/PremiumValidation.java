package com.maan.insurance.validation.premium;

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
import com.maan.insurance.model.req.claim.CopyDatatoDeleteTableReq;
import com.maan.insurance.model.req.premium.ContractidetifierlistReq;
import com.maan.insurance.model.req.premium.PremiumListReq;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.portFolio.PortFolioServiceImple;
import com.maan.insurance.service.impl.premium.PremiumServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.portFolio.PortFolioValidation;

@Service
public class PremiumValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(PremiumValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private PremiumServiceImple imple;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public PremiumValidation() {
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

	public List<ErrorCheck> premiumListVali(PremiumListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getOpendDate())) {
			list.add(new ErrorCheck("Please Enter OpendDate", "OpendDate", "3"));
		}
		if (StringUtils.isBlank(req.getOpstartDate())) {
			list.add(new ErrorCheck("Please Enter OpstartDate", "OpstartDate", "4"));
		}
		if (StringUtils.isBlank(req.getSearchType())) {
			//list.add(new ErrorCheck("Please Enter SearchType", "SearchType", "6"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "7"));
		}
		if (StringUtils.isBlank(req.getTableType())) {
			list.add(new ErrorCheck("Please Enter TableType", "TableType", "8"));
		}
		return list;
	}

	public List<ErrorCheck> contractidetifierlistVali(ContractidetifierlistReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if("N".equalsIgnoreCase(req.getTransactionType())|| "".equalsIgnoreCase(req.getTransactionType())){
			list.add(new ErrorCheck(prop.getProperty("errors.transaction.reqired"),"TransactionType","01"));	
		}
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter BranchCode"), "BranchCode", "2"));
		}	
		return list;
	}

//	public List<ErrorCheck> copyDatatoDeleteTableVali(CopyDatatoDeleteTableReq req) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
