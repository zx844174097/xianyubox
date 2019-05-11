package com.mugui.windows;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public class Clipboard {
	/** 
     * 将字符串复制到剪切板。 
     */  
    public static void setSysClipboardText(String writeMe) {  
        java.awt.datatransfer.Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
        Transferable tText = new StringSelection(writeMe);  
        clip.setContents(tText, null);  
    } 
}
