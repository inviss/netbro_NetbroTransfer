package kr.co.d2net.netbro.transfer.providers;

import java.io.File;

import kr.co.d2net.netbro.transfer.model.Content;
import kr.co.d2net.netbro.transfer.model.providers.ContentProvider;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.TransferData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentDropProvider extends ViewerDropAdapter {

	final Logger logger = LoggerFactory.getLogger(getClass());
	
	private final Viewer viewer;

	public ContentDropProvider(Viewer viewer) {
		super(viewer);
		this.viewer = viewer;
	}

	@Override
	public boolean performDrop(Object data) {
		if(data != null && data instanceof String[]) {
			String[] files = (String[])data;
			for(String filepath : files) {
				
				Content content = new Content();
				/*
				content.setFilePath(filepath.substring(0, filepath.lastIndexOf(File.separator)));
				content.setFileName(filepath.substring(filepath.lastIndexOf(File.separator)+1, 
						filepath.lastIndexOf(".")));
				*/
				if(logger.isDebugEnabled()) {
					logger.debug("drop file name: "+filepath);
				}
				content.setFileName(filepath);
				content.setContentNm(filepath.substring(filepath.lastIndexOf(File.separator)+1, 
						filepath.lastIndexOf(".")));
				content.setFileType(filepath.substring(filepath.lastIndexOf(".")+1));
				content.setProgress(0);
				content.setStatus("N");
				
				ContentProvider.INSTANCE.setContent(content);
			}
			viewer.setInput(ContentProvider.INSTANCE.getContents());
			return true;
		}
		return false;
	}

	@Override
	public boolean validateDrop(Object target, int operation,
			TransferData transferType) {
		return true;
	}

}
