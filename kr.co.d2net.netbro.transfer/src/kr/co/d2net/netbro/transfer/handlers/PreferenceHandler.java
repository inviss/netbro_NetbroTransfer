package kr.co.d2net.netbro.transfer.handlers;

import kr.co.d2net.netbro.transfer.dialogs.PreferenceDialog;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferenceHandler {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Execute
	public void execute(IEclipseContext context) {
		PreferenceDialog dialog = ContextInjectionFactory.make(PreferenceDialog.class, context);
		dialog.open();
	}
}
