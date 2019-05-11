package com.mugui.ew.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;
import com.mugui.DataSaveInterface;
import com.mugui.ModelInterface;
import com.mugui.Dui.DPanel;
import com.mugui.ew.EWUIHandel;
import com.mugui.ew.ui.KRLangTranslate;
import com.mugui.tool.ImgTool;
import com.mugui.tool.Other;

public class KRLangTranslateModel implements ModelInterface {

	private DPanel muguiBrowser;
	private DataSaveInterface datasave = null;
	private KRLangTranslate father = null;

	@Override
	public void init() {
		if (datasave == null)
			datasave = EWUIHandel.datasave;
		if (father == null) {
			father = (KRLangTranslate) datasave.getUIManager().get("KRLangTranslate");
		}
		if (muguiBrowser == null) {
			muguiBrowser = datasave.getUIManager().get("MuguiBrowser");
			muguiBrowser.dataInit();
			muguiBrowser.init();
		}
	}

	@Override
	public void start() {
	}

	@Override
	public boolean isrun() {
		return false;
	}

	@Override
	public void stop() {
	}

	private static final String APP_ID = "9858942";
	private static final String API_KEY = "t4KRUrdgTZ3sETXHLjQrpqKM";
	private static final String SECRET_KEY = "xslFh6oU9Vmpmar83IfCACnB4ekCWa0P";
	private AipOcr ocr = null;
	{

		ocr = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

	}

	public String ImgOCR_KOR(BufferedImage image) {
		// 自定义参数定义
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("detect_direction", "false");
		options.put("language_type", "KOR");

		// 参数为本地图片路径
		JSONObject response = ocr.basicGeneral(ImgTool.ImgToByteArray(image), options);
		if (response.isNull("words_result"))
			return null;
		JSONArray array = response.getJSONArray("words_result");
		Iterator<Object> iterator = array.iterator();
		String ret = "";
		while (iterator.hasNext()) {
			JSONObject value = (JSONObject) iterator.next();
			ret += value.getString("words") + "\r\n";
		}
		return ret;
	}

	public String RenderKOR(String text) {
		URL url;
		try {
			String urll = "http://api.fanyi.baidu.com/api/trans/vip/translate?";
			urll += "q=" + URLEncoder.encode(text, "UTF-8");
			urll += "&from=auto";
			urll += "&to=zh";
			urll += "&appid=20181116000235405";
			String uuid = Other.getShortUuid();
			urll += "&salt=" + uuid;
			String temp = ("20181116000235405" + text + uuid + "6jWYBMZjcVMfgQl5lRyO");
			urll += "&sign=" + Other.MD5(temp);
			url = new URL(urll);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			int code = connection.getResponseCode();
			if (code == 200) {
				byte[] bs = new byte[1024];
				InputStream inputStream = connection.getInputStream();
				int len = 0;
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				while ((len = inputStream.read(bs)) > 0) {
					outputStream.write(bs, 0, len);

				}
				String ret = outputStream.toString("UTF-8");
				JSONObject object = new JSONObject(ret);
				if (object.isNull("trans_result"))
					return null;
				JSONArray array = object.getJSONArray("trans_result");
				Iterator<Object> iterator = array.iterator();
				ret = "";
				while (iterator.hasNext()) {
					ret += ((JSONObject) iterator.next()).getString("dst") + "\r\n";
				}
				System.out.println("识别出的文字:"+ret);  
				return ret;
			}
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
