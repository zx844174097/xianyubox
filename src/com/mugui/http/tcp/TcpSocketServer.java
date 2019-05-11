package com.mugui.http.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketServer extends TcpSocket {
	private ServerSocket serverSocket = null;
	private boolean isTrue = false;
	private int port = 0;

	public TcpSocketServer(int port) {
		try {
			// 创建选择器
			serverSocket = new ServerSocket(port);
			this.port = port;
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	// 信道开始等待操作
	@Override
	public void run() {
		isTrue = true;
		while (isTrue) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
				if (socket != null)
					// 连接请求
					new TcpSocketClient(socket);
			} catch (IOException e1) {
				e1.printStackTrace(); 
				try {
					if (serverSocket != null)
						serverSocket.close();
					serverSocket = new ServerSocket(port);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
				continue;
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}