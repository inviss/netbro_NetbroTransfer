package kr.co.d2net.netbro.transfer.addons;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;

public class MyModelAddon {
	
	@PostConstruct
	public void init(IEclipseContext context) {
		System.out.println("I'm MyModelAddon");
		context.set("test1", "Hello");
	}
	
}
