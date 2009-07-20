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
 * <p><b>The Egg Holder.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle
 * Swarm Methods</i>, Technical Report, North-Eastern Hill University,
 * India, 2006</p>
 *
 * <p>Note: n <= 2</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> f(<b>x</b>*) approx -959.64 </li>
 * <li> <b>x</b>* = (512,404.2319) for n=2</li>
 * <li> for x_i in [-512,512]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Non-seperable</li>
 * <li>Not regular</li>
 * </ul>
 * </p>
 *
 * @author gpampara
 */
class EggHolder extends ContinuousFunction {

    private static final long serialVersionUID = 358993985066821115L;

    public EggHolder() {
        this.setDomain("R(-512,512)^30");
    }

    @Override
    public ContinuousFunction getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        return -959.64;
     }

    @Override
    public Double evaluate(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < input.getDimension() - 1; i++) {
            sum += (-1*(input.getReal(i+1) + 47)
                    *Math.sin(Math.sqrt(Math.abs(input.getReal(i+1) + input.getReal(i)/2 + 47)))
                    + Math.sin(Math.sqrt(Math.abs(input.getReal(i) - (input.getReal(i+1)+47))))
                    *(-1*input.getReal(i)));
        }
        return sum;
    }

}
