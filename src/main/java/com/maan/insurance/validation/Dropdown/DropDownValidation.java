package com.maan.insurance.validation.Dropdown;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.error.ErrorCheck;
import com.maan.insurance.model.req.DropDown.DuplicateCountCheckReq;
import com.maan.insurance.model.req.DropDown.GetClaimDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetContractLayerNoReq;
import com.maan.insurance.model.req.DropDown.GetCopyQuoteReq;
import com.maan.insurance.model.req.DropDown.GetCurrencyIdReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetDepartmentieModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetInwardBusinessTypeDropDownReq;
import com.maan.insurance.model.req.DropDown.GetPreDepartmentDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProductModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProfitCentreieModuleDropDownReq;
import com.maan.insurance.model.req.DropDown.GetProposalNoReq;
import com.maan.insurance.model.req.DropDown.GetSectionListReq;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiDropDownReq;
import com.maan.insurance.model.req.DropDown.GetSubProfitCentreMultiReq;
import com.maan.insurance.model.req.DropDown.GetTreatyTypeDropDownReq;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.Validation;

@Service
public class DropDownValidation {
	private Logger log = LogManager.getLogger(QueryImplemention.class);
	private Properties prop = new Properties();
	
	@Autowired
	private DropDownServiceImple serviceImple;
	
	@Autowired
	private Validation val;

