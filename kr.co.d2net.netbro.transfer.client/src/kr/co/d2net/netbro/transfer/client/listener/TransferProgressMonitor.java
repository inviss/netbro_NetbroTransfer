package kr.co.d2net.netbro.transfer.client.listener;

import kr.co.d2net.netbro.common.utils.TMode;
import kr.co.d2net.netbro.transfer.model.Transfer;
import kr.co.d2net.netbro.transfer.model.event.TransferEventConstants;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enterprisedt.net.ftp.FTPProgressMonitor;

public class TransferProgressMonitor implements FTPProgressMonitor {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private int percent;
	
	private Transfer transfer;
	private IEventBroker broker;
	public TransferProgressMonitor(Transfer transfer, IEventBroker broker) {
		this.transfer = transfer;
		this.broker = broker;
	}
	
	@Override
	public void bytesTransferred(long bytes) {
		int i = (int)(((double)bytes / (double)transfer.getFileSize())*100);
		if(i != percent) { // && (i%2==0)
			percent = i;
			transfer.setProgress(percent);
			transfer.setWorkStatcd("003");
			if(logger.isDebugEnabled()) {
				logger.debug("eqId:"+transfer.getDeviceId()+"_"+transfer.getDeviceNum()+", seq: "+transfer.getSeq()+", progress: "+percent);
			}
			TMode tMode = transfer.getTransferType() == null ? TMode.DEFAULT : transfer.getTransferType();
			switch(tMode) {
			case DEFAULT:
				broker.post("TOPIC_TF"+transfer.getDeviceNum()+"/STATUS", transfer);
				break;
			case SAMPLE:
				broker.post(TransferEventConstants.TOPIC_SAMPLE, transfer);
				break;
			case MANUAL:
				broker.post(TransferEventConstants.TOPIC_MANUAL, transfer);
				break;
			}
		}
	}

}
