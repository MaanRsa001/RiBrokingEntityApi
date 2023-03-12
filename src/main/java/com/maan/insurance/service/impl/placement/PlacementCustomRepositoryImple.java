package com.maan.insurance.service.impl.placement;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.CurrencyMaster;
import com.maan.insurance.model.entity.MailNotificationDetail;
import com.maan.insurance.model.entity.NotificationAttachmentDetail;
import com.maan.insurance.model.entity.PersonalInfo;
import com.maan.insurance.model.entity.PersonalInfoContact;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.StatusMaster;
import com.maan.insurance.model.entity.TmasDepartmentMaster;
import com.maan.insurance.model.entity.TmasDocTypeMaster;
import com.maan.insurance.model.entity.TmasProductMaster;
import com.maan.insurance.model.entity.TtrnRiPlacement;
import com.maan.insurance.model.entity.TtrnRiPlacementStatus;
import com.maan.insurance.model.entity.TtrnRiskDetails;
import com.maan.insurance.model.entity.TtrnRiskProposal;
import com.maan.insurance.model.repository.TtrnRiPlacementRepository;
import com.maan.insurance.model.req.placement.EditPlacingDetailsReq;
import com.maan.insurance.model.req.placement.GetApprovalPendingListReq;
import com.maan.insurance.model.req.placement.GetExistingAttachListReq;
import com.maan.insurance.model.req.placement.GetExistingReinsurerListReq;
import com.maan.insurance.model.req.placement.GetMailTemplateReq;
import com.maan.insurance.model.req.placement.GetMailToListReq;
import com.maan.insurance.model.req.placement.GetPlacementInfoListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewListReq;
import com.maan.insurance.model.req.placement.GetPlacementViewReq;
import com.maan.insurance.model.req.placement.GetPlacingInfoReq;
import com.maan.insurance.model.req.placement.PlacementSummaryReq;
import com.maan.insurance.model.req.placement.SavePlacingReq;
import com.maan.insurance.model.req.placement.SendMailReq;
import com.maan.insurance.model.req.placement.UpdatePlacementListReq;
import com.maan.insurance.model.req.placement.UpdatePlacementReq;
import com.maan.insurance.model.req.placement.UpdateRiplacementReq;
import com.maan.insurance.model.res.placement.GetPlacementNoRes;
import com.maan.insurance.model.res.placement.GetPlacementNoRes1;
import com.maan.insurance.validation.Formatters;

@Repository
public class PlacementCustomRepositoryImple implements PlacementCustomRepository {
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy") ;
	@Autowired
	EntityManager em;
	@Autowired
	private Formatters fm;
	@Autowired
	private TtrnRiPlacementRepository ttrnRiPlacementRepository;
	@Override
	public List<Tuple> getMailToList(GetMailToListReq bean) {
		String cedeingId="";
		List<Tuple> list=null;
		try {
			if(("A".equals(bean.getCurrentStatus())  && "PWL".equals(bean.getNewStatus())) || StringUtils.isBlank(bean.getReinsurerId())) {
				cedeingId="63".equals(bean.getBrokerId())?bean.getCedingId():bean.getBrokerId();
				
			}else {
				cedeingId="63".equals(bean.getBrokerId())?bean.getReinsurerId():bean.getBrokerId();
			}
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PersonalInfoContact> pm = query.from(PersonalInfoContact.class);

			query.multiselect(pm.get("email").alias("CODE")); 

			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PersonalInfoContact> pms = amend.from(PersonalInfoContact.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("customerId"), pms.get("customerId"));
			amend.where(a1);

			// Where
			Predicate n1 = cb.equal(pm.get("customerId"), cedeingId);
			Predicate n2 = cb.equal(pm.get("amendId"), amend);
			query.where(n1,n2);
			
			TypedQuery<Tuple> result = em.createQuery(query);
			list=result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> getExistingReinsurerList(GetExistingReinsurerListReq bean) {
		List<Tuple> list=null;
		try {
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
					
		//reinsurerName
		Subquery<String> reInsurerName = query.subquery(String.class); 
		Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
	
		Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
		reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
		
		//maxAmend
		Subquery<Long> maxAmend = query.subquery(Long.class); 
		Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
		maxAmend.select(cb.max(pis.get("amendId")));
		Predicate b1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
		maxAmend.where(b1);
		
		Predicate a1 = cb.equal( pi.get("customerType"), "R");
		Predicate a2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
		Predicate a3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
		Predicate a4 = cb.equal( pi.get("amendId"), maxAmend);
		reInsurerName.where(a1,a2,a3,a4);
		
		query.multiselect(pm.get("reinsurerId").alias("REINSURER_ID"),reInsurerName.alias("REINSURER_NAME")).distinct(true); 
		
		
		Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		if(StringUtils.isNotBlank(bean.getBouquetNo())) {
			//GET_EX_REINSURER_BOUQUET_LIST
			Predicate n2 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
			query.where(n1,n2);
		}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
			//GET_EX_REINSURER_BASE_LIST
			Predicate n2 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
			query.where(n1,n2);
		}else {
			//GET_EX_REINSURER_PRO_LIST
			Predicate n2 = cb.equal(pm.get("proposalNo"), StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo());
			query.where(n1,n2);
		}
		TypedQuery<Tuple> result = em.createQuery(query);
		list = result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> getExistingBrokerList(GetExistingReinsurerListReq bean) {
		List<Tuple> list=null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
						
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			brokerName.select(cb.concat(firstName1, pi.get("lastName")));
			
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate b1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(b1);
			
			Predicate a1 = cb.equal( pi.get("customerType"), "B");
			Predicate a2 = cb.equal( pi.get("customerId"), pm.get("brokerId"));
			Predicate a3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate a4 = cb.equal( pi.get("amendId"), maxAmend);
			brokerName.where(a1,a2,a3,a4);
			
			query.multiselect(pm.get("brokerId").alias("BROKER_ID"),brokerName.alias("BROKER_NAME")).distinct(true); 
			
			Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				//GET_EX_BROKER_BOUQUET_LIST
				Predicate n2 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
				query.where(n1,n2);
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				//GET_EX_BROKER_BASE_LIST
				Predicate n2 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
				query.where(n1,n2);
			}else {
				//GET_EX_BROKER_PRO_LIST
				Predicate n2 = cb.equal(pm.get("proposalNo"), StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo());
				query.where(n1,n2);
			}
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> getExistingAttachList(GetExistingAttachListReq bean) {
		List<Tuple> list=null;
		try {
		//GET_EX_DOC_PRO_LIST
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		Root<NotificationAttachmentDetail> pm = query.from(NotificationAttachmentDetail.class);
					
		//DOC_TYPE
		Subquery<String> docName = query.subquery(String.class); 
		Root<TmasDocTypeMaster> pi = docName.from(TmasDocTypeMaster.class);
	
		docName.select( pi.get("docName"));
		
		//maxAmend
		Subquery<Long> maxAmend = query.subquery(Long.class); 
		Root<TmasDocTypeMaster> pis = maxAmend.from(TmasDocTypeMaster.class);
		maxAmend.select(cb.max(pis.get("amendId")));
		Predicate b1 = cb.equal( pis.get("docType"), pi.get("docType"));
		Predicate b2 = cb.equal( pis.get("branchCode"), pi.get("branchCode"));
		Predicate b3 = cb.equal( pis.get("moduleType"), pi.get("moduleType"));
		Predicate b4 = cb.equal( pis.get("productId"), pi.get("productId"));
		maxAmend.where(b1,b2,b3,b4);
		
		Predicate a1 = cb.equal( pi.get("productId"), "12");
		Predicate a2 = cb.equal( pi.get("moduleType"), "PL");
		Predicate a3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
		Predicate a4 = cb.equal( pi.get("status"), "Y");
		Predicate a5 = cb.equal( pi.get("amendId"), maxAmend);
		Predicate a6 = cb.equal( pi.get("docType"), pm.get("docType"));
		docName.where(a1,a2,a3,a4,a5,a6);
		
		query.multiselect(pm.get("docId").alias("DOC_ID"),docName.alias("DOC_TYPE"),
				pm.get("docDescription").alias("DOC_DESCRIPTION"),pm.get("orgFileName").alias("ORG_FILE_NAME")
				,pm.get("ourFileName").alias("OUR_FILE_NAME")).distinct(true); 
		
		Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		
		Expression<String> e0 = pm.get("docType");
		Predicate n2 = e0.in("MA","MA2").not();

		Predicate n3 = cb.equal(pm.get("proposalNo"), StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo());
		Predicate n4 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
		Predicate n5 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
		Predicate n6 = cb.equal(pm.get("correspondentId"), bean.getCorresId());
		query.where(n1,n2,n3,n4,n5,n6);
		
		TypedQuery<Tuple> result = em.createQuery(query);
		list = result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> getExistingProposal(String proposal, String branchCode,String reinsId) {
		List<Tuple> list = new ArrayList<>();
		try {
			//GET_EXISTING_PROPOSAL
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class); //'New' POLICY_STATUS,''EXISTING_SHARE
			
			
			// MAXAmend ID
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pms.get("customerId"));
			bAmend.where(f1);
			
			companyName.select(pms.get("companyName"));
			Predicate a1 = cb.equal( rd.get("rskCedingid"), pms.get("customerId"));
			Predicate a2 = cb.equal( pms.get("customerType"), "C");
			Predicate a3 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a4 = cb.equal( pms.get("amendId"), bAmend);
			companyName.where(a1,a2,a3,a4);
			
			
			
			Subquery<String> reinsuredName = query.subquery(String.class); 
			Root<PersonalInfo> pms1 = reinsuredName.from(PersonalInfo.class);
			
			Subquery<Long> bAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pis11 = bAmend1.from(PersonalInfo.class);
			bAmend1.select(cb.max(pis11.get("amendId")));
			Predicate bf1 = cb.equal( pms1.get("customerId"), pis11.get("customerId"));
			bAmend1.where(bf1);
			
			reinsuredName.select(pms1.get("companyName"));
			Predicate r1 = cb.equal( pms1.get("customerId"),reinsId);
			Predicate r2 = cb.equal( pms1.get("customerType"), "R");
			Predicate r3 = cb.equal( pm.get("branchCode"), pms1.get("branchCode"));
			Predicate r4 = cb.equal( pms1.get("amendId"), bAmend1);
			reinsuredName.where(r1,r2,r3,r4);
			
			//baseLayer
			Subquery<String> baseLayer = query.subquery(String.class); 
			Root<PositionMaster> bs = baseLayer.from(PositionMaster.class);
			baseLayer.select(bs.get("baseLayer")).distinct(true)
			.where(cb.equal(bs.get("baseLayer"),cb.coalesce(pm.get("baseLayer"),pm.get("proposalNo"))));
			
			//treatyType
			Subquery<String> treatyType = query.subquery(String.class); 
			Root<ConstantDetail> cd = treatyType.from(ConstantDetail.class);
			treatyType.select(cd.get("detailName"));
			Predicate c1 =  cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,new BigDecimal(43) ).otherwise(new BigDecimal(29)) ,
					  cd.get("categoryId"));
			Predicate c2 = cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,rd.get("treatytype") ).otherwise(rd.get("rskBusinessType")) ,
					  cd.get("type"));	
			treatyType.where(c1,c2);
	
			query.multiselect(rd.get("rskInceptionDate").alias("INS_DATE"),rd.get("rskExpiryDate").alias("EXP_DATE"),
					reinsuredName.alias("REINSURER_NAME"),companyName.alias("COMPANY_NAME"),pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("contractNo").alias("CONTRACT_NO"),
					baseLayer.alias("BASE_LAYER"),pm.get("layerNo").alias("LAYER_NO"),pm.get("sectionNo").alias("SECTION_NO"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),
					rd.get("rskTreatyid").alias("RSK_TREATYID"),treatyType.alias("TREATY_TYPE"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"),
					pm.get("offerNo").alias("OFFER_NO"),rd.get("rskCedingid").alias("RSK_CEDINGID"),
					rd.get("rskBrokerid").alias("RSK_BROKERID"),pm.get("amendId").alias("AMEND_ID")); 
	
			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("offerNo")));
			orderList.add(cb.asc(pm.get("proposalNo")));
	
			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			Predicate n3 = cb.equal(pm.get("proposalNo"), proposal);
			Predicate n4 = cb.equal(pm.get("contractStatus"), "P");  //P
			query.where(n1,n2,n3,n4).orderBy(orderList);
			
