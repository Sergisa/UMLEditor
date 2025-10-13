package org.terifan.nodeeditor;

import java.io.Serial;
import java.io.Serializable;


public class Connection implements Serializable {
	@Serial
	private final static long serialVersionUID = 1L;

	protected Property mOut;
	protected Property mIn;


	public Connection(Property aOut, Property aIn) {
		mOut = aOut;
		mIn = aIn;
	}


	public Property getOut() {
		return mOut;
	}


	public void setOut(Property aOut) {
		mOut = aOut;
	}


	public Property getIn() {
		return mIn;
	}


	public void setIn(Property aIn) {
		mIn = aIn;
	}


	@Override
	public String toString() {
		return "Connection{" + "mOut=" + mOut + ", mIn=" + mIn + '}';
	}
}
