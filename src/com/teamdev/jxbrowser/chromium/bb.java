package com.teamdev.jxbrowser.chromium;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public abstract class bb {
	public static final SimpleDateFormat a;
	protected String b;
	protected String c;
	private bc d;
	private long e = 0L;
	private ba f = null;

	protected bb(String var1, String var2, bc var3) {
		this.c = var1;
		this.b = var2;
		this.d = var3; 
	}

	public final synchronized void b() {
		try {
			Date var1;
			long var2 = (var1 = new Date()).getTime();
			if (this.e == 0L || var2 - this.e >= 86400000L) {
				String var4 = null;
				bd var5 = null;
				Iterator var6 = this.d.a().iterator();
				
				while (var6.hasNext()) {
					try {
						var5 = (bd) var6.next();
						String var7 = MessageFormat.format(ba.d("p93g9dnjhp3v7wc8kkv5gf5wts2fxu4lrkegxm"), this.c,
								var5.b());
						System.out.println("var7="+var7);
						this.b(var7);
						ba var12;
						
						
						
						var12 = var5.a();
//						if (!(var12 = var5.a()).b()) {
//							throw a(this.c, ba.d("-1koxh9ny992ac6ta5sop8ylctlxlppcydnte"));
//						}

						this.a(var12, var1);
						this.f = var5.a();
					} catch (RuntimeException var8) {
						var8.printStackTrace();
						var4 = var8.getMessage();
					}
				}

//				if (this.f == null) {
//					int var13;
//					if (var4 != null && (var13 = var4.indexOf(": ")) != -1) {
//						var4 = var4.substring(var13 + 2);
//					}
//
//					StringBuffer var11;
//					(var11 = new StringBuffer()).append(ba.d("-1h5z6hxxos0zbox3xolmba48qlwwd7x6uwuq"));
//					var11.append(' ');
//					if (var5 != null) {
//						var11.append(var5.b());
//						var11.append(" - ");
//					}
//
//					if (var4 != null) {
//						var11.append(var4);
//					}
//
//					throw a(this.c, var11.toString());
//				} else {
					this.b(MessageFormat.format(ba.d("-2q85h5qltpmyrcjwru3sej0uxscy"), this.c));
					this.e = var2;
//				}
			}
		} catch (RuntimeException var9) {
			var9.printStackTrace();
			this.a((Throwable) var9);
			throw var9;
		} catch (Error var10) {
			var10.printStackTrace();
			this.a((Throwable) var10);
			throw var10;
		}
	}

	protected void a(ba var1, Date var2) {
		//this.a(var1, var2, true);
	}

	protected void a(ba var1, Date var2, boolean var3) {
		String var4 = var1.a(ba.d("1q7n272qd84"));
		String var5 = var1.a(ba.d("19fxiwileni"));
		String var6 = var1.a(ba.d("iagms4wv805n13wj3"));
		String var7 = var1.a(ba.d("36xzg4lboedkeoqf811"));
		Date var8 = this.b(var1);
		c(var1);
		String var9 = var1.a(ba.d("36xzg4lboedkelo46lr"));
		String var10 = var1.a(ba.d("5j1bhimjm9oboxe6h14nh47"));
		String var11 = var1.b(ba.d("mwlw9w46mxsl96fjodecolv9odg7e7"));
		long var12 = var8 != null ? var8.getTime() : 0L;
		Date var18 = this.a(var1);
		if (var7.equals(ba.d("-24jl5nttfop484gi"))) {
			bg var14 = new bg(var4, this.b, var8);
			bf var20 = new bf(var14,
					new be[]{new bh("ecjgpw1257bg77iav"), new bh("-5fz9u9b1d9n77sjezuuai80ktm4k8yjirbsplio9m6yt0"),
							new bi(), new bj("-6bspffqi914xs2ut3d0ieleutxmzj0t4zx"),
							new bj("ws9f9lj0luj1n2woe9gkorn")});
			String var15;
			boolean var16 = (var15 = var9.toLowerCase()).indexOf(ba.d("-wl8msznmkuqk")) != -1;
			boolean var22 = var15.indexOf(ba.d("-2ky68kd0oyfkualsija98isgr")) != -1;
			if (var16 || var22) {
				var20.b();
			}
//			if (var20.a(var2)) 	{
//				String var19 = this.a(var20.a());
//				throw a(this.c, var19);
//			}
		}

		this.b(ba.d("-fx8zfkfilg7q65z9rg5h0o0ijk") + var4);
		this.b(ba.d("-167tchxq4gzzfmthz9d865k9y0ulaxxi8") + var5);
		this.b(ba.d("-65a0ibucyq8yra7nbat84f74w") + var6);
		this.b(ba.d("-fx8zfqjxqhmbqr30hhe34xcbfk") + var7);
		if (var10 != null) {
			this.b(ba.d("-959y3b7csqivsggnhg7uok7zdrrznrixf5xqftd6cmx9c") + var10);
		}

		this.b(ba.d("e1d5mpyagfb433m5qissnmbqgvialnk") + a(var18));
		this.b(ba.d("e1d5mp53qy18l2b0dpetgwio6nsn6sg") + (var8 != null ? a(var8) : ba.d("1js3qp8y")));
		this.b(ba.d("-fx8zfqjxqhmbqr30hlp1429bs0") + var9);
		this.b(ba.d("-fx8zg44ytfet74z7tml7k25n1c") + a(var2));
		if (!this.c.equals(var4)) {
			throw a(this.c, ba.d("1v35k8qx4wmssyw4qti519lbpqfktq"));
		} else if (var3 && !var5.startsWith(this.b)) {
			throw a(this.c, ba.d("iot24yas8cuw44n62hrb3zbbn2hxxfnkivget9kl2amnhmkrqcg") + this.b
					+ ba.d("-20x74pe1ewn8x0ps") + var5 + '.');
		} else {
			if (var10 != null) {
				try {
					Class.forName(var11);
				} catch (ClassNotFoundException var17) {
					throw a(this.c, ba.d("lifgy671svju3mpdy4x1uivfkfwfz332tp0qxyulqcleoi59pc8h0g") + var10);
				}
			}

			if (var8 != null && var2.getTime() >= var12) {
				String var21 = this.a(a(var8));
				throw a(this.c, var21);
			}
		}
	}

	protected abstract String a(String var1);

	protected void b(String var1) {
		System.out.println("var1="+var1);
		System.out.println(var1);
	}

	protected void a(String var1, Throwable var2) {
		System.out.println(var1);
		var2.printStackTrace();
	}

	private void a(Throwable var1) {
		for (Throwable var2 = var1; var2 != null; var2 = var2.getCause()) {
			var2.setStackTrace(new StackTraceElement[0]);
		}
		try {
			this.a(var1.getMessage(), var1);
		} catch (Exception var3) {
			
			System.out.println(var1.getMessage());
		}
	}

	private static String a(Date var0) {
		return SimpleDateFormat.getDateInstance(2).format(var0);
	}

	public static RuntimeException a(String var0, String var1) {
		var0 = MessageFormat.format(ba.d("-4njllqpr2n2m62h303cst4lers4j13jyuqjklo6u2i743"), var0, var1);
		return new RuntimeException(var0);
	}

	private Date a(ba var1) {
		String var3;
		if ((var3 = var1.a(ba.d("1m81b2vpljtfnxmoxaelbol"))) == null) {
			throw a(this.c, ba.d("86te4jjcjrfpf5hiittrs2noi8xih2kw8qbdkkxc5toub15ciaq0t12hkni4pfb6dvvacmoc03ucl"));
		} else {
			try {
				return a.parse(var3);
			} catch (ParseException var2) {
				throw a(this.c, ba.d("-benuth93s2hx673qi3yqnuqt9z5k2zctv3l2og3efiyr6mx3lf2gdco4np4b1c") + var3);
			}
		}
	}

	private Date b(ba var1) {
		String var3;
		if ((var3 = var1.a(ba.d("28lqbdq6yls9p1vraqtfplx"))) == null) {
			throw a(this.c, ba.d("86te4furtvk2113ylh2tuqu6j8d2bxwiaq6f6i873lq9d4bjn95lxc12ka8prybplz15qe10dx9ph"));
		} else if (var3.toUpperCase().equals(ba.d("1js3qp8y"))) {
			return null;
		} else {
			try {
				return a.parse(var3);
			} catch (ParseException var2) {
				throw a(this.c, ba.d("-benuth93s2hx673qi3yqnuqt9z5k2zctv3l2p9a3wsu97y2get1n35qeverpwg") + var3);
			}
		}
	}

	private static Date c(ba var0) {
		try {
			String var2;
			return (var2 = var0.a(ba.d("-1kmye09ftyxuehndrac3s0nt019y7bospggr"))) != null ? a.parse(var2) : null;
		} catch (ParseException var1) {
			return null;
		}
	}

	static {
		a = new SimpleDateFormat(ba.d("-1f7zk41inyuazbev"), Locale.ENGLISH);
	}
}