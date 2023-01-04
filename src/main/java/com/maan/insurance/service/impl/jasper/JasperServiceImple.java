package com.maan.insurance.service.impl.jasper;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.jasper.GetAllocationReportReq;
import com.maan.insurance.model.req.jasper.GetClaimOslrReportReq;
import com.maan.insurance.model.req.jasper.GetClaimPaidRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetClaimRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetDebtorsAgingReportReq;
import com.maan.insurance.model.req.jasper.GetInsallmentDueReportReq;
import com.maan.insurance.model.req.jasper.GetJournalReportReq;
import com.maan.insurance.model.req.jasper.GetJournalReportReq1;
import com.maan.insurance.model.req.jasper.GetJournalViewDownloadReq;
import com.maan.insurance.model.req.jasper.GetPayRecReportReq;
import com.maan.insurance.model.req.jasper.GetPolicyRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetPremiumRegisterReportReq;
import com.maan.insurance.model.req.jasper.GetRetroReportReq;
import com.maan.insurance.model.req.jasper.GetSoaReportReq;
import com.maan.insurance.model.req.jasper.GetTransactionPDFReportReq;
import com.maan.insurance.model.req.jasper.GetTransactionReportReq;
import com.maan.insurance.model.req.jasper.GetTreatyWithdrawReportReq;
import com.maan.insurance.res.jasper.JasperDocumentRes;
import com.maan.insurance.service.jasper.JasperService;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class JasperServiceImple implements JasperService{
	private Logger logger = LogManager.getLogger(JasperServiceImple.class);
	@PersistenceContext
	private EntityManager em;
	private Properties prop = new Properties();
	private Query query1 = null;

	@Autowired
	private JasperConfiguration config;


	public JasperServiceImple() {
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("OracleQuery.properties");
			if (inputStream != null) {
				prop.load(inputStream);
			}

		} catch (Exception e) {
			logger.info(e);
		}
	}
//	final HttpServletRequest request = ServletContext.getRequest();
//	final HttpServletResponse response = ServletActionContext.getResponse();
//	Map<String, Object> session = ActionContext.getContext().getSession();
	
	private JasperDocumentRes getJasperPdfFile(String jasperPath, String filePath,Map<String, Object> input) {
		JasperDocumentRes res = new JasperDocumentRes();
		Connection connection=null;
		try {
			connection=config.getDataSourceForJasper().getConnection();
			InputStream inputStream = this.getClass().getResourceAsStream(jasperPath);
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,input, connection);
			
			System.out.println("filePath name is ====> "+filePath);
			JasperExportManager.exportReportToPdfFile(jasperPrint,filePath);
			
			GetFileFromPath path=new GetFileFromPath(filePath);
			res.setPdfoutfilepath(path.call().getImgUrl());
			
		}catch(Exception e) {
			e.printStackTrace();		
		}finally {
			if(connection!=null)
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return res;
	}
	
	
