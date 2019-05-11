package com.mugui.http.Bean;

public class DataTypeBean extends JsonBean {
	public static final String CLASS = "class";
	public static final String GET_TIME = "get_time";
	public static final String FILE = "file"; 
	private String type;
	private String body;
	private int code;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setCode(int hashCode) {
		this.code = hashCode;
	}

	public int getCode() {
		return code;
	}
}
