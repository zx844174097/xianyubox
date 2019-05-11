package com.mugui.windows;

import java.awt.Color;
import java.awt.Component;
import java.util.LinkedList;
import javax.swing.JComponent;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

public class 呼吸背景 extends Thread {
	private LinkedList<Component> panel = new LinkedList<Component>();
	private boolean isTrue = false;
	private int model = 0;
	private LinkedList<Color> yuan = new LinkedList<Color>();

	public 呼吸背景(Component panel) {
		this(panel, 0);
	}

	public 呼吸背景(Component panel, int model) {
		this.panel.add(panel);
		yuan.add(panel.getBackground());
		this.model = model;
	}

	@Override
	public void run() {
		isTrue = true;
		while (isTrue&&!DataSave.低配模式) {
			float h = 0;
			float s = 0;
			float l = model == 0 ? 0.4f : 0.95f;
			s = (model == 0 ? 0.3f : 0.85f);

			for (s = (float) (s + Math.random() * 0.05); s <= (model == 0 ? 0.35 : 0.90) && isTrue; s += 0.01) {
				for (h = (float) (1 - Math.random()); h >= 0 && isTrue; h -= 0.001) {
					Color temp = hsl转rgb(h, s, l);
					for (int i = 0; i < panel.size(); i++) {
						Component pan = panel.get(i);
						if (pan.getWidth() == 0 || pan.getHeight() == 0)
							break;
						pan.setBackground(temp);

					}
					if (!isTrue||DataSave.低配模式)
						return;
					Other.sleep(25);
				}
			}

		}
	}

	public void close() {
		isTrue = false;
		for (int i = 0; i < panel.size(); i++) {
			panel.get(i).setBackground(yuan.get(i));
		}
	}

	private Color hsl转rgb(float h, float s, float l) {
		float r, g, b;
		float q = 0, p = 0;
		float T[] = new float[3];
		if (s == 0) {
			r = g = b = l;
		} else {
			if (l < 0.5)
				q = (float) (l * (1.0 + s));
			if (l >= 0.5)
				q = l + s - l * s;
			p = (float) (2.0 * l - q);
			T[0] = h + 0.3333333f;
			T[1] = h;
			T[2] = h - 0.3333333f;
			for (int i = 0; i < 3; i++) {
				if (T[i] < 0)
					T[i] += 1.0f;
				if (T[i] > 1)
					T[i] -= 1.0f;
				if ((T[i] * 6) < 1) {
					T[i] = p + ((q - p) * 6.0f * T[i]);
				} else if ((T[i] * 2.0f) < 1) {
					T[i] = q;
				} else if ((T[i] * 3.0f) < 2) {
					T[i] = p + (q - p) * ((2.0f / 3.0f) - T[i]) * 6.0f;
				} else
					T[i] = p;
			}
			r = T[0];
			g = T[1];
			b = T[2];
		}
		r = ((r > 1) ? 1 : ((r < 0) ? 0 : r));// 取值范围(0,1)
		g = ((g > 1) ? 1 : ((g < 0) ? 0 : g));// 取值范围(0,1)
		b = ((b > 1) ? 1 : ((b < 0) ? 0 : b));
		return new Color(r, g, b);
	}

	public void addNew(JComponent panel_2) {
		yuan.add(panel_2.getBackground());
		this.panel.add(panel_2);
	}

}
