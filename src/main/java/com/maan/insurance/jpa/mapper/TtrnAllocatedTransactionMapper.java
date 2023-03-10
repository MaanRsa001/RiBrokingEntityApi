package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.model.entity.TtrnBillingTransaction;
import com.maan.insurance.validation.Formatters;

@Component
public class TtrnAllocatedTransactionMapper extends AbstractEntityMapper<TtrnAllocatedTransaction, String[]>{
	@Autowired
	private Formatters fm;
	
	@Override
	public String[] fromEntity(TtrnAllocatedTransaction input) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public TtrnAllocatedTransaction toEntity(String[] input) throws ParseException {
		TtrnAllocatedTransaction ttrnAllocatedTransaction = null;
		if(input != null) {
			ttrnAllocatedTransaction = new TtrnAllocatedTransaction();
			ttrnAllocatedTransaction.setSno(fm.formatBigDecimal(input[0]));
			ttrnAllocatedTransaction.setContractNo((input[1]));
			ttrnAllocatedTransaction.setLayerNo((input[2]));
			ttrnAllocatedTransaction.setProductName(input[3]);
			ttrnAllocatedTransaction.setTransactionNo(fm.formatBigDecimal(input[4]));
			ttrnAllocatedTransaction.setInceptionDate(formatDate(input[5]));
			ttrnAllocatedTransaction.setPaidAmount(fm.formatBigDecimal(input[6]));
			ttrnAllocatedTransaction.setType(input[7]);
			ttrnAllocatedTransaction.setStatus(input[8]);
			ttrnAllocatedTransaction.setAmendId(fm.formatBigDecimal(input[9]));
			ttrnAllocatedTransaction.setReceiptNo(fm.formatBigDecimal(input[10]));
			ttrnAllocatedTransaction.setCurrencyId(fm.formatBigDecimal(input[11]));
			ttrnAllocatedTransaction.setProcessType(input[12]);
			ttrnAllocatedTransaction.setSubClass((input[13]));
			ttrnAllocatedTransaction.setLoginId(input[14]);
			ttrnAllocatedTransaction.setBranchCode(input[15]);
			ttrnAllocatedTransaction.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnAllocatedTransaction.setProposalNo((input[16]));
		}
		return ttrnAllocatedTransaction;
	}
	public TtrnBillingTransaction toEntityBilling(String[] input) throws ParseException {
		TtrnBillingTransaction ttrnBillingTransaction = null;
		if(input != null) {
			ttrnBillingTransaction = new TtrnBillingTransaction();
			ttrnBillingTransaction.setBillSno(fm.formatBigDecimal(input[0]));
			ttrnBillingTransaction.setContractNo(fm.formatBigDecimal(input[1]));
			ttrnBillingTransaction.setLayerNo(fm.formatBigDecimal((input[2])));
			ttrnBillingTransaction.setProductName(input[3]);
			ttrnBillingTransaction.setTransactionNo(input[4]);
			ttrnBillingTransaction.setInceptionDate(formatDate(input[5]));
			ttrnBillingTransaction.setPaidAmount(fm.formatBigDecimal(input[6]));
			ttrnBillingTransaction.setType(input[7]);
			ttrnBillingTransaction.setStatus(input[8]);
			ttrnBillingTransaction.setAmendId(fm.formatBigDecimal(input[9]));
			ttrnBillingTransaction.setBillNo(fm.formatBigDecimal(input[10]));
			ttrnBillingTransaction.setCurrencyId(fm.formatBigDecimal(input[11]));
			ttrnBillingTransaction.setProcessType(input[12]);
		//	ttrnBillingTransaction.setSubClass((input[13]));
			ttrnBillingTransaction.setLoginId(input[14]);
			ttrnBillingTransaction.setBranchCode(input[15]);
			ttrnBillingTransaction.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			ttrnBillingTransaction.setGrossAmount(fm.formatBigDecimal(input[16]));
			ttrnBillingTransaction.setWhtPremium(fm.formatBigDecimal(input[17]));
			ttrnBillingTransaction.setWhtBrokerage(fm.formatBigDecimal(input[18]));
			ttrnBillingTransaction.setNetAmount(fm.formatBigDecimal(input[19]));

			//ttrnBillingTransaction.setProposalNo(fm.formatBigDecimal(input[16]));

			//ttrnBillingTransaction.setProposalNo(fm.formatBigDecimal(input[16]));
		
//			ttrnBillingTransaction.setAdjustmentType(null);

//			ttrnBillingTransaction.setRemarks(null);
//			ttrnBillingTransaction.setReversalAmount(null);
//			ttrnBillingTransaction.setReversalDate(null);

		}
		return ttrnBillingTransaction;
	}

	

}
