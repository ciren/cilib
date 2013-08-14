/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering.iterationstrategies;

import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.DataClusteringPSO;
import net.sourceforge.cilib.clustering.SlidingWindow;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.QuantisationErrorMinimisationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.distancemeasure.EuclideanDistanceMeasure;
import static org.junit.Assert.*;
import org.junit.Test;

public class SinglePopulationDataClusteringIterationStrategyTest {

    /**
     * Test of performIteration method, of class StandardDataClusteringIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        SlidingWindow window = new SlidingWindow();
        window.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setWindow(window);
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setIterationStrategy(strategy);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        instance.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        instance.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }

    /**
     * Test of setReinitialisationInterval method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetReinitialisationInterval() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        instance.setReinitialisationInterval(2);

        assertEquals(instance.getReinitialisationInterval(), 2);
    }
    /**
     * Test of getReinitialisationInterval method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testGetReinitialisationInterval() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        instance.setReinitialisationInterval(2);

        assertEquals(instance.getReinitialisationInterval(), 2);
    }

    /**
     * Test of setDimensions method, of class SinglePopulationDataClusteringIterationStrategy.
     */
    @Test
    public void testSetDimensions() {
        SinglePopulationDataClusteringIterationStrategy instance = new StandardDataClusteringIterationStrategy();
        instance.setDimensions(2);

        assertEquals(instance.dimensions, 2);
    }

}
