package kr.co.d2net.netbro.transfer.model.event;

public interface IEventHandler<T extends EventArgs> {    
	public void eventReceived(Object sender, T t);
}