/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa.bees;

import com.google.common.collect.Iterables;
import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Rastrigin;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;

import org.junit.Test;

public class WorkerBeeTest {

    @Test
    public void testSetForageLimit() {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-10.048:10.048)^100");
        ContinuousFunction func = new Rastrigin();
        problem.setFunction(func);

        ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
        initStrategy.setEntityNumber(100);
        WorkerBee clone = new WorkerBee();
        clone.setForageLimit(ConstantControlParameter.of(680));
        initStrategy.setEntityType(clone);
        Topology<WorkerBee> population = new GBestTopology<WorkerBee>();
        Iterables.addAll(population, initStrategy.initialise(problem));

        for (WorkerBee bee : population) {
            assertEquals(bee.getForageLimit().getParameter(), 680.0, 0.0001);
        }
    }

}
