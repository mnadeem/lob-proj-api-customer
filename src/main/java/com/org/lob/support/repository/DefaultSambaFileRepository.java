package com.org.lob.support.repository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Value;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2CreateOptions;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

public class DefaultSambaFileRepository implements SambaFileRepository {

	@Value("${samba.share.server}")
	private String sambaServer;
	@Value("${samba.share.user}")
	private String sambaUser;
	@Value("${samba.share.password}")
	private String sambaPassword;
	@Value("${samba.share.domain}")
	private String sambaDomain;
	@Value("${samba.share.name}")
	private String sambaShareName;

	private final SMBClient smbClient;

	public DefaultSambaFileRepository(SMBClient smbClient) {
		this.smbClient = smbClient;
	}

	@Override
	public void execute(SambaShareSessionCallback callback) throws Exception {
		try (Connection connection = this.smbClient.connect(this.sambaServer)) {
			AuthenticationContext ac = new AuthenticationContext(this.sambaUser, this.sambaPassword.toCharArray(),
					this.sambaDomain);
			Session session = connection.authenticate(ac);

			// Connect to Share
			try (DiskShare share = (DiskShare) session.connectShare(this.sambaShareName)) {
				callback.doInSession(share);
			}
		}
	}

	@Override
	public void readSambaFile(DiskShare share, String sourceSambaFilePath, Path targetLocalPath) throws Exception {
		try (File sourceSambaFile = openSambaFileForRead(share, sourceSambaFilePath)) {
			Files.copy(sourceSambaFile.getInputStream(), targetLocalPath, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private File openSambaFileForRead(DiskShare share, String sourceSambaFilePath) {
		return share.openFile(sourceSambaFilePath, EnumSet.of(AccessMask.GENERIC_READ), null, SMB2ShareAccess.ALL,
				SMB2CreateDisposition.FILE_OPEN, null);
	}

	@Override
	public void writeSambaFile(DiskShare share, Path sourceLocalPath, String targetSambaFilePath) throws Exception {

		createFolders(share, targetSambaFilePath);

		try (File targetSambaFile = openSambaFileForWrite(share, targetSambaFilePath);
				OutputStream targetOutputStream = targetSambaFile.getOutputStream()) {
			Files.copy(sourceLocalPath, targetOutputStream);
		}
	}

	private void createFolders(DiskShare share, String targetSambaFilePath) throws IOException {
		int idx = targetSambaFilePath.lastIndexOf("/");

		if (idx > -1) {
			String folder = targetSambaFilePath.substring(0, idx);
			try {
				if (!share.folderExists(folder)) {
					share.mkdir(folder);
				}
			} catch (SMBApiException ex) {
				throw new IOException(ex);
			}
		}
	}

	private File openSambaFileForWrite(DiskShare share, String targetSambaFilePath) {
		return share.openFile(targetSambaFilePath, EnumSet.of(AccessMask.GENERIC_ALL),
				EnumSet.of(FileAttributes.FILE_ATTRIBUTE_NORMAL), SMB2ShareAccess.ALL,
				SMB2CreateDisposition.FILE_OVERWRITE_IF, EnumSet.of(SMB2CreateOptions.FILE_RANDOM_ACCESS));
	}
}
