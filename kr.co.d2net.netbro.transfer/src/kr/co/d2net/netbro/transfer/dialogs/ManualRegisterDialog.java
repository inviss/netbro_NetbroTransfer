package kr.co.d2net.netbro.transfer.dialogs;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import kr.co.d2net.netbro.transfer.model.Transfer;
import kr.co.d2net.netbro.transfer.parts.ManualRegisterWizard1;
import kr.co.d2net.netbro.transfer.parts.ManualRegisterWizard2;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualRegisterDialog extends Wizard {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	boolean finish = false;
	
	@Inject
	private IEclipseContext context;
	
	@PostConstruct
	public void init() {
		context.set("transfer", new Transfer());
	}
	
	public ManualRegisterDialog() {
		setWindowTitle("\uC218\uB3D9\uB4F1\uB85D"); // 수동등록
	}
	
	@Override
	public void addPages() {
		ManualRegisterWizard1 wizard1 = ContextInjectionFactory.make(ManualRegisterWizard1.class, context);
		ManualRegisterWizard2 wizard2 = ContextInjectionFactory.make(ManualRegisterWizard2.class, context);
		addPage(wizard1);
		addPage(wizard2);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
	
	@Override
	public boolean canFinish() {
		return finish;
	}
	
	public void canFinish(boolean finish) {
		this.finish = finish;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return super.getNextPage(page);
	}
}

