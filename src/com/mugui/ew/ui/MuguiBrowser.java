package com.mugui.ew.ui;

import com.mugui.Dui.DFrame;
import com.mugui.Dui.DPanel;
import com.mugui.ew.DataSave;
import com.mugui.ew.EWUIHandel;
import com.mugui.tool.HttpTool;
import com.mugui.tool.UiTool;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.NetworkDelegate;
import com.teamdev.jxbrowser.chromium.PopupContainer;
import com.teamdev.jxbrowser.chromium.PopupHandler;
import com.teamdev.jxbrowser.chromium.PopupParams;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.events.ConsoleEvent;
import com.teamdev.jxbrowser.chromium.events.ConsoleListener;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.events.StartLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.TitleEvent;
import com.teamdev.jxbrowser.chromium.events.TitleListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import com.mugui.ModelInterface;
import com.mugui.Dui.DButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.CardLayout;
import com.mugui.Dui.DVerticalFlowLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class MuguiBrowser extends DPanel {

	public static final String BILIBILI = "www.bilibili.com/video";
	public static final String BILIBILI2 = "www.bilibili.com/bangumi";

	public static final String LIULI = "www.liuli.in/wp/";

	public static final String IQIYI = "www.iqiyi.com/v_";

	public MuguiBrowser() {
		setBackground(null);
		setLayout(new BorderLayout(0, 0));
		body = new JPanel();
		add(body, BorderLayout.CENTER);

		button_1 = new DButton((String) null, (Color) null);
		button_1.setFont(new Font("Dialog", Font.BOLD, 12));
		button_1.setText("前进");

		button = new DButton((String) null, (Color) null);
		button.setFont(new Font("Dialog", Font.BOLD, 12));
		button.setText("后退");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Component component : body.getComponents()) {
					if (component.isVisible()) {
						BrowserView browserView = (BrowserView) component;
						Browser browser = browserView.getBrowser();
						browser.goBack();
					}
				}
			}
		});
		button_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (Component component : body.getComponents()) {
					if (component.isVisible()) {
						BrowserView browserView = (BrowserView) component;
						Browser browser = browserView.getBrowser();
						browser.goForward();
					}
				}
			}
		});
		body.setLayout((bodyCard = new CardLayout(0, 0) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 8107580521575614902L;

			@Override
			public void show(Container parent, String name) {
				super.show(parent, name);
				int ncomponents = parent.getComponentCount();
				for (int i = 0; i < ncomponents; i++) {
					Component comp = parent.getComponent(i);
					if (comp.isVisible()) {
						BrowserView view = (BrowserView) comp;
						setShowUrl(view.getBrowser().getURL());
						setCanGoBack(view.getBrowser().canGoBack());
						setCanGoForward(view.getBrowser().canGoForward());
						break;
					}
				}
			}
		}));
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new DVerticalFlowLayout());
		titlePanel = new JPanel();
		panel.add(titlePanel);
		titlePanel.setBackground(null);
		FlowLayout f = new FlowLayout();
		f.setAlignment(FlowLayout.LEFT);
		titlePanel.setLayout(f);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setVgap(0);
		panel_1.add(panel_2, BorderLayout.WEST);

		panel_2.add(button);

		panel_2.add(button_1);

		DButton button_2 = new DButton((String) null, (Color) null);
		panel_2.add(button_2);
		button_2.setFont(new Font("Dialog", Font.BOLD, 12));
		button_2.setText("刷新");
		button_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (Component component : body.getComponents()) {
					if (component.isVisible()) {
						BrowserView browserView = (BrowserView) component;
						Browser browser = browserView.getBrowser();
						browser.getCacheStorage().clearCache();
						if (browser.getURL().startsWith("http://www.mugui.net.cn/serach/")) {
							EWUIHandel.datasave.getModelManager().get("BrowserModel").invokeFunction("reload");
						} else
							browser.reload();
					}
				}

			}
		});
		JPanel panel_3 = new JPanel();
		panel_1.add(panel_3, BorderLayout.CENTER);

		JLabel label = new JLabel("网址：");

		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (HttpTool.isHttpUrl(textField.getText())) {
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								DBrowserHander browser = new DBrowserHander(MuguiBrowser.this);
								browser.browser.loadURL(textField.getText());
								browser.run();
							}
						});
					} else {
						ModelInterface modelInterface = dataSave.getModelManager().get("BrowserModel");
						if (!modelInterface.isrun()) {
							modelInterface.start();
						}
						modelInterface.invokeFunction("serach", textField.getText());
					}
				}
			}
		});
		textField.setColumns(10);
		JPanel jPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) jPanel.getLayout();
		flowLayout_2.setVgap(0);

		DButton button_3 = new DButton((String) null, (Color) null);
		jPanel.add(button_3);
		button_3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				for (Component component : body.getComponents()) {
					if (component.isVisible()) {
						BrowserView browserView = (BrowserView) component;
						if (isDownload(browserView.getBrowser())) {
							return;
						}
						if (browserView.getBrowser().getURL().indexOf(BILIBILI) != -1 || browserView.getBrowser().getURL().indexOf(BILIBILI2) != -1) {
							int i = JOptionPane.showConfirmDialog(dataSave.frame, "检测到B站视频:" + browserView.getBrowser().getTitle() + ",是否下载", "提示",
									JOptionPane.OK_CANCEL_OPTION);
							if (i == 0) {
								Browser browser2 = new Browser();
								listenerList.put(browser2.hashCode(), browser2);
								browser2.loadURL(browserView.getBrowser().getURL());
							}
						} else if (browserView.getBrowser().getURL().indexOf(IQIYI) != -1) {
							int i = JOptionPane.showConfirmDialog(dataSave.frame, "检测到爱奇艺视频:" + browserView.getBrowser().getTitle() + ",是否下载", "提示",
									JOptionPane.OK_CANCEL_OPTION);
							if (i == 0) {
								Browser browser2 = new Browser();
								listenerList.put(browser2.hashCode(), browser2);
								browser2.loadURL(browserView.getBrowser().getURL());
							}
						} else if (browserView.getBrowser().getURL().indexOf(LIULI) != -1) {
							Object object = dataSave.getModelManager().get("BrowserModel").invokeFunction("resolveHtml", browserView.getBrowser());
							if (object == null) {
								JOptionPane.showMessageDialog(dataSave.frame, "该页面未发现下载资源", "提示", JOptionPane.OK_OPTION);
							} else {
								dataSave.getModelManager().get("BrowserModel").invokeFunction("downloadRes", object);
							}
						} else {

							Object object = dataSave.getModelManager().get("BrowserModel").invokeFunction("resolveHtml", browserView.getBrowser());
							if (object == null) {
								JOptionPane.showMessageDialog(dataSave.frame, "此链接未发现资源或暂不支持该网站的资源下载", "提示", JOptionPane.OK_OPTION);
							} else {
								dataSave.getModelManager().get("BrowserModel").invokeFunction("downloadRes", object);
							}
							return;
						}
					}
				}
			}

		});
		button_3.setFont(new Font("Dialog", Font.BOLD, 14));
		button_3.setText("资源下载");
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addGap(0).addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(jPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING).addComponent(label, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
				.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(textField, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addComponent(jPanel, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)));

		DButton button_4 = new DButton((String) null, (Color) null);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EWUIHandel.datasave.getUIManager().get("com.mugui.ew.ui.component.DownloadView").init();
			}
		});
		button_4.setFont(new Font("Dialog", Font.BOLD, 14));
		button_4.setText("下载列表");
		jPanel.add(button_4);
		panel_3.setLayout(gl_panel_3);

	
		UiTool.全体透明(this);

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		add(panel_4, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("提示：双击标签栏,关闭一个页面");
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setFont(new Font("宋体", Font.BOLD, 12));
		panel_4.add(lblNewLabel);
	}

	private JPanel titlePanel = null;
	private JPanel body = null;
	private CardLayout bodyCard = null;
	private DButton button = null;
	private DButton button_1 = null;

	private HashMap<Integer, Browser> listenerList = null;

	public boolean isDownload(Browser browser) {
		if (listenerList == null) {
			listenerList = new HashMap<>();
		}
		for (Browser browser2 : listenerList.values()) {
			if (browser2.isDisposed()) {
				listenerList.remove(browser2.hashCode());
			} else if (browser2.getURL().equals(browser.getURL())) {
				return true;
			}
		}
		return false;
	}

	public void removeDownload(Browser browser) {
		if (listenerList == null)
			return;
		listenerList.remove(browser.hashCode()).dispose();
	}

	public Browser getDownload(Browser browser) {
		if (listenerList == null)
			return null;
		return listenerList.get(browser.hashCode());
	}

	public static class DBrowserHander implements Runnable {
		private Browser browser = null;
		private BrowserView browserView = null;
		private DButton title = null;
		private MuguiBrowser father = null;

		public Browser getBrowser() {
			return browser;
		}

		public DBrowserHander(Browser browser, MuguiBrowser father) {
			this.browser = browser;
			this.father = father;
			ModelInterface interface1 = EWUIHandel.datasave.getModelManager().get("BrowserDownloadHandler");
			interface1.start();
			browser.setDownloadHandler((DownloadHandler) interface1);

			title = father.getNewTitleObject();
			title.setActionCommand(browser.hashCode() + "");
			father.addTitlePanel(title);
			browserView = new BrowserView(this.browser);

			father.add(browserView, browser.hashCode() + "");
			browser.setPopupHandler(new PopupHandler() {
				@Override
				public PopupContainer handlePopup(PopupParams arg0) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							if ((boolean) EWUIHandel.datasave.getModelManager().get("BrowserModel").invokeFunction("isDownloadUrl", arg0.getURL())) {
								EWUIHandel.datasave.getModelManager().get("BrowserModel").invokeFunction("downloadUrl", arg0.getURL());
								return;
							}
							Browser browser = new Browser();
							browser.loadURL(arg0.getURL());
							DBrowserHander browserHander = new DBrowserHander(browser, father);
							SwingUtilities.invokeLater(browserHander);
						}
					}).start();
					return null;

				}
			});
			browser.addTitleListener(new TitleListener() {
				@Override
				public void onTitleChange(TitleEvent var1) {
					title.setText(var1.getTitle());
					father.setShowUrl(browser.getURL());
					father.setCanGoBack(browser.canGoBack());
					father.setCanGoForward(browser.canGoForward());
				}
			});

			// browser.addConsoleListener(new ConsoleListener() {
			//
			// @Override
			// public void onMessage(ConsoleEvent var1) {
			// System.out.println(var1.getMessage());
			// }
			// });
			browser.addLoadListener(new LoadAdapter() {
				@Override
				public void onFinishLoadingFrame(FinishLoadingEvent event) {
					father.setShowUrl(event.getBrowser().getURL());
					father.setCanGoBack(event.getBrowser().canGoBack());
					father.setCanGoForward(event.getBrowser().canGoForward());
					title.setText(event.getBrowser().getTitle());
					if (event.getBrowser().getURL().startsWith("http://www.mugui.net.cn/serach/")) {
						EWUIHandel.datasave.getModelManager().get("BrowserModel").invokeFunction("show");
					}
				}

				@Override
				public void onStartLoadingFrame(StartLoadingEvent event) {
					father.setShowUrl(event.getValidatedURL());
					father.setCanGoBack(event.getBrowser().canGoBack());
					father.setCanGoForward(event.getBrowser().canGoForward());
					title.setText(event.getBrowser().getTitle());

				}
			});
			title.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() != MouseEvent.BUTTON1) {
						return;
					}
					switch (e.getClickCount()) {
					case 2:
						father.remove(browserView);
						DBrowserHander.this.browser.dispose();
						father.removeTitle(title);
						if (father.getBrowserComponentSize() == 0) {
							DBrowserHander browser = new DBrowserHander(father);
							browser.browser.loadURL("https://www.bilibili.com/");
							SwingUtilities.invokeLater(browser);
						} else {
							father.last();
						}
						break;

					default:
						father.show(((DButton) e.getSource()).getActionCommand());
						break;
					}
				}
			});
		}

		public DBrowserHander(MuguiBrowser father) {
			this(new Browser(), father);

		}

		@Override
		public final void run() {
			father.show(browser.hashCode() + "");
		}

		private final String messageHtml = "<style>\r\n"
				+ "#nav { width:400px; height: 150px; border: 2px solid #D4CD49; position:fixed;right:0;top:10% ; z-index:999999; font-size:15px;background-color: #FAEBD7}"
				+ "</style><div id=\"nav\">" + "检测到本网页的一些信息<br><MESSAGE> </div>";

		public void setPromptMessage(final String string) {
			if (string == null || string.isEmpty())
				return;
			browser.addLoadListener(new LoadAdapter() {
				@Override
				public void onFinishLoadingFrame(FinishLoadingEvent event) {
					Browser browser = event.getBrowser();
					browser.executeJavaScript("");
					DOMElement node = browser.getDocument().createElement("DIV");
					String message = messageHtml.replaceAll("<MESSAGE>", string);
					node.setInnerHTML(message);
					browser.getDocument().getDocumentElement().appendChild(node);

				}
			});
		}

	}

	public final void show(String code) {
		bodyCard.show(body, code);
	}

	public void add(BrowserView browserView, String code) {
		body.add(browserView, code);
	}

	protected void addTitlePanel(DButton title) {

		titlePanel.add(title);

	}

	protected void setShowUrl(String url) {
		if (url.startsWith("http://www.mugui.net.cn/serach/")) {
			textField.setText(dataSave.getModelManager().get("BrowserModel").invokeFunction("getSerachText").toString());
		} else
			textField.setText(url);
	}

	protected void setCanGoBack(boolean canGoBack) {
		button.setEnabled(canGoBack);
	}

	protected void setCanGoForward(boolean canGoForward) {
		button_1.setEnabled(canGoForward);
	}

	protected int getBrowserComponentSize() {
		return body.getComponentCount();
	}

	protected void last() {
		bodyCard.last(body);
	}

	protected void removeTitle(DButton title) {
		titlePanel.remove(title);
	}

	@Override
	public void remove(Component comp) {
		body.remove(comp);
	}

	private int weight = 150;

	private DButton getNewTitleObject() {
		DButton title = new DButton((String) null, (Color) null) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5913086528554664138L;

			@Override
			public void paint(Graphics g) {
				int w = father.getWidth() - 20;
				int wight = w / titlePanel.getComponentCount() - 10;
				if (wight < 25 && weight == 25) {
					super.paint(g);
					return;
				} else if (wight > 150 && weight == 150) {
					super.paint(g);
					return;
				} else if (wight != weight) {
					weight = wight;
					if (weight > 150)
						weight = 150;
					if (weight < 25)
						weight = 25;
					if (weight != this.getWidth()) {
						for (Component component : titlePanel.getComponents()) {
							component.setPreferredSize(new Dimension(weight, getHeight()));
						}
					}
				}
				super.paint(g);
			}
		};
		title.setText("");
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setPreferredSize(new Dimension(150, 25));
		title.setFont(new Font("微软雅黑", Font.PLAIN, 12));

		return title;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6167935557919438045L;
	private Dimension now_Dimension = new Dimension(1200, 800);
	private Point now_point = null;
	private DFrame father = null;

	private DataSave dataSave = (DataSave) EWUIHandel.datasave;
	private JTextField textField;

	@Override
	public void init() {
		System.out.println(this.getClass().getName()+" init");
		father = dataSave.frame;
		if (now_point != null)
			father.setLocation(now_point);
		father.setSize(now_Dimension);
		if(body.getComponentCount()==0) { 
			DBrowserHander browser = new DBrowserHander(this);
			browser.browser.loadURL("https://www.bilibili.com/");
			SwingUtilities.invokeLater(browser);
		}

		BrowserContext.defaultContext().getNetworkService()
				.setNetworkDelegate((NetworkDelegate) EWUIHandel.datasave.getModelManager().get("NetworkCheckModel"));
		EWUIHandel.datasave.getModelManager().get("NetworkCheckModel").start();
	}

	@Override
	public void quit() {
		System.out.println(this.getClass().getName()+" quit");
		now_point = father.getLocation();
		now_Dimension = father.getSize();

	}

	@Override
	public void dispose() {
		System.out.println(this.getClass().getName() + " dispose");
		titlePanel.removeAll();
		// Component[] components = body.getComponents();
		// for (Component comp : components) {
		// if (comp instanceof BrowserView) {
		// BrowserView view = (BrowserView) comp;
		// ((BrowserView) comp).getBrowser().dispose();
		// }
		// }
		body.removeAll();
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}
}
