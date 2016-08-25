package kr.co.d2net.netbro.transfer.handlers;

import kr.co.d2net.netbro.transfer.dialogs.ConnectionCheckDialog;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

public class ConnectionCheckHandler {
	@Execute
	public void execute(IEclipseContext context) {
		ConnectionCheckDialog dialog = ContextInjectionFactory.make(ConnectionCheckDialog.class, context);
		dialog.open();
	}
}
