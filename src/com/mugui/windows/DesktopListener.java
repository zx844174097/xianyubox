package com.mugui.windows;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

public class DesktopListener {
	private static DesktopListenerThread desktopListenerThread = null;

	public static void start() {
		if (desktopListenerThread == null || !desktopListenerThread.isAlive()) {
			desktopListenerThread = new DesktopListenerThread();
			desktopListenerThread.start();
		}
		isTrue = true;
	}

	private static boolean isTrue = false;

	public static void stop() {
		isTrue = false;
	}

	static class DesktopListenerThread extends Thread {
		private Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

		@Override
		public void run() {
			Tool tool = new Tool();
			int[] lin = null;
			byte b[] = new byte[dimension.width * dimension.height * 8];
			int len = 0;  
			int data[] = null;
			File file = new File(DataSave.JARFILEPATH + "/temp/");
			if (!file.isDirectory()) {
				file.mkdir();
			} 
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(new File(DataSave.JARFILEPATH + "/temp/" + System.currentTimeMillis() + ".muguiX"));
				fileOutputStream.write("muguiX".getBytes());
				fileOutputStream.write((dimension.width + "*" + dimension.height + "<|>").getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
				isTrue = false;
			}
			long time_next = System.currentTimeMillis() + 100;
			while (isTrue) {
				data = tool.getScreenBufferInt(0, 0, dimension.width, dimension.height);
				try {
					if (lin == null) {
						lin = new int[dimension.width * dimension.height];
					}
					int l = 0;
					for (int i = 0; i < lin.length; i++) {
						if (data[i] != lin[i]) {
							byte f = 0;
							if (i > 1 && data[i] == data[i - 1]) {
								f = 1;
								if (i - l < 127) {
									b[len++] = f;
									b[len++] = (byte) ((l - i) & 0xFF);
								} else
									f = 0;
							} else if (i > dimension.width) {
								if (data[i] == data[i - dimension.width])
									f = 3;
								else if (i < lin.length - 1 && data[i] == data[i - dimension.width + 1]) {
									f = 4;
								}
							} else if (i > dimension.width + 1 && data[i] == data[i - dimension.width - 1]) {
								f = 2;
							}
							if (f == 0) {
								b[len++] = f;
								b[len++] = (byte) (i & 0xFF);
								b[len++] = (byte) ((i >> 8) & 0xFF);
								b[len++] = (byte) ((i >> 16) & 0xFF);
								b[len++] = (byte) ((i >> 24) & 0xFF);
							}
							l = i;
							int kk = -data[i];
							b[len++] = (byte) (kk & 0xFF);
							b[len++] = (byte) ((kk >> 8) & 0xFF);
							b[len++] = (byte) ((kk >> 16) & 0xFF);
						}
					}
					lin = data;
					byte[] body = Other.ZIPComperssor(b);
					int leng = body.length;
					byte c[] = new byte[4];
					c[0] = (byte) (leng & 0xFF);
					c[1] = (byte) ((leng >> 8) & 0xFF);
					c[2] = (byte) ((leng >> 16) & 0xFF);
					c[3] = (byte) ((leng >> 24) & 0xFF);
					fileOutputStream.write(c);
					leng = len;
					c[0] = (byte) (leng & 0xFF);
					c[1] = (byte) ((leng >> 8) & 0xFF);
					c[2] = (byte) ((leng >> 16) & 0xFF);
					c[3] = (byte) ((leng >> 24) & 0xFF);
					fileOutputStream.write(c);
					fileOutputStream.write(body);
					fileOutputStream.write("<|>".getBytes());
					len = 0;
					while (System.currentTimeMillis() < time_next) {
						tool.delay(1);
					}
					time_next = System.currentTimeMillis() + 100;
				} catch (Exception e) {
					e.printStackTrace();
					isTrue = false;
					return;
				} finally {

				}
			}
		}
	}
}
