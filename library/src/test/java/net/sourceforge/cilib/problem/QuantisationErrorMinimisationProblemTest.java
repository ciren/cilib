/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.problem;

import junit.framework.Assert;
import net.cilib.clustering.entity.ClusterParticle;
import net.cilib.entity.EntityType;
import net.cilib.problem.solution.Fitness;
import net.cilib.problem.solution.MinimisationFitness;
import net.cilib.type.types.container.CentroidHolder;
import net.cilib.type.types.container.ClusterCentroid;
import org.junit.Test;

public class QuantisationErrorMinimisationProblemTest {

    /**
     * Test of calculateFitness method, of class QuantisationErrorMinimisationProblem.
     */
    @Test
    public void testCalculateFitness() {
        ClusterParticle particle = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(12.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(12.0));
        ClusterCentroid centroid = ClusterCentroid.of(1,2,3,4,5,6);
        centroid.setDataItemDistances(new double[]{1,2,3,4});
        holder.add(centroid);
        holder.add(centroid);
        holder.add(centroid);
        particle.setCandidateSolution(holder);

        QuantisationErrorMinimisationProblem instance = new QuantisationErrorMinimisationProblem();
        Fitness fitness = instance.getFitness(particle.getCandidateSolution());

        Assert.assertEquals(fitness.getValue(), 2.5);
    }


}
