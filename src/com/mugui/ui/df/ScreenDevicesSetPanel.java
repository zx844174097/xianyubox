package com.mugui.ui.df;

import com.mugui.Dui.DPanel;
import com.mugui.ui.df.ScreenDeviceThumbnail.DActionListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import java.awt.AWTException;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import com.mugui.Dui.DButton;
import java.awt.Color;

public class ScreenDevicesSetPanel extends DPanel {
	private JPanel body = null;

	public ScreenDevicesSetPanel() {
		setLayout(new BorderLayout(0, 0));

		body = new JPanel();
		add(body, BorderLayout.CENTER);
		body.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.SOUTH);

		DButton button = new DButton((String) null, (Color) null);
		button.setText("确认");
		panel_2.add(button);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
				panel.setLayout(new BorderLayout(0, 0));
		
				JPanel panel_1 = new JPanel();
				panel.add(panel_1);
				
						JLabel label = new JLabel("请选择一个你要使用的屏幕");
						label.setFont(new Font("宋体", Font.BOLD, 16));
						panel_1.add(label);
						
						JPanel panel_3 = new JPanel();
						panel.add(panel_3, BorderLayout.SOUTH);
						
						JLabel label_1 = new JLabel("若选择分屏后，盒子无法正常使用，请前往设置关闭另一种截图方式");
						label_1.setForeground(Color.RED);
						label_1.setFont(new Font("宋体", Font.PLAIN, 12));
						panel_3.add(label_1);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (device != null)
					synchronized (object) {
						object.notify();
					}
			}
		});
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7597553849200972251L;

	@Override
	public void init() {
		initUI();

	}

	private ScreenDeviceThumbnail[] screenList = null;
	private GraphicsDevice device = null;

	private void initUI() {
		GraphicsEnvironment graphicsEnvironmen = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] de = graphicsEnvironmen.getScreenDevices();
		screenList = new ScreenDeviceThumbnail[de.length];
		for (int i = 0; i < de.length; i++) {
			if (de[i] != null && de[i].getType() == GraphicsDevice.TYPE_RASTER_SCREEN)
				try {
					screenList[i] = new ScreenDeviceThumbnail(de[i]);
					screenList[i].addActionListener(action);
					if (i == 0) {
						screenList[i].setSelected(true);
						device = screenList[i].getDevice();
					}
					body.add(screenList[i]);
				} catch (AWTException e) {
					e.printStackTrace();
				}

		}
	}

	private void updateSelect(Object object2) {
		for (ScreenDeviceThumbnail thumbnail2 : screenList) {
			if (thumbnail2 != (ScreenDeviceThumbnail) object2) {
				thumbnail2.setSelected(false);
			} else {
				thumbnail2.setSelected(!thumbnail2.isSelected());
				if (thumbnail2.isSelected()) {
					device = thumbnail2.getDevice();
				} else {
					device = null;
				}
			}
		}
	}

	private DActionListener action = new DActionListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mousePressed(MouseEvent e) {

			updateSelect(e.getSource());
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO 自动生成的方法存根

		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}
	};

	@Override
	public void quit() {

	}

	private Object object = new Object();

	public GraphicsDevice getSelectScreenDevice() {
		synchronized (object) {
			try {
				object.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return device;
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
