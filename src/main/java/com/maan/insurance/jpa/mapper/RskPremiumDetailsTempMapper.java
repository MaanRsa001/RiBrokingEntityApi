package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;

import org.apache.tika.utils.StringUtils;
import org.springframework.stereotype.Component;

import com.maan.insurance.model.entity.RskPremiumDetailsTemp;

@Component
public class RskPremiumDetailsTempMapper extends AbstractEntityMapper<RskPremiumDetailsTemp, String[]>{

	@Override
	public String[] fromEntity(RskPremiumDetailsTemp input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RskPremiumDetailsTemp toEntity(String[] input) throws ParseException {
		RskPremiumDetailsTemp rskPremiumDetailsTemp = null;
		if(input != null) {
			rskPremiumDetailsTemp = new RskPremiumDetailsTemp();
			rskPremiumDetailsTemp.setContractNo(formatBigDecimal(input[0]));
			rskPremiumDetailsTemp.setRequestNo(formatBigDecimal(input[1]));
			rskPremiumDetailsTemp.setTransactionMonthYear(formatDate(input[2]));
			rskPremiumDetailsTemp.setAccountPeriodYear(formatBigDecimal(input[4]));
			rskPremiumDetailsTemp.setCurrencyId(formatBigDecimal(input[5]));
			rskPremiumDetailsTemp.setExchangeRate(formatBigDecimal(input[6]));
			rskPremiumDetailsTemp.setBrokerage(formatBigDecimal(input[7]));
			rskPremiumDetailsTemp.setBrokerageAmtOc(formatBigDecimal(input[8]));
			rskPremiumDetailsTemp.setTax(formatBigDecimal(input[9]));
			rskPremiumDetailsTemp.setTaxAmtOc(formatBigDecimal(input[10]));
			rskPremiumDetailsTemp.setEntryDateTime(getTimestamp(input[11]));
			rskPremiumDetailsTemp.setCommission(formatBigDecimal(input[12]));
			rskPremiumDetailsTemp.setMDpremiumOc(formatBigDecimal(input[13]));
			rskPremiumDetailsTemp.setAdjustmentPremiumOc(formatBigDecimal(input[14]));
			rskPremiumDetailsTemp.setRecPremiumOc(formatBigDecimal(input[15]));
			rskPremiumDetailsTemp.setStatus(input[16]);
			rskPremiumDetailsTemp.setNetdueOc(formatBigDecimal(input[17]));
			rskPremiumDetailsTemp.setLayerNo(formatBigDecimal(input[18]));
			rskPremiumDetailsTemp.setEnteringMode(input[19]);
			rskPremiumDetailsTemp.setReceiptNo(formatBigDecimal(input[20]));
			rskPremiumDetailsTemp.setInstalmentNumber(input[21]);
			rskPremiumDetailsTemp.setSettlementStatus(input[22]);
			rskPremiumDetailsTemp.setOtherCostOc(formatBigDecimal(input[23]));
			rskPremiumDetailsTemp.setBrokerageAmtDc(formatBigDecimal(input[24]));
			rskPremiumDetailsTemp.setTaxAmtDc(formatBigDecimal(input[25]));
			rskPremiumDetailsTemp.setMDpremiumDc(formatBigDecimal(input[26]));
			rskPremiumDetailsTemp.setAdjustmentPremiumDc(formatBigDecimal(input[27]));
			rskPremiumDetailsTemp.setRecPremiumDc(formatBigDecimal(input[28]));
			rskPremiumDetailsTemp.setNetdueDc(formatBigDecimal(input[29]));
			rskPremiumDetailsTemp.setOtherCostDc(formatBigDecimal(input[30]));
			rskPremiumDetailsTemp.setCedantReference(input[31]);
			rskPremiumDetailsTemp.setRemarks(input[32]);
			rskPremiumDetailsTemp.setTotalCrOc(formatBigDecimal(input[33]));
			rskPremiumDetailsTemp.setTotalCrDc(formatBigDecimal(input[34]));
			rskPremiumDetailsTemp.setTotalDrOc(formatBigDecimal(input[35]));
			rskPremiumDetailsTemp.setTotalDrDc(formatBigDecimal(input[36]));
			rskPremiumDetailsTemp.setEntryDate(getCurrentTimestamp());
			rskPremiumDetailsTemp.setWithHoldingTaxOc(formatBigDecimal(input[37]));
			rskPremiumDetailsTemp.setWithHoldingTaxDc(formatBigDecimal(input[38]));
			rskPremiumDetailsTemp.setRiCession(input[39]);
			rskPremiumDetailsTemp.setLoginId(input[40]);
			rskPremiumDetailsTemp.setBranchCode(input[41]);
			rskPremiumDetailsTemp.setSubClass(formatBigDecimal(input[42]));
			rskPremiumDetailsTemp.setTdsOc(formatBigDecimal(input[43]));
			rskPremiumDetailsTemp.setTdsDc(formatBigDecimal(input[44]));
			rskPremiumDetailsTemp.setStOc(formatBigDecimal(input[45]));
			rskPremiumDetailsTemp.setStDc(formatBigDecimal(input[46]));
			rskPremiumDetailsTemp.setBonusOc(formatBigDecimal(input[47]));
			rskPremiumDetailsTemp.setBonusDc(formatBigDecimal(input[48]));
			rskPremiumDetailsTemp.setGnpiEndtNo(input[49]);
			rskPremiumDetailsTemp.setPremiumSubclass(input[50]);
			rskPremiumDetailsTemp.setPremiumClass(input[51]);
			rskPremiumDetailsTemp.setStatementDate(formatDate(input[52]));
			rskPremiumDetailsTemp.setProposalNo(formatBigDecimal(input[53]));
			rskPremiumDetailsTemp.setProductId(formatBigDecimal(input[54]));
			rskPremiumDetailsTemp.setReverselStatus(input[55]);
			rskPremiumDetailsTemp.setTransStatus(input[56]);
			rskPremiumDetailsTemp.setTransType(input[57]);
		}
		return rskPremiumDetailsTemp;
	}
	
	public RskPremiumDetailsTemp toTempEntity(String[] input) throws ParseException {
		RskPremiumDetailsTemp rskPremiumDetailsTemp = null;
		if(input != null) {
			rskPremiumDetailsTemp = new RskPremiumDetailsTemp();
			rskPremiumDetailsTemp.setContractNo(new BigDecimal(input[0]));
			rskPremiumDetailsTemp.setRequestNo(new BigDecimal(input[1]));
			rskPremiumDetailsTemp.setTransactionMonthYear(getTimestamp(input[2]));
			rskPremiumDetailsTemp.setAccountPeriodQtr(input[3]);
			rskPremiumDetailsTemp.setAccountPeriodYear(formatBigDecimal(input[4]));
			rskPremiumDetailsTemp.setCurrencyId(formatBigDecimal(input[5]));
			rskPremiumDetailsTemp.setExchangeRate(formatBigDecimal(input[6]));
			rskPremiumDetailsTemp.setBrokerage(formatBigDecimal(input[7]));
			rskPremiumDetailsTemp.setBrokerageAmtOc(formatBigDecimal(input[8]));
			rskPremiumDetailsTemp.setTax(formatBigDecimal(input[9]));
			rskPremiumDetailsTemp.setTaxAmtOc(formatBigDecimal(input[10]));
			rskPremiumDetailsTemp.setEntryDateTime(getTimestamp(input[11]));
			
			rskPremiumDetailsTemp.setPremiumQuotashareOc(formatBigDecimal(input[12]));
			rskPremiumDetailsTemp.setCommissionQuotashareOc(formatBigDecimal(input[13]));
			rskPremiumDetailsTemp.setPremiumSurplusOc(formatBigDecimal(input[14]));
			rskPremiumDetailsTemp.setCommissionSurplusOc(formatBigDecimal(input[15]));
			rskPremiumDetailsTemp.setPremiumPortfolioinOc(formatBigDecimal(input[16]));
			rskPremiumDetailsTemp.setClaimPortfolioinOc(formatBigDecimal(input[17]));
			rskPremiumDetailsTemp.setPremiumPortfoliooutOc(formatBigDecimal(input[18]));
			rskPremiumDetailsTemp.setLossReserveReleasedOc(formatBigDecimal(input[19]));
			rskPremiumDetailsTemp.setPremiumreserveQuotashareOc(formatBigDecimal(input[20]));
			rskPremiumDetailsTemp.setCashLossCreditOc(formatBigDecimal(input[21]));
			rskPremiumDetailsTemp.setLossReserveretainedOc(formatBigDecimal(input[22]));
			rskPremiumDetailsTemp.setProfitCommissionOc(formatBigDecimal(input[23]));
			rskPremiumDetailsTemp.setCashLosspaidOc(formatBigDecimal(input[24]));
			
			rskPremiumDetailsTemp.setStatus(input[25]);
			rskPremiumDetailsTemp.setEnteringMode(input[26]);
			rskPremiumDetailsTemp.setReceiptNo(formatBigDecimal(input[27]));
			rskPremiumDetailsTemp.setClaimsPaidOc(formatBigDecimal(input[28]));
			rskPremiumDetailsTemp.setSettlementStatus(input[29]);
			rskPremiumDetailsTemp.setXlCostOc(formatBigDecimal(input[30]));
			rskPremiumDetailsTemp.setClaimPortfolioOutOc(formatBigDecimal(input[31]));
			rskPremiumDetailsTemp.setPremiumReserveRealsedOc(formatBigDecimal(input[32]));
			rskPremiumDetailsTemp.setNetdueOc(formatBigDecimal(input[33]));
			rskPremiumDetailsTemp.setOtherCostOc(formatBigDecimal(input[34]));
			rskPremiumDetailsTemp.setBrokerageAmtDc(formatBigDecimal(input[35]));
			rskPremiumDetailsTemp.setTaxAmtDc(formatBigDecimal(input[36]));
			rskPremiumDetailsTemp.setPremiumQuotashareDc(formatBigDecimal(input[37]));
			rskPremiumDetailsTemp.setCommissionQuotashareDc(formatBigDecimal(input[38]));
			rskPremiumDetailsTemp.setPremiumSurplusDc(formatBigDecimal(input[39]));
			
			rskPremiumDetailsTemp.setCommissionSurplusDc(formatBigDecimal(input[40]));
			rskPremiumDetailsTemp.setPremiumPortfolioinDc(formatBigDecimal(input[41]));
			rskPremiumDetailsTemp.setClaimPortfolioinDc(formatBigDecimal(input[42]));
			rskPremiumDetailsTemp.setPremiumPortfoliooutDc(formatBigDecimal(input[43]));
			rskPremiumDetailsTemp.setLossReserveReleasedDc(formatBigDecimal(input[44]));
			rskPremiumDetailsTemp.setPremiumreserveQuotashareDc(formatBigDecimal(input[45]));
			rskPremiumDetailsTemp.setCashLossCreditDc(formatBigDecimal(input[46]));
			rskPremiumDetailsTemp.setLossReserveretainedDc(formatBigDecimal(input[47]));
			rskPremiumDetailsTemp.setProfitCommissionDc(formatBigDecimal(input[48]));
			rskPremiumDetailsTemp.setCashLosspaidDc(formatBigDecimal(input[49]));
			
			rskPremiumDetailsTemp.setClaimsPaidDc(formatBigDecimal(input[50]));
			rskPremiumDetailsTemp.setXlCostDc(formatBigDecimal(input[51]));
			rskPremiumDetailsTemp.setClaimPortfolioOutDc(formatBigDecimal(input[52]));
			rskPremiumDetailsTemp.setPremiumReserveRealsedDc(formatBigDecimal(input[53]));
			rskPremiumDetailsTemp.setNetdueDc(formatBigDecimal(input[54]));
			rskPremiumDetailsTemp.setOtherCostDc(formatBigDecimal(input[55]));
			rskPremiumDetailsTemp.setCommission(formatBigDecimal(input[56]));
			rskPremiumDetailsTemp.setCedantReference(input[57]);
			rskPremiumDetailsTemp.setRemarks(input[58]);
			rskPremiumDetailsTemp.setTotalCrOc(formatBigDecimal(input[59]));
			rskPremiumDetailsTemp.setTotalCrDc(formatBigDecimal(input[60]));
			rskPremiumDetailsTemp.setTotalDrOc(formatBigDecimal(input[61]));
			rskPremiumDetailsTemp.setTotalDrDc(formatBigDecimal(input[62]));
			rskPremiumDetailsTemp.setInterestOc(formatBigDecimal(input[63]));
			rskPremiumDetailsTemp.setInterestDc(formatBigDecimal(input[64]));
			rskPremiumDetailsTemp.setOsclaimLossupdateOc(formatBigDecimal(input[65]));
			rskPremiumDetailsTemp.setOsclaimLossupdateDc(formatBigDecimal(input[66]));
			
			rskPremiumDetailsTemp.setEntryDate(getCurrentTimestamp());
			rskPremiumDetailsTemp.setOverriderAmtOc(formatBigDecimal(input[67]));
			rskPremiumDetailsTemp.setOverriderAmtDc(formatBigDecimal(input[68]));
			rskPremiumDetailsTemp.setOverrider(formatBigDecimal(input[69]));
			rskPremiumDetailsTemp.setWithHoldingTaxOc(formatBigDecimal(input[70]));
			rskPremiumDetailsTemp.setWithHoldingTaxDc(formatBigDecimal(input[71]));
			rskPremiumDetailsTemp.setRiCession(input[72]);
			rskPremiumDetailsTemp.setLoginId(input[73]);
			rskPremiumDetailsTemp.setBranchCode(input[74]);
			rskPremiumDetailsTemp.setSubClass(formatBigDecimal(input[75]));
			rskPremiumDetailsTemp.setTdsOc(formatBigDecimal(input[76]));
			rskPremiumDetailsTemp.setTdsDc(formatBigDecimal(input[77]));
			rskPremiumDetailsTemp.setStOc(formatBigDecimal(input[78]));
			rskPremiumDetailsTemp.setStDc(formatBigDecimal(input[79]));
			rskPremiumDetailsTemp.setScCommOc(formatBigDecimal(input[80]));
			rskPremiumDetailsTemp.setScCommDc(formatBigDecimal(input[81]));
			rskPremiumDetailsTemp.setPremiumClass(input[82]);
			rskPremiumDetailsTemp.setPremiumSubclass(input[83]);
			rskPremiumDetailsTemp.setAccountingPeriodDate(formatDate(input[84]));
			rskPremiumDetailsTemp.setStatementDate(formatDate(input[85]));
			rskPremiumDetailsTemp.setOsbyn(input[86]);
			rskPremiumDetailsTemp.setLpcOc(formatBigDecimal(input[87]));
			rskPremiumDetailsTemp.setLpcDc(formatBigDecimal(input[88]));
			rskPremiumDetailsTemp.setSectionName(input[89]);
			rskPremiumDetailsTemp.setProposalNo(formatBigDecimal(input[90]));
			rskPremiumDetailsTemp.setProductId(formatBigDecimal(input[91]));
			rskPremiumDetailsTemp.setTransStatus(input[92]);
			rskPremiumDetailsTemp.setTransType(input[93]);
		}
		return rskPremiumDetailsTemp;
	}
}
