/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
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

    private RandomProvider random;

    public PerElementReinitialisation() {
        this.random = new MersenneTwister();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity enforce(Entity oldEntity) {
        Entity entity = oldEntity.getClone();
        try {
            enforce((Vector) entity.getCandidateSolution());
        }
        catch (ClassCastException cce) {
            enforce((Numeric) entity.getCandidateSolution());
        }
        
        return entity;
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
