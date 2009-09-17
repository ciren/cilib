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
 * <p><b>The Generalized Ackley.</b></p>
 *
 * <p><b>Reference:</b> T.Back, <i>Evolutionary Algorithms in Theory and Practice</i>,
 * Oxford University Press, 1996</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-32.768,32.768]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * @author Edwin Peer
 * @author Olusegun Olorunda
 *
 */
public class Ackley extends ContinuousFunction {
    private static final long serialVersionUID = -7803711986955989075L;

    public Ackley() {
        setDomain("R(-32.768, 32.768)^30");
    }

    @Override
    public Ackley getClone() {
        return new Ackley();
    }

    public Double getMinimum() {
        return 0.0;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.functions.redux.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
     */
    @Override
    public Double evaluate(Vector input) {
         double sumsq = 0.0;
            double sumcos = 0.0;
            for (int i = 0; i < getDimension(); ++i) {
                sumsq += input.getReal(i) * input.getReal(i);
                sumcos += Math.cos(2 * Math.PI * input.getReal(i));
            }
            return -20.0 * Math.exp(-0.2 * Math.sqrt(sumsq / getDimension())) - Math.exp(sumcos / getDimension()) + 20 + Math.E;
    }

}
