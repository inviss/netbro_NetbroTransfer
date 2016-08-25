package kr.co.d2net.netbro.transfer.model.providers;

import java.util.ArrayList;
import java.util.List;

import kr.co.d2net.netbro.transfer.model.Content;

public enum ContentProvider {
	INSTANCE;
	
	List<Content> list = new ArrayList<Content>();

	public List<Content> getContents() {
		return list;
	}
	
	public void setContent(Content content) {
		this.list.add(content);
	}
	
	public void removeAll() {
		list.clear();
	}
}
