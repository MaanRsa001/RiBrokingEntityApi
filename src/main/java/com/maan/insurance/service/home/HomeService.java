package com.maan.insurance.service.home;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.home.GetOldProductIdReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.home.GetDepartmentListRes;
import com.maan.insurance.model.res.home.GetFinalMenuListRes;
import com.maan.insurance.model.res.home.GetMenuDropDownListRes;
import com.maan.insurance.model.res.retro.CommonSaveRes;

@Service
public interface HomeService {

	GetMenuDropDownListRes getMenuDropDownList(String branchCode, String userId);

	GetCommonDropDownRes getDepartmentList(String branchCode, String productId);

	GetCommonDropDownRes getProcessList(String branchCode, String productId, String deptId);

	GetFinalMenuListRes getFinalMenuList(String userId, String processId);

	CommonSaveRes getOldProductId(GetOldProductIdReq req);

}
