/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;


/**
 * <p>Michalewicz funtion 12.</p>
 *
 ** <p><b>Reference:</b>
 *  http://www.geatbx.com/docu/fcnindex-01.html#TopOfPage</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> if n=5 then f(<b>x</b>*) = -4.687 </li>
 * <li> if n=10 then f(<b>x</b>*) = -9.66 </li>
 * <li> for x_i in [0, pi]</li>
 * </ul>
 * </p>
 *
 * Characteristics:
 * <ul>
 * <li>Multi-modal</li>
 * <li>Has n! local minima</li>
 * <li>Non seperable</li>
 * </ul>

 * @author  engel
 */
public class Michalewicz extends ContinuousFunction {

    private static final long serialVersionUID = -4391269929189674709L;

    /**
     * m controls the steepness of the valleys; the larger m, the
     * more difficult the search
     */
    private int m;

    /** Creates a new instance of EpistaticMichalewicz. */
    public Michalewicz() {
        m = 10;
        setDomain("R(0, 3.141592653589793)^10");
    }

    /**
     * {@inheritDoc}
     */
    public Michalewicz getClone() {
        return new Michalewicz();
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        if (this.getDimension() == 5)
            return -4.687;
        else if (this.getDimension() == 10)
            return -9.66;

        return -Double.MAX_VALUE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sumsq = 0.0;

        for (int i = 0; i < getDimension(); i++) {
            double x = input.getReal(i);
            sumsq += Math.sin(x) * Math.pow(Math.sin(((i+1) * x * x)/Math.PI), 2*m);
        }

        return -sumsq;
    }

    /**
     * Get the current value of <code>M</code>.
     * @return The value of <code>M</code>.
     */
    public int getM() {
        return m;
    }

    /**
     * Set the value of <code>M</code>.
     * @param m The value to set.
     */
    public void setM(int m) {
        this.m = m;
    }
}
