package kr.co.d2net.netbro.common.filters;

import java.io.File;
import java.io.FileFilter;

public class UserFileFilter implements FileFilter {

	private final String[] useFileExtensions = new String[] {"json", "JSON"};
	
	private String[] exts = null;
	
	public UserFileFilter(String[] exts) {
		this.exts = exts;
	}

	public boolean accept(File file) {
		if(exts != null && exts.length > 0) {
			for(String extension : exts) {
				if (file.getName().toLowerCase().endsWith(extension)) {
					return true;
				}
			}
		} else {
			for (String extension : useFileExtensions) {
				if (file.getName().toLowerCase().endsWith(extension)) {
					return true;
				}
			}
		}
		return false;
	}

}
