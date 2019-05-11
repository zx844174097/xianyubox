package com.mugui.ew.model;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.mugui.ModelInterface;
import com.mugui.ew.DDownloadItem;
import com.mugui.ew.DataSave;
import com.mugui.ew.EWUIHandel;
import com.mugui.ew.ui.MuguiBrowser;
import com.mugui.tool.Other;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.teamdev.jxbrowser.chromium.AuthRequiredParams;
import com.teamdev.jxbrowser.chromium.BeforeRedirectParams;
import com.teamdev.jxbrowser.chromium.BeforeSendHeadersParams;
import com.teamdev.jxbrowser.chromium.BeforeSendProxyHeadersParams;
import com.teamdev.jxbrowser.chromium.BeforeURLRequestParams;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Cookie;
import com.teamdev.jxbrowser.chromium.DataReceivedParams;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.HeadersReceivedParams;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.NetworkDelegate;
import com.teamdev.jxbrowser.chromium.PACScriptErrorParams;
import com.teamdev.jxbrowser.chromium.RequestCompletedParams;
import com.teamdev.jxbrowser.chromium.RequestParams;
import com.teamdev.jxbrowser.chromium.ResponseStartedParams;
import com.teamdev.jxbrowser.chromium.SendHeadersParams;

public class NetworkCheckModel implements ModelInterface, NetworkDelegate {
	private final static String BILIBILI_VIDEO_1 = "https://upos-hz-mirror";
	private final static String IQIYI_VIDEO_1 = "https://rc93n.jomodns.com/";
	private final static String BILIBILI_VIDEO_2 = "https://ip*.mobgslb.tbcache.com";
	private final static String BILIBILI_VIDEO_3 = "https://upos-hz-mirrorks3u";
	private DataSave dataSave = null;
	private boolean isTrue = false;
	private Thread mainThread = null;
	private MuguiBrowser father = null;
	private static String BILILBLILI_VIDEO_ARRY[] = { BILIBILI_VIDEO_1, BILIBILI_VIDEO_2, BILIBILI_VIDEO_3 };
	static {
		// 向服务器请求改资源
		// TcpBag bag = new TcpBag();
		// bag.setBag_id(TcpBag.GET_DATA);
		// DataTypeBean bean = new DataTypeBean();
		// bean.setType(DataTypeBean.FILE);
		// bean.setBody("com.mugui.ew.data.BROWSER_NET_CHECK.json");
		// bag.setBody(bean.toJsonObject());
		// bag = TCPModel.waitAccpetSend(bag);
		//
		// System.out.println(bag);
		// if (bag != null && bag.getBody() instanceof String[])
		// BILILBLILI_VIDEO_ARRY = (String[]) bag.getBody();
		//
		// 用类加载方式请求该资源,可以同时验证本地和服务器。
		InputStream inputStream = ((DataSave) EWUIHandel.datasave).loader.getResourceAsStream("BROWSER_NET_CHECK_BILI.data");
		if (inputStream != null) {
			ByteOutputStream outputStream = new ByteOutputStream();
			try {
				int len = 0;
				byte[] bs = new byte[1024];
				while ((len = inputStream.read(bs)) > 0) {
					outputStream.write(bs, 0, len);
				}
				outputStream.close();
				inputStream.close();
				String string = outputStream.toString();
				System.out.println(NetworkCheckModel.class.getName() + "->BILILBLILI_VIDEO_ARRY:" + string);
				if (!string.trim().isEmpty()) {
					BILILBLILI_VIDEO_ARRY = string.split("\\|");
				}
			} catch (IOException e) {
			}
		}
	}

