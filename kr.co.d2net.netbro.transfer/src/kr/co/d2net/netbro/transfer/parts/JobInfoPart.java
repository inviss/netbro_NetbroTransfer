package kr.co.d2net.netbro.transfer.parts;

import kr.co.d2net.netbro.transfer.model.Transfer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

public class JobInfoPart {

	private DataBindingContext ctx = new DataBindingContext();

	private Text categoryNm;
	private Text episodeNm;
	private Text contentText;
	private Text brdDdText;
	private Text ctLengText;
	private Text profileText;
	private Text bitRateText;
	private Text targetNmText;
	private Text routePathText;
	private Text localFileText;
	private Text remoteFileText;

	public void setTransfer(Transfer transfer) {
		updateTransfer(transfer);
	}

	public void setInterfaceUsable(boolean enabled) {
		categoryNm.setEnabled(enabled);
		episodeNm.setEnabled(enabled);
		contentText.setEnabled(enabled);
		brdDdText.setEnabled(enabled);
		ctLengText.setEnabled(enabled);
		profileText.setEnabled(enabled);
		bitRateText.setEnabled(enabled);
		targetNmText.setEnabled(enabled);
		routePathText.setEnabled(enabled);
		localFileText.setEnabled(enabled);
		remoteFileText.setEnabled(enabled);
	}

	private void updateTransfer(Transfer transfer) {
		if(transfer != null) {
			// Remove bindings
			ctx.dispose();

			if(categoryNm != null && !categoryNm.isDisposed()) {
				IObservableValue target = WidgetProperties.text(SWT.Modify).observe(categoryNm);
				IObservableValue model = BeanProperties.value("categoryNm").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(episodeNm);
				model = BeanProperties.value("episodeNm").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(contentText);
				model = BeanProperties.value("ctNm").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(brdDdText);
				model = BeanProperties.value("brdDd").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(ctLengText);
				model = BeanProperties.value("ctLeng").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(profileText);
				model = BeanProperties.value("proFlnm").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(bitRateText);
				model = BeanProperties.value("vdoBitRate").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(targetNmText);
				model = BeanProperties.value("company").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(routePathText);
				model = BeanProperties.value("remoteDir").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(localFileText);
				model = BeanProperties.value("orgFileNm").observe(transfer);
				ctx.bindValue(target, model);

				target = WidgetProperties.text(SWT.Modify).observe(remoteFileText);
				model = BeanProperties.value("wrkFileNm").observe(transfer);
				ctx.bindValue(target, model);
			}
		}
	}

