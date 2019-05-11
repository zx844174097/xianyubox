/*
 * WatchData.java    2012/09/14
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

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.registry.event.RegistryEvent;

import static com.registry.RegistryKey.ERROR_ACCESS_DENIED;
import static com.registry.RegistryKey.ERROR_SUCCESS;
import static com.registry.RegistryKey.KEY_NOTIFY;
import static com.registry.RegistryKey.KEY_READ;
import static com.registry.RegistryKey.NATIVE_HANDLE;
import static com.registry.RegistryKey.NULL_NATIVE_HANDLE;

/**
 * <p>
 * This class represents the watch information for a registry key.
 * It provides control for when to start or stop monitoring
 * a key. This class uses separate threads for each
 * key so as not to hold up any other operations while
 * waiting for a change in the registry.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
final class WatchData {
	static final int WAIT_OBJECT_0  = 0;
	static final int WAIT_ABANDONED = 80;
	static final int WAIT_TIMEOUT   = 102;
	static final int WAIT_FAILED    = 0xffffffff;
	
	static final int INFINITE = 0xffffffff;
	
	static AtomicInteger ID_Counter = new AtomicInteger(0);
	
	/* This thread handles the dispatching of registry events */
	static final Thread rndt = new RegistryNotifyDispatchThread();
	
	static {
		rndt.start();
	}
	
	private RegistryKey key;
	private Thread worker;
	private boolean watchSubtree;
	private int dwNotifyFilter;
	private int id;
	private boolean isRunning;
	
	/**
	 * Constructs a new <code>WatchData</code> using the
	 * specified key. By default it will not watch
	 * any subkeys and it uses the default filter.
	 *
	 * @param key the registry key to watch.
	 */
	WatchData(RegistryKey key) {
		this(key, false, RegistryKey.DEFAULT_FILTER);
	}
	
	/**
	 * Constructs a new <code>WatchData</code> using the
	 * specified key. Also control for whether or
	 * not to watch subkeys is given. The default
	 * filter will be used for watching criteria.
	 *
	 * @param key the registry key to watch.
	 * @param watchSubtree whether or not changes
	 *                     in the subtree should be reported.
	 */
	WatchData(RegistryKey key, boolean watchSubtree) {
		this(key, watchSubtree, RegistryKey.DEFAULT_FILTER);
	}
	
	/**
	 * Constructs a new <code>WatchData</code> using the
	 * specified key. Also control for whether or
	 * not to watch subkeys is given. The watch filters
	 * can also be specified.
	 *
	 * @param key the registry key to watch.
	 * @param watchSubtree whether or not changes
	 *                     in the subtree should be reported.
	 * @param dwNotifyFilter filter of what changes to report.
	 */
	WatchData(RegistryKey key, boolean watchSubtree, int dwNotifyFilter) {
		this.key = key;
		this.watchSubtree = watchSubtree;
		this.dwNotifyFilter = dwNotifyFilter;
		this.id = ID_Counter.getAndIncrement();
	}
	
	/**
	 * Tests whether <code>this</code> is equal to <code>o</code>.
	 * Both are equal if <code>o</code> is an instance of <code>WatchData</code>
	 * and if both are watching the same key.
	 *
	 * @param o the object to test equality with.
	 * @return whether the two objects are equal or not.
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof WatchData))
			return false;
		WatchData d = (WatchData) o;
		return this.key.equals(d.key);
	}
	
	/**
	 * Calculates the hash code value for <code>this</code> watch.
	 *
	 * @return The hash code for <code>this</code>.
	 */
	public int hashCode() {
		return this.key.hashCode();
	}
	
	/**
	 * Retrieves the id for <code>this WatchData</code> object.
	 *
	 * @return the id for <code>this</code>.
	 */
	public int getID() {
		return this.id;
	}
	
	/**
	 * Retrieves the watched registry key for <code>this WatchData</code> object.
	 *
	 * @return the watched registry key for <code>this</code>.
	 */
	public RegistryKey getKey() {
		return this.key;
	}
	
	/**
	 * Tests to see if the registry key is currently being watched.
	 *
	 * @return <code>true</code> if the registry key is being watched,
	 *         <code>false</code> otherwise.
	 */
	public synchronized boolean isRunning() {
		return this.isRunning && this.worker.isAlive();
	}
	
	/**
	 * Sets the property that subkeys should be watched.
	 *
	 * @param watchSubtree whether or not changes
	 *                     in the subtree should be reported.
	 */
	public synchronized void setWatchSubtree(boolean watchSubtree) {
		this.watchSubtree = watchSubtree;
	}
	
	/**
	 * Sets the type of notifications to report.
	 *
	 * @param dwNotifyFilter filter of what changes to report.
	 */
	public synchronized void setNotifyFilter(int dwNotifyFilter) {
		this.dwNotifyFilter = dwNotifyFilter;
	}
	
	/**
	 * Tells <code>this</code> to start monitoring the
	 * specified registry key.
	 */
	public synchronized void start() {
		if (!this.isRunning) {
			this.worker = new Worker(this,
				this.key, this.watchSubtree,
				this.dwNotifyFilter, this.id);
			this.worker.start();
			this.isRunning = true;
		}
	}
	
	/**
	 * Tells <code>this</code> to stop monitoring the
	 * specified registry key.
	 */
	public synchronized void stop() {
		if (this.isRunning) {
			((Worker) this.worker).terminate();
			this.worker.interrupt();
			this.worker = null;
			this.isRunning = false;
		}
	}
	
	static native long CreateEvent();
	static native int WaitForSingleObject(long handle, int timeout);
	static native void CloseHandle(long handle);
}

