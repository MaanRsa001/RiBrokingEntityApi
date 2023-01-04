package com.maan.insurance.validation.Claim;

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
import com.maan.insurance.jpa.service.impl.ClaimJpaServiceImpl;
import com.maan.insurance.model.req.claim.AllocListReq;
import com.maan.insurance.model.req.claim.AllocationListReq;
import com.maan.insurance.model.req.claim.ClaimListMode4Req;
import com.maan.insurance.model.req.claim.ClaimListReq;
import com.maan.insurance.model.req.claim.ClaimPaymentEditReq;
import com.maan.insurance.model.req.claim.ClaimPaymentListReq;
import com.maan.insurance.model.req.claim.ClaimTableListMode2Req;
import com.maan.insurance.model.req.claim.ClaimTableListReq;
import com.maan.insurance.model.req.claim.ContractDetailsModeReq;
import com.maan.insurance.model.req.claim.ContractDetailsReq;
import com.maan.insurance.model.req.claim.ContractidetifierlistReq;
import com.maan.insurance.model.req.claim.GetContractNoReq;
import com.maan.insurance.model.req.claim.GetReInsValueReq;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode12Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode2Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode8Req;
import com.maan.insurance.model.req.claim.ProposalNoReq;
import com.maan.insurance.model.req.claim.claimNoListReq;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.service.impl.QueryImplemention;
import com.maan.insurance.service.impl.Dropdown.DropDownServiceImple;
import com.maan.insurance.validation.CommonCalculation;
import com.maan.insurance.validation.Formatters;

	

@Service
public class ClaimValidation {
	final static DecimalFormat twoDigit = new DecimalFormat("###0.00");
	private Logger log = LogManager.getLogger(QueryImplemention.class);
	private Properties prop = new Properties();
	@Autowired
	private ClaimJpaServiceImpl claimImpl;
	
	@Autowired
	private DropDownServiceImple dropDowmImpl;
	
	@Autowired
	private Formatters fm;
	
	@Autowired
	private DropDownServiceImple dropDownImple;
	
	@Autowired
	private CommonCalculation calcu;
	
	
public ClaimValidation() {
		
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
	

	
	
	public List<ErrorCheck> PaymentRecieptvalidate(ProposalNoReq req) {
		
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.ProductID"),"ProductID", "01"));
			}
		if(StringUtils.isBlank(req.getContarctno())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.Contarctno"),"Contarctno", "02"));
			}
		if(StringUtils.isBlank(req.getDepartmentId())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.DepartmentId"),"DepartmentId", "03"));
			}
		if(StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.LayerNo"),"LayerNo", "04"));
			}
		return list;
	}

	public List<ErrorCheck> validateMode1ContractDetails(ContractDetailsReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.BranchCode"),"BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.ProductId"),"ProductId", "02"));
			}
		if(StringUtils.isBlank(req.getProposalNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.ProposalNo"),"ProposalNo", "03"));
			}
//		if(StringUtils.isBlank(req.getSumOfPaidAmountOC())) {
//			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.SumOfPaidAmountOC"),"SumOfPaidAmountOC", "04"));
//			}
		return list;
	}

	public List<ErrorCheck> validateMode4ContractDetails(ContractDetailsModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.ClaimNo"),"ClaimNo", "01"));
			}
		if(StringUtils.isBlank(req.getPolicyContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.PolicyContractNo"),"PolicyContractNo", "02"));
			}
		
		return list;
	}

	public List<ErrorCheck> validateMode5ContractDetails(ContractDetailsModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.ClaimNo"),"ClaimNo", "01"));
			}
		if(StringUtils.isBlank(req.getPolicyContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.PolicyContractNo"),"PolicyContractNo", "02"));
			}
		
		return list;
	}

	public List<ErrorCheck> validateMode6ContractDetails(ContractDetailsModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.ClaimNo"),"ClaimNo", "01"));
			}
		if(StringUtils.isBlank(req.getPolicyContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.PolicyContractNo"),"PolicyContractNo", "02"));
			}
		
		return list;
	}

	public List<ErrorCheck> validateMode7ContractDetails(ContractDetailsModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.ClaimNo"),"ClaimNo", "01"));
			}
		if(StringUtils.isBlank(req.getPolicyContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.PolicyContractNo"),"PolicyContractNo", "02"));
			}
		
		return list;
	}

	public List<ErrorCheck> validateMode10ContractDetails(ContractDetailsModeReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.ClaimNo"),"ClaimNo", "01"));
			}
		if(StringUtils.isBlank(req.getPolicyContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.contractDetails.PolicyContractNo"),"PolicyContractNo", "02"));
			}
		
		return list;
	}

	public List<ErrorCheck> validateAllocationList(AllocationListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.allocationList.ContractNo"),"ContractNo", "01"));
			}
		if(StringUtils.isBlank(req.getTransactionNumber())) {
			list.add(new ErrorCheck(prop.getProperty("errors.allocationList.TransactionNumber"),"TransactionNumber", "02"));
			}
		
		return list;
	}






	
	public List<ErrorCheck> claimTableListVali(ClaimTableListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getPolicyContractNo())) {
			list.add(new ErrorCheck("Please Enter PolicyContractNo", "PolicyContractNo", "1"));
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
		return list;
	}

	public List<ErrorCheck> claimTableListMode2Vali(ClaimTableListMode2Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck("Please Enter ClaimNo", "ClaimNo", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "3"));
		}
		return list;
	}

	public List<ErrorCheck> claimListMode4Vali(ClaimListMode4Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "1"));
		}

		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "2"));
		}
		
//		if (StringUtils.isBlank(req.getLayerNo())) {
//			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
//		}
		
//		if (StringUtils.isBlank(req.getClaimPaymentNo())) {
//			list.add(new ErrorCheck("Please Enter ClaimPaymentNo", "ClaimPaymentNo", "4"));
//		}
		if (StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck("Please Enter ClaimNo", "ClaimNo", "5"));
		}
		return list;
	}

	public List<ErrorCheck> getContractNoVali(GetContractNoReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck("Please Enter ClaimNo", "ClaimNo", "1"));
		}

		if (StringUtils.isBlank(req.getProductId())) {
			list.add(new ErrorCheck("Please Enter ProductId", "ProductId", "2"));
		}
		return list;
	}

	public List<ErrorCheck> claimPaymentEditVali(ClaimPaymentEditReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "1"));
		}

		if (StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorCheck("Please Enter ClaimNo", "ClaimNo", "2"));
		}
		
		if (StringUtils.isBlank(req.getLayerNo())) {
			list.add(new ErrorCheck("Please Enter LayerNo", "LayerNo", "3"));
		}
		
		if (StringUtils.isBlank(req.getClaimPaymentNo())) {
			list.add(new ErrorCheck("Please Enter ClaimPaymentNo", "ClaimPaymentNo", "4"));
		}
