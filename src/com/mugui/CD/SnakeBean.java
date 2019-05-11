package com.mugui.CD;

import java.util.Vector;

class 食物 {
	private boolean huo = true;
	private int x, y;
	static public int kuang = 0;

	public 食物() {
		食物.kuang = SnakeBean.kuang;
	}

	public boolean isHuo() {
		return huo;
	}

	public void setHuo(boolean huo) {
		this.huo = huo;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

public class SnakeBean implements Runnable {
	private int x, y;
	static final int kuang = 20;
	private int sudu = SnakeBean.kuang;
	private int fangxiang = 0;
	static final int shang = 0;
	static final int xia = 1;
	static final int zuo = 2;
	static final int you = 3;
	static int shuaxin = 140;
	private boolean huo = true;
	static public boolean 网格线 = false;

	public SnakeBean(int x, int y, int fangxiang) {
		this.x = x;
		this.y = y;
		this.fangxiang = fangxiang;
	}

	static Vector<SnakeBean> new1 = null;
	private static 食物 shiwu = null;

	public void getvector(Vector<SnakeBean> new2) {
		new1 = new2;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSudu() {
		return sudu;
	}

	public void setSudu(int sudu) {
		this.sudu = sudu;
	}

	public int getFangxiang() {
		return fangxiang;
	}

	public void setFangxiang(int fangxiang) {
		this.fangxiang = fangxiang;
	}

	public int getKuang() {
		return kuang;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(shuaxin - new1.size()); 
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if (this.getFangxiang() == SnakeBean.shang) {
				this.setY(getY() - getSudu());
			} else if (this.getFangxiang() == SnakeBean.xia) {
				this.setY(getY() + getSudu());
			} else if (this.getFangxiang() == SnakeBean.zuo) {
				this.setX(getX() - getSudu());
			} else if (this.getFangxiang() == SnakeBean.you) {
				this.setX(getX() + getSudu());
			}
			if (this.getX() > SnakeView.with-kuang || this.getY() > SnakeView.hei-kuang || this.getX() < 0 || this.getY() < 0) {
				setHuo(false);
				return;
			}
			蛇走.shexing(new1);
			for (int i = 0; i < new1.size() - 1; i++) {
				if (getX() == new1.get(i).getX() && getY() == new1.get(i).getY()) {
					setHuo(false);
					return;
				}
			}
			if (getX() == shiwu.getX() && getY() == shiwu.getY()) {
				shiwu.setHuo(false);
			}
		}

	}

	public boolean isHuo() {
		return huo;
	}

	public void setHuo(boolean huo) {
		this.huo = huo;
	}

	public 食物 getShiwu() {
		return shiwu;
	}

	public void setShiwu(食物 shiwu) {
		SnakeBean.shiwu = shiwu;
	}
}
