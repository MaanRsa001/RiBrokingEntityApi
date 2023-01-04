package com.maan.insurance.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.insurance.model.entity.MailNotificationDetail;
import com.maan.insurance.model.entity.MailNotificationDetailId;

public interface MailNotificationDetailRepository  extends JpaRepository<MailNotificationDetail,MailNotificationDetailId > , JpaSpecificationExecutor<MailNotificationDetail> {

	MailNotificationDetail findByProposalNoAndReinsurerIdAndBrokerIdAndBranchCode(BigDecimal bigDecimal,
			String reinsurerId, String brokerId, String branchCode);

}
