package com.mugui.http.Bean;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JGOtherBean {
	public LinkedList<JGBean> body = new LinkedList<JGOtherBean.JGBean>();

	public LinkedList<JGBean> getBody() {
		return body;
	}

	public void setBody(String body) {
		JSONArray array = JSONArray.fromObject(body);
		@SuppressWarnings("unchecked")
		Iterator<String> iterator = array.iterator();
		while (iterator.hasNext()) {
			JGBean bean = new JGBean(JSONObject.fromObject(iterator.next()));
			this.body.add(bean);
		}
	}

	public void putBody(JGBean bean) {
		body.add(bean);
	}

	public void removeBody(JGBean bean) {
		body.remove(bean);
	}

	@Override
	public String toString() {
		JSONArray array = new JSONArray();
		Iterator<JGBean> iterator = body.iterator();
		while (iterator.hasNext()) {
			String s = iterator.next().toString();
			array.add(s);
		}
		return array.toString();
	}

	public static class JGBean extends JsonBean {

		private int row = 0;
		private int column = 0;
		private BufferedImage image = null;
		private String model = "加工";
		private int num = 0;
		private int n = 0;

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public int getN() {
			return n;
		}

		public void setN(int n) {
			this.n = n;
		}

		public BufferedImage getImage() {
			return image;
		}

		public void setImage(BufferedImage image) {
			this.image = image;
		}

		public JGBean(JSONObject jsonObject) {
			InitBean(jsonObject);
		}

		public JGBean() {
			this(null);
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		@Override
		public JSONObject toJsonObject() {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("column", getColumn());
			jsonObject.put("row", getRow());
			jsonObject.put("model", getModel());
			jsonObject.put("num", getNum());
			jsonObject.put("n", getN());
			return jsonObject;
		}

		@Override
		public void InitBean(JSONObject jsonObject) {
			if (jsonObject == null)
				return;
			if (jsonObject.get("row") != null)
				setRow(jsonObject.getInt("row"));
			if (jsonObject.get("column") != null)
				setColumn(jsonObject.getInt("column"));
			if (jsonObject.get("n") != null)
				setN(jsonObject.getInt("n"));
			if (jsonObject.get("num") != null)
				setNum(jsonObject.getInt("num"));
			if (jsonObject.get("model") != null)
				setModel(jsonObject.getString("model"));
		}

	}
}
