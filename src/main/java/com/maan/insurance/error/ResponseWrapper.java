package com.maan.insurance.error;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper  {

	HttpServletResponse original;
	TeeServletOutputStream tee;
	ByteArrayOutputStream bos;

	public ResponseWrapper(HttpServletResponse response) {
		super(response);
		original=response;
	}
	public PrintWriter getWriter() throws IOException {
		return original.getWriter();
	}
	public ServletOutputStream getOutputStream() throws IOException {
		if( tee == null ){
			bos = new ByteArrayOutputStream();
			tee = new TeeServletOutputStream( original.getOutputStream(), bos );
		}
		return tee;
	}

	public String getContent() {	
		return bos==null?null:bos.toString();
	}
}
 