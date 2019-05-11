package com.mugui.CD;

import javax.swing.*;

import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class SnakeView extends JPanel implements AWTEventListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2332769845813231629L;
	public static int with;
	public static int hei;
	Vector<SnakeBean> seVector = null;
	食物 shiwu = null;

	public SnakeView(int with, int hei) {
		SnakeView.with = with;
		SnakeView.hei = hei;
		shiwu();
		chushihua();
	}

	private void shiwu() {
		// TODO Auto-generated method stub
		shiwu = new 食物();
		shiwu.setX(suijishu(0, SnakeView.with / SnakeBean.kuang - 1) * SnakeBean.kuang);
		shiwu.setY(suijishu(0, SnakeView.hei / SnakeBean.kuang - 1) * SnakeBean.kuang);
	}

	private int suijishu(int i, int j) {
		int max = j;
		int min = i;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	int zhi = 3;

	public void chushihua() {
		// TODO Auto-generated method stub
		// 初始化一只蛇
		zhi = 3;
		seVector = new Vector<SnakeBean>();
		SnakeBean 蛇类1 = new SnakeBean(zhi * SnakeBean.kuang, 0, SnakeBean.you);
		蛇类1.getvector(seVector);
		for (int i = 0; i < zhi; i++)
			seVector.add(new SnakeBean(0 + i * SnakeBean.kuang, 0, SnakeBean.you));
		seVector.add(蛇类1);
		DataSave.Tank.setSnakeLabel(seVector.size() + "");
		蛇类1.setShiwu(shiwu);
		Thread t = new Thread(蛇类1);
		t.start();
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.darkGray);
		g.fill3DRect(0, 0, with, hei, true);
		// 网格线
		if (SnakeBean.网格线)
			wangge(g);
		// 画出蛇
		huashe(g);
		// 分数
		g.setColor(Color.black);
		g.setFont(new Font("微软雅黑", Font.BOLD, 18));
		// g.drawString("分数：" + (seVector.size() - zhi - 1), 620, 100);
	}

	private void wangge(Graphics g) {
		g.setColor(new Color(0x989898));
		for (int i = 0; i < with; i += SnakeBean.kuang) {
			g.drawLine(SnakeBean.kuang + i, 0, SnakeBean.kuang + i, hei);
		}
		for (int i = 0; i < hei; i += SnakeBean.kuang) {
			g.drawLine(0, SnakeBean.kuang + i, with, SnakeBean.kuang + i);
		}

	}

	private void huashe(Graphics g) {
		g.setColor(Color.white);
		for (int i = 0; i < seVector.size(); i++) {
			SnakeBean new1 = seVector.get(i);
			g.fill3DRect(new1.getX(), new1.getY(), new1.getKuang(), new1.getKuang(), true);
		}
		g.setColor(Color.red);
		g.fill3DRect(shiwu.getX(), shiwu.getY(), seVector.get(0).getKuang(), seVector.get(0).getKuang(), true);
	}

	private SnakeBean s = new SnakeBean(0, 0, 0);
	private boolean isTrue = false;

	@Override
	public void run() {
		isTrue = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isTrue) {
					TcpBag bag = new TcpBag();
					bag.setBag_id(TcpBag.ERROR);
					UserBean userBean = new UserBean();
					userBean.setCode("dy");
					bag.setBody(userBean.toJsonObject());
					TCPModel.SendTcpBag(bag);
					int i = 0;
					while (i <= 60) {
						Other.sleep(1000);
						i++;
						if (!isTrue)
							return;
					}
				}
			}
		}).start();
		while (isTrue) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if (seVector.get(seVector.size() - 1).isHuo() == false) {
				if (seVector.size() > 10) {
					int i = JOptionPane.showConfirmDialog(DataSave.StaticUI, "保存记录", "提示", JOptionPane.OK_OPTION);
					if (i == 0) {
						TcpBag tcpBag = new TcpBag();
						tcpBag.setBag_id(TcpBag.SAVE_SNAKE_MARK);
						UserBean userBean = new UserBean();
						userBean.setUser_name(DataSave.userBean.getUser_name());
						userBean.setUser_snake_mark(seVector.size()+""); 
						tcpBag.setBody(userBean.toJsonObject());
						TCPModel.SendTcpBag(tcpBag);
					}
				}
				seVector.removeAllElements();
				this.chushihua();
				shiwu.setHuo(true);
				shiwu.setX(suijishu(0, SnakeView.with / SnakeBean.kuang - 1) * SnakeBean.kuang);
				shiwu.setY(suijishu(0, SnakeView.hei / SnakeBean.kuang - 1) * SnakeBean.kuang);
			}
			if (shiwu.isHuo() == false) {
				shiwu.setHuo(true);
				shiwu.setX(suijishu(0, SnakeView.with / SnakeBean.kuang - 1) * SnakeBean.kuang);
				shiwu.setY(suijishu(0, SnakeView.hei / SnakeBean.kuang - 1) * SnakeBean.kuang);
				SnakeBean new1 = seVector.get(0);
				if (new1.getFangxiang() == SnakeBean.shang)
					seVector.add(0, new SnakeBean(new1.getX(), new1.getY() - new1.getKuang(), SnakeBean.shang));
				if (new1.getFangxiang() == SnakeBean.xia)
					seVector.add(0, new SnakeBean(new1.getX(), new1.getY() + new1.getKuang(), SnakeBean.xia));
				if (new1.getFangxiang() == SnakeBean.zuo)
					seVector.add(0, new SnakeBean(new1.getX() - new1.getKuang(), new1.getY(), SnakeBean.zuo));
				if (new1.getFangxiang() == SnakeBean.you)
					seVector.add(0, new SnakeBean(new1.getX() + new1.getKuang(), new1.getY(), SnakeBean.you));
				DataSave.Tank.setSnakeLabel(seVector.size() + "");
			}
			this.repaint();
		}
	}

	@Override
	public void eventDispatched(AWTEvent event) {
		SnakeBean ss = seVector.get(seVector.size() - 1);
		if (s.getX() == ss.getX() && s.getY() == ss.getY()) {
			return;
		}
		s.setX(ss.getX());
		s.setY(ss.getY());
		// 上
		KeyEvent arg0 = ((KeyEvent) event);
		SnakeBean new1 = seVector.get(seVector.size() - 1);
		if (arg0.getKeyCode() == 38 && new1.getSudu() != 0) {
			if (new1.getFangxiang() != SnakeBean.shang && new1.getFangxiang() != SnakeBean.xia) {
				new1.setFangxiang(SnakeBean.shang);
			}
		}
		// 下
		else if (arg0.getKeyCode() == 40 && new1.getSudu() != 0) {
			if (new1.getFangxiang() != SnakeBean.xia && new1.getFangxiang() != SnakeBean.shang) {
				new1.setFangxiang(SnakeBean.xia);
			}
		}
		// 左
		else if (arg0.getKeyCode() == 37 && new1.getSudu() != 0) {
			if (new1.getFangxiang() != SnakeBean.zuo && new1.getFangxiang() != SnakeBean.you) {
				new1.setFangxiang(SnakeBean.zuo);
			}
		}
		// 右
		else if (arg0.getKeyCode() == 39 && new1.getSudu() != 0) {
			if (new1.getFangxiang() != SnakeBean.you && new1.getFangxiang() != SnakeBean.zuo) {
				new1.setFangxiang(SnakeBean.you);
			}
		} else if (arg0.getKeyCode() == 32) {
			if (new1.getSudu() != 0)
				new1.setSudu(0);
			else
				new1.setSudu(new1.getKuang());
		}
		蛇走.shexing(seVector);
	}

	public void close() {
		isTrue = false;
	}
}

