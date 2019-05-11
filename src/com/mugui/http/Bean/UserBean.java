package com.mugui.http.Bean;

import com.mugui.ui.DataSave;

import net.sf.json.JSONObject;

public class UserBean extends JsonBean {
	private String user_id;
	private String user_name;
	private String user_post;
	private String user_host;
	private String user_sequence;// 机器码
	private String user_mac;// 网络码
	private String user_pc_name;
	private String user_pc_win;
	private String user_mail;
	private String user_passwd;
	private String code;
	private String user_snake_mark;
	public static String CODE = null;

	public UserBean(JSONObject jsonObject) {
		InitBean(jsonObject);
	}

	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_id", getUser_id());
		jsonObject.put("user_name", getUser_name());
		jsonObject.put("user_mail", getUser_mail());
		jsonObject.put("user_passwd", getUser_passwd());
		jsonObject.put("user_post", getUser_post());
		jsonObject.put("user_host", getUser_host());
		jsonObject.put("user_sequence", getUser_sequence());
		jsonObject.put("user_mac", getUser_mac());
		jsonObject.put("user_pc_name", getUser_pc_name());
		jsonObject.put("user_pc_win", getUser_pc_win());
		jsonObject.put("code", getCode());
		jsonObject.put("user_snake_mark", getUser_snake_mark());
		return jsonObject;
	}

	public UserBean() {
		this(null);
	}

	public String getUser_snake_mark() {
		return user_snake_mark;
	}

	public void setUser_snake_mark(String user_snake_mark) {
		this.user_snake_mark = user_snake_mark;
	}

	public String getUser_mail() {
		return user_mail;
	}

	public void setUser_mail(String user_mail) {
		this.user_mail = user_mail;
	}

	public String getUser_passwd() {
		return user_passwd;
	}

	public void setUser_passwd(String user_passwd) {
		this.user_passwd = user_passwd;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUser_post() {
		return user_post;
	}

	public void setUser_post(String user_post) {
		this.user_post = user_post;
	}

	public String getUser_host() {
		return user_host;
	}

	public void setUser_host(String user_host) {
		this.user_host = user_host;
	}

	public String getUser_sequence() {
		return user_sequence;
	}

	public void setUser_sequence(String user_sequence) {
		this.user_sequence = user_sequence;
	}

	public String getUser_mac() {
		return user_mac;
	}

	public void setUser_mac(String user_mac) {
		this.user_mac = user_mac;
	}

	public String getUser_pc_name() {
		return user_pc_name;
	}

	public void setUser_pc_name(String user_pc_name) {
		this.user_pc_name = user_pc_name;
	}

	public String getUser_pc_win() {
		return user_pc_win;
	}

	public void setUser_pc_win(String user_pc_win) {
		this.user_pc_win = user_pc_win;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	@Override
	public String toString() {
		return toJsonObject().toString();
	}

	@Override
	public void InitBean(JSONObject jsonObject) {
		if (DataSave.userBean != null && !DataSave.userBean.toString().equals("{}")) {
			setUser_mail(DataSave.userBean.user_mail);
		}
		if (CODE != null && !CODE.trim().equals("")) {
			setUser_mac(CODE);
		}
		if (jsonObject == null)
			return;
		if (jsonObject.get("user_id") != null)
			setUser_id(jsonObject.getString("user_id"));
		if (jsonObject.get("user_name") != null)
			setUser_name(jsonObject.getString("user_name"));
		if (jsonObject.get("user_post") != null)
			setUser_post(jsonObject.getString("user_post"));
		if (jsonObject.get("user_host") != null)
			setUser_host(jsonObject.getString("user_host"));
		if (jsonObject.get("user_sequence") != null)
			setUser_sequence(jsonObject.getString("user_sequence"));
		if (jsonObject.get("user_mac") != null)
			setUser_mac(jsonObject.getString("user_mac"));
		if (jsonObject.get("user_pc_name") != null)
			setUser_pc_name(jsonObject.getString("user_pc_name"));
		if (jsonObject.get("user_pc_win") != null)
			setUser_pc_win(jsonObject.getString("user_pc_win"));
		if (jsonObject.get("user_mail") != null)
			setUser_mail(jsonObject.getString("user_mail"));
		if (jsonObject.get("user_passwd") != null)
			setUser_passwd(jsonObject.getString("user_passwd"));
		if (jsonObject.get("code") != null)
			setCode(jsonObject.getString("code"));
		if (jsonObject.get("user_snake_mark") != null)
			setUser_snake_mark(jsonObject.getString("user_snake_mark"));
	}

	public static String getDUser_mail() {
		if (DataSave.userBean == null)
			return null;
		return DataSave.userBean.getUser_mail();
	}

	public static String getDUser_mac() {
		return CODE;
	}
}
