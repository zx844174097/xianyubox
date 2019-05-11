package com.mugui.http.Bean;

import net.sf.json.JSONObject;

public class CMDBean extends JsonBean {
	private String user_id;
	private String user_text;

	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_id", getUser_id());
		jsonObject.put("user_text", getUser_text());
		return jsonObject;
	}

	public CMDBean(JSONObject jsonObject) {
		super(jsonObject);
	}

	public CMDBean() {
		this(null);
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_text() {
		return user_text;
	}

	public void setUser_text(String user_text) {
		this.user_text = user_text;
	}

	@Override
	public void InitBean(JSONObject jsonObject) {
		if (jsonObject == null)
			return;
		if (jsonObject.get("user_id") != null)
			setUser_id(jsonObject.getString("user_id"));
		if (jsonObject.get("user_text") != null)
			setUser_text(jsonObject.getString("user_text"));
	}

}
