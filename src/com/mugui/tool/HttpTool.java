package com.mugui.tool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTool {
	// 得到newSocket
	public synchronized static Socket getSocket(String Host, int Port) {
		return getSocket(Host, Port, 0);
	}

	public synchronized static Socket getSocket(String Host, int Port, int time) {
		try {
			Socket s = new Socket(Host, Port);
			s.setSoTimeout(time);
			return s;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 得到一个InetAddress
	public static InetAddress getInetAddress(String Host) {
		try {
			return InetAddress.getByName(Host);
		} catch (UnknownHostException e) {

		}
		return null;
	}

	public static boolean isHttpUrl(String urls) {
		if (urls == null || urls.isEmpty())
			return false;

		String regex = "(((https|http|ftp)?://)?([a-z0-9]+[.])|(www.))"
				+ "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";// 设置正则表达式

		Pattern pat = Pattern.compile(regex.trim());// 比对
		Matcher mat = pat.matcher(urls.trim());
		if (mat.matches())
			return true;
		URL url;
		try {
			url = new URL(urls);
			String[] string = url.getHost().split("\\.");
			if (string.length != 4)
				return false;
			for (String str : string) {
				if (!Other.isInteger(str))
					return false;
			}
			return true;
		} catch (MalformedURLException e) {
		}
		return false;
	}

}
