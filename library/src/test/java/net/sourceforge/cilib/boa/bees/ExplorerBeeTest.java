/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.boa.bees;

import net.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.cilib.boa.ABC;
import net.cilib.boa.bee.ExplorerBee;
import net.cilib.boa.bee.WorkerBee;
import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.functions.continuous.unconstrained.Rastrigin;
import net.cilib.measurement.generic.Iterations;
import net.cilib.problem.FunctionOptimisationProblem;
import net.cilib.stoppingcondition.Maximum;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.cilib.stoppingcondition.StoppingCondition;
import net.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExplorerBeeTest {

    private ABC abc;

    @Before
    public void setUp() throws Exception {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-5.0:5.0)^5");
        problem.setFunction(new Rastrigin());

        StoppingCondition condition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 2);

        abc = new ABC();
        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(10);
        WorkerBee bee = new WorkerBee();
        initStrategy.setEntityType(bee);
        abc.setForageLimit(ConstantControlParameter.of(-1));
        abc.setInitialisationStrategy(initStrategy);
        abc.setWorkerBeePercentage(ConstantControlParameter.of(0.5));
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.performInitialisation();
    }

    @Test
    public void testSearchAllowed() {
        //get up a position with bounds
        Vector oldPosition = Vector.copyOf(abc.getWorkerBees().head().getPosition());

        //update position with explorer bee
        ExplorerBee explorerBee = abc.getExplorerBee();
        Assert.assertTrue(explorerBee.searchAllowed(1));
        explorerBee.getNewPosition(1, oldPosition);
        //only one update is allowed for the same iteration, this must therefore be false...
        Assert.assertTrue(!explorerBee.searchAllowed(1));
        //and this true.
        Assert.assertTrue(explorerBee.searchAllowed(2));
    }

    @Test
    public void testGetNewPosition() {
        //get up a position with bounds
        Vector oldPosition = Vector.copyOf(abc.getWorkerBees().head().getPosition());
        //update position with explorer bee
        ExplorerBee explorerBee = abc.getExplorerBee();
        Vector newPosition = explorerBee.getNewPosition(1, oldPosition);
        Assert.assertTrue(!oldPosition.equals(newPosition));
    }
}
