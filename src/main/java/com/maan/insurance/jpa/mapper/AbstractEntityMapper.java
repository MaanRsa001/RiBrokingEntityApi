package com.maan.insurance.jpa.mapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractEntityMapper<E,D> {

	public abstract D fromEntity(E input);
	public abstract E toEntity(D input) throws ParseException;
	
	public List<D> fromEntity(List<E> input){
		List<D> result = new ArrayList<>();
		for(E in : input) {
			result.add(fromEntity(in));
		}
		return result;
	}
	
	public List<E> toEntity(List<D> input) throws ParseException{
		List<E> result = new ArrayList<>();
		for(D in : input) {
			result.add(toEntity(in));
		}
		return result;
	}
	
	public Integer format(String input) {
		return input != null ? Integer.parseInt(input) : null;  
	}
	
	public Double formatDouble(String input) {
		return input != null ? Double.parseDouble(input) : null;  
	}
	
	public Long formatLong(String input) {
		return input != null ? Long.parseLong(input) : null;  
	}
	
	public Date formatDate(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = sdf1.parse(input);
		return new java.sql.Date(date.getTime());
	}
	
	public java.util.Date parseDateLocal(String input) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("DD/MM/YYYY");
		return sdf1.parse(input);
	}
	
	public Timestamp getTimestamp(String date) throws ParseException {
		final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		final java.util.Date d = formatter.parse(date);
		((SimpleDateFormat) formatter).applyPattern("yyyy-MM-dd hh:mm:ss");
		String newDateString = formatter.format(d);
		return Timestamp.valueOf(newDateString);
	}
	
	public BigDecimal formatBigDecimal(String input) {
		if(StringUtils.isBlank(input))
			return new BigDecimal("0.0");
		return new BigDecimal(input);
	}
	
	protected String GetDesginationCountry(final String limitOrigCur,final String ExchangeRate) {
		String valu="0";
		if (StringUtils.isNotBlank(limitOrigCur)&& Double.parseDouble(limitOrigCur) != 0) {
			double originalCountry = Double.parseDouble(limitOrigCur)/ Double.parseDouble(ExchangeRate);
			DecimalFormat myFormatter = new DecimalFormat("###0.00");
			final double dround = Math.round(originalCountry * 100.0) / 100.0;
			valu = myFormatter.format(dround);
		}
		return valu;
	}
	
	public Timestamp getCurrentTimestamp() throws ParseException {
		final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		final java.util.Date d = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		((SimpleDateFormat) formatter).applyPattern("yyyy-MM-dd hh:mm:ss");
		String newDateString = formatter.format(d);
		return Timestamp.valueOf(newDateString);
	}
	public java.util.Date  getCurrentDate() throws ParseException {
		final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		final java.util.Date d = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		//String newDateString = formatter.format(d);
		return d;
	}
}
