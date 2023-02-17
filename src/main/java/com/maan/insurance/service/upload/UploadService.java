package com.maan.insurance.service.upload;

import org.springframework.stereotype.Service;
import com.maan.insurance.model.req.DoUploadReq;
import com.maan.insurance.model.req.upload.DoDeleteDocDetailsReq;
import com.maan.insurance.model.req.upload.GetDocListReq;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.model.res.upload.AllmoduleListRes;
import com.maan.insurance.model.res.upload.GetDocListRes;
import com.maan.insurance.model.res.upload.GetDocTypeRes;

@Service
public interface UploadService {

	AllmoduleListRes allmoduleList(String branchCode);

	GetDocListRes getDocList(GetDocListReq req);

	GetDocTypeRes getDocType(String branchCode, String productId, String moduleType);

	GetCommonDropDownRes moduleTypeList(String branchCode);

	CommonResponse doDeleteDocDetails(DoDeleteDocDetailsReq req);

	CommonSaveRes doUpload(DoUploadReq req);

}
