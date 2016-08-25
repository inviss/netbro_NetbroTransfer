package kr.co.d2net.netbro.transfer.providers;

import java.util.List;

import kr.co.d2net.netbro.transfer.model.Transfer;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

@SuppressWarnings("unchecked")
public class TableContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<Transfer> list = (List<Transfer>) inputElement;
		return list.toArray();
	}

}
