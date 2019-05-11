package com.mugui.ui.Bean;

public class GameBean {
	private int g_app_id;
	private long g_parent_hwnd;
	private long g_hwnd;
	private long g_therad_id;
	private long g_style = -1;
	public long getG_style() {
		return g_style;
	}

	public void setG_style(long g_style) {
		this.g_style = g_style;
	}

	public long getG_parent_hwnd() {
		return g_parent_hwnd;
	}

	public void setG_parent_hwnd(long g_parent_hwnd) {
		this.g_parent_hwnd = g_parent_hwnd;
	}

	public long getG_hwnd() {
		return g_hwnd;
	}

	public void setG_hwnd(long g_hwnd) {
		this.g_hwnd = g_hwnd;
	}

	public long getG_therad_id() {
		return g_therad_id;
	}

	public void setG_therad_id(long g_therad_id) {
		this.g_therad_id = g_therad_id;
	}

	public int getG_app_id() {
		return g_app_id;
	}

	public void setG_app_id(int g_app_id) {
		this.g_app_id = g_app_id;
	}

}
