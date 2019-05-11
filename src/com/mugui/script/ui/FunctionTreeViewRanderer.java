package com.mugui.script.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

import java.awt.BasicStroke;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.FlowLayout;

public class FunctionTreeViewRanderer extends JPanel implements TreeCellRenderer {
	private float[] dash1 = { 5.0f };
	private BasicStroke s = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash1, 0.0f);
	public final static Color color = new Color(0, 153, 255);
	private Rectangle2D mfRect = new Rectangle2D.Float();

	public FunctionTreeViewRanderer() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		label = new JLabel("函数名称") {

			/**
			 * 
			 */
			private static final long serialVersionUID = -2168569359677700531L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				if (!hasFocus)
					return;
				Graphics2D g2d = (Graphics2D) g;
				// 设置边框颜色
				g2d.setColor(color);
				// 设置边框范围
				mfRect.setRect(0, 0, getWidth() - 1, getHeight() - 1);
				// 设置边框类型
				g2d.setStroke(s);
				g2d.draw(mfRect);
			}
		};
		label.setForeground(color);
		label.setFont(new Font("宋体", Font.PLAIN, 12));
		add(label);
		setBackground(null);
		setOpaque(false);
		label.setBackground(null);
	}

	private JLabel label = null;
	private boolean hasFocus = false;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1046088173126922605L;

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		// System.out.println(value);
		label.setText(value.toString());
		if (leaf) {
			label.setFont(new Font("宋体", Font.BOLD, 12));
			this.hasFocus = hasFocus;
		} else {
			label.setFont(new Font("宋体", Font.PLAIN, 12));
			this.hasFocus = false;
		}
		return this;
	}

}
