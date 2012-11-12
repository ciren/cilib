/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa.positionupdatestrategies;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.stoppingcondition.Maximum;
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VisualPositionUpdateStategyTest {

    private ABC abc;

    @Before
    public void setUp() throws Exception {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setFunction(new Rastrigin());
        problem.setDomain("R(-5.0:5.0)^10");

        StoppingCondition condition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 2);

        abc = new ABC();
        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(10);
        WorkerBee bee = new WorkerBee();
        initStrategy.setEntityType(bee);
        abc.setInitialisationStrategy(initStrategy);
        abc.setWorkerBeePercentage(ConstantControlParameter.of(0.5));
        abc.setForageLimit(ConstantControlParameter.of(Integer.MAX_VALUE));
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.performInitialisation();
    }

    @Test
    public void testUpdatePosition() {
        HoneyBee bee = abc.getWorkerBees().get(0);
        abc.performIteration();
        Fitness oldFitness = bee.getFitness().getClone();
        abc.performIteration();
        Vector currentPosition = bee.getPosition();
        Assert.assertEquals(10, currentPosition.size());
        for (int i = 0; i < currentPosition.size(); i++) {
            Assert.assertTrue(((Real) currentPosition.get(i)).doubleValue() != Double.NaN);
            Assert.assertTrue(!Double.isInfinite(((Real) currentPosition.get(i)).doubleValue()));
        }
        Fitness newFitness = bee.getFitness();
        Assert.assertTrue(newFitness.compareTo(oldFitness) >= 0);
    }
}
