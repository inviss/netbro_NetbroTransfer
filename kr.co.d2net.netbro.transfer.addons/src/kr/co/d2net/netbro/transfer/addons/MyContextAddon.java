package kr.co.d2net.netbro.transfer.addons;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import kr.co.d2net.netbro.transfer.i18n.ClientConfig;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.extensions.Preference;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class MyContextAddon {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value="usetf1") String usetf1;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value="usetf2") String usetf2;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value="usetf3") String usetf3;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value="usetf4") String usetf4;
	
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "eqId") String eqId;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverIp") String serverIp;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverPort") String serverPort;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverType") String serverType;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverCtx") String serverCtx;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobReqPath") String jobReqPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobRepPath") String jobRepPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "storagePath") String storagePath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "jobIn") String jobInPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "statRep") String statRepPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "updateSite") String updateSite;
	
	@PostConstruct
	public void setPreference(
			@Preference(nodePath = "kr.co.d2net.netbro.transfer") IEclipsePreferences prefs1,
			@Preference(nodePath = "kr.co.d2net.netbro.preference") IEclipsePreferences prefs2) {
		
		prefs1.put("usetf1", StringUtils.defaultIfEmpty(usetf1, "Y"));
		prefs1.put("usetf2", StringUtils.defaultIfEmpty(usetf2, "Y"));
		prefs1.put("usetf3", StringUtils.defaultIfEmpty(usetf3, "Y"));
		prefs1.put("usetf4", StringUtils.defaultIfEmpty(usetf4, "Y"));
		
		prefs2.put("eqId", StringUtils.defaultIfEmpty(eqId, ClientConfig.eqId));
		prefs2.put("serverIp", StringUtils.defaultIfEmpty(serverIp, ClientConfig.serverIp));
		prefs2.put("serverPort", StringUtils.defaultIfEmpty(serverPort, ClientConfig.serverPort));
		prefs2.put("serverType", StringUtils.defaultIfEmpty(serverType, ClientConfig.serverType));
		prefs2.put("serverCtx", StringUtils.defaultIfEmpty(serverCtx, ClientConfig.serverCtx));
		prefs2.put("jobReqPath", StringUtils.defaultIfEmpty(jobReqPath, ClientConfig.jobReqPath));
		prefs2.put("jobRepPath", StringUtils.defaultIfEmpty(jobRepPath, ClientConfig.jobRepPath));
		prefs2.put("storagePath", StringUtils.defaultIfEmpty(storagePath, ClientConfig.storagePath));
		prefs2.put("jobIn", StringUtils.defaultIfEmpty(jobInPath, ClientConfig.jobInPath));
		prefs2.put("statRep", StringUtils.defaultIfEmpty(statRepPath, ClientConfig.statRepPath));
		prefs2.put("updateSite", StringUtils.defaultIfEmpty(updateSite, ClientConfig.updateSite));
		
		try {
			prefs1.sync();
			prefs1.flush();
			prefs2.sync();
			prefs2.flush();
		} catch (BackingStoreException e) {
			logger.error("Preference SET Error", e);
		}
		logger.debug("context addon");
	}
}
