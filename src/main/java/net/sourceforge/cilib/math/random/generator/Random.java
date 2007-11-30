package net.sourceforge.cilib.math.random.generator;

import net.sourceforge.cilib.util.Cloneable;

/**
 * 
 * @author gpampara
 *
 */
public abstract class Random extends java.util.Random implements Cloneable {
	
	public Random(long seed) {
		super(seed);
	}

	public abstract Random getClone();

}
