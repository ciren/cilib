package net.sourceforge.cilib.container.visitor;

import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.entity.visitor.RadiusVisitor;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.functions.continuous.Spherical;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.StandardParticle;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;

import org.junit.Test;

public class TopologyVisitorTest {

	/**
	 * Initialise a dummy algorithm and then test if the visitor knows
	 * which algorithm it used for it's evaluation.
	 * 
	 * @TODO: This setup is clumsy.... better manner to do this?
	 */
	@Test
	public void currentAlgorithmUsed() {
		PSO pso = new PSO();
		PopulationInitialisationStrategy strategy = new ClonedPopulationInitialisationStrategy();
		strategy.setEntityNumber(2);
		strategy.setEntityType(new StandardParticle());
		pso.setInitialisationStrategy(strategy);
		pso.addStoppingCondition(new MaximumIterations(1000));
		
		FunctionMinimisationProblem problem = new FunctionMinimisationProblem();
		problem.setFunction(new Spherical());
		pso.setOptimisationProblem(problem);
		
		pso.initialise();
		TopologyVisitor visitor = new RadiusVisitor();
		
		pso.accept(visitor);
		
		assertTrue(pso == visitor.getCurrentAlgorithm());
	}

}
