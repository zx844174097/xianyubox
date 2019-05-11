package com.mugui.Dui;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DScrollBar extends JScrollBar {
	public DScrollBar() {
		super();
		BasicScrollBarUI barUI = new BasicScrollBarUI() {

			public Dimension getPreferredSize(JComponent c) {
				return new Dimension(16, 16);
			}

			// 重绘滚动条的滑块
//			public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
//				super.paintThumb(g, c, thumbBounds);
//				// 重定图形上下文的原点，这句一定要写，不然会出现拖动滑块时滑块不动的现象
//				g.translate(thumbBounds.x, thumbBounds.y);
//			}

			// 重绘滑块的滑动区域背景
			public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {

			}

			// 重绘当鼠标点击滑动到向上或向左按钮之间的区域
			protected void paintDecreaseHighlight(Graphics g) {
				g.setColor(Color.black);
				int x = this.getTrackBounds().x;
				int y = this.getTrackBounds().y;
				int w = 0, h = 0;
				if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
					w = this.getThumbBounds().width;
					h = this.getThumbBounds().y - y;

				}
				if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
					w = this.getThumbBounds().x - x;
					h = this.getThumbBounds().height;
				}
				g.fillRect(x, y, w, h);
			}

			// 重绘当鼠标点击滑块至向下或向右按钮之间的区域
			protected void paintIncreaseHighlight(Graphics g) {
				g.setColor(Color.black);
				int x = this.getThumbBounds().x;
				int y = this.getThumbBounds().y;
				int w = this.getTrackBounds().width;
				int h = this.getTrackBounds().height;
				g.fillRect(x, y, w, h);
			}

			protected JButton createIncreaseButton(int orientation) {
				return new BasicArrowButton(orientation) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					// 重绘按钮的三角标记
					public void paintTriangle(Graphics g, int x, int y, int size, int direction, boolean isEnabled) {
						Graphics2D g2 = (Graphics2D) g;

						String string = null;
						switch (this.getDirection()) {
						case BasicArrowButton.EAST:
							string = "△";
							break;

						case BasicArrowButton.SOUTH:
							string = "▽";
							break;
						}
						g2.setFont(new Font("微软雅黑", Font.BOLD, 25));
						g2.drawString(string, 0, getHeight());
					}
				};
			}

			protected JButton createDecreaseButton(int orientation) {
				return new BasicArrowButton(orientation) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public void paintTriangle(Graphics g, int x, int y, int size, int direction, boolean isEnabled) {
						Graphics2D g2 = (Graphics2D) g;

						String string = null;
						switch (this.getDirection()) {
						case BasicArrowButton.NORTH:
							string = "△";
							break;

						case BasicArrowButton.WEST:
							string = "▽";
							break;
						}
						g2.setFont(new Font("微软雅黑", Font.BOLD, 25));
						g2.drawString(string, 0, getHeight());
					}
				};
			}
		};
		setUI(barUI);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033739336354902471L;

}
