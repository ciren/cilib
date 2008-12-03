/**
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
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author gpampara
 */
class EggHolder extends ContinuousFunction {

    public EggHolder() {
        this.setDomain("R(-512,512)^30");
    }

    @Override
    public ContinuousFunction getClone() {
        return this;
    }

    @Override
    public double evaluate(Vector x) {
        double sum = 0.0;
        for (int i = 0; i < x.getDimension() - 1; i++) {
            sum += (-1*(x.getReal(i+1) + 47)
                    *Math.sin(Math.sqrt(Math.abs(x.getReal(i+1) + x.getReal(i)/2 + 47)))
                    + Math.sin(Math.sqrt(Math.abs(x.getReal(i) - (x.getReal(i+1)+47))))
                    *(-1*x.getReal(i)));
        }
        return sum;
    }

}
