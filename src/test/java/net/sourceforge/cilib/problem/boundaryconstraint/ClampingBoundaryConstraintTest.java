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
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Int;
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
        Vector candidateSolution = new Vector();

        Bounds bounds = new Bounds(-5.0, 5.0);
        Real r1 = new Real(-6.0, bounds);
        Real r2 = new Real(3.0, bounds);
        Real r3 = new Real(6.0, bounds);

        candidateSolution.add(r1);
        candidateSolution.add(r2);
        candidateSolution.add(r3);

        Individual i = new Individual();
        i.setCandidateSolution(candidateSolution);

        ClampingBoundaryConstraint clampingBoundaryConstraint = new ClampingBoundaryConstraint();
        clampingBoundaryConstraint.enforce(i);

        Assert.assertThat(((Real)candidateSolution.get(0)).getReal(), is(-5.0));
        Assert.assertThat(((Real)candidateSolution.get(1)).getReal(), is(3.0));
        Assert.assertThat(((Real)candidateSolution.get(2)).getReal(), is(5.0-Maths.EPSILON));
    }

    @Test
    public void integerUpperBound() {
        Int i = new Int(5, new Bounds(0, 4));

        Vector candidateSolution = new Vector();
        candidateSolution.add(i);

        Individual individual = new Individual();
        individual.setCandidateSolution(candidateSolution);

        ClampingBoundaryConstraint clampingBoundaryConstraint = new ClampingBoundaryConstraint();
        clampingBoundaryConstraint.enforce(individual);

        Assert.assertThat(i.getInt(), is(4));
    }

}