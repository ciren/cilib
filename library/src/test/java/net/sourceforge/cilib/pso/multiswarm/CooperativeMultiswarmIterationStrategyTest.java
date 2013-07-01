/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.multiswarm;

import junit.framework.Assert;
import net.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.cilib.algorithm.population.IterationStrategy;
import net.cilib.clustering.CooperativePSO;
import net.cilib.clustering.DataClusteringPSO;
import net.cilib.clustering.entity.ClusterParticle;
import net.cilib.clustering.iterationstrategies.CooperativeDataClusteringPSOIterationStrategy;
import net.cilib.measurement.generic.Iterations;
import net.cilib.problem.QuantisationErrorMinimisationProblem;
import net.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.cilib.stoppingcondition.Maximum;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Test;

public class CooperativeMultiswarmIterationStrategyTest {

    /**
     * Test of performIteration method, of class CooperativeMultiswarmIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new CooperativeDataClusteringPSOIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        CooperativePSO cooperative = new CooperativePSO();
        cooperative.setIterationStrategy(strategy);
        cooperative.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        cooperative.addPopulationBasedAlgorithm(instance);
        cooperative.setOptimisationProblem(problem);

        cooperative.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        cooperative.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));

    }

    /**
     * Test of setDelegate method, of class CooperativeMultiswarmIterationStrategy.
     */
    @Test
    public void testSetDelegate() {
        IterationStrategy newDelegate = new CooperativeDataClusteringPSOIterationStrategy();
        CooperativeMultiswarmIterationStrategy instance = new CooperativeMultiswarmIterationStrategy();
        instance.setDelegate(newDelegate);

        Assert.assertEquals(newDelegate, instance.getDelegate());
    }

    /**
     * Test of getDelegate method, of class CooperativeMultiswarmIterationStrategy.
     */
    @Test
    public void testGetDelegate() {
        IterationStrategy newDelegate = new CooperativeDataClusteringPSOIterationStrategy();
        CooperativeMultiswarmIterationStrategy instance = new CooperativeMultiswarmIterationStrategy();
        instance.setDelegate(newDelegate);

        Assert.assertEquals(newDelegate, instance.getDelegate());
    }
}
