/*
 * DataSet.java
 * 
 * Created on May 27, 2004
 *
 * 
 * Copyright (C) 2004 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.InputStream;

/**
 * This interface provides an abstraction for accessing data sets. The underlying data
 * set can be accessed using either an <code>InputStream</code> or a <code>byte[]</code>
 * 
 * @author Edwin Peer
 *
 */
public interface DataSet {
	
	/**
	 * Returns the data set as a byte array.
	 * 
	 * @return the data set as a <code>byte[]</code>
	 */
	public byte[] getData();
	
	/**
	 * Returns the data set as an input stream.
	 * 
	 * @return the data set as a <code>InputStream</code>
	 */
	public InputStream getInputStream();
	
}
