package cz.twokdent.calendar.uploader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPSClient;

public class FtpUploader {
	public static void upload(String host, int port, String username, String password, List<File> generatedFiles) throws SocketException, IOException {
		FTPSClient ftpsClient = new FTPSClient();
		ftpsClient.connect(host, port);
		ftpsClient.enterLocalPassiveMode();
		ftpsClient.login(username, password);
		ftpsClient.execPBSZ(0);
		ftpsClient.execPROT("P");
		for (final File file : generatedFiles) {
			ftpsClient.storeFile(file.getName(), new FileInputStream(file));
	    }
		ftpsClient.disconnect();
	}
}