			// Get Result
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
		}catch(Exception e){
			e.printStackTrace();
			
		}
	return list;
	}

	@Override
	public GetPlacementNoRes getPlacementNo(SavePlacingReq bean) {
		GetPlacementNoRes response = new GetPlacementNoRes();
		GetPlacementNoRes1 res = new GetPlacementNoRes1();
		String placementNo="",statusNo;
		try {
			if("C".equalsIgnoreCase(bean.getPlacementMode())) {
				if(StringUtils.isNotBlank(bean.getBouquetNo())) {
					//GET_PLACEMENT_NO_BOUQUET
					CriteriaBuilder cb = em.getCriteriaBuilder();
					CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
					Root<TtrnRiPlacement> root = cq.from(TtrnRiPlacement.class);
					
					cq.select(root.get("placementNo")).distinct(true)
					.where(cb.equal(root.get("bouquetNo"), bean.getBouquetNo()));
					
					List<BigDecimal> placeno = em.createQuery(cq).getResultList();
					if(placeno.size()>0)
					placementNo = placeno.get(0)==null?"":String.valueOf(placeno.get(0));
					
				}else {
					//GET_PLACEMENT_NO_BASELAYER
					CriteriaBuilder cb = em.getCriteriaBuilder();
					CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
					Root<TtrnRiPlacement> root = cq.from(TtrnRiPlacement.class);
					
					cq.select(root.get("placementNo")).distinct(true)
					.where(cb.equal(root.get("baseProposalNo"), bean.getBaseProposalNo()));
					
					List<BigDecimal> placeno = em.createQuery(cq).getResultList();
					if(placeno.size()>0)
					placementNo = placeno.get(0)==null?"":String.valueOf(placeno.get(0));
				}
			}else {
				//GET_PLACEMENT_NO_PROPOSAL
				CriteriaBuilder cb = em.getCriteriaBuilder();
				CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
				Root<TtrnRiPlacement> root = cq.from(TtrnRiPlacement.class);
				
				cq.select(root.get("placementNo")).distinct(true)
				.where(cb.equal(root.get("proposalNo"), bean.getEproposalNo()));
				
				List<BigDecimal> placeno = em.createQuery(cq).getResultList();
				if(placeno.size()>0)
				placementNo = placeno.get(0)==null?"":String.valueOf(placeno.get(0));
			}
			
			if(StringUtils.isBlank(placementNo)) {
			 	placementNo= fm.getSequence("PlacementNo","0","0", bean.getBranchCode(),"","");
			}
			statusNo= fm.getSequence("StatusNo","0","0", bean.getBranchCode(),"","");
		
			res.setStatusNo(statusNo);
			res.setPlacementNo(placementNo);
		
			response.setCommonResponse(res);	
			response.setMessage("Success");
			response.setIsError(false);
		}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}

	@Override
	public String getMaxAmendId(String branchCode, String eproposalNo, String reinsurerId, String brokerId) {
		String plamendId="0";
		try {
			//GET_PLACEMENT_MAX_AMENDID
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
			Root<TtrnRiPlacement> root = cq.from(TtrnRiPlacement.class);
			
			cq.select(cb.sum(cb.max(root.get("placementAmendId")),1).as(BigDecimal.class))
			.where(cb.equal(root.get("branchCode"), branchCode),
					cb.equal(root.get("proposalNo"), eproposalNo),
					cb.equal(root.get("reinsurerId"), reinsurerId),
					cb.equal(root.get("brokerId"), brokerId));
			
			BigDecimal plamno = em.createQuery(cq).getSingleResult();
			plamendId = String.valueOf(plamno);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return plamendId;
	}

	@Override
	public Tuple getPlacementDetails(String proposalNo, String reinsuerId, String brokerId,String branchCode) {
		Tuple map=null;
		try {
			//GET_PLACEMENT_DETAIL
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);

			query.multiselect(pm.get("placementNo").alias("PLACEMENT_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),
					pm.get("contractNo").alias("CONTRACT_NO"),pm.get("layerNo").alias("LAYER_NO"),
					pm.get("sectionNo").alias("SECTION_NO"),pm.get("amendId").alias("AMEND_ID"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),pm.get("reinsurerId").alias("REINSURER_ID"),
					pm.get("brokerId").alias("BROKER_ID"),pm.get("shareOffered").alias("SHARE_OFFERED"),
					pm.get("placementMode").alias("PLACEMENT_MODE"),pm.get("status").alias("STATUS")); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n3 = cb.equal(pm.get("reinsurerId"), reinsuerId);
			Predicate n4 = cb.equal(pm.get("brokerId"), brokerId);
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);
			query.where(n1,n2,n3,n4,n5);
			
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();

			if(!CollectionUtils.isEmpty(list)) {
					map=list.get(0);
			}
		}
		catch (Exception e) {
				e.printStackTrace();
		}	
		return map;
	}

	@Override
	public List<Tuple> GetPlacementEdit(String branchCode, String brokerId, String eproposalNo, String reinsurerId) {
		List<Tuple> list =null;
		try {
			//GET_PLACEMENT_EDIT
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
			//cedingCompanyName
			Subquery<Long> endPr = query.subquery(Long.class); 
			Root<TtrnRiskProposal> b = endPr.from(TtrnRiskProposal.class);
			endPr.select(cb.max(b.get("rskEndorsementNo")));
			endPr.where(cb.equal( b.get("rskProposalNumber"),pr.get("rskProposalNumber")));
			
			Subquery<Long> endDe = query.subquery(Long.class); 
			Root<TtrnRiskDetails> des = endDe.from(TtrnRiskDetails.class);
			endDe.select(cb.max(des.get("rskEndorsementNo")));
			endDe.where(cb.equal( des.get("rskProposalNumber"),de.get("rskProposalNumber")));
			
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> personal = cedingCompanyName.from(PersonalInfo.class);
			
			Subquery<Long> cmaxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> cpis = cmaxAmend.from(PersonalInfo.class);
			cmaxAmend.select(cb.max(cpis.get("amendId")));
			Predicate ca1 = cb.equal( cpis.get("customerId"), personal.get("customerId"));
			cmaxAmend.where(ca1);
			
			cedingCompanyName.select(personal.get("companyName"));
			Predicate b1 = cb.equal( pm.get("cedingCompanyId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pm.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( personal.get("customerType"), "C");
			Predicate b4 = cb.equal( personal.get("amendId"), cmaxAmend);
			cedingCompanyName.where(b1,b2,b3,b4);
			
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(c1);
			
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reinsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			//maxAmend
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			bAmend.where(f1);
			
			Predicate e1 = cb.equal( pi1.get("customerType"), "B");
			Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
			brokerName.where(e1,e2,e3,e4);
			
			//mailStatus
			Subquery<String> mailStatus = query.subquery(String.class); 
			Root<MailNotificationDetail> mail = mailStatus.from(MailNotificationDetail.class);
			
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(mail.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(mail.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(mail.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(mail.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(mail.get("mailStatus"));
			Predicate g1 = cb.equal( pm.get("proposalNo"), mail.get("proposalNo"));
			Predicate g2 = cb.equal( pm.get("reinsurerId"), mail.get("reinsurerId"));
			Predicate g3 = cb.equal(pm.get("brokerId"), mail.get("brokerId"));
			Predicate g4 = cb.equal(pm.get("status"), mail.get("mailType"));
			Predicate g5 = cb.equal(mail.get("mailRecordNo"), mmaxAmend);
			mailStatus.where(g1,g2,g3,g4,g5);

			query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("shareOffered").alias("SHARE_OFFERED"),pm.get("shareWritten").alias("SHARE_WRITTEN"),
					pm.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),pm.get("shareSigned").alias("SHARE_SIGNED"),
					pm.get("brokerage").alias("BROKERAGE_PER"),pm.get("status").alias("STATUS"),
					pm.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"),pm.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"),
					pm.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"),pm.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"),
					pm.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),mailStatus.alias("MAIL_STATUS"),
					pm.get("placementAmendId").alias("PLACEMENT_AMEND_ID"),pm.get("amendId").alias("AMEND_ID"),
					pm.get("contractNo").alias("CONTRACT_NO"),pm.get("layerNo").alias("LAYER_NO"),
					pm.get("sectionNo").alias("SECTION_NO"),pr.get("rskEpiEstOc").alias("EPI_AMOUNT"),de.get("rskExchangeRate").alias("EXCAHNGE_RATE")); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("proposalNo"), eproposalNo);
			Predicate n3 = cb.equal(pm.get("reinsurerId"), reinsurerId);
			Predicate n4 = cb.equal(pm.get("brokerId"), brokerId);
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);
			Predicate n6 = cb.equal(pr.get("rskProposalNumber"), pm.get("proposalNo"));
			Predicate n7 = cb.equal(pr.get("rskEndorsementNo"), endPr);
			Predicate n8 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
			Predicate n9 = cb.equal(de.get("rskEndorsementNo"), endDe);
			query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9);
			
			TypedQuery<Tuple> res = em.createQuery(query);
			 list = res.getResultList();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> GetPlacementSearchEdit(EditPlacingDetailsReq bean) {
		List<Tuple> list=null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
			Root<TtrnRiskProposal> pr = query.from(TtrnRiskProposal.class);
			Root<TtrnRiskDetails> de = query.from(TtrnRiskDetails.class);
			
			Subquery<Long> endPr = query.subquery(Long.class); 
			Root<TtrnRiskProposal> b = endPr.from(TtrnRiskProposal.class);
			endPr.select(cb.max(b.get("rskEndorsementNo")));
			endPr.where(cb.equal( b.get("rskProposalNumber"),pr.get("rskProposalNumber")));
			
			Subquery<Long> endDe = query.subquery(Long.class); 
			Root<TtrnRiskDetails> des = endDe.from(TtrnRiskDetails.class);
			endDe.select(cb.max(des.get("rskEndorsementNo")));
			endDe.where(cb.equal( des.get("rskProposalNumber"),de.get("rskProposalNumber")));
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> personal = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(personal.get("companyName"));
			Predicate b1 = cb.equal( pm.get("cedingCompanyId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pm.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( personal.get("customerType"), "C");
			cedingCompanyName.where(b1,b2,b3);
			
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(c1);
			
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reinsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			//maxAmend
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			bAmend.where(f1);
			
			Predicate e1 = cb.equal( pi1.get("customerType"), "B");
			Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
			brokerName.where(e1,e2,e3,e4);
			
			//mailStatus
			Subquery<String> mailStatus = query.subquery(String.class); 
			Root<MailNotificationDetail> mail = mailStatus.from(MailNotificationDetail.class);
			
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(mail.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(mail.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(mail.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(mail.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(mail.get("mailStatus"));
			Predicate g1 = cb.equal( pm.get("proposalNo"), mail.get("proposalNo"));
			Predicate g2 = cb.equal( pm.get("reinsurerId"), mail.get("reinsurerId"));
			Predicate g3 = cb.equal(pm.get("brokerId"), mail.get("brokerId"));
			Predicate g4 = cb.equal(pm.get("status"), mail.get("mailType"));
			Predicate g5 = cb.equal(pm.get("mailRecordNo"), mmaxAmend);
			mailStatus.where(g1,g2,g3,g4,g5);

			query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("shareOffered").alias("SHARE_OFFERED"),pm.get("shareWritten").alias("SHARE_WRITTEN"),
					pm.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),pm.get("shareSigned").alias("SHARE_SIGNED"),
					pm.get("brokerage").alias("BROKERAGE_PER"),pm.get("status").alias("STATUS"),
					pm.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"),pm.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"),
					pm.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"),pm.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"),
					pm.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),mailStatus.alias("MAIL_STATUS"),pr.get("rskEpiEstOc").alias("EPI_AMOUNT"),de.get("rskExchangeRate").alias("EXCAHNGE_RATE")); 
					
			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
			Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getSearchReinsurerId());
			Predicate n4 = cb.equal(pm.get("brokerId"), bean.getSearchBrokerId());
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);
			Predicate n6 = cb.equal(pm.get("status"), bean.getSearchStatus());
			Predicate n7 = cb.equal(pr.get("rskProposalNumber"), pm.get("proposalNo"));
			Predicate n8 = cb.equal(pr.get("rskEndorsementNo"), endPr);
			Predicate n9 = cb.equal(de.get("rskProposalNumber"), pr.get("rskProposalNumber"));
			Predicate n10 = cb.equal(de.get("rskEndorsementNo"), endDe);
			if(StringUtils.isNotBlank(bean.getBouquetNo())) {
				//GET_PLACEMENT_SEARCHBQ_EDIT
				Predicate n2 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
				query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10);
				
			}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
				//GET_PLACEMENT_SEARCHBP_EDIT
				Predicate n2 = cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo());
				query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10);
			}else {
				//GET_PLACEMENT_SEARCH_EDIT
				Predicate n2 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
				query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10);
			}
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> getStatusInfo(EditPlacingDetailsReq bean) {
		//GET_PLACEMENT_STATUS_EDIT
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnRiPlacementStatus> pm = query.from(TtrnRiPlacementStatus.class);

		query.multiselect(pm.get("newStatus").alias("NEW_STATUS"),pm.get("currentStatus").alias("CURRENT_STATUS")); 

		//status
		Subquery<Long> status = query.subquery(Long.class); 
		Root<TtrnRiPlacementStatus> pms = status.from(TtrnRiPlacementStatus.class);
		status.select(cb.max(pms.get("statusNo")));
		Predicate a1 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
		Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
		Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
		Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
		status.where(a1,a2,a3,a4);

		Predicate n1 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
		Predicate n2 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
		Predicate n4 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
		Predicate n5 = cb.equal(pm.get("statusNo"), status);
		query.where(n1,n2,n3,n4,n5);
		
		TypedQuery<Tuple> res = em.createQuery(query);
		List<Tuple> list = res.getResultList();
		return list;
	}

	@Override
	public List<Tuple> GetPlacementBouquet(String branchCode, String brokerId, String bouquetNo, String reinsurerId,String baseProposalNo ) {
		List<Tuple> list=null;
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);
			
			//cedingCompanyName
			Subquery<String> cedingCompanyName = query.subquery(String.class); 
			Root<PersonalInfo> personal = cedingCompanyName.from(PersonalInfo.class);
			cedingCompanyName.select(personal.get("companyName"));
			Predicate b1 = cb.equal( pm.get("cedingCompanyId"), personal.get("customerId"));
			Predicate b2 = cb.equal( pm.get("branchCode"), personal.get("branchCode"));
			Predicate b3 = cb.equal( personal.get("customerType"), "C");
			cedingCompanyName.where(b1,b2,b3);
			
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
		
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(c1);
			
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reinsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
		
			Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
			brokerName.select(cb.concat(firstName, pi1.get("lastName")));
			//maxAmend
			Subquery<Long> bAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
			bAmend.select(cb.max(pis1.get("amendId")));
			Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
			bAmend.where(f1);
			
			Predicate e1 = cb.equal( pi1.get("customerType"), "B");
			Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
			Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
			Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
			brokerName.where(e1,e2,e3,e4);
			
			//mailStatus
			Subquery<String> mailStatus = query.subquery(String.class); 
			Root<MailNotificationDetail> mail = mailStatus.from(MailNotificationDetail.class);
			
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(mail.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(mail.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(mail.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(mail.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(mail.get("mailStatus"));
			Predicate g1 = cb.equal( pm.get("proposalNo"), mail.get("proposalNo"));
			Predicate g2 = cb.equal( pm.get("reinsurerId"), mail.get("reinsurerId"));
			Predicate g3 = cb.equal(pm.get("brokerId"), mail.get("brokerId"));
			Predicate g4 = cb.equal(pm.get("status"), mail.get("mailType"));
			Predicate g5 = cb.equal(pm.get("mailRecordNo"), mmaxAmend);
			mailStatus.where(g1,g2,g3,g4,g5);

			query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
					pm.get("cedingCompanyId").alias("CEDING_COMPANY_ID"),cedingCompanyName.alias("CEDING_COMPANY_NAME"),
					pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
					reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"),
					pm.get("shareOffered").alias("SHARE_OFFERED"),pm.get("shareWritten").alias("SHARE_WRITTEN"),
					pm.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),pm.get("shareSigned").alias("SHARE_SIGNED"),
					pm.get("brokerage").alias("BROKERAGE_PER"),pm.get("status").alias("STATUS"),
					pm.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"),pm.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"),
					pm.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"),pm.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"),
					pm.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),mailStatus.alias("MAIL_STATUS"),
					pm.get("contractNo").alias("CONTRACT_NO"),pm.get("layerNo").alias("LAYER_NO"),pm.get("amendId").alias("AMEND_ID"),
					pm.get("sectionNo").alias("SECTION_NO")); 
					

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( pm.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( pm.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( pm.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);

			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n3 = cb.equal(pm.get("reinsurerId"), reinsurerId);
			Predicate n4 = cb.equal(pm.get("brokerId"), brokerId);
			Predicate n5 = cb.equal(pm.get("placementAmendId"), amend);

			if(StringUtils.isNotBlank(bouquetNo)) {
				//GET_PLACEMENT_BOUQUET
				Predicate n2 = cb.equal(pm.get("bouquetNo"), bouquetNo);
				query.where(n1,n2,n3,n4,n5);
			}else {
				//GET_PLACEMENT_BASELAYER
				Predicate n2 = cb.equal(pm.get("baseProposalNo"), baseProposalNo);
				query.where(n1,n2,n3,n4,n5);
			}
			
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Transactional
	public void updatePlacementStatus(UpdatePlacementListReq resp, UpdatePlacementReq bean) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		//UPDATE_PLACEMENT_STATUS_MAIL
		CriteriaUpdate<TtrnRiPlacementStatus> update2 = cb.createCriteriaUpdate(TtrnRiPlacementStatus.class);
		Root<TtrnRiPlacementStatus> ri = update2.from(TtrnRiPlacementStatus.class);
		
		// mailRecordNo
		Subquery<Long> mailRecordNo1 = update2.subquery(Long.class); 
		Root<MailNotificationDetail> mr1 = mailRecordNo1.from(MailNotificationDetail.class);
		mailRecordNo1.select(cb.max(mr1.get("mailRecordNo")));
		Predicate c2 = cb.equal( ri.get("proposalNo"), mr1.get("proposalNo"));
		Predicate c3 = cb.equal( ri.get("reinsurerId"), mr1.get("reinsurerId"));
		Predicate c4 = cb.equal( ri.get("brokerId"), mr1.get("brokerId"));
		//Predicate c5 = cb.equal(ri.get("mailType"), mr1.get("mailType"));
		mailRecordNo1.where(c2,c3,c4);
		
		update2.set("emailRecordid", mailRecordNo1==null?null:mailRecordNo1);
		
		//statusNo
		Subquery<Long> statusNo = update2.subquery(Long.class); 
		Root<TtrnRiPlacementStatus> ris = statusNo.from(TtrnRiPlacementStatus.class);
		statusNo.select(cb.max(ris.get("statusNo")));
		Predicate e1 = cb.equal( ri.get("branchCode"), ris.get("branchCode"));
		Predicate e2 = cb.equal( ri.get("proposalNo"), ris.get("proposalNo"));
		Predicate e3 = cb.equal( ri.get("reinsurerId"), ris.get("reinsurerId"));
		Predicate e4 = cb.equal( ri.get("brokerId"), ris.get("brokerId"));
		statusNo.where(e1,e2,e3,e4);
		
		Predicate d1 = cb.equal(ri.get("proposalNo"), resp.getProposalNo());
		Predicate d2 = cb.equal(ri.get("reinsurerId"), resp.getReinsurerId());
		Predicate d3 = cb.equal(ri.get("brokerId"),resp.getBrokerId());
		Predicate d4 = cb.equal(ri.get("branchCode"), bean.getBranchCode());
		Predicate d5 = cb.equal(ri.get("statusNo"), statusNo);
		update2.where(d1,d2,d3,d4,d5);
		em.createQuery(update2).executeUpdate();
	}

	@Transactional
	public void updateAttachementStatus(UpdatePlacementListReq resp, UpdatePlacementReq bean) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		//UPDATE_ATTACHMENT_MAIL	
		CriteriaUpdate<NotificationAttachmentDetail> update1 = cb.createCriteriaUpdate(NotificationAttachmentDetail.class);
		Root<NotificationAttachmentDetail> pm = update1.from(NotificationAttachmentDetail.class);
		
		// MAXAmend ID
		Subquery<Long> mailRecordNo = update1.subquery(Long.class); 
		Root<MailNotificationDetail> mr = mailRecordNo.from(MailNotificationDetail.class);
		mailRecordNo.select(cb.max(mr.get("mailRecordNo")));
		Predicate b2 = cb.equal( pm.get("proposalNo"), mr.get("proposalNo"));
		Predicate b3 = cb.equal( pm.get("reinsurerId"), mr.get("reinsurerId"));
		Predicate b4 = cb.equal( pm.get("brokerId"), mr.get("brokerId"));
		//Predicate b5 = cb.equal(pm.get("mailType"), mr.get("mailType"));
		mailRecordNo.where(b2,b3,b4);
		
		update1.set("mailRecordNo",mailRecordNo==null?null:mailRecordNo);
		update1.set("statusNo", StringUtils.isBlank(bean.getStatusNo())?null: new BigDecimal( bean.getStatusNo()));
		
		Predicate m1 = cb.equal(pm.get("proposalNo"), resp.getProposalNo());
		Predicate m2 = cb.equal(pm.get("reinsurerId"), resp.getReinsurerId());
		Predicate m3 = cb.equal(pm.get("brokerId"),resp.getBrokerId());
		Predicate m4 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		update1.where(m1,m2,m3,m4);
		em.createQuery(update1).executeUpdate();
	}

	@Transactional
	public void updatePlacement(UpdatePlacementListReq resp, UpdatePlacementReq bean, String mailType) {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaUpdate<TtrnRiPlacement> update = cb.createCriteriaUpdate(TtrnRiPlacement.class);
		// set the root class
		Root<TtrnRiPlacement> m = update.from(TtrnRiPlacement.class);
		// set update and where clause
		update.set("status", mailType);
		update.set("statusNo",new BigDecimal(bean.getStatusNo()));
		
		// MAXAmend ID
		Subquery<Long> amend = update.subquery(Long.class); 
		Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
		amend.select(cb.max(pms.get("placementAmendId")));
		Predicate a1 = cb.equal( m.get("branchCode"), pms.get("branchCode"));
		Predicate a2 = cb.equal( m.get("proposalNo"), pms.get("proposalNo"));
		Predicate a3 = cb.equal( m.get("reinsurerId"), pms.get("reinsurerId"));
		Predicate a4 = cb.equal( m.get("brokerId"), pms.get("brokerId"));
		amend.where(a1,a2,a3,a4);
		
		Predicate n1 = cb.equal(m.get("proposalNo"), resp.getProposalNo());
		Predicate n2 = cb.equal(m.get("reinsurerId"), resp.getReinsurerId());
		Predicate n3 = cb.equal(m.get("brokerId"),resp.getBrokerId());
		Predicate n4 = cb.equal(m.get("branchCode"), bean.getBranchCode());
		Predicate n5 = cb.equal(m.get("placementAmendId"), amend);
		update.where(n1,n2,n3,n4,n5);
		// perform update
		em.createQuery(update).executeUpdate();
	}

	@Transactional
	public void updateNotificationStatus(UpdatePlacementListReq resp, UpdatePlacementReq bean, String status) {
		//UPDATE_PLACEMENT_STATUS
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		// create update
		CriteriaUpdate<MailNotificationDetail> notify = cb.createCriteriaUpdate(MailNotificationDetail.class);
		// set the root class
		Root<MailNotificationDetail> mailnotify = notify.from(MailNotificationDetail.class);
		// set update and where clause
		notify.set("mailStatus", status);
		notify.set("updateDate",new Date());
		notify.set("statusNo",StringUtils.isBlank(bean.getStatusNo())?null:new BigDecimal(bean.getStatusNo()));
		
		// MAXAmend ID
		Subquery<Long> mnamend = notify.subquery(Long.class); 
		Root<MailNotificationDetail> mns = mnamend.from(MailNotificationDetail.class);
		mnamend.select(cb.max(mns.get("mailRecordNo")));
		Predicate mn1 = cb.equal( mailnotify.get("branchCode"), mns.get("branchCode"));
		Predicate mn2 = cb.equal( mailnotify.get("proposalNo"), mns.get("proposalNo"));
		Predicate mn3 = cb.equal( mailnotify.get("reinsurerId"), mns.get("reinsurerId"));
		Predicate mn4 = cb.equal( mailnotify.get("brokerId"), mns.get("brokerId"));
		//Predicate mb4 = cb.equal(mailnotify.get("mailType"), mns.get("mailType"));
		mnamend.where(mn1,mn2,mn3,mn4);
		
		Predicate pn1 = cb.equal(mailnotify.get("proposalNo"), resp.getProposalNo());
		Predicate pn2 = cb.equal(mailnotify.get("reinsurerId"), resp.getReinsurerId());
		Predicate pn3 = cb.equal(mailnotify.get("brokerId"),resp.getBrokerId());
		Predicate pn4 = cb.equal(mailnotify.get("branchCode"), bean.getBranchCode());
		//Predicate pn5 = cb.equal(mailnotify.get("mailType"), p.get("status"));
		Predicate pn5 = cb.equal(mailnotify.get("mailRecordNo"), mnamend);
		notify.where(pn1,pn2,pn3,pn4,pn5);
		// perform update
		em.createQuery(notify).executeUpdate();
		
	}

	@Override
	public List<Tuple> getPlacementViewList(GetPlacementViewListReq bean) {
		//GET_PLACEMENT_VIEW
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
		Root<TtrnRiPlacementStatus> pm = query.from(TtrnRiPlacementStatus.class);
		
		//reinsurerName
		Subquery<String> reinsurerName = query.subquery(String.class); 
		Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
	
		Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
		reinsurerName.select(cb.concat(firstName1, pi.get("lastName")));
		//maxAmend
		Subquery<Long> maxAmend = query.subquery(Long.class); 
		Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
		maxAmend.select(cb.max(pis.get("amendId")));
		Predicate c1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
		maxAmend.where(c1);
		
		Predicate d1 = cb.equal( pi.get("customerType"), "R");
		Predicate d2 = cb.equal( pi.get("customerId"), pm.get("reinsurerId"));
		Predicate d3 = cb.equal( pi.get("branchCode"), pm.get("branchCode"));
		Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
		reinsurerName.where(d1,d2,d3,d4);
		
		//brokerName
		Subquery<String> brokerName = query.subquery(String.class); 
		Root<PersonalInfo> pi1 = brokerName.from(PersonalInfo.class);
	
		Expression<String> firstName = cb.concat(pi1.get("firstName"), " ");
		brokerName.select(cb.concat(firstName, pi1.get("lastName")));
		//maxAmend
		Subquery<Long> bAmend = query.subquery(Long.class); 
		Root<PersonalInfo> pis1 = bAmend.from(PersonalInfo.class);
		bAmend.select(cb.max(pis1.get("amendId")));
		Predicate f1 = cb.equal( pis1.get("customerId"), pi1.get("customerId"));
		bAmend.where(f1);
		
		Predicate e1 = cb.equal( pi1.get("customerType"), "B");
		Predicate e2 = cb.equal( pi1.get("customerId"), pm.get("brokerId"));
		Predicate e3 = cb.equal( pi1.get("branchCode"), pm.get("branchCode"));
		Predicate e4 = cb.equal( pi1.get("amendId"), bAmend);
		brokerName.where(e1,e2,e3,e4);
		
		//currentStatus
		Subquery<String> currentStatus = query.subquery(String.class); 
		Root<StatusMaster> mail = currentStatus.from(StatusMaster.class);
		currentStatus.select(mail.get("statusName"));
		Predicate g1 = cb.equal( pm.get("currentStatus"), mail.get("statusCode"));
		Predicate g2 = cb.equal( pm.get("branchCode"), mail.get("branchCode"));
		currentStatus.where(g1,g2);
		
		//newStatus
		Subquery<String> newStatus = query.subquery(String.class); 
		Root<StatusMaster> mail1 = newStatus.from(StatusMaster.class);
		newStatus.select(mail1.get("statusName"));
		Predicate h1 = cb.equal( pm.get("newStatus"), mail1.get("statusCode"));
		Predicate h2 = cb.equal( pm.get("branchCode"), mail1.get("branchCode"));
		newStatus.where(h1,h2);
		
		//emailBy
		Subquery<String> emailBy = query.subquery(String.class); 
		Root<ConstantDetail> email = emailBy.from(ConstantDetail.class);
		emailBy.select(email.get("detailName"));
		Predicate j1 = cb.equal( email.get("categoryId"), "55");
		Predicate j2 = cb.equal( email.get("type"), pm.get("emailBy"));
		emailBy.where(j1,j2);

		query.multiselect(pm.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),pm.get("sno").alias("SNO"),
				pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("proposalNo").alias("PROPOSAL_NO"),
				pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
				reinsurerName.alias("REINSURER_NAME"),brokerName.alias("BROKER_NAME"), currentStatus.alias("CURRENT_STATUS"),
				newStatus.alias("NEW_STATUS"),pm.get("updateDate").alias("UPDATE_DATE"),
				emailBy.alias("EMAIL_BY"),pm.get("newStatus").alias("STATUS"),
				pm.get("correspondentId").alias("CORRESPONDENT_ID")); 

		
		List<Order> orderList = new ArrayList<Order>();
		orderList.add(cb.desc(pm.get("amendId")));

		Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		Predicate n2 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
		Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
		Predicate n4 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
		query.where(n1,n2,n3,n4).orderBy(orderList);
		
		TypedQuery<Tuple> result = em.createQuery(query);
		List<Tuple> list = result.getResultList();
		return list;
	}

	@Override
	public List<Tuple> getPlacementView(GetPlacementViewReq bean) {
		//GET_PLACEMENT_STATUS_VIEW
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		Root<TtrnRiPlacementStatus> pm = query.from(TtrnRiPlacementStatus.class);
		query.multiselect(pm.get("amendId").alias("AMEND_ID"),pm.get("emailBy").alias("EMAIL_BY"),
				pm.get("currentStatus").alias("CURRENT_STATUS"),pm.get("proposalNo").alias("PROPOSAL_NO"),
				pm.get("reinsurerId").alias("REINSURER_ID"),pm.get("brokerId").alias("BROKER_ID"),
				pm.get("cedentCorrespondence").alias("CEDENT_CORRESPONDENCE"),pm.get("updateDate").alias("UPDATE_DATE"),
				pm.get("newStatus").alias("NEW_STATUS"),pm.get("tqrCorrespondence").alias("TQR_CORRESPONDENCE"),
				pm.get("reinsurerCorrespondence").alias("REINSURER_CORRESPONDENCE")); 
	
		Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
		Predicate n2 = cb.equal(pm.get("proposalNo"), bean.getEproposalNo());
		Predicate n3 = cb.equal(pm.get("reinsurerId"), bean.getReinsurerId());
		Predicate n4 = cb.equal(pm.get("brokerId"), bean.getBrokerId());
		Predicate n5 = cb.equal(pm.get("newStatus"), bean.getNewStatus());
		query.where(n1,n2,n3,n4,n5);
		
		TypedQuery<Tuple> result = em.createQuery(query);
		List<Tuple> list = result.getResultList();
		return list;
	}

	@Override
	public List<Tuple> MailproposalInfo(GetMailTemplateReq bean) {
		List<Tuple> list=null;
		try {

			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiskDetails> rd = query.from(TtrnRiskDetails.class); //'New' POLICY_STATUS,''EXISTING_SHARE
			Root<TtrnRiPlacement> trp = query.from(TtrnRiPlacement.class);
			// companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			companyName.select(pms.get("companyName"));
			Predicate a1 = cb.equal( rd.get("rskCedingid"), pms.get("customerId"));
			Predicate a2 = cb.equal( pms.get("customerType"), "C");
			Predicate a3 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			companyName.where(a1,a2,a3);
			
			//BUSINESS_TYPE
			Subquery<String> businessType = query.subquery(String.class); 
			Root<TmasProductMaster> pd = businessType.from(TmasProductMaster.class);
			businessType.select(pd.get("tmasProductName"));
			Predicate b1 = cb.equal(pd.get("tmasProductId"),  pm.get("productId"));
			Predicate b2 = cb.equal(pd.get("branchCode"),  pm.get("branchCode"));
			businessType.where(b1,b2);
			
			//treatyType
			Subquery<String> treatyType = query.subquery(String.class); 
			Root<ConstantDetail> cd = treatyType.from(ConstantDetail.class);
			treatyType.select(cd.get("detailName"));
			Predicate c1 =  cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,new BigDecimal(43) ).otherwise(new BigDecimal(29)) ,
					  cd.get("categoryId"));
			Predicate c2 = cb.equal(cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,rd.get("treatytype") ).otherwise(rd.get("rskBusinessType")) ,
					  cd.get("type"));	
			treatyType.where(c1,c2);
			
			//CLASS
			Subquery<String> deptName = query.subquery(String.class); 
			Root<TmasDepartmentMaster> dm = deptName.from(TmasDepartmentMaster.class);
			deptName.select(dm.get("tmasDepartmentName"));
			Predicate d1 = cb.equal(dm.get("tmasDepartmentId"),  rd.get("rskDeptid"));
			Predicate d2 = cb.equal(dm.get("branchCode"),  rd.get("branchCode"));
			Predicate d3 = cb.equal(dm.get("tmasProductId"),  rd.get("rskProductid"));
			Predicate d4 = cb.equal(dm.get("tmasStatus"), "Y");
			deptName.where(d1,d2,d3,d4);
			
			//status
			Subquery<Long> status = query.subquery(Long.class); 
			Root<TtrnRiPlacement> TRP1 = status.from(TtrnRiPlacement.class);
			status.select(cb.max(TRP1.get("statusNo")));
			Predicate e1 = cb.equal( TRP1.get("proposalNo"), trp.get("proposalNo"));
			Predicate e2 = cb.equal( TRP1.get("sno"), trp.get("sno"));
			Predicate e3 = cb.equal( TRP1.get("branchCode"), trp.get("branchCode"));
			status.where(e1,e2,e3);
	
			query.multiselect(rd.get("rskInceptionDate").alias("INS_DATE"),rd.get("rskExpiryDate").alias("EXP_DATE"),
					companyName.alias("COMPANY_NAME"),pm.get("uwYear").alias("UW_YEAR"),
					pm.get("uwYearTo").alias("UW_YEAR_TO"),pm.get("contractNo").alias("CONTRACT_NO"),
					pm.get("baseLayer").alias("BASE_LAYER"),pm.get("layerNo").alias("LAYER_NO"),businessType.alias("BUSINESS_TYPE"),
					pm.get("proposalNo").alias("PROPOSAL_NO"),treatyType.alias("TREATY_TYPE"),rd.get("rskTreatyid").alias("RSK_TREATYID"),
					pm.get("bouquetNo").alias("BOUQUET_NO"),pm.get("bouquetModeYn").alias("BOUQUET_MODE_YN"),
					deptName.alias("CLASS"),rd.get("rskSpfcid").alias("SUB_CLASS"),
					cb.selectCase().when(cb.equal(pm.get("productId") ,"2") ,pm.get("sectionNo"))
					.otherwise(pm.get("layerNo")).alias("SECTION_NO"), 	pm.get("offerNo").alias("OFFER_NO"),
					pm.get("productId").alias("PRODUCT_ID"), trp.get("shareOffered").alias("SHARE_OFFERED"),
					trp.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"),
					trp.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED")).distinct(true); 
	
			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("productId")));
			orderList.add(cb.asc(pm.get("proposalNo")));
			orderList.add(cb.asc(pm.get("bouquetNo")));
	
			
			if(StringUtils.isBlank(bean.getSearchType())) {
				// Where
				Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
				Predicate n2 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
			
				Predicate n4 = cb.equal(trp.get("reinsurerId"), bean.getReinsurerId()); 
				Predicate n5 = cb.equal(trp.get("brokerId"), bean.getBrokerId()); 
				Predicate n6 = cb.equal(pm.get("contractStatus"), "P");
				Predicate n7 = cb.equal(trp.get("proposalNo"), pm.get("proposalNo")); 
				
				Expression<String> e0 = trp.get("statusNo");
	      		Predicate n8 = e0.in(status);
				
				if("C".equals(bean.getPlacementMode())) {
					if(StringUtils.isNotBlank(bean.getBouquetNo())) {
						//GET_MAILTEPLATE_BOUQUET
						Predicate n3 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
						query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
					}else {
						//GET_MAILTEPLATE_BASELAYER
						Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), bean.getBaseProposalNo());
						query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
					}
				}else {
					//GET_MAILTEPLATE_PROPOSAL
					Predicate n3 = cb.equal(pm.get("proposalNo"), bean.getProposalNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8).orderBy(orderList);
				}
			}else {
				// Where
				Predicate n1 = cb.equal(pm.get("branchCode"), bean.getBranchCode());
				Predicate n2 = cb.equal(pm.get("proposalNo"), rd.get("rskProposalNumber"));
				Predicate n4 = cb.equal(trp.get("reinsurerId"), bean.getSearchReinsurerId()); 
				Predicate n5 = cb.equal(trp.get("brokerId"), bean.getSearchBrokerId()); 
				Predicate n6 = cb.equal(pm.get("contractStatus"), "P");
				Predicate n7 = cb.equal(trp.get("proposalNo"), pm.get("proposalNo")); 
				Expression<String> e0 = trp.get("statusNo");
	      		Predicate n8 = e0.in(status);
				Predicate n9 = cb.equal(trp.get("status"), bean.getNewStatus()); 
				
				if(StringUtils.isNotBlank(bean.getBouquetNo())) {
					//GET_MAILTEPLATE_BOUQUET_SEARCH
					Predicate n3 = cb.equal(pm.get("bouquetNo"), bean.getBouquetNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9).orderBy(orderList);
				}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
					//GET_MAILTEPLATE_BASELAYER_SEARCH
					Predicate n3 = cb.equal(cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")), bean.getBaseProposalNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9).orderBy(orderList);
				}else {
					//GET_MAILTEPLATE_PROPOSAL_SEARCH
					Predicate n3 = cb.equal(pm.get("proposalNo"), bean.getProposalNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9).orderBy(orderList);
				}
			}
			TypedQuery<Tuple> res = em.createQuery(query);
			list = res.getResultList();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

	

	@Override
	public List<Tuple> getMailAttachList(SendMailReq bean) {
		List<Tuple> list=null;
		try {
		//GET_EX_DOC_PRO_LIST
		CriteriaBuilder cb = em.getCriteriaBuilder(); 
		CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
		Root<NotificationAttachmentDetail> pm = query.from(NotificationAttachmentDetail.class);
					
		
		
		query.multiselect(pm.get("docId").alias("DOC_ID"),
				pm.get("docDescription").alias("DOC_DESCRIPTION"),pm.get("orgFileName").alias("ORG_FILE_NAME")
				,pm.get("ourFileName").alias("OUR_FILE_NAME")).distinct(true); 
		List<Predicate> predicates = new ArrayList<>();
		
		predicates.add(cb.equal(pm.get("branchCode"), bean.getBranchCode()));
		
		//Expression<String> e0 = pm.get("docType");
		//Predicate n2 = e0.in("MA","MA2").not();

		
		predicates.add(cb.equal(pm.get("reinsurerId"), bean.getReinsurerId()));
		predicates.add(cb.equal(pm.get("brokerId"), bean.getBrokerId()));
		predicates.add(cb.equal(pm.get("correspondentId"), bean.getCorresId()));
		if(StringUtils.isNotBlank(bean.getBouquetNo()) && !"0".equals(bean.getBouquetNo())) {
			predicates.add(cb.equal(pm.get("bouquetNo"), bean.getBouquetNo()));
		}else if(StringUtils.isNotBlank(bean.getBaseProposalNo())) {
			predicates.add(cb.equal(pm.get("baseProposalNo"), bean.getBaseProposalNo()));
		}else {
			predicates.add(cb.equal(pm.get("proposalNo"), StringUtils.isBlank(bean.getEproposalNo())?bean.getProposalNo():bean.getEproposalNo()));
		}
		query.where(predicates.toArray(new Predicate[0]));
		
		TypedQuery<Tuple> result = em.createQuery(query);
		list = result.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<Tuple> getReinsurerInfoNotin(String branchCode, String eproposalNo) {
		List<Tuple> list=new ArrayList<>();
		try {
			//GET_REINSURER_INFO_NOTIN left join
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);

			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate a1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate a2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate a3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate a7 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(a1,a2,a3,a7);

			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> ocpm2 = mailStatus.from(MailNotificationDetail.class);
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(ocpm2.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(ocpm2.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(ocpm2.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(ocpm2.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(ocpm2.get("mailStatus"));
			Predicate a4 = cb.equal(ocpm2.get("proposalNo"), p.get("proposalNo"));
			Predicate a5 = cb.equal(ocpm2.get("reinsurerId"), p.get("reinsurerId"));
			Predicate a6 = cb.equal(ocpm2.get("brokerId"), p.get("brokerId"));
			Predicate a8 = cb.equal(ocpm2.get("mailType"), p.get("status"));
			Predicate a9 = cb.equal(ocpm2.get("mailRecordNo"),mmaxAmend);
			mailStatus.where(a4,a5,a6,a8,a9);

			query.multiselect(
				 p.get("sno").alias("SNO"), 
				 p.get("reinsurerId").alias("REINSURER_ID"),
				 p.get("brokerId").alias("BROKER_ID"),
				 p.get("shareOffered").alias("SHARE_OFFERED") ,
				 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS"),
				 p.get("proposalNo").alias("PROPOSAL_NO") ,
				 p.get("bouquetNo").alias("BOUQUET_NO") ,
				 p.get("placementMode").alias("PLACEMENT_MODE")
				).distinct(true);

			Predicate n1 = cb.equal(p.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(p.get("proposalNo"),eproposalNo);
			Predicate n3 = cb.equal(p.get("placementAmendId"), amendId);

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(p.get("sno")));
			orderList.add(cb.asc(p.get("bouquetNo")));
			orderList.add(cb.asc(p.get("proposalNo")));
				
			query.where(n1,n2,n3).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getReinsurerInfoBouquetNotin(String branchCode, String bouquetNo) {
		List<Tuple> list=new ArrayList<>();
		try {
			
			//get_Reinsurer_Info_Bouquet_Notin left join
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);
			
			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> ocpm2 = mailStatus.from(MailNotificationDetail.class);
			
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(ocpm2.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(ocpm2.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(ocpm2.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(ocpm2.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(ocpm2.get("mailStatus"));
			Predicate a4 = cb.equal(ocpm2.get("proposalNo"), p.get("proposalNo"));
			Predicate a5 = cb.equal(ocpm2.get("reinsurerId"), p.get("reinsurerId"));
			Predicate a6 = cb.equal(ocpm2.get("brokerId"), p.get("brokerId"));
			Predicate a8 = cb.equal(ocpm2.get("mailType"), p.get("status"));
			Predicate a9 = cb.equal(ocpm2.get("mailRecordNo"), mmaxAmend);
			
			mailStatus.where(a4,a5,a6,a8,a9);

			query.multiselect( 
					p.get("sno").alias("SNO"), 
					 p.get("reinsurerId").alias("REINSURER_ID"),
					 p.get("brokerId").alias("BROKER_ID"),
					 p.get("shareOffered").alias("SHARE_OFFERED") ,
					 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS"),
					 p.get("bouquetNo").alias("BOUQUET_NO") ,
					// p.get("proposalNo").alias("PROPOSAL_NO") ,
					 p.get("placementMode").alias("PLACEMENT_MODE")).distinct(true);
			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate a1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate a2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate a3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate a7 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(a1,a2,a3,a7);			

			Predicate n1 = cb.equal(p.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(p.get("bouquetNo"),bouquetNo);
			Predicate n3 = cb.equal(p.get("placementAmendId"), amendId);

			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(p.get("sno")));
			orderList.add(cb.asc(p.get("bouquetNo")));
				
			query.where(n1,n2,n3).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getPlacementBouquetList(GetPlacementInfoListReq req) {
		List<Tuple> list=new ArrayList<>();
		try {  
			//GET_PLACEMENT_BOUQUET_LIST--LEFT JOIN
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);

			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> m = mailStatus.from(MailNotificationDetail.class);
			mailStatus.select(m.get("mailStatus"));
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(m.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(m.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(m.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(m.get("mailType"), cm.get("mailType"));
			Predicate mb5 = cb.equal(m.get("sno"), cm.get("sno"));
			mmaxAmend.where(mb1,mb2,mb3,mb4,mb5);
			
			Predicate b1 = cb.equal(m.get("proposalNo"), p.get("proposalNo"));
			Predicate b2 = cb.equal(m.get("reinsurerId"), p.get("reinsurerId"));
			Predicate b3 = cb.equal(m.get("brokerId"), p.get("brokerId"));
			Predicate b4 = cb.equal(m.get("mailType"), p.get("status"));
			Predicate b5 = cb.equal(m.get("mailRecordNo"),mmaxAmend);
			mailStatus.where(b1,b2,b3,b4,b5);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			companyName.select(pms.get("companyName"));
			//maxAmend
			Subquery<Long> cmaxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> cpis = cmaxAmend.from(PersonalInfo.class);
			cmaxAmend.select(cb.max(cpis.get("amendId")));
			Predicate ce1 = cb.equal( cpis.get("customerId"), pms.get("customerId"));
			cmaxAmend.where(ce1);
			Predicate c1 = cb.equal( p.get("cedingCompanyId"), pms.get("customerId"));
			Predicate c2 = cb.equal( pms.get("customerType"), "C");
			Predicate c3 = cb.equal( p.get("branchCode"), pms.get("branchCode"));
			Predicate c4 = cb.equal( pms.get("amendId"), cmaxAmend);
			companyName.where(c1,c2,c3,c4);
			
			//reinsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate e1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(e1);
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), p.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), p.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> bpi = brokerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(bpi.get("firstName"), " ");
			brokerName.select(cb.concat(e0, bpi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pinfo = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pinfo.get("amendId")));
			Predicate g1 = cb.equal( pinfo.get("customerId"), bpi.get("customerId"));
			maxAmend1.where(g1);
			Predicate a1 = cb.equal( bpi.get("customerType"), "B");
			Predicate a2 = cb.equal( bpi.get("customerId"), p.get("brokerId"));
			Predicate a3 = cb.equal( bpi.get("branchCode"), p.get("branchCode"));
			Predicate a4 = cb.equal( bpi.get("amendId"), maxAmend1);
			brokerName.where(a1,a2,a3,a4);
			
			//status
			Subquery<String> status = query.subquery(String.class); 
			Root<StatusMaster> sm = status.from(StatusMaster.class);
			status.select(sm.get("statusName"));
			Predicate h1 = cb.equal(sm.get("statusCode"), p.get("status"));
			Predicate h2 = cb.equal(sm.get("branchCode"), p.get("branchCode"));
			status.where(h1,h2);
			
			//offerNo
			Subquery<String> offer = query.subquery(String.class); 
			Root<PositionMaster> pm = offer.from(PositionMaster.class);
			offer.select(pm.get("offerNo"));
			Subquery<Long> pmmaxAmend = query.subquery(Long.class); 
			Root<PositionMaster> pms1 = pmmaxAmend.from(PositionMaster.class);
			pmmaxAmend.select(cb.max(pms1.get("amendId")));
			Predicate pme1 = cb.equal( pms1.get("proposalNo"), pm.get("proposalNo"));
			pmmaxAmend.where(pme1);
			Predicate i1 = cb.equal(pm.get("proposalNo"), p.get("proposalNo"));
			Predicate i2 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
			Predicate i3 = cb.equal(pm.get("amendId"), pmmaxAmend);
			offer.where(i1,i2,i3);

			query.multiselect( 
					 p.get("sno").alias("SNO"), 
					 p.get("bouquetNo").alias("BOUQUET_NO"),
					 p.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),
					 p.get("proposalNo").alias("PROPOSAL_NO") ,
					 companyName.alias("CEDING_COMPANY_ID"),
					 p.get("reinsurerId").alias("REINSURER_ID"),
					 p.get("brokerId").alias("BROKER_ID"),
					 reInsurerName.alias("REINSURER_NAME"),
					 brokerName.alias("BROKER_NAME"),
					 p.get("shareOffered").alias("SHARE_OFFERED"), 
					 p.get("shareWritten").alias("SHARE_WRITTEN"), 
					 p.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"), 
					 p.get("shareSigned").alias("SHARE_SIGNED"), 
					 p.get("brokerage").alias("BROKERAGE_PER"), 
					 status.alias("STATUS"),
					 p.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"), 
					 p.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"), 
					 p.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"), 
					 p.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"), 
					 p.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),
					 cb.selectCase().when(cb.equal(p.get("approverStatus"),"P"), "Pending").when(cb.equal(p.get("approverStatus"),"A"), "Approved").when(cb.equal(p.get("approverStatus"),"R"), "Rejected").otherwise("Success").alias("APPROVER_STATUS"), 
					 offer.alias("OFFER_NO"),
					 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS")
				//	 mailStatus.alias("MAIL_STATUS")
					 );
			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate j1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate j2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate j3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate j4 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(j1,j2,j3,j4);
					
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("branchCode"), req.getBranchCode()));
			predicates.add(cb.equal(p.get("bouquetNo"),req.getBouquetNo()));
			predicates.add(cb.equal(p.get("placementAmendId"), amendId));
			
	
			if(StringUtils.isNotBlank(req.getSearchType())) {
				predicates.add(cb.equal(p.get("reinsurerId"), req.getSearchReinsurerId()));
				predicates.add(cb.equal(p.get("brokerId"), req.getSearchBrokerId()));
				predicates.add(cb.equal(p.get("status"), req.getSearchStatus()));
				predicates.add(cb.notEqual(p.get("approverStatus"), "P"));
			}
			
//			List<Order> orderList = new ArrayList<Order>();
//			orderList.add(cb.asc(offer));
//			orderList.add(cb.asc(p.get("baseProposalNo")));
//			orderList.add(cb.asc(p.get("proposalNo")));
//			orderList.add(cb.asc(p.get("sno")));
				
			query.where(predicates.toArray(new Predicate[0]));

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getPlacementBaseList(GetPlacementInfoListReq req) {
		List<Tuple> list=new ArrayList<>();
		try {  
			//GET_PLACEMENT_BASE_LIST--LEFT JOIN
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);

			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> m = mailStatus.from(MailNotificationDetail.class);
			mailStatus.select(m.get("mailStatus"));
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(m.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(m.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(m.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(m.get("mailType"), cm.get("mailType"));
			Predicate mb5 = cb.equal(m.get("sno"), cm.get("sno"));
			mmaxAmend.where(mb1,mb2,mb3,mb4,mb5);
			
			Predicate b1 = cb.equal(m.get("proposalNo"), p.get("proposalNo"));
			Predicate b2 = cb.equal(m.get("reinsurerId"), p.get("reinsurerId"));
			Predicate b3 = cb.equal(m.get("brokerId"), p.get("brokerId"));
			Predicate b4 = cb.equal(m.get("mailType"), p.get("status"));
			Predicate b5 = cb.equal(m.get("mailRecordNo"),mmaxAmend);
			mailStatus.where(b1,b2,b3,b4,b5);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			companyName.select(pms.get("companyName"));
			//maxAmend
			Subquery<Long> cmaxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> cpis = cmaxAmend.from(PersonalInfo.class);
			cmaxAmend.select(cb.max(cpis.get("amendId")));
			Predicate ce1 = cb.equal( cpis.get("customerId"), pms.get("customerId"));
			cmaxAmend.where(ce1);
			Predicate c1 = cb.equal( p.get("cedingCompanyId"), pms.get("customerId"));
			Predicate c2 = cb.equal( pms.get("customerType"), "C");
			Predicate c3 = cb.equal( p.get("branchCode"), pms.get("branchCode"));
			Predicate c4 = cb.equal( pms.get("amendId"), cmaxAmend);
			companyName.where(c1,c2,c3,c4);
			
			//reinsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate e1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(e1);
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), p.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), p.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> bpi = brokerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(bpi.get("firstName"), " ");
			brokerName.select(cb.concat(e0, bpi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pinfo = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pinfo.get("amendId")));
			Predicate g1 = cb.equal( pinfo.get("customerId"), bpi.get("customerId"));
			maxAmend1.where(g1);
			Predicate a1 = cb.equal( bpi.get("customerType"), "B");
			Predicate a2 = cb.equal( bpi.get("customerId"), p.get("brokerId"));
			Predicate a3 = cb.equal( bpi.get("branchCode"), p.get("branchCode"));
			Predicate a4 = cb.equal( bpi.get("amendId"), maxAmend1);
			brokerName.where(a1,a2,a3,a4);
			
			//status
			Subquery<String> status = query.subquery(String.class); 
			Root<StatusMaster> sm = status.from(StatusMaster.class);
			status.select(sm.get("statusName"));
			Predicate h1 = cb.equal(sm.get("statusCode"), p.get("status"));
			Predicate h2 = cb.equal(sm.get("branchCode"), p.get("branchCode"));
			status.where(h1,h2);
			
			//offerNo
			Subquery<String> offer = query.subquery(String.class); 
			Root<PositionMaster> pm = offer.from(PositionMaster.class);
			offer.select(pm.get("offerNo"));
			//maxAmend
			Subquery<Long> pmmaxAmend = query.subquery(Long.class); 
			Root<PositionMaster> pms1 = pmmaxAmend.from(PositionMaster.class);
			pmmaxAmend.select(cb.max(pms1.get("amendId")));
			Predicate pme1 = cb.equal( pms1.get("proposalNo"), pm.get("proposalNo"));
			pmmaxAmend.where(pme1);
			Predicate i1 = cb.equal(pm.get("proposalNo"), p.get("proposalNo"));
			Predicate i2 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
			Predicate i3 = cb.equal(pm.get("amendId"), pmmaxAmend);
			offer.where(i1,i2,i3);

			query.multiselect( 
					 p.get("sno").alias("SNO"), 
					 p.get("bouquetNo").alias("BOUQUET_NO"),
					 p.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),
					 p.get("proposalNo").alias("PROPOSAL_NO") ,
					 companyName.alias("CEDING_COMPANY_ID"),
					 p.get("reinsurerId").alias("REINSURER_ID"),
					 p.get("brokerId").alias("BROKER_ID"),
					 reInsurerName.alias("REINSURER_NAME"),
					 brokerName.alias("BROKER_NAME"),
					 p.get("shareOffered").alias("SHARE_OFFERED"), 
					 p.get("shareWritten").alias("SHARE_WRITTEN"), 
					 p.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"), 
					 p.get("shareSigned").alias("SHARE_SIGNED"), 
					 p.get("brokerage").alias("BROKERAGE_PER"), 
					 status.alias("STATUS"),
					 p.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"), 
					 p.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"), 
					 p.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"), 
					 p.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"), 
					 p.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"),
					 cb.selectCase().when(cb.equal(p.get("approverStatus"),"P"), "Pending").when(cb.equal(p.get("approverStatus"),"A"), "Approved").when(cb.equal(p.get("approverStatus"),"R"), "Rejected").otherwise("Success").alias("APPROVER_STATUS"), 
					 offer.alias("OFFER_NO"),
					 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS")
					 );
			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate j1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate j2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate j3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate j4 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(j1,j2,j3,j4);
					
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("branchCode"), req.getBranchCode()));
			predicates.add(cb.equal(p.get("baseProposalNo"),req.getBaseProposalNo()));
			predicates.add(cb.equal(p.get("placementAmendId"), amendId));
			
	
			if(StringUtils.isNotBlank(req.getSearchType())) {
				predicates.add(cb.equal(p.get("reinsurerId"), req.getSearchReinsurerId()));
				predicates.add(cb.equal(p.get("brokerId"), req.getSearchBrokerId()));
				predicates.add(cb.equal(p.get("status"), req.getSearchStatus()));
				predicates.add(cb.notEqual(p.get("approverStatus"), "P"));
			}
			
//			List<Order> orderList = new ArrayList<Order>();
//			orderList.add(cb.asc(offer));
//			orderList.add(cb.asc(p.get("baseProposalNo")));
//			orderList.add(cb.asc(p.get("proposalNo")));
//			orderList.add(cb.asc(p.get("sno")));
				
			query.where(predicates.toArray(new Predicate[0]));

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getPlacementList(GetPlacementInfoListReq req, String prop) {
		List<Tuple> list=new ArrayList<>();
		try {  
			//GET_PLACEMENT_LIST--LEFT JOIN
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);

			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			companyName.select(pms.get("companyName"));
			Predicate c1 = cb.equal( p.get("cedingCompanyId"), pms.get("customerId"));
			Predicate c2 = cb.equal( pms.get("customerType"), "C");
			Predicate c3 = cb.equal( p.get("branchCode"), pms.get("branchCode"));
			companyName.where(c1,c2,c3);
			
			//reinsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate e1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(e1);
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), p.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), p.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> bpi = brokerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(bpi.get("firstName"), " ");
			brokerName.select(cb.concat(e0, bpi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pinfo = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pinfo.get("amendId")));
			Predicate g1 = cb.equal( pinfo.get("customerId"), bpi.get("customerId"));
			maxAmend1.where(g1);
			Predicate a1 = cb.equal( bpi.get("customerType"), "B");
			Predicate a2 = cb.equal( bpi.get("customerId"), p.get("brokerId"));
			Predicate a3 = cb.equal( bpi.get("branchCode"), p.get("branchCode"));
			Predicate a4 = cb.equal( bpi.get("amendId"), maxAmend1);
			brokerName.where(a1,a2,a3,a4);
			
			//status
			Subquery<String> status = query.subquery(String.class); 
			Root<StatusMaster> sm = status.from(StatusMaster.class);
			status.select(sm.get("statusName"));
			Predicate h1 = cb.equal(sm.get("statusCode"), p.get("status"));
			Predicate h2 = cb.equal(sm.get("branchCode"), p.get("branchCode"));
			status.where(h1,h2);
			
			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> m = mailStatus.from(MailNotificationDetail.class);
			
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(m.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(m.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(m.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(m.get("mailType"), cm.get("status"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(m.get("mailStatus"));
			Predicate b1 = cb.equal(m.get("proposalNo"), p.get("proposalNo"));
			Predicate b2 = cb.equal(m.get("reinsurerId"), p.get("reinsurerId"));
			Predicate b3 = cb.equal(m.get("brokerId"), p.get("brokerId"));
			Predicate b4 = cb.equal(m.get("mailType"), p.get("status"));
			Predicate b5 = cb.equal(m.get("mailRecordNo"), mmaxAmend);
			mailStatus.where(b1,b2,b3,b4,b5);				

			//offerNo
			Subquery<String> offer = query.subquery(String.class); 
			Root<PositionMaster> pm = offer.from(PositionMaster.class);
			offer.select(pm.get("offerNo"));
			
			//maxAmend
			Subquery<Long> pmmaxAmend = query.subquery(Long.class); 
			Root<PositionMaster> pms1 = pmmaxAmend.from(PositionMaster.class);
			pmmaxAmend.select(cb.max(pms1.get("amendId")));
			Predicate pme1 = cb.equal( pms1.get("proposalNo"), pm.get("proposalNo"));
			pmmaxAmend.where(pme1);
			
			Predicate i1 = cb.equal(pm.get("proposalNo"), p.get("proposalNo"));
			Predicate i2 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
			Predicate i3 = cb.equal(pm.get("amendId"), pmmaxAmend);
			offer.where(i1,i2,i3);
			
			query.multiselect( 
					 p.get("sno").alias("SNO"), 
					 p.get("bouquetNo").alias("BOUQUET_NO"),
					 p.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),
					 p.get("proposalNo").alias("PROPOSAL_NO") ,
					 companyName.alias("CEDING_COMPANY_ID"),
					 p.get("reinsurerId").alias("REINSURER_ID"),
					 p.get("brokerId").alias("BROKER_ID"),
					 reInsurerName.alias("REINSURER_NAME"),
					 brokerName.alias("BROKER_NAME"),
					 p.get("shareOffered").alias("SHARE_OFFERED"), 
					 p.get("shareWritten").alias("SHARE_WRITTEN"), 
					 p.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"), 
					 p.get("shareSigned").alias("SHARE_SIGNED"), 
					 p.get("brokerage").alias("BROKERAGE_PER"), 
					 status.alias("STATUS"),
					 p.get("writtenLineValidity").alias("WRITTEN_LINE_VALIDITY"), 
					 p.get("writtenLineRemarks").alias("WRITTEN_LINE_REMARKS"), 
					 p.get("shareLineValidity").alias("SHARE_LINE_VALIDITY"), 
					 p.get("shareLineRemarks").alias("SHARE_LINE_REMARKS"), 
					 p.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"), 
					 cb.selectCase().when(cb.equal(p.get("approverStatus"),"P"), "Pending").when(cb.equal(p.get("approverStatus"),"A"), "Approved").when(cb.equal(p.get("approverStatus"),"R"), "Rejected").otherwise("Success").alias("APPROVER_STATUS"), 
					 offer.alias("OFFER_NO"),
					 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS")
					 );
			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate j1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate j2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate j3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate j4 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(j1,j2,j3,j4);
					
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("branchCode"), req.getBranchCode()));
			predicates.add(cb.equal(p.get("proposalNo"), prop));
			predicates.add(cb.equal(p.get("placementAmendId"), amendId));
			
	
			if(StringUtils.isNotBlank(req.getSearchType())) {
				predicates.add(cb.equal(p.get("reinsurerId"), req.getSearchReinsurerId()));
				predicates.add(cb.equal(p.get("brokerId"), req.getSearchBrokerId()));
				predicates.add(cb.equal(p.get("status"), req.getSearchStatus()));
				predicates.add(cb.notEqual(p.get("approverStatus"), "P"));
			}
			
//			List<Order> orderList = new ArrayList<Order>();
//			orderList.add(cb.asc(offer));
//			orderList.add(cb.asc(p.get("baseProposalNo")));
//			orderList.add(cb.asc(p.get("proposalNo")));
//			orderList.add(cb.asc(p.get("sno")));
				
			query.where(predicates.toArray(new Predicate[0]));

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getPlacingBouquetList(GetPlacingInfoReq req) {
		List<Tuple> list=new ArrayList<>();
		try {  
			//GET_PLACING_BOUQUET_LIST  //left join  
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);
			
			//reinsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate e1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(e1);
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), p.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), p.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> bpi = brokerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(bpi.get("firstName"), " ");
			brokerName.select(cb.concat(e0, bpi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pinfo = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pinfo.get("amendId")));
			Predicate g1 = cb.equal( pinfo.get("customerId"), bpi.get("customerId"));
			maxAmend1.where(g1);
			Predicate a1 = cb.equal( bpi.get("customerType"), "B");
			Predicate a2 = cb.equal( bpi.get("customerId"), p.get("brokerId"));
			Predicate a3 = cb.equal( bpi.get("branchCode"), p.get("branchCode"));
			Predicate a4 = cb.equal( bpi.get("amendId"), maxAmend1);
			brokerName.where(a1,a2,a3,a4);
			
			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> m = mailStatus.from(MailNotificationDetail.class);
			
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(m.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(m.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(m.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(m.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(m.get("mailStatus"));
			Predicate b1 = cb.equal(m.get("proposalNo"), p.get("proposalNo"));
			Predicate b2 = cb.equal(m.get("reinsurerId"), p.get("reinsurerId"));
			Predicate b3 = cb.equal(m.get("brokerId"), p.get("brokerId"));
			Predicate b4 = cb.equal(m.get("mailType"), p.get("status"));
			Predicate b5 = cb.equal(m.get("mailRecordNo"), mmaxAmend);
			mailStatus.where(b1,b2,b3,b4,b5);				

			//offerStatus
			Subquery<Integer> offerStatus = query.subquery(Integer.class); 
			Root<TtrnRiPlacement> pm = offerStatus.from(TtrnRiPlacement.class);
			offerStatus.select(cb.count(pm).as(Integer.class));
			Predicate i1 = cb.equal(pm.get("proposalNo"), p.get("proposalNo"));
			Predicate i2 = cb.equal(pm.get("reinsurerId"), p.get("reinsurerId"));
			Predicate i3 = cb.equal(pm.get("brokerId"), p.get("brokerId"));
			Predicate i4 = cb.notEqual(pm.get("status"), "O");
			offerStatus.where(i1,i2,i3,i4);
			
			query.multiselect( 
					 p.get("sno").alias("SNO"), 
					 p.get("bouquetNo").alias("BOUQUET_NO"),
					 p.get("reinsurerId").alias("REINSURER_ID"),
					// p.get("proposalNo").alias("PROPOSAL_NO"),
					 p.get("brokerId").alias("BROKER_ID"),
					 reInsurerName.alias("REINSURER_NAME"),
					 brokerName.alias("BROKER_NAME"),
					 p.get("shareOffered").alias("SHARE_OFFERED"), 
					 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS"), 
					 cb.selectCase().when(cb.greaterThan(offerStatus, 0), "N").otherwise("Y").alias("OFFER_STATUS")
					 ).distinct(true);
		
			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate j1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate j2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate j3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate j4 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(j1,j2,j3,j4);
					
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("branchCode"), req.getBranchCode()));
			predicates.add(cb.equal(p.get("bouquetNo"), req.getBouquetNo()));
			predicates.add(cb.equal(p.get("placementAmendId"), amendId));
	
			if(StringUtils.isNotBlank(req.getSearchType())) {
				predicates.add(cb.equal(p.get("reinsurerId"), req.getSearchReinsurerId()));
				predicates.add(cb.equal(p.get("brokerId"), req.getSearchBrokerId()));
				predicates.add(cb.equal(p.get("status"), req.getSearchStatus()));
			}
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(p.get("sno")));
				
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getPlacingBaselayerList(GetPlacingInfoReq req) {
		List<Tuple> list=new ArrayList<>();
		try {  
			//GET_PLACING_BASELAYER_LIST  //left join  
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);
			
			//reinsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate e1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(e1);
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), p.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), p.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> bpi = brokerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(bpi.get("firstName"), " ");
			brokerName.select(cb.concat(e0, bpi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pinfo = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pinfo.get("amendId")));
			Predicate g1 = cb.equal( pinfo.get("customerId"), bpi.get("customerId"));
			maxAmend1.where(g1);
			Predicate a1 = cb.equal( bpi.get("customerType"), "B");
			Predicate a2 = cb.equal( bpi.get("customerId"), p.get("brokerId"));
			Predicate a3 = cb.equal( bpi.get("branchCode"), p.get("branchCode"));
			Predicate a4 = cb.equal( bpi.get("amendId"), maxAmend1);
			brokerName.where(a1,a2,a3,a4);
			
			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> m = mailStatus.from(MailNotificationDetail.class);

			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(m.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(m.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(m.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(m.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			mailStatus.select(m.get("mailStatus"));
			Predicate b1 = cb.equal(m.get("proposalNo"), p.get("proposalNo"));
			Predicate b2 = cb.equal(m.get("reinsurerId"), p.get("reinsurerId"));
			Predicate b3 = cb.equal(m.get("brokerId"), p.get("brokerId"));
			Predicate b4 = cb.equal(m.get("mailType"), p.get("status"));
			Predicate b5 = cb.equal(m.get("mailRecordNo"), mmaxAmend);
			mailStatus.where(b1,b2,b3,b4,b5);				

			//offerStatus
			Subquery<Integer> offerStatus = query.subquery(Integer.class); 
			Root<TtrnRiPlacement> pm = offerStatus.from(TtrnRiPlacement.class);
			offerStatus.select(cb.count(pm).as(Integer.class));
			Predicate i1 = cb.equal(pm.get("proposalNo"), p.get("proposalNo"));
			Predicate i2 = cb.equal(pm.get("reinsurerId"), p.get("reinsurerId"));
			Predicate i3 = cb.equal(pm.get("brokerId"), p.get("brokerId"));
			Predicate i4 = cb.notEqual(pm.get("status"), "O");
			offerStatus.where(i1,i2,i3,i4);
			
			query.multiselect( 
					 p.get("sno").alias("SNO"), 
					 p.get("bouquetNo").alias("BOUQUET_NO"),
					 p.get("reinsurerId").alias("REINSURER_ID"),
					// p.get("proposalNo").alias("PROPOSAL_NO"),
					 p.get("brokerId").alias("BROKER_ID"),
					 reInsurerName.alias("REINSURER_NAME"),
					 brokerName.alias("BROKER_NAME"),
					 p.get("shareOffered").alias("SHARE_OFFERED"), 
					 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS"), 
					 cb.selectCase().when(cb.greaterThan(offerStatus, 0), "N").otherwise("Y").alias("OFFER_STATUS")
					 ).distinct(true);
		
			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate j1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate j2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate j3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate j4 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(j1,j2,j3,j4);
					
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("branchCode"), req.getBranchCode()));
			predicates.add(cb.equal(p.get("baseProposalNo"), req.getBaseProposalNo()));
			predicates.add(cb.equal(p.get("placementAmendId"), amendId));
	
			if(StringUtils.isNotBlank(req.getSearchType())) {
				predicates.add(cb.equal(p.get("reinsurerId"), req.getSearchReinsurerId()));
				predicates.add(cb.equal(p.get("brokerId"), req.getSearchBrokerId()));
				predicates.add(cb.equal(p.get("status"), req.getSearchStatus()));
			}
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(p.get("sno")));
				
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> getPlacingList(GetPlacingInfoReq req, String prop) {
		List<Tuple> list=new ArrayList<>();
		try {  
			//GET_PLACING_LIST //left join  
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);
			
			//reinsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate e1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(e1);
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), p.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), p.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> bpi = brokerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(bpi.get("firstName"), " ");
			brokerName.select(cb.concat(e0, bpi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pinfo = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pinfo.get("amendId")));
			Predicate g1 = cb.equal( pinfo.get("customerId"), bpi.get("customerId"));
			maxAmend1.where(g1);
			Predicate a1 = cb.equal( bpi.get("customerType"), "B");
			Predicate a2 = cb.equal( bpi.get("customerId"), p.get("brokerId"));
			Predicate a3 = cb.equal( bpi.get("branchCode"), p.get("branchCode"));
			Predicate a4 = cb.equal( bpi.get("amendId"), maxAmend1);
			brokerName.where(a1,a2,a3,a4);
			
			// Mail Status
			Subquery<String> mailStatus = query.subquery(String.class);
			Root<MailNotificationDetail> m = mailStatus.from(MailNotificationDetail.class);
			mailStatus.select(m.get("mailStatus"));
			
			Subquery<Long> mmaxAmend = query.subquery(Long.class); 
			Root<MailNotificationDetail> cm = mmaxAmend.from(MailNotificationDetail.class);
			mmaxAmend.select(cb.max(cm.get("mailRecordNo")));
			Predicate mb1 = cb.equal(m.get("proposalNo"), cm.get("proposalNo"));
			Predicate mb2 = cb.equal(m.get("reinsurerId"), cm.get("reinsurerId"));
			Predicate mb3 = cb.equal(m.get("brokerId"), cm.get("brokerId"));
			Predicate mb4 = cb.equal(m.get("mailType"), cm.get("mailType"));
			mmaxAmend.where(mb1,mb2,mb3,mb4);
			
			Predicate b1 = cb.equal(m.get("proposalNo"), p.get("proposalNo"));
			Predicate b2 = cb.equal(m.get("reinsurerId"), p.get("reinsurerId"));
			Predicate b3 = cb.equal(m.get("brokerId"), p.get("brokerId"));
			Predicate b4 = cb.equal(m.get("mailType"), p.get("status"));
			Predicate b5 = cb.equal(m.get("mailRecordNo"), mmaxAmend);
			mailStatus.where(b1,b2,b3,b4,b5);				

			//offerStatus
			Subquery<Integer> offerStatus = query.subquery(Integer.class); 
			Root<TtrnRiPlacement> pm = offerStatus.from(TtrnRiPlacement.class);
			offerStatus.select(cb.count(pm).as(Integer.class));
			Predicate i1 = cb.equal(pm.get("proposalNo"), p.get("proposalNo"));
			Predicate i2 = cb.equal(pm.get("reinsurerId"), p.get("reinsurerId"));
			Predicate i3 = cb.equal(pm.get("brokerId"), p.get("brokerId"));
			Predicate i4 = cb.notEqual(pm.get("status"), "O");
			offerStatus.where(i1,i2,i3,i4);
			
			query.multiselect( 
					 p.get("sno").alias("SNO"), 
					 p.get("bouquetNo").alias("BOUQUET_NO"),
					 p.get("reinsurerId").alias("REINSURER_ID"),
					 p.get("proposalNo").alias("PROPOSAL_NO"),
					 p.get("brokerId").alias("BROKER_ID"),
					 reInsurerName.alias("REINSURER_NAME"),
					 brokerName.alias("BROKER_NAME"),
					 p.get("shareOffered").alias("SHARE_OFFERED"), 
					 cb.coalesce(mailStatus, "Pending").alias("MAIL_STATUS"), 
					 cb.selectCase().when(cb.greaterThan(offerStatus, 0), "N").otherwise("Y").alias("OFFER_STATUS")
					 ).distinct(true);
		
			// AmendId
			Subquery<Long> amendId = query.subquery(Long.class);
			Root<TtrnRiPlacement> ocpm1 = amendId.from(TtrnRiPlacement.class);
			amendId.select( cb.max(ocpm1.get("placementAmendId")));
			Predicate j1 = cb.equal(ocpm1.get("proposalNo"), p.get("proposalNo"));
			Predicate j2 = cb.equal(ocpm1.get("reinsurerId"), p.get("reinsurerId"));
			Predicate j3 = cb.equal(ocpm1.get("brokerId"), p.get("brokerId"));
			Predicate j4 = cb.equal(ocpm1.get("branchCode"), p.get("branchCode"));
			amendId.where(j1,j2,j3,j4);
					
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("branchCode"), req.getBranchCode()));
			predicates.add(cb.equal(p.get("proposalNo"), prop));
			predicates.add(cb.equal(p.get("placementAmendId"), amendId));
	
			if(StringUtils.isNotBlank(req.getSearchType())) {
				predicates.add(cb.equal(p.get("reinsurerId"), req.getSearchReinsurerId()));
				predicates.add(cb.equal(p.get("brokerId"), req.getSearchBrokerId()));
				predicates.add(cb.equal(p.get("status"), req.getSearchStatus()));
			}
			
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(p.get("sno")));
				
			query.where(predicates.toArray(new Predicate[0])).orderBy(orderList);

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public List<Tuple> newContractPlSummary(PlacementSummaryReq bean) {
		List<Tuple> list = new ArrayList<>(); 
		try {//need to check query single row
			//NEW_CONTRACT_PL_SUMMARY
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnRiskDetails> trd = query.from(TtrnRiskDetails.class);
			Root<PositionMaster> pm = query.from(PositionMaster.class);
			Root<TtrnRiPlacement> trp = query.from(TtrnRiPlacement.class);
			Root<TtrnRiPlacementStatus> trps = query.from(TtrnRiPlacementStatus.class);
			Root<TtrnRiskProposal> tr = query.from(TtrnRiskProposal.class);
		
			//reinsurerName
			Subquery<String> reinsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reinsurerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(pi.get("firstName"), " ");
			Expression<String> e1 = cb.concat(e0, pi.get("lastName"));
			reinsurerName.select(e1);
			Subquery<Long> amen = query.subquery(Long.class); 
			Root<PersonalInfo> pis = amen.from(PersonalInfo.class);
			amen.select(cb.max(pis.get("amendId")));
			Predicate q1 = cb.equal( pi.get("customerId"), pis.get("customerId"));
			amen.where(q1);
			Predicate a1 = cb.equal( pi.get("customerType"), "R");
			Predicate a2 = cb.equal( pi.get("customerId"), trp.get("reinsurerId"));
			Predicate a3 = cb.equal( pi.get("branchCode"), trp.get("branchCode"));
			Predicate a4 = cb.equal( pi.get("amendId"), amen);
			reinsurerName.where(a1,a2,a3,a4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> pib = brokerName.from(PersonalInfo.class);
			Expression<String> e2 = cb.concat(pib.get("firstName"), " ");
			Expression<String> e3 = cb.concat(e2, pib.get("lastName"));
			brokerName.select(e3);
			Subquery<Long> amenb = query.subquery(Long.class); 
			Root<PersonalInfo> pibs = amenb.from(PersonalInfo.class);
			amenb.select(cb.max(pibs.get("amendId")));
			Predicate s1 = cb.equal( pibs.get("customerId"), pib.get("customerId"));
			amenb.where(s1);
			Predicate d1 = cb.equal( pib.get("customerType"), "B");
			Predicate d2 = cb.equal( pib.get("customerId"), trp.get("brokerId"));
			Predicate d3 = cb.equal( pib.get("branchCode"), trp.get("branchCode"));
			Predicate d4 = cb.equal( pib.get("amendId"), amenb);
			brokerName.where(d1,d2,d3,d4);
			
			//shortName
      		Subquery<String> shortName = query.subquery(String.class); 
      		Root<CurrencyMaster> cm = shortName.from(CurrencyMaster.class);
      		shortName.select(cm.get("shortName"));
      		Subquery<Long> amendA = query.subquery(Long.class); 
      		Root<CurrencyMaster> m = amendA.from(CurrencyMaster.class);
      		amendA.select(cb.max(m.get("amendId")));
      		Predicate b1 = cb.equal( cm.get("currencyId"), m.get("currencyId"));
      		Predicate b2 = cb.equal( cm.get("branchCode"), m.get("branchCode"));
      		amendA.where(b1,b2);
      		Predicate x1 = cb.equal(trd.get("rskOriginalCurr"), cm.get("currencyId"));
      		Predicate x2 = cb.equal( pm.get("branchCode"), cm.get("branchCode"));
      		Predicate x3 =cm.get("amendId").in(amendA==null?null:amendA);
      		shortName.where(x3,x1,x2);
			
			
			query.multiselect( //Math.round
							pm.get("offerNo").alias("OFFER_NO"),
							cb.coalesce(pm.get("baseLayer"), pm.get("proposalNo")).alias("BASE_PROPOSAL"),		
							pm.get("proposalNo").alias("PROPOSAL_NO"),
							trd.get("rskTreatyid").alias("TREATY_NAME"),
							cb.selectCase().when(cb.equal(pm.get("productId"), "2"), pm.get("sectionNo")).otherwise(pm.get("layerNo")).alias("LAYER_SECTION"),
							trp.get("sno").alias("SNO"),
							reinsurerName.alias("REINSURER_NAME"),
							brokerName.alias("BROKER_NAME"),
							shortName.alias("CURRENCY"),
							cb.selectCase().when(cb.equal(pm.get("productId"), "2"), tr.get("rskEpiEstOc")).otherwise(cb.selectCase().when(cb.equal(pm.get("productId"), "3"), tr.get("rskSubjPremiumOc"))).alias("EPI_100_OC"),
							cb.selectCase().when(cb.equal(pm.get("productId"), "2"), tr.get("rskEpiEstDc")).otherwise(cb.selectCase().when(cb.equal(pm.get("productId"), "3"), tr.get("rskSubjPremiumDc"))).alias("EPI_100_DC"),
							cb.coalesce(trps.get("newStatus"), trp.get("status")).alias("PLACING_STATUS"),
							cb.coalesce(trp.get("shareSigned"), "0").alias("SHARE_SIGNED"),
							cb.coalesce(trp.get("brokerage"), "0").alias("BROKERAGE") //(EPI_100_DC)*(BROKERAGE/100)*(SHARE_SIGNED/100) = BROKERAGE_AMT
							).distinct(true); 
	
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(pm.get("proposalNo")));
			orderList.add(cb.asc(trp.get("sno")));
			
			//amend
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<PositionMaster> pm1 = amend.from(PositionMaster.class);
			amend.select(cb.max(pm1.get("amendId")));
			Predicate r1 = cb.equal( pm.get("proposalNo"), pm1.get("proposalNo"));
			Predicate r2 = cb.equal( pm1.get("branchCode"),  pm.get("branchCode"));
			amend.where(r1,r2);
			//status
			Subquery<Long> status = query.subquery(Long.class); 
			Root<TtrnRiPlacement> trp1 = status.from(TtrnRiPlacement.class);
			status.select(cb.max(trp1.get("statusNo")));
			Predicate p1 = cb.equal( trp.get("proposalNo"), trp1.get("proposalNo"));
			Predicate p2 = cb.equal( trp1.get("branchCode"),  trp.get("branchCode"));
			Predicate p3 = cb.equal( trp1.get("sno"),  trp.get("sno"));
			status.where(p1,p2,p3);
			
			List<Predicate> predicList = new ArrayList<Predicate>();
			predicList.add(pm.get("amendId").in(amend==null?null:amend));
			predicList.add(cb.equal(pm.get("branchCode"), trp.get("branchCode")));
			predicList.add(cb.equal(pm.get("proposalNo"), trp.get("proposalNo")));
			predicList.add(trp.get("statusNo").in(status==null?null:status));
			predicList.add(cb.equal(pm.get("proposalNo"), trps.get("proposalNo")));
			predicList.add(cb.equal( trps.get("sno"),  trp.get("sno")));
			predicList.add(cb.equal( trps.get("statusNo"),  trp.get("statusNo")));
			predicList.add(cb.equal(pm.get("branchCode"), trd.get("branchCode")));
			predicList.add(cb.equal(pm.get("proposalNo"), trd.get("rskProposalNumber")));
			predicList.add(cb.equal(pm.get("amendId"), trd.get("rskEndorsementNo")));
			predicList.add(cb.equal(pm.get("branchCode"), tr.get("branchCode")));
			predicList.add(cb.equal(pm.get("proposalNo"), tr.get("rskProposalNumber")));
			predicList.add(cb.equal(pm.get("amendId"), tr.get("rskEndorsementNo")));
			predicList.add(cb.equal(pm.get("branchCode"), bean.getBranchCode()));
			predicList.add(cb.equal(pm.get("proposalNo"), bean.getProposalNo()));
			
			//getCompanyNameSearch
			Subquery<String> company = query.subquery(String.class); 
			Root<PersonalInfo> pic = company.from(PersonalInfo.class);
			Expression<String> e4 = cb.concat(pic.get("firstName"), " ");
			Expression<String> e5 = cb.concat(e4, pic.get("lastName"));
			company.select(e5);
			Subquery<Long> amenc = query.subquery(Long.class); 
			Root<PersonalInfo> pic1 = amenc.from(PersonalInfo.class);
			amenc.select(cb.max(pic1.get("amendId")));
			Predicate j1 = cb.equal( pic.get("customerId"), pic1.get("customerId"));
			amenc.where(j1);
			Predicate i1 = cb.equal( pic.get("customerType"), "R");
			Predicate i2 = cb.equal( pic.get("customerId"), trp.get("reinsurerId"));
			Predicate i3 = cb.equal( pic.get("branchCode"), trp.get("branchCode"));
			Predicate i4 = cb.equal( pic.get("amendId"), amenc);
			company.where(i1,i2,i3,i4);
			
			//getBrokerNameSearch
			Subquery<String> broker = query.subquery(String.class); 
			Root<PersonalInfo> pibn = broker.from(PersonalInfo.class);
			Expression<String> e6 = cb.concat(pibn.get("firstName"), " ");
			Expression<String> e7 = cb.concat(e6, pibn.get("lastName"));
			broker.select(e7);
			Subquery<Long> amenbn = query.subquery(Long.class); 
			Root<PersonalInfo> pibns = amenbn.from(PersonalInfo.class);
			amenbn.select(cb.max(pibns.get("amendId")));
			Predicate m1 = cb.equal( pibns.get("customerId"), pibn.get("customerId"));
			amenbn.where(m1);
			Predicate l1 = cb.equal( pibn.get("customerType"), "B");
			Predicate l2 = cb.equal( pibn.get("customerId"), trp.get("brokerId"));
			Predicate l3 = cb.equal( pibn.get("branchCode"), trp.get("branchCode"));
			Predicate l4 = cb.equal( pibn.get("amendId"), amenbn);
			broker.where(l1,l2,l3,l4);
			
			if(StringUtils.isNotBlank(bean.getSearchType()) && null !=bean.getSearchType()){
            	
        		if(StringUtils.isNotBlank(bean.getCompanyNameSearch())){
        			predicList.add(cb.like(cb.upper(company), "%" + bean.getCompanyNameSearch() + "%".toUpperCase()));
            	}
        		if(StringUtils.isNotBlank(bean.getBrokerNameSearch())){
        			predicList.add(cb.like(cb.upper(broker), "%" + bean.getBrokerNameSearch() + "%".toUpperCase()));
            	}
            	if(StringUtils.isNotBlank(bean.getUwYearSearch())){
            		predicList.add(cb.like(cb.upper(pm.get("uwYear")), "%" + bean.getUwYearSearch() + "%".toUpperCase()));
            	}
            	if(StringUtils.isNotBlank(bean.getUwYearToSearch())){
            		predicList.add(cb.like(cb.upper(pm.get("uwYearTo")), "%" + bean.getUwYearToSearch() + "%".toUpperCase()));
            	}
            	if(StringUtils.isNotBlank(bean.getIncepDateSearch())){//doubt
            		//AND TO_CHAR(INCEPTION_DATE,'DD/MM/YYYY') LIKE ?
            	
            	//	Expression<String> exp = sdf.format(pm.get("inceptionDate"));
            		predicList.add(cb.like(pm.get("inceptionDate"), "%" + bean.getIncepDateSearch() + "%"));
            	}
            	if(StringUtils.isNotBlank(bean.getExpDateSearch())){//doubt
            		//AND TO_CHAR(EXPIRY_DATE,'DD/MM/YYYY') LIKE ?
            		predicList.add(cb.like(pm.get("expiryDate"), "%" + bean.getExpDateSearch() + "%"));
            	}
            }
			
			query.where(predicList.toArray(new Predicate[0])).orderBy(orderList);
	
			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
		}catch(Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Override
	public double sumOfShareSigned(String proposalNo) {
		double sum = 0;
		try{
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> pm = query.from(TtrnRiPlacement.class);

			query.multiselect(cb.sum(pm.get("shareSigned")).alias("shareSigned")); 

			Subquery<Long> amendId = query.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amendId.from(TtrnRiPlacement.class);
			amendId.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( pm.get("statusNo"), pms.get("statusNo"));
			amendId.where(a1);

			Predicate n1 = cb.equal(pm.get("proposalNo"), proposalNo);
			Predicate n2 = cb.equal(pm.get("status"), "CSL");
			Predicate n3 = pm.get("amendId").in(amendId==null?null:amendId);
			query.where(n1,n2,n3).groupBy(pm.get("baseProposalNo"),pm.get("proposalNo"));
			
			TypedQuery<Tuple> res = em.createQuery(query);
			List<Tuple> list = res.getResultList();
			
			if(list.size()>0) {
				sum = list.get(0).get("shareSigned")==null?0l :Double.parseDouble(list.get(0).get("shareSigned").toString());
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return sum;
		}

	@Override
	public List<Tuple> getApprovalPendingList(GetApprovalPendingListReq req, String status) {
		List<Tuple> list=new ArrayList<>();
		try {  
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			Root<TtrnRiPlacement> p = query.from(TtrnRiPlacement.class);
			
			//companyName
			Subquery<String> companyName = query.subquery(String.class); 
			Root<PersonalInfo> pms = companyName.from(PersonalInfo.class);
			companyName.select(pms.get("companyName"));
			Predicate c1 = cb.equal( p.get("cedingCompanyId"), pms.get("customerId"));
			Predicate c2 = cb.equal( pms.get("customerType"), "C");
			Predicate c3 = cb.equal( p.get("branchCode"), pms.get("branchCode"));
			companyName.where(c1,c2,c3);
			
			//reinsurerName
			Subquery<String> reInsurerName = query.subquery(String.class); 
			Root<PersonalInfo> pi = reInsurerName.from(PersonalInfo.class);
			Expression<String> firstName1 = cb.concat(pi.get("firstName"), " ");
			reInsurerName.select(cb.concat(firstName1, pi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend = query.subquery(Long.class); 
			Root<PersonalInfo> pis = maxAmend.from(PersonalInfo.class);
			maxAmend.select(cb.max(pis.get("amendId")));
			Predicate e1 = cb.equal( pis.get("customerId"), pi.get("customerId"));
			maxAmend.where(e1);
			Predicate d1 = cb.equal( pi.get("customerType"), "R");
			Predicate d2 = cb.equal( pi.get("customerId"), p.get("reinsurerId"));
			Predicate d3 = cb.equal( pi.get("branchCode"), p.get("branchCode"));
			Predicate d4 = cb.equal( pi.get("amendId"), maxAmend);
			reInsurerName.where(d1,d2,d3,d4);
			
			//brokerName
			Subquery<String> brokerName = query.subquery(String.class); 
			Root<PersonalInfo> bpi = brokerName.from(PersonalInfo.class);
			Expression<String> e0 = cb.concat(bpi.get("firstName"), " ");
			brokerName.select(cb.concat(e0, bpi.get("lastName")));
			//maxAmend
			Subquery<Long> maxAmend1 = query.subquery(Long.class); 
			Root<PersonalInfo> pinfo = maxAmend1.from(PersonalInfo.class);
			maxAmend1.select(cb.max(pinfo.get("amendId")));
			Predicate g1 = cb.equal( pinfo.get("customerId"), bpi.get("customerId"));
			maxAmend1.where(g1);
			Predicate a1 = cb.equal( bpi.get("customerType"), "B");
			Predicate a2 = cb.equal( bpi.get("customerId"), p.get("brokerId"));
			Predicate a3 = cb.equal( bpi.get("branchCode"), p.get("branchCode"));
			Predicate a4 = cb.equal( bpi.get("amendId"), maxAmend1);
			brokerName.where(a1,a2,a3,a4);
			
			//offerNo
			Subquery<String> offer = query.subquery(String.class); 
			Root<PositionMaster> pm = offer.from(PositionMaster.class);
			offer.select(pm.get("offerNo"));
			//maxAmend
			Subquery<Long> amendOffer = query.subquery(Long.class); 
			Root<PositionMaster> piof = amendOffer.from(PositionMaster.class);
			amendOffer.select(cb.max(piof.get("amendId")));
			Predicate m1 = cb.equal( piof.get("proposalNo"), pm.get("proposalNo"));
			Predicate m2 = cb.equal( piof.get("branchCode"), pm.get("branchCode"));
			amendOffer.where(m1,m2);
			Predicate i1 = cb.equal(pm.get("proposalNo"), p.get("proposalNo"));
			Predicate i2 = cb.equal(pm.get("branchCode"), p.get("branchCode"));
			Predicate i3 = cb.equal(pm.get("amendId"), amendOffer);
			offer.where(i1,i2,i3);
			
			query.multiselect( 
					 p.get("sno").alias("SNO"), 
					 p.get("statusNo").alias("STATUS_NO"), 
					 offer.alias("OFFER_NO"), 
					 p.get("bouquetNo").alias("BOUQUET_NO"),
					 p.get("baseProposalNo").alias("BASE_PROPOSAL_NO"),
					 p.get("proposalNo").alias("PROPOSAL_NO") ,
					 p.get("reinsurerId").alias("REINSURER_ID"),
					 p.get("brokerId").alias("BROKER_ID") ,
					 companyName.alias("CEDING_COMPANY_NAME"),
					 reInsurerName.alias("REINSURER_NAME"),
					 brokerName.alias("BROKER_NAME"),
					 p.get("shareWritten").alias("SHARE_WRITTEN"), //WrittenLine
					 p.get("shareProposalWritten").alias("SHARE_PROPOSAL_WRITTEN"), //ProposalWrittenLine
					 p.get("shareSigned").alias("SHARE_SIGNED"), //Confirmed Signed Line 
					 p.get("shareProposedSigned").alias("SHARE_PROPOSED_SIGNED"), //Proposed Signed Line 
					 p.get("shareOffered").alias("SHARE_OFFERED"), //Share Offered 
					 p.get("status").alias("STATUS"),
					 p.get("brokerage").alias("BROKERAGE_PER") ); 

					
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(cb.equal(p.get("approverStatus"), "P"));
			predicates.add(cb.equal(p.get("status"), status));
			
			
			if(StringUtils.isNotBlank(req.getSearchType())) {
				List<String> includeprop =new ArrayList<String>(Arrays.asList(req.getSearchIncludeProposalNos().split(",") )) ;
				List<String> excludeprop =new ArrayList<String>(Arrays.asList(req.getSearchExcludeProposalNos().split(",") )) ;

				
				if(StringUtils.isNotBlank( req.getSearchReinsurerName()))
					predicates.add(cb.equal(reInsurerName, req.getSearchReinsurerName()));
				
				if(StringUtils.isNotBlank( req.getSearchBrokerName()))
					predicates.add(cb.equal(brokerName, req.getSearchBrokerName()));
				
				if(StringUtils.isNotBlank( req.getSearchStatusNo()))
					predicates.add(cb.equal(p.get("statusNo"), req.getSearchStatusNo()));
				
				if(StringUtils.isNotBlank( req.getSearchOfferNo()))
					predicates.add(cb.equal(offer, req.getSearchOfferNo()));
				
				if(StringUtils.isNotBlank( req.getSearchBouquetNo()))
					predicates.add(cb.equal(p.get("bouquetNo"), req.getSearchBouquetNo()));
				
				if(StringUtils.isNotBlank( req.getSearchBaseProposalNo()))
					predicates.add(cb.equal(p.get("baseProposalNo"), req.getSearchBaseProposalNo()));
				
				if(StringUtils.isNotBlank( req.getSearchProposalNo()))
					predicates.add(cb.equal(p.get("proposalNo"), req.getSearchProposalNo()));
				
				if(StringUtils.isNotBlank( req.getSearchCedingCompany()))
					predicates.add(cb.equal(companyName, req.getSearchCedingCompany()));
				
				if(StringUtils.isNotBlank(req.getSearchIncludeProposalNos()) )
					predicates.add(p.get("proposalNo").in(includeprop));
				
				if(StringUtils.isNotBlank(req.getSearchExcludeProposalNos()) )
					predicates.add(p.get("proposalNo").in(excludeprop).not());
			}

				
			query.where(predicates.toArray(new Predicate[0]));

			TypedQuery<Tuple> result = em.createQuery(query);
			list = result.getResultList();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
	}

	@Transactional
	@Override
	public void updateRiplacement(UpdateRiplacementReq req) {	
		try {
			
			//TtrnRiPlacement
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnRiPlacement> update = cb.createCriteriaUpdate(TtrnRiPlacement.class);
			Root<TtrnRiPlacement> m = update.from(TtrnRiPlacement.class);
		
			update.set("approverStatus", req.getApproverStatus());
			update.set("approverLoginId", req.getApproverLoginId());
			update.set("remarks", req.getRemarks());
			update.set("approverSysDate", new Date());
			
			// MAXAmend ID
			Subquery<Long> amend = update.subquery(Long.class); 
			Root<TtrnRiPlacement> pms = amend.from(TtrnRiPlacement.class);
			amend.select(cb.max(pms.get("placementAmendId")));
			Predicate a1 = cb.equal( m.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( m.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( m.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( m.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), req.getProposalNo());
			Predicate n2 = cb.equal(m.get("reinsurerId"), req.getReinsurerId());
			Predicate n3 = cb.equal(m.get("brokerId"),req.getBrokerId());
			Predicate n4 = cb.equal(m.get("branchCode"), req.getBranchCode());
			Predicate n5 = cb.equal(m.get("placementAmendId"), amend);
			Predicate n6 = cb.equal(m.get("status"), req.getNewStatus()); //
			
			update.where(n1,n2,n3,n4,n5,n6);
			em.createQuery(update).executeUpdate();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
}
	@Transactional
	@Override
	public void updateRiplacementStatus(UpdateRiplacementReq req) {
		try {
			//TtrnRiPlacementStatus
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnRiPlacementStatus> update2 = cb.createCriteriaUpdate(TtrnRiPlacementStatus.class);
			Root<TtrnRiPlacementStatus> ri = update2.from(TtrnRiPlacementStatus.class);
		
			update2.set("approverStatus", req.getApproverStatus());
			update2.set("approverLoginId", req.getApproverLoginId());
			update2.set("remarks", req.getRemarks());
			update2.set("approverSysDate", new Date());
			
			// MAXAmend ID
			Subquery<Long> amend = update2.subquery(Long.class); 
			Root<TtrnRiPlacementStatus> pms = amend.from(TtrnRiPlacementStatus.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal( ri.get("branchCode"), pms.get("branchCode"));
			Predicate a2 = cb.equal( ri.get("proposalNo"), pms.get("proposalNo"));
			Predicate a3 = cb.equal( ri.get("reinsurerId"), pms.get("reinsurerId"));
			Predicate a4 = cb.equal( ri.get("brokerId"), pms.get("brokerId"));
			amend.where(a1,a2,a3,a4);
			
			Predicate d1 = cb.equal(ri.get("proposalNo"), req.getProposalNo());
			Predicate d2 = cb.equal(ri.get("reinsurerId"), req.getReinsurerId());
			Predicate d3 = cb.equal(ri.get("brokerId"),req.getBrokerId());
			Predicate d4 = cb.equal(ri.get("branchCode"), req.getBranchCode());
			Predicate d5 = cb.equal(ri.get("amendId"), amend);
			Predicate d6 = cb.equal(ri.get("newStatus"), req.getNewStatus());//
			
			update2.where(d1,d2,d3,d4,d5,d6);
			em.createQuery(update2).executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getStatusNo(String proposal, String branchCode, String reinsurerId, String brokerId) {
		String statusNo="";
		List<TtrnRiPlacement> list=ttrnRiPlacementRepository.findByProposalNoAndBranchCodeAndReinsurerIdAndBrokerId(new BigDecimal(proposal),branchCode,reinsurerId,brokerId);
		if(!CollectionUtils.isEmpty(list)){
			statusNo=list.get(0).getStatusNo()==null?"":list.get(0).getStatusNo().toString();
		}
		return statusNo;
	}

	@Override
	public int getCountContractBouquet(String bouquetNo) {
		int cont=0;
		List<TtrnRiPlacement> list=ttrnRiPlacementRepository.findByBouquetNoAndContractNoIsNotNullAndContractNoNot(new BigDecimal(bouquetNo),BigDecimal.ZERO);
		if(!CollectionUtils.isEmpty(list)){
			cont=1;
		}
		return cont;
	}

	@Override
	public int getCountContractBaseLayer(String baseProposalNo) {
		int cont=0;
		List<TtrnRiPlacement> list=ttrnRiPlacementRepository.findBybaseProposalNoAndContractNoIsNotNullAndContractNoNot(new BigDecimal(baseProposalNo),BigDecimal.ZERO);
		if(!CollectionUtils.isEmpty(list)){
			cont=1;
		}
		return cont;
	}

	@Override
	public int getCountContractProposal(String prop) {
		int cont=0;
		List<TtrnRiPlacement> list=ttrnRiPlacementRepository.findByProposalNoAndContractNoIsNotNullAndContractNoNot(new BigDecimal(prop),BigDecimal.ZERO);
		if(!CollectionUtils.isEmpty(list)){
			cont=1;
		}
		return cont;
	}
}
