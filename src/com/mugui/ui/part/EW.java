package com.mugui.ui.part;

import com.mugui.Dui.DPanel;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.Dui.TimeInfo;
import com.mugui.model.EWHandle;
import com.mugui.tool.SerializeTool;
import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.mugui.ModelInterface;
import com.mugui.Dui.DButton;
import com.mugui.ui.DataSave;
import com.mugui.ui.df.FishPriceFrame;
import com.mugui.ui.part.EWtoDirectionCheckBox.DPoint2D;
import com.mugui.windows.GameBackstageTool;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import net.sourceforge.tess4j.Tesseract2;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;

public class EW extends DPanel {
	public EW() {
		setLayout(new DVerticalFlowLayout());

		JPanel panel_1 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_1.getLayout();
		flowLayout_1.setVgap(0);
		add(panel_1);

		final DButton btnboos = new DButton((String) null, (Color) null);
		panel_1.add(btnboos);
		btnboos.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (DataSave.低配模式)
					return;
				if (btnboos.getText().equals("打开boos时间窗口")) {
					btnboos.setText("关闭boos时间窗口"); 
					// BossUpdateView.start();
				} else {
					btnboos.setText("打开boos时间窗口");
					// BossUpdateView.stop();
				}
			}
		});
		btnboos.setText("打开boos时间窗口");

		final DButton button = new DButton((String) null, (Color) null);
		panel_1.add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (DataSave.低配模式)
					return;
				if (DataSave.服务器.equals("未知")) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							JPanel panel = DataSave.StaticUI.getUI();
							new TimeInfo("信息", "该功能出现异常，您无法使用", 3000).run(panel);
						}
					}).start();
					return; 
				}
				if (!Tesseract2.isInit()) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							JPanel panel = DataSave.StaticUI.getUI();
							new TimeInfo("信息", "该功能资源未加载完成，请稍后重试", 3000).run(panel);
						}
					}).start();
					return;
				}
				if (button.getText().equals("打开共享线路信息")) {
					button.setText("关闭共享线路信息");
					if (FishPriceFrame.lbl_index == -1)
						FishPriceFrame.lbl_index = 0;
					FishPriceFrame.start();
				} else {
					button.setText("打开共享线路信息");
					if (FishPriceFrame.lbl_index == -1)
						FishPriceFrame.lbl_index = 0;
					FishPriceFrame.stop();
				}
			}
		}); 
		button.setText("打开共享线路信息");
		add(directionCheckContainer = new EWtoDirectionContainerPanel());
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setVgap(0);
		add(panel);
		
		DButton button_1 = new DButton((String) null, (Color) null);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModelInterface object = (ModelInterface) DataSave.loader.loadClassToObject("com.mugui.ew.EWUIHandel");
				if (object != null) {
					object.init();
					object.start();
				}
			}
		});
		panel.add(button_1);
		button_1.setText("韩文识别");
		
		DButton button_2 = new DButton((String) null, (Color) null);
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ModelInterface object = (ModelInterface) DataSave.loader.loadClassToObject("com.mugui.ew.EWUIHandel");
				if (object != null) {
					object.init();
					object.start();
				}
			}
		});
		button_2.setText("额外功能");
		panel.add(button_2);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845712768948928682L;
	private Dimension now_Dimension = new Dimension(570, 330);
	private Point now_point = null;
	private EWtoDirectionContainerPanel directionCheckContainer = null;
	private File directionSaveFile = null;

	@Override
	public void init() {
		DataSave.StaticUI.setAlwaysOnTop(false);
		if (now_point != null)
			DataSave.StaticUI.setLocation(now_point);
		DataSave.StaticUI.setSize(now_Dimension);
		DataSave.StaticUI.updateTitle("黑色沙漠咸鱼辅助(其他功能)");
		if (directionSaveFile == null) {
			directionSaveFile = new File(DataSave.JARFILEPATH + "/data/EWToDirection.Object");
		}
		if (!directionSaveFile.isFile()) {
			return;
		}
		remove(directionCheckContainer);
		Object component = SerializeTool.deserializeFileTo(directionSaveFile);
		if (component instanceof EWtoDirectionContainerPanel) {
			directionCheckContainer = (EWtoDirectionContainerPanel) component;
		}
		add(directionCheckContainer);
		directionCheckContainer.init();
		// System.out.println("初始化:" + directionCheckContainer);
		updateUI();
		repaint();
		// 注册一个监听事件
		JIntellitype.getInstance().registerHotKey(99, "f10");
		JIntellitype.getInstance().registerHotKey(101, "f11");
		JIntellitype.getInstance().addHotKeyListener(hotkeyListener);

	}

	public boolean isDirectionT() {
		return directionCheckContainer.isDirectionT();
	}
	private HotkeyListener hotkeyListener = new HotkeyListener() {
		@Override
		public void onHotKey(int arg0) {
			if (arg0 == 99) {
				EWHandle.start("");
			} else if (arg0 == 101) {
				EWHandle.stop();
				directionCheckContainer.quit();
			}
		}
	};
	@Override
	public void quit() {
		now_point = DataSave.StaticUI.getLocation();
		now_Dimension = DataSave.StaticUI.getSize();
		if (directionSaveFile != null)
			SerializeTool.serizlizeToFile(directionCheckContainer, directionSaveFile);
		directionCheckContainer.quit();
		JIntellitype.getInstance().removeHotKeyListener(hotkeyListener);
	}

	public EWtoDirectionCheckBox getDirectionCheckBoxCoordinate() {
		EWtoDirectionCheckBox checkBox = directionCheckContainer.getCheckBox();
		if (checkBox == null)
			return new EWtoDirectionCheckBox(new DPoint2D(0, 0));
		return checkBox;
	}

	public void setDirectionT(boolean b) {
		directionCheckContainer.setDirectionT(b);
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
