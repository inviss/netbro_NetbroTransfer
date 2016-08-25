package kr.co.d2net.netbro.transfer.service.impl;

import kr.co.d2net.netbro.transfer.model.event.IEventHandler;
import kr.co.d2net.netbro.transfer.model.event.ProgressEventArgs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressEventListener implements IEventHandler<ProgressEventArgs> {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Override
	public void eventReceived(Object sender, ProgressEventArgs progressEventArgs) {
		
	}

}
