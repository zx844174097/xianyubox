package com.mugui.http.tcp;

import java.io.IOException;
import java.util.Arrays;

import com.mugui.http.HsHandle;
import com.mugui.http.pack.Bag;

import net.sf.json.JSONObject;

public class TcpSocketHandleDataThread extends Thread {

	private final static byte[] body = "<body>".getBytes();
	private final static byte[] string = Bag.String.getBytes();

	private byte[] data = null;
	private byte[] tou = new byte[64];
	private int dq_len = 0;
	private Bag bag;
	private TcpSocketUserBean tcpSocket;

	public TcpSocketHandleDataThread(byte[] body, TcpSocketUserBean tcpSocketUserBean) {
		this.data = body;
		this.tcpSocket = tcpSocketUserBean;
	}

	public void init(byte[] data2, TcpSocketUserBean tcpSocketUserBean) {
		this.data = data2;
		this.tcpSocket = tcpSocketUserBean;
		Stop();
	}

	public void Stop() {
		// synchronized (object) {
		// object.notifyAll();
		// }
	}

	//private Object object = new Object();

	public void run() {
		Run(data, tcpSocket);
	}

	public void Run(byte[] data2, TcpSocketUserBean tcpSocketUserBean) {
		this.data = data2;
		this.tcpSocket = tcpSocketUserBean;
		try {
			// while (tcpSocket.isSocketRun()) {
			dq_len = 0;
			if (data[dq_len++] != '<')
				return;
			byte b = data[dq_len++];
			boolean isTrue = true;
			int len = 0;
			if (b == 'S') {
				for (int j = 2; j < string.length; j++) {
					if (data[dq_len++] != string[j])
						isTrue = false;
				}
				if (!isTrue)
					return;
				len = 0;
				while ((tou[len++] = data[dq_len++]) != '<')
					;
				len--;
				String s = new String(tou, 0, len, "UTF-8");
				bag = (Bag) Class.forName(s).newInstance();
				dq_len++;
				dq_len++;
				HsHandle.manage(stringHandle(), tcpSocket);
			} else if (b == 'B') {
				if (!new String(data, dq_len, 36, "utf-8").equals("yteArrays>com.mugui.http.pack.TcpBag")) {
					return;
				}
				dq_len += 39;
				bag = (Bag) Class.forName("com.mugui.http.pack.TcpBag").newInstance();
				HsHandle.manage(byteArraysHandle(), tcpSocket);
			}
			// tcpSocket.threadSize--;
			// synchronized (object) {
			// object.wait();
			// }
			// }
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			tcpSocket.setThreadSize(tcpSocket.getThreadSize() - 1);
		}

	}

	private Bag byteArraysHandle() throws IOException {
		int j = dq_len;
		int len = 0;
		while (data[dq_len++] != '<') {
			len++;
		}
		String bag_id = new String(data, j, len, "UTF-8");
		dq_len += body.length - 1;
		bag.setByteArrays(bag_id, Arrays.copyOfRange(data, dq_len, data.length));
		return bag;
	}

	private Bag stringHandle() throws IOException {
		bag.setJsonObject(JSONObject.fromObject(new String(data, dq_len, data.length - dq_len, "UTF-8")));
		return bag;
	}
}
