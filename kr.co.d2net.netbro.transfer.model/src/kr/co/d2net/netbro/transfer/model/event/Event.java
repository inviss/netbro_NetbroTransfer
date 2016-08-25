package kr.co.d2net.netbro.transfer.model.event;

import java.util.ArrayList;
public final class Event<T extends EventArgs> {    
	// Event Handler List    
	private ArrayList<IEventHandler<T>> observerList = new ArrayList<IEventHandler<T>>();         
	
	// Raise Event    
	public void raiseEvent(Object sender, T t) {        
		for(IEventHandler<T> handler : this.observerList) {            
			handler.eventReceived(sender, t);        
		}    
	}         
	
	// Add Event Handler    
	public void addEventHandler(IEventHandler<T> handler) {        
		this.observerList.add(handler);    
	}         
	
	// Remove Event Handler    
	public void removeEventHandler(IEventHandler<T> handler) {        
		this.observerList.remove(handler);    
	}
}
