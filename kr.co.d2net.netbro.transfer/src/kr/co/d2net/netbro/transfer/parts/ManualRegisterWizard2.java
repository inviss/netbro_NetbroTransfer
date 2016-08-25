package kr.co.d2net.netbro.transfer.parts;

import javax.inject.Inject;
import javax.inject.Named;

import kr.co.d2net.netbro.transfer.model.Transfer;
import kr.co.d2net.netbro.transfer.model.event.TransferEventConstants;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualRegisterWizard2 extends WizardPage {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private IEclipseContext context;
	@Inject @Named("transfer")
	private Transfer transfer;
	
	private FtpTransferPart part;
	
	public ManualRegisterWizard2() {
		super("\uC804\uC1A1 \uCF58\uD150\uCE20 \uBAA9\uB85D"); // 전송 콘텐츠 목록
		setTitle("Validate");
		setDescription("Check to create the todo item");
	}

	@Override
	public void createControl(Composite parent) {
		
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		
		part = ContextInjectionFactory.make(FtpTransferPart.class, context);
		part.createControls(getWizard(), container);
		
		setControl(container);
	}
	
	@Inject @Optional
	public void  getEvent(@UIEventTopic(TransferEventConstants.TOPIC_MANUAL) final Transfer transfer) {
		part.updateProgress(transfer);
	}
	
	@Override
	public Control getControl() {
		return super.getControl();
	}
	@Override
	public IWizardPage getPreviousPage() {
		return super.getPreviousPage();
	}
}
