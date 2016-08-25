package kr.co.d2net.netbro.transfer.parts;

import javax.inject.Inject;
import javax.inject.Named;

import kr.co.d2net.netbro.transfer.model.Transfer;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualRegisterWizard1 extends WizardPage {
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private IEclipseContext context;
	@Inject @Named("transfer")
	private Transfer transfer;
	
	public ManualRegisterWizard1() {
		super("\uC804\uC1A1 \uBAA9\uC801\uC9C0 \uC124\uC815"); // 전송 목적지 설정
		setTitle("Validate");
		setDescription("Check to create the todo item");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		FtpConfigurePart part = ContextInjectionFactory.make(FtpConfigurePart.class, context);
		part.createControls(container);
		part.setBinding(transfer);
		
		setControl(container);
	}
	
}
