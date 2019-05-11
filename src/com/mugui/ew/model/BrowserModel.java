package com.mugui.ew.model;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import com.mugui.ModelInterface;
import com.mugui.Dui.DOptionPanel;
import com.mugui.ew.DataSave;
import com.mugui.ew.EWUIHandel;
import com.mugui.ew.ui.MuguiBrowser;
import com.mugui.ew.ui.MuguiBrowser.DBrowserHander;
import com.mugui.http.Bean.URLBean;
import com.mugui.model.CmdModel;
import com.mugui.tool.HttpTool;
import com.mugui.tool.Other;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.By.Type;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.DOMNode;
import com.teamdev.jxbrowser.chromium.dom.DOMNodeType;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import net.sf.json.JSONArray;

public class BrowserModel implements ModelInterface {
	private static final String magnet = "magnet:?xt=urn:btih:";
	private static final String baidupan = "https://pan.baidu.com";
	private DataSave dataSave = (DataSave) EWUIHandel.datasave;
	private boolean isTrue = false;
	private MuguiBrowser father = (MuguiBrowser) dataSave.getUIManager().get("MuguiBrowser");
	private Thread mainThread = null;

	@Override
	public void init() {
		mainThread = new Thread(new Runnable() {

			@Override
			public void run() {
				RUN();
			}

		});
		isTrue = true;
	}

	@Override
	public void start() {
		if (isrun() || (mainThread != null && mainThread.isAlive())) {
			return;
		}
		init();
		mainThread.start();
	}

	@Override
	public boolean isrun() {
		return isTrue;
	}

	@Override
	public void stop() {
		isTrue = false;
	}

	private void RUN() {

		while (isrun()) {
			if (hander != null && hander.getBrowser() != null) {
				if (hander.getBrowser().isDisposed()) {
					stop();
					return;
				}
				List<DOMElement> list = hander.getBrowser().getDocument().findElements(By.tagName("img"));
				if (!list.isEmpty()) {
					for (DOMElement e : list) {
						String src[] = e.getAttribute("src").split("\\|");
						if (src[0].equals(Type.XPATH.name())) {
							Browser browser = getBrowser(src[1]);
							DOMElement domElement = browser.getDocument().findElement(By.xpath(src[2]));
							if (domElement == null)
								continue;
							String value = domElement.getAttribute("src");
							if (value != null && !value.isEmpty()) {
								if (!HttpTool.isHttpUrl(value)) {
									value = "http:" + value;
								}
								e.setAttribute("src", value);
							}

						}
					}
				}
			}

			Other.sleep(2500);
		}
	}

	private Browser baidu = null;
	private Browser bilibili = null;
	private Browser hacg = null;
	//private Browser btdb = null;
	private Browser btstation = null;
	// private Browser iwara = null;
	private Browser[] serachs = null;

	private LoadAdapter loadAdapter = new LoadAdapter() {

		public void onFinishLoadingFrame(com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent event) {
			Browser browser = event.getBrowser();
			if (browser == baidu)
				resolverBaidu(browser.getDocument());
			else if (browser == bilibili)
				resolverBili(browser.getDocument());
			else if (browser == hacg)
				resolverHacg(browser.getDocument());
//			else if (browser == btdb)
//				resolverBtdb(browser.getDocument());
			else if (browser == btstation)
				resolveBtstation(browser.getDocument());
			showUrlBeanArray();
		}

	};

	private void resolveBtstation(DOMDocument document) {

		DOMElement domElement = document.findElement(By.id("search-result-ul"));
		if (domElement == null)
			return;
		List<DOMElement> list = domElement.findElements(By.tagName("li"));
		for (DOMElement element : list) {
			Object object = resolveBtstationItem(element);
			if (object != null)
				urlBeanArray.add(object);

		}
	}

	private Object resolveBtstationItem(DOMElement domElement) {
		URLBean bean = new URLBean();
		DOMElement href = domElement.findElement(By.tagName("a"));
		if (href == null)
			return null;
		String string = href.getAttribute("href");
		int i = string.lastIndexOf("/magnet/");
		string = string.substring(i + "/magnet/".length(), string.length());
		bean.setUrl(BrowserModel.magnet + string);
		bean.setTitle(href.getTextContent());
		return bean;
	}

