package com.mugui.ew;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.DownloadItem;
import com.teamdev.jxbrowser.chromium.events.DownloadEvent;
import com.teamdev.jxbrowser.chromium.events.DownloadListener;

public class DDownloadItem implements DownloadItem {

	private File file = null;
	private OutputStream outputStream = null;
	private Browser browser = null;

	public DDownloadItem(Browser browser) {
		this.browser = browser;
	}

	private DownloadListener downloadlistener = null;

	@Override
	public void addDownloadListener(DownloadListener var1) {
		this.downloadlistener = var1;
	}

	@Override
	public void removeDownloadListener(DownloadListener var1) {
		downloadlistener = null;
	}

	@Override
	public List<DownloadListener> getDownloadListeners() {
		return null;
	}

	@Override
	public String getURL() {
		return (browser != null && !browser.isDisposed()) ? browser.getURL() : null;
	}

	@Override
	public Browser getBrowser() {
		return browser;
	}

	private boolean paused_bool = false;

	@Override
	public boolean isPaused() {
		return paused_bool;
	}

	private boolean canceled_bool = false;

	@Override
	public boolean isCanceled() {
		return canceled_bool;
	}

	private boolean completed_bool = false;

	@Override
	public boolean isCompleted() {
		return completed_bool;
	}

	@Override
	public String getMimeType() {
		return null;
	}

	@Override
	public int getID() {
		return 0;
	}

	private int percentComplete = 0;

	@Override
	public int getPercentComplete() {
		return percentComplete;
	}

	public void setPercentComplete(int var) {
		percentComplete = var;
	}

	@Override
	public long getTotalBytes() {
		return 0;
	}

	private long recived = 0;

	@Override
	public long getReceivedBytes() {
		return recived;
	}

	@Override
	public File getDestinationFile() {
		return file;
	}

	@Override
	public void setDestinationFile(File var1) {
		if (var1 == null)
			return;
		if (file != null) {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		file = var1;

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void cancel() {
		canceled_bool = true;
		browser.dispose();
		timer.cancel();
		try {
			if (outputStream != null) {
				outputStream.close();
			}
			file.delete();
			downloadlistener.onDownloadUpdated(new DownloadEvent(this));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private long time = 0;
	private Timer timer = null;
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			downloadlistener.onDownloadUpdated(new DownloadEvent(DDownloadItem.this));

		}
	};

	public void save(byte[] data) {
		if (outputStream == null)
			try {
				outputStream = new FileOutputStream(file);
				if (timer == null)
					timer = new Timer();
				timer.schedule(task, 1000, 1000);
				time = System.currentTimeMillis();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		recived += data.length;
		try {
			outputStream.write(data);
		} catch (IOException e) {

		}
	}

	@Override
	public long getCurrentSpeed() {
		long s = (System.currentTimeMillis() - time) / 1000;
		if (s == 0)
			return 0;
		return recived / s;
	}

	public void finash() {
		setPercentComplete(100);
		downloadlistener.onDownloadUpdated(new DownloadEvent(this));
		completed_bool = true;
		timer.cancel(); 
		downloadlistener.onDownloadUpdated(new DownloadEvent(this));

		try {
			if (outputStream != null) {
				outputStream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