final class RegistryNotifyDispatchThread extends Thread {
	private LinkedBlockingQueue<RegistryEvent> eventQueue;
	private boolean shutdown;
	
	RegistryNotifyDispatchThread() {
		eventQueue = new LinkedBlockingQueue<RegistryEvent>();
		shutdown = false;
		setPriority(Thread.NORM_PRIORITY - 1);
		setDaemon(true);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown = true;
				RegistryNotifyDispatchThread.this.interrupt();
			}
		});
	}
	
	public void pushEvent(RegistryEvent e) throws InterruptedException {
		eventQueue.put(e);
	}
	
	public void run() {
		while (true) {
			if (shutdown) {
				break;
			}
			
			try {
				RegistryWatcher.fireNotifyChange(eventQueue.take());
			} catch (InterruptedException e) {
			}
		}
	}
}

final class Worker extends Thread {
	private WatchData wd;
	private RegistryKey key;
	private LinkedList<Long> handles;
	private boolean watchSubtree;
	private int dwNotifyFilter;
	private int id;
	
	private long hKey = NULL_NATIVE_HANDLE;
	private long event = 0;
	private boolean isTerminated = false;
	
	Worker(WatchData wd, RegistryKey key, boolean watchSubtree, int dwNotifyFilter, int id) {
		this.wd = wd;
		this.key = key;
		this.handles = new LinkedList<Long>();
		this.watchSubtree = watchSubtree;
		this.dwNotifyFilter = dwNotifyFilter;
		this.id = id;
		this.setPriority(Thread.NORM_PRIORITY - 1);
	}
	
	private boolean exists() {
		boolean result = false;
		long[] open = this.key.openKey0(KEY_READ, this.handles);
		long handle = open[NATIVE_HANDLE];
		if (handle != NULL_NATIVE_HANDLE || this.key.getLastError() == ERROR_ACCESS_DENIED) {
			result = true;
		}
		this.key.closeKey0(this.handles, open[0]);
		open = null;
		return result;
	}
	
	public void terminate() {
		this.isTerminated = true;
	}
	
	public void run() {
		this.key.setWatchHandles(this.handles);
		long[] open = this.key.openKey0(KEY_NOTIFY, this.handles);
		this.hKey = open[NATIVE_HANDLE];
		if (this.hKey == NULL_NATIVE_HANDLE) {
			RegistryWatcher.removeWatch(wd);
			open = null;
			return;
		}
		this.event = WatchData.CreateEvent();
		if (this.event == 0) {
			this.terminate();
			this.key.closeKey0(this.handles, open[0]);
			RegistryWatcher.removeWatch(wd);
			open = null;
			return;
		}
		
		boolean remove = false;
		while (!this.isTerminated) {
			if (!this.exists()) {
				remove = true;
				break;
			}
			
			if (this.isInterrupted()) {
				break;
			}
			
			int error = ERROR_SUCCESS;
			try {
				error = RegistryKey.notifyChange(
					this.hKey, this.watchSubtree, this.dwNotifyFilter, this.event);
				if (error != ERROR_SUCCESS) {
					remove = true;
					break;
				}
				if (WatchData.WaitForSingleObject(this.event, WatchData.INFINITE) == WatchData.WAIT_OBJECT_0) {
					if (!this.isTerminated) {
						RegistryEvent e = new RegistryEvent(this.id, this.key);
						((RegistryNotifyDispatchThread) WatchData.rndt).pushEvent(e);
					} else {
						break;
					}
				}
			} catch (InterruptedException e) {
				if (error != ERROR_SUCCESS)
					remove = true;
			}
		}
		
		this.key.closeKey0(this.handles, open[0]);
		WatchData.CloseHandle(this.event);
		this.event = 0;
		open = null;
		if (remove) {
			RegistryWatcher.removeWatch(wd);
		}
	}
}