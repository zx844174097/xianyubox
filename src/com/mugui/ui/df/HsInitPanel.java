package com.mugui.ui.df;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mugui.MAIN;
import com.mugui.ModelInterface;
import com.mugui.Dui.DInputDialog;
import com.mugui.Dui.DPanel;
import com.mugui.ew.EWUIHandel;
import com.mugui.jni.Tool.DJni;
import com.mugui.model.CmdModel;
import com.mugui.model.GJUIHandle;
import com.mugui.model.HsAllModel;
import com.mugui.model.TCPModel;
import com.mugui.model.UIModel;
import com.mugui.tool.FileTool;
import com.mugui.tool.Other;
import com.mugui.ui.DataSave;
import com.mugui.ui.part.GameListenerThread;
import com.mugui.windows.DRegistry;
import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HsInitPanel extends DPanel {
	public static HsInitPanel main = new HsInitPanel();

	public HsInitPanel() {
		setLayout(new BorderLayout(0, 0));
		add(panel);
	}

	private class TempPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8037202348616985623L;
		private JLabel label = null;

		public TempPanel() {
			setLayout(new BorderLayout(0, 0));
			label = new JLabel("加载中。。。");
			add(label, BorderLayout.CENTER);
			label.setFont(new Font("宋体", Font.BOLD, 28));
			label.setHorizontalAlignment(SwingConstants.CENTER);
		}

		public void setText(String text) {
			label.setText(text);
		}
	}

	private void updateUI(final Component dialog) {
		this.removeAll();
		new Thread(new Runnable() {
			@Override
			public void run() {
				HsInitPanel.this.add(dialog, BorderLayout.CENTER);
				HsInitPanel.this.validate();
				HsInitPanel.this.repaint();
			}
		}).start();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8212596227518760481L;

	private static final String path_tr = "\\software\\DaumGames\\black";
	private static final String path_tw = "\\software\\WOW6432Node\\BlackDesert";
	private static final String path_stream = "\\software\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\Steam App 582660";
	private static final String path_stream2 = "\\software\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\{C1F96C92-7B8C-485F-A9CD-37A0708A2A60}";
	private static final String path_stream3 = "\\Software\\BDO";
	private static final String path_jp = "\\Software\\GameOn\\Pmang\\BlackDesert_live";
	private static final String path_id = "\\SOFTWARE\\WOW6432Node\\BlackDesert_ID";
	private static final String path_gjqt = "\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\{99D89B39-1E5D-4CB0-BC9D-EF78E1B9FA9A}}_is1";
	private static final String path_run = DataSave.JARFILEPATH;
	private DInputDialog dialog = new DInputDialog("信息", "检测到游戏配置不正确，期间将由辅助修改，但是会重启游戏，是否确认重启？", true, true);
	private HsInitPanel1 panel1 = new HsInitPanel1();
	private TempPanel panel = new TempPanel();
	private Thread initstart = null;

	@Override
	public void init() {
		if (initstart != null && initstart.isAlive()) {
			return;
		}
		initstart = new Thread(new Runnable() {
			DJni jni = new DJni();

			@Override
			public void run() {
				DataSave.服务器 = "未知";
				if (DJni.isTrue) {
					DataSave.StaticUI.updateUI(DataSave.bodyPanel);
					GameListenerThread.start();
					return;
				}
				if (!jni.IsRunAsAdmin()) {
					DInputDialog dialog = new DInputDialog("异常", "检测到辅助没有以管理员方式启动，功能无法正常使用。", true, true);
					dialog.setBText("继续", "关闭辅助", null);
					updateUI(dialog);
					if (dialog.start() == 0) {
						DataSave.StaticUI.updateUI(DataSave.bodyPanel);
						GameListenerThread.start();
						return;
					} else {
						MAIN.exit();
					}
					return;
				}
				if (!DRegistry.isReadAndWrite(path_run)) {
					DInputDialog dialog = new DInputDialog("信息", "检测到辅助出现异常，可能某些功能无法正常使用。", true, true);
					dialog.setBText("继续", "关闭辅助", null);
					updateUI(dialog);
					if (dialog.start() == 0) {
						DataSave.StaticUI.updateUI(DataSave.bodyPanel);
						GameListenerThread.start();
						return;
					} else {
						MAIN.exit();
					}
					return;
				}

				LinkedList<String> game_list = new LinkedList<>();
				game_list.add("黑色沙漠");

				game_list.add("古剑奇谭");

				game_list.add("额外功能");
				// String game = "黑色沙漠";
				if (game_list.size() >= 2) {
					GameSelectPanel panel = new GameSelectPanel();
					panel.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							final String string = e.getActionCommand();
							new Thread(new Runnable() {

								@Override
								public void run() {
									switch (string) {
									case "黑色沙漠":
										startHSGame(findHSClient());
										break;
									case "古剑奇谭":
										startGJGame(findGJClient());
										break;
									case "额外功能":
										startEWModel();
										break;
									}
								}

							}).start();

						}

					});
					panel.reInit(game_list);
					updateUI(panel);
				}

				// Tool.reSetDevice();
			}

			private void startEWModel() {
				ModelInterface object = (ModelInterface) DataSave.loader.loadClassToObject("com.mugui.ew.EWUIHandel");
				if (object != null) {
					object.init();
					object.start();
				}

			}

			private File gjqt_file = new File(DataSave.JARFILEPATH + "\\古剑奇谭\\data\\配置.txt");

			private void startGJGame(String gjqt_path) {
				if (!GJUIHandle.isRunGame()) {
					if (gjqt_path == null && gjqt_file.isFile()) {
						BufferedReader reader = null;
						try {
							reader = new BufferedReader(new FileReader(gjqt_file));
							gjqt_path = reader.readLine();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							// if (reader == null) {
							// try {
							// reader.close();
							// } catch (IOException e) {
							// e.printStackTrace();
							// }
							// }
						}
					}
					if (gjqt_path != null) {
						CmdModel.打开应用(gjqt_path + "\\GameLauncher.exe");
					}
					updateUI(panel);
					panel.setText("等待游戏启动");
					while (!GJUIHandle.isRunGame()) {
						Other.sleep(500);
					}

				}
				gjqt_path = GJUIHandle.getGamePath();
				if (gjqt_path != null && !gjqt_path.trim().equals("")) {
					gjqt_path = new File(gjqt_path).getParentFile().getParent();
					if (gjqt_path.startsWith("\\Device\\")) {
						int num = 0;
						int i = 0;
						for (i = 0; i < gjqt_path.length(); i++) {
							if (gjqt_path.charAt(i) == '\\') {
								num++;
								if (num == 3)
									break;
							}
						}
						if (num == 3) {
							for (int a = 'A'; a < 'Z'; a++) {
								String temp_now_path = String.valueOf((char) a) + ":" + gjqt_path.substring(i);
								if (new File(temp_now_path).isDirectory()) {
									gjqt_path = temp_now_path;
									break;
								}
							}
						}
					}
					BufferedWriter writer = null;
					try {
						if (!gjqt_file.isFile()) {
							gjqt_file.getParentFile().mkdirs();
							gjqt_file.createNewFile();
						}
						writer = new BufferedWriter(new FileWriter(gjqt_file));
						writer.write(gjqt_path);
						writer.newLine();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (writer != null) {
							try {
								writer.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				GJUIHandle.enterHZ();

			}

			private String findGJClient() {
				String gjqt_path = null;
				try {
					gjqt_path = DRegistry.getHkeyLocalMachineValue(path_gjqt, "InstallLocation");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("gjqt_path：" + gjqt_path);
				return gjqt_path;

			}

			private void startHSGame(HashMap<String, String> hsclients) {
				DataSave.loader.setDataClassPath("com/mugui/ui/data/");
				HsAllModel.sendGetHsUserAllTime();
				int app_id = jni.getWinAppIDByName("BlackDesert64.exe");
				if (app_id <= 0) {
					app_id = jni.getWinAppIDByName("blackdesert64.exe");
				}
				if (app_id <= 0) {
					app_id = jni.getWinAppIDByName("blackdesert64.bin");
				} 
				if (app_id <= 0) {
					app_id = jni.getWinAppIDByName("BlackDesert64.bin");
				}

				if (app_id > 0) {// 游戏运行中
					try {
						// 判断属于哪个服务器
						int handle_id = jni.getWinAppHandleByID(app_id);
						String now_path = jni.getWinAppFilePath(handle_id).trim();
						System.out.println(now_path);
						now_path = new File(now_path).getParentFile().getParent();
						if (now_path.startsWith("\\Device\\")) {
							int num = 0;
							int i = 0;
							for (i = 0; i < now_path.length(); i++) {
								if (now_path.charAt(i) == '\\') {
									num++;
									if (num == 3)
										break;
								}
							}
							if (num == 3) {
								for (int a = 'A'; a < 'Z'; a++) {
									String temp_now_path = String.valueOf((char) a) + ":" + now_path.substring(i);
									if (new File(temp_now_path).isDirectory()) {
										now_path = temp_now_path;
										break;
									}
								}
							}
						}

						File file = new File(now_path + "\\service.ini");

						System.out.println("now_path=" + file.getPath());
						FileInputStream inputStream = new FileInputStream(file);
						BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
						String type = reader.readLine();
						while ((type = reader.readLine()) != null) {
							if (!type.trim().equals("")) {
								break;
							}
							System.out.println(type);

						}
						System.out.println(type);
						reader.close();
						inputStream.close();
						String s[] = type.split("=");
						if (s.length == 2) {
							if (s[1].equals("KR")) {
								DataSave.服务器 = "韩服";
							} else if (s[1].equals("NA")) {
								DataSave.服务器 = "steam服";
							} else if (s[1].equals("TW")) {
								DataSave.服务器 = "台服";
							} else if (s[1].equals("JP")) {
								DataSave.服务器 = "日服";
							} else if (s[1].equals("ID")) {
								DataSave.服务器 = "东南亚";
							} else {
								DataSave.服务器 = "私服";
							}
						} else
							DataSave.服务器 = "私服";
						// }
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(app_id + " " + DataSave.服务器);
					// 检查游戏配置文件是否正确
					if (!startComparedAppFile()) {
						DataSave.StaticUI.updateUI(DataSave.bodyPanel);
						GameListenerThread.start();
						// Tool.reSetDevice();
						return;
					}
					// 显示第一个界面提醒用户将要关闭游戏
					updateUI(dialog);
					if (dialog.start() == 0) {
						System.out.println("关闭游戏");
						if (!jni.isWinAppRun("BlackDesert64.exe")) {
							startComparedAppFile();
						} else {
							CmdModel.关闭应用(app_id);
							while (jni.isWinAppRun("BlackDesert64.exe")) {
								Other.sleep(100);
							}
						}
						System.out.println("关闭成功");
					} else {
						MAIN.exit();
					}
				} else {
					CmdModel.关闭应用("iexplore.exe");
				}
				if (DataSave.服务器.equals("未知")) {
					startComparedAppFile();
					// 显示选择启动服务器界面
					LinkedList<String> linkedList = new LinkedList<String>();
					if (hsclients.get("tr_path") != null) {
						linkedList.add("韩服");
					}
					if (hsclients.get("tw_path") != null) {
						linkedList.add("台服");
					}
					if (hsclients.get("stream_path") != null || hsclients.get("stream2_path") != null || hsclients.get("stream3_path") != null) {
						linkedList.add("steam服");
					}
					if (hsclients.get("jp_path") != null) {
						linkedList.add("日服");
					}
					if (hsclients.get("id_path") != null) {
						linkedList.add("东南亚");
					}
					linkedList.add("私服");
					panel1.reInit(linkedList);
					updateUI(panel1);
					DataSave.服务器 = panel1.start();
				}
				System.out.println("服务器:" + DataSave.服务器);
				// 启动游戏
				if (DataSave.服务器.equals("韩服")) {
					CmdModel.打开浏览器("http://black.game.daum.net/black/index.daum");
				} else if (DataSave.服务器.equals("台服")) {
					CmdModel.打开应用(hsclients.get("tw_path") + "\\BlackDesertLauncher.exe");
				} else if (DataSave.服务器.equals("steam服")) {
					if (hsclients.get("stream_path") != null)
						CmdModel.打开浏览器("steam://rungameid/582660");
					else {
						String temp = null;
						if (hsclients.get("stream2_path") != null)
							temp = hsclients.get("stream2_path");
						else if (hsclients.get("stream3_path") != null)
							temp = hsclients.get("stream3_path");
						CmdModel.打开应用(temp + "\\Black Desert Online Launcher.exe");
					}
				} else if (DataSave.服务器.equals("私服")) {

				} else if (DataSave.服务器.equals("日服")) {
					CmdModel.打开浏览器("https://blackdesert.pmang.jp/");
				} else if (DataSave.服务器.equals("东南亚")) {
					CmdModel.打开应用(hsclients.get("id_path") + "\\BlackDesertLauncher.exe");
				}
				updateUI(panel); 
				panel.setText("等待游戏启动");
				while (!jni.isWinAppRun("BlackDesert64.exe")) {
					Other.sleep(500);
				}
				DataSave.StaticUI.updateUI(DataSave.bodyPanel);
				GameListenerThread.start();
			}

			private HashMap<String, String> findHSClient() {
				HashMap<String, String> map = new HashMap<>();

				String tr_path = null;
				try {
					tr_path = DRegistry.getHkeyCurrentUserValue(path_tr, "InstallPath");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("tr_path：" + tr_path);
				if (tr_path != null) {
					map.put("tr_path", tr_path);
				}
				String tw_path = null;
				try {
					tw_path = DRegistry.getHkeyLocalMachineValue(path_tw, "Path");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("tw_path：" + tw_path);
				if (tw_path != null) {
					map.put("tw_path", tw_path);
				}
				String stream_path = null;
				try {
					stream_path = DRegistry.getHkeyLocalMachineValue(path_stream, "InstallLocation");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("stream_path：" + stream_path);
				if (stream_path != null) {
					map.put("stream_path", stream_path);
				}
				String stream2_path = null;
				try {
					stream2_path = DRegistry.getHkeyLocalMachineValue(path_stream2, "InstallLocation");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("stream2_path：" + stream2_path);
				if (stream2_path != null) {
					map.put("stream2_path", stream2_path);
				}
				String stream3_path = null;
				try {
					stream3_path = DRegistry.getHkeyCurrentUserValue(path_stream3, "location");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("stream3_path：" + stream3_path);
				if (stream3_path != null) {
					map.put("stream3_path", stream3_path);
				}
				String jp_path = null;
				try {
					jp_path = DRegistry.getHkeyCurrentUserValue(path_jp, "location");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("jp_path：" + jp_path);
				if (jp_path != null) {
					map.put("jp_path", jp_path);
				}
				String id_path = null;
				try {
					id_path = DRegistry.getHkeyLocalMachineValue(path_id, "Path");
				} catch (FileNotFoundException e1) {
					// e1.printStackTrace();
				}
				System.out.println("id_path：" + id_path);
				if (id_path != null) {
					map.put("id_path", id_path);
				}
				return map;
			}

			private static final String gameVariable = "gameVariable.xml";

			private boolean startComparedAppFile() {
				File f = new File(FileTool.getWindowsPath().getPath() + "/Black Desert/GameOption.txt");
				if (!f.exists()) {
					return false;
				}
				File f2 = new File(FileTool.getWindowsPath().getPath() + "/Black Desert/UserCache/");
				if (!f2.isDirectory()) {
					return false;
				}
				Properties properties = new Properties();
				FileInputStream fileInputStream = null;
				boolean bool = false;
				try {
					fileInputStream = new FileInputStream(f);
					properties.load(fileInputStream);
					String fov = properties.getProperty("fov");
					String UIFontSizeType = properties.getProperty("UIFontSizeType");
					if (!fov.trim().equals("70.00") || !UIFontSizeType.trim().equals("0")) {
						bool = true;
						BufferedReader reader = new BufferedReader(new FileReader(f));
						StringBuffer buffer = new StringBuffer();
						String s = null;
						while ((s = reader.readLine()) != null) {
							if (s.split(" = ")[0].trim().equals("fov")) {
								s = "fov = 70.00";
							} else if (s.split(" = ")[0].trim().equals("UIFontSizeType")) {
								s = "UIFontSizeType = 0";
							}
							buffer.append(s + "\r\n");
						}
						reader.close();
						FileOutputStream outputStream = new FileOutputStream(f);
						byte[] bb = buffer.toString().getBytes();
						outputStream.write(bb);
						outputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (fileInputStream != null) {
						try {
							fileInputStream.close();
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}
				File[] temp_files = f2.listFiles(new FileFilter() {
					@Override
					public boolean accept(File pathname) {
						return pathname.isDirectory();
					}
				});
				for (File temp : temp_files) {
					temp = new File(temp.getPath() + "\\" + gameVariable);
					if (!temp.isFile())
						continue;
					try {
						InputStream is = new FileInputStream(temp);
						Enumeration<InputStream> streams = Collections.enumeration(Arrays.asList(
								new InputStream[] { new ByteArrayInputStream("<root>\n".getBytes()), is, new ByteArrayInputStream("</root>".getBytes()), }));
						SequenceInputStream seqStream = new SequenceInputStream(streams);

						// 定义工厂api
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						// 实例化
						DocumentBuilder builder = factory.newDocumentBuilder();

						Document document = builder.parse(seqStream);
						Element root = document.getDocumentElement();
						NodeList nodeList = root.getElementsByTagName("GameOption");
						int n = 0;
						for (int i = 0; i < nodeList.getLength(); i++) {
							// 获取一个节点
							Node node = nodeList.item(i);
							// 获取该节点所有属性
							NamedNodeMap attributes = node.getAttributes();
							for (int j = 0; j < attributes.getLength(); j++) {
								Node attribute = attributes.item(j);
								if (attribute.getNodeValue().equals("WorldmapOpenType")) {
									attribute = attributes.item(j - 1);
									if (attribute.getNodeValue().equals("3")) {
									} else {
										attribute.setNodeValue("3");
										bool = true;
									}
									n++;
								} else if (attribute.getNodeValue().equals("WorldMapCameraPitchType")) {
									attribute = attributes.item(j - 1);
									if (attribute.getNodeValue().equals("1")) {
									} else {
										attribute.setNodeValue("1");
										bool = true;
									}
									n++;
								} else if (attribute.getNodeValue().equals("UIFontSizeType")) {
									attribute = attributes.item(j - 1);
									if (attribute.getNodeValue().equals("0")) {
									} else {
										attribute.setNodeValue("0");
										bool = true;
									}
									n++;
								}
							}
							if (n == 3)
								break;
						}

						nodeList = root.getElementsByTagName("GameOptionGlobal");
						for (int i = 0; i < nodeList.getLength(); i++) {
							// 获取一个节点
							Node node = nodeList.item(i);
							// 获取该节点所有属性
							NodeList attributes = node.getChildNodes();
							// System.out.println(node.getNodeName());
							for (int j = 0; j < attributes.getLength(); j++) {
								Node node2 = attributes.item(j);
								if (node2.getNodeName().equals("UIFontSizeType")) {
									if (!node2.getAttributes().getNamedItem("Value").getNodeValue().equals("ChatFontSizeType_Normal")) {
										node2.getAttributes().getNamedItem("Value").setNodeValue("ChatFontSizeType_Normal");
										bool = true;
									}
									break;
								} else if (node2.getNodeName().equals("Fov")) {
									if (!node2.getAttributes().getNamedItem("Value").getNodeValue().equals("70")) {
										node2.getAttributes().getNamedItem("Value").setNodeValue("70");
										bool = true;
									}
									break;
								}
							}
						}

						TransformerFactory factory2 = TransformerFactory.newInstance();
						Transformer former = factory2.newTransformer();
						DOMSource xmlsSource = new DOMSource(root);
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						former.transform(xmlsSource, new StreamResult(outputStream));
						BufferedReader bReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray())));
						bReader.readLine();
						String s1 = bReader.readLine();
						BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
						String s2 = bReader.readLine();
						do {
							writer.write(s1);
							writer.newLine();
							s1 = s2;
						} while ((s2 = bReader.readLine()) != null);
						writer.close();
						bReader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return bool;
			}
		});
		initstart.start();
	}

	@Override
	public void quit() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void dataInit() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void dataSave() {
		// TODO 自动生成的方法存根

	}

}
