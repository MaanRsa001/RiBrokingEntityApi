package com.maan.insurance.service.premium;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.premium.ContractidetifierlistReq;
import com.maan.insurance.model.req.premium.PremiumListReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.DropDown.GetOpenPeriodRes;
import com.maan.insurance.model.res.premium.ContractidetifierlistRes;
import com.maan.insurance.model.res.premium.PremiumListRes;

@Service
public interface PremiumService {

	GetOpenPeriodRes getOpenPeriod(String branchCode);

	PremiumListRes PremiumList(PremiumListReq req);

	GetCommonDropDownRes productIdList(String branchCode);

	ContractidetifierlistRes contractidetifierlist(ContractidetifierlistReq req);

	PremiumListRes PremiumRiList(PremiumListReq req);

//	CommonResponse copyDatatoDeleteTable(CopyDatatoDeleteTableReq req);

}
