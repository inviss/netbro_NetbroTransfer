package kr.co.d2net.netbro.transfer.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import kr.co.d2net.netbro.transfer.model.Transfer;
import kr.co.d2net.netbro.transfer.model.event.TransferEventConstants;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class Transfer4Part {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Inject
	@Optional
	private IEventBroker eventBroker;
	
	@Inject
	@Preference(nodePath="kr.co.d2net.netbro.transfer", value = "usetf4")
	String useYn;
	
	private ProgressBar progressBar;
	private JobInfoPart jobInfoPart;
	
	private Map<String, Object> eventMap = new HashMap<String, Object>();

	@PostConstruct
	public void createControls(Composite parent) {
		jobInfoPart = new JobInfoPart(parent);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_parent = new GridLayout(2, false);
		gl_parent.marginRight = 0;
		gl_parent.marginLeft = 0;
		gl_parent.horizontalSpacing = 5;
		gl_parent.marginWidth = 5;
		composite.setLayout(gl_parent);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		if(useYn.equals("Y"))
			jobInfoPart.setInterfaceUsable(true);
		else
			jobInfoPart.setInterfaceUsable(false);
		
		progressBar = new ProgressBar(composite, SWT.SMOOTH);
		progressBar.setMaximum(100);
		
		GridData gd_progressBar = new GridData(GridData.FILL_HORIZONTAL);
		progressBar.setLayoutData(gd_progressBar);
	}
	
	@Inject @Optional
	public void  getUseEvent(@UIEventTopic(TransferEventConstants.TOPIC_USE_4) final String isUse) {
		if(logger.isDebugEnabled()) {
			logger.debug("1_part_isUse: "+isUse);
		}
		if(isUse.equals("Y"))
			jobInfoPart.setInterfaceUsable(true);
		else
			jobInfoPart.setInterfaceUsable(false);
	}
	
	@Inject @Optional
	public void  getEvent(@UIEventTopic(TransferEventConstants.TOPIC_TRANSFER_4) final Transfer transfer) {
		if(logger.isDebugEnabled()) {
			logger.debug("4_part_num: "+transfer.getDeviceNum()+", ctNm: "+transfer.getCtNm());
		}
		if(transfer.getProgress() != null && transfer.getProgress() == 100) {
			jobInfoPart.setTransfer(new Transfer());
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					progressBar.setSelection(0);
					if(useYn.equals("Y"))
						jobInfoPart.setInterfaceUsable(true);
					else
						jobInfoPart.setInterfaceUsable(false);
				}
			});
		} else {
			if(transfer.getWorkStatcd() != null && Integer.valueOf(transfer.getWorkStatcd()) > 3) {
				jobInfoPart.setTransfer(new Transfer());
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						progressBar.setSelection(0);
						if(useYn.equals("Y"))
							jobInfoPart.setInterfaceUsable(true);
						else
							jobInfoPart.setInterfaceUsable(false);
					}
				});
			} else {
				jobInfoPart.setTransfer(transfer);
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						progressBar.setSelection(transfer.getProgress());
					}
				});
			}
		}

		eventMap.put("TRANSFER_4", transfer);
		if(eventBroker != null)
			eventBroker.post("REPORT", eventMap);
	}
}
