/*
 * CachedDataSet.java
 * 
 * Created on July 9, 2004
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
package net.sourceforge.cilib.CiClops;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.cilib.Problem.DataSet;

/**
 * @author espeer
 *
 */
public class CachedDataSet implements DataSet {
	
	public static String CACHE_PATH = "";
	
	public CachedDataSet() {
		id = -1;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public byte[] getData() {
		try {
			InputStream is = getInputStream();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer, 0, buffer.length)) != -1) {
				bos.write(buffer, 0, len);
			}
			bos.close();
			
			return bos.toByteArray();
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private int id;

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.DataSet#getInputStream()
	 */
	public InputStream getInputStream() {
		try {
			String fileName = CACHE_PATH + File.pathSeparator + String.valueOf(id) + ".ds";
			InputStream is = new BufferedInputStream(new FileInputStream(fileName));
			return is;
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
