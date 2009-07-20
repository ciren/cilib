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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The Himmelblau function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * </ul>
 *
 * @author Clive Naicker
 * @version 1.0
 */
public class Himmelblau extends ContinuousFunction {
    private static final long serialVersionUID = 7323733640884766707L;

    /**
     * Create an instance of the Himmelblau function.
     * The initial domain is set to R(-6.0, 6.0)^2
     */
    public Himmelblau() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-6, 6)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Himmelblau getClone() {
        return new Himmelblau();
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    public Double evaluate(Vector input) {
        double x = input.getReal(0);
        double y = input.getReal(1);

        return -Math.pow((x*x + y -11), 2) + Math.pow((x + y*y -7), 2);
    }

}
