package com.mugui.script.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.*;

import com.mugui.script.ScriptThread;
import org.eclipse.swt.internal.win32.OS;

import com.mugui.MAIN;
import com.mugui.Dui.DButton;
import com.mugui.Dui.DOptionPanel;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DScrollBar;
import com.mugui.Dui.DTextField;
import com.mugui.Dui.DTextField.CodeListener;
import com.mugui.script.DataSave;
import com.mugui.script.ScriptBean;
import com.mugui.tool.Other;
import com.mugui.tool.UiTool;
import com.mugui.windows.MouseListenerTool;
import com.mugui.windows.MouseListenerTool.KeyListener;
import com.mugui.windows.Tool;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.Dui.TimeInfo;

import java.awt.Color;

public class ListPanel extends DPanel {
	public ListPanel() {
		this.setLayout(new BorderLayout(0, 0));
		JPanel panel_1 = new JPanel();
		this.add(panel_1, BorderLayout.NORTH);
		JLabel label = new JLabel("宏列表");
		label.setFont(new Font("宋体", Font.BOLD, 14));
		panel_1.add(label);

		DButton button = new DButton("新增", (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String string = DOptionPanel.showInputDialog(DataSave.StaticUI, "请输入宏名称", "提示", DOptionPanel.OPTION_OK_CANCEL);
				if (string == null) {
					return;
				}
				if (string.trim().equals(""))
					new TimeInfo("消息", "请输入宏名称", 1000).run();
				ScriptBean bean = new ScriptBean(string, Other.getUUID(), "F10");
				bean.file = new File(save_file.getPath() + "\\" + bean.code + ".muguiScript");
				bean.saveToFile();
				addMacroBean(bean);
			}

		});
		button.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_1.add(button);

		JPanel panel = new JPanel();
		add(panel, BorderLayout.SOUTH);

