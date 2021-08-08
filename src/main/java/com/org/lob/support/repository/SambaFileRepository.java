package com.org.lob.support.repository;

import java.nio.file.Path;

import com.hierynomus.smbj.share.DiskShare;

public interface SambaFileRepository {

	void execute(SambaShareSessionCallback callback) throws Exception;

	void readSambaFile(DiskShare share, String sourceSambaFilePath, Path targetLocalPath) throws Exception;

	void writeSambaFile(DiskShare share, Path sourceLocalPath, String targetSambaFile) throws Exception;
}
