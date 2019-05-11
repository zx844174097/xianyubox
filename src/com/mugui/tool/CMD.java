package com.mugui.tool;

import java.io.*;

public class CMD {
	static private Process rt;
	private boolean isTrue;

	public boolean isColose() {
		return isTrue;
	}

	private String info = "";

	public String getInfo() {
		return info;
	}

	public void reInfo() {
		info = "";
	}

	private PrintWriter pw = null;
	private int winmode = -1;
	private String CHARSET="";
	public static final int LIUNX = 1;
	public static final int WINDOWS = 2;

	public void start() {
		String os = System.getProperties().getProperty("os.name");
		if (!os.startsWith("win") && !os.startsWith("Win")) {
			winmode = LIUNX;
			CHARSET="UTF-8";
		} else {
			winmode = WINDOWS;
			CHARSET="GBK";
		}
		isTrue = true;
		try {
			String url = null;
			if (winmode == WINDOWS)
				url = "cmd.exe /k dir ";
			else if (winmode == LIUNX) {
				url = "/bin/sh -c ls";
			}
			rt = Runtime.getRuntime().exec(url);
			getRtInput();
			getRtInEur();
			Other.sleep(50);
			pw = new PrintWriter(new OutputStreamWriter(rt.getOutputStream(),
					CHARSET));
			reInfo();
		} catch (IOException e) {
			e.printStackTrace();
			isTrue = false;
			rt.destroy();
		}

	}

	public void getRtInput() {
		new Thread(new Runnable() {
			InputStream is = null;
			BufferedReader br = null;

			public void run() {
				try {
					is = rt.getInputStream();
					br = new BufferedReader(new InputStreamReader(is, CHARSET));
					String s = null;
					while ((s = br.readLine()) != null) {
						info += s + "\r\n";
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						isTrue = false;
						if (br != null)
							br.close();
						if (is != null)
							is.close();
						rt.destroy();
					} catch (Exception e2) {

					}
				}
			}
		}).start();
	}

	public void getRtInEur() {
		new Thread(new Runnable() {
			BufferedReader br = null;
			InputStream is = null;

			public void run() {
				try {
					is = rt.getErrorStream();
					br = new BufferedReader(new InputStreamReader(is, CHARSET));
					String s = null;
					while ((s = br.readLine()) != null) {
						info += s + "\r\n";
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						isTrue = false;
						if (br != null)
							br.close();
						if (is != null)
							is.close();
						rt.destroy();
					} catch (Exception e2) {

					}

				}
			}
		}).start();
	}

	public void send(String intfo) {
		if (intfo.equals("exit")) {
			Other.sleep(200);
			rt.destroy(); 
			isTrue = false;
			return;
		}
		pw.println(intfo);
		if (intfo.trim().indexOf("cd") == 0) {
			if (winmode == WINDOWS)
				pw.println("dir");
			else if (winmode == LIUNX)
				pw.println("ls");
		}
		pw.flush();
		
	}

}
