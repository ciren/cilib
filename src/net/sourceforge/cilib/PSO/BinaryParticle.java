/*
 * Created on Mar 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.cilib.PSO;

import net.sourceforge.cilib.Type.Types.Bit;


/**
 * @author gary
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BinaryParticle extends StandardParticle {
	
	public BinaryParticle() {
		super();
	}
	
	public void move() {
		//for (int i = 0; i < position.length; i++) {
		for (int i = 0; i < position.getDimension(); i++) {
			double result = f(velocity.getReal(i));
			double rand = Math.random();
			
			if (rand < result) {
				position.set(i, new Bit(true));
			}
			else {
				//position[i] = 0.0;
				position.set(i, new Bit(false));
			}
		}
	}
	
	private double f(double v) {
		return ( 1/(1+Math.pow(Math.E, -1.0*v)) ); 
	}
}
