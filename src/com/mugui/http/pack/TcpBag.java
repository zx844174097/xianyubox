package com.mugui.http.pack;

import java.nio.charset.Charset;

import com.mugui.tool.Other;
import com.mugui.ui.DataSave;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TcpBag implements Bag {


	private String bag_id;
	private String host;
	private int port;
	private Object body;
	private int body_len;
	private Object body_description = "";
	private String version = DataSave.StaticUI.getApp_id();

	public Object getBody_description() {
		return body_description;
	}

	public void setBody_description(Object body_description) {
		this.body_description = body_description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getBody_len() {
		return body_len;
	}

	public void setBody_len(int body_len) {
		this.body_len = body_len;
	}

	public String getBag_id() {
		return bag_id;
	}

	public void setBag_id(String bag_id) {
		this.bag_id = bag_id;
	}

	public void setBody(JSONObject jsonObject) {
		this.body = jsonObject;
	}

	public void setBody(JSONArray jsonArray) {
		this.body = jsonArray;
	}

	public void setBody(Object object) {
		this.body = object;
	}

	public void setBody(String string) {
		this.body = string;
	}

	public Object getBody() {
		return body;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void setJsonObject(JSONObject jsonObject) {
		if (jsonObject == null)
			return;
		if (jsonObject.get("version") != null)
			setVersion(jsonObject.getString("version"));
		if (jsonObject.get("bag_id") != null)
			setBag_id(jsonObject.getString("bag_id"));
		setBody(jsonObject.get("body"));
		setBody_description(jsonObject.get("body_description"));
		if (jsonObject.get("body_len") != null)
			setBody_len(jsonObject.getInt("body_len"));
	}

	@Override
	public String toString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("bag_id", bag_id);
		if (body != null)
			jsonObject.put("body", body.toString());
		if (body_description != null)
			jsonObject.put("body_description", body_description.toString());
		jsonObject.put("body_len", body_len);
		jsonObject.put("version", version);
		return "<String>" + this.getClass().getName() + "<|>" + jsonObject.toString();
	}

	public void setBag_id(int bag) {
		bag_id = bag + "";
	}

	@Override
	public byte[] toByteArrays() {
		if (body != null && body instanceof byte[]) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("bag_id", bag_id);
			jsonObject.put("body_len", body_len);
			jsonObject.put("version", version);
			jsonObject.put("body_description", body_description);
			String s = "<ByteArrays>" + this.getClass().getName() + "<|>" + jsonObject.toString() + "<body>";
			byte b2[] = s.getBytes(Charset.forName("UTF-8"));
			b2 = Other.ArraysToNewArray(b2, (byte[]) body);
			return b2;
		}
		return toString().getBytes(Charset.forName("UTF-8"));
	}

	@Override
	public void setByteArrays(String bag_id, byte[] ByteArrays) {
		setJsonObject(JSONObject.fromObject(bag_id));
		body = ByteArrays;
	}

}
