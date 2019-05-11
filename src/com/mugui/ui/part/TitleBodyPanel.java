package com.mugui.ui.part;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mugui.Dui.DButton;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DimgDpanel;
import com.mugui.Dui.DimgFile;
import com.mugui.windows.Tool;
import java.awt.BorderLayout;

public class TitleBodyPanel extends DPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2596479905893975750L;
	private Tool tool = null;
	private String filePath = null;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
		init();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public TitleBodyPanel(String filePath, String title) {
		this(filePath, title, null);
	}

	public TitleBodyPanel(String filePath, String title, TitleInfoPanel otherSetPanel2) {
		this.filePath = filePath;
		this.otherSetPanel = otherSetPanel2;
		setLayout(new BorderLayout(0, 0));
		scrollPane = new JScrollPane();
		add(scrollPane);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		bodyPanel = new JPanel();
		scrollPane.setViewportView(bodyPanel);
		bodyPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		titleButton = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) titleButton.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		scrollPane.setColumnHeaderView(titleButton);

		ceshi = new DButton((String) null, (Color) null);
		ceshi.setFont(new Font("Dialog", Font.PLAIN, 13));
		ceshi.setText("测试");
		titleButton.add(ceshi);

		tianjia = new DButton((String) null, (Color) null);
		tianjia.setFont(new Font("Dialog", Font.PLAIN, 13));
		tianjia.setText("删除");
		titleButton.add(tianjia);
		xinzheng = new DButton("新增", (Color) null);
		xinzheng.setFont(new Font("Dialog", Font.PLAIN, 13));
		titleButton.add(xinzheng);
		label = new JLabel(title);
		label.setFont(new Font("宋体", Font.BOLD, 18));
		titleButton.add(label);
	}

	private TitleInfoPanel otherSetPanel = null;
	private JScrollPane scrollPane = null;
	private JPanel titleButton = null;
	private JLabel label = null;

	public void setBoundsSize(int weith, int height) {
		// this.setPreferredSize(new Dimension(weith, height));
		scrollPane.setPreferredSize(new Dimension(weith, height));
		bodyPanel.setPreferredSize(new Dimension(weith, 2046));
	}

	public void addButton(DButton dButton) {
		titleButton.remove(label);
		titleButton.add(dButton);
		titleButton.add(label);
		titleButton.validate();
		titleButton.repaint();
	}

	private DButton xinzheng = null;
	private DButton ceshi = null;
	private DButton tianjia = null;

	public DButton getXinzheng() {
		return xinzheng;
	}

	public void setXinzheng(DButton xinzheng) {
		this.xinzheng = xinzheng;
	}

	public DButton getCeshi() {
		return ceshi;
	}

	public void setCeshi(DButton ceshi) {
		this.ceshi = ceshi;
	}

	public DButton getTianjia() {
		return tianjia;
	}

	public void setTianjia(DButton tianjia) {
		this.tianjia = tianjia;
	}

	public void delXinzheng() {
		titleButton.remove(xinzheng);
	}

	public void addPutActionListener(PutActionListener listener) {
		// TODO 自动生成的方法存根
		xinzheng.addActionListener(listener);
	}

	public void addRemoveActionListener(RemoveActionListener listener) {
		// TODO 自动生成的方法存根
		tianjia.addActionListener(listener);
	}

	public void addTestActionListener(TestActionListener listener) {
		// TODO 自动生成的方法存根
		ceshi.addActionListener(listener);
	}

	public String getPath() {
		return filePath;
	}

	public File getImageFile() {
		if (nowDimg == null)
			return null;
		return nowDimg.getImageFile();
	}

	@Override
	public void init() {
		if (tool == null)
			tool = new Tool();
		bodyPanel.removeAll();
		setImgFiles();
		validate();
		repaint();
	}

	private DimgDpanel imFiles[] = null;
	private JPanel bodyPanel = null;
	private DimgFile[] dimgFiles = null;

	public void setImgFiles() {
		File f = new File(filePath);
		if (f.exists()) {
			File TuFile[] = f.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.isFile();
				}
			});
			Arrays.sort(TuFile, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					if (o1.lastModified() > o2.lastModified()) {
						return 1;
					} else if (o1.lastModified() == o2.lastModified()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			if (TuFile == null) {
				return;
			}
			imFiles = new DimgDpanel[TuFile.length];
			dimgFiles = new DimgFile[TuFile.length];
			for (int i = 0; i < TuFile.length; i++) {
				dimgFiles[i] = new DimgFile();
				dimgFiles[i] = DimgFile.getImgFile(TuFile[i]);
				imFiles[i] = new DimgDpanel(dimgFiles[i]);
				imFiles[i].addMouseListener(l);
				bodyPanel.add(imFiles[i]);
			}
		} else {
			f.mkdirs();
		}

	}

	public void addImgFile() {
		bodyPanel.removeAll();
		setImgFiles();
		bodyPanel.validate();
		bodyPanel.repaint();
	}

	public void deleteImgFile() {
		if (nowDimg == null)
			return;
		nowDimg.removeFile();
		bodyPanel.removeAll();
		setImgFiles();
		bodyPanel.validate();
		bodyPanel.repaint();
	}

	@Override
	public void quit() {

	}

	public DimgDpanel nowDimg = null;
	private MouseListener l = new MouseListener() {

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (nowDimg != e.getSource())
				((DimgDpanel) e.getSource()).setBackground(null);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (nowDimg != e.getSource())
				((DimgDpanel) e.getSource()).setBackground(Color.DARK_GRAY);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (nowDimg != null)
				nowDimg.setBackground(null);
			nowDimg = (DimgDpanel) e.getSource();
			nowDimg.setBackground(Color.red);
			if (otherSetPanel != null)
				otherSetPanel.datainit(TitleBodyPanel.this);
		}
	};

	public DimgFile[] getDimgAll() {
		return dimgFiles;
	}

	public int getDeaultOne() {
		for (int i = 0; i < dimgFiles.length; i++) {
			if (imFiles[i] == nowDimg) {
				return i;
			}
		}
		return 0;
	}

	public BufferedImage[] getImages() {
		BufferedImage[] buffered = new BufferedImage[dimgFiles.length];
		for (int i = 0; i < dimgFiles.length; i++) {
			buffered[i] = dimgFiles[i].bufferedImage;
		}
		return buffered;
	}

	public DimgFile[] getLikeObjectTypes(String levelObject) {
		LinkedList<DimgFile> linkedList = new LinkedList<DimgFile>();
		for (int i = 0; i < dimgFiles.length; i++) {
			if (dimgFiles[i].objecttype.equals(levelObject)) {
				linkedList.add(dimgFiles[i]);
			}
		}
		return linkedList.toArray(new DimgFile[linkedList.size()]);
	}

	public DimgFile[] getLikeLevelTypes(String levelObject) {
		LinkedList<DimgFile> linkedList = new LinkedList<DimgFile>();
		for (int i = 0; i < dimgFiles.length; i++) {
			if (dimgFiles[i].objectlevel.equals(levelObject)) {
				linkedList.add(dimgFiles[i]);
			}
		}
		return linkedList.toArray(new DimgFile[linkedList.size()]);
	}

	// test
	public abstract class TestActionListener implements ActionListener {

		public TitleBodyPanel getThis() {
			return TitleBodyPanel.this;
		}
	}

	// remove
	public abstract class RemoveActionListener implements ActionListener {

		public TitleBodyPanel getThis() {
			return TitleBodyPanel.this;
		}
	}

	// put
	public abstract class PutActionListener implements ActionListener {
		public TitleBodyPanel getThis() {
			return TitleBodyPanel.this;
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