package com.mugui.ui.part;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mugui.Dui.DTextField;
import com.mugui.http.Bean.JGOtherBean.JGBean;
import com.mugui.tool.Other;

class JGOtherView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4593958887296071844L;

	public JGOtherView(JGOtherPanel jgOtherPanel) {
		this("加工", jgOtherPanel);
	}

	public JGOtherView(String model, JGOtherPanel jgOtherPanel) {
		this(model, null, jgOtherPanel);
	}

	public JGOtherView(JGBean jgBean, JGOtherPanel jgOtherPanel) {
		this(jgBean.getModel(), jgBean, jgOtherPanel);
	}

	private JGBean bean = null;

	public JGBean getJGBean() {
		return bean;
	}

	private JGOtherPanel panel = null;

	/**
	 * @wbp.parser.constructor
	 */
	public JGOtherView(String model, JGBean jgBean, JGOtherPanel jgOtherPanel) {
		bean = jgBean;
		panel = jgOtherPanel;
		FlowLayout flowLayout_1 = (FlowLayout) getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		label_3 = new JLabel(model + "物：");
		label_3.addMouseListener(mouseListener);
		JGOtherView.this.addMouseListener(mouseListener);
		add(label_3);
		final DTextField textField = new DTextField(2);
		textField.setColumns(2);
		add(textField);
		JLabel label_4 = new JLabel("行");
		add(label_4);
		final DTextField textField_1 = new DTextField(2);
		textField_1.setColumns(2);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (Other.isInteger(textField.getText().trim()))
					bean.setRow(Integer.parseInt(textField.getText().trim()));
				else
					bean.setRow(0);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (Other.isInteger(textField.getText().trim()))
					bean.setRow(Integer.parseInt(textField.getText().trim()));
				else
					bean.setRow(0);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (Other.isInteger(textField.getText().trim()))
					bean.setRow(Integer.parseInt(textField.getText().trim()));
				else
					bean.setRow(0);
			}
		});
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				if (Other.isInteger(textField_1.getText().trim()))
					bean.setColumn(Integer.parseInt(textField_1.getText().trim()));
				else
					bean.setColumn(0);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (Other.isInteger(textField_1.getText().trim()))
					bean.setColumn(Integer.parseInt(textField_1.getText().trim()));
				else
					bean.setColumn(0);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (Other.isInteger(textField_1.getText().trim()))
					bean.setColumn(Integer.parseInt(textField_1.getText().trim()));
				else
					bean.setColumn(0);
			}
		});
		add(textField_1);
		JLabel label_5 = new JLabel("列");
		add(label_5);
		if (bean != null) {
			textField.setText(jgBean.getRow() + "");
			textField_1.setText(jgBean.getColumn() + "");
		} else {
			bean = new JGBean();
			bean.setModel(model);
		}
		if (bean.getModel().equals(JGList.LJ)) {
			JLabel label = new JLabel("单");
			add(label);
			final DTextField textField_2 = new DTextField(3);
			textField_2.setColumns(3);
			textField_2.setText(bean.getN() + "");
			add(textField_2);
			JLabel label_1 = new JLabel("总");
			add(label_1);
			final DTextField textField_3 = new DTextField(6);
			textField_3.setColumns(6);
			textField_3.setText(bean.getNum() + "");
			add(textField_3);

			textField_2.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					if (Other.isInteger(textField_2.getText().trim()))
						bean.setN(Integer.parseInt(textField_2.getText().trim()));
					else
						bean.setN(0);
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					if (Other.isInteger(textField_2.getText().trim()))
						bean.setN(Integer.parseInt(textField_2.getText().trim()));
					else
						bean.setN(0);
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					if (Other.isInteger(textField_2.getText().trim()))
						bean.setN(Integer.parseInt(textField_2.getText().trim()));
					else
						bean.setN(0);
				}
			});
			textField_3.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent e) {
					if (Other.isInteger(textField_3.getText().trim()))
						bean.setNum(Integer.parseInt(textField_3.getText().trim()));
					else
						bean.setNum(0);
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					if (Other.isInteger(textField_3.getText().trim()))
						bean.setNum(Integer.parseInt(textField_3.getText().trim()));
					else
						bean.setNum(0);
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					if (Other.isInteger(textField_3.getText().trim()))
						bean.setNum(Integer.parseInt(textField_3.getText().trim()));
					else
						bean.setNum(0);
				}
			});

		}
		this.setBackground(null);

	}

	private JLabel label_3 = null;
	public MouseListener mouseListener = new MouseListener() {
		private Color color = null;

		@Override
		public void mouseReleased(MouseEvent e) {
			if (panel.now_view != null) {
				panel.now_view.setBackground(color);
			}
			panel.now_view = JGOtherView.this;
			color = JGOtherView.this.getBackground();
			JGOtherView.this.setBackground(Color.LIGHT_GRAY);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO 自动生成的方法存根

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
			// TODO 自动生成的方法存根

		}
	};
}