package com.maan.insurance.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.MailNotificationDetail;
import com.maan.insurance.model.entity.MailNotificationDetailId;
import com.maan.insurance.model.entity.MailTemplateMaster;
import com.maan.insurance.model.entity.MailTemplateMasterId;

public interface MailTemplateMasterRepository extends JpaRepository<MailTemplateMaster,MailTemplateMasterId > , JpaSpecificationExecutor<MailTemplateMaster> {

	List<MailTemplateMaster> findByMailType(String mailType);

}
