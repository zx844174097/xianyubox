package com.mugui.Dui;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JEditorPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.mugui.Dui.DButton;

public class InfoBody extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7248853081206641630L;

	public InfoBody(String title, String message, boolean trueButton, boolean falseButton) {
		this.title = title;
		this.message = message;
		init(trueButton, falseButton);
	}

	private void init(boolean trueButton, boolean falseButton) {
		setLayout(new BorderLayout(0, 0));
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		editorPane.setFont(new Font("宋体", Font.PLAIN, 25));
		editorPane.setBackground(Color.LIGHT_GRAY);
		editorPane.setText(title);
		add(editorPane, BorderLayout.NORTH);

		JEditorPane editorPane_1 = new JEditorPane();
		editorPane_1.setEditable(false);
		editorPane_1.setFont(new Font("宋体", Font.PLAIN, 20));
		editorPane_1.setBackground(Color.WHITE);
		editorPane_1.setText(message);
		add(editorPane_1, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);
		if (trueButton) {
			DButton button = new DButton((String) null, (Color) null);
			button.setFont(new Font("Dialog", Font.BOLD, 25));
			button.setText("确定");
			panel.add(button);
			button.addActionListener(al);
		}
		if (falseButton) {
			DButton button_1 = new DButton((String) null, (Color) null);
			button_1.setFont(new Font("Dialog", Font.BOLD, 25));
			button_1.setText("取消");
			panel.add(button_1);
			button_1.addActionListener(al);
		}
	}

	private ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			synchronized (InfoBody.this) {
				if (e.getActionCommand().equals("确定")) {
					state = 0;
				} else if (e.getActionCommand().equals("取消")) {
					state = 1;
				}
				InfoBody.this.notifyAll();
			}

		}
	};
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

	private int state = -1;

	public int getState() {
		try {
			synchronized (this) {
				while (state == -1) {
					this.wait();
				}
				return state;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
