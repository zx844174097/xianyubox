package com.mugui.CD;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import com.mugui.Dui.DPanel;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;
import com.mugui.ui.DataSave;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;

public class Tank extends DPanel {
	public Tank() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		add(panel, BorderLayout.EAST);
		panel.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		list = new JList<String>();
		scrollPane.setViewportView(list);

		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);

		JLabel label = new JLabel("贪吃蛇排行榜");
		label.setFont(new Font("宋体", Font.BOLD, 14));
		panel_1.add(label);

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		body = new JPanel();
		panel_2.add(body);
		body.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_4.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_4);

		userLabel = new JLabel("当前用户:");
		panel_4.add(userLabel);

		snakeLabel = new JLabel("分数：");
		panel_3.add(snakeLabel);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -844669917199399395L;
	private SnakeView snakeView = null;
	private Dimension dimension = null;
	private Point yuan = null;
	private JList<String> list = null;
	private JPanel body = null;
	private JLabel userLabel = null;
	private JLabel snakeLabel = null;

	@Override
	public void init() {
		yuan = DataSave.StaticUI.getLocation();
		Dimension dimension2 = Toolkit.getDefaultToolkit().getScreenSize();
		dimension = new Dimension(600, 500);
		setOpaque(false);
		DataSave.bodyPanel.setOpaque(false);
		DataSave.StaticUI.setSize(dimension.width, dimension.height);
		DataSave.StaticUI.setLocation(dimension2.width / 2 - dimension.width / 2, dimension2.height / 2 - dimension.height / 2);
		DataSave.StaticUI.setAlwaysOnTop(true);
		if (DataSave.userBean.getUser_name() != null && !DataSave.userBean.getUser_name().trim().equals("")) {
			startSnake();
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String s = JOptionPane.showInputDialog(DataSave.StaticUI, "设置你的昵称", "提醒", JOptionPane.OK_OPTION).trim();
					UserBean userBean = new UserBean();
					userBean.setUser_name(s);
					TcpBag tcpBag = new TcpBag();
					tcpBag.setBag_id(TcpBag.SET_USER_NAME);
					tcpBag.setBody(userBean.toJsonObject());
					TCPModel.SendTcpBag(tcpBag);
				}
			}).start();
		}
	}

	public void setSnakeLabel(String s) {
		snakeLabel.setText("分数：" + s);
	}

	public void startSnake() {
		userLabel.setText("当前用户:" + DataSave.userBean.getUser_name());
		snakeView = new SnakeView(dimension.width - 260, dimension.height - 140);
		Toolkit.getDefaultToolkit().addAWTEventListener(snakeView, AWTEvent.KEY_EVENT_MASK);
		body.removeAll();
		body.add(snakeView);
		Thread thread = new Thread(snakeView);
		thread.start();
		TcpBag tcpBag = new TcpBag();
		tcpBag.setBag_id(TcpBag.SNAKE_MARK_ALL);
		UserBean userBean = new UserBean();
		tcpBag.setBody(userBean.toJsonObject());
		TCPModel.SendTcpBag(tcpBag);
	}
 
	@Override
	public void quit() {
		if (snakeView != null)
			snakeView.close();
		Toolkit.getDefaultToolkit().removeAWTEventListener(snakeView);
		DataSave.StaticUI.setLocation(yuan);
	}

	public void setList(LinkedList<UserBean> linkedList) {
		DefaultListModel<String> listModel=new DefaultListModel<String>();
		Iterator<UserBean> i=linkedList.iterator();
		while(i.hasNext()){
			UserBean userBean=i.next();
			byte b[]=userBean.getUser_name().getBytes(Charset.forName("UTF-8"));
			String name="";
			if(b.length>43){
				name=new String(b,0,43,Charset.forName("UTF-8"));
			}else {
				byte bb[]=new byte[43];
				for(int j=0;j<bb.length;j++){
					if(j<b.length){
						bb[j]=b[j];
					}else {
						bb[j]=' ';
					}
				}
				name=new String(bb,0,43,Charset.forName("UTF-8"));
			}
			 b=userBean.getUser_snake_mark().getBytes(Charset.forName("UTF-8"));
			String snak_mark="";
			if(b.length>10){
				snak_mark=new String(b,0,10,Charset.forName("UTF-8"));
			}else {
				byte bb[]=new byte[10];
				for(int j=0;j<bb.length;j++){
					if(j<b.length){
						bb[j]=b[j];
					}else {
						bb[j]=' ';
					}
				}
				snak_mark=new String(bb,0,10,Charset.forName("UTF-8"));
			}
			listModel.addElement("用户："+name+"分数:"+snak_mark);
		}
		list.setModel(listModel);
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
