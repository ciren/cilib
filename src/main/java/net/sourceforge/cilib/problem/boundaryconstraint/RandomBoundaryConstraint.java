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

import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Random;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Types;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * <p>
 * If a particle oversteps the boundary it gets randomly re-initialised within the boundary and its
 * velocity gets updated.
 * </p>
 * <p>
 * References:
 * </p>
 * <pre>
 * &nbsp;@inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi",
 *                      title = "Handling boundary constraints for numerical optimization by
 *                      particle swarm flying in periodic search space",
 *                      booktitle = "IEEE Congress on Evolutionary Computation", month = jun,
 *                      year = {2004}, volume = "2", pages = {2307--2311} }
 * &nbsp;@inproceedings{HW07, author = "S. Helwig and R. Wanka",
 *                      title = "Particle Swarm Optimization in High-Dimensional Bounded Search Spaces",
 *                      booktitle = "Proceedings of the 2007 IEEE Swarm Intelligence Symposium", month = apr,
 *                      year = {2007}, pages = {198--205} }
 * </pre>
 * @author Wiehann Matthysen
 */
public class RandomBoundaryConstraint implements BoundaryConstraint {
    private static final long serialVersionUID = -4090871319456989303L;

    private Random random;

    public RandomBoundaryConstraint() {
        this.random = new MersenneTwister();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundaryConstraint getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        StructuredType velocity = (StructuredType) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (velocity == null) {
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a "
                + entity.getClass().getSimpleName());
        }

        Iterator pIterator = entity.getCandidateSolution().iterator();
        Iterator vIterator = velocity.iterator();

        while (pIterator.hasNext()) {
            Numeric pos = (Numeric) pIterator.next();
            Numeric vel = (Numeric) vIterator.next();
            Bounds bounds = pos.getBounds();

            if (Double.compare(pos.getReal(), bounds.getLowerBound()) < 0) {
                constrain(pos, vel);
            } else if (Double.compare(pos.getReal(), bounds.getUpperBound()) > 0) {
                constrain(pos, vel);
            }
        }
    }

    /**
     * Constrain the position.
     * @param position The {@linkplain Numeric} representing the position.
     * @param velocity The {@linkplain Numeric} representing the velocity.
     */
    private void constrain(Numeric position, Numeric velocity) {
        Numeric previousPosition = position.getClone();
        position.randomize(random);
        velocity.set(position.getReal() - previousPosition.getReal());
    }
}
