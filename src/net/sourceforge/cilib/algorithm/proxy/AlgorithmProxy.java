package net.sourceforge.cilib.algorithm.proxy;

import net.sourceforge.cilib.algorithm.Algorithm;

/**
 * 
 * @author Gary Pampara, Francois Geldenhuys, Theuns Cloete
 * @TODO: Convert this code to work on the simulation level
 */
public interface AlgorithmProxy {
	
	public Algorithm get();
	
	public void set(Algorithm algorithm);

}
