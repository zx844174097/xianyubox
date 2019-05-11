package com.mugui.http.Bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

public class InfoBean extends JsonBean {
	public static final String ERROR = "error";
	public static final String SUCCESS = "success";
	public static final String OUTLOGINERROR = "outloginerror";
	public static final String MERCHANT_NEW_ORDER = "merchant_new_order";
	public static final String USER_NEW_ORDER = "user_new_order";
	private String time = null;
	private String message = null;
	private Object body = null;
	private String type = null;
	private String user_mail = UserBean.getDUser_mail();
	private String user_mac = UserBean.getDUser_mac();

	public String getUser_mac() {
		return user_mac;
	}

	public void setUser_mac(String user_mac) {
		this.user_mac = user_mac;
	}

	public String getUser_mail() {
		return user_mail;
	}

	public void setUser_mail(String user_mail) {
		this.user_mail = user_mail;
	}

	public InfoBean(JSONObject jsonObject) {
		InitBean(jsonObject);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getId() {
		return type;
	}

	public void setId(String id) {
		this.type = id;
	}

	public InfoBean() {
		this(null, null);
	}

	public InfoBean(String type, String message) {
		this.type = type;
		this.message = message;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time = df.format(new Date());
	}

	public String getInfo() {
		return message;
	}

	public void setInfo(String message) {
		this.message = message;
	}

	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", type);
		jsonObject.put("time", time);
		jsonObject.put("message", message);
		jsonObject.put("body", body);
		jsonObject.put("user_mail", user_mail);
		jsonObject.put("user_mac", user_mac);
		return jsonObject;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public void InitBean(JSONObject jsonObject) {
		if (jsonObject == null)
			return;
		if (jsonObject.get("time") != null)
			setTime(jsonObject.getString("time"));
		if (jsonObject.get("message") != null)
			setMessage(jsonObject.getString("message"));
		body = jsonObject.get("body");
		if (jsonObject.get("type") != null)
			setType(jsonObject.getString("type"));
		if (jsonObject.get("user_mail") != null)
			setUser_mail(jsonObject.getString("user_mail"));
		if (jsonObject.get("user_mac") != null)
			setUser_mail(jsonObject.getString("user_mac"));
	}

}
