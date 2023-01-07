package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.tika.utils.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.model.entity.RskPremiumDetails;
import com.maan.insurance.model.entity.RskPremiumDetailsTemp;

@Component
public class RskPremiumDetailsMapper extends AbstractEntityMapper<RskPremiumDetails, RskPremiumDetailsTemp>{

	@Override
	public RskPremiumDetailsTemp fromEntity(RskPremiumDetails input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RskPremiumDetails toEntity(RskPremiumDetailsTemp input) throws ParseException {
		RskPremiumDetails rskPremiumDetails = null;
		if(input != null) {
			rskPremiumDetails = new RskPremiumDetails();
			rskPremiumDetails.setContractNo(input.getContractNo());
			rskPremiumDetails.setRequestNo(input.getRequestNo());
			rskPremiumDetails.setTransactionMonthYear(input.getTransactionMonthYear());
			rskPremiumDetails.setAccountPeriodQtr(input.getAccountPeriodQtr());
			rskPremiumDetails.setAccountPeriodYear(input.getAccountPeriodYear());
			rskPremiumDetails.setCurrencyId(input.getCurrencyId());
			rskPremiumDetails.setExchangeRate(input.getExchangeRate());
			rskPremiumDetails.setBrokerage(input.getBrokerage());
			rskPremiumDetails.setBrokerageAmtOc(input.getBrokerageAmtOc());
			rskPremiumDetails.setTax(input.getTax());
			rskPremiumDetails.setTaxAmtOc(input.getTaxAmtOc());
			rskPremiumDetails.setEntryDateTime(input.getEntryDateTime());
			rskPremiumDetails.setCommission(input.getCommission());
			rskPremiumDetails.setMDpremiumOc(input.getMDpremiumOc());
			rskPremiumDetails.setAdjustmentPremiumOc(input.getAdjustmentPremiumOc());
			rskPremiumDetails.setRecPremiumOc(input.getRecPremiumOc());
			rskPremiumDetails.setStatus(input.getStatus());
			rskPremiumDetails.setNetdueOc(input.getNetdueOc());
			rskPremiumDetails.setLayerNo(input.getLayerNo());
			rskPremiumDetails.setEnteringMode(input.getEnteringMode());
			rskPremiumDetails.setReceiptNo(input.getReceiptNo());
			rskPremiumDetails.setInstalmentNumber(input.getInstalmentNumber());
			rskPremiumDetails.setSettlementStatus(input.getSettlementStatus());
			rskPremiumDetails.setOtherCostOc(input.getOtherCostOc());
			rskPremiumDetails.setBrokerageAmtDc(input.getBrokerageAmtDc());
			rskPremiumDetails.setTaxAmtDc(input.getTaxAmtDc());
			rskPremiumDetails.setMDpremiumDc(input.getMDpremiumDc());
			rskPremiumDetails.setAdjustmentPremiumDc(input.getAdjustmentPremiumDc());
			rskPremiumDetails.setRecPremiumDc(input.getRecPremiumDc());
			rskPremiumDetails.setNetdueDc(input.getNetdueDc());
			rskPremiumDetails.setOtherCostDc(input.getOtherCostDc());
			rskPremiumDetails.setCedantReference(input.getCedantReference());
			rskPremiumDetails.setRemarks(input.getRemarks());
			rskPremiumDetails.setTotalCrOc(input.getTotalCrOc());
			rskPremiumDetails.setTotalCrDc(input.getTotalCrDc());
			rskPremiumDetails.setTotalDrOc(input.getTotalDrOc());
			rskPremiumDetails.setTotalDrDc(input.getTotalDrDc());
			rskPremiumDetails.setEntryDate(input.getEntryDate());
			rskPremiumDetails.setWithHoldingTaxOc(input.getWithHoldingTaxOc());
			rskPremiumDetails.setWithHoldingTaxDc(input.getWithHoldingTaxDc());
			rskPremiumDetails.setRiCession(input.getRiCession());
			rskPremiumDetails.setLoginId(input.getLoginId());
			rskPremiumDetails.setBranchCode(input.getBranchCode());
			rskPremiumDetails.setSubClass(input.getSubClass());
			rskPremiumDetails.setTdsOc(input.getTdsOc());
			rskPremiumDetails.setTdsDc(input.getTdsDc());
			rskPremiumDetails.setVatPremiumOc(input.getVatPremiumDc());
			rskPremiumDetails.setVatPremiumDc(input.getVatPremiumDc());
			rskPremiumDetails.setBonusOc(input.getBonusOc());
			rskPremiumDetails.setBonusDc(input.getBonusDc());
			rskPremiumDetails.setGnpiEndtNo(input.getGnpiEndtNo());
			rskPremiumDetails.setPremiumSubclass(input.getPremiumSubclass());
			rskPremiumDetails.setPremiumClass(input.getPremiumClass());
			rskPremiumDetails.setStatementDate(input.getStatementDate());
			rskPremiumDetails.setProposalNo(input.getProposalNo());
			rskPremiumDetails.setProductId(input.getProductId());
			rskPremiumDetails.setReverselStatus(input.getReverselStatus());
			rskPremiumDetails.setTransType(input.getTransType());
			rskPremiumDetails.setBrokerageVatOc(input.getBrokerageVatOc());
			rskPremiumDetails.setBrokerageVatDc(input.getBrokerageVatDc());
			rskPremiumDetails.setDocumentType(input.getDocumentType());
			rskPremiumDetails.setM1Oc(input.getM1Oc());
			rskPremiumDetails.setM2Oc(input.getM2Oc());
			rskPremiumDetails.setM3Oc(input.getM3Oc());
			rskPremiumDetails.setAmendId(formatBigDecimal(input.getAmendId()==null? "" :input.getAmendId().toString()));
		}
		return rskPremiumDetails;
	}
	public RskPremiumDetails toProEntity(RskPremiumDetailsTemp input) {
		RskPremiumDetails rskPremiumDetails = null;
		try {
			if(input != null) {
				rskPremiumDetails = new RskPremiumDetails();
				rskPremiumDetails.setContractNo(input.getContractNo());
				rskPremiumDetails.setRequestNo(input.getRequestNo());
				rskPremiumDetails.setTransactionMonthYear(input.getTransactionMonthYear());
				rskPremiumDetails.setAccountPeriodQtr(input.getAccountPeriodQtr());
				rskPremiumDetails.setAccountPeriodYear(input.getAccountPeriodYear());
				rskPremiumDetails.setCurrencyId(input.getCurrencyId());
				rskPremiumDetails.setExchangeRate(input.getExchangeRate());
				rskPremiumDetails.setBrokerage(input.getBrokerage());
				rskPremiumDetails.setBrokerageAmtOc(input.getBrokerageAmtOc());
				rskPremiumDetails.setTax(input.getTax());
				rskPremiumDetails.setTaxAmtOc(input.getTaxAmtOc());
				rskPremiumDetails.setEntryDateTime(input.getEntryDateTime());
				rskPremiumDetails.setLayerNo(input.getLayerNo());
				rskPremiumDetails.setPremiumQuotashareOc(input.getPremiumQuotashareOc());
				rskPremiumDetails.setCommissionQuotashareOc(input.getCommissionQuotashareOc());
				rskPremiumDetails.setPremiumSurplusOc(input.getPremiumSurplusOc());
				rskPremiumDetails.setCommissionSurplusOc(input.getCommissionSurplusOc());
				rskPremiumDetails.setPremiumPortfolioinOc(input.getPremiumPortfolioinOc());
				rskPremiumDetails.setClaimPortfolioinOc(input.getClaimPortfolioinOc());
				rskPremiumDetails.setPremiumPortfoliooutOc(input.getPremiumPortfoliooutOc());
				rskPremiumDetails.setLossReserveReleasedOc(input.getLossReserveReleasedOc());
				rskPremiumDetails.setPremiumreserveQuotashareOc(input.getPremiumreserveQuotashareOc());
				rskPremiumDetails.setCashLossCreditOc(input.getCashLossCreditOc());
				rskPremiumDetails.setLossReserveretainedOc(input.getLossReserveretainedOc());
				rskPremiumDetails.setProfitCommissionOc(input.getProfitCommissionOc());
				rskPremiumDetails.setCashLosspaidOc(input.getCashLosspaidOc());
				
				rskPremiumDetails.setStatus(input.getStatus());
				rskPremiumDetails.setEnteringMode(input.getEnteringMode());
				rskPremiumDetails.setReceiptNo(input.getReceiptNo());
				rskPremiumDetails.setInstalmentNumber(input.getInstalmentNumber());
				rskPremiumDetails.setClaimsPaidOc(input.getClaimsPaidOc());
				rskPremiumDetails.setSettlementStatus(input.getSettlementStatus());
				rskPremiumDetails.setXlCostOc(input.getXlCostOc());
				rskPremiumDetails.setClaimPortfolioOutOc(input.getClaimPortfolioOutOc());
				rskPremiumDetails.setPremiumReserveRealsedOc(input.getPremiumReserveRealsedOc());
				rskPremiumDetails.setNetdueOc(input.getNetdueOc());
				rskPremiumDetails.setOtherCostOc(input.getOtherCostOc());
				rskPremiumDetails.setBrokerageAmtDc(input.getBrokerageAmtDc());
				rskPremiumDetails.setTaxAmtDc(input.getTaxAmtDc());
				rskPremiumDetails.setMDpremiumDc(input.getMDpremiumDc());
				rskPremiumDetails.setAdjustmentPremiumDc(input.getAdjustmentPremiumDc());
				rskPremiumDetails.setRecPremiumDc(input.getRecPremiumDc());
				rskPremiumDetails.setPremiumQuotashareDc(input.getPremiumQuotashareDc());
				rskPremiumDetails.setCommissionQuotashareDc(input.getCommissionQuotashareDc());
				rskPremiumDetails.setPremiumSurplusDc(input.getPremiumSurplusDc());
				
				rskPremiumDetails.setCommissionSurplusDc(input.getCommissionSurplusDc());
				rskPremiumDetails.setPremiumPortfolioinDc(input.getPremiumPortfolioinDc());
				rskPremiumDetails.setClaimPortfolioinDc(input.getClaimPortfolioinDc());
				rskPremiumDetails.setPremiumPortfoliooutDc(input.getPremiumPortfoliooutDc());
				rskPremiumDetails.setLossReserveReleasedDc(input.getLossReserveReleasedDc());
				rskPremiumDetails.setPremiumreserveQuotashareDc(input.getPremiumreserveQuotashareDc());
				rskPremiumDetails.setCashLossCreditDc(input.getCashLossCreditDc());
				rskPremiumDetails.setLossReserveretainedDc(input.getLossReserveretainedDc());
				rskPremiumDetails.setProfitCommissionDc(input.getProfitCommissionDc());
				rskPremiumDetails.setCashLosspaidDc(input.getCashLosspaidDc());
				
				rskPremiumDetails.setClaimsPaidDc(input.getClaimsPaidDc());
				rskPremiumDetails.setXlCostDc(input.getXlCostDc());
				rskPremiumDetails.setClaimPortfolioOutDc(input.getClaimPortfolioOutDc());
				rskPremiumDetails.setPremiumReserveRealsedDc(input.getPremiumReserveRealsedDc());
				rskPremiumDetails.setNetdueDc(input.getNetdueDc());
				rskPremiumDetails.setOtherCostDc(input.getOtherCostDc());
				rskPremiumDetails.setCommission(input.getCommission());
				rskPremiumDetails.setMDpremiumOc(input.getMDpremiumOc());
				rskPremiumDetails.setAdjustmentPremiumOc(input.getAdjustmentPremiumOc());
				rskPremiumDetails.setRecPremiumOc(input.getRecPremiumOc());
				rskPremiumDetails.setCedantReference(input.getCedantReference());
				rskPremiumDetails.setRemarks(input.getRemarks());
				rskPremiumDetails.setTotalCrOc(input.getTotalCrOc());
				rskPremiumDetails.setTotalCrDc(input.getTotalCrDc());
				rskPremiumDetails.setTotalDrOc(input.getTotalDrOc());
				rskPremiumDetails.setTotalDrDc(input.getTotalDrDc());
				rskPremiumDetails.setInterestOc(input.getInterestOc());
				rskPremiumDetails.setInterestDc(input.getInterestDc());
				rskPremiumDetails.setOsclaimLossupdateOc(input.getOsclaimLossupdateOc());
				rskPremiumDetails.setOsclaimLossupdateDc(input.getOsclaimLossupdateDc());
				
				rskPremiumDetails.setEntryDate(getCurrentDate());
				rskPremiumDetails.setOverriderAmtOc(input.getOverriderAmtOc());
				rskPremiumDetails.setOverriderAmtDc(input.getOverriderAmtDc());
				rskPremiumDetails.setOverrider(input.getOverrider());
				rskPremiumDetails.setWithHoldingTaxOc(input.getWithHoldingTaxOc());
				rskPremiumDetails.setWithHoldingTaxDc(input.getWithHoldingTaxDc());
				rskPremiumDetails.setRiCession(input.getRiCession());
				rskPremiumDetails.setLoginId(input.getLoginId());
				rskPremiumDetails.setBranchCode(input.getBranchCode());
				rskPremiumDetails.setSubClass(input.getSubClass());
				rskPremiumDetails.setTdsOc(input.getTdsOc());
				rskPremiumDetails.setTdsDc(input.getTdsDc());
				rskPremiumDetails.setVatPremiumOc(input.getVatPremiumDc());
				rskPremiumDetails.setVatPremiumDc(input.getVatPremiumDc());
				rskPremiumDetails.setScCommOc(input.getScCommOc());
				rskPremiumDetails.setScCommDc(input.getScCommDc());
				rskPremiumDetails.setPremiumClass(input.getPremiumClass());
				rskPremiumDetails.setPremiumSubclass(input.getPremiumSubclass());
				rskPremiumDetails.setAccountingPeriodDate(input.getAccountingPeriodDate());
				rskPremiumDetails.setStatementDate(input.getStatementDate());
				rskPremiumDetails.setOsbyn(input.getOsbyn());
				rskPremiumDetails.setLpcOc(input.getLpcOc());
				rskPremiumDetails.setLpcDc(input.getLpcDc());
				rskPremiumDetails.setSectionName(input.getSectionName());
				rskPremiumDetails.setProposalNo(input.getProposalNo());
				rskPremiumDetails.setProductId(input.getProductId());
				rskPremiumDetails.setTransType(input.getTransType());
				rskPremiumDetails.setBrokerageVatOc(input.getBrokerageVatOc());
				rskPremiumDetails.setBrokerageVatDc(input.getBrokerageVatDc());
				rskPremiumDetails.setDocumentType(input.getDocumentType());
				rskPremiumDetails.setM1Oc(input.getM1Oc());
				rskPremiumDetails.setM2Oc(input.getM2Oc());
				rskPremiumDetails.setM3Oc(input.getM3Oc());
				rskPremiumDetails.setAmendId(formatBigDecimal(input.getAmendId()==null? "" :input.getAmendId().toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rskPremiumDetails;
	}
	
}
