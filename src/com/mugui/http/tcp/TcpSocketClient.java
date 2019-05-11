package com.mugui.http.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import com.mugui.tool.Other;

public class TcpSocketClient extends TcpSocket {

	// 信道选择器
	// private Selector selector;
	// 通信信道
	Socket socketChannel;
	// 要连接的服务器ip
	private String serverHost;
	private int serverPort;
	//private TcpSocketUserBean bean = null;

	public String getServerHost() {
		return serverHost;
	}

	public int getServerPort() {
		return serverPort;
	}

	public TcpSocketClient(String host, int prot) {
		try {
			socketChannel = new Socket(host, prot);
			initialize(socketChannel);
		} catch (IOException e) {
			e.printStackTrace();
			Stop();
		}
	}

	public TcpSocketClient(Socket socket) {
		socketChannel = socket;
		try {
			initialize(socket);
		} catch (IOException e) {
			e.printStackTrace();
			Stop();
		}
	}

	private void initialize(Socket socketChannel2) throws IOException {
		reStart();
		// selector = Selector.open();
		socketChannel2.setReceiveBufferSize(512 * 1024);
		socketChannel2.setTcpNoDelay(true);
		// clientChannel.socket().setSendBufferSize(128 * 1024);
		// clientChannel.register(selector, SelectionKey.OP_READ,
		// ByteBuffer.allocate(1024 * 1024));
		serverHost = socketChannel2.getInetAddress().getHostAddress();
		serverPort = socketChannel2.getPort();
		start();
	}

	@Override
	public void run() {
		try {
			while (isTrue) {
				receive();
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		} finally {
			close();
		}
	}

	void send(byte[] data, boolean iscom, String code) throws IOException {

		int yuan = -1;
		int xian = data.length;
		if (iscom) {
			data = Other.ArrayBytesEncryption(data);
			yuan = data.length;
			data = Other.ZIPComperssor(data);
			xian = data.length;
		}
		ByteBuffer buffer = ByteBuffer.allocate(xian + 24);
		if (code != null)
			buffer.put(code.getBytes());
		else
			buffer.put(new byte[16]);
		buffer.putInt(xian);
		buffer.putInt(yuan);
		buffer.put(data);
		buffer.clear();
		outputStream = socketChannel.getOutputStream();
		outputStream.write(buffer.array());
	}

	public void close() {

		try {
			Stop();
			
			if (buffer != null)
				buffer.clear();
			buffer = null;
			if (inputStream != null)
				inputStream.close();
			if (outputStream != null)
				outputStream.close();
			if (socketChannel != null)
				socketChannel.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ByteBuffer buffer = ByteBuffer.allocate(512 * 1024);
	private InputStream inputStream = null;
	private OutputStream outputStream = null;

	protected void receive() throws Exception {
		// protected void receive(SelectionKey key) throws IOException {

		// 读取信息获得读取的字节数
		// buffer.limit(1024);
		inputStream = socketChannel.getInputStream();
		int seek = 0;
		int bytesRead = inputStream.read(buffer.array());
		if (bytesRead <= 0) {
			close();
			return;
		}
		String code = null;
		byte[] data = null;
		while (true) {
			if (seek + 20 >= buffer.limit()) {
				buffer.position(seek);
				buffer.compact();
				bytesRead -= seek;
				seek = 0;
				buffer.position(bytesRead);
			}

			while (bytesRead < seek + 20) {
				int len = inputStream.read(buffer.array(), bytesRead, seek + 20 - bytesRead);
				if (len <= 0) {
					close();
					return;
				}
				bytesRead += len;
			}
			buffer.position(seek);
			byte[] lin = new byte[16];
			buffer.get(lin);
			int i = 0;
			for (; i < lin.length; i++) {
				if (lin[i] != 0) {
					break;
				}
			}
			if (i == lin.length)
				code = Other.getShortUuid();
			else
				code = new String(lin);
			int xian = buffer.getInt();
			seek += 20;

			if (seek + xian + 4 >= buffer.limit()) {
				buffer.position(seek);
				buffer.compact();
				bytesRead -= seek;
				seek = 0;
				buffer.position(bytesRead);
				if (xian + 4 >= buffer.limit()) {
					
					ByteBuffer byteBuffer = ByteBuffer.allocate(xian + 4);
					byteBuffer.put(buffer.array(), seek, bytesRead);
					buffer.clear();
					buffer = byteBuffer;
				}
			}

			while (bytesRead < seek + xian + 4) {
				int len = inputStream.read(buffer.array(), bytesRead, seek + xian + 4 - bytesRead);
				if (len <= 0) {
					close();
					return;
				}
				bytesRead += len;
			}
			buffer.position(seek);
			int yuan = buffer.getInt();
			data = new byte[xian];
			buffer.get(data);
			seek += xian + 4;
			if (yuan != -1) {
				data = Other.ZIPDecompressor(data, yuan);
				data = Other.ArrayBytesDecrypt(data);
				// buffer.get(data);
			}
			// 从TcpSocketPrivateMap中取出对应的 TcpSocketUserBean来处理
			TcpSocketUserBean tcpSocketUserBean = TcpSocketPrivateMap.addTcpSocketUserBean(code, this);
			tcpSocketUserBean.reCodeTime();
			tcpSocketUserBean.handleData(data);
			//bean = tcpSocketUserBean;
			// new TcpSocketHandleDataThread(data, tcpSocketUserBean).start();
			if (bytesRead == seek) {
				if (buffer != null)
					buffer.clear();
				break; 
			}
			buffer.position(seek);
		}

	}

}
