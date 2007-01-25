package net.sourceforge.cilib.math.random.generator;

/**
 * 
 * @author gpampara
 *
 */
public abstract class Random extends java.util.Random {
	
	public Random(long seed) {
		super(seed);
	}

	public abstract Random clone();

}
