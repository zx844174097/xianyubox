package com.mugui.Dui;

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import com.mugui.tool.Other;

public class DFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4827923168894442407L;
	int wei, hei;

	public DFrame(int wei, int hei) {
		this.wei = wei;
		this.hei = hei;
		Dset();
	}

	public DFrame() {
		this(1200, 800);
	}

	public void clearTray() {
		if (tray != null && trayIcon != null)
			tray.remove(trayIcon);
		trayIcon = null;
	}

	public void miniTray() {
		if (miniTrayIcon == null) {
			miniTrayIcon = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
		}
		if (trayIcon == null) {
			trayIcon = new TrayIcon(miniTrayIcon, "咸鱼");
			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					setExtendedState(JFrame.NORMAL);
					setVisible(true);
					toFront();
				}
			});
			try {
				if (tray != null)
					tray.add(trayIcon);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
		if (popupMenu != null)
			trayIcon.setPopupMenu(popupMenu);
		trayIcon.setImage(miniTrayIcon);
	}

	public void setPopupMenu(PopupMenu popupMenu) {
		this.popupMenu = popupMenu;
		if (trayIcon != null)
			trayIcon.setPopupMenu(popupMenu);
	}

	private PopupMenu popupMenu = null;
	static SystemTray tray = SystemTray.getSystemTray();
	private Image miniTrayIcon = null;
	private TrayIcon trayIcon = null;

	public void setMiniTrayIcon(Image image) {
		miniTrayIcon = image;
	}

	private void Dset() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(wei, hei);
		this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - 800 / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 600 / 2);
		this.setUndecorated(true);
		this.getLayeredPane().addMouseMotionListener(new MouseMotionListener() {
			private int size = 3;

			@Override
			public void mouseMoved(MouseEvent e) {
				if (e.getX() >= 0 && e.getX() <= size) {
					if (e.getY() >= 0 && e.getY() <= size) {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
					} else if (DFrame.this.getSize().height - e.getY() <= size) {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
					} else {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
					}
				} else if (DFrame.this.getSize().width - e.getX() <= size) {
					if (e.getY() >= 0 && e.getY() <= size) {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
					} else if (DFrame.this.getSize().height - e.getY() <= size) {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
					} else {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
					}
				} else {
					if (e.getY() <= size) {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
						return;
					} else if (DFrame.this.getSize().height - e.getY() <= size) {
						DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
						return;
					}
					if (DFrame.this.getCursor().getType() == Cursor.DEFAULT_CURSOR) {
						return;
					}
					DFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				}

			}

			@Override
			public void mouseDragged(MouseEvent e) {

			}
		});
		this.getLayeredPane().addMouseListener(new MouseListener() {
			private Rectangle yuan = new Rectangle();
			private Point mouse_yuan = new Point();
			private mouseXYThread mThread = null;

			@Override
			public void mouseReleased(MouseEvent e) {
				if (mThread != null) {
					mThread.close();
				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (getCursor().getType() <= 11 && getCursor().getType() >= 4) {
					yuan.x = getX();
					yuan.y = getY();
					yuan.width = getWidth();
					yuan.height = getHeight();
					mouse_yuan = MouseInfo.getPointerInfo().getLocation();
					// mouse_yuan.x = e.getX();
					// mouse_yuan.y = e.getY();
					if (mThread == null || !mThread.isAlive()) {
						mThread = new mouseXYThread();
						mThread.start();
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			class mouseXYThread extends Thread {
				private boolean istrue = false;

				@Override
				public void run() {
					istrue = true;
					while (istrue) {
						int type = getCursor().getType();
						if (type <= 11 && type >= 4) {
							Point point = new Point();
							Point e = MouseInfo.getPointerInfo().getLocation();
							point.x = (int) (mouse_yuan.x - e.getX());
							point.y = (int) (mouse_yuan.y - e.getY());
							mouse_yuan = e;
							if (type == Cursor.NW_RESIZE_CURSOR) {
								yuan.x -= point.x;
								yuan.y -= point.y;
								yuan.width += point.x;
								yuan.height += point.y;
							} else if (type == Cursor.N_RESIZE_CURSOR) {
								yuan.y -= point.y;
								yuan.height += point.y;
							} else if (type == Cursor.W_RESIZE_CURSOR) {
								yuan.x -= point.x;
								yuan.width += point.x;
							} else if (type == Cursor.SW_RESIZE_CURSOR) {
								yuan.x -= point.x;
								yuan.width += point.x;
								yuan.height -= point.y;
							} else if (type == Cursor.S_RESIZE_CURSOR) {
								yuan.height -= point.y;
							} else if (type == Cursor.SE_RESIZE_CURSOR) {
								yuan.height -= point.y;
								yuan.width -= point.x;
							} else if (type == Cursor.E_RESIZE_CURSOR) {
								yuan.width -= point.x;
							} else if (type == Cursor.NE_RESIZE_CURSOR) {
								yuan.width -= point.x;
								yuan.height += point.y;
								yuan.y -= point.y;
							}
							setBounds(yuan);
						}

						Other.sleep(25);
					}
				}

				public void close() {
					istrue = false;
				}
			}
		});

	}
}
