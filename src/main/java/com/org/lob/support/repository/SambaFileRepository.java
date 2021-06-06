package com.org.lob.support.repository;

import java.nio.file.Path;

import com.hierynomus.smbj.share.DiskShare;

public interface SambaFileRepository {

	void execute(SambaShareSessionCallback callback) throws Exception;

	void copySambaFile(DiskShare share, String sambaFile, Path localFilePath) throws Exception;
}
