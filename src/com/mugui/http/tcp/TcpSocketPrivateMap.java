package com.mugui.http.tcp;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class TcpSocketPrivateMap {

	private static ConcurrentHashMap<String, TcpSocketUserBean> concurrentHashMap = new ConcurrentHashMap<String, TcpSocketUserBean>();

	protected static int tcpHandleThreadLimitSize = 20;
	protected static int tcpHandleThreadSize = 0;

	protected static long CODE_ACTIVE_TIME = 15 * 1000 * 60;
	private static byte[] ownCode = new byte[8];

	protected static byte[] getOwnCode() {
		return ownCode;
	}

	public static void setOwnCode(String ownCode) {
		TcpSocketPrivateMap.ownCode = ownCode.getBytes();
	}

	public static void setOutTime(long outTime) {
		CODE_ACTIVE_TIME = outTime;
	}

	public static TcpSocketUserBean getTcpSocketUserBean(String code) {
		TcpSocketUserBean bean = concurrentHashMap.get(code);
		if (bean == null)
			return null;
		if (bean.isOutTimeLimit()) {
			bean = concurrentHashMap.remove(code);
			if (bean != null) {
				bean.clearUserAllData();
			}
			return null;
		}
		return bean;
	}

	public static TcpSocketUserBean addTcpSocketUserBean(String code, TcpSocketClient channel) {
		TcpSocketUserBean bean = getTcpSocketUserBean(code);
		if (bean != null) { 
			bean.setSocketChannel(channel);
			return bean;
		}
		// 有新的连接加入就清理一次
		Iterator<Entry<String, TcpSocketUserBean>> iterator = TcpSocketPrivateMap.concurrentHashMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, TcpSocketUserBean> entry = iterator.next();
			if (entry.getValue().isOutTimeLimit() && entry.getValue().isSocketRun()) {
				iterator.remove();
			}
		}
		bean = new TcpSocketUserBean();
		bean.setCode(code);
		bean.setSocketChannel(channel);
		bean.setCodeTime(System.currentTimeMillis());
		concurrentHashMap.put(code, bean);
		return bean;
	}

	public synchronized static void clearTcpSocketAllUser() {
		Iterator<Entry<String, TcpSocketUserBean>> iterator = concurrentHashMap.entrySet().iterator();
		while (iterator.hasNext()) {
			iterator.next().getValue().clearUserAllData();
			iterator.remove();
		}
	}
}
