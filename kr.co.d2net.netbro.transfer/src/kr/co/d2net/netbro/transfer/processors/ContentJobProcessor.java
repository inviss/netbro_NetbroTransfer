package kr.co.d2net.netbro.transfer.processors;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import kr.co.d2net.netbro.common.filters.UserFileFilter;
import kr.co.d2net.netbro.common.utils.JSON;
import kr.co.d2net.netbro.transfer.i18n.ClientConfig;
import kr.co.d2net.netbro.transfer.model.IRestfulService;
import kr.co.d2net.netbro.transfer.model.Transfer;
import kr.co.d2net.netbro.transfer.model.providers.TransferProvider;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class ContentJobProcessor {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "eqId")
	private String eqId;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverIp")
	private String serverIp;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverPort")
	private String serverPort;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverCtx")
	private String serverCtx;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverType")
	private String serverType;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobReqPath")
	private String jobReqPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobRepPath")
	private String jobRepPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "storagePath")
	private String storagePath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobIn")
	private String jobInPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "updateSite")
	private String updateSite;
	
	@Inject
	private IRestfulService restfulService;

	@PostConstruct
	public void setPreference(@Preference(nodePath = "kr.co.d2net.netbro.preference") IEclipsePreferences prefs) {
		prefs.put("eqId", StringUtils.defaultIfEmpty(eqId, ClientConfig.eqId));
		prefs.put("serverIp", StringUtils.defaultIfEmpty(serverIp, ClientConfig.serverIp));
		prefs.put("serverPort", StringUtils.defaultIfEmpty(serverPort, ClientConfig.serverPort));
		prefs.put("serverType", StringUtils.defaultIfEmpty(serverType, ClientConfig.serverType));
		prefs.put("serverCtx", StringUtils.defaultIfEmpty(serverCtx, ClientConfig.serverCtx));
		prefs.put("jobReqPath", StringUtils.defaultIfEmpty(jobReqPath, ClientConfig.jobReqPath));
		prefs.put("jobRepPath", StringUtils.defaultIfEmpty(jobRepPath, ClientConfig.jobRepPath));
		prefs.put("storagePath", StringUtils.defaultIfEmpty(storagePath, ClientConfig.storagePath));
		prefs.put("jobIn", StringUtils.defaultIfEmpty(jobInPath, ClientConfig.jobInPath));
		prefs.put("updateSite", StringUtils.defaultIfEmpty(updateSite, ClientConfig.updateSite));
		
		try {
			prefs.flush();
			prefs.sync();
		} catch (BackingStoreException e) {
			logger.error("ContentJobProcessor SET Error", e);
		}
	}
	
	@Execute
	public void execute() {
		Runnable runnable = new Runnable() {
			public void run() {
				while(true) {
					if(TransferProvider.INSTANCE.isEmpty()) {
						Transfer transfer = null;
						try {
							if(logger.isDebugEnabled()) {
								logger.debug("Process serverType : "+serverType);
							}
							if(serverType.equals("R")) {
								String url = "http://"+serverIp+":"+serverPort+serverCtx+jobReqPath+eqId;
								transfer = restfulService.get(url, null, Transfer.class);
								if(transfer != null && transfer.getSeq() != null && transfer.getSeq() > 0) {
									TransferProvider.INSTANCE.put(transfer);
								}
							} else {
								File f = new File(jobInPath);
								if(logger.isDebugEnabled()) {
									logger.debug("watcher folder: "+f.getAbsolutePath());
								}
								if(!f.exists()) f.mkdirs();
								File[] files = f.listFiles(new UserFileFilter(new String[]{"json", "JSON"}));
								for(File file : files) {
									transfer = JSON.toObject(FileUtils.readFileToString(file, "utf-8"), Transfer.class);
									if(transfer != null && transfer.getSeq() != null && transfer.getSeq() > 0) {
										TransferProvider.INSTANCE.put(transfer);
									}
									FileUtils.forceDelete(file);
								}
							}
						} catch (Exception e) {
							logger.error("request job error", e);
						}
					}
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {}
				}
			}
		};
		new Thread(runnable).start();
	}
}
