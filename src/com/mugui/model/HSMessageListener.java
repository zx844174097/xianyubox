package com.mugui.model;

import com.mugui.jni.Tool.DJni;
import com.mugui.ui.part.GameListenerThread;

public class HSMessageListener extends Thread {

	private static HSMessageListener INSTANCE = new HSMessageListener();

	public static void Start() {
		if (!INSTANCE.isAlive()) {
			INSTANCE = new HSMessageListener();
		}
		INSTANCE.start(); 

	} 

	private DJni jni = GameListenerThread.DJNI;
  
	@Override   
	public void run() {   
		// 监听消息 
		System.out.println("消息监听");
		if(jni==null) { 
			jni=new DJni();   
		} 
		//jni.listenerHSSocketDate();
	}

}
