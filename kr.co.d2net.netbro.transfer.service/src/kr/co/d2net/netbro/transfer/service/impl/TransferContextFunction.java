package kr.co.d2net.netbro.transfer.service.impl;

import kr.co.d2net.netbro.transfer.model.ITransferService;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferContextFunction extends ContextFunction {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		ITransferService transferService = ContextInjectionFactory.make(TransferServiceImpl.class, context);
		if(logger.isDebugEnabled()) {
			logger.debug("context: "+context);
		}
		MApplication app = context.get(MApplication.class);
		IEclipseContext appCtx = app.getContext();
		appCtx.set(ITransferService.class, transferService);
		
		return transferService;
	}
}
