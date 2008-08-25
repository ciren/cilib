package net.sourceforge.cilib.boa.positionupdatestrategies;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.boa.bees.HoneyBee;
import net.sourceforge.cilib.boa.bees.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.Rastrigin;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;

public class VisualPositionUpdateStategyTest {
	private ABC abc;

	@Before
	public void setUp() throws Exception {
		FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
		ContinuousFunction func = new Rastrigin();
		func.setDomain("R(-5.0, 5.0)^10");
		problem.setFunction(func);

		StoppingCondition condition = new MaximumIterations(2);

		abc = new ABC();	
		ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
		initStrategy.setEntityNumber(10);
		WorkerBee bee = new WorkerBee();
		initStrategy.setEntityType(bee);
		abc.setInitialisationStrategy(initStrategy);
		abc.setWorkerBeePercentage(new ConstantControlParameter(0.5));
		abc.setForageLimit(new ConstantControlParameter(-1));
		abc.addStoppingCondition(condition);
		abc.setOptimisationProblem(problem);
		abc.initialise();
	}

	@Test
	public void testUpdatePosition() {
		HoneyBee bee = abc.getWorkerTopology().get(0);
		abc.performIteration();
		Vector currentPosition = (Vector)bee.getPosition();
		assertEquals(10,currentPosition.size());
		for (int i = 0; i < currentPosition.size(); i++) {
			assertTrue(((Real)currentPosition.get(i)).getReal() != Double.NaN);
			assertTrue(!Double.isInfinite(((Real)currentPosition.get(i)).getReal()));
			assertTrue(((Real)currentPosition.get(i)).getReal() <= 5.0);
			assertTrue(((Real)currentPosition.get(i)).getReal() >= -5.0);
		}
	}

}
