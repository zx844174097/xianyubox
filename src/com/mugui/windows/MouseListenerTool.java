package com.mugui.windows;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.mugui.model.HsAllModel;
import com.mugui.ui.DataSave;
import com.sun.jna.Structure;
import com.sun.jna.examples.win32.Kernel32;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.User32.HHOOK;
import com.sun.jna.examples.win32.User32.MSG;
import com.sun.jna.examples.win32.W32API.HMODULE;
import com.sun.jna.examples.win32.W32API.HWND;
import com.sun.jna.examples.win32.W32API.LRESULT;
import com.sun.jna.examples.win32.W32API.WPARAM;
import com.sun.jna.examples.win32.User32.HOOKPROC;
import com.sun.jna.examples.win32.User32.KBDLLHOOKSTRUCT;

public class MouseListenerTool implements Runnable {

	private static MouseListenerTool mouseListenerTool = null;

	public static final MouseListenerTool newInstance() {
		if (mouseListenerTool != null)
			return mouseListenerTool;
		return mouseListenerTool = new MouseListenerTool();
	}

	private static Thread MOUSE_LISTENER_THEARD = null;

	private static final void Start() {
		if (MOUSE_LISTENER_THEARD == null || !MOUSE_LISTENER_THEARD.isAlive()) {
			MOUSE_LISTENER_THEARD = new Thread(newInstance());
			MOUSE_LISTENER_THEARD.start();
		}
	}

	public static final int WM_MOUSEMOVE = 512;
	public static final int WM_LEFT_MOUSEDOWN = 513;
	public static final int WM_LEFT_MOUSEUP = 514;
	public static final int WM_REGHT_MOUSEDOWN = 516;
	public static final int WM_REGHT_MOUSEUP = 517;
	private static HHOOK hhk;
	private static LowLevelKeyProc keyHook;
	//private static MessageProc messageHook;
	final static User32 lib = User32.INSTANCE;

	public MouseListenerTool() {
	}

	public interface LowLevelMouseProc extends HOOKPROC {
		LRESULT callback(int nCode, WPARAM wParam, MOUSEHOOKSTRUCT lParam);
	}

	public interface LowLevelKeyProc extends HOOKPROC {
		LRESULT callback(int nCode, WPARAM wParm, KBDLLHOOKSTRUCT IParm);
	}

	public interface MessageProc extends HOOKPROC {
		LRESULT callback(int nCode, WPARAM wParm, MSG IParm);
	}

	public static class MOUSEHOOKSTRUCT extends Structure {
		public User32.POINT pt;
		public HWND hwnd;
		public int wHitTestCode;
		public User32.ULONG_PTR dwExtraInfo;
		public User32.UINT_PTR zPtr;
	}

	private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, KeyListener>> hashMap = null;

	public void addKeyListener(KeyListener keyListener) {
		if (keyListener == null)
			return;
		if (keyListener.object == null) {
			throw new NullPointerException("keyListener.object is NULL");
		}
		Start();
		if (hashMap == null)
			hashMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, KeyListener>>();
		ConcurrentHashMap<Integer, KeyListener> teMap = null;
		if ((teMap = hashMap.get(keyListener.key)) == null) {
			hashMap.put(keyListener.key, teMap = new ConcurrentHashMap<>());
		}
		if (teMap.get(keyListener.object.hashCode()) == null) {
			teMap.put(keyListener.object.hashCode(), keyListener);
		}
	}

	public void removeKeyListeners(int key) {
		hashMap.remove(key);
	}

	public void removeKeyListeners(KeyListener keyListener) {
		if (keyListener == null)
			return;
		if (keyListener.isCheckNull())
			return;
		ConcurrentHashMap<Integer, KeyListener> map = hashMap.get(keyListener.key);
		if (map == null)
			return;
		map.remove(keyListener.object.hashCode());
	}

	public void removeKeyListener(Object object, int key) {
		ConcurrentHashMap<Integer, KeyListener> teMap = hashMap.get(key);
		if (teMap != null)
			teMap.remove(object.hashCode());
	}

	public static abstract class KeyListener {
		public Object object = null;
		public int key = 0;

		/**
		 * 
		 * @param object
		 *            想要反馈的对象
		 * @param scanCode
		 *            扫描码，若传递虚拟键值码，请使用OS.MapVirtualKey(keyCode,0) 翻译成扫描码
		 */
		public KeyListener(Object object, int i) {
			this.object = object;
			key = i;
		}

		public boolean isCheckNull() {
			return object == null;
		}

		public abstract void callback(Object object);
	}

	public void run() {
		HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		keyHook = new LowLevelKeyProc() {
			public LRESULT callback(int nCode, WPARAM wParam, KBDLLHOOKSTRUCT IParm) {
				if ((IParm.flags & (1 << 7)) == 0 && IParm.scanCode == 87) {
					HsAllModel.closeAPP();
				}
				if ((IParm.flags & (1 << 7)) == 0 && IParm.scanCode == 67) {
					DataSave.StaticUI.setVisible(true);
					DataSave.StaticUI.setAlwaysOnTop(true);
					DataSave.StaticUI.setAlwaysOnTop(false);
				}

				if ((IParm.flags & (1 << 7)) == 0) {
					if (hashMap != null) {
						ConcurrentHashMap<Integer, KeyListener> map = null;
						if ((map = hashMap.get(IParm.scanCode)) != null) {
							if (map.isEmpty()) {
								hashMap.remove(IParm.scanCode);
							} else {
								Collection<KeyListener> collections = map.values();
								for (KeyListener listener : collections) {
									listener.callback(listener.object);
								}
							}

						}
					}
				}
				return lib.CallNextHookEx(hhk, nCode, wParam, IParm.getPointer());
			}
		};
		hhk = lib.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyHook, hMod, 0);
		int result;
		MSG msg = new MSG();
		while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
			if (result == -1) {
				break;
			} else {
				lib.TranslateMessage(msg);
				lib.DispatchMessage(msg);
			}
		}
		lib.UnhookWindowsHookEx(hhk);
	}
}