	private void resolverBaidu(DOMDocument document) {
		DOMElement domElement = document.findElement(By.id("content_left"));
		if (domElement == null)
			return;
		List<DOMNode> nodes = domElement.getChildren();
		for (DOMNode node : nodes) {
			if (node.getNodeType().equals(DOMNodeType.ElementNode)) {
				DOMElement element = (DOMElement) node;
				Object object = resolveBaiduItem(element);
				if (object != null)
					urlBeanArray.add(object);
			}
		}

	}

	private Object resolveBaiduItem(DOMElement domElement) {
		String srcid = domElement.getAttribute("srcid");
		if (!Other.isInteger(srcid))
			return null;
		URLBean bean = new URLBean();
		List<DOMNode> nodes = domElement.getChildren();
		for (DOMNode node : nodes) {
			if (node.getNodeType().equals(DOMNodeType.ElementNode)) {
				DOMElement element = (DOMElement) node;
				if (element.getNodeName().equals("H3")) {
					bean.setTitle(element.getTextContent().replaceAll("\\s*", ""));
					bean.setUrl(element.findElement(By.tagName("a")).getAttribute("href").trim());
				} else if (element.getNodeName().equals("DIV")) {
					bean.setDetail(element.getTextContent().replaceAll("\\s*", ""));
					return bean;
				}
			}
		}

		return null;
	}

	private void resolverBili(DOMDocument document) {

		DOMElement domElement = document.findElement(By.className("result-wrap clearfix"));
		if (domElement == null)
			return;
		List<DOMElement> list = domElement.findElements(By.tagName("a"));
		for (DOMElement element : list) {
			Object object = resolveBiliItem(element);
			if (object != null)
				urlBeanArray.add(object);

		}

	}

	private Object resolveBiliItem(DOMElement item) {
		DOMElement img = item.findElement(By.tagName("img"));
		if (img == null)
			return null;
		URLBean bean = new URLBean();
		bean.setUrl(item.getAttribute("href"));
		bean.setTitle(item.getAttribute("title"));
		String xpath = item.getParent().getXPath();
		bean.setDetail(item.getParent().findElement(By.xpath(xpath + "/div/div[2]")).getInnerText() + "<br>" + "哔哩哔哩视频：" + item.getInnerText());

		if (bean.getUrl() == null || bean.getUrl().trim().equals(""))
			return null;
		return bean;
	}

	// private int getBrowserid(Browser browser) {
	// for (int i = 0; i < serachs.length; i++)
	// if (browser.equals(serachs[i]))
	// return i;
	// return -1;
	// }

	private Browser getBrowser(String id) {
		int i = Integer.parseInt(id);
		return serachs[i];
	}

	private void resolverHacg(DOMDocument document) {

		DOMElement domElement = document.findElement(By.id("content"));
		if (domElement == null)
			return;
		List<DOMElement> list = domElement.findElements(By.tagName("article"));
		for (DOMElement element : list) {
			Object object = resolveHacgItem(element);
			if (object != null)
				urlBeanArray.add(object);

		}

	}

	private Object resolveHacgItem(DOMElement element) {
		DOMElement header = element.findElement(By.tagName("header"));
		if (header == null)
			return null;
		URLBean bean = new URLBean();
		bean.setTitle(header.getInnerText().trim());
		DOMElement aDomElement = header.findElement(By.tagName("a"));
		if (aDomElement == null)
			return null;
		bean.setUrl(aDomElement.getAttribute("href"));
		DOMElement body = element.findElement(By.xpath(header.getParent().getXPath() + "/div"));
		if (body != null)
			bean.setDetail(body.getInnerHTML());
		return bean;
	}

	private void resolverBtdb(DOMDocument document) {

		DOMElement domElement = document.findElement(By.xpath("/html/body/div[1]/div[4]/div[1]/div[3]/ul"));
		if (domElement == null)
			return;
		List<DOMElement> list = domElement.findElements(By.tagName("li"));
		for (DOMElement element : list) {
			Object object = resolveBtdbItem(element);
			if (object != null)
				urlBeanArray.add(object);

		}

	}

