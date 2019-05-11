package com.mugui.ui.part;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DPanel;
import com.mugui.model.EWHandle;
import com.mugui.ui.part.CJ.HS_MAP;
import com.mugui.ui.part.EWtoDirectionCheckBox.DPoint2D;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

public class EWtoDirectionContainerPanel extends DPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -36131167191306749L;
	private JPanel container;

	private abstract interface ActionAdapter extends Serializable, ActionListener {

		public abstract void actionPerformed(ActionEvent e);

	}

//	private abstract interface DMouseAdapter extends Serializable, MouseListener {
//	}

	public EWtoDirectionContainerPanel() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		add(panel, BorderLayout.NORTH);

		button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionAdapter() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5338674114818340741L;

			public void actionPerformed(ActionEvent e) {
				if(button==null) {
					button=(DButton) e.getSource();
				}
				if (button.getText().equals("关闭小地图方向辅助")) {
					button.setText("开启小地图方向辅助");
					EWHandle.stop();
				} else {
					button.setText("关闭小地图方向辅助");
					HS_MAP map = GameListenerThread.DJNI.getHsMap();
					if (map == null || (map.x == 0 && map.y == 0 && map.z == 0)) {
						JOptionPane.showMessageDialog(EWtoDirectionContainerPanel.this, "请前往采集，开启监听功能，并正常使用监听功能", "提示", JOptionPane.OK_OPTION);
						return;
					}
					EWHandle.start(EWHandle.EW_KEEP_DIRECTION);
				}

			}
		});
		panel.add(button);
		button.setText("开启小地图方向辅助");

		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.addActionListener(new ActionAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2393494624353300961L;

			public void actionPerformed(ActionEvent e) {
				String string = JOptionPane.showInputDialog(EWtoDirectionContainerPanel.this, "请填写点名称：", "请输入", JOptionPane.OK_CANCEL_OPTION);
				if (string == null)
					return;
				if (GameListenerThread.DJNI == null) {
					JOptionPane.showMessageDialog(EWtoDirectionContainerPanel.this, "dll库未载入，请联系管理员:QQ:844174097", "错误", JOptionPane.OK_OPTION);
					return;
				}
				HS_MAP map = GameListenerThread.DJNI.getHsMap();
				if (map == null || (map.x == 0 && map.y == 0 && map.z == 0)) {
					JOptionPane.showMessageDialog(EWtoDirectionContainerPanel.this, "请前往采集，开启监听功能，并正常使用监听功能", "提示", JOptionPane.OK_OPTION);
					return;
				}

				EWtoDirectionCheckBox checkBox = new EWtoDirectionCheckBox(string, new DPoint2D(map.x, map.y));
				addDirectionCheckBox(checkBox);
			}
		});
		panel.add(button_3);
		button_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_3.setText("新增");

		DButton button_4 = new DButton((String) null, (Color) null);
		button_4.addActionListener(new ActionAdapter() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -5869770993111992925L;

			public void actionPerformed(ActionEvent e) {
				if (checkBox != null)
					removeDirectionCheckBox(checkBox);
			}
		});
		panel.add(button_4);
		button_4.setText("删除");
		button_4.setFont(new Font("Dialog", Font.PLAIN, 12));

		rdbtnt = new JRadioButton("自动T");
		panel.add(rdbtnt);

		container = new JPanel();
		container.setLayout(new GridLayout(0, 3, 0, 0));
		add(container);
		addDirectionCheckBox(new EWtoDirectionCheckBox("沙漠贸易BUFF", new DPoint2D(892687.5f, 60647.33f)));

	}

	private void addDirectionCheckBox(EWtoDirectionCheckBox checkBox) {
		container.add(checkBox);
		checkBox.addActionListener(mouseListener);
		updateUI();
		repaint();
	}

	private void removeDirectionCheckBox(EWtoDirectionCheckBox checkBox) {
		checkBox.removeActionListener(mouseListener);
		container.remove(checkBox);
		this.checkBox = null;
		updateUI();
		repaint();
	}



	private EWtoDirectionCheckBox checkBox = null;
	private ActionAdapter mouseListener = new ActionAdapter() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 7389071349127809755L;

		@Override
		public void actionPerformed(ActionEvent e) {
			checkBox = (EWtoDirectionCheckBox) e.getSource();
			if (!checkBox.isSelected()) {
				checkBox = null;
			}
			for (Component component : container.getComponents()) {
				if (component instanceof EWtoDirectionCheckBox) {
					if (component != checkBox) {
						((EWtoDirectionCheckBox) component).setSelected(false);
					}
				}
			}

		}

	};
	private JRadioButton rdbtnt;
	private DButton button;

	public EWtoDirectionCheckBox getCheckBox() {

		return checkBox;
	}

	@Override
	public void init() {
		for (Component component : container.getComponents()) {
			if (component instanceof EWtoDirectionCheckBox) {
				if (((EWtoDirectionCheckBox) component).isSelected()) {
					checkBox = (EWtoDirectionCheckBox) component;
				} else {
					((EWtoDirectionCheckBox) component).setSelected(false);
				}
			}
		}
	}

	@Override
	public void quit() {
		button.setText("开启小地图方向辅助"); 
	}

	public boolean isDirectionT() { 
		return rdbtnt.isSelected();
	}

	public void setDirectionT(boolean b) {
		rdbtnt.setSelected(b);
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
