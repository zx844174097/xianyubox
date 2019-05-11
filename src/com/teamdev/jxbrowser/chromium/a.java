package com.teamdev.jxbrowser.chromium;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale; 

public final class a extends ba {

	private static DateFormat a;
	private List b;

	public a(InputStream var1) throws IOException {
		super(var1);
	}

	protected final void a(BufferedReader var1) {
		try {
			String var3 = "1m81b2vpljtfnxmoxaelbol";
			String var10001 = d("1m81b2vpljtfnxmoxaelbol");
			Date var2;
			if ((var2 = h(this.b(var1))) == null) {
				var2 = new Date();
			}
			this.a(var10001, bb.a.format(var2));
			var3 = "iagms4wv805n13wj3";
			this.a(d("iagms4wv805n13wj3"), this.b(var1));
			var3 = "31kgw2or0myveb8yq91";
			this.a(d("31kgw2or0myveb8yq91"), this.b(var1));
			var3 = "28lqbdq6yls9p1vraqtfplx";
			this.a(d("28lqbdq6yls9p1vraqtfplx"), this.b(var1));
			var3 = "36xzg4lboedkeoqf811";
			this.a(d("36xzg4lboedkeoqf811"), this.b(var1));
			var3 = "36xzg4lboedkelo46lr";
			this.a(d("36xzg4lboedkelo46lr"), this.b(var1));
			var3 = "-bb4dw1s9uscwb1";
			this.a(d("-bb4dw1s9uscwb1"), this.b(var1));
			var3 = "-7nkkov";
			this.a(d("-7nkkov"), var1.readLine());

			var3 = "-7nkkou";
			this.a(d("-7nkkou"), var1.readLine());

			var3 = "19fxiwileni";
			this.a(d("19fxiwileni"), g(this.b(var1)));
			var3 = "1q7n272qd84";
			this.a(d("1q7n272qd84"), g(this.b(var1)));
			String var4;
			if ((var4 = this.b(var1)) != null && var4.length() > 0) {
				var3 = "5j1bhimjm9oboxe6h14nh47";
				this.a(d("5j1bhimjm9oboxe6h14nh47"), g(var4));
				var3 = "mwlw9w46mxsl96fjodecolv9odg7e7";
				this.a(d("mwlw9w46mxsl96fjodecolv9odg7e7"), g(this.b(var1)));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String b(BufferedReader var1) throws IOException {
		if (this.b == null) {
			this.b = new LinkedList();
		}

		String var2;
		if ((var2 = var1.readLine()) != null) {
			this.b.add(var2);
		}

		return var2;
	}

	private static String g(String var0) {
		if (var0 != null) {
			int var1;
			if ((var1 = var0.indexOf(":")) == -1) {
				throw f(d("3bk5yychqbx6fu4yunqzdobuxpt0up1"));
			} else {
				return var0.substring(var1 + 1).trim();
			}
		} else {
			return null;
		}
	}

	private static Date h(String var0) {
		try {
			return a.parse(var0);
		} catch (ParseException var1) {
			return null;
		}
	}

	protected final byte[] a(MessageDigest var1) {
		var1.update(this.i("iagms4wv805n13wj3"));
		var1.update(this.i("31kgw2or0myveb8yq91"));
		try {
			var1.update(this.a(0));
			var1.update(this.i("36xzg4lboedkeoqf811"));
			var1.update(this.i("36xzg4lboedkelo46lr"));
			var1.update(this.i("-bb4dw1s9uscwb1"));
			var1.update(this.a(7));
			var1.update(this.a(8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (this.c()) {
			var1.update(this.i("5j1bhimjm9oboxe6h14nh47"));
			var1.update(this.i("mwlw9w46mxsl96fjodecolv9odg7e7"));
		}

		return var1.digest(this.i("28lqbdq6yls9p1vraqtfplx"));
	}

	private byte[] i(String var1) {
		var1 = d(var1);

		try {
			return this.a((String) var1).getBytes("UTF-8");
		} catch (UnsupportedEncodingException var2) {
			throw new IllegalStateException();
		}
	}

	private byte[] a(int var1) throws UnsupportedEncodingException {
		return this.b != null && var1 <= this.b.size() ? ((String) this.b.get(var1)).getBytes("UTF-8") : new byte[0];
	}

	private boolean c() {
		return this.b.size() >= 11;
	}

	protected final String a() {
		StringBuffer var1;
		StringBuffer var10000 = var1 = new StringBuffer();
		String var2 = "28lqbdq6yls9p1vraqtfplx";
		var10000.append(e(this.a((String) d("28lqbdq6yls9p1vraqtfplx"))));
		var2 = "iagms4wv805n13wj3";
		var1.append(e(this.a((String) d("iagms4wv805n13wj3"))));
		var2 = "31kgw2or0myveb8yq91";
		var1.append(e(this.a((String) d("31kgw2or0myveb8yq91"))));
		var2 = "36xzg4lboedkeoqf811";
		var2 = this.a((String) d("36xzg4lboedkeoqf811"));
		var1.append(var2.charAt(0));
		var2 = "36xzg4lboedkelo46lr";
		var1.append(e(this.a((String) d("36xzg4lboedkelo46lr"))));
		if (this.c()) {
			var2 = "5j1bhimjm9oboxe6h14nh47";
			var1.append(e(this.a((String) d("5j1bhimjm9oboxe6h14nh47"))));
			var2 = "mwlw9w46mxsl96fjodecolv9odg7e7";
			var1.append(e(this.a((String) d("mwlw9w46mxsl96fjodecolv9odg7e7"))));
		}

		return var1.toString();
	}

	static {
		a = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.ENGLISH);
	}
}