		JLabel lblf = new JLabel("宏出现互相干扰时，导致电脑无法控制时，按下f12进行强停");
		lblf.setForeground(Color.RED);
		lblf.setFont(new Font("宋体", Font.BOLD, 12));
		panel.add(lblf);
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBar(new DScrollBar());
		scrollPane.setBackground(null);
		body_list = new JPanel() {
			/**
			 *  
			 */
			private static final long serialVersionUID = 7212539105941007048L;

			@Override
			public Component add(Component comp) {
				comp.addMouseListener(list_mouseListener);
				return super.add(comp);
			}
		};
		body_list.setLayout(new DVerticalFlowLayout());
		body_list.setBackground(null);
		scrollPane.setViewportView(body_list);
		father = (ScriptPanel) DataSave.uiManager.get("ScriptPanel");
	}

	private JScrollPane scrollPane = null;

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		list_mouseListener = (MouseAdapter) l;
	}

	private MouseAdapter list_mouseListener = null;
	private JPanel body_list = null;
	private ScriptPanel father = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = -3443013945527631671L;

	@Override
	public void init() {
		if (body_list.getComponentCount() == 0)
			dataInit();
		MouseListenerTool.newInstance().addKeyListener(new KeyListener(this, OS.MapVirtualKey(Tool.getkeyCode("F12"), 0)) {
			@Override
			public void callback(Object object) {
				for (Component component : body_list.getComponents()) {
					if (component instanceof MacroBeanThumbnailPanel) {
						MacroBeanThumbnailPanel panel = (MacroBeanThumbnailPanel) component;
						MouseListenerTool.newInstance().removeKeyListeners(panel.bean.keyListener);
					}
				}
				ScriptBean.stopAll();
			}
		});

	}

	@Override
	public void quit() {
		MouseListenerTool.newInstance().removeKeyListener(this, OS.MapVirtualKey(Tool.getkeyCode("F12"), 0));
	}

	File save_file = new File(Tool.getRootPath() + "\\按键宏\\");

	@Override
	public void dataInit() {
		if (!save_file.isDirectory())
			return;
		for (File file : save_file.listFiles()) {
			ScriptBean bean = ScriptBean.newInstance(file);
			if (bean == null)
				continue;
			addMacroBean(bean);
		}
	}

	private void addMacroBean(ScriptBean bean) {
		if (body_list.getComponentCount() == 0) {
			this.add(scrollPane, BorderLayout.CENTER);
			body_list.add(new MacroBeanThumbnailPanel(bean));
			UiTool.全体透明(this);
		} else {
			body_list.add(new MacroBeanThumbnailPanel(bean));
		}
		updateUI();
	}

	@Override
	public void dataSave() {

	}

	public class MacroBeanThumbnailPanel extends JPanel {
		/**
		 * 
		 */
		private ScriptBean bean = null;
		private static final long serialVersionUID = -3658907130486007469L;

		public MacroBeanThumbnailPanel(ScriptBean bean) {
			this.bean = bean;
			setBackground(null);
			final JCheckBox checkBox = new JCheckBox(bean.name);
			checkBox.setFont(new Font("微软雅黑", Font.PLAIN, 11));
			checkBox.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (checkBox.isSelected()) {
						MouseListenerTool.newInstance().addKeyListener(MacroBeanThumbnailPanel.this.bean.keyListener);
					} else {
						MouseListenerTool.newInstance().removeKeyListeners(MacroBeanThumbnailPanel.this.bean.keyListener);
					}
				}
			});
			checkBox.setBackground(null);
			checkBox.setPreferredSize(new Dimension(100, 15));
			setLayout(new FlowLayout());
			add(checkBox);
			JRadioButton longPressToStart = new JRadioButton("长按启动");
			longPressToStart.addActionListener(e->{
				if (longPressToStart.isSelected()) {
					this.bean.startupType=ScriptBean.KEY_KEEP_START;
				} else {
					this.bean.startupType=ScriptBean.KEY_START_SHUTDOWN;
				}
			});

			longPressToStart.setFont(new Font("微软雅黑", Font.PLAIN, 12));
			add(longPressToStart);

			JLabel label = new JLabel("热键：");
			label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
			add(label);
			DTextField field = new DTextField(10);
			field.setBackground(null);
			field.setColumns(3);
			field.setText(bean.hot_key);
			field.addKeyCodeListener(new CodeListener() {
				@Override
				public void callBack(DTextField dTextField) {
					if (!dTextField.getText().equals(MacroBeanThumbnailPanel.this.bean.hot_key)) {
						MouseListenerTool.newInstance().removeKeyListeners(MacroBeanThumbnailPanel.this.bean.keyListener);
						MacroBeanThumbnailPanel.this.bean.setHotKey(dTextField.getText());
						if (checkBox.isSelected())
							MouseListenerTool.newInstance().addKeyListener(MacroBeanThumbnailPanel.this.bean.keyListener);
						MacroBeanThumbnailPanel.this.bean.saveToFile();
					}
				}
			});
			add(field);


			DButton updata = new DButton("编辑", null);
			updata.setFont(new Font("Dialog", Font.PLAIN, 12));
			updata.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					((EditPanel) father.GetCenter()).setScriptBean(MacroBeanThumbnailPanel.this.bean);
					father.GetCenter().init();
					UiTool.全体透明(father);
				}
			});
			add(updata);
			DButton delete = new DButton("删除", null);
			delete.setFont(new Font("Dialog", Font.PLAIN, 12));
			add(delete);
			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int i = DOptionPanel.showMessageDialog(ListPanel.this, "确认删除此脚本", "警告", DOptionPanel.OPTION_OK_CANCEL);
					if (i == DOptionPanel.AGREE) {
						MouseListenerTool.newInstance().removeKeyListeners(MacroBeanThumbnailPanel.this.bean.keyListener);
						ListPanel.this.remove(MacroBeanThumbnailPanel.this);
					}
				}
			});
		}
	}

	public void remove(MacroBeanThumbnailPanel comp) {
		comp.bean.removeFile();
		father.GetCenter().quit();
		body_list.remove(comp);
		if (body_list.getComponentCount() == 0) {
			super.remove(scrollPane);
		}
	}

}
