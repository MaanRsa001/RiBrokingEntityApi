package com.maan.insurance.jpa.mapper;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.claim.TtrnClaimPaymentArchive;
import com.maan.insurance.model.entity.TtrnClaimPayment;

@Component
public class TtrnClaimPaymentArchiveMapper extends AbstractEntityMapper<TtrnClaimPaymentArchive, TtrnClaimPayment>{

	@Override
	public TtrnClaimPayment fromEntity(TtrnClaimPaymentArchive input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TtrnClaimPaymentArchive toEntity(TtrnClaimPayment input) throws ParseException {
		TtrnClaimPaymentArchive ttrnClaimPaymentArchive = null;
		
		if(input != null) {
			ttrnClaimPaymentArchive = new TtrnClaimPaymentArchive();
			ttrnClaimPaymentArchive.setSlNo(input.getSlNo());
			ttrnClaimPaymentArchive.setContractNo(input.getContractNo());
			ttrnClaimPaymentArchive.setLayerNo(input.getLayerNo());
			ttrnClaimPaymentArchive.setPaymentRequestNo(input.getPaymentRequestNo());
			ttrnClaimPaymentArchive.setPaidAmountOc(input.getPaidAmountOc());
			ttrnClaimPaymentArchive.setPaidAmountDc(input.getPaidAmountDc());
			ttrnClaimPaymentArchive.setLossEstimateRevisedOc(input.getLossEstimateRevisedOc());
			ttrnClaimPaymentArchive.setLossEstimateRevisedDc(input.getLossEstimateRevisedDc());
			ttrnClaimPaymentArchive.setClaimNoteRecomm(input.getClaimNoteRecomm());
			ttrnClaimPaymentArchive.setPaymentReference(input.getPaymentReference());
			ttrnClaimPaymentArchive.setAdviceTreasury(input.getAdviceTreasury());
			ttrnClaimPaymentArchive.setInceptionDate(input.getInceptionDate());
			ttrnClaimPaymentArchive.setExpiryDate(input.getExpiryDate());
			ttrnClaimPaymentArchive.setRemarks(input.getRemarks());
			ttrnClaimPaymentArchive.setStatus(input.getStatus());
			ttrnClaimPaymentArchive.setClaimPaymentNo(formatLong(input.getClaimPaymentNo().toString()));
			ttrnClaimPaymentArchive.setAdviceTreasuryEmailId(input.getAdviceTreasuryEmailid());
			ttrnClaimPaymentArchive.setAllocatedTillDate(input.getAllocatedTillDate());
			ttrnClaimPaymentArchive.setAccClaim(input.getAccClaim());
			ttrnClaimPaymentArchive.setCheckyn(input.getCheckyn());
			ttrnClaimPaymentArchive.setReserveId(input.getReserveId());
			ttrnClaimPaymentArchive.setSettlementStatus(input.getSettlementStatus());
			ttrnClaimPaymentArchive.setReinstatementType(input.getReinstatementType());
			ttrnClaimPaymentArchive.setReinspremiumOurshareOc(input.getReinspremiumOurshareOc());
			ttrnClaimPaymentArchive.setReinspremiumOurshareDc(input.getReinspremiumOurshareDc());
			ttrnClaimPaymentArchive.setNetclaimamtOurshareOc(input.getNetclaimamtOurshareOc());
			ttrnClaimPaymentArchive.setNetclaimamtOurshareDc(input.getNetclaimamtOurshareDc());
			ttrnClaimPaymentArchive.setExchangeRate(input.getExchangeRate());
			ttrnClaimPaymentArchive.setInsuredName(input.getInsuredName());
			ttrnClaimPaymentArchive.setCashLossSettledTilldate(input.getCashLossSettledTilldate());
			ttrnClaimPaymentArchive.setBranchCode(input.getBranchCode());
			ttrnClaimPaymentArchive.setLoginId(input.getLoginId());
			ttrnClaimPaymentArchive.setPaidClaimOsOc(input.getPaidClaimOsOc());
			ttrnClaimPaymentArchive.setPaidClaimOsDc(input.getPaidClaimOsDc());
			ttrnClaimPaymentArchive.setSafOsOc(input.getSafOsOc());
			ttrnClaimPaymentArchive.setSafOsDc(input.getSafOsDc());
			ttrnClaimPaymentArchive.setOthFeeOsOc(input.getOthFeeOsOc());
			ttrnClaimPaymentArchive.setOthFeeOsDc(input.getOthFeeOsDc());
			ttrnClaimPaymentArchive.setSysDate(null);
			
		}
		return ttrnClaimPaymentArchive;
	}

	

}
