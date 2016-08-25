package kr.co.d2net.netbro.transfer.parts;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import kr.co.d2net.netbro.common.utils.TMode;
import kr.co.d2net.netbro.transfer.dialogs.ManualRegisterDialog;
import kr.co.d2net.netbro.transfer.model.Content;
import kr.co.d2net.netbro.transfer.model.ITransferService;
import kr.co.d2net.netbro.transfer.model.Transfer;
import kr.co.d2net.netbro.transfer.model.providers.ContentProvider;
import kr.co.d2net.netbro.transfer.providers.ColumnEditingSupport;
import kr.co.d2net.netbro.transfer.providers.ContentDropProvider;
import kr.co.d2net.netbro.transfer.providers.TableContentProvider;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FtpTransferPart {

	final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	private ITransferService transferService;
	@Inject @Named("transfer")
	private Transfer transfer;
	private Content active = null;
	private TableViewer tableViewer = null;
	private ManualRegisterDialog dialog = null;

	public void updateProgress(Transfer transfer) {
		if(active != null) {
			active.setProgress(transfer.getProgress());
			if(Integer.valueOf(transfer.getWorkStatcd()) > 3) {
				active.setStatus("E");
				
				// 완료여부 체크
				completeCheck();
				
				active = null;
			} else {
				if(transfer.getWorkStatcd().equals("003") && active.getProgress() == 100) {
					active.setStatus("C");
					
					// 완료여부 체크
					completeCheck();
					
					active = null;
				} else {
					active.setStatus("I");
				}
			}

			if(logger.isDebugEnabled()) {
				logger.debug("manual progress value: "+transfer.getProgress());
			}
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					tableViewer.refresh();
				}
			});
		}
	}

	public void createControls(final IWizard wizard, final Composite container) {
		dialog = (ManualRegisterDialog) wizard;
		tableViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		tableViewer.getTable().setHeaderVisible(true);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableViewer.setContentProvider(new TableContentProvider());

		addResizeEvent(container, tableViewer);

		MenuManager manager = new MenuManager();
		manager.add(new Action("\uC601\uC0C1\uB4F1\uB85D", null) { //영상등록
			@Override
			public void run() {
				// 요청 가능여부 체크
				if(!canTransferCheck()) {
					MessageDialog.openWarning(container.getShell(), "\uC601\uC0C1\uB4F1\uB85D", "\uD604\uC7AC \uC804\uC1A1\uC774 \uC9C4\uD589\uC911\uC785\uB2C8\uB2E4. \uC644\uB8CC \uD6C4 \uC694\uCCAD\uD558\uC138\uC694.");
				} else {
					IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
					final Content content = (Content)selection.getFirstElement();
					Job job = new Job("Manual Register Job") {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							if(content != null) {
								active = content;

								transfer.setTransferType(TMode.MANUAL);
								/*
								 * 개별 파일에 대한 정보를 셋팅한다.
								 */
								transfer.setFlPath(content.getFilePath());
								transfer.setOrgFileNm(content.getFileName());
								transfer.setWrkFileNm(content.getContentNm()+"."+content.getFileType());
								transfer.setFlExt(content.getFileType());
								transfer.setWorkStatcd("001");
								transferService.manualRegister(transfer);
							}
							return null;
						}
					};
					job.schedule();
				}
			}
		});
		manager.add(new Action("\uC601\uC0C1\uC0AD\uC81C", null) { // 영상삭제
			@Override
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				final Content content = (Content)selection.getFirstElement();
				if(content != null) {
					Job job = new Job("Manual Register Job") {
						@Override
						protected IStatus run(IProgressMonitor monitor) {
							if(logger.isDebugEnabled()) {
								logger.debug("file name: "+content.getFileName());
							}

							List<Content> contents = ContentProvider.INSTANCE.getContents();
							contents.remove(content);
							Display.getDefault().asyncExec(new Runnable() {
								@Override
								public void run() {
									tableViewer.refresh();
								}
							});
							
							// 완료여부 체크
							completeCheck();
							
							return Status.OK_STATUS;
						}
					};
					job.schedule();
				}
			}
		});
		tableViewer.getControl().setMenu(manager.createContextMenu(tableViewer.getControl()));

		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK | DND.DROP_DEFAULT;
		org.eclipse.swt.dnd.Transfer[] transferTypes = new org.eclipse.swt.dnd.Transfer[]{FileTransfer.getInstance()};
		tableViewer.addDropSupport(operations, transferTypes , new ContentDropProvider(tableViewer));

		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.LEFT);
		column.getColumn().setText("\uCEE8\uD150\uCE20\uBA85"); //컨텐츠명
		column.getColumn().setWidth(220);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Content content = (Content)element;
				return content.getContentNm();
			}
		});
		column.setEditingSupport(new ColumnEditingSupport(tableViewer));

		column = new TableViewerColumn(tableViewer, SWT.LEFT);
		column.getColumn().setText("\uD30C\uC77C\uBA85"); //파일명
		column.getColumn().setWidth(230);
		column.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Content content = (Content)element;
				return content.getFileName();
			}
		});

		column = new TableViewerColumn(tableViewer, SWT.CENTER);
		column.getColumn().setText("\uC9C4\uD589\uB960"); //진행률
		column.getColumn().setWidth(100);
		column.setLabelProvider(new OwnerDrawLabelProvider() {
			@Override
			protected void paint(Event event, Object element) {
				Content content = (Content)element;

				int percentage = content.getProgress();

				Color foreground = event.gc.getForeground();
				Color background = event.gc.getBackground();
				event.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
				event.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));

				Rectangle bounds = ((TableItem) event.item).getBounds(event.index);

				int width = (bounds.width - 1) * percentage / 100;
				event.gc.fillGradientRectangle(event.x, event.y, width, event.height, true);
				Rectangle rect2 = new Rectangle(event.x, event.y, width - 1, event.height - 1);
				event.gc.drawRectangle(rect2);
				event.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
				String text = percentage + "%";
				Point size = event.gc.textExtent(text);
				int offset = Math.max(0, (event.height - size.y) / 2);
				event.gc.drawText(text, event.x + 2, event.y + offset, true);
				event.gc.setForeground(background);
				event.gc.setBackground(foreground);
			}

			@Override
			protected void measure(Event event, Object element) {
			}
		});

		Button saveAllBtn = new Button(container, SWT.NONE);
		saveAllBtn.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		saveAllBtn.setText("Save All");
		saveAllBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// 요청 가능여부 체크
				if(!canTransferCheck()) {
					MessageDialog.openWarning(container.getShell(), "\uC601\uC0C1\uB4F1\uB85D", "\uD604\uC7AC \uC804\uC1A1\uC774 \uC9C4\uD589\uC911\uC785\uB2C8\uB2E4. \uC644\uB8CC \uD6C4 \uC694\uCCAD\uD558\uC138\uC694.");
				} else {
					final List<Content> contents = ContentProvider.INSTANCE.getContents();
					if(contents != null && !contents.isEmpty()) {
						Job job = new Job("Manual Register Job") {
							@Override
							protected IStatus run(IProgressMonitor monitor) {
								for(Content content : contents) {
									if(!content.getStatus().equals("N")) continue;
									
									active = content;

									transfer.setTransferType(TMode.MANUAL);
									/*
									 * 개별 파일에 대한 정보를 셋팅한다.
									 */
									transfer.setOrgFileNm(content.getFileName());
									transfer.setWrkFileNm(content.getContentNm()+"."+content.getFileType());
									transfer.setFlExt(content.getFileType());
									transfer.setWorkStatcd("001");
									transferService.manualRegister(transfer);
								}
								return Status.OK_STATUS;
							}
						};
						job.schedule();
					}
				}
			}
		});
	}

	private TableViewer addResizeEvent(Composite composite, TableViewer tableViewer) {
		tableViewer.getTable().addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Table table = (Table)event.widget;
				int columnCount = table.getColumnCount();
				if(columnCount == 0)
					return;

				Rectangle area = table.getClientArea();

				int totalAreaWdith = area.width;
				int lineWidth = table.getGridLineWidth();
				int totalGridLineWidth = (columnCount-1)*lineWidth; 
				int totalColumnWidth = 0;

				for(TableColumn column: table.getColumns()) {
					totalColumnWidth = totalColumnWidth+column.getWidth();
				}

				int diff = totalAreaWdith-(totalColumnWidth+totalGridLineWidth);

				TableColumn lastCol = table.getColumns()[columnCount-1];

				//check diff is valid or not. setting negetive width doesnt make sense.
				lastCol.setWidth(diff+lastCol.getWidth());
			}
		});
		return tableViewer;
	}
	
	private boolean canTransferCheck() {
		if(active != null && active.getStatus().equals("I")) return false;
		else return true;
	}
	
	private void completeCheck() {
		boolean completed = true;
		List<Content> contents = ContentProvider.INSTANCE.getContents();
		for(Content content : contents) {
			if(content.getStatus().equals("N")) {
				completed = false;
				break;
			}
		}

		if(completed) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					dialog.canFinish(true);
					dialog.getContainer().updateButtons();
				}
			});
		}
	}
}