	public JobInfoPart(Composite container) {

		GridLayout gl_parent = new GridLayout(1, true);
		gl_parent.marginRight = 5;
		gl_parent.marginLeft = 5;
		gl_parent.horizontalSpacing = 5;
		gl_parent.marginWidth = 5;

		container.setLayout(gl_parent);


		final Group metaGroup = new Group(container, SWT.NONE);
		metaGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		metaGroup.setText("\uCF58\uD150\uCE20 \uC815\uBCF4"); // 콘텐츠 정보
		metaGroup.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				e.gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
				int h = metaGroup.getBounds().height;
				int w = metaGroup.getBounds().width;
				int txtW = e.gc.stringExtent(metaGroup.getText()).x; 
				int line[] = {
						5, 7, 2, 7,
						2, 7, 0, 9,
						0, 9, 0, h-3,
						0, h-3, 2, h-1,
						2, h-1, w-3, h-1,
						w-3,h-1, w-1, h-3,
						w-1,h-3, w-1, 9,
						w-1,9, w-3, 7,
						w-3,7, txtW+12 /*text length + spacing*/, 7
				};
				e.gc.drawPolyline(line);
			}
		});

		GridLayout gl_group = new GridLayout(4, false);
		gl_group.marginRight = 5;
		gl_group.marginLeft = 5;
		gl_group.horizontalSpacing = 10;
		gl_group.marginWidth = 5;

		metaGroup.setLayout(gl_group);

		Label categoryLabel = new Label(metaGroup, SWT.NONE);
		categoryLabel.setText("\uCE74\uD14C\uACE0\uB9AC\uBA85"); // 카테고리명

		categoryNm = new Text(metaGroup, SWT.BORDER);
		GridData gd_categoryNm = new GridData(GridData.FILL_HORIZONTAL);
		gd_categoryNm.horizontalSpan = 3;
		categoryNm.setLayoutData(gd_categoryNm);

		Label episodeLabel = new Label(metaGroup, SWT.NONE);
		episodeLabel.setText("\uC5D0\uD53C\uC18C\uB4DC\uBA85"); // 에피소드명

		episodeNm = new Text(metaGroup, SWT.BORDER);
		GridData gd_episodeNm = new GridData(GridData.FILL_HORIZONTAL);
		gd_episodeNm.horizontalSpan = 3;
		episodeNm.setLayoutData(gd_episodeNm);

		Label contentLabel = new Label(metaGroup, SWT.NONE);
		contentLabel.setText("\uCF58\uD150\uCE20\uBA85"); // 콘텐츠명

		contentText = new Text(metaGroup, SWT.BORDER);
		GridData gd_contentText = new GridData(GridData.FILL_HORIZONTAL);
		gd_contentText.horizontalSpan = 3;
		contentText.setLayoutData(gd_contentText);

		Label brdDdLabel = new Label(metaGroup, SWT.NONE);
		brdDdLabel.setText("\uBC29\uC1A1\uC77C"); // 방송일

		brdDdText = new Text(metaGroup, SWT.BORDER);
		brdDdText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label ctLengLabel = new Label(metaGroup, SWT.NONE);
		ctLengLabel.setText("\uC601\uC0C1\uAE38\uC774"); // 영상길이

		ctLengText = new Text(metaGroup, SWT.BORDER);
		ctLengText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label profileLabel = new Label(metaGroup, SWT.NONE);
		profileLabel.setText("\uD504\uB85C\uD30C\uC77C\uBA85"); // 프로파일명

		profileText = new Text(metaGroup, SWT.BORDER);
		profileText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label bitRateLabel = new Label(metaGroup, SWT.NONE);
		bitRateLabel.setText("\uBE44\uD2B8\uB808\uC774\uD2B8"); // 비트레이트

		bitRateText = new Text(metaGroup, SWT.BORDER);
		bitRateText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		//============================================================================================

		final Group targetGroup = new Group(container, SWT.NONE);
		targetGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		targetGroup.setText("\uBAA9\uC801\uC9C0 \uC815\uBCF4"); //목적지 정보
		targetGroup.addPaintListener(new PaintListener(){
			public void paintControl(PaintEvent e) {
				e.gc.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY));
				int h = targetGroup.getBounds().height;
				int w = targetGroup.getBounds().width;
				int txtW = e.gc.stringExtent(metaGroup.getText()).x; 
				int line[] = {
						5, 7, 2, 7,
						2, 7, 0, 9,
						0, 9, 0, h-3,
						0, h-3, 2, h-1,
						2, h-1, w-3, h-1,
						w-3,h-1, w-1, h-3,
						w-1,h-3, w-1, 9,
						w-1,9, w-3, 7,
						w-3,7, txtW+12 /*text length + spacing*/, 7
				};
				e.gc.drawPolyline(line);
			}
		});

		GridLayout gl_target = new GridLayout(4, false);
		gl_target.marginRight = 5;
		gl_target.marginLeft = 5;
		gl_target.horizontalSpacing = 10;
		gl_target.marginWidth = 5;

		targetGroup.setLayout(gl_target);

		Label targetNmLabel = new Label(targetGroup, SWT.NONE);
		targetNmLabel.setText("\uBAA9\uC801\uC9C0\uBA85"); // 목적지명

		targetNmText = new Text(targetGroup, SWT.BORDER);
		targetNmText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label routePathLabel = new Label(targetGroup, SWT.NONE);
		routePathLabel.setText("\uC804\uC1A1\uACBD\uB85C"); // 전송경로

		routePathText = new Text(targetGroup, SWT.BORDER);
		routePathText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label localFileLabel = new Label(targetGroup, SWT.NONE);
		localFileLabel.setText("\uB85C\uCEEC \uD30C\uC77C\uBA85"); // 로컬 파일명

		localFileText = new Text(targetGroup, SWT.BORDER);
		GridData gd_localFileText = new GridData(GridData.FILL_HORIZONTAL);
		gd_localFileText.horizontalSpan = 3;
		localFileText.setLayoutData(gd_localFileText);

		Label remoteFileLabel = new Label(targetGroup, SWT.NONE);
		remoteFileLabel.setText("\uB9AC\uBAA8\uD2B8 \uD30C\uC77C\uBA85"); // 리모트 파일명

		remoteFileText = new Text(targetGroup, SWT.BORDER);
		GridData gd_remoteFileText = new GridData(GridData.FILL_HORIZONTAL);
		gd_remoteFileText.horizontalSpan = 3;
		remoteFileText.setLayoutData(gd_remoteFileText);
	}
}
