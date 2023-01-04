package com.maan.insurance.error;

import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream  extends OutputStream
{
	private OutputStream mChainStream;
	private OutputStream mTeeStream;

	public TeeOutputStream(OutputStream chainStream, OutputStream teeStream)
	{
		mChainStream = chainStream;
		mTeeStream = teeStream;
	}

	@Override
	public void write(int b) throws IOException
	{
		mChainStream.write(b);
		mTeeStream.write(b);
		mTeeStream.flush();
	}

	@Override
	public void close() throws IOException
	{
		flush();
		mChainStream.close();
		mTeeStream.close();
	}

	@Override
	public void flush() throws IOException
	{
		mChainStream.close();
	}

}
