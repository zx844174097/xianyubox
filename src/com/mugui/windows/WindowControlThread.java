package com.mugui.windows;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

import com.mugui.http.Bean.FileBean;
import com.mugui.http.Bean.WindowListenerBean;
import com.mugui.http.pack.Bag;
import com.mugui.http.pack.TcpBag;
import com.mugui.http.tcp.TcpSocketUserBean;
import com.mugui.model.UIModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

public class WindowControlThread extends Thread {
	public static final int LIUNX = 1;
	public static final int WINDOWS = 2;
	public static int SYSTEM_OS = -1;
	static {
		String os = System.getProperties().getProperty("os.name");
		if (!os.startsWith("win") && !os.startsWith("Win")) {
			SYSTEM_OS = LIUNX;
		} else {
			SYSTEM_OS = WINDOWS;
		}
	}

	private static boolean windowDrawIsTrue = false;

	@Override
	public void run() {
		ByteArrayInputStream inputStream = null;
		try {
			windowDrawIsTrue = true;
			while (windowDrawIsTrue) {
				int size = 1024;

				// DrawHandleThread threads[] = new DrawHandleThread[1];
				// for (int i = 0; i < threads.length; i++) {
				// threads[i] = new DrawHandleThread();
				// threads[i].start();
				// }
				DrawHandleThread thread = new DrawHandleThread();
				thread.start();
				System.out.println("初始化");
				TcpBag bag = new TcpBag();
				while (windowDrawIsTrue) {
					boolean bool = false;
					for (int i = 0; i < map.length && windowDrawIsTrue; i++) {
						WindowControlBean windowControlBean = map[i];
						if (windowControlBean == null || !windowControlBean.isTrue()) {
							continue;
						}
						bool = true;
						DrawHandleBean drawHandleBean = windowControlBean.getOrSetBody(null);
						if (drawHandleBean == null)
							continue;

						bag.setBag_id(TcpBag.USER_WINDOWS_IMG);
						inputStream = new ByteArrayInputStream(drawHandleBean.body);
						byte bb[] = new byte[size];
						int len;
						WindowListenerBean userbean = map[i].getUserBean();
						userbean.setModel(drawHandleBean.type);
						userbean.setRate(WindowControlBean.nsn);
						userbean.setTime(System.currentTimeMillis());
						FileBean bean = userbean.getBean();
						if (bean == null) {
							bean = new FileBean();
							userbean.setBean(bean);
						}
						bean.setFile_page_all_size(drawHandleBean.body.length);
						bean.setFile_name(Other.getShortUuid());

						int seek = 0;
						while ((len = inputStream.read(bb)) > 0 && windowDrawIsTrue) {
							bag.setBody(bb);
							bean.setFile_page_number(len);
							bean.setFile_seek(seek);
							seek += len;
							bag.setBody_description(userbean.toJsonObject());
							if (map[i].getTcpSocket().isSocketRun()) {
								map[i].getTcpSocket().sendByteArray((Bag) bag);
							} else {
								map[i].setTrue(false);
							}
						}
						inputStream.close();
						// System.out.println(System.currentTimeMillis() -
						// time);
					}
					if (windowDrawIsTrue)
						windowDrawIsTrue = bool;
				}
				System.out.println("正常停止");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	class DrawHandleThread extends Thread {
		private DirectColorModel screenCapCM = new DirectColorModel(24,
				/* red mask */ 0x00FF0000, /* green mask */ 0x0000FF00,
				/* blue mask */ 0x000000FF);
		private int[] bandmasks = new int[3];

		@Override
		public void run() {
			bandmasks[0] = screenCapCM.getRedMask();
			bandmasks[1] = screenCapCM.getGreenMask();
			bandmasks[2] = screenCapCM.getBlueMask();

			// int size = WINDOW_SIZE.width * WINDOW_SIZE.height
			// /
			// 8;
			init();
			while (windowDrawIsTrue) {
				for (int i = 0; i < map.length && windowDrawIsTrue; i++) {

					try {
						DrawHandleBean linbean = Draw(map[i]);
						if (linbean == null)
							continue;
						map[i].getOrSetBody(linbean);
					} catch (Exception e) {
						e.printStackTrace();
						windowDrawIsTrue = false;
					} finally {
						if (outputStream != null) {
							try {
								outputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

					}
				}
			}
		}

		private void init() {
			float quality = 0.15f;
			writer = ImageIO.getImageWritersByFormatName("JPEG").next();
			writeParam = (JPEGImageWriteParam) writer.getDefaultWriteParam();
			writeParam.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
			writeParam.setProgressiveMode(JPEGImageWriteParam.MODE_DISABLED);
			writeParam.setCompressionQuality(quality);
			ColorModel colorModel = ColorModel.getRGBdefault();
			// 指定压缩时使用的色彩模式
			writeParam.setDestinationType(new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(6, 6)));

		}

		ByteArrayOutputStream outputStream = null;
		ImageWriter writer;
		JPEGImageWriteParam writeParam;
		BufferedImage bufferedImage;
		DataBufferInt buffer;
		WritableRaster raster;
		double num = 10;

		private DrawHandleBean Draw(WindowControlBean bean) throws IOException {
			if (bean == null || !bean.isTrue()) {
				return null;
			}
			DrawHandleBean last_bean = bean.getLastBean();
			DrawHandleBean linbean = new DrawHandleBean();
			int[] draw_data = getDrawData(bean);
			linbean.draw_data = draw_data;
			if (!windowDrawIsTrue)
				return null;
			if (last_bean != null) {
				outputStream = new ByteArrayOutputStream();
				DataOutputStream outputStream2 = new DataOutputStream(outputStream);
				for (int i = 0; i < last_bean.last_data.length; i++) {
					if (last_bean.last_data[i] != draw_data[i]) {
						outputStream2.writeInt(i);
						outputStream2.writeInt(draw_data[i]);
						i++;
						for (; i < last_bean.last_data.length; i++) {
							if (last_bean.last_data[i] != draw_data[i]) {
								outputStream2.writeInt(draw_data[i]);
							} else {
								outputStream2.writeInt(1);
								break;
							}
						}
					}
				}
				linbean.body = outputStream.toByteArray();
				outputStream2.close();
				outputStream.close();
				if (linbean.body.length <= 0) {
					Other.sleep(10);
					return null;
				}
				linbean.yuan_len = linbean.body.length;
				if (linbean.yuan_len < last_bean.last_body.length * num) {
					linbean.body = Other.ZIPComperssor(linbean.body);

				} else {
					linbean.body = null;
				}
			}
			if (linbean.body != null) {
				linbean.type = 1;
			} else {
				linbean.type = 0;
				buffer = new DataBufferInt(draw_data, draw_data.length);
				raster = Raster.createPackedRaster(buffer, (int) bean.w, (int) bean.h, (int) bean.w, bandmasks, null);
				bufferedImage = new BufferedImage(screenCapCM, raster, false, null);
				outputStream = new ByteArrayOutputStream();
				ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
				writer.setOutput(ios);
				writer.write(null, new IIOImage(bufferedImage, null, null), writeParam);
				// writer.dispose();
				ios.close();
				outputStream.close();
				linbean.body = outputStream.toByteArray();
			}
			return linbean;
		}

	}

	Tool tool = new Tool();
	long times = 0;

	private int[] getDrawData(WindowControlBean bean) {
		while (System.currentTimeMillis() - times < 40 && windowDrawIsTrue) {
			tool.delay(10);
		}
		times = System.currentTimeMillis();
		int[] lin2 = new int[(int) (bean.w * bean.h)];
		int[] lin = new int[bean.getUserBean().getW() * bean.getUserBean().getH()];
		if (bean.getUserBean().getW() >= WindowControlBean.WIDTH || bean.getUserBean().getH() >= WindowControlBean.HEIGHT) {
			lin = tool.getScreenBufferInt(bean.getUserBean().getX(), bean.getUserBean().getY(), bean.getUserBean().getW(), bean.getUserBean().getH());
			for (int j = 0; j < bean.getUserBean().getH(); j++) {
				for (int i = 0; i < bean.getUserBean().getW(); i++) {
					int s = (int) ((int) (j * bean.dh) * bean.w) + (int) (i * bean.dw);
					lin2[s] = lin[j * bean.getUserBean().getW() + i];
				}
			}
			return lin2;
		} else {
			return tool.getScreenBufferInt(bean.getUserBean().getX(), bean.getUserBean().getY(), bean.getUserBean().getW(), bean.getUserBean().getH());
		}
	}

	private static WindowControlThread thread = null;

	public static boolean start(TcpBag bag2, TcpSocketUserBean tcpSocket2) {

		if (thread == null || !thread.isAlive()) {
			thread = new WindowControlThread();
		}
		WindowListenerBean userBean = WindowListenerBean.newInstanceBean(WindowListenerBean.class, bag2.getBody_description());
		if (userBean == null)
			return false;
		WindowControlBean bean = new WindowControlBean(tcpSocket2, userBean);
		if (!thread.addWindowControlBean(bean)) {
			System.err.println("监视加入失败了，监视方过多");
			UIModel.sendAppInfo("警告", "监视加入失败了，监视方过多");
			return false;
		}
		if (!thread.isAlive())
			thread.start();
		return true;
	}

	private boolean addWindowControlBean(WindowControlBean bean) {
		bean.setTrue(true);
		for (int i = 0; i < map.length; i++) {

			if (map[i] == null || !map[i].isTrue()) {
				map[i] = bean;
				return true;
			}
		}
		return false;
	}

	public static void close(int type) {
		// windowDrawIsTrue = false;
		for (int i = 0; i < map.length; i++) {
			if (map[i] != null && map[i].isTrue()) {
				WindowListenerBean bean = map[i].getUserBean();
				if (bean != null && bean.getMark() == type) {
					System.out.println("关闭：" + type);
					map[i].setTrue(false);
				}
			}
		}
	}

	public static void close() {
		windowDrawIsTrue = false;
		for (int i = 0; i < map.length; i++) {
			if (map[i] != null && map[i].isTrue()) {
				map[i].setTrue(false);
			}
		}

	}

	private static WindowControlBean[] map = new WindowControlBean[5];

}

class DrawHandleBean {
	int type = -1;
	int yuan_len = -1;
	byte body[] = null;
	int last_data[] = null;
	int draw_data[] = null;
	byte[] last_body = null;
}

class WindowControlBean {
	private boolean isTrue = false;
	private WindowListenerBean bean = null;
	private DrawHandleBean body = null;
	private boolean isBody = false;
	private TcpSocketUserBean tcpSocket;
	public double w, h, dw, dh;
	public static final double nsn = 1.0;
	public static double WIDTH = 1920 * nsn;
	public static double HEIGHT = 1080 * nsn;

	protected TcpSocketUserBean getTcpSocket() {
		return tcpSocket;
	}

	protected void setTcpSocket(TcpSocketUserBean tcpSocket) {
		this.tcpSocket = tcpSocket;
	}

	private DrawHandleBean getBody() {
		if (!isTrue || !isBody) {
			return null;
		}
		if (body.type == 0) {
			this.body.last_body = this.body.body;
		}
		this.body.last_data = this.body.draw_data;
		isBody = false;
		return body;

	}

	public DrawHandleBean getLastBean() {
		return body;
	}

	public synchronized DrawHandleBean getOrSetBody(DrawHandleBean body) {
		if (body == null) {
			return getBody();
		} else {
			setBody(body);
		}
		return null;
	}

	private void setBody(DrawHandleBean body) {
		if (this.body == null) {
			this.body = body;
			body.last_body = body.body;
			body.last_data = body.draw_data;
		} else {
			this.body.draw_data = body.draw_data;
			this.body.body = body.body;
			this.body.type = body.type;
			this.body.yuan_len = body.yuan_len;
		}
		isBody = true;
	}

	public WindowControlBean(TcpSocketUserBean tcpSocket2, WindowListenerBean userBean) {
		super();
		this.tcpSocket = tcpSocket2;
		this.bean = userBean;
		this.bean.setTo_user_mail(this.bean.getUser_mail());
		this.bean.setUser_mail(DataSave.userBean.getUser_mail());
		w = userBean.getW() > WIDTH ? WIDTH : userBean.getW();
		h = userBean.getH() > HEIGHT ? HEIGHT : userBean.getH();
		dw = userBean.getW() > WIDTH ? WIDTH / userBean.getW() : 1;
		dh = userBean.getH() > HEIGHT ? HEIGHT / userBean.getH() : 1;

	}

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	public WindowListenerBean getUserBean() {
		return bean;
	}

	public void setUserBean(WindowListenerBean bean) {
		this.bean = bean;
	}

}