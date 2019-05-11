package com.mugui.tool;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;

import com.mugui.ui.DataSave;

public class SerializeTool {
	/**
	 * 序列化
	 */
	public static byte[] serizlize(Object object) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = null;
		try {
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(object);
			return outputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 反序列化
	 */
	public static Object deserialize(byte[] data) {
		ObjectInputStream inputStream = null;
		try {
			inputStream = new ObjectInputStream(new ByteArrayInputStream(data));
			return inputStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 序列化到文件
	 */
	public static void serizlizeToFile(Object object, File file) {

		DataOutputStream outputStream = null;
		try {
			byte[] data = serizlize(object);
			if (data == null) {
				throw new NotSerializableException(object + " not serizlize");
			}
			outputStream = new DataOutputStream(new FileOutputStream(file));
			outputStream.writeBytes(DataSave.StaticUI.getApp_id() + "\n");
			outputStream.write(data);
			outputStream.flush();
		} catch (IOException e) {
			// e.printStackTrace();
			return;
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

	/**
	 * 文件反序列化到对象
	 */
	public static Object deserializeFileTo(File file) {
		DataInputStream inputStream = null;
		try {
			if (!file.isFile()) {
				throw new IOException(file.getName() + " no find");
			} 
			inputStream = new DataInputStream(new FileInputStream(file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			String string=reader.readLine();
			if(!DataSave.StaticUI.getApp_id().equals(string)) {
				if (file.isFile())
					file.delete();
				return null;
			}
			byte[] body = new byte[(int) file.length()];
			inputStream.readFully(body);
			return deserialize(body);
		} catch (IOException e) {
			if (file.isFile())
				file.delete();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
