package com.mugui.ui.part;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import com.mugui.Dui.DPanel;
import com.mugui.http.Bean.JGOtherBean;
import com.mugui.ui.DataSave;

public class JGList extends DPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2126561575134197187L;

	public JGList() {

	}

	private HashMap<String, JGOtherPanel> list = new HashMap<String, JGOtherPanel>();
	public static final String LJ = "料理";
	public static final String JG = "加工";
	public String model = "加工";

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void addJGOtherPanelAll(HashMap<Integer, JGOtherPanel> list) {
		Iterator<Entry<Integer, JGOtherPanel>> iterator = list.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, JGOtherPanel> jgOtherPanel = iterator.next();
			addJGOtherPanel(jgOtherPanel.getValue());
		}
	}

	private int u_i = 1;

	public void addJGOtherPanel(JGOtherPanel jgOtherPanel) {
		this.add(jgOtherPanel);
		if (jgOtherPanel.getNumber() == null) {
			jgOtherPanel.setNumber("" + (u_i++));
		}
		list.put(jgOtherPanel.getNumber(), jgOtherPanel);
		if (list.size() != 0)
			this.setPreferredSize(new Dimension(280, (list.values().iterator().next().getHeight() + 5) * list.size()));
	}

	private JGOtherPanel nowJGOther = null;

	public JGOtherPanel getNowJGOther() {
		return nowJGOther;
	}

	public void setNowOther(JGOtherPanel jgOtherPanel) {
		nowJGOther = jgOtherPanel;
	}

	@Override
	public void init() {
		removeAll();
		list.clear();
		u_i = 1;
		File f = new File(DataSave.JARFILEPATH + "/" + model + "模式/save");
		if (!f.exists()) {
			return;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f));
			String s = null;
			while ((s = reader.readLine()) != null) {
				if (!s.trim().equals("")) {
					JGOtherPanel panel = new JGOtherPanel(model);
					panel.setJGModel(s);
					s = reader.readLine();
					if (s == null)
						return;
					if (s.trim().equals(""))
						continue;
					JGOtherBean bean = new JGOtherBean();
					bean.setBody(s);
					panel.DataInit(bean);
					addJGOtherPanel(panel);
				}
			}
		} catch (IOException e) {
			// TODO: handle exception
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void quit() {
		// TODO 自动生成的方法存根

	}

	public JGOtherPanel[] getJGOtherLIst() {
		Collection<JGOtherPanel> collection = list.values();
		JGOtherPanel[] otherPanels = new JGOtherPanel[collection.size()];
		int j = 0;
		Iterator<JGOtherPanel> iterator = collection.iterator();
		while (iterator.hasNext()) {
			otherPanels[j++] = iterator.next();
		}
		return otherPanels;
	}

	public void removeQPOtherPanel(JGOtherPanel jgOtherPanel) {
		list.remove(jgOtherPanel.getNumber());
		remove(jgOtherPanel);
		if (list.size() != 0)
			setPreferredSize(new Dimension(280, (list.values().iterator().next().getHeight() + 5) * list.size()));
		validate();
		repaint();
	}

	public void saveJGOther() {
		JGOtherPanel[] otherPanels = getJGOtherLIst();
		File f = new File(DataSave.JARFILEPATH + "/" + model + "模式/");
		if (!f.isDirectory()) {
			f.mkdir();
		}
		f = new File(f.getPath() + "/save");
		if (f.isFile()) {
			f.delete();
		}
		BufferedWriter writer = null;
		try {
			f.createNewFile();
			writer = new BufferedWriter(new FileWriter(f));
			for (JGOtherPanel otherPanel : otherPanels) {
				writer.write(otherPanel.getJGModel());
				writer.newLine();
				writer.write(otherPanel.getJGOtherBean().toString());
				writer.newLine();
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块 
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
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
