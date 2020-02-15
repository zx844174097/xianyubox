package com.mugui;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Date;

import com.mugui.model.CmdModel;
import com.mugui.model.HsAllModel;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.windows.Tool;

public class MAIN {
	protected static final String UI_MANAGER[] = null;

	private static  String JARFILEPATH = null;
	static {
		System.setProperty("sun.jnu.encoding", "utf-8");
		JARFILEPATH = MAIN.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		try {
			JARFILEPATH = URLDecoder.decode(new File(JARFILEPATH).getParent(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		ClassLoader classLoader = (ClassLoader) ClassLoader.getSystemClassLoader();
		try {
			InputStreamReader input = new InputStreamReader(classLoader.getResourceAsStream("com/mugui/updatafile/updata.list"));
			BufferedReader reader = new BufferedReader(input);
			String str = "";
			while ((str = reader.readLine()) != null) {
				String line[] = str.split("=");
				if (line.length != 2) {
					continue;
				}
				InputStream inputStream = classLoader.getResourceAsStream("com/mugui/updatafile/" + line[0]);
				File outfile = new File(JARFILEPATH + line[1]);
				if (!outfile.isDirectory()) {
					continue;
				}
				System.out.println(JARFILEPATH + line[1] + line[0]);

				FileOutputStream outputStream = new FileOutputStream(JARFILEPATH + line[1] + line[0]);
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(b)) > 0) {
					outputStream.write(b, 0, len);
				}
				outputStream.close();
				inputStream.close();
			}

			Method addUrl = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			addUrl.setAccessible(true);
			System.out.println(new File(JARFILEPATH + "\\lib\\").getAbsolutePath());
			File[] files = new File(JARFILEPATH + "\\lib\\").listFiles();
			for (File file : files) {
				// String end =
				// file.getName().substring(file.getName().lastIndexOf(".") +
				// 1);
				// if (!end.equals("dll")) {
				// file.renameTo(file = new File(DataSave.JARFILEPATH +
				// "\\lib\\" + Other.getShortUuid() + ".dll"));
				// }
				addUrl.invoke(classLoader, file.toURI().toURL());
			}
			addUrl.setAccessible(false);
			DataSave.JARFILEPATH = JARFILEPATH;
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		File file = new File(DataSave.JARFILEPATH + "/log");
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String s[] = name.split("_");
				s = s[1].split("\\.");
				try {
					long time = Long.parseLong(s[0]);
					if (System.currentTimeMillis() - time > 3 * 24 * 60 * 60 * 1000) {
						new File(dir.getPath() + "\\" + name).delete();
					}
				} catch (Exception e) {
					new File(dir.getPath() + "\\" + name).delete();
				}
				return false;
			}
		});

		 init();
		 System.setOut(outputStream); 
		 System.setErr(outputStream);
		System.setOut(new PrintStream(System.out) {
			@SuppressWarnings("deprecation")
			@Override
			public void println(String x) {
				super.println(new Date(System.currentTimeMillis()).toLocaleString() + ":" + x);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void println(Object x) {
				super.println(new Date(System.currentTimeMillis()).toLocaleString() + ":" + x);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void println(int x) {
				super.println(new Date(System.currentTimeMillis()).toLocaleString() + ":" + x);
			}

			@SuppressWarnings("deprecation")
			@Override
			public void println(char x) {
				super.println(new Date(System.currentTimeMillis()).toLocaleString() + ":" + x);
			}

		});
		DataSave save = new DataSave();
		save.init();
		save.start();
		System.getProperties().put("DataSave", save);

//		Tool shouTool = new Tool();
//
//		BufferedImage bufferedImage;
//		try {
//			bufferedImage = shouTool.getDImg("1580942722150_识别区.bmp").bufferedImage;
//			shouTool.chuli(bufferedImage);
//		} catch (IOException e) { 
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public static BufferedImage image = null;

	static PrintStream outputStream = null;
	static Object object = new Object();
	static boolean bool = false;

	private static void init() {
		try {
			if (outputStream == null) {
				synchronized (object) {
					if (!bool) {
						bool = true;
						outputStream = new PrintStream(new FileOutputStream(new File(DataSave.JARFILEPATH + "/log/log_" + System.currentTimeMillis() + ".txt")),
								true, "utf-8");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	static boolean exit_bool = false;

	public static void exit() {
		long time = System.currentTimeMillis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				HsAllModel.closeAll();
				DataSave.StaticUI.clearTray();
				int pid = Other.getDPID();
				CmdModel.关闭应用(pid);
				CmdModel.close();
				exit_bool = true;
			}
		}).start();
		while (!exit_bool && System.currentTimeMillis() - time < 2500) {
			Other.sleep(200);
		}
		try {
			int pid = Other.getDPID();
			CmdModel.关闭应用(pid);
			CmdModel.close();
		} catch (Throwable e) {
			System.exit(0);
		}
	}
}
