/*
 * RegistryKey.java    2012/09/15
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;

import com.mugui.ui.DataSave;

import static com.registry.WinRegistryAPI.*;

/**
 * <p>
 * This is an abstract representation of a key in the Windows Registry. Each key
 * is capable of holding named value-data pairs plus other subkeys in a tree
 * like structure.
 * </p>
 * <p>
 * This class provides many different methods which allow for the creation and
 * deletion of subkeys as well as the creation and deletion of values. <br>
 * <br>
 * Also there are methods which can be used to query the different subkeys and
 * values which can be found in a key as well as to test for the existence of
 * keys and values. Methods are also provided which allow the registry key and
 * its values to be renamed.
 * </p>
 * <p>
 * There are two different constructors available for use. One is generally used
 * for when a key already exists in the registry, and the other can be used to
 * create the key when the constructor is called. <br>
 * <br>
 * Another way to create an instance of the <code>RegistryKey</code> class is
 * through the <code>parseKey</code> methods. There are two versions of the
 * method, one which returns a <code>RegistryKey</code> object if it already
 * exists in the registry, and the other creates the key as the path is being
 * parsed.
 * </p>
 * <p>
 * A final set of methods allow the keys to be saved to files, restored from
 * files, and also to load and unload keys from a registry hive. These methods
 * however require that the calling process have administrative priviledges,
 * otherwise the methods fail with {@link #ERROR_ACCESS_DENIED}.
 * </p>
 * <p>
 * Changes to the registry can be tracked using the
 * {@link com.registry.event.RegistryListener} and {@link RegistryWatcher}
 * classes.
 * </p>
 * <p>
 * <em>NOTE: </em> On some Windows operating systems, the class name parameter
 * may be ignored.<br>
 * The allowance of this parameter is in order to make the library more
 * complete.
 * </p>
 * <p>
 * A warning for users who might use the normal {@link #deleteKey()} method to
 * delete symbolic link keys, if that method is used you run the risk of also
 * deleting the key that the link is pointing to. The {@link #deleteLink()}
 * method should be used instead.
 * </p>
 * 
 * @author Yinon Michaeli
 * @version 1.8
 */
public class RegistryKey implements Cloneable, Comparable<RegistryKey>, java.io.Serializable {
	static final long serialVersionUID = 5812494829706873509L;

	/*
	 * Predefined Root Keys for the Registry.
	 */
	static final long HKEY_CLASSES_ROOT = 0x80000000;
	static final long HKEY_CURRENT_USER = 0x80000001;
	static final long HKEY_LOCAL_MACHINE = 0x80000002;
	static final long HKEY_USERS = 0x80000003;
	static final long HKEY_PERFORMANCE_DATA = 0x80000004;
	static final long HKEY_CURRENT_CONFIG = 0x80000005;
	static final long HKEY_DYN_DATA = 0x80000006;

	/*
	 * Registry value type constants.
	 */
	static final int REG_NONE = 0;
	static final int REG_SZ = 1;
	static final int REG_EXPAND_SZ = 2;
	static final int REG_BINARY = 3;
	static final int REG_DWORD = 4;
	static final int REG_DWORD_LITTLE_ENDIAN = 4;
	static final int REG_DWORD_BIG_ENDIAN = 5;
	static final int REG_LINK = 6;
	static final int REG_MULTI_SZ = 7;
	static final int REG_RESOURCE_LIST = 8;
	static final int REG_FULL_RESOURCE_DESCRIPTOR = 9;
	static final int REG_RESOURCE_REQUIREMENTS_LIST = 10;
	static final int REG_QWORD = 11;
	static final int REG_QWORD_LITTLE_ENDIAN = 11;

	/*
	 * Constants that determine how registry keys get restored from files.
	 */
	static final int REG_FORCE_RESTORE = 0x00000008;
	static final int REG_WHOLE_HIVE_VOLATILE = 0x00000001;

	/*
	 * Constants that identify parts of the arrays returned from RegOpenKeyEx,
	 * RegCreateKeyEx, RegQueryInfoKey, and RegQueryValueEx.
	 */

	/**
	 * A constant to retrieve the error code from the array returned by
	 * {@link #getKeyInfo()}.
	 */
	public static final int ERROR_CODE = 0;

	static final int NATIVE_HANDLE = 1;

	/**
	 * A constant to retrieve the number of subkeys from the array returned by
	 * {@link #getKeyInfo()}.
	 */
	public static final int NUM_SUB_KEYS = 1;

	static final int REG_VALUE_TYPE = 1;
	static final int DISPOSITION = 2;

	/**
	 * A constant to retrieve the maximum length of a subkey name from the array
	 * returned by {@link #getKeyInfo()}.
	 */
	public static final int MAX_SUB_KEY_LENGTH = 2;

	static final int REG_VALUE_DATA = 2;

	/**
	 * A constant to retrieve the maximum length of a subkey's class name from
	 * the array returned by {@link #getKeyInfo()}.
	 */
	public static final int MAX_CLASS_LENGTH = 3;

	/**
	 * A constant to retrieve the number of values from the array returned by
	 * {@link #getKeyInfo()}.
	 */
	public static final int NUM_VALUES = 4;

	/**
	 * A constant to retrieve the maximum length of a value's name from the
	 * array returned by {@link #getKeyInfo()}.
	 */
	public static final int MAX_VALUE_NAME_LENGTH = 5;

	/**
	 * A constant to retrieve the maximum length of a value's data from the
	 * array returned by {@link #getKeyInfo()}.
	 */
	public static final int MAX_VALUE_LENGTH = 6;

	/*
	 * Error code constants.
	 */
	public static final int ERROR_SUCCESS = 0; // The operation completed
												// successfully.
	public static final int ERROR_FILE_NOT_FOUND = 2; // The system cannot find
														// the file specified.
	public static final int ERROR_ACCESS_DENIED = 5; // Access is denied.
	public static final int ERROR_INVALID_HANDLE = 6; // The handle is invalid.
	public static final int ERROR_OUTOFMEMORY = 14; // Not enough storage is
													// available to complete
													// this operation.
	public static final int ERROR_INVALID_PARAMETER = 87; // The parameter is
															// incorrect.
	public static final int ERROR_PROC_NOT_FOUND = 127; // The specified
														// procedure could not
														// be found.
	public static final int ERROR_ALREADY_EXISTS = 183; // Cannot create a file
														// when that file
														// already exists.
	public static final int ERROR_FILENAME_EXCED_RANGE = 206; // The filename or
																// extension is
																// too long.
	public static final int ERROR_BADDB = 1009; // The configuration registry
												// database is corrupt.
	public static final int ERROR_BADKEY = 1010; // The configuration registry
													// key is invalid.
	public static final int ERROR_CANTOPEN = 1011; // The configuration registry
													// key could not be opened.
	public static final int ERROR_CANTREAD = 1012; // The configuration registry
													// key could not be read.
	public static final int ERROR_CANTWRITE = 1013; // The configuration
													// registry key could not be
													// written.
	public static final int ERROR_REGISTRY_CORRUPT = 1015; // The registry is
															// corrupted. The
															// structure of one
															// of the files
															// containing
															// registry data is
															// corrupted, or the
															// system's memory
															// image of the file
															// is corrupted, or
															// the file could
															// not be recovered
															// because the
															// alternate copy or
															// log was absent or
															// corrupted.
	public static final int ERROR_REGISTRY_IO_FAILED = 1016; // An I/O operation
																// initiated by
																// the registry
																// failed
																// unrecoverably.
																// The registry
																// could not
																// read in, or
																// write out, or
																// flush, one of
																// the files
																// that contain
																// the system's
																// image of the
																// registry.
	public static final int ERROR_NOT_REGISTRY_FILE = 1017; // The system has
															// attempted to load
															// or restore a file
															// into the
															// registry, but the
															// specified file is
															// not in a registry
															// file format.
	public static final int ERROR_KEY_DELETED = 1018; // Illegal operation
														// attempted on a
														// registry key that has
														// been marked for
														// deletion.
	public static final int ERROR_NO_LOG_SPACE = 1019; // System could not
														// allocate the required
														// space in a registry
														// log.
	public static final int ERROR_KEY_HAS_CHILDREN = 1020; // Cannot create a
															// symbolic link in
															// a registry key
															// that already has
															// subkeys or
															// values.
	public static final int ERROR_CHILD_MUST_BE_VOLATILE = 1021; // Cannot
																	// create a
																	// stable
																	// subkey
																	// under a
																	// volatile
																	// parent
																	// key.
	public static final int ERROR_NO_SYSTEM_RESOURCES = 1450; // Insufficient
																// system
																// resources
																// exist to
																// complete the
																// requested
																// service.

	/*
	 * Max length constants.
	 */
	public static final int MAX_KEY_LENGTH = 255;
	public static final int MAX_CHARACTER_LENGTH = 16383;

	/*
	 * Security access rights constants.
	 */
	static final int DELETE = 0x10000;
	static final int WRITE_DAC = 0x40000;
	static final int KEY_ALL_ACCESS = 0xf003f;
	static final int KEY_CREATE_LINK = 0x0020;
	static final int KEY_CREATE_SUB_KEY = 0x0004;
	static final int KEY_ENUMERATE_SUB_KEYS = 0x0008;
	static final int KEY_EXECUTE = 0x20019;
	static final int KEY_NOTIFY = 0x0010;
	static final int KEY_QUERY_VALUE = 0x0001;
	static final int KEY_READ = 0x20019;
	static final int KEY_SET_VALUE = 0x0002;
	static final int KEY_WOW64_32KEY = 0x0200;
	static final int KEY_WOW64_64KEY = 0x0100;
	static final int KEY_WRITE = 0x20006;

	/*
	 * RegCreateKeyEx create/open options.
	 */
	static final int REG_OPTION_NON_VOLATILE = 0;
	static final int REG_OPTION_VOLATILE = 1;
	static final int REG_OPTION_CREATE_LINK = 2;
	static final int REG_OPTION_BACKUP_RESTORE = 4;
	static final int REG_OPTION_OPEN_LINK = 8;

	/*
	 * RegCreateKeyEx disposition constants.
	 */
	static final int REG_CREATED_NEW_KEY = 0x00000001;
	static final int REG_OPENED_EXISTING_KEY = 0x00000002;

	/*
	 * RegSaveKeyEx constants
	 */
	static final int REG_STANDARD_FORMAT = 1;
	static final int REG_LATEST_FORMAT = 2;
	static final int REG_NO_COMPRESSION = 4;

	/*
	 * RegNotifyChangeKeyValue filter constants.
	 */

	/**
	 * Constant for {@link com.registry.RegistryWatcher} to notify caller
	 * if a subkey is added or deleted.
	 */
	public static final int REG_NOTIFY_CHANGE_NAME = 0x00000001;

	/**
	 * Constant for {@link com.registry.RegistryWatcher} to notify caller
	 * of changes to the attributes of the key, such as the security descriptor
	 * information.
	 */
	public static final int REG_NOTIFY_CHANGE_ATTRIBUTES = 0x00000002;

	/**
	 * Constant for {@link com.registry.RegistryWatcher} to notify caller
	 * of changes to a value of the key. This can include adding or deleting a
	 * value, or changing an existing value.
	 */
	public static final int REG_NOTIFY_CHANGE_LAST_SET = 0x00000004;

	/**
	 * Constant for {@link com.registry.RegistryWatcher} to notify caller
	 * of changes to the security descriptor of the key.
	 */
	public static final int REG_NOTIFY_CHANGE_SECURITY = 0x00000008;

	/*
	 * SetPrivilege constants.
	 */
	static final int SE_BACKUP_NAME = 0x403000;
	static final int SE_RESTORE_NAME = 0x403016;
	static final String SE_BACKUP_NAME_STRING = "SeBackupPrivilege";
	static final String SE_RESTORE_NAME_STRING = "SeRestorePrivilege";

	/**
	 * Constant for {@link com.registry.RegistryWatcher} to notify caller
	 * if a subkey is added or deleted or if a value of the key changed.
	 */
	public static final int DEFAULT_FILTER = REG_NOTIFY_CHANGE_NAME | REG_NOTIFY_CHANGE_LAST_SET;

	/**
	 * Constant for {@link com.registry.RegistryWatcher} combining all of
	 * the possible filters.
	 */
	public static final int ALL_FILTERS = DEFAULT_FILTER | REG_NOTIFY_CHANGE_ATTRIBUTES | REG_NOTIFY_CHANGE_SECURITY;

	static final int NULL_NATIVE_HANDLE = 0;

	/**
	 * Constant that represents the name of the default value.
	 */
	public static final String DEFAULT_VALUE_NAME = "";

	/*
	 * Root key index constants.
	 */

	/**
	 * The index in the {@link #listRoots()} array for the HKEY_CLASSES_ROOT
	 * registry key.
	 */
	public static final int HKEY_CLASSES_ROOT_INDEX = 0;

	/**
	 * The index in the {@link #listRoots()} array for the HKEY_CURRENT_USER
	 * registry key.
	 */
	public static final int HKEY_CURRENT_USER_INDEX = 1;

	/**
	 * The index in the {@link #listRoots()} array for the HKEY_LOCAL_MACHINE
	 * registry key.
	 */
	public static final int HKEY_LOCAL_MACHINE_INDEX = 2;

	/**
	 * The index in the {@link #listRoots()} array for the HKEY_USERS registry
	 * key.
	 */
	public static final int HKEY_USERS_INDEX = 3;

	/**
	 * The index in the {@link #listRoots()} array for the HKEY_PERFORMANCE_DATA
	 * registry key.
	 */
	public static final int HKEY_PERFORMANCE_DATA_INDEX = 4;

	/**
	 * The index in the {@link #listRoots()} array for the HKEY_CURRENT_CONFIG
	 * registry key.
	 */
	public static final int HKEY_CURRENT_CONFIG_INDEX = 5;

	/**
	 * The index in the {@link #listRoots()} array for the HKEY_DYN_DATA
	 * registry key.
	 */
	public static final int HKEY_DYN_DATA_INDEX = 6;

	/**
	 * System property for using the old copy tree method instead of the new
	 * version.
	 */
	public static final String OLD_COPY_TREE = "JRegistry.copyTree.old";

	/**
	 * System property for using the old delete tree method instead of the new
	 * version.
	 */
	public static final String OLD_DELETE_TREE = "JRegistry.deleteTree.old";

	static final int LIBRARY_VERSION = 0x01080001;

	private static native int initIDs();

