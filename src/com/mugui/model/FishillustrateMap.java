package com.mugui.model;

import java.io.File;
import java.io.IOException;
import com.mugui.ui.DataSave;

public class FishillustrateMap {
	public final static FishillustrateMap INSTANCE = new FishillustrateMap();

	//private ConcurrentHashMap<Integer, DimgFile> fishMap = new ConcurrentHashMap<>();

	public FishillustrateMap() {
		File file = new File(DataSave.JARFILEPATH + "/鱼图鉴");
		if (!file.isDirectory()) {
			file.exists();
		}
		if (!file.isDirectory()) {
			try {
				throw new IOException("空的目录：" + file.getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
