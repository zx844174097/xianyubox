package com.mugui.ui.part;

import java.io.Serializable;

import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

public class EWtoDirectionCheckBox extends JCheckBox {

	public static class DPoint2D implements Serializable {
		public float x;
		public float y;

		public DPoint2D() {
			this(0, 0);
		}

		public DPoint2D(float x, float y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = -2975574815971261037L;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2455165212715925848L;
	private DPoint2D coordinate = null;

	/**
	 * @wbp.parser.constructor
	 */
	public EWtoDirectionCheckBox(DPoint2D coordinate) {
		this("", coordinate);
	}

	public EWtoDirectionCheckBox(String name, DPoint2D point2d) {
		setText(name);
		this.coordinate = point2d;
		setHorizontalAlignment(SwingConstants.CENTER);
		setBackground(null);
		setOpaque(false);
	}

	public DPoint2D getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(DPoint2D coordinate) {
		this.coordinate = coordinate;
	}

}
