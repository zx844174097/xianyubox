package com.mugui.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.NullArgumentException;

import net.sourceforge.tess4j.Tesseract2;

import com.mugui.http.Bean.FishBean;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;
import com.mugui.windows.Tool;

public class FishPrice {
	static int num = 0;
	private static Tool tool = new Tool();
	public static ConcurrentHashMap<Integer, YuAllBody> allbody = new ConcurrentHashMap<Integer, FishPrice.YuAllBody>();
	public static Tesseract2 instance = null;
	static {
		instance = new Tesseract2();
	}

	public static void addLineBody(int line_index, BufferedImage yutu, int index, boolean yU_TYPE) {
		allbody.get(line_index).addYuBody(yutu, index, yU_TYPE);
	}

	public static class XianluBody {
		public BufferedImage yuan = null;
		public BufferedImage te = null;
		public long time = 0;
		public long bold_time = 0;

		public XianluBody(BufferedImage yuan) {
			reSet(yuan);
		}

		public void reSet(BufferedImage image) {
			this.yuan = image;
			try {
				image = ImgTool.cutImage(image, 6, 0, image.getWidth() - 12, image.getHeight());
				te = tool.得到图的特征图(image, 0.12, "61CDB0");
			} catch (Exception e) {
				tool.保存图片(this.yuan, Other.getShortUuid() + ".bmp");
				te = tool.得到图的特征图(this.yuan, 0.12, "61CDB0");
			}
		}
	}

	public static class YuAllBody {
		public XianluBody xianluBody = null;
		public ConcurrentHashMap<Integer, YuBody> body = new ConcurrentHashMap<Integer, FishPrice.YuBody>();

		public YuAllBody(XianluBody temp_img) {
			xianluBody = temp_img;
		}

		public YuAllBody(BufferedImage xianlu) {
			xianluBody = new XianluBody(xianlu);
		}

		public void addYuBody(BufferedImage yutu, int i, boolean yU_TYPE) {
			YuBody lin = new YuBody(yutu, yU_TYPE);
			if (body.get(i) == null) {
				body.put(i, lin);
				return;
			}
			YuBody lin2 = body.get(i);
			if (tool.图中找图(lin2.bean.getFish_img(), lin.bean.getFish_img(), 0.07, 0, 0) != null) {
				lin2.bean.setFish_price(lin.bean.getFish_price());
				return;
			}
			Iterator<Entry<Integer, YuBody>> iterator = body.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, YuBody> temp = iterator.next();
				if (tool.图中找图(temp.getValue().bean.getFish_img(), lin.bean.getFish_img(), 0.07, 0, 0) != null) {
					temp.getValue().bean.setFish_price(lin.bean.getFish_price());
					return;
				}
			}
			body.put(i, lin);
		}

