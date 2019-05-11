package com.mugui.http;

import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.mugui.http.pack.Bag;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;

public class NetBagManager {
	/**
	 * 定义一个公开的可调用方法
	 */
	public static abstract class NetBagListener {
		protected ActionEvent event;

		public NetBagListener(ActionEvent e) {
			event = e;
		}

		public abstract void actionPerformed();
	}

	public NetBagManager() {
		init();
	}

	private static ConcurrentHashMap<Integer, NetBagListener> hashMap = null;

	public void accpetNetBag(Bag bag) {
		TcpBag tcpBag = (TcpBag) bag;
		NetBagListener listener = get(Integer.parseInt(tcpBag.getBody_description() + ""));
		((TcpBag) listener.event.getSource()).setBody(tcpBag.getBody());
		listener.actionPerformed();

	}

	public void init() {
		if (hashMap == null)
			hashMap = new ConcurrentHashMap<>();
	}

	public NetBagListener get(int name) {
		return hashMap.get(name);
	}

	public NetBagListener del(int name) {
		return hashMap.remove(name);
	}

	public void clearAll() {
		hashMap.clear();
	}

	public void add(Bag name, NetBagListener object) {
		int code = name.hashCode();
		hashMap.put(code, object);
		Timer timer = new Timer();
		timer.schedule(new DTimerTask(code) {
			@Override
			public void run() {
				hashMap.get(object).actionPerformed();
			}
		}, 15000);
		TCPModel.SendTcpBag((TcpBag) name);

	}

	abstract class DTimerTask extends TimerTask {
		Object object;

		public DTimerTask(Object object) {
			this.object = object;
		}
	}
}
