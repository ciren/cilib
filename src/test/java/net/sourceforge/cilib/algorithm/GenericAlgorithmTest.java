package net.sourceforge.cilib.algorithm;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EmptyStackException;

import net.sourceforge.cilib.pso.PSO;

import org.junit.Test;

public class GenericAlgorithmTest {
	
	@SuppressWarnings("static-access")
	@Test(expected=EmptyStackException.class)
	public void serialisationAndInitialisation() {
		// Any algorithm can be used, just need a concrete class
		Algorithm algorithm = new PSO();
		ByteArrayOutputStream byteArray = null;
		
		try {
			byteArray = new ByteArrayOutputStream();
			ObjectOutputStream o = new ObjectOutputStream(byteArray);
			o.writeObject(algorithm);
			o.close();	
		} catch (IOException io) {}
		
		byte [] classData = byteArray.toByteArray();
		Algorithm target = null;
		
		try {
			ObjectInputStream i = new ObjectInputStream(new ByteArrayInputStream(classData));
			target = (Algorithm) i.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			algorithm.get();
		}
		catch (EmptyStackException e) {}
		
		// We have to make sure that the AlgorithmStack is in fact created and not null, stack should be empty
		assertNotNull(target.get());
	}

}
