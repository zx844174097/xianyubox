package com.mugui.tool;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class ImgTool {
	public static void FitTableColumns(JTable myTable) {
		JTableHeader header = myTable.getTableHeader();
		int rowCount = myTable.getRowCount();
		Enumeration<TableColumn> columns = myTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
			int width = (int) myTable.getTableHeader().getDefaultRenderer()
					.getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) myTable.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col)
						.getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			header.setResizingColumn(column); // ���к���Ҫ
			column.setWidth(width + myTable.getIntercellSpacing().width + 5);
			FontMetrics label = myTable.getFontMetrics(myTable.getFont());
			myTable.setRowHeight(label.getHeight());
		}
	}

	// 图片剪裁
	public static BufferedImage cutImage(BufferedImage img, int x, int y, int w, int h) {
		try {
			return copyBufferedImage(img.getSubimage(x, y, w, h));
		} catch (Exception e) {
			try {
				throw new Exception(img.getWidth() + " " + img.getHeight() + " " + x + " " + y + " " + w + " " + h);
			} catch (Exception e1) {
				 e1.printStackTrace();
			}
			return null;
		}
	}

	public static BufferedImage copyBufferedImage(BufferedImage src) {
		BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = copy.createGraphics();
		g.drawImage(src, 0, 0, copy.getWidth(), copy.getHeight(), null);
		g.dispose();
		return copy;
	}

	// 图片二值化处理
	public static BufferedImage binaryzationImage2(BufferedImage image) {
		BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_BINARY);
		int color = image.getRGB(0, 0);
		int color2 = new Color(255, 255, 255).getRGB();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				if (color == image.getRGB(i, j)) {
					grayImage.setRGB(i, j, color);
				} else
					grayImage.setRGB(i, j, color2);
			}
		}
		return grayImage;
	}

	/**
	 * 有界限的二值化
	 * 
	 * @param image
	 * @param string
	 * @param d
	 * @return
	 */
	// public static BufferedImage binaryzationImage(BufferedImage image, String
	// string, double d) {
	// BufferedImage grayImage = new BufferedImage(image.getWidth(),
	// image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
	// int color = image.getRGB(0, 0);
	// int color2 = new Color(255, 255, 255).getRGB();
	// Tool tool = new Tool();
	// for (int i = 0; i < image.getWidth(); i++) {
	// for (int j = 0; j < image.getHeight(); j++) {
	// double is = 0;
	// if ((is = tool.颜色比较(tool.StringColor(string), new Color(image.getRGB(i,
	// j)))) > d) {
	// grayImage.setRGB(i, j, color);
	// } else
	// grayImage.setRGB(i, j, color2);
	// // System.out.println(is);
	// }
	// }
	// return grayImage;
	// }

	/**
	 * 图片灰度化处理
	 * 
	 * @param image
	 * @return
	 */
	public static BufferedImage grayscaleImage(BufferedImage image) {
		BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				grayImage.setRGB(i, j, image.getRGB(i, j));
			}
		}
		return grayImage;
	}

	/**
	 * 图片放大
	 * 
	 * @param image
	 * @param i
	 * @return
	 */
	public static BufferedImage imageEnlarge(BufferedImage image, double i) {
		BufferedImage result = new BufferedImage((int) (i * image.getWidth()), (int) (i * image.getHeight()),
				BufferedImage.TYPE_INT_RGB);
		result.getGraphics().drawImage(image.getScaledInstance((int) (i * image.getWidth()),
				(int) (i * image.getHeight()), Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}

	/**
	 * 图片拉伸
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage imageStretching(BufferedImage image, int width, int height) {
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int w = image.getWidth();
		int h = image.getHeight();
		double fan = 1.0;
		if (w > h) {
			fan = width / (w * 1.0);
		} else {
			fan = height / (h * 1.0);
		}
		result.getGraphics().drawImage(image.getScaledInstance((int) (w * fan), (int) (h * fan), Image.SCALE_SMOOTH),
				-(int) (w * fan) / 2 + width / 2, -(int) (h * fan) / 2 + height / 2, null);
		return result;
	}

	/**
	 * 去除文字粘连性
	 * 
	 * @param image
	 * @return
	 */
	public static BufferedImage clearAdhesion(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int color = image.getRGB(0, 0);
		BufferedImage temp = new BufferedImage(width, height, image.getType());
		temp.getGraphics().drawImage(image, 0, 0, null);
		image = temp;
		int state = 0;
		int state2 = 0;
		int pole = height - 1;
		int pole2 = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (state == 0 && image.getRGB(i, j) != color) {
					if (j <= pole) {
						pole = j;
						break;
					} else {
						state = 1;
						pole = j;
						break;
					}
				}
				if (state == 1 && image.getRGB(i, j) != color) {
					if (j < pole) {
						state = 2;
						i = i - 1;
					} else {
						pole = j;
						break;
					}
				}
				if (state != 2 && j == height - 1) {
					pole = j;
					state = 0;
					break;
				}
				if (state == 2) {
					image.setRGB(i, j, color);
					if (j == height - 1) {
						pole = j;
						state = 0;
						i = i + 1;
						break;
					}
				}
			}
		}
		for (int i = 0; i < width; i++) {
			for (int j = height - 1; j >= 0; j--) {
				if (state2 == 0 && image.getRGB(i, j) != color) {
					if (j >= pole2) {
						pole2 = j;
						break;
					} else {
						state2 = 1;
						pole2 = j;
						break;
					}
				}
				if (state2 == 1 && image.getRGB(i, j) != color) {
					if (j > pole2) {
						state2 = 2;
						i = i - 1;
					} else {
						pole2 = j;
						break;
					}
				}
				if (state2 != 2 && j == 0) {
					pole2 = j;
					state2 = 0;
					break;
				}
				if (state2 == 2) {
					image.setRGB(i, j, color);
					if (j == 0) {
						pole2 = j;
						state2 = 0;
						i = i + 1;
						break;
					}
				}
			}
		}
		return image;
	}

	public static double[] imageToDoubleArrays(BufferedImage bimage) {
		int smallHeight = bimage.getHeight();
		int smallWidth = bimage.getWidth();
		// resize to 28*28
		int[] pixes = new int[smallWidth * smallHeight];
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
		return grayMatrix;
	}

	/**
	 * 优化后的图像二值化处理
	 * 
	 * @param src
	 * @return
	 */
	public static BufferedImage binaryzationImage(BufferedImage src) {
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] inPixels = new int[width * height];
		int[] outPixels = new int[width * height];
		inPixels = src.getRGB(0, 0, width, height, null, 0, width);
		// getRGB( src, 0, 0, width, height, inPixels );
		int index = 0;
		int means;
		try {
			means = getThreshold(inPixels, height, width);
		} catch (Exception e) {
			return null;
		}
		for (int row = 0; row < height; row++) {
			int ta = 0, tr = 0, tg = 0, tb = 0;
			for (int col = 0; col < width; col++) {
				index = row * width + col;
				ta = (inPixels[index] >> 24) & 0xff;
				tr = (inPixels[index] >> 16) & 0xff;
				tg = (inPixels[index] >> 8) & 0xff;
				tb = inPixels[index] & 0xff;
				if (tr > means) {
					tr = tg = tb = 255; // white
				} else {
					tr = tg = tb = 0; // black
				}
				outPixels[index] = (ta << 24) | (tr << 16) | (tg << 8) | tb;
			}
		}
		dest.setRGB(0, 0, width, height, outPixels, 0, width);
		// setRGB(dest, 0, 0, width, height, outPixels);
		return dest;
	}

	private static int getThreshold(int[] inPixels, int height, int width) throws Exception {
		// maybe this value can reduce the calculation consume;
		int inithreshold = 127;
		int finalthreshold = 0;
		int temp[] = new int[inPixels.length];
		for (int index = 0; index < inPixels.length; index++) {
			temp[index] = (inPixels[index] >> 16) & 0xff;
		}
		List<Integer> sub1 = new ArrayList<Integer>();
		List<Integer> sub2 = new ArrayList<Integer>();
		int means1 = 0, means2 = 0;
		while (finalthreshold != inithreshold) {
			finalthreshold = inithreshold;
			for (int i = 0; i < temp.length; i++) {
				if (temp[i] <= inithreshold) {
					sub1.add(temp[i]);
				} else {
					sub2.add(temp[i]);
				}
			}
			means1 = getMeans(sub1);
			means2 = getMeans(sub2);
			sub1.clear();
			sub2.clear();
			inithreshold = (means1 + means2) / 2;
		}
		// long start = System.currentTimeMillis();
		// System.out.println("Final threshold = " + finalthreshold);
		// long endTime = System.currentTimeMillis() - start;
		// System.out.println("Time consumes : " + endTime);
		return finalthreshold;

	}

	private static int getMeans(List<Integer> data) throws Exception {
		int result = 0;
		int size = data.size();
		for (Integer i : data) {
			result += i;
		}
		if (size <= 0) {
			throw new Exception();
		}
		return (result / size);
	}

	/**
	 * 图片反色
	 * 
	 * @param src
	 * @return
	 */
	public static BufferedImage imageInverse(BufferedImage src) {
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		temp.getGraphics().drawImage(src, 0, 0, null);
		int pixels[] = new int[w * h];
		pixels = temp.getRGB(0, 0, w, h, null, 0, w);
		for (int i = 0; i < pixels.length; i++) {
			Color c = new Color(pixels[i]);
			c = new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
			pixels[i] = c.getRGB();
		}
		temp.setRGB(0, 0, w, h, pixels, 0, w);
		return temp;
	}

	public static byte[] ImgToByteArray(BufferedImage image) {
		ByteArrayOutputStream outputStream = null;
		try {
			outputStream = new ByteArrayOutputStream();
			ImageOutputStream outputStream2 = ImageIO.createImageOutputStream(outputStream);
			ImageIO.write(image, "BMP", outputStream2);
			return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (outputStream != null)
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static BufferedImage byteArrayToImg(byte[] body) {
		ByteArrayInputStream bais = null;
		ImageInputStream inputStream = null;
		try {
			bais = new ByteArrayInputStream(body);
			inputStream = ImageIO.createImageInputStream(bais);
			return ImageIO.read(inputStream);
		} catch (IOException e) {
			return null;
		} finally {
			if (bais != null)
				try {
					bais.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static BASE64Encoder encoder = new BASE64Encoder();
	private static BASE64Decoder decoder = new BASE64Decoder();

	/**
	 * string To bufferedimage
	 * 
	 * @param image
	 */
	public static BufferedImage StringToBufferedImage(String image) {
		try {
			byte[] bytes = decoder.decodeBuffer(image);
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * BufferedImage To String
	 * 
	 * @param image
	 * @return
	 */
	public static String BufferedImageToString(BufferedImage image) {
		ByteArrayOutputStream outputStream = null;
		try {
			ImageIO.write(image, "BMP", outputStream = new ByteArrayOutputStream());
			return encoder.encode(outputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
}