	private Object resolveBtdbItem(DOMElement element) {
		// /html/body/div[1]/div[4]/div[1]/div[3]/ul/li[1]/h2/a
		// /html/body/div[1]/div[4]/div[1]/div[3]/ul/li[1]/div
		DOMElement header = element.findElement(By.xpath(element.getXPath() + "/h2/a"));
		if (header == null)
			return null;
		URLBean bean = new URLBean();
		/// html/body/div[1]/div[4]/div[1]/div[3]/ul/li[1]/div/a
		bean.setTitle(header.getAttribute("title"));
		header = element.findElement(By.xpath(element.getXPath() + "/div/a"));
		if (header == null)
			return null;
		bean.setUrl(header.getAttribute("href"));
		/// html/body/div[1]/div[4]/div[1]/div[3]/ul/li[1]/ul
		header = element.findElement(By.xpath(element.getXPath() + "/ul"));
		if (header == null)
			return null;
		List<DOMNode> nodes = header.getChildren();
		String body = "";
		for (DOMNode h : nodes) {
			body += h.getTextContent() + "<br>";
		}
		bean.setDetail(body);
		return bean;
	}

	private JSONArray urlBeanArray = null;

	@SuppressWarnings("rawtypes")
	private void showUrlBeanArray() {
		if (urlBeanArray == null || urlBeanArray.isEmpty())
			return;
		ListIterator listIterator = urlBeanArray.listIterator();
		if (hander.getBrowser().isDisposed()) {
			return;
		}
		DOMElement element = hander.getBrowser().getDocument().findElement(By.id("body_view"));
		String string = "";
		while (listIterator.hasNext()) {
			string += "<div>";
			string += "<a target='_blank' href='";
			URLBean urlBean = URLBean.newInstanceBean(URLBean.class, listIterator.next());
			string += urlBean.getUrl() + "'>";
			string += urlBean.getTitle() + "</a>";
			string += "<div>" + urlBean.getDetail() + "</div>";
			string += "</div><br>";
		}
		element.setInnerHTML(string);
	}

	{
		baidu = new Browser();
		bilibili = new Browser();
		hacg = new Browser();
		//btdb = new Browser();
		// acg18 = new Browser();
		// iwara = new Browser();
		btstation = new Browser();
		serachs = new Browser[] { baidu, bilibili, hacg, btstation };
		for (Browser b : serachs) {
			b.addLoadListener(loadAdapter);
		}
	}
	private DBrowserHander hander = null;
	private String serach_text = null;

	public void reload() {
		show();
	}

	public String getSerachText() {
		return serach_text;
	}

	public void show() {
		if (isrun())
			showUrlBeanArray();
	}

	public void serach(String text) {
		if (text == null || text.trim().length() == 0)
			return;
		text = text.replaceAll(" ", "%20");
		serach_text = text;
		if (hander == null || hander.getBrowser().isDisposed()) {
			hander = new MuguiBrowser.DBrowserHander(father);
			hander.getBrowser().loadURL("http://www.mugui.net.cn/serach/");
			SwingUtilities.invokeLater(hander);

		} else {
			father.show(hander.getBrowser().hashCode() + "");
		}
		if (urlBeanArray == null) {
			urlBeanArray = new JSONArray();
		} else {
			urlBeanArray.clear();
		}

		bilibili.loadURL("https://search.bilibili.com/all?keyword=" + text);
		hacg.loadURL("https://www.liuli.in/wp/?s=" + text + "&submit=搜索");
		//btdb.loadURL("https://btdb.to/q/" + text + "/");
		// iwara.loadURL("https://ecchi.iwara.tv/search?query=" + text);
		baidu.loadURL("https://www.baidu.com/s?wd=" + text);
		btstation.loadURL("https://btstation.com/search/all/" + text + "/0");
	}

	public Object resolveHtml(Browser browser) {
		if (browser.getURL().indexOf(MuguiBrowser.LIULI)!=-1) {
			return resolveLiuliHtml(browser);
		} else {
			return resolveOtherHtml(browser);
		}
	}

	private Object resolveOtherHtml(Browser browser) {
		String body = browser.getDocument().getDocumentElement().getInnerText();
		LinkedList<String> strings = new LinkedList<>();
		strings.addAll(findMagnet(body));
		strings.addAll(findMagnet(browser.getURL()));
		strings.addAll(findBaiduPan(body));
		return strings.isEmpty() ? null : strings;
	}

	private final Pattern matcher_Magnet_1 = Pattern.compile("[^0-9a-zA-Z][0-9a-fA-F]{40}[^0-9a-zA-Z]");
	private final Pattern matcher_Magnet_2 = Pattern.compile("[^0-9a-zA-Z][0-9a-fA-Z]{32}[^0-9a-zA-Z]");
	private final Pattern matcher_BaiduPan_1 = Pattern.compile("/s/[0-9a-zA-Z]{8}");
	private final Pattern matcher_BaiduPan_2 = Pattern.compile("[^0-9a-zA-Z][0-9a-zA-Z_]{23}[^0-9a-zA-Z]");

