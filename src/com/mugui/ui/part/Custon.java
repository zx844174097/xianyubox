package com.mugui.ui.part;

import com.mugui.Dui.DPanel;
import com.mugui.Dui.DimgFile;
import com.mugui.Dui.DrawImg;
import com.mugui.Dui.Magnifying;

import java.awt.BorderLayout;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.mugui.Dui.DButton;
import com.mugui.model.UIModel;
import com.mugui.tool.FileTool;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Custon extends DPanel {
	public Custon() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.NORTH);

		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIModel.setUI(DataSave.bodyPanel);
			}
		});
		button_2.setText("取消");

		JPanel panel_7 = new JPanel();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup().addGap(1).addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(button_2, GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE).addGap(5)));
		gl_panel_2
				.setVerticalGroup(gl_panel_2
						.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2.createSequentialGroup().addGap(5).addComponent(button_2,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE));

		JLabel label_1 = new JLabel("钓鱼自定义栏");
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("宋体", Font.BOLD, 24));
		panel_7.add(label_1);
		panel_2.setLayout(gl_panel_2);

		bodyPanel = new JPanel();
		add(bodyPanel, BorderLayout.CENTER);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[] { 1 };
		gbl_panel_5.rowHeights = new int[] { 1, 1 };
		gbl_panel_5.columnWeights = new double[] { 1 };
		gbl_panel_5.rowWeights = new double[] { 1, 1 };
		bodyPanel.setLayout(gbl_panel_5);

		drawPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 2;
		gbc_scrollPane.weighty = 4.0;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		bodyPanel.add(drawPane, gbc_scrollPane);
		panel_1 = new DrawImg(this);
		drawPane.setViewportView(panel_1);

		JPanel panel = new JPanel();
		drawPane.setColumnHeaderView(panel);
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);

		DButton button = new DButton((String) null, (Color) null);
		button.setFont(new Font("Dialog", Font.PLAIN, 13));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel_1.lastDraw();
			}
		});
		button.setText("上一次");
		panel.add(button);

		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.setFont(new Font("Dialog", Font.PLAIN, 13));
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage image = panel_1.nowDraw();
				Point p = tool.区域找图(1500, 318, 1904, 709, 0.15, image);
				if (p != null)
					tool.mouseMove(p.x, p.y);
			}
		});
		button_3.setText("测试");
		panel.add(button_3);
		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.setFont(new Font("Dialog", Font.PLAIN, 13));
		panel.add(button_1);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nowPanel == null) {
					JOptionPane.showConfirmDialog(Custon.this, "请先选择一个自定义项");
					return;
				}
				String path = nowPanel.getPath() + "/" + System.currentTimeMillis() + ".bmp";
				DimgFile dFile = new DimgFile();
				dFile.bufferedImage = panel_1.nowDraw();
				dFile.file = new File(path);
				dFile.saveAllData();
				nowPanel.addImgFile();
			}
		});
		button_1.setText("保存编辑");

		DButton button_6 = new DButton((String) null, (Color) null);
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage bImage = tool.截取屏幕(0, 0, (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
						(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
				panel_1.drawImg(bImage);
				repaint();
			}
		});
		button_6.setText("截取当前屏幕");
		button_6.setFont(new Font("Dialog", Font.PLAIN, 13));
		panel.add(button_6);
		xypanel = new XYPanel(new Dimension(25, 18));
		panel.add(xypanel);
		xypanel.addXYListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				panel_1.drawImg(xypanel.getX_YImg());
			}

		});
		final JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_4.getLayout();
		flowLayout_3.setHgap(0);
		flowLayout_3.setVgap(0);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		add(panel_4, BorderLayout.WEST);
		panel_4.setPreferredSize(new Dimension(300, this.getHeight()));
		// dyGm = DataSave.dyGm;
		// panel_4.add(dyGm);
		// dyGm.setPreferredSize(new Dimension(300, 150));
		yuTu = new TitleBodyPanel(DataSave.JARFILEPATH + "/自定义拾取物", "拾取物栏：");
		yuTu.addPutActionListener(yuTu.new PutActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nowPanel = yuTu;
				// panel_4.remove(keYaoSet);
				panel_4.validate();
				panel_4.repaint();
			}
		});
		yuTu.addRemoveActionListener(yuTu.new RemoveActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.remove(keYaoSet);
				panel_4.validate();
				panel_4.repaint();
				RemoveDyOther(getThis());
			}
		});
		yuTu.addTestActionListener(yuTu.new TestActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.remove(keYaoSet);
				panel_4.validate();
				panel_4.repaint();
				TestDyOther(getThis());
			}

		});
		yuTu.setBoundsSize(300, 150);
		panel_4.add(yuTu);
		yuGan = new TitleBodyPanel(DataSave.JARFILEPATH + "/自定义鱼竿", "更换鱼竿栏：");
		yuGan.addPutActionListener(yuGan.new PutActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nowPanel = yuGan;
				// panel_4.remove(keYaoSet);
				panel_4.validate();
				panel_4.repaint();
			}
		});
		yuGan.addTestActionListener(yuGan.new TestActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.remove(keYaoSet);
				panel_4.validate();
				panel_4.repaint();
				TestDyOther(getThis());
			}
		});
		yuGan.addRemoveActionListener(yuGan.new RemoveActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.remove(keYaoSet);
				panel_4.validate();
				panel_4.repaint();
				RemoveDyOther(getThis());
			}
		});
		yuGan.setBoundsSize(300, 150);
		panel_4.add(yuGan);
		keYao = new TitleBodyPanel(DataSave.JARFILEPATH + "/自定义截图", "料理、炼金台截图：");
		keYao.addPutActionListener(keYao.new PutActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nowPanel = keYao;
				// panel_4.add(keYaoSet);
				// keYaoSet.setPreferredSize(new Dimension(300, 200));
				panel_4.validate();
				panel_4.repaint();
			}
		});
		keYao.addTestActionListener(keYao.new TestActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.add(keYaoSet);
				// keYaoSet.setPreferredSize(new Dimension(300, 200));
				panel_4.validate();
				panel_4.repaint();
				TestDyOther(getThis());
			}

		});
		keYao.addRemoveActionListener(keYao.new RemoveActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.add(keYaoSet);
				// keYaoSet.setPreferredSize(new Dimension(300, 200));
				panel_4.validate();
				panel_4.repaint();
				RemoveDyOther(getThis());
			}
		});
		keYao.setBoundsSize(300, 150);
		panel_4.add(keYao);

		cjimg = new TitleBodyPanel(DataSave.JARFILEPATH + "/采集工具", "采集工具截图：");
		cjimg.addPutActionListener(cjimg.new PutActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nowPanel = cjimg;
				// panel_4.add(keYaoSet);
				// keYaoSet.setPreferredSize(new Dimension(300, 200));
				panel_4.validate();
				panel_4.repaint();
			}
		});
		cjimg.addTestActionListener(cjimg.new TestActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.add(keYaoSet);
				// keYaoSet.setPreferredSize(new Dimension(300, 200));
				panel_4.validate();
				panel_4.repaint();
				TestDyOther(getThis());
			}

		});
		cjimg.addRemoveActionListener(cjimg.new RemoveActionListener() {
			public void actionPerformed(ActionEvent e) {
				// panel_4.add(keYaoSet);
				// keYaoSet.setPreferredSize(new Dimension(300, 200));
				panel_4.validate();
				panel_4.repaint();
				RemoveDyOther(getThis());
			}
		});
		cjimg.setBoundsSize(300, 150);
		panel_4.add(cjimg);
		// keYaoBuffy = new TitleBodyPanel(this, DataSave.JARFILEPATH
		// + "/自动以钓鱼buff", "钓鱼buff图样：");
		// keYaoBuffy.setPreferredSize(new Dimension(300, 150));
		// panel_4.add(keYaoBuffy);

	}

	private void TestDyOther(TitleBodyPanel this1) {
		nowPanel = this1;
		if (this1.nowDimg == null)
			return;
		BufferedImage image = this1.nowDimg.getBufferedImage();
		// Point p = tool.区域找图(1500, 318, 1904, 709, 0.15, image);
		int SCREEN_WIDTH = DataSave.SCREEN_WIDTH;
		int SCREEN_X = DataSave.SCREEN_X;
		int SCREEN_HEIGHT = DataSave.SCREEN_HEIGHT;
		int SCREEN_Y = DataSave.SCREEN_Y;
		Point p = tool.区域找图(SCREEN_WIDTH - 473 + SCREEN_X, SCREEN_HEIGHT / 2 - 367 + SCREEN_Y, SCREEN_WIDTH - 5 + SCREEN_X, SCREEN_HEIGHT / 2 + 366 + SCREEN_Y,
				0.15, image);

		if (p != null)
			tool.mouseMove(p.x, p.y);
	}

	private void RemoveDyOther(TitleBodyPanel this1) {
		nowPanel = this1;
		if (this1.nowDimg == null)
			return;
		this1.deleteImgFile();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2651299641370304594L;
	private DrawImg panel_1 = null;
	// private DYGm dyGm = null;
	private TitleBodyPanel yuTu = null;
	private TitleBodyPanel yuGan = null;
	private TitleBodyPanel keYao = null;
	private TitleBodyPanel cjimg = null;
	// private TitleInfoPanel keYaoSet = null;
	private XYPanel xypanel = null;

	public BufferedImage[] getyuTu() {
		return yuTu.getImages();
	}

	public BufferedImage[] getyuGan() {
		return yuGan.getImages();
	}

	public DimgFile[] getKeYao() {
		return keYao.getDimgAll();
	}

	public BufferedImage[] getLjTai() {
		return keYao.getImages();
	}

	public BufferedImage[] getCjImg() {
		return cjimg.getImages();
	}

	public TitleBodyPanel nowPanel = null;
	private JScrollPane drawPane = null;
	private JPanel bodyPanel = null;
	private Tool tool = null;
	private Dimension now_Dimension = null;
	private Point now_Point = null;
	private Magnifying magnifying = null;

	@Override
	public void init() {
		now_Dimension = DataSave.StaticUI.getSize();
		now_Point = DataSave.StaticUI.getLocation();
		if (tool == null)
			tool = new Tool();
		DataSave.StaticUI.updateTitle("黑色沙漠咸鱼辅助(钓鱼自定义设置)");
		DataSave.StaticUI.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, Toolkit.getDefaultToolkit().getScreenSize().height - 50);
		DataSave.StaticUI.setLocation(0, 0);
		datainit();
		setImgDraw();
		repaint();
		validate();
		if (magnifying == null) {
			magnifying = new Magnifying(DataSave.StaticUI, "", false);
			magnifying.setKai(200, 220);
		}
		if (!magnifying.isClose()) {
			magnifying.start();
		}
	}

	public void datainit() {
		yuTu.init();
		yuGan.init();
		keYao.init();
		// keYaoBuffy.init();
	}

	private void setImgDraw() {
		File f = new File(FileTool.getWindowsPath().getPath() + "\\Black Desert\\ScreenShot");
		if (!f.exists())
			return;
		File ff[] = f.listFiles();
		if (ff.length == 0)
			return;
		for (int i = 1; i < ff.length; i++) {
			if (ff[i].lastModified() > ff[0].lastModified()) {
				ff[0] = ff[i];
			}
		}
		try {
			panel_1.drawImg(ImageIO.read(new FileInputStream(ff[0])));
			repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void quit() {
		DataSave.StaticUI.setLocation(now_Point);
		DataSave.StaticUI.setSize(now_Dimension);
		magnifying.stop();
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
