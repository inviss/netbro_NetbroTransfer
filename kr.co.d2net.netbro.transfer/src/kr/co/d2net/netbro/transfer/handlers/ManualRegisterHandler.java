package kr.co.d2net.netbro.transfer.handlers;

import java.util.List;

import kr.co.d2net.netbro.transfer.dialogs.ManualRegisterDialog;
import kr.co.d2net.netbro.transfer.model.Content;
import kr.co.d2net.netbro.transfer.model.providers.ContentProvider;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualRegisterHandler {
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Execute
	public void execute(Shell shell, IEclipseContext context) {
		ManualRegisterDialog dialog = ContextInjectionFactory.make(ManualRegisterDialog.class, context);
		WizardDialog wizard = new WizardDialog(shell, dialog);
		if (wizard.open()== WizardDialog.OK) {
			List<Content> contents = ContentProvider.INSTANCE.getContents();
			for(Content content : contents) {
				/*
				 * 성공한 영상이 있다면 CMS에 메타정보를 등록?
				 */
				if(content.getStatus().equals("C")) {
					
				}
			}
			// 임시기록을 모두 제거한다.
			ContentProvider.INSTANCE.removeAll();
		}
	}
}
