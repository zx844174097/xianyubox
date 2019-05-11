package com.mugui.tool;

import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.xerial.snappy.Snappy;

import com.melloware.jintellitype.JIntellitype;

public class Other {
	public static void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (Exception e) {
		}
	}


	public static <T> boolean ObjectToNewObjectBoolean(T obj, Class<?> toobj) {
		return toobj.isAssignableFrom(obj.getClass());
	}

	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) (i & 0xFF);
		result[1] = (byte) ((i >> 8) & 0xFF);
		result[2] = (byte) ((i >> 16) & 0xFF);
		result[3] = (byte) ((i >> 24) & 0xFF);
		return result;
	}

	public static int byteArrayint(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

	public static boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static boolean isLong(String string) {
		try {
			Long.parseLong(string);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	public static final Object ByteArrayToNewObject(byte[] b) {
		ByteArrayInputStream bArrayInputStream = null;
		ObjectInputStream ois = null;
		try {
			bArrayInputStream = new ByteArrayInputStream(b, 0, b.length);
			ois = new ObjectInputStream(bArrayInputStream);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (bArrayInputStream != null)
					bArrayInputStream.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public static final byte[] ObjectToNewByteArray(Object obj) {
		ByteArrayOutputStream bArrayOutputStream = null;
		ObjectOutputStream oos = null;
		try {
			bArrayOutputStream = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bArrayOutputStream);
			oos.writeObject(obj);
			return bArrayOutputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (bArrayOutputStream != null)
					bArrayOutputStream.close();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public static String ObjectToNewString(Object object) {
		try {
			return new String(Other.ObjectToNewByteArray(object), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Object StringToNewObjet(String s) {
		try {
			return Other.ByteArrayToNewObject(s.getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	// public static byte[] IntergerToNewByteArray(int length) {
	// String s = length + "";
	// byte[] blen = new byte[s.length()];
	// for (int i = 0; i < blen.length; i++)
	// blen[i] = (byte) (s.charAt(i) - '0');
	// return blen;
	// }

	@SuppressWarnings("unchecked")
	public static <T> T copyClassToNewClass(T obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
			return (T) ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (oos != null)
					oos.close();
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static boolean isMailString(String mialString) {
		Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
		Matcher m = p.matcher(mialString);
		return m.matches();
	}

	public static <T> T[] ArraysToNewArray(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static byte[] ArraysToNewArray(byte[] first, byte[] second) {
		byte[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	public static int getTextLength(Graphics graphics, String text) {
		return graphics.getFontMetrics().stringWidth(text);
	}

	public static int getFontHeight(Graphics graphics) {
		return graphics.getFontMetrics().getAscent();
	}

	public static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	// 生成一定范围随机数
	private static int r(int min, int max) {
		int num = 0;
		num = new Random().nextInt(max - min) + min;
		return num;
	}

	// 生成一个用户验证码
	public static String getVerifyCode(int codeSize) {
		String str = "aqzxswedcfrvgtbhyujklp23456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char c[] = new char[codeSize];
		for (int i = 0; i < codeSize; i++)
			c[i] = str.charAt(r(0, str.length()));
		return new String(c);
	}

	public final static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
			"O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	public static String getShortUuid() {
		StringBuffer stringBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i % 8 * 4, i % 8 * 4 + 4);
			int strInteger = Integer.parseInt(str, 16);
			stringBuffer.append(chars[strInteger % 0x3E]);
		}
		uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i % 8 * 4, i % 8 * 4 + 4);
			int strInteger = Integer.parseInt(str, 16);
			stringBuffer.append(chars[strInteger % 0x3E]);
		}
		return stringBuffer.toString();
	}

	public static boolean isHttpUrl(String http) {
		try {
			if (!http.startsWith("http")) {
				http = "http://" + http;
			}
			URL url = new URL(http);
			url.openConnection().connect();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String uuidStr = str.replace("-", "");
		return uuidStr;
	}

	public static byte[] ZIPComperssor(byte[] body) {
		try {
			return Snappy.compress(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] ZIPDecompressor(byte[] body, int yuan_len) {
		try {
			return Snappy.uncompress(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * java自带文件解压
	 * 
	 * @param file
	 * @param pathFile
	 */
	public static boolean DZipInput(File file, File pathFile) {
		try {
			ZipInputStream Zin = new ZipInputStream(new FileInputStream(file));// 输入源zip路径
			BufferedInputStream Bin = new BufferedInputStream(Zin);
			String Parent = pathFile.getPath(); // 输出路径（文件夹目录）
			File Fout = null;
			ZipEntry entry;
			while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
				Fout = new File(Parent, entry.getName());
				if (!Fout.exists()) {
					(new File(Fout.getParent())).mkdirs();
				}
				FileOutputStream out = new FileOutputStream(Fout);
				BufferedOutputStream Bout = new BufferedOutputStream(out);
				int b;
				while ((b = Bin.read()) != -1) {
					Bout.write(b);
				}
				Bout.close();
				out.close();
			}
			Bin.close();
			Zin.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] ArrayBytesDecrypt(byte[] data) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		byte[] key = new byte[16];
		try {
			int i;
			for (i = 0; i < key.length; i++) {
				key[i] = (byte) (data[i] - 125);
			}
			for (; i < data.length; i++) {
				data[i] = (byte) (key[i % 16] ^ (data[i] + key[i % 16]));
				dataOutputStream.writeByte(data[i]);
			}
			data = outputStream.toByteArray();
			dataOutputStream.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static byte[] ArrayBytesEncryption(byte[] data) {

		byte[] temp = new byte[data.length];
		System.arraycopy(data, 0, temp, 0, temp.length);

		byte[] string = getShortUuid().getBytes();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		try {
			int len = string.length;
			for (int i = 0; i < len; i++) {
				dataOutputStream.writeByte(string[i] + 125);
			}
			for (int i = 0; i < temp.length; i++) {
				temp[i] = (byte) ((string[i % len] ^ temp[i]) - (string[i % len]));
				dataOutputStream.writeByte(temp[i]);
			}
			temp = outputStream.toByteArray();
			dataOutputStream.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return temp;
	}

	/**
	 * 
	 * @param content
	 *            请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encoding
	 *            服务器端请求编码。如GBK,UTF-8等
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
		// 这里调用淘宝API
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		// 从http://whois.pconline.com.cn取得IP所在的省市区信息
		String returnStr = getResult(urlStr, content, encodingString);
		if (returnStr != null) {
			// 处理返回的省市区信息
			returnStr = decodeUnicode(returnStr);
			String[] temp = returnStr.split(",");
			if (temp.length < 3) {
				return "0";// 无效IP，局域网测试
			}
			return returnStr;
		}
		return null;
	}

	/**
	 * @param urlStr
	 *            请求的地址
	 * @param content
	 *            请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encoding
	 *            服务器端请求编码。如GBK,UTF-8等
	 * @return
	 */
	private static String getResult(String urlStr, String content, String encoding) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr + "?" + content);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			// DataOutputStream out = new
			// DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
			// out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			// out.flush();// 刷新
			// out.close();// 关闭输出流
			if (connection.getResponseCode() == 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
				// ,以BufferedReader流来读取
				StringBuffer buffer = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				reader.close();
				return buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}

	/**
	 * unicode 转换成 中文
	 * 
	 * @author fanhui 2007-3-15
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	public static String getPC_NAME() {
		return System.getenv("COMPUTERNAME");
	}

	public static String getPC_sequence() {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(new String[] { "wmic", "cpu", "get", "ProcessorId" });
			process.getOutputStream().close();
			Scanner sc = new Scanner(process.getInputStream());
			String property = sc.next();
			if (!property.trim().equalsIgnoreCase("ProcessorId")) {
				sc.close();
				return null;
			}
			String serial = sc.next();
			sc.close();
			return serial;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (process != null)
				process.destroy();
		}
		return "FFFFFFFFFFFFFFFF";
	}

	public static String getPCUser_NAME() {
		return System.getProperty("user.name");
	}

	public static HashMap<String, Integer> getKey2KeycodeMapping() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();

		map.put("first", Integer.valueOf(400));
		map.put("last", Integer.valueOf(402));
		map.put("typed", Integer.valueOf(400));
		map.put("pressed", Integer.valueOf(401));
		map.put("released", Integer.valueOf(402));
		map.put("enter", Integer.valueOf(13));
		map.put("back_space", Integer.valueOf(8));
		map.put("tab", Integer.valueOf(9));
		map.put("cancel", Integer.valueOf(3));
		map.put("clear", Integer.valueOf(12));
		map.put("pause", Integer.valueOf(19));
		map.put("caps_lock", Integer.valueOf(20));
		map.put("escape", Integer.valueOf(27));
		map.put("space", Integer.valueOf(32));
		map.put("page_up", Integer.valueOf(33));
		map.put("page_down", Integer.valueOf(34));
		map.put("end", Integer.valueOf(35));
		map.put("home", Integer.valueOf(36));
		map.put("left", Integer.valueOf(37));
		map.put("up", Integer.valueOf(38));
		map.put("right", Integer.valueOf(39));
		map.put("down", Integer.valueOf(40));
		map.put("comma", Integer.valueOf(188));
		map.put("minus", Integer.valueOf(109));
		map.put("period", Integer.valueOf(110));
		map.put("slash", Integer.valueOf(191));
		map.put("accent `", Integer.valueOf(192));
		map.put("0", Integer.valueOf(48));
		map.put("1", Integer.valueOf(49));
		map.put("2", Integer.valueOf(50));
		map.put("3", Integer.valueOf(51));
		map.put("4", Integer.valueOf(52));
		map.put("5", Integer.valueOf(53));
		map.put("6", Integer.valueOf(54));
		map.put("7", Integer.valueOf(55));
		map.put("8", Integer.valueOf(56));
		map.put("9", Integer.valueOf(57));
		map.put("semicolon", Integer.valueOf(186));
		map.put("equals", Integer.valueOf(187));
		map.put("a", Integer.valueOf(65));
		map.put("b", Integer.valueOf(66));
		map.put("c", Integer.valueOf(67));
		map.put("d", Integer.valueOf(68));
		map.put("e", Integer.valueOf(69));
		map.put("f", Integer.valueOf(70));
		map.put("g", Integer.valueOf(71));
		map.put("h", Integer.valueOf(72));
		map.put("i", Integer.valueOf(73));
		map.put("j", Integer.valueOf(74));
		map.put("k", Integer.valueOf(75));
		map.put("l", Integer.valueOf(76));
		map.put("m", Integer.valueOf(77));
		map.put("n", Integer.valueOf(78));
		map.put("o", Integer.valueOf(79));
		map.put("p", Integer.valueOf(80));
		map.put("q", Integer.valueOf(81));
		map.put("r", Integer.valueOf(82));
		map.put("s", Integer.valueOf(83));
		map.put("t", Integer.valueOf(84));
		map.put("u", Integer.valueOf(85));
		map.put("v", Integer.valueOf(86));
		map.put("w", Integer.valueOf(87));
		map.put("x", Integer.valueOf(88));
		map.put("y", Integer.valueOf(89));
		map.put("z", Integer.valueOf(90));
		map.put("open_bracket", Integer.valueOf(219));
		map.put("back_slash", Integer.valueOf(220));
		map.put("close_bracket", Integer.valueOf(221));
		map.put("numpad0", Integer.valueOf(96));
		map.put("numpad1", Integer.valueOf(97));
		map.put("numpad2", Integer.valueOf(98));
		map.put("numpad3", Integer.valueOf(99));
		map.put("numpad4", Integer.valueOf(100));
		map.put("numpad5", Integer.valueOf(101));
		map.put("numpad6", Integer.valueOf(102));
		map.put("numpad7", Integer.valueOf(103));
		map.put("numpad8", Integer.valueOf(104));
		map.put("numpad9", Integer.valueOf(105));
		map.put("multiply", Integer.valueOf(106));
		map.put("add", Integer.valueOf(107));
		map.put("separator", Integer.valueOf(108));
		map.put("subtract", Integer.valueOf(109));
		map.put("decimal", Integer.valueOf(110));
		map.put("divide", Integer.valueOf(111));
		map.put("delete", Integer.valueOf(46));
		map.put("num_lock", Integer.valueOf(144));
		map.put("scroll_lock", Integer.valueOf(145));
		map.put("f1", Integer.valueOf(112));
		map.put("f2", Integer.valueOf(113));
		map.put("f3", Integer.valueOf(114));
		map.put("f4", Integer.valueOf(115));
		map.put("f5", Integer.valueOf(116));
		map.put("f6", Integer.valueOf(117));
		map.put("f7", Integer.valueOf(118));
		map.put("f8", Integer.valueOf(119));
		map.put("f9", Integer.valueOf(120));
		map.put("f10", Integer.valueOf(121));
		map.put("f11", Integer.valueOf(122));
		map.put("f12", Integer.valueOf(123));
		map.put("f13", Integer.valueOf(61440));
		map.put("f14", Integer.valueOf(61441));
		map.put("f15", Integer.valueOf(61442));
		map.put("f16", Integer.valueOf(61443));
		map.put("f17", Integer.valueOf(61444));
		map.put("f18", Integer.valueOf(61445));
		map.put("f19", Integer.valueOf(61446));
		map.put("f20", Integer.valueOf(61447));
		map.put("f21", Integer.valueOf(61448));
		map.put("f22", Integer.valueOf(61449));
		map.put("f23", Integer.valueOf(61450));
		map.put("f24", Integer.valueOf(61451));
		map.put("printscreen", Integer.valueOf(44));
		map.put("insert", Integer.valueOf(45));
		map.put("help", Integer.valueOf(47));
		map.put("meta", Integer.valueOf(157));
		map.put("back_quote", Integer.valueOf(192));
		map.put("quote", Integer.valueOf(222));
		map.put("kp_up", Integer.valueOf(224));
		map.put("kp_down", Integer.valueOf(225));
		map.put("kp_left", Integer.valueOf(226));
		map.put("kp_right", Integer.valueOf(227));
		map.put("dead_grave", Integer.valueOf(128));
		map.put("dead_acute", Integer.valueOf(129));
		map.put("dead_circumflex", Integer.valueOf(130));
		map.put("dead_tilde", Integer.valueOf(131));
		map.put("dead_macron", Integer.valueOf(132));
		map.put("dead_breve", Integer.valueOf(133));
		map.put("dead_abovedot", Integer.valueOf(134));
		map.put("dead_diaeresis", Integer.valueOf(135));
		map.put("dead_abovering", Integer.valueOf(136));
		map.put("dead_doubleacute", Integer.valueOf(137));
		map.put("dead_caron", Integer.valueOf(138));
		map.put("dead_cedilla", Integer.valueOf(139));
		map.put("dead_ogonek", Integer.valueOf(140));
		map.put("dead_iota", Integer.valueOf(141));
		map.put("dead_voiced_sound", Integer.valueOf(142));
		map.put("dead_semivoiced_sound", Integer.valueOf(143));
		map.put("ampersand", Integer.valueOf(150));
		map.put("asterisk", Integer.valueOf(151));
		map.put("quotedbl", Integer.valueOf(152));
		map.put("less", Integer.valueOf(153));
		map.put("greater", Integer.valueOf(160));
		map.put("braceleft", Integer.valueOf(161));
		map.put("braceright", Integer.valueOf(162));
		map.put("at", Integer.valueOf(512));
		map.put("colon", Integer.valueOf(513));
		map.put("circumflex", Integer.valueOf(514));
		map.put("dollar", Integer.valueOf(515));
		map.put("euro_sign", Integer.valueOf(516));
		map.put("exclamation_mark", Integer.valueOf(517));
		map.put("inverted_exclamation_mark", Integer.valueOf(518));
		map.put("left_parenthesis", Integer.valueOf(519));
		map.put("number_sign", Integer.valueOf(520));
		map.put("plus", Integer.valueOf(521));
		map.put("right_parenthesis", Integer.valueOf(522));
		map.put("underscore", Integer.valueOf(523));
		map.put("context_menu", Integer.valueOf(525));
		map.put("final", Integer.valueOf(24));
		map.put("convert", Integer.valueOf(28));
		map.put("nonconvert", Integer.valueOf(29));
		map.put("accept", Integer.valueOf(30));
		map.put("modechange", Integer.valueOf(31));
		map.put("kana", Integer.valueOf(21));
		map.put("kanji", Integer.valueOf(25));
		map.put("alphanumeric", Integer.valueOf(240));
		map.put("katakana", Integer.valueOf(241));
		map.put("hiragana", Integer.valueOf(242));
		map.put("full_width", Integer.valueOf(243));
		map.put("half_width", Integer.valueOf(244));
		map.put("roman_characters", Integer.valueOf(245));
		map.put("all_candidates", Integer.valueOf(256));
		map.put("previous_candidate", Integer.valueOf(257));
		map.put("code_input", Integer.valueOf(258));
		map.put("japanese_katakana", Integer.valueOf(259));
		map.put("japanese_hiragana", Integer.valueOf(260));
		map.put("japanese_roman", Integer.valueOf(261));
		map.put("kana_lock", Integer.valueOf(262));
		map.put("input_method_on_off", Integer.valueOf(263));
		map.put("cut", Integer.valueOf(65489));
		map.put("copy", Integer.valueOf(65485));
		map.put("paste", Integer.valueOf(65487));
		map.put("undo", Integer.valueOf(65483));
		map.put("again", Integer.valueOf(65481));
		map.put("find", Integer.valueOf(65488));
		map.put("props", Integer.valueOf(65482));
		map.put("stop", Integer.valueOf(65480));
		map.put("compose", Integer.valueOf(65312));
		map.put("alt_graph", Integer.valueOf(65406));
		map.put("begin", Integer.valueOf(65368));
		return map;
	}

	public static JIntellitype getJIntellitype() {
		return JIntellitype.getInstance();
	}

	public static int getDPID() {
		String string = ManagementFactory.getRuntimeMXBean().getName();
		return Integer.parseInt(string.split("@")[0]);
	}

	public static int random(int l, int r) {

		return (int) (Math.random() * (r - l) + l);
	}

	public static String MD5(String plainText) {
		// 定义一个字节数组
		byte[] secretBytes = null;
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 对字符串进行加密
			md.update(plainText.getBytes());
			// 获得加密后的数据
			secretBytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		// 将加密后的数据转换为16进制数字
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;

	}

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static String RipeMD128(String plainText) {

		// 定义一个字节数组
		byte[] secretBytes = null;
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("RipeMD128");
			// 对字符串进行加密
			md.update(plainText.getBytes());
			// 获得加密后的数据
			secretBytes = md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有RipeMD128这个算法！");
		}
		// 将加密后的数据转换为16进制数字
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;

	}

	/**
	 * 数组截取
	 * 
	 * @param rec
	 * @param i
	 * @param length
	 * @return
	 */
	public static byte[] subArrays(byte[] rec, int i, int length) {
		byte[] by = new byte[length];
		System.arraycopy(rec, i, by, 0, length);
		return by;
	}
}
