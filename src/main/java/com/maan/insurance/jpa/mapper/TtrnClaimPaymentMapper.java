package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.model.entity.TtrnClaimPayment;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;

@Component
public class TtrnClaimPaymentMapper extends AbstractEntityMapper<TtrnClaimPayment, InsertCliamDetailsMode3Req>{

	@Override
	public InsertCliamDetailsMode3Req fromEntity(TtrnClaimPayment input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TtrnClaimPayment toEntity(InsertCliamDetailsMode3Req input) throws ParseException {
		TtrnClaimPayment ttrnClaimPayment = null;
		
		if(input != null) {
			ttrnClaimPayment = new TtrnClaimPayment();
			ttrnClaimPayment.setSlNo(null);
			ttrnClaimPayment.setLayerNo(new BigDecimal(format(StringUtils.isEmpty(input.getLayerNo())?"0":input.getLayerNo())));
			ttrnClaimPayment.setPaymentRequestNo(input.getPaymentRequestNo());
			ttrnClaimPayment.setPaidAmountOc(formatBigDecimal(input.getPaidAmountOrigcurr()));
			ttrnClaimPayment.setPaidAmountDc(formatBigDecimal(GetDesginationCountry(input.getPaidAmountOrigcurr(),input.getExcRate())));
			ttrnClaimPayment.setLossEstimateRevisedOc(formatBigDecimal(input.getLossEstimateRevisedOrigCurr()));
			ttrnClaimPayment.setLossEstimateRevisedDc(formatBigDecimal(GetDesginationCountry(input.getLossEstimateRevisedOrigCurr(),input.getExcRate())));
			ttrnClaimPayment.setClaimNoteRecomm(input.getClaimNoteRecommendations()==null?"":input.getClaimNoteRecommendations());
			ttrnClaimPayment.setPaymentReference(input.getPaymentReference());
			
			if(StringUtils.isNotBlank(input.getAdviceTreasury()))
				ttrnClaimPayment.setAdviceTreasury(input.getAdviceTreasury());
			else
				ttrnClaimPayment.setAdviceTreasury("");
			
			ttrnClaimPayment.setInceptionDate(formatDate(input.getDate()));
			ttrnClaimPayment.setClaimNo(new BigDecimal(format(input.getClaimNo())));
			ttrnClaimPayment.setContractNo(input.getPolicyContractNo());
			ttrnClaimPayment.setStatus("Y");
			ttrnClaimPayment.setClaimPaymentNo(new BigDecimal(format(input.getClaimPaymentNo())));
			ttrnClaimPayment.setRemarks(input.getRemarks());
			ttrnClaimPayment.setAdviceTreasuryEmailid(input.getAdviceTreasuryEmail()==null?"":input.getAdviceTreasuryEmail());
			ttrnClaimPayment.setReserveId(new BigDecimal(1));
			ttrnClaimPayment.setSettlementStatus("Pending");
			
			if("3".equalsIgnoreCase(input.getProductId())){
				ttrnClaimPayment.setReinstatementType(input.getReinstType());
				ttrnClaimPayment.setReinspremiumOurshareOc(formatBigDecimal(input.getReinstPremiumOCOS()));
				ttrnClaimPayment.setReinspremiumOurshareDc(formatBigDecimal(GetDesginationCountry(input.getReinstPremiumOCOS(),input.getExcRate())));
				
			}else{
				ttrnClaimPayment.setReinstatementType("");
				ttrnClaimPayment.setReinspremiumOurshareOc(formatBigDecimal("0"));
				ttrnClaimPayment.setReinspremiumOurshareDc(formatBigDecimal("0"));
			
			}
			
			ttrnClaimPayment.setPaidClaimOsOc(formatBigDecimal(input.getPaidClaimOs()));
			ttrnClaimPayment.setPaidClaimOsDc(formatBigDecimal(GetDesginationCountry(input.getPaidClaimOs(),input.getExcRate())));
			ttrnClaimPayment.setSafOsOc(formatBigDecimal(input.getSurveyorfeeos()));
			ttrnClaimPayment.setSafOsDc(formatBigDecimal(GetDesginationCountry(input.getSurveyorfeeos(),input.getExcRate())));
			ttrnClaimPayment.setOthFeeOsOc(formatBigDecimal(input.getOtherproffeeos()));
			ttrnClaimPayment.setOthFeeOsDc(formatBigDecimal(GetDesginationCountry(input.getOtherproffeeos(),input.getExcRate())));
			ttrnClaimPayment.setBranchCode(input.getBranchCode());
			ttrnClaimPayment.setLoginId(input.getLoginId());
			ttrnClaimPayment.setExchangeRate(formatBigDecimal(input.getExcRate()));
			ttrnClaimPayment.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnClaimPayment.setPaymentType(input.getPaymentType());
			
		}
		return ttrnClaimPayment;
	}

	

}
