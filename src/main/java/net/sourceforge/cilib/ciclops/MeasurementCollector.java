/*
 * MeasurementCollector.java
 *
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
package net.sourceforge.cilib.ciclops;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import net.sourceforge.cilib.measurement.Measurement;


public class MeasurementCollector {
	
	public MeasurementCollector(Measurement measurement) {
		try {
			bos = new ByteArrayOutputStream();
			zos = new GZIPOutputStream(bos);
			oos = new ObjectOutputStream(zos);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		//domain = ComponentFactory.instance().newComponent(measurement.getDomain());
		domain = measurement.getDomain();
	}
	
	public void serialiseValue(Object value) {
		++count;
//		try {
//			//((Type)value).serialise(oos);
//		}
//		catch (IOException ex) {
//			throw new RuntimeException(ex);
//		}
	}
	
	public byte[] getData() {
		try {
			oos.close();
			zos.close();
			
			return bos.toByteArray();
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public String getDomain() {
		//return domain.getRepresentation() + "^" + String.valueOf(count);
		return domain;
	}
	
	public int getDimension() {
		return count;
	}

	private ByteArrayOutputStream bos;
	private GZIPOutputStream zos;
	private ObjectOutputStream oos;
	//private DomainComponent domain;
	private String domain;
	private int count;
}
