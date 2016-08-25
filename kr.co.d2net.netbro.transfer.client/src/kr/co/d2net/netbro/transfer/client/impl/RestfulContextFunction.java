package kr.co.d2net.netbro.transfer.client.impl;

import kr.co.d2net.netbro.transfer.model.IRestfulService;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestfulContextFunction extends ContextFunction {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		IRestfulService restfulService = ContextInjectionFactory.make(RestfulServiceImpl.class, context);
		if(logger.isDebugEnabled()) {
			logger.debug("context: "+context);
		}
		MApplication app = context.get(MApplication.class);
		IEclipseContext appCtx = app.getContext();
		appCtx.set(IRestfulService.class, restfulService);
		
		return restfulService;
	}
}
