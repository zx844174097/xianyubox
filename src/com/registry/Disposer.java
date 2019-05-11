/*
 * Copyright (c) 2002, 2010, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * Disposer.java    2012/09/14
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

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.PhantomReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * This class is used for registering and disposing the native
 * data associated with java objects.
 *
 * The object can register itself by calling one of the addRecord
 * methods and providing either the pointer to the native disposal
 * method or a descendant of the DisposerRecord class with overridden
 * dispose() method.
 * 
 * When the object becomes unreachable, the dispose() method
 * of the associated DisposerRecord object will be called.
 *
 * @author Yinon Michaeli
 * @version 1.8
 * @since 1.3
 * @see DisposerRecord
 */
class Disposer implements Runnable {
	private static final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();
	@SuppressWarnings("rawtypes")
	private static final Hashtable<Reference, DisposerRecord> records = new Hashtable<Reference, DisposerRecord>();
	
	private static Disposer disposerInstance;
	public static final int WEAK = 0;
	public static final int PHANTOM = 1;
	private static int refType = PHANTOM;
	
	static {
		RegistryKey.initialize();
		initIDs();
		String type = java.security.AccessController.doPrivileged(
			new java.security.PrivilegedAction<String>() {
				public String run() {
					return System.getProperty("sun.java2d.reftype");
				}
			}
		);
		if (type != null) {
			if (type.equals("weak")) {
				refType = WEAK;
				System.err.println("Using WEAK refs");
			} else {
				refType = PHANTOM;
				System.err.println("Using PHANTOM refs");
			}
		}
		disposerInstance = new Disposer();
		java.security.AccessController.doPrivileged(
			new java.security.PrivilegedAction<Object>() {
				public Object run() {
					/* The thread must be a member of a thread group
					 * which will not get GCed before VM exit.
					 * Make its parent the top-level thread group.
					 */
					ThreadGroup tg = Thread.currentThread().getThreadGroup();
					for (ThreadGroup tgn = tg;
						tgn != null;
						tg = tgn, tgn = tg.getParent());
					Thread t =
						new Thread(tg, disposerInstance, "JRegistry Disposer");
					t.setContextClassLoader(null);
					t.setDaemon(true);
					t.setPriority(Thread.MAX_PRIORITY);
					t.start();
					return null;
				}
			}
		);
	}
	
	/**
	 * Registers the object and the native data for later disposal.
	 * @param target Object to be registered
	 * @param disposeMethod pointer to the native disposal method
	 * @param pData pointer to the data to be passed to the
	 *              native disposal method
	 */
	public static void addRecord(Object target, long disposeMethod, long pData) {
		disposerInstance.add(target,
			new DefaultDisposerRecord(disposeMethod, pData));
	}
	
	/**
	 * Registers the object and the native data for later disposal.
	 * @param target Object to be registered
	 * @param rec the associated DisposerRecord object
	 * @see DisposerRecord
	 */
	public static void addRecord(Object target, DisposerRecord rec) {
		disposerInstance.add(target, rec);
	}
	
	/**
	 * Performs the actual registration of the target object to be disposed.
	 * @param target Object to be registered, or if target is an instance
	 *               of DisposerTarget, its associated disposer referent
	 *               will be the Object that is registered
	 * @param rec the associated DisposerRecord object
	 * @see DisposerRecord
	 */
	synchronized void add(Object target, DisposerRecord rec) {
		if (target instanceof DisposerTarget) {
			target = ((DisposerTarget) target).getDisposerReferent();
		}
		java.lang.ref.Reference<Object> ref;
		if (refType == PHANTOM) {
			ref = new PhantomReference<Object>(target, queue);
		} else {
			ref = new WeakReference<Object>(target, queue);
		}
		records.put(ref, rec);
	}
	
	public void run() {
		while (true) {
			try {
				Object obj = queue.remove();
				((Reference<?>) obj).clear();
				DisposerRecord rec = records.remove(obj);
				rec.dispose();
				obj = null;
				rec = null;
				clearDeferredRecords();
			} catch (Exception e) {
				System.out.println("Exception while removing reference: " + e);
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * This is a marker interface that, if implemented, means it
	 * doesn't acquire any special locks, and is safe to
	 * be disposed in the poll loop on whatever thread
	 * which happens to be the Toolkit thread, is in use.
	 */
	public static interface PollDisposable {
	};
	
	private static volatile ArrayList<DisposerRecord> deferredRecords = null;
	
	private static void clearDeferredRecords() {
		if (deferredRecords == null || deferredRecords.isEmpty()) {
			return;
		}
		for (int i = 0; i < deferredRecords.size(); i++) {
			try {
				DisposerRecord rec = deferredRecords.get(i);
				rec.dispose();
			} catch (Exception e) {
				System.out.println("Exception while disposing deferred rec.");
				e.printStackTrace();
			}
		}
		deferredRecords.clear();
	}
	
	/*
	 * Set to indicate the queue is presently being polled.
	 */
	public static volatile boolean pollingQueue = false;
	
	/*
	 * The pollRemove() method is called back from a dispose method
	 * that is running on the toolkit thread and wants to
	 * dispose any pending refs that are safe to be disposed
	 * on that thread.
	 */
	public static void pollRemove() {
		/* This should never be called recursively, so this check
		 * is just a safeguard against the unexpected.
		 */
		if (pollingQueue) {
			return;
		}
		Object obj;
		pollingQueue = true;
		int freed = 0;
		int deferred = 0;
		try {
			while ((obj = queue.poll()) != null
				&& freed < 10000 && deferred < 100) {
				freed++;
				((Reference<?>) obj).clear();
				DisposerRecord rec = records.remove(obj);
				if (rec instanceof PollDisposable) {
					rec.dispose();
					obj = null;
					rec = null;
				} else {
					if (rec == null) { // shouldn't happen, but just in case.
						continue;
					}
					deferred++;
					if (deferredRecords == null) {
						deferredRecords = new ArrayList<DisposerRecord>(5);
					}
					deferredRecords.add(rec);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception while removing reference: " + e);
			e.printStackTrace();
		} finally {
			pollingQueue = false;
		}
	}
	
	private static native void initIDs();
	
	/*
	 * This was added to avoid creation
	 * of an additional disposer thread.
	 * WARNING: this thread class monitors a specific queue, so a reference
	 * added here must have been created with this queue. Failure to do
	 * so will clutter the records hashmap and no one will be cleaning up
	 * the reference queue.
	 */
	public static void addReference(Reference<Object> ref, DisposerRecord rec) {
		records.put(ref, rec);
	}
	
	public static void addObjectRecord(Object obj, DisposerRecord rec) {
		records.put(new WeakReference<Object>(obj, queue) , rec);
	}
	
	/* This is intended for use in conjunction with addReference(..)
	 */
	public static ReferenceQueue<Object> getQueue() {
		return queue;
	}
}