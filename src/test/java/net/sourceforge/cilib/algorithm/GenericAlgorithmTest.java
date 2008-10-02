/**
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
