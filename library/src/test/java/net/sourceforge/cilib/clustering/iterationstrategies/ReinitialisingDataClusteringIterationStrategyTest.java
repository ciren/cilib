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
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.problem.QuantisationErrorMinimisationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Test;

public class ReinitialisingDataClusteringIterationStrategyTest {

    /**
     * Test of algorithmIteration method, of class ReinitialisingDataClusteringIterationStrategy.
     */
    @Test
    public void testAlgorithmIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new ReinitialisingDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setIterationStrategy(strategy);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        instance.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().get(0).getClone();

        instance.run();

        ClusterParticle particleAfter = instance.getTopology().get(0).getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }

    /**
     * Test of getDelegate method, of class ReinitialisingDataClusteringIterationStrategy.
     */
    @Test
    public void testGetDelegate() {
        ReinitialisingDataClusteringIterationStrategy instance = new ReinitialisingDataClusteringIterationStrategy();
        StandardDataClusteringIterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        instance.setDelegate(strategy);

        Assert.assertEquals(strategy, instance.getDelegate());
    }

    /**
     * Test of setDelegate method, of class ReinitialisingDataClusteringIterationStrategy.
     */
    @Test
    public void testSetDelegate() {
        ReinitialisingDataClusteringIterationStrategy instance = new ReinitialisingDataClusteringIterationStrategy();
        StandardDataClusteringIterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        instance.setDelegate(strategy);

        Assert.assertEquals(strategy, instance.getDelegate());
    }
}
