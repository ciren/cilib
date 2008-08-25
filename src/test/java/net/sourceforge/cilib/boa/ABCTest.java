package net.sourceforge.cilib.boa;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.boa.bees.HoneyBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.continuous.unconstrained.Ackley;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Type;

import org.junit.Test;

public class ABCTest {
	
	@Test
	public void testPerformInitialisation() {
		FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
		problem.setFunction(new Ackley());
		
		StoppingCondition condition = new MaximumIterations(1000);
		
		ABC abc = new ABC();
		abc.addStoppingCondition(condition);
		abc.setOptimisationProblem(problem);
		abc.setWorkerBeePercentage(new ConstantControlParameter(0.7));
		
		abc.initialise();
		assertEquals(abc.getTopology().size(), 100);
		assertEquals(abc.getWorkerTopology().size(), 70);
		assertEquals(abc.getOnlookerTopology().size(), 30);
		HashMap<Type, Type> map = new HashMap<Type, Type>();
		
		for (HoneyBee bee : abc.getTopology())
		{
			map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
		}
		for (HoneyBee bee : abc.getWorkerTopology())
		{
			map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
		}
		for (HoneyBee bee : abc.getOnlookerTopology())
		{
			map.put(bee.getCandidateSolution(), bee.getCandidateSolution());
		}
		
		assertEquals(map.size(), 100);
	}

}
