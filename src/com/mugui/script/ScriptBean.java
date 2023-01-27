package com.mugui.script;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JOptionPane;

import org.eclipse.swt.internal.win32.OS;

import com.mugui.tool.Other;
import com.mugui.windows.Tool;
import com.mugui.windows.MouseListenerTool.KeyListener;

import net.sf.json.JSONObject;

public class ScriptBean {
	public String name;
	public String code;
	public String hot_key;

	/**
	 * 按键启动 按键关闭
	 */
	public static final int KEY_START_SHUTDOWN=0;

	/**
	 * 按住键盘启动 松开关闭
	 */
	public static final int KEY_KEEP_START=1;

	public int startupType=KEY_START_SHUTDOWN;

	public KeyListener keyListener = null;
	public byte[] body;
	public File file;

	public ScriptBean(String name, String code, String hot_key) {
		this(name,code,hot_key,KEY_START_SHUTDOWN);
	}

	public ScriptBean(String name, String code, String hot_key,int startupType) {
		this.code = code;
		this.name = name;
		this.hot_key = hot_key;
		this.startupType=startupType;
		keyListener = new KeyListener(this, OS.MapVirtualKey(Tool.getkeyCode(hot_key), 0)) {
			/**
			 * 启动脚本
			 */
			@Override
			public void callback(Object object) {
				ScriptBean.startScript((ScriptBean) object);
			}

			/**
			 * 停止脚本
			 * @param object
			 */
			@Override
			public void callRelease(Object object) {
				ScriptBean.stopScript((ScriptBean) object);
			}

		};
	}


	public void setHotKey(String text) {
		hot_key = text;
		keyListener.key = OS.MapVirtualKey(Tool.getkeyCode(hot_key), 0);
	}

	@Override
	public String toString() {
		JSONObject object = new JSONObject();
		object.put("name", name);
		object.put("code", code);
		object.put("hot_key", hot_key);
		return object.toString();
	}

	public void saveToFile() {
		if (!file.getParentFile().isDirectory()) {
			file.getParentFile().mkdirs();
		}
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			FileChannel channel = randomAccessFile.getChannel();
			FileLock lock = channel.tryLock();
			if (lock == null) {
				JOptionPane.showMessageDialog(null, "文件正在被使用:" + file.getPath(), "错误", JOptionPane.OK_OPTION);
			}
			if (lock.isValid()) {
				randomAccessFile.write((toString()).getBytes(Charset.forName("UTF-8")));
				if (body != null)
					randomAccessFile.write(Other.ArrayBytesEncryption(body));

				randomAccessFile.setLength(randomAccessFile.getFilePointer());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static ScriptBean newInstance(File file) {
		ScriptBean bean = null;

		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			FileChannel channel = randomAccessFile.getChannel();
			FileLock lock = channel.tryLock();
			if (lock == null) {
				JOptionPane.showMessageDialog(null, "文件正在被使用:" + file.getPath(), "错误", JOptionPane.OK_OPTION);
			}
			if (lock.isValid()) {
				if (randomAccessFile.read() != '{')
					return null;
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				byteArrayOutputStream.write('{');
				int b;
				while ((b = randomAccessFile.read()) != '}')
					byteArrayOutputStream.write(b);
				byteArrayOutputStream.write(b);
				JSONObject object = JSONObject.fromObject(byteArrayOutputStream.toString("UTF-8"));
				if (object == null || object.isNullObject()) {
					byteArrayOutputStream.close();
					return null;
				}
				byteArrayOutputStream.reset();
				String name = object.getString("name");
				String code = object.getString("code");
				String hot_key = object.getString("hot_key");
				bean = new ScriptBean(name, code, hot_key);
				bean.file = file;
				byte[] byt = new byte[1024];
				int n = 0;
				while ((n = randomAccessFile.read(byt)) > 0) {
					byteArrayOutputStream.write(byt, 0, n);
				}
				byteArrayOutputStream.close();
				byt = byteArrayOutputStream.toByteArray();
				if (byt.length < 2)
					return bean;
				bean.body = Other.ArrayBytesDecrypt(byt);
				return bean;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	public void removeFile() {
		file.delete();
	}

	private static final ConcurrentHashMap<String,ScriptThread> scriptThreads = new ConcurrentHashMap<>();

	protected static void startScript(ScriptBean object) {
		if (object.body == null)
			return;
		 ScriptThread scriptThread=scriptThreads.get(object.code);
 		 if(scriptThread==null||!scriptThread.isAlive()){
			 synchronized (scriptThreads){
				 scriptThread=scriptThreads.get(object.code);
				 if(scriptThread==null||!scriptThread.isAlive()){
					 ScriptThread thread = new ScriptThread(object);
					 thread.setControllableStop(false);
					 scriptThreads.put(object.code, thread);
					 System.out.println("启动脚本"+object.toString());
					 thread.start();
				 }
			 }
		 }else if(scriptThread.isControllableStop()&&object.startupType==ScriptBean.KEY_START_SHUTDOWN){
			 scriptThread.Stop();
			 scriptThreads.remove(object.code);
		 }
	}

	private static void stopScript(ScriptBean object) {
		ScriptThread scriptThread= scriptThreads.get(object.code);
		if(scriptThread!=null&&object.startupType==ScriptBean.KEY_KEEP_START){
			scriptThread.Stop();
			scriptThreads.remove(object.code);
		}else {
			if(scriptThread!=null){
				scriptThread.setControllableStop(true);
			}
		}
	}
	public static void stopAll() {
		Iterator<Map.Entry<String, ScriptThread>> iterator = scriptThreads.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, ScriptThread> next = iterator.next();
			next.getValue().Stop();
			iterator.remove();
		}
	}

}
