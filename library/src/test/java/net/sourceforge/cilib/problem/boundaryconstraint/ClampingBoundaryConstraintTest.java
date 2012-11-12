/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