	static {
		String LIBRARY = null;
		try {

			//System.load(DataSave.JARFILEPATH + "/reg_x64.dll");

			String dllName = null;
			String arch = System.getProperty("os.arch", "x86");
			if (arch.equalsIgnoreCase("x86")) {
				dllName = "reg";
				LIBRARY = DataSave.JARFILEPATH  + "\\" + dllName + ".dll";
			} else {
				dllName = "reg_x64";
				LIBRARY = DataSave.JARFILEPATH  + "\\" + dllName + ".dll";
			}
			// Do NOT load the dll from a network path if the jar is found on a
			// network path!
			// If the path is trusted, then set the java.library.path property
			// to use it.
			if (!LIBRARY.startsWith("\\\\") && (new File(LIBRARY)).exists()) {
				System.load(LIBRARY);
			} 
			int version = 0;
			try {
				version = initIDs();
				if (version != LIBRARY_VERSION) {
					StringBuffer msg = new StringBuffer("JNI version ");
					msg.append(version).append(" doesn't match API version ").append(LIBRARY_VERSION);
					throw new RegistryException(msg.toString());
				}
			} catch (UnsatisfiedLinkError e) {
				StringBuffer msg = new StringBuffer("JNI version ");
				msg.append(version).append(" doesn't match API version ").append(LIBRARY_VERSION);
				throw new RegistryException(msg.toString());
			}
		} catch (UnsatisfiedLinkError e) {
			System.err.println(e.getMessage() + "\n\nCould not load \"" + LIBRARY + "\" file");
			System.exit(1);
		} catch (SecurityException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		roots = new RegistryKey[] { new RegistryKey(HKEY_CLASSES_ROOT), new RegistryKey(HKEY_CURRENT_USER), new RegistryKey(HKEY_LOCAL_MACHINE),
				new RegistryKey(HKEY_USERS), new RegistryKey(HKEY_PERFORMANCE_DATA), new RegistryKey(HKEY_CURRENT_CONFIG), new RegistryKey(HKEY_DYN_DATA) };

		autoExpand = new AtomicBoolean(false);
		computerName = getComputerName();
	}

	private static String getJarFolderName() {
		String myClassName = RegistryKey.class.getSimpleName() + ".class";
		java.net.URL urlJar = RegistryKey.class.getResource(myClassName);
		String urlStr = urlJar.toString();
		if (urlStr.startsWith("jar:file:/")) {
			int from = "jar:file:/".length();
			int to = urlStr.indexOf("!/");
			urlStr = urlStr.substring(from, to);
		} else if (urlStr.startsWith("file:/")) {
			int from = "file:/".length();
			urlStr = urlStr.substring(from);
		}
		File jar = new File(urlStr.replaceAll("%20", " "));
		jar = jar.getParentFile();
		if (jar != null) {
			try {
				return jar.getCanonicalPath();
			} catch (java.io.IOException e) {
				return jar.getAbsolutePath();
			}
		}
		return "";
	}

	/*
	 * Stub method used to invoke the static initializer for RegistryKey if it
	 * wasn't already initialized.
	 */
	static void initialize() {
	}

	class KeyDisposer implements DisposerRecord {
		/*
		 * List of opened handles from WatchData.
		 */
		List<Long> watchHandles;

		public void dispose() {
			RegistryKey.this.closeKey0(0);
			RegistryKey.this.closeKey0(watchHandles, 0);
		}
	}

	private static RegistryKey[] roots;
	private static AtomicBoolean autoExpand;
	private static String computerName;

	private long hKey; // The base key handle
	private RegistryKey parent; // The parent of this
	private String name; // The name of this
	private String path; // The path to this
	private String absolutePath; // The absolute path of this
	private LinkedList<Long> handles; // List of opened handles, useful for when
										// path is longer than 32 levels
	private boolean created; // Tells if this key was created or not
	private int level; // The level in the registry for this
	private int lastError; // The last error returned by a call to RegOpenKeyEx
	private int view64; // What part of the registry to view
	private String machine; // The name of the computer this key belongs to:
							// form=\\computername
	private transient KeyDisposer disposer = new KeyDisposer();

	/**
	 * Internal constructor for creating the root keys.
	 * 
	 * @param hKey
	 *            The predefined handle to a root key.
	 */
	private RegistryKey(long hKey) {
		this.hKey = hKey;
		this.parent = null;
		this.path = "";
		this.handles = new LinkedList<Long>();
		this.created = false;
		this.level = 0;
		String arch = System.getProperty("os.arch", "x86");
		this.view64BitRegistry(!arch.equalsIgnoreCase("x86"));

		if (hKey == HKEY_CLASSES_ROOT)
			this.name = "HKEY_CLASSES_ROOT";
		else if (hKey == HKEY_CURRENT_CONFIG)
			this.name = "HKEY_CURRENT_CONFIG";
		else if (hKey == HKEY_CURRENT_USER)
			this.name = "HKEY_CURRENT_USER";
		else if (hKey == HKEY_DYN_DATA)
			this.name = "HKEY_DYN_DATA";
		else if (hKey == HKEY_LOCAL_MACHINE)
			this.name = "HKEY_LOCAL_MACHINE";
		else if (hKey == HKEY_PERFORMANCE_DATA)
			this.name = "HKEY_PERFORMANCE_DATA";
		else if (hKey == HKEY_USERS)
			this.name = "HKEY_USERS";
		else
			this.name = "";

		Disposer.addRecord(this, disposer);
	}

	/**
	 * Internal constructor used for {@link #getSubKeys()}.
	 * 
	 * @param hKey
	 *            The predefined handle to a root key.
	 * @param parent
	 *            The parent registry key.
	 * @param path
	 *            The path to the key.
	 * @param name
	 *            The name of the key.
	 */
	private RegistryKey(long hKey, RegistryKey parent, String path, String name) {
		this.hKey = hKey;
		this.parent = parent;
		this.path = path;
		this.name = name;
		this.handles = new LinkedList<Long>();
		this.created = false;
		this.level = parent.level + 1;
		this.view64 = parent.view64;
		this.machine = parent.machine;
		Disposer.addRecord(this, disposer);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at HKEY_CURRENT_USER
	 * which goes all the way to <code>subKey</code>. <br>
	 * <br>
	 * The key that is formed from <code>subKey</code> does not have to exist in
	 * the registry.
	 * 
	 * @param subKey
	 *            The subkey under HKEY_CURRENT_USER.
	 * @throws IllegalArgumentException
	 *             if <code>subKey</code> is <code>null</code>.
	 * @see #RegistryKey(String, boolean)
	 * @see #RegistryKey(String, boolean, boolean)
	 * @see #RegistryKey(String, boolean, String)
	 * @see #RegistryKey(String, boolean, String, boolean)
	 */
	public RegistryKey(String subKey) {
		if (subKey == null || subKey == "" || subKey.equals("")) {
			throw new IllegalArgumentException("subKey cannot be null or have a length of zero (0).");
		}

		subKey = toWindowsName(subKey);
		StringTokenizer t = new StringTokenizer(subKey, "\\");
		int levels = t.countTokens();
		int last = subKey.lastIndexOf('\\');

		this.hKey = HKEY_CURRENT_USER;
		this.path = "HKEY_CURRENT_USER" + (last != -1 ? "\\" + subKey.substring(0, last) : "");
		this.name = subKey.substring(last + 1);
		this.handles = new LinkedList<Long>();
		this.created = false;
		this.level = levels;
		String arch = System.getProperty("os.arch", "x86");
		this.view64BitRegistry(!arch.equalsIgnoreCase("x86"));
		Disposer.addRecord(this, disposer);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at HKEY_CURRENT_USER
	 * which goes all the way to <code>subKey</code>. <br>
	 * <br>
	 * The key that is formed from <code>subKey</code> does not have to exist in
	 * the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist.
	 * 
	 * @param subKey
	 *            The subkey under HKEY_CURRENT_USER.
	 * @param create
	 *            If <code>true</code>, will create the specified key now.
	 * @throws IllegalArgumentException
	 *             if <code>subKey</code> is <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(String)
	 * @see #RegistryKey(String, boolean, boolean)
	 * @see #RegistryKey(String, boolean, String)
	 * @see #RegistryKey(String, boolean, String, boolean)
	 */
	public RegistryKey(String subKey, boolean create) {
		this(subKey, create, null, true);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at HKEY_CURRENT_USER
	 * which goes all the way to <code>subKey</code>. <br>
	 * <br>
	 * The key that is formed from <code>subKey</code> does not have to exist in
	 * the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist.
	 * 
	 * @param subKey
	 *            The subkey under HKEY_CURRENT_USER.
	 * @param create
	 *            If <code>true</code>, will create the specified key now.
	 * @param nonVolatile
	 *            <code>true</code> if the subKey should persist,
	 *            <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             if <code>subKey</code> is <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(String)
	 * @see #RegistryKey(String, boolean)
	 * @see #RegistryKey(String, boolean, String)
	 * @see #RegistryKey(String, boolean, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey(String subKey, boolean create, boolean nonVolatile) {
		this(subKey, create, null, nonVolatile);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at HKEY_CURRENT_USER
	 * which goes all the way to <code>subKey</code> with a specific class name. <br>
	 * <br>
	 * The key that is formed from <code>subKey</code> does not have to exist in
	 * the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist. The class name will be applied to the key if it was
	 * created.
	 * 
	 * @param subKey
	 *            The subkey under HKEY_CURRENT_USER.
	 * @param create
	 *            If <code>true</code>, will create the specified key now.
	 * @param clazz
	 *            The user-defined class name for <code>subKey</code>.
	 * @throws IllegalArgumentException
	 *             if <code>subKey</code> is <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(String)
	 * @see #RegistryKey(String, boolean)
	 * @see #RegistryKey(String, boolean, boolean)
	 * @see #RegistryKey(String, boolean, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey(String subKey, boolean create, String clazz) {
		this(subKey, create, clazz, true);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at HKEY_CURRENT_USER
	 * which goes all the way to <code>subKey</code> with a specific class name. <br>
	 * <br>
	 * The key that is formed from <code>subKey</code> does not have to exist in
	 * the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist. The class name will be applied to the key if it was
	 * created.
	 * 
	 * @param subKey
	 *            The subkey under HKEY_CURRENT_USER.
	 * @param create
	 *            If <code>true</code>, will create the specified key now.
	 * @param clazz
	 *            The user-defined class name for <code>subKey</code>.
	 * @param nonVolatile
	 *            <code>true</code> if <code>subKey</code> should persist,
	 *            <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             if <code>subKey</code> is <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(String)
	 * @see #RegistryKey(String, boolean)
	 * @see #RegistryKey(String, boolean, boolean)
	 * @see #RegistryKey(String, boolean, String)
	 * @since 1.4
	 */
	public RegistryKey(String subKey, boolean create, String clazz, boolean nonVolatile) {
		if (subKey == null || subKey == "" || subKey.equals("")) {
			throw new IllegalArgumentException("subKey cannot be null or have a length of zero (0).");
		}

		subKey = toWindowsName(subKey);
		StringTokenizer t = new StringTokenizer(subKey, "\\");
		int levels = t.countTokens();
		int last = subKey.lastIndexOf('\\');
		if (create) {
			int option = (nonVolatile ? REG_OPTION_NON_VOLATILE : REG_OPTION_VOLATILE);
			long[] open = RegCreateKeyEx(HKEY_CURRENT_USER, subKey, option, KEY_CREATE_SUB_KEY, clazz);
			if (open[ERROR_CODE] != ERROR_SUCCESS)
				throw new RegistryException("Registry key could not be created.");
			this.created = (open[DISPOSITION] == REG_CREATED_NEW_KEY);
			open = null;
		}
		this.hKey = HKEY_CURRENT_USER;
		this.path = "HKEY_CURRENT_USER" + (last != -1 ? "\\" + subKey.substring(0, last) : "");
		this.name = subKey.substring(last + 1);
		this.handles = new LinkedList<Long>();
		this.level = levels;
		String arch = System.getProperty("os.arch", "x86");
		this.view64BitRegistry(!arch.equalsIgnoreCase("x86"));
		Disposer.addRecord(this, disposer);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at <code>root</code>
	 * which goes all the way to <code>subKey</code>. <br>
	 * <br>
	 * The key that is formed from <code>root</code> and <code>subKey</code>
	 * does not have to exist in the registry.
	 * 
	 * @param root
	 *            The root of the registry subtree.
	 * @param subKey
	 *            The subkey under <code>root</code>.
	 * @throws IllegalArgumentException
	 *             if <code>root</code> or <code>subKey</code> is
	 *             <code>null</code>.
	 * @see #RegistryKey(RegistryKey, String, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, String)
	 * @see #RegistryKey(RegistryKey, String, boolean, String, boolean)
	 */
	public RegistryKey(RegistryKey root, String subKey) {
		if (root == null) {
			throw new IllegalArgumentException("root cannot be null.");
		} else if (subKey == null || subKey == "" || subKey.equals("")) {
			throw new IllegalArgumentException("subKey cannot be null or have a length of zero (0).");
		}

		subKey = toWindowsName(subKey);
		StringTokenizer t = new StringTokenizer(subKey, "\\");
		int levels = root.level + t.countTokens();
		int last = subKey.lastIndexOf('\\');
		this.hKey = root.hKey;
		this.path = root.getPath() + (last != -1 ? "\\" + subKey.substring(0, last) : "");
		this.name = subKey.substring(last + 1);
		this.handles = new LinkedList<Long>();
		this.created = false;
		this.level = root.level + levels;
		String arch = System.getProperty("os.arch", "x86");
		this.view64BitRegistry(!arch.equalsIgnoreCase("x86"));
		Disposer.addRecord(this, disposer);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at <code>root</code>
	 * which goes all the way to <code>subKey</code>. <br>
	 * <br>
	 * The key that is formed from <code>root</code> and <code>subKey</code>
	 * does not have to exist in the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist.
	 * 
	 * @param root
	 *            The root of the registry subtree.
	 * @param subKey
	 *            The subkey under <code>root</code>.
	 * @param create
	 *            If <code>true</code>, will create the specified key now.
	 * @throws IllegalArgumentException
	 *             if <code>root</code> or <code>subKey</code> is
	 *             <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(RegistryKey, String)
	 * @see #RegistryKey(RegistryKey, String, boolean, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, String)
	 * @see #RegistryKey(RegistryKey, String, boolean, String, boolean)
	 */
	public RegistryKey(RegistryKey root, String subKey, boolean create) {
		this(root, subKey, create, null, true);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at <code>root</code>
	 * which goes all the way to <code>subKey</code>. <br>
	 * <br>
	 * The key that is formed from <code>root</code> and <code>subKey</code>
	 * does not have to exist in the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist.
	 * 
	 * @param root
	 *            The root of the registry subtree.
	 * @param subKey
	 *            The subkey under <code>root</code>.
	 * @param create
	 *            If <code>true</code>, will create the specified key now.
	 * @param nonVolatile
	 *            <code>true</code> if the <code>subKey</code> should persist,
	 *            <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             if <code>root</code> or <code>subKey</code> is
	 *             <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(RegistryKey, String)
	 * @see #RegistryKey(RegistryKey, String, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, String)
	 * @see #RegistryKey(RegistryKey, String, boolean, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey(RegistryKey root, String subKey, boolean create, boolean nonVolatile) {
		this(root, subKey, create, null, nonVolatile);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at <code>root</code>
	 * which goes all the way to <code>subKey</code> with a specific class name. <br>
	 * <br>
	 * The key that is formed from <code>root</code> and <code>subKey</code>
	 * does not have to exist in the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist. The class name will be applied to the key if it was
	 * created.
	 * 
	 * @param root
	 *            The root of the registry subtree.
	 * @param subKey
	 *            The subkey under <code>root</code>.
	 * @param create
	 *            If <code>true</code>, will create the specified key now.
	 * @param clazz
	 *            The user-defined class name for <code>subKey</code>.
	 * @throws IllegalArgumentException
	 *             if <code>root</code> or <code>subKey</code> is
	 *             <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(RegistryKey, String)
	 * @see #RegistryKey(RegistryKey, String, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey(RegistryKey root, String subKey, boolean create, String clazz) {
		this(root, subKey, create, clazz, true);
	}

	/**
	 * Constructs a new <code>RegistryKey</code> rooted at <code>root</code>
	 * which goes all the way to <code>subKey</code> with a specific class name. <br>
	 * <br>
	 * The key that is formed from <code>root</code> and <code>subKey</code>
	 * does not have to exist in the registry. <br>
	 * <br>
	 * An optional boolean value can be passed in to create the registry key if
	 * it doesn't exist. The class name will be applied to the key if it was
	 * created.
	 * 
	 * @param root
	 *            The root of the registry subtree.
	 * @param subKey
	 *            The subkey under <code>root</code>.
	 * @param create
	 *            If <code>true</code> will create the specified key now.
	 * @param clazz
	 *            The user-defined class name for <code>subKey</code>.
	 * @param nonVolatile
	 *            <code>true</code> if <code>subKey</code> should persist,
	 *            <code>false</code> otherwise.
	 * @throws IllegalArgumentException
	 *             if <code>root</code> or <code>subKey</code> is
	 *             <code>null</code>.
	 * @throws RegistryException
	 *             if the key could not be created.
	 * @see #RegistryKey(RegistryKey, String)
	 * @see #RegistryKey(RegistryKey, String, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, boolean)
	 * @see #RegistryKey(RegistryKey, String, boolean, String)
	 * @since 1.4
	 */
	public RegistryKey(RegistryKey root, String subKey, boolean create, String clazz, boolean nonVolatile) {
		if (root == null) {
			throw new IllegalArgumentException("root cannot be null.");
		} else if (subKey == null || subKey == "" || subKey.equals("")) {
			throw new IllegalArgumentException("subKey cannot be null or have a length of zero (0).");
		}

		subKey = toWindowsName(subKey);
		StringTokenizer t = new StringTokenizer(subKey, "\\");
		int levels = root.level + t.countTokens();
		int last = subKey.lastIndexOf('\\');
		if (create) {
			int option = (nonVolatile ? REG_OPTION_NON_VOLATILE : REG_OPTION_VOLATILE);
			long[] open = RegCreateKeyEx(root.hKey, subKey, option, KEY_CREATE_SUB_KEY, clazz);
			if (open[ERROR_CODE] != ERROR_SUCCESS)
				throw new RegistryException("Registry key could not be created.");
			this.created = (open[DISPOSITION] == REG_CREATED_NEW_KEY);
			open = null;
		}
		this.hKey = root.hKey;
		this.path = root.getPath() + (last != -1 ? "\\" + subKey.substring(0, last) : "");
		this.name = subKey.substring(last + 1);
		this.handles = new LinkedList<Long>();
		this.level = levels;
		String arch = System.getProperty("os.arch", "x86");
		this.view64BitRegistry(!arch.equalsIgnoreCase("x86"));
		Disposer.addRecord(this, disposer);
	}

	/**
	 * Specifies whether values of type REG_EXPAND_SZ should have their values
	 * expanded when returned from a call to {@link #getValue(String)} or
	 * {@link #getValues()}. <br>
	 * <br>
	 * <em>Note for v1.8:</em>This method is now made thread safe with the use
	 * of an atomic variable.
	 * 
	 * @param auto
	 *            {@code true} if REG_EXPAND_SZ value data should be expanded,
	 *            {@code false} otherwise.
	 * @since 1.7.3
	 */
	public static synchronized void autoExpandEnvironmentVariables(boolean auto) {
		autoExpand.set(auto);
	}

	/**
	 * Connects to a remote registry on the computer named by
	 * <code>machine</code>. The remote computer must have the Remote Registry
	 * Service started, otherwise a connection won't be established to the
	 * remote computer's registry. If <code>machine</code> is <code>null</code>
	 * or has a length of 0, then this method connects to the local machine. <br>
	 * <br>
	 * Also, <code>root</code> must represent either HKEY_LOCAL_MACHINE,
	 * HKEY_USERS, or HKEY_PERFORMANCE_DATA.
	 * 
	 * @param machine
	 *            The name of the remote computer to connect to. Must be in the
	 *            form: \\computername.
	 * @param root
	 *            The root key of the remote registry to connect to. Must be
	 *            either HKEY_LOCAL_MACHINE, HKEY_USERS, or
	 *            HKEY_PERFORMANCE_DATA.
	 * @return A <code>RegistryKey</code> object which represents the specified
	 *         root key on a remote computer.
	 * @throws IllegalArgumentException
	 *             if root is <code>null</code>.
	 * @throws RegistryException
	 *             if <code>root</code> is not an allowed root key.
	 * @since 1.8
	 */
	public static RegistryKey connect(String machine, RegistryKey root) {
		if (root == null) {
			throw new IllegalArgumentException("root cannot be null.");
		}
		if (machine == null || machine.length() == 0 || machine.equals("") || machine.equalsIgnoreCase(computerName)) {
			RegistryKey remote = new RegistryKey(root.hKey);
			remote.machine = null;
			return remote;
		}

		if (root.equals(roots[HKEY_LOCAL_MACHINE_INDEX]) || root.equals(roots[HKEY_USERS_INDEX]) || root.equals(roots[HKEY_PERFORMANCE_DATA_INDEX])) {
			RegistryKey remote = new RegistryKey(root.hKey);
			remote.machine = machine.toUpperCase();
			return remote;
		}
		throw new RegistryException("Can only connect to HKEY_LOCAL_MACHINE, " + "HKEY_PERFORMANCE_DATA, or HKEY_USERS.");
	}

	private static native String getComputerName();

	/**
	 * Deletes a set of registry keys from the registry. <br>
	 * <em>Note:</em> if an element of <code>keys</code> represents a symbolic
	 * link, then use the {@link #deleteLink()} method on the element instead.
	 * 
	 * @param keys
	 *            The set of registry keys to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #deleteLinks(RegistryKey...)
	 * @since 1.8
	 */
	public static int deleteKeys(RegistryKey... keys) {
		if (keys == null)
			return ERROR_INVALID_PARAMETER;
		int error = ERROR_SUCCESS;
		for (int i = 0; i < keys.length; i++) {
			error = keys[i].deleteKey();
			if (error != ERROR_SUCCESS)
				return error;
		}
		return error;
	}

	/**
	 * Deletes a set of symbolic link registry keys from the registry.
	 * 
	 * @param keys
	 *            The set of symbolic link registry keys to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #deleteKeys(RegistryKey...)
	 * @since 1.8
	 */
	public static int deleteLinks(RegistryKey... keys) {
		if (keys == null)
			return ERROR_INVALID_PARAMETER;
		int error = ERROR_SUCCESS;
		for (int i = 0; i < keys.length; i++) {
			error = keys[i].deleteLink();
			if (error != ERROR_SUCCESS)
				return error;
		}
		return error;
	}

	/**
	 * Allows data from values of REG_EXPAND_SZ to be expanded. This means that
	 * environment variables such as %SystemRoot% or %ProgramFiles% will become
	 * C:\Windows or C:\Program Files respectively.
	 * 
	 * @param envStr
	 *            The {@link String} containing the environment variable to
	 *            expand.
	 * @return The expanded form of <code>envStr</code>.
	 */
	public static String expandEnvironmentVariables(String envStr) {
		return ExpandEnvironmentStrings(envStr);
	}

	/**
	 * Retrieves the description for an error code that was returned by a method
	 * in the {@link RegistryKey} class or one of the {@link RegistryValue}
	 * classes.
	 * 
	 * @param code
	 *            The error code returned from a method.
	 * @return A description of the error code.
	 */
	public static native String formatErrorMessage(int code);

	/**
	 * Retrieves the current size of the registry and the maximum size that the
	 * registry is allowed to attain on the system.
	 * 
	 * @return A {@link RegistryQuota} object that contains the registry size
	 *         values or {@code null} if the function is not supported on the
	 *         current Windows system.
	 * @since 1.8
	 */
	public static RegistryQuota getRegistryQuota() {
		return GetSystemRegistryQuota();
	}

	/**
	 * Retrieves the root key based on the HKEY_*_INDEX values.
	 * 
	 * @param index
	 *            The index of the root key to retrieve, corresponds to the
	 *            HKEY_*_INDEX values.
	 * @return The <code>RegistryKey</code> which is associated with the
	 *         specified index.
	 * @throws ArrayIndexOutOfBoundsException
	 *             if <code>index</code> is negative or greater than the number
	 *             of root keys.
	 * @since 1.8
	 */
	public static RegistryKey getRootKeyForIndex(int index) {
		return roots[index];
	}

	/**
	 * Determines whether values of type REG_EXPAND_SZ have their values
	 * automatically expanded when returned from a call to
	 * {@link #getValue(String)} or {@link #getValues()}. <br>
	 * <br>
	 * <em>Note for v1.8:</em>This method is now made thread safe with the use
	 * of an atomic variable.
	 * 
	 * @return {@code true} if REG_EXPAND_SZ value data should be expanded,
	 *         {@code false} otherwise.
	 */
	public static synchronized boolean isAutoExpandEnvironmentVariables() {
		return autoExpand.get();
	}

	/**
	 * Retrieves an array of all the registry root keys.
	 * 
	 * @return The registry root keys in an array.
	 */
	public static synchronized RegistryKey[] listRoots() {
		RegistryKey[] rk = Arrays.copyOf(roots, roots.length);
		return rk;
	}

	/**
	 * Tranforms the {@link String} path of a registry key into a
	 * <code>RegistryKey</code> object. The path must point to a key that
	 * currently exists in the registry.
	 * 
	 * @param key
	 *            The path to a registry key.
	 * @return A <code>RegistryKey</code> object represented by the path or
	 *         <code>null</code> if the key does not exist.
	 * @see #parseKey(String, boolean)
	 * @see #parseKey(String, boolean, boolean)
	 */
	public static RegistryKey parseKey(String key) {
		return parseKey(key, false, false);
	}

	/**
	 * Tranforms the {@link String} path of a registry key into a
	 * <code>RegistryKey</code> object. The <code>boolean</code> parameter
	 * allows the returned key to be created if it doesn't exist.
	 * 
	 * @param key
	 *            The path to a registry key.
	 * @param create
	 *            If <code>true</code> creates the returned key.
	 * @return A <code>RegistryKey</code> object represented by the path or
	 *         <code>null</code> if the key could not be created.
	 * @see #parseKey(String)
	 * @see #parseKey(String, boolean, boolean)
	 * @since 1.5.1
	 */
	public static RegistryKey parseKey(String key, boolean create) {
		return parseKey(key, create, true);
	}

	/**
	 * Tranforms the {@link String} path of a registry key into a
	 * <code>RegistryKey</code> object. The first optional <code>boolean</code>
	 * parameter allows the returned key to be created if it doesn't exist. The
	 * second <code>boolean</code> determines if the key should be permanent.
	 * 
	 * @param key
	 *            The path to a registry key.
	 * @param create
	 *            If <code>true</code> creates the returned key.
	 * @param nonVolatile
	 *            <code>true</code> if <code>key</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return A <code>RegistryKey</code> object represented by the path or
	 *         <code>null</code> if the key could not be created.
	 */
	public static RegistryKey parseKey(String key, boolean create, boolean nonVolatile) {
		key = toWindowsName(key);
		int backslash = key.indexOf('\\');
		if (backslash == -1) {
			if (key.equals("HKEY_CLASSES_ROOT") || key.equals("HKCR"))
				return roots[HKEY_CLASSES_ROOT_INDEX];
			else if (key.equals("HKEY_CURRENT_USER") || key.equals("HKCU"))
				return roots[HKEY_CURRENT_USER_INDEX];
			else if (key.equals("HKEY_LOCAL_MACHINE") || key.equals("HKLM"))
				return roots[HKEY_LOCAL_MACHINE_INDEX];
			else if (key.equals("HKEY_USERS") || key.equals("HKU") || key.equals("HKUS"))
				return roots[HKEY_USERS_INDEX];
			else if (key.equals("HKEY_PERFORMANCE_DATA") || key.equals("HKPD"))
				return roots[HKEY_PERFORMANCE_DATA_INDEX];
			else if (key.equals("HKEY_CURRENT_CONFIG") || key.equals("HKCC"))
				return roots[HKEY_CURRENT_CONFIG_INDEX];
			else if (key.equals("HKEY_DYN_DATA") || key.equals("HKDD"))
				return roots[HKEY_DYN_DATA_INDEX];
			else
				return null;
		} else {
			String parent = key.substring(0, backslash);
			String child = (backslash == key.length() - 1) ? "" : key.substring(backslash + 1);
			RegistryKey top = null;

			if (parent.equals("HKEY_CLASSES_ROOT") || parent.equals("HKCR"))
				top = roots[HKEY_CLASSES_ROOT_INDEX];
			else if (parent.equals("HKEY_CURRENT_USER") || parent.equals("HKCU"))
				top = roots[HKEY_CURRENT_USER_INDEX];
			else if (parent.equals("HKEY_LOCAL_MACHINE") || parent.equals("HKLM"))
				top = roots[HKEY_LOCAL_MACHINE_INDEX];
			else if (parent.equals("HKEY_USERS") || parent.equals("HKU") || parent.equals("HKUS"))
				top = roots[HKEY_USERS_INDEX];
			else if (key.equals("HKEY_PERFORMANCE_DATA") || parent.equals("HKPD"))
				top = roots[HKEY_PERFORMANCE_DATA_INDEX];
			else if (key.equals("HKEY_CURRENT_CONFIG") || parent.equals("HKCC"))
				top = roots[HKEY_CURRENT_CONFIG_INDEX];
			else if (key.equals("HKEY_DYN_DATA") || parent.equals("HKDD"))
				top = roots[HKEY_DYN_DATA_INDEX];
			else if (parent.equals("Registry")) {
				String machine = "Machine";
				String user = "User";
				String classes = "Software\\Classes";
				String sid = getCurrentUserSid();
				String config = "System\\CurrentControlSet\\Hardware Profiles\\Current";
				int len = child.length();
				if (child.regionMatches(true, 0, machine, 0, machine.length())) {
					if (child.regionMatches(true, machine.length() + 1, classes, 0, classes.length())) {
						int sum = machine.length() + classes.length() + 2;
						child = (sum >= len) ? "" : child.substring(sum);
						top = roots[HKEY_CLASSES_ROOT_INDEX];
					} else if (child.regionMatches(true, machine.length() + 1, config, 0, config.length())) {
						int sum = machine.length() + config.length() + 2;
						child = (sum >= len) ? "" : child.substring(sum);
						top = roots[HKEY_CURRENT_CONFIG_INDEX];
					} else {
						int sum = machine.length() + 1;
						child = (sum >= len) ? "" : child.substring(sum);
						top = roots[HKEY_LOCAL_MACHINE_INDEX];
					}
				} else if (child.regionMatches(true, 0, user, 0, user.length())) {
					if (child.regionMatches(true, user.length() + 1, sid, 0, sid.length())) {
						int sum = user.length() + sid.length() + 2;
						child = (sum >= len) ? "" : child.substring(sum);
						top = roots[HKEY_CURRENT_USER_INDEX];
					} else {
						int sum = user.length() + 1;
						child = (sum >= len) ? "" : child.substring(sum);
						top = roots[HKEY_USERS_INDEX];
					}
				} else {
					top = null;
				}
			} else
				top = null;

			if (!create) {
				if (top != null) {
					if (child.contains("\\")) {
						StringTokenizer t = new StringTokenizer(child, "\\");
						while (t.hasMoreTokens()) {
							String s = t.nextToken();
							if (s.length() != 0 && top.keyExists(s)) {
								top = new RegistryKey(top.hKey, top, top.getPath(), s);
							} else if (s.length() != 0 && top.keyIsLink(s)) {
								top = new RegistryKey(top.hKey, top, top.getPath(), s);
								return top;
							} else if (s.length() == 0 || !(top.keyExists(s) || top.keyIsLink(s))) {
								return null;
							}
						}
						return top;
					} else {
						if (child.length() != 0 && top.keyExists(child)) {
							return new RegistryKey(top.hKey, top, top.getPath(), child);
						} else if (child.length() != 0 && top.keyIsLink(child)) {
							return new RegistryKey(top.hKey, top, top.getPath(), child);
						} else if (child.length() == 0) {
							return top;
						} else {
							return null;
						}
					}
				}
			} else {
				if (top != null) {
					StringTokenizer t = new StringTokenizer(child, "\\");
					while (t.hasMoreTokens()) {
						String str = t.nextToken();
						RegistryKey temp = null;
						if (top.keyIsLink(str)) {
							temp = top.createSubKeyLink(str, null, nonVolatile);
						} else {
							temp = top.createSubKey(str, nonVolatile);
						}
						if (temp != null) {
							top = temp;
						} else {
							top = null;
							break;
						}
					}
					return top;
				}
			}
		}
		return null;
	}

	/**
	 * Retrieves the absolute path of <code>this</code>. An absolute path does
	 * not include the root key in the path.
	 */
	private String absolutePath() {
		if (this.absolutePath == null) {
			int index = this.getPath().indexOf("\\");
			if (index == -1) { // we are a root key
				this.absolutePath = "";
				return this.absolutePath;
			}
			String p = this.getPath().substring(index + 1);
			if (p == null || "".equals(p)) // will this ever happen?
				this.absolutePath = this.name;
			else
				this.absolutePath = p;
		}
		return this.absolutePath;
	}

	/**
	 * Tests to see if <code>this</code> can be deleted. A registry key can only
	 * be deleted if it is not a root key and if it does not contain any
	 * subkeys.
	 * 
	 * @return <code>true</code> if the key can be deleted, <code>false</code>
	 *         otherwise.
	 * @see #deleteKey()
	 * @see #deleteSubKey(String)
	 * @see #deleteView64Key(String)
	 */
	public boolean canDelete() {
		boolean result = false;
		long[] open = this.openKey0(DELETE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE && !this.isRootHandle(handle)) {
			result = !this.hasSubKeys();
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Tests to see if <code>this</code> can be deleted. A registry key can only
	 * be deleted if it is not a root key and if it does not contain any
	 * subkeys.
	 * 
	 * @return <code>true</code> if the key can be deleted, <code>false</code>
	 *         otherwise.
	 * @see #deleteLink()
	 * @since 1.4
	 */
	public boolean canDeleteLink() {
		boolean result = false;
		long[] open = this.openLinkKey(DELETE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE && !this.isRootHandle(handle)) {
			result = true;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Tests to see if the subtree rooted at <code>this</code> can be deleted. A
	 * registry subtree can only be deleted if it is not a root key.
	 * 
	 * @return <code>true</code> if the subtree can be deleted,
	 *         <code>false</code> otherwise.
	 * @see #deleteTree()
	 */
	public boolean canDeleteTree() {
		boolean old = System.getProperty(OLD_DELETE_TREE, "false").equalsIgnoreCase("true");
		int sam = (old ? DELETE : DELETE | KEY_ENUMERATE_SUB_KEYS | KEY_QUERY_VALUE | KEY_SET_VALUE);
		boolean result = false;
		long[] open = this.openKey0(sam, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE && !this.isRootHandle(handle)) {
			result = true;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Tests to see if <code>this</code> can load a registry hive. A registry
	 * key can only load a hive if it is the HKEY_LOCAL_MACHINE or the
	 * HKEY_USERS key.
	 * 
	 * @return <code>true</code> if the key can load a registry hive,
	 *         <code>false</code> otherwise.
	 * @see #loadKey(String, File)
	 */
	public boolean canLoadKey() {
		long handle = this.openKey(KEY_READ);
		if (handle != NULL_NATIVE_HANDLE) {
			this.closeKey(handle);
			return handle == HKEY_LOCAL_MACHINE || handle == HKEY_USERS;
		}
		return false;
	}

	/**
	 * Tests to see if <code>this</code> can be renamed. A registry key can only
	 * be renamed if it is not a root key.
	 * 
	 * @return <code>true</code> if the key can be renamed, <code>false</code>
	 *         otherwise.
	 * @see #renameKey(String)
	 */
	public boolean canRename() {
		boolean result = false;
		long[] open = this.openKey0(KEY_WRITE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE && !this.isRootHandle(handle)) {
			result = true;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Tests to see if <code>this</code> can be unloaded from the registry. A
	 * registry key can only be unloaded if it is the direct descendant of the
	 * HKEY_LOCAL_MACHINE or the HKEY_USERS key.
	 * 
	 * @return <code>true</code> if the key can be unloaded from the registry,
	 *         <code>false</code> otherwise.
	 * @see #unLoadKey()
	 */
	public boolean canUnLoadKey() {
		long handle = this.openKey(KEY_READ);
		if (handle != NULL_NATIVE_HANDLE) {
			this.closeKey(handle);
			return (this.hKey == HKEY_LOCAL_MACHINE || this.hKey == HKEY_USERS) && this.getParent() != null
					&& (this.getParent().equals(roots[2]) || this.getParent().equals(roots[3]));
		}
		return false;
	}

	/**
	 * Tests to see if values can be added to <code>this</code>.
	 * 
	 * @return <code>true</code> if the key can have values, <code>false</code>
	 *         otherwise.
	 * @see #deleteValue(String)
	 * @see #deleteValue(RegistryValue)
	 * @see #newValue(String, ValueType)
	 * @see #renameValue(RegistryValue, String)
	 */
	public boolean canWrite() {
		boolean result = false;
		long[] open = this.openKey0(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			result = true;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Creates a shallow copy of <code>this</code>.
	 * 
	 * @return A copy of <code>this</code> key.
	 */
	public Object clone() {
		try {
			RegistryKey clone = (RegistryKey) super.clone();
			clone.hKey = this.hKey;
			clone.parent = this.parent;
			clone.name = this.name;
			clone.path = this.path;
			clone.absolutePath = this.absolutePath;
			clone.handles = new LinkedList<Long>();
			clone.created = this.created;
			clone.level = this.level;
			clone.lastError = this.lastError;
			clone.view64 = this.view64;
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError();
		}
	}

	/**
	 * Closes the handle to the key which is represented by hKey. Needs to be
	 * accessible to WatchData.
	 */
	int closeKey(long hKey) {
		return RegCloseKey(hKey);
	}

	/**
	 * Closes a list of registry key handles. Needs to be accessible to all
	 * subclasses of RegistryValue.
	 */
	int closeKey0(long stopIndex) {
		return this.closeKey0(this.handles, stopIndex);
	}

	/**
	 * Closes a list of registry key handles as specified by list. Needs to be
	 * accessible to WatchData.
	 */
	int closeKey0(List<Long> list, long stopIndex) {
		int error = ERROR_SUCCESS;
		if (list != null) {
			synchronized (list) {
				int startIndex = (int) (stopIndex + ((this.getLevel() / 32) + 1) + 1);
				if (startIndex > list.size()) {
					int distance = startIndex - list.size();
					startIndex -= distance;
					stopIndex -= distance;
				}
				ListIterator<Long> e = list.listIterator(startIndex);
				while (e.hasPrevious()) {
					Long l = e.previous();
					error |= this.closeKey(l.longValue());
					e.remove();
					if (e.previousIndex() == ((int) stopIndex))
						break;
				}
			}
		}
		return error;
	}

	/**
	 * Implements the compareTo method in the interface {@link Comparable}.
	 * Allows a list of <code>RegistryKey</code>s to be sorted in ascending
	 * order. <code>Null</code> values always come before non-<code>null</code>
	 * values.
	 * 
	 * @param o
	 *            A <code>RegistryKey</code> to compare against
	 *            <code>this</code>.
	 * @return An integer describing which one should be sorted first: if < 0,
	 *         <code>this</code> should go first if > 0, <code>o</code> should
	 *         go first
	 */
	public int compareTo(RegistryKey o) {
		return (o == null ? 1 : (this.hKey != o.hKey ? (int) (this.hKey - o.hKey) : this.getPath().compareToIgnoreCase(o.getPath())));
	}

	/**
	 * Copies all of the subkeys and values from <code>this</code> to
	 * <code>to</code>. <br>
	 * <em>Note for v1.8:</em> This method is now deprecated. The preferred
	 * method is {@link #copyTree(RegistryKey)}.
	 * 
	 * @param to
	 *            The destination <code>RegistryKey</code> for the copy
	 *            operation.
	 */
	@Deprecated
	public void copy(RegistryKey to) {
		List<RegistryKey> subKeys = this.getSubKeys(); // Grab the subkeys
		int size = (subKeys == null) ? 0 : subKeys.size();
		for (int i = 0; i < size; i++) {
			RegistryKey subKey = subKeys.get(i);
			RegistryKey tSubKey = to.createSubKey(subKey.getName()); // Create
																		// the
																		// new
																		// key
			subKey.copy(tSubKey); // copy the new subkey tree
		}
		List<RegistryValue> values = this.getValues(); // Copy the values in
														// this
		int vsize = (values == null) ? 0 : values.size();
		for (int k = 0; k < vsize; k++) {
			RegistryValue value = values.get(k);
			String name = value.getName();
			ValueType type = value.getValueType();
			byte[] b = value.getByteData();
			if (type == null) {
				value = new RegBinaryValue(to, name, value.getValueTypeInt(), null);
				value.setByteData(b);
			} else {
				switch (type) {
				case REG_SZ:
				case REG_EXPAND_SZ:
					value = new RegStringValue(to, name, type, null);
					value.setByteData(b);
					break;
				case REG_BINARY:
				case REG_NONE:
				case REG_LINK:
				case REG_RESOURCE_LIST:
				case REG_FULL_RESOURCE_DESCRIPTOR:
				case REG_RESOURCE_REQUIREMENTS_LIST:
					value = new RegBinaryValue(to, name, type, null);
					value.setByteData(b);
					break;
				case REG_DWORD:
				case REG_DWORD_LITTLE_ENDIAN:
				case REG_DWORD_BIG_ENDIAN:
					value = new RegDWORDValue(to, name, type, null);
					value.setByteData(b);
					break;
				case REG_MULTI_SZ:
					value = new RegMultiStringValue(to, name, type, null);
					value.setByteData(b);
					break;
				case REG_QWORD:
				case REG_QWORD_LITTLE_ENDIAN:
					value = new RegQWORDValue(to, name, type, null);
					value.setByteData(b);
					break;
				}
			}
			b = null;
		}
	}

	/**
	 * Copies all of the subkeys and values from <code>this</code> to
	 * <code>dest</code>. This method will fail if a registry link key is
	 * encountered. <br>
	 * <br>
	 * <em>Note for v1.8:</em> If the operating system is Windows Vista or
	 * higher, then this method uses the Windows function RegCopyTree. If not,
	 * then this method mimics the way that RegCopyTree works. If the older
	 * implementation is preferred, then set the system property
	 * {@link #OLD_COPY_TREE} to "true".
	 * 
	 * @param dest
	 *            The destination <code>RegistryKey</code> for the copy
	 *            operation.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #copyTree(RegistryKey, boolean)
	 * @see #copySubTree(String, RegistryKey)
	 * @see #copySubTree(String, RegistryKey, boolean)
	 * @since 1.8
	 */
	public int copyTree(RegistryKey dest) {
		return copyTree(dest, false);
	}

	/**
	 * Copies all of the subkeys and values from <code>this</code> to
	 * <code>dest</code>. This method will fail if a registry link key is
	 * encountered unless <code>copyLinks</code> is set to <code>true</code>. <br>
	 * <br>
	 * <em>Note for v1.8:</em> If the operating system is Windows Vista or
	 * higher, then this method uses the Windows function RegCopyTree. If not,
	 * then this method mimics the way that RegCopyTree works. If
	 * <code>copyLinks</code> is <code>true</code>, then this method will try to
	 * copy any link keys it finds without using RegCopyTree because that method
	 * can't copy links. If the older implementation is preferred, then set the
	 * system property {@link #OLD_COPY_TREE} to "true".
	 * 
	 * @param dest
	 *            The destination <code>RegistryKey</code> for the copy
	 *            operation.
	 * @param copyLinks
	 *            <code>true</code> if the physical link should be copied,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #copyTree(RegistryKey)
	 * @see #copySubTree(String, RegistryKey)
	 * @see #copySubTree(String, RegistryKey, boolean)
	 * @since 1.8
	 */
	public int copyTree(RegistryKey dest, boolean copyLinks) {
		boolean old = System.getProperty(OLD_COPY_TREE, "false").equalsIgnoreCase("true");
		if (old)
			return (this.lastError = this.copyTree0(this, dest, ERROR_SUCCESS, old, copyLinks));

		if (!dest.exists()) {
			dest.create();
			if (dest.getLastError() == ERROR_CHILD_MUST_BE_VOLATILE) {
				dest.create(false);
			}
		}

		long[] open = this.openKey0(KEY_READ, this.handles);
		long[] open2 = dest.openKey0(KEY_CREATE_SUB_KEY | KEY_QUERY_VALUE | WRITE_DAC, dest.handles);
		long handle = open[NATIVE_HANDLE];
		long handle2 = open2[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE && handle2 != NULL_NATIVE_HANDLE) {
			int error = ERROR_SUCCESS;
			if (copyLinks) {
				error = this.copyTree0(this, dest, ERROR_SUCCESS, old, copyLinks);
			} else {
				error = RegCopyTree(handle, null, handle2);
			}
			this.closeKey0(this.handles, open[0]);
			dest.closeKey0(dest.handles, open2[0]);
			this.lastError = error;
			open = null;
			open2 = null;
			if (error == ERROR_PROC_NOT_FOUND)
				return (this.lastError = this.copyTree0(this, dest, ERROR_SUCCESS, old, copyLinks));
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		dest.closeKey0(dest.handles, open2[0]);
		open = null;
		open2 = null;
		return this.lastError;
	}

	/**
	 * Copies all of the subkeys and values from <code>subkey</code> to
	 * <code>dest</code>. This method will fail if a registry link key is
	 * encountered. <br>
	 * This method uses the Windows Vista implementation for copying a subtree.
	 * 
	 * @param subKey
	 *            The subkey tree under <code>this</code> to copy.
	 * @param dest
	 *            The destination <code>RegistryKey</code> for the copy
	 *            operation.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #copyTree(RegistryKey)
	 * @see #copyTree(RegistryKey, boolean)
	 * @see #copySubTree(String, RegistryKey, boolean)
	 * @since 1.8
	 */
	public int copySubTree(String subKey, RegistryKey dest) {
		return copySubTree(subKey, dest, false);
	}

	/**
	 * Copies all of the subkeys and values from <code>subkey</code> to
	 * <code>dest</code>. This method will fail if a registry link key is
	 * encountered unless <code>copyLinks</code> is set to <code>true</code>. <br>
	 * This method uses the Windows Vista implementation for copying a subtree.
	 * If <code>copyLinks</code> is <code>true</code>, then this method will try
	 * to copy any link keys it finds without using RegCopyTree because that
	 * method can't copy links.
	 * 
	 * @param subKey
	 *            The subkey tree under <code>this</code> to copy.
	 * @param dest
	 *            The destination <code>RegistryKey</code> for the copy
	 *            operation.
	 * @param copyLinks
	 *            <code>true</code> if the physical link should be copied,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #copyTree(RegistryKey)
	 * @see #copyTree(RegistryKey, boolean)
	 * @see #copySubTree(String, RegistryKey)
	 * @since 1.8
	 */
	public int copySubTree(String subKey, RegistryKey dest, boolean copyLinks) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return this.lastError;
		}

		if (!dest.exists()) {
			dest.create();
			if (dest.getLastError() == ERROR_CHILD_MUST_BE_VOLATILE) {
				dest.create(false);
			}
		}

		long[] open = this.openKey0(KEY_READ, this.handles);
		long[] open2 = dest.openKey0(KEY_CREATE_SUB_KEY | KEY_QUERY_VALUE | WRITE_DAC, dest.handles);
		long handle = open[NATIVE_HANDLE];
		long handle2 = open2[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE && handle2 != NULL_NATIVE_HANDLE) {
			int error = ERROR_SUCCESS;
			if (copyLinks) {
				RegistryKey sub = this.getSubKey(subKey);
				if (sub != null)
					error = sub.copyTree0(sub, dest, ERROR_SUCCESS, false, copyLinks);
				error = this.lastError;
			} else {
				error = RegCopyTree(handle, RegistryKey.toWindowsName(subKey), handle2);
			}
			this.closeKey0(this.handles, open[0]);
			dest.closeKey0(dest.handles, open2[0]);
			this.lastError = error;
			open = null;
			open2 = null;
			if (error == ERROR_PROC_NOT_FOUND) {
				RegistryKey sub = this.getSubKey(subKey);
				if (sub != null)
					return (this.lastError = sub.copyTree0(sub, dest, ERROR_SUCCESS, false, copyLinks));
				error = this.lastError;
			}
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		dest.closeKey0(dest.handles, open2[0]);
		open = null;
		open2 = null;
		return this.lastError;
	}

	private int copyTree0(RegistryKey src, RegistryKey dest, int error, boolean old, boolean copyLinks) {
		if (error != ERROR_SUCCESS) {
			return error;
		}

		boolean skip = false;
		if (!old && copyLinks) {
			skip = src.isLinkKey();
			RegistryKey link = src.getLinkLocation();
			if (link != null) {
				long[] open = dest.openLinkKey(KEY_SET_VALUE, dest.handles);
				long handle = open[NATIVE_HANDLE];
				if (handle != NULL_NATIVE_HANDLE) {
					error = RegSetLinkValue(handle, "SymbolicLinkValue", link.toNativePath());
				} else {
					error = dest.getLastError();
				}
				dest.closeKey0(dest.handles, open[0]);
				open = null;
			}
		}
		if (!skip) {
			List<RegistryValue> values = src.getValues(); // Copy the values in
															// this
			int vsize = (values == null) ? 0 : values.size();
			for (int k = 0; k < vsize; k++) {
				RegistryValue value = values.get(k);
				String name = value.getName();
				byte[] b = value.getByteData();

				value = new RegBinaryValue(dest, name, value.getValueTypeInt(), null);
				error = value.setByteData(b);

				b = null;
				name = null;

				if (error != ERROR_SUCCESS) {
					return error;
				}
			}

			List<RegistryKey> subKeys = src.getSubKeys(); // Grab the subkeys
			int size = (subKeys == null) ? 0 : subKeys.size();
			for (int i = 0; i < size; i++) {
				RegistryKey subKey = subKeys.get(i);
				if (!old) {
					if (!copyLinks) {
						if (subKey.isLinkKey())
							return ERROR_INVALID_PARAMETER;
					} else {
						if (subKey.isLinkKey()) {
							RegistryKey link = subKey.getLinkLocation();
							RegistryKey tLink = dest.createSubKeyLink(subKey.getName(), link);
							if (tLink == null && dest.getLastError() == ERROR_ALREADY_EXISTS) {
								tLink = dest.createSubKeyLink(subKey.getName(), link, false);
							} else if (tLink == null) {
								return dest.getLastError();
							}

							long[] open = subKey.openLinkKey(KEY_READ, subKey.handles);
							long[] open2 = tLink.openLinkKey(WRITE_DAC, tLink.handles);
							long handle = open[NATIVE_HANDLE];
							long handle2 = open2[NATIVE_HANDLE];
							if (handle != NULL_NATIVE_HANDLE && handle2 != NULL_NATIVE_HANDLE) {
								error = RegGetSetKeySecurity(handle, handle2);
							}
							subKey.closeKey0(subKey.handles, open[0]);
							tLink.closeKey0(tLink.handles, open2[0]);
							open = open2 = null;
							if (error != ERROR_SUCCESS) {
								return error;
							}

							continue;
						}
					}
				}

				RegistryKey tSubKey = dest.createSubKey(subKey.getName()); // Create
																			// the
																			// new
																			// key
				if (tSubKey == null && dest.getLastError() == ERROR_CHILD_MUST_BE_VOLATILE) {
					tSubKey = dest.createSubKey(subKey.getName(), false);
				} else if (tSubKey == null) {
					return dest.getLastError();
				}

				error = this.copyTree0(subKey, tSubKey, error, old, copyLinks); // copy
																				// the
																				// new
																				// subkey
																				// tree
				if (error != ERROR_SUCCESS) {
					return error;
				}
			}
		}

		if (!old) {
			long[] open = (!skip ? src.openKey0(KEY_READ, src.handles) : src.openLinkKey(KEY_READ, src.handles));
			long[] open2 = dest.openKey0(WRITE_DAC, dest.handles);
			long handle = open[NATIVE_HANDLE];
			long handle2 = open2[NATIVE_HANDLE];
			if (handle != NULL_NATIVE_HANDLE && handle2 != NULL_NATIVE_HANDLE) {
				error = RegGetSetKeySecurity(handle, handle2);
			}
			src.closeKey0(src.handles, open[0]);
			dest.closeKey0(dest.handles, open2[0]);
			open = open2 = null;
			if (error != ERROR_SUCCESS) {
				return error;
			}
		}

		return error;
	}

	private native int RegGetSetKeySecurity(long src, long dest);

	/**
	 * Copies the registry value represented by <code>value</code> to the
	 * registry key <code>dest</code>.
	 * 
	 * @param value
	 *            The registry value to copy.
	 * @param dest
	 *            The registry key that <code>value</code> should be copied to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #copyValues(RegistryKey, String...)
	 * @see #copyValues(RegistryKey, RegistryValue...)
	 * @since 1.8
	 */
	public int copyValue(RegistryKey dest, RegistryValue value) {
		if (value == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);
		String name = value.getName();
		byte[] b = value.getByteData();

		value = new RegBinaryValue(dest, name, value.getValueTypeInt(), null);
		this.lastError = value.setByteData(b);
		b = null;
		name = null;
		return this.lastError;
	}

	/**
	 * Copies the set of registry values named by <code>va</code> to the
	 * registry key <code>dest</code>.
	 * 
	 * @param va
	 *            The set of names of the registry values to copy.
	 * @param dest
	 *            The registry key that the names in <code>va</code> should be
	 *            copied to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #copyValue(RegistryKey, RegistryValue)
	 * @see #copyValues(RegistryKey, RegistryValue...)
	 * @since 1.8
	 */
	public int copyValues(RegistryKey dest, String... va) {
		if (va != null) {
			for (int i = 0; i < va.length; i++) {
				RegistryValue v = this.getValue(va[i]);
				if (v == null)
					return this.lastError;
				this.lastError = this.copyValue(dest, v);
				if (this.lastError != ERROR_SUCCESS)
					return this.lastError;
			}
		}
		return (this.lastError = ERROR_SUCCESS);
	}

	/**
	 * Copies the set of registry values represented by <code>va</code> to the
	 * registry key <code>dest</code>.
	 * 
	 * @param va
	 *            A set of registry values to copy.
	 * @param dest
	 *            The registry key that the values in <code>va</code> should be
	 *            copied to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #copyValue(RegistryKey, RegistryValue)
	 * @see #copyValues(RegistryKey, String...)
	 * @since 1.8
	 */
	public int copyValues(RegistryKey dest, RegistryValue... va) {
		if (va != null) {
			for (int i = 0; i < va.length; i++) {
				this.lastError = this.copyValue(dest, va[i]);
				if (this.lastError != ERROR_SUCCESS)
					return this.lastError;
			}
		}
		return (this.lastError = ERROR_SUCCESS);
	}

	/**
	 * Copies the registry value represented by <code>value</code> within
	 * <code>subKey</code> to the registry key <code>dest</code>.
	 * 
	 * @param subKey
	 *            The subkey from which the values will be copied from.
	 * @param value
	 *            The registry value to copy.
	 * @param dest
	 *            The registry key that <code>value</code> should be copied to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #copySubKeyValues(String, RegistryKey, String...)
	 * @see #copySubKeyValues(String, RegistryKey, RegistryValue...)
	 * @since 1.8
	 */
	public int copySubKeyValue(String subKey, RegistryKey dest, RegistryValue value) {
		RegistryKey sub = this.getSubKey(subKey);
		if (sub == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);

		return (this.lastError = sub.copyValue(dest, value));
	}

	/**
	 * Copies the set of registry values named by <code>va</code> within
	 * <code>subKey</code> to the registry key <code>dest</code>.
	 * 
	 * @param subKey
	 *            The subkey from which the values will be copied from.
	 * @param va
	 *            The set of names of the registry values to copy.
	 * @param dest
	 *            The registry key that the names in <code>va</code> should be
	 *            copied to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #copySubKeyValue(String, RegistryKey, RegistryValue)
	 * @see #copySubKeyValues(String, RegistryKey, RegistryValue...)
	 * @since 1.8
	 */
	public int copySubKeyValues(String subKey, RegistryKey dest, String... va) {
		RegistryKey sub = this.getSubKey(subKey);
		if (sub == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);

		return (this.lastError = sub.copyValues(dest, va));
	}

	/**
	 * Copies the set of registry values represented by <code>va</code> within
	 * <code>subKey</code> to the registry key <code>dest</code>.
	 * 
	 * @param subKey
	 *            The subkey from which the values will be copied from.
	 * @param va
	 *            A set of registry values to copy.
	 * @param dest
	 *            The registry key that the values in <code>va</code> should be
	 *            copied to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #copySubKeyValue(String, RegistryKey, RegistryValue)
	 * @see #copySubKeyValues(String, RegistryKey, String...)
	 * @since 1.8
	 */
	public int copySubKeyValues(String subKey, RegistryKey dest, RegistryValue... va) {
		RegistryKey sub = this.getSubKey(subKey);
		if (sub == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);

		return (this.lastError = sub.copyValues(dest, va));
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #create(boolean)
	 * @see #create(String)
	 * @see #create(String, boolean)
	 */
	public int create() {
		return this.create(null, true);
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry.
	 * 
	 * @param nonVolatile
	 *            <code>true</code> if <code>this</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #create()
	 * @see #create(String)
	 * @see #create(String, boolean)
	 * @since 1.4
	 */
	public int create(boolean nonVolatile) {
		return this.create(null, nonVolatile);
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry with the
	 * specified class name.
	 * 
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #create()
	 * @see #create(boolean)
	 * @see #create(String, boolean)
	 * @since 1.4
	 */
	public int create(String clazz) {
		return this.create(clazz, true);
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry with the
	 * specified class name.
	 * 
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @param nonVolatile
	 *            <code>true</code> if <code>this</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #create()
	 * @see #create(boolean)
	 * @see #create(String)
	 * @since 1.4
	 */
	public int create(String clazz, boolean nonVolatile) {
		String path = this.absolutePath();
		if (path.charAt(0) == '\\')
			path = path.substring(1);
		if (path.charAt(path.length() - 1) == '\\')
			path = path.substring(0, path.length() - 1);
		int option = (nonVolatile ? REG_OPTION_NON_VOLATILE : REG_OPTION_VOLATILE);
		long[] create = RegCreateKeyEx(this.hKey, path, option, KEY_CREATE_SUB_KEY | this.view64, clazz);
		if (create != null && create[ERROR_CODE] == ERROR_SUCCESS) {
			if (create[DISPOSITION] == REG_CREATED_NEW_KEY)
				this.created = true;
			this.closeKey(create[NATIVE_HANDLE]);
		}
		this.lastError = (create == null ? ERROR_OUTOFMEMORY : (int) create[ERROR_CODE]);
		create = null;
		return this.lastError;
	}

	/**
	 * Retrieves whether or not <code>this</code> was created or if it already
	 * existed in the registry.
	 * 
	 * @return <code>true</code> if the key was created, <code>false</code>
	 *         otherwise.
	 */
	public boolean created() {
		return this.created;
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry as a
	 * link to another key.
	 * 
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #createLink(RegistryKey, boolean)
	 * @see #createLink(RegistryKey, String)
	 * @see #createLink(RegistryKey, String, boolean)
	 * @since 1.4
	 */
	public int createLink(RegistryKey target) {
		return this.createLink(target, null, true);
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry as a
	 * link to another key.
	 * 
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @param nonVolatile
	 *            <code>true</code> if <code>this</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #createLink(RegistryKey)
	 * @see #createLink(RegistryKey, String)
	 * @see #createLink(RegistryKey, String, boolean)
	 * @since 1.4
	 */
	public int createLink(RegistryKey target, boolean nonVolatile) {
		return this.createLink(target, null, nonVolatile);
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry with the
	 * specified class name as a link to another key.
	 * 
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #createLink(RegistryKey)
	 * @see #createLink(RegistryKey, boolean)
	 * @see #createLink(RegistryKey, String, boolean)
	 * @since 1.4
	 */
	public int createLink(RegistryKey target, String clazz) {
		return this.createLink(target, clazz, true);
	}

	/**
	 * Creates the key represented by <code>this</code> in the registry with the
	 * specified class name as a link to another key.
	 * 
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @param nonVolatile
	 *            <code>true</code> if <code>this</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #createLink(RegistryKey)
	 * @see #createLink(RegistryKey, boolean)
	 * @see #createLink(RegistryKey, String)
	 * @since 1.4
	 */
	public int createLink(RegistryKey target, String clazz, boolean nonVolatile) {
		String path = this.absolutePath();
		if (path.charAt(0) == '\\')
			path = path.substring(1);
		if (path.charAt(path.length() - 1) == '\\')
			path = path.substring(0, path.length() - 1);
		int option = (nonVolatile ? REG_OPTION_NON_VOLATILE : REG_OPTION_VOLATILE) | REG_OPTION_CREATE_LINK;
		long[] create = RegCreateKeyEx(this.hKey, path, option, KEY_CREATE_SUB_KEY | KEY_CREATE_LINK | this.view64, clazz);
		if (create != null && (create[ERROR_CODE] == ERROR_SUCCESS || create[ERROR_CODE] == ERROR_ALREADY_EXISTS)) {
			if (create[DISPOSITION] == REG_CREATED_NEW_KEY)
				this.created = true;
			this.closeKey(create[NATIVE_HANDLE]);
			this.setLinkTo(target);
		}
		this.lastError = (create == null ? ERROR_OUTOFMEMORY : (int) create[ERROR_CODE]);
		create = null;
		return this.lastError;
	}

	/**
	 * Creates a new subkey under <code>this</code>. If the key could not be
	 * created, then <code>null</code> is returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @return The new <code>RegistryKey</code> representing <code>subKey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKey(String, boolean)
	 * @see #createSubKey(String, String)
	 * @see #createSubKey(String, String, boolean)
	 */
	public RegistryKey createSubKey(String subKey) {
		return this.createSubKey(subKey, null, true);
	}

	/**
	 * Creates a new subkey under <code>this</code>. If the key could not be
	 * created, then <code>null</code> is returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @param nonVolatile
	 *            <code>true</code> if <code>subKey</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return The new <code>RegistryKey</code> representing <code>subKey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKey(String)
	 * @see #createSubKey(String, String)
	 * @see #createSubKey(String, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey createSubKey(String subKey, boolean nonVolatile) {
		return this.createSubKey(subKey, null, nonVolatile);
	}

	/**
	 * Creates a new subkey under <code>this</code> with the specified class
	 * name. If the key could not be created, then <code>null</code> is
	 * returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @return The new <code>RegistryKey</code> representing <code>subKey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKey(String)
	 * @see #createSubKey(String, boolean)
	 * @see #createSubKey(String, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey createSubKey(String subKey, String clazz) {
		return this.createSubKey(subKey, clazz, true);
	}

	/**
	 * Creates a new subkey under <code>this</code> with the specified class
	 * name. If the key could not be created, then <code>null</code> is
	 * returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @param nonVolatile
	 *            <code>true</code> if <code>subKey</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return The new <code>RegistryKey</code> representing <code>subKey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKey(String)
	 * @see #createSubKey(String, boolean)
	 * @see #createSubKey(String, String)
	 * @since 1.4
	 */
	public RegistryKey createSubKey(String subKey, String clazz, boolean nonVolatile) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return null;
		}
		String path = this.absolutePath() + "\\" + RegistryKey.toWindowsName(subKey);
		if (path.charAt(0) == '\\')
			path = path.substring(1);
		if (path.charAt(path.length() - 1) == '\\')
			path = path.substring(0, path.length() - 1);
		int option = (nonVolatile ? REG_OPTION_NON_VOLATILE : REG_OPTION_VOLATILE);
		long[] create = RegCreateKeyEx(this.hKey, path, option, KEY_CREATE_SUB_KEY | this.view64, clazz);
		RegistryKey key = null;
		if (create != null && create[ERROR_CODE] == ERROR_SUCCESS) {
			key = new RegistryKey(this.hKey, this, this.getPath(), subKey);
			if (create[DISPOSITION] == REG_CREATED_NEW_KEY)
				key.created = true;
			this.closeKey(create[NATIVE_HANDLE]);
		}
		this.lastError = (create == null ? ERROR_OUTOFMEMORY : (int) create[ERROR_CODE]);
		create = null;
		return key;
	}

	/**
	 * Creates a new subkey under <code>this</code> which will link to another
	 * key. If the key could not be created, then <code>null</code> is returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @return The new <code>RegistryKey</code> representing <code>subkey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKeyLink(String, RegistryKey, boolean)
	 * @see #createSubKeyLink(String, RegistryKey, String)
	 * @see #createSubKeyLink(String, RegistryKey, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey createSubKeyLink(String subKey, RegistryKey target) {
		return this.createSubKeyLink(subKey, target, null, true);
	}

	/**
	 * Creates a new subkey under <code>this</code> which will link to another
	 * key. If the key could not be created, then <code>null</code> is returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @param nonVolatile
	 *            <code>true</code> if <code>subKey</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return The new <code>RegistryKey</code> representing <code>subKey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKeyLink(String, RegistryKey)
	 * @see #createSubKeyLink(String, RegistryKey, String)
	 * @see #createSubKeyLink(String, RegistryKey, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey createSubKeyLink(String subKey, RegistryKey target, boolean nonVolatile) {
		return this.createSubKeyLink(subKey, target, null, nonVolatile);
	}

	/**
	 * Creates a new subkey under <code>this</code> with the specified class
	 * name as a link to another key. If the key could not be created, then
	 * <code>null</code> is returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @return The new <code>RegistryKey</code> representing <code>subKey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKeyLink(String, RegistryKey)
	 * @see #createSubKeyLink(String, RegistryKey, boolean)
	 * @see #createSubKeyLink(String, RegistryKey, String, boolean)
	 * @since 1.4
	 */
	public RegistryKey createSubKeyLink(String subKey, RegistryKey target, String clazz) {
		return this.createSubKeyLink(subKey, target, clazz, true);
	}

	/**
	 * Creates a new subkey under <code>this</code> with the specified class
	 * name as a link to another key. If the key could not be created, then
	 * <code>null</code> is returned.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to create.
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @param clazz
	 *            The class name for <code>this</code>.
	 * @param nonVolatile
	 *            <code>true</code> if <code>subKey</code> should persist,
	 *            <code>false</code> otherwise.
	 * @return The new <code>RegistryKey</code> representing <code>subKey</code>
	 *         or <code>null</code> if the key could not be created.
	 * @see #createSubKeyLink(String, RegistryKey)
	 * @see #createSubKeyLink(String, RegistryKey, boolean)
	 * @see #createSubKeyLink(String, RegistryKey, String)
	 * @since 1.4
	 */
	public RegistryKey createSubKeyLink(String subKey, RegistryKey target, String clazz, boolean nonVolatile) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return null;
		}
		String path = this.absolutePath() + "\\" + RegistryKey.toWindowsName(subKey);
		if (path.charAt(0) == '\\')
			path = path.substring(1);
		if (path.charAt(path.length() - 1) == '\\')
			path = path.substring(0, path.length() - 1);
		int option = (nonVolatile ? REG_OPTION_NON_VOLATILE : REG_OPTION_VOLATILE) | REG_OPTION_CREATE_LINK;
		long[] create = RegCreateKeyEx(this.hKey, path, option, KEY_CREATE_SUB_KEY | KEY_CREATE_LINK | this.view64, clazz);
		RegistryKey key = null;
		if (create != null && (create[ERROR_CODE] == ERROR_SUCCESS || create[ERROR_CODE] == ERROR_ALREADY_EXISTS)) {
			key = new RegistryKey(this.hKey, this, this.getPath(), subKey);
			if (create[DISPOSITION] == REG_CREATED_NEW_KEY)
				key.created = true;
			this.closeKey(create[NATIVE_HANDLE]);
			key.setLinkTo(target);
		}
		this.lastError = (create == null ? ERROR_OUTOFMEMORY : (int) create[ERROR_CODE]);
		create = null;
		return key;
	}

	/**
	 * Deletes <code>this</code> from the registry. <br>
	 * <em>Note:</em> if <code>this</code> represents a symbolic link, then use
	 * the {@link #deleteLink()} method instead.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDelete()
	 * @see #deleteSubKey(String)
	 * @see #deleteSubKeys(String...)
	 * @see #deleteView64Key(String)
	 * @see #deleteView64Keys(String...)
	 */
	public int deleteKey() {
		RegistryKey parent = this.getParent();
		if (parent == null)
			return ERROR_ACCESS_DENIED;
		long[] open = parent.openKey0(DELETE, parent.handles);
		long parentHandle = open[NATIVE_HANDLE];
		if (parentHandle != NULL_NATIVE_HANDLE) {
			int error = RegDeleteKey(parentHandle, this.name);
			parent.closeKey0(parent.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		parent.closeKey0(parent.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes <code>this</code> symbolic link from the registry.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDeleteLink()
	 * @since 1.4
	 */
	public int deleteLink() {
		if (!this.isLinkKey())
			return this.lastError;
		long[] open = this.openLinkKey(DELETE | KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = RegDeleteLink(handle);
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes <code>subKey</code> from <code>this</code>. <br>
	 * <em>Note:</em> if <code>subKey</code> represents a symbolic link, then
	 * use the {@link #deleteLink()} method on the subkey instead.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDelete()
	 * @see #deleteKey()
	 * @see #deleteSubKeys(String...)
	 * @see #deleteView64Key(String)
	 * @see #deleteView64Keys(String...)
	 */
	public int deleteSubKey(String subKey) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return this.lastError;
		}
		long[] open = this.openKey0(DELETE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = RegDeleteKey(handle, RegistryKey.toWindowsName(subKey));
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes the <code>subKey</code>s from <code>this</code>. <br>
	 * <em>Note:</em> if an element of <code>subKey</code>s represents a
	 * symbolic link, then use the {@link #deleteLink()} method on the subkey
	 * instead.
	 * 
	 * @param subKeys
	 *            The subkeys under <code>this</code> to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDelete()
	 * @see #deleteKey()
	 * @see #deleteSubKey(String)
	 * @see #deleteView64Key(String)
	 * @see #deleteView64Keys(String...)
	 * @since 1.8
	 */
	public int deleteSubKeys(String... subKeys) {
		if (subKeys == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);
		long[] open = this.openKey0(DELETE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			for (int i = 0; i < subKeys.length; i++) {
				String subKey = subKeys[i];
				if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
					this.lastError = ERROR_INVALID_PARAMETER;
					this.closeKey0(this.handles, open[i]);
					open = null;
					return this.lastError;
				}
				this.lastError = RegDeleteKey(handle, RegistryKey.toWindowsName(subKey));
				if (this.lastError != ERROR_SUCCESS) {
					this.closeKey0(this.handles, open[i]);
					open = null;
					return this.lastError;
				}
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes <code>subKey</code> from a 64-bit specific view of the registry
	 * from <code>this</code> if {@link #isViewing64BitRegistry()} is
	 * <code>true</code>. Otherwise <code>subKey</code> is deleted from the
	 * 32-bit specific view of the registry. <br>
	 * <em>Note:</em> if <code>subKey</code> represents a symbolic link, then
	 * use the {@link #deleteLink()} method on the subkey instead.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDelete()
	 * @see #deleteKey()
	 * @see #deleteSubKey(String)
	 * @see #deleteSubKeys(String...)
	 * @see #deleteView64Keys(String...)
	 * @see #isViewing64BitRegistry()
	 * @see #view64BitRegistry(boolean)
	 * @since 1.8
	 */
	public int deleteView64Key(String subKey) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return this.lastError;
		}
		long[] open = this.openKey0(DELETE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = RegDeleteKeyEx(handle, RegistryKey.toWindowsName(subKey), this.view64);
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes the <code>subKey</code>s from a 64-bit specific view of the
	 * registry from <code>this</code> if {@link #isViewing64BitRegistry()} is
	 * <code>true</code>. Otherwise the <code>subKey</code>s are deleted from
	 * the 32-bit specific view of the registry. <br>
	 * <em>Note:</em> if an element of <code>subKey</code>s represents a
	 * symbolic link, then use the {@link #deleteLink()} method on the subkey
	 * instead.
	 * 
	 * @param subKeys
	 *            The subkeys under <code>this</code> to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDelete()
	 * @see #deleteKey()
	 * @see #deleteSubKey(String)
	 * @see #deleteSubKeys(String...)
	 * @see #deleteView64Key(String)
	 * @see #isViewing64BitRegistry()
	 * @see #view64BitRegistry(boolean)
	 * @since 1.8
	 */
	public int deleteView64Keys(String... subKeys) {
		if (subKeys == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);
		long[] open = this.openKey0(DELETE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			for (int i = 0; i < subKeys.length; i++) {
				String subKey = subKeys[i];
				if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
					this.lastError = ERROR_INVALID_PARAMETER;
					this.closeKey0(this.handles, open[i]);
					open = null;
					return this.lastError;
				}
				this.lastError = RegDeleteKeyEx(handle, RegistryKey.toWindowsName(subKey), this.view64);
				if (this.lastError != ERROR_SUCCESS) {
					this.closeKey0(this.handles, open[i]);
					open = null;
					return this.lastError;
				}
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes the entire subtree rooted at <code>this</code> from the registry. <br>
	 * <em>Note:</em> if <code>this</code> represents a symbolic link, then use
	 * the {@link #deleteLink()} method instead. <br>
	 * <em>Note for v1.8:</em> The implementation for this method has changed.
	 * If the operating system is Windows Vista or higher, then this method uses
	 * the Windows function RegDeleteTree. If not, then this method mimics the
	 * way that RegDeleteTree works. This means that only the subkeys and values
	 * under <code>this</code> key will be deleted, not <code>this</code>. If
	 * the older implementation is preferred, then set the system property
	 * {@link #OLD_DELETE_TREE} to "true".
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDeleteTree()
	 * @see #deleteTree(boolean)
	 * @see #deleteSubTree(String)
	 * @see #deleteSubTree(String, boolean)
	 */
	public int deleteTree() {
		return this.deleteTree(false);
	}

	/**
	 * Deletes the entire subtree rooted at <code>this</code> from the registry. <br>
	 * <em>Note:</em> if <code>this</code> represents a symbolic link, then use
	 * the {@link #deleteLink()} method instead. <br>
	 * <br>
	 * If the operating system is Windows Vista or higher, then this method uses
	 * the Windows function RegDeleteTree. If not, then this method mimics the
	 * way that RegDeleteTree works. This means that only the subkeys and values
	 * under <code>this</code> key will be deleted, not <code>this</code>. If
	 * <code>delLinks</code> is <code>true</code>, then this method will try to
	 * delete any link keys it finds without using RegDeleteTree because that
	 * method can't delete links. If the older implementation is preferred, then
	 * set the system property {@link #OLD_DELETE_TREE} to "true".
	 * 
	 * @param delLinks
	 *            <code>true</code> if link keys should be deleted,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDeleteTree()
	 * @see #deleteTree()
	 * @see #deleteSubTree(String)
	 * @see #deleteSubTree(String, boolean)
	 * @since 1.8
	 */
	public int deleteTree(boolean delLinks) {
		boolean old = System.getProperty(OLD_DELETE_TREE, "false").equalsIgnoreCase("true");
		if (old)
			return this.deleteTree0(old, delLinks);
		long[] open = this.openKey0(DELETE | KEY_ENUMERATE_SUB_KEYS | KEY_QUERY_VALUE | KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = ERROR_SUCCESS;
			if (delLinks) {
				error = this.deleteTree0(old, delLinks);
				this.closeKey0(this.handles, open[0]);
			} else {
				error = RegDeleteTree(handle, null);
				this.closeKey0(this.handles, open[0]);
			}
			this.lastError = error;
			open = null;
			if (error == ERROR_PROC_NOT_FOUND)
				return this.deleteTree0(old, delLinks);
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes the entire subtree rooted at <code>subKey</code> from the
	 * registry.<br>
	 * This method uses the Windows Vista implementation for deleting a subtree. <br>
	 * <em>Note:</em> if <code>subKey</code> represents a symbolic link, then
	 * use the {@link #deleteLink()} method on the subkey instead.
	 * 
	 * @param subKey
	 *            The subkey tree under <code>this</code> to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDeleteTree()
	 * @see #deleteTree()
	 * @see #deleteTree(boolean)
	 * @see #deleteSubTree(String, boolean)
	 * @since 1.8
	 */
	public int deleteSubTree(String subKey) {
		return this.deleteSubTree(subKey, false);
	}

	/**
	 * Deletes the entire subtree rooted at <code>subKey</code> from the
	 * registry.<br>
	 * This method uses the Windows Vista implementation for deleting a subtree.
	 * If <code>delLinks</code> is <code>true</code>, then this method will try
	 * to delete any link keys it finds without using RegDeleteTree because that
	 * method can't delete links. <br>
	 * <em>Note:</em> if <code>subKey</code> represents a symbolic link, then
	 * use the {@link #deleteLink()} method on the subkey instead.
	 * 
	 * @param subKey
	 *            The subkey tree under <code>this</code> to delete.
	 * @param delLinks
	 *            <code>true</code> if link keys should be deleted,
	 *            <code>false</code> otherwise.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canDeleteTree()
	 * @see #deleteTree()
	 * @see #deleteTree(boolean)
	 * @see #deleteSubTree(String)
	 * @since 1.8
	 */
	public int deleteSubTree(String subKey, boolean delLinks) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return this.lastError;
		}
		long[] open = this.openKey0(DELETE | KEY_ENUMERATE_SUB_KEYS | KEY_QUERY_VALUE | KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = ERROR_SUCCESS;
			if (delLinks) {
				RegistryKey sub = this.getSubKey(subKey);
				if (sub != null) {
					error = sub.deleteTree0(false, delLinks);
					if (error == ERROR_SUCCESS)
						error = RegDeleteKey(handle, RegistryKey.toWindowsName(subKey));
				} else {
					error = this.lastError;
				}
			} else {
				error = RegDeleteTree(handle, RegistryKey.toWindowsName(subKey));
			}
			if (error == ERROR_PROC_NOT_FOUND) {
				RegistryKey sub = this.getSubKey(subKey);
				if (sub != null) {
					error = sub.deleteTree0(false, delLinks);
					if (error == ERROR_SUCCESS)
						error = RegDeleteKey(handle, RegistryKey.toWindowsName(subKey));
				} else {
					error = this.lastError;
				}
			}
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	private int deleteTree0(boolean old, boolean delLinks) {
		if (old)
			return (this.lastError = this.deleteTree1(this, ERROR_SUCCESS, old, delLinks));
		int error = ERROR_SUCCESS;
		if (this.hasSubKeys()) {
			List<RegistryKey> l = this.getSubKeys();
			for (int i = 0; i < l.size(); i++) {
				error = this.deleteTree1(l.get(i), error, old, delLinks);
				if (error != ERROR_SUCCESS) {
					return (this.lastError = error);
				}
			}
		}
		return (this.lastError = error);
	}

	/**
	 * Recursive helper method to delete the subtree.
	 */
	private int deleteTree1(RegistryKey root, int error, boolean old, boolean delLinks) {
		if (error != ERROR_SUCCESS) {
			return error;
		}
		if (delLinks) {
			if (root.isLinkKey()) {
				error = root.deleteLink();
				return error;
			}
		}
		if (root.hasSubKeys()) {
			List<RegistryKey> l = root.getSubKeys();
			for (int i = 0; i < l.size(); i++) {
				error = this.deleteTree1(l.get(i), error, old, delLinks);
				if (error != ERROR_SUCCESS) {
					return error;
				}
			}
		}
		if (old) {
			return root.deleteKey();
		} else {
			error = root.deleteKey();
			if (root.isLinkKey())
				error = root.deleteKey();
			return error;
		}
	}

	/**
	 * Deletes a named value from <code>this</code>.
	 * 
	 * @param value
	 *            The name of the value to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(RegistryValue)
	 * @see #deleteValues(String...)
	 * @see #deleteValues(RegistryValue...)
	 * @see #deleteSubKeyValue(String, String)
	 * @see #deleteSubKeyValue(String, RegistryValue)
	 * @see #deleteSubKeyValues(String, String...)
	 * @see #deleteSubKeyValues(String, RegistryValue...)
	 */
	public int deleteValue(String value) {
		if (value == null) {
			value = DEFAULT_VALUE_NAME;
		}
		long[] open = this.openKey0(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = RegDeleteValue(handle, value);
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes a named value represented by <code>value</code> from
	 * <code>this</code>.
	 * 
	 * @param value
	 *            The <code>RegistryValue</code> to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(String)
	 * @see #deleteValues(String...)
	 * @see #deleteValues(RegistryValue...)
	 * @see #deleteSubKeyValue(String, String)
	 * @see #deleteSubKeyValue(String, RegistryValue)
	 * @see #deleteSubKeyValues(String, String...)
	 * @see #deleteSubKeyValues(String, RegistryValue...)
	 */
	public int deleteValue(RegistryValue value) {
		return this.deleteValue(value.getName());
	}

	/**
	 * Deletes a set of named values within <code>va</code> from
	 * <code>this</code>.
	 * 
	 * @param va
	 *            The set of names of the values to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(String)
	 * @see #deleteValue(RegistryValue)
	 * @see #deleteValues(RegistryValue...)
	 * @see #deleteSubKeyValue(String, String)
	 * @see #deleteSubKeyValue(String, RegistryValue)
	 * @see #deleteSubKeyValues(String, String...)
	 * @see #deleteSubKeyValues(String, RegistryValue...)
	 * @since 1.8
	 */
	public int deleteValues(String... va) {
		if (va == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);
		long[] open = this.openKey0(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			for (int i = 0; i < va.length; i++) {
				if (va[i] == null) {
					va[i] = DEFAULT_VALUE_NAME;
				}
				this.lastError = RegDeleteValue(handle, va[i]);
				if (this.lastError != ERROR_SUCCESS) {
					this.closeKey0(this.handles, open[0]);
					open = null;
					return this.lastError;
				}
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes a set of named values represented by <code>RegistryValues</code>
	 * within <code>va</code> from <code>this</code>.
	 * 
	 * @param va
	 *            The set of <code>RegistryValue</code>s to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(String)
	 * @see #deleteValue(RegistryValue)
	 * @see #deleteValues(String...)
	 * @see #deleteSubKeyValue(String, String)
	 * @see #deleteSubKeyValue(String, RegistryValue)
	 * @see #deleteSubKeyValues(String, String...)
	 * @see #deleteSubKeyValues(String, RegistryValue...)
	 * @since 1.8
	 */
	public int deleteValues(RegistryValue... va) {
		if (va == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);
		long[] open = this.openKey0(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			for (int i = 0; i < va.length; i++) {
				String val = va[i].getName();
				if (val == null) {
					val = DEFAULT_VALUE_NAME;
				}
				this.lastError = RegDeleteValue(handle, val);
				if (this.lastError != ERROR_SUCCESS) {
					this.closeKey0(this.handles, open[0]);
					open = null;
					return this.lastError;
				}
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes a named value from <code>subKey</code>. If <code>subKey</code> is
	 * <code>null</code> or has a length of 0, then this behaves exactly as
	 * {@link #deleteValue(String)}.
	 * 
	 * @param subKey
	 *            The subKey from which to delete the value.
	 * @param value
	 *            The name of the value to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(String)
	 * @see #deleteValue(RegistryValue)
	 * @see #deleteValues(String...)
	 * @see #deleteValues(RegistryValue...)
	 * @see #deleteSubKeyValue(String, RegistryValue)
	 * @see #deleteSubKeyValues(String, String...)
	 * @see #deleteSubKeyValues(String, RegistryValue...)
	 * @since 1.8
	 */
	public int deleteSubKeyValue(String subKey, String value) {
		if (value == null) {
			value = DEFAULT_VALUE_NAME;
		}
		long[] open = this.openKey0(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = RegDeleteKeyValue(handle, subKey, value);
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			if (error == ERROR_PROC_NOT_FOUND) {
				if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
					return this.deleteValue(value);
				} else {
					RegistryKey sub = this.getSubKey(subKey);
					if (sub != null)
						error = sub.deleteValue(value);
					else
						error = this.lastError = ERROR_FILE_NOT_FOUND;
				}
			}
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes a named value represented by <code>value</code> from
	 * <code>subKey</code>. If <code>subKey</code> is <code>null</code> or has a
	 * length of 0, then this behaves exactly as
	 * {@link #deleteValue(RegistryValue)}.
	 * 
	 * @param subKey
	 *            The subKey from which to delete the value.
	 * @param value
	 *            The <code>RegistryValue</code> to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(String)
	 * @see #deleteValue(RegistryValue)
	 * @see #deleteValues(String...)
	 * @see #deleteValues(RegistryValue...)
	 * @see #deleteSubKeyValue(String, String)
	 * @see #deleteSubKeyValues(String, String...)
	 * @see #deleteSubKeyValues(String, RegistryValue...)
	 * @since 1.8
	 */
	public int deleteSubKeyValue(String subKey, RegistryValue value) {
		return this.deleteSubKeyValue(subKey, value.getName());
	}

	/**
	 * Deletes a set of named values within <code>va</code> from
	 * <code>subKey</code>. If <code>subKey</code> is <code>null</code> or has a
	 * length of 0, then this behaves exactly as
	 * {@link #deleteValues(String...)}.
	 * 
	 * @param subKey
	 *            The subKey from which to delete the values.
	 * @param va
	 *            The set of names of the values to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(String)
	 * @see #deleteValue(RegistryValue)
	 * @see #deleteValues(String...)
	 * @see #deleteValues(RegistryValue...)
	 * @see #deleteSubKeyValue(String, String)
	 * @see #deleteSubKeyValue(String, RegistryValue)
	 * @see #deleteSubKeyValues(String, RegistryValue...)
	 * @since 1.8
	 */
	public int deleteSubKeyValues(String subKey, String... va) {
		if (va == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);
		long[] open = this.openKey0(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			for (int i = 0; i < va.length; i++) {
				if (va[i] == null) {
					va[i] = DEFAULT_VALUE_NAME;
				}
				this.lastError = RegDeleteKeyValue(handle, subKey, va[i]);
				if (this.lastError == ERROR_PROC_NOT_FOUND) {
					if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
						this.lastError = RegDeleteValue(handle, va[i]);
					} else {
						RegistryKey sub = this.getSubKey(subKey);
						if (sub != null)
							this.lastError = sub.deleteValue(va[i]);
						else
							this.lastError = ERROR_FILE_NOT_FOUND;
					}
				}
				if (this.lastError != ERROR_SUCCESS) {
					this.closeKey0(this.handles, open[0]);
					open = null;
					return this.lastError;
				}
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Deletes a set of named values represented by <code>va</code> from
	 * <code>subKey</code>. If <code>subKey</code> is <code>null</code> or has a
	 * length of 0, then this behaves exactly as
	 * {@link #deleteValues(RegistryValue...)}.
	 * 
	 * @param subKey
	 *            The subKey from which to delete the values.
	 * @param va
	 *            The set of <code>RegistryValue</code>s to delete.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canWrite()
	 * @see #deleteValue(String)
	 * @see #deleteValue(RegistryValue)
	 * @see #deleteValues(String...)
	 * @see #deleteValues(RegistryValue...)
	 * @see #deleteSubKeyValue(String, String)
	 * @see #deleteSubKeyValue(String, RegistryValue)
	 * @see #deleteSubKeyValues(String, String...)
	 * @since 1.8
	 */
	public int deleteSubKeyValues(String subKey, RegistryValue... va) {
		if (va == null)
			return (this.lastError = ERROR_INVALID_PARAMETER);
		long[] open = this.openKey0(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			for (int i = 0; i < va.length; i++) {
				String val = va[i].getName();
				if (val == null) {
					val = DEFAULT_VALUE_NAME;
				}
				this.lastError = RegDeleteKeyValue(handle, subKey, val);
				if (this.lastError == ERROR_PROC_NOT_FOUND) {
					if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
						this.lastError = RegDeleteValue(handle, val);
					} else {
						RegistryKey sub = this.getSubKey(subKey);
						if (sub != null)
							this.lastError = sub.deleteValue(val);
						else
							this.lastError = ERROR_FILE_NOT_FOUND;
					}
				}
				if (this.lastError != ERROR_SUCCESS) {
					this.closeKey0(this.handles, open[0]);
					open = null;
					return this.lastError;
				}
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Disables registry reflection for <code>this</code> key. <br>
	 * <em>Note:</em> This method only works on 64-bit versions of Windows XP
	 * and higher.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #disableReflectionTree
	 * @since 1.6
	 */
	public int disableReflection() {
		return (this.lastError = RegDisableReflectionKey(this.hKey));
	}

	/**
	 * Disables registry reflection for <code>this</code> key and its entire
	 * subtree. <br>
	 * <em>Note:</em> This method only works on 64-bit versions of Windows XP
	 * and higher.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #disableReflection()
	 * @since 1.6
	 */
	public int disableReflectionTree() {
		return (this.lastError = this.disableReflectionTree(this, ERROR_SUCCESS));
	}

	/**
	 * Recursive helper method to disable reflection on the subtree.
	 */
	private int disableReflectionTree(RegistryKey root, int error) {
		if (error != ERROR_SUCCESS) {
			return error;
		}
		if (root.hasSubKeys()) {
			List<RegistryKey> l = root.getSubKeys();
			for (int i = 0; i < l.size(); i++) {
				error = this.disableReflectionTree(l.get(i), error);
				if (error != ERROR_SUCCESS) {
					return error;
				}
			}
			error = root.disableReflection();
			return error;
		}
		return root.disableReflection();
	}

	/**
	 * Enables registry reflection for <code>this</code> key. <br>
	 * <em>Note:</em> This method only works on 64-bit versions of Windows XP
	 * and higher.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #enableReflectionTree
	 * @since 1.6
	 */
	public int enableReflection() {
		return (this.lastError = RegEnableReflectionKey(this.hKey));
	}

	/**
	 * Enables registry reflection for <code>this</code> key and its entire
	 * subtree. <br>
	 * <em>Note:</em> This method only works on 64-bit versions of Windows XP
	 * and higher.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #enableReflection()
	 * @since 1.6
	 */
	public int enableReflectionTree() {
		return (this.lastError = this.enableReflectionTree(this, ERROR_SUCCESS));
	}

	/**
	 * Recursive helper method to enable reflection on the subtree.
	 */
	private int enableReflectionTree(RegistryKey root, int error) {
		if (error != ERROR_SUCCESS) {
			return error;
		}
		if (root.hasSubKeys()) {
			List<RegistryKey> l = root.getSubKeys();
			for (int i = 0; i < l.size(); i++) {
				error = this.enableReflectionTree(l.get(i), error);
				if (error != ERROR_SUCCESS) {
					return error;
				}
			}
			error = root.enableReflection();
			return error;
		}
		return root.enableReflection();
	}

	/**
	 * Tests for equality with another {@link Object}. Two {@link Object}s are
	 * equal if they are both of type <code>RegistryKey</code>, and both of
	 * their names are the same.
	 * 
	 * @param o
	 *            An {@link Object} to test equality with.
	 * @return <code>true</code> if the two are equal, <code>false</code>
	 *         otherwise.
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RegistryKey))
			return false;
		RegistryKey k = (RegistryKey) o;
		return this.compareTo(k) == 0;
	}

	/**
	 * Tests to see if <code>this</code> exists in the registry.
	 * 
	 * @return <code>true</code> if <code>this</code> exists, <code>false</code>
	 *         otherwise.
	 */
	public boolean exists() {
		boolean result = false;
		long[] open = this.openKey0(KEY_READ, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE || this.lastError == ERROR_ACCESS_DENIED) {
			result = true;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Writes all attributes of <code>this</code> into the registry. <br>
	 * <br>
	 * Calling <code>flushKey</code> is an expensive operation that will
	 * signficantly affect performance because it blocks modifications to
	 * <code>this</code> until the flush operation completes.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 */
	public int flushKey() {
		if (!this.exists())
			return this.getParent().flushKey();
		long[] open = this.openKey0(KEY_READ, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int error = RegFlushKey(handle);
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Retrieves the class name for <code>this</code> if it is set.
	 * 
	 * @return The class name of <code>this</code> if set or <code>null</code>
	 *         otherwise.
	 */
	public String getClassName() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			String clazz = this.getClassName(handle, info[MAX_CLASS_LENGTH] + 1);
			this.closeKey0(this.handles, open[0]);
			info = null;
			open = null;
			return clazz;
		}
		this.closeKey0(this.handles, open[0]);
		info = null;
		open = null;
		return null;
	}

	// native method to retrieve the class name
	private native String getClassName(long hKey, int lpcMaxClassLen);

	/**
	 * Retrieves the current user's SID in {@link String} format.
	 * 
	 * @return A {@link String} representation of the current user's SID.
	 * @since 1.4
	 */
	public static native String getCurrentUserSid();

	/**
	 * Retrieves the index of <code>this</code> under the parent. The index is
	 * zero based.
	 * 
	 * @return The index of <code>this</code> or Integer.MIN_VALUE if
	 *         <code>this</code> is a root key or if <code>this</code> no longer
	 *         exists.
	 */
	public int getIndex() {
		if (this.getParent() != null) {
			String[] keyNames = this.getParent().getSubKeyNames();
			int index = Arrays.binarySearch(keyNames, this.name, String.CASE_INSENSITIVE_ORDER);
			keyNames = null;
			if (index < 0)
				index = Integer.MIN_VALUE;
			return index;
		}
		return Integer.MIN_VALUE;
	}

	/**
	 * Retrieves information about <code>this</code>. The information includes: <br>
	 * <br>
	 * <ul>
	 * <li>The number of subkeys in <code>this</code></li>
	 * <li>The length of the name of the longest subkey</li>
	 * <li>The length of the class name for <code>this</code></li>
	 * <li>The number of values in <code>this</code></li>
	 * <li>The length of the name of the longest value</li>
	 * <li>The number of bytes in the largest data component of the values</li>
	 * </ul>
	 * 
	 * @return An array of integers containing various information about
	 *         <code>this</code> or <code>null</code> if an error occurred.
	 */
	public int[] getKeyInfo() {
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int[] info = RegQueryInfoKey(handle);
			this.closeKey0(this.handles, open[0]);
			open = null;
			this.lastError = (info != null ? info[ERROR_CODE] : ERROR_OUTOFMEMORY);
			if (info != null && this.lastError == ERROR_SUCCESS)
				return info;
			else {
				info = null;
				return null;
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return null;
	}

	/**
	 * Retrieves the error code from the last open operation on
	 * <code>this</code>.
	 * 
	 * @return The last error from an open operation.
	 */
	public int getLastError() {
		return this.lastError;
	}

	/**
	 * Retrieves the level in the registry that <code>this</code> resides in.
	 * 
	 * @return The level in the registry for <code>this</code>.
	 */
	public int getLevel() {
		return this.level;
	}

	/**
	 * Retrieves the {@link RegistryKey} that <code>this</code> is linked to or
	 * <code>null</code> if <code>this</code> is not a link.
	 * 
	 * @return The target of <code>this</code> if it is a link.
	 * @since 1.4
	 */
	public RegistryKey getLinkLocation() {
		if (!this.isLinkKey())
			return null;
		long[] open = this.openLinkKey(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		String ntPath = null;
		if (handle != NULL_NATIVE_HANDLE) {
			ntPath = RegGetLinkLocation(handle);
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return (ntPath == null ? null : parseKey(ntPath));
	}

	/**
	 * Used to retrieve the name of <code>this</code> key.
	 * 
	 * @return The name of <code>this</code>.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieves the number of subkeys that are under <code>this</code>.
	 * 
	 * @return The number of subkeys under <code>this</code>.
	 */
	public int getNumberOfSubKeys() {
		int[] info = this.getKeyInfo();
		int num = (info == null ? 0 : info[NUM_SUB_KEYS]);
		info = null;
		return num;
	}

	/**
	 * Retrieves the number of values that are in <code>this</code>.
	 * 
	 * @return The number of values in <code>this</code>.
	 */
	public int getNumberOfValues() {
		int[] info = this.getKeyInfo();
		int num = (info == null ? 0 : info[NUM_VALUES]);
		info = null;
		return num;
	}

	/**
	 * Retrieves the parent <code>RegistryKey</code> of <code>this</code>. If
	 * <code>this</code> refers to a root key, then <code>null</code> is
	 * returned.
	 * 
	 * @return The parent of <code>this</code> or <code>null</code> if
	 *         <code>this</code> is a root.
	 */
	public RegistryKey getParent() {
		if (this.path == null || this.path.equals(""))
			return null;
		if (this.parent == null) {
			String abs = this.absolutePath();
			int index = abs.lastIndexOf('\\');
			RegistryKey rootKey = null;

			if (this.hKey == HKEY_CLASSES_ROOT)
				rootKey = roots[HKEY_CLASSES_ROOT_INDEX];
			else if (this.hKey == HKEY_CURRENT_USER)
				rootKey = roots[HKEY_CURRENT_USER_INDEX];
			else if (this.hKey == HKEY_LOCAL_MACHINE)
				rootKey = roots[HKEY_LOCAL_MACHINE_INDEX];
			else if (this.hKey == HKEY_USERS)
				rootKey = roots[HKEY_USERS_INDEX];
			else if (this.hKey == HKEY_PERFORMANCE_DATA)
				rootKey = roots[HKEY_PERFORMANCE_DATA_INDEX];
			else if (this.hKey == HKEY_CURRENT_CONFIG)
				rootKey = roots[HKEY_CURRENT_CONFIG_INDEX];
			else if (this.hKey == HKEY_DYN_DATA)
				rootKey = roots[HKEY_DYN_DATA_INDEX];
			else
				rootKey = null;

			if (rootKey == null)
				return null;
			if (index >= 0) {
				this.parent = new RegistryKey(rootKey, abs.substring(0, index));
			} else {
				this.parent = rootKey;
			}
		}
		return this.parent;
	}

	/**
	 * Retrieves the path in the registry which points to <code>this</code>.
	 * 
	 * @return The path in the registry to <code>this</code>.
	 */
	public String getPath() {
		if (this.path == null || this.path.equals(""))
			return this.name;
		return this.path + "\\" + this.name;
	}

	/**
	 * Retrieves the index of the root key of <code>this</code> in the
	 * {@link #listRoots} array.
	 * 
	 * @return The index of the parent root key in the {@link #listRoots} array
	 *         of <code>this</code>.
	 */
	public int getRootIndex() {
		if (this.hKey == HKEY_CLASSES_ROOT)
			return HKEY_CLASSES_ROOT_INDEX;
		else if (this.hKey == HKEY_CURRENT_CONFIG)
			return HKEY_CURRENT_CONFIG_INDEX;
		else if (this.hKey == HKEY_CURRENT_USER)
			return HKEY_CURRENT_USER_INDEX;
		else if (this.hKey == HKEY_DYN_DATA)
			return HKEY_DYN_DATA_INDEX;
		else if (this.hKey == HKEY_LOCAL_MACHINE)
			return HKEY_LOCAL_MACHINE_INDEX;
		else if (this.hKey == HKEY_PERFORMANCE_DATA)
			return HKEY_PERFORMANCE_DATA_INDEX;
		else if (this.hKey == HKEY_USERS)
			return HKEY_USERS_INDEX;
		else
			return -1;
	}

	/**
	 * Retrieves the root key for <code>this</code>. A root key is similar to
	 * HKEY_CURRENT_USER or HKEY_LOCAL_MACHINE.
	 * 
	 * @return The parent root key of <code>this</code>.
	 */
	public RegistryKey getRootKey() {
		return roots[this.getRootIndex()];
	}

	/**
	 * Retrieves a <code>RegistryKey</code> which represents the subkey with a
	 * name of <code>name</code> under <code>this</code>. If the key doesn't
	 * exist, then <code>null</code> is returned.
	 * 
	 * @param name
	 *            The name of the subkey to get.
	 * @return A <code>RegistryKey</code> which is a subkey of <code>this</code>
	 *         with name <code>name</code> or <code>null</code> if the key
	 *         doesn't exist.
	 * @see #getSubKeyNames()
	 * @see #getSubKeys()
	 * @see #subKeysIterator()
	 * @see #subKeyNamesIterator()
	 * @since 1.5.4
	 */
	public RegistryKey getSubKey(String name) {
		if (name == null || name.length() == 0 || name.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return null;
		}
		if (this.keyExists(name)) {
			return new RegistryKey(this.hKey, this, this.getPath(), name);
		} else {
			return null;
		}
	}

	/**
	 * Retrieves a sorted array of {@link String}s which are the names of the
	 * subkeys under <code>this</code>. The method returns <code>null</code>
	 * when an error occurs.
	 * 
	 * @return A sorted array of the names of the subkeys of <code>this</code>
	 *         or <code>null</code> if an error occured.
	 * @see #getSubKey(String)
	 * @see #getSubKeys()
	 * @see #getSubKeys(String...)
	 * @see #subKeysIterator()
	 * @see #subKeyNamesIterator()
	 */
	public String[] getSubKeyNames() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;
		long[] open = this.openKey0(KEY_ENUMERATE_SUB_KEYS, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			String[] names = new String[info[NUM_SUB_KEYS]];
			for (int i = 0; i < names.length; i++) {
				String subKey = RegEnumKeyEx(handle, i, info[MAX_SUB_KEY_LENGTH] + 1);
				if (subKey != null)
					names[i] = subKey;
			}
			Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
			this.closeKey0(this.handles, open[0]);
			info = null;
			open = null;
			return names;
		}
		this.closeKey0(this.handles, open[0]);
		info = null;
		open = null;
		return null;
	}

	/**
	 * Retrieves a list of <code>RegistryKey</code>s which represent the subkeys
	 * that are under <code>this</code>. The list is unsorted. The method
	 * returns <code>null</code> when an error occurs.
	 * 
	 * @return An unsorted list of <code>RegistryKey</code>s that are the
	 *         subkeys of <code>this</code> or <code>null</code> if an error
	 *         occured.
	 * @see #getSubKey(String)
	 * @see #getSubKeyNames()
	 * @see #getSubKeys(String...)
	 * @see #subKeysIterator()
	 * @see #subKeyNamesIterator()
	 */
	public List<RegistryKey> getSubKeys() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;
		long[] open = this.openKey0(KEY_ENUMERATE_SUB_KEYS, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int size = info[NUM_SUB_KEYS];
			List<RegistryKey> list = new ArrayList<RegistryKey>(size);
			for (int i = 0; i < size; i++) {
				String subKey = RegEnumKeyEx(handle, i, info[MAX_SUB_KEY_LENGTH] + 1);
				if (subKey != null)
					list.add(new RegistryKey(this.hKey, this, this.getPath(), subKey));
			}
			this.closeKey0(this.handles, open[0]);
			info = null;
			open = null;
			return list;
		}
		this.closeKey0(this.handles, open[0]);
		info = null;
		open = null;
		return null;
	}

	/**
	 * Retrieves a list of <code>RegistryKey</code>s which are specified by the
	 * names in <code>subKeys</code> that are under <code>this</code>. The
	 * method returns <code>null</code> when an error occurs.
	 * 
	 * @param subKeys
	 *            The names of the subkeys to retrieve.
	 * @return A list of <code>RegistryKey</code>s that are the subkeys of
	 *         <code>this</code> named by <code>subKeys</code> or
	 *         <code>null</code> if an error occured.
	 * @see #getSubKey(String)
	 * @see #getSubKeyNames()
	 * @see #getSubKeys()
	 * @see #subKeysIterator()
	 * @see #subKeyNamesIterator()
	 * @since 1.8
	 */
	public List<RegistryKey> getSubKeys(String... subKeys) {
		if (subKeys == null) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return null;
		}
		List<RegistryKey> list = new ArrayList<RegistryKey>(subKeys.length);
		for (int i = 0; i < subKeys.length; i++) {
			RegistryKey sub = this.getSubKey(subKeys[i]);
			if (sub == null) {
				list.clear();
				list = null;
				return null;
			}
			list.add(sub);
		}
		if (list.size() == 0)
			list = null;
		this.lastError = ERROR_SUCCESS;
		return list;
	}

	/**
	 * Retrieves the last write time of <code>this</code> as a
	 * {@link SystemTime} object.
	 * 
	 * @return The last write time of <code>this</code> as a {@link SystemTime}
	 *         object.
	 * @see SystemTime
	 */
	public SystemTime getSystemTime() {
		long[] open = this.openKey0(KEY_READ, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			SystemTime time = (SystemTime) this.getSystemTime(handle);
			this.closeKey0(this.handles, open[0]);
			open = null;
			if (time == null)
				this.lastError = ERROR_OUTOFMEMORY;
			return time;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return null;
	}

	// native method to retrieve a SystemTime object
	private native Object getSystemTime(long hKey);

	/**
	 * Retrieves a {@link RegistryValue} which represents the value with a name
	 * of <code>name</code> under <code>this</code>. <br>
	 * <br>
	 * If the value does not exist, then <code>null</code> is returned.
	 * 
	 * @param name
	 *            The name of the value to obtain.
	 * @return A {@link RegistryValue} which represents the value named by
	 *         <code>name</code> or <code>null</code> if the value doesn't
	 *         exist.
	 * @see #getValueNames()
	 * @see #getValues()
	 * @see #getValues(String...)
	 * @see #valuesIterator()
	 * @see #valueNamesIterator()
	 */
	public RegistryValue getValue(String name) {
		RegistryValue value = null;
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			Object[] info = RegQueryValueEx(handle, name);
			if (info != null) {
				this.lastError = ((Integer) info[ERROR_CODE]).intValue();
				if (this.lastError == ERROR_SUCCESS) {
					int t = ((Integer) info[REG_VALUE_TYPE]).intValue();
					ValueType type = getValueType(t);
					Object data = info[REG_VALUE_DATA];
					if (type == null) {
						value = new RegBinaryValue(this, name, t, (byte[]) data);
					} else {
						switch (type) {
						case REG_SZ:
							value = new RegStringValue(this, name, type, (String) data);
							break;
						case REG_EXPAND_SZ:
							value = new RegStringValue(this, name, type, (isAutoExpandEnvironmentVariables() ? expandEnvironmentVariables((String) data)
									: (String) data));
							break;
						case REG_BINARY:
						case REG_NONE:
						case REG_LINK:
						case REG_RESOURCE_LIST:
						case REG_FULL_RESOURCE_DESCRIPTOR:
						case REG_RESOURCE_REQUIREMENTS_LIST:
							value = new RegBinaryValue(this, name, type, (byte[]) data);
							break;
						case REG_DWORD:
						case REG_DWORD_LITTLE_ENDIAN:
						case REG_DWORD_BIG_ENDIAN:
							value = new RegDWORDValue(this, name, type, (Integer) data);
							break;
						case REG_MULTI_SZ:
							value = new RegMultiStringValue(this, name, type, (String[]) data);
							break;
						case REG_QWORD:
						case REG_QWORD_LITTLE_ENDIAN:
							value = new RegQWORDValue(this, name, type, (Long) data);
							break;
						}
					}
				}
				this.closeKey0(this.handles, open[0]);
				info = null;
			} else {
				this.lastError = ERROR_OUTOFMEMORY;
			}
		} else {
			this.closeKey0(this.handles, open[0]);
		}
		open = null;
		return value;
	}

	/**
	 * Retrieves a sorted array of {@link String}s which are the names of the
	 * values under <code>this</code>. The method returns <code>null</code> when
	 * an error occurs.
	 * 
	 * @return A sorted array of the names of the values of this key or
	 *         <code>null</code> if an error occured.
	 * @see #getValue(String)
	 * @see #getValues()
	 * @see #getValues(String...)
	 * @see #valuesIterator()
	 * @see #valueNamesIterator()
	 */
	public String[] getValueNames() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			String[] names = new String[info[NUM_VALUES]];
			for (int i = 0; i < names.length; i++) {
				String value = RegEnumValue(handle, i, info[MAX_VALUE_NAME_LENGTH] + 1);
				if (value != null)
					names[i] = value;
			}
			Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
			this.closeKey0(this.handles, open[0]);
			info = null;
			open = null;
			return names;
		}
		this.closeKey0(this.handles, open[0]);
		info = null;
		open = null;
		return null;
	}

	/**
	 * Retrieves a list of {@link RegistryValue}s which represent the values
	 * that are under <code>this</code>. The list is unsorted. The method
	 * returns <code>null</code> when an error occurs.
	 * 
	 * @return An unsorted list of {@link RegistryValue}s that are the values of
	 *         this key or <code>null</code> if an error occured.
	 * @see #getValue(String)
	 * @see #getValueNames()
	 * @see #getValues(String...)
	 * @see #valuesIterator()
	 * @see #valueNamesIterator()
	 */
	public List<RegistryValue> getValues() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int size = info[NUM_VALUES];
			List<RegistryValue> list = new ArrayList<RegistryValue>(size);
			for (int i = 0; i < size; i++) {
				String name = RegEnumValue(handle, i, info[MAX_VALUE_NAME_LENGTH] + 1);
				if (name == null)
					continue;
				Object[] data = RegQueryValueEx(handle, name);
				if (data == null) {
					this.lastError = ERROR_OUTOFMEMORY;
					continue;
				}
				this.lastError = ((Integer) data[ERROR_CODE]).intValue();
				if (this.lastError == ERROR_SUCCESS) {
					Integer t = (Integer) data[REG_VALUE_TYPE];
					if (t == null)
						continue;
					ValueType type = getValueType(t.intValue());
					Object value = data[REG_VALUE_DATA];
					if (type == null) {
						list.add(new RegBinaryValue(this, name, t.intValue(), (byte[]) value));
					} else {
						switch (type) {
						case REG_SZ:
							list.add(new RegStringValue(this, name, type, (String) value));
							break;
						case REG_EXPAND_SZ:
							list.add(new RegStringValue(this, name, type, (isAutoExpandEnvironmentVariables() ? expandEnvironmentVariables((String) value)
									: (String) value)));
							break;
						case REG_BINARY:
						case REG_NONE:
						case REG_LINK:
						case REG_RESOURCE_LIST:
						case REG_FULL_RESOURCE_DESCRIPTOR:
						case REG_RESOURCE_REQUIREMENTS_LIST:
							list.add(new RegBinaryValue(this, name, type, (byte[]) value));
							break;
						case REG_DWORD:
						case REG_DWORD_LITTLE_ENDIAN:
						case REG_DWORD_BIG_ENDIAN:
							list.add(new RegDWORDValue(this, name, type, (Integer) value));
							break;
						case REG_MULTI_SZ:
							list.add(new RegMultiStringValue(this, name, type, (String[]) value));
							break;
						case REG_QWORD:
						case REG_QWORD_LITTLE_ENDIAN:
							list.add(new RegQWORDValue(this, name, type, (Long) value));
							break;
						}
					}
				}
				data = null;
			}
			this.closeKey0(this.handles, open[0]);
			info = null;
			open = null;
			return list;
		}
		this.closeKey0(this.handles, open[0]);
		info = null;
		open = null;
		return null;
	}

	/**
	 * Retrieves a list of {@link RegistryValue}s which represent the values
	 * that are named by <code>va</code>. The method returns <code>null</code>
	 * if an error occurs.
	 * 
	 * @param va
	 *            The names of the values to retrieve.
	 * @return A list of {@link RegistryValue}s that are named by
	 *         <code>va</code> under <code>this</code> or <code>null</code> if
	 *         an error occurs.
	 * @see #getValue(String)
	 * @see #getValueNames()
	 * @see #getValues()
	 * @see #valuesIterator()
	 * @see #valueNamesIterator()
	 * @since 1.8
	 */
	public List<RegistryValue> getValues(String... va) {
		if (va == null) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return null;
		}
		List<RegistryValue> list = null;
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			Object[] info = RegQueryMultipleValues(handle, va);
			if (info != null) {
				list = new ArrayList<RegistryValue>(va.length);
				this.lastError = ((Integer) info[ERROR_CODE]).intValue();
				if (this.lastError == ERROR_SUCCESS) {
					for (int i = 0, j = 1; i < va.length; i++) {
						String name = va[i];
						Integer t = (Integer) info[j++];
						if (t == null)
							continue;
						ValueType type = getValueType(t.intValue());
						Object value = info[j++];
						if (type == null) {
							list.add(new RegBinaryValue(this, name, t.intValue(), (byte[]) value));
						} else {
							switch (type) {
							case REG_SZ:
								list.add(new RegStringValue(this, name, type, (String) value));
								break;
							case REG_EXPAND_SZ:
								list.add(new RegStringValue(this, name, type, (isAutoExpandEnvironmentVariables() ? expandEnvironmentVariables((String) value)
										: (String) value)));
								break;
							case REG_BINARY:
							case REG_NONE:
							case REG_LINK:
							case REG_RESOURCE_LIST:
							case REG_FULL_RESOURCE_DESCRIPTOR:
							case REG_RESOURCE_REQUIREMENTS_LIST:
								list.add(new RegBinaryValue(this, name, type, (byte[]) value));
								break;
							case REG_DWORD:
							case REG_DWORD_LITTLE_ENDIAN:
							case REG_DWORD_BIG_ENDIAN:
								list.add(new RegDWORDValue(this, name, type, (Integer) value));
								break;
							case REG_MULTI_SZ:
								list.add(new RegMultiStringValue(this, name, type, (String[]) value));
								break;
							case REG_QWORD:
							case REG_QWORD_LITTLE_ENDIAN:
								list.add(new RegQWORDValue(this, name, type, (Long) value));
								break;
							}
						}
					}
				} else if (!(this.lastError == ERROR_CANTREAD || this.lastError == ERROR_FILE_NOT_FOUND)) {
					for (int i = 0; i < va.length; i++) {
						RegistryValue val = this.getValue(va[i]);
						if (val == null) {
							list.clear();
							break;
						}
						list.add(val);
					}
					list = null;
				} else {
					list = null;
				}
			} else {
				this.lastError = ERROR_OUTOFMEMORY;
			}
			this.closeKey0(this.handles, open[0]);
			info = null;
			open = null;
			return list;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return null;
	}

	/**
	 * Convenience method to retrieve the <code>ValueType</code> object based on
	 * the integer type that is passed in. If <code>type</code> is not a legal
	 * registry value type, then <code>null</code> is returned.
	 * 
	 * @param type
	 *            An integer representing a value type.
	 * @return A <code>ValueType</code> object based on the integer type or
	 *         <code>null</code> if <code>type</code> is not a legal registry
	 *         value type.
	 */
	public static ValueType getValueType(int type) {
		switch (type) {
		case REG_NONE:
			return ValueType.REG_NONE;
		case REG_SZ:
			return ValueType.REG_SZ;
		case REG_EXPAND_SZ:
			return ValueType.REG_EXPAND_SZ;
		case REG_BINARY:
			return ValueType.REG_BINARY;
		case REG_DWORD:
			return ValueType.REG_DWORD;
		case REG_DWORD_BIG_ENDIAN:
			return ValueType.REG_DWORD_BIG_ENDIAN;
		case REG_LINK:
			return ValueType.REG_LINK;
		case REG_MULTI_SZ:
			return ValueType.REG_MULTI_SZ;
		case REG_RESOURCE_LIST:
			return ValueType.REG_RESOURCE_LIST;
		case REG_FULL_RESOURCE_DESCRIPTOR:
			return ValueType.REG_FULL_RESOURCE_DESCRIPTOR;
		case REG_RESOURCE_REQUIREMENTS_LIST:
			return ValueType.REG_RESOURCE_REQUIREMENTS_LIST;
		case REG_QWORD:
			return ValueType.REG_QWORD;
		default:
			return null;
		}
	}

	/**
	 * Convenience method to test to see if <code>this</code> contains the
	 * default or unnamed value.
	 * 
	 * @return <code>true</code> if the default value exists, <code>false</code>
	 *         otherwise.
	 */
	public boolean hasDefaultValue() {
		return this.valueExists(DEFAULT_VALUE_NAME);
	}

	/**
	 * Calculates the hash code value for <code>this</code>.
	 * 
	 * @return The hash code for <code>this</code>.
	 */
	public int hashCode() {
		return (this.name.hashCode() ^ this.path.hashCode());
	}

	/**
	 * Convenience method to test to see if <code>this</code> contains any
	 * subkeys.
	 * 
	 * @return <code>true</code> if subkeys exist, <code>false</code> otherwise.
	 */
	public boolean hasSubKeys() {
		int[] info = this.getKeyInfo();
		boolean has = (info != null && info[NUM_SUB_KEYS] != 0);
		info = null;
		return has;
	}

	/**
	 * Convenience method to test to see if <code>this</code> contains any
	 * values.
	 * 
	 * @return <code>true</code> if values exist, <code>false</code> otherwise.
	 */
	public boolean hasValues() {
		int[] info = this.getKeyInfo();
		boolean has = (info != null && info[NUM_VALUES] != 0);
		info = null;
		return has;
	}

	/**
	 * Checks to see whether <code>this</code> key is a symbolic link to another
	 * key.
	 * 
	 * @return <code>true</code> if the key is a link, or <code>false</code>
	 *         otherwise.
	 * @since 1.4
	 */
	public boolean isLinkKey() {
		boolean result = false;
		long[] open = this.openLinkKey(KEY_QUERY_VALUE | KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int[] error = RegIsLinkKey(handle);
			if (error != null) {
				this.lastError = error[ERROR_CODE];
				result = (error[1] == 1);
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Checks to see whether <code>this</code> key is a remote registry key.
	 * 
	 * @return <code>true</code> if the key is a remote registry key, or
	 *         <code>false</code> otherwise.
	 * @since 1.8
	 */
	public boolean isRemoteRegistryKey() {
		return this.machine != null;
	}

	/**
	 * Tests to see if a handle to registry key is a root handle.
	 */
	private boolean isRootHandle(long handle) {
		if (handle == HKEY_CLASSES_ROOT || handle == HKEY_CURRENT_USER || handle == HKEY_LOCAL_MACHINE || handle == HKEY_USERS || handle == HKEY_CURRENT_CONFIG
				|| handle == HKEY_DYN_DATA || handle == HKEY_PERFORMANCE_DATA) {
			return true;
		}
		return false;
	}

	/**
	 * Determines if the key is viewing the 64-bit registry or the 32-bit
	 * registry.
	 * 
	 * @return <code>true</code> if the 64-bit registry is being viewed,
	 *         <code>false</code> otherwise.
	 * @since 1.6
	 */
	public boolean isViewing64BitRegistry() {
		return (this.view64 == KEY_WOW64_64KEY);
	}

	/**
	 * Tests to see if the specified subkey exists under <code>this</code>.
	 * 
	 * @param subKey
	 *            The subkey whose existence will be tested.
	 * @return <code>true</code> if <code>subKey</code> exists,
	 *         <code>false</code> otherwise.
	 */
	public boolean keyExists(String subKey) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return false;
		}
		long[] open = this.openSubKey0(subKey);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE || this.lastError == ERROR_ACCESS_DENIED) {
			this.closeKey0(this.handles, open[0]);
			open = null;
			return true;
		}
		open = null;
		return false;
	}

	/**
	 * Tests to see if the specified subkey under <code>this</code> is a link.
	 * 
	 * @param subKey
	 *            The subkey whose existence will be tested.
	 * @return <code>true</code> if <code>subKey</code> exists,
	 *         <code>false</code> otherwise.
	 * @since 1.5.1
	 */
	public boolean keyIsLink(String subKey) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return false;
		}
		boolean result = false;
		long[] open = this.openSubLinkKey0(subKey);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int[] error = RegIsLinkKey(handle);
			if (error != null) {
				result = (error[1] == 1);
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Returns the time that the key denoted by <code>this</code> was last
	 * modified.
	 * 
	 * @return A <code>long</code> value representing the time the key was last
	 *         modified, measured in milliseconds since the epoch (00:00:00 GMT,
	 *         January 1, 1970), or 0L if the key does not exist.
	 * @since 1.5.4
	 */
	public long lastModified() {
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			long time = this.lastModified(handle);
			time -= 11644473600000L;
			this.closeKey0(this.handles, open[0]);
			open = null;
			return time;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return 0L;
	}

	// native method to retrieve the last modified time
	private native long lastModified(long hKey);

	/**
	 * Creates a subkey under HKEY_USERS or HKEY_LOCAL_MACHINE and loads the
	 * data from the specified registry hive into that subkey.
	 * 
	 * @param subKey
	 *            The subkey to create.
	 * @param regFile
	 *            The registry hive that contains the information to load.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canLoadKey()
	 */
	public int loadKey(String subKey, File regFile) {
		if (!this.canLoadKey())
			return ERROR_ACCESS_DENIED;

		long handle = this.openKey(KEY_WRITE);
		if (handle != NULL_NATIVE_HANDLE) {
			if (!SetPrivilege(true, SE_BACKUP_NAME_STRING)) {
				this.closeKey(handle);
				return ERROR_ACCESS_DENIED;
			}
			if (!SetPrivilege(true, SE_RESTORE_NAME_STRING)) {
				this.closeKey(handle);
				return ERROR_ACCESS_DENIED;
			}

			int error = RegLoadKey(handle, RegistryKey.toWindowsName(subKey), regFile.getAbsolutePath());
			this.closeKey(handle);
			this.lastError = error;
			SetPrivilege(false, SE_BACKUP_NAME_STRING);
			SetPrivilege(false, SE_RESTORE_NAME_STRING);
			return error;
		}
		return this.lastError;
	}

	/**
	 * Creates a new value under <code>this</code> with the specified name and
	 * type. The newly created value is then returned. If the value name already
	 * exists in the registry, then <code>null</code> is returned.
	 * 
	 * @param name
	 *            The name of the value to create.
	 * @param type
	 *            The type of the value.
	 * @return The newly create value or <code>null</code> if it already exists
	 *         or an error occurred.
	 * @see #canWrite()
	 */
	public RegistryValue newValue(String name, ValueType type) {
		if (name == null) {
			name = DEFAULT_VALUE_NAME;
		}
		if (this.valueExists(name)) {
			return null;
		}
		RegistryValue value = null;
		switch (type) {
		case REG_SZ:
		case REG_EXPAND_SZ:
			value = new RegStringValue(this, name, type, null);
			this.lastError = ((RegStringValue) value).setValue("");
			break;
		case REG_BINARY:
		case REG_NONE:
		case REG_LINK:
		case REG_RESOURCE_LIST:
		case REG_FULL_RESOURCE_DESCRIPTOR:
		case REG_RESOURCE_REQUIREMENTS_LIST:
			value = new RegBinaryValue(this, name, type, null);
			this.lastError = ((RegBinaryValue) value).setValue(new byte[0]);
			break;
		case REG_DWORD:
		case REG_DWORD_LITTLE_ENDIAN:
		case REG_DWORD_BIG_ENDIAN:
			value = new RegDWORDValue(this, name, type, null);
			this.lastError = ((RegDWORDValue) value).setIntValue(0);
			break;
		case REG_MULTI_SZ:
			value = new RegMultiStringValue(this, name, type, null);
			this.lastError = ((RegMultiStringValue) value).setValue(new String[0]);
			break;
		case REG_QWORD:
		case REG_QWORD_LITTLE_ENDIAN:
			value = new RegQWORDValue(this, name, type, null);
			this.lastError = ((RegQWORDValue) value).setLongValue(0L);
			break;
		}
		return (this.lastError == ERROR_SUCCESS ? value : null);
	}

	/**
	 * Creates a new value under <code>this</code> with the specified name and
	 * type. The newly created value is then returned. If the value name already
	 * exists in the registry, then <code>null</code> is returned.
	 * 
	 * @param name
	 *            The name of the value to create.
	 * @param type
	 *            The type of the value.
	 * @return The newly create value or <code>null</code> if it already exists
	 *         or an error occurred.
	 * @see #canWrite()
	 */
	public RegistryValue newValue(String name, int type) {
		ValueType vt = getValueType(type);
		if (vt != null) {
			return this.newValue(name, vt);
		}

		if (name == null) {
			name = DEFAULT_VALUE_NAME;
		}

		RegistryValue value = new RegBinaryValue(this, name, type, null);
		this.lastError = ((RegBinaryValue) value).setValue(new byte[0]);

		return (this.lastError == ERROR_SUCCESS ? value : null);
	}

	/**
	 * Called by WatchData so that when a change occurs, the thread will be
	 * notified.
	 */
	static int notifyChange(long hKey, boolean watchSubtree, int dwNotifyFilter, long eventHandle) {
		return RegNotifyChangeKeyValue(hKey, watchSubtree, dwNotifyFilter, eventHandle, true);
	}

	/**
	 * Opens this key with the specified security access right.
	 */
	private long openKey(int sam) {
		int view = this.view64;
		if (this.absolutePath().length() == 0)
			view = 0;
		long m_hKey = NULL_NATIVE_HANDLE;
		if (this.machine == null) {
			m_hKey = this.hKey;
		} else {
			long[] conn = RegConnectRegistry(this.machine, this.hKey);
			if (conn != null && conn[ERROR_CODE] != ERROR_SUCCESS)
				m_hKey = NULL_NATIVE_HANDLE;
			else if (conn != null)
				m_hKey = conn[NATIVE_HANDLE];
			this.lastError = (conn == null ? ERROR_OUTOFMEMORY : (int) conn[ERROR_CODE]);
			if (this.lastError != ERROR_SUCCESS)
				return 0L;
		}
		long[] open = RegOpenKeyEx(m_hKey, this.absolutePath(), sam | view);
		if (open != null && open[ERROR_CODE] != ERROR_SUCCESS)
			open[NATIVE_HANDLE] = NULL_NATIVE_HANDLE;
		this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
		return (open == null ? NULL_NATIVE_HANDLE : open[NATIVE_HANDLE]);
	}

	/**
	 * Opens this key with the specified security access right and puts all of
	 * the handles into a list. Needs to be accessible to all subclasses of
	 * RegistryValue.
	 */
	long[] openKey0(int sam) {
		return this.openKey0(sam, this.handles);
	}

	/**
	 * Opens this key with the specified security access right and puts all of
	 * the handles into the specified list. Needs to be accessible to WatchData.
	 */
	long[] openKey0(int sam, List<Long> list) {
		synchronized (list) {
			long m_hKey = NULL_NATIVE_HANDLE;
			if (this.machine == null) {
				m_hKey = this.hKey;
			} else {
				long[] conn = RegConnectRegistry(this.machine, this.hKey);
				if (conn != null && conn[ERROR_CODE] != ERROR_SUCCESS)
					m_hKey = NULL_NATIVE_HANDLE;
				else if (conn != null)
					m_hKey = conn[NATIVE_HANDLE];
				this.lastError = (conn == null ? ERROR_OUTOFMEMORY : (int) conn[ERROR_CODE]);
				if (this.lastError != ERROR_SUCCESS)
					return new long[] { 0, NULL_NATIVE_HANDLE };
			}
			StringTokenizer t = new StringTokenizer(this.absolutePath(), "\\"); // break
																				// up
																				// the
																				// path
			StringBuffer buf = new StringBuffer();
			int count = 0;
			long stopIndex = (list.size() == 0 ? 0 : list.size() - 1);
			long lastHandle = m_hKey;
			sam |= this.view64;
			while (t.hasMoreTokens()) {
				if (count != 32) { // keep adding to the current path until 32
									// levels is reached
					buf.append(t.nextToken() + "\\");
					count++;
				} else {
					long[] open = RegOpenKeyEx(lastHandle, buf.toString(), sam); // Maximum
																					// number
																					// of
																					// levels
																					// opened
																					// at
																					// once
																					// is
																					// 32
					this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
					if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
						list.add(open[NATIVE_HANDLE]);
						lastHandle = open[NATIVE_HANDLE];
						buf.setLength(0);
						count = 0;
					} else {
						if (open != null)
							this.closeKey(open[NATIVE_HANDLE]);
						buf.setLength(0);
						this.closeKey0(this.handles, stopIndex);
						return new long[] { stopIndex, NULL_NATIVE_HANDLE };
					}
				}
			}
			if (buf.length() != 0) { // Add the remained parts of the path
				if (this.absolutePath().length() == 0)
					sam &= ~this.view64;
				long[] open = RegOpenKeyEx(lastHandle, buf.toString(), sam);
				this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
				if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
					list.add(open[NATIVE_HANDLE]);
					lastHandle = open[NATIVE_HANDLE];
				} else {
					if (open != null)
						this.closeKey(open[NATIVE_HANDLE]);
					this.closeKey0(this.handles, stopIndex);
					lastHandle = NULL_NATIVE_HANDLE;
				}
			}
			return new long[] { stopIndex, lastHandle };
		}
	}

	/**
	 * Opens this key and not the key that this is linked to with the specified
	 * security access right and puts all of the handles into the specified
	 * list.
	 */
	private long[] openLinkKey(int sam, List<Long> list) {
		synchronized (list) {
			long m_hKey = NULL_NATIVE_HANDLE;
			if (this.machine == null) {
				m_hKey = this.hKey;
			} else {
				long[] conn = RegConnectRegistry(this.machine, this.hKey);
				if (conn != null && conn[ERROR_CODE] != ERROR_SUCCESS)
					m_hKey = NULL_NATIVE_HANDLE;
				else if (conn != null)
					m_hKey = conn[NATIVE_HANDLE];
				this.lastError = (conn == null ? ERROR_OUTOFMEMORY : (int) conn[ERROR_CODE]);
				if (this.lastError != ERROR_SUCCESS)
					return new long[] { 0, NULL_NATIVE_HANDLE };
			}
			StringTokenizer t = new StringTokenizer(this.absolutePath(), "\\"); // break
																				// up
																				// the
																				// path
			StringBuffer buf = new StringBuffer();
			int count = 0;
			long stopIndex = (list.size() == 0 ? 0 : list.size() - 1);
			long lastHandle = m_hKey;
			sam |= this.view64;
			while (t.hasMoreTokens()) {
				if (count != 32) { // keep adding to the current path until 32
									// levels is reached
					buf.append(t.nextToken() + "\\");
					count++;
				} else {
					long[] open = null;
					if (t.hasMoreTokens())
						open = RegOpenKeyEx(lastHandle, buf.toString(), sam); // Maximum
																				// number
																				// of
																				// levels
																				// opened
																				// at
																				// once
																				// is
																				// 32
					else {
						int option = REG_OPTION_VOLATILE | REG_OPTION_OPEN_LINK;
						open = RegCreateKeyEx(lastHandle, buf.toString(), option, sam, null);
					}
					this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
					if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
						if (open[DISPOSITION] == REG_CREATED_NEW_KEY) {
							RegDeleteKey(lastHandle, buf.toString());
							this.lastError = ERROR_FILE_NOT_FOUND;
							this.closeKey0(this.handles, stopIndex);
							return new long[] { stopIndex, NULL_NATIVE_HANDLE };
						}
						list.add(open[NATIVE_HANDLE]);
						lastHandle = open[NATIVE_HANDLE];
						buf.setLength(0);
						count = 0;
					} else {
						if (open != null)
							this.closeKey(open[NATIVE_HANDLE]);
						buf.setLength(0);
						this.closeKey0(this.handles, stopIndex);
						return new long[] { stopIndex, NULL_NATIVE_HANDLE };
					}
				}
			}
			if (buf.length() != 0) { // Add the remained parts of the path
				if (this.absolutePath().length() == 0)
					sam &= ~this.view64;
				int option = REG_OPTION_VOLATILE | REG_OPTION_OPEN_LINK;
				long[] open = RegCreateKeyEx(lastHandle, buf.toString(), option, sam, null);
				this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
				if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
					if (open[DISPOSITION] == REG_CREATED_NEW_KEY) {
						RegDeleteKey(lastHandle, buf.toString());
						this.lastError = ERROR_FILE_NOT_FOUND;
						this.closeKey0(this.handles, stopIndex);
						return new long[] { stopIndex, NULL_NATIVE_HANDLE };
					}
					list.add(open[NATIVE_HANDLE]);
					lastHandle = open[NATIVE_HANDLE];
				} else {
					if (open != null)
						this.closeKey(open[NATIVE_HANDLE]);
					this.closeKey0(this.handles, stopIndex);
					lastHandle = NULL_NATIVE_HANDLE;
				}
			}
			return new long[] { stopIndex, lastHandle };
		}
	}

	/**
	 * Opens the specified subkey that is under this key.
	 */
	private long openSubKey(String subKey) {
		subKey = toWindowsName(subKey);
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return NULL_NATIVE_HANDLE;
		}
		String path = this.absolutePath() + "\\" + subKey;
		if (path.charAt(0) == '\\')
			path = path.substring(1);
		if (path.charAt(path.length() - 1) == '\\')
			path = path.substring(0, path.length() - 1);

		long m_hKey = NULL_NATIVE_HANDLE;
		if (this.machine == null) {
			m_hKey = this.hKey;
		} else {
			long[] conn = RegConnectRegistry(this.machine, this.hKey);
			if (conn != null && conn[ERROR_CODE] != ERROR_SUCCESS)
				m_hKey = NULL_NATIVE_HANDLE;
			else if (conn != null)
				m_hKey = conn[NATIVE_HANDLE];
			this.lastError = (conn == null ? ERROR_OUTOFMEMORY : (int) conn[ERROR_CODE]);
			if (this.lastError != ERROR_SUCCESS)
				return 0L;
		}
		long[] open = RegOpenKeyEx(m_hKey, path, KEY_READ | this.view64);
		if (open != null && open[ERROR_CODE] != ERROR_SUCCESS)
			open[NATIVE_HANDLE] = NULL_NATIVE_HANDLE;
		this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
		return (open != null ? open[NATIVE_HANDLE] : 0L);
	}

	/**
	 * Opens the specified subkey under this key and stores all of the handles
	 * in a list.
	 */
	private long[] openSubKey0(String subKey) {
		synchronized (this.handles) {
			subKey = toWindowsName(subKey);
			if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
				this.lastError = ERROR_INVALID_PARAMETER;
				return new long[] { 0, NULL_NATIVE_HANDLE };
			}
			String path = this.absolutePath() + "\\" + subKey;
			if (path.charAt(0) == '\\')
				path = path.substring(1);
			if (path.charAt(path.length() - 1) == '\\')
				path = path.substring(0, path.length() - 1);
			long m_hKey = NULL_NATIVE_HANDLE;
			if (this.machine == null) {
				m_hKey = this.hKey;
			} else {
				long[] conn = RegConnectRegistry(this.machine, this.hKey);
				if (conn != null && conn[ERROR_CODE] != ERROR_SUCCESS)
					m_hKey = NULL_NATIVE_HANDLE;
				else if (conn != null)
					m_hKey = conn[NATIVE_HANDLE];
				this.lastError = (conn == null ? ERROR_OUTOFMEMORY : (int) conn[ERROR_CODE]);
				if (this.lastError != ERROR_SUCCESS)
					return new long[] { 0, NULL_NATIVE_HANDLE };
			}
			StringTokenizer t = new StringTokenizer(path, "\\"); // break up the
																	// path
			StringBuffer buf = new StringBuffer();
			int count = 0;
			long stopIndex = (this.handles.size() == 0 ? 0 : this.handles.size() - 1);
			long lastHandle = m_hKey;
			while (t.hasMoreTokens()) {
				if (count != 32) { // keep adding to the current path until 32
									// levels is reached
					buf.append(t.nextToken() + "\\");
					count++;
				} else {
					long[] open = RegOpenKeyEx(lastHandle, buf.toString(), KEY_READ | this.view64); // Maximum
																									// number
																									// of
																									// levels
																									// opened
																									// at
																									// once
																									// is
																									// 32
					if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
						this.handles.add(open[NATIVE_HANDLE]);
						lastHandle = open[NATIVE_HANDLE];
						buf.setLength(0);
						count = 0;
					} else {
						if (open != null)
							this.closeKey(open[NATIVE_HANDLE]);
						buf.setLength(0);
						this.closeKey0(this.handles, stopIndex);
						return new long[] { stopIndex, NULL_NATIVE_HANDLE };
					}
				}
			}
			if (buf.length() != 0) { // Add the remained parts of the path
				long[] open = RegOpenKeyEx(lastHandle, buf.toString(), KEY_READ | this.view64);
				if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
					this.handles.add(open[NATIVE_HANDLE]);
					lastHandle = open[NATIVE_HANDLE];
				} else {
					if (open != null)
						this.closeKey(open[NATIVE_HANDLE]);
					this.closeKey0(this.handles, stopIndex);
					lastHandle = NULL_NATIVE_HANDLE;
				}
			}
			return new long[] { stopIndex, lastHandle };
		}
	}

	/**
	 * Opens the specified subkey under this key that is a link and stores all
	 * of the handles in a list.
	 */
	private long[] openSubLinkKey0(String subKey) {
		synchronized (this.handles) {
			subKey = toWindowsName(subKey);
			if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
				this.lastError = ERROR_INVALID_PARAMETER;
				return new long[] { 0, NULL_NATIVE_HANDLE };
			}
			String path = this.absolutePath() + "\\" + subKey;
			if (path.charAt(0) == '\\')
				path = path.substring(1);
			if (path.charAt(path.length() - 1) == '\\')
				path = path.substring(0, path.length() - 1);
			long m_hKey = NULL_NATIVE_HANDLE;
			if (this.machine == null) {
				m_hKey = this.hKey;
			} else {
				long[] conn = RegConnectRegistry(this.machine, this.hKey);
				if (conn != null && conn[ERROR_CODE] != ERROR_SUCCESS)
					m_hKey = NULL_NATIVE_HANDLE;
				else if (conn != null)
					m_hKey = conn[NATIVE_HANDLE];
				this.lastError = (conn == null ? ERROR_OUTOFMEMORY : (int) conn[ERROR_CODE]);
				if (this.lastError != ERROR_SUCCESS)
					return new long[] { 0, NULL_NATIVE_HANDLE };
			}
			StringTokenizer t = new StringTokenizer(path, "\\"); // break up the
																	// path
			StringBuffer buf = new StringBuffer();
			int count = 0;
			long stopIndex = (this.handles.size() == 0 ? 0 : this.handles.size() - 1);
			long lastHandle = m_hKey;
			int sam = KEY_QUERY_VALUE | KEY_SET_VALUE | this.view64;
			while (t.hasMoreTokens()) {
				if (count != 32) { // keep adding to the current path until 32
									// levels is reached
					buf.append(t.nextToken() + "\\");
					count++;
				} else {
					long[] open = null;
					if (t.hasMoreTokens())
						open = RegOpenKeyEx(lastHandle, buf.toString(), sam); // Maximum
																				// number
																				// of
																				// levels
																				// opened
																				// at
																				// once
																				// is
																				// 32
					else {
						int option = REG_OPTION_VOLATILE | REG_OPTION_OPEN_LINK;
						open = RegCreateKeyEx(lastHandle, buf.toString(), option, sam, null);
					}
					this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
					if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
						if (open[DISPOSITION] == REG_CREATED_NEW_KEY) {
							this.lastError = RegDeleteKey(lastHandle, buf.toString());
							this.closeKey0(this.handles, stopIndex);
							return new long[] { stopIndex, NULL_NATIVE_HANDLE };
						}
						this.handles.add(open[NATIVE_HANDLE]);
						lastHandle = open[NATIVE_HANDLE];
						buf.setLength(0);
						count = 0;
					} else {
						if (open != null)
							this.closeKey(open[NATIVE_HANDLE]);
						buf.setLength(0);
						this.closeKey0(this.handles, stopIndex);
						return new long[] { stopIndex, NULL_NATIVE_HANDLE };
					}
				}
			}
			if (buf.length() != 0) { // Add the remained parts of the path
				int option = REG_OPTION_VOLATILE | REG_OPTION_OPEN_LINK;
				long[] open = RegCreateKeyEx(lastHandle, buf.toString(), option, sam, null);
				this.lastError = (open == null ? ERROR_OUTOFMEMORY : (int) open[ERROR_CODE]);
				if (open != null && open[ERROR_CODE] == ERROR_SUCCESS) {
					if (open[DISPOSITION] == REG_CREATED_NEW_KEY) {
						this.lastError = RegDeleteKey(lastHandle, buf.toString());
						this.closeKey0(this.handles, stopIndex);
						return new long[] { stopIndex, NULL_NATIVE_HANDLE };
					}
					this.handles.add(open[NATIVE_HANDLE]);
					lastHandle = open[NATIVE_HANDLE];
				} else {
					if (open != null)
						this.closeKey(open[NATIVE_HANDLE]);
					this.closeKey0(this.handles, stopIndex);
					lastHandle = NULL_NATIVE_HANDLE;
				}
			}
			return new long[] { stopIndex, lastHandle };
		}
	}

	/**
	 * Determines whether registry reflection for <code>this</code> key is
	 * turned off. <br>
	 * <em>Note:</em> This method only works on 64-bit versions of Windows XP
	 * and higher.
	 * 
	 * @return <code>true</code> if reflection is disabled, <code>false</code>
	 *         otherwise
	 * @since 1.6
	 */
	public boolean queryReflection() {
		return RegQueryReflectionKey(this.hKey);
	}

	/**
	 * Reads in the data for <code>this</code> from the stream. The information
	 * for the parent and handles are reset so that accurate information is
	 * maintained.
	 */
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		this.hKey = in.readLong();
		this.parent = null;
		this.name = (String) in.readObject();
		this.path = (String) in.readObject();
		this.absolutePath = (String) in.readObject();
		this.handles = new LinkedList<Long>();
		this.created = in.readBoolean();
		this.level = in.readInt();
		this.lastError = in.readInt();
		this.view64 = in.readInt();
		this.machine = (String) in.readObject();
		disposer = new KeyDisposer();
		Disposer.addRecord(this, disposer);
	}

	/**
	 * Renames <code>this</code> to <code>newKeyName</code>.
	 * 
	 * @return <code>true</code> if the rename completed successfully,
	 *         <code>false</code> otherwise.
	 * @see #canRename()
	 */
	public boolean renameKey(String newKeyName) {
		long[] open = this.openLinkKey(KEY_WRITE, this.handles);
		long handle = open[NATIVE_HANDLE];
		boolean result = false;
		if (handle != NULL_NATIVE_HANDLE) {
			this.lastError = RegRenameKey(handle, newKeyName);
			result = this.lastError == ERROR_SUCCESS;
			if (result) {
				int index = this.absolutePath.lastIndexOf(this.name);
				if (index != -1) {
					String temp = this.absolutePath.substring(0, index);
					this.absolutePath = temp + newKeyName;
					this.name = newKeyName;
				}
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Renames the value specified by the name <code>old</code> to
	 * <code>newValueName</code>.
	 * 
	 * @param old
	 *            The name of the current value to rename.
	 * @param newValueName
	 *            The new name of the value.
	 * @return <code>true</code> if the rename completed successfully,
	 *         <code>false</code> otherwise.
	 * @see #canWrite()
	 * @see #renameValue(RegistryValue, String)
	 * @since 1.8
	 */
	public boolean renameValue(String old, String newValueName) {
		RegistryValue v = this.getValue(old);
		if (v == null)
			return false;
		if (!this.valueExists(newValueName)) {
			String name = v.getName();
			this.lastError = v.setName(newValueName);
			if (this.lastError == ERROR_SUCCESS) {
				this.deleteValue(name);
				return true;
			}
		}
		return false;
	}

	/**
	 * Renames the value specified by <code>old</code> to
	 * <code>newValueName</code>.
	 * 
	 * @param old
	 *            The current value to rename.
	 * @param newValueName
	 *            The new name of the value.
	 * @return <code>true</code> if the rename completed successfully,
	 *         <code>false</code> otherwise.
	 * @see #canWrite()
	 * @see #renameValue(String, String)
	 */
	public boolean renameValue(RegistryValue old, String newValueName) {
		if (old == null)
			return false;
		String name = old.getName();
		if (this.equals(old.getKey()) && !this.valueExists(newValueName)) {
			this.lastError = old.setName(newValueName);
			if (this.lastError == ERROR_SUCCESS) {
				this.deleteValue(name);
				return true;
			}
		}
		return false;
	}

	/**
	 * Replaces the file backing <code>this</code> key and all the subkeys with
	 * another file, so that when the system is next started, the key and
	 * subkeys will have the values stored in the new file.
	 * 
	 * @param newRegFile
	 *            The file containing the new information for the key.
	 * @param oldRegFile
	 *            The file that will be receive a backup of the information
	 *            being replaced.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 */
	public int replaceKey(File newRegFile, File oldRegFile) {
		long[] open = this.openKey0(KEY_WRITE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			if (!SetPrivilege(true, SE_BACKUP_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}
			if (!SetPrivilege(true, SE_RESTORE_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}

			int error = RegReplaceKey(handle, "", newRegFile.getAbsolutePath(), oldRegFile.getAbsolutePath());
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			SetPrivilege(false, SE_BACKUP_NAME_STRING);
			SetPrivilege(false, SE_RESTORE_NAME_STRING);
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Replaces the file backing the subkey of <code>this</code> key and all its
	 * subkeys with another file, so that when the system is next started, the
	 * key and subkeys will have the values stored in the new file.
	 * 
	 * @param subKey
	 *            The subkey under <code>this</code> key.
	 * @param newRegFile
	 *            The file containing the new information for the key.
	 * @param oldRegFile
	 *            The file that will be receive a backup of the information
	 *            being replaced.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 */
	public int replaceSubKey(String subKey, File newRegFile, File oldRegFile) {
		if (subKey == null || subKey.length() == 0 || subKey.equals("")) {
			this.lastError = ERROR_INVALID_PARAMETER;
			return this.lastError;
		}
		long[] open = this.openKey0(KEY_WRITE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			if (!SetPrivilege(true, SE_BACKUP_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}
			if (!SetPrivilege(true, SE_RESTORE_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}

			int error = RegReplaceKey(handle, RegistryKey.toWindowsName(subKey), newRegFile.getAbsolutePath(), oldRegFile.getAbsolutePath());
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			SetPrivilege(false, SE_BACKUP_NAME_STRING);
			SetPrivilege(false, SE_RESTORE_NAME_STRING);
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Reads the registry information in a specified file and copies it over
	 * <code>this</code> key. This registry information may be in the form of a
	 * key and multiple levels of subkeys.
	 * 
	 * @param regFile
	 *            The file containing the registry information to restore.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 */
	public int restoreKey(File regFile) {
		return restoreKey(regFile, false);
	}

	/**
	 * Reads the registry information in a specified file and copies it over
	 * <code>this</code> key. This registry information may be in the form of a
	 * key and multiple levels of subkeys.
	 * 
	 * @param regFile
	 *            The file containing the registry information to restore.
	 * @param force
	 *            Force the restore operation to execute even if
	 *            <code>this</code> key or subkeys of <code>this</code> have
	 *            open handles.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @since 1.7.5
	 */
	public int restoreKey(File regFile, boolean force) {
		long[] open = this.openKey0(KEY_WRITE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			if (!SetPrivilege(true, SE_BACKUP_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}
			if (!SetPrivilege(true, SE_RESTORE_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}

			int error = RegRestoreKey(handle, regFile.getAbsolutePath(), (force ? REG_FORCE_RESTORE : 0));
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			SetPrivilege(false, SE_BACKUP_NAME_STRING);
			SetPrivilege(false, SE_RESTORE_NAME_STRING);
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Saves <code>this</code> and all subkeys and values to a new file, in the
	 * standard format.
	 * 
	 * @param regFile
	 *            The file to save to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @throws IOException
	 *             If <code>regFile</code> already exists and a temporary file
	 *             could not be created.
	 * @see #saveKey(boolean, boolean, File)
	 */
	public int saveKey(File regFile) throws IOException {
		long[] open = this.openKey0(KEY_WRITE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			if (!SetPrivilege(true, SE_BACKUP_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}

			File tmp = null;
			if (regFile.exists()) {
				tmp = File.createTempFile("jrsk", null);
				tmp.delete();
			}

			int error = RegSaveKey(handle, (tmp != null ? tmp.getAbsolutePath() : regFile.getAbsolutePath()));
			if (error == ERROR_SUCCESS) {
				if (tmp != null) {
					boolean del = regFile.delete();
					if (del)
						tmp.renameTo(regFile);
					tmp = null;
				}
			} else {
				if (tmp != null)
					tmp.delete();
			}
			SetPrivilege(false, SE_BACKUP_NAME_STRING);
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Saves <code>this</code> and all subkeys and values to a new file, in the
	 * specified format. The standard format is the only format supported by
	 * Windows 2000. The latest format works for Windows XP and higher. If
	 * <code>noCompress</code> is <code>true</code>, then <code>this</code>
	 * should be a root hive under <strong>HKEY_LOCAL_MACHINE</strong> or
	 * <strong>HKEY_USERS</strong>. For example,
	 * <strong>HKEY_LOCAL_MACHINE\SOFTWARE</strong> is the root of a hive. <br>
	 * <br>
	 * This method will only work on Windows XP systems or higher. If run on
	 * Windows 2000, then this method will perform exactly like
	 * {@link #saveKey(File)}.
	 * 
	 * @param latest
	 *            <code>true</code> if the hive should be saved using the
	 *            lastest format, <code>false</code> otherwise.
	 * @param noCompress
	 *            <code>true</code> if the hive should not be saved with
	 *            compression, <code>false</code> otherwise.
	 * @param regFile
	 *            The file to save to.
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @throws IOException
	 *             If <code>regFile</code> already exists and a temporary file
	 *             could not be created.
	 * @see #saveKey(File)
	 * @since 1.8
	 */
	public int saveKey(boolean latest, boolean noCompress, File regFile) throws IOException {
		if (noCompress && !this.canUnLoadKey())
			return ERROR_INVALID_PARAMETER;

		long[] open = this.openKey0(KEY_WRITE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			if (!SetPrivilege(true, SE_BACKUP_NAME_STRING)) {
				this.closeKey0(this.handles, open[0]);
				open = null;
				return ERROR_ACCESS_DENIED;
			}

			File tmp = null;
			if (regFile.exists()) {
				tmp = File.createTempFile("jrsk", null);
				tmp.delete();
			}

			int flags = (latest ? REG_LATEST_FORMAT : REG_STANDARD_FORMAT);
			if (noCompress)
				flags = REG_NO_COMPRESSION;

			int error = RegSaveKeyEx(handle, (tmp != null ? tmp.getAbsolutePath() : regFile.getAbsolutePath()), flags);
			if (error == ERROR_PROC_NOT_FOUND) {
				error = RegSaveKey(handle, (tmp != null ? tmp.getAbsolutePath() : regFile.getAbsolutePath()));
			}
			if (error == ERROR_SUCCESS) {
				if (tmp != null) {
					boolean del = regFile.delete();
					if (del)
						tmp.renameTo(regFile);
					tmp = null;
				}
			} else {
				if (tmp != null)
					tmp.delete();
			}
			SetPrivilege(false, SE_BACKUP_NAME_STRING);
			this.closeKey0(this.handles, open[0]);
			this.lastError = error;
			open = null;
			return error;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return this.lastError;
	}

	/**
	 * Allows <code>this</code> key's integer values (REG_DWORD and REG_QWORD,
	 * etc.) to be searched for a particular value. The index, if not -1,
	 * corresponds to the index of the value in a sorted list.
	 * 
	 * @param startIndex
	 *            The index for the value to start with.
	 * @param value
	 *            The integer key to search for.
	 * @return The index of the value that matches the key, or -1 if the key was
	 *         not found.
	 */
	public int searchIntegerValue(int startIndex, long value) {
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int[] info = RegQueryInfoKey(handle);
			this.lastError = (info != null ? info[ERROR_CODE] : ERROR_OUTOFMEMORY);
			if (info != null && info[ERROR_CODE] == ERROR_SUCCESS) {
				if (startIndex < 0 || startIndex >= info[NUM_VALUES]) {
					this.closeKey0(this.handles, open[0]);
					info = null;
					open = null;
					return -1;
				}

				String[] names = this.getValueNames();
				int result = this.searchInteger(handle, startIndex, value, names);
				this.closeKey0(this.handles, open[0]);
				names = null;
				info = null;
				open = null;
				return result;
			}
			info = null;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return -1;
	}

	// native method to search integer values
	private native int searchInteger(long hKey, int startIndex, long value, String[] names);

	/**
	 * Allows <code>this</code> key's string values (REG_SZ, REG_EXPAND_SZ and
	 * REG_MULTI_SZ) to be searched for a particular value. This method is just
	 * a wrapper that searches the values starting at the specified value index.
	 * It tests the value names as well as the data for any part that matches
	 * the passed in key to search for. The index, if not -1, corresponds to the
	 * index of the value in a sorted list.
	 * 
	 * @param startIndex
	 *            The index for the value to start with.
	 * @param key
	 *            The string key to search for.
	 * @return The index of the value that matches the key, either in the name
	 *         or data, or -1 if the key was not found.
	 */
	public int searchStringValue(int startIndex, String key) {
		return this.searchStringValue(startIndex, true, true, false, key);
	}

	/**
	 * Allows <code>this</code> key's string values (REG_SZ, REG_EXPAND_SZ and
	 * REG_MULTI_SZ) to be searched for a particular value. The parameters for
	 * this method allow for the value name as well as the value's data to be
	 * searched. Also if <code>matchStr</code> is <code>true</code>, then the
	 * entire value name or data must match the passed in search key. The index,
	 * if not -1, corresponds to the index of the value in a sorted list.
	 * 
	 * @param startIndex
	 *            The index for the value to start with.
	 * @param valueName
	 *            <code>true</code> if the names of the values should be
	 *            searched.
	 * @param data
	 *            <code>true</code> if the data of the values should be
	 *            searched.
	 * @param matchStr
	 *            <code>true</code> if match should be constrained to the entire
	 *            name or data, <code>false</code> if only a portion of the name
	 *            or data contains the search key.
	 * @param key
	 *            The string key to search for.
	 * @return The index of the value that matches the key, either in the name
	 *         or data, or -1 if the key was not found.
	 */
	public int searchStringValue(int startIndex, boolean valueName, boolean data, boolean matchStr, String key) {
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int[] info = RegQueryInfoKey(handle);
			this.lastError = (info != null ? info[ERROR_CODE] : ERROR_OUTOFMEMORY);
			if (info != null && info[ERROR_CODE] == ERROR_SUCCESS) {
				if (startIndex < 0 || startIndex >= info[NUM_VALUES]) {
					this.closeKey0(this.handles, open[0]);
					info = null;
					open = null;
					return -1;
				}

				String[] names = this.getValueNames();
				int result = this.searchString(handle, startIndex, valueName, data, matchStr, key, names);
				this.closeKey0(this.handles, open[0]);
				names = null;
				info = null;
				open = null;
				return result;
			}
			info = null;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return -1;
	}

	// native method to search key names and string values
	private native int searchString(long hKey, int startIndex, boolean valueName, boolean data, boolean matchStr, String key, String[] names);

	/**
	 * Allows <code>this</code> key's binary values (REG_NONE, REG_BINARY, etc.)
	 * to be searched for a particular value. The index, if not -1, corresponds
	 * to the index of the value in a sorted list.
	 * 
	 * @param startIndex
	 *            The index for the value to start with.
	 * @param value
	 *            The binary key to search for.
	 * @return The index of the value that matches the key, or -1 if the key was
	 *         not found.
	 * @since 1.6.1
	 */
	public int searchBinaryValue(int startIndex, byte[] value) {
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			int[] info = RegQueryInfoKey(handle);
			this.lastError = (info != null ? info[ERROR_CODE] : ERROR_OUTOFMEMORY);
			if (info != null && info[ERROR_CODE] == ERROR_SUCCESS) {
				if (startIndex < 0 || startIndex >= info[NUM_VALUES]) {
					this.closeKey0(this.handles, open[0]);
					info = null;
					open = null;
					return -1;
				}

				String[] names = this.getValueNames();
				int result = this.searchBinary(handle, startIndex, value, names);
				this.closeKey0(this.handles, open[0]);
				names = null;
				info = null;
				open = null;
				return result;
			}
			info = null;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return -1;
	}

	// native method to search binary values
	private native int searchBinary(long hKey, int startIndex, byte[] value, String[] names);

	/**
	 * If <code>this</code> represents a symbolic link, then the target for
	 * <code>this</code> will be changed to the key represented by
	 * <code>target</code>.
	 * 
	 * @param target
	 *            The <code>RegistryKey</code> to link to.
	 * @return <code>true</code> if the link was changed, <code>false</code>
	 *         otherwise.
	 * @since 1.4
	 */
	public boolean setLinkTo(RegistryKey target) {
		if (!this.isLinkKey())
			return false;
		if (target == null)
			return false;
		String ntPath = target.toNativePath();
		if (ntPath == null)
			return false;
		long[] open = this.openLinkKey(KEY_SET_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		boolean result = false;
		if (handle != NULL_NATIVE_HANDLE) {
			this.lastError = RegSetLinkValue(handle, "SymbolicLinkValue", ntPath);
			result = this.lastError == ERROR_SUCCESS;
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Sets the keys opened by WatchData to be disposed by this key's disposer.
	 * Needs to be accessible to WatchData
	 */
	void setWatchHandles(List<Long> list) {
		if (disposer.watchHandles != null)
			this.closeKey0(disposer.watchHandles, 0);
		disposer.watchHandles = list;
	}

	/**
	 * Retrieves an {@link Iterator} of {@link String}s which represent the
	 * names of the subkeys that are under <code>this</code>. If the subkeys of
	 * <code>this</code> are changed from outside the iterator while an
	 * iteration is in progress, then a {@link ConcurrentModificationException}
	 * is thrown. If an error occurs, then <code>null</code> is returned.
	 * 
	 * @return An {@link Iterator} of {@link String}s that are the names of the
	 *         subkeys of <code>this</code> or <code>null</code> if an error
	 *         occurs.
	 * @see #getSubKey(String)
	 * @see #getSubKeyNames()
	 * @see #getSubKeys()
	 * @see #getSubKeys(String...)
	 * @see #subKeysIterator()
	 * @since 1.8
	 */
	public Iterator<String> subKeyNamesIterator() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;

		Iterator<String> e = new RegistryKeyIterator<String>(info[NUM_SUB_KEYS], info[MAX_SUB_KEY_LENGTH], true);
		info = null;
		return e;
	}

	/**
	 * Retrieves an {@link Iterator} of <code>RegistryKey</code>s which
	 * represent the subkeys that are under <code>this</code>. If the subkeys of
	 * <code>this</code> are changed from outside the iterator while an
	 * iteration is in progress, then a {@link ConcurrentModificationException}
	 * is thrown. If an error occurs, then <code>null</code> is returned.
	 * 
	 * @return An {@link Iterator} of <code>RegistryKey</code>s that are the
	 *         subkeys of <code>this</code> or <code>null</code> if an error
	 *         occurs.
	 * @see #getSubKey(String)
	 * @see #getSubKeyNames()
	 * @see #getSubKeys()
	 * @see #getSubKeys(String...)
	 * @see #subKeyNamesIterator()
	 * @since 1.8
	 */
	public Iterator<RegistryKey> subKeysIterator() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;

		Iterator<RegistryKey> e = new RegistryKeyIterator<RegistryKey>(info[NUM_SUB_KEYS], info[MAX_SUB_KEY_LENGTH], false);
		info = null;
		return e;
	}

	/**
	 * Retrieves the {@link String} representation of <code>this</code> using
	 * the native registry api format.
	 * 
	 * @return A {@link String} representation for <code>this</code> in native
	 *         format.
	 * @since 1.4
	 */
	public String toNativePath() {
		StringBuffer buf = new StringBuffer("\\Registry\\");
		if (this.hKey == HKEY_CLASSES_ROOT)
			buf.append("Machine\\Software\\Classes\\");
		else if (this.hKey == HKEY_CURRENT_USER)
			buf.append("User\\" + getCurrentUserSid() + "\\");
		else if (this.hKey == HKEY_LOCAL_MACHINE)
			buf.append("Machine\\");
		else if (this.hKey == HKEY_USERS)
			buf.append("Users\\");
		else if (this.hKey == HKEY_CURRENT_CONFIG)
			buf.append("Machine\\System\\CurrentControlSet\\Hardware Profiles\\Current\\");
		else {
			buf = null;
			return null;
		}
		buf.append(this.absolutePath());
		return buf.toString();
	}

	/**
	 * Retrieve a {@link String} representation of <code>this</code>.
	 * 
	 * @return A {@link String} representation for <code>this</code>.
	 */
	public String toString() {
		if (this.machine != null)
			return this.machine + "\\" + this.getPath();
		return this.getPath();
	}

	/**
	 * Normalizes the passed in name to one that Windows can handle. This means
	 * that all extra '\' are deleted.
	 */
	private static String toWindowsName(String name) {
		if (name == null) {
			return "";
		}
		StringBuffer buf = new StringBuffer(name);
		while (buf.length() != 0 && buf.charAt(0) == '\\')
			buf.deleteCharAt(0);
		for (int i = 0; i < buf.length(); i++) {
			if (buf.charAt(i) == '\\') {
				if (buf.charAt(i - 1) == '\\') {
					buf.deleteCharAt(i--);
				}
			}
		}
		return buf.toString();
	}

	/**
	 * Unloads <code>this</code> and all subkeys and values from the registry.
	 * 
	 * @return An integer error code which tells if the operation was successful
	 *         or not. To obtain a description of the error, use
	 *         {@link #formatErrorMessage(int)}.
	 * @see #canUnLoadKey()
	 */
	public int unLoadKey() {
		if (!this.canUnLoadKey())
			return ERROR_ACCESS_DENIED;

		RegistryKey k = (this.hKey == HKEY_LOCAL_MACHINE ? roots[2] : roots[3]);

		long handle = k.openKey(KEY_WRITE);
		if (handle != NULL_NATIVE_HANDLE) {
			if (!SetPrivilege(true, SE_BACKUP_NAME_STRING)) {
				k.closeKey(handle);
				return ERROR_ACCESS_DENIED;
			}
			if (!SetPrivilege(true, SE_RESTORE_NAME_STRING)) {
				k.closeKey(handle);
				return ERROR_ACCESS_DENIED;
			}

			int error = RegUnLoadKey(handle, this.absolutePath());
			k.closeKey(handle);
			this.lastError = error;
			SetPrivilege(false, SE_BACKUP_NAME_STRING);
			SetPrivilege(false, SE_RESTORE_NAME_STRING);
			return error;
		}
		return this.lastError;
	}

	/**
	 * Tests to see if the specified value exists under <code>this</code>.
	 * 
	 * @param value
	 *            The name of the value whose existence will be tested.
	 * @return <code>true</code> if the value exists, <code>false</code>
	 *         otherwise.
	 */
	public boolean valueExists(String value) {
		boolean result = false;
		long[] open = this.openKey0(KEY_QUERY_VALUE, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE) {
			Object[] info = RegQueryValueEx(handle, value);
			this.lastError = (info != null ? ((Integer) info[ERROR_CODE]).intValue() : ERROR_OUTOFMEMORY);
			if (info != null && this.lastError == ERROR_SUCCESS) {
				info = null;
				result = true;
			}
		}
		this.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}

	/**
	 * Retrieves an {@link Iterator} of {@link String}s which represent the
	 * names of the values that are under <code>this</code>. If the values of
	 * <code>this</code> are changed from outside the iterator while an
	 * iteration is in progress, then a {@link ConcurrentModificationException}
	 * is thrown. If an error occurs, then <code>null</code> is returned.
	 * 
	 * @return An {@link Iterator} of {@link String}s that are the names of the
	 *         values of <code>this</code> or <code>null</code> if an error
	 *         occurs.
	 * @see #getValue(String)
	 * @see #getValueNames()
	 * @see #getValues()
	 * @see #getValues(String...)
	 * @see #valuesIterator()
	 * @since 1.8
	 */
	public Iterator<String> valueNamesIterator() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;

		Iterator<String> e = new RegistryValueIterator<String>(info[NUM_VALUES], info[MAX_VALUE_NAME_LENGTH], true);
		info = null;
		return e;
	}

	/**
	 * Retrieves an {@link Iterator} of {@link RegistryValue}s which represent
	 * the values that are under <code>this</code>. If the values of
	 * <code>this</code> are changed from outside the iterator while an
	 * iteration is in progress, then a {@link ConcurrentModificationException}
	 * is thrown. If an error occurs, then <code>null</code> is returned.
	 * 
	 * @return An {@link Iterator} of {@link RegistryValue}s that are the values
	 *         of <code>this</code> or <code>null</code> if an error occurs.
	 * @see #getValue(String)
	 * @see #getValueNames()
	 * @see #getValues()
	 * @see #getValues(String...)
	 * @see #valueNamesIterator()
	 * @since 1.8
	 */
	public Iterator<RegistryValue> valuesIterator() {
		int[] info = this.getKeyInfo();
		if (info == null)
			return null;

		Iterator<RegistryValue> e = new RegistryValueIterator<RegistryValue>(info[NUM_VALUES], info[MAX_VALUE_NAME_LENGTH], false);
		info = null;
		return e;
	}

	/**
	 * Specifies whether the key should look into the 64-bit registry or the
	 * 32-bit registry This method does nothing on Windows 2000 systems.
	 * 
	 * @param view64
	 *            <code>true</code> if the key should view the 64-bit part of
	 *            the registry, <code>false</code> otherwise.
	 * @since 1.6
	 */
	public void view64BitRegistry(boolean view64) {
		String ver = System.getProperty("os.version");
		if (ver.compareTo("5.0") <= 0)
			this.view64 = 0;
		else
			this.view64 = (view64 ? KEY_WOW64_64KEY : KEY_WOW64_32KEY);
	}

	/**
	 * Saves only the necessary data for <code>this</code>. Omitted fields are
	 * the parent and handles.
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
		out.writeLong(this.hKey);
		out.writeObject(this.name);
		out.writeObject(this.path);
		out.writeObject(this.absolutePath);
		out.writeBoolean(this.created);
		out.writeInt(this.level);
		out.writeInt(this.lastError);
		out.writeInt(this.view64);
		if (LIBRARY_VERSION >= 0x01080000) // v1.8 or higher
			out.writeObject(this.machine);
	}

	private final class RegistryKeyIterator<E> implements Iterator<E> {
		private int index;
		private int numSubKeys;
		private int maxSubKeyLen;
		private boolean isString;
		private E lastRet = null;

		private RegistryKeyIterator(int numSubKeys, int maxSubKeyLen, boolean isString) {
			this.index = 0;
			this.numSubKeys = numSubKeys;
			this.maxSubKeyLen = maxSubKeyLen;
			this.isString = isString;
		}

		public boolean hasNext() {
			return this.index < this.numSubKeys;
		}

		@SuppressWarnings("unchecked")
		public E next() {
			if (this.index >= this.numSubKeys)
				throw new NoSuchElementException();

			Object result = null;
			long[] open = RegistryKey.this.openKey0(KEY_ENUMERATE_SUB_KEYS, RegistryKey.this.handles);
			long handle = open[NATIVE_HANDLE];
			if (handle != NULL_NATIVE_HANDLE) {
				String subKey = RegEnumKeyEx(handle, this.index++, this.maxSubKeyLen + 1);
				if (subKey == null) {
					RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
					open = null;
					this.numSubKeys = 0;
					this.lastRet = null;
					throw new ConcurrentModificationException();
				}

				if (this.isString) {
					result = subKey;
				} else {
					result = new RegistryKey(RegistryKey.this.hKey, RegistryKey.this, RegistryKey.this.getPath(), subKey);
				}
				this.lastRet = (E) result;
			} else {
				this.numSubKeys = 0;
				this.lastRet = null;
			}
			RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
			open = null;
			if (this.numSubKeys == 0)
				throw new ConcurrentModificationException();
			return (E) result;
		}

		public void remove() {
			if (this.lastRet == null)
				throw new IllegalStateException();

			long[] open = RegistryKey.this.openKey0(DELETE, RegistryKey.this.handles);
			long handle = open[NATIVE_HANDLE];
			if (handle != NULL_NATIVE_HANDLE) {
				int error = ERROR_SUCCESS;
				if (this.isString) {
					error = RegDeleteKey(handle, (String) this.lastRet);
				} else {
					error = RegDeleteKey(handle, ((RegistryKey) this.lastRet).name);
				}

				RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
				open = null;
				this.lastRet = null;
				this.index--;
				this.numSubKeys--;

				if (error != ERROR_SUCCESS) {
					this.numSubKeys = 0;
					throw new ConcurrentModificationException();
				}
				return;
			} else {
				this.numSubKeys = 0;
				this.lastRet = null;
			}
			RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
			open = null;
			if (this.numSubKeys == 0)
				throw new ConcurrentModificationException();
		}
	}

	private final class RegistryValueIterator<E> implements Iterator<E> {
		private int index;
		private int numValues;
		private int maxValueNLen;
		private boolean isString;
		private E lastRet = null;

		private RegistryValueIterator(int numValues, int maxValueNLen, boolean isString) {
			this.index = 0;
			this.numValues = numValues;
			this.maxValueNLen = maxValueNLen;
			this.isString = isString;
		}

		public boolean hasNext() {
			return this.index < this.numValues;
		}

		@SuppressWarnings("unchecked")
		public E next() {
			if (this.index >= this.numValues)
				throw new NoSuchElementException();

			Object result = null;
			long[] open = RegistryKey.this.openKey0(KEY_QUERY_VALUE, RegistryKey.this.handles);
			long handle = open[NATIVE_HANDLE];
			if (handle != NULL_NATIVE_HANDLE) {
				String value = RegEnumValue(handle, this.index++, this.maxValueNLen + 1);
				if (value == null) {
					RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
					open = null;
					this.numValues = 0;
					this.lastRet = null;
					throw new ConcurrentModificationException();
				}

				if (this.isString) {
					result = value;
				} else {
					Object[] info = RegQueryValueEx(handle, value);
					if (info != null) {
						if (((Integer) info[ERROR_CODE]).intValue() == ERROR_SUCCESS) {
							int t = ((Integer) info[REG_VALUE_TYPE]).intValue();
							ValueType type = RegistryKey.getValueType(t);
							Object data = info[REG_VALUE_DATA];
							if (type == null) {
								result = new RegBinaryValue(RegistryKey.this, value, t, (byte[]) data);
							} else {
								switch (type) {
								case REG_SZ:
									result = new RegStringValue(RegistryKey.this, value, type, (String) data);
									break;
								case REG_EXPAND_SZ:
									result = new RegStringValue(RegistryKey.this, value, type,
											(RegistryKey.isAutoExpandEnvironmentVariables() ? expandEnvironmentVariables((String) data) : (String) data));
									break;
								case REG_BINARY:
								case REG_NONE:
								case REG_LINK:
								case REG_RESOURCE_LIST:
								case REG_FULL_RESOURCE_DESCRIPTOR:
								case REG_RESOURCE_REQUIREMENTS_LIST:
									result = new RegBinaryValue(RegistryKey.this, value, type, (byte[]) data);
									break;
								case REG_DWORD:
								case REG_DWORD_LITTLE_ENDIAN:
								case REG_DWORD_BIG_ENDIAN:
									result = new RegDWORDValue(RegistryKey.this, value, type, (Integer) data);
									break;
								case REG_MULTI_SZ:
									result = new RegMultiStringValue(RegistryKey.this, value, type, (String[]) data);
									break;
								case REG_QWORD:
								case REG_QWORD_LITTLE_ENDIAN:
									result = new RegQWORDValue(RegistryKey.this, value, type, (Long) data);
									break;
								}
							}
						}
						info = null;
					} else {
						RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
						open = null;
						this.numValues = 0;
						this.lastRet = null;
						throw new ConcurrentModificationException();
					}
				}
				this.lastRet = (E) result;
			} else {
				this.numValues = 0;
				this.lastRet = null;
			}
			RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
			open = null;
			if (this.numValues == 0)
				throw new ConcurrentModificationException();
			return (E) result;
		}

		public void remove() {
			if (this.lastRet == null)
				throw new IllegalStateException();

			long[] open = RegistryKey.this.openKey0(KEY_SET_VALUE, RegistryKey.this.handles);
			long handle = open[NATIVE_HANDLE];
			if (handle != NULL_NATIVE_HANDLE) {
				int error = ERROR_SUCCESS;
				if (this.isString) {
					error = RegDeleteValue(handle, (String) this.lastRet);
				} else {
					error = RegDeleteValue(handle, ((RegistryValue) this.lastRet).getName());
				}

				RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
				open = null;
				this.lastRet = null;
				this.index--;
				this.numValues--;

				if (error != ERROR_SUCCESS) {
					this.numValues = 0;
					throw new ConcurrentModificationException();
				}
				return;
			} else {
				this.numValues = 0;
				this.lastRet = null;
			}
			RegistryKey.this.closeKey0(RegistryKey.this.handles, open[0]);
			open = null;
			if (this.numValues == 0)
				throw new ConcurrentModificationException();
		}
	}
}