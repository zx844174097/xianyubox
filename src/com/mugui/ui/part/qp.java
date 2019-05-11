package com.mugui.ui.part;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.Dui.DButton;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DimgFile;
import com.mugui.model.QpHandle;
import com.mugui.model.UIModel;
import com.mugui.tool.FileTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;
import com.mugui.Dui.DTextField;
import com.mugui.Dui.DVerticalFlowLayout;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class qp extends DPanel {
	private Tool tool = new Tool();

	public qp() {
		setLayout(new BorderLayout(0, 0));

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new DVerticalFlowLayout());

		JPanel panel_13 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_13.getLayout();
		flowLayout_2.setVgap(0);
		flowLayout_2.setHgap(0);
		panel_2.add(panel_13);

		JLabel label_1 = new JLabel("抢拍卖行定制栏");
		panel_13.add(label_1);
		label_1.setForeground(Color.RED);
		label_1.setFont(new Font("宋体", Font.BOLD, 24));

		startButton = new DButton((String) null, (Color) null);
		panel_13.add(startButton);

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 开始抢拍
				if (!readyQp()) {
					return;
				}
				startButton.setEnabled(false);
				startButton2.setEnabled(false);
				startButton3.setEnabled(false);
				DataSave.maidPoint1 = maidPoint1.getX_YPoint();
				DataSave.maidPoint2 = maidPoint2.getX_YPoint();
				DataSave.StaticUI.updateUI(DataSave.qpList);
				QpHandle.setModel(2);
				QpHandle.start();
			}
		});
		startButton.setText("开始多物品抢拍");

		startButton3 = new DButton((String) null, (Color) null);
		panel_13.add(startButton3);
		startButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!readyQp())
					return;
				startButton.setEnabled(false);
				startButton2.setEnabled(false);
				startButton3.setEnabled(false);
				DataSave.maidPoint1 = maidPoint1.getX_YPoint();
				DataSave.maidPoint2 = maidPoint2.getX_YPoint();
				// DataSave.StaticUI.updateUI(DataSave.qpList);
				QpHandle.setModel(3);
				QpHandle.start();
			}
		});
		startButton3.setText("开始无差别多物品抢拍");

		startButton2 = new DButton((String) null, (Color) null);
		panel_13.add(startButton2);
		startButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!readyQp())
					return;
				startButton.setEnabled(false);
				startButton2.setEnabled(false);
				DataSave.maidPoint1 = maidPoint1.getX_YPoint();
				DataSave.maidPoint2 = maidPoint2.getX_YPoint();
				// DataSave.StaticUI.updateUI(DataSave.qpList);
				QpHandle.setModel(1);
				QpHandle.start();
			}

		});
		startButton2.setText("开始单物品抢拍");

		JPanel panel_14 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_14.getLayout();
		flowLayout_4.setVgap(0);
		flowLayout_4.setHgap(0);
		panel_2.add(panel_14);

		ds_fck = new JRadioButton("单扫放仓库 时间:");
		panel_14.add(ds_fck);

		cf_time = new DTextField(5);
		panel_14.add(cf_time);
		cf_time.setText("60");
		cf_time.setColumns(5);

		JLabel lblNewLabel = new JLabel("分钟");
		panel_14.add(lblNewLabel);

		DButton button_2 = new DButton((String) null, (Color) null);
		panel_14.add(button_2);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "(待测试)只支持单扫和多扫，不支持无差别", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_2.setText("说明");
		button_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_2.setBackground(Color.GRAY);

		JPanel panel_12 = new JPanel();
		panel_14.add(panel_12);
		FlowLayout flowLayout_1 = (FlowLayout) panel_12.getLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setHgap(0);

		JLabel label_17 = new JLabel("          ");
		panel_12.add(label_17);

		jejc = new JRadioButton("剩余金额控制");
		panel_12.add(jejc);

		JLabel label_16 = new JLabel("金额");
		panel_12.add(label_16);
		je = new DTextField(11);
		je.setHorizontalAlignment(SwingConstants.RIGHT);
		je.setFont(new Font("宋体", Font.BOLD, 14));
		je.setColumns(14);
		je.setText("0");
		panel_12.add(je);

		je_s = new JLabel("剩余:  未知");
		panel_12.add(je_s);

		DButton button_3 = new DButton((String) null, (Color) null);
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "（待测试）当钱少于设定的金额时，辅助会停止抢拍", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_3.setText("说明");
		button_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_3.setBackground(Color.GRAY);
		panel_14.add(button_3);

		maidbutton = new JRadioButton("女仆");
		maidbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataSave.MAIDDQ = maidbutton.isSelected();
			}
		});
		panel_14.add(maidbutton);

		DButton button_4 = new DButton((String) null, (Color) null);
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						JPanel panel = DataSave.StaticUI.getUI();
						DInputDialog dialog = new DInputDialog("说明：", "抢拍将会通过女仆页面打开", true, false);
						UIModel.setUI(dialog);
						dialog.start();
						UIModel.setUI(panel);
					}
				}).start();
			}
		});
		button_4.setText("说明");
		button_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		button_4.setBackground(Color.GRAY);
		panel_14.add(button_4);

		radioButton = new JRadioButton("3秒强制刷新");
		panel_14.add(radioButton);

		JPanel panel_16 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_16.getLayout();
		flowLayout_6.setVgap(0);

		JLabel label_20 = new JLabel("女仆打开交易所坐标设置：");
		panel_16.add(label_20);
		maidPoint1 = new XYPanel(new Dimension(1, 1));
		maidPoint1.addXYListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				DataSave.maidPoint1 = maidPoint1.getX_YPoint();
			}
		});
		panel_16.add(maidPoint1);
		maidPoint2 = new XYPanel(new Dimension(1, 1));
		maidPoint2.addXYListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				DataSave.maidPoint2 = maidPoint2.getX_YPoint();
			}
		});
		panel_16.add(maidPoint2);
		panel_2.add(panel_16);
		otherSetPanel = new TitleInfoPanel("qp_other");
		allPanel = new TitleBodyPanel(DataSave.JARFILEPATH + "/物品图鉴表", "物品图鉴表", otherSetPanel);
		DButton test = new DButton((String) null, (Color) null);
		test.setFont(new Font("Dialog", Font.PLAIN, 13));
		test.setText("重置物品图");
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TestOneQpOther(allPanel);
			}

		});
		allPanel.addButton(test);
		add(allPanel, BorderLayout.CENTER);

		otherSetPanel.addObjecttypeListener(new ItemListener() {
			private String object_name;

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (object_name == null) {
					object_name = otherSetPanel.getObjectName();
					return;
				}
				switch (e.getStateChange()) {
				case ItemEvent.SELECTED:
					if (!object_name.equals(otherSetPanel.getObjectName())) {
						object_name = otherSetPanel.getObjectName();
						return;
					}
					DimgFile[] files = allPanel.getLikeObjectTypes(otherSetPanel.getObjecttype());
					if (files.length > 0) {
						otherSetPanel.setObjectTime(files[0].objecttime);
					}
					break;
				}

			}
		});

		JPanel panel_4 = new JPanel();
		add(panel_4, BorderLayout.WEST);
		FlowLayout flowLayout_3 = (FlowLayout) panel_4.getLayout();
		flowLayout_3.setVgap(0);
		flowLayout_3.setHgap(0);
		flowLayout_3.setAlignment(FlowLayout.LEFT);
		panel_4.setPreferredSize(new Dimension(300, this.getHeight()));
		nowPanel = new TitleBodyPanel(DataSave.JARFILEPATH + "/个人抢拍物品表", "抢拍物品添加栏", otherSetPanel);
		nowPanel.setBoundsSize(300, 150);
		nowPanel.addTestActionListener(nowPanel.new TestActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TestQpOther(getThis());
			}

		});
		nowPanel.addRemoveActionListener(nowPanel.new RemoveActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RemoveQpOther(getThis());
			}
		});

		panel_4.add(nowPanel);
		otherSetPanel.setPreferredSize(new Dimension(300, 200));
		panel_4.add(otherSetPanel);

		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3);

		// panel_4.add(panel_3);
		panel_3.setLayout(new DVerticalFlowLayout());
		JPanel panel_9 = new JPanel();
		panel_3.add(panel_9);
		panel_9.setLayout(new BorderLayout(0, 0));

		JLabel label = new JLabel("基本设置：");
		panel_9.add(label);
		label.setFont(new Font("宋体", Font.BOLD, 16));

		DButton button = new DButton((String) null, (Color) null);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataSave.savedata();
			}
		});
		button.setText("保存");
		panel_9.add(button, BorderLayout.EAST);

		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_5);

		JLabel label_2 = new JLabel("复数物品抢拍数量：");
		panel_5.add(label_2);

		QP_number = new DTextField(5);
		QP_number.setColumns(5);
		panel_5.add(QP_number);

		JLabel label_10 = new JLabel("个");
		panel_5.add(label_10);

		JPanel panel_10 = new JPanel();
		panel_3.add(panel_10);

		JLabel label_13 = new JLabel("物品输入前置时间：");
		panel_10.add(label_13);

		QP_writetime = new DTextField(5);
		QP_writetime.setColumns(5);
		panel_10.add(QP_writetime);

		JLabel lblMs_1 = new JLabel("ms");
		panel_10.add(lblMs_1);

		JPanel panel = new JPanel();
		panel_3.add(panel);

		JLabel label_3 = new JLabel("分类选择前置时间：");
		panel.add(label_3);

		QP_type = new DTextField(5);
		QP_type.setColumns(5);
		panel.add(QP_type);

		JLabel lblMs = new JLabel("ms");
		panel.add(lblMs);

		JPanel panel_8 = new JPanel();
		panel_3.add(panel_8);

		JLabel label_11 = new JLabel("等级选择前置时间：");
		panel_8.add(label_11);

		QP_lv = new DTextField(5);
		QP_lv.setColumns(5);
		panel_8.add(QP_lv);

		JLabel label_12 = new JLabel("ms");
		panel_8.add(label_12);

		JPanel panel_1 = new JPanel();
		panel_3.add(panel_1);

		JLabel label_4 = new JLabel("名称输入前置时间：");
		panel_1.add(label_4);

		QP_name = new DTextField(5);
		QP_name.setColumns(5);
		panel_1.add(QP_name);

		JLabel label_7 = new JLabel("ms");
		panel_1.add(label_7);

		JPanel panel_6 = new JPanel();
		panel_3.add(panel_6);

		JLabel label_5 = new JLabel("颜色判断前置时间：");
		panel_6.add(label_5);

		QP_color = new DTextField(5);
		QP_color.setColumns(5);
		panel_6.add(QP_color);

		JLabel label_8 = new JLabel("ms");
		panel_6.add(label_8);

		JPanel panel_11 = new JPanel();
		panel_3.add(panel_11);

		JLabel label_14 = new JLabel("物品判断前置时间：");
		panel_11.add(label_14);

		QP_objecttime = new DTextField(5);
		QP_objecttime.setText("500");
		QP_objecttime.setColumns(5);
		panel_11.add(QP_objecttime);

		JLabel label_15 = new JLabel("ms");
		panel_11.add(label_15);

		JPanel panel_7 = new JPanel();
		panel_3.add(panel_7);

		JLabel label_6 = new JLabel("抢夺开始前置时间：");
		panel_7.add(label_6);

		QP_start = new DTextField(5);
		QP_start.setColumns(5);
		panel_7.add(QP_start);

		JLabel label_9 = new JLabel("ms");
		panel_7.add(label_9);

		JPanel panel_15 = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) panel_15.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		panel_3.add(panel_15);

		JLabel label_18 = new JLabel("物品置换后摇时间：");
		panel_15.add(label_18);

		Qp_replace = new DTextField(5);
		Qp_replace.setColumns(5);
		panel_15.add(Qp_replace);

		JLabel label_19 = new JLabel("ms");
		panel_15.add(label_19);

		JPanel panel_17 = new JPanel();
		panel_3.add(panel_17);

		JLabel label_21 = new JLabel("两次购买间隔时间：");
		panel_17.add(label_21);

		QP_buyDelay = new DTextField(5);
		QP_buyDelay.setText("0");
		QP_buyDelay.setColumns(5);
		panel_17.add(QP_buyDelay);

		JLabel label_22 = new JLabel("ms");
		panel_17.add(label_22);
		nowPanel.addPutActionListener(allPanel.new PutActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nowPanel == null) {
					JOptionPane.showMessageDialog(qp.this, "请先选择一个自定义项", "信息", JOptionPane.OK_OPTION);
					return;
				}
				File f = allPanel.getImageFile();
				if (f == null) {
					JOptionPane.showMessageDialog(qp.this, "请先选择一个自定义项", "信息", JOptionPane.OK_OPTION);
					return;
				}
				FileTool.renameFileTo(f, new File(DataSave.JARFILEPATH + "/个人抢拍物品表/" + f.getName()));
				nowPanel.addImgFile();
			}
		});
		allPanel.addPutActionListener(allPanel.new PutActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filename = JOptionPane.showInputDialog(allPanel, "请输入新增物品名称", "信息", JOptionPane.OK_CANCEL_OPTION);
				if (StringUtils.isBlank(filename)) {
					return;
				}
				DimgFile dimgFile = new DimgFile();
				dimgFile.file = new File(DataSave.JARFILEPATH + "/物品图鉴表/" + Other.getShortUuid() + ".bmp");
				dimgFile.bufferedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
				dimgFile.bufferedImage.getGraphics().setColor(Color.red);
				dimgFile.bufferedImage.getGraphics().fillRect(0, 0, 50, 50);
				dimgFile.objectname = filename;
				dimgFile.saveAllData();
				allPanel.addImgFile();
			}
		});
		allPanel.addTestActionListener(allPanel.new TestActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TestQpOther(getThis());
			}

		});

		allPanel.addRemoveActionListener(allPanel.new RemoveActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RemoveQpOther(getThis());
			}
		});
		DButton testall = new DButton("测试全部", (Color) null);
		testall.setFont(new Font("Dialog", Font.PLAIN, 13));
		allPanel.addButton(testall);
		testall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TesTAllQpOther(allPanel);
			}
		});
	}

	private DTextField QP_number;
	private DTextField QP_lv;
	private DTextField QP_color;
	private DTextField QP_type;
	private DTextField QP_start;
	private DTextField QP_name;
	private DTextField QP_writetime;
	private DTextField QP_objecttime;
	private DTextField QP_buyDelay;

	public int getQp_Liang() {
		if (Other.isInteger(QP_number.getText().trim())) {
			return Integer.parseInt(QP_number.getText().trim());
		}
		return 0;
	}

	public int getQp_LV() {
		if (Other.isInteger(QP_lv.getText().trim())) {
			return Integer.parseInt(QP_lv.getText().trim());
		}
		return 200;
	}

	public int getQp_Color() {
		if (Other.isInteger(QP_color.getText().trim())) {
			return Integer.parseInt(QP_color.getText().trim());
		}
		return 200;
	}

	public int getQp_Type() {
		if (Other.isInteger(QP_type.getText().trim())) {
			return Integer.parseInt(QP_type.getText().trim());
		}
		return 200;
	}

	public int getQp_Start() {
		if (Other.isInteger(QP_start.getText().trim())) {
			return Integer.parseInt(QP_start.getText().trim());
		}
		return 100;
	}

	public int getQp_Replace() {
		if (Other.isInteger(Qp_replace.getText().trim())) {
			return Integer.parseInt(Qp_replace.getText().trim());
		}
		return 500;
	}

	public int getQp_Name() {
		if (Other.isInteger(QP_name.getText().trim())) {
			return Integer.parseInt(QP_name.getText().trim());
		}
		return 200;
	}

	public int getQp_Writertime() {
		if (Other.isInteger(QP_writetime.getText().trim())) {
			return Integer.parseInt(QP_writetime.getText().trim());
		}
		return 0;
	}

	public int getQp_ObjectTime() {
		if (Other.isInteger(QP_objecttime.getText().trim())) {
			return Integer.parseInt(QP_objecttime.getText().trim());
		}
		return 500;
	}

	public int getQP_BuyDelay() {
		if (Other.isInteger(QP_buyDelay.getText().trim())) {
			return Integer.parseInt(QP_buyDelay.getText().trim());
		}
		return 0;
	}

	public boolean readyQp() {
		tool.mouseMovePressOne(DataSave.SCREEN_WIDTH - 10 + DataSave.SCREEN_X, 10 + DataSave.SCREEN_Y, InputEvent.BUTTON1_MASK);
		return true;
	}

	private void TestQpOther(TitleBodyPanel this1) {
		if (!readyQp())
			return;
		startButton.setEnabled(false);
		startButton2.setEnabled(false);
		if (this1.nowDimg == null) {
			JOptionPane.showMessageDialog(qp.this, "信息", "未明确选择测试物品", JOptionPane.OK_OPTION);
			startButton.setEnabled(true);
			startButton2.setEnabled(true);
			return;
		}
		QpHandle.setISTrue(true);
		QpHandle.setModel(3);
		BufferedImage image = QpHandle.start(this1.nowDimg.getDimgFile());
		DataSave.StaticUI.setAlwaysOnTop(true);
		DataSave.StaticUI.setAlwaysOnTop(false);
		if (image != null) {
			this1.nowDimg.getDimgFile().bufferedImage = image;
			this1.nowDimg.getDimgFile().saveAllData();
		}
		QpHandle.stop();
		this1.validate();
		this1.repaint();
		startButton.setEnabled(true);
		startButton2.setEnabled(true);
	}

	private void TestOneQpOther(TitleBodyPanel this1) {
		if (!readyQp()) {
			return;
		}
		startButton.setEnabled(false);
		startButton2.setEnabled(false);
		if (this1.nowDimg == null) {
			JOptionPane.showMessageDialog(qp.this, "信息", "未明确选择测试物品", JOptionPane.OK_OPTION);
			startButton.setEnabled(true);
			startButton2.setEnabled(true);
			return;
		}
		QpHandle.setISTrue(true);
		QpHandle.setModel(4);
		BufferedImage image = QpHandle.start(this1.nowDimg.getDimgFile());
		DataSave.StaticUI.setAlwaysOnTop(true);
		DataSave.StaticUI.setAlwaysOnTop(false);
		if (image != null) {
			this1.nowDimg.getDimgFile().bufferedImage = image;
			this1.nowDimg.getDimgFile().saveAllData();
		}
		QpHandle.stop();
		this1.validate();
		this1.repaint();
		startButton.setEnabled(true);
		startButton2.setEnabled(true);
	}

	private void TesTAllQpOther(TitleBodyPanel allPanel) {
		DimgFile f[] = allPanel.getDimgAll();
		QpHandle.setModel(3);
		startButton.setEnabled(false);
		startButton2.setEnabled(false);
		QpHandle.setISTrue(true);
		for (DimgFile dimgFile : f) {
			if (!readyQp())
				return;
			BufferedImage image = QpHandle.start(dimgFile);
			if (image != null) {
				dimgFile.bufferedImage = image;
				dimgFile.saveAllData();
			}
			if (!QpHandle.isClose()) {
				QpHandle.stop();
				allPanel.validate();
				allPanel.repaint();
				return;
			}
		}
		QpHandle.stop();
		allPanel.validate();
		allPanel.repaint();
		startButton.setEnabled(true);
		startButton2.setEnabled(true);
	}

	private void RemoveQpOther(TitleBodyPanel this1) {
		this1.deleteImgFile();
	}

	public int getDeaultOne() {
		return nowPanel.getDeaultOne();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2651299641370304594L;
	// private DrawImg panel_1 = null;
	private TitleBodyPanel nowPanel = null;
	private TitleInfoPanel otherSetPanel = null;
	private TitleBodyPanel allPanel = null;
	// private JScrollPane drawPane = null;
	// private JPanel bodyPanel = null;
	private DButton startButton = null;
	private DButton startButton2 = null;
	private DButton startButton3 = null;
	private Dimension now_Dimension = new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
			Toolkit.getDefaultToolkit().getScreenSize().height - 50);
	private Point now_point = new Point(0, 0);
	private DTextField cf_time = null;

	public int getCf_time() {
		String time = cf_time.getText().trim();
		if (Other.isInteger(time)) {
			return Integer.parseInt(time) * 1000 * 60;
		}
		return 60 * 1000 * 60;
	}

	@Override
	public void init() {
		DataSave.StaticUI.setAlwaysOnTop(false);
		DataSave.StaticUI.updateTitle("黑色沙漠咸鱼辅助(抢拍模式)");
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.setLocation(now_point);
		allPanel.setBoundsSize(DataSave.StaticUI.getWidth() - 300, 300);
		dataInit();
		setImgDraw();
		repaint();
		validate();
		// 注册一个监听事件
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);

	}

	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {
				// 开始抢拍
				startButton.setEnabled(false);
				startButton2.setEnabled(false);
				startButton3.setEnabled(false);
				DataSave.maidPoint1 = maidPoint1.getX_YPoint();
				DataSave.maidPoint2 = maidPoint2.getX_YPoint();
				QpHandle.setModel(1);
				QpHandle.start();
				// DataSave.StaticUI.updateUI(DataSave.qpList);
			} else if (arg0 == 100) {
				// BufferedImage bImage = tool.截取屏幕(0, 0, (int)
				// Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)
				// Toolkit.getDefaultToolkit()
				// .getScreenSize().getHeight());
				// // gpanel_1.drawImg(bImage);
				// repaint();
			} else if (arg0 == 101) {
				QpHandle.stop();
				startButton.setEnabled(true);
				startButton2.setEnabled(true);
				startButton3.setEnabled(true);

			}
		}
	};

	public void dataInit() {
		allPanel.init();
		nowPanel.init();
		maidPoint1.setX_Y(DataSave.maidPoint1);
		maidPoint2.setX_Y(DataSave.maidPoint2);
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
			ImageIO.read(new FileInputStream(ff[0]));
			// panel_1.drawImg());
			repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void quit() {
		QpHandle.stop();
		startButton.setEnabled(true);
		startButton2.setEnabled(true);
		startButton3.setEnabled(true);
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
	}

	public DimgFile[] getDimgFiles() {
		return nowPanel.getDimgAll();
	}

	public void setQp_Color(String string) {
		QP_color.setText(string);
	}

	public void setQp_Liang(String string) {
		QP_number.setText(string);
	}

	public void setQp_LV(String string) {
		QP_lv.setText(string);
	}

	public void setQp_Start(String string) {
		QP_start.setText(string);
	}

	public void setQp_Replace(String string) {
		Qp_replace.setText(string);
	}

	public void setQp_Type(String string) {
		QP_type.setText(string);
	}

	public void setQp_Name(String string) {
		QP_name.setText(string);
	}

	public void setQp_Writetime(String string) {
		QP_writetime.setText(string);
	}

	public void setQp_BuyDelay(String string) {
		QP_buyDelay.setText(string);
	}

	private JRadioButton ds_fck = null;
	private JRadioButton jejc = null;
	private DTextField je = null;
	private JLabel je_s = null;
	private JRadioButton maidbutton;
	private DTextField Qp_replace;
	private JRadioButton radioButton;
	private XYPanel maidPoint1;
	private XYPanel maidPoint2;

	public boolean isForceRefresh() {
		return radioButton.isSelected();
	}

	public void setJe_s(String s) {
		je_s.setText("剩余:  " + s);
	}

	public boolean isQp_jejc() {
		return jejc.isSelected();
	}

	public long getQp_jejc() {
		String string = je.getText().trim();
		if (Other.isLong(string)) {
			long l = Long.parseLong(string);
			return l > 0 ? l : 0;
		}
		return 0;
	}

	public boolean isDs_fck() {
		return ds_fck.isSelected();
	}

	public void setUse(boolean b) {
		DataSave.bodyPanel.setButtonUse("qp", b);
		if (com.mugui.http.DataSave.qpTime <= 0 && b) {
			startButton2.setBackground(new Color(255, 105, 180));
		} else {
			startButton2.setBackground(new Color(64, 64, 64));
		}
	}

	@Override
	public void dataSave() {
		// TODO 自动生成的方法存根

	}

}