//		if (StringUtils.isBlank(req.getDropDown())) {
//			list.add(new ErrorCheck("Please Enter DropDown", "DropDown", "5"));
//		}
		return list;
	}

	public List<ErrorCheck> allocListVali(AllocListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getContractNo())) {
			list.add(new ErrorCheck("Please Enter ContractNo", "ContractNo", "1"));
		}

		if (StringUtils.isBlank(req.getTransactionNo())) {
			list.add(new ErrorCheck("Please Enter TransactionNo", "TransactionNo", "2"));
		}	
		return list;
	}

	public List<ErrorCheck> contractidetifierlistVali(ContractidetifierlistReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if("N".equalsIgnoreCase(req.getProductId())|| "".equalsIgnoreCase(req.getProductId())){
			list.add(new ErrorCheck(prop.getProperty("errors.transaction.reqired"),"ProductId","01"));	
		}
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("Please Enter BranchCode"), "BranchCode", "2"));
		}	
		return list;
	}

	public List<ErrorCheck> claimPaymentListVali(ClaimPaymentListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();

		if (StringUtils.isBlank(req.getFlag())) {	
			list.add(new ErrorCheck("Please Enter Flag", "Flag", "1"));
		}

			
		if (StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck("Please Enter BranchCode", "BranchCode", "3"));
		}
		return list;
	}

	public List<ErrorCheck> getReInsValueVali(GetReInsValueReq req) {
	

		return null;
	}

	public List<ErrorCheck> validateclaimlist(ClaimListReq req) {
		
		return null;
	}

	public List<ErrorCheck>  claimsInsertValidation(InsertCliamDetailsMode2Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val = new Validation();
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		
			if (val.isNull(req.getStatusofClaim()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.status_of_claim.required"),"StatusofClaim","01"));
			}else if("Closed".equals(req.getStatusofClaim()))
			{
				if(Double.parseDouble(StringUtils.isBlank(req.getOsAmt())?"0":req.getOsAmt())!=0){
					list.add(new ErrorCheck(prop.getProperty("claim.close.osAmout"),"OsAmt","01"));
				}
				
			}
			if("3".equalsIgnoreCase(req.getProductId())){
				if(StringUtils.isBlank(req.getDepartmentClass())){
					list.add(new ErrorCheck(prop.getProperty("error.departmentclass"),"DepartmentClass","02"));
				}
			}
			if(!"2".equalsIgnoreCase(req.getProductId())){
				if (val.checkDate(req.getDateofLoss()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.date_of_Loss.required"), "ProductId", "01"));
				}else if(val.belowCurrentDate(req.getDateofLoss()).equalsIgnoreCase("INVALID"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.date_of_Loss.CurrentDate"), "ProductId", "01"));				
				} 
				else if("3".equalsIgnoreCase(req.getProductId()) && req.getBasis()!=null && "2".equals(req.getBasis())) {
					if(Validation.ValidateTwo(req.getFrom(), req.getDateofLoss()).equalsIgnoreCase("Invalid"))
					{ 
						list.add(new ErrorCheck(prop.getProperty("errors.date_of_Loss.inception.Error"), "ProductId", "01"));
					} else if(Validation.ValidateTwo(req.getDateofLoss(), req.getTo()).equalsIgnoreCase("Invalid"))
					{ 
						list.add(new ErrorCheck(prop.getProperty("errors.date_of_Loss.inception.Error"), "ProductId", "01"));
					}
				}
				else if("1".equalsIgnoreCase(req.getProductId())) {
					if(Validation.ValidateTwo(req.getFrom(), req.getDateofLoss()).equalsIgnoreCase("Invalid"))
					{ 
						list.add(new ErrorCheck(prop.getProperty("errors.date_of_Loss.inception.Error.pid1"), "ProductId", "01"));
					} else if(Validation.ValidateTwo(req.getDateofLoss(), req.getTo()).equalsIgnoreCase("Invalid"))
					{ 
						list.add(new ErrorCheck(prop.getProperty("errors.date_of_Loss.inception.Error.pid1"), "ProductId", "01"));
					}
				}					
			}
			if (val.checkDate(req.getReportDate()).equalsIgnoreCase("INVALID")) {

				list.add(new ErrorCheck(prop.getProperty("errors.report_Date.required"), "ReportDate", "01"));
			}
			else if(val.belowCurrentDate(req.getReportDate()).equalsIgnoreCase("INVALID"))
			{
				list.add(new ErrorCheck(prop.getProperty("errors.report_Date.Error"), "ReportDate", "01"));
			}
			if (val.checkDate(req.getReservePositionDate()).equalsIgnoreCase("INVALID")) {

				list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.required"), "ReservePositionDate", "01"));
			}
			else if(val.belowCurrentDate(req.getReservePositionDate()).equalsIgnoreCase("INVALID"))
			{
				list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.Invalid"), "ReservePositionDate", "01"));
			}
			if(Validation.ValidateTwo(req.getDateofLoss(), req.getReportDate()).equalsIgnoreCase("Invalid"))
			{ 
				list.add(new ErrorCheck(prop.getProperty("errors.report_Date1.Error"), "DateofLoss,ReportDate", "01"));
			}
			if(Validation.ValidateTwo(req.getDateofLoss(), req.getReservePositionDate()).equalsIgnoreCase("Invalid"))
			{ 
				list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.Error"), "DateofLoss,ReservePositionDate", "01"));
			}
			if(Validation.ValidateTwo(req.getReservePositionDate(), req.getReportDate()).equalsIgnoreCase("Invalid"))
			{ 
				list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.Error1"), "ReservePositionDate,ReportDate", "01"));
			}
			if (val.isNull(req.getCreatedBy()).equalsIgnoreCase("")) {
				
			}
			else if(Validation.ValidateTwo(req.getReportDate(),req.getCreatedDate()).equalsIgnoreCase("invalid"))
			{
				list.add(new ErrorCheck(prop.getProperty("errors.created_Date.greater"), "ReportDate,CreatedDate", "01"));
			}
			else if(val.belowCurrentDate(req.getCreatedDate()).equalsIgnoreCase("Invalid"))
			{
				list.add(new ErrorCheck(prop.getProperty("errors.created_Date.invalid"), "CreatedDate", "01"));
			}
			if (val.checkDate(req.getCreatedDate()).equalsIgnoreCase("INVALID"))
			{
				list.add(new ErrorCheck(prop.getProperty("errors.created_Date.required"), "CreatedDate", "01"));
			}
			else if(Validation.ValidateTwo(req.getDateofLoss(),req.getCreatedDate()).equalsIgnoreCase("invalid"))
			{
				list.add(new ErrorCheck(prop.getProperty("errors.created_Date.greater1"), "DateofLoss,CreatedDate", "01"));
			}
			 if(Validation.ValidateTwo(req.getReservePositionDate(), req.getCreatedDate()).equalsIgnoreCase("Invalid"))
			{ 
				list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.Error2"), "CreatedDate,ReservePositionDate", "01"));
			}
			 else if(Validation.ValidateTwo(req.getAcceptenceDate(),req.getCreatedDate()).equalsIgnoreCase("invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.claim.acDate"), "AcceptenceDate,CreatedDate", "01"));
				}
			 if(!"Reopened".equalsIgnoreCase(req.getStatusofClaim()) && !"Repudiate".equalsIgnoreCase(req.getStatusofClaim())){
			if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getCreatedDate()).equalsIgnoreCase("")){
				if(dropDowmImpl.Validatethree(req.getBranchCode(), req.getCreatedDate())==0){
					list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.Claim.reg")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
				}	
			}
			 }
			if (val.isNull(req.getLossDetails()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Details.required"), "LossDetails", "01"));
			}

			if (val.isNull(req.getCauseofLoss()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.cause_of_Loss.required"), "CauseofLoss", "01"));
			}

			if (val.isNull(req.getLocation()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.location.required"), "Location", "01"));
			}
			if(StringUtils.isBlank(req.getCedentClaimNo())){
				list.add(new ErrorCheck(prop.getProperty("error.cedent.claim.no"), "CedentClaimNo", "01"));
			}else {
				String claimno=claimImpl.getDuplicateCedentClaim(req);
			if(StringUtils.isNotBlank(claimno)) {
			list.add(new ErrorCheck(prop.getProperty("error.cedent.claim.no.duplicate")+claimno, "claimno", "01"));
				}
			}
			
			if(val.isSelect(req.getCurrecny()).equalsIgnoreCase(""))
			{
				list.add(new ErrorCheck(prop.getProperty("Error.Currency.required"), "Currecny", "01"));
			}
			if("2".equalsIgnoreCase(req.getProductId())){
			if(StringUtils.isBlank(req.getClaimdepartId())){
				list.add(new ErrorCheck(prop.getProperty("error.departId.required"), "ClaimdepartId", "01"));
			}
			}if(!"3".equalsIgnoreCase(req.getProductId()) ){
				if(StringUtils.isBlank(req.getSubProfitId())){
					list.add(new ErrorCheck(prop.getProperty("error.subProfit_center.required"), "SubProfitId", "01"));
				}else{
					req.setSubProfitId((req.getSubProfitId()).replaceAll(" ", ""));
				}
			}
			if (val.isNull(req.getLossEstimateOrigCurr()).equalsIgnoreCase("")) {
				if( "1".equals(req.getProductId())){
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate100OC.required" ), "ProductId", "01"));
				}else if( "2".equals(req.getProductId())){
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate100OC.required.cash" ), "ProductId", "01"));
				}else {
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate100OC.required.loss" ), "ProductId", "01"));
				}
				
			} else
			{
				req.setLossEstimateOrigCurr((req.getLossEstimateOrigCurr()).replaceAll(",",""));
				if (val.numbervalid(req.getLossEstimateOrigCurr()).equalsIgnoreCase("INVALID")) 
				{
					if( "1".equals(req.getProductId())){
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate100OC_invalid"), "ProductId", "01"));
					}else if( "2".equals(req.getProductId())){
						list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate100OC_invalid.cash" ), "ProductId", "01"));
					}else {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate100OC_invalid.loss" ), "ProductId", "01"));
					}
					
				}
			}
			if(StringUtils.isNotBlank(req.getGrossLossFGU())){
				req.setGrossLossFGU((req.getGrossLossFGU()).replaceAll(",",""));
				if(Double.parseDouble(req.getLossEstimateOrigCurr())>Double.parseDouble(req.getGrossLossFGU())){
					if( "1".equals(req.getProductId())){
					
					}else if( "2".equals(req.getProductId())){
						
					}else {
					
					}
					
				}
			}
			boolean flag=true;
			if(StringUtils.isBlank(req.getInsuredName())) {
				list.add(new ErrorCheck(prop.getProperty("error.claim.insuredname"), "InsuredName", "01"));
			}
			
			
			if (val.isNull(req.getLossEstimateOurShareOrigCurr()).equalsIgnoreCase("")) {
				if( "1".equals(req.getProductId())){
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Orig_Curr.required"), "LossEstimateOurShareOrigCurr", "01"));
				}else if( "2".equals(req.getProductId())){
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Orig_Curr.required.cash" ), "ProductId", "01"));
				}else {
				list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Orig_Curr.required.loss" ), "ProductId", "01"));
				}
				flag=false;
			} else
			{
				req.setLossEstimateOurShareOrigCurr((req.getLossEstimateOurShareOrigCurr()).replaceAll(",",""));
				if (val.numbervalid(req.getLossEstimateOurShareOrigCurr()).equalsIgnoreCase("INVALID")) 
				{
					if( "1".equals(req.getProductId())){
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Orig_Curr.invalid"), "ProductId", "01"));
					}else if( "2".equals(req.getProductId())){
						list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Orig_Curr.invalid.cash" ), "ProductId", "01"));
					}else {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Orig_Curr.invalid.loss" ), "ProductId", "01"));
					}
					flag=false;
				}
				else {
					String ans = calculateClaimRegistration(req,"Gross");
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getLossEstimateOurShareOrigCurr())){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "LossEstimateOurShareOrigCurr", "01"));
						
					}else{
						req.setLossEstimateOurShareOrigCurr(ans);
					}
					
				}
			}
			if (val.isNull(req.getExcRate()).equalsIgnoreCase("")) {
				list.add(new ErrorCheck(prop.getProperty("errors.exc_Rate.required"), "ExcRate", "01"));
				flag=false;
			}
			if(flag && "1".equals(req.getProductId())&& ! val.isNull(req.getSumInsOSDC()).equalsIgnoreCase(""))
			{
				req.setSumInsOSDC((req.getSumInsOSDC()).replaceAll(",",""));
				if((Double.parseDouble(req.getLossEstimateOurShareOrigCurr())/Double.parseDouble(req.getExcRate()))>Double.parseDouble(req.getSumInsOSDC()))
				{
					//list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Orig_Curr.greaterSIOSDC"), "", "01"));
				}
			}
			if(StringUtils.isNotBlank(req.getLossEstimateOrigCurr()) && StringUtils.isNotBlank(req.getLossEstimateOurShareOrigCurr())){
				req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
				double value = ((Double.parseDouble(req.getLossEstimateOrigCurr())*Double.parseDouble(req.getSignedShare()))/100);
				final double dround=Math.round(value*100.0)/100.0;
				final String valu=twoDigit.format(dround);
				if(Double.parseDouble(valu)<Double.parseDouble(req.getLossEstimateOurShareOrigCurr())){
					if( "1".equals(req.getProductId())){
					list.add(new ErrorCheck(prop.getProperty("error.our.share.loss.value"), "ProductId", "01"));
					}else if( "2".equals(req.getProductId())){
						list.add(new ErrorCheck(prop.getProperty("error.our.share.loss.value.cash" ), "ProductId", "01"));
					}else {
					list.add(new ErrorCheck(prop.getProperty("error.our.share.loss.value.loss" ), "ProductId", "01"));
					}
				}
			}
			
			if(StringUtils.isBlank(req.getRecordFees())){
				list.add(new ErrorCheck(prop.getProperty("error.record.fees"), "RecordFees", "01"));
			}
			else{
				if("Yes".equalsIgnoreCase(req.getRecordFees())){
					if(StringUtils.isBlank(req.getSurveyorAdjesterPerOC())){
						list.add(new ErrorCheck(prop.getProperty("surveyor.error.per"), "SurveyorAdjesterPerOC", "01"));
					}
					else{
						req.setSurveyorAdjesterPerOC(req.getSurveyorAdjesterPerOC().replaceAll(",", ""));
					}
					if(StringUtils.isBlank(req.getSurveyorAdjesterOurShareOC())){
						list.add(new ErrorCheck(prop.getProperty("surveyor.error.our.share.per"), "SurveyorAdjesterOurShareOC", "01"));
					}
					else{
						req.setSurveyorAdjesterOurShareOC(req.getSurveyorAdjesterOurShareOC().replaceAll(",", ""));
						String ans = calculateClaimRegistration(req,"Surveyor");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getSurveyorAdjesterOurShareOC())){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "SurveyorAdjesterOurShareOC", "01"));
							
						}else{
							req.setSurveyorAdjesterOurShareOC(ans);
						}
					}
					if(StringUtils.isNotBlank(req.getSurveyorAdjesterPerOC()) && StringUtils.isNotBlank(req.getSurveyorAdjesterOurShareOC())){
						req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
						double value = ((Double.parseDouble(req.getSurveyorAdjesterPerOC())*Double.parseDouble(req.getSignedShare()))/100);
						final double dround=Math.round(value*100.0)/100.0;
						final String valu=twoDigit.format(dround);
						if(Double.parseDouble(valu)<Double.parseDouble(req.getSurveyorAdjesterOurShareOC())){
							list.add(new ErrorCheck(prop.getProperty("error.our.share.value"), "SurveyorAdjesterOurShareOC", "01"));
						}
					}
					if(StringUtils.isBlank(req.getOtherProfessionalPerOc())){
						list.add(new ErrorCheck(prop.getProperty("other.error.per"), "OtherProfessionalPerOc", "01"));
					}
					else{
						req.setOtherProfessionalPerOc(req.getOtherProfessionalPerOc().replaceAll(",", ""));
					}
					if(StringUtils.isBlank(req.getProfessionalOurShareOc())){
						list.add(new ErrorCheck(prop.getProperty("other.error.our.share.per"), "ProfessionalOurShareOc", "01"));
					}
					else{
						req.setProfessionalOurShareOc(req.getProfessionalOurShareOc().replaceAll(",", ""));
						req.setSurveyorAdjesterOurShareOC(req.getSurveyorAdjesterOurShareOC().replaceAll(",", ""));
						String ans =calculateClaimRegistration(req,"professional");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getProfessionalOurShareOc())){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"), "ProfessionalOurShareOc", "01"));
							log.info("Insertion Failed. Please retry. If problem persists, please contact support.");
						}else{
							req.setProfessionalOurShareOc(ans);
						}
					}
					if(StringUtils.isNotBlank(req.getOtherProfessionalPerOc()) && StringUtils.isNotBlank(req.getProfessionalOurShareOc())){
						req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
						double value = ((Double.parseDouble(req.getOtherProfessionalPerOc())*Double.parseDouble(req.getSignedShare()))/100);
						final double dround=Math.round(value*100.0)/100.0;
						final String valu=twoDigit.format(dround);
						if(Double.parseDouble(valu)<Double.parseDouble(req.getProfessionalOurShareOc())){
							list.add(new ErrorCheck(prop.getProperty("error.our.share.other.value"), "ProfessionalOurShareOc", "01"));
						}
					}
				}
			}
			if(StringUtils.isBlank(req.getRecordIbnr())){
				list.add(new ErrorCheck(prop.getProperty("error.record.ibnr"), "RecordIbnr", "01"));
			}
			else{
				if("Yes".equalsIgnoreCase(req.getRecordIbnr())){
					if(StringUtils.isBlank(req.getIbnrPerOc())){
						list.add(new ErrorCheck(prop.getProperty("ibnr.error.per"), "RecordIbnr", "01"));
					}
					else{
						req.setIbnrPerOc(req.getIbnrPerOc().replaceAll(",", ""));
					}
					if(StringUtils.isBlank(req.getIbnrOurShareOc())){
						list.add(new ErrorCheck(prop.getProperty("ibnr.error.our.share.per"), "IbnrOurShareOc", "01"));
					}
					else{
						req.setIbnrOurShareOc(req.getIbnrOurShareOc().replaceAll(",", ""));
					}
					
				}
				if(StringUtils.isNotBlank(req.getIbnrPerOc()) && StringUtils.isNotBlank(req.getIbnrOurShareOc())){
					req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
					double value = ((Double.parseDouble(req.getIbnrPerOc())*Double.parseDouble(req.getSignedShare()))/100);
					final double dround=Math.round(value*100.0)/100.0;
					final String valu=twoDigit.format(dround);
					if(Double.parseDouble(valu)<Double.parseDouble(req.getIbnrOurShareOc())){
						list.add(new ErrorCheck(prop.getProperty("error.our.share.ibnr.value"), "IbnrOurShareOc", "01"));
					}
			}
			}
			if (val.isNull(req.getRiRecovery()).equalsIgnoreCase("")) {

				list.add(new ErrorCheck(prop.getProperty("errors.ri_Recovery.required"), "RiRecovery", "01"));
			}
			else if(req.getRiRecovery().equalsIgnoreCase("Yes") && val.isSelect(req.getRecoveryFrom()).equalsIgnoreCase(""))
			{
				list.add(new ErrorCheck(prop.getProperty("errors.recovery_from.required"), "RiRecovery", "01"));
			}


			if (!val.isNull(req.getStatusofClaim()).equalsIgnoreCase("") && req.getStatusofClaim().equalsIgnoreCase("Closed") && val.isNull(req.getRemarks()).equalsIgnoreCase("")) 
			{
				list.add(new ErrorCheck(prop.getProperty("errors.remarks.required"), "StatusofClaim", "01"));				 
			}	
			req.setLossEstimateOurShareOrigCurr((req.getLossEstimateOurShareOrigCurr()).replaceAll(",",""));
			if("1".equalsIgnoreCase(req.getProductId())){
			if (claimImpl.BusinessValidation(req, 3))
			  {
				list.add(new ErrorCheck(prop.getProperty("DateInvalide.Compare.InceptionDate"), "ProductId", "01"));
			  }	
			}
			if("Reopened".equals(req.getStatusofClaim()))
			{
				req.setPreReOpenDate(dropDownImple.getpreReopendDate(req.getPolicyContractNo(),req.getClaimNo(),"Reopen"));
				if (val.checkDate(req.getReOpenDate()).equalsIgnoreCase("INVALID")) 
				{
					list.add(new ErrorCheck(prop.getProperty("errors.claim.reopendate.required"), "ReOpenDate", "01"));
				}
				else{
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getReOpenDate()).equalsIgnoreCase("")){
					if(dropDowmImpl.Validatethree(req.getBranchCode(), req.getReOpenDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.Claim.reopen")+req.getOpenPeriodDate(), "OpenPeriodDate", "01"));
					}
				}
				 if(!val.isNull(req.getReOpenDate()).equalsIgnoreCase("") && !val.isNull(req.getCreatedDate()).equalsIgnoreCase(""))  {
					if(Validation.ValidateTwo(req.getCreatedDate(),req.getReOpenDate()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("error.claim.registration.reopen.gr"), "CreatedDate,ReOpenDate", "01"));
					}
				}
				 if(!val.isNull(req.getReOpenDate()).equalsIgnoreCase("") && !val.isNull(req.getCreatedDate()).equalsIgnoreCase(""))  {
					if(Validation.ValidateTwo(Validation.getMaxDateValidate(req.getCreatedDate(), req.getPreReOpenDate()),req.getReOpenDate()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("error.claim.registration.reopen.pr"), "CreatedDate,ReOpenDate", "01"));
					}
				}
			}
			}
			if("Repudiate".equals(req.getStatusofClaim()))
			{
				String preDate = dropDownImple.getpreReopendDate(req.getPolicyContractNo(),req.getClaimNo(),"Reputed");
				if (val.checkDate(req.getReputedDate()).equalsIgnoreCase("INVALID")) 
				{
					list.add(new ErrorCheck(prop.getProperty("errors.claim.repudiatedate.required"), "ReputedDate", "01"));
				}
				else{
					if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getReputedDate()).equalsIgnoreCase("")){
					if(dropDowmImpl.Validatethree(req.getBranchCode(), req.getReputedDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.Claim.repudiate")+req.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
				}
				if(!val.isNull(req.getReputedDate()).equalsIgnoreCase("") && !val.isNull(req.getCreatedDate()).equalsIgnoreCase(""))  {
					if(Validation.ValidateTwo(req.getCreatedDate(),req.getReputedDate()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("error.claim.registration.repudiate.gr"), "CreatedDate,ReputedDate", "01"));
					}
				}
				 if(!val.isNull(req.getReputedDate()).equalsIgnoreCase("") && !val.isNull(req.getCreatedDate()).equalsIgnoreCase(""))  {
					if(Validation.ValidateTwo(Validation.getMaxDateValidate(req.getCreatedDate(), preDate),req.getReputedDate()).equalsIgnoreCase("invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("error.claim.registration.repudi.pr"), "CreatedDate,ReputedDate", "01"));
					}
				}
			}
			}
			return list;
			}
	public String calculateClaimRegistration(InsertCliamDetailsMode2Req req, String type) {
		String result ="";
		double amt = 0;
		try{
			if("Surveyor".equalsIgnoreCase(type)){
				
				String premiumRate=StringUtils.isBlank(req.getSignedShare())?"0":req.getSignedShare().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req.getSurveyorAdjesterPerOC())?"0":req.getSurveyorAdjesterPerOC().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}
			else if ("professional".equalsIgnoreCase(type)){
				
				String premiumRate=StringUtils.isBlank(req.getSignedShare())?"0":req.getSignedShare().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req.getOtherProfessionalPerOc())?"0":req.getOtherProfessionalPerOc().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}else if ("Gross".equalsIgnoreCase(type)){
				
				String premiumRate=StringUtils.isBlank(req.getSignedShare())?"0":req.getSignedShare().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req.getLossEstimateOrigCurr())?"0":req.getLossEstimateOrigCurr().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			log.debug(""+e);
		}
		return result;
	}
	

	public List<ErrorCheck> updationValidation8(InsertCliamDetailsMode8Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		final Validation val = new Validation();
		
		if(claimImpl.BusinessValidation(req, 6)){
			list.add(new ErrorCheck(prop.getProperty("error.claimClosed"),"claimClosed","01"));
			}else{	
				if("Yes".equalsIgnoreCase(req.getReverseClaimYN())&&"Yes".equalsIgnoreCase(req.getCloseClaimYN())){
					list.add(new ErrorCheck(prop.getProperty("claim.reserveClaimloseYN"),"reserveClaimloseYN","01"));
				}else if("Yes".equalsIgnoreCase(req.getReverseClaimYN()))
				{
				if (val.isNull(req.getUpdateRivisedoriginalCur()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Revised_Orig_Curr.required"),"UpdateRivisedoriginalCur","01"));
				} 
				else
				{
					req.setUpdateRivisedoriginalCur((req.getUpdateRivisedoriginalCur()).replaceAll(",",""));
					if (val.isValidNo(req.getUpdateRivisedoriginalCur()).equalsIgnoreCase("INVALID")) {
						list.add(new ErrorCheck(prop.getProperty("errors.loss_Estimate_Revised_Orig_Curr.invalid"),"UpdateRivisedoriginalCur","01"));
					}else{
						String ans = calculateClaimReversal(req,"OutRes");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getUpdateRivisedoriginalCur())){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"UpdateRivisedoriginalCur","01"));
							
						}else{
							req.setUpdateRivisedoriginalCur(ans);
						}
					}
					
				}
				if(StringUtils.isBlank(req.getUpdateRivisedpercentage())){
					list.add(new ErrorCheck(prop.getProperty("error.update.revised"),"","UpdateRivisedpercentage"));
				}
				if(StringUtils.isNotBlank(req.getUpdateRivisedpercentage()) && StringUtils.isNotBlank(req.getUpdateRivisedoriginalCur())){
					req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
					double value = ((Double.parseDouble(req.getUpdateRivisedpercentage())*Double.parseDouble(req.getSignedShare()))/100);
					final double dround=Math.round(value*100.0)/100.0;
					final String valu=twoDigit.format(dround);
					if(Double.parseDouble(valu)<Double.parseDouble(req.getUpdateRivisedoriginalCur())){
						list.add(new ErrorCheck(prop.getProperty("error.our.share.update.revised.value"),"UpdateRivisedoriginalCur","01"));
					}
				}
				if(StringUtils.isBlank(req.getUpdaterecordFees())){
					list.add(new ErrorCheck(prop.getProperty("error.record.fees"),"UpdaterecordFees","01"));
				}
				else{
					if("Yes".equalsIgnoreCase(req.getUpdaterecordFees())){
						if(StringUtils.isBlank(req.getUpdatesurveyorAdjesterPerOC())){
							list.add(new ErrorCheck(prop.getProperty("surveyor.error.per"),"UpdaterecordFees","01"));
						}
						else{
							req.setUpdatesurveyorAdjesterPerOC(req.getUpdatesurveyorAdjesterPerOC().replaceAll(",", ""));
						}
						if(StringUtils.isBlank(req.getUpdatesurveyorAdjesterOurShareOC())){
							list.add(new ErrorCheck(prop.getProperty("surveyor.error.our.share.per"),"UpdatesurveyorAdjesterOurShareOC","01"));
						}
						else{
							req.setUpdatesurveyorAdjesterOurShareOC(req.getUpdatesurveyorAdjesterOurShareOC().replaceAll(",", ""));
							String ans = calculateClaimReversal(req,"Surveyor");
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getUpdatesurveyorAdjesterOurShareOC())){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"UpdatesurveyorAdjesterOurShareOC","01"));
								
							}else{
								req.setUpdatesurveyorAdjesterOurShareOC(ans);
							}
						}
						if(StringUtils.isNotBlank(req.getUpdatesurveyorAdjesterOurShareOC()) && StringUtils.isNotBlank(req.getUpdatesurveyorAdjesterPerOC())){
							req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
							double value = ((Double.parseDouble(req.getUpdatesurveyorAdjesterPerOC())*Double.parseDouble(req.getSignedShare()))/100);
							final double dround=Math.round(value*100.0)/100.0;
							final String valu=twoDigit.format(dround);
							if(Double.parseDouble(valu)<Double.parseDouble(req.getUpdatesurveyorAdjesterOurShareOC())){
								list.add(new ErrorCheck(prop.getProperty("error.our.share.value"),"UpdatesurveyorAdjesterOurShareOC","01"));
							}
						}
						
						if(StringUtils.isBlank(req.getUpdateotherProfessionalPerOc())){
							list.add(new ErrorCheck(prop.getProperty("other.error.per"),"UpdateotherProfessionalPerOc","01"));
						}
						else{
							req.setUpdateotherProfessionalPerOc(req.getUpdateotherProfessionalPerOc().replaceAll(",", ""));
						}
						if(StringUtils.isBlank(req.getUpdateprofessionalOurShareOc())){
							list.add(new ErrorCheck(prop.getProperty("other.error.our.share.per"),"UpdateotherProfessionalPerOc","01"));
						}
						else{
							req.setUpdateprofessionalOurShareOc(req.getUpdateprofessionalOurShareOc().replaceAll(",", ""));
							String ans = calculateClaimReversal(req,"Professional");
							if(Double.parseDouble(ans)!=Double.parseDouble(req.getUpdateprofessionalOurShareOc())){
								list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"UpdateotherProfessionalPerOc","01"));
								
							}else{
								req.setUpdateprofessionalOurShareOc(ans);
							}
						}
						if(StringUtils.isNotBlank(req.getUpdateotherProfessionalPerOc()) && StringUtils.isNotBlank(req.getUpdateprofessionalOurShareOc())){
							req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
							double value = ((Double.parseDouble(req.getUpdateotherProfessionalPerOc())*Double.parseDouble(req.getSignedShare()))/100);
							final double dround=Math.round(value*100.0)/100.0;
							final String valu=twoDigit.format(dround);
							if(Double.parseDouble(valu)<Double.parseDouble(req.getUpdateprofessionalOurShareOc())){
								list.add(new ErrorCheck(prop.getProperty("error.our.share.other.value"),"UpdateotherProfessionalPerOc","01"));
							}
						}
				}
				}
				if(StringUtils.isBlank(req.getUpdaterecordIbnr())){
					list.add(new ErrorCheck(prop.getProperty("update.error.record.ibnr"),"UpdaterecordIbnr","01"));
				}
				else{
					if("Yes".equalsIgnoreCase(req.getUpdaterecordIbnr())){
						if(StringUtils.isBlank(req.getUpdateibnrPerOc())){
							list.add(new ErrorCheck(prop.getProperty("ibnr.error.per"),"UpdaterecordIbnr","01"));
						}
						else{
							req.setUpdateibnrPerOc(req.getUpdateibnrPerOc().replaceAll(",", ""));
						}
						if(StringUtils.isBlank(req.getUpdateibnrOurShareOc())){
							list.add(new ErrorCheck(prop.getProperty("ibnr.error.our.share.per"),"UpdateibnrOurShareOc","01"));
						}
						else{
							req.setUpdateibnrOurShareOc(req.getUpdateibnrOurShareOc().replaceAll(",", ""));
						}
						if(StringUtils.isNotBlank(req.getUpdateibnrOurShareOc()) && StringUtils.isNotBlank(req.getUpdateibnrPerOc())){
						req.setSignedShare(req.getSignedShare().replaceAll(",", ""));
						double value = ((Double.parseDouble(req.getUpdateibnrPerOc())*Double.parseDouble(req.getSignedShare()))/100);
							final double dround=Math.round(value*100.0)/100.0;
							final String valu=twoDigit.format(dround);
						if(Double.parseDouble(valu)<Double.parseDouble(req.getUpdateibnrOurShareOc())){
							list.add(new ErrorCheck(prop.getProperty("error.our.share.ibnr.value"),"UpdateibnrOurShareOc","01"));
						}
					}
				}
				}
				if(StringUtils.isBlank(req.getTotalReserveOSOC())){
					list.add(new ErrorCheck(prop.getProperty("errors.total_reserveosoc.required"),"TotalReserveOSOC","01"));
				}
				else{
					req.setTotalReserveOSOC(req.getTotalReserveOSOC().replaceAll(",",""));
					String ans = calculateClaimReversal(req,"Total");
					if(Double.parseDouble(ans)!=Double.parseDouble(req.getTotalReserveOSOC().replaceAll(",",""))){
						list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"TotalReserveOSOC","01"));
						
					}else{
						req.setTotalReserveOSOC(ans);
					}
				}
				if (val.isNull(req.getUpdateReference()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.update_Reference.required"),"UpdateReference","01"));
				}
				
				if(val.isNull(req.getCliamupdateDate()).equalsIgnoreCase(""))
				{
					list.add(new ErrorCheck(prop.getProperty("Error.UpdateDate.Requireed"),"CliamupdateDate","01"))	;
				}
				else if(val.checkDate(req.getCliamupdateDate()).equalsIgnoreCase("INVALID"))
				{
					list.add(new ErrorCheck(prop.getProperty("Error.UpdateDate.DateError"),"CliamupdateDate","01"));
				}
				else if(val.ValidateTwoDates(claimImpl.getClaimDate(req,1),req.getCliamupdateDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("Error.UpdateDate.Patdate"),"ClaimDate,CliamupdateDate","01"));
				}else if(val.ValidateTwoDates(claimImpl.getClaimDate(req,4),req.getCliamupdateDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("Error.UpdateDtLastPayDt"),"ClaimDate,CliamupdateDate","01"));
				}
				else if(val.belowCurrentDate(req.getCliamupdateDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("Error.UpdateDate.CurrentDate"),"CliamupdateDate","01"));
				}
				else if(val.ValidateTwoDates(claimImpl.getClaimDate(req,2),req.getCliamupdateDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("Error.UpdateDate.LossDate"),"ClaimDate,CliamupdateDate","01"));
				}
				else if(Validation.ValidateTwo(req.getDateofLoss(),req.getCliamupdateDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.created_Date.invalid2"),"DateofLoss,CliamupdateDate","01"));
				}
				if(val.isNull(req.getReservePositionDate()).equalsIgnoreCase(""))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.required"),"ReservePositionDate","01"));
				}
				else if(val.checkDate(req.getReservePositionDate()).equalsIgnoreCase("INVALID"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.Invalid"),"ReservePositionDate","01"));
				}
				else if(val.ValidateTwoDates(claimImpl.getClaimDate(req,5),req.getReservePositionDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.reservepositionDate.Error3"),"ClaimDate,ReservePositionDate","01"));
				}
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getCliamupdateDate()).equalsIgnoreCase("")){
					int res = dropDowmImpl.Validatethree(req.getBranchCode(), req.getCliamupdateDate());
					if(res == 0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.Claim.update")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
					}
			}else if("No".equals(req.getReverseClaimYN())){
				if("None".equals(req.getCloseClaimYN())||"No".equalsIgnoreCase(req.getCloseClaimYN())){
					list.add(new ErrorCheck(prop.getProperty("claim.reserveNo"),"CloseClaimYN","01"));
				}
				if(val.checkDate(req.getClaimclosedDate()).equalsIgnoreCase("INVALID")){
					list.add(new ErrorCheck(prop.getProperty("claim.closed.date"),"ClaimclosedDate","01"));
				}else{
					
					if(val.ValidateTwoDates(claimImpl.getClaimDate(req,5),req.getClaimclosedDate()).equalsIgnoreCase("Invalid"))
					{
						list.add(new ErrorCheck(prop.getProperty("Error.lost.reserve.Date"),"ClaimDate,ClaimclosedDate","01"));
					}
				}
				
		}
	
}
		
		return list;
	}
	public String calculateClaimReversal(InsertCliamDetailsMode8Req req, String type) {
		String result ="";
		double amt = 0;
		String premiumRate = "";
		String coverlimit ="";
		try{
			if("Surveyor".equalsIgnoreCase(type)){
				
				 premiumRate=StringUtils.isBlank(req.getSignedShare())?"0":req.getSignedShare().replaceAll(",", "");
				 coverlimit=StringUtils.isBlank(req.getUpdatesurveyorAdjesterPerOC())?"0":req.getUpdatesurveyorAdjesterPerOC().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}
			else if ("Professional".equalsIgnoreCase(type)){
				
				 premiumRate=StringUtils.isBlank(req.getSignedShare())?"0":req.getSignedShare().replaceAll(",", "");
				 coverlimit=StringUtils.isBlank(req.getUpdateotherProfessionalPerOc())?"0":req.getUpdateotherProfessionalPerOc().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
				result = fm.formatter(Double.toString(amt)).replaceAll(",", "");
			}else if ("OutRes".equalsIgnoreCase(type)){	
				
				 premiumRate=StringUtils.isBlank(req.getSignedShare())?"0":req.getSignedShare().replaceAll(",", "");
				 coverlimit=StringUtils.isBlank(req.getUpdateRivisedpercentage())?"0":req.getUpdateRivisedpercentage().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) * Double.parseDouble(coverlimit))/100;
				result = fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}
			if("Total".equalsIgnoreCase(type)){
			
				 premiumRate=StringUtils.isBlank(req.getUpdatesurveyorAdjesterOurShareOC())?"0":req.getUpdatesurveyorAdjesterOurShareOC().replaceAll(",", "");
				 coverlimit=StringUtils.isBlank(req.getUpdateprofessionalOurShareOc())?"0":req.getUpdateprofessionalOurShareOc().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) + Double.parseDouble(req.getUpdateRivisedoriginalCur().replaceAll(",", "")) + Double.parseDouble(coverlimit));
				result = fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		return result;
	}




	public List<ErrorCheck> insertCliamDetailsMode3Vali(InsertCliamDetailsMode3Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val = new Validation();	
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		try {
					
			InsertCliamDetailsMode8Req req1 = new InsertCliamDetailsMode8Req();
			req1.setPolicyContractNo(req.getPolicyContractNo());
			req1.setClaimNo(req.getClaimNo());
			
				if(claimImpl.BusinessValidation(req1, 6)){
					list.add(new ErrorCheck(prop.getProperty("error.claimClosed"),"claimClosedStatus","01"));
				}
				else if(claimImpl.BusinessValidation(req1,10)){
					list.add(new ErrorCheck(prop.getProperty("error.claimRepudiated"),"claimRepudiatedStatus","01"));
				}
				else{ 
					
				if (val.isNull(req.getPaidAmountOrigcurr()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.paid_Amount_Orig_curr.required"),"PaidAmountOrigcurr","01"));
				} else
				{
					req.setPaidAmountOrigcurr((req.getPaidAmountOrigcurr()).replaceAll(",",""));
					if (val.numbervalid(req.getPaidAmountOrigcurr()).equalsIgnoreCase("INVALID")) 
					{
						list.add(new ErrorCheck(prop.getProperty("errors.paid_Amount_Orig_curr.invalid"),"PaidAmountOrigcurr","01"));
					}else{
						String ans =calcu.calculateClaimPayment(req,"Total");
						if(Double.parseDouble(ans)!=Double.parseDouble(req.getPaidAmountOrigcurr())){
							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"PaidAmountOrigcurr","01"));
							
						}else{
							req.setPaidAmountOrigcurr(ans);
						}
					}
					
					
				}			 

				if (val.isNull(req.getPaymentReference()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.payment_Reference.required"),"PaymentReference","01"));
				}
				if (val.isNull(req.getPaidClaimOs()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.paid_claim_os.required"),"PaidClaimOs","01"));
				} else {
					req.setPaidClaimOs((req.getPaidClaimOs()).replaceAll(",",""));
					if (val.numbervalid(req.getPaidClaimOs()).equalsIgnoreCase("INVALID")) 
					{
						list.add(new ErrorCheck(prop.getProperty("errors.paid_claim_os.invalid"),"PaidClaimOs","01"));
					} else if(claimImpl.BusinessValidation(req,7)){
						list.add(new ErrorCheck(prop.getProperty("errors.paid_claim_osGrRsrvAmt"),"PaidClaimOs","01"));
					}
				}
				if (val.isNull(req.getSurveyorfeeos()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.surveyor_fee_os.required"),"Surveyorfeeos","01"));
				}  else {
					req.setSurveyorfeeos((req.getSurveyorfeeos()).replaceAll(",",""));
					if (val.numbervalid(req.getSurveyorfeeos()).equalsIgnoreCase("INVALID")) 
					{
						list.add(new ErrorCheck(prop.getProperty("errors.surveyor_fee_os.invalid"),"Surveyorfeeos","01"));
					} else if(claimImpl.BusinessValidation(req,8)){
						list.add(new ErrorCheck(prop.getProperty("errors.surveyor_fee_osGrRsrvAmt"),"Surveyorfeeos","01"));
					}
				}
				if (val.isNull(req.getOtherproffeeos()).equalsIgnoreCase("")) {
					list.add(new ErrorCheck(prop.getProperty("errors.other_prof_fee_os.required"),"Otherproffeeos","01"));
				}  else {
					req.setOtherproffeeos((req.getOtherproffeeos()).replaceAll(",",""));
					if (val.numbervalid(req.getOtherproffeeos()).equalsIgnoreCase("INVALID")) 
					{
						list.add(new ErrorCheck(prop.getProperty("errors.other_prof_fee_os.invalid"),"Otherproffeeos","01"));
					} else if(claimImpl.BusinessValidation(req,9)){
						list.add(new ErrorCheck(prop.getProperty("errors.other_prof_fee_osGrRsrvAmt"),"Otherproffeeos","01"));
					}
				}
//				if("3".equalsIgnoreCase(req.getProductId())){
//				if (!val.isNull(req.getPaidClaimOs()).equalsIgnoreCase("") && !val.isNull(req.getSurveyorfeeos()).equalsIgnoreCase("") && !val.isNull(req.getOtherproffeeos()).equalsIgnoreCase("")) {
//					double totalVal = Double.parseDouble(req.getPaidClaimOs().replaceAll(",", "")) +Double.parseDouble(req.getSurveyorfeeos().replaceAll(",", "")) +Double.parseDouble(req.getOtherproffeeos().replaceAll(",", ""));
//					String annualVal =new DropDownControllor().getAnnualAgregateVal(req.getProposalNo(),req.getPolicyContractNo(),req.getLayerNo(),req.getBranchCode(),req.getDepartmentId());
//					if(totalVal>Double.parseDouble(annualVal)){
//						//list.add(new ErrorCheck(prop.getProperty("error.annual.agregate",new String[] {req.getDepartmentName() }));
//					}
//				}
//				}
				
				if (val.checkDate(req.getDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.date.required"),"Date","01"));
				}else if(val.belowCurrentDate(req.getDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.created_Date.invalid5"),"Date","01"));
				}
//				else if(Validation.ValidateTwo(req.getDateofLoss(),req.getDate()).equalsIgnoreCase("Invalid"))
//				{
//					list.add(new ErrorCheck(prop.getProperty("errors.created_Date.invalid1"),"","01"));
//				}
				else if(Validation.ValidateTwo(claimImpl.getClaimDate(req1,1),req.getDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.payDtGrELastResDt"),"ClaimDate","01"));
				}else if(Validation.ValidateTwo(claimImpl.getClaimDate(req1,4),req.getDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.payDtGrELastPayDt"),"ClaimDate","01"));
				}
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getDate()).equalsIgnoreCase("")){
					if(dropDowmImpl.Validatethree(req.getBranchCode(), req.getDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.Claim.pay")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
					}
				if(!"edit".equalsIgnoreCase(req.getPaymentFlag())){
					if(claimImpl.BusinessValidation(req,4)){
						list.add(new ErrorCheck(prop.getProperty("errors.PaidAmtGrOSAmt"),"PaymentFlag","01"));
					}
				}
			}	
				if("3".equalsIgnoreCase(req.getProductId())){
				req.setReinstPremiumOCOS((req.getReinstPremiumOCOS()).replaceAll(",",""));
				
				}

		} catch (NumberFormatException e) {
			list.add(new ErrorCheck(prop.getProperty("error.claim.payment.validation"),"","01"));
			e.printStackTrace();
		}
			
	return list;
	}




	public List<ErrorCheck> insertCliamDetailsMode12Vali(InsertCliamDetailsMode12Req req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		final Validation val = new Validation();	
		GetOpenPeriodRes openPeriodRes = new GetOpenPeriodRes();
		openPeriodRes = dropDownImple.getOpenPeriod(req.getBranchCode());
		try {
					
			InsertCliamDetailsMode8Req req1 = new InsertCliamDetailsMode8Req();
			req1.setPolicyContractNo(req.getPolicyContractNo());
			req1.setClaimNo(req.getClaimNo());
			
				if(claimImpl.BusinessValidation(req1, 6)){
					list.add(new ErrorCheck(prop.getProperty("error.claimClosed"),"claimClosedStatus","01"));
				}
				else if(claimImpl.BusinessValidation(req1,10)){
					list.add(new ErrorCheck(prop.getProperty("error.claimRepudiated"),"claimRepudiatedStatus","01"));
				}
				else{ 
					
//				if (val.isNull(req.getPaidAmountOrigcurr()).equalsIgnoreCase("")) {
//					list.add(new ErrorCheck(prop.getProperty("errors.paid_Amount_Orig_curr.required"),"PaidAmountOrigcurr","01"));
//				} else
//				{
//					req.setPaidAmountOrigcurr((req.getPaidAmountOrigcurr()).replaceAll(",",""));
//					if (val.numbervalid(req.getPaidAmountOrigcurr()).equalsIgnoreCase("INVALID")) 
//					{
//						list.add(new ErrorCheck(prop.getProperty("errors.paid_Amount_Orig_curr.invalid"),"PaidAmountOrigcurr","01"));
//					}else{
//						String ans =calcu.calculateClaimPayment(req,"Total");
//						if(Double.parseDouble(ans)!=Double.parseDouble(req.getPaidAmountOrigcurr())){
//							list.add(new ErrorCheck(prop.getProperty("error.calcul.mistake"),"PaidAmountOrigcurr","01"));
//							
//						}else{
//							req.setPaidAmountOrigcurr(ans);
//						}
//					}
//					
//					
//				}			 

//				if (val.isNull(req.getPaymentReference()).equalsIgnoreCase("")) {
//					list.add(new ErrorCheck(prop.getProperty("errors.payment_Reference.required"),"PaymentReference","01"));
//				}
//				if (val.isNull(req.getPaidClaimOs()).equalsIgnoreCase("")) {
//					list.add(new ErrorCheck(prop.getProperty("errors.paid_claim_os.required"),"PaidClaimOs","01"));
//				} else {
//					req.setPaidClaimOs((req.getPaidClaimOs()).replaceAll(",",""));
//					if (val.numbervalid(req.getPaidClaimOs()).equalsIgnoreCase("INVALID")) 
//					{
//						list.add(new ErrorCheck(prop.getProperty("errors.paid_claim_os.invalid"),"PaidClaimOs","01"));
//					} else if(claimImpl.BusinessValidation(req,7)){
//						list.add(new ErrorCheck(prop.getProperty("errors.paid_claim_osGrRsrvAmt"),"PaidClaimOs","01"));
//					}
//				}
//				if (val.isNull(req.getSurveyorfeeos()).equalsIgnoreCase("")) {
//					list.add(new ErrorCheck(prop.getProperty("errors.surveyor_fee_os.required"),"Surveyorfeeos","01"));
//				}  else {
//					req.setSurveyorfeeos((req.getSurveyorfeeos()).replaceAll(",",""));
//					if (val.numbervalid(req.getSurveyorfeeos()).equalsIgnoreCase("INVALID")) 
//					{
//						list.add(new ErrorCheck(prop.getProperty("errors.surveyor_fee_os.invalid"),"Surveyorfeeos","01"));
//					} else if(claimImpl.BusinessValidation(req,8)){
//						list.add(new ErrorCheck(prop.getProperty("errors.surveyor_fee_osGrRsrvAmt"),"Surveyorfeeos","01"));
//					}
//				}
//				if (val.isNull(req.getOtherproffeeos()).equalsIgnoreCase("")) {
//					list.add(new ErrorCheck(prop.getProperty("errors.other_prof_fee_os.required"),"Otherproffeeos","01"));
//				}  else {
//					req.setOtherproffeeos((req.getOtherproffeeos()).replaceAll(",",""));
//					if (val.numbervalid(req.getOtherproffeeos()).equalsIgnoreCase("INVALID")) 
//					{
//						list.add(new ErrorCheck(prop.getProperty("errors.other_prof_fee_os.invalid"),"Otherproffeeos","01"));
//					} else if(claimImpl.BusinessValidation(req,9)){
//						list.add(new ErrorCheck(prop.getProperty("errors.other_prof_fee_osGrRsrvAmt"),"Otherproffeeos","01"));
//					}
				}
//				if("3".equalsIgnoreCase(req.getProductId())){
//				if (!val.isNull(req.getPaidClaimOs()).equalsIgnoreCase("") && !val.isNull(req.getSurveyorfeeos()).equalsIgnoreCase("") && !val.isNull(req.getOtherproffeeos()).equalsIgnoreCase("")) {
//					double totalVal = Double.parseDouble(req.getPaidClaimOs().replaceAll(",", "")) +Double.parseDouble(req.getSurveyorfeeos().replaceAll(",", "")) +Double.parseDouble(req.getOtherproffeeos().replaceAll(",", ""));
//					String annualVal =new DropDownControllor().getAnnualAgregateVal(req.getProposalNo(),req.getPolicyContractNo(),req.getLayerNo(),req.getBranchCode(),req.getDepartmentId());
//					if(totalVal>Double.parseDouble(annualVal)){
//						//list.add(new ErrorCheck(prop.getProperty("error.annual.agregate",new String[] {req.getDepartmentName() }));
//					}
//				}
//				}
				
				if (val.checkDate(req.getDate()).equalsIgnoreCase("INVALID")) {
					list.add(new ErrorCheck(prop.getProperty("errors.date.required"),"Date","01"));
				}else if(val.belowCurrentDate(req.getDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.created_Date.invalid5"),"Date","01"));
				}
//				else if(Validation.ValidateTwo(req.getDateofLoss(),req.getDate()).equalsIgnoreCase("Invalid"))
//				{
//					list.add(new ErrorCheck(prop.getProperty("errors.created_Date.invalid1"),"","01"));
//				}
				else if(Validation.ValidateTwo(claimImpl.getClaimDate(req1,1),req.getDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.payDtGrELastResDt"),"ClaimDate","01"));
				}else if(Validation.ValidateTwo(claimImpl.getClaimDate(req1,4),req.getDate()).equalsIgnoreCase("Invalid"))
				{
					list.add(new ErrorCheck(prop.getProperty("errors.payDtGrELastPayDt"),"ClaimDate","01"));
				}
				if(!val.isNull(openPeriodRes.getOpenPeriodDate()).equalsIgnoreCase("") && !val.isNull(req.getDate()).equalsIgnoreCase("")){
					if(dropDowmImpl.Validatethree(req.getBranchCode(), req.getDate())==0){
						list.add(new ErrorCheck(prop.getProperty("errors.open.period.date.Claim.pay")+openPeriodRes.getOpenPeriodDate(),"OpenPeriodDate","01"));
					}
					}
//				if(!"edit".equalsIgnoreCase(req.getPaymentFlag())){
//					if(claimImpl.BusinessValidation(req,4)){
//						list.add(new ErrorCheck(prop.getProperty("errors.PaidAmtGrOSAmt"),"PaymentFlag","01"));
//					}
//				}
//			}	
//				if("3".equalsIgnoreCase(req.getProductId())){
//				req.setReinstPremiumOCOS((req.getReinstPremiumOCOS()).replaceAll(",",""));
//				
//				}

		} catch (NumberFormatException e) {
			list.add(new ErrorCheck(prop.getProperty("error.claim.payment.validation"),"","01"));
			e.printStackTrace();
		}
			
	return list;
	}
	public List<ErrorCheck> claimNoListVali(claimNoListReq req) {
		List<ErrorCheck> list = new ArrayList<ErrorCheck>();
		if(StringUtils.isBlank(req.getBranchCode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.BranchCode"),"BranchCode", "01"));
			}
		if(StringUtils.isBlank(req.getCedentClaimNo())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.CedentClaimNo"),"CedentClaimNo", "02"));
			}
		if(StringUtils.isBlank(req.getCedingCompanyCode())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.CedingCompanyCode"),"CedingCompanyCode", "03"));
			}
		if(StringUtils.isBlank(req.getDateofLoss())) {
			list.add(new ErrorCheck(prop.getProperty("errors.payment.DateofLoss"),"DateofLoss", "04"));
			}
		return list;
	}
	}

