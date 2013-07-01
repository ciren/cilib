/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.boa;

import java.util.HashMap;
import net.cilib.boa.bee.HoneyBee;
import net.cilib.controlparameter.ConstantControlParameter;
import net.cilib.functions.continuous.unconstrained.Ackley;
import net.cilib.measurement.generic.Iterations;
import net.cilib.problem.FunctionOptimisationProblem;
import net.cilib.stoppingcondition.Maximum;
import net.cilib.stoppingcondition.MeasuredStoppingCondition;
import net.cilib.type.types.Type;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ABCTest {

    @Test
    public void testPerformInitialisation() {
        FunctionOptimisationProblem problem = new FunctionOptimisationProblem();
        problem.setDomain("R(-32.768:32.768)^30");
        problem.setFunction(new Ackley());

        MeasuredStoppingCondition condition = new MeasuredStoppingCondition(new Iterations(), new Maximum(), 100);

        ABC abc = new ABC();
        abc.addStoppingCondition(condition);
        abc.setOptimisationProblem(problem);
        abc.setWorkerBeePercentage(ConstantControlParameter.of(0.7));

        abc.performInitialisation();
        assertEquals(abc.getTopology().length(), 100);
        assertEquals(abc.getWorkerBees().length(), 70);
        assertEquals(abc.getOnlookerBees().length(), 30);
        HashMap<Type, Type> map = new HashMap<Type, Type>();

        for (HoneyBee bee : abc.getTopology()) {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }
        for (HoneyBee bee : abc.getWorkerBees()) {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }
        for (HoneyBee bee : abc.getOnlookerBees()) {
            map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
        }

        assertEquals(100, map.size());
    }
}
