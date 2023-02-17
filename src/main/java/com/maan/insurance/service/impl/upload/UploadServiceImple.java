package com.maan.insurance.service.impl.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.maan.insurance.model.entity.ConstantDetail;
import com.maan.insurance.model.entity.PositionMaster;
import com.maan.insurance.model.entity.TmasDocTypeMaster;
import com.maan.insurance.model.entity.TtrnDocUploadDetails;
import com.maan.insurance.model.repository.ConstantDetailRepository;
import com.maan.insurance.model.repository.TtrnDocUploadDetailsRepository;
import com.maan.insurance.model.req.DoUploadReq;
import com.maan.insurance.model.req.placement.InsertDocdetailsReq;
import com.maan.insurance.model.req.upload.DoDeleteDocDetailsReq;
import com.maan.insurance.model.req.upload.GetDocListReq;
import com.maan.insurance.model.res.DropDown.CommonResDropDown;
import com.maan.insurance.model.res.DropDown.GetCommonDropDownRes;
import com.maan.insurance.model.res.retro.CommonResponse;
import com.maan.insurance.model.res.retro.CommonSaveRes;
import com.maan.insurance.model.res.upload.AllmoduleListRes;
import com.maan.insurance.model.res.upload.AllmoduleListRes1;
import com.maan.insurance.model.res.upload.GetDocListRes;
import com.maan.insurance.model.res.upload.GetDocListRes1;
import com.maan.insurance.model.res.upload.GetDocTypeRes;
import com.maan.insurance.model.res.upload.GetDocTypeRes1;
import com.maan.insurance.service.upload.UploadService;
import com.maan.insurance.validation.Formatters;

