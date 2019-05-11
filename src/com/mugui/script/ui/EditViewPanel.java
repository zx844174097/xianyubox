package com.mugui.script.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DTextField;
import com.mugui.Dui.DTextField.CodeListener;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.script.FunctionBean;
import com.mugui.script.FunctionBean.FunctionParameter;
import com.mugui.script.ScriptBean;
import com.mugui.tool.UiTool;

public class EditViewPanel extends JPanel {
	public EditViewPanel() {
		setLayout(new DVerticalFlowLayout());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2577627632091559490L;
	private ScriptBean bean = null;

	public void init(ScriptBean scriptBean) {

		this.bean = scriptBean;
		removeAll();
		if (scriptBean.body == null) {
			return;
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(scriptBean.body)));
		String string;
		try {
			while ((string = reader.readLine()) != null) {
				if (!string.trim().equals("")) {
					add(string);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public String toString() {
		// 翻译成源码输出
		String body = "";
		for (Component panel : getComponents()) {
			if (panel instanceof BasicEditViewPanel) {
				body += panel.toString();
			}
		}
		return body;
	}

	public void add(FunctionBean functionbean) {
		if (functionbean == null)
			return;
		BasicEditViewPanel viewPanel = new BasicEditViewPanel(this, functionbean);
		this.add(viewPanel);
		UiTool.全体透明(this);
		validate();
		repaint();
	}

	public void add(String body) {
		if (body == null)
			return;
		BasicEditViewPanel viewPanel = new BasicEditViewPanel(this, body);
		this.add(viewPanel);
		UiTool.全体透明(this);
		validate();
		repaint();
	}

	private class BasicEditViewPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1799868449872263472L;
		private FunctionBean functionBean = null;

		@Override
		public String toString() {
			String function = functionBean.name;
			for (Component component : getComponents()) {
				if (component instanceof DTextField) {
					function += " " + ((DTextField) component).getText();
				}
			}
			return function + "\n";
		}

		public BasicEditViewPanel(EditViewPanel editViewPanel, FunctionBean bean) {
			viewInit(editViewPanel);
			init(bean);
		}

		public BasicEditViewPanel(EditViewPanel editViewPanel, String body) {
			this(editViewPanel, FunctionBean.createFunctionBean(body.split(" ")[0]));
			String yuan[] = body.split(" ");
			String src[] = new String[yuan.length - 1];
			System.arraycopy(yuan, 1, src, 0, src.length);
			initParameter(src);
		}

		private void initParameter(String[] split) {
			int i = 0;
			for (Component component : getComponents()) {
				if (component instanceof DTextField) {
					if (split.length <= i)
						return;
					((DTextField) component).setText(split[i++].trim());

				}
			}
		}

		private void viewInit(final EditViewPanel editViewPanel) {
			FlowLayout flowLayout = (FlowLayout) getLayout();
			flowLayout.setVgap(3);
			flowLayout.setAlignment(FlowLayout.LEFT);
			DButton button = new DButton("删除", null);
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editViewPanel.remove(BasicEditViewPanel.this);
					editViewPanel.validate();
				}
			});
			button.setFont(new Font("宋体", Font.PLAIN, 12));
			add(button);
		}

		private CodeListener codeListener = new CodeListener() {
			@Override
			public void callBack(DTextField dTextField) {
				EditViewPanel.this.bean.body = EditViewPanel.this.toString().getBytes();
			}
		};

		private void init(FunctionBean bean) {
			this.functionBean = bean;
			JLabel label = new JLabel(bean.name);
			add(label);
			if (bean.parameters == null)
				return;
			for (int i = 0; i < bean.parameters.length; i++) {
				label = new JLabel("参数" + (i + 1) + ":");
				add(label);

				switch (bean.parameters[i]) {
				case FunctionParameter.INT:
					DTextField field = new DTextField(10);
					field.setColumns(3);
					field.setOption(DTextField.OPTION_INT);
					field.addCodeListener(codeListener);
					add(field);
					break;
				case FunctionParameter.KEY_CODE:
					field = new DTextField(10);
					field.setColumns(3);
					field.setOption(DTextField.OPTION_KEY_LISTENER);
					field.addKeyCodeListener(codeListener);
					add(field);
					break;
				case FunctionParameter.MOUSE_BUTTON:
					field = new DTextField(10);
					field.setColumns(3);
					field.setOption(DTextField.OPTION_MOUSE_LISTENER);
					field.addMouseCodeListener(codeListener);
					add(field);
					break;
				case FunctionParameter.STRING:
					field = new DTextField(32);
					field.setColumns(6);
					add(field);
					break;
				default:
					break;
				}
			}
		}
	}
}
