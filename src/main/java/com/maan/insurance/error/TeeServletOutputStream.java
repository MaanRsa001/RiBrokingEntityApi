package com.maan.insurance.error;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class TeeServletOutputStream extends ServletOutputStream {

	private final TeeOutputStream targetStream;

	public TeeServletOutputStream( OutputStream one, OutputStream two ) {
		targetStream = new TeeOutputStream( one, two);
	}

	@Override
	public void write(int arg0) throws IOException {
		this.targetStream.write(arg0);
	}

	public void flush() throws IOException {
		super.flush();
		this.targetStream.flush();
	}

	public void close() throws IOException {
		super.close();
		this.targetStream.close();
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWriteListener(WriteListener listener) {
		// TODO Auto-generated method stub
		
	}	

}
