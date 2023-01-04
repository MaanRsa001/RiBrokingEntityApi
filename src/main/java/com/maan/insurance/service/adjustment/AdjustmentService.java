package com.maan.insurance.service.adjustment;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.adjustment.AdjustmentViewReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentDoneListReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentListReq;
import com.maan.insurance.model.req.adjustment.GetAdjustmentPayRecListReq;
import com.maan.insurance.model.req.adjustment.InsertReverseReq;
import com.maan.insurance.model.res.adjustment.AdjustmentViewRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentDoneListRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentListRes;
import com.maan.insurance.model.res.adjustment.GetAdjustmentPayRecListRes;
import com.maan.insurance.model.res.adjustment.InsertReverseRes;
import com.maan.insurance.model.res.retro.CommonResponse;

@Service
public interface AdjustmentService {

	GetAdjustmentDoneListRes getAdjustmentDoneList(GetAdjustmentDoneListReq req);

	AdjustmentViewRes adjustmentView(AdjustmentViewReq req);

	GetAdjustmentListRes getAdjustmentList(GetAdjustmentListReq req);

	GetAdjustmentPayRecListRes getAdjustmentPayRecList(GetAdjustmentListReq req);

	CommonResponse addAdjustment(GetAdjustmentListReq req);

	CommonResponse addAdjustmentPayRec(GetAdjustmentListReq req);

	InsertReverseRes insertReverse(InsertReverseReq req);

}
