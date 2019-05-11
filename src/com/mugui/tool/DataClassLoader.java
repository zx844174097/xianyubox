package com.mugui.tool;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.mugui.DataClassLoaderInterface;
import com.mugui.http.Bean.DataTypeBean;
import com.mugui.http.Bean.UserBean;
import com.mugui.http.pack.Bag;
import com.mugui.http.pack.TcpBag;
import com.mugui.model.TCPModel;

public class DataClassLoader extends DataClassLoaderInterface {

	@Override
	protected synchronized Class<?> findClass(String name) throws ClassNotFoundException {
		Class<?> clazz = null;
		clazz = this.findLoadedClass(name); // 父类已加载
		if (clazz == null) {
			String name_t = name.replace('.', '/') + ".class";
			byte[] classData = getClassData(name_t);
			if (classData == null) {
				return null;
			}
			clazz = defineClass(name, classData, 0, classData.length); // 将class的字节码数组转换成Class类的实例
		} 
		return clazz;
	}

	private byte[] getClassData(String name) {
		TcpBag bag = new TcpBag();
		bag.setBag_id(Bag.GET_DATA);
		DataTypeBean bean = new DataTypeBean();
		bean.setType(DataTypeBean.CLASS);
		bean.setBody(name);
		bean.setCode(bag.hashCode());
		bag.setBody_description(bean.toString());
		bag.setBody(new UserBean().toJsonObject());
		bag = TCPModel.waitAccpetSend(bag);
		if (bag.getBody() instanceof byte[])
			return (byte[]) bag.getBody();
		else {
			System.out.println(name + " is null");
			return null;
		}
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		InputStream inputStream = getClass().getResourceAsStream("/" + getDataClassPath() + name);
		if (inputStream != null) {
			return inputStream;
		}
		byte[] body = getClassData(getDataClassPath() + name);
		if (body == null) {
			return null;
		}
		return new ByteArrayInputStream(body);
	}
}
