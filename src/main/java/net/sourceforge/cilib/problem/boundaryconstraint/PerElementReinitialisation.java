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
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Types;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Reinitialise each element within the provided {@code Type} element if
 * it is no longer within the valid search space. Each element violating the
 * condition will be reinitilaised witin the domain of the problem (search space).
 *
 * @see Types#isInsideBounds(net.sourceforge.cilib.type.types.Type)
 */
public class PerElementReinitialisation extends ReinitialisationBoundary {
    private static final long serialVersionUID = 7080824227269710787L;

    private Random random;

    public PerElementReinitialisation() {
        this.random = new MersenneTwister();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        try {
            enforce((Vector) entity.getCandidateSolution());
        }
        catch (ClassCastException cce) {
            enforce((Numeric) entity.getCandidateSolution());
        }
    }

    /**
     * This method only randomises those elements inside the given {@linkplain Type} object that are out of bounds.
     * NOTE: This method is recursive so that it can handle <tt>Vectors</tt> inside <tt>Vectors</tt>.
     * @param type the {@linkplain Type} object whose individual elements should be randomised if they are out of bounds
     */
    private void enforce(Vector vector) {
        for (Type element : vector) {
            try {
                enforce((Numeric) element);
            }
            catch (ClassCastException cce) {
                enforce((Vector) element);
            }
        }
    }

    private void enforce(Numeric numeric) {
        if (!Types.isInsideBounds(numeric)) {
            numeric.randomize(random);
        }
    }
}
