package com.mugui.script.ui;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.mugui.Dui.DButton;
import com.mugui.script.DataSave;
import com.mugui.script.FunctionBean;
import com.mugui.script.FunctionBean.FunctionParameter;

import cern.colt.Arrays;

public class FunctionTree extends JTree {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FunctionTree() {
		setCellRenderer((TreeCellRenderer) DataSave.loader.loadClassToObject("com.mugui.script.ui.FunctionTreeViewRanderer"));
		setLayout(null);
		addTreeSelectionListener(functionCheckListener);
		try {
			init();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private void init() throws ParserConfigurationException, SAXException, IOException {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode() {
			/**
			*
			*/
			private static final long serialVersionUID = 4074384291556704170L;
			DefaultMutableTreeNode treeNode = this;
			{
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				DefaultHandler handler = new DefaultHandler() {

					FunctionBean functionBean = null;
					String body = "";

					@Override
					public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
						switch (qName) {
						case "class_name":
						case "function":
						case "explanation":
						case "parameters":
							body = "";
							break;
						default:
							if (treeNode.getUserObject() != null) {
								DefaultMutableTreeNode temp = new DefaultMutableTreeNode();
								treeNode.add(temp);
								treeNode = temp;
							}
							functionBean = new FunctionBean(qName, null, null);
							treeNode.setUserObject(functionBean);
							body = "";
							break;
						}
					}

					public void characters(char[] ch, int start, int length) throws SAXException {
						body += new String(ch, start, length);
					};

					@Override
					public void endElement(String uri, String localName, String qName) throws SAXException {
						switch (qName) {
						case "class_name":
							functionBean.class_name = body;
							break;
						case "function":
							functionBean.function = body;
							break;
						case "explanation":
							functionBean.explanation = body;
							break;
						case "parameters":
							String temp[] = body.subSequence(1, body.length() - 1).toString().split(",");
							int index[] = new int[temp.length];
							for (int i = 0; i < index.length; i++) {
								index[i] = Integer.parseInt(temp[i].trim());
							}
							functionBean.parameters = index;
							body = "";
							break;
						default:
							treeNode = (DefaultMutableTreeNode) treeNode.getParent();
						}
					}

				};
				InputStream inputStream;
				while ((inputStream = DataSave.loader.getResourceAsStream("Function.xml")) == null)
					;
				parser.parse(inputStream, handler);
			}
		};

		functionModel = new DefaultTreeModel(root);
		setModel(functionModel);

	}

