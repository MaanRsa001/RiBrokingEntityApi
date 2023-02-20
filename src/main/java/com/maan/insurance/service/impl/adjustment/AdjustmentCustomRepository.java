package com.maan.insurance.service.impl.adjustment;

import java.util.List;

import javax.persistence.Tuple;

import com.maan.insurance.model.req.adjustment.GetAdjustmentListReq;

public interface AdjustmentCustomRepository {

	List<Tuple> adjutmentList(String[] obj, GetAdjustmentListReq bean);

	List<Tuple> adjutmentListUnion(String[] obj, GetAdjustmentListReq bean);

	List<Tuple> adjustmentListIndTransactionCP(String branchCode, String transactionNo);

	List<Tuple> adjustmentListIndTransactionCPUnion(String branchCode, String transactionNo);

	List<Tuple> adjustmentPayrecList(String[] obj,GetAdjustmentListReq bean);

	List<Tuple> adjustmentListIndTransactionRP(String branchCode, String transactionNo);


}
