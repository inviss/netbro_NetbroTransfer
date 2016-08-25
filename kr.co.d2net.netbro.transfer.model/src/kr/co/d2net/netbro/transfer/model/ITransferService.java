package kr.co.d2net.netbro.transfer.model;


public interface ITransferService {
	public boolean connectionCheck(Transfer transfer);
	
	public boolean uploadCheck(Transfer transfer);
	
	public void manualRegister(Transfer transfer);
}
