package com.mugui.script.other;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * csgo脚本
 */
public class CsGoScript {
    public static void handle(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        if (source.getActionCommand().equals("开启连跳")) {
            source.setActionCommand("关闭连跳");
        }else {
            source.setActionCommand("开启连跳");
        }
    }



}
