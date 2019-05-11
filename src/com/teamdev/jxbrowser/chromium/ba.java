package com.teamdev.jxbrowser.chromium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public class ba {
	private static final byte[] a = new byte[] { 120, -7, -64, -10, -27, 104, 124, -25, 102, 30, -19, 18, 71, 99, 64, -9, 66, 83, -126, 115, -75, 22, 106, -32,
			48, -61, -39, 104, -119, -116, 5, -57, -57, -18, -46, -111, -14, -40, 8, 120, -82, 121, 18, 24, -83, 68, 60, -128, 116, 116 };
	private static final BigInteger b = new BigInteger(d("21"));
	private static final BigInteger c = new BigInteger(d("20"));
	private static final BigInteger d = new BigInteger(d(
			"2ag3jyqgv3cjfndlmicgeaqpytqgrezlv74q350p2u3sx205b3gw8nxayha7uxmm0733vt2tq9lnpvby2to4udh52n4l4t6ag3lmyx936i9fj7c4cumlgqo83rw0b2lrx10vt1bappvetmnv1fw09f2exj7230bmlptthjrutxuxkw3j37inmi2rk4h5lu633n"));
	private static final BigInteger e = new BigInteger(d(
			"2ag0dj43je3oxf22utaopxp61jqgc2epo0drzxzsf3uc12i4kzru7v17z9mv954zr81m1yl0k1c07y9adyzteowk0yt8xdn01qvtamv6b360we9k0z5mqq670yxu3yu4e4vmflp18ie8ojq2nabd2qdjb6wuwoejtllllkkd8hh5tvlbgr65tps5bukokdni2c"));
	private static final BigInteger f = new BigInteger(d(
			"29suwrt1kkridgt5sm6a1pb4q0hiwa3nskk7hdsvqw2fikywu1vvyp69k3msaezzcwi2k17lx13tqcpko0gxq2p00rkwc00dzfh5b3cgqz9kgh8v2qhveb8iak1mzov5889ws3xha13p2ntsd8j1iw81esznyktx8t6ahz6vfpt2uw4hxegllgp5bdwyco1stu"));

//	 private static final BigInteger e = new BigInteger(1+"");
//	 private static final BigInteger f = new BigInteger(1+"");

	private final Map g = new HashMap();
	private final List h = new ArrayList();

	public ba(InputStream var1) throws IOException {
		BufferedReader var2 = new BufferedReader(new InputStreamReader(var1, "UTF-8"));
		this.a(var2);
		var2.close();
	}

	protected void a(BufferedReader var1) throws IOException {
		String var2;
		while ((var2 = var1.readLine()) != null) {
			int var3;
			if ((var3 = var2.indexOf(":")) == -1) {
				throw f(d("3bk5yychqbx6fu4yunqzdobuxpt0up1"));
			}

			String var4 = var2.substring(0, var3).trim();
			var2 = var2.substring(var3 + 1).trim();
			this.a(var4, var2);
		}

	}

	public final String a(String var1) {
		var1 = var1.trim();
		return (String) this.g.get(var1);
	}
	protected final void a(String var1, String var2) {

		this.g.put(var1, var2);
		this.h.add(var1);
	}

	public final String b(String var1) {
		return (var1 = this.a(var1)) == null ? null : d(var1);
	}

	public static String c(String var0) {
		byte[] var2;
		try {
			var2 = var0.getBytes("UTF-8");
		} catch (UnsupportedEncodingException var1) {
			throw f(var1.getMessage());
		}

		a(var2);
		return (new BigInteger(var2)).toString(36);
	}

	public static String d(String var0) {
		byte[] var2;
		a(var2 = (new BigInteger(var0, 36)).toByteArray());

		try {
			
			return new String(var2, "UTF-8");

		} catch (UnsupportedEncodingException var1) {
			throw f(var1.getMessage());
		}
	}

	private static void a(byte[] var0) {
		int var1 = 0;

		for (int var2 = var0.length; var1 < var2; ++var1) {
			var0[0] ^= a[var1 % 50];
		}

	}

	public final boolean b() {
		BigInteger var1 = new BigInteger(this.a(d("-7nkkov")), 36);
		BigInteger var2 = new BigInteger(this.a(d("-7nkkou")), 36);
		BigInteger var3 = this.d();
		BigInteger var4 = d.subtract(b);
		boolean var5;
		if (var3.compareTo(c) >= 0 && var3.compareTo(var4) < 0 && var1.compareTo(c) >= 0 && var1.compareTo(var4) < 0 && var2.compareTo(c) >= 0
				&& var2.compareTo(var4) < 0) {
			var5 = f.modPow(var1, d).multiply(var1.modPow(var2, d)).mod(d).equals(e.modPow(var3, d));
		} else {
			var5 = false;
		}
		System.out.println("boolean -> var5"+var5);
		return var5;
	}

	protected String a() {
		StringBuffer var1 = new StringBuffer();
		Iterator var2 = this.c();

		while (var2.hasNext()) {
			Entry var3;
			String var4 = (String) (var3 = (Entry) var2.next()).getKey();
			String var5 = (String) var3.getValue();
			var1.append(e(var4));
			var1.append(e(var5));
		}
		System.out.println("var1->a"+ var1.toString());
		return var1.toString();
	}

	private Iterator c() {
		TreeMap var1;
		(var1 = new TreeMap(this.g)).remove(d("-7nkkov"));
		var1.remove(d("-7nkkou"));
		return var1.entrySet().iterator();
	}

	protected static String e(String var0) {
		char[] var4 = var0.toCharArray();
		StringBuffer var1 = new StringBuffer(var4.length);

		for (int var2 = 0; var2 < var4.length; ++var2) {
			char var3;
			if (Character.isLetterOrDigit(var3 = var4[var2])) {
				var1.append(var3);
			}
		}

		return var1.toString();
	}

	private BigInteger d() {
		try {
			MessageDigest var1 = MessageDigest.getInstance(d("poht"));
			byte[] var5 = this.a(var1);
			byte[] var2 = this.a().getBytes("UTF-8");
			byte[] var3 = new byte[50];
			System.arraycopy(var5, 0, var3, 0, var5.length);
			System.arraycopy(var2, 0, var3, var5.length, Math.min(50 - var5.length, var2.length));
			
			return new BigInteger(1, var3);
		} catch (Exception var4) {
			throw f(var4.toString());
		}
	}

	protected byte[] a(MessageDigest var1) throws UnsupportedEncodingException {
		Iterator var2 = this.c();

		while (var2.hasNext()) {
			Entry var3;
			String var4 = (String) (var3 = (Entry) var2.next()).getKey();
			String var5 = (String) var3.getValue();
			var1.update(var4.getBytes("UTF-8"));
			var1.update(var5.getBytes("UTF-8"));
		}

		return var1.digest();
	}

	protected static RuntimeException f(String var0) {
		RuntimeException var1;
		(var1 = new RuntimeException(var0)).setStackTrace(new StackTraceElement[0]);
		return var1;
	}
}