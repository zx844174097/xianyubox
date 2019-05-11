package com.mugui.ew.ui;

import com.mugui.Dui.DPanel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import com.mugui.Dui.DVerticalFlowLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

import com.mugui.Dui.DButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import com.mugui.Dui.DTextField;
import com.mugui.Dui.DrawImg;
import com.mugui.Dui.ToggleButtonManage;
import com.mugui.tool.Other;

import javax.swing.JRadioButton;

import com.mugui.ui.DataSave;
import com.mugui.ui.part.XYPanel;
import com.mugui.windows.Tool;

public class UsersPrivateScript extends DPanel {
	private ToggleButtonManage manage = new ToggleButtonManage();
	private ToggleButtonManage manage2 = new ToggleButtonManage();

	public UsersPrivateScript() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new DVerticalFlowLayout());

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		panel_3.add(panel_5);

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (button.getText().equals("开始脚本")) {
					if (getSeekImage() == null) {
						return;
					}
					if (scriptThread.isAlive())
						return;
					scriptThread = new ScriptThread();
					scriptThread.start();
					button.setText("停止运行");
				} else {
					scriptThread.Stop();
					button.setText("开始脚本");
				}
			}
		});
		button.setFont(new Font("Dialog", Font.BOLD, 15));
		button.setText("开始脚本");
		panel_5.add(button);

		JRadioButton radioButton_2 = new JRadioButton("兼容模式");
		panel_5.add(radioButton_2);
		radioButton_2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.兼容 = radioButton_2.isSelected();

			}
		});

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_4);

		checkBox = new JCheckBox("循环运行？");
		panel_4.add(checkBox);

		JLabel label_2 = new JLabel("单次间隔时间");
		panel_4.add(label_2);

		textField = new DTextField(6);
		textField.setColumns(6);
		textField.setText("10");
		panel_4.add(textField);

		JLabel lblMs = new JLabel("秒");
		panel_4.add(lblMs);

		JLabel label_8 = new JLabel("      ");
		panel_4.add(label_8);

		radioButton_3 = new JRadioButton("鼠标左键");
		panel_4.add(radioButton_3);

		JRadioButton radioButton_4 = new JRadioButton("鼠标右键");
		panel_4.add(radioButton_4);
		manage2.addRadioButton(radioButton_3);
		manage2.addRadioButton(radioButton_4);
		JLabel label_9 = new JLabel("    ");
		panel_4.add(label_9);

		radioButton = new JRadioButton("鼠标单击");
		panel_4.add(radioButton);

		manage.addRadioButton(radioButton);

		JRadioButton radioButton_1 = new JRadioButton("鼠标双击");
		panel_4.add(radioButton_1);
		manage.addRadioButton(radioButton_1);

		JPanel panel_8 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_8.getLayout();
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_8);

		JLabel label_4 = new JLabel("识图区域：");
		panel_8.add(label_4);

		JLabel label_5 = new JLabel("左上角");
		panel_8.add(label_5);

		left = new XYPanel();
		panel_8.add(left);

		JLabel label_6 = new JLabel("右下角");
		panel_8.add(label_6);

		right = new XYPanel();
		panel_8.add(right);

		JLabel label_7 = new JLabel("（若未设置或设置错误则默认全屏）");
		panel_8.add(label_7);
		JPanel panel_6 = new JPanel();
		panel.add(panel_6, BorderLayout.CENTER);
		panel_6.setLayout(new BorderLayout(0, 0));

		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_7.getLayout();
		flowLayout_2.setHgap(10);
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		flowLayout_2.setVgap(0);
		panel_6.add(panel_7, BorderLayout.NORTH);

		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawImg.lastDraw();
			}
		});
		button_1.setFont(new Font("Dialog", Font.BOLD, 13));
		button_1.setText("上一次");
		panel_7.add(button_1);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawImg.drawImg(tool.截取屏幕(0, 0, screenDimension.width, screenDimension.height));
			}
		});
		button_2.setFont(new Font("Dialog", Font.BOLD, 13));
		button_2.setText("截取屏幕");
		panel_7.add(button_2);

		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point point = 区域中查找对应图();
				if (point != null) {
					point.x += getSeekImage().getWidth() / 2;
					point.y += getSeekImage().getHeight() / 2;
					tool.mouseMove(point.x, point.y);
				}
			}
		});
		button_3.setFont(new Font("Dialog", Font.BOLD, 13));
		button_3.setText("测试");
		panel_7.add(button_3);

		JLabel label_3 = new JLabel("（选择好截图后点击测试按钮，确认鼠标移动到目标处）");
		label_3.setForeground(Color.RED);
		panel_7.add(label_3);

		drawImg = new DrawImg((JPanel) this);

		JScrollPane scrollPane_1 = new JScrollPane(drawImg);
		panel_6.add(scrollPane_1, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.WEST);
		panel_2.setLayout(new BorderLayout(0, 0));

		JLabel label_1 = new JLabel("小工具列表");
		label_1.setFont(new Font("宋体", Font.BOLD, 16));
		panel_2.add(label_1, BorderLayout.NORTH);

		JPanel panel_1 = new JPanel();
		JScrollPane scrollPane = new JScrollPane(panel_1);
		panel_2.add(scrollPane, BorderLayout.CENTER);

		panel_1.setLayout(new DVerticalFlowLayout());

		JLabel label = new JLabel("根据截图点击某处");
		panel_1.add(label);
	}

	public int getMouseNum() {
		if (manage.getSelectButton() == radioButton) {
			return 1;
		} else
			return 2;
	}

	public int getMouseCode() {
		if (manage2.getSelectButton() == radioButton_3) {
			return InputEvent.BUTTON1_MASK;
		} else
			return InputEvent.BUTTON3_MASK;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2600971253545016558L;
	private JRadioButton radioButton;
	private JRadioButton radioButton_3;
	private JCheckBox checkBox;
	private DTextField textField;
	private XYPanel left;
	private XYPanel right;
	private DrawImg drawImg;
	private Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

	public boolean isWhileRun() {
		return checkBox.isSelected();
	}

	public int getIntervalTime() {
		if (Other.isInteger(textField.getText().trim())) {
			return Integer.parseInt(textField.getText().trim()) * 1000;
		}
		return 10000;
	}

	public Rectangle getSeekRect() {
		Point point = left.getX_YPoint();
		Point point2 = right.getX_YPoint();
		Rectangle rectangle = new Rectangle(0, 0, screenDimension.width, screenDimension.height);
		if (point2.x - point.x <= 0 || point2.y - point.y <= 0)
			return rectangle;
		if (point.getX() <= 0 && point.getY() <= 0)
			return rectangle;
		if (point2.getX() < point.getX() || point2.getY() < point.getY())
			return rectangle;

		return new Rectangle(point.x, point.y, point2.x - point.x, point2.y - point.y);
	}

	public BufferedImage getSeekImage() {
		return drawImg.nowDraw();
	}

	@Override
	public void init() {
	}

	@Override
	public void quit() {
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

	private ScriptThread scriptThread = new ScriptThread();
	private boolean script_bool = false;

	private class ScriptThread extends Thread {
		@Override
		public void run() {
			script_bool = true;
			DataSave.鼠标修正 = true;
			Run();
		}

		public void Stop() {
			script_bool = false;
		}
	}

	private Tool tool = new Tool();

	protected Point 区域中查找对应图() {
		BufferedImage image = getSeekImage();
		Rectangle rectangle = getSeekRect();
		if (!rectangle.isEmpty()) {
			return tool.区域找图EX((int) rectangle.getX(), (int) rectangle.getY(), (int) (rectangle.getX() + rectangle.getWidth()),
					(int) (rectangle.getY() + rectangle.getHeight()), 0.07, image);
		}
		return null;
	}

	private void Run() {
		long yuan = System.currentTimeMillis();
		long wait = 0;
		if (isWhileRun()) {
			wait = getIntervalTime();
		}
		while (script_bool) {
			Point point = 区域中查找对应图();
			if (point != null) {
				point.x += getSeekImage().getWidth() / 2;
				point.y += getSeekImage().getHeight() / 2;
				tool.mouseMove(point.x, point.y);
				for (int i = 0; i < getMouseNum(); i++) {
					tool.mousePressOne(getMouseCode());
				}
			}
			if (wait == 0) {
				scriptThread.Stop();
			}
			while (System.currentTimeMillis() < yuan + wait) {
				Other.sleep(200);
			}
			yuan = System.currentTimeMillis();
		}
	}
}
