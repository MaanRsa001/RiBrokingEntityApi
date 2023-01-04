package com.maan.insurance.validation.statistics;

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
import com.maan.insurance.model.req.statistics.GetColumnHeaderlistReq;
import com.maan.insurance.model.req.statistics.GetRenewalStatisticsListReq;
import com.maan.insurance.model.req.statistics.SlideScenarioReq;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.retro.RetroServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.retro.RetroValidation;

@Service
public class StatisticsValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(StatisticsValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private RetroServiceImple retroImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public StatisticsValidation() {
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

	public List<ErrorCheck> slideScenarioVali(SlideScenarioReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "1"));
		}
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "2"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		return list;
	}

	public List<ErrorCheck> getColumnHeaderlistVali(GetColumnHeaderlistReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getPeriodFrom())) {
			list.add(new ErrorCheck("Please Enter PeriodFrom", "PeriodFrom", "1"));
		}
		if (StringUtils.isBlank(req.getPeriodTo())) {
			list.add(new ErrorCheck("Please Enter PeriodTo", "PeriodTo", "2"));
		}
		return list;
	}

	public List<ErrorCheck> getRenewalStatisticsListVali(GetRenewalStatisticsListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "3"));
		}
		if (StringUtils.isBlank(req.getUwFrom())) {
			list.add(new ErrorCheck("Please Enter UwFrom", "UwFrom", "4"));
		}
		if (StringUtils.isBlank(req.getPeriodFrom())) {
			list.add(new ErrorCheck("Please Enter PeriodFrom", "PeriodFrom", "5"));
		}
		if (StringUtils.isBlank(req.getPeriodTo())) {
			list.add(new ErrorCheck("Please Enter PeriodTo", "PeriodTo", "6"));
		}
		if (StringUtils.isBlank(req.getUwTo())) {
			list.add(new ErrorCheck("Please Enter UwTo", "UwTo", "7"));
		}
		return list;
	}
}