class 优化蛇走 {
	public static void shexing(Vector<SnakeBean> seVector) {
		SnakeBean new1 = seVector.get(seVector.size() - 1);
		if (new1.getFangxiang() == SnakeBean.shang) {
			new1.setY(new1.getY() - new1.getKuang());
		}
		if (new1.getFangxiang() == SnakeBean.xia) {
			new1.setY(new1.getY() + new1.getKuang());
		}
		if (new1.getFangxiang() == SnakeBean.zuo) {
			new1.setX(new1.getX() - new1.getKuang());
		}
		if (new1.getFangxiang() == SnakeBean.you) {
			new1.setX(new1.getX() + new1.getKuang());
		}
		for (int i = 0; i < seVector.size() - 1; i++) {
			SnakeBean new2 = seVector.get(i);
			SnakeBean new3 = seVector.get(i + 1);
			new2.setX(new3.getX());
			new2.setY(new3.getY());
			new2.setFangxiang(new3.getFangxiang());
		}
	}
}

class 蛇走 {
	public static void shexing(Vector<SnakeBean> seVector) {
		// TODO Auto-generated method stub
		for (int i = seVector.size() - 2; i >= 0; i--) {
			SnakeBean new1 = seVector.get(i);
			SnakeBean new2 = seVector.get(i + 1);
			if (new1.getY() - new1.getKuang() == new2.getY()) {
				if (new2.getFangxiang() == SnakeBean.shang)
					new1.setX(new2.getX());
				else
					new1.setY(new2.getY());
			}
			if (new1.getY() + new1.getKuang() == new2.getY()) {
				if (new2.getFangxiang() == SnakeBean.xia)
					new1.setX(new2.getX());
				else
					new1.setY(new2.getY());
			}
			if (new1.getY() + new1.getKuang() * 2 == new2.getY()) {
				new1.setY(new2.getY() - new2.getKuang());
				new1.setFangxiang(new2.getFangxiang());
			}
			if (new1.getY() - new1.getKuang() * 2 == new2.getY()) {
				new1.setY(new2.getY() + new2.getKuang());
				new1.setFangxiang(new2.getFangxiang());
			}
			if (new1.getX() + new1.getKuang() * 2 == new2.getX()) {
				new1.setX(new2.getX() - new2.getKuang());
				new1.setFangxiang(new2.getFangxiang());
			}
			if (new1.getX() - new1.getKuang() * 2 == new2.getX()) {
				new1.setX(new2.getX() + new2.getKuang());
				new1.setFangxiang(new2.getFangxiang());
			}
		}
		for (int i = seVector.size() - 2; i >= 0; i--) {
			SnakeBean new1 = seVector.get(i);
			SnakeBean new2 = seVector.get(i + 1);
			if (new1.getY() == new2.getY() && new1.getX() - new1.getKuang() == new2.getX()) {
				new1.setFangxiang(SnakeBean.zuo);
			}
			if (new1.getY() == new2.getY() && new1.getX() + new1.getKuang() == new2.getX()) {
				new1.setFangxiang(SnakeBean.you);
			}
			if (new1.getX() == new2.getX() && new1.getY() - new1.getKuang() == new2.getY()) {
				new1.setFangxiang(SnakeBean.shang);
			}
			if (new1.getX() == new2.getX() && new1.getY() + new1.getKuang() == new2.getY()) {
				new1.setFangxiang(SnakeBean.xia);
			}
		}

	}
}