	// https://www.bilibili.com/video/av39054091
	@Override
	public void init() {
		dataSave = (DataSave) EWUIHandel.datasave;
		father = (MuguiBrowser) dataSave.getUIManager().get("MuguiBrowser");
		list = new ConcurrentHashMap<String, NetworkCheckModel.DataBean>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7001813225027498006L;

			/**
			 * reloadAccess : "mukio_reloadAccess", play : "jwPlay",/*播放 pause :
			 * "jwPause",/*暂停 stop : "jwStop",/*停止 seek : "jwSeek",/*回到某个seek
			 * prev : "jwPlaylistPrev", next : "jwPlaylistNext", getBufferRate :
			 * "jwGetBuffer",/*加载进度 getDuration : "jwGetDuration", isFullScreen
			 * : "jwGetFullscreen", getWidth : "jwGetWidth", getHeight :
			 * "jwGetHeight", isMute : "jwGetMute", setMute : "jwSetMute",
			 * getPlaylist : "jwGetPlaylist", getPlaylistIndex :
			 * "jwGetPlaylistIndex", getCurrentTime : "jwGetPosition", getState
			 * : "jwGetState", getVersion : "jwGetVersion", getPlayurl :
			 * "jwGetPlayurl", volume : "jwGetVolume|jwSetVolume",
			 * sendADShowState : "sendADShowState"
			 * 
			 * 
			 * 
			 */
			@Override
			public DataBean put(String key, DataBean value) {
				if (key == null || value == null) {
					return null;
				}
				if (get(key) != null)
					return null;
				value.item.getBrowser().executeJavaScript("var player_placeholder=document.getElementById('player_placeholder')");
				value.item.getBrowser().executeJavaScript("player_placeholder.jwSetVolume(0)");
				return super.put(key, value);
			}

			@Override
			public void clear() {
				if (isEmpty()) {
					super.clear();
					return;
				}
				Iterator<Entry<String, DataBean>> iterator = entrySet().iterator();

				while (iterator.hasNext()) {
					Entry<String, DataBean> entry = iterator.next();
					entry.getKey();
					entry.getValue().end();
				}
				super.clear();
			}
		};
		mainThread = new Thread(new Runnable() {

			@Override
			public void run() {
				RUN();
				try {
					synchronized (key) {
						key.notifyAll();
					}
				} catch (Exception e) {
				}
			}

		});
		isTrue = true;
	}

	private void RUN() {
		while (isrun()) {
			Other.sleep(3000);
			if (list.isEmpty())
				continue;
			Iterator<Entry<String, DataBean>> iterator = list.entrySet().iterator();
			double seek = 0;
			while (iterator.hasNext()) {
				Entry<String, DataBean> entry = iterator.next();
				if (entry.getValue().item.getBrowser().isDisposed()) {
					iterator.remove();
					continue;
				}
				if (entry.getValue().item.getBrowser().getURL().indexOf(MuguiBrowser.BILIBILI) != -1
						|| entry.getValue().item.getBrowser().getURL().indexOf(MuguiBrowser.BILIBILI2) != -1) {
					JSValue value = entry.getValue().item.getBrowser().executeJavaScriptAndReturnValue("player_placeholder.jwGetBuffer()");
					if (value.isNumber()) {
						double dou = Double.parseDouble(value.toString().trim());
						if (dou == 100.00d) {
							entry.getValue().end();
							iterator.remove();
							return;
						}
						((DDownloadItem) entry.getValue().item).setPercentComplete((int) dou);
						value = entry.getValue().item.getBrowser().executeJavaScriptAndReturnValue("player_placeholder.jwGetDuration()");
						if (value.isNumber()) {
							double length = Double.parseDouble(value.toString().trim());
							double temp = length * dou / 100.0d - 30;
							if (temp > 0) {
								if (temp - seek > 30) {
									seek = temp;
									value = entry.getValue().item.getBrowser().executeJavaScriptAndReturnValue("player_placeholder.jwGetPosition()");
									double play_seek = Double.parseDouble(value.toString().trim());
									if (seek - play_seek > 30)
										entry.getValue().item.getBrowser().executeJavaScriptAndReturnValue("player_placeholder.jwSeek(" + seek + ")");

								}
							}
						}
					}
				} else if (entry.getValue().item.getBrowser().getURL().startsWith(MuguiBrowser.IQIYI)) {
					// 爱奇艺操作

				}
			}

		}

	}

	@Override
	public void start() {
		if (isrun())
			return;
		init();
		mainThread.start();

	}

	@Override
	public boolean isrun() {
		return isTrue;
	}

	private Object key = new Object();

	@Override
	public void stop() {
		System.out.println(this.getClass().getName() + " stop");
		try {
			synchronized (key) {
				isTrue = false;
				key.wait();
			}
		} catch (Exception e) {
		}
		if (list != null)
			list.clear();

	}

	private ConcurrentHashMap<String, DataBean> list = null;

	private String getURLName(String url) {
		url = url.split("\\?")[0];
		URL url2 = null;
		try {
			url2 = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		String name = new File(url2.getFile()).getName();
		name = name.split("\\?")[0];
		if (name.length() > 128) {
			name = name.substring(0, 128);
		}
		return name;
	}

	@Override
	public void onBeforeURLRequest(BeforeURLRequestParams var1) {
	}

	@Override
	public void onBeforeRedirect(BeforeRedirectParams var1) {
	}

	@Override
	public void onBeforeSendHeaders(BeforeSendHeadersParams var1) {
	}

	@Override
	public void onSendHeaders(SendHeadersParams var1) {
	}

	@Override
	public void onHeadersReceived(HeadersReceivedParams var1) {
	}

	@Override
	public void onResponseStarted(ResponseStartedParams var1) {

	}

	private LinkedList<Browser> linkedList = new LinkedList<>();

	public void addBrowserToNetListener(Browser browser) {
		linkedList.add(browser);
	}

	@Override
	public void onDataReceived(DataReceivedParams var1) {
		// System.out.println(var1.getURL());
		if (var1.getBrowser() != null && father.getDownload(var1.getBrowser()) != null) {
			boolean bool = false;
			for (String url : BILILBLILI_VIDEO_ARRY) {
				// System.out.println(url+" "+var1.getURL().matches(url));
				if (var1.getURL().matches(url)) {
					bool = true;
					break;
				}
			}
			if (bool) {
				// bilibili flash播放器 player
				Browser browser = var1.getBrowser();
				DataBean bean = list.get(browser.getURL());
				if (bean == null) {
					String name = getURLName(var1.getURL());
					bean = new DataBean().init(name, browser);
					if (bean == null) {
						father.removeDownload(var1.getBrowser());
						return;
					}
					browser.executeJavaScriptAndReturnValue("player_placeholder.jwSetMute()");
					list.put(browser.getURL(), bean);
				}
				bean.save(var1.getData());

			} else if (var1.getURL().startsWith(IQIYI_VIDEO_1)) {
				// IQIYI_VIDEO_1 播放器 player
				Browser browser = var1.getBrowser();
				DataBean bean = list.get(browser.getURL());
				if (bean == null) {
					String name = getURLName(var1.getURL());
					bean = new DataBean().init(name, browser);
					if (bean == null)
						return;
					list.put(browser.getURL(), bean);
				}
				bean.save(var1.getData());
			}
		}
		for (Browser browser : linkedList) {
			if (browser == var1.getBrowser()) {
				System.out.println(var1.getURL());
			}
		}
	}

	@Override
	public void onCompleted(RequestCompletedParams var1) {
	}

	@Override
	public void onDestroyed(RequestParams var1) {

	}

	@Override
	public boolean onAuthRequired(AuthRequiredParams params) {
		String var2 = "该网页 " + params.getURL() + " 需要用户名和密码.";
		AtomicBoolean var3 = new AtomicBoolean();

		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {

					JTextField var1 = new JTextField();
					JPasswordField passwordField = new JPasswordField();
					Object[] objects = new Object[] { new JLabel(var2), Box.createVerticalStrut(10), new JLabel("用户名:"), var1, new JLabel("密码:"),
							passwordField };
					if (JOptionPane.showConfirmDialog((Component) null, objects, "Authentication Required", 2, -1) == 0) {
						params.setUsername(var1.getText());
						params.setPassword(new String(passwordField.getPassword()));
						var3.set(false);
					} else {
						var3.set(true);
					}

				}
			});
		} catch (InterruptedException var4) {
			Thread.currentThread().interrupt();
		} catch (InvocationTargetException var5) {
			throw new RuntimeException(var5);
		}

		return var3.get();
	}

	@Override
	public boolean onCanSetCookies(String var1, List<Cookie> var2) {
		return true;
	}

	@Override
	public boolean onCanGetCookies(String var1, List<Cookie> var2) {
		return true;
	}

	@Override
	public void onBeforeSendProxyHeaders(BeforeSendProxyHeadersParams var1) {
	}

	@Override
	public void onPACScriptError(PACScriptErrorParams var1) {
	}

	private class DataBean {
		private DownloadItem item = null;

		public DataBean init(String name, Browser browser) {
			File file = new File(dataSave.JARFILEPATH + "\\browser\\download\\" + name);
			item = (DownloadItem) dataSave.loader.loadClassToObject("com.mugui.ew.DDownloadItem", browser);
			item.setDestinationFile(file);
			ModelInterface interface1 = EWUIHandel.datasave.getModelManager().get("BrowserDownloadHandler");
			interface1.start();
			boolean bool = (boolean) interface1.invokeFunction("allowDownload", item);
			return bool ? this : null;
		}

		public void save(byte[] data) {
			((DDownloadItem) item).save(data);
		}

		public void end() {
			((DDownloadItem) item).finash();
			father.removeDownload(item.getBrowser());
		}
	}
}
