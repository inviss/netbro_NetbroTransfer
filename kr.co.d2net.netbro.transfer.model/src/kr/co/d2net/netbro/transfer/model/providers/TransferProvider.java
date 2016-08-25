package kr.co.d2net.netbro.transfer.model.providers;

import java.util.LinkedList;
import java.util.Queue;

import kr.co.d2net.netbro.transfer.model.Transfer;

public enum TransferProvider {
	INSTANCE;
	
	private volatile Queue<Transfer> instances = new LinkedList<>();

	public void put(Transfer o) {
		instances.offer(o);
	}

	public Transfer get() {
		return instances.poll();
	}

	public Transfer peek() {
		return instances.peek();
	}

	public boolean isEmpty() {
		return instances.isEmpty();
	}

	public int size() {
		return instances.size();
	}
	
	public Transfer[] getTransfers() {
		return (Transfer[])instances.toArray();
	}

}
