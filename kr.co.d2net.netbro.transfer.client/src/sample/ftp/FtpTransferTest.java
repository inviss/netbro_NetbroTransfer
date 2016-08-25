package sample.ftp;

import org.apache.commons.lang.StringUtils;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;

public class FtpTransferTest {

	public static void main(String[] args) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.setRemoteHost("14.36.147.23");
			ftpClient.setRemotePort(21);
			ftpClient.setAutoPassiveIPSubstitution(false);
			
			if(StringUtils.defaultString(null, "P").toLowerCase().startsWith("p"))
				ftpClient.setConnectMode(FTPConnectMode.PASV);
			else
				ftpClient.setConnectMode(FTPConnectMode.ACTIVE);
			
			ftpClient.setDetectTransferMode(true);
			ftpClient.setStrictReturnCodes(false);
			ftpClient.setControlEncoding("euc-kr");

			ftpClient.connect();
			ftpClient.login("d2net", "elxnspt");
			ftpClient.quitImmediately();
			System.out.println("ftp connected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
