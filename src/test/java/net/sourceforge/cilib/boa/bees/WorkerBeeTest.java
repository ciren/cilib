package net.sourceforge.cilib.boa.bees;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.bee.WorkerBee;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.Rastrigin;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;

import org.junit.Test;

public class WorkerBeeTest {

	@Test
	public void testSetForageLimit() {
		FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
		ContinuousFunction func = new Rastrigin();
		func.setDomain("R(-10.048, 10.048)^100");
		problem.setFunction(func);
		
		ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
		initStrategy.setEntityNumber(100);
		WorkerBee clone = new WorkerBee();
		clone.setForageLimit(new ConstantControlParameter(680));
		initStrategy.setEntityType(clone);
		Topology<WorkerBee> population = new GBestTopology<WorkerBee>();
		initStrategy.initialise(population, problem);
		
		for (WorkerBee bee : population) {
			assertEquals(bee.getForageLimit().getParameter(), 680.0, 0.0001);
		}
	}

}
