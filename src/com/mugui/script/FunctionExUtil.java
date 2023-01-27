package com.mugui.script;

import com.mugui.tool.Other;
import com.mugui.windows.Tool;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.POINT;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FunctionExUtil {

    static Tool  tool=new Tool();

   private static  ConcurrentHashMap<String,TempThread> mapThread=new ConcurrentHashMap<String,TempThread>();

    public static void  鼠标位于某范围触发按键(int x1 ,int y1,int x2,int y2,int vk){
        鼠标位于某范围触发按键(new Rectangle(x1,y1,x2,y2),vk);
    }


    public static void  鼠标位于某范围触发按键(Rectangle dimension,int vk){
        String v= dimension.getX()+"" + dimension.getY() + dimension.getWidth() + dimension.getHeight() + vk;

        TempThread thread = mapThread.get(v);
        if(thread==null||!thread.isAlive()){
            synchronized (mapThread){
                thread = mapThread.get(v);
                if(thread==null||!thread.isAlive()){
                    thread=new TempThread(dimension,vk);
                    mapThread.put(v,thread);
                    thread.start();
                }
            }
        }
    }
    public static void Stop(){
        for (Map.Entry<String, TempThread> stringThreadEntry : mapThread.entrySet()) {
            stringThreadEntry.getValue().Stop();
        }
        mapThread.clear();
    }

    static class TempThread extends Thread {

        private Rectangle dimension;
        private int vk;
        private boolean isRun=false;
        public TempThread(Rectangle dimension, int vk) {
            this.dimension=dimension;
            this.vk=vk;
        }


        @Override
        public void run() {
            isRun=true;
            boolean check=false;
            while(isRun){
                POINT point = new POINT();
                boolean b = OS.GetCursorPos(point);
                if(b){
                    if(point.x>=dimension.x&&point.y>=dimension.y&&point.x<=dimension.width&&point.y<=dimension.height){
                        if(!check){
                            System.out.println("按下 "+vk);
                            tool.keyPress(vk);
                            check=true;
                        }
                    }else {
                        if(check){
                            tool.keyRelease(vk);
                            System.out.println("釋放 "+vk);
                            check=false;
                        }
                    }
                }
                Other.sleep(1);

            }
            if(check){
                tool.keyRelease(vk);
                System.out.println("釋放 "+vk);
            }

        }

        public void Stop() {
            isRun=false;
            while(this.isAlive()){
                Other.sleep(1);
            }

        }
    }
}
