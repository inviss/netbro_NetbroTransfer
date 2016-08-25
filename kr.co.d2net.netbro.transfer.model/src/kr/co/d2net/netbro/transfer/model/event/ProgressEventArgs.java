package kr.co.d2net.netbro.transfer.model.event;

import kr.co.d2net.netbro.transfer.model.Transfer;

public class ProgressEventArgs extends EventArgs {
	private Transfer transfer;
	
	public ProgressEventArgs(Transfer transfer) {
		this.transfer = transfer;
	}

	public Transfer getTransfer() {
		return transfer;
	}
	
}
