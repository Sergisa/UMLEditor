package org.terifan.nodeeditor;

import java.io.Serial;
import java.io.Serializable;


public class Connection<T> implements Serializable {
	@Serial
	private final static long serialVersionUID = 1L;

	protected T mFrom;
	protected T mTo;


	public Connection(T aOut, T aIn) {
		mFrom = aOut;
		mTo = aIn;
	}


	public T getOut() {
		return mFrom;
	}


	public void setOut(T aOut) {
		mFrom = aOut;
	}


	public T getIn() {
		return mTo;
	}


	public void setIn(T aIn) {
		mTo = aIn;
	}


	@Override
	public String toString() {
		return "Connection{" + "mOut=" + mOut + ", mIn=" + mIn + '}';
	}
}
