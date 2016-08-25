package kr.co.d2net.netbro.transfer.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class CanUseHandler {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf1") String part1;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf2") String part2;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf3") String part3;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf4") String part4;
	
	@Execute
	public void execute(IEventBroker broker, @Preference(nodePath = "kr.co.d2net.netbro.transfer") IEclipsePreferences prefs,
			@Named("kr.co.d2net.netbro.transfer.commandparameter.partno") String param) {
		String prefix = "TOPIC_PART";
		String value = "";
		switch(param) {
		case "1":
			value = part1.equals("Y") ? "N" : "Y";
			break;
		case "2":
			value = part2.equals("Y") ? "N" : "Y";
			break;
		case "3":
			value = part3.equals("Y") ? "N" : "Y";
			break;
		case "4":
			value = part4.equals("Y") ? "N" : "Y";
			break;
		}
		prefs.put("usetf"+param, value);
		try {
			prefs.flush();
			prefs.sync();
		} catch (BackingStoreException e) {
			logger.error("CanUseHandler Preferences Error", e);
		}
		
		broker.post(prefix+param+"/USE", value);
	}
}
