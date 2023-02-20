package com.maan.insurance.service.impl.upload;

import java.util.List;

import javax.persistence.Tuple;

import com.maan.insurance.model.req.placement.SendMailReq;

public interface UploadCustomRepository {

	List<Tuple> getMailAttachList(SendMailReq bean);

	


}
