package kr.co.d2net.netbro.transfer.handlers;

import kr.co.d2net.netbro.transfer.dialogs.ContentTransferCheckDialog;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;

public class ContentTransferHandler {
	@Execute
	public void execute(IEclipseContext context) {
		ContentTransferCheckDialog dialog = ContextInjectionFactory.make(ContentTransferCheckDialog.class, context);
		dialog.open();
	}
}
