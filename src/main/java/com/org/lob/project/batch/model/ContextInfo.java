package com.org.lob.project.batch.model;

import java.io.Serializable;

public class ContextInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String data;

	public ContextInfo() {
		
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ContextInfo [data=" + data + "]";
	}
}
