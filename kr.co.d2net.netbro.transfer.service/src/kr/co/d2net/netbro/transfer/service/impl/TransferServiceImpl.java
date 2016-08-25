package kr.co.d2net.netbro.transfer.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import kr.co.d2net.netbro.common.exception.TransferException;
import kr.co.d2net.netbro.common.utils.DStatus;
import kr.co.d2net.netbro.common.utils.JSON;
import kr.co.d2net.netbro.common.utils.Utility;
import kr.co.d2net.netbro.transfer.model.EqTbl;
import kr.co.d2net.netbro.transfer.model.IFtpClientService;
import kr.co.d2net.netbro.transfer.model.IRestfulService;
import kr.co.d2net.netbro.transfer.model.ITransferService;
import kr.co.d2net.netbro.transfer.model.Status;
import kr.co.d2net.netbro.transfer.model.Transfer;
import kr.co.d2net.netbro.transfer.model.providers.TransferProvider;

import org.apache.commons.io.FileUtils;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class TransferServiceImpl implements ITransferService {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf1") 
	private String usetf1;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf2") 
	private String usetf2;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf3") 
	private String usetf3;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf4") 
	private String usetf4;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "eqId")
	private String eqId;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverIp")
	private String serverIp;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverPort")
	private String serverPort;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverType")
	private String serverType;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverCtx")
	private String serverCtx;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobRepPath")
	private String jobRepPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "statRep")
	private String statRepPath;

	@Inject
	private IFtpClientService ftpClientService;
	@Inject
	private IRestfulService restfulService;

	@Inject
	private IEventBroker eventBroker;

	private volatile ExecutorService transferPool = Executors.newFixedThreadPool(4);

	private String url = null;

	@Inject
	public void init() {
		if(logger.isDebugEnabled()) {
			logger.debug("####################### "+eqId+" Started ##################");
		}
		url = "http://"+serverIp+":"+serverPort+serverCtx+jobRepPath;

		EqTbl eqTbl = null;
		for(int i=1; i<=4; i++) {
			eqTbl = new EqTbl();
			eqTbl.setNum(i);
			eqTbl.setEqId(eqId);
			transferPool.execute(new TransferUpload(eqTbl));
		}
	}

	class TransferUpload implements Runnable {

		private EqTbl eqTbl;
		private Status status = new Status();

		public TransferUpload(EqTbl eqTbl) {
			this.eqTbl = eqTbl;
			status.setDeviceId(eqTbl.getEqId());
			status.setDeviceNum(eqTbl.getNum());
		}

		@Override
		public void run() {
			while(true) {
				boolean process = true;
				switch(eqTbl.getNum()) {
				case 1:
					if("N".equals(usetf1)) process = false;
					else process = true;
					break;
				case 2:
					if("N".equals(usetf2)) process = false;
					else process = true;
					break;
				case 3:
					if("N".equals(usetf3)) process = false;
					else process = true;
					break;
				case 4:
					if("N".equals(usetf4)) process = false;
					else process = true;
					break;
				}
				if(logger.isDebugEnabled()) {
					logger.debug("tf_id: "+eqTbl.getEqId()+"_"+eqTbl.getNum()+", process: "+process);
				}

				if(process) {
					if(!TransferProvider.INSTANCE.isEmpty()) {
						Transfer transfer = TransferProvider.INSTANCE.get();
						transfer.setDeviceId(eqTbl.getEqId());
						transfer.setDeviceNum(eqTbl.getNum());
						try {
							/* 임시로 wrkFileName -> orgFileNm으로 복사 */
							transfer.setFtpIp("14.36.147.23");
							transfer.setFtpPort(21);
							transfer.setUserId("d2net");
							transfer.setUserPass("elxnspt");
							transfer.setOrgFileNm(transfer.getWrkFileNm());
							transfer.setWrkFileNm(Utility.getWantDay(0, "yyyyMMddHHmmss", DStatus.DEFAULT));

							// FTP 전송로직 구현
							ftpClientService.upload(eqTbl, transfer);
						} catch (Exception e) {
							logger.error("ftp upload error", e);
							if(e instanceof TransferException) {
								TransferException te = (TransferException)e;
								transfer.setErrorCode(te.getErrorCode());
							} else {
								transfer.setErrorCode('9');
							}
							transfer.setWorkStatcd("005");
							eventBroker.post("TOPIC_TF"+transfer.getDeviceNum()+"/STATUS", transfer);
						}
					} else {
						status.setErrorCd(null);
						status.setProgress(null);
						status.setSeq(null);
						status.setWrkStatcd(null);
						status.setDeviceState("W");
						restfulService.post(url, null, status);
					}
				}

				try {
					Thread.sleep(3000L);
				} catch (Exception e) {}
			}
		}

	}

	@Inject @Optional
	public void  hookEvents() {
		eventBroker.subscribe("REPORT", new EventHandler() {
			@Override
			public void handleEvent(Event event) {
				Transfer transfer = null;
				if(event.containsProperty("TRANSFER_1")) {
					transfer = (Transfer)event.getProperty("TRANSFER_1");
				} else if(event.containsProperty("TRANSFER_2")) {
					transfer = (Transfer)event.getProperty("TRANSFER_2");
				} else if(event.containsProperty("TRANSFER_3")) {
					transfer = (Transfer)event.getProperty("TRANSFER_3");
				} else if(event.containsProperty("TRANSFER_4")) {
					transfer = (Transfer)event.getProperty("TRANSFER_4");
				}

				if(transfer != null) {
					Status status = new Status();
					status.setDeviceId(transfer.getDeviceId());
					status.setDeviceNum(transfer.getDeviceNum());
					status.setErrorCd(String.valueOf(transfer.getErrorCode()));
					status.setProgress(transfer.getProgress());
					status.setSeq(transfer.getSeq());
					if(transfer.getProgress() != null && transfer.getProgress() == 100) 
						status.setWrkStatcd("004");
					else status.setWrkStatcd(transfer.getWorkStatcd());
					status.setDeviceState("B");

					// Restful
					if(serverType.equals("R"))
						restfulService.post(url, null, status);
					else {
						File tmp = new File(statRepPath, Utility.getWantDay(0, status.getSeq()+"_yyyyMMddHHmmss", DStatus.DEFAULT)+".tmp");
						File json = new File(statRepPath, Utility.getWantDay(0, status.getSeq()+"_yyyyMMddHHmmss", DStatus.DEFAULT)+".json");
						try {
							FileUtils.writeStringToFile(tmp, JSON.toString(status), "utf-8");
							tmp.renameTo(json);
						} catch (IOException e) {
							logger.error("JSON file write error", e);
						}
					}
				}
			}
		});
	}

	@PreDestroy
	public void destory() {
		try {
			if(!transferPool.isShutdown()) {
				transferPool.shutdownNow();
				if(logger.isInfoEnabled()) {
					logger.info("PutPool Thread shutdown now!!");
				}
			}
		} catch (Exception e) {}
	}

	@Override
	public boolean connectionCheck(Transfer transfer) {
		try {
			ftpClientService.connectionCheck(transfer);
		} catch (Exception e) {
			logger.error("connectionCheck error", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean uploadCheck(Transfer transfer) {
		try {
			ftpClientService.uploadCheck(transfer);
		} catch (Exception e) {
			logger.error("uploadCheck error", e);
			return false;
		}
		return true;
	}

	@Override
	public void manualRegister(Transfer transfer) {
		uploadCheck(transfer);
	}

}
