/*
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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.Differentiable;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * Spherical function.
 * 
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * <li>Separable</li>
 * <li>Convex</li>
 * </ul>
 * 
 * f(x) = 0; x = (0,0,...,0)
 * x e [-5.12, 5.12]
 * 
 * @author  Edwin Peer
 */
public class Spherical extends ContinuousFunction implements Differentiable {
    private static final long serialVersionUID = 5811377575647995206L;

    /**
     * Create a new instance of {@code Spherical}.
     */
	public Spherical() {
        setDomain("R(-5.12, 5.12)^30");
    }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Spherical getClone() {
		return new Spherical();
	}

	/**
	 * Get the minimum of the function. It is defined to be a value of <code>0.0</code>.
	 * @return The function minimum value.
	 */
    public Object getMinimum() {
        return new Double(0);
    }

	/**
	 * {@inheritDoc}
	 * @return The function maximum defined in the default range of the function.
	 */
    public Object getMaximum() {
    	return new Double(78.6432);
    }

    /**
     * {@inheritDoc}
     */
    public double evaluate(Vector x) {
        double tmp = 0;
        for (int i = 0; i < x.getDimension(); i++) {
        	double value = x.getReal(i);
            tmp += value * value;
        }
        return tmp;
    }
    
    /**
     * {@inheritDoc}
     */
    public Vector getGradient(Vector x) {
    	Vector tmp = new Vector(x.getDimension());
    	    	
    	for (int i = 0; i < x.getDimension(); ++i) {
    		tmp.setReal(i, 2*x.getReal(i));
    	}
    	
        return tmp;        
    }
    
}
