package com.mugui.script.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.Charset;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DScrollBar;
import com.mugui.Dui.DTextArea;
import com.mugui.script.DataSave;
import com.mugui.script.FunctionBean;
import com.mugui.script.ScriptBean;
import com.mugui.script.other.CsGoScript;

public class EditPanel extends DPanel {
	public EditPanel() {
		this.setLayout(new BorderLayout(0, 0));

		JPanel panel_5 = new JPanel();
		this.add(panel_5, BorderLayout.NORTH);

		final DButton csgoButton = new DButton("开启连跳", (Color) null);
		csgoButton.addActionListener(CsGoScript::handle);
		panel_5.add(csgoButton);

		final DButton button_1 = new DButton("源码", (Color) null);

		panel_5.add(button_1);

		final DButton button = new DButton("视图", (Color) null);
		panel_5.add(button);

		DButton button_2 = new DButton("保存", (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scriptBean.saveToFile();
			}
		});
		panel_5.add(button_2);
		cardManger = new JPanel();
		cl_cardManger = new CardLayout();
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((e.getSource()) == button) {
					editpanel.init(scriptBean);
					cl_cardManger.show(cardManger, "view");
				} else if (e.getSource() == button_1) {
					sourcetext.setText(new String(scriptBean.body, Charset.forName("UTF-8")));
					cl_cardManger.show(cardManger, "source");
				}
			}
		};
		button_1.addActionListener(actionListener);
		button.addActionListener(actionListener);

		this.add(cardManger, BorderLayout.CENTER);
		cardManger.setLayout(cl_cardManger);

		JPanel sourePanel = new JPanel();
		sourePanel.setLayout(new BorderLayout(0, 0));

		sourcetext = new DTextArea();
		sourcetext.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				scriptBean.body = sourcetext.getText().getBytes();
			}
		});

		JScrollPane scrollPane = new JScrollPane(sourcetext);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBar(new DScrollBar());
		sourePanel.add(scrollPane, BorderLayout.CENTER);

		JPanel viewPanel = new JPanel();
		cardManger.add(viewPanel, "view");
		cardManger.add(sourePanel, "source");

		editpanel = new EditViewPanel();

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBar(new DScrollBar());
		scrollPane_1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		functionTree = new FunctionTree();
		scrollPane_1.setViewportView(functionTree);
		final JLabel lblNewLabel = new JLabel();
		functionTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
				if (node.isRoot())
					return;
				FunctionBean bean = (FunctionBean) node.getUserObject();
				String string = bean.explanation;
				string = string.replaceAll("\\[name\\]", bean.name);
				string = string.replaceAll("\\[function\\]", bean.function);
				string = string.replaceAll("\\[class_name\\]", bean.class_name);
				lblNewLabel.setText("<html>" + string + "</html>");
			}
		});
		JScrollPane scrollPane_2 = new JScrollPane();
		functionTree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editpanel.add(functionTree.now_functionbean);
				scrollPane_2.validate();
				scrollPane_2.repaint();
			}
		});
		

		
		
		JPanel panel_1 = new JPanel();
		GroupLayout gl_viewPanel = new GroupLayout(viewPanel);
		gl_viewPanel.setHorizontalGroup(
			gl_viewPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_viewPanel.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 213, GroupLayout.PREFERRED_SIZE))
		);
		gl_viewPanel.setVerticalGroup(
			gl_viewPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
				.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
		);
		scrollPane_2.setViewportView(editpanel);
//		editpanel.add(scrollPane_2);
		panel_1.setLayout(new BorderLayout(0, 0));

		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		panel_1.add(lblNewLabel, BorderLayout.CENTER);
		viewPanel.setLayout(gl_viewPanel);
		father = (ScriptPanel) DataSave.uiManager.get("ScriptPanel");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9168865923452677484L;
	private ScriptPanel father = null;
	private JPanel cardManger;
	private CardLayout cl_cardManger = null;
	private ScriptBean scriptBean = null;
	private FunctionTree functionTree = null;
	private EditViewPanel editpanel = null;
	private DTextArea sourcetext;
	private JScrollPane scrollPane_1;

	@Override
	public void init() {
		father.add(this, BorderLayout.CENTER);
		father.validate();
		cl_cardManger.show(cardManger, "view");
		editpanel.init(scriptBean);
		
	}

	@Override
	public void quit() {
		father.remove(this);
		father.validate();
	}

	@Override
	public void dataInit() {
	}

	@Override
	public void dataSave() {
	}

	public void setScriptBean(ScriptBean bean) {
		scriptBean = bean;
	}
}
