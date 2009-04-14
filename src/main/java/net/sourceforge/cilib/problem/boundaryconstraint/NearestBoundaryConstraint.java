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

package net.sourceforge.cilib.problem.boundaryconstraint;

import java.util.Iterator;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.MathUtil;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * If a <b>particle</b> oversteps the boundary it gets re-initialised and placed on the overstepped
 * boundary. A terbulence probability gets specified to allow particles to escape the boundaries.
 * References:
 * @inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi", title = "Handling
 *                       boundary constraints for numerical optimization by particle swarm flying in
 *                       periodic search space", booktitle = "IEEE Congress on Evolutionary
 *                       Computation", month = jun, year = {2004}, volume = "2", pages =
 *                       {2307--2311} }
 * @inproceedings{HW07, author = "S. Helwig and R. Wanka", title = "Particle Swarm Optimization in
 *                      High-Dimensional Bounded Search Spaces", booktitle = "Proceedings of the
 *                      2007 IEEE Swarm Intelligence Symposium", month = apr, year = {2007}, pages =
 *                      {198--205} }
 * @author Wiehann Matthysen
 */
public class NearestBoundaryConstraint implements BoundaryConstraint {
    private static final long serialVersionUID = 3177150919194273857L;

    private ControlParameter turbulenceProbability;

    public NearestBoundaryConstraint() {
        turbulenceProbability = new ConstantControlParameter(0.0);
    }

    @Override
    public BoundaryConstraint getClone() {
        return this;
    }

    @Override
    public void enforce(Entity entity) {
        StructuredType s = (StructuredType) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (s == null)
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a "
                + entity.getClass().getSimpleName());

        Iterator pIterator = entity.getCandidateSolution().iterator();
        Iterator vIterator = s.iterator();

        while (pIterator.hasNext()) {
            Numeric position = (Numeric) pIterator.next();
            Numeric velocity = (Numeric) vIterator.next();
            Bounds bounds = position.getBounds();

            double previousPosition = position.getReal();

            if (Double.compare(position.getReal(), bounds.getLowerBound()) < 0) {
                position.set(bounds.getLowerBound());    // lower boundary is inclusive

                if (MathUtil.random() < turbulenceProbability.getParameter()) {
                    position.set(position.getReal() + MathUtil.random() * bounds.getRange());
                }
                velocity.set(position.getReal() - previousPosition);
            }
            else if (Double.compare(position.getReal(), bounds.getUpperBound()) > 0) {
                position.set(bounds.getUpperBound() - Double.MIN_NORMAL);    // upper boundary is exclusive
                if (MathUtil.random() < turbulenceProbability.getParameter()) {
                    position.set(position.getReal() - MathUtil.random() * bounds.getRange());
                }
                velocity.set(position.getReal() - previousPosition);
            }
        }
    }

    /**
     * Get the {@linkplain ControlParameter} representing the current turbulence probability.
     * @return The turbulence {@linkplain ControlParameter}.
     */
    public ControlParameter getTurbulenceProbability() {
        return turbulenceProbability;
    }

    /**
     * Set the turbulence probability with the provided {@linkplain ControlParameter}.
     * @param turbulenceProbability The value to set.
     */
    public void setTurbulenceProbability(ControlParameter turbulenceProbability) {
        this.turbulenceProbability = turbulenceProbability;
    }

}
