package com.mugui.http.Bean;

import net.sf.json.JSONObject;

public class FileBean extends JsonBean {
	private String file_name;
	private int file_page_all_size;
	private int file_page_number;
	private int file_seek;
	private String other_description;
	private String user_id; 
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@Override
	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("file_name", file_name);
		jsonObject.put("file_page_all_size", file_page_all_size);
		jsonObject.put("file_page_number", file_page_number);
		jsonObject.put("file_seek", file_seek);
		jsonObject.put("other_description", other_description);
		jsonObject.put("user_id", user_id);
		return jsonObject;
	}

//	@Override
//	public void InitBean(JSONObject jsonObject) {
//		if (jsonObject == null)
//			return;
//		if (jsonObject.get("other_description") != null)
//			setOther_description(jsonObject.getString("other_description"));
//		if (jsonObject.get("file_name") != null)
//			setFile_name(jsonObject.getString("file_name"));
//		if (jsonObject.get("file_page_all_size") != null)
//			setFile_page_all_size(jsonObject.getInt("file_page_all_size"));
//		if (jsonObject.get("file_page_number") != null)
//			setFile_page_number(jsonObject.getInt("file_page_number"));
//		if (jsonObject.get("file_seek") != null)
//			setFile_seek(jsonObject.getInt("file_seek"));
//		if (jsonObject.get("user_id") != null)
//			setUser_id(jsonObject.getString("user_id"));
//	}

//	public FileBean() {
//		super();
//	}
//
//	public FileBean(JSONObject object) {
//		super(object);
//	}
	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getFile_page_all_size() {
		return file_page_all_size;
	}

	public void setFile_page_all_size(int file_page_all_size) {
		this.file_page_all_size = file_page_all_size;
	}

	public int getFile_page_number() {
		return file_page_number;
	}

	public void setFile_page_number(int file_page_number) {
		this.file_page_number = file_page_number;
	}

	public int getFile_seek() {
		return file_seek;
	}

	public void setFile_seek(int file_seek) {
		this.file_seek = file_seek;
	}

	public String getOther_description() {
		return other_description;
	}

	public void setOther_description(String other_description) {
		this.other_description = other_description;
	}

}
