package com.mugui.ui.part;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DTextField;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.UIModel;
import com.mugui.tool.FileTool;
import com.mugui.ui.DataSave;

import javax.swing.JCheckBox;

public class admin extends DPanel {

	private CardLayout cardLayout = null;
	private JPanel panel_1 = null;

	public admin() {
		setBackground(null);
		setForeground(null);
		setLayout(new BorderLayout(0, 0));
		panel_1 = new JPanel(); 
		panel_1.setForeground(Color.DARK_GRAY);
		panel_1.setBackground(Color.DARK_GRAY);
		add(panel_1, BorderLayout.CENTER);
		cardLayout = new CardLayout(0, 0);
		panel_1.setLayout(cardLayout);

		JPanel panel_7 = new JPanel();
		panel_7.setForeground(Color.DARK_GRAY);
		panel_1.add(panel_7, "注册");

		JPanel panel_9 = new JPanel();
		panel_9.setLayout(new GridLayout(0, 2, 1, 0));

		JLabel label_4 = new JLabel("密码：");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setFont(new Font("宋体", Font.BOLD, 20));
		panel_9.add(label_4);

		passwd1 = new JPasswordField(32);
		passwd1.setColumns(32);
		panel_9.add(passwd1); 

		JPanel panel_10 = new JPanel();
		panel_10.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel label_5 = new JLabel("邮箱地址：");
		label_5.setBackground(new Color(255, 228, 225));
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		label_5.setFont(new Font("宋体", Font.BOLD, 20));
		panel_10.add(label_5);

		mail1 = new DTextField(64);
		mail1.setColumns(64);
		panel_10.add(mail1);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new GridLayout(0, 2, 1, 0));

		JLabel label_6 = new JLabel("验证码：");
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		label_6.setFont(new Font("宋体", Font.BOLD, 20));
		panel_3.add(label_6);

		code = new DTextField(20);
		code.setColumns(20);
		panel_3.add(code);

		JPanel panel_8 = new JPanel();

