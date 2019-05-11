package com.mugui.tool;

public class WinTool {
	private static CMD cmd = null;

	public static void Regsvr32(String fileString) {
		Init();
		cmd.send("Regsvr32 /s " + fileString);
		close();
	}

	public static void UnRegsvr32(String fileString) {
		Init();
		cmd.send("Regsvr32 /s /u " + fileString);
		close();
	}

	private static void Init() {
		if (cmd == null || !cmd.isColose()) {
			cmd = new CMD();
			cmd.start();
		}
	}

	private static void close() {
		if (cmd != null && cmd.isColose())
			cmd.send("exit");
	}

	public static void reStart() {
		Init();
		cmd.send("shutdown -r -t 10");
		Other.sleep(1000);
		close();
	}

	public static void Run(String s) {
		Init();
		cmd.send(s);
		close();
	}
}
