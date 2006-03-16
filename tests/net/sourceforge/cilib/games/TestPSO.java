/*
 * TestPSO.java
 * 
 * Created on Sep 6, 2004
 *
 * Copyright (C) 2004 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.games;

import net.sourceforge.cilib.ciclops.Simulation;
import net.sourceforge.cilib.functions.continuous.Spherical;
import net.sourceforge.cilib.measurement.single.FitnessEvaluations;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;

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
			
			//StandardVelocityUpdate vu = new StandardVelocityUpdate();
			
			PSO algo = new PSO();
			algo.addStoppingCondition(new MaximumIterations(10));
			algo.setParticles(10);
			//algo.setVelocityUpdate(vu);
			
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
