/*
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.ioc.registry;

import java.util.Hashtable;

/**
 * Container to hold references to objects that need to used with object
 * injection.
 *
 * @author Gary Pampara and Francois Geldenhuys
 */
public final class ObjectRegistry {

	private Hashtable<String, Object> objectSet;

	private ObjectRegistry() {
		objectSet = new Hashtable<String, Object>();
	}


	/**
	 *
	 * @return
	 */
	public static ObjectRegistry getInstance() {
		return ObjectRegistryHolder.INSTANCE;
	}


	/**
	 *
	 * @param name
	 * @return
	 */
	public Object getObject(String name) {
		return this.objectSet.get(name);
	}


	/**
	 *
	 * @param name
	 * @param object
	 */
	public void addObject(String name, Object object) {
		this.objectSet.put(name, object);
	}


	/**
	 * Return the number of entries contained within the ObjectRegistry.
	 * @return The number of entries within the <tt>ObjectRegistry</tt>
	 */
	public int size() {
		return this.objectSet.size();
	}

	private static class ObjectRegistryHolder {
		public static final ObjectRegistry INSTANCE = new ObjectRegistry();
	}

}
