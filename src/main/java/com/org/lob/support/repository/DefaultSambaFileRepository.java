package com.org.lob.support.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Value;

import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
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
			AuthenticationContext ac = new AuthenticationContext(this.sambaUser, this.sambaPassword.toCharArray(), this.sambaDomain);
			Session session = connection.authenticate(ac);

			// Connect to Share
			try (DiskShare share = (DiskShare) session.connectShare(this.sambaShareName)) {
				callback.doInSession(share);
			}
		}
	}

	@Override
	public void copySambaFile(DiskShare share, String sambaFile, Path localFilePath) throws Exception {
		try (File sf = openFileForRead(share, sambaFile)) {
			Files.copy(sf.getInputStream(), localFilePath, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private File openFileForRead(DiskShare share, String localFilePath) {
		return share.openFile(localFilePath,
				EnumSet.of(AccessMask.GENERIC_READ),
				null,
				SMB2ShareAccess.ALL,
				SMB2CreateDisposition.FILE_OPEN,
				null);
	}
}
