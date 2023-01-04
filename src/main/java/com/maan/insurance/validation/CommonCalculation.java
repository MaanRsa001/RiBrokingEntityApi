package com.maan.insurance.validation;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.insurance.model.req.GenerationReq;
import com.maan.insurance.model.req.PaymentRecieptReq;
import com.maan.insurance.model.req.claim.InsertCliamDetailsMode3Req;
import com.maan.insurance.model.req.journal.InsertManualJVReq;
import com.maan.insurance.model.req.journal.LedgerIdReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq;
import com.maan.insurance.model.req.premium.SubmitPremiumReservedReq1;



@Service
public class CommonCalculation {
	@Autowired
	private Formatters fm;
	private Logger logger = LogManager.getLogger(CommonCalculation.class);
	public String calculateTreasuryPayment(PaymentRecieptReq bean, String type, int i) {
		String result ="";
		double amt = 0;
		try{
			if("netAmt".equalsIgnoreCase(type)){
				logger.info("Jsp Calculation Net Amount "+bean.getNetAmount());
				String premiumRate=StringUtils.isBlank(bean.getPaymentAmount())?"0":bean.getPaymentAmount().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(bean.getBankCharges())?"0":bean.getBankCharges().replaceAll(",", "");
				String coverlimit1=StringUtils.isBlank(bean.getWithHoldingTaxOC())?"0":bean.getWithHoldingTaxOC().replaceAll(",", "");
				String pemiumLavy=StringUtils.isBlank(bean.getPremiumLavy())?"0":bean.getPremiumLavy().replaceAll(",", "");
				if("PT".equalsIgnoreCase(bean.getTransType())){
					amt = (Double.parseDouble(premiumRate) +  Double.parseDouble(coverlimit)) - Double.parseDouble(coverlimit1)- Double.parseDouble(pemiumLavy);
				}else{
					amt = (Double.parseDouble(premiumRate) -  Double.parseDouble(coverlimit)) - Double.parseDouble(coverlimit1)- Double.parseDouble(pemiumLavy);
				}
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Net Amount "+result);
			}
			else if("baseCur".equalsIgnoreCase(type)){
				logger.info("Jsp Calculation Base Currency Amount "+bean.getBaseCurrencyAmount());
				String premiumRate=StringUtils.isBlank(bean.getPaymentAmount())?"0":bean.getPaymentAmount().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(bean.getExchangeRate())?"0":bean.getExchangeRate().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) / Double.parseDouble(coverlimit));
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Base Currency Amount "+result);
			}else if("ConRec".equalsIgnoreCase(type)){
				GenerationReq req=bean.getGenerationReq().get(i);
				logger.info("Jsp Calculation Converted Paid Amount "+req.getConRecCurValList() + " in Row number " +i);
				String premiumRate=StringUtils.isBlank(req.getPayamountValList())?"0":req.getPayamountValList().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req.getSetExcRateValList())?"0":req.getSetExcRateValList().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) / Double.parseDouble(coverlimit));
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Converted Paid Amount "+result + " in Row number" +i);
			}else if("totalAmt".equalsIgnoreCase(type)){
				GenerationReq req=bean.getGenerationReq().get(i);
				logger.info("Jsp Calculation Total Amount "+req.getRowamountValList() + "in Row number " +i);
				String premiumRate=StringUtils.isBlank(req.getPayamountValList())?"0":req.getPayamountValList().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req.getExachangeValList())?"0":req.getExachangeValList().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) / Double.parseDouble(coverlimit));
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Total Amount "+result + " in Row number" +i);
			}else if("setExchangeRate".equalsIgnoreCase(type)){
				GenerationReq req=bean.getGenerationReq().get(i);
				logger.info("Jsp Calculation Client Exchange Rate "+req.getSetExcRateValList() + "in Row number " +i);
				String premiumRate=StringUtils.isBlank(req.getPayamountValList())?"0":req.getPayamountValList().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req.getConRecCurValList())?"0":req.getConRecCurValList().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate) / Double.parseDouble(coverlimit));
				result =  fm.formattereight(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Client Exchange Rate "+result + " in Row number " +i);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.debug(""+e);
		}
		return result;
	}
	public String calculatePTTYPopUp(SubmitPremiumReservedReq bean, String type, int i) {
		String result ="";
		double amt = 0;
		SubmitPremiumReservedReq1 req1 = bean.getReqList().get(i);
		try{
			if("PremiRes".equalsIgnoreCase(type)){
				
				
				String premiumRate=StringUtils.isBlank(req1.getCreditAmountCLD())?"0":req1.getCreditAmountCLD().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req1.getCLCsettlementRate())?"0":req1.getCLCsettlementRate().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate)/Double.parseDouble(coverlimit));
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}else if("CashLoss".equalsIgnoreCase(type)){
				
				String premiumRate=StringUtils.isBlank(req1.getCreditAmountCLC())?"0":req1.getCreditAmountCLC().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(req1.getCLCsettlementRate())?"0":req1.getCLCsettlementRate().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate)/Double.parseDouble(coverlimit));
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.debug(""+e);
		}
		return result;
	}
	public String calculateClaimPayment(InsertCliamDetailsMode3Req bean, String type) {
		String result ="";
		double amt = 0;
		try{
			if("Total".equalsIgnoreCase(type)){
				logger.info("Jsp Calculation Total Claims Paid - Our Share - OC "+bean.getPaidAmountOrigcurr() );
				String premiumRate=StringUtils.isBlank(bean.getPaidClaimOs())?"0":bean.getPaidClaimOs().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(bean.getSurveyorfeeos())?"0":bean.getSurveyorfeeos().replaceAll(",", "");
				String premiumRate1=StringUtils.isBlank(bean.getOtherproffeeos())?"0":bean.getOtherproffeeos().replaceAll(",", "");
				
				amt = (Double.parseDouble(premiumRate) + Double.parseDouble(coverlimit) + Double.parseDouble(premiumRate1));
				if("3".equalsIgnoreCase(bean.getProductId())){
					String coverlimit1=StringUtils.isBlank(bean.getReinstPremiumOCOS())?"0":bean.getReinstPremiumOCOS().replaceAll(",", "");
					amt =  amt - Double.parseDouble(coverlimit1);
				}
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Total Claims Paid - Our Share - OC "+result );
			}
		}
		catch(Exception e){
			e.printStackTrace();
			logger.debug(""+e);
		}
		return result;
	}

	public String calculateManualJournal(InsertManualJVReq bean, String type, int i) {
		String result ="";
		double amt = 0;
		try{
			LedgerIdReq req = bean.getLedgerIdReq().get(i);
			if("CreditDC".equalsIgnoreCase(type)){
				logger.info("Jsp Calculation Credit DC  "+req.getCreditDC().replaceAll(",", "") + " in Row number " +i);
				String premiumRate=StringUtils.isBlank(req.getCreditOC())?"0":req.getCreditOC().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(bean.getExchRate())?"0":bean.getExchRate().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate)/Double.parseDouble(coverlimit));
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Credit DC   "+result + " in Row number " +i);
			}else if("DebitDC".equalsIgnoreCase(type)){
				logger.info("Jsp Calculation Debit DC  "+req.getDebitDC().replaceAll(",", "") + " in Row number " +i);
				String premiumRate=StringUtils.isBlank(req.getDebitOC())?"0":req.getDebitOC().replaceAll(",", "");
				String coverlimit=StringUtils.isBlank(bean.getExchRate())?"0":bean.getExchRate().replaceAll(",", "");
				amt = (Double.parseDouble(premiumRate)/Double.parseDouble(coverlimit));
				result =  fm.formatter(Double.toString(amt)).replaceAll(",", "");
				logger.info("Java Calculation Debit DC   "+result + " in Row number " +i);
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
			logger.debug(""+e);
		}
		return result;
	}	
}
