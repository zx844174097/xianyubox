package com.mugui.ui.part;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Point;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.mugui.Dui.DPanel;

import javax.swing.JPanel;

import com.mugui.Dui.DButton;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.DyHandle;
import com.mugui.model.TCPModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.LayoutStyle.ComponentPlacement;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JLabel;

public class WebBrowrer2 extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -294051259134801444L;
	private DisplayThread displayThread;
	private Canvas canvas;
	private Browser browser;
	private JTextField textField;
	private JLabel stuts = null;

	public WebBrowrer2() {
		displayThread = new DisplayThread();
		displayThread.start();

		JPanel panel = new JPanel();
		panel.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				displayThread.display.syncExec(new Runnable() {
					@Override
					public void run() {
						browser.setVisible(false);
						browser.setVisible(true);
					}
				});

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO 自动生成的方法存根

			}
		});
		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayThread.display.syncExec(new Runnable() {
					@Override
					public void run() {
						if (browser.isBackEnabled())
							browser.back();
					}
				});
			}
		});
		button.setFont(new Font("Dialog", Font.BOLD, 12));
		button.setText("后退");

		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayThread.display.syncExec(new Runnable() {
					@Override
					public void run() {
						if (browser.isForwardEnabled())
							browser.forward();
					}
				});

			}
		});
		button_1.setText("前进");
		button_1.setFont(new Font("Dialog", Font.BOLD, 12));

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout(0, 0));

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}

		});
		button_2.setText("刷新");
		button_2.setFont(new Font("Dialog", Font.BOLD, 12));

		textField = new JTextField();
		textField.addKeyListener(new java.awt.event.KeyListener() {

			@Override
			public void keyTyped(java.awt.event.KeyEvent e) {

			}

			@Override
			public void keyReleased(java.awt.event.KeyEvent e) {

			}

			@Override
			public void keyPressed(java.awt.event.KeyEvent e) {
				if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
					String http = textField.getText();
					if (!Other.isHttpUrl(http)) {
						try {
							http = "http://www.baidu.com/baidu?word=" + URLEncoder.encode(http, "utf-8");
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
					}
					showHttp(http);
				}
			}
		});
		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String http = textField.getText();
				if (!Other.isHttpUrl(http)) {
					try {
						http = "http://www.baidu.com/baidu?word=" + URLEncoder.encode(http, "utf-8");
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}
				showHttp(http);
			}
		});
		button_3.setText("转到");
		button_3.setFont(new Font("Dialog", Font.BOLD, 12));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup().addComponent(button, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(textField, GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(button_3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(button, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(button_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(button_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(2))
				.addGroup(gl_panel.createSequentialGroup()
						.addComponent(button_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_panel.createSequentialGroup().addGap(1).addComponent(textField, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE).addContainerGap()));
		panel.setLayout(gl_panel);
		canvas = new Canvas();
		panel_1.add(canvas);

		stuts = new JLabel("");
		stuts.setFont(new Font("宋体", Font.PLAIN, 12));
		setLayout(new BorderLayout(0, 0));
		add(panel, BorderLayout.NORTH);
		add(stuts, BorderLayout.SOUTH);
		add(panel_1, BorderLayout.CENTER);

	}

	public void addNotify() {
		super.addNotify();
		Display dis = displayThread.getDisplay();
		dis.syncExec(new Runnable() {
			public void run() {
				Shell shell = SWT_AWT.new_Shell(displayThread.getDisplay(), canvas);
				shell.setLayout(new FillLayout());
				browser = new Browser(shell, SWT.BORDER | SWT.SHADOW_IN);
				// browser.setLayoutData(BorderLayout.CENTER);
				browser.addOpenWindowListener(httpListener);
				// browser.addCloseWindowListener(httpListener);
				// browser.addProgressListener(httpListener);
				browser.addStatusTextListener(httpListener);
				// browser.addLocationListener(httpListener);
				// browser.addTitleListener(httpListener);
				browser.addMouseListener(httpListener);
				// showHttp("http://www.baidu.com");
				browser.setVisible(false);

			}

			HttpListener httpListener = new HttpListener();
			private String new_php = null;

			class HttpListener implements OpenWindowListener, StatusTextListener, TitleListener, org.eclipse.swt.events.MouseListener {

				@Override
				public void changed(TitleEvent arg0) {
					// TODO 自动生成的方法存根
					 System.out.println("changed TitleEvent" + arg0.title);
				}

				@Override
				public void changed(StatusTextEvent arg0) {
					String s = arg0.text;
					setStuts(s);
					if (s.startsWith("http")) {
						new_php = s;
					} else if (s.startsWith("mugui")) {
						new_php = s;
					}
					
				}

				@Override
				public void open(WindowEvent arg0) {
					arg0.browser = browser;
					// new_php = browser.getUrl();
					if (!new_php.startsWith("http") && !new_php.startsWith("https")) {
						return;
					}
					showHttp(new_php);
				}

				@Override
				public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent paramMouseEvent) {

				}

				@Override
				public void mouseDown(org.eclipse.swt.events.MouseEvent paramMouseEvent) {

				}

				@Override
				public void mouseUp(org.eclipse.swt.events.MouseEvent paramMouseEvent) {
					String s = new_php;
					if (s == null) {
						return;
					}
				}

			}

		});
	}

	public class DisplayThread extends Thread {
		private Display display;
		Object sem = new Object();

		public void run() {
			synchronized (sem) {
				display = Display.getDefault();
				sem.notifyAll();
			}
			swtEventLoop();
		}

		private void swtEventLoop() {
			try {
				while (true) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		public Display getDisplay() {
			try {
				synchronized (sem) {
					while (display == null)
						sem.wait();
					return display;
				}
			} catch (Exception e) {
				return null;
			}
		}

	}

	private Dimension now_Dimension = new Dimension(800, 600);
	private Point now_point = new Point(0, 0);
	private boolean isTrue = false;

	@Override
	public void init() {
		displayThread.display.syncExec(new Runnable() {
			@Override
			public void run() {
				browser.setVisible(false);
				browser.setVisible(true);
				browser.setFocus();
				if (browser.getUrl().trim().equals("about:blank")) {
					showHttp("http://www.baidu.com");
				}
			}
		});
		// DataSave.StaticUI.pack();
		if (now_point != null)
			DataSave.StaticUI.setLocation(now_point);
		if (now_Dimension != null)
			DataSave.StaticUI.setSize(now_Dimension);
		if (DyHandle.isRun()) {
			DataSave.StaticUI.setAlwaysOnTop(true);
		} else
			DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.updateTitle("黑色沙漠咸鱼辅助(浏览器)");
		isTrue = true;
		if (!DyHandle.isRun()) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (isTrue) {
						TcpBag bag = new TcpBag();
						bag.setBag_id(TcpBag.ERROR);
						UserBean userBean = new UserBean();
						userBean.setCode("dy");
						bag.setBody(userBean.toJsonObject());
						TCPModel.SendTcpBag(bag);
						int i = 0;
						while (i <= 60) {
							Other.sleep(1000);
							i++;
							if (!isTrue)
								return;
						}
					}
				}
			}).start();
		}
	}

	@Override
	public void quit() {
		reInit();
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
		isTrue = false;

	}

	public void showHttp(final String string) {
		displayThread.display.syncExec(new Runnable() {
			@Override
			public void run() {
				setHttpUrl(string);
				browser.setUrl(string);

			}
		});

	}

	public void stopBrowser() {
		if (displayThread != null && displayThread.display != null && displayThread.display.isDisposed()) {
			displayThread.display.syncExec(new Runnable() {
				@Override
				public void run() {
					browser.stop();

					browser.close();
				}
			});
		}
	}

	public Browser getBrowser() {
		return browser;
	}

	public void setStuts(String s) {
		if (s.trim().equals(""))
			stuts.setText(null);
		else
			stuts.setText(s);
	}

	public void setHttpUrl(String s) {
		textField.setText(s);
	}

	public void reInit() {
		displayThread.display.syncExec(new Runnable() {
			@Override
			public void run() {
				browser.setVisible(false);
				browser.setVisible(true);
				return;
			}
		});
	}

	String html = null;

	public String getHttp() {
		html = null;
		// if (displayThread != null && displayThread.display != null &&
		// displayThread.display.isDisposed()) {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				html = browser.getText();
			}
		});
		// }
		while (html == null) {
			Other.sleep(50);
		}
		return html;
	}

	public void refresh() {
		displayThread.display.syncExec(new Runnable() {
			@Override
			public void run() {
				browser.refresh();
			}
		});
	}

	@Override
	public void dataInit() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void dataSave() {
		// TODO 自动生成的方法存根

	}
}
