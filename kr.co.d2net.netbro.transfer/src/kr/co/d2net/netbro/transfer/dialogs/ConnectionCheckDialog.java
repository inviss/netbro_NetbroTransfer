package kr.co.d2net.netbro.transfer.dialogs;

import javax.inject.Inject;
import javax.inject.Named;

import kr.co.d2net.netbro.transfer.model.ITransferService;
import kr.co.d2net.netbro.transfer.model.Transfer;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class ConnectionCheckDialog extends Dialog {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	public ConnectionCheckDialog(@Optional @Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		super(parentShell);
	}

	private DataBindingContext ctx = new DataBindingContext();

	@Inject @Preference(nodePath="ftp.test", value = "host") 
	private String host;
	@Inject @Preference(nodePath="ftp.test", value = "port") 
	private String port;
	@Inject @Preference(nodePath="ftp.test", value = "userId") 
	private String userId;
	@Inject @Preference(nodePath="ftp.test", value = "userPass") 
	private String userPass;
	@Inject @Preference(nodePath="ftp.test", value = "connType") 
	private String connType;
	@Inject @Preference(nodePath="ftp.test", value = "trsfType") 
	private String trsfType;

	
	
	private @Inject @Preference(nodePath = "kr.co.d2net.netbro.preference") IEclipsePreferences prefs;
	
	@Inject
	private ITransferService transferService;

	private Text ftpIpText;
	private Text ftpPortText;
	private Text userIdText;
	private Text userPassText;

	private Button activeBtn;
	private Button passiveBtn;
	private Button binaryBtn;
	private Button asciiBtn;
	private Label resultLabel;

	private Transfer transfer = new Transfer();
	
	
	@Override
	public void create() {
		super.create();
		if(ftpIpText != null && !ftpIpText.isDisposed()) {
			// Remove bindings
			ctx.dispose();

			IObservableValue target = WidgetProperties.text(SWT.Modify).observe(ftpIpText);
			IObservableValue model = BeanProperties.value("ftpIp").observe(transfer);
			ctx.bindValue(target, model);

			target = WidgetProperties.text(SWT.Modify).observe(ftpPortText);
			model = BeanProperties.value("ftpPort").observe(transfer);
			ctx.bindValue(target, model);

			target = WidgetProperties.text(SWT.Modify).observe(userIdText);
			model = BeanProperties.value("userId").observe(transfer);
			ctx.bindValue(target, model);

			target = WidgetProperties.text(SWT.Modify).observe(userPassText);
			model = BeanProperties.value("userPass").observe(transfer);
			ctx.bindValue(target, model);
			
			transfer.setConnectMode(StringUtils.defaultIfEmpty(transfer.getConnectMode(), "P"));
			transfer.setTransferMode(StringUtils.defaultIfEmpty(transfer.getTransferMode(), "B"));
		}
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		getShell().setText("\uC5F0\uACB0\uD14C\uC2A4\uD2B8"); // 연결테스트

		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout gl_parent = new GridLayout(4, false);
		gl_parent.marginRight = 5;
		gl_parent.marginLeft = 5;
		gl_parent.horizontalSpacing = 5;
		gl_parent.marginWidth = 5;
		container.setLayout(gl_parent);
		//--------------------------------------------------------------------

		Label ftpIpLabel = new Label(container, SWT.NONE);
		ftpIpLabel.setText("\uBAA9\uC801\uC9C0 \uD638\uC2A4\uD2B8"); // 목적지 호스트

		ftpIpText = new Text(container, SWT.BORDER);
		ftpIpText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label ftpPortLabel = new Label(container, SWT.NONE);
		ftpPortLabel.setText("\uBAA9\uC801\uC9C0 \uD3EC\uD2B8"); // 목적지 포트

		ftpPortText = new Text(container, SWT.BORDER);
		ftpPortText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label userIdLabel = new Label(container, SWT.NONE);
		userIdLabel.setText("\uC544\uC774\uB514"); // 아이디

		userIdText = new Text(container, SWT.BORDER);
		userIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label userPassLabel = new Label(container, SWT.NONE);
		userPassLabel.setText("\uBE44\uBC00\uBC88\uD638"); // 비밀번호

		userPassText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		GridData gd_userPassText = new GridData(GridData.FILL_HORIZONTAL);
		gd_userPassText.grabExcessHorizontalSpace = false;
		userPassText.setLayoutData(gd_userPassText);

		final Group group = new Group(container, SWT.NONE);
		GridData gd_group = new GridData(GridData.FILL_HORIZONTAL);
		gd_group.horizontalSpan = 2;
		group.setLayoutData(gd_group);
		group.setText("\uC5F0\uACB0\uBC29\uC2DD"); // 연결방식

		GridLayout gl_group = new GridLayout(1, false);
		gl_group.marginRight = 5;
		gl_group.marginLeft = 5;
		gl_group.horizontalSpacing = 10;
		gl_group.marginWidth = 5;
		group.setLayout(gl_group);

		activeBtn = new Button(group, SWT.RADIO);
		activeBtn.setText("Active");
		activeBtn.setSelection(true);
		activeBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				transfer.setConnectMode("A");
			}
		});

		passiveBtn = new Button(group, SWT.RADIO);
		passiveBtn.setText("Passive");
		passiveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				transfer.setConnectMode("P");
			}
		});

		if(connType != null && connType.equals("P")) {
			if(activeBtn.getSelection()) {
				activeBtn.setSelection(false);
				activeBtn.redraw();
				passiveBtn.setSelection(true);
				passiveBtn.redraw();
			}
		}

		final Group group2 = new Group(container, SWT.NONE);
		GridData gd_group2 = new GridData(GridData.FILL_HORIZONTAL);
		gd_group2.horizontalSpan = 2;
		group2.setLayoutData(gd_group2);
		group2.setText("\uC804\uC1A1\uBC29\uC2DD"); // 전송방식

		GridLayout gl_group2 = new GridLayout(1, false);
		gl_group2.marginRight = 5;
		gl_group2.marginLeft = 5;
		gl_group2.horizontalSpacing = 10;
		gl_group2.marginWidth = 5;
		group2.setLayout(gl_group2);

		binaryBtn = new Button(group2, SWT.RADIO);
		binaryBtn.setData("B");
		binaryBtn.setText("Binary");
		binaryBtn.setSelection(true);
		binaryBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				transfer.setTransferMode("B");
			}
		});

		asciiBtn = new Button(group2, SWT.RADIO);
		asciiBtn.setData("A");
		asciiBtn.setText("Ascii");
		asciiBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				transfer.setTransferMode("A");
			}
		});

		if(trsfType != null && trsfType.equals("A")) {
			if(binaryBtn.getSelection()) {
				binaryBtn.setSelection(false);
				binaryBtn.redraw();
				asciiBtn.setSelection(true);
				asciiBtn.redraw();
			}
		}

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		resultLabel = new Label(container, SWT.NONE);
		resultLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 4, 1));
		resultLabel.setText("             ");
		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Connect", true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 260);
	}



	@Override
	protected void okPressed() {
		if(transferService != null) {
			if(transferService.connectionCheck(transfer)) {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						resultLabel.setText("Succeed");
						resultLabel.redraw();
					}
				});
				
				try {
					prefs.put("host", transfer.getFtpIp());
					prefs.put("port", String.valueOf(transfer.getFtpPort()));
					prefs.put("userId", transfer.getUserId());
					prefs.put("userPass", transfer.getUserPass());
					prefs.put("connType", transfer.getConnectMode());
					prefs.put("trsfType", transfer.getTransferMode());
					prefs.flush();
					prefs.sync();
				} catch (BackingStoreException e) {
					logger.error("ConnectionCheckDialog SET Error", e);
				}
			} else {
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						resultLabel.setText("Failed");
						resultLabel.redraw();
					}
				});
			}
		}
		//super.okPressed();
	}
}
