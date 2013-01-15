/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.clustering;

import java.util.ArrayList;
import junit.framework.Assert;
import net.sourceforge.cilib.algorithm.initialisation.DataDependantPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.clustering.entity.ClusterParticle;
import net.sourceforge.cilib.clustering.iterationstrategies.SinglePopulationDataClusteringIterationStrategy;
import net.sourceforge.cilib.clustering.iterationstrategies.StandardDataClusteringIterationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.QuantisationErrorMinimisationProblem;
import net.sourceforge.cilib.problem.boundaryconstraint.CentroidBoundaryConstraint;
import net.sourceforge.cilib.problem.boundaryconstraint.RandomBoundaryConstraint;
import net.sourceforge.cilib.problem.solution.MinimisationFitness;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.type.types.container.CentroidHolder;
import net.sourceforge.cilib.type.types.container.ClusterCentroid;
import org.junit.Test;

public class CooperativePSOTest {

    /**
     * Test of algorithmIteration method, of class CooperativePSO.
     */
    @Test
    public void testAlgorithmIteration() {
        DataClusteringPSO instance = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint = new CentroidBoundaryConstraint();
        constraint.setDelegate(new RandomBoundaryConstraint());
        strategy.setBoundaryConstraint(constraint);
        instance.setOptimisationProblem(problem);
        DataDependantPopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();

        init.setEntityType(new ClusterParticle());
        init.setEntityNumber(2);
        instance.setInitialisationStrategy(init);
        instance.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance.setOptimisationProblem(problem);
        instance.addStoppingCondition(new MeasuredStoppingCondition());

        DataClusteringPSO instance2 = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem2 = new QuantisationErrorMinimisationProblem();
        problem2.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy2 = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint2 = new CentroidBoundaryConstraint();
        constraint2.setDelegate(new RandomBoundaryConstraint());
        strategy2.setBoundaryConstraint(constraint2);
        instance2.setOptimisationProblem(problem2);
        DataDependantPopulationInitialisationStrategy init2 = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();

        init2.setEntityType(new ClusterParticle());
        init2.setEntityNumber(2);
        instance2.setInitialisationStrategy(init2);
        instance2.setSourceURL("library/src/test/resources/datasets/iris2.arff");

        instance2.setOptimisationProblem(problem2);
        instance2.addStoppingCondition(new MeasuredStoppingCondition());

        DataClusteringPSO instance3 = new DataClusteringPSO();

        QuantisationErrorMinimisationProblem problem3 = new QuantisationErrorMinimisationProblem();
        problem3.setDomain("R(-5.12:5.12)");
        IterationStrategy strategy3 = new StandardDataClusteringIterationStrategy();
        CentroidBoundaryConstraint constraint3 = new CentroidBoundaryConstraint();
        constraint3.setDelegate(new RandomBoundaryConstraint());
        strategy3.setBoundaryConstraint(constraint3);
        instance3.setOptimisationProblem(problem3);
        DataDependantPopulationInitialisationStrategy init3 = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();

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

        ClusterParticle particleBefore = instance.getTopology().get(0).getClone();

        cooperative.run();

        ClusterParticle particleAfter = instance.getTopology().get(0).getClone();

        Assert.assertFalse(particleAfter.getCandidateSolution().containsAll(particleBefore.getCandidateSolution()));
    }

