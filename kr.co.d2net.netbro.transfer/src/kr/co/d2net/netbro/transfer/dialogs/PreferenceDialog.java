package kr.co.d2net.netbro.transfer.dialogs;

import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import kr.co.d2net.netbro.transfer.model.EqPreference;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class PreferenceDialog extends TitleAreaDialog {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	public PreferenceDialog(@Optional @Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell) {
		super(parentShell);
	}

	@Inject @Preference(nodePath="kr.co.d2net.netbro.preference", value = "eqId") 
	private String eqId;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.preference", value = "serverType") 
	private String serverType;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.preference", value = "serverIp") 
	private String serverIp;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.preference", value = "serverPort")
	private String serverPort;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "serverCtx")
	private String serverCtx;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.preference", value = "jobIn")
	private String jobInPath;
	@Inject @Preference(nodePath="kr.co.d2net.netbro.preference", value = "statRep")
	private String statRepPath;
	@Inject @Preference(nodePath = "kr.co.d2net.netbro.preference", value = "storagePath") 
	private String storagePath;

	private @Inject @Preference(nodePath = "kr.co.d2net.netbro.preference") IEclipsePreferences prefs;

	private Text eqIdText;
	private Text serverIpText;
	private Text serverPortText;
	private Text serverCtxText;
	private Text storagePathText;
	private Text jobInPathText;
	private Text statRepPathText;
	private Button storagebtn;
	private Button jobInbtn;
	private Button statRepbtn;
	private Button restfulBtn;
	private Button watcherBtn;

	private EqPreference preference = new EqPreference();

	@Inject
	public void setup() {
		if(logger.isDebugEnabled()) {
			logger.debug("preference serverType: "+serverType);
			logger.debug("preference serverIp: "+serverIp);
			logger.debug("preference serverPort: "+serverPort);
			logger.debug("preference serverCtx: "+serverCtx);
			logger.debug("preference jobInPath: "+jobInPath);
			logger.debug("preference statRepPath: "+statRepPath);
			logger.debug("preference storagePath: "+storagePath);
		}
		preference.setEqId(StringUtils.defaultIfEmpty(eqId, "TF01"));
		preference.setServerIp(StringUtils.defaultIfEmpty(serverIp, ""));
		preference.setServerPort(StringUtils.defaultIfEmpty(serverPort, ""));
		preference.setServerCtx(StringUtils.defaultIfEmpty(serverCtx, ""));
		preference.setJobIn(StringUtils.defaultIfEmpty(jobInPath, ""));
		preference.setStatRep(StringUtils.defaultIfEmpty(statRepPath, ""));
		preference.setServerType(StringUtils.defaultIfEmpty(serverType, "R"));
		preference.setStoragePath(StringUtils.defaultIfEmpty(storagePath, ""));
	}

	@Override
	protected void okPressed() {
		if(logger.isDebugEnabled()) {
			logger.debug("okPressed serverType: "+preference.getServerType());
		}
		prefs.put("eqId", eqIdText.getText());
		if(preference.getServerType().equals("R")) {
			prefs.put("serverIp", serverIpText.getText());
			prefs.put("serverPort", serverPortText.getText());
			prefs.put("serverCtx", serverCtxText.getText());
			prefs.put("serverType", "R");
		} else {
			prefs.put("storagePath", storagePathText.getText());
			prefs.put("jobIn", jobInPathText.getText());
			prefs.put("statRep", statRepPathText.getText());
			prefs.put("serverType", "W");
		}

		try {
			File f = new File(jobInPathText.getText());
			if(!f.exists()) f.mkdirs();
			
			f = new File(statRepPathText.getText());
			if(!f.exists()) f.mkdirs();
			
			prefs.flush();
			prefs.sync();
			super.okPressed();
		} catch (BackingStoreException e) {
			logger.error("PreferenceHandler SET Error", e);
			ErrorDialog.openError(
					getShell(),
					"Error",
					"Error while storing preferences",
					new Status(IStatus.ERROR, "kr.co.d2net.netbro.transfer", e
							.getMessage(), e));
		}
	}

	private void restUpdate(boolean used) {
		if(serverIpText != null && !serverIpText.isDisposed()) {
			serverIpText.setEnabled(used);
			serverIpText.redraw();
			serverPortText.setEnabled(used);
			serverPortText.redraw();
			serverCtxText.setEnabled(used);
			serverPortText.redraw();
		}
	}

	private void watcherUpdate(boolean used) {
		if(jobInPathText != null && !jobInPathText.isDisposed()) {
			storagePathText.setEnabled(used);
			storagePathText.redraw();
			jobInPathText.setEnabled(used);
			jobInPathText.redraw();
			statRepPathText.setEnabled(used);
			statRepPathText.redraw();
			storagebtn.setEnabled(used);
			storagebtn.redraw();
			jobInbtn.setEnabled(used);
			jobInbtn.redraw();
			statRepbtn.setEnabled(used);
			statRepbtn.redraw();
		}
	}

	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		getShell().setText("\uD504\uB85C\uADF8\uB7A8 \uAD00\uB9AC"); // 프로그램 관리
		setTitle("\uC804\uC1A1\uC11C\uBC84 \uC124\uC815\uC815\uBCF4 \uAD00\uB9AC"); //전송서버 설정정보 관리
		// 작업요청 및 상태보고를 위한 연결정보를 설정합니다.
		setMessage("\uC791\uC5C5\uC694\uCCAD \uBC0F \uC0C1\uD0DC\uBCF4\uACE0\uB97C \uC704\uD55C \uC5F0\uACB0\uC815\uBCF4\uB97C \uC124\uC815\uD569\uB2C8\uB2E4.");

		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout gl_parent = new GridLayout(3, false);
		gl_parent.marginRight = 5;
		gl_parent.marginLeft = 5;
		gl_parent.horizontalSpacing = 5;
		gl_parent.marginWidth = 5;
		container.setLayout(gl_parent);

		//======================================================================================

		Group serverTypeGroup = new Group(container, SWT.NONE);
		serverTypeGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serverTypeGroup.setText("\uC5F0\uACB0\uBC29\uC2DD");

		GridLayout gl_serverType = new GridLayout(2, true);
		gl_serverType.marginRight = 5;
		gl_serverType.marginLeft = 5;
		gl_serverType.horizontalSpacing = 10;
		gl_serverType.marginWidth = 5;
		serverTypeGroup.setLayout(gl_serverType);

		restfulBtn = new Button(serverTypeGroup, SWT.RADIO);
		restfulBtn.setText("Restful");
		restfulBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(logger.isDebugEnabled()) {
					logger.debug("restfulBtn event: "+serverType);
				}
				preference.setServerType("R");
				restUpdate(true);
				watcherUpdate(false);
			}
		});

		watcherBtn = new Button(serverTypeGroup, SWT.RADIO);
		watcherBtn.setText("Watcher Folder");
		watcherBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if(logger.isDebugEnabled()) {
					logger.debug("watcherBtn event: "+serverType);
				}
				preference.setServerType("W");
				restUpdate(false);
				watcherUpdate(true);
			}
		});

		if(logger.isDebugEnabled()) {
			logger.debug("init ui serverType: "+preference.getServerType());
		}
		if(preference.getServerType().equals("R")) {
			if(watcherBtn.getSelection()) {
				watcherBtn.setSelection(false);
				watcherBtn.redraw();
			}
			restfulBtn.setSelection(true);
			restfulBtn.redraw();
		} else {
			if(restfulBtn.getSelection()) {
				restfulBtn.setSelection(false);
				restfulBtn.redraw();
			}
			watcherBtn.setSelection(true);
			watcherBtn.redraw();
		}

		Label eqIdLabel = new Label(container, SWT.NONE);
		eqIdLabel.setText("\uC7A5\uBE44ID"); // 장비ID

		eqIdText = new Text(container, SWT.BORDER);
		eqIdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		eqIdText.setText(preference.getEqId());

		//==============================================================================================

		Group restfulGroup = new Group(container, SWT.NONE);
		GridData gd_restfulGroup = new GridData(GridData.FILL_HORIZONTAL);
		gd_restfulGroup.horizontalSpan = 3;
		restfulGroup.setLayoutData(gd_restfulGroup);
		restfulGroup.setText("Restful Interface");

		GridLayout gl_restful = new GridLayout(6, false);
		gl_restful.marginRight = 5;
		gl_restful.marginLeft = 5;
		gl_restful.horizontalSpacing = 10;
		gl_restful.marginWidth = 5;
		restfulGroup.setLayout(gl_restful);

		Label serverIPLabel = new Label(restfulGroup, SWT.NONE);
		serverIPLabel.setText("Server IP");

		serverIpText = new Text(restfulGroup, SWT.BORDER);
		serverIpText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serverIpText.setText(preference.getServerIp());

		Label serverPortLabel = new Label(restfulGroup, SWT.NONE);
		serverPortLabel.setText("Server Port");

		serverPortText = new Text(restfulGroup, SWT.BORDER);
		serverPortText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serverPortText.setText(preference.getServerPort());

		Label serverCtxLabel = new Label(restfulGroup, SWT.NONE);
		serverCtxLabel.setText("Context");

		serverCtxText = new Text(restfulGroup, SWT.BORDER);
		serverCtxText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		serverCtxText.setText(preference.getServerCtx());

		//==============================================================================================

		final Group watcherGroup = new Group(container, SWT.NONE);
		GridData gd_watcherGroup = new GridData(GridData.FILL_HORIZONTAL);
		gd_watcherGroup.horizontalSpan = 3;
		watcherGroup.setLayoutData(gd_watcherGroup);
		watcherGroup.setText("Watcher Interface");

		GridLayout gl_watcher = new GridLayout(3, false);
		gl_watcher.marginRight = 5;
		gl_watcher.marginLeft = 5;
		gl_watcher.horizontalSpacing = 10;
		gl_watcher.marginWidth = 5;
		watcherGroup.setLayout(gl_watcher);

		Label storageLabel = new Label(watcherGroup, SWT.NONE);
		storageLabel.setText("\uC791\uC5C5\uC694\uCCAD \uD3F4\uB354"); // 작업요청 폴더

		storagePathText = new Text(watcherGroup, SWT.BORDER);
		storagePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		storagePathText.setText(preference.getStoragePath());

		storagebtn = new Button(watcherGroup, SWT.PUSH);
		storagebtn.setText("Browse...");
		storagebtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(watcherGroup.getShell());

				// 입력된 경로가 있다면 해당 경로를 표시한다.
				dlg.setFilterPath(storagePathText.getText());
				// 팝업창 타이틀명
				dlg.setText("\uC791\uC5C5 \uB4DC\uB77C\uC774\uBE0C \uC120\uD0DD"); // 작업 드라이브 선택
				dlg.setMessage("\uB4DC\uB77C\uC774\uBE0C\uB97C \uC120\uD0DD\uD558\uC138\uC694"); // 드라이브를 선택하세요
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					storagePathText.setText(dir);
				}
			}
		});
		
		Label jobInLabel = new Label(watcherGroup, SWT.NONE);
		jobInLabel.setText("\uC791\uC5C5\uC694\uCCAD \uD3F4\uB354"); // 작업요청 폴더

		jobInPathText = new Text(watcherGroup, SWT.BORDER);
		jobInPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jobInPathText.setText(preference.getJobIn());

		jobInbtn = new Button(watcherGroup, SWT.PUSH);
		jobInbtn.setText("Browse...");
		jobInbtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(watcherGroup.getShell());

				// 입력된 경로가 있다면 해당 경로를 표시한다.
				if(StringUtils.isBlank(preference.getJobIn())) {
					dlg.setFilterPath(storagePathText.getText()+"/");
				} else {
					dlg.setFilterPath(preference.getJobIn());
				}
				
				// 팝업창 타이틀명
				dlg.setText("\uC791\uC5C5\uD3F4\uB354 \uC120\uD0DD"); // 작업폴더 선택
				dlg.setMessage("\uD3F4\uB354\uB97C \uC120\uD0DD\uD558\uC138\uC694"); // 폴더를 선택하세요
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					jobInPathText.setText(dir.replaceAll("\\\\", "/"));
					preference.setJobIn(dir.replaceAll("\\\\", "/"));
				}
			}
		});

		Label statusOutLabel = new Label(watcherGroup, SWT.NONE);
		statusOutLabel.setText("\uC0C1\uD0DC\uBCF4\uACE0 \uD3F4\uB354"); // 상태보고 폴더

		statRepPathText = new Text(watcherGroup, SWT.BORDER);
		statRepPathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		statRepPathText.setText(preference.getStatRep());

		statRepbtn = new Button(watcherGroup, SWT.PUSH);
		statRepbtn.setText("Browse...");
		statRepbtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(watcherGroup.getShell());

				// 입력된 경로가 있다면 해당 경로를 표시한다.
				if(StringUtils.isBlank(preference.getStatRep())) {
					dlg.setFilterPath(storagePathText.getText()+"/");
				} else {
					dlg.setFilterPath(preference.getStatRep());
				}
				
				// 팝업창 타이틀명
				dlg.setText("\uC791\uC5C5\uD3F4\uB354 \uC120\uD0DD"); // 작업폴더 선택
				dlg.setMessage("\uD3F4\uB354\uB97C \uC120\uD0DD\uD558\uC138\uC694"); // 폴더를 선택하세요
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					statRepPathText.setText(dir.replaceAll("\\\\", "/"));
					preference.setStatRep(dir.replaceAll("\\\\", "/"));
				}
			}
		});

		if(preference.getServerType().equals("R")) {
			restUpdate(true);
			watcherUpdate(false);
		} else {
			restUpdate(false);
			watcherUpdate(true);
		}

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 370);
	}
}
