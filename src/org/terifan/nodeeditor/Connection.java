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


	public T getFrom() {
		return mFrom;
	}


	public void setFrom(T aOut) {
		mFrom = aOut;
	}


	public T getTo() {
		return mTo;
	}


	public void setTo(T aIn) {
		mTo = aIn;
	}


	@Override
	public String toString() {
		return "Connection{ " + mFrom + " --> " + mTo + " }";
	}
}
