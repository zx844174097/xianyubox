package net.sourceforge.tess4j;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.mugui.Dui.DimgFile;
import com.mugui.network.model.ImageModel;
import com.mugui.network.neuralnetwork.Network;
import com.mugui.tool.FileTool;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

public class Tesseract2 {
	private static boolean bool = false;

	public static void initData() {
		if (DataSave.低配模式)
			return;
		if (network == null) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					bool = false;
					new Tesseract2();
					bool = true;
				}
			}).start();
		}
	}

	private static Network network = null;

	public Tesseract2() {
		if (DataSave.低配模式)
			return;
		if (network == null) {
			network = new Network(new int[] { 28 * 28, 30, 10 });
			init();
			bool = true;
		}
		long time = System.currentTimeMillis();
		while (!bool && System.currentTimeMillis() - time < 5 * 60 * 1000) {
			Other.sleep(1000);
		}
		if (!bool) {
			throw new NullPointerException("价格识别驱动启动时间超时，稍等一会吧");
		}

	}

	/**
	 * 初始化
	 */
	private void init() {
		// 这里初始化
		// 解压文件到指定目录
		File file = new File(DataSave.JARFILEPATH + "/tessdata2.dll");
		File pathFile = new File(System.getProperty("java.io.tmpdir") + "/temp");
		if (!file.isFile()) {
			throw new NullPointerException("严重错误，辅助文件缺失，请重新下载辅助." + file.getPath());
		}
		FileTool.deleteAllFilesOfDir(pathFile);
		if (!pathFile.isDirectory()) {
			pathFile.mkdirs();
		}
		if (!Other.DZipInput(file, pathFile)) {
			throw new NullPointerException("严重错误，辅助文件不完整，请重新下载辅助.");
		}
		List<ImageModel> modelList = getImageModel(pathFile);
		network.SGD(modelList, 10000, 0.55);
	}

	public String doOCR(BufferedImage image) {
		if (DataSave.低配模式)
			return "";
		String body = "";
		// 提取中间的单个字符
		int height = image.getHeight();
		int width = image.getWidth();
		int color = image.getRGB(0, 0);
		Point p = new Point(-1, -1);
		Point p2 = new Point();
		int mode = 0;
		boolean bool = false;
		for (int n = 0; n < width; n++) {
			bool = false;
			for (int m = 0; m < height; m++) {
				if (mode == 0 && image.getRGB(n, m) != color) {
					p.x = n;
					p.y = m;
					p2.x = n;
					p2.y = m;
					mode = 1;
					bool = true;
				} else if (mode == 1 && image.getRGB(n, m) != color) {
					if (n < p.x)
						p.x = n;
					if (m < p.y)
						p.y = m;
					if (n > p2.x)
						p2.x = n;
					if (m > p2.y)
						p2.y = m;
					bool = true;
				}
			}
			if (!bool && p.x != -1) {
				if (p2.y - p.y > 48) {
					BufferedImage bufferedImage;
					bufferedImage = ImgTool.cutImage(image, p.x, p.y, p2.x - p.x + 1, p2.y - p.y + 1);
					bufferedImage = ImgTool.imageStretching(bufferedImage, 28, 28);
					int digit = network.predict(ImgTool.imageToDoubleArrays(bufferedImage));
					DimgFile dimgFile = new DimgFile();
					dimgFile.file = new File(DataSave.JARFILEPATH + "/temp/" + Other.getShortUuid() + ".bmp");
					dimgFile.objectname = digit + "";
					dimgFile.bufferedImage = bufferedImage;
					dimgFile.saveAllData();
					// new Tool().保存图片(bufferedImage,
					// Other.getShortUuid()+".bmp");
					if (digit != -1)
						body += digit;
				}
				p.x = -1;
				p.y = -1;
				mode = 0;
			}
		}

		return body;
	}

	/**
	 * 加载一个目录下的初始化文件
	 * 
	 * @param file
	 * @return
	 */
	private List<ImageModel> getImageModel(File file) {
		List<ImageModel> list = new LinkedList<ImageModel>();
		if (!file.isDirectory()) {
			throw new NullPointerException("这不是一个文件夹");
		}
		String items[] = file.list();
		for (String item : items) {
			File temp = new File(file.getPath() + "\\" + item);
			DimgFile file2 = DimgFile.getImgFile(temp);
			if(file2.bufferedImage==null ) continue;
			BufferedImage bimage = file2.bufferedImage;
			int smallHeight = bimage.getHeight();
			int smallWidth = bimage.getWidth();

			bimage = new BufferedImage(smallWidth, smallHeight, BufferedImage.TYPE_INT_RGB);
			bimage.getGraphics().drawImage(file2.bufferedImage, 0, 0, null);
			int[] pixes = new int[smallHeight * smallWidth];
			double[] grayMatrix = new double[smallWidth * smallHeight];
			int index = -1;
			pixes = (int[]) bimage.getRaster().getDataElements(0, 0, smallWidth, smallHeight, pixes);
			for (int i = 0; i < smallWidth; i++) {
				for (int j = 0; j < smallHeight; j++) {
					int rgb = pixes[i * smallWidth + j];
					int r = (rgb & 0xff0000) >> 16;
					int g = (rgb & 0xff00) >> 8;
					int b = (rgb & 0xff);
					double gray = Double.valueOf(r * 299 + g * 587 + b * 114 + 500) / 255000.0;
					grayMatrix[++index] = gray;
				}
			}
			Integer digit = 0;
			if (Other.isInteger(file2.objectname))
				digit = Integer.parseInt(file2.objectname);
			else
				digit = 10;
			// System.out.println(digit);
			ImageModel curModel = new ImageModel(grayMatrix, digit);
			list.add(curModel);
		}
		return list;
	}

	public static boolean isInit() {
		
		return bool;
	}

}
