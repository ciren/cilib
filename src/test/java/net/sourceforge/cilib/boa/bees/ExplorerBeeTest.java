package net.sourceforge.cilib.boa.bees;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.boa.ABC;
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

public class ExplorerBeeTest {
	private ABC abc;
	@Before
	public void setUp() throws Exception {
		FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
		ContinuousFunction func = new Rastrigin();
		func.setDomain("R(-5.0, 5.0)^5");
		problem.setFunction(func);

		StoppingCondition condition = new MaximumIterations(2);

		abc = new ABC();	
		ClonedPopulationInitialisationStrategy initStrategy = new ClonedPopulationInitialisationStrategy();
		initStrategy.setEntityNumber(10);
		WorkerBee bee = new WorkerBee();
		initStrategy.setPrototypeEntity(bee);
		abc.setForageLimit(new ConstantControlParameter(-1));
		abc.setInitialisationStrategy(initStrategy);
		abc.setWorkerBeePercentage(new ConstantControlParameter(0.5));
		abc.addStoppingCondition(condition);
		abc.setOptimisationProblem(problem);
		abc.initialise();
	}

	@Test
	public void testGetNewPosition() {
		//get up a position with bounds
		Vector currentPosition;

		//update position with explorer bee since forage threshold is -1
		ArrayList<Vector> oldPositions = new ArrayList<Vector>();
		for (int k = 0; k < abc.getWorkerTopology().size(); k++) {
			oldPositions.add((Vector)abc.getWorkerTopology().get(k).getPosition().getClone());
		}
		abc.performIteration();

		boolean explorerUpdateOccured = false;
		//assertions
		for (int k = 0; k < abc.getWorkerTopology().size(); k++) {
			boolean allDimensionsChanged = true;
			currentPosition = (Vector)abc.getWorkerTopology().get(k).getCandidateSolution();
			assertEquals(5,currentPosition.size());
			for (int i = 0; i < currentPosition.size(); i++) {
				assertTrue(((Real)currentPosition.get(i)).getReal() != Double.NaN);
				assertTrue(!Double.isInfinite(((Real)currentPosition.get(i)).getReal()));
				allDimensionsChanged = allDimensionsChanged & (Double.compare(((Real)currentPosition.get(i)).getReal(),
						((Real)oldPositions.get(k).get(i)).getReal())!=0);
				assertTrue(((Real)currentPosition.get(i)).getReal() <= 5.0);
				assertTrue(((Real)currentPosition.get(i)).getReal() >= -5.0);
			}
			explorerUpdateOccured = explorerUpdateOccured | allDimensionsChanged;
		}
		assertTrue(explorerUpdateOccured);
	}

}
