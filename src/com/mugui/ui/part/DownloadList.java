package com.mugui.ui.part;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.mugui.MAIN;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.DPanel;
import com.mugui.Dui.DVerticalFlowLayout;
import com.mugui.http.Bean.FileBean;
import com.mugui.http.Bean.JsonBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.CmdModel;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.info.DownloadPanel;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JPanel;

public class DownloadList extends DPanel {
	private ConcurrentHashMap<String, DownloadPanel> linkedList = new ConcurrentHashMap<String, DownloadPanel>();

	public DownloadList() {
		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		body = new JPanel();
		scrollPane.setViewportView(body);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		body.setLayout(new DVerticalFlowLayout(DVerticalFlowLayout.TOP, 5, 5, true, false));
	}

	private JPanel body;

	public void addDownloadPanelList(LinkedList<DownloadPanel> list) {
		Iterator<DownloadPanel> iterator = list.iterator();
		while (iterator.hasNext()) {
			addDownloadPanel(iterator.next());
		}
	}

	int len = 0;

	public void addDownloadPanel(DownloadPanel panel) {
		if (panel.getBean() != null && panel.getBean().getOther_description().startsWith("App")) {
			len++;
		}
		linkedList.put(panel.getBean().getFile_name(), panel);
		body.add(panel);
		panel.sendDown();
		validate();
		repaint();
	}

	public void removeDownloadPanel(DownloadPanel panel) {
		linkedList.remove(panel.getBean().getFile_name());
		panel.setState(3);
		body.remove(panel);
		validate();
		repaint();
	}

	public void removeDownloadPanelAll() {
		Iterator<DownloadPanel> iterator = linkedList.values().iterator();
		while (iterator.hasNext()) {
			DownloadPanel d = iterator.next();
			iterator.remove();
			d.setState(3);

		}
		linkedList.clear();
		body.removeAll();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6135639823551859476L;

	@Override
	public void init() {
	}

	@Override
	public void quit() {

	}

	public void File_(TcpBag accpet) {
		FileBean fileBean = JsonBean.newInstanceBean(FileBean.class, accpet.getBody_description());
		if (fileBean == null)
			return;
		if (accpet.getBody() == null)
			return;
		// Iterator<Entry<String, DownloadPanel>> iterator =
		// linkedList.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Entry<String, DownloadPanel> entry = iterator.next();
		// System.out.println(entry.getValue() + " " + entry.getKey());
		// }
		linkedList.get(fileBean.getFile_name()).File_page((byte[]) accpet.getBody(), fileBean);

	}

	LinkedList<File> files = new LinkedList<File>();

	public void downState(DownloadPanel panel) {
		if (panel.getBean().getOther_description().split(":")[0].trim().equals("App")) {
			if (panel.getState() == 2) {
				removeDownloadPanelAll();
				DInputDialog dialog = new DInputDialog("警告", "辅助更新文件下载失败，请手动重启辅助", true, false);
				UIModel.setUI(dialog);
				dialog.start();
				Other.sleep(500);
				MAIN.exit();
			} else if (panel.getState() == 3) {
				// DInputDialog dialog = new DInputDialog("信息", "更新成功,稍后将自动重启",
				// true, false);
				len--;
				files.add(panel.getDownLoadFile());
				if (len == 0) {
					// App下载完成
					String s = "";
					for (File ff : files) {
						s += "\"" + ff.getName() + "\" ";
					}
					String path = "\"" + DataSave.JARFILEPATH + "/jre/bin/java.exe\" -jar \"" + DataSave.JARFILEPATH + "/PIC2.dll\" " + s;
					CmdModel.run(path);
					// UIModel.setUI(dialog);
					// dialog.start();
					// Other.sleep(500);
					MAIN.exit();
				}
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
