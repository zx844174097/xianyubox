package com.mugui.http.tcp;

public class TcpSocket extends Thread {
	protected boolean isTrue;

	public void Stop() {
		isTrue = false;
	}

	public void reStart() {
		isTrue = true;
	}

	public boolean isRun() {
		return isTrue && isAlive();
	}
}
