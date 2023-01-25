package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;
import com.maan.insurance.model.entity.TtrnBillingTransaction;

@Component
public class TtrnAllocatedTransactionMapper extends AbstractEntityMapper<TtrnAllocatedTransaction, String[]>{

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
			ttrnAllocatedTransaction.setSno(new BigDecimal(input[0]));
			ttrnAllocatedTransaction.setContractNo((input[1]));
			ttrnAllocatedTransaction.setLayerNo((input[2]));
			ttrnAllocatedTransaction.setProductName(input[3]);
			ttrnAllocatedTransaction.setTransactionNo(new BigDecimal(input[4]));
			ttrnAllocatedTransaction.setInceptionDate(formatDate(input[5]));
			ttrnAllocatedTransaction.setPaidAmount(new BigDecimal(input[6]));
			ttrnAllocatedTransaction.setType(input[7]);
			ttrnAllocatedTransaction.setStatus(input[8]);
			ttrnAllocatedTransaction.setAmendId(new BigDecimal(input[9]));
			ttrnAllocatedTransaction.setReceiptNo(new BigDecimal(input[10]));
			ttrnAllocatedTransaction.setCurrencyId(new BigDecimal(input[11]));
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
			ttrnBillingTransaction.setBillSno(new BigDecimal(input[0]));
			ttrnBillingTransaction.setContractNo(new BigDecimal(input[1]));
			ttrnBillingTransaction.setLayerNo(new BigDecimal((input[2])));
			ttrnBillingTransaction.setProductName(input[3]);
			ttrnBillingTransaction.setTransactionNo(input[4]);
			ttrnBillingTransaction.setInceptionDate(formatDate(input[5]));
			ttrnBillingTransaction.setPaidAmount(new BigDecimal(input[6]));
			ttrnBillingTransaction.setType(input[7]);
			ttrnBillingTransaction.setStatus(input[8]);
			ttrnBillingTransaction.setAmendId(new BigDecimal(input[9]));
			ttrnBillingTransaction.setBillNo(new BigDecimal(input[10]));
			ttrnBillingTransaction.setCurrencyId(new BigDecimal(input[11]));
			ttrnBillingTransaction.setProcessType(input[12]);
		//	ttrnBillingTransaction.setSubClass((input[13]));
			ttrnBillingTransaction.setLoginId(input[14]);
			ttrnBillingTransaction.setBranchCode(input[15]);
			ttrnBillingTransaction.setSysDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));

			//ttrnBillingTransaction.setProposalNo(new BigDecimal(input[16]));

			ttrnBillingTransaction.setProposalNo(new BigDecimal(input[16]));
		
//			ttrnBillingTransaction.setAdjustmentType(null);

//			ttrnBillingTransaction.setRemarks(null);
//			ttrnBillingTransaction.setReversalAmount(null);
//			ttrnBillingTransaction.setReversalDate(null);

		}
		return ttrnBillingTransaction;
	}

	

}
