package kr.co.d2net.netbro.common.utils;

import java.util.LinkedList;
import java.util.Queue;

public class SwappingFifoQueue<E> {

	private volatile Queue<E> instances = new LinkedList<E>();

	public void put(E o) {
		instances.offer(o);
	}

	public E get() {
		return instances.poll();
	}

	public E peek() {
		return instances.peek();
	}

	public boolean isEmpty() {
		return instances.isEmpty();
	}

	public int size() {
		return instances.size();
	}

}
