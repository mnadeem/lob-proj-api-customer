package com.org.lob.project.messaging.model;

public class BatchProcessEvent {

	private Long id;
	private String path;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "BatchProcessEvent [id=" + id + ", path=" + path + "]";
	}
		
}
