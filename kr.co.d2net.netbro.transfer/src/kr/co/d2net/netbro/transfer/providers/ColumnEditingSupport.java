package kr.co.d2net.netbro.transfer.providers;

import kr.co.d2net.netbro.transfer.model.Content;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;

public class ColumnEditingSupport extends EditingSupport {

	private final TableViewer viewer;

	public ColumnEditingSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor(viewer.getTable(), SWT.NONE);
	}

	@Override
	protected boolean canEdit(Object element) {
		return ((Content) element).getStatus().equals("N");
	}

	@Override
	protected Object getValue(Object element) {
		return ((Content) element).getContentNm();
	}

	@Override
	protected void setValue(Object element, Object value) {
		((Content) element).setContentNm(String.valueOf(value));
		viewer.refresh();
	}

}
