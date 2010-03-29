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

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class ClampingBoundaryConstraintTest {

    @Test
    public void testEnforce() {
        Bounds bounds = new Bounds(-5.0, 5.0);
        Vector.Builder candidateSolutionBuilder = Vector.newBuilder();
        candidateSolutionBuilder.add(new Real(-6.0, bounds));
        candidateSolutionBuilder.add(new Real(3.0, bounds));
        candidateSolutionBuilder.add(new Real(6.0, bounds));

        Individual i = new Individual();
        i.setCandidateSolution(candidateSolutionBuilder.build());

        ClampingBoundaryConstraint clampingBoundaryConstraint = new ClampingBoundaryConstraint();
        clampingBoundaryConstraint.enforce(i);

        Vector solution = (Vector) i.getCandidateSolution();
        Assert.assertThat(solution.doubleValueOf(0), is(-5.0));
        Assert.assertThat(solution.doubleValueOf(1), is(3.0));
        Assert.assertThat(solution.doubleValueOf(2), is(5.0 - Maths.EPSILON));
    }

    @Test
    public void integerUpperBound() {
        Individual individual = new Individual();
        individual.setCandidateSolution(Vector.newBuilder().addWithin(5, new Bounds(0, 4)).build());

        ClampingBoundaryConstraint clampingBoundaryConstraint = new ClampingBoundaryConstraint();
        clampingBoundaryConstraint.enforce(individual);

        Assert.assertThat(((Vector) individual.getCandidateSolution()).intValueOf(0), is(4));
    }
}
