/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.clustering.iterationstrategies;

import junit.framework.Assert;
import net.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.cilib.algorithm.population.IterationStrategy;
import net.cilib.clustering.CooperativePSO;
import net.cilib.clustering.DataClusteringPSO;
import net.cilib.clustering.entity.ClusterParticle;
import net.cilib.math.random.generator.Rand;
import net.cilib.measurement.generic.Iterations;
import net.cilib.problem.QuantisationErrorMinimisationProblem;
import net.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.cilib.stoppingcondition.Maximum;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import org.junit.Test;


public class CooperativeDataClusteringPSOIterationStrategyTest {

    /**
     * Test of performIteration method, of class CooperativeDataClusteringPSOIterationStrategy.
     */
    @Test
    public void testPerformIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        DataClusteringPSO instance2 = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem2 = new QuantisationErrorMinimisationProblem();
        problem2.setDomain("R(-5.12:5.12)");
        CentroidBoundaryConstraint constraint2 = new CentroidBoundaryConstraint();
        constraint2.setDelegate(new RandomBoundaryConstraint());
        instance2.setOptimisationProblem(problem2);
        DataDependantPopulationInitialisationStrategy init2 = new DataDependantPopulationInitialisationStrategy();

        init2.setEntityType(new ClusterParticle());
        init2.setEntityNumber(2);
        instance2.setInitialisationStrategy(init2);
        instance2.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance2.setOptimisationProblem(problem2);
        instance2.addStoppingCondition(new MeasuredStoppingCondition());

        DataClusteringPSO instance3 = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem3 = new QuantisationErrorMinimisationProblem();
        problem3.setDomain("R(-5.12:5.12)");
        CentroidBoundaryConstraint constraint3 = new CentroidBoundaryConstraint();
        constraint3.setDelegate(new RandomBoundaryConstraint());
        instance3.setOptimisationProblem(problem3);
        DataDependantPopulationInitialisationStrategy init3 = new DataDependantPopulationInitialisationStrategy();

        init3.setEntityType(new ClusterParticle());
        init3.setEntityNumber(2);
        instance3.setInitialisationStrategy(init3);
        instance3.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance3.setOptimisationProblem(problem3);
        instance3.addStoppingCondition(new MeasuredStoppingCondition());

        CooperativePSO cooperative = new CooperativePSO();
        cooperative.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        cooperative.addPopulationBasedAlgorithm(instance);
        cooperative.addPopulationBasedAlgorithm(instance2);
        cooperative.addPopulationBasedAlgorithm(instance3);
        cooperative.setOptimisationProblem(problem);

        cooperative.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        cooperative.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }

    /**
     * Test of reinitialiseContext method, of class CooperativeDataClusteringPSOIterationStrategy.
     */
    @Test
    public void testReinitialiseContext() {
        Rand.setSeed(0);
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
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
        cooperative.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 30));
        cooperative.addPopulationBasedAlgorithm(instance);
        cooperative.setOptimisationProblem(problem);

        cooperative.performInitialisation();

        ClusterParticle particleBefore = instance.getTopology().head().getClone();

        cooperative.run();

        ClusterParticle particleAfter = instance.getTopology().head().getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }
}
