/*
 * DomainSerialiser.java
 * 
 * Created on Nov 1, 2004
 *
 */
package net.sourceforge.cilib.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import net.sourceforge.cilib.Type.Types.Type;

/**
 * @author espeer
 *
 */
@SuppressWarnings("unused")
public class DomainSerialiser extends DomainBuilder {
	
	public DomainSerialiser(Type subject) {
		this.subject = subject;
		reset();
	}

	public void beginBuildPrefixVector() {

	}

	public void buildBit() {
		
	}
	
	public void buildInt(int lower, int upper) {
	
	}
	
	public void buildPostfixVector(String previous, int dimension, int slack) {
	
	}
	
	public void buildReal(double lower, double upper) {
	
	}
	
	public void endBuildPrefixVector() {
		
	}
	
	public void reset() {
		try {
			bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public byte[] getResult() {
		return bos.toByteArray();
	}
	
	private void next() {
		if (isVector) {
			
		}
	}
	
	private Type current;
	private Type subject;
	private ObjectOutputStream oos;
	private ByteArrayOutputStream bos;
	private int index;
	private boolean isVector;
}
