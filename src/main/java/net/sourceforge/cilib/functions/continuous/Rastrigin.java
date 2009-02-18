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
 * The rastrigin function.
 *
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Seperable</li>
 * </ul>
 *
 * f(x) = 0; x = (0,0,...,0);
 *
 * x e [-5.12, 5.12];
 *
 * @author  Edwin Peer
 */
public class Rastrigin extends ContinuousFunction implements Differentiable {
    private static final long serialVersionUID = 447701182683968035L;

    /**
     * Create an instance of the Rastrigin function. The default domain is defined to be
     * R(-5.12, 5.12)^30
     */
	public Rastrigin() {
        setDomain("R(-5.12, 5.12)^30");
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Rastrigin getClone() {
		return new Rastrigin();
	}

	/**
	 * {@inheritDoc}
	 */
    public Object getMinimum() {
        return new Double(0);
    }

    /**
     * {@inheritDoc}
     */
    public double evaluate(Vector x) {
        double tmp = 0;
        for (int i = 0; i < getDimension(); ++i) {
            tmp += x.getReal(i) * x.getReal(i) - 10.0 * Math.cos(2 * Math.PI * x.getReal(i));
        }
        return 10*getDimension() + tmp;
    }


    /**
     * {@inheritDoc}
     */
    public Vector getGradient(Vector x) {
        Vector tmp = new Vector(getDimension());

        for (int i = 0; i < getDimension(); ++i) {
        	tmp.setReal(i, (2.0 * x.getReal(i)) + (20 * Math.PI * Math.sin(2.0 * Math.PI * x.getReal(i))));
        }

        return tmp;
    }
}
