package com.mugui.http.Bean;

import net.sf.json.JSONObject;

public class ImgBean extends JsonBean {
	private String user_id = null;
	private String body = null;

	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("user_id", getUser_id());
		jsonObject.put("body", body);
		return jsonObject;
	}

	public ImgBean(JSONObject jsonObject) {
		InitBean(jsonObject);

	}

	public ImgBean() {
		this(null);
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public void  InitBean(JSONObject jsonObject) {
		if (jsonObject == null)
			return ;
		if (jsonObject.get("user_id") != null)
			setUser_id(jsonObject.getString("user_id"));
		if (jsonObject.get("body") != null)
			setBody(jsonObject.getString("body"));
	}

}