//	private void exporter(String downloadType, JasperPrint jasperPrint, OutputStream os) throws Exception {
//	if("xls".equals(downloadType)) {
//		JRXlsExporter exporter = new JRXlsExporter();
//		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,os);
//		exporter.setParameter(JRXlsAbstractExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
//		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
//		exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//		exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//		//exporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.FALSE);
//		exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
//		exporter.setParameter(JRXlsAbstractExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
//		//exporter.setParameter(JRXlsAbstractExporterParameter.IS_IGNORE_GRAPHICS, Boolean.TRUE);
//		exporter.exportReport();
//	}			
//	else if("pdf".equals(downloadType)) {
//		JRPdfExporter exporter = new JRPdfExporter();
//		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);					
//		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,os);							
//		exporter.exportReport();				
//	}			
//	else {
//		JRHtmlExporter exporter = new JRHtmlExporter();
//		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,os);	
//		exporter.setParameter(JRHtmlExporterParameter.IS_WHITE_PAGE_BACKGROUND, false);
//		exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, true);
//		exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true);
//		exporter.setParameter(JRHtmlExporterParameter.ZOOM_RATIO, 1.3F);
//		exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
//		exporter.setParameter(JRHtmlExporterParameter.FRAMES_AS_NESTED_TABLES,true);	
//		exporter.exportReport();			
//	}
//}
	
	@Override
	public JasperDocumentRes getJournalReport(GetJournalReportReq req) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", req.getStartDate());
			jasperParameter.put("endDate", req.getEndDate());
			jasperParameter.put("JournalId", req.getJournalId());
			jasperParameter.put("branchCode", req.getBranchCode());
			jasperParameter.put("status", req.getStatus());
			jasperParameter.put("processStatus", req.getProcessStatus());
			
			if(null!=jasperParameter && jasperParameter.size()>0) {
				
				String directoryname=req.getJournalId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				
				getPdfOutFilePath = filePath+"/"+req.getJournalId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/Journals_Report.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}
	@Override
	public JasperDocumentRes getPayRecReport(GetPayRecReportReq bean) {
		
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		
		try{
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("broker", bean.getBrokerId().equals("-1")?"ALL":bean.getBrokerId());
			jasperParameter.put("ceding", bean.getCedingId().equals("-1")?"ALL":bean.getCedingId());
			jasperParameter.put("payrectype", bean.getPayrecType());
			jasperParameter.put("transactionType", bean.getTransactionType());
			jasperParameter.put("status", bean.getShowAllFields());
			jasperParameter.put("reportName", bean.getReportName());
			jasperParameter.put("cedingId", bean.getCedingCoName());
			jasperParameter.put("brokerId", bean.getBrokerName());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/PayRecRegister.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getTransactionReport(GetTransactionReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
		
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("branchCode",bean.getBranchCode());
			jasperParameter.put("docType", bean.getDocType().replaceAll("\\s+",""));
			jasperParameter.put("uwYear", bean.getUwYear());
			jasperParameter.put("contractNo", bean.getContractNo());
			jasperParameter.put("cedingName", bean.getCedingId());
			jasperParameter.put("brokerName", bean.getBrokerId());
			jasperParameter.put("cedingId", bean.getCedingCoName());
			jasperParameter.put("brokerId", bean.getBrokerName());
			jasperParameter.put("showfieldYN", bean.getShowAllFields());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("loginId", bean.getLoginName());
			jasperParameter.put("reportName", bean.getReportName());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/TransactionReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getClaimRegisterReport(GetClaimRegisterReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
		
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("countryId", bean.getCountryId());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("uwYear", bean.getUwYear());
			jasperParameter.put("cedingName", bean.getCedingId());
			jasperParameter.put("brokerName", bean.getBrokerId());
			jasperParameter.put("cedingId", bean.getCedingCoName());
			jasperParameter.put("brokerId", bean.getBrokerName());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("loginId", bean.getLoginId());
			jasperParameter.put("reportName", bean.getReportName());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/ClaimRegister.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getPremiumRegisterReport(GetPremiumRegisterReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
		
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("countryId", bean.getCountryId());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("uwYear", bean.getUwYear());
			
			jasperParameter.put("cedingName", bean.getCedingId());
			jasperParameter.put("brokerName", bean.getBrokerId());
			jasperParameter.put("cedingId", bean.getCedingCoName());
			jasperParameter.put("brokerId", bean.getBrokerName());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("loginId", bean.getLoginId());
			jasperParameter.put("reportName", bean.getReportName());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/PremiumRegisterReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getClaimPaidRegisterReport(GetClaimPaidRegisterReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("countryId", bean.getCountryId());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("uwYear", bean.getUwYear().equals("-1")?"ALL":bean.getUwYear());
			
			jasperParameter.put("cedingName", bean.getCedingId().equals("-1")?"ALL":bean.getCedingId());
			jasperParameter.put("brokerName", bean.getBrokerId().equals("-1")?"ALL":bean.getBrokerId());
			jasperParameter.put("cedingId", bean.getCedingCoName());
			jasperParameter.put("brokerId", bean.getBrokerName());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("loginId", bean.getLoginId().equals("-1")?"ALL":bean.getLoginId());
			jasperParameter.put("reportName", bean.getReportName());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/ClaimPaidReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getPolicyRegisterReport(GetPolicyRegisterReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
		
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("countryId", bean.getCountryId());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("uwYear", bean.getUwYear());
			jasperParameter.put("cedingName", bean.getCedingId());
			jasperParameter.put("brokerName", bean.getBrokerId());
			jasperParameter.put("cedingId", bean.getCedingCoName());
			jasperParameter.put("brokerId", bean.getBrokerName());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("loginId", bean.getLoginId());
			jasperParameter.put("deptId", bean.getDeptId());
			jasperParameter.put("reportName", bean.getReportName());
			jasperParameter.put("rowNum", "ALL");
			jasperParameter.put("pageNum", "ALL");
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/PolicyRegisterReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getJournalReport1(GetJournalReportReq1 bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			String sampledate[]=bean.getOpenperiod().split("-");
			String startDate=sampledate[0];
			String endDate=sampledate[1];
			jasperParameter.put("startDate", startDate);
			jasperParameter.put("endDate", endDate);
			jasperParameter.put("journalId", bean.getJournalId());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("shortname", bean.getShortname());
			
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getJournalId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getJournalId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/journalReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getRetroReport(GetRetroReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("type", bean.getType());
		
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getType().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getType().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/RetrProcessReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getRetroRegisterReport(GetPolicyRegisterReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("countryId", bean.getCountryId());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("uwYear", bean.getUwYear());
			jasperParameter.put("cedingName", bean.getCedingId());
			jasperParameter.put("brokerName", bean.getBrokerId());
			jasperParameter.put("cedingId", bean.getCedingCoName());
			jasperParameter.put("brokerId", bean.getBrokerName());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("loginId", bean.getLoginId());
			jasperParameter.put("deptId", bean.getDeptId());
			jasperParameter.put("reportName", bean.getReportName());
			
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/RetroCessionReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getTreatyWithdrawReport(GetTreatyWithdrawReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("cedingId", "-1".equalsIgnoreCase(bean.getCedingId())?"ALL":bean.getCedingId());
			jasperParameter.put("brokerId", "-1".equalsIgnoreCase(bean.getBrokerId())?"ALL":bean.getBrokerId());
			jasperParameter.put("uwYear", "-1".equalsIgnoreCase(bean.getUwYear())?"ALL":bean.getUwYear());
			jasperParameter.put("class", "-1".equalsIgnoreCase(bean.getTreatyMainClass())?"ALL":bean.getTreatyMainClass());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("treatyType", "-1".equalsIgnoreCase(bean.getTreatyType())?"ALL":bean.getTreatyType());
			jasperParameter.put("withdrawDate", bean.getEndDate());
			
			jasperParameter.put("reportName", bean.getReportName());
		
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/TreatyWithDrawReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getDebtorsAgingReport(GetDebtorsAgingReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			
			jasperParameter.put("type", bean.getType());
			jasperParameter.put("param1", bean.getDebFrom());
			jasperParameter.put("param2", bean.getDebTo());
			jasperParameter.put("param3", bean.getDebFrom1());
			jasperParameter.put("param4", bean.getDebTo1());
			jasperParameter.put("param5", bean.getDebFrom2());
			jasperParameter.put("param6", bean.getDebTo2());
			jasperParameter.put("param7", bean.getDebFrom3());
			jasperParameter.put("param8", bean.getDebTo3());
			jasperParameter.put("param9", bean.getDebFrom4());
			jasperParameter.put("param10", bean.getDebTo4());
			jasperParameter.put("param11", bean.getDebFrom5());
			jasperParameter.put("param12", bean.getDebTo5());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("brokerId", bean.getBrokerId());
			jasperParameter.put("cedingId", bean.getCedingId());
			jasperParameter.put("docType", bean.getDocType());
			jasperParameter.put("reportDate", bean.getStartDate());
			jasperParameter.put("reportName", bean.getReportName());
			
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/DrCrAgeingReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getsoaReport(GetSoaReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("imagePath", bean.getImagePath());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("cedingName", bean.getCedingCoName());
			jasperParameter.put("brokerName", bean.getBrokerName());
			jasperParameter.put("cedingId", bean.getCedingId());
			jasperParameter.put("brokerId", bean.getBrokerId());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("reportName", bean.getReportName());
			jasperParameter.put("settledItems", bean.getSettledItems());
			jasperParameter.put("unAllocatedCash", bean.getUnAllocatedCash());
			jasperParameter.put("prclSeparate", bean.getSaperatePRCLYN());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/SOAReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getAllocationReport(GetAllocationReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			
			HashMap<String,Object> jasperParameter = new HashMap<String,Object>();    		
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("settleType", bean.getSettlementType());
			jasperParameter.put("cedingName", "-1".equalsIgnoreCase(bean.getCedingId())?"ALL":bean.getCedingId());
			jasperParameter.put("brokerName", "-1".equalsIgnoreCase(bean.getBrokerId())?"ALL":bean.getBrokerId());
			jasperParameter.put("allocatedStatus", bean.getAllocateStatus());
			jasperParameter.put("branchCode", bean.getBranchCode());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/AllocationReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
}

	@Override
	public JasperDocumentRes getTransactionPDFReport(GetTransactionPDFReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("imagePath", bean.getImagePath());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("receiptNo", bean.getReceiptNo());
			
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getReceiptNo().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getReceiptNo().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/IssuePayment.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getClaimOslrReport(GetClaimOslrReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("imagePath", bean.getImagePath());
			jasperParameter.put("reportName", bean.getReportName());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("reportDate", bean.getStartDate());
			
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBranchCode().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBranchCode().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/PttyClaimOslrReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getSOAReport1(GetSoaReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("productId", bean.getProductId());
			jasperParameter.put("imagePath", bean.getImagePath());
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("cedingName", bean.getCedingCoName());
			jasperParameter.put("brokerName", bean.getBrokerName());
			jasperParameter.put("cedingId", bean.getCedingId());
			jasperParameter.put("brokerId", bean.getBrokerId());
			jasperParameter.put("systemDate", bean.getSysDate());
			jasperParameter.put("productName", bean.getProductName());
			jasperParameter.put("reportName", bean.getReportName());
			jasperParameter.put("settledItems", bean.getSettledItems());
			jasperParameter.put("unAllocatedCash", bean.getUnAllocatedCash());
			jasperParameter.put("prclSeparate", bean.getSaperatePRCLYN());
		
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBranchCode().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBranchCode().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/SOAReportXls.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getJournalViewDownload(GetJournalViewDownloadReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("branchCode", bean.getBranchCode());
			jasperParameter.put("startDate", bean.getStartDate());
			jasperParameter.put("endDate", bean.getEndDate());
			jasperParameter.put("status", bean.getStatus());
			jasperParameter.put("type",bean.getJournalType());
			jasperParameter.put("typeId", bean.getTypeId());
			jasperParameter.put("reportName", bean.getReportName());
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getTypeId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getTypeId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/JournalViewReport.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}

	@Override
	public JasperDocumentRes getInsallmentDueReport(GetInsallmentDueReportReq bean) {
		JasperDocumentRes res = new JasperDocumentRes();
		String getPdfOutFilePath="";
		try{
			HashMap<String, Object> jasperParameter = new HashMap<String, Object>();
			jasperParameter.put("pvdate", bean.getEndDate());
			jasperParameter.put("Pvbranch", bean.getBranchCode());
			jasperParameter.put("pvProductId", bean.getProductId());
			jasperParameter.put("pvbroker", bean.getBrokerId());
			jasperParameter.put("pvceding",bean.getCedingId());
			jasperParameter.put("pvallocateYN", bean.getAllocationYN());
			
			if(null!=jasperParameter && jasperParameter.size()>0) {
				String directoryname=bean.getBrokerId().replaceAll("[\\/:*?\"<>|]*", "") ;
				String filePath="D:/Jasper/"+directoryname;
				File theDir = new File(filePath);
				if (!theDir.exists()){
				    theDir.mkdirs();
				}			
				getPdfOutFilePath = filePath+"/"+bean.getBrokerId().replace("/", "-")+".pdf";
				res = getJasperPdfFile("/report/InstallmentDue.jrxml",getPdfOutFilePath,jasperParameter);
			}
		}	catch(Exception exception){
			logger.debug("Exception @ { " + exception + " } ");
			exception.printStackTrace();
			return null ;
		}
		logger.info("Filling report finished.........");
		return res ;
	}
}
