package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.NotificationAttachmentDetail;
import com.maan.insurance.model.entity.NotificationAttachmentDetailId;

public interface NotificationAttachmentDetailRepository  extends JpaRepository<NotificationAttachmentDetail,NotificationAttachmentDetailId > , JpaSpecificationExecutor<NotificationAttachmentDetail> {

//	NotificationAttachmentDetail findTop1OrderByDocIdDesc();

//	NotificationAttachmentDetail getMaxDocId();

//	NotificationAttachmentDetail findTop1ByDocId();
//
//	NotificationAttachmentDetail findTopByOrderByDocId();

	NotificationAttachmentDetail findTop1ByOrderByDocIdDesc();

	void deleteByDocIdAndOrgFileName(BigDecimal bigDecimal, String fileName);

	NotificationAttachmentDetail findByDocIdAndOrgFileName(BigDecimal bigDecimal, String fileName);

}