	public List<ErrorCheck> getClaimDepartmentDropDownVali(GetClaimDepartmentDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "4"));
		}
		
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "5"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "6"));
		}
		return list;
	}

	public List<ErrorCheck> getSubProfitCentreMultiVali(GetSubProfitCentreMultiReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getProductCode())) {
			list.add(new ErrorCheck("Please Enter ProductCode", "ProductCode", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "3"));
		}
		return list;
	}

	public List<ErrorCheck> getCurrencyIdVali(GetCurrencyIdReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck("Please Enter ClaimNo", "ClaimNo", "1"));
		}

		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "2"));
		}
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "3"));
		}
		return list;
	}

	public List<ErrorCheck> getProposalNoVali(GetProposalNoReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.ProductID"),"ProductID", "01"));
			}
		if(StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.Contarctno"),"Contarctno", "02"));
			}
		if(StringUtils.isBlank(req.getDepartId())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.DepartmentId"),"DepartmentId", "03"));
			}
		if(StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.LayerNo"),"LayerNo", "04"));
			}
		return list;
	}

	public List<ErrorCheck> getSectionListVali(GetSectionListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter BranchCode"),"BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter ContractNo"),"ContractNo", "02"));
			}
		if(StringUtils.isBlank(req.getDepartId())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter DepartmentId"),"DepartmentId", "03"));
			}
		return list;
	}

	public List<ErrorCheck> getContractLayerNoVali(GetContractLayerNoReq req) {
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
		return list;
	}

	public List<ErrorCheck> getPreDepartmentDropDownVali(GetPreDepartmentDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "4"));
		}
		
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "5"));
		}
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "6"));
		}
		return list;
	}

	public List<ErrorCheck> getSubProfitCentreMultiDropDownVali(GetSubProfitCentreMultiDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getProductCode())) {
			list.add(new ErrorCheck("Please Enter ProductCode", "ProductCode", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank((CharSequence) req.getSubProfitId())) {
			list.add(new ErrorCheck("Please Enter SubProfitId", "SubProfitId", "3"));
		}
		if (StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck("Please Enter DepartmentId", "DepartmentId", "4"));
		}
		return list;
	}

	public List<ErrorCheck> getDepartmentDropDownVali(GetDepartmentDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
//		if (StringUtils.isBlank(req.getContractNo())) {
//			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "1"));
//		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
//		if (StringUtils.isBlank( req.getProposalNo())) {
//			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
//		}
		if (StringUtils.isBlank(req.getBaseLayer())) {
			list.add(new ErrorCheck("Please Enter BaseLayer", "BaseLayer", "4"));
		}
//		if (StringUtils.isBlank(req.getProductCode())) {
//			list.add(new ErrorCheck("Please Enter ProductCode", "ProductCode", "5"));
//		}
		if (StringUtils.isBlank(req.getStatus())) {
			list.add(new ErrorCheck("Please Enter Status", "Status", "6"));
		}
			return null;
	}



	public List<ErrorCheck> getCopyQuoteVali(GetCopyQuoteReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "1"));
		}
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		if (StringUtils.isBlank( req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "3"));
		}
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "4"));
		}
		return list;
	}

	public List<ErrorCheck> getProductieModuleDropDown(GetProductModuleDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		try {
			
		
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter Mode", "Mode", "3"));
		}
		
		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "4"));
		}
		
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "5"));
		}
	
		
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return list;
	}

	public List<ErrorCheck> getInwardBusinessTypeDropDown(GetInwardBusinessTypeDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		try {
			
		
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "1"));
		}

		if (StringUtils.isBlank(req.getCategoryId())) {
			list.add(new ErrorCheck("Please Enter CategoryId", "CategoryId", "2"));
		}
		
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter Mode", "Mode", "3"));
		}
		
		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "4"));
		}
		
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "5"));
		}
	
		
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return list;
	}

	public List<ErrorCheck> getTreatyTypeDropDownVali(GetTreatyTypeDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		try {
			
		
		if (StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "1"));
		}

		if (StringUtils.isBlank(req.getCategoryId())) {
			list.add(new ErrorCheck("Please Enter CategoryId", "CategoryId", "2"));
		}
		
		if (StringUtils.isBlank(req.getMode())) {
			list.add(new ErrorCheck("Please Enter Mode", "Mode", "3"));
		}
		
		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "4"));
		}
		
		if (StringUtils.isBlank(req.getType())) {
			list.add(new ErrorCheck("Please Enter Type", "Type", "5"));
		}
	
		
		}catch(Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return list;
	}

	public List<ErrorCheck> getProfitCentreieModuleDropDown(GetProfitCentreieModuleDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		try {

			if (StringUtils.isBlank(req.getProposalNo())) {
				list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "1"));
			}

			if (StringUtils.isBlank(req.getBranchCode())) {
				list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
			}

			if (StringUtils.isBlank(req.getMode())) {
				list.add(new ErrorCheck("Please Enter Mode", "Mode", "3"));
			}

			if (StringUtils.isBlank(req.getTransactionNo())) {
				list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "4"));
			}

			if (StringUtils.isBlank(req.getType())) {
				list.add(new ErrorCheck("Please Enter Type", "Type", "5"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return list;
	}

	public List<ErrorCheck> getDepartmentieModuleDropDown(GetDepartmentieModuleDropDownReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		try {

			if (StringUtils.isBlank(req.getProposalNo())) {
				list.add(new ErrorCheck("Please Enter ProposalNo", "ProposalNo", "1"));
			}

			if (StringUtils.isBlank(req.getBranchCode())) {
				list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
			}

			if (StringUtils.isBlank(req.getMode())) {
				list.add(new ErrorCheck("Please Enter Mode", "Mode", "3"));
			}

			if (StringUtils.isBlank(req.getTransactionNo())) {
				list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "4"));
			}

			if (StringUtils.isBlank(req.getType())) {
				list.add(new ErrorCheck("Please Enter Type", "Type", "5"));
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return list;
	}

	public List<ErrorCheck> DuplicateCountCheck(DuplicateCountCheckReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		try {

			if (StringUtils.isBlank(req.getBranchCode())) {
				list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
			}

			if (StringUtils.isBlank(req.getUwYear())) {
				list.add(new ErrorCheck("Please Enter UwYear", "UwYear", "3"));
			}

			if (StringUtils.isBlank(req.getPid())) {
				list.add(new ErrorCheck("Please Enter Pid", "Pid", "4"));
			}


		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return list;
	}

	public List<ErrorCheck> GetExchangeRateVali(String incepDate, String accDate, String date) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		try {
		//	Validation val = new Validation();
			if(StringUtils.isNotBlank(incepDate) && StringUtils.isNotBlank(accDate)){
				boolean ins =val.checkDate(incepDate).equalsIgnoreCase("INVALID");
				boolean acc =val.checkDate(accDate).equalsIgnoreCase("INVALID");
				if(!ins&&!acc){
				date = Validation.getMinDateValidate(incepDate, accDate);
				}
				if(StringUtils.isBlank(date) && !acc){
					date =accDate;
				}else if(StringUtils.isBlank(date) && !ins){
					date = incepDate;
				}
			}
			else if(StringUtils.isNotBlank(incepDate) && !val.checkDate(incepDate).equalsIgnoreCase("INVALID")){
				date = incepDate;
			}
			else if(StringUtils.isNotBlank(accDate) &&val.checkDate(accDate).equalsIgnoreCase("INVALID")){
				date =accDate;
			}
			if(!val.checkDate(date).equalsIgnoreCase("INVALID")){
				list.add(new ErrorCheck("Please Enter valid Date", "Date", "1"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String validExchangeDate(String incepDate, String accDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
