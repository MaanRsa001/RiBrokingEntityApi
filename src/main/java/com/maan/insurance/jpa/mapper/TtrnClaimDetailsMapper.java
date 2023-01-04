package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.claim.TtrnClaimUpdation;
import com.maan.insurance.model.entity.TtrnClaimDetails;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode12Req;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode2Req;

@Component
public class TtrnClaimDetailsMapper extends AbstractEntityMapper<TtrnClaimDetails, InsertCliamDetailsMode2Req>{

	@Override
	public InsertCliamDetailsMode2Req fromEntity(TtrnClaimDetails input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TtrnClaimDetails toEntity(InsertCliamDetailsMode2Req req) throws ParseException {
		TtrnClaimDetails ttrnClaimDetails = null;
		if(req != null) {
			ttrnClaimDetails = new TtrnClaimDetails();
			ttrnClaimDetails.setStatusOfClaim(req.getStatusofClaim());
			ttrnClaimDetails.setDateOfLoss(formatDate(req.getDateofLoss()));
			ttrnClaimDetails.setReportDate(formatDate(req.getReportDate()));
			ttrnClaimDetails.setLossDetails(req.getLossDetails());
			ttrnClaimDetails.setCauseOfLoss(req.getCauseofLoss());
			ttrnClaimDetails.setCurrency(req.getCurrecny());
			ttrnClaimDetails.setLossEstimateOc(new BigDecimal(req.getLossEstimateOrigCurr()));
			ttrnClaimDetails.setLossEstimateOsOc(new BigDecimal(req.getLossEstimateOurShareOrigCurr()));
			ttrnClaimDetails.setExchangeRate(new BigDecimal(req.getExcRate()));
			ttrnClaimDetails.setLossEstimateOsDc(new BigDecimal(GetDesginationCountry(req.getLossEstimateOurShareOrigCurr(), req.getExcRate())));
			ttrnClaimDetails.setAdviceUw(req.getAdviceUW()==null?"":req.getAdviceUW());
			ttrnClaimDetails.setAdviceManagement(req.getAdviceMangement()==null?"":req.getAdviceMangement());
			ttrnClaimDetails.setRiRecovery(req.getRiRecovery());
			ttrnClaimDetails.setRiRecoveryAmountDc(BigDecimal.ZERO);
			ttrnClaimDetails.setRecoveryFrom(req.getRecoveryFrom());
			ttrnClaimDetails.setCreatedBy(req.getCreatedBy());
			ttrnClaimDetails.setCreatedDate(formatDate(req.getCreatedDate()));
			ttrnClaimDetails.setLocation(req.getLocation());
			ttrnClaimDetails.setRemarks(req.getRemarks());
			ttrnClaimDetails.setAdviceUwEmailid(req.getAdviceUwEmail()==null?"":req.getAdviceUwEmail());
			ttrnClaimDetails.setAdviceMgtEmailid(req.getAdviceManagementEmail()==null?"":req.getAdviceManagementEmail());
			ttrnClaimDetails.setInceptionDate(new Date());
			ttrnClaimDetails.setStatus("Y");
			ttrnClaimDetails.setRiskCode(req.getRiskCode()==null?"":req.getRiskCode());
			ttrnClaimDetails.setAccumulationCode(req.getAccumulationCode()==null?"":req.getAccumulationCode());
			ttrnClaimDetails.setEventCode(req.getEventCode()==null?"":req.getEventCode());
			ttrnClaimDetails.setInsuredName(req.getInsuredName());
			ttrnClaimDetails.setSubClass(new BigDecimal(req.getDepartmentId()));
			ttrnClaimDetails.setBranchCode(req.getBranchCode());
			ttrnClaimDetails.setLoginId(req.getLoginId());
			ttrnClaimDetails.setRecordFeesCreReserve(req.getRecordFees()==null?"":req.getRecordFees());
			ttrnClaimDetails.setSaf100Oc(req.getSurveyorAdjesterPerOC()==null?"0":req.getSurveyorAdjesterPerOC());
			ttrnClaimDetails.setSaf100Dc(StringUtils.isEmpty(req.getSurveyorAdjesterPerOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getSurveyorAdjesterPerOC(),req.getExcRate()));
			ttrnClaimDetails.setOthFee100Oc(req.getOtherProfessionalPerOc()==null?"0":req.getOtherProfessionalPerOc());
			ttrnClaimDetails.setOthFee100Dc(StringUtils.isEmpty(req.getOtherProfessionalPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getOtherProfessionalPerOc(),req.getExcRate()));
			ttrnClaimDetails.setCIbnr100Oc(req.getIbnrPerOc()==null?"0":req.getIbnrPerOc());
			ttrnClaimDetails.setCIbnr100Dc(StringUtils.isEmpty(req.getIbnrPerOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getIbnrPerOc(),req.getExcRate()));
			ttrnClaimDetails.setRecordIbnr(req.getRecordIbnr());
			ttrnClaimDetails.setCedentClaimNo(req.getCedentClaimNo());
			ttrnClaimDetails.setSafOsOc(req.getSurveyorAdjesterOurShareOC()==null?BigDecimal.ZERO:new BigDecimal(req.getSurveyorAdjesterOurShareOC()));
			ttrnClaimDetails.setOthFeeOsOc(req.getProfessionalOurShareOc()==null?BigDecimal.ZERO:new BigDecimal(req.getProfessionalOurShareOc()));
			ttrnClaimDetails.setCIbnrOsOc(req.getIbnrOurShareOc()==null?BigDecimal.ZERO:new BigDecimal(req.getIbnrOurShareOc()));
			ttrnClaimDetails.setSafOsDc(StringUtils.isEmpty(req.getSurveyorAdjesterOurShareOC())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getSurveyorAdjesterOurShareOC(),req.getExcRate()));
			ttrnClaimDetails.setOthFeeOsDc(StringUtils.isEmpty(req.getProfessionalOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getProfessionalOurShareOc(),req.getExcRate()));
			ttrnClaimDetails.setSafOsDc(StringUtils.isEmpty(req.getIbnrOurShareOc())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getIbnrOurShareOc(),req.getExcRate()));
			ttrnClaimDetails.setLossEstimateDc(StringUtils.isEmpty(req.getLossEstimateOrigCurr())|| StringUtils.isEmpty(req.getExcRate()) ? "0":GetDesginationCountry(req.getLossEstimateOrigCurr(),req.getExcRate()));
			ttrnClaimDetails.setReopenedDate(req.getReOpenDate()==null?null:formatDate(req.getReOpenDate()));
			ttrnClaimDetails.setGrosslossFguOc(req.getGrossLossFGU()==null?BigDecimal.ZERO:new BigDecimal(req.getGrossLossFGU()));
			 if("2".equalsIgnoreCase(req.getProductId())){
				 ttrnClaimDetails.setClaimClass(req.getClaimdepartId());
			 }else if("1".equalsIgnoreCase(req.getProductId())){
				 ttrnClaimDetails.setClaimClass(req.getDepartmentId());
			 }else if("3".equalsIgnoreCase(req.getProductId())){
				 ttrnClaimDetails.setClaimClass(req.getDepartmentClass()); //
			 }
			
			ttrnClaimDetails.setClaimSubclass(req.getSubProfitId()==null?"D":req.getSubProfitId());
			ttrnClaimDetails.setResPosDate(formatDate(req.getReservePositionDate()));
			ttrnClaimDetails.setCoverLimitDeptid(req.getDepartmentClass());
			ttrnClaimDetails.setProposalNo(new BigDecimal(req.getProposalNo()));
			ttrnClaimDetails.setProductId(new BigDecimal(req.getProductId()));
			ttrnClaimDetails.setRepudateDate(req.getReOpenDate()==null?null:formatDate(req.getReOpenDate()));
			ttrnClaimDetails.setContractNo(req.getPolicyContractNo().trim());
		//	ttrnClaimDetails.setClaimNo(new BigDecimal(req.getClaimNo()));
			ttrnClaimDetails.setLayerNo(StringUtils.isEmpty(req.getLayerNo())?BigDecimal.ZERO:new BigDecimal(req.getLayerNo()));
		}
		return ttrnClaimDetails;
	}
	


}
