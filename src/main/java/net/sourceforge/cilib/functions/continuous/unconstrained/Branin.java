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

import java.io.Serializable;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>Booth Function</b></p>
 *
 * <p><b>Reference:</b> X. Yao, Y. Liu, G. Liu, <i>Evolutionary Programming Made Faster</i>,
 * IEEE Transactions on Evolutionary Computation, 3(1):82--102, 1999</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0.397887 </li>
 * <li> <b>x</b>* = (-&pi;, 12.275), (&pi;, 2.275), (9.425, 2.425)</li>
 * <li> for x<sub>1</sub> in [-5,10], x<sub>2</sub> in [0,15]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Has 3 global minima</li>
 * <li>Unimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * @author Clive Naicker
 *
 */
public class Branin extends ContinuousFunction implements Serializable {
    private static final long serialVersionUID = -2254223453957430344L;

    private double a = 1.0;
    private double b = 5.1/(4*Math.PI*Math.PI);
    private double c = 5.0/Math.PI;
    private double d = 6.0;
    private double e = 10.0;
    private double f = 1.0/(8.0*Math.PI);

    /**
     * Create a new instance of {@linkplain Branin}. Domain defaults to: <code>R(-5,10),R(0,15)</code>
     */
    public Branin() {
        a = 1.0;
        b = 5.1/(4*Math.PI*Math.PI);
        c = 5.0/Math.PI;
        d = 6.0;
        e = 10.0;
        f = 1.0/(8.0*Math.PI);

        //constraint.add(new DimensionValidator(2));
        //constraint.add(new ContentValidator(0, new QuantitativeBoundValidator(new Double(-5), new Double(15))));
        //constraint.add(new ContentValidator(1, new QuantitativeBoundValidator(new Double(0), new Double(15))));

        setDomain("R(-5,10),R(0,15)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Branin getClone() {
        return new Branin();
    }

    /**
     * {@inheritDoc}
     *
     * The minimum is located at <code>0.397887</code>.
     */
    public Double getMinimum() {
        return 0.397887;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double x1 = input.getReal(0);
        double x2 = input.getReal(1);

        return a*Math.pow((x2 - b*x1*x1 + c*x1 - d), 2) + e*(1 - f)*Math.cos(x1) + e;
    }

}
