package com.maan.insurance.service.portFolio;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.portFolio.GetAutoPendingListReq;
import com.maan.insurance.model.req.portFolio.GetContractsListReq;
import com.maan.insurance.model.req.portFolio.GetHistoryListReq;
import com.maan.insurance.model.req.portFolio.GetPendingListReq;
import com.maan.insurance.model.req.portFolio.ProcAutoReq;
import com.maan.insurance.model.res.portFolio.GetAutoPendingListRes;
import com.maan.insurance.model.res.portFolio.GetContractsListRes;
import com.maan.insurance.model.res.portFolio.GetHistoryListRes;
import com.maan.insurance.model.res.portFolio.GetPendingListRes;
import com.maan.insurance.model.res.portFolio.GetRejectedListRes;
import com.maan.insurance.model.res.retro.CommonResponse;

@Service
public interface PortFolioService {

	GetPendingListRes getPendingList(GetPendingListReq req);

	GetRejectedListRes getRejectedList(GetPendingListReq req);

	GetAutoPendingListRes getAutoPendingList(GetAutoPendingListReq req);

	GetContractsListRes getContractsList(GetContractsListReq req);

	GetHistoryListRes getHistoryList(GetHistoryListReq req);

	CommonResponse procAuto(ProcAutoReq req);

}
