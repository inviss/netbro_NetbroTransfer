package kr.co.d2net.netbro.transfer.addons;

import javax.annotation.PostConstruct;

import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetPerspectiveAddon {

	final Logger logger = LoggerFactory.getLogger(getClass());

	private String[] args;

	@PostConstruct
	public void setPerspective(IApplicationContext context) {
		args = (String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		if(logger.isDebugEnabled()) {
			for (Object value : args) {
				if(logger.isDebugEnabled())
					logger.debug("args: "+value);
			}
		}
		String perspective = getArgValue("perspective", context, false);
		if(logger.isDebugEnabled()) {
			logger.debug("perspective: "+perspective);
		}
	}

	private String getArgValue(String argName, IApplicationContext appContext,
			boolean singledCmdArgValue) {
		// Is it in the arg list ?
		if (argName == null || argName.length() == 0)
			return null;

		if (singledCmdArgValue) {
			for (int i = 0; i < args.length; i++) {
				if (("-" + argName).equals(args[i]))
					return "true";
			}
		} else {
			for (int i = 0; i < args.length; i++) {
				if (("-" + argName).equals(args[i]) && i + 1 < args.length)
					return args[i + 1];
			}
		}

		final String brandingProperty = appContext.getBrandingProperty(argName);
		return brandingProperty == null ? System.getProperty(argName): brandingProperty;
	}
}