@Service
public class UploadServiceImple  implements UploadService{
	private Logger log = LogManager.getLogger(UploadServiceImple.class);
	String commonPath = (UploadServiceImple.class).getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " ");
	@Autowired
	private ConstantDetailRepository cdRepo;
	@Autowired
	private TtrnDocUploadDetailsRepository udRepo;
	@Autowired
	private Formatters fm;
	
	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();

	public UploadServiceImple() {
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
			if (inputStream != null) {
				prop.load(inputStream);
			}

		} catch (Exception e) {
			log.info(e);
		}
	}

	@Override
	public AllmoduleListRes allmoduleList(String branchCode) {
		AllmoduleListRes response = new AllmoduleListRes();
		List<AllmoduleListRes1> resList = new ArrayList<AllmoduleListRes1>();
		try{
			//UPLOAD_GETMODULEDOCLIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TtrnDocUploadDetails> pm = query.from(TtrnDocUploadDetails.class);
			
			// docName
			Subquery<String> docName = query.subquery(String.class); 
			Root<TmasDocTypeMaster> pms = docName.from(TmasDocTypeMaster.class);
			docName.select(pms.get("docName"));
			Predicate a1 = cb.equal( pm.get("docType"), pms.get("docType"));
			Predicate a2 = cb.equal( pm.get("branchCode"), pms.get("branchCode"));
			Predicate a3 = cb.equal( pm.get("moduleType"), pms.get("moduleType")); 
			Predicate a4 = cb.equal( pm.get("productId"), pms.get("productId"));
			docName.where(a1,a2,a3,a4);

			// Select
			query.multiselect(pm.get("moduleType").alias("moduleType"),pm.get("contractNo").alias("contractNo"),
					pm.get("layerNo").alias("layerNo"),pm.get("docId").alias("docId"),
					pm.get("docDesc").alias("docDesc"),pm.get("orgFileName").alias("orgFileName"),
					pm.get("tranNo").alias("tranNo"),pm.get("ourFileName").alias("ourFileName"),
					pm.get("productId").alias("productId"),pm.get("docType").alias("docType"),
					docName.alias("docName")); 

			// Where
			Predicate n1 = cb.equal(pm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(pm.get("status"), "Y");
			query.where(n1,n2);
			
			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			if(list.size()>0) {
				for(Tuple data: list) {
					AllmoduleListRes1 res = new AllmoduleListRes1();
					res.setContractNo(data.get("contractNo")==null?"":data.get("contractNo").toString());
					res.setDocDesc(data.get("docDesc")==null?"":data.get("docDesc").toString());
					res.setDocId(data.get("docId")==null?"":data.get("docId").toString());
					res.setDocName(data.get("docName")==null?"":data.get("docName").toString());
					res.setDocType(data.get("docType")==null?"":data.get("docType").toString());
					res.setLayerNo(data.get("layerNo")==null?"":data.get("layerNo").toString());
					res.setModuleType(data.get("moduleType")==null?"":data.get("moduleType").toString());
					res.setOrgFileName(data.get("orgFileName")==null?"":data.get("orgFileName").toString());
					res.setOurFileName(data.get("ourFileName")==null?"":data.get("ourFileName").toString());
					res.setProductId(data.get("productId")==null?"":data.get("productId").toString());
					res.setTranNo(data.get("tranNo")==null?"":data.get("tranNo").toString());
					resList.add(res);
				}
			}
			response.setCommonResponse(resList);	
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
	public GetDocListRes getDocList(GetDocListReq bean) {
		GetDocListRes response = new GetDocListRes();
		List<GetDocListRes1> resList = new ArrayList<GetDocListRes1>();
		SimpleDateFormat sdf = new  SimpleDateFormat("dd/MM/yyyy");
		try{
			if("document".equalsIgnoreCase(bean.getType())){
			//	 sql="select CATEGORY_DETAIL_ID,DETAIL_NAME from  CONSTANT_DETAIL where CATEGORY_ID=22 and BRANCH_CODE=? order by CATEGORY_DETAIL_ID";
				
			List<ConstantDetail> list	= cdRepo.findByCategoryIdAndBranchCodeOrderByCategoryDetailId(new BigDecimal("22"),bean.getBranchCode());
			if(list.size()>0) {
				for(ConstantDetail data: list) {
					GetDocListRes1 res = new GetDocListRes1();
					res.setCategoryDetailId(data.getCategoryDetailId()==null?"":data.getCategoryDetailId().toString());
					res.setDetailName(data.getDetailName()==null?"":data.getDetailName());
					resList.add(res);
				}
			}
			}
			else{
				CriteriaBuilder cb = em.getCriteriaBuilder(); 
				CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
				
				Root<TtrnDocUploadDetails> dud = query.from(TtrnDocUploadDetails.class);
				Root<TmasDocTypeMaster> dtm = query.from(TmasDocTypeMaster.class);
				
				// Select
				query.multiselect(dud.get("docId").alias("docId"),dtm.get("docName").alias("docName"),
						dud.get("docDesc").alias("docDesc"),dud.get("orgFileName").alias("orgFileName"),
						dud.get("fileLocation").alias("fileLocation"),dud.get("ourFileName").alias("ourFileName"),
						dud.get("effDate").alias("effDate"));
				
				// MAXAmend ID
				Subquery<Long> amend = query.subquery(Long.class); 
				Root<TmasDocTypeMaster> pms = amend.from(TmasDocTypeMaster.class);
				amend.select(cb.max(pms.get("amendId")));
				Predicate a1 = cb.equal( pms.get("docType"), dtm.get("docType"));
				Predicate a2 = cb.equal( pms.get("branchCode"), dtm.get("branchCode"));
				Predicate a3 = cb.equal( pms.get("productId"), dtm.get("productId"));
				Predicate a4 = cb.equal( pms.get("moduleType"), dtm.get("moduleType"));
				amend.where(a1,a2,a3,a4);
				
				//Order By
				List<Order> orderList = new ArrayList<Order>();
				orderList.add(cb.asc(dud.get("docId")));
				
				// Where
				Predicate n1 = cb.equal(dud.get("branchCode"), bean.getBranchCode());
				Predicate n2 = cb.equal(dud.get("moduleType"), bean.getModuleType());
				Predicate n3 = cb.equal(dud.get("productId"), bean.getProductId());
				Predicate n4 = cb.equal(dud.get("status"), "Y");
				Predicate n5 = cb.equal(dud.get("docType"), dtm.get("docType"));
				Predicate n6 = cb.equal(dud.get("branchCode"), dtm.get("branchCode"));
				Predicate n7 = cb.equal(dud.get("productId"), dtm.get("productId"));
				Predicate n8 = cb.equal(dud.get("moduleType"), dtm.get("moduleType"));
				Predicate n9 = cb.equal(dtm.get("status"), "Y");
				Predicate n10 = cb.equal(dtm.get("amendId"), amend);
				
				 if("RDS".equalsIgnoreCase(bean.getModuleType())||"6".equalsIgnoreCase(bean.getModuleType())){
					 Predicate n11 = cb.equal(dud.get("contractNo"), bean.getContractNo());
					Predicate n12 = cb.equal(dud.get("layerNo"), StringUtils.isBlank(bean.getLayerNo())?"0":bean.getLayerNo());
					query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12).orderBy(orderList);
			}else
			{
				 Predicate n11 = cb.equal(dud.get("tranNo"), bean.getTranNo());	
				 query.where(n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11).orderBy(orderList);
			}
				// Get Result
					TypedQuery<Tuple> result = em.createQuery(query);
					List<Tuple> list = result.getResultList();
					
					if(list.size()>0) {
						for(Tuple data: list) {
							GetDocListRes1 res = new GetDocListRes1();
							res.setDocDesc(data.get("docDesc")==null?"":data.get("docDesc").toString());
							res.setDocId(data.get("docId")==null?"":data.get("docId").toString());
							res.setDocName(data.get("docName")==null?"":data.get("docName").toString());
							res.setEffDate(data.get("effDate")==null?"":sdf.format(data.get("effDate")));
							res.setLocation(data.get("fileLocation")==null?"":data.get("fileLocation").toString().replace("\\", "~")); //doubt
							res.setOrgFileName(data.get("orgFileName")==null?"":data.get("orgFileName").toString());
							res.setOurFileName(data.get("ourFileName")==null?"":data.get("ourFileName").toString());
							resList.add(res);
						}
					}
			}
			response.setCommonResponse(resList);	
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
	public GetDocTypeRes getDocType(String branchCode, String productId, String moduleType) {
		GetDocTypeRes response = new GetDocTypeRes();
		List<GetDocTypeRes1> resList = new ArrayList<GetDocTypeRes1>(); 
		try{
			if(moduleType==null){
				moduleType="";	
			}
			//UPLOAD_GETDOCTYPELIST
			CriteriaBuilder cb = em.getCriteriaBuilder(); 
			CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class); 
			
			Root<TmasDocTypeMaster> dtm = query.from(TmasDocTypeMaster.class);

			// Select
			query.multiselect(dtm.get("docType").alias("docType"), dtm.get("docName").alias("docName")); 

			// MAXAmend ID
			Subquery<Long> amend = query.subquery(Long.class); 
			Root<TmasDocTypeMaster> pms = amend.from(TmasDocTypeMaster.class);
			amend.select(cb.max(pms.get("amendId")));
			Predicate a1 = cb.equal(pms.get("docType"), dtm.get("docType"));
			Predicate a2 = cb.equal(pms.get("branchCode"), dtm.get("branchCode"));
			Predicate a3 = cb.equal(pms.get("productId"), dtm.get("productId"));
			Predicate a4 = cb.equal(pms.get("moduleType"), dtm.get("moduleType"));
			amend.where(a1,a2,a3,a4);
			
			//Order By
			List<Order> orderList = new ArrayList<Order>();
			orderList.add(cb.asc(dtm.get("docName")));

			// Where
			Predicate n1 = cb.equal(dtm.get("branchCode"), branchCode);
			Predicate n2 = cb.equal(dtm.get("moduleType"), moduleType);
			Predicate n3 = cb.equal(dtm.get("productId"), productId);
			Predicate n4 = cb.equal(dtm.get("status"), "Y");
			Predicate n5 = cb.equal(dtm.get("amendId"), amend);
			query.where(n1,n2,n3,n4,n5).orderBy(orderList);

			// Get Result
			TypedQuery<Tuple> result = em.createQuery(query);
			List<Tuple> list = result.getResultList();
			if(list.size()>0) {
				for(Tuple data: list) {
					GetDocTypeRes1 res = new GetDocTypeRes1();
					res.setDocType(data.get("docType")==null?"":data.get("docType").toString());
					res.setDocName(data.get("docName")==null?"":data.get("docName").toString());
					resList.add(res);
				}
			}
			response.setCommonResponse(resList);	
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
	public GetCommonDropDownRes moduleTypeList(String branchCode) {
		GetCommonDropDownRes response = new GetCommonDropDownRes();
		List<CommonResDropDown> resList = new ArrayList<CommonResDropDown>();
		try{
			//UPLOAD_GETMODULETYPELIST
			List<ConstantDetail> list	= cdRepo.findByCategoryIdAndBranchCodeOrderByCategoryDetailId(new BigDecimal("21"),branchCode);
			if(list.size()>0) {
				for(ConstantDetail data: list) {
					CommonResDropDown res = new CommonResDropDown();
					res.setCode(data.getCategoryDetailId()==null?"":data.getCategoryDetailId().toString());
					res.setCodeDescription(data.getDetailName()==null?"":data.getDetailName());
					resList.add(res);
				}
			}
			response.setCommonResponse(resList);	
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");
				response.setIsError(true);
			}
		return response;
	}
	@Transactional
	@Override
	public CommonResponse doDeleteDocDetails(DoDeleteDocDetailsReq bean) {
		CommonResponse response = new CommonResponse();
		try{
			//UPLOAD_UPDATEDOCDETAILS
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			// create update
			CriteriaUpdate<TtrnDocUploadDetails> update = cb.createCriteriaUpdate(TtrnDocUploadDetails.class);
			
			// set the root class
			Root<TtrnDocUploadDetails> m = update.from(TtrnDocUploadDetails.class);
			
			// set update and where clause
			update.set("status", "D");
			update.set("delLoginId", bean.getLoginId());
			
			Predicate n1 = cb.equal(m.get("branchCode"), bean.getBranchCode());
			Predicate n2 = cb.equal(m.get("moduleType"), bean.getModuleType());
			Predicate n3 = cb.equal(m.get("productId"), bean.getProductId());
			Predicate n4 = cb.equal(m.get("docId"), bean.getDocId());
			Predicate n5 = cb.equal(m.get("ourFileName"), bean.getOurFileName());
		
			if("RDS".equalsIgnoreCase(bean.getModuleType())||"6".equalsIgnoreCase(bean.getModuleType())){
				Predicate n6 = cb.equal(m.get("contractNo"), bean.getContractNo());
				Predicate n7 = cb.equal(m.get("layerNo"), StringUtils.isBlank(bean.getLayerNo())?"0":bean.getLayerNo());
				update.where(n1,n2,n3,n4,n5,n6,n7);
			}else
			{
				Predicate n6 = cb.equal(m.get("tranNo"),bean.getTranNo());
				update.where(n1,n2,n3,n4,n5,n6);
			}
			// perform update
			em.createQuery(update).executeUpdate();
			
			//UPLOAD_SWAPDOCID
			CriteriaUpdate<TtrnDocUploadDetails> update1 = cb.createCriteriaUpdate(TtrnDocUploadDetails.class);
			
			// set the root class
			Root<TtrnDocUploadDetails> m1 = update1.from(TtrnDocUploadDetails.class);
			
			// set update and where clause
			update1.set("docId", cb.diff(m1.get("docId"), 1));
			
			Predicate a1 = cb.equal(m1.get("branchCode"), bean.getBranchCode());
			Predicate a2 = cb.equal(m1.get("moduleType"), bean.getModuleType());
			Predicate a3 = cb.equal(m1.get("productId"), bean.getProductId());
			Predicate a4 = cb.greaterThan(m1.get("docId"), bean.getDocId());
			Predicate a5 = cb.equal(m1.get("ourFileName"), bean.getOurFileName());
			
			if("RDS".equalsIgnoreCase(bean.getModuleType())||"6".equalsIgnoreCase(bean.getModuleType())){
				Predicate a6 = cb.equal(m1.get("contractNo"), bean.getContractNo());
				Predicate a7 = cb.equal(m1.get("layerNo"), StringUtils.isBlank(bean.getLayerNo())?"0":bean.getLayerNo());
				update.where(a1,a2,a3,a4,a5,a6,a7);
			}else
			{
				Predicate a6 = cb.equal(m1.get("tranNo"),bean.getTranNo());
				update.where(a1,a2,a3,a4,a5,a6);
			}
			// perform update
			em.createQuery(update).executeUpdate();
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				response.setMessage("Failed");	
				response.setIsError(true);
			}
		return response;
	}
	@Transactional
	@Override
	public CommonSaveRes doUpload(DoUploadReq bean) {
		CommonSaveRes response = new CommonSaveRes();
		String result="";
		try{
			List<String[]> list = new ArrayList<String[]>();
			String args[]=new String[5];
			String filePath=commonPath+"documents/"+bean.getModuleType()+"/";
			/*if("RDS".equalsIgnoreCase(bean.getModuleType())||"PR".equalsIgnoreCase(bean.getModuleType())||"CL".equalsIgnoreCase(bean.getModuleType()))
			{
				filePath += bean.getModuleType()+"/"+bean.getContractNo()+"/";
			}
			else {
				filePath += bean.getModuleType()+"/"+bean.getTranNo()+"/";
			}
			
			 * File tmpFile = new File(filePath); if(!tmpFile.exists()){ tmpFile.mkdir(); }
			 * if("PR".equalsIgnoreCase(bean.getModuleType())||"CL".equalsIgnoreCase(bean.
			 * getModuleType())){ filePath +=bean.getTranNo()+"/"; tmpFile = new
			 * File(filePath); if(!tmpFile.exists()) tmpFile.mkdir(); }
			 */

			for(int i=0;i<bean.getInsertDocdetailsReq().size();i++) {
				InsertDocdetailsReq req =  bean.getInsertDocdetailsReq().get(i);
				final String orgFileName=req.getUploadFileName();
				Calendar cal = Calendar.getInstance();
				String time = cal.get(Calendar.DATE)+"-"+(cal.get(Calendar.MONTH)+1)+"-"
				+cal.get(Calendar.YEAR)+"_"+cal.get(Calendar.HOUR)+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND);
				String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
				String fileName = orgFileName.substring(0, orgFileName.lastIndexOf("."))+"_"+time;
				fileName = fileName + ext;
				final File copyFile = new File(filePath+fileName);
				encodeBase64ToFile( req.getUpload(),copyFile);
				TtrnDocUploadDetails dlist = udRepo.findTop1ByOrderByDocIdDesc();
				String docId="";
				if(dlist!=null) {
					docId =	dlist.getDocId()==null?"0":String.valueOf(dlist.getDocId().intValue()+1);
				}
				final String[] obj = new String[14];
				obj[0] =  docId;
				obj[1] = req.getDocTypeId();
				obj[2] = req.getDocDesc();
				obj[3] = orgFileName;
				obj[4] = fileName;
				obj[5] = filePath;
				obj[6] = bean.getModuleType();
				obj[7] = StringUtils.isBlank(bean.getProposalNo())?"":bean.getProposalNo();
				obj[8] = StringUtils.isBlank(bean.getContractNo())?"":bean.getContractNo();
				obj[9] = StringUtils.isBlank(bean.getLayerNo())?"0":bean.getLayerNo();
				obj[10] = StringUtils.isBlank(bean.getTranNo())?"":bean.getTranNo();
				obj[11] = bean.getProductId();
				obj[12] = bean.getBranchCode();
				obj[13] = bean.getLoginId();
				list.add(obj);
				
				args[0] = StringUtils.isBlank(bean.getProposalNo())?"":bean.getProposalNo();
				args[1] = StringUtils.isBlank(bean.getProposalNo())?"":bean.getProposalNo();
				args[2] = StringUtils.isBlank(bean.getProposalNo())?"":bean.getProposalNo();
				args[3] = StringUtils.isBlank(bean.getProposalNo())?"":bean.getProposalNo();
				args[4] = StringUtils.isBlank(bean.getProposalNo())?"":bean.getProposalNo();
			}
			if(list.size()>0){
				result = doUploadDocDetails(list,args);
			}
			response.setMessage("Success");
			response.setIsError(false);
			}catch(Exception e){
				e.printStackTrace();
				result = "Error while saving the Files => " + e.getMessage();
				response.setMessage("Failed");	
				response.setIsError(true);
			}
			return response;
	}
	@Transactional
	public String doUploadDocDetails(List<String[]> list, String[] args) {
		String result="";
		TtrnDocUploadDetails entity = new TtrnDocUploadDetails();
		try{
			//UPLOAD_INSERTDOCDETAILS
			for(String[] obj:list)
			{
				entity.setDocId(new BigDecimal(obj[0]));
				entity.setDocType(obj[1]);
				entity.setDocDesc(obj[2]);
				entity.setOrgFileName(obj[3]);
				entity.setOurFileName(obj[4]);
				entity.setFileLocation(obj[5]);
				entity.setModuleType(obj[6]);
				entity.setProposalNo(fm.formatBigDecimal(obj[7]));
				entity.setContractNo(fm.formatBigDecimal(obj[8]));
				entity.setLayerNo(fm.formatBigDecimal(obj[9]));
				entity.setTranNo(fm.formatBigDecimal(obj[10]));
				entity.setProductId(new BigDecimal(obj[11]));
				entity.setBranchCode(obj[12]);
				entity.setLoginId(obj[13]);
				entity.setEffDate(new Date());
				entity.setStatus("Y");
				entity.setEndorsementNo(BigDecimal.ZERO);
				udRepo.saveAndFlush(entity);
			}
			if(StringUtils.isNotBlank(args[0])) {
			//DOC_CON_UPDATE
			CriteriaBuilder cb = this.em.getCriteriaBuilder();
			CriteriaUpdate<TtrnDocUploadDetails> update = cb.createCriteriaUpdate(TtrnDocUploadDetails.class);
			
			// set the root class
			Root<TtrnDocUploadDetails> m = update.from(TtrnDocUploadDetails.class);
			
			// endorsementNo
			Subquery<Long> maxEndRp = update.subquery(Long.class); 
			Root<PositionMaster> rps = maxEndRp.from(PositionMaster.class);
			maxEndRp.select(cb.max(rps.get("amendId")));
			Predicate y1 = cb.equal( rps.get("proposalNo"), args[0]);
			maxEndRp.where(y1);
			
			// endorsementNo
			Subquery<Long> deptId = update.subquery(Long.class); 
			Root<PositionMaster> p = deptId.from(PositionMaster.class);
			deptId.select(p.get("deptId"));
			Predicate x1 = cb.equal( p.get("proposalNo"), args[1]);
			Predicate x2 = cb.equal( p.get("amendId"), maxEndRp);
			deptId.where(x1,x2);
			
			// set update and where clause
			update.set("endorsementNo", maxEndRp);
			update.set("deptId", deptId);
			
			Predicate n1 = cb.equal(m.get("proposalNo"), args[2]);
			Predicate n2 = cb.equal(m.get("endorsementNo"), args[3]);
		
			update.where(n1,n2);
			em.createQuery(update).executeUpdate();
			}
		}catch(Exception e){
			result = e.getMessage();
			e.printStackTrace();
		}
		return result;
	}

	private void  encodeBase64ToFile(String file, File copyFile) {
		byte[] data = Base64.getDecoder().decode(file);

		try( OutputStream stream = new FileOutputStream(copyFile) ) 
		{
		   stream.write(data);
		}
		catch (Exception e) 
		{
		   System.err.println("Couldn't write to file...");
		   e.printStackTrace();
		}
	}
}

