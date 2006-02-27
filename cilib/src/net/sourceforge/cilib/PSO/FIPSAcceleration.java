/*
 * Created on Jun 15, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.cilib.PSO;

/**
 * @author engel
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 * 
 * This is an implementation of the fully informed PSO, for which there is only one
 * velocity component in addition to the inertia, and therefor the one larger default
 * for the acceleration
 */
public class FIPSAcceleration extends StandardAcceleration {

	/**
	 * 
	 */
	public FIPSAcceleration() {
		super();
		acceleration = 1.0;
	}

	/**
	 * @return Returns the acceleration.
	 */
	public double getAcceleration() {
		return acceleration  * randomGenerator.nextFloat();
	}

	/**
	 * @param acceleration The acceleration to set.
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

}