	private TreeModel functionModel = null;
	private TreeSelectionListener functionCheckListener = new TreeSelectionListener() {
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
			Rectangle rectangle = FunctionTree.this.getPathBounds(e.getPath());
			if (rectangle == null) {
				now_functionbean = null;
				FunctionTree.this.remove(button);
				return;
			}
			if (((FunctionBean) node.getUserObject()).function != null) {
				now_functionbean = (FunctionBean) node.getUserObject();
				button.setBounds(FunctionTree.this.getWidth() - 35, rectangle.y + 3, 30, 20);
				FunctionTree.this.add(button);
				FunctionTree.this.validate();
			} else {
				now_functionbean = null;
				FunctionTree.this.remove(button);
			}
		}
	};
	FunctionBean now_functionbean = null;
	// private TreeCellRenderer viewRanderer = null;
	private DButton button = new DButton("插入", null) {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			setFont(new Font("宋体", Font.PLAIN, 12));
		}
	};

	public void addActionListener(ActionListener actionListener) {
		button.addActionListener(actionListener);

	}

	// }

	// private void init() throws TransformerConfigurationException,
	// SAXException {
	// functionModel = new DefaultTreeModel(new DefaultMutableTreeNode(new
	// FunctionBean("函数", null, "里面有非常实用的[name]")) {
	// /**
	// *
	// */
	// private static final long serialVersionUID = 1L;
	// {
	//
	// DefaultMutableTreeNode node_1;
	// node_1 = new DefaultMutableTreeNode(new FunctionBean("鼠标函数", null,
	// "里面存有非常实用的[name]"));
	// node_1.add(new DefaultMutableTreeNode(
	// new FunctionBean("鼠标按下", "mousePress", "使用方法：<br> [name]
	// 参数1<br><br>参数1:鼠标的虚拟键值码", FunctionParameter.MOUSE_BUTTON)));
	// node_1.add(new DefaultMutableTreeNode(
	// new FunctionBean("鼠标释放", "mouseRelease", "使用方法：<br> [name]参数1<br><br>参数1:
	// 鼠标的虚拟键值码", FunctionParameter.MOUSE_BUTTON)));
	// node_1.add(new DefaultMutableTreeNode(
	// new FunctionBean("鼠标滑轮滚动", "mouseWheel", "使用方法：<br> [name]
	// 参数1<br><br>参数1: 小于0 则滚轮向上 反之向下,参数1大小决定滚轮滑动幅度", FunctionParameter.INT)));
	// node_1.add(new DefaultMutableTreeNode(new FunctionBean("鼠标单击1次",
	// "mousePressOne",
	// "使用方法：<br> [name] 参数1 参数2<br><br>参数1：鼠标的虚拟键值码<br>参数2：鼠标按下与释放之间的间隔，可不填写",
	// FunctionParameter.MOUSE_BUTTON, FunctionParameter.INT)));
	// node_1.add(new DefaultMutableTreeNode(new FunctionBean("鼠标移动",
	// "mouseMove", "使用方法：<br> [name] 参数1 参数2<br><br>参数1：屏幕x位置<br>参数2：屏幕y位置",
	// FunctionParameter.INT, FunctionParameter.INT)));
	// node_1.add(new DefaultMutableTreeNode(
	// new FunctionBean("鼠标移动并单击1次", "mouseMovePressOne", "使用方法：<br> [name] 参数1
	// 参数2 参数3<br><br>参数1：屏幕x位置<br>参数2：屏幕y位置<br>参数3：鼠标的虚拟键值码",
	// FunctionParameter.INT, FunctionParameter.INT,
	// FunctionParameter.MOUSE_BUTTON)));
	// add(node_1);
	// node_1 = new DefaultMutableTreeNode(new FunctionBean("键盘函数", null,
	// "里面存有非常实用的[name]"));
	// node_1.add(new DefaultMutableTreeNode(
	// new FunctionBean("按下某个键", "keyPress", "使用方法：<br> [name]
	// 参数1<br><br>参数1：键盘的虚拟键值码", FunctionParameter.KEY_CODE)));
	// node_1.add(new DefaultMutableTreeNode(
	// new FunctionBean("释放某个键", "keyRelease", "使用方法：<br> [name] 参数1
	// <br><br>参数1：键盘的虚拟键值码", FunctionParameter.KEY_CODE)));
	// node_1.add(new DefaultMutableTreeNode(new FunctionBean("键盘单击1次",
	// "keyRelease",
	// "使用方法：<br> [name] 参数1 参数2<br><br>参数1：键盘的虚拟键值码<br>参数2：键盘按下与释放之间的间隔，可不填写",
	// FunctionParameter.KEY_CODE)));
	// add(node_1);
	// node_1 = new DefaultMutableTreeNode(new FunctionBean("其他函数", null,
	// "里面存有非常实用的[name]"));
	// node_1.add(new DefaultMutableTreeNode(new FunctionBean("延迟函数", "delay",
	// "使用方法：<br> [name] 参数1 <br><br>参数1：延迟时间", FunctionParameter.INT)));
	// add(node_1);
	//
	// // 保存这颗树
	// SAXTransformerFactory factory = (SAXTransformerFactory)
	// SAXTransformerFactory.newInstance();
	// TransformerHandler handler = factory.newTransformerHandler();
	// Transformer transformer = handler.getTransformer();
	//
	// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	// transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	// StreamResult result = new StreamResult(new File(DataSave.JARFILEPATH +
	// "\\tree.xml"));
	// handler.setResult(result);
	// handler.startDocument();
	// String tab = "\n";
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length);
	// SaveTree(getRoot(), handler, tab + " ");
	// handler.endDocument();
	// }
	//
	// private void SaveTree(TreeNode root, TransformerHandler handler, String
	// tab) throws SAXException {
	// DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) root;
	// FunctionBean bean = (FunctionBean) treeNode.getUserObject();
	//
	// if (root.getChildCount() > 0) {
	// handler.startElement("", "", bean.name, null);
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length);
	// for (Enumeration<?> enumeration = root.children();
	// enumeration.hasMoreElements();) {
	// SaveTree((TreeNode) enumeration.nextElement(), handler, tab + " ");
	// if (enumeration.hasMoreElements())
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length);
	// else {
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length - 4);
	// }
	// }
	// handler.endElement("", "", bean.name);
	// } else {
	// handler.startElement("", "", bean.name, null);
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length);
	// handler.startElement("", "", "class_name", null);
	// handler.characters(bean.class_name.toCharArray(), 0,
	// bean.class_name.toCharArray().length);
	// handler.endElement("", "", "class_name");
	//
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length);
	//
	// handler.startElement("", "", "function", null);
	// handler.characters(bean.function.toCharArray(), 0,
	// bean.function.toCharArray().length);
	// handler.endElement("", "", "function");
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length);
	//
	// handler.startElement("", "", "explanation", null);
	// handler.characters(bean.explanation.toCharArray(), 0,
	// bean.explanation.toCharArray().length);
	// handler.endElement("", "", "explanation");
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length);
	//
	// handler.startElement("", "", "parameters", null);
	// handler.characters(Arrays.toString(bean.parameters).toCharArray(), 0,
	// Arrays.toString(bean.parameters).toCharArray().length);
	// handler.endElement("", "", "parameters");
	//
	// handler.characters(tab.toCharArray(), 0, tab.toCharArray().length - 4);
	//
	// handler.endElement("", "", bean.name);
	// }
	// }
	// });
	// setModel(functionModel);
	// }
}