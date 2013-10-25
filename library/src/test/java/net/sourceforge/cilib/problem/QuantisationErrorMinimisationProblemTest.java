/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import junit.framework.Assert;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.io.StandardPatternDataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        holder.add(ClusterCentroid.of(5,6));
        holder.add(ClusterCentroid.of(5,-6));
        holder.add(ClusterCentroid.of(-5,6));
        particle.setCandidateSolution(holder);

        StandardPatternDataTable trainingSet = new StandardPatternDataTable();
        //patterns for first centroid
        trainingSet.addRow(new StandardPattern(Vector.of(5,6+1), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(5,6+2), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(5,6+3), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(5,6+4), Vector.of(0)));
        //patterns for second centroid
        trainingSet.addRow(new StandardPattern(Vector.of(5,-6-1), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(5,-6-2), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(5,-6-3), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(5,-6-4), Vector.of(0)));
        //patterns for third centroid
        trainingSet.addRow(new StandardPattern(Vector.of(-5-1,6), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(-5-2,6), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(-5-3,6), Vector.of(0)));
        trainingSet.addRow(new StandardPattern(Vector.of(-5-4,6), Vector.of(0)));

        SlidingWindow window = mock(SlidingWindow.class);
        when(window.slideWindow()).thenReturn(trainingSet);

        QuantisationErrorMinimisationProblem instance = new QuantisationErrorMinimisationProblem();
        instance.setWindow(window);

        Fitness fitness = instance.getFitness(particle.getCandidateSolution());

        Assert.assertEquals(fitness.getValue(), 2.5);
    }

    /**
     * Test of assignDataPatternsToParticle method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testAssignDataPatternsToParticle() {
        QuantisationErrorMinimisationProblem instance = new QuantisationErrorMinimisationProblem();
        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        window.setWindowSize(3);
        instance.setWindow(window);

        CentroidHolder candidateSolution = new CentroidHolder();
        candidateSolution.add(ClusterCentroid.of(1.25,1.1,1.3,1.9));
        candidateSolution.add(ClusterCentroid.of(1.92,2.6,3.1,1.8));
        candidateSolution.add(ClusterCentroid.of(0.9,1.1,0.85,0.79));

        instance.assignDataPatternsToParticle(candidateSolution);
        Assert.assertTrue(candidateSolution.get(0).getDataItems().contains(Vector.of(1.0,1.0,1.0,2.0)));
        Assert.assertTrue(candidateSolution.get(1).getDataItems().contains(Vector.of(2.0,3.0,4.0,2.0)));
        Assert.assertTrue(candidateSolution.get(2).getDataItems().contains(Vector.of(1.0,1.0,1.0,1.0)));
    }

}
