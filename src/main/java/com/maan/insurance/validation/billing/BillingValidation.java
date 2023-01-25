package com.maan.insurance.validation.billing;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.jpa.service.impl.ClaimJpaServiceImpl;
import com.maan.insurance.model.req.GetTransContractReq;
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
	// TODO Auto-generated method stub
	return null;
}


public List<ErrorCheck> getTransContractVali(GetTransContractReqRi req) {
	// TODO Auto-generated method stub
	return null;
}


public List<ErrorCheck> getBillingInfoListVali(GetBillingInfoListReq req) {
	// TODO Auto-generated method stub
	return null;
}


public List<ErrorCheck> getTransContractRiVali(GetTransContractReqRi req) {
	// TODO Auto-generated method stub
	return null;
}
}
