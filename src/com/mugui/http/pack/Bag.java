/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.mugui.http.pack;

import net.sf.json.JSONObject;

public abstract interface Bag { 
	public static final String String = "<String>";
	public static final String ByteArrays = "<ByteArrays>";
	public static final String KEY_INFO = "key_info";
	public static final String MOUSE_INFO = "mouse_info";
	public static final int LOGIN = 1;
	public static final int REG = 2;
	public static final int REG_CODE = 3;
	public static final int ERROR = 0;
	public static final int OUT_LOGIN = 4;
	public static final int DY_CODE = 5;
	public static final int GET_DYTIME = 6;
	public static final int GET_QPTIME = 7;
	public static final int LOGIN_SEND = 8;
	public static final int REG_SEND = 9;
	public static final int REG_CODE_SEND = 10;
	public static final int SET_USER_NAME = 11;
	public static final int SAVE_SNAKE_MARK = 12;
	public static final int SNAKE_MARK_ALL = 13;
	public static final int APP_OLD = 14;
	public static final int GET_JGTIME = 15;
	public static final int GET_DSTIME = 16;
	public static final int GET_MYTIME = 17;
	public static final int GET_LJTIME = 31;
	public static final int UPDATE_USER_PAWD = 18;
	public static final int UPDATE_REG_CODE = 19;
	public static final int SEND_FISH_LINE_FEATURE = 20;
	public static final int SEND_LINE_ALL_FISH_PRICE = 21;
	public static final int SEND_ONE_FISH_LINE_FEATURE = 22;
	public static final int GET_LINE_ALL_YU_BODY = 23;
	public static final int SEND_BOLD_ONE = 24;
	public static final int SEND_ONE_BOLD_LINE = 25;
	public static final int GET_BOLD_LINE = 26;
	public static final int SEND_DEL_BOLD_ONE = 27; 
	public static final int SEND_NEW_BOSS_UPDATE_TIME = 28;
	public static final int SEND_BOSS_UPDATE_ONE = 29; 
	public static final int GET_ALL_BOSS_UPDATE_TIME = 30; 
	public static final int APP_USER_LOGIN = 32; 
	public static final int APP_SEND_START_LISTENER_1 = 33;
	public static final int APP_SEND_START_LISTENER_2 = 34;
	public static final int PC_SEND_APP_INFO = 35; 
	public static final int SEND_MAN_LISTENER = 36; 
	public static final int STOP_MAN_LISTENER = 37;
	public static final int SEND_START_DY = 38;
	public static final int SEND_STOP_DY = 39;
	public static final int SEND_STOP_MAN_LISTENER = 40;
	public static final int SEND_PC_SEND_INFO = 41;
	public static final int SEND_RECEIVE_CDK = 42; 
	public static final int GET_DATA = 43;  
	public static final String GET_USER_LIST = "get_user_list";
	public static final String USER_CMD_INFO = "user_cmd_info";
	public static final String ADMIN_CMD_INFO = "admin_cmd_info";
	public static final String USER_WINDOWS_IMG = "user_windows_img";
	public static final String GET_USER_WINDOWS = "get_user_windows";
	public static final String STOP_USER_WINDOW = "stop_user_window";

	public static final String SELECT_APP_ID = "select_app_id";
	public static final String START_DOWNLOAD_FILE = "start_download_file";
	public static final String RE_DOWNLOAD_FILE = "re_download_file";

	public abstract void setPort(int paramInt);

	public abstract void setHost(String paramString);

	public abstract void setJsonObject(JSONObject paramJSONObject);

	public abstract byte[] toByteArrays();

	public abstract String toString();

	public abstract void setByteArrays(String paramString, byte[] paramArrayOfByte);

	public abstract String getHost();

	public abstract int getPort();
}