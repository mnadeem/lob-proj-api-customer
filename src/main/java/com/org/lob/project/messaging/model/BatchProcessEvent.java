package com.org.lob.project.messaging.model;

import org.springframework.util.ResourceUtils;

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

	public String getFilePath() {
		return ResourceUtils.FILE_URL_PREFIX + getPath();
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "BatchProcessEvent [id=" + id + ", path=" + path + "]";
	}
		
}
