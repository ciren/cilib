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
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * If a particle oversteps the upper boundary it gets re-initialised and placed near the lower
 * boundary and vice versa.
 * </p>
 * <p>
 * References:
 * </p>
 * <pre>
 * {@literal @}inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi",
 *                      title = "Handling boundary constraints for numerical optimization by
 *                      particle swarm flying in periodic search space",
 *                      booktitle = "IEEE Congress on Evolutionary Computation", month = jun,
 *                      year = {2004}, volume = "2", pages = {2307--2311} }
 * </pre>
 * @author Wiehann Matthysen
 */
public class PeriodicBoundaryConstraint implements BoundaryConstraint {

    private static final long serialVersionUID = 6381401553771951793L;

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
        StructuredType<?> velocity = (StructuredType<?>) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (velocity == null) {
            throw new UnsupportedOperationException("Cannot apply a ["
                    + this.getClass().getSimpleName()
                    + "] to an Entity that is not a Particle");
        }

        Vector.Builder positionBuilder = Vector.newBuilder();

        Iterator<?> i = entity.getCandidateSolution().iterator();
        Iterator<?> velocityIterator = velocity.iterator();

        for (; i.hasNext();) {
            Numeric v = (Numeric) velocityIterator.next();
            Numeric p = (Numeric) i.next();
            Bounds bounds = p.getBounds();
            double desiredPosition = p.doubleValue() + v.doubleValue();

            if (Double.compare(p.doubleValue(), bounds.getLowerBound()) < 0) {
                positionBuilder.add(bounds.getUpperBound() - (bounds.getLowerBound() - desiredPosition) % bounds.getRange());
            } else if (Double.compare(p.doubleValue(), bounds.getUpperBound()) > 0) {
                positionBuilder.add(bounds.getLowerBound() + (desiredPosition - bounds.getUpperBound()) % bounds.getRange());
            } else {
                positionBuilder.add(p);
            }
        }
        entity.getProperties().put(EntityType.CANDIDATE_SOLUTION, positionBuilder.build());
    }
}
