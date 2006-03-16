package net.sourceforge.cilib.pso.niching;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

public class FitnessDeviationCreationStrategy<E extends PopulationBasedAlgorithm> implements SwarmCreationStrategy<E> {

	/*
	@SuppressWarnings("unchecked")
	public Collection<E> create(E mainSwarm, Collection<? extends E> subSwarms) {
		int iterationNumber = mainSwarm.getIterations();
		int fitnessCheckInterval = 3;
		
		List<Double> [] mainSwarmParticleFitnesses = new ArrayList[fitnessCheckInterval];
		
		int listIndex = iterationNumber % fitnessCheckInterval;
		
		Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
			
		while(mainSwarmIterator.hasNext()) {
			Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
				
			mainSwarmParticleFitnesses[listIndex].add(mainSwarmParticle.getFitness().getValue());
		}
		
		if((iterationNumber % fitnessCheckInterval) == 0) {
			//Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
			
			//while(mainSwarmIterator.hasNext()) {
				
			//}
		}
		
		else {
			// do nothing (for the time being)
		}
		
		return null;
	}
	*/
	
//	 TODO: Check generics
	public void create(E mainSwarm, Collection<PSO> subSwarms,List<Double> [] mainSwarmParticleFitnesses)
	{
		int mainSwarmIteration = mainSwarm.getIterations();
		
		if( mainSwarmIteration < 3 )
		{
			// populate fitnesses
			Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
	        
	        while(mainSwarmIterator.hasNext())
	        {
	            Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
	            mainSwarmParticleFitnesses[mainSwarmIteration].add(mainSwarmParticle.getFitness().getValue());
	        }
		}
		else // = 3
		{
			mainSwarmIteration = 0;
			for( int i = 0; i < 3; i++ )
				mainSwarmParticleFitnesses[i].clear();
				
			// perform creation of subwarms
						
			for( int j = 0; j < mainSwarm.getTopology().size(); j++ )
			{
				double total = 0.0d;
				for( int i = 0; i < 3; i++ )
					total += mainSwarmParticleFitnesses[i].get(j);
				
				double ave = total / 3.0d;
								
				double stdDev = 0.0d;
				for( int x = 0; x < 3; x++ )
					stdDev += Math.pow(mainSwarmParticleFitnesses[x].get(j) - ave, 2.0d) / 3.0d;
				
				//	 check if last iteration value is correct
				if( mainSwarmParticleFitnesses[2].get(j) < stdDev )
				{
					// find nearest neighbor
					Particle p = (Particle)mainSwarm.getTopology().get(j);
					double minDistance = Double.MAX_VALUE;
					Particle minDistanceParticle = null;
					
					Iterator mainSwarmIterator = mainSwarm.getTopology().iterator();
			        while(mainSwarmIterator.hasNext())
			        {
			            Particle mainSwarmParticle = (Particle)mainSwarmIterator.next();
			            if( p.getId() != mainSwarmParticle.getId() )
			            {
			            	DistanceMeasure distanceMeasure = new EuclideanDistanceMeasure();
			                double distance = distanceMeasure.distance((Vector)p.getPosition(), (Vector)mainSwarmParticle.getPosition());
			                
			                if( distance <= minDistance )
			                {
			                	minDistance = distance;
			                	minDistanceParticle = mainSwarmParticle;
			                }
			            }
			        }
			        
			        // create new subswarm
			        PSO newSubSwarm = new PSO();
			        newSubSwarm.getTopology().add(p);
			        newSubSwarm.getTopology().add(minDistanceParticle);
			        newSubSwarm.addStoppingCondition(mainSwarm.getStoppingConditions().elementAt(0));
			        newSubSwarm.setOptimisationProblem(mainSwarm.getOptimisationProblem());
			        newSubSwarm.initialise();
			        
			        subSwarms.add(newSubSwarm);
				}
			}
				
			
		}
	}

}
