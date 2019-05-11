package com.mugui.http.Bean;

import net.sf.json.JSONObject;

public class MouseInfoBean extends JsonBean {
	private String mail = null;
	private int x;
	private int y;
	private int button;
	private int chick;
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public MouseInfoBean(JSONObject jsonObject) {
		InitBean(jsonObject);
	}

	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mail", getMail());
		jsonObject.put("x", getX());
		jsonObject.put("y", getY());
		jsonObject.put("button", getButton());
		jsonObject.put("chick", getChick());
		jsonObject.put("time", getTime());
		return jsonObject;
	}

	public MouseInfoBean() {
		this(null);
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getButton() {
		return button;
	}

	public void setButton(int button) {
		this.button = button;
	}

	public int getChick() {
		return chick;
	}

	public void setChick(int chick) {
		this.chick = chick;
	}

	public void InitBean(JSONObject jsonObject) {
		if (jsonObject == null)
			return;
		if (jsonObject.get("mail") != null)
			setMail(jsonObject.getString("mail"));
		if (jsonObject.get("x") != null)
			setX(jsonObject.getInt("x"));
		if (jsonObject.get("y") != null)
			setY(jsonObject.getInt("y"));
		if (jsonObject.get("button") != null)
			setButton(jsonObject.getInt("button"));
		if (jsonObject.get("chick") != null)
			setChick(jsonObject.getInt("chick"));
		if (jsonObject.get("time") != null)
			setTime(jsonObject.getLong("time"));

	}

}