	private Collection<? extends String> findMagnet(String html) {
		html = html.replaceAll("本站不提供下载", "");
		html = html.replaceAll("本站不提供任何下载链接", "");
		html = html.toUpperCase();
		Matcher matcher_40 = matcher_Magnet_1.matcher(html);
		LinkedList<String> strings = new LinkedList<>();
		while (matcher_40.find()) {
			String item = matcher_40.group();
			strings.add(BrowserModel.magnet + item.substring(1, item.length() - 1));
		}
		Matcher matcher_32 = matcher_Magnet_2.matcher(html);

		while (matcher_32.find()) {
			String item = matcher_32.group();
			strings.add(BrowserModel.magnet + item.substring(1, item.length() - 1));

		}
		if (!strings.isEmpty())
			strings.addFirst(BrowserModel.magnet);
		return strings;
	}

	private Object resolveLiuliHtml(Browser browser) {
		String body = browser.getDocument().findElement(By.id("content")).getInnerText();
		LinkedList<String> strings = new LinkedList<>();
		strings.addAll(findMagnet(body));
		strings.addAll(findMagnet(browser.getURL()));
		strings.addAll(findBaiduPan(body));
		return strings.isEmpty() ? null : strings;
	}

	private Collection<? extends String> findBaiduPan(String html) {
		Matcher matcher_Pan = matcher_BaiduPan_1.matcher(html);
		LinkedList<String> strings = new LinkedList<>();
		char[] body = html.toCharArray();
		while (matcher_Pan.find()) {
			int start = matcher_Pan.start();
			int end = matcher_Pan.end();
			char c = 0;
			while (end < body.length && ((c = body[end]) <= '9' && c >= '0') || (c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A')) {
				end++;
			}
			end++;
			String url = new String(body, start, end - start);
			int end_2 = (end + 48 < body.length) ? end + 48 : body.length - end;
			String message = new String(body, end, end_2 - end);
			String item = baidupan + url + "|" + message;
			strings.add(item);
		}

		matcher_Pan = matcher_BaiduPan_2.matcher(html);
		while (matcher_Pan.find()) {
			int start = matcher_Pan.start();
			int end = matcher_Pan.end();
			String url = new String(body, start + 1, end - 2 - start);
			int end_2 = (end + 48 < body.length) ? end + 48 : body.length - end;
			String message = new String(body, end, end_2 - end);
			String item = baidupan + "/s/" + url + "|" + message;
			strings.add(item);
		}

		if (!strings.isEmpty())
			strings.addFirst(BrowserModel.baidupan);
		return strings;
	}

	public void downloadRes(LinkedList<String> linkedList) {
		if (linkedList.size() > 10) {
			int i = DOptionPanel.showMessageDialog(dataSave.frame, "发现的资源连接较多，可能都是无法下载的虚假资源,是否继续下载", "提示", DOptionPanel.OPTION_OK_CANCEL);
			if (i != DOptionPanel.RET_YES) {
				return;
			}
		}
		String type = "";
		for (String str : linkedList) {
			if (str.equals(BrowserModel.magnet)) {
				type = BrowserModel.magnet;
				continue;
			}
			if (str.equals(BrowserModel.baidupan)) {
				type = BrowserModel.baidupan;
				continue;
			}
			if (type == BrowserModel.magnet) {
				System.out.println(str);
				if (isDownloadUrl(str))
					downloadUrl(str);
			}
			if (type == BrowserModel.baidupan) {
				System.out.println(str);
				downloadBaiduPan(str);
			}
		}
	}

	private void downloadBaiduPan(String str) {
		if (!isrun())
			init();
		DBrowserHander hander = new DBrowserHander(father);
		String s[] = str.split("\\|");
		hander.getBrowser().loadURL(s[0]);
		if (s.length == 2)
			hander.setPromptMessage(s[1]);
		SwingUtilities.invokeLater(hander);
	}

	public boolean isDownloadUrl(String url) {
		if (url.startsWith(BrowserModel.magnet)) {
			return true;
		}
		return false;
	}

	public void downloadUrl(String url) {
		if (url.startsWith(BrowserModel.magnet)) {
			CmdModel.run("start " + url);
			return;
		}
	}

}
