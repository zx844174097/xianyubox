package com.mugui.Dui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import java.awt.FlowLayout;

public class DInputDialog extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7248853081206644430L;
	public static final int RET_YES = 0;
	public static final int RET_NO = 1;

	/**
	 * @wbp.parser.constructor
	 */
	public DInputDialog(String title, String message, boolean trueButton, boolean falseButton) {
		this(title, message, trueButton, falseButton, false);
	}

	public DInputDialog(String title, String message, boolean trueButton, boolean falseButton, boolean infoButton) {
		this.title = title;
		this.message = message;
		init(trueButton, falseButton, infoButton);
	}

	private DButton trueB = null;
	private DButton falseB = null;
	private DButton infoB = null;
	private DTextField inputbox = null;

	public void setTrueText(String s) {
		if (trueB != null)
			trueB.setText(s);
	}

	public void setfalseText(String s) {
		if (falseB != null)
			falseB.setText(s);
	}

	public void setinfoText(String s) {
		if (infoB != null)
			infoB.setText(s);
	}

	public void setBText(String trueB, String falseB, String infoB) {
		setTrueText(trueB);
		setfalseText(falseB);
		setinfoText(infoB);
	}

	public void init(boolean trueButton, boolean falseButton, boolean infoButton) {
		removeAll();
		setLayout(new BorderLayout(0, 0));
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setFont(new Font("微软雅黑", Font.BOLD, 20));
		editorPane.setBackground(Color.LIGHT_GRAY);
		editorPane.setText(title);
		add(editorPane, BorderLayout.NORTH);

		JEditorPane editorPane_1 = new JEditorPane();
		editorPane_1.setEditable(false);
		editorPane_1.setFont(new Font("宋体", Font.PLAIN, 14));
		editorPane_1.setBackground(Color.WHITE);
		editorPane_1.setText(message);
		add(editorPane_1, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new DVerticalFlowLayout());

		panel_2 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_2.getLayout();
		flowLayout_1.setVgap(0);
		panel_1.add(panel_2);

		JPanel panel = new JPanel();
		// FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		panel_1.add(panel);
		if (trueButton) {
			trueB = new DButton((String) null, Color.white);
			trueB.setFont(new Font("Dialog", Font.BOLD, 20));
			trueB.setText("确认");
			trueB.setActionCommand(0 + "");
			panel.add(trueB);
			trueB.addActionListener(al);
		}
		if (falseButton) {
			falseB = new DButton((String) null, Color.white);
			falseB.setFont(new Font("Dialog", Font.BOLD, 20));
			falseB.setText("取消");
			falseB.setActionCommand(1 + "");
			panel.add(falseB);
			falseB.addActionListener(al);
		}
		if (infoButton) {
			infoB = new DButton((String) null, Color.white);
			infoB.setFont(new Font("Dialog", Font.BOLD, 20));
			infoB.setText("信息");
			infoB.setActionCommand(2 + "");
			panel.add(infoB);
			infoB.addActionListener(al);
		}
		validate();
		repaint();
	}

	private Object lock = new Object();

	public int start() {
		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return zhi;
	}

	private int zhi = -1;
	private ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("0")) {
				zhi = 0;
			} else if (e.getActionCommand().equals("1")) {
				zhi = 1;
			} else if (e.getActionCommand().equals("2")) {
				zhi = 2;
			}
			synchronized (lock) {
				if (lock instanceof Window) {
					((Window) lock).dispose();
				} else
					lock.notifyAll();
			}
		}
	};

	public void setLock(Object lock) {
		this.lock = lock;
	}

	private String title = null;
	private String message = null;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void stop() {
		zhi = -2;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	private JPanel panel_2 = null;

	public void openInputBox() {
		inputbox = new DTextField(12);
		inputbox.setFont(new Font("宋体", Font.BOLD, 14));
		panel_2.add(inputbox);
		inputbox.setColumns(12);
		updateUI();
	}

	public Object getInputBoxText() {
		if (inputbox == null) {
			return zhi;
		} else
			return inputbox.getText().trim();
	}
}
