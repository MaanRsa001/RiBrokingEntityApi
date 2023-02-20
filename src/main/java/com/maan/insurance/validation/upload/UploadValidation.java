package com.maan.insurance.validation.upload;

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
import org.springframework.web.multipart.MultipartFile;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.DoUploadReq;
import com.maan.insurance.model.req.upload.DoDeleteDocDetailsReq;
import com.maan.insurance.model.req.upload.GetDocListReq;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.service.impl.home.HomeServiceImple;
import com.maan.insurance.validation.Formatters;
import com.maan.insurance.validation.home.HomeValidation;

@Service
public class UploadValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(UploadValidation.class);
	private Properties prop = new Properties();
	@Autowired
	private HomeServiceImple imple;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	
	
	public UploadValidation() {
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


	public List<ErrorCheck> getDocListVali(GetDocListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getModuleType())) {
			list.add(new ErrorCheck("Please Enter ModuleType", "ModuleType", "5"));
		}
		if (StringUtils.isBlank(req.getTranNo())) {
			list.add(new ErrorCheck("Please Enter TranNo", "TranNo", "6"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "7"));
		}
		return list;
	}


	public List<ErrorCheck> doDeleteDocDetailsVali(DoDeleteDocDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "1"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "4"));
		}
		if (StringUtils.isBlank(req.getModuleType())) {
			list.add(new ErrorCheck("Please Enter ModuleType", "ModuleType", "5"));
		}
		if (StringUtils.isBlank(req.getTranNo())) {
			list.add(new ErrorCheck("Please Enter TranNo", "TranNo", "6"));
		}
		if (StringUtils.isBlank(req.getDocId())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "7"));
		}
		if (StringUtils.isBlank(req.getLoginId())) {
			list.add(new ErrorCheck("Please Enter TranNo", "TranNo", "8"));
		}
		if (StringUtils.isBlank(req.getOurFileName())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "9"));
		}
		return list;
	}



	public List<ErrorCheck> doUploadVali(DoUploadReq req) {
		// TODO Auto-generated method stub
		return null;
	}

}
