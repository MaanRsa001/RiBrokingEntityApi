package com.maan.insurance.validation.retroManualAdj;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.maan.insurance.validation.LogUtil;
//@Service
public class Validation {
	private EmailValidator emailValid;
	private static final String NEEDED = "needed";
	private static final String INVALID = "Invalid";
	final static org.slf4j.Logger logger = LogUtil.getLogger(Validation.class);
	public String validString(final String value) {
		String returnval = "";
		try {
			final String val = value.trim();
			
			if (val.length() > 0) {
				
				returnval="";
			} else
			{
				returnval = NEEDED;
			}
		} catch (Exception e) {
			returnval = NEEDED;
		}
		return returnval;
	}

	//Validation to check  <Date2
	public static String ValidateTwo(String Date1,String Date2)
	{
		logger.info("ValidateTwo || Enter ");
		logger.info("Date1=>"+Date1);
		logger.info("Date2=>"+Date2);
		String Result="";
		try {
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2); 
		
		if(dateObj2.compareTo(dateObj1)<0)
		{
			Result="Invalid";
		}
		else 
		{
			Result="";
		}
		} catch (ParseException e) {
		 
			
		}
		logger.info("ValidateTwo || Exit Result "+Result);
		return Result;
	}
	
	//Validation to check Date1<=Date2
	public String getDateValidate(String Date1,String Date2)
	{
		logger.info("ValidateTwo || Enter ");
		logger.info("Date1=>"+Date1);
		logger.info("Date2=>"+Date2);
		String Result="";
		try {
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2);
		if(dateObj2.compareTo(dateObj1)<=0)
		{
			Result="Invalid";
		}
		else 
		{
			Result="";
		}
		logger.info("ValidateTwo || Exit Result "+Result);
		} catch (ParseException e) {
		 
			e.printStackTrace();
		} 
		return Result;
	}
	
	public String currencyvalid(String value)
	{
		String result = "";
		final String Value = value.trim();
		double rate=Double.parseDouble(value);
		for (int i = 0; i < Value.length(); ++i) {
			final char car = Value.charAt(i);
			if (!Character.isDigit(car))
			{
				result = INVALID;
			}
			else
			{
				NumberFormat nf = NumberFormat.getCurrencyInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(2);
				result=nf.format(rate).toString();
			}
		}
		return result;
	}
	
	//float validation
	public String floatvalid(String val)
	{
		String result = "";
		final String Value = val.trim();
		for (int i = 0; i < Value.length(); ++i) {
			final char car = Value.charAt(i);
			if (!Character.isDigit(car))
			{
				result = "INVALID";
			}
			else
			{
				double rate=Double.parseDouble(val);
				result=String.valueOf(rate);
			}
		}
		System.out.print(result);
		return (result);
	}
	
	public String validLength(final String value, final int len) {
		String returnval = "";
		try {
			final String val = value.trim();
			if (val.length() >= len) {
				returnval = "";
			} else
			{
				returnval = NEEDED;
			}
		} catch (Exception e) {
			returnval = NEEDED;
		}
		return returnval;
	}
	public String PresentDate(String Date1,String Date2)
	{
		String Result="";
		try {
			logger.info("Firts Date"+Date1);
			logger.info("Second Date"+Date2);
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2);
	    long diff = dateObj2.getTime() - dateObj1.getTime(); 
		long diffDays = (diff / (24* 1000 * 60 * 60));
		if(diffDays == 0)
		{
			Result="";
		}
		else 
		{
			Result="Invalid";
		}
		} catch (ParseException e) {
			
			e.printStackTrace();
		} 
		return Result;
	}
	public String emailValidate(final String mailId) {
		String returnval = "";
		try {
			emailValid = new EmailValidator();
			
			boolean valid = emailValid.validate(mailId);
			
			if (!valid) {
				returnval = INVALID;
			}
			
			
		} catch (Exception e) {
			returnval = NEEDED;
		}
		return returnval;
	
	}

	static boolean isLegalDate(String s) 
	{
	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    sdf.setLenient(false);
	    return sdf.parse(s, new ParsePosition(0)) != null;
	}
	
	//check date
	public String checkDate(final String strDate) {
		String returnVal = "";
		
		final java.text.ParsePosition pos = new java.text.ParsePosition(0);
		boolean date=isLegalDate(strDate);
		logger.info(" Date " + date);
		
		Pattern datePatt = Pattern.compile("([0-9]{2})/([0-9]{2})/([0-9]{4})");
		Matcher m = datePatt.matcher(strDate);
		if ((date == false) || (pos.getErrorIndex() != -1) || m.matches()==false) {
			if (date == false) 
			{
				logger.info("  if ");
				returnVal = INVALID;
			}
			if (pos.getErrorIndex() != -1)
			{
				logger.info("  if else ");
				returnVal = INVALID;
			}
			if (m.matches()==false) 
			{
				logger.info("  if else else");
				returnVal = INVALID;
			}
			returnVal = INVALID;
		}
		else if(ValidateTwo("01/01/2000",strDate).equalsIgnoreCase("Invalid"))
		{
			logger.info("Date Should be Greater than or equal to 01/01/2000");
			returnVal = INVALID;
		}
		else
		{
			logger.info(" correct ");
			returnVal=strDate;			
		}
		return returnVal;
	}
	
	public String checkDate1(final String strDate) {
		String returnVal = "";
		if (strDate== null ||! strDate.matches("\\d{4}-[01]\\d-[0-3]\\d")) 
			returnVal= "INVALID";
		final java.text.SimpleDateFormat datfor = new java.text.SimpleDateFormat("dd/MM/yyyy");
		datfor.setLenient(false);
			try {
				datfor.parse(strDate);
				returnVal= strDate;
			} catch (java.text.ParseException e) {
				e.printStackTrace();
				returnVal="INVALID";
			}
			return returnVal;
	}

	public String validInteger(final String value) {
		String returnval = "";
		try {
			Long.parseLong(value);
		} catch (Exception e) {
			returnval = INVALID;
		}
		return returnval;
	}
	
	public String numbervalid(final String value) {
		String result = "";
	 	final String Value = value.trim();
	 
		for (int i = 0; i < Value.length();i++) {
			final char car = Value.charAt(i);
			if (!Character.isDigit(car)&&(car!='.')&&(car!='-'))
			{
				result = "INVALID";
				break;
			}
			else if(Character.isWhitespace(car))
			{
				result = "INVALID";	
				break;
			}
			else
			{
				 result="";
			}
		}
		return result;
	}
	
	public String isValidNo(final String value) {
		String result = "";
	 	final String Value = value.trim();
	 
		for (int i = 0; i < Value.length();i++) {
			final char car = Value.charAt(i);
			if (!Character.isDigit(car)&&(car!='.'))
			{
				result = "INVALID";
				break;
			}
			else if(Character.isWhitespace(car))
			{
				result = "INVALID";	
				break;
			}
		}
		return result;
	}
	
	public String percentageValid(final String value) {
		String result = "";
		boolean f=false;
		try
		{
			final String Value = value.trim();
			logger.info("Percentage value--> " + Value);
			for (int i = 0; i < Value.length(); ++i) 
			{
				final char car = Value.charAt(i);
				if (! Character.isDigit(car) && (car!='.'))
				{
					result = "INVALID";
					break;
				}
				else
				{
					result="";
					f=true;
				}
				if(f)
				{
				   try{  
						logger.info("value===>"+value);
						double val=Double.parseDouble(value);
						logger.info("val===>"+val);
						if (val >= 0 && val <= 100) {
							result = String.valueOf(val);
						} else if (val <= 0) {
							logger.info("<<<<");
							result = "less";
						} else {
							logger.info(">>>");
							result = "greater";
						}
				   }
				   catch (Exception exe)
				   {
						result="INVALID";
						break;
				   }
				}
			}
		}
		catch(NumberFormatException num)
		{
			num.printStackTrace();
		}
		return result;
	}
		
	public String contactNumber(String value)
	{
		String result = "";
		final String Value = value.trim();
		for (int i = 0; i < Value.length();i++) {
			final char car = Value.charAt(i);
			if (!Character.isDigit(car))
			{
				result = INVALID;
			}
			else if(value.length() < 10)
			{
				 result=INVALID;
			}
			else
			{
				 result="";
			}
		}
		return result;
	}
	
	public String postiveInteger(String value)
	{
		int val=0;
		val=Integer.parseInt(value.trim());
		if(val>0)
		{
			return value;
		}
		else
		{
			return "INVALID";
		}
	}
	public String notEqualToZero(String value)
	{
		float val=0;
		val=Float.parseFloat(value.trim());
		if(val>0)
		{
			return value;
		}
		else
		{
			return "INVALID";
		}
	}
	
	
	public String negativeInteger(String value)
	{
		int val=0;
		val=Integer.parseInt(value.trim());
		if(val<0)
		{
			return value;
		}
		else
		{
			return "INVALID";
		}
	}

	public String validateVarchar(final String value) {
		String result = "";
		final String Value = value.trim();
		for (int i = 0; i < Value.length(); ++i) {
			final char car = Value.charAt(i);
			if (!Character.isDigit(car) && !Character.isLetter(car))
			{
				result = INVALID;
			}
		}
		return result;
	}
	
	public String validateVarchar2(final String value) {
		String result = "";
		final String Value = value.trim();
		for (int i = 0; i < Value.length(); ++i) {
			final char car = Value.charAt(i);
			if (!Character.isDigit(car) && !Character.isLetter(car))
			{
				result = INVALID;
			}
		}
		return result;
	}
	
	public String validateStringValue(final String value) {
		String result = "";
		final String Value = value.trim();
		for (int i = 0; i < Value.length(); ++i) {
			final char car = Value.charAt(i);
			if (!Character.isLetter(car))
			{
				result = INVALID;
			}
		}
		return result;
	}

	public String validateDate(final int year, final int month, final int day) {
		String result = "";
		if (month == 0 || day == 0 || year == 0) {
			result = INVALID;
		} else if (month == 2 && year % 4 == 0) {
			if (day == 30 || day == 31) {
				result = INVALID;
			}
		} else if (month == 2 && year % 4 != 0) {
			if (day == 29 || day == 30 || day == 31) {
				result = INVALID;
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day == 31) {
				result = INVALID;
			}
		}
		return result;
	}
	
	public String isNull(final String content){
		//final StringBuffer contents = new StringBuffer(1000);
		//if(content==null){
			//contents.append("");
		//}
		//else{
			//contents.append(content);
		//}
		//return contents.toString();
		return StringUtils.isBlank(content)?"":content;
	}
	
	public String isNull(final String content,final String value){
		logger.info("content="+content+"value="+value);
		final StringBuffer contents = new StringBuffer();
		if(content==null||content.length()<=0){
			contents.append(value);
		}
		else{
			contents.append(content);
		}
		return contents.toString();
	}
	
	public String[] isNull(final String[] content){
		String[] contents;
		if(content==null||content.length<=0){
			contents =new String[0];
		}
		else
		{
			contents = content;
		}
		return contents;
	}
	
	public String isSelect(final String content,final String value){
		final StringBuffer contents = new StringBuffer();
		if(content==null || "0".equalsIgnoreCase(content) ||content.length()<=0){
			contents.append(value);
		}
		else{
			contents.append(content);
		}
		return contents.toString();
	}
	
	public String isSelect(final String content){
		final StringBuffer contents = new StringBuffer();
		if(content==null || "0".equalsIgnoreCase(content) ||content.length()<=0){
			contents.append("");
		}
		else
		{
			contents.append(content);
		}
		return contents.toString();
	}

	public String getStringMonth(final int month) {
		String iVal = "";
		if (month == 1)
		{
			iVal = "January";
		}
		else if (month == 2)
		{
			iVal = "February";
		}
		else if (month == 3)
		{
			iVal = "March";
		}
		else if (month == 4)
		{
			iVal = "April";
		}
		else if (month == 5)
		{
			iVal = "May";
		}
		else if (month == 6)
		{
			iVal = "June";
		}
		else if (month == 7)
		{
			iVal = "July";
		}
		else if (month == 8)
		{
			iVal = "August";
		}
		else if (month == 9)
		{
			iVal = "September";
		}
		else if (month == 10)
		{
			iVal = "October";
		}
		else if (month == 11)
		{
			iVal = "November";
		}
		else if (month == 12)
		{
			iVal = "December";
		}
		else
		{
			iVal="INVALID";
		}
		return iVal;
	}
	
	public int getIntegerMonth(final String month) {
		int iVal = 0;
		final Map<String, Object> hsIntMonth = new HashMap<String, Object>();
		hsIntMonth.put("Jan", Integer.valueOf(1));
		hsIntMonth.put("Feb", Integer.valueOf(2));
		hsIntMonth.put("Mar", Integer.valueOf(3));
		hsIntMonth.put("Apr", Integer.valueOf(4));
		hsIntMonth.put("May", Integer.valueOf(5));
		hsIntMonth.put("Jun", Integer.valueOf(6));
		hsIntMonth.put("Jul", Integer.valueOf(7));
		hsIntMonth.put("Aug", Integer.valueOf(8));
		hsIntMonth.put("Sep", Integer.valueOf(9));
		hsIntMonth.put("Oct", Integer.valueOf(10));
		hsIntMonth.put("Nov", Integer.valueOf(11));
		hsIntMonth.put("Dec", Integer.valueOf(12));
		try {
			if (hsIntMonth.get(month) != null) {
				iVal = Integer.parseInt(hsIntMonth.get(month).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return iVal;
	}

	public String isSelectYears(final String value)
	{
		String result = "";
		int val=0;
		val=Integer.parseInt(value.trim());
		Calendar cals=new GregorianCalendar();
		int y=cals.get(Calendar.YEAR);
		if(val==y || val<=y)
		{
		  result="";
		}
		else
		{
			result = INVALID;
		}
		return result;
	}

	public String isSelectMonths(final String value)
	{
		//final StringBuffer val=new StringBuffer();
		String result = "";
		int val=0;
		val=Integer.parseInt(value.trim());
		Calendar cals=new GregorianCalendar();
		int y=cals.get(Calendar.MONTH);
		if(val==y || val<=y)
		{
		  result="";
		}
		else
		{
			result = INVALID;
		}
		return result;
	}
	
	
	
	public static String ValidateTwoDatesdate(String Date1,String Date2)
	{
		String Result="";
		try {
		
		logger.info("_---------------Find The Date Differents-----------------");
		logger.info("Firts Date"+Date1);
		logger.info("Second Date"+Date2);
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2); 
		long diff = dateObj2.getTime() - dateObj1.getTime(); 
		int diffDays =  (int) (diff / (24* 1000 * 60 * 60)); 
		logger.info("diff dates ::::::::  "+diffDays);
		if(diffDays < 365)
		{
			Result="less";
			logger.info("greate");
			
		}
		else 
		{
		Result="";
		logger.info("eeeeee");
		}
		logger.info("--------date Validation Status"+Result);
		} catch (ParseException e) {
		 
			e.printStackTrace();
		} 
		return Result;
	}
	
	public String ValidateTwoDates(String Date1,String Date2)
	{
		String Result="";
		try {
		logger.info("Firts Date--> " + Date1);
		logger.info("Second Date--> " + Date2);
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2); 
		long diff = dateObj2.getTime() - dateObj1.getTime(); 
		int diffDays =  (int) (diff / (24* 1000 * 60 * 60)); 
		logger.info("Diff dates--> " + diffDays);
		if(diffDays < 0 )
		{
			logger.info("less");
			//Result="less";
			Result="Invalid";
			
		}
	
		else 
		{
		Result="";
		}
		} catch (ParseException e) {
		 
			//e.printStackTrace();
		} 
		return Result;
	}
	public String ValidateTwoDates1(String Date1,String Date2)
	{
		String Result="";
		try {
		logger.info("Firts Date--> " + Date1);
		logger.info("Second Date--> " + Date2);
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2); 
		long diff = dateObj2.getTime() - dateObj1.getTime(); 
		int diffDays =  (int) (diff / (24* 1000 * 60 * 60)); 
		logger.info("Diff dates--> " + diffDays);
		if(diffDays <=0 )
		{
			logger.info("less");
			//Result="less";
			Result="Invalid";
			
		}
	
		else 
		{
		Result="";
		}
		} catch (ParseException e) {
		 
			e.printStackTrace();
		} 
		return Result;
	}
	
	public String percentageNewValid(String value) {
		String result = "";
		boolean f=false;
		try
		{
		final String Value = value.trim();
		for (int i = 0; i < Value.length(); ++i) {
			final char car = Value.charAt(i);
			if (! Character.isDigit(car))
			{
				result = "INVALID";
			}
			else
			{
				result="";
				f=true;
				
			}
			if(f)
			{
		 try
		 {
			double val=Double.parseDouble(value);
			 
			if(val>0 &&  val<=100)
			{
				
	           result=String.valueOf(val);
			}else
			{
				result = INVALID;
			}
		 }
		 catch (Exception exe)
		 {
			 result = "INVALID";
			 break;
		 	}
		 }
		}
		}
		catch(NumberFormatException num)
		{
			num.printStackTrace();
		}
		return result;
	}
	
	public String belowCurrentDate(String Date1)
	{
		String Result="";
		try {
		logger.info("First Date--> " + Date1);
		Date Date2=new Date();
		logger.info("Second Date--> " + Date2);
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		 
	    double diff = Date2.getTime() - dateObj1.getTime(); 
		double diffDays = (diff / (24* 1000 * 60 * 60));
		if(diffDays < 0)
		{
			Result="Invalid";
			
		}
		else 
		{
			Result="";
		}
		} catch (ParseException e) {
			
			e.printStackTrace();
		} 
		return Result;
	}
	
	public String CurrentDate(String Date1)
	{
		String Result="";
		try {
		logger.info("Firts Date--> " + Date1);
		Date Date2=new Date();
		logger.info("Second Date--> " + Date2);

		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
	    long diff = Date2.getTime() - dateObj1.getTime(); 
		long diffDays = (diff / (24* 1000 * 60 * 60));
		if(diffDays < 0)
		{
			Result="Invalid";
		}
		else if(diffDays > 0)
		{
			Result="Invalid";
		}
		else 
		{
			Result="";
		}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return Result;
	}
	
	public Object ConvertDate(String inceptionDate) throws ParseException {
		SimpleDateFormat sdfSource = new SimpleDateFormat("dd/mm/yyyy");
		Date date = sdfSource.parse(inceptionDate);
	    SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/mm/yyyy");
	    String strDate = sdfDestination.format(date);
	    return strDate;
	}
	
	public String GetProcedureDate(String inceptionDate) {
		logger.info("Date--> " + inceptionDate);
		String[] Split=inceptionDate.split("/");
		String month=getProcedureMonth(Integer.parseInt(Split[1]));
		return String.valueOf(Split[0]+"/"+month+"/"+Split[2]);
	}
	
	public String getProcedureMonth(final int month) {
		String iVal = "";
		if (month == 1)
		{
			iVal = "Jan";
		}
		else if (month == 2)
		{
			iVal = "Feb";
		}
		else if (month == 3)
		{
			iVal = "Mar";
		}
		else if (month == 4)
		{
			iVal = "Apr";
		}
		else if (month == 5)
		{
			iVal = "May";
		}
		else if (month == 6)
		{
			iVal = "Jun";
		}
		else if (month == 7)
		{
			iVal = "Jul";
		}
		else if (month == 8)
		{
			iVal = "Aug";
		}
		else if (month == 9)
		{
			iVal = "Sep";
		}
		else if (month == 10)
		{
			iVal = "Oct";
		}
		else if (month == 11)
		{
			iVal = "Nov";
		}
		else if (month == 12)
		{
			iVal = "Dec";
		}
		else
		{
			iVal="INVALID";
		}
		return iVal;
	}
	public String ValidateINstallDates(String Date1, String Date2) {
		 
		String Result="";
		try {
		logger.info("Firts Date--> " + Date1);
		logger.info("Second Date--> " + Date2);
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2); 
		long diff = dateObj2.getTime() - dateObj1.getTime(); 
		int diffDays =  (int) (diff / (24* 1000 * 60 * 60)); 
		if(diffDays < 0)
		{
			Result="Invalid";
		}
		else 
		{
		Result="";
		}
		logger.info("Date Validation Status--> " + Result);
		} catch (ParseException e) {
		 
			e.printStackTrace();
		} 
		return Result;
	}
	
	public static String getMaxDateValidate(String Date1,String Date2)
	{
		logger.info("ValidateTwo || Enter ");
		logger.info("Date1=>"+Date1);
		logger.info("Date2=>"+Date2);
		String Result="";
		try {
		if(Date2==null){
			Result=Date1;
		}else{
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2);
		if(dateObj1.compareTo(dateObj2)>0)
		{
			Result=Date1;
		}
		else 
		{
			Result=Date2;
		}
		logger.info("ValidateTwo || Exit Result "+Result);
			}
		} catch (ParseException e) {
		 
			e.printStackTrace();
		} 
		return Result;
	}
	
	public static String getMinDateValidate(String Date1,String Date2)
	{
		logger.info("ValidateTwo || Enter ");
		logger.info("Date1=>"+Date1);
		logger.info("Date2=>"+Date2);
		String Result="";
		try {
		if(Date2==null){
			Result=Date1;
		}else{
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2);
		if(dateObj1.compareTo(dateObj2)<0)
		{
			Result=Date1;
		}
		else 
		{
			Result=Date2;
		}
		logger.info("ValidateTwo || Exit Result "+Result);
			}
		} catch (ParseException e) {
		 
			e.printStackTrace();
		} 
		return Result;
	}
	
	public static String Validatethree(String Date1,String Date2 ,String Date3)
	{
		logger.info("ValidateTwo || Enter ");
		logger.info("Date1=>"+Date1);
		logger.info("Date2=>"+Date2);
		logger.info("Date3=>"+Date3);
		String Result="";
		try {
		String format = "dd/MM/yyyy"; 
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date dateObj1 = sdf.parse(Date1);
		Date dateObj2 = sdf.parse(Date2);
		Date dateObj3=sdf.parse(Date3);
		
		if(dateObj1.compareTo(dateObj3)<=0 && dateObj3.compareTo(dateObj2)>=0)
		{
			Result="Invalid";
		}
		else 
		{
			Result="";
		}
		} catch (ParseException e) {
		 
			
		}
		logger.info("ValidateTwo || Exit Result "+Result);
		return Result;
	}
	public class EmailValidator {
		 
		private Pattern pattern;
		private Matcher matcher;
	 
		private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 
		public EmailValidator() {
			pattern = Pattern.compile(EMAIL_PATTERN);
		}
	 
		/**
		 * Validate hex with regular expression
		 * 
		 * @param hex
		 *            hex for validation
		 * @return true valid hex, false invalid hex
		 */
		public boolean validate(final String hex) {
	 
			matcher = pattern.matcher(hex);
			return matcher.matches();
	 
		}
	}
	
}
