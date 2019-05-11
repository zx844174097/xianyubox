package com.mugui.Dui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.mugui.tool.BufferedRandomAccessFile;
import com.mugui.ui.DataSave;

public class DimgFile {
	public BufferedImage bufferedImage = null;
	public File file = null;
	public String objecttype = "不区分";
	public String objectcolor = "任意";
	public String objectlevel = "任意";
	public String objecttime = "0-16";
	public String objectPRI = "999";
	public String objectname = "";

	public void saveAllData() {
		if (bufferedImage != null && file != null)
			try {
				if (objectcolor != null && objectlevel != null && objectPRI != null && objecttime != null && objecttype != null) {

					// 创建储存图片二进制流的输出流'
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					// 创建ImageOutputStream流
					ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(baos);
					ImageIO.write(bufferedImage, "bmp", imageOutputStream);
					BufferedRandomAccessFile bRandomAccessFile = new BufferedRandomAccessFile(file, "rw");
					String s = "{";
					s += objecttype + ",";
					s += objectcolor + ",";
					s += objectlevel + ",";
					s += objecttime + ",";
					s += objectPRI + ",";
					s += objectname + "}";
					bRandomAccessFile.seek(0);
					bRandomAccessFile.write(s.getBytes("utf-8"));
					bRandomAccessFile.seek(s.getBytes("utf-8").length);
					bRandomAccessFile.write(baos.toByteArray());
					bRandomAccessFile.close();
					imageOutputStream.close();
					baos.close();
					return;
				}
				ImageIO.write(bufferedImage, "bmp", file);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public static DimgFile getImgFile(String filepath) {
		File file = new File(filepath + DataSave.服务器);
		if (!file.isFile()) {
			file = new File(filepath);
		}
		return getImgFile(new File(filepath));
	}

	public static InputStream getInputString(File file) throws FileNotFoundException {
		InputStream inputStream = null;
		if (file.isFile()) {
			inputStream = new FileInputStream(file);
			return inputStream;
		}
		inputStream = DataSave.loader.getResourceAsStream(file.getName());
		return inputStream;
	}

	public static DimgFile getImgFile(File file2) {
		try {
			InputStream inputStream = getInputString(file2);
			if (inputStream == null)
				return null;
			DataInputStream dataInputStream = null;
			DimgFile dimgFile = new DimgFile();
			dataInputStream = new DataInputStream(inputStream);
			int data = 0;
			if ((data = dataInputStream.read()) == '{') {

				byte[] b = new byte[1024 * 8];
				int bi = 0;
				int i = 0;
				while ((i = dataInputStream.read()) != '}') {
					b[bi++] = (byte) i;
				}
				String s = new String(b, "utf-8").trim();
				String pei[] = s.split(",");
				dimgFile.objecttype = pei[0];
				dimgFile.objectcolor = pei[1];
				dimgFile.objectlevel = pei[2];
				dimgFile.objecttime = pei[3];
				dimgFile.objectPRI = pei[4];
				if (pei.length > 5) {
					dimgFile.objectname = pei[5];
				} else {
					dimgFile.objectname = file2.getName().split("\\.")[0];
				}
				bi += 2;
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if (data != '{')
				bos.write(data);
			byte bbb[] = new byte[1024];
			int sss = 0;
			while ((sss = dataInputStream.read(bbb)) != -1) {
				bos.write(bbb, 0, sss);
			}
			ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(bais);
			dimgFile.bufferedImage = ImageIO.read(imageInputStream);
			if (dimgFile.bufferedImage == null)
				throw new IOException("bufferedImage create bad");
			bais.close();
			dataInputStream.close();
			inputStream.close();
			dimgFile.file = file2;
			return dimgFile;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
