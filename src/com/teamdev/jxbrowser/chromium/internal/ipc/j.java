package com.teamdev.jxbrowser.chromium.internal.ipc;

import com.teamdev.jxbrowser.chromium.ba;
import com.teamdev.jxbrowser.chromium.bb;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

final class j extends bb {
	private static final j d = new j();
	private final String[] e;

	private j() {
		this(ba.d("-cdvdo6flt8qlce"), ba.d("4nerc"));
	}

	private j(String var1, String var2) {
		super(var1, var2, new k(var1));
		this.e = new String[]{ba.d("4nerc")};
	}

	static j a() { 
		d.b();
		return d;
	}

	private static boolean c() {
		return Boolean.getBoolean(ba.d("3wfqc2fmdua27hya5ydiasendauzwq7"));
	}

	private static String a(Date var0) {
		return DateFormat.getDateInstance(2).format(var0);
	}

	private static Date d() {
		try {
			return new Date(Long.valueOf(ba.d("h43ligdw8d7oof5wfcc2")));
		} catch (NumberFormatException var0) {
			return new Date();
		}
	}

	protected final void a(ba var1, Date var2, boolean var3) {
		String var6 = var1.a(ba.d("36xzg4lboedkeoqf811"));
		if (ba.d("-24jl5nttfop484gi").equals(var6)) {
			System.setProperty(ba.d("3wfqc2fmdua27hya5ydiasendauzwq7"), ba.d("-gsugez"));
		}

		var6 = var1.a(ba.d("36xzg4lboedkeoqf811"));
		if (ba.d("-3hw901").equals(var6)) {
			throw a(this.c, ba.d(
					"-mwe7pku8a790s25lj16wauzhckkgmlop6dq6qx1woqnq8pxv1nsulg3epe6mzfk60fitf2zoyl34ggfdq7hr7w22y3sqnawn7r7g9qn4hwmdyla3ln2dei89asd6j4wyx7a4rd334zpf62doua4y"));
		} else {
			super.a(var1, var2, false);
			String var5;
			if ((var6 = System.getProperty(ba.d("36vnbp1zlnp"))).startsWith(ba.d("1c99b5djvjn"))) {
				var5 = ba.d("21tea6uq");
			} else if (var6.equals(ba.d("1o06i648"))) {
				var5 = ba.d("3f563xbs");
			} else {
				if (!var6.startsWith(ba.d("he1v"))) {
					throw a(this.c, ba.d("-1va50gv4iyb5sczgtweuz6k0w") + var6);
				}

				var5 = ba.d("1qc83");
			}

			String var7;
			if (!(var7 = System.getProperty(ba.d("36vnblgspd4"))).equals(ba.d("2tgxfayc"))
					&& !var7.equals(ba.d("pt430hyr8"))) {
				if (var7.endsWith(ba.d("-dze"))) {
					var7 = ba.d("28dh2");
				}
			} else {
				var7 = ba.d("28d2s");
			}

			var7 = var5 + '/' + var7;
			if (!var1.a(ba.d("-bb4dw1s9uscwb1")).contains(var7)) {
				throw a(this.c, ba.d("b8d0gfrhfnhp4xjqpc4zbdkqdtu00herbc3afjjixjh1kyi9kk4nclykwpm6cmrs0") + var6);
			} else {
				this.a(var1);
			}
		}
	}

	protected final String a(String var1) {
		return MessageFormat.format(ba.d(
				"902vga92ertpq6e9byrhqpsz016itzm7tgmnsmt7lndx8mdnuinnlx4tnilpvksxjzur60yrybik163rufb0clgvu30hmzkn0ekkvwznn8qjrbnr69myh1my95xlysdsp9pbxdhmql144v5p6d284aws7fjv97kmz0ac7d1rkbdoejs0cly15g6vxkunc1lao9xkow00ejdufcy9a"),
				var1);
	}

	private void a(ba var1) {
		String var2 = var1.a(ba.d("19fxiwileni"));
		String var3;
		if (!Arrays.asList(this.e).contains(var2)) {
			StringBuilder var8 = new StringBuilder();
			String[] var10 = this.e;

			for (int var4 = 0; var4 <= 0; ++var4) {
				String var5 = var10[0];
				var8.append(var5);
				var8.append(", ");
			}

			var3 = ba.d("-2169xlbmfki8ji6s37lk83xhibrmehjftfokfr75jnkn9ultr4") + var8 + ba.d("100xwx9xr4") + var2 + '.';
			throw a(this.c, var3);
		} else if ((var3 = var1.a(ba.d("-1kmye09ftyxuehndrac3s0nt019y7bospggr"))) != null
				&& !ba.d("-28i92rt6orfshxv0").equals(var3)) {
			Date var7 = this.b(var1);
			Date var9;
			if ((var9 = d()).after(var7)) {
				throw a(this.c, ba.d(
						"2lptmzi3j2788pmek81xdk4nfhp9z0y5oshg1h7dbaqvfq6xat2pmiv1hjon0hy8tx1phmepynvtny52lv7cn3nwznrsi72dk8")
						+ this.b + ba.d("42c68")
						+ ba.d("-1b08pq50r0apkot333t5z9at8b3kwgysevr08n08evue8slo2z49pbqrlpw6mm7mb9heg6uwgllq02nboe20r6wwrgsfxbib4fh3jie8")
						+ a(var7) + ba.d("6k4vg9fy2lms5t1s8n0w4raxlqfxne00931skawspyv6giuepeiduu7api3oxrjts") + a(var9)
						+ '.');
			}
		} else if (!var2.equals(this.b)) {
			String var6 = ba.d("-2169xlbmfki8ji6s37lk83xhibrmehjftfokfr75jnkn9ultr4") + this.b + ba.d("100xwx9xr4")
					+ var2 + '.';
			throw a(this.c, var6);
		}
	}

	protected final void b(String var1) {
		if (c()) {
			super.b(var1);
		}

	}

	protected final void a(String var1, Throwable var2) {
		if (c()) {
			super.a(var1, var2);
		}

	}

	private Date b(ba var1) {
		String var3;
		if ((var3 = var1.a(ba.d("-1kmye09ftyxuehndrac3s0nt019y7bospggr"))) == null) {
			throw a(this.c, ba
					.d("-4ohj9zgnf2k4tqkjcpa17daamwuut28t4th3x887oxfo31gqp1uerkogzibe9otzdavshorem2rq511abhjhobojh7"));
		} else {
			try {
				return a.parse(var3);
			} catch (ParseException var2) {
				throw a(this.c,
						ba.d("x4949gcbfcouhwdkamf28yyxtnyutml37tp0m8gp0s1jglcezrwyszuj2icz9xmtvtpxkhu00") + var3);
			}
		}
	}
}