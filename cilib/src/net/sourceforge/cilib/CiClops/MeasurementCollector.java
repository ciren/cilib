/*
 * Created on Nov 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.cilib.CiClops;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPOutputStream;

import net.sourceforge.cilib.Domain.ComponentFactory;
import net.sourceforge.cilib.Domain.DomainComponent;
import net.sourceforge.cilib.Measurement.Measurement;


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
		domain = ComponentFactory.instance().newComponent(measurement.getDomain());
	}
	
	public void serialiseValue(Object value) {
		++count;
		try {
			domain.serialise(oos, value);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
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
		return domain.getRepresentation() + "^" + String.valueOf(count);
	}

	private ByteArrayOutputStream bos;
	private GZIPOutputStream zos;
	private ObjectOutputStream oos;
	private DomainComponent domain;
	private int count;
}