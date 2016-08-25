package kr.co.d2net.netbro.transfer.client.impl;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import kr.co.d2net.netbro.common.exception.TransferException;
import kr.co.d2net.netbro.common.utils.DStatus;
import kr.co.d2net.netbro.common.utils.TMode;
import kr.co.d2net.netbro.common.utils.Utility;
import kr.co.d2net.netbro.transfer.client.listener.TransferProgressMonitor;
import kr.co.d2net.netbro.transfer.model.EqTbl;
import kr.co.d2net.netbro.transfer.model.IFtpClientService;
import kr.co.d2net.netbro.transfer.model.Transfer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPMessageCollector;
import com.enterprisedt.net.ftp.FTPTransferCancelledException;
import com.enterprisedt.net.ftp.FTPTransferType;

@SuppressWarnings("restriction")
public class FtpClientServiceImpl implements IFtpClientService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	IEventBroker broker;

	@Inject
	@Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverIp")
	private String serverIp;
	@Inject
	@Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverPort")
	private String serverPort;
	@Inject
	@Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverCtx")
	private String serverCtx;
	@Inject
	@Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobRepPath")
	private String jobRepPath;
	@Inject
	@Preference(nodePath = "kr.co.d2net.netbro.preference", value = "storagePath") 
	private String storagePath;

	private FTPMessageCollector listener;

	@Override
	public void upload(EqTbl eqTbl, Transfer transfer) throws Exception {
		FTPClient ftpClient = ftpConnect(transfer);

		/*
		eqTbl.setServerIp(serverIp);
		eqTbl.setServerPort(serverPort);
		eqTbl.setServerCtx(serverCtx);
		eqTbl.setJobRepPath(jobRepPath);
		 */
		try {
			File drive = new File(storagePath+"/");
			if(!drive.exists()) {
				throw new TransferException('5', "Storage Dirve Not Found", null);
			}
			if(!transfer.getFlPath().startsWith("/")) transfer.setFlPath("/"+transfer.getFlPath());
			if(!transfer.getFlPath().endsWith("/")) transfer.setFlPath(transfer.getFlPath()+"/");

			if(logger.isDebugEnabled()) {
				logger.debug("local file path: "+storagePath+transfer.getFlPath()+transfer.getOrgFileNm()+"."+transfer.getFlExt());
			}

			File f = new File(storagePath+transfer.getFlPath(), transfer.getOrgFileNm()+"."+transfer.getFlExt());
			if(f.exists()) {
				transfer.setFileSize(f.length());

				/* 원격지에 저장할 디렉토리 생성 */
				String remoteDir = "";
				if(StringUtils.isNotBlank(transfer.getRemoteDir())) {
					remoteDir = transfer.getRemoteDir().trim();
					if(remoteDir.length() != 1 && !remoteDir.equals("/")) {
						if(remoteDir.startsWith("/"))
							remoteDir = remoteDir.substring(1);
						if(remoteDir.endsWith("/"))
							remoteDir = remoteDir.substring(0, remoteDir.lastIndexOf("/"));

						if(remoteDir.indexOf("/") > -1) {
							String[] dirs = remoteDir.split("\\/");
							for(String dir : dirs) {
								if(!ftpClient.existsDirectory(dir)) {
									ftpClient.mkdir(dir);
									ftpClient.chdir(dir);
								}else{
									ftpClient.chdir(dir);
								}
							}
						}else{
							if(!ftpClient.existsDirectory(remoteDir)) {
								ftpClient.mkdir(remoteDir);
								ftpClient.chdir(remoteDir);
							}else{
								ftpClient.chdir(remoteDir);
							}
						}
					}
				}


				ftpClient.setProgressMonitor(new TransferProgressMonitor(transfer, broker), 3000);

				ftpClient.put(f.getAbsolutePath(), transfer.getWrkFileNm()+"."+transfer.getFlExt(), false); // append false
				long remoteSize = ftpClient.size(transfer.getWrkFileNm());

				String messages = listener.getLog();
				if(logger.isDebugEnabled()) {
					logger.debug("messages: "+messages);
				}
				if(transfer.getFileSize() != remoteSize) {
					if(logger.isInfoEnabled()) {
						logger.info("local size: "+transfer.getFileSize()+", remote file size: "+remoteSize);
					}
					ftpClient.delete(transfer.getWrkFileNm());
					// Remote Write Error
					throw new TransferException('6', "Local and Remote file's size are different", null);
				}

				ftpClient.quit();
			} else throw new TransferException('4', "File Not Found", null);
		} catch (IOException e) {
			if(e instanceof UnknownHostException) {
				throw new TransferException('1', e);
			} else if(e instanceof SocketTimeoutException) {
				// Remote Read Error
				throw new TransferException('2', e);
			} else if(e instanceof SocketException) {
				// Remote Write Error
				throw new TransferException('3', e);
			}
		} catch (FTPException e) {
			if(e instanceof FTPTransferCancelledException) {
				// Transfer Canceled
				throw new TransferException('8', e);
			} else throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if(ftpClient != null) {
				if (ftpClient.connected()) {
					ftpClient.quitImmediately();
				}
			}
			ftpClient = null;
		}
	}

	@Override
	public void download(EqTbl eqTbl, Transfer transfer) throws Exception {

	}

	private FTPClient ftpConnect(Transfer transfer) throws Exception {
		FTPClient ftpClient = new FTPClient();

		try {
			ftpClient.setRemoteHost(transfer.getFtpIp());
			ftpClient.setRemotePort(transfer.getFtpPort());
			ftpClient.setAutoPassiveIPSubstitution(false);

			if(StringUtils.defaultString(transfer.getConnectMode(), "P").toLowerCase().startsWith("p"))
				ftpClient.setConnectMode(FTPConnectMode.PASV);
			else
				ftpClient.setConnectMode(FTPConnectMode.ACTIVE);

			ftpClient.setDetectTransferMode(true);
			ftpClient.setStrictReturnCodes(false);
			//ftpClient.setControlEncoding("euc-kr");
			ftpClient.setControlEncoding("utf-8");

			ftpClient.connect();
			ftpClient.login(transfer.getUserId(), transfer.getUserPass());
			
			if(StringUtils.defaultString(transfer.getTransferMode(), "B").toLowerCase().startsWith("b"))
				ftpClient.setType(FTPTransferType.BINARY);
			else
				ftpClient.setType(FTPTransferType.ASCII);
		} catch (ConnectException e) {
			throw new TransferException('0', e);
		} catch (Exception e) {
			throw e;
		}

		return ftpClient;
	}

	@Override
	public void connectionCheck(Transfer transfer) throws Exception {
		if(logger.isInfoEnabled()) {
			logger.info("ftp_ip: "+transfer.getFtpIp()+", ftp_port: "+transfer.getFtpPort());
			logger.info("conn_mode: "+transfer.getConnectMode()+", trsf_mode: "+transfer.getTransferMode());
		}
		
		FTPClient ftpClient = null;
		try {
			ftpConnect(transfer);
		} finally {
			if(ftpClient != null) {
				if (ftpClient.connected()) {
					ftpClient.quitImmediately();
				}
			}
			ftpClient = null;
		}
		
	}

	@Override
	public void uploadCheck(Transfer transfer) throws Exception {
		FTPClient ftpClient = ftpConnect(transfer);
		try {
			if(logger.isDebugEnabled()) {
				logger.debug("local file path: "+transfer.getOrgFileNm());
			}

			File f = new File(transfer.getOrgFileNm());
			if(f.exists()) {
				transfer.setFileSize(f.length());
				if(StringUtils.isBlank(transfer.getWrkFileNm()) || transfer.getTransferType() == TMode.SAMPLE) {
					transfer.setWrkFileNm(Utility.getWantDay(0, "yyyyMMddHHmmss", DStatus.DEFAULT)+"."+transfer.getFlExt());
				}

				/* 원격지에 저장할 디렉토리 생성 */
				String remoteDir = StringUtils.defaultIfEmpty(transfer.getRemoteDir(), "sample");
				if(remoteDir.length() != 1 && !remoteDir.equals("/")) {
					if(remoteDir.startsWith("/"))
						remoteDir = remoteDir.substring(1);
					if(remoteDir.endsWith("/"))
						remoteDir = remoteDir.substring(0, remoteDir.lastIndexOf("/"));

					if(remoteDir.indexOf("/") > -1) {
						String[] dirs = remoteDir.split("\\/");
						for(String dir : dirs) {
							if(!ftpClient.existsDirectory(dir)) {
								ftpClient.mkdir(dir);
								ftpClient.chdir(dir);
							}else{
								ftpClient.chdir(dir);
							}
						}
					}else{
						if(!ftpClient.existsDirectory(remoteDir)) {
							ftpClient.mkdir(remoteDir);
							ftpClient.chdir(remoteDir);
						}else{
							ftpClient.chdir(remoteDir);
						}
					}
				}
			}
			
			ftpClient.setProgressMonitor(new TransferProgressMonitor(transfer, broker), 3000);
			ftpClient.put(f.getAbsolutePath(), transfer.getWrkFileNm(), false); // append false
		} catch (IOException e) {
			if(e instanceof UnknownHostException) {
				throw new TransferException('1', e);
			} else if(e instanceof SocketTimeoutException) {
				// Remote Read Error
				throw new TransferException('2', e);
			} else if(e instanceof SocketException) {
				// Remote Write Error
				throw new TransferException('3', e);
			}
		} catch (FTPException e) {
			if(e instanceof FTPTransferCancelledException) {
				// Transfer Canceled
				throw new TransferException('8', e);
			} else throw e;
		} catch (Exception e) {
			throw e;
		} finally {
			if(ftpClient != null) {
				if (ftpClient.connected()) {
					ftpClient.quitImmediately();
				}
			}
			ftpClient = null;
		}
	}

}
