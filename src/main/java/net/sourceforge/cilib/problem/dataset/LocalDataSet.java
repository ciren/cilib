/*
 * LocalDataSet.java
 * 
 * Created on Jul 9, 2004
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
 */
package net.sourceforge.cilib.problem.dataset;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * @author Edwin Peer
 */
public class LocalDataSet extends DataSet {
	private static final long serialVersionUID = -3482617012711168661L;
	private static Logger log = Logger.getLogger(LocalDataSet.class);

	private String fileName = null;

	public LocalDataSet() {
		super();
		fileName = "<not set>";
	}

	public LocalDataSet(LocalDataSet rhs) {
		super(rhs);
		fileName = new String(rhs.fileName);
	}

	@Override
	public LocalDataSet clone() {
		return new LocalDataSet(this);
	}

	public void setFile(String fileName) {
		this.fileName = fileName;
	}

	public String getFile() {
		return fileName;
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

	public InputStream getInputStream() {
		try {
			log.info("Using dataset " + fileName);
			InputStream is = new BufferedInputStream(new FileInputStream(fileName));
			return is;
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
