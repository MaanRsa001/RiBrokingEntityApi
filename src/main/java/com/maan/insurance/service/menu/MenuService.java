package com.maan.insurance.service.menu;

import org.springframework.stereotype.Service;

import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.menu.GetAdminMenuRes;
import com.maan.insurance.model.res.menu.GetRigthsOfProcessRes;
import com.maan.insurance.model.res.xolPremium.CommonSaveRes;

@Service
public interface MenuService {

	GetAdminMenuRes getAdminMenu(String loginId);

	CommonSaveRes getManuName(String mfrId, String branchCode);

	GetRigthsOfProcessRes getRigthsOfProcess(String loginId, String menuId, String processId);

	GetCommonDropDownRes getDepartmentListNew(String productId, String branchCode);

	GetCommonDropDownRes getProcessList(String branchCode, String productId, String deptId);

}
