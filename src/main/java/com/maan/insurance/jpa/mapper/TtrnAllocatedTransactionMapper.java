package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;

import org.springframework.stereotype.Component;

import com.maan.insurance.jpa.entity.treasury.TtrnAllocatedTransaction;

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

	

}
