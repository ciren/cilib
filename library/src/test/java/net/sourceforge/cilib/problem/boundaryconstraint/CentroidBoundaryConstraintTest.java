/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.Assert;
import org.junit.Test;

public class CentroidBoundaryConstraintTest {
    
    /**
     * Test of enforce method, of class CentroidBoundaryConstraint.
     */
    @Test
    public void testEnforce() {
        Bounds bounds = new Bounds(-5.0, 5.0);
        CentroidHolder candidateSolutionBuilder = new CentroidHolder();
        candidateSolutionBuilder.add(ClusterCentroid.of(Real.valueOf(-6.0, bounds)));
        candidateSolutionBuilder.add(ClusterCentroid.of(Real.valueOf(3.0, bounds)));
        candidateSolutionBuilder.add(ClusterCentroid.of(Real.valueOf(6.0, bounds)));

        ClusterParticle particle = new ClusterParticle();
        particle.setCandidateSolution(candidateSolutionBuilder);
        particle.getProperties().put(EntityType.Particle.VELOCITY, candidateSolutionBuilder);
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, candidateSolutionBuilder);

        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new ClampingBoundaryConstraint());
        constraint.enforce(particle);

        CentroidHolder solution = (CentroidHolder) particle.getCandidateSolution();
        Assert.assertTrue(solution.get(0).get(0).doubleValue() == -5.0);
        Assert.assertTrue(solution.get(1).get(0).doubleValue() == 3.0);
        Assert.assertTrue(solution.get(2).get(0).doubleValue() == (5.0 - Maths.EPSILON));
    }

    /**
     * Test of setDelegate method, of class CentroidBoundaryConstraint.
     */
    @Test
    public void testSetDelegate() {
        BoundaryConstraint constraint = new ReinitialisationBoundary();
        CentroidBoundaryConstraint instance = new CentroidBoundaryConstraint();
        instance.setDelegate(constraint);
        Assert.assertEquals(constraint, instance.delegate);
        
    }
}
