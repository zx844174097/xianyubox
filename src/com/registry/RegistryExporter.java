/*
 * RegistryExporter.java    2012/09/15
 * Copyright (C) 2011  Yinon Michaeli
 *
 * This file is part of JRegistry.
 *
 * JRegistry is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact by e-mail if you discover any bugs or if you have a suggestion
 * to myinon2005@hotmail.com
 */

package com.registry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * <p>
 * A utility class which provides the ability to save
 * registry information to either a *.reg or a *.txt
 * file.
 * </p><p>
 * This functionality differs from the {@link RegistryKey#saveKey(File)}
 * method in that *.reg files can be double-clicked in Windows Explorer
 * to automatically add the keys that were saved to the Windows Registry.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 * @since 1.2
 */
public final class RegistryExporter {
	/**
	 * <p>
	 * Specifies the type of format to use when
	 * saving registry information.
	 * </p>
	 *
	 * @author Yinon Michaeli
	 * @version 1.8
	 * @since 1.2
	 */
	public static enum Format {
		/**
		 * The new format that is used on Windows 2000/XP/Vista/7.
		 * This format is not compatible with Windows 9x/NT.
		 *
		 * @see #REGEDIT4
		 */
		REGEDIT5,
		
		/**
		 * The old format that is used on Windows 9x/NT.
		 * It is provided for use on Windows 2000/XP/Vista/7
		 * because of backwards compatibility.
		 *
		 * @see #REGEDIT5
		 */
		REGEDIT4,
		
		/**
		 * A text format which is used to give some information
		 * about the keys that were saved such the last time
		 * the key was written to and also its class name
		 * if one was set.
		 * <br><br>
		 * <strong>Note</strong> that this format cannot be imported into the Windows Registry.
		 */
		TEXT
	};
	
	private static final boolean IS_2000 = System.getProperty("os.version").compareTo("5.0") >= 0;
	
	private RegistryExporter() {
		throw new InternalError();
	}
	
	/**
	 * Exports the specified {@link RegistryKey} objects into the file specified.
	 *
	 * @param file The file to save the keys to.
	 * @param format The file format to use when saving.
	 * @param keys An array of {@link RegistryKey}s to save.
	 * @return <code>true</code> if the export was successful,
	 *         <code>false</code> if an <code>IOException</code> occurred.
	 * @throws RegistryException if the format isn't supported on the current OS.
	 */
	public static boolean exportKey(File file, Format format, RegistryKey... keys) {
		if (format == Format.REGEDIT5 && !IS_2000)
			throw new RegistryException(format + " is not supported on this version of Windows.");
		BufferedWriter out = null;
		
		try {
			switch (format) {
				case REGEDIT4:
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "ISO-8859-1"));
				break;
				case REGEDIT5:
				case TEXT:
					out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UnicodeLittle")));
				break;
				default: // should not go here
					throw new RegistryException(format + " is an unrecognised format.");
			}
			
			if (format.equals(Format.REGEDIT4)) {
				out.write("REGEDIT4");
				out.newLine();
				out.newLine();
			} else {
				if (format.equals(Format.REGEDIT5)) {
					out.write("Windows Registry Editor Version 5.00");
					out.newLine();
					out.newLine();
				}
			}
			
			for (int i = 0; i < keys.length; i++) {
				if (keys[i].isRemoteRegistryKey())
					throw new RegistryException(keys[i] + ": remote registry keys cannot be saved.");
				if (format.equals(Format.TEXT))
					export(file, keys[i], out);
				else
					export(file, format, keys[i], out);
			}
			
			out.close();
			return true;
		} catch (IOException e) {
			try {
				if (out != null)
					out.close();
			} catch (IOException x) {
			}
			return false;
		}
	}
	
	/*
	 * Used for saving keys in the TEXT format.
	 */
	private static void export(File file, RegistryKey key, BufferedWriter out) throws IOException {
		if (!key.exists())
			return;
		
		/*
		 * Structure is:
		 * -------------
		 *
		 * Key Name:          [key]
		 * Class Name:        [clazz]
		 * Last Write Time:   [time]
		 * Value [#]
		 *   Name:            [value name]
		 *   Type:            [value type]
		 *   Data:            [value data]
		 */
		out.write("Key Name:          " + key.getPath());
		out.newLine();
		String clazz = key.getClassName();
		if (clazz == null || clazz.equals(""))
			clazz = "<NO CLASS>";
		out.write("Class Name:        " + clazz);
		out.newLine();
		out.write("Last Write Time:   " + key.getSystemTime());
		out.newLine();
		
		if (key.hasValues()) {
			List<RegistryValue> val = key.getValues();
			for (int k = 0; k < val.size(); k++) {
				RegistryValue v = val.get(k);
				out.write("Value " + k);
				out.newLine();
				
				String name = v.getName();
				if (name.equals(""))
					name = "<NO NAME>";
				out.write("  Name:            " + name);
				out.newLine();
				ValueType type = v.getValueType();
				if (type != null)
					out.write("  Type:            " + type);
				else
					out.write("  Type:            0x" + Integer.toHexString(v.getValueTypeInt()));
				out.newLine();
				out.write("  Data:            ");
				
				if (type == null) {
					out.newLine();
					byte[] b = new byte[16];
					byte[] data = v.getByteData();
					int length = (data.length + 15) / 16;
					for (int i = 0; i < length; i++) { // break off a chunck of 16 bytes
						int rem = (data.length - (i * 16));
						if (rem < 16) {
							byte[] small = new byte[rem];
							System.arraycopy(data, i * 16, small, 0, rem);
							b = small;
						} else {
							System.arraycopy(data, i * 16, b, 0, 16);
						}
						formatLine(i, b, out); // formats the line to look like the above diagram
					}
				} else {
					switch (v.getValueType()) {
						case REG_SZ:
						case REG_EXPAND_SZ:
							// For strings, just print out the value
							out.write(((RegStringValue) v).getValue());
							out.newLine();
							break;
						case REG_BINARY:
						case REG_NONE:
						case REG_LINK:
						case REG_DWORD_BIG_ENDIAN:
						case REG_RESOURCE_LIST:
						case REG_FULL_RESOURCE_DESCRIPTOR:
						case REG_RESOURCE_REQUIREMENTS_LIST:
						case REG_QWORD:
						case REG_QWORD_LITTLE_ENDIAN:
							/*
							 * For binary, none, and qwords,
							 *   Data:            
							 *                    00 00 00 00 00 00 00 00 - 00 00 00 00 00 00 00 00 00 00
							 *
							 * where the zeros are replaced by the actual bytes of the
							 * value. If the number of bytes is greater than 16, then
							 * the bytes are wrapped onto a new line.
							 */
							out.newLine();
							byte[] b = new byte[16];
							byte[] data = v.getByteData();
							int length = (data.length + 15) / 16;
							for (int i = 0; i < length; i++) { // break off a chunck of 16 bytes
								int rem = (data.length - (i * 16));
								if (rem < 16) {
									byte[] small = new byte[rem];
									System.arraycopy(data, i * 16, small, 0, rem);
									b = small;
								} else {
									System.arraycopy(data, i * 16, b, 0, 16);
								}
								formatLine(i, b, out); // formats the line to look like the above diagram
							}
							break;
						case REG_DWORD:
						case REG_DWORD_LITTLE_ENDIAN:
							// For integers, print it out in hexadecimal form
							try {
								int dword = ((RegDWORDValue) v).getIntValue();
								String d = Integer.toHexString(dword);
								if (dword != 0)
									d = "0x" + d;
								out.write(d);
							} catch (RegistryException e) {
								out.write("0");
							}
							out.newLine();
							break;
						case REG_MULTI_SZ:
							// For multi strings, print out like the binary
							// where each string in the array is put on a
							// separate line
							String[] a = ((RegMultiStringValue) v).getValue();
							out.write(a[0]);
							out.newLine();
							for (int i = 1; i < a.length; i++) {
								out.write("                   ");
								out.write(a[i]);
								out.newLine();
							}
							break;
					}
				}
				out.newLine();
			}
		}
		out.newLine();
		
		if (key.hasSubKeys()) { // Traverse sub keys
			List<RegistryKey> keys = key.getSubKeys();
			if (keys != null) {
				for (int i = 0; i < keys.size(); i++) {
					export(file, keys.get(i), out);
				}
			}
		}
	}
	
	private static void formatLine(int l, byte[] data, BufferedWriter out) throws IOException {
		StringBuffer line = new StringBuffer(Integer.toHexString(l * 16));
		while (line.length() < 8)
			line.insert(0, "0");
		line.append("   ");
		out.write(line.toString());
		line.setLength(0);
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int da = data[i] & 0xff;
			tmp.append(Integer.toHexString(da));
			if (tmp.length() == 1)
				tmp.insert(0, "0");
			if (i == 8)
				tmp.insert(0, "- ");
			if ((da >= 32 && da < 127))
				line.append((char) da);
			else if ((da >= 160 && da < 256))
				line.append((char) da);
			else
				line.append("."); // system characters like CTRL-Z become '.'
			out.write(tmp.append(" ").toString());
			tmp.setLength(0);
		}
		int rem = (8 - data.length) * 3;
		for (int i = 1; i <= rem; i++)
			out.write(" ");
		if (rem == 0 && data.length == 8)
			out.write("- ");
		else if (data.length < 8)
			out.write("  "); // dash
		if (data.length >= 8) {
			rem = (16 - data.length) * 3;
		} else {
			rem = ((16 - data.length) - (rem / 3)) * 3;
		}
		for (int i = 1; i <= rem; i++)
			out.write(" ");
		line.insert(0, " ");
		out.write(line.toString());
		out.newLine();
	}
	
	/*
	 * Used for REGEDIT4 and REGEDIT5
	 */
	private static void export(File file, Format format, RegistryKey key, BufferedWriter out) throws IOException {
		if (!key.exists())
			return;
		
		out.write("[" + key.getPath() + "]");
		out.newLine();
		
		List<RegistryValue> val = null;
		if (key.hasValues()) {
			val = key.getValues();
			for (int k = 0; k < val.size(); k++) {
				RegistryValue v = val.get(k);
				if (v.getName().equals("")) { // Default value has a name of '@'
					out.write("@=");
				} else {
					String name = v.getName();
					if (name.contains("\\") || name.contains("\"")) {
						StringBuffer buf = new StringBuffer(name);
						for (int i = 0; i < buf.length(); i++) { // It is necessary to delimit
																 // a '\' as '\\' and a '"' as '\"'
							if (buf.charAt(i) == '\\' || buf.charAt(i) == '\"') {
								buf.insert(i, '\\');
								i++;
							}
						}
						name = buf.toString();
					}
					out.write("\"" + name + "\"=");
				}
				writeValue(v, format, out);
			}
		}
		
		out.newLine();
		
		if (key.hasSubKeys()) { // Traverse sub keys
			List<RegistryKey> keys = key.getSubKeys();
			if (keys != null) {
				for (int i = 0; i < keys.size(); i++) {
					export(file, format, keys.get(i), out);
				}
			}
		}
	}
	
	/*
	 * Writes out a key's values.
	 */
	private static void writeValue(RegistryValue v, Format format, BufferedWriter out) throws IOException {
		ValueType t = v.getValueType();
		int count = 0;
		boolean limit = false;
		if (t == null) {
			out.write("hex(" + Integer.toHexString(v.getValueTypeInt()) + "):");
			byte[] b = ((RegBinaryValue) v).getValue();
			for (int i = 0; i < b.length; i++) {
				if (!limit && count == 20) {
					out.write("\\");
					out.newLine();
					out.write("  ");
					count = 0;
					limit = true;
				} else if (limit && count == 25) {
					out.write("\\");
					out.newLine();
					out.write("  ");
					count = 0;
				}
				String s = Integer.toHexString(b[i] & 0xff);
				if (s.length() != 2)
					s = "0" + s;
				out.write(s);
				count++;
				if (i != b.length - 1)
					out.write(",");
			}
			out.newLine();
		} else {
			switch (t) {
				case REG_SZ:
					String value = ((RegStringValue) v).getValue();
					if (value.contains("\\") || value.contains("\"")) {
						StringBuffer buf = new StringBuffer(value);
						for (int i = 0; i < buf.length(); i++) { // It is necessary to delimit
																 // a '\' as '\\' and a '"' as '\"'
							if (buf.charAt(i) == '\\' || buf.charAt(i) == '\"') {
								buf.insert(i, '\\');
								i++;
							}
						}
						value = buf.toString();
					}
					out.write("\"" + value + "\"");
					out.newLine();
					break;
				case REG_EXPAND_SZ:
					// Write out the bytes delimited by commas ','
					// On first line, if there are more than 19 bytes,
					// the line is terminated by a '\'.
					// On lines after the first, they are delimited after 25 bytes
					byte[] c = ((RegStringValue) v).getByteData();
					out.write("hex(2):");
					for (int i = 0; i < c.length; i++) {
						if (!limit && count == 19) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
							limit = true;
						} else if (limit && count == 25) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
						}
						String hex = Integer.toHexString(c[i] & 0xff);
						if (hex.length() != 2)
							hex = "0" + hex;
						out.write(hex);
						if (format.equals(Format.REGEDIT4) && (i != c.length - 2))
							out.write(",");
						else {
							if (format.equals(Format.REGEDIT5) && (i != c.length - 1))
								out.write(",");
						}
						count++;
						
						if (format.equals(Format.REGEDIT4)) {
							i++;
						}
					}
					
					out.newLine();
					break;
				case REG_BINARY:
				case REG_NONE:
				case REG_LINK:
				case REG_RESOURCE_LIST:
				case REG_FULL_RESOURCE_DESCRIPTOR:
				case REG_RESOURCE_REQUIREMENTS_LIST:
					// Write out the bytes delimited by commas ','
					// On first line, if there are more than 20 bytes,
					// the line is terminated by a '\'.
					// On lines after the first, they are delimited after 25 bytes
					if (t.equals(ValueType.REG_BINARY))
						out.write("hex:");
					else
						out.write("hex(" + Integer.toHexString(t.getValue()) + "):");
					byte[] b = ((RegBinaryValue) v).getValue();
					for (int i = 0; i < b.length; i++) {
						if (!limit && count == 20) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
							limit = true;
						} else if (limit && count == 25) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
						}
						String s = Integer.toHexString(b[i] & 0xff);
						if (s.length() != 2)
							s = "0" + s;
						out.write(s);
						count++;
						if (i != b.length - 1)
							out.write(",");
					}
					out.newLine();
					break;
				case REG_DWORD:
				case REG_DWORD_LITTLE_ENDIAN:
					// Integers are written in hexadecimal form
					try {
						int dec = ((RegDWORDValue) v).getIntValue();
						String dword = Integer.toHexString(dec);
						int length = dword.length();
						out.write("dword:");
						while (length < 8) {
							out.write("0");
							length++;
						}
						out.write(dword);
					} catch (RegistryException e) {
						out.write("hex(4):");
						byte[] d = ((RegDWORDValue) v).getByteData();
						for (int i = 0; i < d.length; i++) {
							if (!limit && count == 19) {
								out.write("\\");
								out.newLine();
								out.write("  ");
								count = 0;
								limit = true;
							} else if (limit && count == 25) {
								out.write("\\");
								out.newLine();
								out.write("  ");
								count = 0;
							}
							String s = Integer.toHexString(d[i] & 0xff);
							if (s.length() != 2)
								s = "0" + s;
							out.write(s);
							count++;
							if (i != d.length - 1)
								out.write(",");
						}
					}
					out.newLine();
					break;
				case REG_DWORD_BIG_ENDIAN:
					out.write("hex(5):");
					byte[] db = ((RegDWORDValue) v).getByteData();
					for (int i = 0; i < db.length; i++) {
						if (!limit && count == 19) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
							limit = true;
						} else if (limit && count == 25) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
						}
						String s = Integer.toHexString(db[i] & 0xff);
						if (s.length() != 2)
							s = "0" + s;
						out.write(s);
						count++;
						if (i != db.length - 1)
							out.write(",");
					}
					out.newLine();
					break;
				case REG_MULTI_SZ:
					// Write out the bytes delimited by commas ','
					// On first line, if there are more than 19 bytes,
					// the line is terminated by a '\'.
					// On lines after the first, they are delimited after 25 bytes
					byte[] array = ((RegMultiStringValue) v).getByteData();
					out.write("hex(7):");
					for (int i = 0; i < array.length; i++) {
						if (!limit && count == 19) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
							limit = true;
						} else if (limit && count == 25) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
						}
						
						String hex = Integer.toHexString(array[i] & 0xff);
						if (hex.length() != 2)
							hex = "0" + hex;
						out.write(hex);
						if (format.equals(Format.REGEDIT4) && (i != array.length - 2))
							out.write(",");
						else {
							if (format.equals(Format.REGEDIT5) && (i != array.length - 1))
								out.write(",");
						}
						count++;
						if (format == Format.REGEDIT4) {
							i++;
						}
					}
					
					out.newLine();
					break;
				case REG_QWORD:
				case REG_QWORD_LITTLE_ENDIAN:
					// Write out the bytes delimited by commas ','
					out.write("hex(b):");
					byte[] q = ((RegQWORDValue) v).getByteData();
					for (int i = 0; i < q.length; i++) {
						if (!limit && count == 19) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
							limit = true;
						} else if (limit && count == 25) {
							out.write("\\");
							out.newLine();
							out.write("  ");
							count = 0;
						}
						String s = Integer.toHexString(q[i] & 0xff);
						if (s.length() != 2)
							s = "0" + s;
						out.write(s);
						count++;
						if (i != q.length - 1)
							out.write(",");
					}
					out.newLine();
			}
		}
	}
}