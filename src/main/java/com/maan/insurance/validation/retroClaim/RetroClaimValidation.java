package com.maan.insurance.validation.retroClaim;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.retroClaim.ContractDetailsMode1Req;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.retro.RetroServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.statistics.StatisticsValidation;

@Service
public class RetroClaimValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(RetroClaimValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private RetroServiceImple retroImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	

	
	@Autowired
	private CommonCalculation calcu;
	
	public RetroClaimValidation() {
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

	public List<ErrorCheck> contractDetailsMode1Vali(ContractDetailsMode1Req req) {
		// TODO Auto-generated method stub
		return null;
	}

}