		public void addYuBody(FishBean yutu, int i) {
			YuBody lin = new YuBody(yutu);
			if (body.get(i) == null) {
				body.put(i, lin);
				return;
			}
			YuBody lin2 = body.get(i);
			if (tool.图中找图(lin2.bean.getFish_img(), lin.bean.getFish_img(), 0.07, 0, 0) != null) {
				lin2.bean = yutu;
				return;
			}
			Iterator<Entry<Integer, YuBody>> iterator = body.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Integer, YuBody> temp = iterator.next();
				if (tool.图中找图(temp.getValue().bean.getFish_img(), lin.bean.getFish_img(), 0.07, 0, 0) != null) {
					temp.getValue().bean = yutu;
					return;
				}
			}
			body.put(i, lin);
		}
	}

	public static class YuBody {
		public FishBean bean = null;

		public YuBody(BufferedImage yuan, boolean yU_TYPE) {
			bean = new FishBean();
			BufferedImage fish_name_img = ImgTool.cutImage(yuan, 46, 4, 90, 20);
			if (yU_TYPE) {
				fish_name_img = ImgTool.imageInverse(fish_name_img);
			}
			bean.setFish_name_img(fish_name_img);
			bean.setFish_img(ImgTool.cutImage(yuan, 0, 0, 46, 46));
			yuan = ImgTool.cutImage(yuan, 100, 30, yuan.getWidth() - 100, 14);
			yuan = tool.得到图的特征图(yuan, 0.3, "2D9F53");
			yuan = ImgTool.grayscaleImage(yuan);
			// tool.保存图片(yuan, (num) + "q.bmp");
			BufferedImage yuan2 = ImgTool.binaryzationImage(yuan);
			yuan = ImgTool.clearAdhesion(yuan2);
			yuan = ImgTool.imageEnlarge(yuan, 6);
			String yujia;
			try {
				yujia = instance.doOCR(yuan).trim();
				if (Other.isInteger(yujia)) {
					bean.setFish_price(Integer.parseInt(yujia));
					if (bean.getFish_price() > 150 || bean.getFish_price() < 70) {
						tool.保存图片(yuan2, (num++) + "_" + yujia + ".bmp");
					}
				} else {
					bean.setFish_price(0);
					tool.保存图片(yuan2, (num++) + "_" + yujia + ".bmp");
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			// tool.保存图片(yutu, (num) + "yutu" + ".bmp");
		}

		public YuBody(FishBean yutu) {
			bean = yutu;
		}
	}

	public static byte[] getLineAllBody(int d_index2) {
		YuAllBody temp = allbody.get(d_index2);
		if (temp == null) {
			throw new NullArgumentException("该线路不存在：" + d_index2);
		}
		Iterator<Entry<Integer, YuBody>> iterator = temp.body.entrySet().iterator();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(out);
		try {
			byte b[] = null;
			while (iterator.hasNext()) {
				Entry<Integer, YuBody> entry = iterator.next();
				dataOutputStream.writeInt(entry.getKey());
				YuBody body = entry.getValue();
				// if (body.bean.getFish_price() < 70 ||
				// body.bean.getFish_price() > 150) {
				// throw new NullArgumentException("无法解析的鱼价格，无法上传至服务器作为参考");
				// }r
				dataOutputStream.writeInt((int) body.bean.getFish_price());
				if (body.bean.getFish_name() == null) {
					body.bean.setFish_name(" ");
				}
				b = body.bean.getFish_name().getBytes(Charset.forName("UTF-8"));
				dataOutputStream.writeInt(b.length);
				dataOutputStream.write(b);
				b = ImgTool.ImgToByteArray(body.bean.getFish_name_img());
				dataOutputStream.writeInt(b.length);
				dataOutputStream.write(b);
				b = ImgTool.ImgToByteArray(body.bean.getFish_img());
				dataOutputStream.writeInt(b.length);
				dataOutputStream.write(b);
			}
			return out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (dataOutputStream != null)
					dataOutputStream.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 重新设置一个线路的ui
	public static YuAllBody addLineFeature(int key, BufferedImage image) {
		if (image == null)
			return null;
		YuAllBody yuAllBody = allbody.get(key);
		if (yuAllBody == null) {
			yuAllBody = new YuAllBody(image);
			allbody.put(key, yuAllBody);
			return yuAllBody;
		}
		yuAllBody.xianluBody.reSet(image);
		return yuAllBody;
	}

	/**
	 * 设置一条线路的鱼
	 * 
	 * @param d_index2
	 * @param linkedList
	 */
	public static void setLineAllYuBody(int d_index2, LinkedList<BufferedImage> linkedList) {
		allbody.get(d_index2).body.clear();
		Iterator<BufferedImage> lIterator = linkedList.iterator();
		int n = 0;
		boolean YU_TYPE = false;
		BufferedImage tempLin = null;
		while (lIterator.hasNext()) {
			BufferedImage temp_buf = lIterator.next();
			if (temp_buf.getWidth() == 1) {
				YU_TYPE = true;
				continue;
			}
			if (tempLin != null && tool.图中找图(tempLin, temp_buf, 0.03, 0, 0) != null) {
				continue;
			}
			tempLin = temp_buf;
			addLineBody(d_index2, temp_buf, n++, YU_TYPE);
		}
	}

	public static void addAllBody(int line_index, FishBean yutu, int index) {
		// tool.保存图片(yutu, index + "yu.bmp");
		if (allbody.get(line_index) == null) {
			throw new NullArgumentException("该调线路未被建立失败");
		}
		allbody.get(line_index).addYuBody(yutu, index);
	}

	public static void setLineTime(int key, long time) {
		allbody.get(key).xianluBody.time = time;
	}

	public static void clearLineAllBody(int line_index) {
		allbody.get(line_index).body.clear();
	}

	public static void updateBoldTime(int parseInt, long parseLong) {
		YuAllBody body = allbody.get(parseInt);
		if (body == null)
			return;
		body.xianluBody.bold_time = parseLong;
	}

	public static LinkedList<FishBean> getBoldAll() {
		Iterator<Entry<Integer, YuAllBody>> iterator = allbody.entrySet().iterator();
		LinkedList<FishBean> linkedList = new LinkedList<FishBean>();
		while (iterator.hasNext()) {
			Entry<Integer, YuAllBody> entry = iterator.next();
			if (System.currentTimeMillis() - entry.getValue().xianluBody.bold_time < 60 * 60 * 1000) {
				FishBean bean = new FishBean();
				bean.setFish_name_img(entry.getValue().xianluBody.yuan);
				bean.setFish_price(entry.getValue().xianluBody.bold_time);
				linkedList.add(bean);
			}
		}
		return linkedList;
	}

	public static Entry<Integer, YuAllBody> getYuAllBodyOne(BufferedImage te) {
		XianluBody temp_img = new XianluBody(te);
		Iterator<Entry<Integer, YuAllBody>> body = allbody.entrySet().iterator();
		while (body.hasNext()) {
			Entry<Integer, YuAllBody> temp_body = body.next();
			if (null != tool.图中找图EX(temp_body.getValue().xianluBody.yuan, temp_img.te, 0.015d, 0, 0)) {
				return temp_body;
			}
		}
		return null;
	}

	public static int getYuAllBodyOneKey(BufferedImage te) {
		XianluBody temp_img = new XianluBody(te);
		Iterator<Entry<Integer, YuAllBody>> body = allbody.entrySet().iterator();
		while (body.hasNext()) {
			Entry<Integer, YuAllBody> temp_body = body.next();
			if (null != tool.图中找图EX(temp_body.getValue().xianluBody.yuan, temp_img.te, 0.015d, 0, 0)) {
				return temp_body.getKey();
			}
		}
		return -1;
	}
}
