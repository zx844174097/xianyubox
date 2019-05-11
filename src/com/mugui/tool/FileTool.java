package com.mugui.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileTool {
	public static final boolean renameFileTo(File nowfile, File newfile) {
		FileInputStream fis = null;
		FileChannel bos = null;
		FileOutputStream fos = null;
		FileChannel bis = null;
		try {
			fis = new FileInputStream(nowfile);
			bis = fis.getChannel();
			if (!newfile.exists()) {
				newfile.createNewFile();
			}
			fos = new FileOutputStream(newfile);
			bos = fos.getChannel();
			bis.transferTo(0, bis.size(), bos);
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (bos != null)
					bos.close();
				if (bis != null)
					bis.close();
				if (fos != null)
					fos.close();
				if (fis != null)
					fis.close();
			} catch (Exception e) {

			}
		}
		return true;
	}

	public static final File getWindowsPath() {
		return javax.swing.filechooser.FileSystemView.getFileSystemView().getDefaultDirectory();
	}

	public static String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	public static void deleteAllFilesOfDir(File path) {
		if (!path.exists())
			return;
		if (path.isFile()) {
			path.delete();
			return;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFilesOfDir(files[i]);
		}
		path.delete();
	}

	// /**
	// * 清空文件内容
	// *
	// * @param fileName
	// */
	// public static void clearInfoForFile(File file) {
	// try {
	// if (!file.exists()) {
	// file.createNewFile();
	// }
	// FileWriter fileWriter = new FileWriter(file);
	// fileWriter.write("");
	// fileWriter.flush();
	// fileWriter.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	public static void saveDataToFile(byte[] bytes, File file) {
		if (file.getParentFile().isDirectory()) {
			file.getParentFile().mkdirs();
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "rw");
			FileChannel channel = raf.getChannel();
			FileLock lock = channel.tryLock();
			if (lock.isValid()) {
				raf.setLength(bytes.length);
				raf.write(bytes);
			}
			lock.release();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
