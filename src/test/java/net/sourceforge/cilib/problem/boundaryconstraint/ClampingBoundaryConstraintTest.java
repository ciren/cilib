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

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

/**
 *
 */
public class ClampingBoundaryConstraintTest {

    @Test
    public void testEnforce() {
        Bounds bounds = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder = Vector.newBuilder();
        candidateSolutionBuilder.add(Real.valueOf(-6.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(3.0, bounds));
        candidateSolutionBuilder.add(Real.valueOf(6.0, bounds));

        Individual i = new Individual();
        i.setCandidateSolution(candidateSolutionBuilder.build());

        ClampingBoundaryConstraint clampingBoundaryConstraint = new ClampingBoundaryConstraint();
        Entity enforcedEntity = clampingBoundaryConstraint.enforce(i);

        Vector solution = (Vector) enforcedEntity.getCandidateSolution();
        Assert.assertThat(solution.doubleValueOf(0), is(-5.0));
        Assert.assertThat(solution.doubleValueOf(1), is(3.0));
        Assert.assertThat(solution.doubleValueOf(2), is(5.0 - Maths.EPSILON));
    }

    @Test
    public void integerUpperBound() {
        Individual individual = new Individual();
        individual.setCandidateSolution(Vector.newBuilder().addWithin(5, new Bounds(0, 4)).build());

        ClampingBoundaryConstraint clampingBoundaryConstraint = new ClampingBoundaryConstraint();
        Entity enforcedEntity = clampingBoundaryConstraint.enforce(individual);

        Assert.assertThat(((Vector) enforcedEntity.getCandidateSolution()).intValueOf(0), is(4));
    }
}
