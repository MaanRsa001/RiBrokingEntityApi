package com.maan.insurance.jpa.mapper;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.claim.TtrnClaimUpdation;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode12Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode8Req;

@Component
public class TtrnClaimUpdationMapper extends AbstractEntityMapper<TtrnClaimUpdation, InsertCliamDetailsMode12Req>{

	@Override
	public InsertCliamDetailsMode12Req fromEntity(TtrnClaimUpdation input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TtrnClaimUpdation toEntity(InsertCliamDetailsMode12Req req) throws ParseException {
		TtrnClaimUpdation ttrnClaimUpdation = null;
		if(req != null) {
			ttrnClaimUpdation = new TtrnClaimUpdation();
			ttrnClaimUpdation.setSlNo(null); 
			ttrnClaimUpdation.setLayerNo(0); 
			ttrnClaimUpdation.setLossEstimateRevisedOc(formatBigDecimal("0.0")); 
			ttrnClaimUpdation.setLossEstimateRevisedDc(formatBigDecimal(GetDesginationCountry("0",req.getExcRate()))); 
			
			if(req.getPaymentType().equals("Final"))
				ttrnClaimUpdation.setUpdateReference("Inserted at the time of Claim Closure");
			else
				ttrnClaimUpdation.setUpdateReference(req.getUpdateReference()==null?"":req.getUpdateReference());
			
			ttrnClaimUpdation.setInceptionDate(formatDate(req.getDate()));  
			ttrnClaimUpdation.setClaimNo(req.getClaimNo()); 
			ttrnClaimUpdation.setContractNo(req.getPolicyContractNo()); 
			ttrnClaimUpdation.setStatus("Y"); 
			ttrnClaimUpdation.setRemarks(req.getRemarks());
			ttrnClaimUpdation.setBranchCode(req.getBranchCode());
			ttrnClaimUpdation.setLoginId(req.getLoginId());
			ttrnClaimUpdation.setSaf100Oc(req.getUpdatesurveyorAdjesterPerOC()==null?"0":req.getUpdatesurveyorAdjesterPerOC());
			ttrnClaimUpdation.setSaf100Dc(StringUtils.isEmpty(req.getUpdatesurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterPerOC(),req.getExcRate()));
			ttrnClaimUpdation.setOthFee100Oc(req.getUpdateotherProfessionalPerOc()==null?"0":req.getUpdateotherProfessionalPerOc());
			ttrnClaimUpdation.setOthFee100Dc(StringUtils.isEmpty(req.getUpdateotherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateotherProfessionalPerOc(),req.getExcRate()));
			ttrnClaimUpdation.setCIbnr100Oc(req.getUpdateibnrPerOc()==null?"0":req.getUpdateibnrPerOc());
			ttrnClaimUpdation.setCIbnr100Dc(StringUtils.isEmpty(req.getUpdateibnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrPerOc(),req.getExcRate()));
			ttrnClaimUpdation.setSafOsOc(formatBigDecimal(req.getUpdatesurveyorAdjesterOurShareOC()==null?"0":req.getUpdatesurveyorAdjesterOurShareOC()));
			ttrnClaimUpdation.setOthFeeOsOc(formatBigDecimal(req.getUpdateprofessionalOurShareOc()==null?"0":req.getUpdateprofessionalOurShareOc()));
			ttrnClaimUpdation.setCIbnrOsOc(formatBigDecimal(req.getUpdateibnrOurShareOc()==null?"0":req.getUpdateibnrOurShareOc()));
			ttrnClaimUpdation.setSafOsDc(StringUtils.isEmpty(req.getUpdatesurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterOurShareOC(),req.getExcRate()));
			ttrnClaimUpdation.setOthFeeOsDc(StringUtils.isEmpty(req.getUpdateprofessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateprofessionalOurShareOc(),req.getExcRate()));
			ttrnClaimUpdation.setCIbnrOsDc(StringUtils.isEmpty(req.getUpdateibnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrOurShareOc(),req.getExcRate()));
			ttrnClaimUpdation.setLossEstimate100Oc(req.getLossEstimateOrigCurr());
			ttrnClaimUpdation.setLossEstimate100Dc(StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate()));
			ttrnClaimUpdation.setReserveFees(req.getUpdaterecordFees()==null?"No":req.getUpdaterecordFees());
			ttrnClaimUpdation.setReserveIbnr(req.getUpdaterecordIbnr()==null?"No":req.getUpdaterecordIbnr());
			ttrnClaimUpdation.setExchangeRate(formatBigDecimal(req.getExcRate()));
			ttrnClaimUpdation.setTotResAmountOc(formatBigDecimal(req.getTotalReserveOSOC()==null?"0":req.getTotalReserveOSOC()));
			ttrnClaimUpdation.setTotResAmountDc(formatBigDecimal(StringUtils.isEmpty(req.getTotalReserveOSOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getTotalReserveOSOC(),req.getExcRate())));
			ttrnClaimUpdation.setResPosDate(formatDate(req.getDate()));
			ttrnClaimUpdation.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			
		}
		return ttrnClaimUpdation;
	}
	
