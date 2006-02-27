/*
 * Created on 2004/09/06
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.cilib.GamesTest;

import net.sourceforge.cilib.CiClops.Simulation;
import net.sourceforge.cilib.Functions.Spherical;
import net.sourceforge.cilib.Measurement.FitnessEvaluations;
import net.sourceforge.cilib.PSO.PSO;
import net.sourceforge.cilib.PSO.StandardVelocityUpdate;
import net.sourceforge.cilib.Problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.StoppingCondition.MaximumIterations;

/**
 * @author Vangos
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestPSO {

		public static void main(String[] args)
		{
			Spherical func = new Spherical();
	        func.setDomain("R(-100, 100)^3");
	        
			FunctionMinimisationProblem prob = new FunctionMinimisationProblem();
			prob.setFunction(func);
			
			StandardVelocityUpdate vu = new StandardVelocityUpdate();
			
			PSO algo = new PSO();
			algo.addStoppingCondition(new MaximumIterations(10));
			algo.setParticles(10);
			algo.setVelocityUpdate(vu);
			
			algo.setOptimisationProblem(prob);
	
			FitnessEvaluations measure = new FitnessEvaluations();
			
			Simulation sim = new Simulation();
			sim.setAlgorithm(algo);
			sim.setResolution(1);
			sim.addMeasurement(measure);
			sim.initialise();

			sim.run();
		}
}
