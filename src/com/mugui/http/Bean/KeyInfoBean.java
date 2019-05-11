package com.mugui.http.Bean;

import net.sf.json.JSONObject;

public class KeyInfoBean extends JsonBean {
	private String mail = null;
	private int key;
	private int chick;

	public KeyInfoBean(JSONObject jsonObject) {
		InitBean(jsonObject);
	}

	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mail", getMail());
		jsonObject.put("key", getKey());
		jsonObject.put("chick", getChick());
		return jsonObject;
	}

	public KeyInfoBean() {
		this(null);
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getChick() {
		return chick;
	}

	public void setChick(int chick) {
		this.chick = chick;
	}

	@Override
	public void InitBean(JSONObject jsonObject) {

		if (jsonObject == null)
			return;
		if (jsonObject.get("mail") != null)
			setMail(jsonObject.getString("mail"));
		if (jsonObject.get("key") != null)
			setKey(jsonObject.getInt("key"));
		if (jsonObject.get("chick") != null)
			setChick(jsonObject.getInt("chick"));

	
	}

}