	public TtrnClaimUpdation insertCliamDetailsMode8(InsertCliamDetailsMode8Req req) throws ParseException {
		TtrnClaimUpdation ttrnClaimUpdation = null;
		if(req != null) {
			ttrnClaimUpdation = new TtrnClaimUpdation();
			ttrnClaimUpdation.setSlNo(null); 
			ttrnClaimUpdation.setLayerNo(format(StringUtils.isEmpty(req.getLayerNo())?"0":req.getLayerNo())); 
			ttrnClaimUpdation.setLossEstimateRevisedOc(formatBigDecimal(req.getUpdateRivisedoriginalCur())); 
			ttrnClaimUpdation.setLossEstimateRevisedDc(formatBigDecimal(GetDesginationCountry(req.getUpdateRivisedoriginalCur(),req.getExcRate()))); 
			ttrnClaimUpdation.setUpdateReference(req.getUpdateReference());
			
			ttrnClaimUpdation.setInceptionDate(formatDate(req.getCliamupdateDate()));  
			ttrnClaimUpdation.setClaimNo(req.getClaimNo()); 
			ttrnClaimUpdation.setContractNo(req.getPolicyContractNo()); 
			ttrnClaimUpdation.setStatus("Y"); 
			ttrnClaimUpdation.setRemarks(req.getRemarks());
			ttrnClaimUpdation.setBranchCode(req.getBranchCode());
			ttrnClaimUpdation.setLoginId(req.getLoginId());
			
			ttrnClaimUpdation.setSaf100Oc(req.getUpdatesurveyorAdjesterPerOC()==null?"0":req.getUpdatesurveyorAdjesterPerOC());
			ttrnClaimUpdation.setSaf100Dc(StringUtils.isEmpty(req.getUpdatesurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterPerOC(),req.getExcRate()));
			ttrnClaimUpdation.setOthFee100Oc(req.getUpdateotherProfessionalPerOc()==null?"0":req.getUpdateotherProfessionalPerOc());
			ttrnClaimUpdation.setOthFee100Dc(StringUtils.isEmpty(req.getUpdateotherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateotherProfessionalPerOc(),req.getExcRate()));
			ttrnClaimUpdation.setCIbnr100Oc(req.getUpdateibnrPerOc()==null?"0":req.getUpdateibnrPerOc());
			ttrnClaimUpdation.setCIbnr100Dc(StringUtils.isEmpty(req.getUpdateibnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrPerOc(),req.getExcRate()));
			ttrnClaimUpdation.setSafOsOc(formatBigDecimal(req.getUpdatesurveyorAdjesterOurShareOC()==null?"0":req.getUpdatesurveyorAdjesterOurShareOC()));
			ttrnClaimUpdation.setOthFeeOsOc(formatBigDecimal(req.getUpdateprofessionalOurShareOc()==null?"0":req.getUpdateprofessionalOurShareOc()));
			ttrnClaimUpdation.setCIbnrOsOc(formatBigDecimal(req.getUpdateibnrOurShareOc()==null?"0":req.getUpdateibnrOurShareOc()));
			ttrnClaimUpdation.setSafOsDc(StringUtils.isEmpty(req.getUpdatesurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterOurShareOC(),req.getExcRate()));
			ttrnClaimUpdation.setOthFeeOsDc(StringUtils.isEmpty(req.getUpdateprofessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateprofessionalOurShareOc(),req.getExcRate()));
			ttrnClaimUpdation.setCIbnrOsDc(StringUtils.isEmpty(req.getUpdateibnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrOurShareOc(),req.getExcRate()));
			ttrnClaimUpdation.setLossEstimate100Oc(req.getLossEstimateOrigCurr());
			ttrnClaimUpdation.setLossEstimate100Dc(StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate()));
			ttrnClaimUpdation.setReserveFees(req.getUpdaterecordFees());
			ttrnClaimUpdation.setReserveIbnr(req.getUpdaterecordIbnr());
			ttrnClaimUpdation.setExchangeRate(formatBigDecimal(req.getExcRate()));
			ttrnClaimUpdation.setTotResAmountOc(formatBigDecimal(req.getTotalReserveOSOC()));
			ttrnClaimUpdation.setTotResAmountDc(formatBigDecimal(StringUtils.isEmpty(req.getTotalReserveOSOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getTotalReserveOSOC(),req.getExcRate())));
			ttrnClaimUpdation.setResPosDate(formatDate(req.getReservePositionDate()));
			ttrnClaimUpdation.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			
		}
		return ttrnClaimUpdation;
	}
	
	
	public TtrnClaimUpdation insertCliamDetailsMode12(InsertCliamDetailsMode8Req req) throws ParseException {
		TtrnClaimUpdation ttrnClaimUpdation = null;
		if(req != null) {
			ttrnClaimUpdation = new TtrnClaimUpdation();
			ttrnClaimUpdation.setSlNo(null); 
			ttrnClaimUpdation.setLayerNo(0); 
			ttrnClaimUpdation.setLossEstimateRevisedOc(formatBigDecimal("0.0")); 
			ttrnClaimUpdation.setLossEstimateRevisedDc(formatBigDecimal(GetDesginationCountry("0",req.getExcRate()))); 
			ttrnClaimUpdation.setUpdateReference(req.getUpdateReference());
			
			ttrnClaimUpdation.setInceptionDate(formatDate(req.getCliamupdateDate()));  
			ttrnClaimUpdation.setClaimNo(req.getClaimNo()); 
			ttrnClaimUpdation.setContractNo(req.getPolicyContractNo()); 
			ttrnClaimUpdation.setStatus("Y"); 
			ttrnClaimUpdation.setRemarks(req.getRemarks());
			ttrnClaimUpdation.setBranchCode(req.getBranchCode());
			ttrnClaimUpdation.setLoginId(req.getLoginId());
			ttrnClaimUpdation.setSaf100Oc(req.getUpdatesurveyorAdjesterPerOC()==null?"0":req.getUpdatesurveyorAdjesterPerOC());
			ttrnClaimUpdation.setSaf100Dc(StringUtils.isEmpty(req.getUpdatesurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterPerOC(),req.getExcRate()));
			ttrnClaimUpdation.setOthFee100Oc(req.getUpdateotherProfessionalPerOc()==null?"0":req.getUpdateotherProfessionalPerOc());
			ttrnClaimUpdation.setOthFee100Dc(StringUtils.isEmpty(req.getUpdateotherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateotherProfessionalPerOc(),req.getExcRate()));
			ttrnClaimUpdation.setCIbnr100Oc(req.getUpdateibnrPerOc()==null?"0":req.getUpdateibnrPerOc());
			ttrnClaimUpdation.setCIbnr100Dc(StringUtils.isEmpty(req.getUpdateibnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrPerOc(),req.getExcRate()));
			ttrnClaimUpdation.setSafOsOc(formatBigDecimal(req.getUpdatesurveyorAdjesterOurShareOC()==null?"0":req.getUpdatesurveyorAdjesterOurShareOC()));
			ttrnClaimUpdation.setOthFeeOsOc(formatBigDecimal(req.getUpdateprofessionalOurShareOc()==null?"0":req.getUpdateprofessionalOurShareOc()));
			ttrnClaimUpdation.setCIbnrOsOc(formatBigDecimal(req.getUpdateibnrOurShareOc()==null?"0":req.getUpdateibnrOurShareOc()));
			ttrnClaimUpdation.setSafOsDc(StringUtils.isEmpty(req.getUpdatesurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdatesurveyorAdjesterOurShareOC(),req.getExcRate()));
			ttrnClaimUpdation.setOthFeeOsDc(StringUtils.isEmpty(req.getUpdateprofessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateprofessionalOurShareOc(),req.getExcRate()));
			ttrnClaimUpdation.setCIbnrOsDc(StringUtils.isEmpty(req.getUpdateibnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getUpdateibnrOurShareOc(),req.getExcRate()));
			ttrnClaimUpdation.setLossEstimate100Oc(req.getLossEstimateOrigCurr());
			ttrnClaimUpdation.setLossEstimate100Dc(StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate()));
			ttrnClaimUpdation.setReserveFees(req.getUpdaterecordFees()==null?"No":req.getUpdaterecordFees());
			ttrnClaimUpdation.setReserveIbnr(req.getUpdaterecordIbnr()==null?"No":req.getUpdaterecordIbnr());
			ttrnClaimUpdation.setExchangeRate(formatBigDecimal(req.getExcRate()));
			ttrnClaimUpdation.setTotResAmountOc(formatBigDecimal(req.getTotalReserveOSOC()==null?"0":req.getTotalReserveOSOC()));
			ttrnClaimUpdation.setTotResAmountDc(formatBigDecimal(StringUtils.isEmpty(req.getTotalReserveOSOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getTotalReserveOSOC(),req.getExcRate())));
			ttrnClaimUpdation.setResPosDate(formatDate(req.getReservePositionDate()));
			ttrnClaimUpdation.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			
		}
		return ttrnClaimUpdation;
	}
}
