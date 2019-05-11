package com.mugui.ew.model;

import com.mugui.ModelInterface;
import com.mugui.Dui.DPanel;
import com.mugui.ew.EWUIHandel;
import com.teamdev.jxbrowser.chromium.DownloadHandler;
import com.teamdev.jxbrowser.chromium.DownloadItem;

public class BrowserDownloadHandler implements ModelInterface, DownloadHandler {

	private boolean istrue = false;
	private DPanel downloadSelector = null;
	private DPanel downloadView = null;

	@Override
	public void init() {
		downloadSelector = EWUIHandel.datasave.getUIManager().get("com.mugui.ew.ui.component.DownloadSelector");
		downloadSelector.init();
		downloadView = EWUIHandel.datasave.getUIManager().get("com.mugui.ew.ui.component.DownloadView");
		downloadView.dataInit();
	}

	@Override
	public void start() {
		if (isrun())
			return;
		init();
		istrue=true;
	}

	@Override
	public boolean isrun() {
		return istrue;
	}

	@Override
	public void stop() {
		istrue = false;
		downloadSelector.quit();
		downloadView.quit();
	}

	/**
	 * 发现一个新的下载
	 */
	@Override
	public boolean allowDownload(DownloadItem var1) {
		var1 = (DownloadItem) downloadSelector.invokeFunction("init", var1);
		if (var1 == null)
			return false;
		return (boolean) downloadView.invokeFunction("addDownloadItem", var1);
	}

}