		JPanel panel_11 = new JPanel();
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_panel_7
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_7.createParallelGroup(Alignment.TRAILING)
										.addComponent(panel_11, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addComponent(panel_9, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addComponent(panel_10, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addComponent(panel_8, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)).addContainerGap()));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_7.createSequentialGroup().addContainerGap().addComponent(panel_10, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_9, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_8, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_11, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE).addContainerGap()));
		panel_8.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		DButton button_2 = new DButton((String) null, Color.white);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 注册
				UserBean userBag = new UserBean();
				userBag.setCode(code.getText().trim());
				userBag.setUser_mail(mail1.getText().trim());
				userBag.setUser_passwd(new String(passwd1.getPassword()).trim());
				TcpBag bag = new TcpBag();
				bag.setBag_id(TcpBag.REG_SEND);
				bag.setBody(userBag.toJsonObject());
				UIModel.reg(bag);
				// UDPModel.SendTcpBag(bag);
			}
		});
		button_2.setFont(new Font("宋体", Font.BOLD, 25));
		button_2.setText("注册");
		panel_8.add(button_2);
		DButton button_3 = new DButton((String) null, Color.white);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TcpBag bag = new TcpBag();
				UserBean userBag = new UserBean();
				userBag.setUser_mail(mail1.getText().trim());
				bag.setBody(userBag.toJsonObject());
				bag.setBag_id(TcpBag.REG_CODE_SEND);
				UIModel.reg_code(bag);
				// UDPModel.SendTcpBag(bag);
			}
		});
		button_3.setFont(new Font("宋体", Font.BOLD, 25));
		button_3.setText("获取验证码");
		panel_8.add(button_3);
		panel_7.setLayout(gl_panel_7);

		JPanel panel_2 = new JPanel();
		panel_2.setForeground(Color.DARK_GRAY);
		panel_1.add(panel_2, "登录");

		JPanel panel_4 = new JPanel();

		JPanel panel_5 = new JPanel();

		JPanel panel_6 = new JPanel();

		JPanel panel_12 = new JPanel();
		panel_12.setLayout(new BorderLayout(0, 0));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_panel_2
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_2.createParallelGroup(Alignment.TRAILING)
										.addComponent(panel_12, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addComponent(panel_6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)).addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_2.createSequentialGroup().addContainerGap().addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_12, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE).addContainerGap()));

		JPanel panel_13 = new JPanel();
		panel_12.add(panel_13, BorderLayout.NORTH);

		JPanel panel_14 = new JPanel();
		panel_12.add(panel_14, BorderLayout.SOUTH);
		panel_4.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel label_2 = new JLabel("邮箱地址：");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("宋体", Font.BOLD, 20));
		panel_4.add(label_2);

		mail = new DTextField(64);
		mail.setText("");
		mail.setColumns(64);
		panel_4.add(mail);
		panel_6.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		DButton button = new DButton((String) null, Color.white);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 登录
				UserBean userBag = new UserBean();
				userBag.setUser_mail(mail.getText().trim());
				userBag.setUser_passwd(new String(passwd.getPassword()).trim());
				TcpBag bag = new TcpBag();
				bag.setBag_id(TcpBag.LOGIN_SEND);
				bag.setBody(userBag.toJsonObject());
				UIModel.login(bag);
			}
		});
		button.setFont(new Font("宋体", Font.BOLD, 25));
		button.setText("登录");
		panel_6.add(button);

		DButton button_1 = new DButton((String) null, Color.white);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(panel_1, e.getActionCommand());
			}
		});
		button_1.setFont(new Font("宋体", Font.BOLD, 25));
		button_1.setText("找回密码");
		panel_6.add(button_1);

		iszhanghao = new JCheckBox("记住账号");
		iszhanghao.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isZhanghao())
					;
				else {
					saveZhanghao("");
				}
			}
		});
		iszhanghao.setFont(new Font("宋体", Font.PLAIN, 22));
		panel_6.add(iszhanghao);
		panel_5.setLayout(new GridLayout(0, 2, 1, 0));

		JLabel label_3 = new JLabel("密码：");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setFont(new Font("宋体", Font.BOLD, 20));
		panel_5.add(label_3);

		passwd = new JPasswordField();
		passwd.setColumns(32);
		passwd.setText("");
		passwd.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// 登录
					UserBean userBag = new UserBean();
					userBag.setUser_mail(mail.getText().trim());
					userBag.setUser_passwd(new String(passwd.getPassword()).trim());
					TcpBag bag = new TcpBag();
					bag.setBag_id(TcpBag.LOGIN_SEND);
					bag.setBody(userBag.toJsonObject());
					UIModel.login(bag);
				}
			}
		});
		panel_5.add(passwd);
		panel_2.setLayout(gl_panel_2);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));

		JLabel label_1 = new JLabel("登录");
		label_1.addMouseListener(l);
		label_1.setBackground(Color.GREEN);
		label_1.setFont(new Font("宋体", Font.BOLD, 35));
		label_1.setForeground(Color.BLACK);
		panel.add(label_1);
		JLabel label = new JLabel("注册");
		label.addMouseListener(l);
		label.setBackground(new Color(240, 248, 255));
		label.setFont(new Font("宋体", Font.BOLD, 35));
		panel.add(label);
		cardLayout.show(panel_1, "登录");

		JPanel panel_15 = new JPanel();
		panel_15.setForeground(Color.DARK_GRAY);
		panel_1.add(panel_15, "找回密码");

		JPanel panel_16 = new JPanel();

		JPanel panel_17 = new JPanel();
		panel_17.setLayout(new GridLayout(0, 2, 1, 0));

		JLabel label_9 = new JLabel("密码：");
		label_9.setHorizontalAlignment(SwingConstants.RIGHT);
		label_9.setFont(new Font("宋体", Font.BOLD, 20));
		panel_17.add(label_9);

		passwd2 = new JPasswordField(32);
		panel_17.add(passwd2);

		JPanel panel_18 = new JPanel();
		panel_18.setLayout(new GridLayout(1, 0, 0, 0));

		JLabel label_10 = new JLabel("邮箱地址：");
		label_10.setHorizontalAlignment(SwingConstants.RIGHT);
		label_10.setFont(new Font("宋体", Font.BOLD, 20));
		label_10.setBackground(new Color(255, 228, 225));
		panel_18.add(label_10);

		mail2 = new DTextField(64);
		mail2.setColumns(64);
		panel_18.add(mail2);

		JPanel panel_19 = new JPanel();
		panel_19.setLayout(new GridLayout(0, 2, 1, 0));

		JLabel label_11 = new JLabel("验证码：");
		label_11.setHorizontalAlignment(SwingConstants.RIGHT);
		label_11.setFont(new Font("宋体", Font.BOLD, 20));
		panel_19.add(label_11);

		 code2 = new DTextField(20);
		 code2.setColumns(20);
		panel_19.add(code2);

		JPanel panel_20 = new JPanel();
		panel_20.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		DButton button_4 = new DButton((String) null, Color.WHITE);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//更新密码
				UserBean userBag = new UserBean();
				userBag.setCode(code2.getText().trim());
				userBag.setUser_mail(mail2.getText().trim());
				userBag.setUser_passwd(new String(passwd2.getPassword()).trim());
				TcpBag bag = new TcpBag();
				bag.setBag_id(TcpBag.UPDATE_USER_PAWD);
				bag.setBody(userBag.toJsonObject());
				UIModel.reg(bag);
			}
		});
		button_4.setText("更新密码");
		button_4.setFont(new Font("宋体", Font.BOLD, 25));
		panel_20.add(button_4);

		DButton button_5 = new DButton((String) null, Color.WHITE);
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TcpBag bag = new TcpBag();
				UserBean userBag = new UserBean();
				userBag.setUser_mail(mail2.getText().trim());
				bag.setBody(userBag.toJsonObject());
				bag.setBag_id(TcpBag.UPDATE_REG_CODE);
				UIModel.reg_code(bag);
			}
		});
		button_5.setText("获取验证码");
		button_5.setFont(new Font("宋体", Font.BOLD, 25));
		panel_20.add(button_5);
		GroupLayout gl_panel_15 = new GroupLayout(panel_15);
		gl_panel_15.setHorizontalGroup(gl_panel_15
				.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 450, Short.MAX_VALUE)
				.addGroup(
						gl_panel_15
								.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_panel_15.createParallelGroup(Alignment.TRAILING)
												.addComponent(panel_16, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
												.addComponent(panel_17, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
												.addComponent(panel_18, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
												.addComponent(panel_19, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
												.addComponent(panel_20, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)).addContainerGap()));
		gl_panel_15.setVerticalGroup(gl_panel_15
				.createParallelGroup(Alignment.LEADING)
				.addGap(0, 251, Short.MAX_VALUE)
				.addGroup(
						gl_panel_15.createSequentialGroup().addContainerGap()
								.addComponent(panel_18, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_17, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_19, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_20, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_16, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
		panel_15.setLayout(gl_panel_15);
	}

	private MouseListener l = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
			JLabel j = (JLabel) (e.getSource());
			int i = j.getBackground().getRGB() - 4000;
			j.setBackground(new Color(i));
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			JLabel j = (JLabel) (e.getSource());
			int i = j.getBackground().getRGB() + 4000;
			j.setBackground(new Color(i));

		}

		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel j = (JLabel) (e.getSource());
			cardLayout.show(panel_1, j.getText());
		}
	};

	public void setCardLayout(String cardname) {
		cardLayout.show(panel_1, cardname);
	}

	private DTextField mail = null;
	private DTextField mail1 = null;
	private DTextField mail2 = null;
	private DTextField code = null;
	private DTextField code2 = null;
	private JPasswordField passwd = null;
	private JPasswordField passwd1 = null;
	private JPasswordField passwd2 = null;
	private JCheckBox iszhanghao = null;

	public boolean isZhanghao() {
		return iszhanghao.isSelected();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6552809899881312249L;

	@Override
	public void init() {
		DataSave.StaticUI.updateTitle("黑色沙漠咸鱼辅助系统");
		if (isZhang())
			iszhanghao.setSelected(true);
		else
			iszhanghao.setSelected(false);
		passwd.setText("");
		mail2.setText(getSaveUser());
		DataSave.StaticUI.setSize(560, 360);
		repaint();
	}

	private String getSaveUser() {
		File f = new File(FileTool.getWindowsPath() + "\\1");
		BufferedReader br = null;
		try {
			if (!f.exists())
				f.createNewFile();
			br = new BufferedReader(new FileReader(f));
			String s = br.readLine();
			if (s == null || s.trim().equals("")) {
				return "";
			} else {
				return s.trim();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";

	}

	private boolean isZhang() {
		File f = new File(FileTool.getWindowsPath() + "\\1");
		BufferedReader br = null;
		try {
			if (!f.exists())
				f.createNewFile();
			br = new BufferedReader(new FileReader(f));
			String s = br.readLine();
			if (s == null || s.trim().equals("")) {
				mail.setText("");
				return false;
			} else {
				mail.setText(s);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void quit() {
	}

	public void saveZhanghao(String mail) {
		File f = new File(FileTool.getWindowsPath() + "\\1");
		BufferedWriter br = null;
		try {
			if (!f.exists())
				f.createNewFile();
			br = new BufferedWriter(new FileWriter(f));
			br.write(mail);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
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