    /**
     * Test of getSolutions method, of class CooperativePSO.
     */
    @Test
    public void testGetSolutions() {
        DataClusteringPSO standard  = new DataClusteringPSO();
        ClusterParticle particle = new ClusterParticle();
        CentroidHolder holder = new CentroidHolder();
        holder.add(ClusterCentroid.of(1,2,3,4,5));
        holder.add(ClusterCentroid.of(5,4,3,2,1));
        particle.setCandidateSolution(holder);
        particle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.0));
        particle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(2.0));
        particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getCandidateSolution());
        particle.setNeighbourhoodBest(particle);
        standard.getTopology().add(particle);

        ClusterParticle otherParticle = new ClusterParticle();
        CentroidHolder otherHolder = new CentroidHolder();
        otherHolder.add(ClusterCentroid.of(6,7,8,9,10));
        otherHolder.add(ClusterCentroid.of(10,9,8,7,6));
        otherParticle.setCandidateSolution(otherHolder);
        otherParticle.getProperties().put(EntityType.FITNESS, new MinimisationFitness(1.0));
        otherParticle.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(1.0));
        otherParticle.getProperties().put(EntityType.Particle.BEST_POSITION, otherParticle.getCandidateSolution());
        otherParticle.setNeighbourhoodBest(otherParticle);
        standard.getTopology().add(otherParticle);

        DataClusteringPSO standard2  = new DataClusteringPSO();
        ClusterParticle particle2 = new ClusterParticle();
        CentroidHolder otherHolder2 = new CentroidHolder();
        otherHolder2.add(ClusterCentroid.of(3,2,3,4,5));
        otherHolder2.add(ClusterCentroid.of(5,10,3,7,1));
        particle2.setCandidateSolution(holder);
        particle2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(2.1));
        particle2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(2.1));
        particle2.getProperties().put(EntityType.Particle.BEST_POSITION, particle2.getCandidateSolution());
        particle2.setNeighbourhoodBest(particle2);
        standard2.getTopology().add(particle2);

        ClusterParticle otherParticle2 = new ClusterParticle();
        CentroidHolder holder2 = new CentroidHolder();
        holder2.add(ClusterCentroid.of(9,7,2,9,10));
        holder2.add(ClusterCentroid.of(11,9,5,7,6));
        otherParticle2.setCandidateSolution(holder2);
        otherParticle2.getProperties().put(EntityType.FITNESS, new MinimisationFitness(3.0));
        otherParticle2.getProperties().put(EntityType.Particle.BEST_FITNESS, new MinimisationFitness(3.0));
        otherParticle2.getProperties().put(EntityType.Particle.BEST_POSITION, otherParticle.getCandidateSolution());
        otherParticle2.setNeighbourhoodBest(otherParticle2);
        standard2.getTopology().add(otherParticle2);

        CooperativePSO cooperative = new CooperativePSO();
        cooperative.addPopulationBasedAlgorithm(standard);
        cooperative.addPopulationBasedAlgorithm(standard2);

        ArrayList<OptimisationSolution> list = (ArrayList<OptimisationSolution>) cooperative.getSolutions();
        ArrayList<CentroidHolder> holders = new ArrayList<CentroidHolder>();
        for(OptimisationSolution solution : list) {
            holders.add((CentroidHolder) solution.getPosition());
        }

        Assert.assertTrue(!list.isEmpty());
        boolean contains = false;
        for(CentroidHolder centroidHolder : holders) {
            if(centroidHolder.containsAll((CentroidHolder) otherParticle.getCandidateSolution())){
                contains = true;
            }
        }

        Assert.assertTrue(contains);

        contains = false;
        for(CentroidHolder centroidHolder : holders) {
            if(centroidHolder.containsAll((CentroidHolder) particle2.getCandidateSolution())){
                contains = true;
            }
        }

        Assert.assertTrue(contains);

    }


    /**
     * Test of performInitialisation method, of class CooperativePSO.
     */
    @Test
    public void testPerformInitialisation() {
        DataClusteringPSO standard = new DataClusteringPSO();
        QuantisationErrorMinimisationProblem problem = new QuantisationErrorMinimisationProblem();
        problem.setDomain("R(-5.12:5.12)");
        standard.setOptimisationProblem(problem);
        standard.addStoppingCondition(new MeasuredStoppingCondition(new Iterations(), new Maximum(), 1));
        PopulationInitialisationStrategy init = new DataDependantPopulationInitialisationStrategy<ClusterParticle>();
        init.setEntityType(new ClusterParticle());
        standard.setInitialisationStrategy(init);
        standard.setSourceURL("library/src/test/resources/datasets/iris2.arff");
        standard.performInitialisation();

        CooperativePSO instance = new CooperativePSO();
        instance.addPopulationBasedAlgorithm(standard);

        Assert.assertTrue(((SinglePopulationDataClusteringIterationStrategy)((DataClusteringPSO) instance.getPopulations().get(0)).getIterationStrategy()).getDataset().size() > 0);
        Assert.assertTrue(!instance.getPopulations().isEmpty());
        Assert.assertTrue(!instance.getPopulations().get(0).getTopology().isEmpty());
    }

    /**
     * Test of setIterationStrategy method, of class CooperativePSO.
     */
    @Test
    public void testSetIterationStrategy() {
       CooperativePSO cooperative = new CooperativePSO();
       IterationStrategy strategy = new StandardDataClusteringIterationStrategy();

       cooperative.setIterationStrategy(strategy);

       Assert.assertEquals(strategy, cooperative.getIterationStrategy());
    }

    /**
     * Test of getIterationStrategy method, of class CooperativePSO.
     */
    @Test
    public void testGetIterationStrategy() {
       CooperativePSO cooperative = new CooperativePSO();
       IterationStrategy strategy = new StandardDataClusteringIterationStrategy();

       cooperative.setIterationStrategy(strategy);

       Assert.assertEquals(strategy, cooperative.getIterationStrategy());
    }
}
