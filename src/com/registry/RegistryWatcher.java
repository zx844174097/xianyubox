/*
 * RegistryWatcher.java    2012/09/15
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.registry.event.RegistryEvent;
import com.registry.event.RegistryListener;

/**
 * <p>
 * This class provides the functionality to watch
 * for registry key changes.
 * </p>
 * <p>
 * To start monitoring changes, a class would first implement
 * the {@link com.registry.event.RegistryListener} class. A call to
 * {@link #addRegistryListener} would then register that
 * listener so that registry key changes will be sent to it.
 * </p>
 * <p>
 * After that, just add registry keys to monitor via the
 * {@link #watchKey(RegistryKey)} method or remove registry keys
 * to stop monitoring them with the {@link #removeKey(RegistryKey)}
 * method.
 * </p>
 *
 * @author Yinon Michaeli
 * @version 1.8
 */
public final class RegistryWatcher {
	private static List<RegistryListener> listenerList = Collections.synchronizedList(
		new ArrayList<RegistryListener>());
	private static List<WatchData> watchData = Collections.synchronizedList(new ArrayList<WatchData>());
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				for (int i = 0; i < RegistryWatcher.watchData.size(); i++)
					RegistryWatcher.watchData.get(i).stop();
			}
		});
	}
	
	private RegistryWatcher() {
		throw new InternalError();
	}
	
	/**
	 * Registers a listener to receive registry key changes.
	 *
	 * @param l the listener that will receive the changes.
	 */
	public static void addRegistryListener(RegistryListener l) {
		listenerList.add(l);
	}
	
	/**
	 * Unregisters the listener so that it will not longer receive
	 * registry key changes.
	 *
	 * @param l the listener to unregister.
	 */
	public static void removeRegistryListener(RegistryListener l) {
		listenerList.remove(l);
	}
	
	/**
	 * Adds the specified registry key to the watch list.
	 * When the key is added, listeners will be
	 * able to receive change notifications for the key.
	 * <br>
	 * By default, changes will only be recorded within the key
	 * not any of the subkeys.
	 *
	 * @param key the registry key to watch.
	 * @return the watch id for this key.
	 * @throws RegistryException if at least one listener is not registered.
	 */
	public static int watchKey(RegistryKey key) {
		return watchKey(key, false, RegistryKey.DEFAULT_FILTER);
	}
	
	/**
	 * Adds the specified registry key to the watch list.
	 * When the key is added, listeners will be
	 * able to receive change notifications for the key.
	 * The user can choose whether the subtree of the key
	 * should be watched as well.
	 *
	 * @param key the registry key to watch.
	 * @param watchSubtree whether or not changes in the subtree
	 *                     should be reported.
	 * @return the watch id for this key.
	 * @throws RegistryException if at least one listener is not registered.
	 */
	public static int watchKey(RegistryKey key, boolean watchSubtree) {
		return watchKey(key, watchSubtree, RegistryKey.DEFAULT_FILTER);
	}
	
	/**
	 * Adds the specified registry key to the watch list.
	 * When the key is added, listeners will be
	 * able to receive change notifications for the key.
	 * The user can choose whether the subtree of the key
	 * should be watched as well. The filter describes what sort
	 * of changes to report.
	 *
	 * @param key the registry key to watch.
	 * @param watchSubtree whether or not changes in the subtree
	 *                     should be reported.
	 * @param dwNotifyFilter filter of what changes to report.
	 * @return the watch id for this key.
	 * @throws RegistryException if at least one listener is not registered.
	 */
	public static int watchKey(RegistryKey key, boolean watchSubtree, int dwNotifyFilter) {
		if (listenerList.size() == 0)
			throw new RegistryException("Illegal State: At least one listener must be registered.");
		if (!key.exists() || dwNotifyFilter == 0)
			return -1;
		for (int i = 0; i < watchData.size(); i++) {
			WatchData a = (WatchData) watchData.get(i);
			if (a.getKey().equals(key)) {
				a.stop();
				a.setWatchSubtree(watchSubtree);
				a.setNotifyFilter(dwNotifyFilter);
				a.start();
				return a.getID();
			}
		}
		WatchData data = new WatchData(key, watchSubtree, dwNotifyFilter);
		watchData.add(data);
		data.start();
		return data.getID();
	}
	
	/**
	 * Removes <code>key</code> from the list of watched
	 * keys.
	 *
	 * @param key the registry key to stop monitoring.
	 */
	public static void removeKey(RegistryKey key) {
		for (int i = 0; i < watchData.size(); i++) {
			WatchData a = (WatchData) watchData.get(i);
			if (a.getKey().equals(key)) {
				a.stop();
				watchData.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Removes the registry key with the corresponding watch id
	 * from the list of watched keys.
	 *
	 * @param id the id of the registry key to remove.
	 */
	public static void removeID(int id) {
		for (int i = 0; i < watchData.size(); i++) {
			WatchData a = (WatchData) watchData.get(i);
			if (a.getID() == id) {
				a.stop();
				watchData.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Called to stop monitoring a key that
	 * was deleted.
	 */
	static void removeWatch(WatchData data) {
		for (int i = 0; i < watchData.size(); i++) {
			WatchData a = (WatchData) watchData.get(i);
			if (a.equals(data)) {
				a.stop();
				watchData.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Notify listeners when a registry key has changed.
	 * Called by {@link WatchData}.
	 */
	static void fireNotifyChange(RegistryEvent e) {
		for (int i = 0; i < listenerList.size(); i++) {
			RegistryListener l = (RegistryListener) listenerList.get(i);
			l.notifyChange(e);
		}
	}
}