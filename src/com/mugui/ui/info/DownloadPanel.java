package com.mugui.ui.info;

import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JProgressBar;

import com.mugui.http.Bean.FileBean;
import com.mugui.http.pack.TcpBag;
import com.mugui.http.pack.UdpBag;
import com.mugui.model.TCPModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DownloadPanel extends JPanel {
	public FileBean bean = null;

	public FileBean getBean() {
		return bean;
	}

	public void setBean(FileBean bean) {
		File file = new File(DataSave.JARFILEPATH + "/temp");
		if (!file.isDirectory()) {
			file.mkdir();
		}
		this.bean = bean;
		label.setText("正在下载：" + this.bean.getFile_name());
	}

	progressBarThread thread = null;
	private static final String UUID = Other.getShortUuid();

	public void sendDown() {
		//System.out.println(bean.toJsonObject());
		// 发送下载某个文件的请求
		size = bean.getFile_page_all_size();
		b = new boolean[bean.getFile_page_all_size()];
		TcpBag udpBag = new TcpBag();
		udpBag.setBag_id(TcpBag.START_DOWNLOAD_FILE);
		udpBag.setBody(bean.toJsonObject());
		udpBag.setBody_description(UUID);
		TCPModel.SendTcpBag(udpBag);
		if (thread == null || !thread.isAlive()) {
			thread = new progressBarThread();
			thread.start();
		}
	}

	private JLabel label = null;
	private JProgressBar progressBar = null;

	public DownloadPanel(FileBean bean) {
		setLayout(new BorderLayout(0, 0));
		label = new JLabel("正在下载：");
		add(label, BorderLayout.NORTH);
		progressBar = new JProgressBar();
		progressBar.setEnabled(false);
		add(progressBar, BorderLayout.CENTER);
		progressBar.setForeground(Color.GRAY);
		progressBar.setStringPainted(true);
		progressBar.setString("资源连接中。。。");
		progressBar.setBackground(Color.WHITE);
		progressBar.setFont(new Font("微软雅黑", Font.BOLD, 15));
		progressBar.setMinimum(0);
		if (bean != null)
			setBean(bean);
	}

	public DownloadPanel() {
		this(null);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3377331771563696060L;
	private boolean[] b = null;
	private File file = null;
	private boolean is = false;
	private int size = 0;
	private int len = 0;
	private int state = 0;// 准备，1,运行,2,终止,3结束

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	static long time = System.currentTimeMillis();
	static long time2 = System.currentTimeMillis();

	private class progressBarThread extends Thread {
		@Override
		public void run() {
			int last_len = 0; 
			while (state != 3) {
				if (last_len == len) {
					
				} else {
					last_len = len;
					time = System.currentTimeMillis();
					time2 = System.currentTimeMillis();
				}
				if (System.currentTimeMillis()-time >= 5000) {
					time = System.currentTimeMillis();
					String s = "";
					for (int i = 0; i < b.length; i++) {
						s += "" + (b[i] ? 1 : 0);
					}
					TcpBag udpBag = new TcpBag();
					udpBag.setBag_id(UdpBag.RE_DOWNLOAD_FILE);
					udpBag.setBody(s);
					udpBag.setBody_description(bean);
					// UDPModel.SendTcpBag(udpBag);
					TCPModel.SendTcpBag(udpBag);
					// System.out.println(udpBag);
					progressBar.setString("重新连接");
				}
				if (System.currentTimeMillis()-time2 >= 3 * 1000 * 60) {
					progressBar.setString("下载失败");
					state = 2;
					DataSave.downloadList.downState(DownloadPanel.this);
					return;
				}
				progressBar.setValue((int) (len / (size * 1.0) * 100));
				if (len == size || progressBar.getValue() == 100) {
					progressBar.setString("下载成功1");
					thread2.isTrue = false;
					while (thread2.isAlive()) {
						Other.sleep(50);
					}
					// 得到文件写出线程结束

					progressBar.setString("下载成功2");
					state = 3;
					DataSave.downloadList.downState(DownloadPanel.this);
					return;
				}
				Other.sleep(40);
			}
		}
	}

	private FileWriteThread thread2 = null;

	private class FileWriteThread extends Thread {
		private RandomAccessFile accessFile = null;

		public FileWriteThread(File file) {
			try {
				if (accessFile == null)
					accessFile = new RandomAccessFile(file, "rw");
				isTrue = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				if (accessFile != null) {
					try {
						accessFile.close();
						accessFile = null;
					} catch (IOException ee) {
						ee.printStackTrace();
					}
				}
			}
		}

		private boolean isTrue = false;

		@Override
		public void run() {
			linBody body = null;
			try {
				while (true) {
					if ((body = linkedList.poll()) != null) {
						accessFile.seek(body.fileBean.getFile_seek());
						accessFile.write(body.body); 
					} else {
						if (!isTrue)
							return;
						try {
							sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (accessFile != null) {
						while ((body = linkedList.poll()) != null) {
							accessFile.seek(body.fileBean.getFile_seek());
							accessFile.write(body.body);
						}
						accessFile.close();
						accessFile = null;
						linkedList.clear();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		ConcurrentLinkedDeque<linBody> linkedList = new ConcurrentLinkedDeque<linBody>();

		public void add(byte[] body, FileBean fileBean) {
			if (accessFile == null)
				return;
			linBody linBody = new linBody(body, fileBean);
			linkedList.add(linBody);
		}

		private class linBody {
			byte[] body;
			FileBean fileBean;

			public linBody(byte[] body, FileBean fileBean) {
				this.body = body;
				this.fileBean = fileBean;
			}
		}
	}

	public void File_page(byte[] body, FileBean fileBean) {
		if (state >= 2)
			return;
		//System.out.println(fileBean.getFile_page_all_size()+" "+fileBean.getFile_page_number());
		synchronized (this) {
			try {
				while (file == null || !file.exists()) {
					if (!is) {
						state = 1;
						is = true;
						progressBar.setString(null);
						progressBar.setStringPainted(true);
						file = new File(DataSave.JARFILEPATH + "/temp/" + fileBean.getFile_name());
						file.createNewFile();
						thread2 = new FileWriteThread(file);
						thread2.start();
						this.notifyAll();
					} else
						this.wait();
				}
				if (b[fileBean.getFile_page_number()]) {
					return;
				}
				
				b[fileBean.getFile_page_number()] = true;
				thread2.add(body, fileBean);
				len++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public File getDownLoadFile() {
		return file;
	}
}
